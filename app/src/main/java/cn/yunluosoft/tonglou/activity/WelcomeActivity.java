package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.easemob.chatuidemo.DemoHXSDKHelper;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToosUtils;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Mu
 * @date 2015-8-1上午9:00:03
 * @description 
 */
public class WelcomeActivity extends BaseActivity {
//
	private ImageView icon;

	private ImageView title;

	private ImageView content;

	private int width;

	private static final int sleepTime = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome);
		width = DensityUtil.getScreenWidth(this);
		MobclickAgent.updateOnlineConfig(this);
		 MobclickAgent.setDebugMode(true);
		initView();

		new Thread(new Runnable() {

			@Override
			public void run() {
				if (true) {
					try {
						Thread.sleep(2000);
						go();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		}).start();
	}

	@Override
	protected void onStart() {
		super.onStart();

		new Thread(new Runnable() {
			public void run() {
				if (!ToosUtils.isStringEmpty(ShareDataTool
						.getToken(WelcomeActivity.this))
						&& DemoHXSDKHelper.getInstance().isLogined()) {
					// ** 免登陆情况 加载所有本地群和会话
					// 不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
					// 加上的话保证进了主页面会话和群组都已经load完毕
					long start = System.currentTimeMillis();
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					long costTime = System.currentTimeMillis() - start;
					// 等待sleeptime时长
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// 进入主页面
					startActivity(new Intent(WelcomeActivity.this,
							MainActivity.class));
					finish();
				}
				else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
					startActivity(new Intent(WelcomeActivity.this,
							LoginActivity.class));
					finish();
				}
			}
		}).start();

	}

	private void initView() {
		icon = (ImageView) findViewById(R.id.welcome_icon);
		title = (ImageView) findViewById(R.id.welcome_title);
		content = (ImageView) findViewById(R.id.welcome_content);
		LayoutParams iconParams = (LayoutParams) icon.getLayoutParams();
		iconParams.width = (int) (0.368 * width);
		iconParams.height = (int) (0.388 * width);
		iconParams.topMargin = (int) (0.33 * width);
		icon.setLayoutParams(iconParams);

	}

	private void go() {
		if (ToosUtils.isStringEmpty(ShareDataTool.getToken(this))) {
			Intent intent = new Intent(WelcomeActivity.this,
					LoginActivity.class);
			startActivity(intent);
			finish();
			return;
		}

		if (0 == ShareDataTool.getFlag(this)) {
			Intent intent = new Intent(WelcomeActivity.this,
					PerfectDataActivity.class);
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

	}
}
