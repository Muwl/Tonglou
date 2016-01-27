package cn.yunluosoft.tonglou.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.MyfloorspeekAdapter;
import cn.yunluosoft.tonglou.utils.DensityUtil;
import cn.yunluosoft.tonglou.view.swipelist.SwipeMenu;
import cn.yunluosoft.tonglou.view.swipelist.SwipeMenuCreator;
import cn.yunluosoft.tonglou.view.swipelist.SwipeMenuItem;
import cn.yunluosoft.tonglou.view.swipelist.SwipeMenuListView;

/**
 * Created by Mu on 2016/1/27.
 */
public class MyfloorspeekActivity extends BaseActivity implements View.OnClickListener{

    private TextView title;

    private ImageView back;

    private RadioGroup group;

    private RadioButton atten;

    private RadioButton publish;

    private SwipeMenuListView listView;

    private View pro;

    private MyfloorspeekAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfloorspeek);
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title_back);
        title= (TextView) findViewById(R.id.title_title);
        group= (RadioGroup) findViewById(R.id.myfloorspeech_group);
        atten= (RadioButton) findViewById(R.id.myfloorspeech_atten);
        publish= (RadioButton) findViewById(R.id.myfloorspeech_pub);
        listView= (SwipeMenuListView) findViewById(R.id.myfloorspeech_list);
        pro=findViewById(R.id.myfloorspeech_pro);
        title.setText("我的楼语");
        back.setOnClickListener(this);
        group.check(R.id.myfloorspeech_atten);

        adapter=new MyfloorspeekAdapter(this);
        listView.setAdapter(adapter);

         SwipeMenuCreator creator = new SwipeMenuCreator() {

         @Override
         public void create(SwipeMenu menu) {
         // create "open" item
         SwipeMenuItem openItem = new SwipeMenuItem(MyfloorspeekActivity.this);
         // set item background
         openItem.setBackground(new ColorDrawable(Color.rgb(0xff, 0x00,
         0x00)));
         // e8e9
         // 00c91c
         // set item width
         openItem.setWidth(DensityUtil.dip2px(MyfloorspeekActivity.this, 80));
         // set item title
         openItem.setTitle("删除");
         // set item title fontsize

         openItem.setTitleSize(16);
         // set item title font color
         openItem.setTitleColor(Color.rgb(0xFF, 0xFF, 0xFF));
         // new
         menu.addMenuItem(openItem);

         }
         };
         // set creator
         listView.setMenuCreator(creator);

//        // step 2. listener item click event
//        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
//                switch (index) {
//                    case 0:
//                        delete(position);
//                        break;
//                }
//            }
//        });

         listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

         @Override
         public void onSwipeStart(int position) {
         // swipe start
         }

         @Override
         public void onSwipeEnd(int position) {
         // swipe end
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
