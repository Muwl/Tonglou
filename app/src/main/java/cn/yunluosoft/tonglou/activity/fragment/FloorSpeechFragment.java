package cn.yunluosoft.tonglou.activity.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.ChatActivity;
import cn.yunluosoft.tonglou.activity.ConsultActivity;
import cn.yunluosoft.tonglou.adapter.FloorSpeechAdapter;
import cn.yunluosoft.tonglou.dialog.CustomeDialog;
import cn.yunluosoft.tonglou.easemob.chatuidemo.db.InviteMessgeDao;
import cn.yunluosoft.tonglou.model.ConsultEntity;
import cn.yunluosoft.tonglou.model.MessageInfo;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.NewsDBUtils;
import cn.yunluosoft.tonglou.view.swipelist.SwipeMenu;
import cn.yunluosoft.tonglou.view.swipelist.SwipeMenuListView;

/**
 * Created by Mu on 2016/1/25.
 */
public class FloorSpeechFragment extends Fragment {

    private TextView title;

    private ImageView back;

    private SwipeMenuListView listView;

    private View pro;

    private FloorSpeechAdapter adapter;

    private List<EMConversation> entities;

    private InviteMessgeDao dao;

    private View empty;

    private ImageView empty_image;

    private TextView empty_text;

    private NewsDBUtils newsDBUtils;

    private List<ConsultEntity> consultEntities;

    private static  Context context;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 40:
                    int position = msg.arg1;
                    if (consultEntities.size()!=0 && position==0){
                        newsDBUtils.removeAllNews();
                        consultEntities.clear();
                        adapter.notifyDataSetChanged();
                        reFushEmpty();
                    }else{
                        if (consultEntities.size()!=0){
                            delete(position-1);
                        }else{
                            delete(position);
                        }
                    }

                    break;

