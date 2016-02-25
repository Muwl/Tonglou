package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.utils.DensityUtil;

/**
 * Created by Administrator on 2016/2/10.
 */
public class PublishUsedAdapter extends BaseAdapter {

    private Context context;
    private int width;
    private List<File> files;
    private BitmapUtils bitmapUtils;

    public PublishUsedAdapter(Context context, int width, List<File> files) {
        this.context = context;
        this.width = width;
        this.files=files;
        bitmapUtils=new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        if (files.size()<4){
            return files.size()+1;
        }else{
            return 4;
        }
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.publish_used_item, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.publish_used_item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.imageView.getLayoutParams();
        params.width = (width - DensityUtil.dip2px(context, 48)) / 4;
        params.height = (width - DensityUtil.dip2px(context, 48)) / 4;
        holder.imageView.setLayoutParams(params);
        if (files.size()!=4){
            if (position<files.size()){
                bitmapUtils.display(holder.imageView,files.get(position).getAbsolutePath());
            }else{
                holder.imageView.setImageResource(R.mipmap.image_add);
            }
        }else{
            bitmapUtils.display(holder.imageView,files.get(position).getAbsolutePath());
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
    }
}
