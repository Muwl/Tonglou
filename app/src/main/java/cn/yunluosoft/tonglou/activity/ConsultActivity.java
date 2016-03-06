package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.ConsultAdapter;
import cn.yunluosoft.tonglou.model.ConsultEntity;
import cn.yunluosoft.tonglou.utils.NewsDBUtils;

/**
 * Created by Mu on 2016/1/28.
 */
public class ConsultActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;

    private TextView title;

    private ListView listView;

    private View pro;

    private ConsultAdapter adapter;

   private List<ConsultEntity> entities;

    private NewsDBUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consult);
        initView();
    }

    private void initView() {
        entities=new ArrayList<>();
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        listView= (ListView) findViewById(R.id.consult_list);
        pro=findViewById(R.id.consult_pro);

        back.setOnClickListener(this);
        title.setText("咨询推送");
        dbUtils=new NewsDBUtils(this);
        entities=dbUtils.getAllConsultEntities();
        adapter=new ConsultAdapter(this,entities);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
        }
    }
}
