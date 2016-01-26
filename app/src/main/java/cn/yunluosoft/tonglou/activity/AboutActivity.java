package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.yunluosoft.tonglou.R;

/**
 * @author Mu
 * @date 2015-8-4 下午3:15:57
 * @Description 
 */
public class AboutActivity extends BaseActivity implements OnClickListener {

	private ImageView back;

	private TextView title;

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		initView();
		
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		back.setOnClickListener(this);
		title.setText("关于我们");

		webView = (WebView) findViewById(R.id.about_webview);
		WebSettings wSet = webView.getSettings();
		wSet.setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/guanyu.html");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;

		default:
			break;
		}
	}
}
