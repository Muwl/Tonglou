package cn.yunluosoft.tonglou.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mu
 * @date 2015-8-8下午7:44:46
 * @description 楼语返回数据
 */
public class FloorSpeechState implements Serializable {

	public String msg;
	public List<FloorSpeechEntity> result;

}
