package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
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
                Intent intent = new Intent(PublishActivity.this, PublishGroupActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.publish_used:
                Intent intent2 = new Intent(PublishActivity.this, PublishUsedActivity.class);
                startActivity(intent2);
                finish();
                break;

            case R.id.publish_pp:
                Intent intent3 = new Intent(PublishActivity.this, PublishPPActivity.class);
                startActivity(intent3);
                finish();
                break;

            case R.id.publish_help:
                Intent intent4 = new Intent(PublishActivity.this, PublishHelpActivity.class);
                startActivity(intent4);
                finish();
                break;
        }
    }
}
