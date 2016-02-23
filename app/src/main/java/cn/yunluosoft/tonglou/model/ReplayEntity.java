package cn.yunluosoft.tonglou.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/23.
 */
public class ReplayEntity implements Serializable {

    public String id;
    public String parentId;
    public String publishUserId;
    public String publishUserIcon;
    public String publishUserNickname;
    public String targetUserId;
    public String targetUserNickname;
    public String content;
    public String createDate;
}
