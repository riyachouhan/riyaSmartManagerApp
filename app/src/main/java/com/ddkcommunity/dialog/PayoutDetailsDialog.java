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
import com.ddkcommunity.adapters.ReferralPayoutDetailsAdapter;
import com.ddkcommunity.model.ReferralPayout;

import java.util.List;

public class PayoutDetailsDialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public ImageView close;
    public RecyclerView recyclerView;
    public List<ReferralPayout.Wallet> walletPayout;
    private TextView tvPhone, tvName, tvEmail;

    public PayoutDetailsDialog(Activity a, List<ReferralPayout.Wallet> walletPayout) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.walletPayout = walletPayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_referral_payout);

        close = findViewById(R.id.close);
        recyclerView = findViewById(R.id.recycler_view);
        close.setOnClickListener(this);
/*
        ReferralPayoutDetailsAdapter mAdapter = new ReferralPayoutDetailsAdapter(walletPayout, c);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
*/
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