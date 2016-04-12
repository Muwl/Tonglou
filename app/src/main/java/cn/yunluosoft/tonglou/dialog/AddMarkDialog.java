package cn.yunluosoft.tonglou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

/**
 * @author Mu
 * @date  2015-3-6
 * @description 
 */
public class AddMarkDialog extends Dialog implements
		View.OnClickListener {
	private Context context;
	private Handler handler;
	private TextView ok;
	private TextView cancel;
	private EditText editText;
	InputMethodManager imm;

	public AddMarkDialog(Context context, Handler handler) {
		super(context, R.style.dialog2);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		this.handler = handler;
		this.context = context;
		setContentView(R.layout.addmark_dialog);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		show();
		initView();

	}

	private void initView() {
		ok = (TextView) findViewById(R.id.addmark_dialog_ok);
		cancel = (TextView) findViewById(R.id.addmark_dialog_cancel);
		editText=(EditText) findViewById(R.id.addmark_dialog_name);
		// titleText.setText(title);

		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addmark_dialog_ok:
//			if (ToosUtils.isTextEmpty(editText)) {
//				ToastUtils.displayShortToast(context, "备注名称不能为空");
//				return;
//			}
			Message message = new Message();
			message.what = 104;
			message.obj=ToosUtils.getTextContent(editText);
			handler.sendMessage(message);
			imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.SHOW_FORCED);
			dismiss();
			break;
		case R.id.addmark_dialog_cancel:
			handler.sendEmptyMessage(105);
			imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.SHOW_FORCED);
			dismiss();
			break;
		default:
			break;
		}

	}

}
