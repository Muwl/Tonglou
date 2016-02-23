package cn.yunluosoft.tonglou.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.easemob.applib.controller.HXSDKHelper;
import cn.yunluosoft.tonglou.easemob.chatuidemo.utils.CommonUtils;
import cn.yunluosoft.tonglou.model.MessageInfo;
import cn.yunluosoft.tonglou.utils.MyApplication;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.TimeUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.NotificationCompat;
import com.easemob.util.EasyUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Mu
 * @date 2015-6-16
 * @description
 */
public class BaseActivity extends FragmentActivity {
	private static final int notifiId = 11;
	public static final String BATG = "BaseActivity";
	InputMethodManager imm;
	protected NotificationManager notificationManager;
	private EMChatOptions chatOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(this);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		super.onCreate(savedInstanceState);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		chatOptions = EMChatManager.getInstance().getChatOptions();

	}

//	protected void onResume() {
//		super.onResume();
//		EMChatManager.getInstance().activityResumed();
//		MobclickAgent.onResume(this);
//	}

	@Override
	protected void onPause() {
		super.onPause();
		if (imm != null && getCurrentFocus() != null
				&& getCurrentFocus().getWindowToken() != null) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
		MobclickAgent.onPause(this);
	}

	/**
	 * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下 如果不需要，注释掉即可
	 * 
	 * @param message
	 */
	protected void notifyNewMessage(EMMessage message) {
		// int start = ShareDataTool.getStart(this);
		// int stop = ShareDataTool.getStop(this);
		int cur = TimeUtils.getHours();
		// if (start > stop) {
		// if (cur >= start && cur <= stop) {
		chatOptions.setNotificationEnable(true);
		EMChatManager.getInstance().setChatOptions(chatOptions);
		HXSDKHelper.getInstance().getModel().setSettingMsgNotification(true);
		// } else {
		// chatOptions.setNotificationEnable(false);
		// EMChatManager.getInstance().setChatOptions(chatOptions);
		// HXSDKHelper.getInstance().getModel()
		// .setSettingMsgNotification(false);
		// }
		// } else if (start < stop) {
		// if (cur >= start || cur <= stop) {
		// chatOptions.setNotificationEnable(true);
		// EMChatManager.getInstance().setChatOptions(chatOptions);
		// HXSDKHelper.getInstance().getModel()
		// .setSettingMsgNotification(true);
		// } else {
		// chatOptions.setNotificationEnable(false);
		// EMChatManager.getInstance().setChatOptions(chatOptions);
		// HXSDKHelper.getInstance().getModel()
		// .setSettingMsgNotification(false);
		// }
		// }
		// 如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
		// 以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
		if (!EasyUtils.isAppRunningForeground(this)) {
			return;
		}

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(getApplicationInfo().icon)
				.setWhen(System.currentTimeMillis()).setAutoCancel(true);

		String ticker = CommonUtils.getMessageDigest(message, this);
		MessageInfo messageInfo = null;
		// Info info=null;
		try {
			messageInfo = ToosUtils.getMessageInfo(message);
			// info=CommonUtils.getMesInfo(message, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String st = getResources().getString(R.string.expression);
		if (message.getType() == Type.TXT)
			ticker = ticker.replaceAll("\\[.{2,3}\\]", st);
		String name = message.getFrom();
		if (messageInfo != null) {
			name = messageInfo.senderNickName;
		}
		// 设置状态栏提示
		mBuilder.setTicker(name + ": " + ticker);

		// 必须设置pendingintent，否则在2.3的机器上会有bug
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, notifiId,
				intent, PendingIntent.FLAG_ONE_SHOT);
		mBuilder.setContentIntent(pendingIntent);

		Notification notification = mBuilder.build();
		notificationManager.notify(notifiId, notification);
		notificationManager.cancel(notifiId);
	}

}
