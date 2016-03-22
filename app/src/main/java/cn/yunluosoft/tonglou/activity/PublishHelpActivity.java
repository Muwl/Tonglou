package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
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

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.dialog.DateSelectDialog;
import cn.yunluosoft.tonglou.dialog.SubmitDialog;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.TimeUtils;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

/**
 * Created by Administrator on 2016/2/9.
 */
public class PublishHelpActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private EditText name;

    private EditText detail;

    private View tiemView;

    private TextView time;

    private int width;

    private RadioGroup group;

    private RadioButton lef;

    private RadioButton rig;

    private TextView ok;

    private View pro;

    private String sdate;

    private int flag;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 51:
                    sdate = (String) msg.obj;
                    time.setText(sdate);
                    break;

                case 552:
                    Intent intent=new Intent(PublishHelpActivity.this,AssistActivity.class);
                    if (group.getCheckedRadioButtonId()==R.id.title_rb_lef){
                        intent.putExtra("flag",0);
                    }else{
                        intent.putExtra("flag",1);
                    }
                    startActivity(intent);
                    finish();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_help);
        initView();
    }

    private void initView() {
        flag=getIntent().getIntExtra("flag",0);
        title = (TextView) findViewById(R.id.title_title);
        back = (ImageView) findViewById(R.id.title_back);
        name = (EditText) findViewById(R.id.publish_help_name);
        detail = (EditText) findViewById(R.id.publish_help_content);
        ok = (TextView) findViewById(R.id.publish_help_ok);
        tiemView=findViewById(R.id.publish_help_timeview);
        time= (TextView) findViewById(R.id.publish_help_time);
        pro = findViewById(R.id.publish_help_pro);
        group = (RadioGroup) findViewById(R.id.title_group);
        lef = (RadioButton) findViewById(R.id.title_rb_lef);
        rig = (RadioButton) findViewById(R.id.title_rb_rig);
        title.setVisibility(View.GONE);

        back.setOnClickListener(this);
        sdate = TimeUtils.getDate();
        time.setText(sdate);
        ok.setOnClickListener(this);
        tiemView.setOnClickListener(this);
        lef.setText("发布帮助");
        rig.setText("发布自荐");
        width = DensityUtil.getScreenWidth(this);
        group.setVisibility(View.VISIBLE);
        if (flag==0){
            group.check(R.id.title_rb_lef);
        }else{
            group.check(R.id.title_rb_rig);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.publish_help_timeview:
                DateSelectDialog selectDialog = new DateSelectDialog(
                        PublishHelpActivity.this,"选择日期", handler, sdate,1);
                break;

            case R.id.publish_help_ok:
                if (checkInput()){
                    sendPublish();
                }
                break;
        }
    }

    private boolean checkInput(){
        if (ToosUtils.isTextEmpty(name)){
            ToastUtils.displayShortToast(PublishHelpActivity.this, "名称不能为空！");
            return false;
        }
        if (ToosUtils.isTextEmpty(detail)){
            ToastUtils.displayShortToast(PublishHelpActivity.this,"概述不能为空！");
            return false;
        }
        if (ToosUtils.isTextEmpty(time)){
            ToastUtils.displayShortToast(PublishHelpActivity.this,"起点不能为空！");
            return false;
        }
        return  true;
    }

    private void sendPublish() {
        final Gson gson = new Gson();
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("topic", ToosUtils.getTextContent(name));
        rp.addBodyParameter("detail", ToosUtils.getTextContent(detail));
        rp.addBodyParameter("endDate", ToosUtils.getTextContent(time));
        if (group.getCheckedRadioButtonId()==R.id.title_rb_lef){
            rp.addBodyParameter("supplyType","0");
        }else{
            rp.addBodyParameter("supplyType","1");
        }
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH + "/v1_1_0/dynamic/helpSave",
                rp, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        pro.setVisibility(View.VISIBLE);
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        pro.setVisibility(View.GONE);
                        ToastUtils.displayFailureToast(PublishHelpActivity.this);
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
                            if (Constant.RETURN_OK.equals(state.msg)) {;
                                SubmitDialog dialog=new SubmitDialog(PublishHelpActivity.this,3,handler);
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        PublishHelpActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(PublishHelpActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        PublishHelpActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(PublishHelpActivity.this);
                        }

                    }
                });

    }
}
