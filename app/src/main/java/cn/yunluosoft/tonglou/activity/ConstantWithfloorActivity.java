package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.ConstantWithfloorAdapter;

/**
 * Created by Mu on 2016/1/28.
 */
public class ConstantWithfloorActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private View pro;

    private ListView listView;

    private ConstantWithfloorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constant_withfloor);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title_title);
        back= (ImageView) findViewById(R.id.title_back);
        listView= (ListView) findViewById(R.id.constant_withfloor_list);
        pro=findViewById(R.id.constant_withfloor_pro);

        title.setText("同楼好友");
        back.setOnClickListener(this);
        adapter=new ConstantWithfloorAdapter(this);
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
