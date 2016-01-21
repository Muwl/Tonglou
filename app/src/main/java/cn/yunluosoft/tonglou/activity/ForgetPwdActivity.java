package cn.yunluosoft.tonglou.activity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HeterogeneousExpandableList;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.utils.BPUtil;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.MD5Util;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

/**
 * @author Mu
 * @date 2015-8-1下午7:56:43
 * @description
 */
public class ForgetPwdActivity extends BaseActivity implements OnClickListener {

	private ImageView back;

	private TextView title;

	private EditText phone;

	private EditText code;

	private TextView getCode;

	private EditText pwd;

	private CheckBox show;

	private TextView ok;

	private View pro;

	private TimeCount time;

	private String sphone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_pwd);
		time = new TimeCount(60000, 1000);
		initView();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		phone = (EditText) findViewById(R.id.forgetpwd_name);
		code = (EditText) findViewById(R.id.forgetpwd_code);
		getCode = (TextView) findViewById(R.id.forgetpwd_getcode);
		pwd = (EditText) findViewById(R.id.forgetpwd_pwd);
		show = (CheckBox) findViewById(R.id.forgetpwd_show);
		ok = (TextView) findViewById(R.id.forgetpwd_ok);
		pro = findViewById(R.id.forgetpwd_pro);

		back.setOnClickListener(this);
		getCode.setOnClickListener(this);
		ok.setOnClickListener(this);

		title.setText("找回密码");

		show.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					// 如果选中，显示密码
					pwd.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				} else {
					// 否则隐藏密码
					pwd.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				}

			}
		});
	}

	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			getCode.setText("发送验证码");
			getCode.setClickable(true);
			getCode.setEnabled(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			getCode.setClickable(false);
			getCode.setEnabled(false);
			getCode.setText(millisUntilFinished / 1000 + "S");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.forgetpwd_getcode:
			if (checkPhoneInput()) {
				//sendMessage();
			}
			// time.start();
			break;

		case R.id.forgetpwd_ok:
			if (checkInput()) {
				//sendSub();
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 发送短信验证
	 * 
	 * @return
	 */
	private boolean checkPhoneInput() {
		if (ToosUtils.isTextEmpty(phone)) {
			ToastUtils.displayShortToast(this, "手机号不能为空！");
			return false;
		}
		if (!ToosUtils.MatchPhone(ToosUtils.getTextContent(phone))) {
			ToastUtils.displayShortToast(this, "输入的手机号格式错误！");
			return false;
		}
		return true;
	}

	/**
	 * 注册输入
	 * 
	 * @return
	 */
	private boolean checkInput() {
		String temp = code.getText().toString().trim();
		if (ToosUtils.isStringEmpty(temp) || !temp.equals(BPUtil.getCode())
				|| !sphone.equals(ToosUtils.getTextContent(phone))) {
			ToastUtils.displayShortToast(ForgetPwdActivity.this, "请输入正确的验证码");
			return false;
		}
		if (ToosUtils.isTextEmpty(phone)) {
			ToastUtils.displayShortToast(this, "手机号不能为空！");
			return false;
		}
		if (!ToosUtils.MatchPhone(ToosUtils.getTextContent(phone))) {
			ToastUtils.displayShortToast(this, "输入的手机号格式错误！");
			return false;
		}
		if (ToosUtils.isTextEmpty(pwd)) {
			ToastUtils.displayShortToast(this, "密码不能为空！");
			return false;
		}
		if (!ToosUtils.checkPwd(ToosUtils.getTextContent(pwd))) {
			ToastUtils.displayShortToast(this, "密码不能少于6位！");
			return false;
		}

		return true;

	}

//	// 发送短信
//	private void sendMessage() {
//
//		RequestParams rp = new RequestParams();
//		JsonObject jsonObject = new JsonObject();
//		jsonObject.addProperty("phone", ToosUtils.getTextContent(phone));
//		jsonObject.addProperty("content", BPUtil.createCode());
//		jsonObject.addProperty("type", "1");
//		rp.addBodyParameter("info", ToosUtils.getEncrypt(jsonObject.toString()));
//		HttpUtils utils = new HttpUtils();
//		utils.configTimeout(20000);
//		utils.send(HttpMethod.POST, Constant.ROOT_PATH + "/v1/sms/sendSms", rp,
//				new RequestCallBack<String>() {
//					@Override
//					public void onStart() {
//						pro.setVisibility(View.VISIBLE);
//						super.onStart();
//					}
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						pro.setVisibility(View.GONE);
//						ToastUtils.displayFailureToast(ForgetPwdActivity.this);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> arg0) {
//						pro.setVisibility(View.GONE);
//						Gson gson = new Gson();
//						try {
//							ReturnState state = gson.fromJson(arg0.result,
//									ReturnState.class);
//							LogManager.LogShow("----", arg0.result,
//									LogManager.ERROR);
//							if (Constant.RETURN_OK.equals(state.msg)) {
//								ToastUtils.displayShortToast(
//										ForgetPwdActivity.this, "发送成功！");
//								sphone = ToosUtils.getTextContent(phone);
//								time.start();
//							} else {
//								ToastUtils.displayShortToast(
//										ForgetPwdActivity.this,
//										(String) state.result);
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//							ToastUtils
//									.displaySendFailureToast(ForgetPwdActivity.this);
//						}
//
//					}
//
//				});
//
//	}

//	/**
//	 * 联网注册
//	 */
//	private void sendSub() {
//		RequestParams rp = new RequestParams();
//		JsonObject jsonObject = new JsonObject();
//		jsonObject.addProperty("username", ToosUtils.getTextContent(phone));
//		jsonObject.addProperty("password",
//				MD5Util.MD5(ToosUtils.getTextContent(pwd)));
//		rp.addBodyParameter("info", ToosUtils.getEncrypt(jsonObject.toString()));
//		HttpUtils utils = new HttpUtils();
//		utils.configTimeout(20000);
//		utils.send(HttpMethod.POST, Constant.ROOT_PATH + "/v1/user/foundPwd",
//				rp, new RequestCallBack<String>() {
//					@Override
//					public void onStart() {
//						pro.setVisibility(View.VISIBLE);
//						super.onStart();
//					}
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						pro.setVisibility(View.GONE);
//						ToastUtils.displayFailureToast(ForgetPwdActivity.this);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> arg0) {
//						pro.setVisibility(View.GONE);
//						try {
//							Gson gson = new Gson();
//							LogManager.LogShow("----", arg0.result,
//									LogManager.ERROR);
//							ReturnState state = gson.fromJson(arg0.result,
//									ReturnState.class);
//							if (Constant.RETURN_OK.equals(state.msg)) {
//								ToastUtils.displayShortToast(
//										ForgetPwdActivity.this,
//										(String) state.result);
//								Intent intent = new Intent(
//										ForgetPwdActivity.this,
//										LoginActivity.class);
//								startActivity(intent);
//								finish();
//							} else {
//								ToastUtils.displayShortToast(
//										ForgetPwdActivity.this,
//										(String) state.result);
//							}
//						} catch (Exception e) {
//							ToastUtils
//									.displaySendFailureToast(ForgetPwdActivity.this);
//						}
//
//					}
//				});
//
//	}

}
