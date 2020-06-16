package cn.itcast.mobliesafe;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.mobliesafe.adapter.AppManagerAdapter;
import cn.itcast.mobliesafe.entity.AppInfo;
import cn.itcast.mobliesafe.utils.AppInfoParser;

public class AppManagerActivity extends Activity implements View.OnClickListener {
    private RelativeLayout mRl_titlebar;
    private ImageView mLeftImgv;
    private TextView mTv_title,mPhoneMemoryTV,mAppNumTV;
    private ListView mListView;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos;
    private  List<AppInfo> systemAppInfos;
    private AppManagerAdapter adapter;
    /**接收应用程序卸载成功的广播*/
    private UninstallRececiver receciver;

    private Handler mHandler = new Handler(){

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 10:
                    if(adapter == null){
                        //如果还没实例过适配器
                        adapter = new AppManagerAdapter(userAppInfos, systemAppInfos, AppManagerActivity.this);
                    }
                    mListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case 15:
                    adapter.notifyDataSetChanged();//通知更新ListView的显示
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_app_manager);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        mRl_titlebar=(RelativeLayout) findViewById(R.id.rl_titlebar);
        mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mTv_title=((TextView) findViewById(R.id.tv_title));

        mPhoneMemoryTV = (TextView) findViewById(R.id.tv_phonememory_appmanager);
        mAppNumTV = (TextView) findViewById(R.id.tv_appnumber);
        mListView = (ListView) findViewById(R.id.lv_appmanager);
    }
    private void initData() {
        mLeftImgv.setImageResource(R.drawable.back);
        mTv_title.setText("软件管家");
        mRl_titlebar.setBackgroundColor(getResources().getColor(R.color.bright_yellow));

        getMemoryFromPhone();

        appInfos = new ArrayList<AppInfo>();//存放所有应用程序的集合
        userAppInfos = new ArrayList<AppInfo>();//存放用户应用程序的集合
        systemAppInfos = new ArrayList<AppInfo>();//存放系统应用程序的集合

        getAppinfo();

        //注册广播
        receciver = new UninstallRececiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(receciver, intentFilter);
    }

    private void getAppinfo(){
        //创建子线程，用于读取应用程序信息
        new Thread(){
            public void run() {
                appInfos.clear();
                userAppInfos.clear();
                systemAppInfos.clear();
                appInfos.addAll(AppInfoParser.getAppInfos(AppManagerActivity.this));
                for( AppInfo appInfo : appInfos){
                    //如果是用户App
                    if(appInfo.isUserApp){
                        userAppInfos.add(appInfo);
                    }else{
                        systemAppInfos.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(10);
            };
        }.start();
    }

    private void initListener() {
        mLeftImgv.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                if (adapter != null) {
                    new Thread(){
                        public void run() {
                            AppInfo mappInfo  = (AppInfo) adapter.getItem(position);
                            //记住当前条目的状态
                            boolean flag = mappInfo.isSelected;
                            //先将集合中所有条目的AppInfo变为未选中状态
                            for(AppInfo appInfo :userAppInfos){
                                appInfo.isSelected = false;
                            }
                            for(AppInfo appInfo : systemAppInfos){
                                appInfo.isSelected = false;
                            }
                            if(mappInfo != null){
                                //如果已经选中，则变为未选中
                                if(flag){
                                    mappInfo.isSelected = false;
                                }else{
                                    mappInfo.isSelected = true;
                                }
                                mHandler.sendEmptyMessage(15);
                            }
                        };
                    }.start();
                }
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem >= userAppInfos.size()+1){
                    mAppNumTV.setText("系统程序："+systemAppInfos.size()+"个");
                }else{
                    mAppNumTV.setText("用户程序："+userAppInfos.size()+"个");
                }
            }
        });
    }


    /**读取手机剩余内存*/
    private void getMemoryFromPhone() {
        long avail_sd = Environment.getExternalStorageDirectory().getUsableSpace();
        String str_avail_sd = Formatter.formatFileSize(this, avail_sd);//格式化内存数据
        mPhoneMemoryTV.setText("剩余手机内存：" + str_avail_sd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }

    /***
     * 接收应用程序卸载的广播
     * @author admin
     */
    class UninstallRececiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 收到广播了
            getAppinfo();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receciver);
        receciver = null;
        super.onDestroy();
    }
}

