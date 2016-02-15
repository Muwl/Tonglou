package cn.yunluosoft.tonglou.model;

import java.io.Serializable;

/**
 * @author Mu
 * @date 2015-8-6 下午4:32:17
 * @Description 
 */
public class PersonInfo implements Serializable {
	public String icon;
	public String nickname;
	public String birthday;
	public String sex;
	public String industry;
	public String job;
	public String location;
	public String signature;
	public String companyName;
	public String affectiveState;
	public String hobby;
	public String buildingId;

	public PersonInfo(String icon, String nickname, String birthday, String sex, String industry, String job, String location, String signature, String companyName, String affectiveState, String hobby, String buildingId) {
		this.icon = icon;
		this.nickname = nickname;
		this.birthday = birthday;
		this.sex = sex;
		this.industry = industry;
		this.job = job;
		this.location = location;
		this.signature = signature;
		this.companyName = companyName;
		this.affectiveState = affectiveState;
		this.hobby = hobby;
		this.buildingId = buildingId;
	}

	public PersonInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "PersonInfo{" +
				"icon='" + icon + '\'' +
				", nickname='" + nickname + '\'' +
				", birthday='" + birthday + '\'' +
				", sex='" + sex + '\'' +
				", industry='" + industry + '\'' +
				", job='" + job + '\'' +
				", location='" + location + '\'' +
				", signature='" + signature + '\'' +
				", companyName='" + companyName + '\'' +
				", affectiveState='" + affectiveState + '\'' +
				", hobby='" + hobby + '\'' +
				", buildingId='" + buildingId + '\'' +
				'}';
	}
}
