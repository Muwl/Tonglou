package cn.yunluosoft.tonglou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.ConstactsAddAdapter;

/**
 * Created by Mu on 2016/2/1.
 */
public class ConstactsAddActivity extends BaseActivity implements View.OnClickListener{

    private TextView title;

    private ImageView back;

    private EditText editText;

    private ListView listView;

    private ConstactsAddAdapter addAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constacts_add);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title_title);
        back= (ImageView) findViewById(R.id.title_back);
        editText= (EditText) findViewById(R.id.constacts_add_edit);
        listView= (ListView) findViewById(R.id.constacts_add_list);

        title.setText("添加好友");
        back.setOnClickListener(this);
        addAdapter=new ConstactsAddAdapter(this);
        listView.setAdapter(addAdapter);
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
