package cn.itcast.mobliesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.itcast.mobliesafe.adapter.HomeAdapter;
import cn.itcast.mobliesafe.dialog.InterPasswordDialog;
import cn.itcast.mobliesafe.dialog.SetUpPasswordDialog;
import cn.itcast.mobliesafe.utils.MD5Utils;
import cn.itcast.mobliesafe.utils.VersionUtils;

public class HomeActivity extends Activity {

    private GridView gv_home;

    /** 存储手机防盗密码的sp */
    private SharedPreferences msharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initData();
        initListener();


    }

    private void initView(){
        gv_home = (GridView) this.findViewById(R.id.gv_home);
    }
    private void initData(){
        gv_home.setAdapter(new HomeAdapter(HomeActivity.this));
        msharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
    }
    private void initListener(){
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                switch (arg2) {
                    case 0: // 点击手机防盗
                        if (isSetUpPassword()) {
                            // 弹出输入密码对话框
                            showInterPswdDialog();
                        } else {
                            // 弹出设置密码对话框
                            showSetUpPswdDialog();
                        }
                        break;
                    case 1: // 点击通讯卫士
                        startActivity(SecurityPhoneActivity.class);
                        break;
                    case 2: // 软件管家
                        startActivity(AppManagerActivity.class);
                        break;
                    case 4:// 缓存清理
                        //startActivity(CacheClearListActivity.class);
                        break;
                    case 9:
                        String verString = VersionUtils
                                .getVersion(getApplicationContext());
                        showCloseVerDialog(verString, HomeActivity.this);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    /***
     * 弹出关于对话框
     */
    private void showCloseVerDialog(String version, Context context) {
        // 创建dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("关于");// 设置标题
        builder.setMessage("版本号：" + version + "，版权所有：YJC。欢迎使用！");// 根据服务器返回描述,设置升级描述信息
        builder.setCancelable(false);// 设置不能点击手机返回按钮隐藏对话框
        builder.setIcon(R.drawable.ic_launcher);// 设置对话框图标
        // 设置立即升级按钮点击事件
        builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // 设置暂不升级按钮点击事件
        builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // 对话框必须调用show方法 否则不显示
        builder.show();
    }

    /***
     * 弹出设置密码对话框
     */
    private void showSetUpPswdDialog() {
        final SetUpPasswordDialog setUpPasswordDialog = new SetUpPasswordDialog(
                HomeActivity.this);
        setUpPasswordDialog
                .setCallBack(new SetUpPasswordDialog.MyCallBack() {

                    @Override
                    public void ok() {
                        String firstPwsd = setUpPasswordDialog.mFirstPWDET
                                .getText().toString().trim();
                        String affirmPwsd = setUpPasswordDialog.mAffirmET
                                .getText().toString().trim();
                        if (!TextUtils.isEmpty(firstPwsd)
                                && !TextUtils.isEmpty(affirmPwsd)) {
                            if (firstPwsd.equals(affirmPwsd)) {
                                // 两次密码一致,存储密码
                                savePswd(affirmPwsd);
                                setUpPasswordDialog.dismiss();
                                // 显示输入密码对话框
                                showInterPswdDialog();
                            } else {
                                Toast.makeText(HomeActivity.this, "两次密码不一致！", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, "密码不能为空！", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void cancle() {
                        setUpPasswordDialog.dismiss();
                    }
                });
        setUpPasswordDialog.setCancelable(true);
        setUpPasswordDialog.show();
    }

    /**
     * 弹出输入密码对话框
     */
    private void showInterPswdDialog() {
        final String password = getPassword();
        final InterPasswordDialog mInPswdDialog = new InterPasswordDialog(
                HomeActivity.this);
        mInPswdDialog.setCallBack(new InterPasswordDialog.MyCallBack() {
            @Override
            public void confirm() {
                if (TextUtils.isEmpty(mInPswdDialog.getPassword())) {
                    Toast.makeText(HomeActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                } else if (password.equals(MD5Utils.encode(mInPswdDialog
                        .getPassword()))) {
                    // 进入防盗主界面
                    mInPswdDialog.dismiss();
                    startActivity(LostFindActivity.class);

                } else {
                    // 对话框消失，弹出土司
                    mInPswdDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "密码有误，请重新输入！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancle() {
                mInPswdDialog.dismiss();
            }
        });
        mInPswdDialog.setCancelable(true);
        // 让对话框显示
        mInPswdDialog.show();
    }

    /***
     * 保存密码
     *
     * @param affirmPwsd
     */
    private void savePswd(String affirmPwsd) {
        SharedPreferences.Editor edit = msharedPreferences.edit();
        // 为了防止用户隐私被泄露，因此需要加密密码
        edit.putString("PhoneAntiTheftPWD", MD5Utils.encode(affirmPwsd));
        edit.commit();
    }

    /***
     * 获取密码
     *
     * @return sp存储的密码
     */
    private String getPassword() {
        String password = msharedPreferences.getString("PhoneAntiTheftPWD",
                null);
        if (TextUtils.isEmpty(password)) {
            return "";
        }
        return password;
    }

    /** 判断用户是否设置过手机防盗密码 */
    private boolean isSetUpPassword() {
        String password = msharedPreferences.getString("PhoneAntiTheftPWD",
                null);
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }

    /**
     * 开启新的activity不关闭自己
     *
     * @param cls
     *            新的activity的字节码
     */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(HomeActivity.this, cls);
        startActivity(intent);
    }
}
