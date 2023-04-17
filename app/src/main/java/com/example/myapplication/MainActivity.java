package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Button install = findViewById(R.id.install);
        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isGranted = context.getPackageManager().canRequestPackageInstalls();
                if (isGranted) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri apkURI = FileProvider.getUriForFile(context, "com.example.myapplication.provider", new File("/sdcard/test.apk"));
                    List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                    for (ResolveInfo resolveInfo : resInfoList) {
//                        String packageName = resolveInfo.activityInfo.packageName;
//                        context.grantUriPermission(packageName, apkURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    }
                    intent.setDataAndType(apkURI, "application/vnd.android.package-archive");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    context.startActivity(intent);
                } else {
                    new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setTitle("安装应用需要打开未知来源权限，请去设置中开启权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int w) {
                                //https://blog.csdn.net/changmu175/article/details/78906829
                                Uri packageURI = Uri.parse("package:" + context.getPackageName());
                                //注意这个是8.0新API
                                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                                MainActivity.this.startActivityForResult(intent, 1);
                            }
                        })
                        .show();
                }
            }
        });
    }
}