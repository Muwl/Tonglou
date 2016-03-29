package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
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
import cn.yunluosoft.tonglou.adapter.HelpAdapter;
import cn.yunluosoft.tonglou.adapter.HiGroupAdapter;
import cn.yunluosoft.tonglou.adapter.PPAdapter;
import cn.yunluosoft.tonglou.adapter.SerchFloorAdapter;
import cn.yunluosoft.tonglou.adapter.UsedAdapter;
import cn.yunluosoft.tonglou.adapter.WithFloorAdapter;
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
 * Created by Administrator on 2016/2/23.
 */
public class SerchSpeechActivity extends BaseActivity implements View.OnClickListener {

    public static final int ATTEN = 1011;

    public static final int PRAISE = 1012;

    private EditText serch;

    private TextView cancel;

    private CustomListView customListView;

    private HiGroupAdapter hiGroupAdapter;

    private UsedAdapter usedAdapter;

    private PPAdapter ppAdapter;

    private HelpAdapter helpAdapter;

    private SerchFloorAdapter serchFloorAdapter;

    private View pro;

    private int pageNo = 1;

    private boolean proShow = true;

    public boolean isFirst = true;

    private List<FloorSpeechEntity> entities;

    private String keyword;

    private String stag;

    private int modelFlag = 4;//模块类型（0：团嗨，1：二手，2：拼拼，3：帮帮，4：所有

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ATTEN:
                    int position = (int) msg.obj;
                    if (Constant.ATTEN_OK.equals(entities.get(position).isAttention)) {
                        AddAtten(1, position);
                    } else {
                        AddAtten(0, position);
                    }
                    break;
                case PRAISE:
                    int position2 = (int) msg.obj;
                    if (Constant.PRAISE_OK.equals(entities.get(position2).isPraise)) {
                        AddPraise(1, position2);
                    } else {
                        AddPraise(0, position2);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serch_speeck);
        modelFlag = getIntent().getIntExtra("modelFlag", 4);
        initView();
        serch.setFocusableInTouchMode(true);
        serch.requestFocus();
        imm.showSoftInput(serch, 0);
    }

    private void initView() {
        serch = (EditText) findViewById(R.id.serch_speeck_text);
        cancel = (TextView) findViewById(R.id.serch_speeck_cancel);
        customListView = (CustomListView) findViewById(R.id.serch_speeck_list);
        pro = findViewById(R.id.serch_speeck_pro);

        cancel.setOnClickListener(this);

        entities = new ArrayList<>();

        if (modelFlag == 0) {
            hiGroupAdapter = new HiGroupAdapter(this, entities, handler);
            customListView.setAdapter(hiGroupAdapter);
        } else if (modelFlag == 1) {
            usedAdapter = new UsedAdapter(this, entities, handler);
            customListView.setAdapter(usedAdapter);
        } else if (modelFlag == 2) {
            ppAdapter = new PPAdapter(this, entities, handler);
            customListView.setAdapter(ppAdapter);
        } else if (modelFlag == 3) {
            helpAdapter = new HelpAdapter(this, entities, handler);
            customListView.setAdapter(helpAdapter);
        } else if (modelFlag == 4) {
            serchFloorAdapter = new SerchFloorAdapter(this, entities, handler,customListView);
            customListView.setAdapter(serchFloorAdapter);
        }

        customListView.setOnRefreshListener(new CustomListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // pageNo = 0;
                // pageNo = 1;
                // entities.clear();
                // adapter.notifyDataSetChanged();
                if (ToosUtils.isStringEmpty(keyword)) {
                    return;
                }
                customListView.setCanLoadMore(false);
                getInfo(1, keyword);
            }
        });
        customListView.setOnLoadListener(new CustomListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                // pageNo++;
                if (ToosUtils.isStringEmpty(keyword)) {
                    return;
                }
                getInfo(pageNo + 1, keyword);
            }
        });
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if ("0".equals(entities.get(position).modelType)) {
                    intent = new Intent(SerchSpeechActivity.this, GroupDetailActivity.class);
                } else if ("1".equals(entities.get(position).modelType)) {
                    intent = new Intent(SerchSpeechActivity.this, UsedDetailActivity.class);
                } else if ("2".equals(entities.get(position).modelType)) {
                    intent = new Intent(SerchSpeechActivity.this, PPDetailActivity.class);
                } else if ("3".equals(entities.get(position).modelType)) {
                    intent = new Intent(SerchSpeechActivity.this, HelpDetailActivity.class);
                }
                intent.putExtra("id", entities.get(position - 1).id);
                startActivity(intent);
            }
        });


        serch.addTextChangedListener(new TextWatcher() {

                                         @Override
                                         public void onTextChanged(CharSequence s, int start, int before,
                                                                   int count) {
                                         }

                                         @Override
                                         public void beforeTextChanged(CharSequence s, int start, int count,
                                                                       int after) {
                                             // LogManager
                                             // .LogShow("--------", "bbbbbbbbbbbb", LogManager.ERROR);
                                         }

                                         @Override
                                         public void afterTextChanged(Editable s) {
                                             if (!ToosUtils.isStringEmpty(s.toString())) {
                                                 stag = String.valueOf(System.currentTimeMillis());
                                                 keyword = s.toString();
                                                 getInfo(1, s.toString());
                                             } else {
                                                 entities.clear();
                                                 refushAdapter();
                                             }

                                         }
                                     }

        );


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.serch_speeck_cancel:
                finish();
                break;
        }
    }

    private void refushAdapter() {
        if (modelFlag == 0) {
            hiGroupAdapter.notifyDataSetChanged();
        } else if (modelFlag == 1) {
            usedAdapter.notifyDataSetChanged();
        } else if (modelFlag == 2) {
            ppAdapter.notifyDataSetChanged();
        } else if (modelFlag == 3) {
            helpAdapter.notifyDataSetChanged();
        } else if (modelFlag == 4) {
            serchFloorAdapter.notifyDataSetChanged();
        }
    }


    /**
     *
     */
    private void getInfo(final int page, String keyword) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("modelType", modelFlag+"");
        rp.addBodyParameter("keyword", keyword);
        rp.addBodyParameter("pageNo", String.valueOf(page));
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        LogManager.LogShow("=========",
                Constant.ROOT_PATH + "/v1_1_0/dynamic/searchDynamic?sign="
                        + ShareDataTool.getToken(this) + "&modelType=4&pageNo="
                        + String.valueOf(pageNo)+"&keyword="+keyword,LogManager.ERROR);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH
                + "/v1_1_0/dynamic/searchDynamic", rp, new RequestCallBack<String>() {
            @Override
            public void onStart() {

                super.onStart();
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                ToastUtils.displayFailureToast(SerchSpeechActivity.this);
                customListView.onRefreshComplete();
                customListView.onLoadMoreComplete();
                customListView.setCanLoadMore(false);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
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
                            refushAdapter();
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
                            refushAdapter();
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
                            ToastUtils.displayShortToast(SerchSpeechActivity.this,
                                    "验证错误，请重新登录");
                            ToosUtils.goReLogin(SerchSpeechActivity.this);
                        } else {
                            ToastUtils.displayShortToast(SerchSpeechActivity.this,
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
                    ToastUtils.displaySendFailureToast(SerchSpeechActivity.this);
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
        String url = "/v1_1_0/dynamic/addAttention";
        if (flag == 0) {
            url = "/v1_1_0/dynamic/addAttention";
        } else {
            url = "/v1_1_0/dynamic/delAttention";
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
                        ToastUtils.displayFailureToast(SerchSpeechActivity.this);
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
                                ToastUtils.displayShortToast(SerchSpeechActivity.this,
                                        String.valueOf(state.result));
                                entities.get(position).isAttention = flag + "";
                                refushAdapter();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        SerchSpeechActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(SerchSpeechActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        SerchSpeechActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(SerchSpeechActivity.this);
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
        rp.addBodyParameter("sign", ShareDataTool.getToken(SerchSpeechActivity.this));
        rp.addBodyParameter("dynamicId", entities.get(position).id);
        String url = "/v1_1_0/dynamic/praise";
        if (flag == 0) {
            url = "/v1_1_0/dynamic/praise";
        } else {
            url = "/v1_1_0/dynamic/cancelPraise";
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
                        ToastUtils.displayFailureToast(SerchSpeechActivity.this);
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
                                ToastUtils.displayShortToast(SerchSpeechActivity.this,
                                        "操作成功");
                                entities.get(position).isPraise = flag + "";
                                if (flag == 0) {
                                    entities.get(position).praiseNum = String.valueOf((Integer.valueOf(entities.get(position).praiseNum) + 1));
                                } else {
                                    entities.get(position).praiseNum = String.valueOf((Integer.valueOf(entities.get(position).praiseNum) - 1));
                                }
                                refushAdapter();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        SerchSpeechActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(SerchSpeechActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        SerchSpeechActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(SerchSpeechActivity.this);
                        }

                    }
                });

    }
}
