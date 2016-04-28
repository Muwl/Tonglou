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
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.AssistActivity;
import cn.yunluosoft.tonglou.activity.ChatActivity;
import cn.yunluosoft.tonglou.activity.ConstactActivity;
import cn.yunluosoft.tonglou.activity.HiGroupActivity;
import cn.yunluosoft.tonglou.activity.PPActivity;
import cn.yunluosoft.tonglou.activity.SerchSpeechActivity;
import cn.yunluosoft.tonglou.activity.UsedActivity;
import cn.yunluosoft.tonglou.activity.fragment.WithFloorFragment;
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.model.MessageInfo;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CircleImageView;
import cn.yunluosoft.tonglou.view.MyGridView;

/**
 * Created by Mu on 2016/2/2.
 */
public class WithFloorAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private List<FloorSpeechEntity> entities;
    private BitmapUtils bitmapUtils;
    private Handler handler;
    private ListView listView;
    private int type0 = 1;
    private int type1 = 2;

    public WithFloorAdapter(Context context, List<FloorSpeechEntity> entities, Handler handler, ListView listView) {
        this.context = context;
        this.entities = entities;
        this.handler = handler;
        this.listView = listView;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return entities.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return type0;
        } else {
            return type1;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        ViewHolder1 holder1 = null;
        int type = getItemViewType(position);
        if (type == type0) {
            convertView = null;
            if (convertView == null
                    || !convertView.getTag().getClass()
                    .equals(ViewHolder1.class)) {
                convertView = View.inflate(context, R.layout.fwithfloor_head,
                        null);
                holder1 = new ViewHolder1();
                holder1.serch = convertView.findViewById(R.id.fwithfloor_serch);
                holder1.group = (TextView) convertView.findViewById(R.id.fwithfloor_group);
                holder1.used = (TextView) convertView.findViewById(R.id.fwithfloor_used);
                holder1.pp = (TextView) convertView.findViewById(R.id.fwithfloor_pp);
                holder1.help = (TextView) convertView.findViewById(R.id.fwithfloor_help);
                convertView.setTag(holder1);
            } else {
                holder1 = (ViewHolder1) convertView.getTag();
            }
        } else if (type == type1) {
            if (convertView == null || !convertView.getTag().getClass()
                    .equals(ViewHolder.class)) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.fwithfloor_item, null);
                holder.icon = (CircleImageView) convertView.findViewById(R.id.fwithfloor_item_icon);
                holder.name = (TextView) convertView.findViewById(R.id.fwithfloor_item_name);
                holder.tip = (TextView) convertView.findViewById(R.id.fwithfloor_item_tip);
                holder.content = (TextView) convertView.findViewById(R.id.fwithfloor_item_content);
                holder.bluebtn = convertView.findViewById(R.id.fwithfloor_item_bluebtn);
                holder.graybtn = convertView.findViewById(R.id.fwithfloor_item_graybtn);
                holder.bluetext = (TextView) convertView.findViewById(R.id.fwithfloor_item_bluetext);
                holder.blueimage = (ImageView) convertView.findViewById(R.id.fwithfloor_item_blueimage);
                holder.graytext = (TextView) convertView.findViewById(R.id.fwithfloor_item_graytext);
                holder.praise = (TextView) convertView.findViewById(R.id.fwithfloor_item_atten);
                holder.time = (TextView) convertView.findViewById(R.id.fwithfloor_item_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
        }
        if (type == type0) {
            holder1.serch.setOnClickListener(this);
            holder1.group.setOnClickListener(this);
            holder1.used.setOnClickListener(this);
            holder1.pp.setOnClickListener(this);
            holder1.help.setOnClickListener(this);
        } else if (type == type1) {
            bitmapUtils.display(holder.icon, entities.get(position - 1).publishUserIcon);

            holder.name.setText(entities.get(position - 1).publishUserNickname);
            if("0".equals(entities.get(position-1).modelType)){
                holder.content.setText("活动："+entities.get(position-1).detail);
            }else if("1".equals(entities.get(position-1).modelType)){
                if ("0".equals(entities.get(position-1).supplyType)){
                    holder.content.setText("转让："+entities.get(position-1).detail);
                }else{
                    holder.content.setText("求购："+entities.get(position-1).detail);
                }

            }else if("2".equals(entities.get(position-1).modelType)){
                if ("0".equals(entities.get(position-1).supplyType)){
                    holder.content.setText("车主："+entities.get(position-1).detail);
                }else{
                    holder.content.setText("乘客："+entities.get(position-1).detail);
                }
            }else if("3".equals(entities.get(position-1).modelType)){
                if ("0".equals(entities.get(position-1).supplyType)){
                    holder.content.setText("求帮："+entities.get(position-1).detail);
                }else{
                    holder.content.setText("自荐："+entities.get(position-1).detail);
                }
            }
            holder.tip.setText(entities.get(position-1).topic);
            holder.time.setText(entities.get(position-1).createDate);
//            if ("0".equals(entities.get(position-1).modelType) || !entities.get(position-1).publishUserId.equals(ShareDataTool.getUserId(context))) {
                holder.bluebtn.setBackgroundResource(R.drawable.blue_chat);
                holder.bluebtn.setClickable(true);
                holder.bluebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (ToosUtils.CheckComInfo(context)) {


                            if ("0".equals(entities.get(position - 1).modelType)) {
                                if ("0".equals(entities.get(position - 1).isInGroup)) {
                                    Intent intent = new Intent(context,
                                            ChatActivity.class);
                                    MessageInfo messageInfo = new MessageInfo();
                                    messageInfo.receiverHeadUrl = entities.get(position - 1).id;
                                    messageInfo.groupDynamicID = entities.get(position - 1).id;
                                    messageInfo.receiverImUserName = entities.get(position - 1).imGroupId;
                                    messageInfo.receiverNickName = entities.get(position - 1).groupName;
                                    messageInfo.receiverUserId = entities.get(position - 1).imGroupId;
                                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("info", messageInfo);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                } else {
                                    Message message = new Message();
                                    message.what = HiGroupActivity.ADDGROUP;
                                    message.arg1 = position - 1;
                                    handler.sendMessage(message);
                                }
                            } else {
                                Intent intent = new Intent(context,
                                        ChatActivity.class);
                                MessageInfo messageInfo = new MessageInfo();
                                messageInfo.receiverHeadUrl = entities.get(position - 1).publishUserIcon;
                                messageInfo.receiverImUserName = entities.get(position - 1).publishUserImUsername;
                                messageInfo.receiverNickName = entities.get(position - 1).publishUserNickname;
                                messageInfo.receiverUserId = entities.get(position - 1).publishUserId;
                                messageInfo.senderHeadUrl = ShareDataTool.getIcon(context);
                                messageInfo.senderImUserName = ShareDataTool.getImUsername(context);
                                messageInfo.senderUserId = ShareDataTool.getUserId(context);
                                messageInfo.senderNickName = ShareDataTool.getNickname(context);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("info", messageInfo);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        }
                    }
                });

                if ("1".equals(entities.get(position-1).isInGroup) && "0".equals(entities.get(position-1).modelType)) {
                    if (ToosUtils.isStringEmpty(entities.get(position-1).planPeopleNum)){
                        entities.get(position-1).planPeopleNum="0";
                    }
                    if (ToosUtils.isStringEmpty(entities.get(position-1).groupNum)){
                        entities.get(position-1).groupNum="0";
                    }
                    if ("0".equals(entities.get(position-1).applyState) && Integer.valueOf(entities.get(position-1).planPeopleNum) <= Integer.valueOf(entities.get(position-1).groupNum)) {
                        holder.bluetext.setText("已关闭");
                        holder.blueimage.setImageResource(R.mipmap.end);
                        holder.bluebtn.setBackgroundResource(R.drawable.gray_atten);
                        holder.bluebtn.setClickable(false);
                        holder.bluebtn.setEnabled(false);
                    } else {
                        holder.bluetext.setText("加入");
                        holder.blueimage.setImageResource(R.mipmap.add_chat);
                    }

                } else {
                    holder.bluetext.setText("聊聊");
                    holder.blueimage.setImageResource(R.mipmap.myfloor_speak);
                }
