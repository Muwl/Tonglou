package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.AssistActivity;
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Mu on 2016/1/27.
 */
public class MyfloorspeekAdapter extends BaseAdapter {

    private Context context;
    private List<FloorSpeechEntity> entities;
    private BitmapUtils bitmapUtils;
    private Handler handler;
    private int flag;//0代表我的关注 1代表我的发布
    public MyfloorspeekAdapter(Context context,List<FloorSpeechEntity> entities,Handler handler) {
        this.context = context;
        this.handler=handler;
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

    public void setFlag(int flag){
        this.flag=flag;
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
            holder.bluebtn= convertView.findViewById(R.id.myfloorspeech_item_bluebtn);
            holder.blueimage= (ImageView) convertView.findViewById(R.id.myfloorspeech_item_blueimage);
            holder.bluetext= (TextView) convertView.findViewById(R.id.myfloorspeech_item_bluetext);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        bitmapUtils.display(holder.icon, entities.get(position).publishUserIcon);
        holder.name.setText(entities.get(position).publishUserNickname);
        holder.tip.setText(entities.get(position).topic);
        holder.content.setText(entities.get(position).detail);
        holder.bluebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if ("0".equals(entities.get(position).isInGroup)){
            holder.bluetext.setText("聊聊");
            holder.blueimage.setImageResource(R.mipmap.myfloor_speak);
        }else{
            holder.bluetext.setText("进群聊");
            holder.blueimage.setImageResource(R.mipmap.add_chat);
        }

        LogManager.LogShow("----",flag+"----------",LogManager.ERROR);

        if (flag==0){
            holder.name.setVisibility(View.VISIBLE);
            holder.bluebtn.setVisibility(View.VISIBLE);
        }else{
            holder.name.setVisibility(View.GONE);
            holder.bluebtn.setVisibility(View.GONE);
        }

        return convertView;
    }
    class ViewHolder{
        public CircleImageView icon;
        public TextView name;
        public TextView tip;
        public TextView content;
        public View bluebtn;
        public ImageView blueimage;
        public TextView bluetext;
    }
}
