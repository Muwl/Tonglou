package cn.yunluosoft.tonglou.utils;

public class Constant {

	public static final String ROOT_PATH = "http://120.25.225.224:8080/louyu";
//	public static final String ROOT_PATH = "http://192.168.1.10:8080/louyu";


	public static final String SYS_NAME = "100000";
	public static final String SYS_GETNAME = "100001";

	public static final boolean LOGOFF = false;
	public static final boolean LOGOFF_VERBOSE = false;
	public static final boolean LOGOFF_DEBUG = false;

	public static final String[] TRADS = new String[] { "IT互联网", "文化传媒", "通信",
			"金融", "学生", "教育培训", "医疗生物", "政府科研", "司法法律", "房产建筑", "服务业", "汽车摩托",
			"轻工贸易", "电子电器", "机械重工", "农林牧渔", "光电新能源", "物联网", "化工环保" };

	public static final int EMOTION_NOMARRIED= 0;

	public static final int EMOTION_MARRIEING = 1;

	public static final int EMOTION_MARRIED = 2;

	public static final int EMOTION_SERCET = 3;

	public static final String SEX_MAN = "0";

	public static final String SEX_WOMEN = "1";

	public static final String RELATION_YES = "0";

	public static final String RELATION_NO = "1";

	public static final String BLACK_YES = "0";

	public static final String BLACK_NO = "1";

	// 联网返回成功
	public static final String RETURN_OK = "200";
	// 联网Token失败
	public static final String TOKEN_ERR = "201";

	public static final String USER_NOCOM = "206";

	public static final String USER_NOINFO= "209";

	public static final String PRAISE_OK = "0";//已点赞

	public static final String PRAISE_NO = "1";//未点赞

	public static final String ATTEN_OK = "0";//已关注

	public static final String ATTEN_NO = "1";//未关注

}
