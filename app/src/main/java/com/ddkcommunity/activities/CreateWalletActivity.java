package com.ddkcommunity.activities;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ddkcommunity.R;
import com.ddkcommunity.fragment.wallet.FragmentCreatePassphrase;

public class CreateWalletActivity extends AppCompatActivity {
    private static FragmentManager fm;
    private Activity activity;

    public static void addFragment(Fragment fragment, boolean addToBackStack, String TAG) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment, TAG);
        //if (!tag.equals("Home"))
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void clearFragmentStack() {
        fm.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        fm = getSupportFragmentManager();
        activity = this;

        addFragment(new FragmentCreatePassphrase(), false, "create");
    }

    @Override
    public void onBackPressed() {

        if (fm != null) {
            Fragment create_frag = fm.findFragmentByTag("create");
            Fragment confrim_frag = fm.findFragmentByTag("confirm");

            Fragment safekeep_frag = fm.findFragmentByTag("safekeep");
            Fragment safekeep1_frag = fm.findFragmentByTag("safekeep1");
            Fragment safekeep2_frag = fm.findFragmentByTag("safekeep2");

            if (create_frag != null) {
                if (create_frag.isAdded()) {
                    if (create_frag.isVisible()) {
                        super.onBackPressed();

                    }
                }
            }

            if (confrim_frag != null) {
                if (confrim_frag.isAdded()) {
                    if (confrim_frag.isVisible()) {
                        super.onBackPressed();

                    }
                }
            }

            if (safekeep_frag != null) {
                if (safekeep_frag.isAdded()) {
                    if (safekeep_frag.isVisible()) {


                    }
                }
            }
            if (safekeep1_frag != null) {
                if (safekeep1_frag.isAdded()) {
                    if (safekeep1_frag.isVisible()) {


                    }
                }
            }
            if (safekeep2_frag != null) {
                if (safekeep2_frag.isAdded()) {
                    if (safekeep2_frag.isVisible()) {

                    }
                }
            }
        }

    }
}
