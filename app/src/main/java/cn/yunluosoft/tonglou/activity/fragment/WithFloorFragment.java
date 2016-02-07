package cn.yunluosoft.tonglou.activity.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.HelpActivity;
import cn.yunluosoft.tonglou.activity.HiGroupActivity;
import cn.yunluosoft.tonglou.activity.PPActivity;
import cn.yunluosoft.tonglou.activity.UsedActivity;
import cn.yunluosoft.tonglou.adapter.WithFloorAdapter;

/**
 * Created by Mu on 2016/1/25.
 */
public class WithFloorFragment extends Fragment implements View.OnClickListener{

    private TextView title;

    private View serch;

    private TextView group;

    private TextView used;

    private TextView pp;

    private TextView help;

    private ListView listView;

    private View pro;

    private WithFloorAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fwithfloor, container, false);
        title= (TextView) view.findViewById(R.id.title_title);
        view.findViewById(R.id.title_back).setVisibility(View.GONE);
        serch=view.findViewById(R.id.fwithfloor_serch);
        group= (TextView) view.findViewById(R.id.fwithfloor_group);
        used= (TextView) view.findViewById(R.id.fwithfloor_used);
        pp= (TextView) view.findViewById(R.id.fwithfloor_pp);
        help= (TextView) view.findViewById(R.id.fwithfloor_help);
        listView= (ListView) view.findViewById(R.id.fwithfloor_list);
        pro=view.findViewById(R.id.fwithfloor_pro);

        title.setText("同楼");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        serch.setOnClickListener(this);
        group.setOnClickListener(this);
        used.setOnClickListener(this);
        pp.setOnClickListener(this);
        help.setOnClickListener(this);
        adapter=new WithFloorAdapter(getActivity());
        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fwithfloor_serch:

                break;

            case R.id.fwithfloor_group:
                Intent intent=new Intent(getActivity(), HiGroupActivity.class);
                startActivity(intent);
                break;

            case R.id.fwithfloor_used:
                Intent intent1=new Intent(getActivity(), UsedActivity.class);
                startActivity(intent1);

                break;

            case R.id.fwithfloor_pp:
                Intent intent2=new Intent(getActivity(), PPActivity.class);
                startActivity(intent2);

                break;

            case R.id.fwithfloor_help:
                Intent intent3=new Intent(getActivity(), HelpActivity.class);
                startActivity(intent3);

                break;

        }
    }
}
