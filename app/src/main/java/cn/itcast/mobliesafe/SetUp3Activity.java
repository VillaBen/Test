package cn.itcast.mobliesafe;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SetUp3Activity extends BaseSetUpActivity implements View.OnClickListener {

    private RadioButton mRb_third;
    private EditText mInputPhone;
    private Button mBtn_addcontact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initView();
        initData();
        initListener();
    }
    /**
     * 初始化控件
     */
    private void initView() {
        mRb_third=((RadioButton)findViewById(R.id.rb_third));
        mBtn_addcontact=(Button)findViewById(R.id.btn_addcontact);
        mInputPhone = (EditText) findViewById(R.id.et_inputphone);
    }
    private void initData(){
        mRb_third.setChecked(true);
        String  safephone= sp.getString("safephone", null);
        if(!TextUtils.isEmpty(safephone)){
            mInputPhone.setText(safephone);
        }
    }
    private void initListener(){
        mBtn_addcontact.setOnClickListener(this);
    }
    @Override
    public void showNext() {
        //判断文本输入框中是否有电话号码
        String safePhone = mInputPhone.getText().toString().trim();
        if(TextUtils.isEmpty(safePhone)){
            Toast.makeText(this, "请输入安全号码", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("safephone", safePhone);
        edit.commit();
        startActivityAndFinishSelf(SetUp4Activity.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(SetUp2Activity.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addcontact:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
                } else {
                    startActivityForResult(new Intent(this,ContactSelectActivity.class), 0);
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
                    startActivityForResult(new Intent(getApplicationContext(),ContactSelectActivity.class), 0);
                } else {
                    Toast.makeText(this, "不允许授权", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            String phone = data.getStringExtra("phone");
            mInputPhone.setText(phone);
        }
    }
}

