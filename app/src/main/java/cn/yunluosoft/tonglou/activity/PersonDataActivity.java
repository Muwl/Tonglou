package cn.yunluosoft.tonglou.activity;

import java.io.File;

import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.dialog.DateSelectDialog;
import cn.yunluosoft.tonglou.dialog.PhotoDialog;
import cn.yunluosoft.tonglou.dialog.SexSelectDialog;
import cn.yunluosoft.tonglou.dialog.TradeSelectDialog;
import cn.yunluosoft.tonglou.model.PerfectDataState;
import cn.yunluosoft.tonglou.model.PersonInfo;
import cn.yunluosoft.tonglou.model.PersonInfoState;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.TimeUtils;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * @author Mu
 * @date 2015-8-5 上午9:43:52
 * @Description
 */
public class PersonDataActivity extends BaseActivity implements OnClickListener {

    private ImageView back;

    private TextView title;

    private TextView save;

    private CircleImageView icon;

    private TextView name;

    private TextView sex;

    private TextView birth;

    private View nameView;

    private View sexView;

    private View iconView;

    private View birthView;

    private TextView trade;

    private View tradeView;

    private TextView job;

    private View jobView;

    private TextView signature;

    private View signatureView;

    private View pro;

    private View gv;

    public final int PHOTO_PICKED_WITH_DATA = 3021;

    public final int CAMERA_WITH_DATA = 3023;

    public final int CONTENT_WITH_DATA = 3024;

    private Bitmap bitmap = null;

    private File tempFile;

    private String sdate;

    private int emotionFlag;

    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";

    private int tradIndex = 0;

    private PersonInfo info;

