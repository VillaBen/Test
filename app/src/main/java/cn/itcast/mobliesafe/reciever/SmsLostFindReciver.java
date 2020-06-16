package cn.itcast.mobliesafe.reciever;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import cn.itcast.mobliesafe.R;
import cn.itcast.mobliesafe.service.GPSLocationService;

public class SmsLostFindReciver extends BroadcastReceiver {

    private static final String TAG = SmsLostFindReciver.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private ComponentName componentName;
    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences("config",
                Activity.MODE_PRIVATE);
        boolean protecting = sharedPreferences.getBoolean("protecting", true);
        if (protecting) {
            // 防盗保护开启
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            // 获取超级管理员
            DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            Log.d("<<<超级管理员>>>",dpm.toString());
            for (Object obj : objs) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String sender = smsMessage.getOriginatingAddress();
                //Log.d("<<<短信发送者号码>>>",sender);
                if(sender.startsWith("+86")){
                    sender = sender.substring(3, sender.length());
                }
                sender = sender.substring(1);
                Log.d("<<<短信发送者号码>>>",sender);
                String body = smsMessage.getMessageBody();
                Log.d("<<<短信内容>>>",body);
                String safephone = sharedPreferences.getString("safephone",null);
                Log.d("<<<安全号码>>>",safephone);
                //如果该短信是安全号码发送的
                if (!TextUtils.isEmpty(safephone) & sender.equals(safephone)) {
                    Log.d("<<<短信发送者号码>>>",sender);
                    Log.d("<<<安全号码>>>",safephone);
                    if ("#*location*#".equals(body)) {
                        Log.i(TAG, "返回位置信息.");
                        // 获取位置 放在服务里面去实现。
                        Intent service = new Intent(context,
                                GPSLocationService.class);
                        context.startService(service);
                        abortBroadcast();
                    } else if ("#*alarm*#".equals(body)) {
                        Log.i(TAG, "播放报警音乐.");
                        MediaPlayer player = MediaPlayer.create(context,
                                R.raw.ylzs);
                        player.setVolume(1.0f, 1.0f);
                        player.start();
                        abortBroadcast();
                    } else if ("#*lockScreen*#".equals(body)) {
                        //远程锁屏，下次完成……

                    }else if ("#*wipedata*#".equals(body)) {
                        //远程清除数据，下次完成

                    }
                }
            }
        }
    }
}
