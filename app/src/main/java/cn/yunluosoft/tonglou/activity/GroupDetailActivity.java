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
public class GroupDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;

    private TextView title;

    private ImageView share;

    private CircleImageView icon;

    private TextView name;

    private TextView address;

    private TextView num;

    private TextView time;

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
        setContentView(R.layout.groupdetail);
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        share= (ImageView) findViewById(R.id.title_share);
        icon= (CircleImageView) findViewById(R.id.groupdetail_icon);
        name= (TextView) findViewById(R.id.groupdetail_name);
        address= (TextView) findViewById(R.id.groupdetail_address);
        num= (TextView) findViewById(R.id.groupdetail_num);
        time= (TextView) findViewById(R.id.groupdetail_time);
        content= (TextView) findViewById(R.id.groupdetail_content);
        join= (TextView) findViewById(R.id.groupdetail_join);
        atten= (TextView) findViewById(R.id.groupdetail_atten);
        replay= (ImageView) findViewById(R.id.groupdetail_replay);
        comment= (TextView) findViewById(R.id.groupdetail_comment);
        listView= (ListView) findViewById(R.id.groupdetail_list);
        edit= (EditText) findViewById(R.id.groupdetail_edit);
        send= (TextView) findViewById(R.id.groupdetail_send);
        pro=findViewById(R.id.groupdetail_pro);

        adapter=new GroupDetailAdapter(this);
        listView.setAdapter(adapter);
        share.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        title.setText("活动详情");
        share.setOnClickListener(this);
        replay.setOnClickListener(this);
        join.setOnClickListener(this);
        atten.setOnClickListener(this);
        comment.setOnClickListener(this);
        listView.setAdapter(adapter);
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

            case R.id.groupdetail_join:

                break;

            case R.id.groupdetail_atten:

                break;

            case R.id.groupdetail_comment:

                break;

            case R.id.groupdetail_replay:

                break;

            case R.id.groupdetail_send:

                break;

        }
    }
}
