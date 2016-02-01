package cn.yunluosoft.tonglou.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CircleImageView;
import cn.yunluosoft.tonglou.view.MyScrollLayout;

import com.lidroid.xutils.BitmapUtils;

/**
 * @author Mu
 * @date 2015-5-21 下午1:13:42
 * @Description
 */
public class GalleryAdapter extends BaseAdapter {
	private Context context;
	private BitmapUtils bitmapUtils;

	public GalleryAdapter(Context context,
			BitmapUtils bitmapUtils) {
		this.context = context;
		this.bitmapUtils = bitmapUtils;
		// bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position == 0) {
			convertView = View.inflate(context,
					R.layout.constact_head_mygrally1, null);
			LinearLayout view = (LinearLayout) convertView
					.findViewById(R.id.constact_head_root1);
			CircleImageView icon = (CircleImageView) convertView
					.findViewById(R.id.constact_head_icon);
			TextView name = (TextView) convertView
					.findViewById(R.id.constact_head_name);
			TextView job = (TextView) convertView
					.findViewById(R.id.fwithfloor_item_job);
			TextView tarde = (TextView) convertView
					.findViewById(R.id.fwithfloor_item_career);
			TextView content = (TextView) convertView
					.findViewById(R.id.fwithfloor_item_content);

			LayoutParams params = (LayoutParams) view.getLayoutParams();
			params.width = DensityUtil.getScreenWidth(context);
			params.height = DensityUtil.dip2px(context, 253);
			view.setLayoutParams(params);

//			icon.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					Intent intent = new Intent(context, PhotoShowActivity.class);
//					List<String> photos = new ArrayList<String>();
//					photos.add(floorEntity.icon);
//					intent.putExtra("photo", (Serializable) photos);
//					context.startActivity(intent);
//				}
//			});
//			bitmapUtils.display(icon, floorEntity.icon);
//			name.setText(floorEntity.nickname);
//
//			age.setText(floorEntity.age);
//			job.setText(floorEntity.job);
//			tarde.setText(floorEntity.industry);
//			if (!ToosUtils.isStringEmpty(floorEntity.signature)) {
//				content.setText(floorEntity.signature);
//			} else {
//				content.setText("让整栋楼听到你的声音");
//			}

		} else if (position == 1) {
			convertView = View.inflate(context,
					R.layout.constact_head_mygrally2, null);
			LinearLayout view = (LinearLayout) convertView
					.findViewById(R.id.constact_head_root2);
			TextView unit = (TextView) convertView
					.findViewById(R.id.constact_head_unit);
			TextView emotion = (TextView) convertView
					.findViewById(R.id.constact_head_emotion);
			TextView birth = (TextView) convertView
					.findViewById(R.id.constact_head_birth);
			TextView interect = (TextView) convertView
					.findViewById(R.id.constact_head_interect);

			LayoutParams params = (LayoutParams) view.getLayoutParams();
			params.width = DensityUtil.getScreenWidth(context);
			params.height = DensityUtil.dip2px(context, 253);
			view.setLayoutParams(params);

//			if (!ToosUtils.isStringEmpty(floorEntity.companyName)) {
//				unit.setText(floorEntity.companyName);
//			} else {
//				unit.setText("未填写");
//			}
//			if (!ToosUtils.isStringEmpty(floorEntity.affectiveState)) {
//				if (String.valueOf(Constant.EMOTION_MARRIED).equals(
//						floorEntity.affectiveState)) {
//					emotion.setText("情感：已婚");
//				} else if (String.valueOf(Constant.EMOTION_NOMARRIED).equals(
//						floorEntity.affectiveState)) {
//					emotion.setText("情感：未婚");
//				} else {
//					emotion.setText("情感：保密");
//				}
//			} else {
//				emotion.setText("情感：保密");
//			}
//
//			birth.setText("星座：" + floorEntity.constellation);
//			if (!ToosUtils.isStringEmpty(floorEntity.hobby)) {
//				interect.setText(floorEntity.hobby);
//			} else {
//				interect.setText("未填写");
//			}
		}

		return convertView;
	}
}
