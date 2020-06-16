package cn.itcast.mobliesafe.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.itcast.mobliesafe.HomeActivity;
import cn.itcast.mobliesafe.R;
import cn.itcast.mobliesafe.entity.VersionEntity;

/** 更新版本工具类 */
public class VersionUpdateUtils {

    /** 本地版本号 */
    private String mVersion;
    private Activity context;
    private ProgressDialog mProgressDialog;
    private VersionEntity versionEntity;

    public VersionUpdateUtils(String Version, Activity activity) {
        mVersion = Version;
        context = activity;
    }

    /**
     * 读取服务器安装包的版本信息
     * @param url--服务器上版本信息文件地址
     */
    public void getDownloadVer(final String url) {
        HttpUtils http = new HttpUtils(6000);
        http.configResponseTextCharset("gbk");//根据读取的文件编码设置以何种编码读
        http.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(context,"网络访问失败。",Toast.LENGTH_LONG).show();
                Log.d("<<<网络失败地址>>",url);
                enterHome();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Log.d("<<version infomation>>", arg0.result);
                String retString="";
                retString=arg0.result;//如果服务端与客户端编码一致，则不用转换编码

                versionEntity = new VersionEntity();
                try {
                    JSONObject jsonObject = new JSONObject(retString);
                    String code;
                    code = jsonObject.getString("code");
                    versionEntity.versioncode = code;
                    String des = jsonObject.getString("des");
                    versionEntity.description = des;
                    String apkurl = jsonObject.getString("apkurl");
                    versionEntity.apkurl = apkurl;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // e.printStackTrace();
                    Toast.makeText(context, "JSON处理异常", Toast.LENGTH_SHORT).show();
                }

                if (mVersion.compareTo(versionEntity.versioncode) < 0) {// 有新版本
                    showUpdateDialog(versionEntity);
                } else {
                    enterHome();
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                // TODO Auto-generated method stub
                super.onLoading(total, current, isUploading);
            }
        });
    }

    /**
     * 弹出更新提示对话框
     *
     * @param versionEntity
     */
    private void showUpdateDialog(final VersionEntity versionEntity) {
        // 创建dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("检查到新版本：" + versionEntity.versioncode);// 设置标题
        builder.setMessage(versionEntity.description);// 根据服务器返回描述,设置升级描述信息
        builder.setCancelable(false);// 设置不能点击手机返回按钮隐藏对话框
        builder.setIcon(R.drawable.ic_launcher);// 设置对话框图标
        // 设置立即升级按钮点击事件
        builder.setPositiveButton("立即升级",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initProgressDialog();
                        downloadNewApk(versionEntity.apkurl);
                    }
                });
        // 设置暂不升级按钮点击事件
        builder.setNegativeButton("暂不升级",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        enterHome();
                    }
                });
        // 对话框必须调用show方法 否则不显示
        builder.show();
    }

    /**
     * 初始化进度条对话框
     */
    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("准备下载...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }

    /***
     * 下载新版本
     */
    protected void downloadNewApk(String apkurl) {
        DownLoadUtils downLoadUtils = new DownLoadUtils();
        String downPath= Environment.getExternalStorageDirectory() + "/mobilesafe2.0.apk";
        Log.d("<<<下载文件路径>>>", downPath);
        //downLoadUtils.downapk(apkurl,"/mnt/sdcard/mobilesafe2.0.apk" , new DownLoadUtils.MyCallBack() {
        downLoadUtils.downapk(apkurl,downPath , new DownLoadUtils.MyCallBack() {
            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                // TODO Auto-generated method stub
                mProgressDialog.dismiss();
                VersionUtils.installApk(context);
            }

            @Override
            public void onLoadding(long total, long current, boolean isUploading) {
                // TODO Auto-generated method stub
                mProgressDialog.setMax((int)total);
                mProgressDialog.setMessage("正在下载...");
                mProgressDialog.setProgress((int) current);
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                mProgressDialog.setMessage("下载失败");
                mProgressDialog.dismiss();
                enterHome();
            }

        });
    }


    private void enterHome() {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
        context.finish();
    }
}
