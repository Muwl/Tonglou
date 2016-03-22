package cn.yunluosoft.tonglou.model;

import java.util.Comparator;

public class FriendComparator implements Comparator<FriendEntity> {

	@Override
	public int compare(FriendEntity lhs, FriendEntity rhs) {
		if ("#".equals(lhs.code)){
			return 1;
		}

		return lhs.code.compareTo(rhs.code);
	}

}
