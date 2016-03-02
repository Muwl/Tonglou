package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.ConstactActivity;
import cn.yunluosoft.tonglou.model.ConstantWithfloorEntity;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Mu on 2016/1/28.
 */
public class ConstantWithfloorAdapter extends BaseAdapter {

    private Context context;
    private List<ConstantWithfloorEntity> entities;
    private BitmapUtils bitmapUtils;

    public ConstantWithfloorAdapter(Context context,List<ConstantWithfloorEntity> entities) {
        this.context = context;
        this.entities=entities;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
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
        bitmapUtils.display(holder.icon,entities.get(position).icon);
        if (Constant.SEX_MAN.endsWith(entities.get(position).sex)){
            holder.sex.setImageResource(R.mipmap.icon_sex_male);
        }else{
            holder.sex.setImageResource(R.mipmap.icon_sex_female);
        }
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConstactActivity.class);
                intent.putExtra("id", entities.get(position).id);
                intent.putExtra("name", entities.get(position).nickname);
                context.startActivity(intent);
            }
        });
        holder.name.setText(entities.get(position).nickname);
        holder.job.setText(entities.get(position).job);
        holder.trade.setText(entities.get(position).industry);
        holder.content.setText(entities.get(position).signature);
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
