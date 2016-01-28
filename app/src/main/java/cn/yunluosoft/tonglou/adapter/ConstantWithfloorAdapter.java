package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Mu on 2016/1/28.
 */
public class ConstantWithfloorAdapter extends BaseAdapter {

    private Context context;

    public ConstantWithfloorAdapter(Context context) {
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
            convertView=View.inflate(context, R.layout.constant_withfloor_item,null);
            holder.icon= (CircleImageView) convertView.findViewById(R.id.constant_withfloor_item_icon);
            holder.sex= (ImageView) convertView.findViewById(R.id.constant_withfloor_item_sex);
            holder.name= (TextView) convertView.findViewById(R.id.constant_withfloor_item_name);
            holder.job= (TextView) convertView.findViewById(R.id.constant_withfloor_item_job);
            holder.trade= (TextView) convertView.findViewById(R.id.constant_withfloor_item_trade);
            holder.content= (TextView) convertView.findViewById(R.id.constant_withfloor_item_content);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    class ViewHolder{
        public CircleImageView icon;
        public ImageView sex;
        public TextView name;
        public TextView job;
        public TextView trade;
        public TextView content;
    }
}
