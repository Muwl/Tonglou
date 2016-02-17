package cn.yunluosoft.tonglou.model;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Id;

/**
 * @author Mu
 * @date 2015-6-4 上午8:42:09
 * @Description 数据库基础类
 */
public abstract class EntityBase implements Serializable {

	@Id(column = "id")
	public int Id;

}
