package cn.itcast.mobliesafe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.util.List;

import cn.itcast.mobliesafe.BuildConfig;

public class VersionUtils
{

    /**
     * 获取版本号
     *
     * @param context
     * @return 返回版本号
     */
    public static String getVersion(Context context) {
        // PackageManager 可以获取清单文件中的所有信息
        PackageManager manager = context.getPackageManager();
        try {
            // 获取到一个应用程序的信息
            // getPackageName() 获取到当前程序的包名
            PackageInfo packageInfo = manager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 安装新版本
     * @param activity
     */
    public static void installApk(Activity activity) {
        Intent intent = new Intent("android.intent.action.VIEW");
        // 添加默认分类
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String downPath=Environment.getExternalStorageDirectory()+"/mobilesafe2.0.apk";
        Log.d("<<<安装APK路径>>>",downPath);
        Uri data=null;
        File file=new File(downPath);
        if (Build.VERSION.SDK_INT < 24) {
            data = Uri.fromFile(file);
        } else {
            data = FileProvider.getUriForFile(activity, "cn.itcast.mobliesafe.fileprovider", new File(downPath));
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(data,    "application/vnd.android.package-archive");
        activity.startActivityForResult(intent, 0);
//        // 设置数据和类型 在文件中
////        intent.setDataAndType(
////                Uri.fromFile(new File("/mnt/sdcard/mobilesafe2.0.apk")),
////                "application/vnd.android.package-archive");

//        Log.d("<<<安装APK路径>>>",downPath);
//        Uri data;
//        //data = FileProvider.getUriForFile(activity, "cn.itcast.mobliesafe.fileprovider", new File(downPath));
////        //设置 intent 目标应用类型和传递的数据，这句要放在查询之前
////        intent.setDataAndType(data,
////                "application/vnd.android.package-archive");
////
////        //查询所有符合 intent 跳转目标应用类型的应用
////        List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
////        //然后全部授权
////        for (ResolveInfo resolveInfo : resInfoList) {
////            String packageName = resolveInfo.activityInfo.packageName;
////            activity.grantUriPermission(packageName, data, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
////        }
//        if (Build.VERSION.SDK_INT < 24) {
//            data = Uri.fromFile(new File(downPath));
//        } else {
//            data = FileProvider.getUriForFile(activity, "cn.itcast.mobliesafe.fileprovider", new File(downPath));
//            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        }
        Log.d("真实路径",data+"");
//        intent.setDataAndType(data,    "application/vnd.android.package-archive");
//        // 如果开启的activity 退出的时候 会回调当前activity的onActivityResult
//        activity.startActivityForResult(intent, 0);
//

    }

}
