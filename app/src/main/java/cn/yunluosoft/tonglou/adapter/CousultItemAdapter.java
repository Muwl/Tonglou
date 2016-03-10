package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.model.NewsEntity;

/**
 * Created by Administrator on 2016/3/6.
 */
public class CousultItemAdapter extends BaseAdapter {

    private Context context;
    private BitmapUtils bitmapUtils;
    private List<NewsEntity> newsEntities;

    public CousultItemAdapter(Context context, BitmapUtils bitmapUtils, List<NewsEntity> newsEntities) {
        this.context = context;
        this.bitmapUtils = bitmapUtils;
        this.newsEntities = newsEntities;
    }

    @Override
    public int getCount() {
        return newsEntities.size()-1;
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
            convertView=View.inflate(context, R.layout.consult_item_item,null);
            holder=new ViewHolder();
            holder.textView= (TextView) convertView.findViewById(R.id.consult_item_item_text);
            holder.imageView= (ImageView) convertView.findViewById(R.id.consult_item_item_image);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(newsEntities.get(position+1).topic);
        bitmapUtils.display(holder.imageView,newsEntities.get(position+1).coverImage);
        return convertView;
    }
    class  ViewHolder{
        public TextView textView;
        public ImageView imageView;
    }
}
