package cn.yunluosoft.tonglou.activity;

import u.aly.bl;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.dialog.CustomeDialog;
import cn.yunluosoft.tonglou.easemob.applib.controller.HXSDKHelper;
import cn.yunluosoft.tonglou.easemob.chatuidemo.DemoHXSDKModel;
import cn.yunluosoft.tonglou.utils.DataCleanManager;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ToosUtils;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.lidroid.xutils.BitmapUtils;

/**
 * @author Mu
 * @date 2015-8-4 下午4:53:46
 * @Description 
 */
public class SettingActivity extends BaseActivity implements OnClickListener {

	private TextView title;

	private ImageView back;

	private TextView clearCash;

	private ToggleButton toggleButton;

	private TextView exit;

	private View black;


	DemoHXSDKModel model;

	private EMChatOptions chatOptions;

	private BitmapUtils bitmapUtils;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 40:
				int flag = msg.arg2;
				if (flag == -1) {
					DataCleanManager.cleanInternalCache(SettingActivity.this);
					bitmapUtils.clearCache();
					bitmapUtils.clearDiskCache();
					bitmapUtils.clearMemoryCache();
				} else {
					ToosUtils.goReLogin(SettingActivity.this);
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		bitmapUtils = new BitmapUtils(this);
		initView();

	}

	private void initView() {
		title = (TextView) findViewById(R.id.title_title);
		back = (ImageView) findViewById(R.id.title_back);
		clearCash = (TextView) findViewById(R.id.setting_clearcash);
		exit = (TextView) findViewById(R.id.setting_exit);
		black = findViewById(R.id.setting_black);
		toggleButton = (ToggleButton) findViewById(R.id.setting_recmes_toggle);

		title.setText("系统设置");
		back.setOnClickListener(this);
		black.setOnClickListener(this);
		clearCash.setOnClickListener(this);
		exit.setOnClickListener(this);

		chatOptions = EMChatManager.getInstance().getChatOptions();
		model = (DemoHXSDKModel) HXSDKHelper.getInstance().getModel();
		if (model.getSettingMsgNotification()) {
			toggleButton.setChecked(true);
		} else {
			toggleButton.setChecked(false);
		}

		toggleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (toggleButton.isChecked()) {
					// LogManager.LogShow("----------------------",
					// "kkkkkkkkkkkkkkkkkkkkkkk", LogManager.ERROR);
					chatOptions.setNotificationEnable(true);
					EMChatManager.getInstance().setChatOptions(chatOptions);
					HXSDKHelper.getInstance().getModel()
							.setSettingMsgNotification(true);
				} else {
					chatOptions.setNotificationEnable(false);
					EMChatManager.getInstance().setChatOptions(chatOptions);

					HXSDKHelper.getInstance().getModel()
							.setSettingMsgNotification(false);
					// LogManager.LogShow("----------------------",
					// "gggggggggggggggggggggg", LogManager.ERROR);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;case R.id.setting_black:
				Intent intent=new Intent(SettingActivity.this,BlackActivity.class);
				startActivity(intent);
			break;
		case R.id.setting_clearcash:
			CustomeDialog customeDialog = new CustomeDialog(
					SettingActivity.this, handler, "确定清除缓存？", 0, -1);
			break;
			case R.id.setting_exit:
				CustomeDialog customeDialog2 = new CustomeDialog(
						SettingActivity.this, handler, "确定退出？", 0, -2);

				break;
		default:
			break;
		}
	}

}
