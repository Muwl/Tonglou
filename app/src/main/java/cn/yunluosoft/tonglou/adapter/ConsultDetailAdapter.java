package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.model.ConsultDetailEntity;
import cn.yunluosoft.tonglou.model.ConsultInfoEntity;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Mu on 2016/2/1.
 */
public class ConsultDetailAdapter extends BaseAdapter {

    private Context context;
    private int type0 = 1;
    private int type1 = 2;
    private String id;
    private List<ConsultDetailEntity> entities;
    private Handler handler;
    private BitmapUtils bitmapUtils;
    private ConsultInfoEntity entity;


    public ConsultDetailAdapter(Context context, String id,ConsultInfoEntity entity, List<ConsultDetailEntity> entities, Handler handler) {
        this.context = context;
        this.id = id;
        this.entity=entity;
        this.entities = entities;
        this.handler = handler;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return entities.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return type0;
        } else {
            return type1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        ViewHolder1 holder1 = null;
        int type = getItemViewType(position);
        if (type == type0) {
            if (convertView == null
                    || !convertView.getTag().getClass()
                    .equals(ViewHolder.class)) {
                convertView = View.inflate(context, R.layout.consult_detail_head, null);
                holder = new ViewHolder();
                holder.webView = (WebView) convertView.findViewById(R.id.consult_detail_web);
                holder.atten = (TextView) convertView.findViewById(R.id.consult_detail_atten);
                holder.read = (TextView) convertView.findViewById(R.id.consult_detail_read);
                holder.report = (TextView) convertView.findViewById(R.id.consult_detail_report);
                holder.message = (TextView) convertView.findViewById(R.id.consult_detail_message);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
        } else if (type == type1) {
            if (convertView == null || !convertView.getTag().getClass()
                    .equals(ViewHolder1.class)) {
                holder1 = new ViewHolder1();
                convertView = View.inflate(context, R.layout.consult_detail_item, null);
                holder1.icon = (CircleImageView) convertView.findViewById(R.id.consult_detail_item_icon);
                holder1.name = (TextView) convertView.findViewById(R.id.consult_detail_item_name);
                holder1.atten = (TextView) convertView.findViewById(R.id.consult_detail_item_atten);
                holder1.content = (TextView) convertView.findViewById(R.id.consult_detail_item_content);
                holder1.time = (TextView) convertView.findViewById(R.id.consult_detail_item_time);
                holder1.reply = (TextView) convertView.findViewById(R.id.consult_detail_item_reply);
                convertView.setTag(holder1);
            } else {
                holder1 = (ViewHolder1) convertView.getTag();
            }
        }
        if (type == type0) {
            holder.webView.getSettings().setDefaultTextEncodingName("utf-8");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                holder.webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            } else {
                holder.webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            }
            holder.webView.loadUrl(Constant.ROOT_PATH + "/share/news?newsId=" + id);

        } else {
            bitmapUtils.display(holder1.icon,entities.get(position-1).publishUserIcon);
            holder1.name.setText(entities.get(position - 1).publishUserName);
            holder1.atten.setText(entities.get(position - 1).praiseNum + "");
            holder1.content.setText(entities.get(position-1).content);
            holder1.time.setText(entities.get(position-1).createDate);
            holder1.atten.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
        return convertView;
    }


    class ViewHolder {
        public WebView webView;
        public TextView atten;
        public TextView read;
        public TextView report;
        public TextView message;
    }

    class ViewHolder1 {
        public CircleImageView icon;
        public TextView name;
        public TextView atten;
        public TextView content;
        public TextView time;
        public TextView reply;
    }
}
