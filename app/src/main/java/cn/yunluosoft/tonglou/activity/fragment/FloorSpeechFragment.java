package cn.yunluosoft.tonglou.activity.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yunluosoft.tonglou.R;

/**
 * Created by Mu on 2016/1/25.
 */
public class FloorSpeechFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ffloorspeech, container, false);
        return view;
    }
}
