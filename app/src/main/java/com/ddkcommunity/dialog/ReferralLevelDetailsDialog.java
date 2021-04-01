package com.ddkcommunity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.adapters.ReferralWalletAddAdapter;
import com.ddkcommunity.model.ReferalSubWalletListModel;
import com.ddkcommunity.model.referral.ReferralLevelList;
import com.ddkcommunity.model.referralSublistModel;

import java.util.ArrayList;

public class ReferralLevelDetailsDialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public ImageView close;
    public RecyclerView recyclerView;
    public ArrayList<referralSublistModel.Datum> walletlisr;
    private TextView tvPhone, tvName, tvEmail;
    String name,email,mobile;

    public ReferralLevelDetailsDialog(Activity a, ArrayList<referralSublistModel.Datum> walletlisr,String name,String email,String mobile) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.walletlisr =walletlisr;
        this.name=name;
        this.email=email;
        this.mobile=mobile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_referral_level_details);
        tvEmail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);

        close = findViewById(R.id.close);
        recyclerView = findViewById(R.id.recycler_view);
        close.setOnClickListener(this);
        ReferralWalletAddAdapter mAdapter = new ReferralWalletAddAdapter(walletlisr, c);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        tvEmail.setText(email);
        tvName.setText(name);
        if (mobile != null) {
            tvPhone.setText(mobile);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}