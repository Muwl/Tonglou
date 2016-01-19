package cn.yunluosoft.tonglou.utils;

import android.util.Log;

public class LogManager {



	/** 调试日志类型 */
	public static final int DEBUG = 111;


	public static final int ERROR = 112;
	public static final int INFO = 113;
	public static final int VERBOSE = 114;
	public static final int WARN = 115;


	public static void LogShow(String Tag, String Message, int Style) {
		if (!Constant.LOGOFF) {
			switch (Style) {
			case DEBUG:
				if (!Constant.LOGOFF_DEBUG) {
					Log.d(Tag, Message);
				}
				break;
			case ERROR:
				Log.e(Tag, Message);
				break;
			case INFO:
				Log.i(Tag, Message);
				break;
			case VERBOSE:
				if (!Constant.LOGOFF_VERBOSE) {
					Log.v(Tag, Message);
				}
				break;
			case WARN:
				Log.w(Tag, Message);
				break;
			}
		}
	}
}
