package cn.itcast.mobliesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.itcast.mobliesafe.adapter.ContactAdapter;
import cn.itcast.mobliesafe.entity.ContactInfo;
import cn.itcast.mobliesafe.utils.ContactInfoParser;

public class ContactSelectActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private TextView mT_title;
    private ImageView mLeftImgv;
    private RelativeLayout mRl_titlebar;
    private ListView mListView;
    private ContactAdapter adapter;
    private List<ContactInfo> systemContacts;
    private Handler mHandler  = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 10:
                    if(systemContacts != null){
                        adapter = new ContactAdapter(systemContacts,getApplicationContext());
                        mListView.setAdapter(adapter);
                    }
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contact_select);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mT_title=((TextView) findViewById(R.id.tv_title));
        mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mRl_titlebar=(RelativeLayout) findViewById(R.id.rl_titlebar);
        mListView = (ListView) findViewById(R.id.lv_contact);
    }

    private void initData(){
        mT_title.setText("选择联系人");
        mLeftImgv.setImageResource(R.drawable.back);
        mRl_titlebar.setBackgroundColor(getResources().getColor(R.color.purple));//设置导航栏颜色

        new Thread(){
            public void run() {
                systemContacts = ContactInfoParser.getSystemContact(getApplicationContext());//手机联系人
                systemContacts.addAll(ContactInfoParser.getSimContacts(getApplicationContext()));//SIM卡联系人
                mHandler.sendEmptyMessage(10);
            };
        }.start();
    }
    private void initListener(){
        mLeftImgv.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ContactInfo item = (ContactInfo) adapter.getItem(position);
        Intent intent  = new Intent();
        intent.putExtra("phone", item.phone);
        setResult(0, intent);
        finish();
    }
}
