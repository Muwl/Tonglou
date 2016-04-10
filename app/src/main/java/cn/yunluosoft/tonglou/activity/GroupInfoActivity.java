package cn.yunluosoft.tonglou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.GroupInfoAdapter;
import cn.yunluosoft.tonglou.dialog.CustomeDialog;
import cn.yunluosoft.tonglou.easemob.chatuidemo.db.InviteMessgeDao;
import cn.yunluosoft.tonglou.model.GroupInfoEntity;
import cn.yunluosoft.tonglou.model.GroupInfoState;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.MyApplication;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.MyGridView;

/**
 * Created by Administrator on 2016/3/1.
 */
public class GroupInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;

    private TextView title;

    private MyGridView myGridView;

    private ToggleButton toggleButton;

    private View report;

    private TextView exit;

    private View pro;

    private GroupInfoAdapter adapter;

    private List<GroupInfoEntity> entities;

    private int width;

    private String id;

    private String groupId;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 40:
                    exitGroup();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupinfo);
        initView();

    }

    private void initView() {
        id=getIntent().getStringExtra("id");
        groupId=getIntent().getStringExtra("groupId");
        width= DensityUtil.getScreenWidth(this);
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        myGridView= (MyGridView) findViewById(R.id.groupinfo_gridview);
        toggleButton= (ToggleButton) findViewById(R.id.groupinfo_toggle);
        report=findViewById(R.id.groupinfo_report);
        exit= (TextView) findViewById(R.id.groupinfo_exit);
        pro=findViewById(R.id.groupinfo_pro);

        entities=new ArrayList<>();
        adapter=new GroupInfoAdapter(this,entities,width);
        back.setOnClickListener(this);
        title.setText("群信息");
        report.setOnClickListener(this);
        exit.setOnClickListener(this);
        toggleButton.setOnClickListener(this);
        myGridView.setAdapter(adapter);
        getInfo();
        updateGroup();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;

            case R.id.groupinfo_report:
                Intent intent=new Intent(GroupInfoActivity.this,ReportActivity.class);
                intent.putExtra("deyid",id);
                intent.putExtra("flag",3);
                startActivity(intent);

                break;

            case R.id.groupinfo_exit:
                CustomeDialog dialog=new CustomeDialog(GroupInfoActivity.this,handler,"删除聊天群活动也会关闭，是否删除？",-1,-1,null);

                break;

            case R.id.groupinfo_toggle:
                if (toggleButton.isChecked()) {
                    try {
                        EMGroupManager.getInstance().unblockGroupMessage(
                                groupId);

                    } catch (EaseMobException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    try {
                        EMGroupManager.getInstance()
                                .blockGroupMessage(groupId);
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    // 获取聊天记录是否屏蔽
    protected void updateGroup() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMGroup returnGroup = EMGroupManager.getInstance()
                            .getGroupFromServer(groupId);
                    // 更新本地数据
                    EMGroupManager.getInstance().createOrUpdateLocalGroup(
                            returnGroup);
                    final EMGroup group = EMGroupManager.getInstance()
                            .getGroup(groupId);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            LogManager.LogShow("-----","group msg is blocked:"
                                    + group.getMsgBlocked(),LogManager.ERROR);
                            // update block
                            System.out.println("group msg is blocked:"
                                    + group.getMsgBlocked());
                            if (group.getMsgBlocked()) {
                                toggleButton.setChecked(false);
                            } else {
                                toggleButton.setChecked(true);
                            }
                        }
                    });

                } catch (Exception e) {
                }
            }
        }).start();
    }

    /**
     *看群详情
     */
    private void getInfo() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        LogManager.LogShow("----------",id+"***********",LogManager.ERROR);
        rp.addBodyParameter("dynamicId",id);
        String url="/v1_1_0/dynamic/groupDetail";
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH + url,
                rp, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        pro.setVisibility(View.VISIBLE);
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        pro.setVisibility(View.GONE);
                        ToastUtils.displayFailureToast(GroupInfoActivity.this);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        pro.setVisibility(View.GONE);
                        try {
                            // Gson gson = new Gson();
                            LogManager.LogShow("----", arg0.result,
                                    LogManager.ERROR);
                            Gson gson = new Gson();
                            ReturnState state = gson.fromJson(arg0.result,
                                    ReturnState.class);
                            if (Constant.RETURN_OK.equals(state.msg)) {
                                GroupInfoState state1 = gson.fromJson(arg0.result, GroupInfoState.class);
                                if (state1.result != null && state1.result.size() != 0) {
                                    for (int i = 0; i < state1.result.size(); i++) {
                                        entities.add(state1.result.get(i));
                                    }
                                }
                                LogManager.LogShow("--", gson.toJson(entities), LogManager.ERROR);
                                adapter.notifyDataSetChanged();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        GroupInfoActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(GroupInfoActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        GroupInfoActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(GroupInfoActivity.this);
                        }

                    }
                });

    }

    /**
     *看群详情
     */
    private void exitGroup() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        LogManager.LogShow("----------",id+"***********",LogManager.ERROR);
        rp.addBodyParameter("dynamicId",id);
        String url="/v1_1_0/dynamic/quitActivityGroup";
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH + url,
                rp, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        pro.setVisibility(View.VISIBLE);
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        pro.setVisibility(View.GONE);
                        ToastUtils.displayFailureToast(GroupInfoActivity.this);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        pro.setVisibility(View.GONE);
                        try {
                            // Gson gson = new Gson();
                            LogManager.LogShow("----", arg0.result,
                                    LogManager.ERROR);
                            Gson gson = new Gson();
                            ReturnState state = gson.fromJson(arg0.result,
                                    ReturnState.class);
                            if (Constant.RETURN_OK.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        GroupInfoActivity.this,
                                        String.valueOf(state.result));
                                List<Activity> activities = MyApplication.getInstance().getActivities();
                                delGroupMes(groupId);
                                try {
                                    if (activities != null && activities.size() != 0) {
                                        for (int i = 0; i < activities.size(); i++) {
                                            if (activities.get(i).getClass().getName().equals(ChatActivity.class.getName())) {
                                                activities.get(i).finish();
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                finish();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        GroupInfoActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(GroupInfoActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        GroupInfoActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(GroupInfoActivity.this);
                        }

                    }
                });

    }


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


    /**
     * 删除群聊信息
     * @param
     * @return
     */
    public void delGroupMes(String groppId) {

        List<EMConversation> conversations=loadConversationsWithRecentChat();
        for (int i=0;i<conversations.size();i++){
            // 获取用户username或者群组groupid
            String username = conversations.get(i).getUserName();
                if (username.equals(groppId)) {
                    EMChatManager.getInstance().deleteConversation(
                            conversations.get(i).getUserName(),
                            conversations.get(i).isGroup(), true);
                    InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(GroupInfoActivity.this);
                    inviteMessgeDao.deleteMessage(conversations.get(i).getUserName());
                    break;
                }


        }


    }
}
