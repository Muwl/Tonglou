package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import cn.yunluosoft.tonglou.adapter.PPDetailAdapter;
import cn.yunluosoft.tonglou.adapter.UsedDetailAdapter;
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.model.GroupDetailState;
import cn.yunluosoft.tonglou.model.ReplayEntity;
import cn.yunluosoft.tonglou.model.ReplayState;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CircleImageView;
import cn.yunluosoft.tonglou.view.CustomListView;

/**
 * Created by Administrator on 2016/2/7.
 */
public class PPDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;

    private TextView title;

    private ImageView share;

    private CustomListView customListView;

    private View pro;

    private PPDetailAdapter adapter;

    private EditText edit;

    private TextView send;

    private String id;

    private FloorSpeechEntity entity;

    private BitmapUtils bitmapUtils;

    private int pageNo = 1;

    private boolean proShow = true;

    public boolean isFirst = true;

    private List<ReplayEntity> entities;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ppdetail);
        id=getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        share= (ImageView) findViewById(R.id.title_share);
        customListView= (CustomListView) findViewById(R.id.ppdetail_list);
        edit= (EditText) findViewById(R.id.ppdetail_edit);
        send= (TextView) findViewById(R.id.ppdetail_send);
        pro=findViewById(R.id.ppdetail_pro);

        bitmapUtils=new BitmapUtils(this);
        share.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        title.setText("拼车详情");
        share.setOnClickListener(this);
        customListView.setAdapter(adapter);
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

            case R.id.ppdetail_join:

                break;

            case R.id.ppdetail_replay:

                break;

            case R.id.ppdetail_send:

                break;

        }
    }

    // 获取信息
    private void getInfo() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("dynamicId", id);
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        LogManager.LogShow("---", Constant.ROOT_PATH + "/v1_1_0/dynamic/dynamicDetail?sign=" + ShareDataTool.getToken(this) + "&dynamicId=" + id, LogManager.ERROR);
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
                        ToastUtils.displayFailureToast(PPDetailActivity.this);
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
                                adapter = new PPDetailAdapter(PPDetailActivity.this, entities,entity, handler);
                                customListView.setAdapter(adapter);
                                getDynamic(1);
                            } else {
                                ToastUtils.displayShortToast(
                                        PPDetailActivity.this,
                                        (String) state.result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils
                                    .displaySendFailureToast(PPDetailActivity.this);
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
                ToastUtils.displayFailureToast(PPDetailActivity.this);
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
                            ToastUtils.displayShortToast(PPDetailActivity.this,
                                    "验证错误，请重新登录");
                            ToosUtils.goReLogin(PPDetailActivity.this);
                        } else {
                            ToastUtils.displayShortToast(PPDetailActivity.this,
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
                    ToastUtils.displaySendFailureToast(PPDetailActivity.this);
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
}
