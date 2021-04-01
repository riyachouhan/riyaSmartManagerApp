package com.ddkcommunity.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ddkcommunity.R;
import com.ddkcommunity.fragment.projects.PoolingFragment;
import com.ddkcommunity.fragment.projects.PoolingTransactionFragment;
import com.ddkcommunity.fragment.projects.PoolingWithdrawalFragment;
import com.ddkcommunity.utilies.AppConfig;
import com.google.android.material.tabs.TabLayout;

public class PoolingActivity extends AppCompatActivity implements View.OnClickListener {
    private static Context mContext;
    private static FragmentManager fragmentManager;

    public static void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    private TabLayout tabLayout;

    private int[] tabIcons = {
            R.drawable.ic_projects_tab,
            R.drawable.ic_transaction_tab,
            R.drawable.ic_cancellation_tab
    };

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pooling);
        mContext = this;
        fragmentManager = getSupportFragmentManager();
        AppConfig.hideKeyboard(PoolingActivity.this);

        tabLayout = findViewById(R.id.tabs);


        tabLayout.addTab(tabLayout.newTab().setText("Projects"));
        tabLayout.addTab(tabLayout.newTab().setText("Transaction"));
        tabLayout.addTab(tabLayout.newTab().setText("Cancellation"));

         setupTabIcons();

        addFragment(new PoolingFragment(), false, "PoolingFragment");
        findViewById(R.id.back).setOnClickListener(this);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    addFragment(new PoolingFragment(), false, "PoolingFragment");
                } else if (tab.getPosition() == 1) {
                    addFragment(new PoolingTransactionFragment(), false, "PoolingTransactionFragment");
                } else if (tab.getPosition() == 2) {
                    addFragment(new PoolingWithdrawalFragment(), false, "PoolingWithdrawalFragment");
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            finish();
        }
    }
}

