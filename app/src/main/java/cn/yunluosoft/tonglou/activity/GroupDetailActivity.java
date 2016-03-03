package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.GroupDetailAdapter;
import cn.yunluosoft.tonglou.adapter.GroupDetailGridViewAdapter;
import cn.yunluosoft.tonglou.dialog.DiscussDialog;
import cn.yunluosoft.tonglou.model.CommentState;
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.model.GroupDetailState;
import cn.yunluosoft.tonglou.model.MessageInfo;
import cn.yunluosoft.tonglou.model.ReplayEntity;
import cn.yunluosoft.tonglou.model.ReplayState;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.model.ReturnUserState;
import cn.yunluosoft.tonglou.model.User;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.TimeUtils;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CircleImageView;
import cn.yunluosoft.tonglou.view.CustomListView;
import cn.yunluosoft.tonglou.view.MyGridView;

/**
 * Created by Administrator on 2016/2/7.
 */
public class GroupDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;

    private TextView title;

    private ImageView share;

    private GroupDetailGridViewAdapter gridAdapter;

    private CustomListView customListView;

    private View pro;

    private View sendView;

    private EditText sendEdit;

    private TextView send;

    private GroupDetailAdapter adapter;

    private String id;

    private FloorSpeechEntity entity;

    private BitmapUtils bitmapUtils;

    private int pageNo = 1;

    private boolean proShow = true;

    public boolean isFirst = true;

    private List<ReplayEntity> entities;

    private DiscussDialog discussDialog;

    private int flagIndex=-1;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1002:
                    AddPraise();
                    break;
                case 1005:
                    flagIndex=msg.arg1;
                    if (flagIndex==-1){
                        sendEdit.setHint("请输入评论内容");
                    }else{
                        sendEdit.setHint("回复"+entities.get(flagIndex).publishUserNickname);
                    }
                    imm.showSoftInput(sendEdit, InputMethodManager.SHOW_FORCED);
