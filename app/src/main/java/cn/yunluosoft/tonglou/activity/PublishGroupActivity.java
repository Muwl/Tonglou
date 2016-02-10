package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import cn.yunluosoft.tonglou.R;

/**
 * Created by Administrator on 2016/2/9.
 */
public class PublishGroupActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private EditText name;

    private EditText detail;

    private View timeView;

    private TextView time;

    private View numView;

    private TextView num;

    private ToggleButton toggleButton;

    private TextView ok;

    private View pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_group);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title_title);
        back = (ImageView) findViewById(R.id.title_back);
        name = (EditText) findViewById(R.id.publish_group_name);
        detail = (EditText) findViewById(R.id.publish_group_content);
        timeView = findViewById(R.id.publish_group_timeview);
        time = (TextView) findViewById(R.id.publish_group_time);
        numView = findViewById(R.id.publish_group_numview);
        num = (TextView) findViewById(R.id.publish_group_num);
        toggleButton = (ToggleButton) findViewById(R.id.publish_group_toggle);
        ok = (TextView) findViewById(R.id.publish_group_ok);
        pro = findViewById(R.id.publish_group_pro);

        title.setText("发布活动");
        back.setOnClickListener(this);
        timeView.setOnClickListener(this);
        numView.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.publish_group_timeview:

                break;

            case R.id.publish_group_numview:

                break;

            case R.id.publish_group_ok:

                break;
        }

    }
}
