package com.ddkcommunity.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.AnnouncementAdapter;
import com.ddkcommunity.adapters.IncomeAdapter;
import com.ddkcommunity.utilies.AppConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeOpportunitiesFragment extends Fragment implements View.OnClickListener {

    public IncomeOpportunitiesFragment() {
    }

    private View rootView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_income_opportunities, container, false);
        if (rootView == null) {
            rootView = view;

            RecyclerView recyclerView = view.findViewById(R.id.rvIncome);
            IncomeAdapter incomeAdapter = new IncomeAdapter(new IncomeAdapter.SetOnItemClick() {
                @Override
                public void onItemClick(int position) {
                    MainActivity.addFragment(new IncomeVideoWatchFragment(), true);
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(incomeAdapter);
            AppConfig.openOkDialog1(getActivity());
        }

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Income Opportunities");
        MainActivity.enableBackViews(true);
    }

    @Override
    public void onClick(View v) {
    }
}
