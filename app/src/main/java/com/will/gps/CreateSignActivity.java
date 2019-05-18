package com.will.gps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.will.gps.map.AmapActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by MaiBenBen on 2019/5/7.
 */

public class CreateSignActivity extends Activity implements View.OnClickListener{
    private ImageView btn_back;
    private EditText sign_id;
    private Button btn_create,btn_dingwei1,btn_dingwei2;
    private Button dateButton,timeButton;
    private int id;
    private EditText latitude,longitude;
    public GregorianCalendar QCalendar;
    SimpleDateFormat dateFormat1,dateFormat2;//格式化时间
    private Intent i;
    private double lat,lgt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_create);
        bindView();
    }

    @SuppressLint("SimpleDateFormat")
    private void bindView(){
        btn_back=(ImageView)findViewById(R.id.sign_create_back);
        sign_id=(EditText)findViewById(R.id.sign_create_id);
        latitude=(EditText)findViewById(R.id.sign_create_latitude);
        longitude=(EditText)findViewById(R.id.sign_create_longitude);
        btn_create=(Button)findViewById(R.id.sign_create_btn);
        btn_dingwei1=(Button)findViewById(R.id.sign_create_dingwei1);
        btn_dingwei2=(Button)findViewById(R.id.sign_create_dingwei2);

        dateButton = (Button)findViewById(R.id.dateButton);
        timeButton = (Button)findViewById(R.id.timeButton);

        btn_dingwei1.setOnClickListener(this);
        btn_dingwei2.setOnClickListener(this);
        btn_create.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        dateButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);

        QCalendar = getCalendarAfter30Mins();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = df.format(QCalendar.getTime());
        dateButton.setText(dateString);

        df = new SimpleDateFormat("HH:mm");
        String timeString = df.format(QCalendar.getTime());
        timeButton.setText(timeString);

        dateFormat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);//不同的时间格式
        dateFormat2=new SimpleDateFormat("y年M月d日 H时m分s秒", Locale.CHINA);

        i=getIntent();
        try{
            lat=i.getDoubleExtra("lat",0.000000);
            lgt=i.getDoubleExtra("lgt",0.000000);
            latitude.setText(String.valueOf(lat));
            longitude.setText(String.valueOf(lgt));
        }catch(Exception e){
            Toast.makeText(this,"intent为空",Toast.LENGTH_SHORT).show();
        }
    }

    @Override@SuppressLint("SimpleDateFormat")
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_create_dingwei1:
                Intent i=new Intent(CreateSignActivity.this, AmapActivity.class);
                i.putExtra("jingdu","high");
                startActivity(i);
                break;
            case R.id.sign_create_dingwei2:
                Intent i2=new Intent(CreateSignActivity.this, AmapActivity.class);
                i2.putExtra("jingdu","low");
                startActivity(i2);
            case R.id.sign_create_btn:

                if (TextUtils.isEmpty(sign_id.getText().toString().trim())) {
                    Toast.makeText(CreateSignActivity.this, "请输入签到名称", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    id = Integer.valueOf(sign_id.getText().toString().trim());
                }
                Toast.makeText(CreateSignActivity.this,"创建签到活动成功！",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.sign_create_back:
                //finish();
                System.out.println(dateFormat1.format(QCalendar.getTime()));
                System.out.println(dateFormat2.format(QCalendar.getTime()));
                break;
            case R.id.dateButton:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        QCalendar.set(year, monthOfYear, dayOfMonth);
                        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                        String dateString = df.format(QCalendar.getTime());
                        dateButton.setText(dateString);
                    }
                }, QCalendar.get(Calendar.YEAR), QCalendar.get(Calendar.MONTH),
                        QCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.timeButton:
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        QCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        QCalendar.set(Calendar.MINUTE, minute);
                        DateFormat df = new SimpleDateFormat("HH:mm");
                        String timeString = df.format(QCalendar.getTime());
                        timeButton.setText(timeString);
                    }
                }, QCalendar.get(Calendar.HOUR_OF_DAY), QCalendar
                        .get(Calendar.MINUTE), true).show();
                break;
        }
    }
    public GregorianCalendar getCalendarAfter30Mins() {
        GregorianCalendar calendar = new GregorianCalendar();
        if (calendar.get(Calendar.MINUTE) >= 30) {
            calendar.add(Calendar.MINUTE, 60 - calendar.get(Calendar.MINUTE));
        } else {
            calendar.add(Calendar.MINUTE, 30 - calendar.get(Calendar.MINUTE));
        }

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }
}
