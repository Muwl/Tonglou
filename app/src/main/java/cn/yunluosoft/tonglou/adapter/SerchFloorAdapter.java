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
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.ChatActivity;
import cn.yunluosoft.tonglou.activity.ConstactActivity;
import cn.yunluosoft.tonglou.activity.HiGroupActivity;
import cn.yunluosoft.tonglou.activity.fragment.WithFloorFragment;
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.model.MessageInfo;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Mu on 2016/2/2.
 */
public class SerchFloorAdapter extends BaseAdapter {

    private Context context;
    private List<FloorSpeechEntity> entities;
    private BitmapUtils bitmapUtils;
    private Handler handler;
    private ListView listView;

    public SerchFloorAdapter(Context context, List<FloorSpeechEntity> entities, Handler handler, ListView listView) {
        this.context = context;
        this.entities = entities;
        this.handler = handler;
        this.listView = listView;
        bitmapUtils = new BitmapUtils(context);
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
        ViewHolder holder = null;
            if (convertView == null ) {
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


        bitmapUtils.display(holder.icon, entities.get(position).publishUserIcon);
        holder.name.setText(entities.get(position).publishUserNickname);
        holder.content.setText(entities.get(position).detail);
        holder.tip.setText(entities.get(position).topic);
        holder.time.setText(entities.get(position).createDate);
        if ("0".equals(entities.get(position).modelType) || !entities.get(position).publishUserId.equals(ShareDataTool.getUserId(context))) {
            holder.bluebtn.setBackgroundResource(R.drawable.blue_chat);
            holder.bluebtn.setClickable(true);
            holder.bluebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ToosUtils.CheckComInfo(context)) {
                        if ("0".equals(entities.get(position).modelType)) {
                            if ("0".equals(entities.get(position).isInGroup)) {
                                Intent intent = new Intent(context,
                                        ChatActivity.class);
                                MessageInfo messageInfo = new MessageInfo();
                                messageInfo.receiverHeadUrl = entities.get(position).id;
                                messageInfo.groupDynamicID = entities.get(position).id;
                                messageInfo.receiverImUserName = entities.get(position).imGroupId;
                                messageInfo.receiverNickName = entities.get(position).groupName;
                                messageInfo.receiverUserId = entities.get(position).imGroupId;
                                intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("info", messageInfo);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            } else {
                                Message message = new Message();
                                message.what = HiGroupActivity.ADDGROUP;
                                message.arg1 = position;
                                handler.sendMessage(message);
                            }
                        } else {
                            Intent intent = new Intent(context,
                                    ChatActivity.class);
                            MessageInfo messageInfo = new MessageInfo();
                            messageInfo.receiverHeadUrl = entities.get(position).publishUserIcon;
                            messageInfo.receiverImUserName = entities.get(position).publishUserImUsername;
                            messageInfo.receiverNickName = entities.get(position).publishUserNickname;
                            messageInfo.receiverUserId = entities.get(position).publishUserId;
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

            if ("1".equals(entities.get(position).isInGroup) && "0".equals(entities.get(position).modelType)) {
                if ("0".equals(entities.get(position).applyState) && Integer.valueOf(entities.get(position).planPeopleNum) <= Integer.valueOf(entities.get(position).groupNum)) {
                    holder.bluetext.setText("已满");
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
        } else {
            holder.bluetext.setText("聊聊");
            holder.blueimage.setImageResource(R.mipmap.myfloor_speak);
            holder.bluebtn.setBackgroundResource(R.drawable.gray_atten);
            holder.bluebtn.setClickable(false);
        }

//        holder.graybtn.setTag("graybtn"+position);
//        holder.graytext.setTag("graytext"+position);
        if (Constant.ATTEN_OK.equals(entities.get(position).isAttention)) {
            holder.graybtn.setBackgroundResource(R.drawable.gray_attened);
            holder.graytext.setText("已关注");
        } else {
            holder.graybtn.setBackgroundResource(R.drawable.gray_atten);
            holder.graytext.setText("关注");
        }
        holder.graybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ToosUtils.CheckComInfo(context)) {
                    if (!ShareDataTool.getUserId(context).equals(entities.get(position).publishUserId)) {
                        Message message = new Message();
                        message.what = WithFloorFragment.ATTEN;
                        message.obj = position;
                        handler.sendMessage(message);
                    } else {
                        ToastUtils.displayShortToast(context, "不可以关注自己的发布！");
                    }
                }
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
        holder.praise.setTag("praise" + position);
        holder.praise.setText(entities.get(position).praiseNum);
        if (Constant.PRAISE_OK.equals(entities.get(position).isPraise)) {
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
                    message.obj = position;
                    handler.sendMessage(message);
                }
            }
        });
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
