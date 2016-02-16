package cn.yunluosoft.tonglou.activity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.easemob.chat.EMChatManager;
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
        locationEntities = new ArrayList<LocationEntity>();
        title = (TextView) findViewById(R.id.title_title);
        back = (ImageView) findViewById(R.id.title_back);
        listView = (ListView) findViewById(R.id.location_nearbay_list);
        pro = findViewById(R.id.location_nearbay_pro);
        adapter = new LocationNearbayAdapter(this,locationEntities);

        listView.setAdapter(adapter);
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
}
