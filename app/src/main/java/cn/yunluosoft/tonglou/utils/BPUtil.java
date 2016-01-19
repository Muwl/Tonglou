package cn.yunluosoft.tonglou.utils;

import java.util.Random;

public class BPUtil {

	private static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9' };

	private static BPUtil bpUtil;

	public static BPUtil getInstance() {
		if (bpUtil == null)
			bpUtil = new BPUtil();
		return bpUtil;
	}

	private static final int DEFAULT_CODE_LENGTH = 6;

	private static int codeLength = DEFAULT_CODE_LENGTH;
	
	private static String  code;
	
	private static Random random=new Random();

	public static String getCode() {
		
		return code;
	}
	
	public static  String createCode() {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < codeLength; i++) {
			buffer.append(CHARS[random.nextInt(CHARS.length)]);
		}
		code=buffer.toString();
		return buffer.toString();
	}

}