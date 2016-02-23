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
public class UsedDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;

    private TextView title;

    private ImageView share;

    private CircleImageView icon;

    private TextView name;

    private TextView address;

    private TextView num;

    private TextView time;

    private TextView content;

    private TextView atten;

    private TextView comment;

    private ImageView replay;

    private ListView listView;

    private ImageView imageView;

    private View pro;

    private GroupDetailAdapter adapter;

    private EditText edit;

    private TextView send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.useddetail);
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        share= (ImageView) findViewById(R.id.title_share);
        icon= (CircleImageView) findViewById(R.id.useddetail_icon);
        name= (TextView) findViewById(R.id.useddetail_name);
        address= (TextView) findViewById(R.id.useddetail_address);
        imageView= (ImageView) findViewById(R.id.useddetail_image);
        content= (TextView) findViewById(R.id.useddetail_content);
        atten= (TextView) findViewById(R.id.useddetail_atten);
        replay= (ImageView) findViewById(R.id.useddetail_replay);
        comment= (TextView) findViewById(R.id.useddetail_comment);
        listView= (ListView) findViewById(R.id.useddetail_list);
        edit= (EditText) findViewById(R.id.useddetail_edit);
        send= (TextView) findViewById(R.id.useddetail_send);
        pro=findViewById(R.id.useddetail_pro);

        adapter=new GroupDetailAdapter(this,null,null,null);
        listView.setAdapter(adapter);
        share.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        title.setText("二手详情");
        share.setOnClickListener(this);
        replay.setOnClickListener(this);
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

            case R.id.useddetail_atten:

                break;

            case R.id.useddetail_comment:

                break;

            case R.id.useddetail_replay:

                break;

            case R.id.useddetail_send:

                break;

        }
    }
}
