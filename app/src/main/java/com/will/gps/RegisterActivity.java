package com.will.gps;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.will.gps.Password.Password3Activity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.will.gps.base.MainALiSms.SendSms;


/**
 * Created by MaiBenBen on 2019/3/19.
 */

public class RegisterActivity extends Activity implements  View.OnClickListener {
    private TimerTask tt;
    private Timer tm;
    private EditText et_phonenum;
    private Button btn_check;
    private EditText et_checkecode;
    private Button btn_sure;
    private String code;
    private int TIME = 60;//倒计时60s这里应该多设置些因为mob后台需要60s,我们前端会有差异的建议设置90，100或者120
    public String country="86";//这是中国区号，如果需要其他国家列表，可以使用getSupportedCountries();获得国家区号
    private String phone;
    private static final int CODE_REPEAT = 1; //重新发送
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /*MobSDK.init(this, "24793dde94dc6", "6e636da9b16e5bf8d5fae19ca30ea6ac");
        SMSSDK.registerEventHandler(eh); //注册短信回调（记得销毁，避免泄露内存）*/
        initView();
    }
    Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_REPEAT) {
                btn_check.setEnabled(true);
                btn_sure.setEnabled(true);
                tm.cancel();//取消任务
                tt.cancel();//取消任务
                TIME = 60;//时间重置
                btn_check.setText("重新发送验证码");
            }else {
                btn_check.setText(TIME + "重新发送验证码");
            }
        }
    };
    /*EventHandler eh=new EventHandler(){//回调
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    toast("验证成功");
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){       //获取验证码成功
                    toast("获取验证码成功");
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//如果你调用了获取国家区号类表会在这里回调
                    // 返回支持发送验证码的国家列表
                }
            }else{//错误等在这里（包括验证失败）
                // 错误码请参照http://wiki.mob.com/android-api-错误码参考/这里我就不再继续写了
                ((Throwable)data).printStackTrace();
                String str = data.toString();
                toast(str);
            }
        }
    };*/
    //吐司的一个小方法
    /*private void toast(final String str) {//不知道为什么调用了却不显示toast
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    private void initView() {
        et_phonenum = (EditText) findViewById(R.id.et_phonenum);
        btn_check = (Button) findViewById(R.id.register_check);
        et_checkecode = (EditText) findViewById(R.id.et_checkecode);
        btn_sure = (Button) findViewById(R.id.register_sure);
        btn_check.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_check:
                phone = et_phonenum.getText().toString().trim().replaceAll("/s","");
                if (!TextUtils.isEmpty(phone)) {//定义需要匹配的正则表达式的规则
                    String REGEX_MOBILE_SIMPLE =  "[1][358]\\d{9}";//把正则表达式的规则编译成模板
                    Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);//把需要匹配的字符给模板匹配，获得匹配器
                    Matcher matcher = pattern.matcher(phone);//通过匹配器查找是否有该字符，不可重复调用重复调用matcher.find()
                    if (matcher.find()) {
                        alterWarning();   //匹配手机号是否存在
                    }
                    else {
                        //toast("手机号格式错误");
                        Toast.makeText(RegisterActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //toast("请先输入手机号");
                    Toast.makeText(RegisterActivity.this, "请先输入手机号", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_sure:                //获得用户输入的验证码
                String codetxt = et_checkecode.getText().toString().replaceAll("/s","");
                if (!TextUtils.isEmpty(codetxt)) {//判断验证码是否为空
                    //SMSSDK.submitVerificationCode( country,  phone,  code); //验证
                    if(code.equals(codetxt))
                    {
                        Intent intent=new Intent(RegisterActivity.this, Password3Activity.class);
                        intent.putExtra("type","注册");
                        intent.putExtra("phone",phone);
                        startActivity(intent);
                        finish();
                    }
                    else
                        Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                }
                else{//如果用户输入的内容为空，提醒用户
                    //toast("请输入验证码后再提交");
                    Toast.makeText(RegisterActivity.this, "请输入验证码后再提交", Toast.LENGTH_SHORT).show();
                }
                break;
                default:break;
        }
    }
    //弹窗确认下发
     private void alterWarning() {        //构造器
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle("提示"); //设置标题
         builder.setMessage("我们将要发送到" + phone + "验证"); //设置内容
         builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
         builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {            //设置确定按钮
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss(); //关闭dialog
                 new Thread(){
                     @Override
                     public void run()
                     {
                         code = Integer.toString((int)((Math.random()*9+1)*100000));  //每次调用生成一位四位数的随机数
                         try {
                             SendSms(phone,code);//发送验证码
                         } catch (Exception e) {
                             e.printStackTrace();
                         }//把网络访问的代码放在这里
                         System.out.println("code="+code);
                         System.out.println("#######################");
                         //SMSSDK.getVerificationCode(country, phone);//通过sdk发送短信验证（请求获取短信验证码，在监听（eh）中返回）
                     }
                 }.start();
                 Toast.makeText(RegisterActivity.this, "已发送" + which, Toast.LENGTH_SHORT).show();
                 btn_check.setEnabled(false);
                 btn_sure.setEnabled(true);
                 tm = new Timer();
                 tt = new TimerTask() {
                     @Override
                     public void run() {
                         hd.sendEmptyMessage(TIME--); //做倒计时操作
                     }
                 };
                 tm.schedule(tt,0,1000);
             }
         });
         builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
             // @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
                 Toast.makeText(RegisterActivity.this, "已取消" + which, Toast.LENGTH_SHORT).show();
             }
         });
         builder.create().show();//参数都设置完成了，创建并显示出来
    }
    /*@Override
    protected void onDestroy() { //销毁短信注册
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh); // 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
    }*/
}
