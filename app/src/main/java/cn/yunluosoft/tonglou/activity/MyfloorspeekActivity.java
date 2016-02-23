package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import cn.yunluosoft.tonglou.adapter.MyfloorspeekAdapter;
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.model.FloorSpeechState;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CustomListView;

/**
 * Created by Mu on 2016/1/27.
 */
public class MyfloorspeekActivity extends BaseActivity implements View.OnClickListener{

    private TextView title;

    private ImageView back;

    private RadioGroup group;

    private RadioButton atten;

    private RadioButton publish;

    private CustomListView customListView;

    private View pro;

    private MyfloorspeekAdapter adapter;

    private int pageNo = 1;

    private boolean proShow = true;

    public boolean isFirst = true;

    private List<FloorSpeechEntity> entities;

    private int flag=0;//0代表我的关注 1代表我的发布

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfloorspeek);
        initView();
    }

    private void initView() {
        entities=new ArrayList<>();
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        group= (RadioGroup) findViewById(R.id.myfloorspeech_group);
        atten= (RadioButton) findViewById(R.id.myfloorspeech_atten);
        publish= (RadioButton) findViewById(R.id.myfloorspeech_pub);
        customListView= (CustomListView) findViewById(R.id.myfloorspeech_list);
        pro=findViewById(R.id.myfloorspeech_pro);
        title.setText("我的楼语");
        back.setOnClickListener(this);
        group.check(R.id.myfloorspeech_atten);
        getAtten(pageNo, flag);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.myfloorspeech_atten) {
                    flag = 0;
                    pageNo = 1;
                    entities.clear();
                    adapter.setFlag(flag);
                    getAtten(pageNo, flag);
                } else {
                    flag = 1;
                    entities.clear();
                    adapter.setFlag(flag);
                    getAtten(pageNo, flag);
                }
            }
        });

        customListView.setOnRefreshListener(new CustomListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // pageNo = 0;
                closePro();
                // pageNo = 1;
                // entities.clear();
                // adapter.notifyDataSetChanged();
                customListView.setCanLoadMore(false);
                getAtten(1, flag);
            }
        });
        customListView.setOnLoadListener(new CustomListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                // pageNo++;
                closePro();
                getAtten(pageNo + 1, flag);
            }
        });

        adapter=new MyfloorspeekAdapter(this,entities,handler);
        customListView.setAdapter(adapter);
    }

    public void closePro() {
        proShow = false;
    }

    public void openPro() {
        proShow = true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
        }
    }

    /**
     * 获取我的关注
     */
    private void getAtten(final int page,int flag) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(MyfloorspeekActivity.this));
        rp.addBodyParameter("pageNo", String.valueOf(page));
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        String url="/v1_1_0/dynamic/myAttention";
        if (flag==0){
            url="/v1_1_0/dynamic/myAttention";
        }else{
            url="/v1_1_0/dynamic/myDynamic";
        }
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
                ToastUtils.displayFailureToast(MyfloorspeekActivity.this);
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
                        FloorSpeechState state = gson.fromJson(arg0.result,
                                FloorSpeechState.class);
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
                            ToastUtils.displayShortToast(MyfloorspeekActivity.this,
                                    "验证错误，请重新登录");
                            ToosUtils.goReLogin(MyfloorspeekActivity.this);
                        } else {
                            ToastUtils.displayShortToast(MyfloorspeekActivity.this,
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
                    ToastUtils.displaySendFailureToast(MyfloorspeekActivity.this);
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
     * 添加或者取消关注
     *
     */
    private void AddAtten(final int position) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("dynamicId", entities.get(position).id);
        String url="/v1_1_0/dynamic/delAttention";
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
                        ToastUtils.displayFailureToast(MyfloorspeekActivity.this);
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
                                ToastUtils.displayShortToast(MyfloorspeekActivity.this,
                                        String.valueOf(state.result));
                                entities.remove(position);
                                adapter.notifyDataSetChanged();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        MyfloorspeekActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(MyfloorspeekActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        MyfloorspeekActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(MyfloorspeekActivity.this);
                        }

                    }
                });

    }


}
