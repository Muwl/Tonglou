package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.dialog.SubmitDialog;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author Mu
 * @date 2015-9-15 上午10:00:58
 * @Description 
 */
public class ReportActivity extends BaseActivity implements OnClickListener {

	private TextView title;

	private ImageView back;

	private View item1;

	private View item2;

	private View item3;

	private View item4;

	private View item5;

	private CheckBox check1;
	private CheckBox check2;
	private CheckBox check3;
	private CheckBox check4;
	private CheckBox check5;

	private TextView ok;

	private View pro;

	private int flag;// 0代表用户举报 1代表楼宇举报 3代表新闻举报

	private String userId;

	private String contactId;

	private String newsId;

	private String reson = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);
		initView();
	}

	private void initView() {
		flag = getIntent().getIntExtra("flag", 0);
		userId = getIntent().getStringExtra("userId");
		contactId = getIntent().getStringExtra("contactId");
		newsId = getIntent().getStringExtra("newsId");
		title = (TextView) findViewById(R.id.title_title);
		back = (ImageView) findViewById(R.id.title_back);
		item1 =  findViewById(R.id.report_item1);
		item2 = findViewById(R.id.report_item2);
		item3 = findViewById(R.id.report_item3);
		item4 = findViewById(R.id.report_item4);
		item5 = findViewById(R.id.report_item5);

		check1 = (CheckBox) findViewById(R.id.reportimage_item1);
		check2 = (CheckBox) findViewById(R.id.reportimage_item2);
		check3 = (CheckBox) findViewById(R.id.reportimage_item3);
		check4 = (CheckBox) findViewById(R.id.reportimage_item4);
		check5 = (CheckBox) findViewById(R.id.reportimage_item5);

		ok = (TextView) findViewById(R.id.report_ok);
		pro = findViewById(R.id.report_pro);
		item1.setOnClickListener(this);
		item2.setOnClickListener(this);
		item3.setOnClickListener(this);
		item4.setOnClickListener(this);
		item5.setOnClickListener(this);

		back.setOnClickListener(this);
		ok.setOnClickListener(this);
		title.setText("选择原因");

		reson = "色情低俗";
		check1.setChecked(true);
		check2.setChecked(false);
		check3.setChecked(false);
		check4.setChecked(false);
		check5.setChecked(false);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.report_ok:

			sendFreeback();
			break;
		case R.id.report_item1:
			reson = "色情低俗";
			check1.setChecked(true);
			check2.setChecked(false);
			check3.setChecked(false);
			check4.setChecked(false);
			check5.setChecked(false);
			break;
		case R.id.report_item2:
			reson = "赌博";
			check1.setChecked(false);
			check2.setChecked(true);
			check3.setChecked(false);
			check4.setChecked(false);
			check5.setChecked(false);
			break;
		case R.id.report_item3:
			reson = "政治敏感";
			check1.setChecked(false);
			check2.setChecked(false);
			check3.setChecked(true);
			check4.setChecked(false);
			check5.setChecked(false);
			break;
		case R.id.report_item4:
			reson = "欺诈骗钱";
			check1.setChecked(false);
			check2.setChecked(false);
			check3.setChecked(false);
			check4.setChecked(true);
			check5.setChecked(false);
			break;
		case R.id.report_item5:
			reson = "违法（暴力恐怖、违禁品等）";
			check1.setChecked(false);
			check2.setChecked(false);
			check3.setChecked(false);
			check4.setChecked(false);
			check5.setChecked(true);
			break;

		default:
			break;
		}
	}

	private void sendFreeback() {
		final Gson gson = new Gson();
		RequestParams rp = new RequestParams();
		String url = Constant.ROOT_PATH + "/v1/reportUser/save";
		if (flag == 0) {
			url = Constant.ROOT_PATH + "/v1/reportUser/save";
			rp.addBodyParameter("byReportUserId", userId);
		}
		if (flag == 1) {
			rp.addBodyParameter("contactId", contactId);
			url = Constant.ROOT_PATH + "/v1/reportContact/save";
		}

		if (flag ==2) {
			rp.addBodyParameter("newsId", newsId);
			url = Constant.ROOT_PATH + "/v1_1_0/reportNews/report";
		}
		rp.addBodyParameter("sign", ShareDataTool.getToken(this));
		rp.addBodyParameter("reason", reson);
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		utils.send(HttpMethod.POST, url, rp, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				pro.setVisibility(View.VISIBLE);
				super.onStart();
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				pro.setVisibility(View.GONE);
				ToastUtils.displayFailureToast(ReportActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				pro.setVisibility(View.GONE);
				try {
					// Gson gson = new Gson();
					LogManager.LogShow("----", arg0.result, LogManager.ERROR);
					ReturnState state = gson.fromJson(arg0.result,
							ReturnState.class);
					if (Constant.RETURN_OK.equals(state.msg)) {
						ToastUtils.displayShortToast(ReportActivity.this,
								String.valueOf(state.result));
						finish();
					} else if (Constant.TOKEN_ERR.equals(state.msg)) {
						ToastUtils.displayShortToast(ReportActivity.this,
								"验证错误，请重新登录");
						ToosUtils.goReLogin(ReportActivity.this);
					} else {
						ToastUtils.displayShortToast(ReportActivity.this,
								String.valueOf(state.result));
					}
				} catch (Exception e) {
					ToastUtils.displaySendFailureToast(ReportActivity.this);
				}

			}
		});

	}
}
