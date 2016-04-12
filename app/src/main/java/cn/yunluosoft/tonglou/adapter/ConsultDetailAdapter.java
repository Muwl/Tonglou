package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.ConsultDetailActivity;
import cn.yunluosoft.tonglou.model.ConsultDetailEntity;
import cn.yunluosoft.tonglou.model.ConsultInfoEntity;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ToosUtils;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
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
            holder.read.setText(entity.readNum);
            holder.atten.setText(entity.praiseNum);
            if(Constant.PRAISE_OK.equals(entity.isPraise)){
                holder.atten.setTextColor(Color.parseColor("#499EB8"));
                Drawable drawable=context.getResources().getDrawable(R.mipmap.consult_atten_checked);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.atten.setCompoundDrawables(drawable, null, null, null);
            }else{
                holder.atten.setTextColor(Color.parseColor("#B3B3B3"));
                Drawable drawable=context.getResources().getDrawable(R.mipmap.consult_atten);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.atten.setCompoundDrawables(drawable, null, null, null);
            }
            holder.atten.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (ToosUtils.CheckComInfo(context)) {
                                                        Message message = new Message();
                                                        message.what = ConsultDetailActivity.CONSULT_ATTEN;
                                                        message.obj = position;
                                                        handler.sendMessage(message);
                                                    }
                                                }
                                            }

            );

                holder.report.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         if (ToosUtils.CheckComInfo(context)) {
                                                             handler.sendEmptyMessage(ConsultDetailActivity.CONSULT_REPORT);
                                                         }
                                                     }
                                                 }

                );

                    holder.message.setOnClickListener(new View.OnClickListener()

                    {
                        @Override
                        public void onClick(View v) {
                            if (ToosUtils.CheckComInfo(context)) {
                                Message message = new Message();
                                message.what = ConsultDetailActivity.CONSULT_COMMENT;
                                message.obj = -1;
                                handler.sendMessage(message);
                            }

                            }
                        }

                        );

                    }else {
            bitmapUtils.display(holder1.icon, entities.get(position - 1).publishUserIcon);
            if ("0".equals(entities.get(position - 1).type)){
                holder1.name.setText(entities.get(position - 1).publishUserName);
            }else{
                holder1.name.setText(Html.fromHtml(entities.get(position - 1).publishUserName + "\u2000<font color=\"#0076FF\">回复</font>\u2000" + entities.get(position - 1).targetUserName));
            }

            if(Constant.PRAISE_OK.equals(entities.get(position - 1).isPraise)){
                holder1.atten.setTextColor(Color.parseColor("#499EB8"));
                Drawable drawable=context.getResources().getDrawable(R.mipmap.consult_atten_checked);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder1.atten.setCompoundDrawables(drawable, null, null, null);
            }else{
                holder1.atten.setTextColor(Color.parseColor("#B3B3B3"));
                Drawable drawable=context.getResources().getDrawable(R.mipmap.consult_atten);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder1.atten.setCompoundDrawables(drawable, null, null, null);
            }
            if (ToosUtils.isStringEmpty(entities.get(position - 1).praiseNum)){
                entities.get(position - 1).praiseNum="0";
            }
            holder1.atten.setText(entities.get(position - 1).praiseNum + "");
            holder1.content.setText(entities.get(position - 1).content);
            holder1.time.setText(entities.get(position - 1).createDate);
            holder1.atten.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ToosUtils.CheckComInfo(context)) {
                        Message message = new Message();
                        message.what = ConsultDetailActivity.CONSULT_ATTEN;
                        message.obj = position;
                        handler.sendMessage(message);
                    }
                }
            });

            holder1.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ToosUtils.CheckComInfo(context)) {
                        Message message = new Message();
                        message.what = ConsultDetailActivity.CONSULT_COMMENT;
                        message.obj = position - 1;
                        handler.sendMessage(message);
                    }
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
