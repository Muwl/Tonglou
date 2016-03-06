package cn.yunluosoft.tonglou.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/6.
 * 新闻详情
 */
public class NewsEntity implements Serializable{

    public String id;//新闻id
    public String coverImage;//封面图
    public String topic;//新闻标题
    public String content;//内容
    public String readNum;//阅读量
    public String state;//
}
