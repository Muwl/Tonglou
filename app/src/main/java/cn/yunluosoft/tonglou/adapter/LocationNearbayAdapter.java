package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.model.LocationEntity;

/**
 * Created by Mu on 2016/1/26.
 */
public class LocationNearbayAdapter extends BaseAdapter {

    private Context context;
    private List<LocationEntity> entities;

    public LocationNearbayAdapter(Context context,List<LocationEntity> entities) {
        this.context = context;
        this.entities=entities;
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
            convertView=View.inflate(context, R.layout.location_nearbay_item,null);
            holder=new ViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.location_nearbay_item_name);
            holder.address= (TextView) convertView.findViewById(R.id.location_nearbay_item_address);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.name.setText(entities.get(position).name);
        holder.address.setText(entities.get(position).addr);
        return convertView;
    }
    class ViewHolder{
        public TextView name;
        public TextView address;

    }
}
