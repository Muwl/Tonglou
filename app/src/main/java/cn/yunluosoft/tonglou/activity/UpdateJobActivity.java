package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.model.PerfectDataState;
import cn.yunluosoft.tonglou.model.PersonInfo;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

/**
 * Created by Mu on 2016/1/25.
 */
public class UpdateJobActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private TextView com;

    private TextView textView;

    private EditText name;

    private View pro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_name);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title_title);
        back= (ImageView) findViewById(R.id.title_back);
        com= (TextView) findViewById(R.id.title_rig);
        textView= (TextView) findViewById(R.id.update_name_text);
        name= (EditText) findViewById(R.id.update_name_name);
        pro=findViewById(R.id.update_name_pro);

        title.setText("职位");
        back.setOnClickListener(this);
        com.setVisibility(View.VISIBLE);
        com.setOnClickListener(this);
        com.setText("完成");
        name.setHint("职位名称");
        textView.setText("请输入新的职位");


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.title_rig:
                if (ToosUtils.isTextEmpty(name)){
                    ToastUtils.displayShortToast(UpdateJobActivity.this,"职位不能为空！");
                    return;
                }
                PersonInfo info=new PersonInfo();
                info.job=ToosUtils.getTextContent(name);
                sendUpdate(info, null);
                break;
        }
    }

    //flag 1修改头像 2 修改名称 3修改生日 4修改性别 5修改行业 6修改职位 7修改签名
    private void sendUpdate(final PersonInfo personInfo, File iconFile) {
        RequestParams rp = new RequestParams();
        final Gson gson = new Gson();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        if (personInfo != null) {
            rp.addBodyParameter("info", gson.toJson(personInfo));
        }
        if (iconFile != null) {
            rp.addBodyParameter("icon", iconFile);
        }

        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH
                        + "/v1/user/saveOrUpdateInfo", rp,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        pro.setVisibility(View.VISIBLE);
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        pro.setVisibility(View.GONE);
                        ToastUtils.displayFailureToast(UpdateJobActivity.this);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        pro.setVisibility(View.GONE);
                        try {
                            // Gson gson = new Gson();
                            LogManager.LogShow("----", arg0.result,
                                    LogManager.ERROR);
                            ReturnState state = gson.fromJson(arg0.result,
                                    ReturnState.class);
                            if (Constant.RETURN_OK.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        UpdateJobActivity.this, "修改成功");
                                PerfectDataState dataState = gson.fromJson(
                                        arg0.result, PerfectDataState.class);
                                ShareDataTool.SaveInfoDetail(
                                        UpdateJobActivity.this,
                                        dataState.result.nickname,
                                        dataState.result.icon,
                                        dataState.result.location,ShareDataTool.getBuildingId(UpdateJobActivity.this));
                                ShareDataTool.SaveFlag(UpdateJobActivity.this,
                                        1);
                                EMChatManager.getInstance()
                                        .updateCurrentUserNick(
                                                dataState.result.nickname);
                                Intent intent=new Intent(UpdateJobActivity.this,PersonDataActivity.class);
                                intent.putExtra("name",personInfo.job);
                                setResult(RESULT_OK,intent);
                                finish();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        UpdateJobActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(UpdateJobActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        UpdateJobActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(UpdateJobActivity.this);
                        }

                    }
                });

    }
}
