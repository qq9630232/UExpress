package com.wty.app.uexpress.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.wty.app.uexpress.R;
import com.wty.app.uexpress.data.model.LoginBean;
import com.wty.app.uexpress.util.SPUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static cn.smssdk.SMSSDK.getSupportedCountries;
import static cn.smssdk.SMSSDK.getVerificationCode;
import static cn.smssdk.SMSSDK.submitVerificationCode;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private boolean isChange;
    private boolean tag = true;
    private int i=60;
    private String LOGIN = "login";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    //客户端验证成功，可以进行注册,返回校验的手机和国家代码phone/country
                    Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    LoginBean loginBean = new LoginBean();
                    loginBean.setUsername(phone);
                    loginBean.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Log.e("zxz","添加数据成功，返回objectId为：");
                            }else{
                                Log.e("zxz","创建数据失败：" + e.getMessage());

                            }
                        }
                    });
                    launch();
                    break;
                case 1:
                    //获取验证码成功
                    Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //返回支持发送验证码的国家列表
                    break;
                case 3:
                    Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }


    };
    private void launch() {
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("zhuce_phone",phone);
        startActivity(intent);
        finish();
    }
    private ImageView mBack;
    private BootstrapCircleThumbnail mBcircleBorderChangeExample;
    /**
     * 手机号
     */
    private BootstrapEditText mZhanghao;
    /**
     * 获取验证码
     */
    private Button mBtGetCode;
    /**
     * 输入验证码
     */
    private BootstrapEditText mEtCode;
    private RelativeLayout mGetCodeRl;
    /**
     * 登录
     */
    private BootstrapButton mDenglu;
    private String phone;
    private void changeBtnGetCode() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                if (tag) {
                    while (i > 0) {
                        i--;
                        //如果活动为空
                        if (LoginActivity.this == null) {
                            break;
                        }

                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mBtGetCode.setText("获取验证码(" + i + ")");
                                mBtGetCode.setClickable(false);
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tag = false;
                }
                i = 60;
                tag = true;

                if (LoginActivity.this != null) {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBtGetCode.setText("获取验证码");
                            mBtGetCode.setClickable(true);
                        }
                    });
                }
            }
        };
        thread.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        boolean login = SPUtil.getBoolean(this, LOGIN);
        if(login){
            launch();
        }
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Message msg = new Message();
                        msg.arg1 = 0;
                        msg.obj = "提交验证码成功";
                        handler.sendMessage(msg);
                        SPUtil.putBoolean(LoginActivity.this, LOGIN, true);
                        SPUtil.putString(LoginActivity.this, "phone_num", phone);

                        Log.d(TAG, "提交验证码成功");
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Message msg = new Message();
                        //获取验证码成功
                        msg.arg1 = 1;
                        msg.obj = "获取验证码成功";
                        handler.sendMessage(msg);
                        Log.d(TAG, "获取验证码成功");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        Message msg = new Message();
                        //返回支持发送验证码的国家列表
                        msg.arg1 = 2;
                        msg.obj = "@null";
                        handler.sendMessage(msg);
                        Log.d(TAG, "返回支持发送验证码的国家列表");
                    }
                } else {
                    Message msg = new Message();
                    //返回支持发送验证码的国家列表
                    msg.arg1 = 3;
                    msg.obj = "验证失败";
                    handler.sendMessage(msg);
                    Log.d(TAG, "验证失败");
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler); //注册短信回调

    }

    private boolean isMobileNO(String phone) {
       /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phone)) return false;
        else return phone.matches(telRegex);
    }

    private void initView() {
        mBack = (ImageView) findViewById(R.id.back);
        mBcircleBorderChangeExample = (BootstrapCircleThumbnail) findViewById(R.id.bcircle_border_change_example);
        mZhanghao = (BootstrapEditText) findViewById(R.id.zhanghao);
        mBtGetCode = (Button) findViewById(R.id.bt_getCode);
        mBtGetCode.setOnClickListener(this);
        mEtCode = (BootstrapEditText) findViewById(R.id.et_code);
        mGetCodeRl = (RelativeLayout) findViewById(R.id.get_code_rl);
        mDenglu = (BootstrapButton) findViewById(R.id.denglu);
        mDenglu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.bt_getCode:
                phone = mZhanghao.getText().toString();
                if(isMobileNO(phone)){
                    getSupportedCountries();
                    getVerificationCode("86", phone);
                    changeBtnGetCode();
                }else {
                    Toast.makeText(LoginActivity.this,"手机号格式错误，请检查",Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.denglu:
                String code=mEtCode.getText().toString();
                if (TextUtils.isEmpty(code)){
                    Toast.makeText(LoginActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    submitVerificationCode("86", phone, code);
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }
//    private void changeBtnGetCode() {
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                if (tag) {
//                    while (i > 0) {
//                        i--;
//                        //如果活动为空
//                        if (RegisterActivity.this == null) {
//                            break;
//                        }
//
//                        RegisterActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                bt_getCode.setText("获取验证码(" + i + ")");
//                                bt_getCode.setClickable(false);
//                            }
//                        });
//
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    tag = false;
//                }
//                i = 60;
//                tag = true;
//
//                if (RegisterActivity.this != null) {
//                    RegisterActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            bt_getCode.setText("获取验证码");
//                            bt_getCode.setClickable(true);
//                        }
//                    });
//                }
//            }
//        };
//        thread.start();
//    }
}
