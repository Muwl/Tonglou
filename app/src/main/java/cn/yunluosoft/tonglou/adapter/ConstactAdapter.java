package cn.yunluosoft.tonglou.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.ConstactActivity;
import cn.yunluosoft.tonglou.activity.GroupDetailActivity;
import cn.yunluosoft.tonglou.activity.HelpDetailActivity;
import cn.yunluosoft.tonglou.activity.PPDetailActivity;
import cn.yunluosoft.tonglou.activity.PhotoShowActivity;
import cn.yunluosoft.tonglou.activity.UsedDetailActivity;
import cn.yunluosoft.tonglou.model.ConstantWithfloorEntity;
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CircleImageView;
import cn.yunluosoft.tonglou.view.CustomListView;
import cn.yunluosoft.tonglou.view.MyGallery;

import com.lidroid.xutils.BitmapUtils;

/**
 * @author Mu
 * @date 2015-8-6 上午10:24:22
 * @Description 
 */
public class ConstactAdapter extends BaseAdapter {

	private int width;
	private int type0 = 1;
	private int type1 = 2;
	LayoutInflater inflater;
	Context context;
	private BitmapUtils bitmapUtils;
	CustomListView listView;
	private ConstantWithfloorEntity floorEntity;
	private List<FloorSpeechEntity> entities;

	private int flag;

	public ConstactAdapter(Context context,ConstantWithfloorEntity floorEntity,List<FloorSpeechEntity> entities, int width,
			CustomListView listView, int flag, BitmapUtils bitmapUtils) {
		super();
		this.context = context;
		this.width = width;
		this.floorEntity=floorEntity;
		this.entities=entities;
		this.listView = listView;
		this.flag = flag;
		inflater = LayoutInflater.from(context);
		this.bitmapUtils = bitmapUtils;
		// bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		return entities.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return type0;
		} else {
			return type1;
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		ViewHolder1 holder1 = null;
		int type = getItemViewType(position);
		if (type == type0) {
			if (convertView == null
					|| !convertView.getTag().getClass()
							.equals(ViewHolder.class)) {
				convertView = inflater.inflate(R.layout.constact_head_item,
						null);
				holder = new ViewHolder();
				holder.imageBg = (ImageView) convertView
						.findViewById(R.id.constact_head_image);
				holder.gallery = (MyGallery) convertView
						.findViewById(R.id.gallery);
				holder.lllayout = (LinearLayout) convertView
						.findViewById(R.id.lin);
				holder.locate = (TextView) convertView
						.findViewById(R.id.constact_head_locate);
				holder.empty = convertView
						.findViewById(R.id.constact_head_empty);
				holder.empty_image = (ImageView) convertView
						.findViewById(R.id.empty_image);
				holder.empty_text = (TextView) convertView
						.findViewById(R.id.empty_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		} else if (type == type1) {
			if (convertView == null
					|| !convertView.getTag().getClass()
							.equals(ViewHolder1.class)) {
				convertView = inflater.inflate(R.layout.myfloorspeech_publish_item, null);
				holder1 = new ViewHolder1();
				holder1.icon = (CircleImageView) convertView
						.findViewById(R.id.myfloorspeech_publish_item_icon);
				holder1.name = (TextView) convertView
						.findViewById(R.id.myfloorspeech_publish_item_name);
				holder1.content = (TextView) convertView
						.findViewById(R.id.myfloorspeech_publish_item_content);
				convertView.setTag(holder1);
			} else {
				holder1 = (ViewHolder1) convertView.getTag();
			}
		}

		if (type == type0) {
//			bitmapUtils.display(holder.imageBg, floorEntity.background);
			holder.lllayout.removeAllViews();
			holder.locate.setText(floorEntity.location);
			int m = DensityUtil.dip2px(context, 3);
			for (int i = 0; i < 2; i++) {
				ImageView image = (ImageView) LayoutInflater.from(context)
						.inflate(R.layout.banner_point, null);
				image.setPadding(m, 0, m, 0);
				holder.lllayout.addView(image);
			}
			GalleryAdapter adapter = new GalleryAdapter(context,floorEntity,
					bitmapUtils);
			holder.gallery.setAdapter(adapter);
			holder.lllayout.setTag("aaaaaaaaaackefklk");
			holder.gallery
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							ImageView iv;
							LinearLayout layout = (LinearLayout) listView
									.findViewWithTag("aaaaaaaaaackefklk");
							int count = layout.getChildCount();
							for (int i = 0; i < count; i++) {
								iv = (ImageView) layout.getChildAt(i);
								if (i == position % 2) {
									iv.setImageResource(R.drawable.indicator_normal);
								} else {
									iv.setImageResource(R.drawable.indicator_checked);
								}
							}

						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {

						}
					});

			if ((entities == null || entities.size() == 0) && flag == 1) {
				holder.empty.setVisibility(View.VISIBLE);
				holder.empty_image.setImageDrawable(context.getResources()
						.getDrawable(R.mipmap.empty_mes));
				holder.empty_text.setText("什么都没有呢！");
			} else {
				holder.empty.setVisibility(View.GONE);
			}
		} else if (type == type1) {

			bitmapUtils.display(holder1.icon, entities.get(position - 1).publishUserIcon);
//			holder1.icon.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Intent intent = new Intent(context, ConstactActivity.class);
//					intent.putExtra("id", entities.get(position-1).publishUserId);
//					intent.putExtra("name", entities.get(position-1).publishUserNickname);
//					context.startActivity(intent);
//				}
//			});
			holder1.name.setText(entities.get(position - 1).publishUserNickname);
			if (ToosUtils.isStringEmpty(entities.get(position - 1).detail)) {
				holder1.content.setVisibility(View.GONE);
			} else if (entities.get(position - 1).detail.length() <= 96) {
				holder1.content.setText(entities.get(position - 1).detail);
			} else {
				String s = entities.get(position - 1).detail.substring(0, 96);
				holder1.content.setText(s + "...");
			}

//			holder1.content.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					Intent intent = null;
//					if ("0".equals(entities.get(position-1).modelType)) {
//						intent = new Intent(context, GroupDetailActivity.class);
//					} else if ("1".equals(entities.get(position-1).modelType)) {
//						intent = new Intent(context, UsedDetailActivity.class);
//					} else if ("2".equals(entities.get(position-1).modelType)) {
//						intent = new Intent(context, PPDetailActivity.class);
//					} else if ("3".equals(entities.get(position-1).modelType)) {
//						intent = new Intent(context, HelpDetailActivity.class);
//					}
//					intent.putExtra("id", entities.get(position-1).id);
//					context.startActivity(intent);
////					Intent intent = new Intent(context,
////							FloorSpeechDetailActivity.class);
////					intent.putExtra("flag", 1);
////					intent.putExtra("id", entities.get(position - 1).id);
////					((ConstactActivity) context).startActivityForResult(intent,
////							1005);
//				}
//			});



		}
		return convertView;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	class ViewHolder {
		public ImageView imageBg;
		public MyGallery gallery;
		public LinearLayout lllayout;
		public TextView locate;
		public View empty;
		public ImageView empty_image;
		public TextView empty_text;
	}

	class ViewHolder1 {
		public CircleImageView icon;
		public TextView name;
		public TextView content;
	}

}
