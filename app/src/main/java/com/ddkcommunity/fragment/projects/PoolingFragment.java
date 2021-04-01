package com.ddkcommunity.fragment.projects;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.activities.PoolingActivity;
import com.ddkcommunity.activities.TermsAndConditionActivity;
import com.ddkcommunity.activities.WebViewActivity;
import com.ddkcommunity.adapters.PoolingDocAdapter;
import com.ddkcommunity.model.projects.PoolingDoc;
import com.ddkcommunity.utilies.AppConfig;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoolingFragment extends Fragment implements View.OnClickListener {

    public PoolingFragment() {

    }

    private AppCompatButton btnInterested;

    private TextView btnTerms;
    private Context mContext;
    private CheckBox cbTermCondition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pooling, container, false);

        mContext = getActivity();

        cbTermCondition = view.findViewById(R.id.cbTermCondition);
        btnInterested = view.findViewById(R.id.btnInterested);
        btnTerms = view.findViewById(R.id.btnTerms);
        btnTerms.setPaintFlags(btnTerms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//
        btnTerms.setOnClickListener(this);
//
        btnInterested.setOnClickListener(this);
        final ArrayList<PoolingDoc> arrayList = new ArrayList<>();
        arrayList.add(new PoolingDoc("Consolidated Approaches Phase 1 We aim for $10 Million USD as PRE DEVELOPMENT CAPITAL RAISING", ContextCompat.getDrawable(mContext, R.drawable.banner6), "consolidated_approaches"));
        arrayList.add(new PoolingDoc("The Free Flight Vouchers UPDATE", ContextCompat.getDrawable(mContext, R.drawable.banner2), "the_free_flight_voucher_update"));
        arrayList.add(new PoolingDoc("Client will be FREE FLIGHT VOUCHERS AUSTRALIA PTT LTD., as authorized reseller of Free Flight Promotions for RIX Group AG of Switzerland.", ContextCompat.getDrawable(mContext, R.drawable.banner1), "free_flight_voucher"));
        arrayList.add(new PoolingDoc("Smart Asset Managers Buying Mechanisms", ContextCompat.getDrawable(mContext, R.drawable.banner4), "smart_asset_manager"));
        arrayList.add(new PoolingDoc("THE MARKETING I would like to share with you this no-secret simple wealth-creating technology.", ContextCompat.getDrawable(mContext, R.drawable.banner3), "the_marketing"));
//
        RecyclerView rvDoc = (RecyclerView) view.findViewById(R.id.rvDoc);
        PoolingDocAdapter mAdapter = new PoolingDocAdapter(arrayList, mContext, new PoolingDocAdapter.SetOnClickListener() {
            @Override
            public void setClick(int position) {
//                LoadPdfFile(arrayList.get(position).docName);
                startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra("fileName", arrayList.get(position).docName));
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvDoc.setLayoutManager(mLayoutManager);
        rvDoc.setItemAnimator(new DefaultItemAnimator());
        rvDoc.setAdapter(mAdapter);
        return view;
    }
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnInterested) {
            if (cbTermCondition.isChecked()) {
//                PoolingActivity.addFragment(new SelectPaymentPoolingFragment(), true, "PoolingFragment");
                MainActivity.addFragment(new SelectPaymentPoolingFragment(), true);
            } else {
                AppConfig.showToast("Please check the terms and conditions");
            }
        }

        if (v.getId() == R.id.btnTerms) {
            startActivity(new Intent(mContext, TermsAndConditionActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // HomeActivity.setHomeItem(getActivity(), R.id.profile);
        MainActivity.setTitle("Spend free money and earn free money");
        MainActivity.enableBackViews(true);
    }
}
