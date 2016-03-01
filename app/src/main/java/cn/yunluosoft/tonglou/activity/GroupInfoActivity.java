package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

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
import cn.yunluosoft.tonglou.adapter.GroupInfoAdapter;
import cn.yunluosoft.tonglou.model.CommentState;
import cn.yunluosoft.tonglou.model.GroupInfoEntity;
import cn.yunluosoft.tonglou.model.ReplayEntity;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.MyGridView;

/**
 * Created by Administrator on 2016/3/1.
 */
public class GroupInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;

    private TextView title;

    private MyGridView myGridView;

    private ToggleButton toggleButton;

    private View report;

    private TextView exit;

    private View pro;

    private GroupInfoAdapter adapter;

    private List<GroupInfoEntity> entities;

    private int width;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupinfo);
        initView();

    }

    private void initView() {
        id=getIntent().getStringExtra("id");
        width= DensityUtil.getScreenWidth(this);
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        myGridView= (MyGridView) findViewById(R.id.groupinfo_gridview);
        toggleButton= (ToggleButton) findViewById(R.id.groupinfo_toggle);
        report=findViewById(R.id.groupinfo_report);
        exit= (TextView) findViewById(R.id.groupinfo_exit);
        pro=findViewById(R.id.groupinfo_pro);

        entities=new ArrayList<>();
        adapter=new GroupInfoAdapter(this,entities,width);
        back.setOnClickListener(this);
        title.setText("群信息");
        report.setOnClickListener(this);
        exit.setOnClickListener(this);
        getInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;

            case R.id.groupinfo_report:

                break;

            case R.id.groupinfo_exit:

                break;
        }
    }

    /**
     *看群详情
     */
    private void getInfo() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("dynamicId",id);
        String url="/v1_1_0/dynamic/groupDetail";
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
                        ToastUtils.displayFailureToast(GroupInfoActivity.this);
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
                                ToastUtils.displayShortToast(GroupInfoActivity.this,
                                        "操作成功");
                                LogManager.LogShow("----", arg0.result,
                                        LogManager.ERROR);

                                adapter.notifyDataSetChanged();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        GroupInfoActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(GroupInfoActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        GroupInfoActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(GroupInfoActivity.this);
                        }

                    }
                });

    }
}