//                    if (discussDialog==null){
//                        discussDialog=new DiscussDialog(GroupDetailActivity.this,handler,-1,"");
//                    }

                    break;

                case 1009:
                    int position1=msg.arg1;
                    delComment(position1);
                    break;

                case 1112:
                    if ("0".equals(entity.isInGroup)){
                        Intent intent = new Intent(GroupDetailActivity.this,
                                ChatActivity.class);
                        MessageInfo messageInfo=new MessageInfo();
                        messageInfo.receiverHeadUrl=entity.id;
                        messageInfo.receiverImUserName=entity.imGroupId;
                        messageInfo.receiverNickName=entity.groupName;
                        messageInfo.receiverUserId=entity.imGroupId;
                        intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("info", messageInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        AddJoin();
                    }

                    break;

                case DiscussDialog.DISCUSS_OK:
                    int position=msg.arg1;
                    String temp= (String) msg.obj;
                    sendComment(position,temp);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupdetail);
        id=getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        share= (ImageView) findViewById(R.id.title_share);
        customListView= (CustomListView) findViewById(R.id.groupdetail_list);
        sendView= findViewById(R.id.groupdetail_sendview);
        sendEdit= (EditText) findViewById(R.id.groupdetail_edit);
        send= (TextView) findViewById(R.id.groupdetail_send);
        pro=findViewById(R.id.groupdetail_pro);

        bitmapUtils=new BitmapUtils(this);
        share.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        title.setText("活动详情");
        share.setOnClickListener(this);
        send.setOnClickListener(this);

        customListView.setOnLoadListener(new CustomListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                // pageNo++;
                closePro();
                getDynamic(pageNo + 1);
            }
        });
        entities=new ArrayList<>();


        getInfo();

    }
    public void closePro() {
        proShow = false;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.title_share:

                break;

            case R.id.groupdetail_replay:

                break;

            case R.id.groupdetail_send:
                if (ToosUtils.isTextEmpty(sendEdit)){
                ToastUtils.displayShortToast(GroupDetailActivity.this,"内容不能为空！");
                return;
            }
                sendComment(flagIndex,ToosUtils.getTextContent(sendEdit));
                break;

        }
    }


    // 获取信息
    private void getInfo() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("dynamicId",id);
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH + "/v1_1_0/dynamic/dynamicDetail", rp,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        pro.setVisibility(View.VISIBLE);
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        pro.setVisibility(View.GONE);
                        ToastUtils.displayFailureToast(GroupDetailActivity.this);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        pro.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        try {
                            ReturnState state = gson.fromJson(arg0.result,
                                    ReturnState.class);
                            LogManager.LogShow("----", arg0.result,
                                    LogManager.ERROR);
                            if (Constant.RETURN_OK.equals(state.msg)) {
                                GroupDetailState state1 = gson.fromJson(arg0.result, GroupDetailState.class);
                                entity = state1.result;
                                adapter = new GroupDetailAdapter(GroupDetailActivity.this, entities,entity, handler);
                                customListView.setAdapter(adapter);
                                getDynamic(1);
                            } else {
                                ToastUtils.displayShortToast(
                                        GroupDetailActivity.this,
                                        (String) state.result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils
                                    .displaySendFailureToast(GroupDetailActivity.this);
                        }

                    }

                });

    }

    /**
     * 查询动态评论或回复
     */
    private void getDynamic(final int page) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("dynamicId",id);
        rp.addBodyParameter("pageNo", String.valueOf(page));
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        String url="/v1_1_0/dynamicComment/findComment";
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH
                + url, rp, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (proShow) {
                    pro.setVisibility(View.VISIBLE);
                } else {
                    pro.setVisibility(View.GONE);
                }

                super.onStart();
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                pro.setVisibility(View.GONE);
                ToastUtils.displayFailureToast(GroupDetailActivity.this);
                customListView.onRefreshComplete();
                customListView.onLoadMoreComplete();
                customListView.setCanLoadMore(false);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                pro.setVisibility(View.GONE);
                try {
                    Gson gson = new Gson();
                    LogManager.LogShow("----", arg0.result, LogManager.ERROR);
                    ReturnState allState = gson.fromJson(arg0.result,
                            ReturnState.class);
                    if (Constant.RETURN_OK.equals(allState.msg)) {
                        pageNo = page;
//                        ShareDataTool.savePageNo(HiGroupActivity.this, page);
                        if (page == 1) {
                            entities.clear();
                            adapter.notifyDataSetChanged();
                        }
                        if (allState.result == null
                                || ToosUtils.isStringEmpty(String
                                .valueOf(allState.result))) {
                            customListView.onRefreshComplete();
                            customListView.onLoadMoreComplete();
                            customListView.setCanLoadMore(false);
//                            if (pageNo == 1) {
//                                empty.setVisibility(View.VISIBLE);
//                                empty_image.setImageDrawable(getResources()
//                                        .getDrawable(R.drawable.empty_floor));
//                                empty_text.setText("没有楼语信息");
//                            } else {
//                                empty.setVisibility(View.GONE);
//                            }
                            // ToastUtils.displayShortToast(getActivity(),
                            // "无数据");
                            return;
                        }
                        ReplayState state = gson.fromJson(arg0.result,
                                ReplayState.class);
                        if (state.result == null || state.result.size() == 0) {
                            customListView.onRefreshComplete();
                            customListView.onLoadMoreComplete();
                            customListView.setCanLoadMore(false);
//                            if (pageNo == 1) {
//                                empty.setVisibility(View.VISIBLE);
//                                empty_image.setImageDrawable(getResources()
//                                        .getDrawable(R.drawable.empty_floor));
//                                empty_text.setText("没有楼语信息");
//                            } else {
//                                empty.setVisibility(View.GONE);
//                            }
                            // ToastUtils.displayShortToast(getActivity(),
                            // "无数据");
                        } else {
                            for (int i = 0; i < state.result.size(); i++) {
                                entities.add(state.result.get(i));
                            }
                            adapter.notifyDataSetChanged();
                            if (pageNo == 1) {
                                customListView.onRefreshComplete();
                                customListView.setSelection(0);
                            } else {
                                customListView.onRefreshComplete();
                                customListView.onLoadMoreComplete();
                            }
                            customListView.setCanLoadMore(true);
                        }

                    } else {
                        ReturnState state = gson.fromJson(arg0.result,
                                ReturnState.class);
                        if (Constant.TOKEN_ERR.equals(state.msg)) {
                            ToastUtils.displayShortToast(GroupDetailActivity.this,
                                    "验证错误，请重新登录");
                            ToosUtils.goReLogin(GroupDetailActivity.this);
                        } else {
                            ToastUtils.displayShortToast(GroupDetailActivity.this,
                                    (String) state.result);

                        }
                        customListView.onRefreshComplete();
                        customListView.onLoadMoreComplete();
                        customListView.setCanLoadMore(false);
                    }

//                    if (entities.size() == 0) {
//                        empty.setVisibility(View.VISIBLE);
//                        empty_image.setImageDrawable(getResources()
//                                .getDrawable(R.drawable.empty_floor));
//                        empty_text.setText("没有楼语信息");
//                    } else {
//                        empty.setVisibility(View.GONE);
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    customListView.onRefreshComplete();
                    customListView.onLoadMoreComplete();
                    customListView.setCanLoadMore(false);
                    ToastUtils.displaySendFailureToast(GroupDetailActivity.this);
//                    if (entities.size() == 0) {
//                        empty.setVisibility(View.VISIBLE);
//                        empty_image.setImageDrawable(getResources()
//                                .getDrawable(R.drawable.empty_floor));
//                        empty_text.setText("没有楼语信息");
//                    } else {
//                        empty.setVisibility(View.GONE);
//                    }
                }

            }
        });

    }


    /**
     * 添加或者取消点赞
     * flag 0 代表添加点赞 1代表取消点赞
     */
    private void AddPraise() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("dynamicId", entity.id);
        String url="/v1_1_0/dynamic/praise";
        if ("0".equals(entity.isPraise)){
            url="/v1_1_0/dynamic/cancelPraise";
        }else{
            url="/v1_1_0/dynamic/praise";
        }
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
                        ToastUtils.displayFailureToast(GroupDetailActivity.this);
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
                                ToastUtils.displayShortToast(GroupDetailActivity.this,
                                        "操作成功");
                                if ("0".equals(entity.isPraise)){
                                    entity.isPraise=1+"";

                                    for (int i=0;i<entity.praiseUser.size();i++){
                                        if (entity.praiseUser.get(i).id.equals( ShareDataTool.getUserId(GroupDetailActivity.this))){
                                            entity.praiseUser.remove(i);
                                            break;
                                        }
                                    }
                                }else{
                                    entity.isPraise=0+"";
                                    User user=new User();
                                    user.id=ShareDataTool.getUserId(GroupDetailActivity.this);
                                    user.icon=ShareDataTool.getIcon(GroupDetailActivity.this);
                                    entity.praiseUser.add(user);

                                }
                                adapter.notifyDataSetChanged();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        GroupDetailActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(GroupDetailActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        GroupDetailActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(GroupDetailActivity.this);
                        }

                    }
                });

    }


    /**
     *参加活动

     */
    private void AddJoin() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("dynamicId", entity.id);
        String url="/v1_1_0/dynamic/joinActivity";
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
                        ToastUtils.displayFailureToast(GroupDetailActivity.this);
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
                                ToastUtils.displayShortToast(GroupDetailActivity.this,
                                        "操作成功");
                                entity.isInGroup=0+"";
                                entity.groupNum=String.valueOf(Integer.valueOf(entity.groupNum)+1);
                                adapter.notifyDataSetChanged();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        GroupDetailActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(GroupDetailActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        GroupDetailActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(GroupDetailActivity.this);
                        }

                    }
                });

    }


    /**
     *  动态评论/回复保存
     */
    private void sendComment(final int position, final String temp) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        if (position==-1){
            rp.addBodyParameter("parentId", entity.id);
            rp.addBodyParameter("targetUserId",entity.publishUserId);
        }else{
            rp.addBodyParameter("parentId", entities.get(position).parentId);
            rp.addBodyParameter("targetUserId",entities.get(position).publishUserId);

        }
        rp.addBodyParameter("dynamicId",entity.id);
        rp.addBodyParameter("content",temp);
        String url="/v1_1_0/dynamicComment/save";
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
                        ToastUtils.displayFailureToast(GroupDetailActivity.this);
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
                                ToastUtils.displayShortToast(GroupDetailActivity.this,
                                        "操作成功");
                                CommentState commentState=gson.fromJson(arg0.result,CommentState.class);
                                if (position==-1){
                                    ReplayEntity replayEntity=new ReplayEntity();
                                    replayEntity.id=commentState.result.commentId;
                                    replayEntity.content=temp;
                                    replayEntity.parentId="-1";
                                    replayEntity.publishUserIcon=ShareDataTool.getIcon(GroupDetailActivity.this);
                                    replayEntity.publishUserId=ShareDataTool.getUserId(GroupDetailActivity.this);
                                    replayEntity.publishUserNickname=ShareDataTool.getNickname(GroupDetailActivity.this);
                                    replayEntity.createDate= TimeUtils.getDate();
                                    entities.add(0, replayEntity);
                                }else{
                                    ReplayEntity replayEntity=new ReplayEntity();
                                    replayEntity.id=commentState.result.commentId;
                                    replayEntity.content=temp;
                                    replayEntity.parentId="-1";
                                    replayEntity.publishUserIcon=ShareDataTool.getIcon(GroupDetailActivity.this);
                                    replayEntity.publishUserId = ShareDataTool.getUserId(GroupDetailActivity.this);
                                    replayEntity.publishUserNickname = ShareDataTool.getNickname(GroupDetailActivity.this);
                                    replayEntity.targetUserId=entities.get(position).publishUserId;
                                    replayEntity.targetUserNickname=entities.get(position).publishUserNickname;
                                    replayEntity.createDate= TimeUtils.getDate();
                                    entities.add(position + 1, replayEntity);
                                }
                                flagIndex=-1;
                                sendEdit.setHint("请输入评论内容");
                                sendEdit.setText("");
                                imm.hideSoftInputFromWindow(sendEdit.getWindowToken(), 0);
                                adapter.notifyDataSetChanged();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        GroupDetailActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(GroupDetailActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        GroupDetailActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(GroupDetailActivity.this);
                        }

                    }
                });

    }


    /**
     *  删除评论
     */
    private void delComment(final int position) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
            rp.addBodyParameter("commentId", entities.get(position).id);
        String url="/v1_1_0/dynamicComment/delComment";
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
                        ToastUtils.displayFailureToast(GroupDetailActivity.this);
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
                                ToastUtils.displayShortToast(GroupDetailActivity.this,
                                        "操作成功");
                               entities.remove(position);
                                adapter.notifyDataSetChanged();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        GroupDetailActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(GroupDetailActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        GroupDetailActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(GroupDetailActivity.this);
                        }

                    }
                });

    }

}
