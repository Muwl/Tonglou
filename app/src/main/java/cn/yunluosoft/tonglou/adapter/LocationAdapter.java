package cn.yunluosoft.tonglou.adapter;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.model.LocationEntity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Mu
 * @date 2015-8-6下午8:24:36
 * @description 
 */
public class LocationAdapter extends BaseAdapter {

	private Context context;
	private List<LocationEntity> entities;

	public LocationAdapter(Context context, List<LocationEntity> entities) {
		super();
		this.context = context;
		this.entities = entities;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.location_item, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.location_item_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(entities.get(position).name);
		return convertView;
	}

	class ViewHolder {
		public TextView name;
	}

}
