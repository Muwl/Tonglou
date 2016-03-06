package cn.yunluosoft.tonglou.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.fragment.ConstactFragment;
import cn.yunluosoft.tonglou.activity.fragment.FloorSpeechFragment;
import cn.yunluosoft.tonglou.activity.fragment.PersonFragment;
import cn.yunluosoft.tonglou.activity.fragment.WithFloorFragment;
import cn.yunluosoft.tonglou.easemob.applib.controller.HXSDKHelper;
import cn.yunluosoft.tonglou.easemob.chatuidemo.Constant;
import cn.yunluosoft.tonglou.easemob.chatuidemo.DemoHXSDKHelper;
import cn.yunluosoft.tonglou.easemob.chatuidemo.db.InviteMessgeDao;
import cn.yunluosoft.tonglou.easemob.chatuidemo.db.UserDao;
import cn.yunluosoft.tonglou.easemob.chatuidemo.domain.InviteMessage;
import cn.yunluosoft.tonglou.easemob.chatuidemo.domain.User;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.MyApplication;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMMessage;
import com.easemob.util.EMLog;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * @author Mu
 * @date 2015-8-2下午3:58:59
 * @description
 */
public class MainActivity extends BaseActivity implements EMEventListener {

	protected static final String TAG = "MainActivity";

	private static FragmentManager fMgr;

	private RadioButton floorSpeech;

	private RadioButton withFloor;

	private RadioButton constact;

	private RadioButton person;

	private RadioGroup group;

	private boolean canExit = false;

	private int pageIndex = 1;// 1代表楼语 2 代表同楼 3 代表人脉 4代表我的

	private MyConnectionListener connectionListener = null;

	// 账号在别处登录
	public boolean isConflict = false;

	// 账号被移除
	private boolean isCurrentAccountRemoved = false;

	private TextView readNo;

	private int width;

	private int mFlag = 0;

//	private TextView floornum;

	private MyBroadCastReceiver broadCastReceiver;

	public static final String BROADCAST_ACTION = "cn.yunluosoft.tonglou.Main";

