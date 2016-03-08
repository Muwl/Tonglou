package cn.yunluosoft.tonglou.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.easemob.chatuidemo.DemoHXSDKHelper;
import cn.yunluosoft.tonglou.easemob.chatuidemo.domain.User;

import com.easemob.EMCallBack;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
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

	private PushAgent mPushAgent;

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
		PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
		//新浪微博 appkey appsecret
		PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
		// QQ和Qzone appid appkey
		PlatformConfig.setAlipay("2015111700822536");
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.setDebugMode(true);


		UmengMessageHandler messageHandler = new UmengMessageHandler(){
			/**
			 * 参考集成文档的1.6.3
			 * http://dev.umeng.com/push/android/integration#1_6_3
			 * */
			@Override
			public void dealWithCustomMessage(final Context context, final UMessage msg) {
				new Handler().post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 对自定义消息的处理方式，点击或者忽略
						boolean isClickOrDismissed = true;
						if(isClickOrDismissed) {
							//自定义消息的点击统计
							UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
						} else {
							//自定义消息的忽略统计
							UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
						}
						Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
					}
				});
			}

			/**
			 * 参考集成文档的1.6.4
			 * http://dev.umeng.com/push/android/integration#1_6_4
			 * */
			@Override
			public Notification getNotification(Context context,
												UMessage msg) {

				LogManager.LogShow("------------","shoudao-----------",LogManager.ERROR);
				LogManager.LogShow("------------","msg-----------"+new Gson().toJson(msg.extra),LogManager.ERROR);
				switch (msg.builder_id) {
					case 1:
						android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context);
						RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
						myNotificationView.setTextViewText(R.id.notification_title, msg.title);
						myNotificationView.setTextViewText(R.id.notification_text, msg.text);
						myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
						myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
						builder.setContent(myNotificationView)
								.setContentTitle(msg.title)
								.setSmallIcon(getSmallIconId(context, msg))
								.setContentText(msg.text)
								.setTicker(msg.ticker)
								.setAutoCancel(true);
						Notification mNotification = builder.build();
						//由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
						mNotification.contentView = myNotificationView;
						return mNotification;
					default:
						//默认为0，若填写的builder_id并不存在，也使用默认。
						return super.getNotification(context, msg);
				}
			}
		};
		mPushAgent.setMessageHandler(messageHandler);

		/**
		 * 该Handler是在BroadcastReceiver中被调用，故
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
		 * 参考集成文档的1.6.2
		 * http://dev.umeng.com/push/android/integration#1_6_2
		 * */
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
			}
		};
		//使用自定义的NotificationHandler，来结合友盟统计处理消息通知
		//参考http://bbs.umeng.com/thread-11112-1-1.html
		//CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
		mPushAgent.setNotificationClickHandler(notificationClickHandler);

//		if (MiPushRegistar.checkDevice(this)) {
//            MiPushRegistar.register(this, "2882303761517400865", "5501740053865");
//		}
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
