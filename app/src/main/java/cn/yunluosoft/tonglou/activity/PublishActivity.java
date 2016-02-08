package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import cn.yunluosoft.tonglou.R;

/**
 * Created by Administrator on 2016/2/8.
 */
public class PublishActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;

    private TextView title;

    private TextView group;

    private TextView used;

    private TextView pp;

    private TextView help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish);
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        group= (TextView) findViewById(R.id.publish_group);
        used= (TextView) findViewById(R.id.publish_used);
        pp= (TextView) findViewById(R.id.publish_pp);
        help= (TextView) findViewById(R.id.publish_help);

        back.setOnClickListener(this);
        title.setText("发布信息");
        group.setOnClickListener(this);
        used.setOnClickListener(this);
        pp.setOnClickListener(this);
        help.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;

            case R.id.publish_group:
                break;

            case R.id.publish_used:
                break;

            case R.id.publish_pp:
                break;

            case R.id.publish_help:
                break;
        }
    }
}
