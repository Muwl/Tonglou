package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
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

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.dialog.DateSelectDialog;
import cn.yunluosoft.tonglou.dialog.SubmitDialog;
import cn.yunluosoft.tonglou.model.PGroupEntity;
import cn.yunluosoft.tonglou.model.PGroupState;
import cn.yunluosoft.tonglou.model.PersonInfo;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.TimeUtils;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

/**
 * Created by Administrator on 2016/2/9.
 */
public class PublishGroupActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private EditText name;

    private EditText detail;

    private View timeView;

    private TextView time;

    private View numView;

    private EditText num;

    private ToggleButton toggleButton;

    private TextView ok;


    private String sdate;


    private View pro;

    private PGroupEntity pGroupEntity;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 51:
                    sdate = (String) msg.obj;
                    time.setText(sdate);
                    break;

                case 552:
                    finish();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_group);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title_title);
        back = (ImageView) findViewById(R.id.title_back);
        name = (EditText) findViewById(R.id.publish_group_name);
        detail = (EditText) findViewById(R.id.publish_group_content);
        timeView = findViewById(R.id.publish_group_timeview);
        time = (TextView) findViewById(R.id.publish_group_time);
        numView = findViewById(R.id.publish_group_numview);
        num = (EditText) findViewById(R.id.publish_group_num);
        toggleButton = (ToggleButton) findViewById(R.id.publish_group_toggle);
        ok = (TextView) findViewById(R.id.publish_group_ok);
        pro = findViewById(R.id.publish_group_pro);

        sdate = TimeUtils.getDate();
        time.setText(sdate);
        title.setText("发布活动");
        back.setOnClickListener(this);
        timeView.setOnClickListener(this);
//        numView.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.publish_group_timeview:
                DateSelectDialog selectDialog = new DateSelectDialog(
                        PublishGroupActivity.this, handler, sdate);
                break;

//            case R.id.publish_group_numview:
//
//                break;

            case R.id.publish_group_ok:
                if (checkInput()){
                    sendPublish();
                }

                break;
        }

    }

    private boolean checkInput(){
        if (ToosUtils.isTextEmpty(name)){
            ToastUtils.displayShortToast(PublishGroupActivity.this,"名称不能为空！");
            return false;
        }
        if (ToosUtils.isTextEmpty(detail)){
            ToastUtils.displayShortToast(PublishGroupActivity.this,"详情不能为空！");
            return false;
        }
        if (ToosUtils.isTextEmpty(num)){
            ToastUtils.displayShortToast(PublishGroupActivity.this,"人数不能为空！");
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
        rp.addBodyParameter("endDate", sdate);
        rp.addBodyParameter("planPeopleNum", ToosUtils.getTextContent(num));
        if (toggleButton.isChecked()){
            rp.addBodyParameter("applyState","0");
        }else{
            rp.addBodyParameter("applyState","1");
        }
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH + "/v1_1_0/dynamic/activitySave",
                rp, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        pro.setVisibility(View.VISIBLE);
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        pro.setVisibility(View.GONE);
                        ToastUtils.displayFailureToast(PublishGroupActivity.this);
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
                                PGroupState pGroupState=gson.fromJson(arg0.result,PGroupState.class);
                                pGroupEntity=pGroupState.result;
                                SubmitDialog dialog=new SubmitDialog(PublishGroupActivity.this,1,handler);
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        PublishGroupActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(PublishGroupActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        PublishGroupActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(PublishGroupActivity.this);
                        }

                    }
                });

    }
}
