package cn.yunluosoft.tonglou.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	public static void displayShortToast(Context context, String temp) {
		if (context == null) {
			return;
		}
		Toast.makeText(context, temp, Toast.LENGTH_SHORT).show();
	}

	public static void displaLongToast(Context context, String temp) {
		if (context == null) {
			return;
		}
		Toast.makeText(context, temp, Toast.LENGTH_LONG).show();
	}

	public static void displaDefinedToast(Context context, String temp, int m) {
		if (context == null) {
			return;
		}
		Toast.makeText(context, temp, m).show();
	}

	public static void displayFailureToast(Context context) {
		if (context == null) {
			return;
		}
		Toast.makeText(context, "网络连接失败，请检查你的网络设置", Toast.LENGTH_LONG).show();
	}

	public static void displaySendFailureToast(Context context) {
		if (context == null) {
			return;
		}
		Toast.makeText(context, "网络请求失败，请稍后再试", Toast.LENGTH_LONG).show();
	}

}
