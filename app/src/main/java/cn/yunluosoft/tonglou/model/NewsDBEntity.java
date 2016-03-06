package cn.yunluosoft.tonglou.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/3/6.
 */
@Table(name = "News")
public class NewsDBEntity extends EntityBase{

    @Column(column = "nid")
    public String id;//资讯id

    @Column(column = "createDate")
    public String createDate;//发布时间

    @Column(column = "isPush")
    public String isPush;//用户后台判断是否推送 前端无用

    @Column(column = "pushState")
    public String pushState;//推送状态，前端无用

    @Column(column = "industry")
    public String industry;//群发对象此属性前端无用

    @Column(column = "news")
    public String news;//新闻详情

    public NewsDBEntity(String id, String createDate, String isPush, String pushState, String industry, String news) {
        this.id = id;
        this.createDate = createDate;
        this.isPush = isPush;
        this.pushState = pushState;
        this.industry = industry;
        this.news = news;
    }
}
