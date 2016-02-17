package cn.yunluosoft.tonglou.utils;

import java.util.List;

import android.content.Context;
import cn.yunluosoft.tonglou.model.FriendEntity;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

public class FriendDBUtils {

	private static final String TAG = "DelListDBUtils";

	private Context context;

	private DbUtils db;

	public FriendDBUtils(Context context) {
		super();
		this.context = context;
		db = DbUtils.create(context, ShareDataTool.getUserId(context));
		db.configAllowTransaction(true);
		db.configDebug(true);
	}

	public List<FriendEntity> getAllFriends() {

		try {
			return db.findAll(FriendEntity.class);
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void saveAllFriends(List<FriendEntity> entities) {
		try {
			db.deleteAll(FriendEntity.class);
			db.saveAll(entities);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public void removeFriend(FriendEntity entity) {
		try {
			db.deleteAll(db.findAll(Selector.from(FriendEntity.class).where(
					"fid", "=", entity.id)));
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

}
