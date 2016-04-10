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
import cn.yunluosoft.tonglou.adapter.PublishUsedAdapter;
import cn.yunluosoft.tonglou.dialog.SubmitDialog;
import cn.yunluosoft.tonglou.model.PGroupState;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.MyGridView;

/**
 * Created by Administrator on 2016/2/9.
 */
public class PublishPPActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private EditText name;

    private EditText detail;

    private EditText start;

    private EditText stop;

    private TextView delcontent;

    private int width;

    private RadioGroup group;

    private RadioButton lef;

    private RadioButton rig;

    private TextView ok;

    private View pro;

    private int flag;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 552:
                    Intent intent=new Intent(PublishPPActivity.this,PPActivity.class);
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
        setContentView(R.layout.publish_pp);
        initView();
    }

    private void initView() {
        flag=getIntent().getIntExtra("flag",0);
        title = (TextView) findViewById(R.id.title_title);
        back = (ImageView) findViewById(R.id.title_back);
        name = (EditText) findViewById(R.id.publish_pp_name);
        detail = (EditText) findViewById(R.id.publish_pp_content);
        ok = (TextView) findViewById(R.id.publish_pp_ok);
        delcontent= (TextView) findViewById(R.id.publish_pp_delcontent);
        start= (EditText) findViewById(R.id.publish_pp_start);
        stop= (EditText) findViewById(R.id.publish_pp_stop);
        pro = findViewById(R.id.publish_pp_pro);
        group = (RadioGroup) findViewById(R.id.title_group);
        lef = (RadioButton) findViewById(R.id.title_rb_lef);
        rig = (RadioButton) findViewById(R.id.title_rb_rig);
        title.setVisibility(View.GONE);

        back.setOnClickListener(this);
        ok.setOnClickListener(this);
        lef.setText("拼车发布");
        rig.setText("求带发布");
        width = DensityUtil.getScreenWidth(this);
        group.setVisibility(View.VISIBLE);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.title_rb_lef) {
                    name.setHint("车主+内容（10字之内）");
                    delcontent.setText("拼车详情");
                    detail.setHint("车型+上下班时间+金额+……（100字之内）");
                } else {
                    name.setHint("乘客+内容（10字之内）");
                    delcontent.setText("求带详情");
                    detail.setHint("上下班时间+金额+……（100字之内）");
                }
            }
        });

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

            case R.id.publish_pp_ok:
                if (checkInput()){
                    sendPublish();
                }

                break;
        }
    }


    private boolean checkInput(){
        if (ToosUtils.isTextEmpty(name)){
            ToastUtils.displayShortToast(PublishPPActivity.this, "名称不能为空！");
            return false;
        }
        if (ToosUtils.isTextEmpty(detail)){
            ToastUtils.displayShortToast(PublishPPActivity.this,"概述不能为空！");
            return false;
        }
        if (ToosUtils.isTextEmpty(start)){
            ToastUtils.displayShortToast(PublishPPActivity.this,"起点不能为空！");
            return false;
        }
        if (ToosUtils.isTextEmpty(stop)){
            ToastUtils.displayShortToast(PublishPPActivity.this,"终点不能为空！");
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
        rp.addBodyParameter("start", ToosUtils.getTextContent(start));
        rp.addBodyParameter("end", ToosUtils.getTextContent(stop));
        if (group.getCheckedRadioButtonId()==R.id.title_rb_lef){
            rp.addBodyParameter("supplyType","0");
        }else{
            rp.addBodyParameter("supplyType","1");
        }
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH + "/v1_1_0/dynamic/carSharingSave",
                rp, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        pro.setVisibility(View.VISIBLE);
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        pro.setVisibility(View.GONE);
                        ToastUtils.displayFailureToast(PublishPPActivity.this);
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
                                SubmitDialog dialog=new SubmitDialog(PublishPPActivity.this,2,handler);
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        PublishPPActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(PublishPPActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        PublishPPActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(PublishPPActivity.this);
                        }

                    }
                });

    }
}
