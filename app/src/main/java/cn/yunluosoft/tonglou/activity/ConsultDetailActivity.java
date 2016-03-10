package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.ConstactAdapter;
import cn.yunluosoft.tonglou.adapter.ConsultDetailAdapter;
import cn.yunluosoft.tonglou.model.CommentState;
import cn.yunluosoft.tonglou.model.ConsultDetailEntity;
import cn.yunluosoft.tonglou.model.ConsultDetailReturnEntity;
import cn.yunluosoft.tonglou.model.ConsultDetailState;
import cn.yunluosoft.tonglou.model.ConsultInfoEntity;
import cn.yunluosoft.tonglou.model.ConsultInfoState;
import cn.yunluosoft.tonglou.model.FloorSpeechState;
import cn.yunluosoft.tonglou.model.ReplayEntity;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.model.User;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.TimeUtils;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CustomListView;

/**
 * Created by Mu on 2016/2/1.
 */
public class ConsultDetailActivity extends BaseActivity implements View.OnClickListener{

    public static final int  CONSULT_ATTEN=1001;

    public static final int CONSULT_REPORT=1002;

    public static final int CONSULT_COMMENT=1003;

    private ImageView back;

    private TextView title;

    private ImageView share;

    private CustomListView customListView;;

    private ConsultDetailAdapter adapter;

    private View pro;

    private String id;

    private ConsultInfoEntity entity;

    private EditText sendEdit;

    private TextView send;


    private int pageNo = 1;

    private boolean proShow = true;

    public boolean isFirst = true;

    private int flagIndex=-1;

