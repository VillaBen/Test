package cn.itcast.mobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LostFindActivity extends Activity implements View.OnClickListener {

    private TextView mSafePhoneTV,mTitleTV;
    private ImageView mLeftImgv;
    private RelativeLayout mRl_inter_setup_wizard;
    private SharedPreferences msharedPreferences;
    private ToggleButton mToggleButton;
    private TextView mProtectStatusTV;
    private RelativeLayout mRl_titlebar;


    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lostfind);
        initView();
        initData();
    }

    /**初始化控件*/
    private void initView() {
        mTitleTV = (TextView) findViewById(R.id.tv_title);
        mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mRl_titlebar=(RelativeLayout) findViewById(R.id.rl_titlebar);
        mSafePhoneTV = (TextView) findViewById(R.id.tv_safephone);
        mToggleButton = (ToggleButton) findViewById(R.id.togglebtn_lostfind);
        mRl_inter_setup_wizard = (RelativeLayout) findViewById(R.id.rl_inter_setup_wizard);
        mProtectStatusTV = (TextView) findViewById(R.id.tv_lostfind_protectstauts);
    }
    private void initData(){
        msharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        if(!isSetUp()){
            //如果没有进入过设置向导，则进入
            startSetUp1Activity();
        }
        mTitleTV.setText("手机防盗");
        mLeftImgv.setImageResource(R.drawable.back);
        mSafePhoneTV.setText(msharedPreferences.getString("safephone", ""));
        //查询手机防盗是否开启，默认为开启
        boolean protecting = msharedPreferences.getBoolean("protecting", true);
        if(protecting){
            mProtectStatusTV.setText("防盗保护已经开启");
            mToggleButton.setChecked(true);
        }else{
            mProtectStatusTV.setText("防盗保护没有开启");
            mToggleButton.setChecked(false);
        }
        mRl_titlebar.setBackgroundColor(getResources().getColor(R.color.purple));

    }

    private  void initListener(){
        mLeftImgv.setOnClickListener(this);
        mRl_inter_setup_wizard.setOnClickListener(this);
        mToggleButton.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mProtectStatusTV.setText("防盗保护已经开启");
                }else{
                    mProtectStatusTV.setText("防盗保护没有开启");
                }
                SharedPreferences.Editor editor = msharedPreferences.edit();
                editor.putBoolean("protecting", isChecked);
                editor.commit();

            }
        });
    }
    private boolean isSetUp() {
        return msharedPreferences.getBoolean("isSetUp", false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //case R.id.tv_lostfind_protectstauts:
            case R.id.rl_inter_setup_wizard:
                //重新进入设置向导
                startSetUp1Activity();
                break;
            case R.id.imgv_leftbtn:
                //返回
                finish();
                break;
        }
    }

    private void startSetUp1Activity() {
        Intent intent =new Intent(getApplicationContext(),SetUp1Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
