package cn.yunluosoft.tonglou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.adapter.FriendsAdaper;
import cn.yunluosoft.tonglou.dialog.ShareAlertDialog;
import cn.yunluosoft.tonglou.model.FriendEntity;
import cn.yunluosoft.tonglou.model.MessageInfo;
import cn.yunluosoft.tonglou.utils.FriendDBUtils;
import cn.yunluosoft.tonglou.utils.LogManager;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToosUtils;

/**
 * Created by Administrator on 2016/3/12.
 */
public class ShareFriendActivity extends BaseActivity {

    private ImageView back;

    private TextView title;

    private ListView listView;

    private View pro;

    private FriendsAdaper adaper;

    private List<FriendEntity> entities;

    private FriendDBUtils friendDBUtils;

    private String stip;

    private String scontent;

    private String tempUrl;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 90:
                    int position=msg.arg1;
                    Intent intent = new Intent(ShareFriendActivity.this,
                            ChatActivity.class);
                    String myname = ShareDataTool.getNickname(ShareFriendActivity.this);
                    if (!ToosUtils.isStringEmpty(entities.get(position).byRemarkName)){
                        myname=entities.get(position).byRemarkName;
                    }
                    String rename = entities.get(position).userName;
                    if (!ToosUtils.isStringEmpty(entities.get(position).remarkName)) {
                        rename = entities.get(position).remarkName;
                    }

                    MessageInfo info = new MessageInfo(
                            ShareDataTool.getUserId(ShareFriendActivity.this), entities.get(position).userId,
                            ShareDataTool.getUserId(ShareFriendActivity.this), entities.get(position).userId,
                            ShareDataTool.getIcon(ShareFriendActivity.this),
                            entities.get(position).icon, myname, rename);
                    intent.putExtra("info", (Serializable) info);
                    LogManager.LogShow("-----", new Gson().toJson(info), LogManager.ERROR);
                    intent.putExtra("flag",-1000);
                    intent.putExtra("tempUrl",tempUrl);
                    startActivity(intent);

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_friend);
        initView();
    }

    private void initView() {
        stip=getIntent().getStringExtra("tip");
        scontent=getIntent().getStringExtra("content");
        tempUrl=getIntent().getStringExtra("tempUrl");
        back = (ImageView) findViewById(R.id.title_back);
        title = (TextView) findViewById(R.id.title_title);
        listView= (ListView) findViewById(R.id.share_friend_list);
        pro= findViewById(R.id.share_friend_pro);

        title.setText("分享到");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        friendDBUtils = new FriendDBUtils(this);
        entities = friendDBUtils.getAllFriends();
        entities = friendDBUtils.getAllFriends();
        if (entities == null) {
            entities = new ArrayList<FriendEntity>();
        }

        adaper=new FriendsAdaper(this,entities);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String name=entities.get(position).userName;
                if (!ToosUtils.isStringEmpty(entities.get(position).remarkName)) {
                   name=entities.get(position).remarkName;
                }
                ShareAlertDialog dialog=new ShareAlertDialog(ShareFriendActivity.this,handler,position,name,stip,scontent);

            }
        });
        listView.setAdapter(adaper);


    }
}
