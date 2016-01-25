package cn.yunluosoft.tonglou.activity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;

/**
 * Created by Mu on 2016/1/25.
 */
public class UpdateNameActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private TextView com;

    private TextView textView;

    private EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_name);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title_title);
        back= (ImageView) findViewById(R.id.title_back);
        com= (TextView) findViewById(R.id.title_rig);
        textView= (TextView) findViewById(R.id.update_name_text);
        name= (EditText) findViewById(R.id.update_name_name);

        title.setText("昵称");
        back.setOnClickListener(this);
        com.setVisibility(View.VISIBLE);
        com.setOnClickListener(this);
        com.setText("完成");
        name.setHint("好名字可以让楼友更容易记住你");
        name.setText("请输入新的昵称");


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.title_rig:
                finish();
                break;
        }
    }
}
