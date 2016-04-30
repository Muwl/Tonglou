package cn.yunluosoft.tonglou.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.LocationAdapter;
import cn.yunluosoft.tonglou.dialog.CustomeDialog;
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
import cn.yunluosoft.tonglou.view.CircleImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author Mu
 * @date 2015-8-2上午9:28:07
 * @description
 */
public class LocationSelActivity extends BaseActivity implements
        OnClickListener {

    private LocationClient mLocationClient;

    private LocationMode tempMode = LocationMode.Hight_Accuracy;

    private String tempcoor = "gcj02";

    public MyLocationListener mMyLocationListener;

    private BDLocation bdLocation = null;

    private ImageView back;

    private TextView title;

    private EditText name;

    private TextView go;

    private View pro;

    private int flag;// 0代表注册 1代表修改

    private View root;

//    private PersonInfo info;
//
//    private String path;

    private List<LocationEntity> locationEntities;

    private ListView listView;

    private String stag;

    private LocationAdapter adapter;

    private LocationEntity locationEntity;

    private boolean checkFlag = true;

    private BitmapUtils bitmapUtils;

    private TextView ok;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 40:
                    Intent intent=new Intent(LocationSelActivity.this,LoactionAddActivity.class);
                    intent.putExtra("flag",flag);
                    startActivity(intent);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_select);
        bitmapUtils = new BitmapUtils(this);

        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        InitLocation();
        initView();
        mLocationClient.start();
    }

    private void initView() {
        flag = getIntent().getIntExtra("flag", 0);
        if (flag==1){
            locationEntity= (LocationEntity) getIntent().getSerializableExtra("entity");
        }
        locationEntities = new ArrayList<LocationEntity>();
        adapter = new LocationAdapter(this, locationEntities);
        root = findViewById(R.id.location_root);
        back = (ImageView) findViewById(R.id.title_back);
        title = (TextView) findViewById(R.id.title_title);
        name = (EditText) findViewById(R.id.location_name);
        listView = (ListView) findViewById(R.id.location_listview);
        ok = (TextView) findViewById(R.id.title_rig);
        go = (TextView) findViewById(R.id.location_ok);
        pro = findViewById(R.id.location_pro);
        // CircleImageView circleImageView=(CircleImageView)
        // findViewById(R.id.location_icon);
        // bitmapUtils.display(circleImageView, path);
        listView.setAdapter(adapter);
        if (flag == 0) {
            root.setBackgroundColor(getResources().getColor(R.color.bg));
        } else {
            root.setBackgroundColor(getResources()
                    .getColor(R.color.activity_bg));
        }

        if (locationEntity!=null){
            name.setText(locationEntity.name);
        }

        back.setOnClickListener(this);
        ok.setOnClickListener(this);
        ok.setText("完成");
//        ok.setVisibility(View.VISIBLE);
        title.setText("位置");
        go.setOnClickListener(this);

        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // LogManager.LogShow("--------", "aaaaaaaaaaaaaa",
                // LogManager.ERROR);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // LogManager
                // .LogShow("--------", "bbbbbbbbbbbb", LogManager.ERROR);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // LogManager.LogShow("--------", "ccccccccccccc",
                // LogManager.ERROR);
                if (checkFlag) {
                    if (!ToosUtils.isStringEmpty(s.toString())) {
                        stag = String.valueOf(System.currentTimeMillis());
                        getLocations(stag, 1);

                    } else {
                        locationEntities.clear();
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    checkFlag = true;
                }

            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                locationEntity = locationEntities.get(position);
                checkFlag = false;
                name.setText(locationEntities.get(position).name);
                locationEntities.clear();
                adapter.notifyDataSetChanged();
            }
        });

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
                getLocations(stag, 0);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.location_ok:
                if (flag == 0) {
                    if (locationEntity != null
                            && locationEntity.name.equals(ToosUtils
                            .getTextContent(name))) {
                        updateLocation();
                    } else {
//                        ToastUtils.displayShortToast(LocationSelActivity.this,
//                                "请选择您所在楼宇");
                        CustomeDialog dialog=new CustomeDialog(LocationSelActivity.this,handler,"请输入的楼语暂时还未收录\n请点击添加楼宇名称！",-1,-1,"添加");
                    }

                } else {
                    if (locationEntity != null
                            && locationEntity.name.equals(ToosUtils
                            .getTextContent(name))) {
                        updateLocation();
                    } else {
                        CustomeDialog dialog=new CustomeDialog(LocationSelActivity.this,handler,"请输入的楼语暂时还未收录\n请点击添加楼宇名称！",-1,-1,"添加");
//                        ToastUtils.displayShortToast(LocationSelActivity.this,
//                                "请选择您所在楼宇");

                    }
                }
                break;
//            case R.id.location_ok:
//                Intent intent=new Intent(LocationSelActivity.this,LoactionAddActivity.class);
//                startActivity(intent);
//                break;

            default:
                break;
        }
    }

    /**
     * 查询楼
     *
     * @param tag  标示
     * @param type 0代表经纬度 1代表名称
     */
    private void getLocations(String tag, int type) {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        if (type == 0) {
            rp.addBodyParameter("longitude",
                    String.valueOf(bdLocation.getLongitude()));
            rp.addBodyParameter("latitude",
                    String.valueOf(bdLocation.getLatitude()));
        } else {
            rp.addBodyParameter("name", ToosUtils.getTextContent(name));
        }
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
        String url = "/v1/building/findByName";
        if (type == 0) {
            url = "/v1/building/findByLocation";
        } else {
            url = "/v1/building/findByName";
        }
        utils.send(HttpMethod.POST, Constant.ROOT_PATH + url, rp,
                requestCallBack);
    }

