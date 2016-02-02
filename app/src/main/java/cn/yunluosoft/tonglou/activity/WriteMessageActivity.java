package cn.yunluosoft.tonglou.activity;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;

/**
 * Created by Mu on 2016/2/2.
 */
public class WriteMessageActivity  extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageView back;

    private EditText content;

    private ImageView face;

    private View ok;

    private View pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_message);
        initView();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title_title);
        back= (ImageView) findViewById(R.id.title_back);
        content= (EditText) findViewById(R.id.write_message_content);
        face= (ImageView) findViewById(R.id.write_message_face);
        ok=findViewById(R.id.write_message_ok);
        pro=findViewById(R.id.write_message_pro);

        title.setText("写留言");
        back.setOnClickListener(this);
        ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;

            case R.id.write_message_ok:
                break;
        }

    }
}