    private BitmapUtils bitmapUtils;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 91:
                    // tradIndex = msg.arg1;
                    // trade.setText((String) msg.obj);
                    PersonInfo personInfo = new PersonInfo();
                    personInfo.industry = (String) msg.obj;
                    sendUpdate(personInfo, null, 5, (String) msg.obj);
                    break;
                case 94:
                    // tradIndex = msg.arg1;
                    // trade.setText((String) msg.obj);
                    PersonInfo personInfo3 = new PersonInfo();
                    personInfo3.sex = (String) msg.obj;
                    sendUpdate(personInfo3, null, 4, (String) msg.obj);
                    break;
                case 51:
                    sdate = (String) msg.obj;
//                    birth.setText(sdate);
                    PersonInfo personInfo2 = new PersonInfo();
                    personInfo2.birthday = (String) msg.obj;
                    sendUpdate(personInfo2, null, 3, sdate);
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
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_data);
        sdate = TimeUtils.getDate();
        bitmapUtils = new BitmapUtils(this);
        initView();
        getInfo();
    }

    @SuppressLint("CutPasteId")
    private void initView() {
        title = (TextView) findViewById(R.id.title_title);
        back = (ImageView) findViewById(R.id.title_back);
        icon = (CircleImageView) findViewById(R.id.person_data_icon);
        iconView = findViewById(R.id.person_data_iconview);
        name = (TextView) findViewById(R.id.person_data_name);
        sex = (TextView) findViewById(R.id.person_data_sex);
        birth = (TextView) findViewById(R.id.person_data_birth);
        birthView = findViewById(R.id.person_data_birthview);
        nameView = findViewById(R.id.person_data_nameview);
        sexView = findViewById(R.id.person_data_sexview);
        jobView = findViewById(R.id.person_data_jobview);
        signatureView = findViewById(R.id.person_data_signview);
        tradeView = findViewById(R.id.person_data_tradview);
        trade = (TextView) findViewById(R.id.person_data_trad);
        job = (TextView) findViewById(R.id.person_data_job);
        signature = (TextView) findViewById(R.id.person_data_sign);

        pro = findViewById(R.id.person_data_pro);
        gv = findViewById(R.id.person_data_gv);

        title.setText("个人资料");
        back.setOnClickListener(this);
        iconView.setOnClickListener(this);
        birthView.setOnClickListener(this);
        tradeView.setOnClickListener(this);
        nameView.setOnClickListener(this);
        sexView.setOnClickListener(this);
        jobView.setOnClickListener(this);
        signatureView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;


            case R.id.person_data_birthview:
                DateSelectDialog selectDialog = new DateSelectDialog(
                        PersonDataActivity.this, handler, sdate);

                break;

            case R.id.person_data_iconview:
                PhotoDialog dialog = new PhotoDialog(PersonDataActivity.this,
                        handler);

                break;

            case R.id.person_data_tradview:
                TradeSelectDialog selectDialog2 = new TradeSelectDialog(
                        PersonDataActivity.this, handler,
                        ToosUtils.getTextContent(trade));

                break;

            case R.id.person_data_nameview:
                Intent intent = new Intent(PersonDataActivity.this, UpdateNameActivity.class);
                startActivityForResult(intent, 785);
                // startActivity(intent);
                break;

            case R.id.person_data_sexview:
                SexSelectDialog selectDialog3 = new SexSelectDialog(
                        PersonDataActivity.this, handler,
                        ToosUtils.getTextContent(sex));

                break;

            case R.id.person_data_jobview:
                Intent intent5 = new Intent(PersonDataActivity.this, UpdateJobActivity.class);
//                startActivity(intent5);
                startActivityForResult(intent5, 786);
                break;
            case R.id.person_data_signview:
                Intent intent4 = new Intent(PersonDataActivity.this, UpdateSignActivity.class);
//                startActivity(intent4);
                startActivityForResult(intent4, 787);
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
                case 785:
                    String temp=data.getStringExtra("name");
                    name.setText(temp);
                    break;
                case 786:
                    String temp1=data.getStringExtra("name");
                    job.setText(temp1);
                    break;
                case 787:
                    String temp2=data.getStringExtra("name");
                    signature.setText(temp2);
                    break;
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
                        ToastUtils.displayShortToast(PersonDataActivity.this,
                                "未找到存储卡，无法存储照片！");
                    }
                    break;
                case PHOTO_PICKED_WITH_DATA:
                    bitmap = data.getParcelableExtra("data");

                    if (bitmap != null) {
                        File file = null;
                        if (Environment.getExternalStorageState().equals(
                                Environment.MEDIA_MOUNTED)) {

                            file = ToosUtils.saveImage2SD(
                                    Environment.getExternalStorageDirectory() + "/louyu/"
                                            + String.valueOf(System.currentTimeMillis())
                                            + ".JPEG", bitmap);
                            sendUpdate(null, file, 1, null);
//                            rp.addBodyParameter("icon", file);
                        } else {
                            ToastUtils.displayShortToast(PersonDataActivity.this,
                                    "无SD卡,无法上传图片");
                            return;
                        }

                    }
