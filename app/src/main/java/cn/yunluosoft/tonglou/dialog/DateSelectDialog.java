package cn.yunluosoft.tonglou.dialog;

import java.lang.reflect.Field;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
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
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.TimeUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

/**
 * @author Mu
 * @date 2015-8-2上午9:00:39
 * @description 
 */
public class DateSelectDialog implements OnClickListener {
	private Dialog d = null;
	private View view = null;
	private Context context = null;
	private TextView ok;
	private TextView cancel;
	private DatePicker datePicker;
	int height;
	private Handler handler;
	private String date;
	private String temp;

	public DateSelectDialog(Context context, Handler handler, String temp) {
		super();
		this.context = context;
		this.handler = handler;
		this.temp = temp;
		d = new Dialog(context);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = View.inflate(context, R.layout.date_select, null);
		d.setContentView(view);
		ok = (TextView) d.findViewById(R.id.date_ok);
		cancel = (TextView) d.findViewById(R.id.date_cancel);
		datePicker = (DatePicker) d.findViewById(R.id.date_date);
		Calendar calendar = Calendar.getInstance();
		if (!ToosUtils.isStringEmpty(temp)) {
			calendar.setTime(TimeUtils.getDateByStr(temp));
		} else {
			calendar.setTime(TimeUtils.getDateByStr("1990-01-01"));
		}
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
		datePicker.init(year, monthOfYear, dayOfMonth,
				new OnDateChangedListener() {

					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						date = year + "-" + (monthOfYear + 1) + "-"
								+ dayOfMonth;
					}

				});
		try {
			setDatePickerDividerColor(datePicker);
		} catch (Exception e) {
			LogManager.LogShow("111111111", "11111111111111111",
					LogManager.ERROR);
			e.printStackTrace();
		}
		ok.setOnClickListener(this);
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
		case R.id.date_cancel:
			dialogAnimation(d, view, height, getWindowHeight(), true);
			break;
		case R.id.date_ok:
			dialogAnimation(d, view, height, getWindowHeight(), true);
			Message message = new Message();
			message.what = 51;
			message.obj = TimeUtils.getStringByString(date);
			handler.sendMessage(message);
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * 设置时间选择器的分割线颜色
	 * 
	 * @param datePicker
	 */
	@SuppressLint("NewApi")
	private void setDatePickerDividerColor(DatePicker datePicker) {
		// Divider changing:

		// 获取 mSpinners
		LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);

		// 获取 NumberPicker
		LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
		for (int i = 0; i < mSpinners.getChildCount(); i++) {
			NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

			Field[] pickerFields = NumberPicker.class.getDeclaredFields();
			for (Field pf : pickerFields) {
				if (pf.getName().equals("mSelectionDivider")) {
					pf.setAccessible(true);
					try {
						pf.set(picker, new ColorDrawable(context.getResources()
								.getColor(R.color.ptime_lin)));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (NotFoundException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}

}
