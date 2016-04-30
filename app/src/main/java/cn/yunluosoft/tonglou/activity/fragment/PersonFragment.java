package cn.yunluosoft.tonglou.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.AboutActivity;
import cn.yunluosoft.tonglou.activity.FreebackActivity;
import cn.yunluosoft.tonglou.activity.LocationSelActivity;
import cn.yunluosoft.tonglou.activity.MyfloorspeekActivity;
import cn.yunluosoft.tonglou.activity.PersonDataActivity;
import cn.yunluosoft.tonglou.activity.SettingActivity;
import cn.yunluosoft.tonglou.model.LocationEntity;
import cn.yunluosoft.tonglou.utils.ShareDataTool;
import cn.yunluosoft.tonglou.utils.ToosUtils;

import com.lidroid.xutils.BitmapUtils;

/**
 * @author Mu
 * @date 2015-8-2下午4:16:36
 * @description
 */
public class PersonFragment extends Fragment implements OnClickListener {

    private ImageView back;

    private TextView title;

    private ImageView icon;

    private TextView name;

    private View dataView;

    private View black;

    private View locateView;

    private TextView locate;

    private View floorSpeechView;

    private View suggestView;

    private View settingView;

    private View aboutView;

    private BitmapUtils bitmapUtils;

    private static Context context;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fperson, container, false);
        back = (ImageView) view.findViewById(R.id.title_back);
        title = (TextView) view.findViewById(R.id.title_title);
        icon = (ImageView) view.findViewById(R.id.fperson_icon);
        name = (TextView) view.findViewById(R.id.fperson_name);
        dataView = view.findViewById(R.id.fperson_dataview);
        locateView = view.findViewById(R.id.fperson_locateview);
        locate = (TextView) view.findViewById(R.id.fperson_locate);
        floorSpeechView = view.findViewById(R.id.fperson_floorspeech);
        suggestView = view.findViewById(R.id.fperson_suggestview);
        settingView = view.findViewById(R.id.fperson_settingview);
        aboutView = view.findViewById(R.id.fperson_about);

        back.setVisibility(View.GONE);
        title.setText("我的");
        return view;
    }

    public static  PersonFragment getInstance(Context context) {
        PersonFragment fragment = new PersonFragment() ;
        PersonFragment.context = context;
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataView.setOnClickListener(this);
        locateView.setOnClickListener(this);
        floorSpeechView.setOnClickListener(this);
        suggestView.setOnClickListener(this);
        settingView.setOnClickListener(this);
        aboutView.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        name.setText(ShareDataTool.getNickname(context));
        locate.setText(ShareDataTool.getLocation(context));
        bitmapUtils = new BitmapUtils(context);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.icon_default);
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.icon_default);
        bitmapUtils.display(icon, ShareDataTool.getIcon(context));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fperson_dataview:
//                if (ToosUtils.CheckComInfo(context)) {
                    Intent intent5 = new Intent(context, PersonDataActivity.class);
                    startActivity(intent5);
//                }
                break;
            case R.id.fperson_locateview:
                Intent intent3 = new Intent(context,
                        LocationSelActivity.class);
                LocationEntity locationEntity=new LocationEntity();
                locationEntity.name=ShareDataTool.getLocation(context);
                locationEntity.id=ShareDataTool.getBuildingId(context);
                intent3.putExtra("entity",locationEntity);
                intent3.putExtra("flag", 1);
                startActivity(intent3);

                break;
            case R.id.fperson_floorspeech:
                Intent intent = new Intent(context,
                        MyfloorspeekActivity.class);
                startActivity(intent);
                break;
            case R.id.fperson_suggestview:
                if (ToosUtils.CheckComInfo(context)) {
                    Intent intent2 = new Intent(context, FreebackActivity.class);
                    startActivity(intent2);
                }
                break;
            case R.id.fperson_settingview:
                Intent intent6 = new Intent(context, SettingActivity.class);
                startActivity(intent6);

                break;
            case R.id.fperson_about:
                Intent intent4 = new Intent(context, AboutActivity.class);
                startActivity(intent4);

                break;

            default:
                break;
        }
    }
}