//                    icon.setImageBitmap(bitmap);
                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 剪切图片
     *
     * @param uri
     * @function:
     * @author:Jerry
     * @date:2013-12-30
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

    /**
     * 获取个人信息
     */
    private void getInfo() {
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH
                + "/v1/user/findUserInfo", rp, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                pro.setVisibility(View.VISIBLE);
                gv.setVisibility(View.GONE);
                super.onStart();
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                pro.setVisibility(View.GONE);
                gv.setVisibility(View.GONE);
                ToastUtils.displayFailureToast(PersonDataActivity.this);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                pro.setVisibility(View.GONE);
                try {
                    Gson gson = new Gson();
                    ReturnState state = gson.fromJson(arg0.result,
                            ReturnState.class);
                    if (Constant.RETURN_OK.equals(state.msg)) {
                        gv.setVisibility(View.VISIBLE);
                        LogManager.LogShow("-----", arg0.result,
                                LogManager.ERROR);
                        PersonInfoState infoState = gson.fromJson(arg0.result,
                                PersonInfoState.class);
                        info = infoState.result;
                        setValue();
                        // setEnable(false);
                    } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                        ToastUtils.displayShortToast(PersonDataActivity.this,
                                "验证错误，请重新登录");
                        ToosUtils.goReLogin(PersonDataActivity.this);
                    } else {
                        ToastUtils.displayShortToast(PersonDataActivity.this,
                                (String) state.result);
                    }
                } catch (Exception e) {
                    ToastUtils.displaySendFailureToast(PersonDataActivity.this);
                }

            }
        });
    }

    public void setValue() {
        bitmapUtils.display(icon, info.icon);
        name.setText(info.nickname);
        if (Constant.SEX_MAN.equals(info.sex)) {
            sex.setText("男");
        } else {
            sex.setText("女");
        }
        birth.setText(info.birthday);
        sdate = info.birthday;
        trade.setText(info.industry);
        job.setText(info.job);
        if (!ToosUtils.isStringEmpty(info.signature)) {
            signature.setText(info.signature);
        }

//        if (!ToosUtils.isStringEmpty(info.hobby)) {
//            interest.setText(info.hobby);
//        }
//        if (!ToosUtils.isStringEmpty(info.companyName)) {
//            unit.setText(info.companyName);
//        }

    }

    private void setEnable(boolean flag) {
        if (flag) {
            save.setText("保存");
        } else {
            save.setText("编辑");
        }
        iconView.setClickable(flag);
        name.setEnabled(flag);
        sex.setEnabled(flag);
        birthView.setClickable(flag);
        tradeView.setClickable(flag);
        job.setEnabled(flag);
        signature.setEnabled(flag);
//        emotionView.setClickable(flag);
//        interest.setEnabled(flag);
//        unit.setEnabled(flag);
    }

    private boolean checkInput() {
        if (ToosUtils.isTextEmpty(name)) {
            ToastUtils.displayShortToast(this, "昵称不能为空");
            return false;
        }
        if (ToosUtils.isTextEmpty(job)) {
            ToastUtils.displayShortToast(this, "职位不能为空");
            return false;
        }
        return true;
    }

    //flag 1修改头像 2 修改名称 3修改生日 4修改性别 5修改行业 6修改职位 7修改签名
    private void sendUpdate(PersonInfo personInfo, File iconFile, final int flag, final String temp) {
        RequestParams rp = new RequestParams();
        final Gson gson = new Gson();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        if (personInfo != null) {
            rp.addBodyParameter("info", gson.toJson(personInfo));
        }
        if (iconFile != null) {
            rp.addBodyParameter("icon", iconFile);
        }

        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH
                        + "/v1/user/saveOrUpdateInfo", rp,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        pro.setVisibility(View.VISIBLE);
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        pro.setVisibility(View.GONE);
                        ToastUtils.displayFailureToast(PersonDataActivity.this);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        pro.setVisibility(View.GONE);
                        try {
                            // Gson gson = new Gson();
                            LogManager.LogShow("----", arg0.result,
                                    LogManager.ERROR);
                            ReturnState state = gson.fromJson(arg0.result,
                                    ReturnState.class);
                            if (Constant.RETURN_OK.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        PersonDataActivity.this, "修改成功");
                                PerfectDataState dataState = gson.fromJson(
                                        arg0.result, PerfectDataState.class);
                                ShareDataTool.SaveInfoDetail(
                                        PersonDataActivity.this,
                                        dataState.result.nickname,
                                        dataState.result.icon,
                                        dataState.result.location);
                                ShareDataTool.SaveFlag(PersonDataActivity.this,
                                        1);
                                if (flag == 1) {
                                    bitmapUtils.display(icon, dataState.result.icon);
                                } else if (flag == 2) {
                                    name.setText(temp);
                                } else if (flag == 3) {
                                    birth.setText(temp);
                                } else if (flag == 4) {
                                    sex.setText(temp);
                                } else if (flag == 5) {
                                    trade.setText(temp);
                                } else if (flag == 6) {
                                    job.setText(temp);
                                } else if (flag == 7) {
                                    signature.setText(temp);
                                }
                                EMChatManager.getInstance()
                                        .updateCurrentUserNick(
                                                dataState.result.nickname);
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        PersonDataActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(PersonDataActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        PersonDataActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(PersonDataActivity.this);
                        }

                    }
                });

    }


}
