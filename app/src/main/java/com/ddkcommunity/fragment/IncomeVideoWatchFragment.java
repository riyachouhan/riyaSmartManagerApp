package com.ddkcommunity.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeVideoWatchFragment extends Fragment{

    public IncomeVideoWatchFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_income_video_watch, container, false);
//        AppConfig.openOkDialog1(getActivity());
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Income Opportunities");
        MainActivity.enableBackViews(true);
    }

}