//    private void sendSub() {
//        final Gson gson = new Gson();
//        RequestParams rp = new RequestParams();
//        info.location = locationEntity.name;
//        info.buildingId = locationEntity.id;
//        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
//        rp.addBodyParameter("info", gson.toJson(info));
//        rp.addBodyParameter("icon", new File(path));
//        LogManager.LogShow("------", gson.toJson(info), LogManager.ERROR);
//        HttpUtils utils = new HttpUtils();
//        utils.configTimeout(20000);
//        utils.send(HttpMethod.POST, Constant.ROOT_PATH
//                        + "/v1/user/saveOrUpdateInfo", rp,
//                new RequestCallBack<String>() {
//                    @Override
//                    public void onStart() {
//                        pro.setVisibility(View.VISIBLE);
//                        super.onStart();
//                    }
//
//                    @Override
//                    public void onFailure(HttpException arg0, String arg1) {
//                        pro.setVisibility(View.GONE);
//                        ToastUtils
//                                .displayFailureToast(LocationSelActivity.this);
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseInfo<String> arg0) {
//                        pro.setVisibility(View.GONE);
//                        try {
//                            // Gson gson = new Gson();
//                            LogManager.LogShow("----", arg0.result,
//                                    LogManager.ERROR);
//                            ReturnState state = gson.fromJson(arg0.result,
//                                    ReturnState.class);
//                            if (Constant.RETURN_OK.equals(state.msg)) {
//                                PerfectDataState dataState = gson.fromJson(
//                                        arg0.result, PerfectDataState.class);
//                                ShareDataTool.SaveInfoDetail(
//                                        LocationSelActivity.this,
//                                        dataState.result.nickname,
//                                        dataState.result.icon,
//                                        dataState.result.location,locationEntity.id);
//                                ShareDataTool.SaveFlag(
//                                        LocationSelActivity.this, 1);
//                                loginHX(ShareDataTool
//                                                .getImUsername(LocationSelActivity.this),
//                                        ShareDataTool
//                                                .getImPassword(LocationSelActivity.this));
//                                // Intent intent = new Intent(
//                                // LocationSelActivity.this,
//                                // MainActivity.class);
//                                // startActivity(intent);
//                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
//                                ToastUtils.displayShortToast(
//                                        LocationSelActivity.this, "验证错误，请重新登录");
//                                ToosUtils.goReLogin(LocationSelActivity.this);
//                            } else {
//                                ToastUtils.displayShortToast(
//                                        LocationSelActivity.this,
//                                        String.valueOf(state.result));
//                            }
//                        } catch (Exception e) {
//                            ToastUtils
//                                    .displaySendFailureToast(LocationSelActivity.this);
//                        }
//
//                    }
//                });
//
//    }

    private void updateLocation() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("buildingId", locationEntity.id);
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpMethod.POST, Constant.ROOT_PATH
                + "/v1/user/updateUserLocation", rp, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                pro.setVisibility(View.VISIBLE);
                super.onStart();
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                pro.setVisibility(View.GONE);
                ToastUtils.displayFailureToast(LocationSelActivity.this);
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
                                        LocationSelActivity.this,
                                        ShareDataTool
                                                .getNickname(LocationSelActivity.this),
                                        ShareDataTool
                                                .getIcon(LocationSelActivity.this),
                                        (String) locationEntity.name, locationEntity.id);
                        if (flag==0){
                            loginHX(ShareDataTool
                                                .getImUsername(LocationSelActivity.this),
                                        ShareDataTool
                                                .getImPassword(LocationSelActivity.this));
                        }else{
                            ToastUtils.displayShortToast(LocationSelActivity.this,
                                    "修改成功");
                            finish();
                        }

                    } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                        ToastUtils.displayShortToast(LocationSelActivity.this,
                                "验证错误，请重新登录");
                        ToosUtils.goReLogin(LocationSelActivity.this);
                    } else {
                        ToastUtils.displayShortToast(LocationSelActivity.this,
                                String.valueOf(state.result));
                    }
                } catch (Exception e) {
                    ToastUtils
                            .displaySendFailureToast(LocationSelActivity.this);
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
                                                    .getNickname(LocationSelActivity.this));
                            EMGroupManager.getInstance().loadAllGroups();
                            EMChatManager.getInstance().loadAllConversations();
                        } catch (Exception e) {
                            e.printStackTrace();
                            // 取好友或者群聊失败，不让进入主页面
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    MyApplication.getInstance().logout(null);
                                    ToastUtils.displayShortToast(
                                            LocationSelActivity.this, "登录失败");
                                }
                            });
                            return;
                        }

                        // 进入主页面
                        LogManager.LogShow("-----", "登录成功", LogManager.ERROR);
                        Intent intent = new Intent(LocationSelActivity.this,MainActivity.class);
                        intent.putExtra("index",2);
                        startActivity(intent);
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
                                        LocationSelActivity.this, "登录失败");
                            }
                        });
                    }
                });

    }
}
