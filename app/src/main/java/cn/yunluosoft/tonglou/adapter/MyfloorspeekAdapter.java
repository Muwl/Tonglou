package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Mu on 2016/1/27.
 */
public class MyfloorspeekAdapter extends BaseAdapter {

    private Context context;

    public MyfloorspeekAdapter(Context context) {
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
            convertView=View.inflate(context, R.layout.myfloorspeech_item,null);
            holder=new ViewHolder();
            holder.icon= (CircleImageView) convertView.findViewById(R.id.myfloorspeech_item_icon);
            holder.name= (TextView) convertView.findViewById(R.id.myfloorspeech_item_name);
            holder.tip= (TextView) convertView.findViewById(R.id.myfloorspeech_item_tip);
            holder.content= (TextView) convertView.findViewById(R.id.myfloorspeech_item_content);
            holder.speck= (TextView) convertView.findViewById(R.id.myfloorspeech_item_speak);
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
        public TextView content;
        public TextView speck;
    }
}
