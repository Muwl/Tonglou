package cn.yunluosoft.tonglou.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.ls.LSInput;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import u.aly.ad;
import u.aly.ba;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.BlackAdaper;
import cn.yunluosoft.tonglou.dialog.CustomeDialog;
import cn.yunluosoft.tonglou.model.BlackEntity;
import cn.yunluosoft.tonglou.model.BlackState;
import cn.yunluosoft.tonglou.model.FriendComparator;
import cn.yunluosoft.tonglou.model.FriendState;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.sortlistview.SideBar.OnTouchingLetterChangedListener;
import cn.yunluosoft.tonglou.view.swipelist.SwipeMenuListView;

/**
 * @author Mu
 * @date 2015-9-22 下午2:05:42
 * @Description 黑名单
 */
public class BlackActivity extends BaseActivity implements OnClickListener {

	private ImageView back;

	private TextView title;

	private SwipeMenuListView listView;

	private View pro;

	private View empty;

	private ImageView empty_image;

	private TextView empty_text;

	private BlackAdaper adaper;

	private List<BlackEntity> entities;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 40:
				int position = msg.arg1;
				delBlack(position);
				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.black);
		initView();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		listView = (SwipeMenuListView) findViewById(R.id.black_list);
		empty = findViewById(R.id.black_empty);
		empty_image = (ImageView) findViewById(R.id.empty_image);
		empty_text = (TextView) findViewById(R.id.empty_text);
		pro = findViewById(R.id.black_pro);
		entities = new ArrayList<BlackEntity>();
		adaper = new BlackAdaper(this, entities);
		listView.setAdapter(adaper);

		title.setText("黑名单");
		back.setOnClickListener(this);
		getInfo();

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				CustomeDialog customeDialog = new CustomeDialog(
						BlackActivity.this, handler, "确定删除？", position, -1,null);
				// delete(position);
				return true;
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(BlackActivity.this,
						ConstactActivity.class);
				intent.putExtra("id", entities.get(position).id);
				intent.putExtra("name", entities.get(position).nickname);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;

		default:
			break;
		}
	}

	private void getInfo() {
		RequestParams rp = new RequestParams();
		rp.addBodyParameter("sign", ShareDataTool.getToken(this));
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		utils.send(HttpMethod.POST, Constant.ROOT_PATH + "/v1/userBlack/query",
				rp, new RequestCallBack<String>() {
					@Override
					public void onStart() {
						pro.setVisibility(View.VISIBLE);
						super.onStart();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						pro.setVisibility(View.GONE);
						ToastUtils.displayFailureToast(BlackActivity.this);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						pro.setVisibility(View.GONE);
						try {
							// Gson gson = new Gson();
							LogManager.LogShow("----", arg0.result,
									LogManager.ERROR);
							Gson gson = new Gson();
							ReturnState state = gson.fromJson(arg0.result,
									ReturnState.class);
							if (Constant.RETURN_OK.equals(state.msg)) {
								BlackState blackState = gson.fromJson(
										arg0.result, BlackState.class);
								if (blackState.result != null
										&& blackState.result.size() != 0) {
									for (int i = 0; i < blackState.result
											.size(); i++) {
										entities.add(blackState.result.get(i));
									}
									adaper.notifyDataSetChanged();
								}
								reFushEmpty();
							} else if (Constant.TOKEN_ERR.equals(state.msg)) {
								ToastUtils.displayShortToast(
										BlackActivity.this, "验证错误，请重新登录");
								ToosUtils.goReLogin(BlackActivity.this);
							} else {
								ToastUtils.displayShortToast(
										BlackActivity.this,
										String.valueOf(state.result));
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils
									.displaySendFailureToast(BlackActivity.this);
						}

					}
				});

	}

	public void reFushEmpty() {
		if (entities == null || entities.size() == 0) {
			empty.setVisibility(View.VISIBLE);
//			empty_image.setImageDrawable(getResources().getDrawable(
//					R.mipmap.empty_black));
			empty_text.setText("没有黑名单");
		} else {
			empty.setVisibility(View.GONE);
		}
	}

	private void delBlack(final int position) {
		RequestParams rp = new RequestParams();
		rp.addBodyParameter("sign", ShareDataTool.getToken(this));
		rp.addBodyParameter("blackUserId", entities.get(position).id);
		HttpUtils utils = new HttpUtils();
		LogManager.LogShow("----", ShareDataTool.getToken(this),
				LogManager.ERROR);
		LogManager.LogShow("----", entities.get(position).id, LogManager.ERROR);
		utils.configTimeout(20000);
		utils.send(HttpMethod.POST, Constant.ROOT_PATH + "/v1/userBlack/del",
				rp, new RequestCallBack<String>() {
					@Override
					public void onStart() {
						pro.setVisibility(View.VISIBLE);
						super.onStart();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						pro.setVisibility(View.GONE);
						ToastUtils.displayFailureToast(BlackActivity.this);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						pro.setVisibility(View.GONE);
						try {
							// Gson gson = new Gson();
							LogManager.LogShow("----", arg0.result,
									LogManager.ERROR);
							Gson gson = new Gson();
							ReturnState state = gson.fromJson(arg0.result,
									ReturnState.class);
							if (Constant.RETURN_OK.equals(state.msg)) {
								ToastUtils.displayShortToast(
										BlackActivity.this,
										String.valueOf(state.result));
								entities.remove(position);
								adaper.notifyDataSetChanged();
								reFushEmpty();
							} else if (Constant.TOKEN_ERR.equals(state.msg)) {
								ToastUtils.displayShortToast(
										BlackActivity.this, "验证错误，请重新登录");
								ToosUtils.goReLogin(BlackActivity.this);
							} else {
								ToastUtils.displayShortToast(
										BlackActivity.this,
										String.valueOf(state.result));
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils
									.displaySendFailureToast(BlackActivity.this);
						}

					}
				});

	}
}
