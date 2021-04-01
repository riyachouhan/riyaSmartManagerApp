package com.ddkcommunity.fragment.wallet;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ddkcommunity.App;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.CreateWalletActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSafeKeeping extends Fragment {

    public static final String TAG = FragmentSafeKeeping.class.getSimpleName();
    public static RelativeLayout rel1;
    private static FragmentSafeKeeping homeFragment;
    private static FragmentManager fm1;
    private static String pass;
    Context context;


    public FragmentSafeKeeping() {
        // Required empty public constructor
    }

    public static Fragment getInstance(FragmentManager fm, String passPhrase) {
        homeFragment = new FragmentSafeKeeping();
        fm1 = fm;
        pass = passPhrase;
        return homeFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_safekeeping, container, false);


        view.findViewById(R.id.understand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CreateWalletActivity.addFragment(new FragmentSafeKeeping1(), false);
                App.editor.putString("PassPhrase", "");
                App.editor.commit();
                CreateWalletActivity.addFragment(FragmentSafeKeeping1.getInstance(fm1, pass), true, "safekeep1");
            }
        });


        return view;
    }


}


