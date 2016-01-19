package cn.yunluosoft.tonglou.utils;

public class MD5Util {

	public static String MD5(String s) {
		// 用来将字节转换成 16 进制表示的字符
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			java.security.MessageDigest mdInst = java.security.MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文，MD5 的计算结果是一个 128 位的长整数，用字节表示就是 16 个字节
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;	//此处也可以默认写成16
			// 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
			char str[] = new char[j * 2];
			// 表示转换结果中对应的字符位置
			int k = 0;
			// 从第一个字节开始，对 MD5 的每一个字节， 转换成 16 进制字符的转换
			for (int i = 0; i < j; i++) {
				// 取第 i 个字节
				byte byte0 = md[i];
				// 取字节中高 4 位的数字转换,>>>为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				// 取字节中低 4 位的数字转换
				str[k++] = hexDigits[byte0 & 0xf];
			}
			// 换后的结果转换为字符串
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

//	public static void main(String[] args) {
//		System.out.println(MD5Util.MD5("test"));
//	}
}
