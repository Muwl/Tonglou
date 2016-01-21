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
 * @date 2015-8-19 下午5:12:22 
 * @Description
 */
public class ProtocolActivity extends BaseActivity{
	
	private TextView title;
	
	private ImageView back;
	
	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		initView();
	}
	private void initView() {
		title=(TextView) findViewById(R.id.title_title);
		back=(ImageView) findViewById(R.id.title_back);
		webView=(WebView) findViewById(R.id.about_webview);
		WebSettings wSet = webView.getSettings();
		wSet.setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/xieyi.html");
		
		title.setText("服务条款");
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

}
