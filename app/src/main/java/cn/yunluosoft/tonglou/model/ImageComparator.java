package cn.yunluosoft.tonglou.model;

import java.util.Comparator;

public class ImageComparator implements Comparator<ImageItem> {

	@Override
	public int compare(ImageItem rhs, ImageItem lhs) {
		if (lhs.million > rhs.million)
			return 1;
		else if (lhs.million < rhs.million)
			return -1;
		else if (lhs.million == rhs.million)
			return 0;
		return 0;
	}

}
