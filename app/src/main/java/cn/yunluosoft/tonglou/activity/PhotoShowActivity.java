package cn.yunluosoft.tonglou.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.BaseActivity;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.view.CirclePageIndicator;
import cn.yunluosoft.tonglou.view.HackyViewPager;
import cn.yunluosoft.tonglou.view.photo.PhotoView;

/**
 * @author Mu
 * @date 2015-6-1 上午11:00:53
 * @Description 图片查看页面
 */
public class PhotoShowActivity extends BaseActivity {

	private HackyViewPager pager;

	private CirclePageIndicator mIndicator;

	private List<String> photoEntities;

	private int position;

	private TextView title;

	private ImageView back;

	// private ImageLoaderUtil2 loaderUtil2;

	private static final String STATE_POSITION = "STATE_POSITION";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_pager);
		Intent intent = getIntent();
		position = intent.getIntExtra("position", 0);
		photoEntities = (List<String>) intent
				.getSerializableExtra("photo");
		LogManager.LogShow("-------", new Gson().toJson(photoEntities), LogManager.ERROR);
		initView();
		if (savedInstanceState != null) {
			position = savedInstanceState.getInt(STATE_POSITION);
		}
		back = (ImageView) findViewById(R.id.image_page_back);
		title = (TextView) findViewById(R.id.image_page_title);
		pager.setAdapter(new ImagePagerAdapter(photoEntities, this));
		pager.setCurrentItem(position);
		mIndicator.setViewPager(pager);

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				title.setText((arg0 + 1) + "/" + photoEntities.size());
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void initView() {
		pager = (HackyViewPager) findViewById(R.id.pager);
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private List<String> paths;
		private LayoutInflater inflater;
		private Context mContext;
		private BitmapUtils utils;

		ImagePagerAdapter(List<String> paths, Context context) {
			this.paths = paths;
			this.mContext = context;
			inflater = getLayoutInflater();
			utils = new BitmapUtils(context);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return paths.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_image,
					view, false);

			final PhotoView imageView = (PhotoView) imageLayout
					.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);
			// title.setText((photoEntities.size()-position) + "/" +
			// photoEntities.size());
			utils.display(imageView, paths.get(position),
					new BitmapLoadCallBack<View>() {

						@Override
						public void onLoadStarted(View container, String uri,
												  BitmapDisplayConfig config) {
							super.onLoadStarted(container, uri, config);
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadCompleted(View arg0, String arg1,
													Bitmap arg2, BitmapDisplayConfig arg3,
													BitmapLoadFrom arg4) {
							spinner.setVisibility(View.GONE);
							imageView.setImageBitmap(arg2);
						}

						@Override
						public void onLoadFailed(View arg0, String arg1,
												 Drawable arg2) {
							ToastUtils.displayShortToast(
									PhotoShowActivity.this, "加载失败");
							spinner.setVisibility(View.GONE);

						}
					});

			((ViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
	}
}