//            } else {
//                holder.bluetext.setText("聊聊");
//                holder.blueimage.setImageResource(R.mipmap.myfloor_speak);
//                holder.bluebtn.setBackgroundResource(R.drawable.gray_atten);
//                holder.bluebtn.setClickable(false);
//            }

//        holder.graybtn.setTag("graybtn"+position);
//        holder.graytext.setTag("graytext"+position);
            if (Constant.ATTEN_OK.equals(entities.get(position-1).isAttention)) {
                holder.graybtn.setBackgroundResource(R.drawable.gray_attened);
                holder.graytext.setText("已关注");
            } else {
                holder.graybtn.setBackgroundResource(R.drawable.gray_atten);
                holder.graytext.setText("关注");
            }
            holder.graybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (ToosUtils.CheckComInfo(context)) {
//                        if (!ShareDataTool.getUserId(context).equals(entities.get(position - 1).publishUserId)) {
                            Message message = new Message();
                            message.what = WithFloorFragment.ATTEN;
                            message.obj = position - 1;
                            handler.sendMessage(message);
//                        } else {
//                            ToastUtils.displayShortToast(context, "不可以关注自己的发布！");
//                        }
//                    }
                }
            });

            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ConstactActivity.class);
                    intent.putExtra("id", entities.get(position-1).publishUserId);
                    intent.putExtra("name", entities.get(position-1).publishUserNickname);
                    context.startActivity(intent);
                }
            });
            holder.praise.setTag("praise" + (position-1));
            holder.praise.setText(entities.get(position-1).praiseNum);
            if (Constant.PRAISE_OK.equals(entities.get(position-1).isPraise)) {
                holder.praise.setTextColor(Color.parseColor("#499EB8"));
                Drawable drawable = ContextCompat.getDrawable(context, R.mipmap.consult_atten_checked);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.praise.setCompoundDrawables(drawable, null, null, null);

            } else {
                holder.praise.setTextColor(Color.parseColor("#B3B3B3"));
                Drawable drawable = ContextCompat.getDrawable(context, R.mipmap.consult_atten);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.praise.setCompoundDrawables(drawable, null, null, null);
            }

            holder.praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ToosUtils.CheckComInfo(context)) {
                        Message message = new Message();
                        message.what = WithFloorFragment.PRAISE;
                        message.obj = position - 1;
                        handler.sendMessage(message);
                    }

                }
            });
        }
        return convertView;
    }

    public void refushAtten(int position) {
        TextView praise = (TextView) listView.findViewWithTag("praise" + position);
        praise.setText(entities.get(position).praiseNum);
        if (Constant.PRAISE_OK.equals(entities.get(position).isPraise)) {
            praise.setTextColor(Color.parseColor("#499EB8"));
            Drawable drawable = context.getResources().getDrawable(R.mipmap.consult_atten_checked);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            praise.setCompoundDrawables(drawable, null, null, null);
        } else {
            praise.setTextColor(Color.parseColor("#B3B3B3"));
            Drawable drawable = context.getResources().getDrawable(R.mipmap.consult_atten);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            praise.setCompoundDrawables(drawable, null, null, null);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fwithfloor_serch:
                Intent intent5=new Intent(context, SerchSpeechActivity.class);
                intent5.putExtra("modelFlag",4);
                context.startActivity(intent5);
                break;
            case R.id.fwithfloor_group:
                Intent intent = new Intent(context, HiGroupActivity.class);
                context.startActivity(intent);
                break;

            case R.id.fwithfloor_used:
                Intent intent1 = new Intent(context, UsedActivity.class);
                context.startActivity(intent1);

                break;

            case R.id.fwithfloor_pp:
                Intent intent2 = new Intent(context, PPActivity.class);
                context.startActivity(intent2);

                break;

            case R.id.fwithfloor_help:
                Intent intent3 = new Intent(context, AssistActivity.class);
                context.startActivity(intent3);

                break;
        }
    }

    class ViewHolder1 {
        public View serch;
        public TextView group;
        public TextView used;
        public TextView pp;
        public TextView help;
    }

    class ViewHolder {
        public CircleImageView icon;
        public TextView name;
        public TextView tip;
        public TextView content;
        public View bluebtn;
        public View graybtn;
        public TextView bluetext;
        public ImageView blueimage;
        public TextView graytext;
        public TextView praise;
        public TextView time;
    }
}
