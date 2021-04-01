package com.ddkcommunity.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.send.EmailFragment;
import com.ddkcommunity.fragment.send.QrScanFragment;
import com.ddkcommunity.fragment.send.QrScanFragmentScan;
import com.ddkcommunity.fragment.send.SendDDkFragment;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.RPResultListener;
import com.ddkcommunity.utilies.RuntimePermissionUtil;

public class ScanFragment extends Fragment implements View.OnClickListener
{

    public ScanFragment() {
        // Required empty public constructor
    }
    private View view;
    private Context mContext;
    private String clickAddressname="";
    public static String ddkscan="";
    private UserResponse userData;
    LinearLayout payusignSamKoin,payUsingPHP;
    public static String clickoption;
    boolean hasCameraPermission = false;
    private static final String cameraPerm = Manifest.permission.CAMERA;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_scan, container, false);
            mContext = getActivity();
            userData = AppConfig.getUserData(mContext);
            payusignSamKoin=view.findViewById(R.id.scanSamKoin);
            payUsingPHP=view.findViewById(R.id.payUsingPHP);
            payusignSamKoin.setOnClickListener(this);
            payUsingPHP.setOnClickListener(this);
            String countrydata=userData.getUser().country.get(0).country;
            String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
            if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
            {
              payUsingPHP.setVisibility(View.VISIBLE);
            }else
            {
                payUsingPHP.setVisibility(View.GONE);
            }
        }
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        if (requestCode == 100)
        {
            RuntimePermissionUtil.onRequestPermissionsResult(grantResults, new RPResultListener() {
                @Override
                public void onPermissionGranted()
                {
                    if (RuntimePermissionUtil.checkPermissonGranted(getActivity(), cameraPerm))
                    {
                        MainActivity.scanFragement=1;
                        clickoption="usingphp";
                        MainActivity.addFragment(new QrScanFragmentScan(), true);
                    }
                }

                @Override
                public void onPermissionDenied() {
                    // do nothing
                }
            });
        }
        if (requestCode == 101)
        {
            RuntimePermissionUtil.onRequestPermissionsResult(grantResults, new RPResultListener() {
                @Override
                public void onPermissionGranted()
                {
                    if (RuntimePermissionUtil.checkPermissonGranted(getActivity(), cameraPerm))
                    {
                        MainActivity.scanFragement=2;
                        clickoption="usingsamkoin";
                        MainActivity.addFragment(new QrScanFragmentScan(), true);
                    }
                }

                @Override
                public void onPermissionDenied() {
                    // do nothing
                }
            });
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.payUsingPHP)
        {
            if (hasCameraPermission)
            {
                // Setup QREader
                MainActivity.scanFragement=1;
                clickoption="usingphp";
                MainActivity.addFragment(new QrScanFragmentScan(), true);

            } else {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        100);
            }
        }
        if (v.getId() == R.id.scanSamKoin)
        {
            if (hasCameraPermission)
            {
                // Setup QREader
                MainActivity.scanFragement=2;
                clickoption="usingsamkoin";
                MainActivity.addFragment(new QrScanFragmentScan(), true);

            } else {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        101);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Scan QR");
        MainActivity.enableBackViews(true);
        MainActivity.scanview.setVisibility(View.GONE);
    }
}
