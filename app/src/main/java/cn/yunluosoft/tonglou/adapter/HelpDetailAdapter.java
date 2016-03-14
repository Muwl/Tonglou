package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.ChatActivity;
import cn.yunluosoft.tonglou.activity.ConstactActivity;
import cn.yunluosoft.tonglou.activity.GroupDetailActivity;
import cn.yunluosoft.tonglou.activity.HelpDetailActivity;
import cn.yunluosoft.tonglou.dialog.CustomeDialog;
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.model.MessageInfo;
import cn.yunluosoft.tonglou.model.ReplayEntity;
import cn.yunluosoft.tonglou.model.User;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CircleImageView;
import cn.yunluosoft.tonglou.view.MyGridView;

/**
 * Created by Mu on 2016/2/1.
 */
public class HelpDetailAdapter extends BaseAdapter {

    private Context context;
    private List<ReplayEntity> entities;
    private BitmapUtils bitmapUtils;
    private Handler handler;
    private int type0 = 1;
    private int type1 = 2;
    private FloorSpeechEntity entity;
    private PopupWindow menuWindow;
    private LinearLayout linearLayout;
    private ImageView rep;

    public HelpDetailAdapter(Context context, List<ReplayEntity> entities, FloorSpeechEntity entity, Handler handler) {
        this.context = context;
        this.entities = entities;
        this.handler = handler;
        this.entity = entity;
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
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return type0;
        } else {
            return type1;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        ViewHolder1 holder1=null;
        int type = getItemViewType(position);
        if (type == type0) {
            if (convertView == null
                    || !convertView.getTag().getClass()
                    .equals(ViewHolder.class)) {
                convertView = View.inflate(context, R.layout.helpdetail_head,
                        null);
                holder = new ViewHolder();
                holder.icon = (CircleImageView) convertView.findViewById(R.id.helpdetail_icon);
                holder.name = (TextView) convertView.findViewById(R.id.helpdetail_name);
                holder.address = (TextView) convertView.findViewById(R.id.helpdetail_address);
                holder.content = (TextView) convertView.findViewById(R.id.helpdetail_content);
                holder.replay = (ImageView) convertView.findViewById(R.id.helpdetail_replay);
                holder.join = (TextView) convertView.findViewById(R.id.helpdetail_join);
                holder.menu_lin = (LinearLayout) convertView.findViewById(R.id.helpdetail_menu_lin);
                holder.gridView = (MyGridView) convertView.findViewById(R.id.helpdetail_grid);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
        } else if (type == type1) {
            if (convertView == null
                    || !convertView.getTag().getClass()
                    .equals(ViewHolder1.class)) {
                convertView = View.inflate(context, R.layout.groupdetail_item, null);
                holder1 = new ViewHolder1();
                holder1.icon = (CircleImageView) convertView.findViewById(R.id.groupdetail_item_icon);
                holder1.name = (TextView) convertView.findViewById(R.id.groupdetail_item_name);
                holder1.content = (TextView) convertView.findViewById(R.id.groupdetail_item_content);
                holder1.time = (TextView) convertView.findViewById(R.id.groupdetail_item_time);
                holder1.reply = (TextView) convertView.findViewById(R.id.groupdetail_item_reply);
                holder1.del = (TextView) convertView.findViewById(R.id.groupdetail_item_del);
                convertView.setTag(holder1);
            } else {
                holder1 = (ViewHolder1) convertView.getTag();
            }
        }
        if (type == type0) {
            bitmapUtils.display(holder.icon, entity.publishUserIcon);
            holder.name.setText(entity.publishUserNickname);
            holder.address.setText(entity.locationName);
            holder.content.setText(entity.detail);
            List<User> userList=entity.praiseUser;
            if(userList==null){
                userList=new ArrayList<>();
            }
            GroupDetailGridViewAdapter gridAdapter=new GroupDetailGridViewAdapter(context,userList);
            holder.gridView.setAdapter(gridAdapter);

            linearLayout=holder.menu_lin;
            rep=holder.replay;

            holder.join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,
                            ChatActivity.class);
                    MessageInfo messageInfo=new MessageInfo();
                    messageInfo.receiverHeadUrl=entity.publishUserIcon;
                    messageInfo.receiverImUserName=entity.publishUserImUsername;
                    messageInfo.receiverNickName=entity.publishUserNickname;
                    messageInfo.receiverUserId=entity.publishUserId;
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
            holder.replay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (menuWindow == null || menuWindow.isShowing() == false) {
                        menu_press();
                        // menu_display = true;
                    } else {
                        // menu_display = false;
                        menuWindow.dismiss();

                    }
                }
            });

            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ConstactActivity.class);
                    intent.putExtra("id", entity.publishUserId);
                    intent.putExtra("name", entity.publishUserNickname);
                    context.startActivity(intent);
                }
            });

        } else if(type == type1) {
            bitmapUtils.display(holder1.icon, entities.get(position-1).publishUserIcon);
            holder1.name.setText(entities.get(position - 1).publishUserNickname);
            holder1.content.setText(entities.get(position-1).content);
            holder1.time.setText(entities.get(position-1).createDate);
            holder1.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message message = new Message();
                    message.what = 1005;
                    message.arg1 = (position - 1);
                    handler.sendMessage(message);
                }
            });

            holder1.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ConstactActivity.class);
                    intent.putExtra("id", entities.get(position-1).publishUserId);
                    intent.putExtra("name", entities.get(position-1).publishUserNickname);
                    context.startActivity(intent);
                }
            });

            if (ShareDataTool.getUserId(context).equals(entities.get(position-1).publishUserId)){
                holder1.del.setVisibility(View.VISIBLE);
                holder1.del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String temp="是否删除该评论？";
                        if (ToosUtils.isStringEmpty(entities.get(position - 1).targetUserId)) {
                            temp="是否删除该评论？";
                        }else{
                            temp="是否删除该回复？";
                        }

                        CustomeDialog dialog=new CustomeDialog(context,handler,temp,position-1,-2);
//                        Message message=new Message();
//                        message.what=1009;
//                        message.arg1=(position-1);
//                        handler.sendMessage(message);
                    }
                });
            }else{
                holder1.del.setVisibility(View.GONE);
            }

        }
        return convertView;
    }

    /**
     * 弹出menu菜单
     */
    public void menu_press() {
        // if (!menu_display) {
        // 获取LayoutInflater实例
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService("layout_inflater");
        // 这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
        // 该方法返回的是一个View的对象，是布局中的根
        View layout = inflater.inflate(R.layout.commite_menu, null);
        View atten = layout
                .findViewById(R.id.groupdetail_atten);
        View comment = layout
                .findViewById(R.id.groupdetail_comment);
        TextView attentext= (TextView) layout.findViewById(R.id.groupdetail_attentext);
        if ("0".equals(entity.isPraise)){
            attentext.setText("已赞");
        }else{
            attentext.setText("赞");
        }
        atten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
                handler.sendEmptyMessage(1002);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
                Message message=new Message();
                message.what=1005;
                message.arg1=-1;
                handler.sendMessage(message);
            }
        });
        int screenWidth = ((HelpDetailActivity)context).getWindowManager().getDefaultDisplay()
                .getWidth();
        // 下面我们要考虑了，我怎样将我的layout加入到PopupWindow中呢？？？很简单
        menuWindow = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT); // 后两个参数是width和height
        // menuWindow.showAsDropDown(layout); //设置弹出效果
        // menuWindow.showAsDropDown(null, 0, layout.getHeight());
        // 设置如下四条信息，当点击其他区域使其隐藏，要在show之前配置
        menuWindow.setBackgroundDrawable(new BitmapDrawable());
        menuWindow.setFocusable(true);
        menuWindow.setOutsideTouchable(true);
        menuWindow.update();
//        menuWindow.showAsDropDown(linearLayout,150,50);
        layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int xOffset = -(layout.getMeasuredWidth() + rep.getWidth());
        menuWindow.showAsDropDown(rep,xOffset,DensityUtil.dip2px(context, -25));
//        menuWindow.showAtLocation(linearLayout,  Gravity.RIGHT,
//                DensityUtil.dip2px(context, 45),
//                (int) (rep.getY()-DensityUtil.dip2px(context,20))); // 设置layout在PopupWindow中显示的位置
        // 如何获取我们main中的控件呢？也很简单
        // }
    }
    

    class ViewHolder {
        public CircleImageView icon;
        public TextView name;
        public TextView address;
        public TextView content;
        public TextView join;
        public LinearLayout menu_lin;
        public ImageView replay;
        public MyGridView gridView;
    }

    class ViewHolder1 {
        public CircleImageView icon;
        public TextView name;
        public TextView content;
        public TextView time;
        public TextView reply;
        public TextView del;
    }


}
