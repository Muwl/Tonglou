package cn.yunluosoft.tonglou.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ShareDataTool {
	public static boolean SaveInfo(Context context, String token,
			String userId, String imUsername, String imPassword) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString("token", token);
		e.putString("userId", userId);
		e.putString("imUsername", imUsername);
		e.putString("imPassword", imPassword);
		return e.commit();
	}
	public static boolean SaveInfoDetail(Context context, String nickname,
			String icon, String location,String buildingId) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString("nickname", nickname);
		e.putString("icon", icon);
		e.putString("location", location);
		e.putString("buildingId", buildingId);
		return e.commit();
	}

	public static String getUserId(Context context) {

		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("userId", "");
	}

	public static String getNickname(Context context) {

		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("nickname", "");
	}

	public static String getIcon(Context context) {

		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("icon", "");
	}

	public static String getBuildingId(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("buildingId", "");
	}

	public static String getLocation(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("location", "");
	}

	public static String getToken(Context context) {

		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)

		.getString("token", "");
	}

	public static String getImUsername(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("imUsername", "");
	}
	public static String getImPassword(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("imPassword", "");
	}
	public static boolean SaveFlag(Context context, int flag) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt("flag", flag);
		return e.commit();
	}

	public static int getFlag(Context context) {
		// return 1;
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE).getInt(
				"flag", 0);
	}

	public static boolean saveNum(Context context, int num) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt("noreadnum", num);
		return e.commit();
	}

	public static int getNum(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE).getInt(
				"noreadnum", 0);
	}

	public static boolean saveNoReadTime(Context context, long million) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putLong("noreadtime", million);
		return e.commit();
	}
	public static long getNoReadTime(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getLong("noreadtime", 0);
	}

	public static boolean saveGetNum(Context context, int num) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt("noget", num);
		return e.commit();
	}

	public static int getGetNum(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE).getInt(
				"noget", 0);
	}
	public static boolean savePageNo(Context context, int num) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt("pageNo", num);
		return e.commit();
	}
	public static int getPageNo(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE).getInt(
				"pageNo", 1);
	}

	public static boolean saveGetNumTime(Context context, long million) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putLong("nogettime", million);
		return e.commit();
	}

	public static long getGetNumTime(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getLong("nogettime", 0);
	}

	public static boolean saveUpdateFlag(Context context, int flag) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt("updateFlag", flag);
		return e.commit();
	}

	public static int getUpdateFlag(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE).getInt(
				"updateFlag", 0);
	}

}
