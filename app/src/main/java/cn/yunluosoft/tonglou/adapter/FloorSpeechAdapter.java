package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Mu on 2016/1/27.
 */
public class FloorSpeechAdapter extends BaseAdapter {

    private Context context;

    public FloorSpeechAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.ffloorspeech_item, null);
            holder.icon= (CircleImageView) convertView.findViewById(R.id.ffloorspeech_item_icon);
            holder.name= (TextView) convertView.findViewById(R.id.ffloorspeech_item_name);
            holder.time= (TextView) convertView.findViewById(R.id.ffloorspeech_item_time);
            holder.content= (TextView) convertView.findViewById(R.id.ffloorspeech_item_content);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder{
        public CircleImageView icon;
        public TextView name;
        public TextView time;
        public TextView content;
    }
}
