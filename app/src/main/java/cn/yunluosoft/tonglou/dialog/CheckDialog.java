package cn.yunluosoft.tonglou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.PerfectDataActivity;
import cn.yunluosoft.tonglou.utils.ToosUtils;

/**
 * @author Mu
 * @date 2015-3-6
 * @description
 */
public class CheckDialog extends Dialog implements
		View.OnClickListener {
	private Context context;
	private TextView ok;
	private TextView cancel;
	private TextView text;
	public CheckDialog(Context context) {
		super(context, R.style.dialog2);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
		setContentView(R.layout.custom_dialog);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		show();
		initView();

	}

	private void initView() {
		text = (TextView) findViewById(R.id.custom_dialog_text);
		ok = (TextView) findViewById(R.id.custom_dialog_ok);
		cancel = (TextView) findViewById(R.id.custom_dialog_cancel);
		text.setText("完善资料，开启缤纷生活");
		ok.setText("去完善");


		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.custom_dialog_ok:
			Intent intent=new Intent(context, PerfectDataActivity.class);
			context.startActivity(intent);
			dismiss();
			break;
		case R.id.custom_dialog_cancel:
			dismiss();
			break;
		default:
			break;
		}

	}

}
