package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.ConsultAdapter;

/**
 * Created by Mu on 2016/1/28.
 */
public class ConsultActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;

    private TextView title;

    private ListView listView;

    private View pro;

    private ConsultAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consult);
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        listView= (ListView) findViewById(R.id.consult_list);
        pro=findViewById(R.id.consult_pro);

        back.setOnClickListener(this);
        title.setText("咨询推送");
        adapter=new ConsultAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(ConsultActivity.this,ConsultDetailActivity.class);
                startActivity(intent);
            }
        });
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
