package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.model.GroupDetailState;
import cn.yunluosoft.tonglou.model.ReplayEntity;
import cn.yunluosoft.tonglou.model.ReplayState;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.model.User;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
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

    private CircleImageView icon;

    private TextView name;

    private TextView address;

    private TextView num;

    private TextView time;

    private TextView content;

    private TextView join;

    private TextView atten;

    private TextView comment;

    private ImageView replay;

    private MyGridView gridView;

    private GroupDetailGridViewAdapter gridAdapter;

    private CustomListView customListView;

    private View pro;

    private GroupDetailAdapter adapter;

    private EditText edit;

    private TextView send;

    private String id;

    private FloorSpeechEntity entity;

    private BitmapUtils bitmapUtils;

    private List<User> userList;

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
        setContentView(R.layout.groupdetail);
        id=getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        share= (ImageView) findViewById(R.id.title_share);
        icon= (CircleImageView) findViewById(R.id.groupdetail_icon);
        name= (TextView) findViewById(R.id.groupdetail_name);
        address= (TextView) findViewById(R.id.groupdetail_address);
        num= (TextView) findViewById(R.id.groupdetail_num);
        time= (TextView) findViewById(R.id.groupdetail_time);
        content= (TextView) findViewById(R.id.groupdetail_content);
        join= (TextView) findViewById(R.id.groupdetail_join);
        atten= (TextView) findViewById(R.id.groupdetail_atten);
        replay= (ImageView) findViewById(R.id.groupdetail_replay);
        comment= (TextView) findViewById(R.id.groupdetail_comment);
        gridView= (MyGridView) findViewById(R.id.groupdetail_grid);
        customListView= (CustomListView) findViewById(R.id.groupdetail_list);
        edit= (EditText) findViewById(R.id.groupdetail_edit);
        send= (TextView) findViewById(R.id.groupdetail_send);
        pro=findViewById(R.id.groupdetail_pro);

        bitmapUtils=new BitmapUtils(this);
        share.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        title.setText("活动详情");
        share.setOnClickListener(this);
        replay.setOnClickListener(this);
        join.setOnClickListener(this);
        atten.setOnClickListener(this);
        comment.setOnClickListener(this);
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
        adapter=new GroupDetailAdapter(this,entities,handler);
        customListView.setAdapter(adapter);
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

            case R.id.groupdetail_join:

                break;

            case R.id.groupdetail_atten:

                break;

            case R.id.groupdetail_comment:

                break;

            case R.id.groupdetail_replay:

                break;

            case R.id.groupdetail_send:

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
                                GroupDetailState state1=gson.fromJson(arg0.result,GroupDetailState.class);
                                entity=state1.result;
                                setValue(entity);
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

    private void setValue(FloorSpeechEntity entity){
        bitmapUtils.display(icon, entity.publishUserIcon);
        name.setText(entity.publishUserNickname);
        address.setText(entity.locationName);
        num.setText("参团人数：" + entity.planPeopleNum + "/" + entity.groupNum);
        time.setText("截止日期："+entity.endDate);
        content.setText(entity.detail);
        join.setText("参加");
        userList=entity.praiseUser;
        if(userList==null){
            userList=new ArrayList<>();
        }
        gridAdapter=new GroupDetailGridViewAdapter(this,userList);
        gridView.setAdapter(gridAdapter);
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

}
