package cn.yunluosoft.tonglou.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import cn.yunluosoft.tonglou.easemob.chatuidemo.DemoHXSDKHelper;
import cn.yunluosoft.tonglou.easemob.chatuidemo.domain.User;

import com.easemob.EMCallBack;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;

public class MyApplication extends Application {

	private static final String TAG = MyApplication.class.getName();

	private List<Activity> list = new ArrayList<Activity>();

	private static MyApplication instance;

	public static Context applicationContext;

	// login user name
	public final String PREF_USERNAME = "username";

	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	public static MyApplication getInstance() {
		return instance;
	}

	public void addActivity(Activity activity) {
		list.add(activity);
	}

	public List<Activity> getActivities() {
		return list;
	}

	public void exit() {
		for (Activity activity : list) {
			if (activity != null) {
				activity.finish();
			}
		}
		MobclickAgent.onKillProcess(this);
		// android.os.Process.killProcess(android.os.Process.myPid());
//		System.exit(0);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = this;
		instance = this;
		hxSDKHelper.onInit(applicationContext);
		PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
		//微信 appid appsecret
		PlatformConfig.setSinaWeibo("3921700954","04b48b094faeb16683c32669824ebdad");
		//新浪微博 appkey appsecret
		PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
		// QQ和Qzone appid appkey
		PlatformConfig.setAlipay("2015111700822536");

	}

	public String getUserName() {
		return hxSDKHelper.getHXId();
	}

	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	public void setUserName(String username) {
		hxSDKHelper.setHXId(username);
	}
	public void setPassword(String pwd) {
		hxSDKHelper.setPassword(pwd);
	}

	public Map<String, User> getContactList() {
		return hxSDKHelper.getContactList();
	}

	public void setContactList(Map<String, User> contactList) {
		hxSDKHelper.setContactList(contactList);
	}
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
		hxSDKHelper.logout(false, emCallBack);
	}
}
