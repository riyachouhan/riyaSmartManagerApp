package com.ddkcommunity.fragment.wallet;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ddkcommunity.App;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.CreateWalletActivity;
import com.ddkcommunity.utilies.AppConfig;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSafeKeeping1 extends Fragment {

    public static final String TAG = FragmentSafeKeeping1.class.getSimpleName();
    public static RelativeLayout rel1;
    public static FragmentSafeKeeping1 homeFragment;
    private static FragmentManager fm1;
    private static String pass, passphrase;
    CardView send, receive, history, buyload;
    AppCompatButton yessafe, copy_passphrase;
    Context context;
    String ptext1;
    ShowHidePasswordEditText customFontTV;
    ClipboardManager clipboard;
    ClipData clip;

    public FragmentSafeKeeping1() {
        // Required empty public constructor
    }

    public static Fragment getInstance(FragmentManager fm, String passphrase1) {
        homeFragment = new FragmentSafeKeeping1();
        fm1 = fm;
        pass = passphrase1;

        return homeFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_safekeeping1, container, false);

        customFontTV = view.findViewById(R.id.customFontAndHideShow);
        customFontTV.setText(pass);
        customFontTV.setHorizontallyScrolling(false);
        customFontTV.setMaxLines(Integer.MAX_VALUE);


        copy_passphrase = view.findViewById(R.id.copy_passphrase);
        yessafe = view.findViewById(R.id.yessafe);

        copy_passphrase.setEnabled(true);
        yessafe.setEnabled(true);

        view.findViewById(R.id.copy_passphrase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ptext1 = customFontTV.getText().toString();


                if (!AppConfig.isStringNullOrBlank(ptext1)) {

                    clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    clip = ClipData.newPlainText("Copy", ptext1);
                    clipboard.setPrimaryClip(clip);

                    AppConfig.showToast("Passphrase copied.");

                } else {
                    AppConfig.showToast("Passphrase not copy.");
                }
            }
        });

        view.findViewById(R.id.yessafe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passphrase = customFontTV.getText().toString();
                if (!AppConfig.isStringNullOrBlank(passphrase)) {
                    App.editor.putString("PassPhrase", "");
                    App.editor.commit();
                    CreateWalletActivity.addFragment(new FragmentSafeKeeping2(), false, "safekeep2");
                }

            }
        });

        return view;
    }
}
