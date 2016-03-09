package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import cn.yunluosoft.tonglou.model.ConsultDetailEntity;
import cn.yunluosoft.tonglou.model.ConsultDetailState;
import cn.yunluosoft.tonglou.model.ConsultInfoEntity;
import cn.yunluosoft.tonglou.model.ConsultInfoState;
import cn.yunluosoft.tonglou.model.FloorSpeechState;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.model.User;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CustomListView;

/**
 * Created by Mu on 2016/2/1.
 */
public class ConsultDetailActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;

    private TextView title;

    private ImageView share;

    private CustomListView customListView;;

    private ConsultDetailAdapter adapter;

    private View pro;

    private String id;

    private ConsultInfoEntity entity;

    private int pageNo = 1;

    private boolean proShow = true;

    public boolean isFirst = true;

    private List<ConsultDetailEntity> entities;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
        customListView= (CustomListView) findViewById(R.id.consult_detail_list);
        pro=findViewById(R.id.consult_detail_pro);


        back.setOnClickListener(this);
        title.setText("资讯详情");
        share.setVisibility(View.VISIBLE);
        share.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.title_share:
                break;

            case R.id.consult_detail_atten:
                AddPraise();
                break;
            case R.id.consult_detail_report:
                break;
            case R.id.consult_detail_message:
                Intent intent=new Intent(ConsultDetailActivity.this,WriteMessageActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 添加新闻评论点赞
     */
    private void getInfo() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("Id", id);
        String url="/v1_1_0//news/readNumAndPraiseNum";
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
                    Gson gson = new Gson();
                    // LogManager.LogShow("----", arg0.result+"1111111111",
                    // LogManager.ERROR);
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
        rp.addBodyParameter("Id", id);
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
}