	/**
	 * 检查当前用户是否被删除
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

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
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);

		if (savedInstanceState != null
				&& savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED,
				false)) {
			// 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			DemoHXSDKHelper.getInstance().logout(true, null);
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		} else if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false)) {
			// 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		setContentView(R.layout.main);
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		ToosUtils.deleteFile(new File(Environment.getExternalStorageDirectory()
				+ "/louyu/"));

		if (getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
				&& !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}
		inviteMessgeDao = new InviteMessgeDao(this);
		userDao = new UserDao(this);

		fMgr = getSupportFragmentManager();
		initFragment();
		group = (RadioGroup) findViewById(R.id.main_rg);
		floorSpeech = (RadioButton) findViewById(R.id.main_bottom_floorSpeech);
		withFloor = (RadioButton) findViewById(R.id.main_bottom_withfloor);
		constact = (RadioButton) findViewById(R.id.main_bottom_contact);
		person = (RadioButton) findViewById(R.id.main_bottom_person);
		readNo = (TextView) findViewById(R.id.main_bottom_number);
//		floornum = (TextView) findViewById(R.id.main_bottom_floornum);

		width = DensityUtil.getScreenWidth(this);
		LayoutParams params = (LayoutParams) readNo.getLayoutParams();
		params.leftMargin = width * 1 / 4- DensityUtil.dip2px(this, 38);
		params.topMargin = DensityUtil.dip2px(this, 3);
		readNo.setLayoutParams(params);

//		LayoutParams params2 = (LayoutParams) floornum.getLayoutParams();
//		params2.leftMargin = width * 1 / 5 - DensityUtil.dip2px(this, 34);
//		params2.topMargin = DensityUtil.dip2px(this, 3);
//		floornum.setLayoutParams(params2);

		// initFragment();
		group.check(R.id.main_bottom_floorSpeech);

		floorSpeech.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pageIndex == 1) {
					FloorSpeechFragment fragment = (FloorSpeechFragment) fMgr
							.findFragmentByTag("FloorSpeechFragment");
					// 当前页面如果为聊天历史页面，刷新此页面
					if (fragment != null) {
						//fragment.onLoading();
					}
				} else {
					pageIndex = 1;
					FloorSpeechFragment fragment = (FloorSpeechFragment) fMgr
							.findFragmentByTag("FloorSpeechFragment");
					if (fragment != null) {
						//fragment.getNoReadNum();
					}
				}
			}
		});

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				LogManager.LogShow("aaa", checkedId + "", LogManager.VERBOSE);
				switch (checkedId) {
					case R.id.main_bottom_floorSpeech:
						// pageIndex = 1;
						try {
							if ((fMgr.findFragmentByTag("FloorSpeechFragment") != null && fMgr
									.findFragmentByTag("FloorSpeechFragment")
									.isVisible())) {
								return;
							}
							FloorSpeechFragment fragment = (FloorSpeechFragment) fMgr
									.findFragmentByTag("FloorSpeechFragment");
							// 当前页面如果为聊天历史页面，刷新此页面
							if (fragment != null) {
								//fragment.onrefush();
							}
							popAllFragmentsExceptTheBottomOne();
						} catch (Exception e) {
						}

						break;

					case R.id.main_bottom_withfloor:
						pageIndex = 2;
						try {
							popAllFragmentsExceptTheBottomOne();
							FragmentTransaction ft = fMgr.beginTransaction();
							ft.hide(fMgr.findFragmentByTag("FloorSpeechFragment"));
							WithFloorFragment floorFragment = new WithFloorFragment();
							ft.add(R.id.main_fragment, floorFragment,
									"WithFloorFragment");
							ft.addToBackStack("WithFloorFragment");
							ft.commitAllowingStateLoss();
						} catch (Exception e) {
						}

						break;
					case R.id.main_bottom_contact:
						pageIndex = 3;
						try {
							popAllFragmentsExceptTheBottomOne();
							FragmentTransaction ft2 = fMgr.beginTransaction();
							ft2.hide(fMgr.findFragmentByTag("FloorSpeechFragment"));
							ConstactFragment constactFragment = new ConstactFragment();
							ft2.add(R.id.main_fragment, constactFragment,
									"ConstactFragment");
							ft2.addToBackStack("ConstactFragment");
							ft2.commitAllowingStateLoss();

						} catch (Exception e) {
						}

						break;
					case R.id.main_bottom_person:
						pageIndex = 4;
						try {
							popAllFragmentsExceptTheBottomOne();
							FragmentTransaction ft3 = fMgr.beginTransaction();
							ft3.hide(fMgr.findFragmentByTag("FloorSpeechFragment"));
							PersonFragment personFragment = new PersonFragment();
							ft3.add(R.id.main_fragment, personFragment,
									"PersonFragment");
							ft3.addToBackStack("PersonFragment");
							ft3.commitAllowingStateLoss();
						} catch (Exception e) {
						}

						break;

					default:
						break;
				}
			}
		});
		broadCastReceiver = new MyBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BROADCAST_ACTION);
		registerReceiver(broadCastReceiver, intentFilter);

		init();

		pageIndex = getIntent().getIntExtra("index", 1);

	}

	@Override
	protected void onResume() {
		super.onResume();
		LogManager.LogShow("-------", "}}}}}}}}}}}}}}}", LogManager.ERROR);

		mFlag = 1;
		if (!isConflict && !isCurrentAccountRemoved) {
			updateUnreadLabel();
			// updateUnreadAddressLable();
			EMChatManager.getInstance().activityResumed();
		}

		// unregister this event listener when this activity enters the
		// background
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();
		sdkHelper.getNotifier().reset();
		sdkHelper.pushActivity(this);
		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] {
						EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventConversationListChanged });

		mFlag = 1;

		if (pageIndex == 1) {
			group.check(R.id.main_bottom_floorSpeech);
			try {
				popAllFragmentsExceptTheBottomOne();
			} catch (Exception e) {
			}
		}

		onrefush();
	}

	private void init() {
		// setContactListener监听联系人的变化等
		// EMContactManager.getInstance().setContactListener(
		// new MyContactListener());
		// 注册一个监听连接状态的listener

		connectionListener = new MyConnectionListener();
		EMChatManager.getInstance().addConnectionListener(connectionListener);
	}

	/**
	 * 初始化首个Fragment
	 */
	private void initFragment() {
		try {
			FragmentTransaction ft = fMgr.beginTransaction();
			FloorSpeechFragment floorSpeechFragment = new FloorSpeechFragment();
			ft.add(R.id.main_fragment, floorSpeechFragment, "FloorSpeechFragment");
			ft.addToBackStack("FloorSpeechFragment");
			ft.commitAllowingStateLoss();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 从back stack弹出所有的fragment，保留首页的那个
	 */
	public static void popAllFragmentsExceptTheBottomOne() {
		for (int i = 0, count = fMgr.getBackStackEntryCount() - 1; i < count; i++) {
			fMgr.popBackStack();

		}

	}

	// 点击返回按钮
	@Override
	public void onBackPressed() {
		// if (ToosUtils.isApplicationBroughtToBackground(MainActivity.this)) {
		// if (canExit) {
		// finish();
		// MyApplication.getInstance().exit();
		// } else {
		// ToastUtils.displayShortToast(this, "再按一次退出程序");
		// canExit = true;
		// handler.sendEmptyMessageDelayed(0, 2000);
		// }
		boolean flag = false;
		for (int i = 0; i < MyApplication.getInstance().getActivities().size(); i++) {
			if (MyApplication.getInstance().getActivities().get(i) != null) {
				if (MainActivity.class.equals(MyApplication.getInstance()
						.getActivities().get(i).getClass())
						&& !flag) {
					flag = true;

				} else {
					MyApplication.getInstance().getActivities().get(i).finish();
				}
			}

		}
		moveTaskToBack(true);
		// } else {
		// super.onBackPressed();
		// }

	}

	public void onrefush() {
//		FloorSpeechFragment fragment = (FloorSpeechFragment) fMgr
//				.findFragmentByTag("FloorSpeechFragment");
//		// 当前页面如果为聊天历史页面，刷新此页面
//		if (fragment != null) {
//			fragment.onrefush();
//		}
//
//		if (ShareDataTool.getNum(this) > 0) {
//			floornum.setVisibility(View.VISIBLE);
//			floornum.setText(String.valueOf(ShareDataTool.getNum(this)));
//			LayoutParams params2 = (LayoutParams) floornum.getLayoutParams();
//			params2.leftMargin = width * 1 / 5 - DensityUtil.dip2px(this, 34);
//			params2.topMargin = DensityUtil.dip2px(this, 3);
//			params2.width = DensityUtil.dip2px(this, 16);
//			params2.height = DensityUtil.dip2px(this, 16);
//			floornum.setLayoutParams(params2);
//			return;
//		}
//
//		if (ShareDataTool.getGetNum(this) > 0) {
//			floornum.setVisibility(View.VISIBLE);
//			floornum.setText("");
//			LayoutParams params2 = (LayoutParams) floornum.getLayoutParams();
//			params2.leftMargin = width * 1 / 5 - DensityUtil.dip2px(this, 34);
//			params2.topMargin = DensityUtil.dip2px(this, 3);
//			params2.width = DensityUtil.dip2px(this, 10);
//			params2.height = DensityUtil.dip2px(this, 10);
//			floornum.setLayoutParams(params2);
//			return;
//
//		}
//		floornum.setVisibility(View.GONE);

		// if (pageIndex == 1) {
		// FloorSpeechFragment fragment = (FloorSpeechFragment) fMgr
		// .findFragmentByTag("FloorSpeechFragment");
		// // 当前页面如果为聊天历史页面，刷新此页面
		// if (fragment != null) {
		// fragment.onrefush();
		// }
		// }
	}

	// public void onrefush() {
	// if (ShareDataTool.getNoReadTime(this) >= ShareDataTool
	// .getGetNumTime(this)) {
	// if (ShareDataTool.getNum(this) > 0) {
	// floornum.setVisibility(View.VISIBLE);
	// floornum.setText(String.valueOf(ShareDataTool.getNum(this)));
	// LayoutParams params2 = (LayoutParams) floornum
	// .getLayoutParams();
	// params2.leftMargin = width * 1 / 5
	// - DensityUtil.dip2px(this, 34);
	// params2.topMargin = DensityUtil.dip2px(this, 3);
	// params2.width = DensityUtil.dip2px(this, 16);
	// params2.height = DensityUtil.dip2px(this, 16);
	// floornum.setLayoutParams(params2);
	// } else {
	// floornum.setVisibility(View.GONE);
	// }
	//
	// } else {
	// if (ShareDataTool.getGetNum(this) > 0) {
	// floornum.setVisibility(View.VISIBLE);
	// floornum.setText("");
	// LayoutParams params2 = (LayoutParams) floornum
	// .getLayoutParams();
	// params2.leftMargin = width * 1 / 5
	// - DensityUtil.dip2px(this, 34);
	// params2.topMargin = DensityUtil.dip2px(this, 3);
	// params2.width = DensityUtil.dip2px(this, 10);
	// params2.height = DensityUtil.dip2px(this, 10);
	// floornum.setLayoutParams(params2);
	//
	// } else {
	// floornum.setVisibility(View.GONE);
	// }
	//
	// }
	//
	// // if (pageIndex == 1) {
	// FloorSpeechFragment fragment = (FloorSpeechFragment) fMgr
	// .findFragmentByTag("FloorSpeechFragment");
	// // 当前页面如果为聊天历史页面，刷新此页面
	// if (fragment != null) {
	// fragment.onrefush();
	// }
	// // }
	// }

	public class MyBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			onrefush();
		}

	}

