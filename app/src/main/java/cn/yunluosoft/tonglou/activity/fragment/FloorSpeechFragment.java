package cn.yunluosoft.tonglou.activity.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import cn.yunluosoft.tonglou.R;
import cn.yunluosoft.tonglou.activity.ConsultActivity;
import cn.yunluosoft.tonglou.adapter.FloorSpeechAdapter;

/**
 * Created by Mu on 2016/1/25.
 */
public class FloorSpeechFragment extends Fragment {

    private TextView title;

    private ListView listView;

    private View pro;

    private FloorSpeechAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ffloorspeech, container, false);
        title= (TextView) view.findViewById(R.id.title_title);
        view.findViewById(R.id.title_back).setVisibility(View.GONE);
        listView= (ListView) view.findViewById(R.id.ffloorspeech_list);
        pro=view.findViewById(R.id.ffloorspeech_pro);
        title.setText("让大家听到你的声音");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter=new FloorSpeechAdapter(getActivity());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(), ConsultActivity.class);
                startActivity(intent);
            }
        });
    }
}
