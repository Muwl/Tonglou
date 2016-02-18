package cn.yunluosoft.tonglou.activity.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.yunluosoft.tonglou.activity.AssistActivity;
import cn.yunluosoft.tonglou.activity.HiGroupActivity;
import cn.yunluosoft.tonglou.activity.MainActivity;
import cn.yunluosoft.tonglou.activity.PPActivity;
import cn.yunluosoft.tonglou.activity.PublishActivity;
import cn.yunluosoft.tonglou.activity.UsedActivity;
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
 * Created by Mu on 2016/1/25.
 */
public class WithFloorFragment extends Fragment implements View.OnClickListener {

    public static final int ATTEN = 1011;

    public static final int PRAISE = 1012;

    private TextView title;

    private View serch;

    private TextView rig;

    private TextView group;

    private TextView used;

    private TextView pp;

    private TextView help;

    private CustomListView customListView;

    private View pro;

    private WithFloorAdapter adapter;

    private int pageNo = 1;

    private boolean proShow = true;

    public boolean isFirst = true;

    private List<FloorSpeechEntity> entities;

    private boolean flag = true;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ATTEN:
                    break;
                case PRAISE:
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fwithfloor, container, false);
        title = (TextView) view.findViewById(R.id.title_title);
        view.findViewById(R.id.title_back).setVisibility(View.GONE);
        serch = view.findViewById(R.id.fwithfloor_serch);
        rig = (TextView) view.findViewById(R.id.title_rig);
        group = (TextView) view.findViewById(R.id.fwithfloor_group);
        used = (TextView) view.findViewById(R.id.fwithfloor_used);
        pp = (TextView) view.findViewById(R.id.fwithfloor_pp);
        help = (TextView) view.findViewById(R.id.fwithfloor_help);
        customListView = (CustomListView) view.findViewById(R.id.fwithfloor_list);
        pro = view.findViewById(R.id.fwithfloor_pro);

        rig.setText("发布");
        rig.setVisibility(View.VISIBLE);

        title.setText("同楼");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        entities = new ArrayList<>();
        serch.setOnClickListener(this);
        group.setOnClickListener(this);
        used.setOnClickListener(this);
        pp.setOnClickListener(this);
        rig.setOnClickListener(this);
        help.setOnClickListener(this);
        adapter = new WithFloorAdapter(getActivity(), entities, handler);
        customListView.setAdapter(adapter);

        getInfo(1);
        customListView.setOnRefreshListener(new CustomListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // pageNo = 0;
                closePro();
                // pageNo = 1;
                // entities.clear();
                // adapter.notifyDataSetChanged();
                customListView.setCanLoadMore(false);
                getInfo(1);
            }
        });
        customListView.setOnLoadListener(new CustomListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                // pageNo++;
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
        switch (view.getId()) {
            case R.id.fwithfloor_serch:

                break;
            case R.id.title_rig:
                Intent intent4 = new Intent(getActivity(), PublishActivity.class);
                startActivity(intent4);
                break;
            case R.id.fwithfloor_group:
                Intent intent = new Intent(getActivity(), HiGroupActivity.class);
                startActivity(intent);
                break;

            case R.id.fwithfloor_used:
                Intent intent1 = new Intent(getActivity(), UsedActivity.class);
                startActivity(intent1);

                break;

            case R.id.fwithfloor_pp:
                Intent intent2 = new Intent(getActivity(), PPActivity.class);
                startActivity(intent2);

                break;

            case R.id.fwithfloor_help:
                Intent intent3 = new Intent(getActivity(), AssistActivity.class);
                startActivity(intent3);

                break;

        }
    }

    /**
     * 获取楼语列表
     */
    private void getInfo(final int page) {
        if (getActivity() == null) {
            return;
        }
        if (ToosUtils.isStringEmpty(ShareDataTool.getToken(getActivity()))) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(getActivity()));
        rp.addBodyParameter("modelType","4");
        rp.addBodyParameter("pageNo", String.valueOf(page));
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        LogManager.LogShow("=========",
                Constant.ROOT_PATH + "/v1_1_0/dynamic/findDynamic?sign="
                        + ShareDataTool.getToken(getActivity()) + "&modelType=4&pageNo="
                        + String.valueOf(pageNo), LogManager.ERROR);
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
                ToastUtils.displayFailureToast(getActivity());
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
                        ShareDataTool.savePageNo(getActivity(), page);
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
                                ShareDataTool.saveGetNum(getActivity(), 0);
                                ShareDataTool.saveGetNumTime(getActivity(), 0);
                                ((MainActivity) getActivity()).onrefush();
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
                            ToastUtils.displayShortToast(getActivity(),
                                    "验证错误，请重新登录");
                            ToosUtils.goReLogin(getActivity());
                        } else {
                            ToastUtils.displayShortToast(getActivity(),
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
                    ToastUtils.displaySendFailureToast(getActivity());
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
