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

/**
 * @author Mu
 * @date 2015-3-6
 * @description
 */
public class SubmitDialog extends Dialog implements
		View.OnClickListener {
	private Context context;
	private Handler handler;
	private ImageView imageView;
	private TextView tip;
	private TextView content;
	private TextView close;
	private String msg;
	private int position;
	private int flag;
	public SubmitDialog(Context context) {
		super(context, R.style.dialog2);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
		setContentView(R.layout.submit_dialog);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		show();
		initView();

	}

	private void initView() {
		imageView= (ImageView) findViewById(R.id.submit_image);
		tip= (TextView) findViewById(R.id.submit_tip);
		content= (TextView) findViewById(R.id.submit_content);
		close= (TextView) findViewById(R.id.submit_close);
		close.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit_close:
			dismiss();
			break;
		default:
			break;
		}

	}

}
