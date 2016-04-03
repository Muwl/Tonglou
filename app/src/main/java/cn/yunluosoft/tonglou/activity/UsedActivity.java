package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import cn.yunluosoft.tonglou.adapter.HiGroupAdapter;
import cn.yunluosoft.tonglou.adapter.UsedAdapter;
import cn.yunluosoft.tonglou.dialog.ReportMenuDialog;
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
 * Created by Administrator on 2016/2/7.
 */
public class UsedActivity extends BaseActivity implements View.OnClickListener {

    public static final int ATTEN = 1011;

    public static final int PRAISE = 1012;

    private ImageView back;

    private TextView rig;

    private RadioGroup group;

    private RadioButton assign;

    private RadioButton buy;

    private View serch;

    private CustomListView customListView;

    private View pro;

    private UsedAdapter adapter;

    private int pageNo = 1;

    private boolean proShow = true;

    public boolean isFirst = true;

    private List<FloorSpeechEntity> entities;

    private int flag=0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 55:
                    int position4=msg.arg1;
                    Intent intent6=new Intent(UsedActivity.this, ReportActivity.class);
                    intent6.putExtra("flag",1);
                    intent6.putExtra("contactId",entities.get(position4).id);
                    startActivity(intent6);
                    break;
                case ATTEN:
                    int position= (int) msg.obj;
                    if (Constant.ATTEN_OK.equals(entities.get(position).isAttention)){
                        AddAtten(1,position);
                    }else{
                        AddAtten(0,position);
                    }
                    break;
                case PRAISE:
                    int position2= (int) msg.obj;
                    if (Constant.PRAISE_OK.equals(entities.get(position2).isPraise)){
                        AddPraise(1, position2);
                    }else{
                        AddPraise(0, position2);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.used);
        initView();
    }

    private void initView() {
        flag=getIntent().getIntExtra("flag",0);
        back= (ImageView) findViewById(R.id.title_back);
        rig= (TextView) findViewById(R.id.title_rig);
        group= (RadioGroup) findViewById(R.id.title_group);
        assign= (RadioButton) findViewById(R.id.title_rb_lef);
        buy= (RadioButton) findViewById(R.id.title_rb_rig);
        serch=findViewById(R.id.used_serch);
        customListView= (CustomListView) findViewById(R.id.used_list);
        pro=findViewById(R.id.used_pro);

        back.setOnClickListener(this);
        findViewById(R.id.title_title).setVisibility(View.GONE);
        rig.setVisibility(View.VISIBLE);
        rig.setOnClickListener(this);
        rig.setText("发布");
        group.setVisibility(View.VISIBLE);
        assign.setText("转让");
        buy.setText("求购");
        serch.setOnClickListener(this);

        if (flag==0){
            group.check(R.id.title_rb_lef);
        }else{
            group.check(R.id.title_rb_rig);
        }
        entities=new ArrayList<>();

        adapter = new UsedAdapter(this, entities, handler);
        customListView.setAdapter(adapter);
        getInfo(pageNo, flag);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.title_rb_lef) {
                    pageNo = 1;
                    flag = 0;
                    getInfo(pageNo, flag);
                } else {
                    pageNo = 1;
                    flag = 1;
                    getInfo(pageNo, flag);
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
                getInfo(1, flag);
            }
        });
        customListView.setOnLoadListener(new CustomListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                // pageNo++;
                closePro();
                getInfo(pageNo + 1, flag);
            }
        });
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UsedActivity.this, UsedDetailActivity.class);
                intent.putExtra("id", entities.get(position - 1).id);
                startActivity(intent);

            }
        });

        customListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ToosUtils.CheckComInfo(UsedActivity.this)) {
                    ReportMenuDialog dialog = new ReportMenuDialog(UsedActivity.this, handler, position);
                }
                return true;
            }
        });
    }

    public void closePro() {
        proShow = false;
    }

    public void openPro() {
        proShow = true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.title_rig:
                if (ToosUtils.CheckComInfo(UsedActivity.this)) {
                    Intent intent = new Intent(UsedActivity.this, PublishUsedActivity.class);
                    intent.putExtra("flag", flag);
                    startActivity(intent);
                }
                break;
            case R.id.used_serch:
                Intent intent5=new Intent(UsedActivity.this, SerchSpeechActivity.class);
                intent5.putExtra("modelFlag",1);
                startActivity(intent5);
                break;

        }

    }


    /**
     *
     */
    private void getInfo(final int page,int flag) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(UsedActivity.this));
        rp.addBodyParameter("modelType", "1");
        rp.addBodyParameter("supplyType", flag+"");
        rp.addBodyParameter("pageNo", String.valueOf(page));
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH
                + "/v1_1_0/dynamic/findDynamic", rp, new RequestCallBack<String>() {
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
                ToastUtils.displayFailureToast(UsedActivity.this);
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
                            ToastUtils.displayShortToast(UsedActivity.this,
                                    "验证错误，请重新登录");
                            ToosUtils.goReLogin(UsedActivity.this);
                        } else {
                            ToastUtils.displayShortToast(UsedActivity.this,
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
                    ToastUtils.displaySendFailureToast(UsedActivity.this);
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
     * flag 0 代表添加关注 1代表取消关注
     */
    private void AddAtten(final int flag, final int position) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("dynamicId", entities.get(position).id);
        String url="/v1_1_0/dynamic/addAttention";
        if (flag==0){
            url="/v1_1_0/dynamic/addAttention";
        }else{
            url="/v1_1_0/dynamic/delAttention";
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
                        ToastUtils.displayFailureToast(UsedActivity.this);
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
                                ToastUtils.displayShortToast(UsedActivity.this,
                                        String.valueOf(state.result));
                                entities.get(position).isAttention=flag+"";
                                adapter.notifyDataSetChanged();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        UsedActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(UsedActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        UsedActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(UsedActivity.this);
                        }

                    }
                });

    }

    /**
     * 添加或者取消点赞
     * flag 0 代表添加点赞 1代表取消点赞
     */
    private void AddPraise(final int flag, final int position) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(UsedActivity.this));
        rp.addBodyParameter("dynamicId", entities.get(position).id);
        String url="/v1_1_0/dynamic/praise";
        if (flag==0){
            url="/v1_1_0/dynamic/praise";
        }else{
            url="/v1_1_0/dynamic/cancelPraise";
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
                        ToastUtils.displayFailureToast(UsedActivity.this);
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
                                ToastUtils.displayShortToast(UsedActivity.this,
                                        "操作成功");
                                entities.get(position).isPraise=flag+"";
                                if (flag==0){
                                    entities.get(position).praiseNum=String.valueOf((Integer.valueOf(entities.get(position).praiseNum)+1));
                                }else{
                                    entities.get(position).praiseNum=String.valueOf((Integer.valueOf(entities.get(position).praiseNum)-1));
                                }
                                adapter.notifyDataSetChanged();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        UsedActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(UsedActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        UsedActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(UsedActivity.this);
                        }

                    }
                });

    }
}
