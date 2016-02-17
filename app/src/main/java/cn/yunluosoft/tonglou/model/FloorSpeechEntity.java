package cn.yunluosoft.tonglou.model;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;

public class FloorSpeechEntity implements Serializable {
    public String isInGroup;//是否报名（0：是，1：否）
    public String locationName;//发布人所在大楼名称
    public String endDate;//截止日期
    public String applyState;//团嗨模块，是否限制报名人数（0：是，1：否）
    public String start;//起点
    public String publishUserId;//发布人id
    public String praiseNum;//点赞数
    public String planPeopleNum;//计划人数
    public String groupNum;//已报名人数
    public String modelType;//模块类型（0：团嗨，1：二手，2：拼拼，3：帮帮，4：所有）
    public String isAttention;//是否已关注（0：是，1：否）
    public String imGroupId;//环信群id
    public String publishUserIcon;//发布人头像
    public String groupName;//团队名称
    public String publishUserNickname;//发布人昵称
    public String publishUserImUsername;//发布人usename
    public String supplyType;//供给类型（0：转，1：求）
    public String isPraise;//是否已点赞（0：是，1：否）
    public String topic;//标题
    public String end;//终点
    public String id;//id
    public String detail;//详情
    public String createDate;//创建时间

    public String toString() {
        String s = "";
        Field[] arr = this.getClass().getFields();
        for (Field f : getClass().getFields()) {
            try {
                s += f.getName() + "=" + f.get(this) + "\n,";
            } catch (Exception e) {
            }
        }
        return getClass().getSimpleName() + "[" + (arr.length == 0 ? s : s.substring(0, s.length() - 1)) + "]";
    }
}