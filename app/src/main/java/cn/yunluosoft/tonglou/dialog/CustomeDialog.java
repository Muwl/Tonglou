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
public class CustomeDialog extends Dialog implements
		View.OnClickListener {
	private Context context;
	private Handler handler;
	private TextView ok;
	private TextView cancel;
	private TextView text;
	private String msg;
	private int position;
	private int flag;
	public CustomeDialog(Context context, Handler handler, String msg,int position,int flag) {
		super(context, R.style.dialog2);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.handler = handler;
		this.context = context;
		this.msg = msg;
		this.position=position;
		this.flag=flag;
		setContentView(R.layout.custom_dialog);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		show();
		initView();

	}

	private void initView() {
		text = (TextView) findViewById(R.id.custom_dialog_text);
		ok = (TextView) findViewById(R.id.custom_dialog_ok);
		cancel = (TextView) findViewById(R.id.custom_dialog_cancel);
		text.setText(msg);

		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_dialog_ok:
			Message message = new Message();
			message.what = 40;
			message.arg1=position;
			message.arg2=flag;
			handler.sendMessage(message);
			dismiss();
			break;
		case R.id.custom_dialog_cancel:
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
