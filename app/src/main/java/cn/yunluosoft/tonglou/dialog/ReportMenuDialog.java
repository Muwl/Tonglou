package cn.yunluosoft.tonglou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;

/**
 * @author Mu
 * @date 2015-3-6
 * @description
 */
public class ReportMenuDialog extends Dialog implements
		View.OnClickListener {
	private Context context;
	private Handler handler;
	private TextView report;
	private TextView cancel;
	private int position;
	public ReportMenuDialog(Context context, Handler handler, int position) {
		super(context, R.style.dialog2);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.handler = handler;
		this.context = context;
		this.position=position;
		setContentView(R.layout.report_dialog);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		show();
		initView();

	}

	private void initView() {
		report = (TextView) findViewById(R.id.report_pro);
		cancel = (TextView) findViewById(R.id.report_cancel);

		report.setOnClickListener(this);
		cancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.report_pro:
			Message message = new Message();
			message.what = 55;
			message.arg1=position;
			handler.sendMessage(message);
			dismiss();
			break;
		case R.id.report_cancel:
			Message message2=new Message();
			message2.what=41;
			message2.arg1=position;
			handler.sendMessage(message2);
			dismiss();
			break;
		default:
			break;
		}

	}

}
