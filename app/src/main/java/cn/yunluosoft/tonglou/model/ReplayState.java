package cn.yunluosoft.tonglou.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/2/23.
 */
public class ReplayState implements Serializable {

    public String msg;
    public List<ReplayEntity> result;
}