	/**
	 * 监听事件
	 */
	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
			case EventNewMessage: // 普通消息
			{
				EMMessage message = (EMMessage) event.getData();
				if (ToosUtils.filterSysMes(MainActivity.this, message)) {
					return;
				}
				// 提示新消息
				HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
				refreshUI();
				break;
			}

			case EventOfflineMessage: {
				refreshUI();
				break;
			}

			case EventConversationListChanged: {
				refreshUI();
				break;
			}

			default:
				break;
		}
	}

	private void refreshUI() {
		runOnUiThread(new Runnable() {
			public void run() {
				// 刷新bottom bar消息未读数
				updateUnreadLabel();
				if (pageIndex == 1) {
					FloorSpeechFragment fragment = (FloorSpeechFragment) fMgr
							.findFragmentByTag("FloorSpeechFragment");
					LogManager.LogShow("-----------","dddddddddddddd",LogManager.ERROR);
					// 当前页面如果为聊天历史页面，刷新此页面
					if (fragment != null) {
						LogManager.LogShow("-----------","xxxxxxxxxxxxxxxxxxxxxxxxx",LogManager.ERROR);
						fragment.refush();
					}
				}
			}
		});
	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		delSysCon();
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			readNo.setText(String.valueOf(count));
			readNo.setVisibility(View.VISIBLE);
		} else {
			readNo.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}

		if (connectionListener != null) {
			EMChatManager.getInstance().removeConnectionListener(
					connectionListener);
		}

		try {
			unregisterReceiver(broadCastReceiver);
		} catch (Exception e) {
		}
		try {
			unregisterReceiver(internalDebugReceiver);
		} catch (Exception e) {
		}
	}

	/**
	 * 获取未读申请与通知消息
	 *
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(
				Constant.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = ((DemoHXSDKHelper) HXSDKHelper
					.getInstance()).getContactList()
					.get(Constant.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
		return unreadAddressCountTotal;
	}

	/**
	 * 获取未读消息数
	 *
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		int chatroomUnreadMsgCount = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		for (EMConversation conversation : EMChatManager.getInstance()
				.getAllConversations().values()) {
			if (conversation.getType() == EMConversationType.ChatRoom)
				chatroomUnreadMsgCount = chatroomUnreadMsgCount
						+ conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal - chatroomUnreadMsgCount;
	}

	/**
	 * 获取所有会话
	 *
	 * @param
	 * @return +
	 */
	private void delSysCon() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					sortList.add(new Pair<Long, EMConversation>(conversation
							.getLastMessage().getMsgTime(), conversation));
				}
			}
		}
		for (Pair<Long, EMConversation> sortItem : sortList) {
			if (sortItem.second.getLastMessage().getFrom()
					.equals(cn.yunluosoft.tonglou.utils.Constant.SYS_NAME)
					|| sortItem.second
					.getLastMessage()
					.getTo()
					.equals(cn.yunluosoft.tonglou.utils.Constant.SYS_NAME)
					|| sortItem.second
					.getLastMessage()
					.getFrom()
					.equals(cn.yunluosoft.tonglou.utils.Constant.SYS_GETNAME)
					|| sortItem.second
					.getLastMessage()
					.getFrom()
					.equals(cn.yunluosoft.tonglou.utils.Constant.SYS_GETNAME)) {
				EMChatManager.getInstance().deleteConversation(
						sortItem.second.getUserName(),
						sortItem.second.isGroup(), true);
				InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(
						MainActivity.this);
				inviteMessgeDao.deleteMessage(sortItem.second.getUserName());
				continue;
			}
		}
	}

	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;

	/**
	 * 连接监听listener
	 *
	 */
	public class MyConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {
			boolean groupSynced = HXSDKHelper.getInstance()
					.isGroupsSyncedWithServer();
			boolean contactSynced = HXSDKHelper.getInstance()
					.isContactsSyncedWithServer();

			// in case group and contact were already synced, we supposed to
			// notify sdk we are ready to receive the events
			if (groupSynced && contactSynced) {
				new Thread() {
					@Override
					public void run() {
						HXSDKHelper.getInstance().notifyForRecevingEvents();
					}
				}.start();
			} else {
			}

		}

		@Override
		public void onDisconnected(final int error) {
			final String st1 = getResources().getString(
					R.string.can_not_connect_chat_server_connection);
			final String st2 = getResources().getString(
					R.string.the_current_network);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (error == EMError.USER_REMOVED) {
						// 显示帐号已经被移除
						showAccountRemovedDialog();
					} else if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆dialog
						showConflictDialog();
					} else {

					}
				}

			});
		}
	}

	/**
	 * 保存提示新消息
	 *
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(null);

		// // 刷新bottom bar消息未读数
		// updateUnreadAddressLable();
		// // 刷新好友页面ui
		// if (currentTabIndex == 1)
		// contactListFragment.refresh();
	}

	/**
	 * 保存邀请等msg
	 *
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = ((DemoHXSDKHelper) HXSDKHelper.getInstance())
				.getContactList().get(Constant.NEW_FRIENDS_USERNAME);
		if (user.getUnreadMsgCount() == 0)
			user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mFlag = 2;
	}

	@Override
	protected void onStop() {
		EMChatManager.getInstance().unregisterEventListener(this);
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();
		sdkHelper.popActivity(this);

		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}

	private android.app.AlertDialog.Builder conflictBuilder;
	private android.app.AlertDialog.Builder accountRemovedBuilder;
	private boolean isConflictDialogShow;
	private boolean isAccountRemovedDialogShow;
	private BroadcastReceiver internalDebugReceiver;

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		DemoHXSDKHelper.getInstance().logout(false, null);
		String st = getResources().getString(R.string.Logoff_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(
							MainActivity.this);
				conflictBuilder.setTitle(st);
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								dialog.dismiss();
								ToosUtils.goReLogin(MainActivity.this);
							}
						});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				EMLog.e(TAG,
						"---------color conflictBuilder error" + e.getMessage());
			}

		}

	}

	/**
	 * 帐号被移除的dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		DemoHXSDKHelper.getInstance().logout(true, null);
		String st5 = getResources().getString(R.string.Remove_the_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(
							MainActivity.this);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage(R.string.em_user_remove);
				accountRemovedBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								dialog.dismiss();
								accountRemovedBuilder = null;
								finish();
								startActivity(new Intent(MainActivity.this,
										LoginActivity.class));
							}
						});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {
				EMLog.e(TAG,
						"---------color userRemovedBuilder error"
								+ e.getMessage());
			}

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// setIntent(intent);
		if (getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
				&& !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}
		LogManager.LogShow("-----", "::::::::::", LogManager.ERROR);
		pageIndex = intent.getIntExtra("index", 1);
		if (pageIndex == 2) {
			group.check(R.id.main_bottom_withfloor);
			try {
				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction ft1 = fMgr.beginTransaction();
				ft1.hide(fMgr.findFragmentByTag("FloorSpeechFragment"));
				WithFloorFragment withFloorFragment = new WithFloorFragment();
				ft1.add(R.id.main_fragment, withFloorFragment, "WithFloorFragment");
				ft1.addToBackStack("WithFloorFragment");
				ft1.commitAllowingStateLoss();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// getMenuInflater().inflate(R.menu.context_tab_contact, menu);
	}

}
