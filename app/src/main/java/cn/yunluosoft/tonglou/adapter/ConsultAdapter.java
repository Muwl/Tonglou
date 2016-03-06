package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.ConsultDetailActivity;
import cn.yunluosoft.tonglou.model.ConsultEntity;
import cn.yunluosoft.tonglou.view.MyListView;

/**
 * Created by Mu on 2016/1/28.
 */
public class ConsultAdapter extends BaseAdapter {

    private Context context;
    private List<ConsultEntity> entities;
    private BitmapUtils bitmapUtils;

    public ConsultAdapter(Context context,List<ConsultEntity> entities) {
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
            convertView=View.inflate(context, R.layout.consult_item,null);
            holder=new ViewHolder();
            holder.imageView= (ImageView) convertView.findViewById(R.id.consult_item_image);
            holder.textView= (TextView) convertView.findViewById(R.id.consult_item_text);
            holder.view= (TextView) convertView.findViewById(R.id.consult_item_view);
            holder.listView= (MyListView) convertView.findViewById(R.id.consult_item_listview);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        bitmapUtils.display(holder.imageView, entities.get(position).news.get(0).coverImage);
        holder.textView.setText(entities.get(position).news.get(0).topic);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConsultDetailActivity.class);
                intent.putExtra("id", entities.get(position).news.get(0).id);
                context.startActivity(intent);
            }
        });
        CousultItemAdapter adapter=new CousultItemAdapter(context,bitmapUtils,entities.get(position).news);
        holder.listView.setAdapter(adapter);

        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context, ConsultDetailActivity.class);
                intent.putExtra("id",entities.get(position).news.get(position+1).id);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class  ViewHolder{
        public ImageView imageView;
        public TextView textView;
        public View view;
        public MyListView listView;
    }
}
