package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.GroupDetailAdapter;
import cn.yunluosoft.tonglou.view.CircleImageView;

/**
 * Created by Administrator on 2016/2/7.
 */
public class HelpDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;

    private TextView title;

    private ImageView share;

    private CircleImageView icon;

    private TextView name;

    private TextView address;

    private TextView content;

    private TextView join;

    private TextView atten;

    private TextView comment;

    private ImageView replay;

    private ListView listView;

    private View pro;

    private GroupDetailAdapter adapter;

    private EditText edit;

    private TextView send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpdetail);
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        share= (ImageView) findViewById(R.id.title_share);
        icon= (CircleImageView) findViewById(R.id.helpdetail_icon);
        name= (TextView) findViewById(R.id.helpdetail_name);
        address= (TextView) findViewById(R.id.helpdetail_address);
        content= (TextView) findViewById(R.id.helpdetail_content);
        join= (TextView) findViewById(R.id.helpdetail_join);
        atten= (TextView) findViewById(R.id.helpdetail_atten);
        replay= (ImageView) findViewById(R.id.helpdetail_replay);
        comment= (TextView) findViewById(R.id.helpdetail_comment);
        listView= (ListView) findViewById(R.id.helpdetail_list);
        edit= (EditText) findViewById(R.id.helpdetail_edit);
        send= (TextView) findViewById(R.id.helpdetail_send);
        pro=findViewById(R.id.helpdetail_pro);

        adapter=new GroupDetailAdapter(this,null,null,null);
        listView.setAdapter(adapter);
        share.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        title.setText("帮帮详情");
        share.setOnClickListener(this);
        replay.setOnClickListener(this);
        join.setOnClickListener(this);
        atten.setOnClickListener(this);
        comment.setOnClickListener(this);
        send.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.title_share:

                break;

            case R.id.helpdetail_join:

                break;

            case R.id.helpdetail_atten:

                break;

            case R.id.helpdetail_comment:

                break;

            case R.id.helpdetail_replay:

                break;

            case R.id.helpdetail_send:

                break;

        }
    }
}
