package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.utils.DensityUtil;

/**
 * Created by Administrator on 2016/2/10.
 */
public class PublishUsedAdapter extends BaseAdapter {

    private Context context;
    private int width;

    public PublishUsedAdapter(Context context, int width) {
        this.context = context;
        this.width = width;
    }

    @Override
    public int getCount() {
        return 1;
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

        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
    }
}
