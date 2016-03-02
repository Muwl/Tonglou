package cn.yunluosoft.tonglou.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.ChatActivity;
import cn.yunluosoft.tonglou.activity.ConstactActivity;
import cn.yunluosoft.tonglou.activity.fragment.WithFloorFragment;
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.model.MessageInfo;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Mu on 2016/2/2.
 */
public class WithFloorAdapter extends BaseAdapter {

    private Context context;
    private List<FloorSpeechEntity> entities;
    private BitmapUtils bitmapUtils;
    private Handler handler;

    public WithFloorAdapter(Context context,List<FloorSpeechEntity> entities,Handler handler) {
        this.context = context;
        this.entities=entities;
        this.handler=handler;
        bitmapUtils=new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.fwithfloor_item,null);
            holder.icon= (CircleImageView) convertView.findViewById(R.id.fwithfloor_item_icon);
            holder.name= (TextView) convertView.findViewById(R.id.fwithfloor_item_name);
            holder.tip= (TextView) convertView.findViewById(R.id.fwithfloor_item_tip);
            holder.content= (TextView) convertView.findViewById(R.id.fwithfloor_item_tip);
            holder.bluebtn= convertView.findViewById(R.id.fwithfloor_item_bluebtn);
            holder.graybtn= convertView.findViewById(R.id.fwithfloor_item_graybtn);
            holder.bluetext= (TextView) convertView.findViewById(R.id.fwithfloor_item_bluetext);
            holder.graytext= (TextView) convertView.findViewById(R.id.fwithfloor_item_graytext);
            holder.praise= (TextView) convertView.findViewById(R.id.fwithfloor_item_atten);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        bitmapUtils.display(holder.icon, entities.get(position).publishUserIcon);
        holder.name.setText(entities.get(position).publishUserNickname);
        holder.content.setText(entities.get(position).detail);
        holder.tip.setText(entities.get(position).topic);
        holder.bluebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,
                        ChatActivity.class);
                MessageInfo messageInfo=new MessageInfo();
                messageInfo.receiverHeadUrl=entities.get(position).publishUserIcon;
                messageInfo.receiverImUserName=entities.get(position).publishUserImUsername;
                messageInfo.receiverNickName=entities.get(position).publishUserNickname;
                messageInfo.receiverUserId=entities.get(position).publishUserId;
                messageInfo.senderHeadUrl= ShareDataTool.getIcon(context);
                messageInfo.senderImUserName=ShareDataTool.getImUsername(context);
                messageInfo.senderUserId= ShareDataTool.getUserId(context);
                messageInfo.senderNickName= ShareDataTool.getNickname(context);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", messageInfo);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        if (Constant.ATTEN_OK.equals(entities.get(position).isAttention)){
            holder.graybtn.setBackgroundResource(R.drawable.gray_attened);
            holder.graytext.setText("已关注");
        }else{
            holder.graybtn.setBackgroundResource(R.drawable.gray_atten);
            holder.graytext.setText("关注");
        }
        holder.graybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message=new Message();
                message.what= WithFloorFragment.ATTEN;
                message.obj=position;
                handler.sendMessage(message);
            }
        });

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConstactActivity.class);
                intent.putExtra("id", entities.get(position).publishUserId);
                intent.putExtra("name", entities.get(position).publishUserNickname);
                context.startActivity(intent);
            }
        });

        holder.praise.setText(entities.get(position).praiseNum);
        if (Constant.PRAISE_OK.equals(entities.get(position).isPraise)){
            holder.praise.setTextColor(Color.parseColor("#499EB8"));
            Drawable drawable=context.getResources().getDrawable(R.mipmap.consult_atten_checked);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.praise.setCompoundDrawables(drawable,null,null,null);

        }else{
            holder.praise.setTextColor(Color.parseColor("#B3B3B3"));
            Drawable drawable=context.getResources().getDrawable(R.mipmap.consult_atten);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.praise.setCompoundDrawables(drawable, null, null, null);
        }

        holder.praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message=new Message();
                message.what= WithFloorFragment.PRAISE;
                message.obj=position;
                handler.sendMessage(message);
            }
        });
        return convertView;
    }

    class ViewHolder{
        public CircleImageView icon;
        public TextView name;
        public TextView tip;
        public TextView content;
        public View bluebtn;
        public View graybtn;
        public TextView bluetext;
        public TextView graytext;
        public TextView praise;
    }
}
