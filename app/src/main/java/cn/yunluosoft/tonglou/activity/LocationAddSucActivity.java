package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;

/**
 * Created by Mu on 2016/1/26.
 */
public class LocationAddSucActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private TextView ok;

    private int flag;// 0代表注册 1代表修改

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_addsuc);
        flag = getIntent().getIntExtra("flag", 0);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title_title);
        back= (ImageView) findViewById(R.id.title_back);
        ok= (TextView) findViewById(R.id.location_addsuc_ok);

        title.setText("楼宇地址");
        back.setOnClickListener(this);
        ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;

            case R.id.location_addsuc_ok:
                Intent intent=new Intent(LocationAddSucActivity.this,LocationNerbayActivity.class);
                intent.putExtra("flag",flag);
                startActivity(intent);
                finish();
                break;
        }
    }
}
