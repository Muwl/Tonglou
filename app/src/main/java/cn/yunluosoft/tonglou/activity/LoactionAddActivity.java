package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.dialog.AreaDialog;
import cn.yunluosoft.tonglou.model.PerfectDataState;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

/**
 * Created by Mu on 2016/1/26.
 */
public class LoactionAddActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private EditText name;

    private View provinceView;

    private TextView province;

    private EditText detailAddress;

    private TextView ok;

    private View pro;

    private String[] strings;

    private int flag;// 0代表注册 1代表修改

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1004:
                    String s= (String) msg.obj;
                    strings=s.split("\\*\\*");
                    province.setText(strings[0]+strings[1]);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_add);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title_title);
        back= (ImageView) findViewById(R.id.title_back);
        flag = getIntent().getIntExtra("flag", 0);
        name= (EditText) findViewById(R.id.location_add_name);
        provinceView=findViewById(R.id.location_add_provinceview);
        province= (TextView) findViewById(R.id.location_add_province);
        detailAddress= (EditText) findViewById(R.id.location_add_detailaddress);
        ok= (TextView) findViewById(R.id.location_add_ok);
        pro=findViewById(R.id.location_add_pro);

        title.setText("楼宇地址收录");
        back.setOnClickListener(this);
        provinceView.setOnClickListener(this);
        ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.location_add_ok:
                if (checkInput()){
                    AddAddress();
                }

                break;

            case R.id.location_add_provinceview:
                AreaDialog dialog=new AreaDialog(LoactionAddActivity.this,handler);
                break;


        }
    }

    private boolean checkInput(){
        if (ToosUtils.isTextEmpty(name)){
            ToastUtils.displayShortToast(LoactionAddActivity.this,"请输入楼宇名称！");
            return false;
        }
        if (ToosUtils.isTextEmpty(province)){
            ToastUtils.displayShortToast(LoactionAddActivity.this,"请输入省/市！");
            return  false;
        }
        if (ToosUtils.isTextEmpty(detailAddress)){
            ToastUtils.displayShortToast(LoactionAddActivity.this,"请输入详细地址！");
            return  false;
        }
        return true;
    }


    private void AddAddress() {
        RequestParams rp = new RequestParams();
        final Gson gson = new Gson();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("buildingName", ToosUtils.getTextContent(name));
        rp.addBodyParameter("city", strings[1]);
        rp.addBodyParameter("province",strings[0]);
        rp.addBodyParameter("address",ToosUtils.getTextContent(detailAddress));
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH
                        + "/v1_1_0/buildingCollection/save", rp,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        pro.setVisibility(View.VISIBLE);
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        pro.setVisibility(View.GONE);
                        ToastUtils.displayFailureToast(LoactionAddActivity.this);
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
                                        LoactionAddActivity.this, "提交成功");
                                Intent intent=new Intent(LoactionAddActivity.this,LocationAddSucActivity.class);
                                intent.putExtra("flag",flag);
                                startActivity(intent);
                                finish();
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        LoactionAddActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(LoactionAddActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        LoactionAddActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(LoactionAddActivity.this);
                        }

                    }
                });

    }
}
