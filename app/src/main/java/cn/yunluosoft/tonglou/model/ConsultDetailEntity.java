package cn.yunluosoft.tonglou.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/9.
 */
public class ConsultDetailEntity implements Serializable {

    public String id;
    public String parentId;
    public String publishUserName;
    public String publishUserIcon;
    public String publishUserId;
    public String targetUserName;
    public String targetUserIcon;
    public String targetUserId;
    public String newsId;
    public String content;
    public List<String> praise;
    public String praiseNum;
    public String isPraise;
    public String createDate;
    public String type;


}
