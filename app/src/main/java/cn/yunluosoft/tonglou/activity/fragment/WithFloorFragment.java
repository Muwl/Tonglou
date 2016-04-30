package cn.yunluosoft.tonglou.activity.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import cn.yunluosoft.tonglou.activity.ChatActivity;
import cn.yunluosoft.tonglou.activity.GroupDetailActivity;
import cn.yunluosoft.tonglou.activity.HelpDetailActivity;
import cn.yunluosoft.tonglou.activity.HiGroupActivity;
import cn.yunluosoft.tonglou.activity.MainActivity;
import cn.yunluosoft.tonglou.activity.PPActivity;
import cn.yunluosoft.tonglou.activity.PPDetailActivity;
import cn.yunluosoft.tonglou.activity.PublishActivity;
import cn.yunluosoft.tonglou.activity.ReportActivity;
import cn.yunluosoft.tonglou.activity.SerchSpeechActivity;
import cn.yunluosoft.tonglou.activity.UsedActivity;
import cn.yunluosoft.tonglou.activity.UsedDetailActivity;
import cn.yunluosoft.tonglou.adapter.WithFloorAdapter;
import cn.yunluosoft.tonglou.dialog.CustomeDialog;
import cn.yunluosoft.tonglou.dialog.ReportMenuDialog;
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.model.FloorSpeechState;
import cn.yunluosoft.tonglou.model.MessageInfo;
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

    public static final int ADDGROUP = 1013;

    public static final int ATTEN = 1011;

    public static final int PRAISE = 1012;

    private TextView title;

    private TextView rig;

    private CustomListView customListView;

    private View pro;

    private WithFloorAdapter adapter;

    private int pageNo = 1;

    private boolean proShow = true;

    public boolean isFirst = true;

    private List<FloorSpeechEntity> entities;

    private boolean flag = true;

    private static Context context;

    private View empty;

    private ImageView empty_image;

    private TextView empty_text;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 55:
                    int position4=msg.arg1;
                    Intent intent6=new Intent(context, ReportActivity.class);
                    intent6.putExtra("flag",1);
                    intent6.putExtra("contactId",entities.get(position4).id);
                    startActivity(intent6);
                    break;
                case ADDGROUP:
                    int position3=msg.arg1;
                    AddJoin(position3);
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

    public static  WithFloorFragment getInstance(Context context) {
        WithFloorFragment fragment = new WithFloorFragment() ;
        WithFloorFragment.context = context;
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fwithfloor, container, false);
        title = (TextView) view.findViewById(R.id.title_title);
        view.findViewById(R.id.title_back).setVisibility(View.GONE);
        rig = (TextView) view.findViewById(R.id.title_rig);
        customListView = (CustomListView) view.findViewById(R.id.fwithfloor_list);
        pro = view.findViewById(R.id.fwithfloor_pro);

        empty = view.findViewById(R.id.fwithfloor_empty);
        empty_image = (ImageView) view.findViewById(R.id.empty_image);
        empty_text = (TextView) view.findViewById(R.id.empty_text);

        rig.setText("发布");
        rig.setVisibility(View.VISIBLE);

        title.setText("同楼");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        entities = new ArrayList<>();
        rig.setOnClickListener(this);
        adapter = new WithFloorAdapter(context, entities, handler,customListView);
        customListView.setAdapter(adapter);
        customListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if ("0".equals(entities.get(position - 2).modelType)) {
                    intent = new Intent(context, GroupDetailActivity.class);
                } else if ("1".equals(entities.get(position - 2).modelType)) {
                    intent = new Intent(context, UsedDetailActivity.class);
                } else if ("2".equals(entities.get(position - 2).modelType)) {
                    intent = new Intent(context, PPDetailActivity.class);
                } else if ("3".equals(entities.get(position - 2).modelType)) {
                    intent = new Intent(context, HelpDetailActivity.class);
                }
                intent.putExtra("id", entities.get(position - 2).id);
                startActivity(intent);
            }
        });

        customListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ToosUtils.CheckComInfo(context)) {

                    ReportMenuDialog dialog = new ReportMenuDialog(context, handler, position - 1);
                }

                return true;
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

    @Override
    public void onResume() {
        super.onResume();
        getInfo(1);
    }

    public void closePro() {
        proShow = false;
    }

    public void openPro() {
        proShow = true;
    }


    public void scrollFirst(){
        customListView.setSelection(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.title_rig:
                if (ToosUtils.CheckComInfo(context)) {
                    Intent intent4 = new Intent(context, PublishActivity.class);
                    startActivity(intent4);
                }
                break;


        }
    }

    /**
     *参加活动

     */
    private void AddJoin(final int position) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(context));
        rp.addBodyParameter("dynamicId", entities.get(position).id);
        String url="/v1_1_0/dynamic/joinActivity";
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
                        ToastUtils.displayFailureToast(context);
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
                                ToastUtils.displayShortToast(context,
                                        "操作成功");
                                entities.get(position).isInGroup=0+"";
                                entities.get(position).groupNum=String.valueOf(Integer.valueOf(entities.get(position).groupNum)+1);
                                adapter.notifyDataSetChanged();
                                Intent intent = new Intent(context,
                                        ChatActivity.class);
                                MessageInfo messageInfo = new MessageInfo();
                                messageInfo.receiverHeadUrl = entities.get(position).id;
                                messageInfo.groupDynamicID = entities.get(position ).id;
                                messageInfo.receiverImUserName = entities.get(position ).imGroupId;
                                messageInfo.receiverNickName = entities.get(position).groupName;
                                messageInfo.receiverUserId = entities.get(position).imGroupId;
                                intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("info", messageInfo);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        context, "验证错误，请重新登录");
                                ToosUtils.goReLogin(context);
                            } else {
                                ToastUtils.displayShortToast(
                                        context,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(context);
                        }

                    }
                });

    }


    /**
     * 获取楼语列表
     */
    private void getInfo(final int page) {
        if (context == null) {
            return;
        }
        if (ToosUtils.isStringEmpty(ShareDataTool.getToken(context))) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(context));
        rp.addBodyParameter("modelType", "4");
        rp.addBodyParameter("pageNo", String.valueOf(page));
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        LogManager.LogShow("=========",
                Constant.ROOT_PATH + "/v1_1_0/dynamic/findDynamic?sign="
                        + ShareDataTool.getToken(context) + "&modelType=4&pageNo="
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
                ToastUtils.displayFailureToast(context);
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
//                        ShareDataTool.savePageNo(context, page);
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
                            // ToastUtils.displayShortToast(context,
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
                            // ToastUtils.displayShortToast(context,
                            // "无数据");
                        } else {
                            for (int i = 0; i < state.result.size(); i++) {
                                entities.add(state.result.get(i));
                            }
                            adapter.notifyDataSetChanged();
                            if (pageNo == 1) {
                                customListView.onRefreshComplete();
//                                ShareDataTool.saveGetNum(context, 0);
//                                ShareDataTool.saveGetNumTime(context, 0);
//                                ((MainActivity) context).onrefush();
                                customListView.setSelection(0);
                            } else {
                                customListView.onRefreshComplete();
                                customListView.onLoadMoreComplete();
                            }
                            customListView.setCanLoadMore(true);
                        }

                        reFushEmpty();

                    } else {
                        ReturnState state = gson.fromJson(arg0.result,
                                ReturnState.class);
                        if (Constant.TOKEN_ERR.equals(state.msg)) {
                            ToastUtils.displayShortToast(context,
                                    "验证错误，请重新登录");
                            ToosUtils.goReLogin(context);
                        } else {
                            ToastUtils.displayShortToast(context,
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
                    ToastUtils.displaySendFailureToast(context);
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

    public void reFushEmpty() {
        if (entities == null || entities.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            empty_image.setImageDrawable(getResources().getDrawable(
                    R.mipmap.withfloor_empty));
            empty_text.setText("没有同楼信息！");
        } else {
            empty.setVisibility(View.GONE);
        }
    }


    /**
     * 添加或者取消关注
     * flag 0 代表添加关注 1代表取消关注
     */
    private void AddAtten(final int flag, final int position) {
        if (context == null) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(context));
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
                        ToastUtils.displayFailureToast(context);
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
                                ToastUtils.displayShortToast(context,
                                        String.valueOf(state.result));
                                entities.get(position).isAttention=flag+"";
                                adapter.notifyDataSetChanged();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        context, "验证错误，请重新登录");
                                ToosUtils.goReLogin(context);
                            } else {
                                ToastUtils.displayShortToast(
                                        context,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(context);
                        }

                    }
                });

    }

    /**
     * 添加或者取消点赞
     * flag 0 代表添加点赞 1代表取消点赞
     */
    private void AddPraise(final int flag, final int position) {
        if (context == null) {
            return;
        }
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(context));
        rp.addBodyParameter("dynamicId", entities.get(position).id);
        String url="/v1_1_0/dynamic/praise";
        if (flag==0){
            url="/v1_1_0/dynamic/praise";
        }else{
            url="/v1_1_0/dynamic/cancelPraise";
        }
        LogManager.LogShow("----",Constant.ROOT_PATH + url+"?sign="+ ShareDataTool.getToken(context)+"&dynamicId="+entities.get(position).id,LogManager.ERROR);
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
                        ToastUtils.displayFailureToast(context);
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
                                ToastUtils.displayShortToast(context,
                                        "操作成功");
                                entities.get(position).isPraise=flag+"";
                                if (flag==0){
                                    entities.get(position).praiseNum=String.valueOf((Integer.valueOf(entities.get(position).praiseNum)+1));
                                }else{
                                    entities.get(position).praiseNum=String.valueOf((Integer.valueOf(entities.get(position).praiseNum)-1));
                                }
                                adapter.refushAtten(position);
//                                adapter.notifyDataSetChanged();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        context, "验证错误，请重新登录");
                                ToosUtils.goReLogin(context);
                            } else {
                                ToastUtils.displayShortToast(
                                        context,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(context);
                        }

                    }
                });

    }

}
