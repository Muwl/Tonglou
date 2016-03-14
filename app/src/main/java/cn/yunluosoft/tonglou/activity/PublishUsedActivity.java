package cn.yunluosoft.tonglou.activity;

import android.annotation.TargetApi;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.PublishUsedAdapter;
import cn.yunluosoft.tonglou.dialog.CustomeDialog;
import cn.yunluosoft.tonglou.dialog.PhotoDialog;
import cn.yunluosoft.tonglou.dialog.SubmitDialog;
import cn.yunluosoft.tonglou.model.ReturnState;
import cn.yunluosoft.tonglou.utils.Constant;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToastUtils;
import cn.yunluosoft.tonglou.utils.ToosUtils;
import cn.yunluosoft.tonglou.view.MyGridView;

/**
 * Created by Administrator on 2016/2/9.
 */
public class PublishUsedActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private EditText name;

    private EditText detail;

    private MyGridView gridView;

    private PublishUsedAdapter adapter;

    private int width;

    private RadioGroup group;

    private RadioButton lef;

    private RadioButton rig;

    private TextView ok;

    private View pro;

    private List<File> files;

    public final int PHOTO_PICKED_WITH_DATA = 3021;

    public final int CAMERA_WITH_DATA = 3023;

    public final int CONTENT_WITH_DATA = 3024;

    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";

    private File tempFile;

    private File file = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 40:
                    int position = msg.arg1;
                    files.remove(position);
                    // files.remove(position);
                    adapter.notifyDataSetChanged();
                    // delphoto(entity);
                    break;

                case 552:
                    Intent intent3=new Intent(PublishUsedActivity.this,UsedActivity.class);
                    if (group.getCheckedRadioButtonId()==R.id.title_rb_lef){
                        intent3.putExtra("flag",0);
                    }else{
                        intent3.putExtra("flag",1);
                    }
                    startActivity(intent3);
                    finish();
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

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_used);
        initView();
    }

    private void initView() {
        files = new ArrayList<>();
        title = (TextView) findViewById(R.id.title_title);
        back = (ImageView) findViewById(R.id.title_back);
        name = (EditText) findViewById(R.id.publish_used_name);
        detail = (EditText) findViewById(R.id.publish_used_content);
        gridView = (MyGridView) findViewById(R.id.publish_used_grid);
        ok = (TextView) findViewById(R.id.publish_used_ok);
        pro = findViewById(R.id.publish_used_pro);
        group = (RadioGroup) findViewById(R.id.title_group);
        lef = (RadioButton) findViewById(R.id.title_rb_lef);
        rig = (RadioButton) findViewById(R.id.title_rb_rig);
        title.setVisibility(View.GONE);

        back.setOnClickListener(this);
        ok.setOnClickListener(this);
        lef.setText("转让发布");
        rig.setText("求购发布");
        width = DensityUtil.getScreenWidth(this);
        adapter = new PublishUsedAdapter(this, width, files);
        gridView.setAdapter(adapter);
        group.setVisibility(View.VISIBLE);
        group.check(R.id.title_rb_lef);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position < files.size()) {
                    Intent intent = new Intent(PublishUsedActivity.this,
                            PhotoShowActivity.class);
                    intent.putExtra("position", position);
                    Bundle bundle = new Bundle();
                    List<String> entities = new ArrayList<String>();
                    for (int i = 0; i < files.size(); i++) {
                        entities.add(files.get(i).getAbsolutePath());
                    }

                    intent.putExtra("photo",
                            (Serializable) entities);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    PhotoDialog dialog = new PhotoDialog(PublishUsedActivity.this,
                            handler);
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                if (position < files.size()) {
                    CustomeDialog dialog = new CustomeDialog(PublishUsedActivity.this,
                            handler, "确定要删除该照片？", position, -1);
                }
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.publish_used_ok:
                if (checkInput()) {
                    sendPublish();
                }

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
                        int degree = readPictureDegree(getRealPathFromURI(data
                                .getData()));
                        BitmapFactory.Options opts = new BitmapFactory.Options();// 获取缩略图显示到屏幕上
                        opts.inSampleSize = 2;
                        Bitmap cbitmap = BitmapFactory.decodeFile(
                                getRealPathFromURI(data.getData()), opts);
                        Bitmap newbitmap = rotaingImageView(degree, cbitmap);
                        // 得到图片的全路径
                        File file = ToosUtils.compressBmpToFile(newbitmap);
                        files.add(file);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case CAMERA_WITH_DATA:
                    if (ToosUtils.hasSdcard()) {
                        tempFile = new File(Environment.getExternalStorageDirectory(),
                                PHOTO_FILE_NAME);
                        int degree = readPictureDegree(tempFile.getAbsolutePath());
                        BitmapFactory.Options opts = new BitmapFactory.Options();// 获取缩略图显示到屏幕上
                        opts.inSampleSize = 2;
                        Bitmap cbitmap = BitmapFactory.decodeFile(
                                tempFile.getAbsolutePath(), opts);
                        Bitmap newbitmap = rotaingImageView(degree, cbitmap);
                        File file = ToosUtils.compressBmpToFile(newbitmap);
                        files.add(file);
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.displayShortToast(PublishUsedActivity.this,
                                "未找到存储卡，无法存储照片！");
                    }
                    break;

            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     *
     * @param angle
     *
     * @param bitmap
     *
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 通过Uri获取地址
     *
     * @param contentUri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(this, contentUri,
                    proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
                return result;
            }
        } catch (Exception o) {

        }
        return null;

    }

    private boolean checkInput() {
        if (ToosUtils.isTextEmpty(name)) {
            ToastUtils.displayShortToast(PublishUsedActivity.this, "名称不能为空！");
            return false;
        }
        if (ToosUtils.isTextEmpty(detail)) {
            ToastUtils.displayShortToast(PublishUsedActivity.this, "概述不能为空！");
            return false;
        }
        return true;
    }

    private void sendPublish() {
        final Gson gson = new Gson();
        RequestParams rp = new RequestParams();
        rp.addBodyParameter("sign", ShareDataTool.getToken(this));
        rp.addBodyParameter("topic", ToosUtils.getTextContent(name));
        rp.addBodyParameter("detail", ToosUtils.getTextContent(detail));
        if (group.getCheckedRadioButtonId() == R.id.title_rb_lef) {
            rp.addBodyParameter("supplyType", "0");
        } else {
            rp.addBodyParameter("supplyType", "1");
        }
        if (files != null && files.size() != 0) {
            for (int i = 0; i < files.size(); i++) {
                rp.addBodyParameter(i + "", files.get(i));
            }
        }
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(20000);
        utils.send(HttpRequest.HttpMethod.POST, Constant.ROOT_PATH + "/v1_1_0/dynamic/secondhandSave",
                rp, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        pro.setVisibility(View.VISIBLE);
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        pro.setVisibility(View.GONE);
                        ToastUtils.displayFailureToast(PublishUsedActivity.this);
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
                                ;
                                SubmitDialog dialog = new SubmitDialog(PublishUsedActivity.this, 4, handler);
                            } else if (Constant.TOKEN_ERR.equals(state.msg)) {
                                ToastUtils.displayShortToast(
                                        PublishUsedActivity.this, "验证错误，请重新登录");
                                ToosUtils.goReLogin(PublishUsedActivity.this);
                            } else {
                                ToastUtils.displayShortToast(
                                        PublishUsedActivity.this,
                                        String.valueOf(state.result));
                            }
                        } catch (Exception e) {
                            ToastUtils
                                    .displaySendFailureToast(PublishUsedActivity.this);
                        }

                    }
                });

    }
}
