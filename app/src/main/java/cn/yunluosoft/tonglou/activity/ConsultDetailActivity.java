package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
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

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.ConsultDetailAdapter;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.model.User;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

/**
 * Created by Mu on 2016/2/1.
 */
public class ConsultDetailActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;

    private TextView title;

    private ImageView share;

    private WebView webView;

    private TextView atten;

    private TextView read;

    private TextView report;

    private TextView message;

    private ListView listView;

    private ConsultDetailAdapter adapter;

    private View pro;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consult_detail);
        id=getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        share= (ImageView) findViewById(R.id.title_share);
        webView= (WebView) findViewById(R.id.consult_detail_web);
        atten= (TextView) findViewById(R.id.consult_detail_atten);
        read= (TextView) findViewById(R.id.consult_detail_read);
        report= (TextView) findViewById(R.id.consult_detail_report);
        message= (TextView) findViewById(R.id.consult_detail_message);
        listView= (android.widget.ListView) findViewById(R.id.consult_detail_list);
        pro=findViewById(R.id.consult_detail_pro);

        WebView webView = new WebView(this);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        webView.loadUrl(Constant.ROOT_PATH+"/share/news?newsId="+id);
        back.setOnClickListener(this);
        title.setText("资讯详情");
        share.setVisibility(View.VISIBLE);
        share.setOnClickListener(this);
        atten.setOnClickListener(this);
        read.setOnClickListener(this);
        message.setOnClickListener(this);
        report.setOnClickListener(this);
        adapter=new ConsultDetailAdapter(this);
        listView.setAdapter(adapter);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.title_share:
                break;

            case R.id.consult_detail_atten:
                AddPraise();
                break;
            case R.id.consult_detail_report:
                break;
            case R.id.consult_detail_message:
                Intent intent=new Intent(ConsultDetailActivity.this,WriteMessageActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * 添加新闻点赞
     */
    private void AddPraise() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("Id", id);
        String url="/v1_1_0/news/praise";
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
                        ToastUtils.displayFailureToast(ConsultDetailActivity.this);
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
                                ToastUtils.displayShortToast(ConsultDetailActivity.this,
                                        "操作成功");
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        ConsultDetailActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(ConsultDetailActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        ConsultDetailActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(ConsultDetailActivity.this);
                        }

                    }
                });

    }

    /**
     * 添加新闻评论点赞
     */
    private void AddCommentPraise() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("Id", id);
        String url="/v1_1_0/newsComment/praise";
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
                        ToastUtils.displayFailureToast(ConsultDetailActivity.this);
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
                                ToastUtils.displayShortToast(ConsultDetailActivity.this,
                                        "操作成功");
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        ConsultDetailActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(ConsultDetailActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        ConsultDetailActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(ConsultDetailActivity.this);
                        }

                    }
                });

    }
}
