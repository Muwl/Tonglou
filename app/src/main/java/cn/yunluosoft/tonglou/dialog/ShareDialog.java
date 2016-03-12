package cn.yunluosoft.tonglou.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
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
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.ScrollerNumberPicker;

/**
 * @author Mu
 * @date 2015-8-6 上午9:23:47
 * @Description 
 */
public class ShareDialog implements OnClickListener {
	private Dialog d = null;
	private View view = null;
	private Context context = null;
	private TextView cricle;
	private TextView weixin;
	private TextView qq;
	private TextView sina;
	private TextView sms;
	private TextView email;
	private TextView louyu;
	private TextView coypy;
	private TextView cancel;
	int height;
	private Handler handler;
	private String text;
	private String url;
	private UMShareListener listener;
	private UMImage image;
	private ClipboardManager myClipboard;
	private ClipData myClip;
	private String topic;
	public ShareDialog(Context context, Handler handler,String text,String topic,String url,UMImage image, UMShareListener listener) {
		super();
		this.context = context;
		this.handler = handler;
		this.text=text;
		this.url=url;
		this.image=image;
		this.topic=topic;
		this.listener=listener;
		d = new Dialog(context);
		myClipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = View.inflate(context, R.layout.share_dialog, null);
		d.setContentView(view);
		cricle = (TextView) d.findViewById(R.id.share_weixincirlce);
		weixin = (TextView) d.findViewById(R.id.share_weixin);
		qq = (TextView) d.findViewById(R.id.share_qq);
		sina = (TextView) d.findViewById(R.id.share_sina);
		sms = (TextView) d.findViewById(R.id.share_sms);
		email = (TextView) d.findViewById(R.id.share_email);
		louyu = (TextView) d.findViewById(R.id.share_friend);
		coypy = (TextView) d.findViewById(R.id.share_copy);
		cancel = (TextView) d.findViewById(R.id.share_cancel);
		cricle.setOnClickListener(this);
		weixin.setOnClickListener(this);
		qq.setOnClickListener(this);
		sina.setOnClickListener(this);
		sms.setOnClickListener(this);
		email.setOnClickListener(this);
		louyu.setOnClickListener(this);
		coypy.setOnClickListener(this);
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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_cancel:
			dialogAnimation(d, view, height, getWindowHeight(), true);
			break;

		case R.id.share_weixincirlce:
			new ShareAction((Activity)context)
					.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
					.setCallback(listener)
					.withText(text)
					.withTitle(topic)
					.withTargetUrl(url)
					.withMedia(image)
					.share();
			dialogAnimation(d, view, height, getWindowHeight(), true);
			break;

		case R.id.share_weixin:
			new ShareAction((Activity)context)
					.setPlatform(SHARE_MEDIA.WEIXIN)
					.setCallback(listener)
					.withText(text)
					.withTitle(topic)
					.withTargetUrl(url)
					.withMedia(image)
					.share();
			dialogAnimation(d, view, height, getWindowHeight(), true);
			break;

		case R.id.share_qq:
			new ShareAction((Activity)context)
					.setPlatform(SHARE_MEDIA.QQ)
					.setCallback(listener)
					.withText(text)
					.withTitle(topic)
					.withTargetUrl(url)
					.withMedia(image)
					.share();
			dialogAnimation(d, view, height, getWindowHeight(), true);
			break;

		case R.id.share_sina:
			new ShareAction((Activity)context)
					.setPlatform(SHARE_MEDIA.SINA)
					.setCallback(listener)
					.withText(text)
					.withTitle(topic)
					.withTargetUrl(url)
					.withMedia(image)
					.share();
			dialogAnimation(d, view, height, getWindowHeight(), true);
			break;

		case R.id.share_sms:
			new ShareAction((Activity)context)
					.setPlatform(SHARE_MEDIA.SMS)
					.setCallback(listener)
					.withText(text)
					.withTitle(topic)
					.withTargetUrl(url)
					.withMedia(image)
					.share();
			dialogAnimation(d, view, height, getWindowHeight(), true);
			break;

		case R.id.share_email:
			new ShareAction((Activity)context)
					.setPlatform(SHARE_MEDIA.EMAIL)
					.setCallback(listener)
					.withText(text)
					.withTitle(topic)
					.withTargetUrl(url)
					.withMedia(image)
					.share();
			dialogAnimation(d, view, height, getWindowHeight(), true);
			break;
		case R.id.share_friend:
			dialogAnimation(d, view, height, getWindowHeight(), true);
			handler.sendEmptyMessage(1223);
			break;
		case R.id.share_copy:

			myClip = ClipData.newPlainText("text", url);
			myClipboard.setPrimaryClip(myClip);

			dialogAnimation(d, view, height, getWindowHeight(), true);
			break;


		default:
			break;
		}
	}

}
