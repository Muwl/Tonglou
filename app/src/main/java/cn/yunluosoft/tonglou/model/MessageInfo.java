package cn.yunluosoft.tonglou.model;

import java.io.Serializable;

/**
 * @author MU
 * @date 2015-5-23
 * @description 普通消息实体类
 */
public class MessageInfo implements Serializable {

	public String senderUserId;
	public String receiverUserId;
	public String senderImUserName;
	public String receiverImUserName;
	public String senderHeadUrl;
	public String receiverHeadUrl;
	public String senderNickName;
	public String receiverNickName;
	public String groupDynamicID;

	

	public MessageInfo(String senderUserId, String receiverUserId,
			String senderImUserName, String receiverImUserName,
			String senderHeadUrl, String receiverHeadUrl,
			String senderNickName, String receiverNickName) {
		super();
		this.senderUserId = senderUserId;
		this.receiverUserId = receiverUserId;
		this.senderImUserName = senderImUserName;
		this.receiverImUserName = receiverImUserName;
		this.senderHeadUrl = senderHeadUrl;
		this.receiverHeadUrl = receiverHeadUrl;
		this.senderNickName = senderNickName;
		this.receiverNickName = receiverNickName;
	}



	public MessageInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

}
