package com.ddkcommunity.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ddkcommunity.R;
import com.ddkcommunity.fragment.ReferralChainFragment;
import com.ddkcommunity.fragment.ReferralPayoutFragment;
import com.ddkcommunity.utilies.AppConfig;

public class ReferralChainPayoutActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private TextView btnReferralChain, btnReferralPayout;

    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, tag);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_chain_payout);
        mContext = this;
        AppConfig.hideKeyboard(ReferralChainPayoutActivity.this);
        btnReferralPayout = findViewById(R.id.btnReferralPayout);
        btnReferralChain = findViewById(R.id.btnReferralChain);

        addFragment(new ReferralChainFragment(), false, "ReferralChainFragment");
        findViewById(R.id.back).setOnClickListener(this);
        btnReferralChain.setOnClickListener(this);
        btnReferralPayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnReferralChain) {
            addFragment(new ReferralChainFragment(), false, "ReferralChainFragment");
            btnReferralChain.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pooling_tab_bg));
            btnReferralPayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pooling_tab_bg_white));
            btnReferralChain.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            btnReferralPayout.setTextColor(ContextCompat.getColor(mContext, R.color.darkShadow));
        }
        if (v.getId() == R.id.btnReferralPayout)
        {
            btnReferralChain.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pooling_tab_bg_white));
            btnReferralPayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pooling_tab_bg));
            btnReferralChain.setTextColor(ContextCompat.getColor(mContext, R.color.darkShadow));
            btnReferralPayout.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            addFragment(new ReferralPayoutFragment(), false, "ReferralPayoutFragment");
       }
        if (v.getId() == R.id.back) {
            finish();
        }
    }
}
