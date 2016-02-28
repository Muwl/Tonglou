package cn.yunluosoft.tonglou.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.ConstactAdapter;
import cn.yunluosoft.tonglou.dialog.AddMarkDialog;
import cn.yunluosoft.tonglou.dialog.CustomeDialog;
import cn.yunluosoft.tonglou.dialog.RigDialog;
import cn.yunluosoft.tonglou.easemob.chatuidemo.db.InviteMessgeDao;
import cn.yunluosoft.tonglou.model.ConstaceDetailState;
import cn.yunluosoft.tonglou.model.ConstactDetailReEntity;
import cn.yunluosoft.tonglou.model.ConstantWithfloorEntity;
import cn.yunluosoft.tonglou.model.FloorSpeechEntity;
import cn.yunluosoft.tonglou.model.FloorSpeechState;
import cn.yunluosoft.tonglou.model.MessageInfo;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CustomListView;
import cn.yunluosoft.tonglou.view.CustomListView.OnLoadMoreListener;
import cn.yunluosoft.tonglou.view.CustomListView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Mu
 * @date 2015-8-6 下午12:58:32
 * @Description
 */
@SuppressLint("NewApi")
public class ConstactActivity extends BaseActivity implements OnClickListener,
        OnScrollListener {

    private CustomListView customListView;

    private ImageView back;

    private TextView title;

    private ImageView remark;

    private ConstactAdapter adapter;

    private TextView conversate;

    private TextView add;

    private View bomView;

    private View pro;

    private View gv;

    private String id;

    private String titleString;

    private ConstactDetailReEntity constactDetailReEntity;

    private ConstantWithfloorEntity infoEntity = null;

    private List<FloorSpeechEntity> entities;

    private int pageNo = 1;

    private boolean proShow = true;

    public boolean isFirst = true;

    private BitmapUtils bitmapUtils;

    private int flag;

    private View head;

    private String sname = "";

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 40:
                    int flag = msg.arg2;
                    if (flag == -1) {
                        addFriends();
                    }
                    if (flag == -100) {
                        addBlack();
                    }
                    break;

                case 104:
                    String temp = (String) msg.obj;
                    addRemark(temp);
                    break;
                case 81:
                    AddMarkDialog markDialog = new AddMarkDialog(
                            ConstactActivity.this, handler);
                    break;
                case 82:
                    Intent intent = new Intent(ConstactActivity.this,
                            ReportActivity.class);
                    intent.putExtra("flag", 0);
                    intent.putExtra("userId", infoEntity.id);
                    startActivity(intent);
                    break;

                case 83:
                    CustomeDialog customeDialog = new CustomeDialog(
                            ConstactActivity.this, handler, "确定加入黑名单？", -100, -100);

                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constact_detail);
        bitmapUtils = new BitmapUtils(this);
        initView();
    }

    private void initView() {
        id = getIntent().getStringExtra("id");
        titleString = getIntent().getStringExtra("name");
        flag = getIntent().getIntExtra("flag", 0);
        customListView = (CustomListView) findViewById(R.id.constact_detail_listview);
        back = (ImageView) findViewById(R.id.constact_detail_back);
        title = (TextView) findViewById(R.id.constact_detail_title);
        head = findViewById(R.id.constact_detail_head);
        remark = (ImageView) findViewById(R.id.constact_detail_remark);
        pro = findViewById(R.id.constact_detail_pro);
        gv = findViewById(R.id.constact_detail_gv);
        conversate = (TextView) findViewById(R.id.constact_detail_conversate);
        add = (TextView) findViewById(R.id.constact_detail_add);
        bomView = findViewById(R.id.constact_detail_bom);

        customListView.setTitle(title);
        title.setText(titleString);
        back.setOnClickListener(this);
        conversate.setOnClickListener(this);
        add.setOnClickListener(this);
        remark.setOnClickListener(this);
		entities = new ArrayList<FloorSpeechEntity>();
		remark.setVisibility(View.GONE);
        getInfo();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1005:
//                String id = data.getStringExtra("id");
//                String num = data.getStringExtra("num");
//                for (int i = 0; i < entities.size(); i++) {
//                    if (entities.get(i).id.equals(id)) {
//                        entities.get(i).commentAmount = entities.get(i).commentAmount
//                                + Integer.valueOf(num);
//                    }
//                }
//                adapter.notifyDataSetChanged();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (flag == 155) {
            Intent intent = new Intent(ConstactActivity.this,
                    ChatActivity.class);
            intent.putExtra("name", sname);
            setResult(RESULT_OK, intent);
            finish();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.constact_detail_back:
                if (flag == 155) {
                    Intent intent = new Intent(ConstactActivity.this,
                            ChatActivity.class);
                    intent.putExtra("name", sname);
                    setResult(RESULT_OK, intent);
                }
                finish();
                break;
            case R.id.constact_detail_remark:
                int flag = 0;
                int backflag = 0;
//
                if (Constant.RELATION_YES
                        .equals(constactDetailReEntity.relationship)) {
                    flag = 0;
                } else {
                    flag = 1;
                }
                if (Constant.BLACK_YES.equals(constactDetailReEntity.isBlack)) {
                    backflag = 0;
                } else {
                    backflag = 1;
                }

                RigDialog rigDialog = new RigDialog(ConstactActivity.this, handler,
                        flag, backflag);
                break;
            case R.id.constact_detail_conversate:
                Intent intent = new Intent(ConstactActivity.this,
                        ChatActivity.class);
                String myname = ShareDataTool.getNickname(ConstactActivity.this);
                if (ToosUtils.isStringEmpty(constactDetailReEntity.friendsToMe)) {
                    myname = ShareDataTool.getNickname(ConstactActivity.this);
                } else {
                    myname = constactDetailReEntity.friendsToMe;
                }

                String rename = infoEntity.nickname;
                if (ToosUtils.isStringEmpty(constactDetailReEntity.meToFriends)) {
                    rename = infoEntity.nickname;
                } else {
                    rename = constactDetailReEntity.meToFriends;
                }

                MessageInfo info = new MessageInfo(
                        ShareDataTool.getUserId(ConstactActivity.this), id,
                        ShareDataTool.getUserId(ConstactActivity.this), id,
                        ShareDataTool.getIcon(ConstactActivity.this),
                        infoEntity.icon, myname, rename);
                intent.putExtra("info", (Serializable) info);
                startActivity(intent);

                break;
            case R.id.constact_detail_add:
                CustomeDialog customeDialog = new CustomeDialog(this, handler,
                        "确定添加？", 0, -1);
                break;

            default:
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem > 1) {
            title.setAlpha(1);
        } else {
            title.setAlpha(0);
        }
    }



	/**
	 * 获取个人信息
	 */
	private void getInfo() {
		RequestParams rp = new RequestParams();
		rp.addBodyParameter("sign", ShareDataTool.getToken(this));
		rp.addBodyParameter("userId", id);
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		utils.send(HttpMethod.POST, Constant.ROOT_PATH
				+ "/v1/user/findUserDetail", rp, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				pro.setVisibility(View.VISIBLE);
				gv.setVisibility(View.GONE);
				customListView.setCanLoadMore(false);
				customListView.setCanRefresh(false);
				super.onStart();
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				pro.setVisibility(View.GONE);
				gv.setVisibility(View.GONE);
				ToastUtils.displayFailureToast(ConstactActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {

				try {
					Gson gson = new Gson();
					ReturnState state = gson.fromJson(arg0.result,
							ReturnState.class);
					if (Constant.RETURN_OK.equals(state.msg)) {
						gv.setVisibility(View.VISIBLE);
						LogManager.LogShow("-----", arg0.result,
								LogManager.ERROR);
						ConstaceDetailState floorState = gson.fromJson(
								arg0.result, ConstaceDetailState.class);
						constactDetailReEntity = floorState.result;
						infoEntity = constactDetailReEntity.userInfo;
						if (ShareDataTool.getUserId(ConstactActivity.this)
								.equals(id)) {
							bomView.setVisibility(View.GONE);
						} else {
							bomView.setVisibility(View.VISIBLE);
						}
						remark.setVisibility(View.VISIBLE);
						if (Constant.RELATION_YES
								.equals(constactDetailReEntity.relationship)) {
							add.setVisibility(View.GONE);
						} else {

							add.setVisibility(View.VISIBLE);
						}
						if (Constant.BLACK_YES
								.equals(constactDetailReEntity.isBlack)) {
							remark.setVisibility(View.GONE);
							bomView.setVisibility(View.GONE);
						} else {
							remark.setVisibility(View.VISIBLE);
							bomView.setVisibility(View.VISIBLE);
						}

						LogManager.LogShow("ffff", infoEntity.toString(),
								LogManager.ERROR);
						adapter = new ConstactAdapter(ConstactActivity.this,
								infoEntity, entities, DensityUtil
										.getScreenWidth(ConstactActivity.this),
								customListView, 0, bitmapUtils);
						customListView.setAdapter(adapter);
						customListView.setBitmapUtils(bitmapUtils);
						customListView
								.setOnRefreshListener(new OnRefreshListener() {
									@Override
									public void onRefresh() {
										customListView.setCanLoadMore(false);
										getInfoList(1);
									}
								});
						customListView
								.setOnLoadListener(new OnLoadMoreListener() {
									@Override
									public void onLoadMore() {
										getInfoList(pageNo + 1);
									}
								});
						getInfoList(1);
						customListView.setCanLoadMore(true);
						customListView.setCanRefresh(true);

					} else if (Constant.TOKEN_ERR.equals(state.msg)) {
						pro.setVisibility(View.GONE);
						ToastUtils.displayShortToast(ConstactActivity.this,
								"验证错误，请重新登录");
						ToosUtils.goReLogin(ConstactActivity.this);
					} else {
						pro.setVisibility(View.GONE);
						ToastUtils.displayShortToast(ConstactActivity.this,
								(String) state.result);
					}
				} catch (Exception e) {
					e.printStackTrace();
					pro.setVisibility(View.GONE);
					ToastUtils.displaySendFailureToast(ConstactActivity.this);
				}

			}
		});
	}

	/**
	 * 获取楼语列表
	 */
	private void getInfoList(final int page) {
		RequestParams rp = new RequestParams();
		rp.addBodyParameter("sign", ShareDataTool.getToken(this));
		rp.addBodyParameter("userId", id);
		rp.addBodyParameter("pageNo", String.valueOf(page));
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		utils.send(HttpMethod.POST, Constant.ROOT_PATH
				+ "/v1_1_0/dynamic/userDynamic", rp, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				pro.setVisibility(View.GONE);
				adapter.setFlag(1);
				ToastUtils.displayFailureToast(ConstactActivity.this);
				customListView.onRefreshComplete();
				customListView.onLoadMoreComplete();
				customListView.setCanLoadMore(false);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				adapter.setFlag(1);
				pro.setVisibility(View.GONE);
				gv.setVisibility(View.VISIBLE);
				try {
					Gson gson = new Gson();
					// LogManager.LogShow("----", arg0.result+"1111111111",
					// LogManager.ERROR);
					ReturnState allState = gson.fromJson(arg0.result,
							ReturnState.class);
					if (Constant.RETURN_OK.equals(allState.msg)) {
						pageNo = page;
						if (page == 1) {
							entities.clear();
							adapter.notifyDataSetChanged();
						}
						if (allState.result == null
								|| ToosUtils.isStringEmpty(String
										.valueOf(allState.result))) {
							customListView.onRefreshComplete();
							customListView.onLoadMoreComplete();
							customListView.setCanLoadMore(false);
							adapter.notifyDataSetChanged();
							// ToastUtils.displayShortToast(
							// MyFloorSpeechActivity.this, "无数据");
							return;
						}
						FloorSpeechState state = gson.fromJson(arg0.result,
								FloorSpeechState.class);
						if (state.result == null || state.result.size() == 0) {
							customListView.onRefreshComplete();
							customListView.onLoadMoreComplete();
							customListView.setCanLoadMore(false);
							adapter.notifyDataSetChanged();
							// ToastUtils.displayShortToast(
							// MyFloorSpeechActivity.this, "无数据");
						} else {
							for (int i = 0; i < state.result.size(); i++) {
								entities.add(state.result.get(i));
							}
							adapter.notifyDataSetChanged();
							if (pageNo == 1) {
								customListView.onRefreshComplete();
							} else {
								customListView.onRefreshComplete();
								customListView.onLoadMoreComplete();
							}
							customListView.setCanLoadMore(true);
						}

					} else {
						ReturnState state = gson.fromJson(arg0.result,
								ReturnState.class);
						if (Constant.TOKEN_ERR.equals(state.msg)) {
							ToastUtils.displayShortToast(ConstactActivity.this,
									"验证错误，请重新登录");
							// ToosUtils.goReLogin(getActivity());
						} else {
							ToastUtils.displayShortToast(ConstactActivity.this,
									(String) state.result);

						}
						customListView.onRefreshComplete();
						customListView.onLoadMoreComplete();
						customListView.setCanLoadMore(false);
					}

				} catch (Exception e) {
					e.printStackTrace();
					customListView.onRefreshComplete();
					customListView.onLoadMoreComplete();
					customListView.setCanLoadMore(false);
					ToastUtils.displaySendFailureToast(ConstactActivity.this);
				}

			}
		});

	}

	/**
	 * 加人脉
	 */
	private void addFriends() {
		RequestParams rp = new RequestParams();
		rp.addBodyParameter("sign", ShareDataTool.getToken(this));
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("toUserId", id);
		rp.addBodyParameter("contactVoStr", jsonObject.toString());
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		utils.send(HttpMethod.POST, Constant.ROOT_PATH + "/v1/contact/save",
				rp, new RequestCallBack<String>() {
					@Override
					public void onStart() {
						pro.setVisibility(View.VISIBLE);
						super.onStart();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						pro.setVisibility(View.GONE);
						ToastUtils.displayFailureToast(ConstactActivity.this);
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
										ConstactActivity.this,
										String.valueOf(state.result));
								constactDetailReEntity.relationship = Constant.RELATION_YES;
								// aremark.setVisibility(View.VISIBLE);
								add.setVisibility(View.GONE);
							} else if (Constant.TOKEN_ERR.equals(state.msg)) {
								ToastUtils.displayShortToast(
										ConstactActivity.this, "验证错误，请重新登录");
								ToosUtils.goReLogin(ConstactActivity.this);
							} else {
								ToastUtils.displayShortToast(
										ConstactActivity.this,
										String.valueOf(state.result));
							}
						} catch (Exception e) {
							ToastUtils
									.displaySendFailureToast(ConstactActivity.this);
						}

					}
				});

	}

	/**
	 * 加人脉
	 */
	private void addRemark(final String temp) {
		RequestParams rp = new RequestParams();
		rp.addBodyParameter("sign", ShareDataTool.getToken(this));
		rp.addBodyParameter("toUserId", id);
		rp.addBodyParameter("remarkName", temp);
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		utils.send(HttpMethod.POST, Constant.ROOT_PATH
				+ "/v1/contact/updateRemarkName", rp,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						pro.setVisibility(View.VISIBLE);
						super.onStart();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						pro.setVisibility(View.GONE);
						ToastUtils.displayFailureToast(ConstactActivity.this);
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
										ConstactActivity.this, "修改成功");
								title.setText(String.valueOf(state.result));
								infoEntity.nickname = String
										.valueOf(state.result);
								sname = String.valueOf(state.result);
								setCon(String.valueOf(state.result));
								adapter.notifyDataSetChanged();
							} else if (Constant.TOKEN_ERR.equals(state.msg)) {
								ToastUtils.displayShortToast(
										ConstactActivity.this, "验证错误，请重新登录");
								ToosUtils.goReLogin(ConstactActivity.this);
							} else {
								ToastUtils.displayShortToast(
										ConstactActivity.this,
										String.valueOf(state.result));
							}
						} catch (Exception e) {
							ToastUtils
									.displaySendFailureToast(ConstactActivity.this);
						}

					}
				});

	}

	private void setCon(String temp) {
		InviteMessgeDao dao = new InviteMessgeDao(this);
		List<EMConversation> conversations = loadConversationsWithRecentChat(temp);
	}

	/**
	 * 获取所有会话
	 *
	 * @param
	 * @return +
	 */
	private List<EMConversation> loadConversationsWithRecentChat(String name) {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					sortList.add(new Pair<Long, EMConversation>(conversation
							.getLastMessage().getMsgTime(), conversation));
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			if (sortItem.second.getLastMessage().getFrom()
					.equals(Constant.SYS_NAME)
					|| sortItem.second.getLastMessage().getTo()
							.equals(Constant.SYS_NAME)
					|| sortItem.second.getLastMessage().getFrom()
							.equals(Constant.SYS_GETNAME)
					|| sortItem.second.getLastMessage().getFrom()
							.equals(Constant.SYS_GETNAME)) {
				EMChatManager.getInstance().deleteConversation(
						sortItem.second.getUserName(),
						sortItem.second.isGroup(), true);
				InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(this);
				inviteMessgeDao.deleteMessage(sortItem.second.getUserName());
				continue;
			} else {
				if (sortItem.second.getLastMessage().getFrom().equals(id)) {
					sortItem.second.getLastMessage().setAttribute(
							"senderNickName", name);
				} else if (sortItem.second.getLastMessage().getTo().equals(id)) {
					sortItem.second.getLastMessage().setAttribute(
							"receiverNickName", name);
				}
				EMChatManager.getInstance().updateMessageBody(
						sortItem.second.getLastMessage());
				list.add(sortItem.second);
			}
		}
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 *
	 * @param
	 */
	private void sortConversationByLastChatTime(
			List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList,
				new Comparator<Pair<Long, EMConversation>>() {
					@Override
					public int compare(final Pair<Long, EMConversation> con1,
							final Pair<Long, EMConversation> con2) {

						if (con1.first == con2.first) {
							return 0;
						} else if (con2.first > con1.first) {
							return 1;
						} else {
							return -1;
						}
					}

				});
	}

	/**
	 * 加人脉
	 */
	private void addBlack() {
		RequestParams rp = new RequestParams();
		rp.addBodyParameter("sign", ShareDataTool.getToken(this));
		rp.addBodyParameter("blackUserId", id);
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		utils.send(HttpMethod.POST, Constant.ROOT_PATH + "/v1/userBlack/save",
				rp, new RequestCallBack<String>() {
					@Override
					public void onStart() {
						pro.setVisibility(View.VISIBLE);
						super.onStart();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						pro.setVisibility(View.GONE);
						ToastUtils.displayFailureToast(ConstactActivity.this);
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
										ConstactActivity.this, "添加成功");
								finish();
							} else if (Constant.TOKEN_ERR.equals(state.msg)) {
								ToastUtils.displayShortToast(
										ConstactActivity.this, "验证错误，请重新登录");
								ToosUtils.goReLogin(ConstactActivity.this);
							} else {
								ToastUtils.displayShortToast(
										ConstactActivity.this,
										String.valueOf(state.result));
							}
						} catch (Exception e) {
							ToastUtils
									.displaySendFailureToast(ConstactActivity.this);
						}

					}
				});

	}

}
