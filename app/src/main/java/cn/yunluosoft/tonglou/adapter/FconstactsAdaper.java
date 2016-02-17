package cn.yunluosoft.tonglou.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.model.FriendEntity;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CircleImageView;

import com.lidroid.xutils.BitmapUtils;

/**
 * @author Mu
 * @date 2015-8-3下午9:18:54
 * @description
 */
public class FconstactsAdaper extends BaseAdapter implements SectionIndexer {

	private Context context;
	private List<FriendEntity> entities;
	private BitmapUtils bitmapUtils;

	public FconstactsAdaper(Context context, List<FriendEntity> entities) {
		super();
		this.context = context;
		this.entities = entities;
		bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		return entities.size();
	}

	@Override
	public Object getItem(int position) {
		return entities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.fconstacts_item, null);
			holder = new ViewHolder();
			holder.icon = (CircleImageView) convertView
					.findViewById(R.id.fconstacts_item_icon);
			holder.name = (TextView) convertView
					.findViewById(R.id.fconstacts_item_name);
			holder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
			holder.lin = (ImageView) convertView.findViewById(R.id.index_lin);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			holder.tvLetter.setVisibility(View.VISIBLE);
			holder.lin.setVisibility(View.VISIBLE);
			holder.tvLetter.setText(entities.get(position).code);
		} else {
			holder.tvLetter.setVisibility(View.GONE);
			holder.lin.setVisibility(View.GONE);
		}

		bitmapUtils.display(holder.icon, entities.get(position).icon);
		if (!ToosUtils.isStringEmpty(entities.get(position).remarkName)) {
			holder.name.setText(entities.get(position).remarkName);
		} else {
			holder.name.setText(entities.get(position).userName);
		}

		return convertView;
	}

	class ViewHolder {
		TextView tvLetter;
		public CircleImageView icon;
		public TextView name;
		ImageView lin;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return entities.get(position).code.charAt(0);

	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = entities.get(i).code;
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}

}
