package cn.itcast.mobliesafe;


import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SetUp2Activity extends BaseSetUpActivity implements View.OnClickListener {
    private RadioButton mRb_second;
    private TelephonyManager mTelephonyManager;
    private Button mBindSIMBtn;
    private TextView tvsim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        // 设置第二个小圆点的颜色
        mRb_second=((RadioButton) findViewById(R.id.rb_second));
        mBindSIMBtn = (Button) findViewById(R.id.btn_bind_sim);
        tvsim=(TextView) this.findViewById(R.id.tvsim);
    }
    private void initData(){
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        mRb_second.setChecked(true);
        if (isBind()) {
            mBindSIMBtn.setEnabled(false);
        } else {
            mBindSIMBtn.setEnabled(true);
        }
    }
    private void initListener(){
        mBindSIMBtn.setOnClickListener(this);
    }
    private boolean isBind() {
        String simString = sp.getString("sim", null);
        if (TextUtils.isEmpty(simString)) {
            return false;
        }
        tvsim.setText(simString);
        return true;
    }

    @Override
    public void showNext() {
        if (!isBind()) {
            Toast.makeText(this, "您还没有帮定SIM卡！", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivityAndFinishSelf(SetUp3Activity.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(SetUp1Activity.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bind_sim:
                // 绑定SIM卡
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                } else {
                    bindSIM();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //处理权限申请
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bindSIM();
                } else {
                    Toast.makeText(this, "不允许授权", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    /**
     * 绑定sim卡
     */
    private void bindSIM() {
        if (!isBind()) {
            String simSerialNumber = mTelephonyManager.getSimSerialNumber();
            if (!simSerialNumber.isEmpty()) {
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("sim", simSerialNumber);
                edit.commit();
                Toast.makeText(this, "SIM卡绑定成功！", Toast.LENGTH_SHORT).show();
                mBindSIMBtn.setEnabled(false);
                tvsim.setText(simSerialNumber);
            }
            else{
                Toast.makeText(this, "找不到SIM卡！", Toast.LENGTH_SHORT).show();
            }
        } else {
            // 已经绑定，提醒用户
            Toast.makeText(this, "SIM卡已经绑定！", Toast.LENGTH_SHORT).show();
            mBindSIMBtn.setEnabled(false);
        }
    }
}

