package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.HiGroupAdapter;

/**
 * Created by Administrator on 2016/2/6.
 */
public class HiGroupActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private TextView rig;

    private View serch;

    private ListView listView;

    private View pro;

    private HiGroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.higroup);
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        rig= (TextView) findViewById(R.id.title_rig);
        serch=findViewById(R.id.higroup_serch);
        listView= (ListView) findViewById(R.id.higroup_list);
        pro=findViewById(R.id.higroup_pro);

        back.setOnClickListener(this);
        title.setText("嗨团");
        rig= (TextView) findViewById(R.id.title_rig);
        serch.setOnClickListener(this);
        rig.setVisibility(View.VISIBLE);
        rig.setOnClickListener(this);
        adapter=new HiGroupAdapter(this);
        listView.setAdapter(adapter);
        rig.setText("发布");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(HiGroupActivity.this,GroupDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.title_rig:

                break;
            case  R.id.higroup_serch:
                break;



        }
    }
}
