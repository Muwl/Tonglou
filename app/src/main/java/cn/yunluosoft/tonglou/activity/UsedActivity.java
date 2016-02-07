package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.UsedAdapter;

/**
 * Created by Administrator on 2016/2/7.
 */
public class UsedActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;

    private TextView rig;

    private RadioGroup group;

    private RadioButton assign;

    private RadioButton buy;

    private View serch;

    private ListView listView;

    private View pro;

    private UsedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.used);
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title_back);
        rig= (TextView) findViewById(R.id.title_rig);
        group= (RadioGroup) findViewById(R.id.title_group);
        assign= (RadioButton) findViewById(R.id.title_rb_lef);
        buy= (RadioButton) findViewById(R.id.title_rb_rig);
        serch=findViewById(R.id.used_serch);
        listView= (ListView) findViewById(R.id.used_list);
        pro=findViewById(R.id.used_pro);
        adapter=new UsedAdapter(this);

        back.setOnClickListener(this);
        findViewById(R.id.title_title).setVisibility(View.GONE);
        rig.setVisibility(View.VISIBLE);
        rig.setOnClickListener(this);
        rig.setText("发布");
        group.setVisibility(View.VISIBLE);
        assign.setText("转让");
        buy.setText("求购");
        serch.setOnClickListener(this);
        adapter=new UsedAdapter(this);
        listView.setAdapter(adapter);

        group.check(R.id.title_rb_lef);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.title_rig:

                break;
            case R.id.used_serch:

                break;

        }

    }
}
