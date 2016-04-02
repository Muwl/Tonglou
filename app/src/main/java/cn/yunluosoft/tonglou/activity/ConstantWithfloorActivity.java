package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
 * Created by Mu on 2016/1/28.
 */
public class ConstantWithfloorActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private View pro;

    private CustomListView listView;

    private ConstantWithfloorAdapter adapter;

    private int pageNo = 1;

    private boolean proShow = true;

    public boolean isFirst = true;

    private List<ConstantWithfloorEntity> entities;

    private View empty;

    private ImageView empty_image;

    private TextView empty_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constant_withfloor);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title_title);
        back= (ImageView) findViewById(R.id.title_back);
        listView= (CustomListView) findViewById(R.id.constant_withfloor_list);
        pro=findViewById(R.id.constant_withfloor_pro);
        empty = findViewById(R.id.constant_withfloor_empty);
        empty_image = (ImageView) findViewById(R.id.empty_image);
        empty_text = (TextView)findViewById(R.id.empty_text);

        title.setText("本楼人员");
        back.setOnClickListener(this);
        entities = new ArrayList<ConstantWithfloorEntity>();
        adapter=new ConstantWithfloorAdapter(this,entities);
        listView.setAdapter(adapter);
        getInfo(pageNo);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(ConstantWithfloorActivity.this,
                        ConstactActivity.class);
                intent.putExtra("id", entities.get(position - 1).id);
                intent.putExtra("name", entities.get(position - 1).nickname);
                startActivity(intent);

            }
        });

        listView.setOnRefreshListener(new CustomListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // pageNo = 0;
                closePro();
                // pageNo = 1;
                // entities.clear();
                // adapter.notifyDataSetChanged();
                listView.setCanLoadMore(false);
                getInfo(1);
            }
        });
        listView.setOnLoadListener(new CustomListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                closePro();
                getInfo(pageNo + 1);
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
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
        }
    }


    /**
     * 获取同楼列表
     */
    private void getInfo(final int page) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("pageNo", String.valueOf(page));
        rp.addBodyParameter("buildingId",
                ShareDataTool.getBuildingId(this));
        LogManager.LogShow("----",  ShareDataTool.getBuildingId(this)+"dddd",
                LogManager.ERROR);
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH
                        + "/v1/user/findOneBuilding", rp,
                new RequestCallBack<String>() {
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
                        ToastUtils.displayFailureToast(ConstantWithfloorActivity.this);
                        listView.onRefreshComplete();
                        listView.onLoadMoreComplete();
                        listView.setCanLoadMore(false);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        pro.setVisibility(View.GONE);
                        try {
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
                                    ToastUtils.displayShortToast(ConstantWithfloorActivity.this,
                                            "无数据");
                                    if (pageNo == 1) {
                                        empty.setVisibility(View.VISIBLE);
//                                        empty_image
//                                                .setImageDrawable(getResources()
//                                                        .getDrawable(
//                                                                R.drawable.empty_floor));
                                        empty_text.setText("没有同楼");
                                    } else {
                                        empty.setVisibility(View.GONE);
                                    }
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
                                    if (pageNo == 1) {
                                        empty.setVisibility(View.VISIBLE);
//                                        empty_image
//                                                .setImageDrawable(getResources()
//                                                        .getDrawable(
//                                                                R.drawable.empty_floor));
                                        empty_text.setText("没有同楼");
                                    } else {
                                        empty.setVisibility(View.GONE);
                                    }
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
                                    ToastUtils.displayShortToast(ConstantWithfloorActivity.this,
                                            "验证错误，请重新登录");
                                    ToosUtils.goReLogin(ConstantWithfloorActivity.this);
                                } else {
                                    ToastUtils.displayShortToast(ConstantWithfloorActivity.this,
                                            (String) state.result);

                                }
                                listView.onRefreshComplete();
                                listView.onLoadMoreComplete();
                                listView.setCanLoadMore(false);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            listView.onRefreshComplete();
                            listView.onLoadMoreComplete();
                            listView.setCanLoadMore(false);
                            ToastUtils.displaySendFailureToast(ConstantWithfloorActivity.this);
                        }

                    }
                });

    }
}
