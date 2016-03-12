package cn.yunluosoft.tonglou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.utils.ToosUtils;

/**
 * @author Mu
 * @date 2015-3-6
 * @description
 */
public class ShareAlertDialog extends Dialog implements
		View.OnClickListener {
	private Context context;
	private Handler handler;
	private TextView ok;
	private TextView cancel;
	private TextView tip;
	private TextView content;
	private ImageView imageView;
	private TextView name;
	private String sname;
	private String scontent;
	private String stip;
	private int position;
	public ShareAlertDialog(Context context, Handler handler, int position, String sname, String stip,String scontent) {
		super(context, R.style.dialog2);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.handler = handler;
		this.context = context;
		this.stip = stip;
		this.sname=sname;
		this.scontent=scontent;
		this.position=position;
		setContentView(R.layout.share_alert_dialog);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		show();
		initView();

	}

	private void initView() {
		tip = (TextView) findViewById(R.id.share_alert_tip);
		content = (TextView) findViewById(R.id.share_alert_content);
		name = (TextView) findViewById(R.id.share_alert_name);
		imageView = (ImageView) findViewById(R.id.share_alert_image);
		ok = (TextView) findViewById(R.id.share_alert_ok);
		cancel = (TextView) findViewById(R.id.share_alert_cancel);

		name.setText("发送到\u2000" + sname);
		imageView.setImageResource(R.mipmap.ic_launcher);
		if (!ToosUtils.isStringEmpty(stip)){
			tip.setText(stip);
		}
		if (!ToosUtils.isStringEmpty(scontent)){
			content.setText(scontent);
		}
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_alert_ok:
			Message message = new Message();
			message.what = 90;
			message.arg1=position;
			handler.sendMessage(message);
			dismiss();
			break;
		case R.id.share_alert_cancel:
			Message message2=new Message();
			message2.what=91;
			message2.arg1=position;
			handler.sendMessage(message2);
			dismiss();
			break;
		default:
			break;
		}

	}

}
