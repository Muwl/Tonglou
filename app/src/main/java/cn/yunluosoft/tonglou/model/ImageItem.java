package cn.yunluosoft.tonglou.model;

import java.io.Serializable;

/**
 * 
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public long million;
	public boolean isSelected = false;
}
