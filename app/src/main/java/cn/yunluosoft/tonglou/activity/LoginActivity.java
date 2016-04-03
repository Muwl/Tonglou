package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import cn.yunluosoft.tonglou.model.LoginEntity;
import cn.yunluosoft.tonglou.model.RegisterEntity;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.MD5Util;
import cn.yunluosoft.tonglou.utils.MyApplication;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
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
 * @date 2015-8-1上午10:18:01
 * @description
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private ImageView back;

	private TextView title;

	private TextView register;

	private EditText name;

	private EditText pwd;

	private CheckBox show;

	private TextView login;

	private TextView forget;

	private View pro;

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
		setContentView(R.layout.login);
		initView();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		register = (TextView) findViewById(R.id.title_rig);
		name = (EditText) findViewById(R.id.login_name);
		pwd = (EditText) findViewById(R.id.login_pwd);
		show = (CheckBox) findViewById(R.id.login_pwd_show);
		login = (TextView) findViewById(R.id.login_login);
		forget = (TextView) findViewById(R.id.login_forgetpwd);
		pro = findViewById(R.id.login_pro);

		title.setText("登录");
		register.setText("注册");
		register.setOnClickListener(this);
		login.setOnClickListener(this);
		forget.setOnClickListener(this);
		back.setVisibility(View.GONE);
		register.setVisibility(View.VISIBLE);

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

	/**
	 * 验证输入
	 * 
	 * @return
	 */
	private boolean checkInput() {

		if (ToosUtils.isTextEmpty(name)) {
			ToastUtils.displayShortToast(this, "手机号不能为空！");
			return false;
		}
		if (!ToosUtils.MatchPhone(ToosUtils.getTextContent(name))) {
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_rig:
			Intent intent2 = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(intent2);
			break;
		case R.id.login_login:
			if (checkInput()) {
				sendLogin();
			}
			break;

		case R.id.login_forgetpwd:

			Intent intent = new Intent(LoginActivity.this,
					ForgetPwdActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 联网登陆
	 */
	private void sendLogin() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("username", ToosUtils.getTextContent(name));
		jsonObject.addProperty("password",
				MD5Util.MD5(ToosUtils.getTextContent(pwd)));
		RequestParams rp = new RequestParams();
		rp.addBodyParameter("info", ToosUtils.getEncrypt(jsonObject.toString()));
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		LogManager.LogShow("---",ToosUtils.getEncrypt(jsonObject.toString()),LogManager.ERROR);
		utils.send(HttpMethod.POST, Constant.ROOT_PATH + "/v1/user/login", rp,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						pro.setVisibility(View.VISIBLE);
						super.onStart();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						pro.setVisibility(View.GONE);
						ToastUtils.displayFailureToast(LoginActivity.this);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						pro.setVisibility(View.GONE);

						try {
							Gson gson = new Gson();
							ReturnState state = gson.fromJson(arg0.result,
									ReturnState.class);
							if (Constant.RETURN_OK.equals(state.msg)) {
								LogManager
										.LogShow("------",
												(String) state.result,
												LogManager.ERROR);
								String temp = ToosUtils
										.getEncryptto((String) state.result);
								LogManager.LogShow("------", temp,
										LogManager.ERROR);
								LoginEntity entity = gson.fromJson(temp,
										LoginEntity.class);
								ShareDataTool.SaveInfo(LoginActivity.this,
										entity.token, entity.userId,
										entity.imUsername, entity.imPassword);
								ShareDataTool.SaveInfoDetail(
										LoginActivity.this, entity.nickname,
										entity.icon, entity.buildingName,entity.buildingId);
								ShareDataTool.SaveFlag(LoginActivity.this, 1);
								// ToastUtils.displayShortToast(
								// LoginActivity.this, "登陆成功");
								// Intent intent = new
								// Intent(LoginActivity.this,
								// MainActivity.class);
								// startActivity(intent);
								loginHX(entity.imUsername, entity.imPassword);
							} else if (Constant.USER_NOCOM.equals(state.msg)) {
								String temp = ToosUtils
										.getEncryptto((String) state.result);
								LogManager
										.LogShow("------206",
												temp,
												LogManager.ERROR);
								LoginEntity entity = gson.fromJson(temp,
										LoginEntity.class);
								ShareDataTool.SaveInfo(LoginActivity.this,
										entity.token, entity.userId,
										entity.imUsername, entity.imPassword);
								ShareDataTool.SaveInfoDetail(
										LoginActivity.this, "",
										"",entity.buildingName, entity.buildingId);
								ShareDataTool.SaveFlag(LoginActivity.this, 0);
//								ToastUtils.displayShortToast(
//										LoginActivity.this, "登陆成功，请完善信息");
								if (ToosUtils.isStringEmpty(entity.buildingId)){
									Intent intent = new Intent(LoginActivity.this,LocationSelActivity.class);
									intent.putExtra("flag", 0);
									startActivity(intent);
								}else{
									loginHX(entity.imUsername, entity.imPassword);
								}

							} else {
								ToastUtils.displayShortToast(
										LoginActivity.this,
										(String) state.result);
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils
									.displaySendFailureToast(LoginActivity.this);
						}

					}
				});
	}

	// 点击返回按钮
	@Override
	public void onBackPressed() {
		// if (ToosUtils.isApplicationBroughtToBackground(LoginActivity.this)) {
		// if (canExit) {
		// finish();
		// MyApplication.getInstance().exit();
		// } else {
		// Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
		// canExit = true;
		// handler.sendEmptyMessageDelayed(0, 2000);
		// }
		// Log.e("0000000", "推出..................");
		for (int i = 0; i < MyApplication.getInstance().getActivities().size(); i++) {
			if (MyApplication.getInstance().getActivities().get(i) != null
					&& !LoginActivity.class.equals(MyApplication.getInstance()
							.getActivities().get(i).getClass())) {
				MyApplication.getInstance().getActivities().get(i).finish();
			}
		}
		moveTaskToBack(true);
		// super.onBackPressed();
		// return true;
		// } else {
		// super.onBackPressed();
		// }
	}

	private void loginHX(final String currentUsername,
			final String currentPassword) {
		pro.setVisibility(View.VISIBLE);
		EMChatManager.getInstance().login(currentUsername, currentPassword,
				new EMCallBack() {
					@Override
					public void onSuccess() {
						// pro.setVisibility(View.GONE);
						// 登陆成功，保存用户名密码
						MyApplication.getInstance()
								.setUserName(currentUsername);
						MyApplication.getInstance()
								.setPassword(currentPassword);

						try {
							// pro.setVisibility(View.GONE);
							// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
							// ** manually load all local groups and
							// conversations in case we are auto login
							EMChatManager.getInstance().updateCurrentUserNick(
									ShareDataTool
											.getNickname(LoginActivity.this));
							EMGroupManager.getInstance().loadAllGroups();
							EMChatManager.getInstance().loadAllConversations();
						} catch (Exception e) {
							e.printStackTrace();
							// 取好友或者群聊失败，不让进入主页面
							runOnUiThread(new Runnable() {
								public void run() {
									MyApplication.getInstance().logout(null);
									ToastUtils.displayShortToast(
											LoginActivity.this, "登录失败");
								}
							});
							return;
						}

						// 进入主页面
						LogManager.LogShow("-----", "登录成功", LogManager.ERROR);

						startActivity(new Intent(LoginActivity.this,
								MainActivity.class));
						finish();
					}

					@Override
					public void onProgress(int progress, String status) {
					}

					@Override
					public void onError(final int code, final String message) {

						runOnUiThread(new Runnable() {
							public void run() {
								pro.setVisibility(View.GONE);
								ToastUtils.displayShortToast(
										LoginActivity.this, "登录失败");
							}
						});
					}
				});

	}

}
