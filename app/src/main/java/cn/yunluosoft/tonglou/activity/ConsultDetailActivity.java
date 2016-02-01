package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.ConsultDetailAdapter;

/**
 * Created by Mu on 2016/2/1.
 */
public class ConsultDetailActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;

    private TextView title;

    private ImageView share;

    private TextView tip;

    private TextView time;

    private TextView content;

    private TextView atten;

    private TextView read;

    private TextView report;

    private TextView message;

    private ListView listView;

    private ConsultDetailAdapter adapter;

    private View pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consult_detail);
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        share= (ImageView) findViewById(R.id.title_share);
        tip= (TextView) findViewById(R.id.consult_detail_tip);
        time= (TextView) findViewById(R.id.consult_detail_time);
        content= (TextView) findViewById(R.id.consult_detail_content);
        atten= (TextView) findViewById(R.id.consult_detail_atten);
        read= (TextView) findViewById(R.id.consult_detail_read);
        report= (TextView) findViewById(R.id.consult_detail_report);
        message= (TextView) findViewById(R.id.consult_detail_message);
        listView= (android.widget.ListView) findViewById(R.id.consult_detail_list);
        pro=findViewById(R.id.consult_detail_pro);

        back.setOnClickListener(this);
        title.setText("资讯详情");
        share.setVisibility(View.VISIBLE);
        share.setOnClickListener(this);
        atten.setOnClickListener(this);
        read.setOnClickListener(this);
        message.setOnClickListener(this);
        report.setOnClickListener(this);
        adapter=new ConsultDetailAdapter(this);
        listView.setAdapter(adapter);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.title_share:
                break;

            case R.id.consult_detail_atten:
                break;
            case R.id.consult_detail_read:
                break;
            case R.id.consult_detail_report:
                break;
            case R.id.consult_detail_message:
                break;
        }
    }
}
