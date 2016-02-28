package cn.yunluosoft.tonglou.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

/**
 * @author Mu
 * @date 2015-8-1下午10:27:46
 * @description 
 */
public class DiscussDialog implements OnClickListener {
	public static final int DISCUSS_OK=1116;
	private Dialog d = null;
	private View view = null;
	private Context context = null;
	int height;
	private Handler handler;
	private EditText editText;
	private TextView send;
	private int position;
	private String temp;
	public DiscussDialog(final Context context, Handler handler,int position,String temp) {
		super();
		this.context = context;
		this.handler = handler;
		this.position=position;
		this.temp=temp;
		d = new Dialog(context);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = View.inflate(context, R.layout.discuss_dialog, null);
		d.setContentView(view);
		editText = (EditText) d.findViewById(R.id.discuss_edit);
		send = (TextView) d.findViewById(R.id.discuss_send);
		if (ToosUtils.isStringEmpty(temp)){
			editText.setHint(temp);
		}
		send.setOnClickListener(this);
		init();
	}

	public void discuss(){
		discuss();
	}
	private void init() {
		Window dialogWindow = d.getWindow();
		LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		lp.width = LayoutParams.MATCH_PARENT;
		// dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
		lp.height = LayoutParams.WRAP_CONTENT;
		dialogWindow
				.setBackgroundDrawableResource(R.drawable.background_dialog);
		height = lp.height;
		d.show();
		dialogAnimation(d, view, getWindowHeight(), height, false);
	}

	private int getWindowHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	private void dialogAnimation(final Dialog d, View v, int from, int to,
			boolean needDismiss) {

		Animation anim = new TranslateAnimation(0, 0, from, to);
		anim.setFillAfter(true);
		anim.setDuration(500);
		if (needDismiss) {
			anim.setAnimationListener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					d.dismiss();
				}
			});

		}
		v.startAnimation(anim);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.discuss_send:
				if (ToosUtils.isTextEmpty(editText)){
					ToastUtils.displayShortToast(context,"内容不能为空！");
					return;
				}
				Message message=new Message();
				message.what=DISCUSS_OK;
				message.arg1=position;
				message.obj=ToosUtils.getTextContent(editText);
				handler.sendMessage(message);
				break;

		default:
			break;
		}
	}

}
