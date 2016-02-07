package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Mu on 2016/2/1.
 */
public class GroupDetailAdapter extends BaseAdapter {

    private Context context;

    public GroupDetailAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
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
