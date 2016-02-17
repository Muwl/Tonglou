package cn.yunluosoft.tonglou.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * @author Mu
 * @date 2015-8-11 上午9:45:54
 * @Description 
 */
@Table(name = "Friend")
public class FriendEntity extends EntityBase {

	@Column(column = "fid")
	public String id;
	
	@Column(column = "userid")
	public String userId;
	
	@Column(column = "icon")
	public String icon;
	
	@Column(column = "username")
	public String userName;
	
	@Column(column = "remarkname")
	public String remarkName;
	
	@Column(column = "createdate")
	public String createDate;
	
	@Column(column = "byremarkname")
	public String byRemarkName;
	
	@Column(column = "code")
	public String code;

	public FriendEntity(String id, String userId, String icon, String userName,
			String remarkName, String createDate, String byRemarkName,
			String code) {
		super();
		this.id = id;
		this.userId = userId;
		this.icon = icon;
		this.userName = userName;
		this.remarkName = remarkName;
		this.createDate = createDate;
		this.byRemarkName = byRemarkName;
		this.code = code;
	}

	@Override
	public String toString() {
		return "FriendEntity [id=" + id + ", userId=" + userId + ", icon="
				+ icon + ", userName=" + userName + ", remarkName="
				+ remarkName + ", createDate=" + createDate + ", byRemarkName="
				+ byRemarkName + ", code=" + code + "]";
	}

	public FriendEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

}
