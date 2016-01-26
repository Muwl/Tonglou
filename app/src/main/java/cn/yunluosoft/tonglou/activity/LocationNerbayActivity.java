package cn.yunluosoft.tonglou.activity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.LocationNearbayAdapter;

/**
 * Created by Mu on 2016/1/26.
 */
public class LocationNerbayActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private ListView listView;

    private LocationNearbayAdapter adapter;

    private  View pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_nearbay);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title_title);
        back= (ImageView) findViewById(R.id.title_back);
        listView= (ListView) findViewById(R.id.location_nearbay_list);
        pro=findViewById(R.id.location_nearbay_pro);
        adapter=new LocationNearbayAdapter(this);

        listView.setAdapter(adapter);
        title.setText("附近");
        back.setOnClickListener(this);

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