                default:
                    break;
            }
        };
    };

    public static  FloorSpeechFragment getInstance(Context context) {
        FloorSpeechFragment fragment = new FloorSpeechFragment() ;
        FloorSpeechFragment.context = context;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ffloorspeech, container, false);
        title= (TextView) view.findViewById(R.id.title_title);
        view.findViewById(R.id.title_back).setVisibility(View.GONE);
        listView= (SwipeMenuListView) view.findViewById(R.id.ffloorspeech_list);
        pro=view.findViewById(R.id.ffloorspeech_pro);
        empty = view.findViewById(R.id.ffloorspeech_empty);
        empty_image = (ImageView) view.findViewById(R.id.empty_image);
        empty_text = (TextView) view.findViewById(R.id.empty_text);
        title.setText("让整栋楼听到你的声音");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dao = new InviteMessgeDao(context);
        newsDBUtils=new NewsDBUtils(context);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        delete(position);
                        break;
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int tempPoi=position;
                if (consultEntities.size()>0){
                    if (position==0){
                        Intent intent=new Intent(context,ConsultActivity.class);
                        startActivity(intent);
                        return;
                    }
                    tempPoi=position-1;
                }
                Intent intent = new Intent(context, ChatActivity.class);
                MessageInfo adapterInfo = adapter.getInfo(tempPoi);
                int m = adapter.getDic(tempPoi);
                if (adapter.getGroupFlag(tempPoi)) {
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("info", adapterInfo);
                    intent.putExtras(bundle);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("info", adapterInfo);
                    bundle.putInt("dic", m);
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                CustomeDialog customeDialog = new CustomeDialog(context,
                        handler, "确定删除？", position, -1,null);
                // delete(position);
                return true;
            }
        });
    }

    public void onResume() {
        super.onResume();
        entities = new ArrayList<EMConversation>();
        List<EMConversation> conversations = loadConversationsWithRecentChat();
        if (conversations != null && conversations.size() != 0) {
            for (int i = 0; i < conversations.size(); i++) {
                entities.add(conversations.get(i));
            }
        }

        consultEntities=newsDBUtils.getAllConsultEntities();
        if (consultEntities==null){
            consultEntities=new ArrayList<>();
        }
        adapter = new FloorSpeechAdapter(context, entities,consultEntities);
        listView.setAdapter(adapter);
        reFushEmpty();
    }

    /**
     * 删除对话
     *
     * @param position
     */
    private void delete(int position) {
        // delete app
        // if (entity.flag == 1) {
        // 删除此会话
        EMChatManager.getInstance().deleteConversation(
                entities.get(position).getUserName(),
                entities.get(position).isGroup(), true);
        InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(context);
        inviteMessgeDao.deleteMessage(entities.get(position).getUserName());
        entities.remove(position);
        adapter.notifyDataSetChanged();
        reFushEmpty();
        // }

    }

    public void reFushEmpty() {
        if (entities == null || entities.size() == 0 && consultEntities.size()==0) {
            empty.setVisibility(View.VISIBLE);
            empty_image.setImageDrawable(getResources().getDrawable(
                    R.mipmap.empty_mes));
            empty_text.setText("没有消息");
        } else {
            empty.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新界面
     */
    public void refush() {
        entities.clear();
        List<EMConversation> conversations = loadConversationsWithRecentChat();
        if (conversations != null && conversations.size() != 0) {
            for (int i = 0; i < conversations.size(); i++) {
                entities.add(conversations.get(i));

            }
        }
        consultEntities.clear();
        List<ConsultEntity> tempEntities=newsDBUtils.getAllConsultEntities();
        if (tempEntities==null){
            tempEntities=new ArrayList<>();
        }
        for (ConsultEntity consultEntity:tempEntities){
            consultEntities.add(consultEntity);
        }
        adapter.notifyDataSetChanged();
        reFushEmpty();
    }

//    /**
//     * 获取所有会话
//     *
//     * @param context
//     * @return +
//     */
//    private List<EMConversation> loadConversationsWithRecentChat() {
//        // 获取所有会话，包括陌生人
//        Hashtable<String, EMConversation> conversations = EMChatManager
//                .getInstance().getAllConversations();
//        // 过滤掉messages size为0的conversation
//        /**
//         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
//         * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
//         */
//        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
//        synchronized (conversations) {
//            for (EMConversation conversation : conversations.values()) {
//                if (conversation.getAllMessages().size() != 0) {
//                    sortList.add(new Pair<Long, EMConversation>(conversation
//                            .getLastMessage().getMsgTime(), conversation));
//                }
//            }
//        }
//        try {
//            // Internal is TimSort algorithm, has bug
//            sortConversationByLastChatTime(sortList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        List<EMConversation> list = new ArrayList<EMConversation>();
//        for (Pair<Long, EMConversation> sortItem : sortList) {
//            if (sortItem.second.getLastMessage().getFrom()
//                    .equals(Constant.SYS_NAME)
//                    || sortItem.second.getLastMessage().getTo()
//                    .equals(Constant.SYS_NAME)
//                    || sortItem.second.getLastMessage().getFrom()
//                    .equals(Constant.SYS_GETNAME)
//                    || sortItem.second.getLastMessage().getFrom()
//                    .equals(Constant.SYS_GETNAME)) {
//                EMChatManager.getInstance().deleteConversation(
//                        sortItem.second.getUserName(),
//                        sortItem.second.isGroup(), true);
//                InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(
//                        context);
//                inviteMessgeDao.deleteMessage(sortItem.second.getUserName());
//                continue;
//            } else {
//                list.add(sortItem.second);
//            }
//        }
//        return list;
//    }

    /**
     * 获取所有会话
     *
     * @param context
     * @return +
     */
    private List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager
                .getInstance().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation
                            .getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }


    /**
     * 根据最后一条消息的时间排序
     *
     * @param usernames
     */
    private void sortConversationByLastChatTime(
            List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList,
                new Comparator<Pair<Long, EMConversation>>() {
                    @Override
                    public int compare(final Pair<Long, EMConversation> con1,
                                       final Pair<Long, EMConversation> con2) {

                        if (con1.first == con2.first) {
                            return 0;
                        } else if (con2.first > con1.first) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }

                });
    }


}
