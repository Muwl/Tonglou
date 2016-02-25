package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;

/**
 * Created by Administrator on 2016/2/25.
 */
public class UseddetailheadAdapter extends BaseAdapter {

    private List<String> entities;
    private Context context;
    private BitmapUtils bitmapUtils;
    public UseddetailheadAdapter(Context context,List<String> entities) {
        this.context=context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context,R.layout.useddetail_head_item,null);
            holder.imageView= (ImageView) convertView.findViewById(R.id.useddetail_head_item_image);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        bitmapUtils.display(holder.imageView,entities.get(position));
        return convertView;
    }
    class ViewHolder{
        public ImageView imageView;
    }
}
