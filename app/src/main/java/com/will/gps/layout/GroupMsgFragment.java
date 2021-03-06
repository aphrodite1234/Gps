package com.will.gps.layout;

/**
 * Created by MaiBenBen on 2019/4/21.
 */

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.will.gps.GroupChatActivity;
/*import com.will.gps.GroupChatActivity;
import com.will.gps.MainActivity;*/
import com.will.gps.R;
import com.will.gps.base.DBOpenHelper;
import com.will.gps.bean.Group;
import com.will.gps.base.RMessage;
import com.will.gps.bean.RecentContactBean;
import com.suntek.commonlibrary.adapter.OnItemClickListener;
import com.suntek.commonlibrary.adapter.RViewHolder;
import com.suntek.commonlibrary.adapter.RecycleViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by wudeng on 2017/8/28.
 */
@SuppressLint("ValidFragment")
public class GroupMsgFragment extends Fragment {

    private static final String TAG = GroupMsgFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<RecentContactBean> mContactList;
    private RecycleViewAdapter<RecentContactBean> mViewAdapter;
    private SimpleDateFormat mDateFormat;
    private Context context;
    private View view;
    private Gson gson = new Gson();

    private Group group1 = new Group();
    private RecentContactBean rcb1 = new RecentContactBean();

    @SuppressLint("ValidFragment")
    public GroupMsgFragment(List<RecentContactBean> List) {
        mContactList = List;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.activity_fragment_group, container, false);
        //缓存的rootView需要判断是否已经被加过parent
        //如果有parent需要从parent删除，要不然会发生这个rootView已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        mRecyclerView = view.findViewById(R.id.rcv_group_list);
        mDateFormat = new SimpleDateFormat("HH:mm");

        DBOpenHelper dbOpenHelper=new DBOpenHelper(getActivity());
        initRecyclerView(dbOpenHelper.searchgroup(dbOpenHelper));
        //initListener();//更新最近联系人列表
        for (int i = 0; i < mContactList.size(); i++) {
            //RecentContactBean bean = mContactList.get(i);
            //if (bean.getRecentContact().getContactId().equals(contact.getContactId())){
            //bean.setRecentContact(contact);
            mViewAdapter.notifyItemChanged(i);
        }
        //loadRecentList();
        return view;
    }

    public void initRecyclerView(List<String> groups) {//初始化RecyclerView组件
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mContactList = new ArrayList<>();

        if (!groups.isEmpty()) {
            for (String group : groups) {
                Group group4 = gson.fromJson(group, Group.class);
                RecentContactBean recentContactBean = new RecentContactBean();
                recentContactBean.setGroup(group4);
                mContactList.add(recentContactBean);
            }
        }else {
            TextView textView = view.findViewById(R.id.txt_empty);
            textView.setText("暂未加入签到群");
        }

        mRecyclerView.setLayoutManager(layoutManager);
        mViewAdapter = new RecycleViewAdapter<RecentContactBean>(context, mContactList) {
            @Override
            public int setItemLayoutId(int position) {
                return R.layout.item_recent_msg;
            }

            @Override
            public void bindView(RViewHolder holder, int position) {
                RecentContactBean contactBean = mContactList.get(position);
                //UserInfo userInfo = contactBean.getUserInfo();
                Group group = contactBean.getGroup();
                if (group != null) {
                    //mContactList.get(position).setUserInfo(userInfo);
                    mContactList.get(position).setGroup(group);
                    holder.setImageByUrl(context, R.id.iv_head_picture,
                            contactBean.getGroup().getGroupimg(), R.mipmap.group_chat);
                    holder.setText(R.id.tv_recent_name, contactBean.getGroup().getGroupname());
                    holder.setText(R.id.tv_recent_content, contactBean.getGroup().getMembernum()+"人");
                } else {
                    holder.setImageResource(R.id.iv_head_picture, R.mipmap.app_logo_main);
                    holder.setText(R.id.tv_recent_name, Integer.toString(contactBean.getGroup().getGroupid()));
                }
                //String time = mDateFormat.format(new Date(contactBean.getRecentContact().getTime()));
                String time = mDateFormat.format(new Date());
                holder.setText(R.id.tv_recent_time, time);
            }
        };

        mViewAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RViewHolder holder, int position) {
                RecentContactBean contactBean = mContactList.get(position);
                Intent intent;
                //if (contactBean.getRecentContact().getSessionType() == SessionTypeEnum.P2P){
                intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra("groupname", contactBean.getGroup().getGroupname());
                intent.putExtra("groupid", String.valueOf(contactBean.getGroup().getGroupid()));
                intent.putExtra("groupowner", contactBean.getGroup().getGroupowner());
                intent.putExtra("membernum", contactBean.getGroup().getMembernum());
                intent.putExtra("ismember","true");
                startActivity(intent);
                //}
            }
        });

        mRecyclerView.setAdapter(mViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        //NIMClient.getService(MsgServiceObserve.class).observeRecentContact(mObserver,true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }
}

