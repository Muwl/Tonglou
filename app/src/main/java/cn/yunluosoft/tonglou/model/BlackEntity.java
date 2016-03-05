package cn.yunluosoft.tonglou.model;

import java.io.Serializable;

/**
 * @author Mu
 * @date 2015-9-22 下午2:32:00
 * @Description 黑名单实体类
 */
public class BlackEntity implements Serializable {

	public String id;// id
	public String icon;// 头像
	public String nickname;// 昵称
	public String sex;// 性别（0：男，1：女）
	public String age;// 年龄
	public String birthday;// 生日
	public String industry;// 行业
	public String job;// 职业
	public String location;// 所在楼宇
	public String signature;// 签名
	public String companyName;// 公司名称
	public String affectiveState;// 情感状态（0：已婚，1：未婚，2：保密）
	public String hobby;// 爱好
}
