package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.model.RegisterEntity;
import cn.yunluosoft.tonglou.model.RegisterSubEntity;
import cn.yunluosoft.tonglou.model.RegisterSubLocate;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.BPUtil;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.MD5Util;
import cn.yunluosoft.tonglou.utils.MyApplication;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author Mu
 * @date 2015-8-1下午7:56:43
 * @description 
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {

	private ImageView back;

	private TextView title;

	private TextView login;

	private EditText phone;

	private EditText code;

	private TextView getCode;

	private EditText pwd;

	private CheckBox show;

	private TextView ok;

	private View pro;

	private TimeCount time;

	private TextView protocol;

	private String sphone;

	private LocationClient mLocationClient;

	private LocationMode tempMode = LocationMode.Hight_Accuracy;

	private String tempcoor = "gcj02";

	public MyLocationListener mMyLocationListener;

	private BDLocation bdLocation = null;

	private boolean canExit = false;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				canExit = false;
				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		InitLocation();
		time = new TimeCount(60000, 1000);
		initView();
	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 1000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		// option.setIsNeedAddress(checkGeoLocation.isChecked());
		mLocationClient.setLocOption(option);
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location != null) {
				bdLocation = location;
				sendSub();
				mLocationClient.stop();

			}

		}

	}

	private void initView() {
		back = (ImageView) findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		login = (TextView) findViewById(R.id.title_rig);
		phone = (EditText) findViewById(R.id.register_name);
		code = (EditText) findViewById(R.id.register_code);
		getCode = (TextView) findViewById(R.id.register_getcode);
		pwd = (EditText) findViewById(R.id.register_pwd);
		show = (CheckBox) findViewById(R.id.register_show);
		ok = (TextView) findViewById(R.id.register_ok);
		protocol = (TextView) findViewById(R.id.register_protocol);
		pro = findViewById(R.id.register_pro);

		back.setOnClickListener(this);
		login.setOnClickListener(this);
		login.setVisibility(View.VISIBLE);
		getCode.setOnClickListener(this);
		protocol.setOnClickListener(this);
		ok.setOnClickListener(this);
		back.setVisibility(View.GONE);
		title.setText("注册");
		login.setText("登录");

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
		case R.id.register_getcode:
			if (checkPhoneInput()) {
				sendMessage();
			}
			break;

		case R.id.register_ok:
			if (checkInput()) {
				pro.setVisibility(View.VISIBLE);
				mLocationClient.start();

			}
			break;
		case R.id.title_rig:
			Intent intent = new Intent(RegisterActivity.this,
					LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.register_protocol:
			Intent intent2 = new Intent(RegisterActivity.this,
					ProtocolActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStop() {
		mLocationClient.stop();
		super.onStop();
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
			ToastUtils.displayShortToast(RegisterActivity.this, "请输入正确的验证码");
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

	// 发送短信
	private void sendMessage() {

		RequestParams rp = new RequestParams();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("phone", ToosUtils.getTextContent(phone));
		jsonObject.addProperty("content", BPUtil.createCode());
		LogManager.LogShow("----", BPUtil.getCode(), LogManager.ERROR);
		jsonObject.addProperty("type", "0");
		rp.addBodyParameter("info", ToosUtils.getEncrypt(jsonObject.toString()));
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		utils.send(HttpMethod.POST, Constant.ROOT_PATH + "/v1/sms/sendSms", rp,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						pro.setVisibility(View.VISIBLE);
						super.onStart();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						pro.setVisibility(View.GONE);
						ToastUtils.displayFailureToast(RegisterActivity.this);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						pro.setVisibility(View.GONE);
						Gson gson = new Gson();
						try {
							ReturnState state = gson.fromJson(arg0.result,
									ReturnState.class);
							LogManager.LogShow("----", arg0.result,
									LogManager.ERROR);
							if (Constant.RETURN_OK.equals(state.msg)) {
								ToastUtils.displayShortToast(
										RegisterActivity.this, "发送成功！");
								sphone = ToosUtils.getTextContent(phone);
								time.start();
							} else {
								ToastUtils.displayShortToast(
										RegisterActivity.this,
										(String) state.result);
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils
									.displaySendFailureToast(RegisterActivity.this);
						}

					}

				});

	}

	/**
	 * 联网注册
	 */
	private void sendSub() {
		RequestParams rp = new RequestParams();
		JsonObject jsonObject = new JsonObject();
		RegisterSubEntity registerEntity = new RegisterSubEntity();
		RegisterSubLocate locate = new RegisterSubLocate();
		locate.x = bdLocation.getLongitude();
		locate.y = bdLocation.getLatitude();
		registerEntity.username = ToosUtils.getTextContent(phone);
		registerEntity.password = MD5Util.MD5(ToosUtils.getTextContent(pwd));
		registerEntity.coordinates = locate;
		Gson gson = new Gson();
		rp.addBodyParameter("info",
				ToosUtils.getEncrypt(gson.toJson(registerEntity)));
		LogManager.LogShow("---",ToosUtils.getEncrypt(gson.toJson(registerEntity)),LogManager.ERROR);
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		utils.send(HttpMethod.POST, Constant.ROOT_PATH + "/v1/user/register",
				rp, new RequestCallBack<String>() {
					@Override
					public void onStart() {
						// pro.setVisibility(View.VISIBLE);
						super.onStart();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						pro.setVisibility(View.GONE);
						ToastUtils.displayFailureToast(RegisterActivity.this);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						pro.setVisibility(View.GONE);
						try {
							Gson gson = new Gson();
							LogManager.LogShow("----", arg0.result,
									LogManager.ERROR);
							ReturnState state = gson.fromJson(arg0.result,
									ReturnState.class);
							if (Constant.RETURN_OK.equals(state.msg)) {
								String temp = ToosUtils
										.getEncryptto((String) state.result);
								RegisterEntity entity = gson.fromJson(temp,
										RegisterEntity.class);
								ShareDataTool.SaveInfo(RegisterActivity.this,
										entity.token, entity.userId,
										entity.imUsername, entity.imPassword);
								ShareDataTool
										.SaveFlag(RegisterActivity.this, 0);
								ToastUtils.displayShortToast(
										RegisterActivity.this, "注册成功，请完善信息");
								Intent intent = new Intent(
										RegisterActivity.this,
										PerfectDataActivity.class);
								startActivity(intent);

							} else {
								ToastUtils.displayShortToast(
										RegisterActivity.this,
										(String) state.result);
							}
						} catch (Exception e) {
							ToastUtils
									.displaySendFailureToast(RegisterActivity.this);
						}

					}
				});

	}

}
