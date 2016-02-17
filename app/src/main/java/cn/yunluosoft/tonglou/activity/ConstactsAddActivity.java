package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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
import cn.yunluosoft.tonglou.adapter.ConstactsAddAdapter;
import cn.yunluosoft.tonglou.adapter.ConstantWithfloorAdapter;
import cn.yunluosoft.tonglou.model.ConstantWithfloorEntity;
import cn.yunluosoft.tonglou.model.ConstantWithfloorState;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CustomListView;

/**
 * Created by Mu on 2016/2/1.
 */
public class ConstactsAddActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private EditText editText;

    private CustomListView listView;

    private ConstactsAddAdapter adapter;

    private View pro;

    private int pageNo = 1;

    private boolean proShow = true;

    public boolean isFirst = true;

    private List<ConstantWithfloorEntity> entities;

    private String stag="";

    private boolean checkFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constacts_add);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title_title);
        back = (ImageView) findViewById(R.id.title_back);
        editText = (EditText) findViewById(R.id.constacts_add_edit);
        listView = (CustomListView) findViewById(R.id.constacts_add_list);
        pro = findViewById(R.id.constacts_add_pro);

        title.setText("添加好友");
        back.setOnClickListener(this);

        entities = new ArrayList<ConstantWithfloorEntity>();
        adapter = new ConstactsAddAdapter(this, entities);
        listView.setAdapter(adapter);
       // getInfo(pageNo,stag);
        listView.setCanRefresh(false);
        listView.setOnLoadListener(new CustomListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                closePro();
                getInfo(pageNo + 1, stag);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // LogManager.LogShow("--------", "aaaaaaaaaaaaaa",
                // LogManager.ERROR);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // LogManager
                // .LogShow("--------", "bbbbbbbbbbbb", LogManager.ERROR);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // LogManager.LogShow("--------", "ccccccccccccc",
                // LogManager.ERROR);
                if (checkFlag) {
                    if (!ToosUtils.isStringEmpty(s.toString())) {
                        stag = String.valueOf(System.currentTimeMillis());
                        getInfo(1, stag);
                    } else {
                        entities.clear();
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    checkFlag = true;
                }

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(ConstactsAddActivity.this,
                        ConstactActivity.class);
                intent.putExtra("id", entities.get(position-1).id);
                intent.putExtra("name", entities.get(position-1).nickname);
                startActivity(intent);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }


    /**
     * 根据关键词搜索同楼的人
     */
    private void getInfo(final int page,String tag) {
        if (ToosUtils.isTextEmpty(editText)) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("pageNo", String.valueOf(page));
        rp.addBodyParameter("keywords", ToosUtils.getTextContent(editText));
        LogManager.LogShow("----", ShareDataTool.getBuildingId(this) + "dddd",
                LogManager.ERROR);
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        RequestCallBack<String> requestCallBack = new RequestCallBack<String>() {
            @Override
            public void onStart() {
//                if (proShow) {
//                    pro.setVisibility(View.VISIBLE);
//                } else {
//                    pro.setVisibility(View.GONE);
//                }

                super.onStart();
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                ToastUtils.displayFailureToast(ConstactsAddActivity.this);
                listView.onRefreshComplete();
                listView.onLoadMoreComplete();
                listView.setCanLoadMore(false);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                try {
                    if (stag.equals(userTag)) {
                        Gson gson = new Gson();
                        LogManager.LogShow("----", arg0.result,
                                LogManager.ERROR);
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
                                listView.onRefreshComplete();
                                listView.onLoadMoreComplete();
                                listView.setCanLoadMore(false);
                                ToastUtils.displayShortToast(ConstactsAddActivity.this,
                                        "无数据");
//                            if (pageNo == 1) {
//                                empty.setVisibility(View.VISIBLE);
////                                        empty_image
////                                                .setImageDrawable(getResources()
////                                                        .getDrawable(
////                                                                R.drawable.empty_floor));
//                                empty_text.setText("没有同楼");
//                            } else {
//                                empty.setVisibility(View.GONE);
//                            }
                                return;
                            }
                            ConstantWithfloorState state = gson.fromJson(
                                    arg0.result, ConstantWithfloorState.class);
                            if (state.result == null
                                    || state.result.size() == 0) {
                                listView.onRefreshComplete();
                                listView.onLoadMoreComplete();
                                listView.setCanLoadMore(false);
                                // ToastUtils.displayShortToast(getActivity(),
                                // "无数据");
//                            if (pageNo == 1) {
//                                empty.setVisibility(View.VISIBLE);
////                                        empty_image
////                                                .setImageDrawable(getResources()
////                                                        .getDrawable(
////                                                                R.drawable.empty_floor));
//                                empty_text.setText("没有同楼");
//                            } else {
//                                empty.setVisibility(View.GONE);
//                            }
                            } else {
                                for (int i = 0; i < state.result.size(); i++) {
                                    entities.add(state.result.get(i));
                                }
                                adapter.notifyDataSetChanged();
                                if (pageNo == 1) {
                                    listView.onRefreshComplete();
                                } else {
                                    listView.onRefreshComplete();
                                    listView.onLoadMoreComplete();
                                }
                                listView.setCanLoadMore(true);
                            }

                        } else {
                            ReturnState state = gson.fromJson(arg0.result,
                                    ReturnState.class);
                            if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(ConstactsAddActivity.this,
                                        "验证错误，请重新登录");
                                // ToosUtils.goReLogin(getActivity());
                            } else {
                                ToastUtils.displayShortToast(ConstactsAddActivity.this,
                                        (String) state.result);

                            }
                            listView.onRefreshComplete();
                            listView.onLoadMoreComplete();
                            listView.setCanLoadMore(false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listView.onRefreshComplete();
                    listView.onLoadMoreComplete();
                    listView.setCanLoadMore(false);
                    ToastUtils.displaySendFailureToast(ConstactsAddActivity.this);
                }

            }
        };

        requestCallBack.setUserTag(tag);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH +"/v1/user/search", rp,
                requestCallBack);

    }
}
