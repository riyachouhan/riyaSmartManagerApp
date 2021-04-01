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
public class PayBillsFragment extends Fragment implements View.OnClickListener {

    private Context mContext;

    public PayBillsFragment() {

    }

    private LinearLayout lytCreditCard, lytUtilities, lytSubscription, btnPayBills;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_bills, container, false);

        mContext = getActivity();

        btnPayBills = view.findViewById(R.id.btnPayBills);
        lytCreditCard = view.findViewById(R.id.lytCreditCard);
        lytUtilities = view.findViewById(R.id.lytUtilities);
        lytSubscription = view.findViewById(R.id.lytSubscription);

        lytSubscription.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lytUtilities) {

        }
        if (v.getId() == R.id.lytSubscription) {
            MainActivity.addFragment(new SubscriptionFragment(), true);
        }
        if (v.getId() == R.id.lytCreditCard) {

        }
        if (v.getId() == R.id.btnPayBills) {

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        // HomeActivity.setHomeItem(getActivity(), R.id.profile);
        MainActivity.setTitle("Subscribe");
        MainActivity.enableBackViews(true);
    }
}