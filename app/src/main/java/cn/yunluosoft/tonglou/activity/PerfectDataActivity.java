package cn.yunluosoft.tonglou.activity;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.dialog.DateSelectDialog;
import cn.yunluosoft.tonglou.dialog.PhotoDialog;
import cn.yunluosoft.tonglou.dialog.TradeSelectDialog;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.TimeUtils;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CircleImageView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author Mu
 * @date 2015-8-1下午10:14:26
 * @description 
 */
public class PerfectDataActivity extends BaseActivity implements
		OnClickListener {

	private ImageView back;

	private TextView title;

	private CircleImageView icon;

	private EditText name;

	private ToggleButton sex;

	private View birthlin;

	private TextView birth;

	private View tradlin;

	private TextView trade;

	private EditText job;

	private TextView ok;

	private View pro;

	public final int PHOTO_PICKED_WITH_DATA = 3021;

	public final int CAMERA_WITH_DATA = 3023;

	public final int CONTENT_WITH_DATA = 3024;

	private Bitmap bitmap = null;

	private File tempFile;

	private String sdate;

	private static final String PHOTO_FILE_NAME = "temp_photo.png";

	// private String sname;
	// private String ssex;
	private String strade = Constant.TRADS[0];
	// private String sjob;

	// private int tradIndex = 0;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 91:
				// tradIndex = msg.arg1;
				strade = (String) msg.obj;
				trade.setText((String) msg.obj);
				break;
			case 51:
				sdate = (String) msg.obj;
				birth.setText(sdate);
				break;
			case 82:
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, CONTENT_WITH_DATA);
				break;
			case 81:
				Intent intent2 = new Intent(
						"android.media.action.IMAGE_CAPTURE");
				// 判断存储卡是否可以用，可用进行存储
				if (ToosUtils.hasSdcard()) {
					intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri
							.fromFile(new File(Environment
									.getExternalStorageDirectory(),
									PHOTO_FILE_NAME)));
				}
				startActivityForResult(intent2, CAMERA_WITH_DATA);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.perfect_data);
		sdate = "1990-01-01";
		initView();
		if (null != savedInstanceState) {
			if (!ToosUtils.isStringEmpty(savedInstanceState.getString("name"))) {
				name.setText(savedInstanceState.getString("name"));
			}
			if ("0".equals(savedInstanceState.getString("sex"))) {
				sex.setChecked(false);
			} else {
				sex.setChecked(true);
			}
			birth.setText(savedInstanceState.getString("date"));
			trade.setText(savedInstanceState.getString("trade"));
			if (!ToosUtils.isStringEmpty(savedInstanceState.getString("job"))) {
				job.setText(savedInstanceState.getString("job"));
			}
		}
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		icon = (CircleImageView) findViewById(R.id.perfect_icon);
		name = (EditText) findViewById(R.id.perfect_name);
		sex = (ToggleButton) findViewById(R.id.perfect_sex);
		birthlin = findViewById(R.id.perfect_birthlin);
		birth = (TextView) findViewById(R.id.perfect_birth);
		tradlin = findViewById(R.id.perfect_tradelin);
		trade = (TextView) findViewById(R.id.perfect_trade);
		job = (EditText) findViewById(R.id.perfect_job);
		ok = (TextView) findViewById(R.id.perfect_ok);
		pro = findViewById(R.id.perfect_pro);

		back.setOnClickListener(this);
		title.setText("完善资料");
		icon.setOnClickListener(this);
		birthlin.setOnClickListener(this);
		tradlin.setOnClickListener(this);
		ok.setOnClickListener(this);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (!ToosUtils.isTextEmpty(name)) {
			outState.putString("name", ToosUtils.getTextContent(name));
		}
		if (sex.isChecked()) {
			outState.putString("sex", "0");
		} else {
			outState.putString("sex", "1");
		}
		outState.putString("date", sdate);
		outState.putString("trade", strade);
		if (!ToosUtils.isTextEmpty(job)) {
			outState.putString("job", ToosUtils.getTextContent(job));
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;

		case R.id.perfect_icon:
			PhotoDialog dialog = new PhotoDialog(PerfectDataActivity.this,
					handler);

			break;

		case R.id.perfect_birthlin:

			DateSelectDialog selectDialog = new DateSelectDialog(
					PerfectDataActivity.this, handler, sdate);

			break;

		case R.id.perfect_tradelin:
			TradeSelectDialog selectDialog2 = new TradeSelectDialog(
					PerfectDataActivity.this, handler,
					ToosUtils.getTextContent(trade));
			break;

		case R.id.perfect_ok:
			if (checkInput()) {
//				File file = null;
//				if (Environment.getExternalStorageState().equals(
//						Environment.MEDIA_MOUNTED)) {
//
//					file = ToosUtils
//							.saveImage2SD(
//									Environment.getExternalStorageDirectory()
//											+ "/louyu/"
//											+ String.valueOf(System
//													.currentTimeMillis())
//											+ ".JPEG", bitmap);
//				} else {
//					ToastUtils.displayShortToast(PerfectDataActivity.this,
//							"无SD卡,无法上传图片");
//					return;
//				}
//
//				PersonInfo info = new PersonInfo();
//				info.nickname = ToosUtils.getTextContent(name);
//				if (sex.isChecked()) {
//					info.sex = "0";
//				} else {
//					info.sex = "1";
//				}
//				info.birthday = ToosUtils.getTextContent(birth);
//				info.industry = ToosUtils.getTextContent(trade);
//				info.job = ToosUtils.getTextContent(job);
//				Intent intent = new Intent(PerfectDataActivity.this,
//						LocationSelActivity.class);
//				intent.putExtra("flag", 0);
//				Gson gson = new Gson();
//				intent.putExtra("info", info);
//				// intent.putExtra("info", gson.toJson(info));
//				if (file != null) {
//					intent.putExtra("path", file.getAbsolutePath());
//				}
//
//				startActivity(intent);
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 0)
			return;
		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case CONTENT_WITH_DATA:
				if (data != null) {
					// 得到图片的全路径
					Uri uri = data.getData();
					crop(uri);
				}
				break;
			case CAMERA_WITH_DATA:
				if (ToosUtils.hasSdcard()) {
					tempFile = new File(
							Environment.getExternalStorageDirectory(),
							PHOTO_FILE_NAME);
					crop(Uri.fromFile(tempFile));
				} else {
					ToastUtils.displayShortToast(PerfectDataActivity.this,
							"未找到存储卡，无法存储照片！");
				}
				break;
			case PHOTO_PICKED_WITH_DATA:
				bitmap = data.getParcelableExtra("data");
				icon.setImageBitmap(bitmap);
				break;
			}

		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * 剪切图片
	 * 
	 * @function:
	 * @author:Jerry
	 * @date:2013-12-30
	 * @param uri
	 */
	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		// 图片格式
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
		startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
	}

	private boolean checkInput() {
		if (bitmap == null) {
			ToastUtils.displayShortToast(this, "头像不能为空");
			return false;
		}
		if (ToosUtils.isTextEmpty(name)) {
			ToastUtils.displayShortToast(this, "昵称不能为空");
			return false;
		}
		if (ToosUtils.isTextEmpty(birth)) {
			ToastUtils.displayShortToast(this, "生日不能为空");
			return false;
		}
		if (ToosUtils.isTextEmpty(trade)) {
			ToastUtils.displayShortToast(this, "行业不能为空");
			return false;
		}
		if (ToosUtils.isTextEmpty(job)) {
			ToastUtils.displayShortToast(this, "职业不能为空");
			return false;
		}
		return true;
	}

}
