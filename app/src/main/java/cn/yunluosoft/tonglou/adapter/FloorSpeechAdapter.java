package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

import java.util.Date;
import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.easemob.chatuidemo.Constant;
import cn.yunluosoft.tonglou.easemob.chatuidemo.utils.DateUtils;
import cn.yunluosoft.tonglou.easemob.chatuidemo.utils.SmileUtils;
import cn.yunluosoft.tonglou.model.ConsultEntity;
import cn.yunluosoft.tonglou.model.MessageInfo;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.TimeUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Mu on 2016/1/27.
 */
public class FloorSpeechAdapter extends BaseAdapter {

    private Context context;
    private List<EMConversation> entities;
    private Gson gson;
    private BitmapUtils bitmapUtils;
    private List<ConsultEntity> consultEntities;
    public FloorSpeechAdapter(Context context, List<EMConversation> entities,List<ConsultEntity> consultEntities) {
        this.context = context;
        this.entities = entities;
        this.consultEntities=consultEntities;
        gson = new Gson();
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        if (consultEntities==null || consultEntities.size()==0) {
            return entities.size();
        }else{
            return  entities.size()+1;
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.ffloorspeech_item, null);
            holder.icon= (CircleImageView) convertView.findViewById(R.id.ffloorspeech_item_icon);
            holder.name= (TextView) convertView.findViewById(R.id.ffloorspeech_item_name);
            holder.time= (TextView) convertView.findViewById(R.id.ffloorspeech_item_time);
            holder.content= (TextView) convertView.findViewById(R.id.ffloorspeech_item_content);
            holder.num = (TextView) convertView
                    .findViewById(R.id.floorspeech_item_number);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        if ( consultEntities.size()!=0){
            if (position==0){
                ConsultEntity consultEntity=consultEntities.get(consultEntities.size() - 1);
                holder.name.setText("资讯信息");
                holder.icon.setImageResource(R.mipmap.message_consult);
                holder.time.setText(DateUtils.getTimestampString(TimeUtils.getDateByStr2(consultEntity.createDate)));
                holder.content.setText(
                        SmileUtils.getSmiledText(context,consultEntity.news.get(consultEntity.news.size()-1).topic),
                        TextView.BufferType.SPANNABLE);
            }else{
                EMConversation conversation = entities.get(position-1);
                // 获取用户username或者群组groupid
                String username = conversation.getUserName();
                List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
                EMGroup contact = null;
                EMMessage lastMessage = null;
                MessageInfo messageInfo = null;
                if (conversation.getMsgCount() != 0) {
                    // 把最后一条消息的内容作为item的message内容
                    lastMessage = conversation.getLastMessage();
                    String temp = null;
                    try {
                        messageInfo = ToosUtils.getMessageInfo(lastMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    holder.time.setText(DateUtils.getTimestampString(new Date(
                            lastMessage.getMsgTime())));

                    holder.content.setText(
                            SmileUtils.getSmiledText(context,
                                    getMessageDigest(lastMessage, (context))),
                            TextView.BufferType.SPANNABLE);
                }

                if (conversation.getUnreadMsgCount() > 0) {
                    // 显示与此用户的消息未读数
                    holder.num
                            .setText(String.valueOf(conversation.getUnreadMsgCount()));
                    holder.num.setVisibility(View.VISIBLE);
                } else {
                    holder.num.setVisibility(View.INVISIBLE);
                }

                if (messageInfo != null) {
                    if (!getGroupFlag(conversation)){
                        if (lastMessage.direct == EMMessage.Direct.SEND) {
                            holder.name.setText(messageInfo.receiverNickName);
                            bitmapUtils.display(holder.icon, messageInfo.receiverHeadUrl);
                        } else {
                            holder.name.setText(messageInfo.senderNickName);
                            bitmapUtils.display(holder.icon, messageInfo.senderHeadUrl);
                        }}else{
                        holder.name.setText(messageInfo.receiverNickName);
                        holder.icon.setImageResource(R.mipmap.ic_launcher);
                    }
                }
            }

        }else {
            EMConversation conversation = entities.get(position);
            // 获取用户username或者群组groupid
            String username = conversation.getUserName();
            List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
            EMGroup contact = null;
            EMMessage lastMessage = null;
            MessageInfo messageInfo = null;
            if (conversation.getMsgCount() != 0) {
                // 把最后一条消息的内容作为item的message内容
                lastMessage = conversation.getLastMessage();
                String temp = null;
                try {
                    messageInfo = ToosUtils.getMessageInfo(lastMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                holder.time.setText(DateUtils.getTimestampString(new Date(
                        lastMessage.getMsgTime())));

                holder.content.setText(
                        SmileUtils.getSmiledText(context,
                                getMessageDigest(lastMessage, (context))),
                        TextView.BufferType.SPANNABLE);
            }

            if (conversation.getUnreadMsgCount() > 0) {
                // 显示与此用户的消息未读数
                holder.num
                        .setText(String.valueOf(conversation.getUnreadMsgCount()));
                holder.num.setVisibility(View.VISIBLE);
            } else {
                holder.num.setVisibility(View.INVISIBLE);
            }

            if (messageInfo != null) {
                if (!getGroupFlag(conversation)) {
                    if (lastMessage.direct == EMMessage.Direct.SEND) {
                        holder.name.setText(messageInfo.receiverNickName);
                        bitmapUtils.display(holder.icon, messageInfo.receiverHeadUrl);
                    } else {
                        holder.name.setText(messageInfo.senderNickName);
                        bitmapUtils.display(holder.icon, messageInfo.senderHeadUrl);
                    }
                } else {
                    holder.name.setText(messageInfo.receiverNickName);
                    holder.icon.setImageResource(R.mipmap.ic_launcher);
                }
            }
        }
        return convertView;
    }

    class ViewHolder{
        public CircleImageView icon;
        public TextView name;
        public TextView time;
        public TextView content;
        public TextView num;
    }

    /**
     * 判断某一行是否是群聊
     *
     * @param position
     * @return
     */
    public boolean getGroupFlag(int position) {
        EMConversation conversation = entities.get(position);
        // 获取用户username或者群组groupid
        String username = conversation.getUserName();
        List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
        EMContact contact = null;
        EMMessage lastMessage = null;
        boolean isGroup = false;
        LogManager.LogShow("-----",groups.size()+"----"+username,LogManager.ERROR);
        for (EMGroup group : groups) {
           // LogManager.LogShow("-----",groups.get(position).getGroupId()+"----"+username,LogManager.ERROR);
            if (group.getGroupId().equals(username)) {
                isGroup = true;
                contact = group;
                break;
            }
        }
        return isGroup;
    }

    /**
     * 判断某一行是否是群聊
     *
     * @param position
     * @return
     */
    public boolean getGroupFlag(EMConversation conversation) {
        // 获取用户username或者群组groupid
        String username = conversation.getUserName();
        List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
        EMContact contact = null;
        EMMessage lastMessage = null;
        boolean isGroup = false;
        LogManager.LogShow("-----",groups.size()+"----"+username,LogManager.ERROR);
        for (EMGroup group : groups) {
            if (group.getGroupId().equals(username)) {
                isGroup = true;
                contact = group;
                break;
            }
        }
        return isGroup;
    }

    /**
     * 获取某行的info
     *
     * @param position
     * @return
     */
    public MessageInfo getInfo(int position) {
        EMConversation conversation = entities.get(position);
        // 获取用户username或者群组groupid
        String username = conversation.getUserName();
        List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
        EMContact contact = null;
        EMMessage lastMessage = null;
        MessageInfo info = null;
        if (conversation.getMsgCount() != 0) {
            // 把最后一条消息的内容作为item的message内容
            lastMessage = conversation.getLastMessage();
            String temp;
            try {
                // // if (lastMessage.direct == EMMessage.Direct.RECEIVE) {
                // temp = lastMessage.getStringAttribute("sendInfo");
                // } else {
                // temp = lastMessage.getStringAttribute("info");
                // }
                // info = gson.fromJson(temp, Info.class);
                info = ToosUtils.getMessageInfo(lastMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return info;
    }

    /**
     * 0代表发送 1代表接受
     *
     * @param position
     * @return
     */
    public int getDic(int position) {
        EMConversation conversation = entities.get(position);
        // 获取用户username或者群组groupid
        String username = conversation.getUserName();
        List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
        EMContact contact = null;
        EMMessage lastMessage = null;
        // MessageInfo info = null;
        int m = 0;
        if (conversation.getMsgCount() != 0) {
            // 把最后一条消息的内容作为item的message内容
            lastMessage = conversation.getLastMessage();
            String temp;
            try {
                if (lastMessage.direct == EMMessage.Direct.RECEIVE) {
                    m = 1;
                } else {
                    m = 0;
                }
                // info = gson.fromJson(temp, Info.class);
                // info = ToosUtils.getMessageInfo(lastMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return m;
    }

    /**
     * 根据消息内容和消息类型获取消息内容提示
     *
     * @param message
     * @param context
     * @return
     */
    private String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        switch (message.getType()) {
            case LOCATION: // 位置消息
                if (message.direct == EMMessage.Direct.RECEIVE) {
                    // 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_recv");
                    digest = getStrng(context, R.string.location_recv);
                    digest = String.format(digest, message.getFrom());
                    return digest;
                } else {
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_prefix");
                    digest = getStrng(context, R.string.location_prefix);
                }
                break;
            case IMAGE: // 图片消息
                ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
                digest = getStrng(context, R.string.picture);
                break;
            case VOICE:// 语音消息
                digest = getStrng(context, R.string.voice);
                break;
            case VIDEO: // 视频消息
                digest = getStrng(context, R.string.video);
                break;
            case TXT: // 文本消息
                if (!message.getBooleanAttribute(
                        Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = txtBody.getMessage();
                } else {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = getStrng(context, R.string.voice_call)
                            + txtBody.getMessage();
                }
                break;
            case FILE: // 普通文件消息
                digest = getStrng(context, R.string.file);
                break;
            default:
                System.err.println("error, unknow type");
                return "";
        }

        return digest;
    }

    String getStrng(Context context, int resId) {
        return context.getResources().getString(resId);
    }
}
