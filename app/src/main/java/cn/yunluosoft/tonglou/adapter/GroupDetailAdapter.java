package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.model.ReplayEntity;
import cn.yunluosoft.tonglou.model.User;
import cn.yunluosoft.tonglou.view.CircleImageView;
import cn.yunluosoft.tonglou.view.MyGallery;
import cn.yunluosoft.tonglou.view.MyGridView;

/**
 * Created by Mu on 2016/2/1.
 */
public class GroupDetailAdapter extends BaseAdapter {

    private Context context;
    private List<ReplayEntity> entities;
    private BitmapUtils bitmapUtils;
    private Handler handler;
    private int type0 = 1;
    private int type1 = 2;
    private FloorSpeechEntity entity;

    public GroupDetailAdapter(Context context, List<ReplayEntity> entities, FloorSpeechEntity entity, Handler handler) {
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        ViewHolder1 holder1=null;
        int type = getItemViewType(position);
        if (type == type0) {
            if (convertView == null
                    || !convertView.getTag().getClass()
                    .equals(ViewHolder.class)) {
                convertView = View.inflate(context, R.layout.groupdetail_head,
                        null);
                holder = new ViewHolder();
                holder.icon = (CircleImageView) convertView.findViewById(R.id.groupdetail_icon);
                holder.name = (TextView) convertView.findViewById(R.id.groupdetail_name);
                holder.address = (TextView) convertView.findViewById(R.id.groupdetail_address);
                holder.num = (TextView) convertView.findViewById(R.id.groupdetail_num);
                holder.time = (TextView) convertView.findViewById(R.id.groupdetail_time);
                holder.content = (TextView) convertView.findViewById(R.id.groupdetail_content);
                holder.join = (TextView) convertView.findViewById(R.id.groupdetail_join);
                holder.atten = (TextView) convertView.findViewById(R.id.groupdetail_atten);
                holder.replay = (ImageView) convertView.findViewById(R.id.groupdetail_replay);
                holder.comment = (TextView) convertView.findViewById(R.id.groupdetail_comment);
                holder.gridView = (MyGridView) convertView.findViewById(R.id.groupdetail_grid);
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
                convertView.setTag(holder1);
            } else {
                holder1 = (ViewHolder1) convertView.getTag();
            }
        }
        if (type == type0) {
            bitmapUtils.display(holder.icon, entity.publishUserIcon);
            holder.name.setText(entity.publishUserNickname);
            holder.address.setText(entity.locationName);
            holder.num.setText("参团人数：" + entity.planPeopleNum + "/" + entity.groupNum);
            holder.time.setText("截止日期："+entity.endDate);
            holder.content.setText(entity.detail);
            holder.join.setText("参加");
            List<User> userList=entity.praiseUser;
            if(userList==null){
                userList=new ArrayList<>();
            }
            GroupDetailGridViewAdapter gridAdapter=new GroupDetailGridViewAdapter(context,userList);
            holder.gridView.setAdapter(gridAdapter);

        } else if(type == type1) {
            bitmapUtils.display(holder.icon, entities.get(position-1).publishUserIcon);
            holder.name.setText(entities.get(position-1).publishUserNickname);
            holder.content.setText(entities.get(position-1).content);
            holder.time.setText(entities.get(position-1).createDate);
        }
        return convertView;
    }
    

    class ViewHolder {
        private CircleImageView icon;
        private TextView name;
        private TextView address;
        private TextView num;
        private TextView time;
        private TextView content;
        private TextView join;
        private TextView atten;
        private TextView comment;
        private ImageView replay;
        private MyGridView gridView;
    }

    class ViewHolder1 {
        public CircleImageView icon;
        public TextView name;
        public TextView content;
        public TextView time;
        public TextView reply;
    }


}
