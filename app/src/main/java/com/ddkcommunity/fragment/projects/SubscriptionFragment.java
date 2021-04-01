package com.ddkcommunity.fragment.projects;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriptionFragment extends Fragment implements View.OnClickListener {

    private Context mContext;

    public SubscriptionFragment() {

    }

    private LinearLayout lytHealth, btnSpendMoney, lytBusinessInsider;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);
        mContext = getActivity();
        btnSpendMoney = view.findViewById(R.id.btnSpendMoney);
        lytBusinessInsider = view.findViewById(R.id.lytBusinessInsider);
        lytHealth = view.findViewById(R.id.lytHealth);
        btnSpendMoney.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSpendMoney) {
            MainActivity.addFragment(new SelectPaymentPoolingFragment(), true);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        // HomeActivity.setHomeItem(getActivity(), R.id.profile);
        MainActivity.setTitle("Subscription");
        MainActivity.enableBackViews(true);
    }

}