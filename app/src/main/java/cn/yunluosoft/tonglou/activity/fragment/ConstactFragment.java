package cn.yunluosoft.tonglou.activity.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.ChatActivity;
import cn.yunluosoft.tonglou.activity.ConstactActivity;
import cn.yunluosoft.tonglou.activity.ConstactsAddActivity;
import cn.yunluosoft.tonglou.activity.ConstantWithfloorActivity;
import cn.yunluosoft.tonglou.adapter.FconstactsAdaper;
import cn.yunluosoft.tonglou.dialog.CustomeDialog;
import cn.yunluosoft.tonglou.model.FriendComparator;
import cn.yunluosoft.tonglou.model.FriendEntity;
import cn.yunluosoft.tonglou.model.FriendState;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.FriendDBUtils;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.sortlistview.SideBar;
import cn.yunluosoft.tonglou.view.sortlistview.SideBar.OnTouchingLetterChangedListener;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author Mu
 * @date 2015-8-2下午4:16:36
 * @description
 */
public class ConstactFragment extends Fragment implements View.OnClickListener{

    private ImageView back;

    private TextView title;

    private View pro;

    private ListView listView;

    private TextView dialog;

    private SideBar sideBar;

    private FconstactsAdaper adaper;

    private List<FriendEntity> entities;

    private FriendDBUtils friendDBUtils;

    private View withfloorFriend;

    private View addFriend;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 40:
                    int position = msg.arg1;
                    delCon(position);
                    break;

                default:
                    break;
            }
        };
    };

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fcontacts, container, false);
        back = (ImageView) view.findViewById(R.id.title_back);
        title = (TextView) view.findViewById(R.id.title_title);
        listView = (ListView) view.findViewById(R.id.fconstact_listview);
        dialog = (TextView) view.findViewById(R.id.fconstact_dialog);
        sideBar = (SideBar) view.findViewById(R.id.fconstact_sidrbar);
        pro = view.findViewById(R.id.fconstact_pro);
        withfloorFriend=view.findViewById(R.id.fconstacts_constact);
        addFriend=view.findViewById(R.id.fconstacts_add);

        back.setVisibility(View.GONE);
        title.setText("人脉");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        withfloorFriend.setOnClickListener(this);
        addFriend.setOnClickListener(this);
        friendDBUtils = new FriendDBUtils(getActivity());
        entities = friendDBUtils.getAllFriends();
        if (entities == null) {
            entities = new ArrayList<FriendEntity>();
        }
        adaper = new FconstactsAdaper(getActivity(),entities);
        listView.setAdapter(adaper);


        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                CustomeDialog customeDialog = new CustomeDialog(getActivity(),
                        handler, "确定删除？", position, -1);
                // delete(position);
                return true;
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(getActivity(),
                        ConstactActivity.class);
                intent.putExtra("id", entities.get(position).userId);
                if (!ToosUtils.isStringEmpty(entities.get(position).remarkName)) {
                    intent.putExtra("name", entities.get(position).remarkName);
                } else {
                    intent.putExtra("name", entities.get(position).userName);
                }
                startActivity(intent);


            }
        });
//
        getInfo();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.fconstacts_constact:
                Intent intent=new Intent(getActivity(), ConstantWithfloorActivity.class);
                startActivity(intent);
                break;
            case R.id.fconstacts_add:
                Intent intent2=new Intent(getActivity(), ConstactsAddActivity.class);
                startActivity(intent2);
                break;

        }

    }

    private void getInfo() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(getActivity()));
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpMethod.POST, Constant.ROOT_PATH + "/v1/contact/find",
                rp, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                         pro.setVisibility(View.VISIBLE);
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        pro.setVisibility(View.GONE);
                        ToastUtils.displayFailureToast(getActivity());
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
                                entities.clear();
                                FriendState friendState = gson.fromJson(
                                        arg0.result, FriendState.class);
                                if (friendState.result == null) {
                                    return;
                                }
                                for (int i = 0; i < friendState.result.size(); i++) {
                                    for (int j = 0; j < friendState.result
                                            .get(i).contactListVos.size(); j++) {
                                        entities.add(friendState.result.get(i).contactListVos
                                                .get(j));
                                    }

                                }

                                LogManager.LogShow("----", entities.toString(),
                                        LogManager.ERROR);
                                sideBar.setTextView(dialog);
                                Collections.sort(entities,
                                        new FriendComparator());
                                friendDBUtils.saveAllFriends(entities);
                                adaper.notifyDataSetChanged();

                                // adaper = new FconstactsAdaper(getActivity(),
                                // entities);
                                // listView.setAdapter(adaper);
                                // 设置右侧触摸监听
                                sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

                                    @Override
                                    public void onTouchingLetterChanged(String s) {
                                        // 该字母首次出现的位置
                                        int position = adaper
                                                .getPositionForSection(s
                                                        .charAt(0));
                                        if (position != -1) {
                                            listView.setSelection(position);
                                        }

                                    }
                                });
                                reFushEmpty();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(getActivity(),
                                        "验证错误，请重新登录");
                                ToosUtils.goReLogin(getActivity());
                            } else {
                                ToastUtils.displayShortToast(getActivity(),
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.displaySendFailureToast(getActivity());
                        }

                    }
                });

    }

    public void reFushEmpty() {
//        if (entities == null || entities.size() == 0) {
//            empty.setVisibility(View.VISIBLE);
//            empty_image.setImageDrawable(getResources().getDrawable(
//                    R.drawable.empty_contact));
//            empty_text.setText("没有人脉");
//        } else {
//            empty.setVisibility(View.GONE);
//        }
    }

    private void delCon(final int position) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(getActivity()));
        rp.addBodyParameter("toUserId", entities.get(position).userId);
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpMethod.POST, Constant.ROOT_PATH + "/v1/contact/del", rp,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        pro.setVisibility(View.VISIBLE);
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        pro.setVisibility(View.GONE);
                        ToastUtils.displayFailureToast(getActivity());
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
                                ToastUtils.displayShortToast(getActivity(),
                                        String.valueOf(state.result));
                                friendDBUtils.removeFriend(entities
                                        .get(position));
                                entities.remove(position);
                                adaper.notifyDataSetChanged();
                                reFushEmpty();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(getActivity(),
                                        "验证错误，请重新登录");
                                ToosUtils.goReLogin(getActivity());
                            } else {
                                ToastUtils.displayShortToast(getActivity(),
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils.displaySendFailureToast(getActivity());
                        }

                    }
                });

    }
}
