package cn.yunluosoft.tonglou.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.io.Serializable;
import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.ChatActivity;
import cn.yunluosoft.tonglou.activity.ConstactActivity;
import cn.yunluosoft.tonglou.activity.PPActivity;
import cn.yunluosoft.tonglou.activity.UsedActivity;
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
public class PPAdapter extends BaseAdapter {

    private Context context;
    private List<FloorSpeechEntity> entities;
    private BitmapUtils bitmapUtils;
    private Handler handler;

    public PPAdapter(Context context, List<FloorSpeechEntity> entities, Handler handler) {
        this.context = context;
        this.entities = entities;
        this.handler = handler;
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.pp_item, null);
            holder.icon = (CircleImageView) convertView.findViewById(R.id.pp_item_icon);
            holder.name = (TextView) convertView.findViewById(R.id.pp_item_name);
            holder.tip = (TextView) convertView.findViewById(R.id.pp_item_tip);
            holder.start = (TextView) convertView.findViewById(R.id.pp_item_start);
            holder.stop = (TextView) convertView.findViewById(R.id.pp_item_stop);
            holder.content = (TextView) convertView.findViewById(R.id.pp_item_content);
            holder.bluebtn = convertView.findViewById(R.id.pp_item_bluebtn);
            holder.graybtn = convertView.findViewById(R.id.pp_item_graybtn);
            holder.blueimage = (ImageView) convertView.findViewById(R.id.pp_item_blueimage);
            holder.bluetext = (TextView) convertView.findViewById(R.id.pp_item_bluetext);
            holder.graytext = (TextView) convertView.findViewById(R.id.pp_item_graytext);
            holder.praise = (TextView) convertView.findViewById(R.id.pp_item_atten);
            holder.time = (TextView) convertView.findViewById(R.id.pp_item_time);
            holder.car= (ImageView) convertView.findViewById(R.id.pp_item_car);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        bitmapUtils.display(holder.icon, entities.get(position).publishUserIcon);
        holder.name.setText(entities.get(position).publishUserNickname);
        holder.tip.setText(entities.get(position).topic);
        holder.content.setText(entities.get(position).detail);
        holder.time.setText(entities.get(position).createDate);
        holder.start.setText(entities.get(position).start);
        holder.stop.setText(entities.get(position).end);
//        if (!entities.get(position).publishUserId.equals(ShareDataTool.getUserId(context))) {
            holder.bluebtn.setBackgroundResource(R.drawable.blue_chat);
            holder.bluebtn.setClickable(true);
            holder.bluebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ToosUtils.CheckComInfo(context)) {
                        Intent intent = new Intent(context,
                                ChatActivity.class);
                        MessageInfo info = new MessageInfo(
                                ShareDataTool.getUserId(context), entities.get(position).publishUserId,
                                ShareDataTool.getUserId(context), entities.get(position).publishUserImUsername,
                                ShareDataTool.getIcon(context),
                                entities.get(position).publishUserIcon, ShareDataTool.getNickname(context), entities.get(position).publishUserNickname);
                        intent.putExtra("info", (Serializable) info);
                        context.startActivity(intent);
                    }
                }
            });
//        } else {
//            holder.bluebtn.setBackgroundResource(R.drawable.gray_atten);
//            holder.bluebtn.setClickable(false);
//        }
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
//                if (ToosUtils.CheckComInfo(context)) {
//                    if (!ShareDataTool.getUserId(context).equals(entities.get(position).publishUserId)) {
                        Message message = new Message();
                        message.what = PPActivity.ATTEN;
                        message.obj = position;
                        handler.sendMessage(message);
//                    } else {
//                        ToastUtils.displayShortToast(context, "不可以关注自己的发布！");
////                    }
//                }
            }
        });
        holder.praise.setText(entities.get(position).praiseNum);
        if (Constant.PRAISE_OK.equals(entities.get(position).isPraise)) {
            holder.praise.setTextColor(Color.parseColor("#499EB8"));

            Drawable drawable =ContextCompat.getDrawable(context, R.mipmap.consult_atten_checked);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.praise.setCompoundDrawables(drawable, null, null, null);

        } else {
            holder.praise.setTextColor(Color.parseColor("#B3B3B3"));

            Drawable drawable =   ContextCompat.getDrawable(context, R.mipmap.consult_atten);;
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.praise.setCompoundDrawables(drawable, null, null, null);
        }

        if ("0".equals(entities.get(position).supplyType)){
            holder.car.setImageResource(R.mipmap.car_car);
        }else{
            holder.car.setImageResource(R.mipmap.car_nocar);
        }

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConstactActivity.class);
                intent.putExtra("id", entities.get(position).publishUserId);
                intent.putExtra("name", entities.get(position).publishUserNickname);
                context.startActivity(intent);
            }
        });
        holder.praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ToosUtils.CheckComInfo(context)) {
                    Message message = new Message();
                    message.what = PPActivity.PRAISE;
                    message.obj = position;
                    handler.sendMessage(message);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        public CircleImageView icon;
        public TextView name;
        public TextView tip;
        public TextView start;
        public TextView stop;
        public TextView content;
        public View bluebtn;
        public View graybtn;
        public ImageView blueimage;
        public TextView bluetext;
        public TextView graytext;
        public  ImageView car;
        public TextView praise;
        public TextView time;
    }
}
