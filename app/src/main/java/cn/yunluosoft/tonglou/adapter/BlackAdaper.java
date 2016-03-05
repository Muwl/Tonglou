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
import cn.yunluosoft.tonglou.activity.ConstactActivity;
import cn.yunluosoft.tonglou.model.BlackEntity;
import cn.yunluosoft.tonglou.model.FriendEntity;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CircleImageView;

import com.lidroid.xutils.BitmapUtils;

/**
 * @author Mu
 * @date 2015-8-3下午9:18:54
 * @description 我的黑名单
 */
public class BlackAdaper extends BaseAdapter {

	private Context context;
	private List<BlackEntity> entities;
	private BitmapUtils bitmapUtils;

	public BlackAdaper(Context context, List<BlackEntity> entities) {
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
			convertView = View.inflate(context, R.layout.black_item, null);
			holder = new ViewHolder();
			holder.icon = (CircleImageView) convertView
					.findViewById(R.id.black_item_icon);
			holder.name = (TextView) convertView
					.findViewById(R.id.black_item_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 根据position获取分类的首字母的Char ascii值

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		// if (position == getPositionForSection(section)) {
		// holder.tvLetter.setVisibility(View.VISIBLE);
		// holder.lin.setVisibility(View.VISIBLE);
		// holder.tvLetter.setText(entities.get(position).code);
		// } else {
		// }

		bitmapUtils.display(holder.icon, entities.get(position).icon);
		holder.name.setText(entities.get(position).nickname);

//		holder.icon.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context, ConstactActivity.class);
//				intent.putExtra("id", entities.get(position).id);
//				intent.putExtra("name", entities.get(position).nickname);
//				context.startActivity(intent);
//			}
//		});

		return convertView;
	}

	class ViewHolder {
		public CircleImageView icon;
		public TextView name;
	}

}
