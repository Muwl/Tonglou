package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.utils.DensityUtil;

/**
 * Created by Administrator on 2016/2/9.
 */
public class PublishHelpActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private EditText name;

    private EditText detail;

    private View tiemView;

    private TextView time;

    private int width;

    private RadioGroup group;

    private RadioButton lef;

    private RadioButton rig;

    private TextView ok;

    private View pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_help);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title_title);
        back = (ImageView) findViewById(R.id.title_back);
        name = (EditText) findViewById(R.id.publish_help_name);
        detail = (EditText) findViewById(R.id.publish_help_content);
        ok = (TextView) findViewById(R.id.publish_help_ok);
        tiemView=findViewById(R.id.publish_help_timeview);
        time= (TextView) findViewById(R.id.publish_help_time);
        pro = findViewById(R.id.publish_help_pro);
        group = (RadioGroup) findViewById(R.id.title_group);
        lef = (RadioButton) findViewById(R.id.title_rb_lef);
        rig = (RadioButton) findViewById(R.id.title_rb_rig);
        title.setVisibility(View.GONE);

        back.setOnClickListener(this);
        ok.setOnClickListener(this);
        lef.setText("发布帮助");
        rig.setText("发布自荐");
        width = DensityUtil.getScreenWidth(this);
        group.setVisibility(View.VISIBLE);
        group.check(R.id.title_rb_lef);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.publish_help_ok:

                break;
        }
    }
}
