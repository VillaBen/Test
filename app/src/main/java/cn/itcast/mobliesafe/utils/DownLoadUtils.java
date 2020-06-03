package cn.itcast.mobliesafe.utils;

import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

public class DownLoadUtils {

    /***
     * 下载APK的方法 对HttpUtils的download再次封装，以灵活控制下载成功后后续怎么处理
     * 例如VersionUpdateUtils中的downloadNewApk方法用到它
     * @param url
     * @param targerFile
     * @param myCallBack
     */
    public void downapk(String url,String targerFile, final MyCallBack myCallBack){
        //创建HttpUtils对象
        HttpUtils http = new HttpUtils(6000);
        http.configResponseTextCharset("gbk");
        //调用HttpUtils下载的方法下载指定文件
        http.download(url, targerFile, new RequestCallBack<File>() {

            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                Log.d("<<<下载成功>>>","OKOKOK");
                myCallBack.onSuccess(arg0);
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                myCallBack.onFailure(arg0, arg1);

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                Log.d("<<<下载中……>>>","…………");
                myCallBack.onLoadding(total, current, isUploading);
            }
        });
    }

    /**
     *接口，用于监听下载状态的接口
     * */
    interface MyCallBack{
        /**下载成功时调用*/
        void onSuccess(ResponseInfo<File> arg0);
        /**下载失败时调用*/
        void onFailure(HttpException arg0, String arg1);
        /**下载中调用*/
        void onLoadding(long total, long current, boolean isUploading);
    }
}
