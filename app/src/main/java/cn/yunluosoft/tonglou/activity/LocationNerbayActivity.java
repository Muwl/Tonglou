package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.LocationNearbayAdapter;
import cn.yunluosoft.tonglou.model.LocationEntity;
import cn.yunluosoft.tonglou.model.LocationState;
import cn.yunluosoft.tonglou.model.PerfectDataState;
import cn.yunluosoft.tonglou.model.PersonInfo;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.MyApplication;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;

/**
 * Created by Mu on 2016/1/26.
 */
public class LocationNerbayActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private ListView listView;

    private LocationNearbayAdapter adapter;

    private View pro;

    private LocationClient mLocationClient;

    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;

    private String tempcoor = "gcj02";

    public MyLocationListener mMyLocationListener;

    private BDLocation bdLocation = null;

    private List<LocationEntity> locationEntities;

    private String stag;

    private int flag;// 0代表注册 1代表修改

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_nearbay);
        initView();
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        InitLocation();
        mLocationClient.start();
    }

    private void initView() {
        flag = getIntent().getIntExtra("flag", 0);
        locationEntities = new ArrayList<LocationEntity>();
        title = (TextView) findViewById(R.id.title_title);
        back = (ImageView) findViewById(R.id.title_back);
        listView = (ListView) findViewById(R.id.location_nearbay_list);
        pro = findViewById(R.id.location_nearbay_pro);
        adapter = new LocationNearbayAdapter(this,locationEntities);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateLocation(locationEntities.get(position));
            }
        });
        title.setText("附近");
        back.setOnClickListener(this);

    }

    private void InitLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);// 设置定位模式
        option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
        int span = 1000;
        option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
        // option.setIsNeedAddress(checkGeoLocation.isChecked());
        mLocationClient.setLocOption(option);
    }

    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                bdLocation = location;
                stag = String.valueOf(System.currentTimeMillis());
                getLocations(stag);
                LogManager.LogShow("------", "++++++++++++++++++",
                        LogManager.ERROR);
                mLocationClient.stop();

            }

        }

    }

    @Override
    protected void onStop() {
        mLocationClient.stop();
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    /**
     * 查询楼
     *
     * @param tag  标示
     */
    private void getLocations(String tag) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("longitude",
                String.valueOf(bdLocation.getLongitude()));
        rp.addBodyParameter("latitude",
                String.valueOf(bdLocation.getLatitude()));

        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        RequestCallBack<String> requestCallBack = new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LogManager.LogShow("------", "ccccccccccccccccccc",
                        LogManager.ERROR);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                try {
                    if (stag.equals(userTag)) {
                        Gson gson = new Gson();
                        ReturnState state = gson.fromJson(arg0.result,
                                ReturnState.class);
                        LogManager.LogShow("------", arg0.result,
                                LogManager.ERROR);
                        if (Constant.RETURN_OK.equals(state.msg)) {
                            locationEntities.clear();
                            adapter.notifyDataSetChanged();
                            LocationState locationState = gson.fromJson(
                                    arg0.result, LocationState.class);
                            for (int i = 0; i < locationState.result.size(); i++) {
                                locationEntities.add(locationState.result
                                        .get(i));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        requestCallBack.setUserTag(tag);
        String url = "/v1/building/findByLocation";
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH + url, rp,
                requestCallBack);
    }


    private void updateLocation(final LocationEntity entity) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("buildingId", entity.id);
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH
                + "/v1/user/updateUserLocation", rp, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                pro.setVisibility(View.VISIBLE);
                super.onStart();
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                pro.setVisibility(View.GONE);
                ToastUtils.displayFailureToast(LocationNerbayActivity.this);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                pro.setVisibility(View.GONE);
                try {
                    // Gson gson = new Gson();
                    LogManager.LogShow("----", arg0.result, LogManager.ERROR);
                    Gson gson = new Gson();
                    ReturnState state = gson.fromJson(arg0.result,
                            ReturnState.class);
                    if (Constant.RETURN_OK.equals(state.msg)) {
                        ShareDataTool
                                .SaveInfoDetail(
                                        LocationNerbayActivity.this,
                                        ShareDataTool
                                                .getNickname(LocationNerbayActivity.this),
                                        ShareDataTool
                                                .getIcon(LocationNerbayActivity.this),
                                        (String) entity.name, entity.id);
                        ShareDataTool.saveUpdateFlag(LocationNerbayActivity.this,
                                1);

                        if (flag==0){
                            loginHX(ShareDataTool
                                            .getImUsername(LocationNerbayActivity.this),
                                    ShareDataTool
                                            .getImPassword(LocationNerbayActivity.this));
                        }else if(flag==1){
                            Intent intent=new Intent(LocationNerbayActivity.this,
                                    MainActivity.class);
                            intent.putExtra("index",4);
                            startActivity(intent);
                            finish();
                        }
                    } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                        ToastUtils.displayShortToast(LocationNerbayActivity.this,
                                "验证错误，请重新登录");
                        ToosUtils.goReLogin(LocationNerbayActivity.this);
                    } else {
                        ToastUtils.displayShortToast(LocationNerbayActivity.this,
                                String.valueOf(state.result));
                    }
                } catch (Exception e) {
                    ToastUtils
                            .displaySendFailureToast(LocationNerbayActivity.this);
                }

            }
        });

    }


    private void loginHX(final String currentUsername,
                         final String currentPassword) {
        pro.setVisibility(View.VISIBLE);
        EMChatManager.getInstance().login(currentUsername, currentPassword,
                new EMCallBack() {
                    @Override
                    public void onSuccess() {

                        // 登陆成功，保存用户名密码
                        MyApplication.getInstance()
                                .setUserName(currentUsername);
                        MyApplication.getInstance()
                                .setPassword(currentPassword);

                        try {
                            // pro.setVisibility(View.GONE);
                            // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                            // ** manually load all local groups and
                            // conversations in case we are auto login
                            EMChatManager
                                    .getInstance()
                                    .updateCurrentUserNick(
                                            ShareDataTool
                                                    .getNickname(LocationNerbayActivity.this));
                            EMGroupManager.getInstance().loadAllGroups();
                            EMChatManager.getInstance().loadAllConversations();
                        } catch (Exception e) {
                            e.printStackTrace();
                            // 取好友或者群聊失败，不让进入主页面
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    MyApplication.getInstance().logout(null);
                                    ToastUtils.displayShortToast(
                                            LocationNerbayActivity.this, "登录失败");
                                }
                            });
                            return;
                        }

                        // 进入主页面
                        LogManager.LogShow("-----", "登录成功", LogManager.ERROR);

                        startActivity(new Intent(LocationNerbayActivity.this,
                                MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(final int code, final String message) {

                        runOnUiThread(new Runnable() {
                            public void run() {
                                // pro.setVisibility(View.GONE);
                                ToastUtils.displayShortToast(
                                        LocationNerbayActivity.this, "登录失败");
                            }
                        });
                    }
                });

    }
}
