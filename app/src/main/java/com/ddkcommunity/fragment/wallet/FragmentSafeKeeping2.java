package com.ddkcommunity.fragment.wallet;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ddkcommunity.App;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSafeKeeping2 extends Fragment {

    private static FragmentSafeKeeping2 homeFragment;
    private static FragmentManager fm1;
    Context context;

    public FragmentSafeKeeping2() {
    }

    public static Fragment getInstance(FragmentManager fm) {
        homeFragment = new FragmentSafeKeeping2();
        fm1 = fm;
        return homeFragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_safekeeping2, container, false);


        view.findViewById(R.id.continu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.editor.putString("PassPhrase", "");
                App.editor.commit();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
                //HomeActivity.viewPager.getAdapter().notifyDataSetChanged();
            }
        });

        return view;
    }


}
