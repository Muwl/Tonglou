package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

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
public class PublishUsedActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private EditText name;

    private EditText detail;

    private MyGridView gridView;

    private PublishUsedAdapter adapter;

    private int width;

    private RadioGroup group;

    private RadioButton lef;

    private RadioButton rig;

    private TextView ok;

    private View pro;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 552:
                    finish();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_used);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title_title);
        back = (ImageView) findViewById(R.id.title_back);
        name = (EditText) findViewById(R.id.publish_used_name);
        detail = (EditText) findViewById(R.id.publish_used_content);
        gridView = (MyGridView) findViewById(R.id.publish_used_grid);
        ok = (TextView) findViewById(R.id.publish_used_ok);
        pro = findViewById(R.id.publish_used_pro);
        group = (RadioGroup) findViewById(R.id.title_group);
        lef = (RadioButton) findViewById(R.id.title_rb_lef);
        rig = (RadioButton) findViewById(R.id.title_rb_rig);
        title.setVisibility(View.GONE);

        back.setOnClickListener(this);
        ok.setOnClickListener(this);
        lef.setText("转让发布");
        rig.setText("求购发布");
        width = DensityUtil.getScreenWidth(this);
        adapter = new PublishUsedAdapter(this, width);
        gridView.setAdapter(adapter);
        group.setVisibility(View.VISIBLE);
        group.check(R.id.title_rb_lef);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.publish_used_ok:
                if (checkInput()){
                    sendPublish();
                }

                break;
        }
    }

    private boolean checkInput(){
        if (ToosUtils.isTextEmpty(name)){
            ToastUtils.displayShortToast(PublishUsedActivity.this, "名称不能为空！");
            return false;
        }
        if (ToosUtils.isTextEmpty(detail)){
            ToastUtils.displayShortToast(PublishUsedActivity.this,"概述不能为空！");
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
        if (group.getCheckedRadioButtonId()==R.id.title_rb_lef){
            rp.addBodyParameter("supplyType","0");
        }else{
            rp.addBodyParameter("supplyType","1");
        }
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH + "/v1_1_0/dynamic/secondhandSave",
                rp, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        pro.setVisibility(View.VISIBLE);
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        pro.setVisibility(View.GONE);
                        ToastUtils.displayFailureToast(PublishUsedActivity.this);
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
                                SubmitDialog dialog=new SubmitDialog(PublishUsedActivity.this,4,handler);
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        PublishUsedActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(PublishUsedActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        PublishUsedActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(PublishUsedActivity.this);
                        }

                    }
                });

    }
}
