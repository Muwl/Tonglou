package cn.yunluosoft.tonglou.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/6.
 */
public class ConsultEntity implements Serializable  {
    public String id;//资讯id
    public String createDate;//发布时间
    public String isPush;//用户后台判断是否推送 前端无用
    public String pushState;//推送状态，前端无用
    public String industry;//群发对象此属性前端无用
    public List<NewsEntity> news;//主新闻详情(此新闻中图片大图显示)

    public ConsultEntity(String id, String createDate, String isPush, String pushState, String industry, List<NewsEntity> news) {
        this.id = id;
        this.createDate = createDate;
        this.isPush = isPush;
        this.pushState = pushState;
        this.industry = industry;
        this.news = news;
    }
}
