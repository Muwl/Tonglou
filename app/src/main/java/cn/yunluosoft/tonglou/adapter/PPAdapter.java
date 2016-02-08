package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Mu on 2016/2/2.
 */
public class PPAdapter extends BaseAdapter {

    private Context context;

    public PPAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 8;
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
            convertView=View.inflate(context, R.layout.pp_item,null);
            holder.icon= (CircleImageView) convertView.findViewById(R.id.pp_item_icon);
            holder.name= (TextView) convertView.findViewById(R.id.pp_item_name);
            holder.start= (TextView) convertView.findViewById(R.id.pp_item_start);
            holder.stop= (TextView) convertView.findViewById(R.id.pp_item_stop);
            holder.content= (TextView) convertView.findViewById(R.id.pp_item_tip);
            holder.bluebtn= (TextView) convertView.findViewById(R.id.pp_item_bluebtn);
            holder.graybtn= (TextView) convertView.findViewById(R.id.pp_item_graybtn);
            holder.atten= (TextView) convertView.findViewById(R.id.pp_item_atten);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder{
        public CircleImageView icon;
        public TextView name;
        public TextView tip;
        public TextView start;
        public TextView stop;
        public TextView content;
        public TextView bluebtn;
        public TextView graybtn;
        public TextView atten;
    }
}