package cn.yunluosoft.tonglou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.model.FriendEntity;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * @author Mu
 * @date 2015-8-3下午9:18:54
 * @description
 */
public class FriendsAdaper extends BaseAdapter {

	private Context context;
	private List<FriendEntity> entities;
	private BitmapUtils bitmapUtils;

	public FriendsAdaper(Context context, List<FriendEntity> entities) {
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
			convertView = View.inflate(context, R.layout.friend_item, null);
			holder = new ViewHolder();
			holder.icon = (CircleImageView) convertView
					.findViewById(R.id.friend_item_icon);
			holder.name = (TextView) convertView
					.findViewById(R.id.friend_item_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
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
		public CircleImageView icon;
		public TextView name;
	}


}
