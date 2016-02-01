package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Mu on 2016/2/1.
 */
public class ConstactsAddAdapter extends BaseAdapter {

    private Context context;

    public ConstactsAddAdapter(Context context) {
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
            convertView=View.inflate(context, R.layout.constacts_add_item,null);
            holder=new ViewHolder();
            holder.icon= (CircleImageView) convertView.findViewById(R.id.constacts_add_item_icon);
            holder.name= (TextView) convertView.findViewById(R.id.constacts_add_item_name);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder{
        public CircleImageView icon;
        public TextView name;
    }

}
