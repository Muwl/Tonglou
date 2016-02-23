package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.model.ReplayEntity;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Mu on 2016/2/1.
 */
public class GroupDetailAdapter extends BaseAdapter {

    private Context context;
    private List<ReplayEntity> entities;
    private BitmapUtils bitmapUtils;
    private Handler handler;

    public GroupDetailAdapter(Context context,List<ReplayEntity> entities,Handler handler) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.groupdetail_item,null);
            holder.icon= (CircleImageView) convertView.findViewById(R.id.groupdetail_item_icon);
            holder.name= (TextView) convertView.findViewById(R.id.groupdetail_item_name);
            holder.content= (TextView) convertView.findViewById(R.id.groupdetail_item_content);
            holder.time= (TextView) convertView.findViewById(R.id.groupdetail_item_time);
            holder.reply= (TextView) convertView.findViewById(R.id.groupdetail_item_reply);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        bitmapUtils.display(holder.icon,entities.get(position).publishUserIcon);
        holder.name.setText(entities.get(position).publishUserNickname);
        holder.content.setText(entities.get(position).content);
        holder.time.setText(entities.get(position).createDate);
        return convertView;
    }

    class ViewHolder{
        public CircleImageView icon;
        public TextView name;
        public TextView content;
        public TextView time;
        public TextView reply;
    }
}
