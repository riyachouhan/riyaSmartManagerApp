package com.ddkcommunity.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.projects.MapOtionAllFragment;
import com.ddkcommunity.fragment.projects.MapreferralFragment;
import com.ddkcommunity.model.checkRefferalModel;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ncorti.slidetoact.SlideToActView;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.fragment.ScanFragment.clickoption;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatrixFragment extends Fragment{

    private Context mContext;
    private View rootView = null;
    private SlideToActView slide_custom_icon;

    public MatrixFragment()
    {
        // Required empty public constructor
    }

    private UserResponse userData;
    String txtid,name,amount;
    String action;
    String emailaddress,currentWalletBalance;
    TextView upperheading,hintform,etWalletUserName,etDDKCoins,btnVerify;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        if (rootView == null) {

            View view = inflater.inflate(R.layout.matrixlayout, container, false);
            try {
                rootView = view;
                mContext = getActivity();
                if (getArguments().getString("htmlink") != null) {
                    action= getArguments().getString("htmlink");
                }

                WebView wv = (WebView) rootView.findViewById(R.id.webview);
                final String mimeType = "text/html";
                final String encoding = "UTF-8";
                String html = action;
                //wv.loadURL(htmlink);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        MainActivity.setTitle("Matrix");
        MainActivity.scanview.setVisibility(View.GONE);
    }

}
