package cn.yunluosoft.tonglou.activity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

/**
 * @author Mu
 * @date 2015-8-4 下午2:24:53
 * @Description
 */
public class FreebackActivity extends BaseActivity implements OnClickListener {

    private TextView title;

    private ImageView back;

    private EditText content;

    private TextView commite;

    private View pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freeback);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title_title);
        back = (ImageView) findViewById(R.id.title_back);
        commite = (TextView) findViewById(R.id.freeback_ok);
        content = (EditText) findViewById(R.id.freeback_context);
        pro = findViewById(R.id.freeback_pro);

        title.setText("意见反馈");
        commite.setText("提交");
        back.setOnClickListener(this);
        commite.setVisibility(View.VISIBLE);
        commite.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.freeback_ok:
			if (checkInput()) {
				sendFreeback();
			}
                break;

            default:
                break;
        }
    }

    private boolean checkInput() {
        if (ToosUtils.isTextEmpty(content)) {
            ToastUtils.displayShortToast(this, "请输入反馈意见");
            return false;
        }
        return true;
    }

	private void sendFreeback() {
		final Gson gson = new Gson();
		RequestParams rp = new RequestParams();
		rp.addBodyParameter("sign", ShareDataTool.getToken(this));
		rp.addBodyParameter("content", ToosUtils.getTextContent(content));
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		utils.send(HttpMethod.POST, Constant.ROOT_PATH + "/v1/feedback/save",
				rp, new RequestCallBack<String>() {
					@Override
					public void onStart() {
						pro.setVisibility(View.VISIBLE);
						super.onStart();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						pro.setVisibility(View.GONE);
						ToastUtils.displayFailureToast(FreebackActivity.this);
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
										FreebackActivity.this,
										String.valueOf(state.result));
								finish();
							} else if (Constant.TOKEN_ERR.equals(state.msg)) {
								ToastUtils.displayShortToast(
										FreebackActivity.this, "验证错误，请重新登录");
								ToosUtils.goReLogin(FreebackActivity.this);
							} else {
								ToastUtils.displayShortToast(
										FreebackActivity.this,
										String.valueOf(state.result));
							}
						} catch (Exception e) {
							ToastUtils
									.displaySendFailureToast(FreebackActivity.this);
						}

					}
				});

	}
}
