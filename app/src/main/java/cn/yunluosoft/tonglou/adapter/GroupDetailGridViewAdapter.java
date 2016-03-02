package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.ConstactActivity;
import cn.yunluosoft.tonglou.model.User;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Administrator on 2016/2/23.
 */
public class GroupDetailGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<User> entities;
    private BitmapUtils bitmapUtils;

    public GroupDetailGridViewAdapter(Context context, List<User> entities) {
        this.context = context;
        this.entities = entities;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.groupdetail_grid_item,null);
            holder=new ViewHolder();
            holder.icon= (CircleImageView) convertView.findViewById(R.id.groupdetail_grid_item_image);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        bitmapUtils.display(holder.icon,entities.get(position).icon);
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConstactActivity.class);
                intent.putExtra("id", entities.get(position).id);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        public CircleImageView icon;
    }
}
