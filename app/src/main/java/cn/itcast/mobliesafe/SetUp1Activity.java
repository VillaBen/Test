package cn.itcast.mobliesafe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RadioButton;
import android.widget.Toast;

public class SetUp1Activity extends BaseSetUpActivity {

    private RadioButton mRb_first;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        initView();
        initData();
        initListener();
    }

    private void initView(){
        // 设置第一个小圆点的颜色
        mRb_first=findViewById(R.id.rb_first);
    }

    private void initData(){
        mRb_first.setChecked(true);
    }

    private void initListener(){

    }

    @Override
    public void showPre() {
        Toast.makeText(this,"当前页面已经是第一页",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNext() {
        startActivityAndFinishSelf(SetUp2Activity.class);
    }
}
