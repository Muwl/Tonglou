package cn.yunluosoft.tonglou.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.yunluosoft.tonglou.model.ConsultEntity;
import cn.yunluosoft.tonglou.model.FriendEntity;
import cn.yunluosoft.tonglou.model.NewsDBEntity;
import cn.yunluosoft.tonglou.model.NewsEntity;

public class NewsDBUtils {

	private static final String TAG = "DelListDBUtils";

	private Context context;

	private DbUtils db;

	public NewsDBUtils(Context context) {
		super();
		this.context = context;
		db = DbUtils.create(context, ShareDataTool.getUserId(context));
		db.configAllowTransaction(true);
		db.configDebug(true);
	}

	public List<NewsDBEntity> getAllNews() {

		try {
			return db.findAll(NewsDBEntity.class);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void saveAllNews(List<NewsDBEntity> entities) {
		try {
			db.deleteAll(NewsDBEntity.class);
			db.saveAll(entities);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public void saveNews(NewsDBEntity entity) {
		try {
			db.save(entity);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public List<ConsultEntity> getAllConsultEntities(){

		List<NewsDBEntity> newsDBEntities=getAllNews();
		List<ConsultEntity> consultEntities=new ArrayList<>();
		if (newsDBEntities==null ||newsDBEntities.size()==0){
			return consultEntities;
		}
		Type type = new TypeToken<ArrayList<NewsEntity>>() {}.getType();
		Gson gson=new Gson();
		for (int i=0;i<newsDBEntities.size();i++){
			List<NewsEntity> d=gson.fromJson(newsDBEntities.get(i).news,type);
			ConsultEntity consultEntity=new ConsultEntity(newsDBEntities.get(i).id,newsDBEntities.get(i).createDate,newsDBEntities.get(i).isPush,newsDBEntities.get(i).pushState,newsDBEntities.get(i).industry,d);
			consultEntities.add(consultEntity);
		}
		return  consultEntities;
	}

	public void savelConsult(ConsultEntity consultEntity){
		Gson gson=new Gson();
		String temp=gson.toJson(consultEntity.news);
		NewsDBEntity newsDBEntity=new NewsDBEntity(consultEntity.id,consultEntity.createDate,consultEntity.isPush,consultEntity.pushState,consultEntity.industry,temp);
		saveNews(newsDBEntity);
	}

}
