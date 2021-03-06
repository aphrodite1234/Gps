package com.will.gps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.will.gps.R;
import com.will.gps.base.ApplicationData;
import com.will.gps.bean.ChatEntity;
import com.will.gps.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaiBenBen on 2019/4/27.
 */

public class ChatMessageAdapter extends BaseAdapter{
    private List<ChatEntity> chatEntitiesList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext0;

    public ChatMessageAdapter(Context context, List<ChatEntity> vector) {
        this.chatEntitiesList = vector;
        mInflater = LayoutInflater.from(context);
        mContext0 = context;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMessageView;
        TextView rightMessageView;
        TextView timeView,friend_name,my_name;
        ImageView leftPhotoView;
        ImageView rightPhotoView;
        view = mInflater.inflate(R.layout.chat_message_item, null);
        ChatEntity chatEntity = chatEntitiesList.get(position);
        leftLayout = (LinearLayout) view
                .findViewById(R.id.chat_friend_left_layout);
        rightLayout = (LinearLayout) view
                .findViewById(R.id.chat_user_right_layout);
        timeView = (TextView) view.findViewById(R.id.message_time);
        leftPhotoView = (ImageView) view
                .findViewById(R.id.message_friend_userphoto);
        rightPhotoView = (ImageView) view
                .findViewById(R.id.message_user_userphoto);
        leftMessageView = (TextView) view.findViewById(R.id.friend_message);
        rightMessageView = (TextView) view.findViewById(R.id.user_message);
        friend_name=(TextView)view.findViewById(R.id.friend_name);
        my_name=(TextView)view.findViewById(R.id.my_name);

        User user = ApplicationData.getInstance().getUserInfo();
        timeView.setText(chatEntity.getSendTime());
        if (chatEntity.getMessageType() == ChatEntity.SEND) {
            rightLayout.setVisibility(View.VISIBLE);
            leftLayout.setVisibility(View.GONE);

            /*rightPhotoView.setImageBitmap(ApplicationData.getInstance()
                    .getUserPhoto());*/
            rightMessageView.setText(chatEntity.getContent());
            my_name.setText(chatEntity.getSendername());
        } else if (chatEntity.getMessageType() == ChatEntity.RECEIVE) {// 本身作为接收方
            leftLayout.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.GONE);
            /*Bitmap photo = ApplicationData.getInstance().getFriendPhotoMap()
                    .get(chatEntity.getSenderId());*/
            /*if (photo != null)
                leftPhotoView.setImageBitmap(photo);*/
            leftMessageView.setText(chatEntity.getContent());
            friend_name.setText(chatEntity.getSendername());
        }
        return view;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(chatEntitiesList!=null){
            return chatEntitiesList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if(chatEntitiesList!=null){
            return chatEntitiesList.get(position);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
