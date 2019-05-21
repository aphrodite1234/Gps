package com.will.gps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntek.commonlibrary.adapter.RViewHolder;
import com.suntek.commonlibrary.adapter.RecycleViewAdapter;
import com.will.gps.bean.ReceiverBean;
import com.will.gps.bean.SignTableBean;
import com.will.gps.map.ReceiverMapActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaiBenBen on 2019/5/10.
 */

public class ReceiverListAcitivty extends Activity{
    private TextView title,tip;
    private RecyclerView mRecyclerView;
    private List<ReceiverBean> receiverBeanList;
    private RecycleViewAdapter<ReceiverBean> receiverAdapter;
    private List<String> receivers;//从数据库查询到的签到表列表
    private Gson gson=new Gson();
    private ImageView btn_back,btn_map;
    private SignTableBean signTableBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list);

        initData();//初始化receivers

        signTableBean=(SignTableBean)getIntent().getSerializableExtra("signtable");
        title=(TextView)findViewById(R.id.sign_table_title);
        title.setText(String.valueOf(signTableBean.getId())+"的签到情况");
        tip=(TextView)findViewById(R.id.sign_table_tip);
        mRecyclerView=findViewById(R.id.sign_list);

        btn_back=(ImageView)findViewById(R.id.sign_table_back);
        btn_map=(ImageView)findViewById(R.id.btn_map);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_map.setImageResource(R.drawable.ic_location_on_white_24dp);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ReceiverListAcitivty.this,ReceiverMapActivity.class);
                i.putExtra("receivers",(Serializable) receivers);
                i.putExtra("signtable",signTableBean);
                startActivity(i);
            }
        });

        initRecyclerView(receivers);
        for(int i=0;i<receiverBeanList.size();i++)
            receiverAdapter.notifyItemChanged(i);
    }
    private void initData(){
        receivers=new ArrayList<>();
        ReceiverBean receiver1=new ReceiverBean();
        ReceiverBean receiver2=new ReceiverBean();
        receiver1.setId("15837811860");
        receiver1.setRealname("方元岭");
        receiver1.setRlongitude(114.314895);
        receiver1.setRlatitude(34.815956);
        receiver1.setDone(true);
        receiver1.setResult(true);

        receiver2.setId("12345678910");
        receiver2.setRlongitude(114.312232);
        receiver2.setRlatitude(34.81612);
        receiver2.setDone(false);
        receiver2.setResult(false);

        receivers.add(gson.toJson(receiver1));
        receivers.add(gson.toJson(receiver2));
    }

    private void initRecyclerView(List<String> receivers){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        receiverBeanList=new ArrayList<>();

        if (!receivers.isEmpty()) {
            for (String receiver : receivers) {
                ReceiverBean receiver1 = gson.fromJson(receiver, ReceiverBean.class);
                receiverBeanList.add(receiver1);
            }
        } else {
            tip.setText("还没有人签到");
        }
        mRecyclerView.setLayoutManager(layoutManager);
        receiverAdapter=new RecycleViewAdapter<ReceiverBean>(this,receiverBeanList) {
            @Override
            public int setItemLayoutId(int position) {
                return R.layout.receiver_item;
            }

            @Override
            public void bindView(RViewHolder holder, int position) {
                ReceiverBean receiverBean=receiverBeanList.get(position);
                if(receiverBean!=null){
                    holder.setText(R.id.receiver_id,String.valueOf(receiverBean.getId()));
                    holder.setText(R.id.receiver_realname,receiverBean.getRealname());
                    if(receiverBean.isDone())
                        holder.setText(R.id.receiver_done,"已签到");
                    else
                        holder.setText(R.id.receiver_done,"未签到");
                    holder.setText(R.id.receiver_longitude,"纬度:"+String.valueOf(receiverBean.getRlongitude()));
                    holder.setText(R.id.receiver_latitude,"纬度:"+String.valueOf(receiverBean.getRlatitude()));
                    if(receiverBean.isResult()){
                        holder.setImageResource(R.id.receiver_result,R.mipmap.result_cg);
                        holder.setText(R.id.receiver_textresult,"签到成功");
                    }
                    else{
                        holder.setImageResource(R.id.receiver_result,R.mipmap.result_sb);
                        holder.setText(R.id.receiver_textresult,"签到失败");
                    }
                }
            }
        };
        mRecyclerView.setAdapter(receiverAdapter);
    }
}