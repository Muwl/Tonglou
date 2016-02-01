package cn.yunluosoft.tonglou.dialog;

import u.aly.ca;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import cn.yunluosoft.tonglou.R;

/**
 * @author Mu
 * @date 2015-8-1下午10:27:46
 * @description 
 */
public class RigDialog implements OnClickListener {

	private Dialog d = null;
	private View view = null;
	private Context context = null;
	int height;
	private Handler handler;
	private TextView camara;
	private TextView photo;
	private TextView cancel;
	private TextView black;
	private int flag;
	private int backflag;

	public RigDialog(final Context context, Handler handler, int flag,
			int backflag) {
		super();
		this.context = context;
		this.handler = handler;
		this.flag = flag;
		this.backflag = backflag;
		d = new Dialog(context);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = View.inflate(context, R.layout.photo_dialog, null);
		d.setContentView(view);
		camara = (TextView) d.findViewById(R.id.photo_dialog_camera);
		photo = (TextView) d.findViewById(R.id.photo_dialog_photo);
		black = (TextView) d.findViewById(R.id.photo_dialog_back);
		cancel = (TextView) d.findViewById(R.id.photo_dialog_cancel);
		if (flag == 1) {
			camara.setVisibility(View.GONE);
			d.findViewById(R.id.photo_dialog_div).setVisibility(View.GONE);
		}
		if (backflag == 1) {
			black.setVisibility(View.VISIBLE);
			d.findViewById(R.id.photo_dialog_backdiv).setVisibility(
					View.VISIBLE);
		}
		camara.setText("备注");
		photo.setText("举报");
		camara.setOnClickListener(this);
		photo.setOnClickListener(this);
		black.setOnClickListener(this);
		cancel.setOnClickListener(this);
		init();
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
		case R.id.photo_dialog_camera:
			handler.sendEmptyMessage(81);
			d.dismiss();
			break;
		case R.id.photo_dialog_photo:
			handler.sendEmptyMessage(82);
			d.dismiss();
			break;
		case R.id.photo_dialog_back:
			handler.sendEmptyMessage(83);
			d.dismiss();
			break;
		case R.id.photo_dialog_cancel:
			d.dismiss();
			break;

		default:
			break;
		}
	}

}
