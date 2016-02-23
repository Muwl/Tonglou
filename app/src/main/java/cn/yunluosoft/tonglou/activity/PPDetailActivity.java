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
public class PPDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;

    private TextView title;

    private ImageView share;

    private CircleImageView icon;

    private TextView name;

    private TextView address;

    private TextView content;

    private TextView join;

    private ImageView replay;

    private ListView listView;

    private View pro;

    private GroupDetailAdapter adapter;

    private EditText edit;

    private TextView send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ppdetail);
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        share= (ImageView) findViewById(R.id.title_share);
        icon= (CircleImageView) findViewById(R.id.ppdetail_icon);
        name= (TextView) findViewById(R.id.ppdetail_name);
        address= (TextView) findViewById(R.id.ppdetail_address);
        content= (TextView) findViewById(R.id.ppdetail_content);
        join= (TextView) findViewById(R.id.ppdetail_join);
        replay= (ImageView) findViewById(R.id.ppdetail_replay);
        listView= (ListView) findViewById(R.id.ppdetail_list);
        edit= (EditText) findViewById(R.id.ppdetail_edit);
        send= (TextView) findViewById(R.id.ppdetail_send);
        pro=findViewById(R.id.ppdetail_pro);

        adapter=new GroupDetailAdapter(this,null,null);
        listView.setAdapter(adapter);
        share.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        title.setText("拼车详情");
        share.setOnClickListener(this);
        replay.setOnClickListener(this);
        join.setOnClickListener(this);
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

            case R.id.ppdetail_join:

                break;

            case R.id.ppdetail_replay:

                break;

            case R.id.ppdetail_send:

                break;

        }
    }
}
