package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.ConstactActivity;
import cn.yunluosoft.tonglou.model.GroupInfoEntity;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Administrator on 2016/3/1.
 */
public class GroupInfoAdapter extends BaseAdapter {

    private Context context;
    private BitmapUtils bitmapUtils;
    private List<GroupInfoEntity> entities;
    private int width;

    public GroupInfoAdapter(Context context, List<GroupInfoEntity> entities,int width) {
        this.context = context;
        this.entities = entities;
        this.width=width;
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
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.groupinfo_item,null);
            holder.icon= (CircleImageView) convertView.findViewById(R.id.groupinfo_item_icon);
            holder.name= (TextView) convertView.findViewById(R.id.groupinfo_item_name);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        bitmapUtils.display(holder.icon, entities.get(position).icon);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) holder.icon.getLayoutParams();
        params.width=(width- DensityUtil.dip2px(context,68))/4;
        params.height=(width- DensityUtil.dip2px(context,68))/4;
        holder.icon.setLayoutParams(params);
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConstactActivity.class);
                intent.putExtra("id", entities.get(position).id);
                intent.putExtra("name", entities.get(position).nickname);
                context.startActivity(intent);
            }
        });
        if (!ToosUtils.isStringEmpty(entities.get(position).nickname)) {
            holder.name.setText(entities.get(position).nickname);
        }
        return convertView;
    }
    class ViewHolder{
        public CircleImageView icon;
        public TextView name;
    }
}