    private List<ConsultDetailEntity> entities;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case CONSULT_ATTEN:
                   int position= (int) msg.obj;
                   if (position==0){
                       if (Constant.PRAISE_NO.equals(entity.isPraise)){
                           AddPraise();
                       }else{

                       }
                   }else{
                       if (Constant.PRAISE_NO.equals(entities.get(position - 1).isPraise)){
                           AddComPraise(position - 1);
                       }else{

                       }
                   }
                   break;
               case CONSULT_COMMENT:
                   flagIndex=msg.arg1;
                   if (flagIndex==-1){
                       sendEdit.setHint("请输入评论内容");
                   }else{
                       sendEdit.setHint("回复"+entities.get(flagIndex).publishUserName);
                   }
                   imm.showSoftInput(sendEdit, InputMethodManager.SHOW_FORCED);
                   break;

           }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consult_detail);
        id=getIntent().getStringExtra("id");
        initView();
        getInfo();
    }

    private void initView() {
        entities=new ArrayList<>();
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        share= (ImageView) findViewById(R.id.title_share);
        sendEdit= (EditText) findViewById(R.id.consult_detail_edit);
        send= (TextView) findViewById(R.id.consult_detail_send);
        customListView= (CustomListView) findViewById(R.id.consult_detail_list);
        pro=findViewById(R.id.consult_detail_pro);


        back.setOnClickListener(this);
        title.setText("资讯详情");
        share.setVisibility(View.VISIBLE);
        share.setOnClickListener(this);

        LogManager.LogShow("----", Constant.ROOT_PATH + "/share/news?newsId=" + id, LogManager.ERROR);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.title_share:
                break;

            case R.id.consult_detail_message:
                Intent intent=new Intent(ConsultDetailActivity.this,WriteMessageActivity.class);
                startActivity(intent);
                break;

            case R.id.groupdetail_send:
                if (ToosUtils.isTextEmpty(sendEdit)){
                    ToastUtils.displayShortToast(ConsultDetailActivity.this,"内容不能为空！");
                    return;
                }
                sendComment(flagIndex, ToosUtils.getTextContent(sendEdit));
                break;
        }
    }


    private void getInfo() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("newsId", id);
        String url="/v1_1_0/news/readNumAndPraiseNum";
        LogManager.LogShow("----",Constant.ROOT_PATH + "/v1_1_0/news/readNumAndPraiseNum?newsId=" + id+"&sign="+ShareDataTool.getToken(this),LogManager.ERROR);
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
                        ToastUtils.displayFailureToast(ConsultDetailActivity.this);
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
                                ConsultInfoState state1=gson.fromJson(arg0.result,ConsultInfoState.class);
                                entity=state1.result;
                                adapter = new ConsultDetailAdapter(ConsultDetailActivity.this,id,entity, entities, handler);
                                customListView.setAdapter(adapter);
                                customListView
                                        .setOnRefreshListener(new CustomListView.OnRefreshListener() {
                                            @Override
                                            public void onRefresh() {
                                                customListView.setCanLoadMore(false);
                                                getInfoList(1);
                                            }
                                        });
                                customListView
                                        .setOnLoadListener(new CustomListView.OnLoadMoreListener() {
                                            @Override
                                            public void onLoadMore() {
                                                getInfoList(pageNo + 1);
                                            }
                                        });
                                getInfoList(1);
                                customListView.setCanLoadMore(true);
                                customListView.setCanRefresh(true);

                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        ConsultDetailActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(ConsultDetailActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        ConsultDetailActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(ConsultDetailActivity.this);
                        }

                    }
                });

    }


    /**
     * 添加新闻点赞
     */
    private void AddPraise() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("Id", id);
        String url="/v1_1_0/news/praise";
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        LogManager.LogShow("----", Constant.ROOT_PATH + "/v1_1_0/news/praise?newsId=" + id + "&sign=" + ShareDataTool.getToken(this), LogManager.ERROR);
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
                        ToastUtils.displayFailureToast(ConsultDetailActivity.this);
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
                                ToastUtils.displayShortToast(ConsultDetailActivity.this,
                                        "操作成功");
                                entity.isPraise=Constant.PRAISE_OK;
                                entity.praiseNum=String.valueOf(Integer.valueOf(entity.praiseNum)+1);
                                adapter.notifyDataSetChanged();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        ConsultDetailActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(ConsultDetailActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        ConsultDetailActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(ConsultDetailActivity.this);
                        }

                    }
                });

    }
    /**
     * 添加评论点赞
     */
    private void AddComPraise(final int position) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("id", entities.get(position).id);
        String url="/v1_1_0/newsComment/praise";
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
                        ToastUtils.displayFailureToast(ConsultDetailActivity.this);
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
                                ToastUtils.displayShortToast(ConsultDetailActivity.this,
                                        "操作成功");
                                entities.get(position).isPraise=Constant.PRAISE_OK;
                                entities.get(position).praiseNum=String.valueOf(Integer.valueOf(entities.get(position).praiseNum)+1);
                                adapter.notifyDataSetChanged();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        ConsultDetailActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(ConsultDetailActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        ConsultDetailActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(ConsultDetailActivity.this);
                        }

                    }
                });

    }



    /**
     * 获取评论列表
     */
    private void getInfoList(final int page) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("newsId", id);
        rp.addBodyParameter("pageNo", String.valueOf(page));
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH
                + "/v1_1_0/newsComment/find", rp, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                pro.setVisibility(View.GONE);
                ToastUtils.displayFailureToast(ConsultDetailActivity.this);
                customListView.onRefreshComplete();
                customListView.onLoadMoreComplete();
                customListView.setCanLoadMore(false);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                pro.setVisibility(View.GONE);
                try {
                    if (arg0.result==null){
                        customListView.onRefreshComplete();
                        customListView.onLoadMoreComplete();
                        customListView.setCanLoadMore(false);
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    Gson gson = new Gson();
                    ReturnState allState = gson.fromJson(arg0.result,
                            ReturnState.class);
                    if (Constant.RETURN_OK.equals(allState.msg)) {
                        pageNo = page;
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
                            adapter.notifyDataSetChanged();
                            // ToastUtils.displayShortToast(
                            // MyFloorSpeechActivity.this, "无数据");
                            return;
                        }
                        ConsultDetailState state = gson.fromJson(arg0.result,
                                ConsultDetailState.class);
                        if (state.result == null || state.result.size() == 0) {
                            customListView.onRefreshComplete();
                            customListView.onLoadMoreComplete();
                            customListView.setCanLoadMore(false);
                            adapter.notifyDataSetChanged();
                            // ToastUtils.displayShortToast(
                            // MyFloorSpeechActivity.this, "无数据");
                        } else {
                            for (int i = 0; i < state.result.size(); i++) {
                                entities.add(state.result.get(i));
                            }
                            adapter.notifyDataSetChanged();
                            if (pageNo == 1) {
                                customListView.onRefreshComplete();
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
                            ToastUtils.displayShortToast(ConsultDetailActivity.this,
                                    "验证错误，请重新登录");
                            // ToosUtils.goReLogin(getActivity());
                        } else {
                            ToastUtils.displayShortToast(ConsultDetailActivity.this,
                                    (String) state.result);

                        }
                        customListView.onRefreshComplete();
                        customListView.onLoadMoreComplete();
                        customListView.setCanLoadMore(false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    customListView.onRefreshComplete();
                    customListView.onLoadMoreComplete();
                    customListView.setCanLoadMore(false);
                    ToastUtils.displaySendFailureToast(ConsultDetailActivity.this);
                }

            }
        });

    }

    /**
     * 添加新闻评论点赞
     */
    private void AddCommentPraise() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("id", id);
        String url="/v1_1_0/newsComment/praise";
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
                        ToastUtils.displayFailureToast(ConsultDetailActivity.this);
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
                                ToastUtils.displayShortToast(ConsultDetailActivity.this,
                                        "操作成功");
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        ConsultDetailActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(ConsultDetailActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        ConsultDetailActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(ConsultDetailActivity.this);
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
        if (position!=-1){
            rp.addBodyParameter("targetId", entities.get(position).publishUserId);
            rp.addBodyParameter("id",entities.get(position).id);
        }
        rp.addBodyParameter("newsId",id);
        rp.addBodyParameter("content",temp);
        String url="/v1_1_0/newsComment/save";
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
                        ToastUtils.displayFailureToast(ConsultDetailActivity.this);
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
                                ToastUtils.displayShortToast(ConsultDetailActivity.this,
                                        "操作成功");
                                ConsultDetailReturnEntity returnEntity=gson.fromJson(arg0.result,ConsultDetailReturnEntity.class);
                                if (position==-1) {
                                    entities.add(0, returnEntity.result);
                                }else{
                                    entities.add(position + 1, returnEntity.result);
                                }

//                                CommentState commentState=gson.fromJson(arg0.result,CommentState.class);
//                                if (position==-1){
//                                    ReplayEntity replayEntity=new ReplayEntity();
//                                    replayEntity.id=commentState.result.commentId;
//                                    replayEntity.content=temp;
//                                    replayEntity.parentId="-1";
//                                    replayEntity.publishUserIcon=ShareDataTool.getIcon(ConsultDetailActivity.this);
//                                    replayEntity.publishUserId=ShareDataTool.getUserId(ConsultDetailActivity.this);
//                                    replayEntity.publishUserNickname=ShareDataTool.getNickname(ConsultDetailActivity.this);
//                                    replayEntity.createDate= TimeUtils.getDate();
//                                    entities.add(0, replayEntity);
//                                }else{
//                                    ReplayEntity replayEntity=new ReplayEntity();
//                                    replayEntity.id=commentState.result.commentId;
//                                    replayEntity.content=temp;
//                                    replayEntity.parentId="-1";
//                                    replayEntity.publishUserIcon=ShareDataTool.getIcon(ConsultDetailActivity.this);
//                                    replayEntity.publishUserId = ShareDataTool.getUserId(ConsultDetailActivity.this);
//                                    replayEntity.publishUserNickname = ShareDataTool.getNickname(ConsultDetailActivity.this);
//                                    replayEntity.targetUserId=entities.get(position).publishUserId;
//                                    replayEntity.targetUserNickname=entities.get(position).publishUserNickname;
//                                    replayEntity.createDate= TimeUtils.getDate();
//                                    entities.add(position + 1, replayEntity);
//                                }
                                flagIndex=-1;
                                sendEdit.setHint("请输入评论内容");
                                sendEdit.setText("");
                                imm.hideSoftInputFromWindow(sendEdit.getWindowToken(), 0);
                                adapter.notifyDataSetChanged();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        ConsultDetailActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(ConsultDetailActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        ConsultDetailActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(ConsultDetailActivity.this);
                        }

                    }
                });

    }
}
