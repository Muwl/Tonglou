package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;

/**
 * Created by Mu on 2016/1/26.
 */
public class LoactionAddActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private EditText name;

    private View areaView;

    private TextView area;

    private View provinceView;

    private TextView province;

    private EditText detailAddress;

    private TextView ok;

    private View pro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_add);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title_title);
        back= (ImageView) findViewById(R.id.title_back);
        name= (EditText) findViewById(R.id.location_add_name);
        areaView=findViewById(R.id.location_add_areaview);
        area= (TextView) findViewById(R.id.location_add_area);
        provinceView=findViewById(R.id.location_add_provinceview);
        province= (TextView) findViewById(R.id.location_add_province);
        detailAddress= (EditText) findViewById(R.id.location_add_detailaddress);
        ok= (TextView) findViewById(R.id.location_add_ok);
        pro=findViewById(R.id.location_add_pro);

        title.setText("楼宇地址收录");
        back.setOnClickListener(this);
        areaView.setOnClickListener(this);
        provinceView.setOnClickListener(this);
        ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.location_add_ok:

                Intent intent=new Intent(LoactionAddActivity.this,LocationAddSucActivity.class);
                startActivity(intent);
                break;

            case R.id.location_add_areaview:
                break;

            case R.id.location_add_provinceview:
                break;


        }
    }
}
