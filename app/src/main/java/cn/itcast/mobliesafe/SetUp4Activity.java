package cn.itcast.mobliesafe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SetUp4Activity extends BaseSetUpActivity{

    private RadioButton mRb_four;
    private TextView mStatusTV;
    private ToggleButton mToggleButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initView();
        initData();
        initListener();
    }

    private void initView(){
        mRb_four=findViewById(R.id.rb_four);
        mStatusTV=findViewById(R.id.tv_setup4_status);
        mToggleButton=findViewById(R.id.togglebtn_securityfunction);
    }

    private void initData(){
        mRb_four.setChecked(true);
        boolean protecting = sp.getBoolean("protectting",true);
        if (protecting){
            mStatusTV.setText("防盗保护已经开启");
            mToggleButton.setChecked(true);
        }else{
            mStatusTV.setText("防盗保护没有开启");
            mToggleButton.setChecked(false);
        }
    }

    private void initListener(){
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mStatusTV.setText("防盗保护已经开启");
                }else{
                    mStatusTV.setText("防盗保护没有开启");
                }
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("protecting", isChecked);
                editor.commit();
            }
        });

    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(SetUp3Activity.class);
    }

    @Override
    public void showNext() {
        //跳转至 防盗保护页面
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isSetUp", true);
        editor.commit();
        startActivityAndFinishSelf(LostFindActivity.class);

    }
}
