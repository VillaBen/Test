package cn.itcast.mobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

public abstract class BaseSetUpActivity extends Activity {

    public SharedPreferences sp;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
        initData();
        initListener();
    }

    private void initView() {

    }

    private void initData() {
        sp=getSharedPreferences("config",MODE_PRIVATE);
        // 2.初始化手势识别器。
        mGestureDetector=new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            // 用户按下触摸屏、快速移动后松开
            // e1 代表手指第一次触摸屏幕的事件
            // e2 代表手指离开屏幕一瞬间的事件
            // velocityX  X轴上的移动速度，像素/秒
            // velocityY Y轴上的移动速度，像素/秒

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(velocityX)<200){
                    Toast.makeText(getApplicationContext(),"无效动作，移动太慢",Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (e2.getRawX()-e1.getRawX()>200){
                    // 从左向右滑动屏幕，显示上一个界面
                    showPre();
                    overridePendingTransition(R.anim.pre_in,R.anim.pre_out);
                    return true;
                }
                if (e1.getRawX()-e2.getRawX()>200){
                    // 从右向左滑动屏幕，显示下一个界面
                    showNext();
                    overridePendingTransition(R.anim.next_in,R.anim.next_out);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    private void initListener() {

    }

    public abstract void showPre();

    public abstract void showNext();

    // 3.用手势识别器去识别事件

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 分析手势事件
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 开启新的activity并且关闭自己
     *
     * @param cls
     *            新的activity的字节码
     */
    public void  startActivityAndFinishSelf(Class<?> cls) {
        Intent intent=new Intent(this,cls);
        startActivity(intent);
        finish();
    }
}
