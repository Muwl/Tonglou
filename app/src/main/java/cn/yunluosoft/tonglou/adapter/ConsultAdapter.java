package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;

/**
 * Created by Mu on 2016/1/28.
 */
public class ConsultAdapter extends BaseAdapter {

    private Context context;

    public ConsultAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 4;
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
            convertView=View.inflate(context, R.layout.consult_item,null);
            holder=new ViewHolder();
            holder.imageView= (ImageView) convertView.findViewById(R.id.consult_item_image);
            holder.textView= (TextView) convertView.findViewById(R.id.consult_item_text);
            holder.imageView1= (ImageView) convertView.findViewById(R.id.consult_item_image1);
            holder.textView1= (TextView) convertView.findViewById(R.id.consult_item_text1);
            holder.imageView2= (ImageView) convertView.findViewById(R.id.consult_item_image2);
            holder.textView2= (TextView) convertView.findViewById(R.id.consult_item_text2);
            holder.imageView3= (ImageView) convertView.findViewById(R.id.consult_item_image3);
            holder.textView3= (TextView) convertView.findViewById(R.id.consult_item_text3);
            convertView.setTag(holder);

        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class  ViewHolder{
        public ImageView imageView;
        public TextView textView;
        public ImageView imageView1;
        public TextView textView1;
        public ImageView imageView2;
        public TextView textView2;
        public ImageView imageView3;
        public TextView textView3;
    }
}
