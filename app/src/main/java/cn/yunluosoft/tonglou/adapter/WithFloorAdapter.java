package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.fragment.WithFloorFragment;
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Mu on 2016/2/2.
 */
public class WithFloorAdapter extends BaseAdapter {

    private Context context;
    private List<FloorSpeechEntity> entities;
    private BitmapUtils bitmapUtils;
    private Handler handler;

    public WithFloorAdapter(Context context,List<FloorSpeechEntity> entities,Handler handler) {
        this.context = context;
        this.entities=entities;
        this.handler=handler;
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
            convertView=View.inflate(context, R.layout.fwithfloor_item,null);
            holder.icon= (CircleImageView) convertView.findViewById(R.id.fwithfloor_item_icon);
            holder.name= (TextView) convertView.findViewById(R.id.fwithfloor_item_name);
            holder.content= (TextView) convertView.findViewById(R.id.fwithfloor_item_tip);
            holder.bluebtn= (TextView) convertView.findViewById(R.id.fwithfloor_item_bluebtn);
            holder.graybtn= (TextView) convertView.findViewById(R.id.fwithfloor_item_graybtn);
            holder.praise= (TextView) convertView.findViewById(R.id.fwithfloor_item_atten);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        bitmapUtils.display(holder.icon, entities.get(position).publishUserIcon);
        holder.name.setText(entities.get(position).publishUserNickname);
        holder.content.setText(entities.get(position).detail);
        holder.bluebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (Constant.ATTEN_OK.equals(entities.get(position).isAttention)){
            holder.graybtn.setBackgroundResource(R.drawable.gray_attened);
        }else{
            holder.graybtn.setBackgroundResource(R.drawable.gray_atten);
        }
        holder.graybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message=new Message();
                message.what= WithFloorFragment.ATTEN;
                message.obj=position;
                handler.sendMessage(message);
            }
        });

        holder.praise.setText(entities.get(position).praiseNum);
        if (Constant.PRAISE_OK.equals(entities.get(position).isPraise)){
            holder.praise.setTextColor(Color.parseColor("#B3B3B3"));
        }else{
            holder.praise.setTextColor(Color.parseColor("#499EB8"));
        }

        holder.praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message=new Message();
                message.what= WithFloorFragment.PRAISE;
                message.obj=position;
                handler.sendMessage(message);
            }
        });
        return convertView;
    }

    class ViewHolder{
        public CircleImageView icon;
        public TextView name;
        public TextView tip;
        public TextView content;
        public TextView bluebtn;
        public TextView graybtn;
        public TextView praise;
    }
}
