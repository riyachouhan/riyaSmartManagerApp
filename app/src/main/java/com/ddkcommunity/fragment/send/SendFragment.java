package com.ddkcommunity.fragment.send;

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
import com.ddkcommunity.fragment.HomeFragment;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.RPResultListener;
import com.ddkcommunity.utilies.RuntimePermissionUtil;

public class SendFragment extends Fragment implements View.OnClickListener
{

    public SendFragment() {
        // Required empty public constructor
    }
    private View view;
    private LinearLayout lytShareViaQrCode, lytShareViaWalletAddress, lytShareViaEmailAddress;
    private Context mContext;
    private String clickAddressname="";
    TextView sendheaderview;
    public static String ddkscan="";
    private UserResponse userData;
    boolean hasCameraPermission = false;
    private static final String cameraPerm = Manifest.permission.CAMERA;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_send, container, false);
            mContext = getActivity();
            lytShareViaEmailAddress = view.findViewById(R.id.lytShareViaEmailAddress);
            lytShareViaWalletAddress = view.findViewById(R.id.lytShareViaWalletAddress);
            lytShareViaQrCode = view.findViewById(R.id.lytShareViaQrCode);
            sendheaderview=view.findViewById(R.id.sendheaderview);
            lytShareViaEmailAddress.setOnClickListener(this);
            lytShareViaQrCode.setOnClickListener(this);
            lytShareViaWalletAddress.setOnClickListener(this);
            userData = AppConfig.getUserData(mContext);
            String countrydata=userData.getUser().country.get(0).country;
            String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
            if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
            {
                if(HomeFragment.tabclickevent==1)
                {
                    clickAddressname="Send PHP ";

                }else if(HomeFragment.tabclickevent==2)
                {
                    clickAddressname="Send SAM Koin ";

                }else if(HomeFragment.tabclickevent==6)
                {
                    clickAddressname="Send DDK ";

                }else if(HomeFragment.tabclickevent==3)
                {
                    clickAddressname="Send BTC ";

                }/*else if(HomeFragment.tabclickevent==4)
                {
                    clickAddressname="Send TRON ";

                }*/else if(HomeFragment.tabclickevent==4)
                {
                    clickAddressname="Send ETH ";

                }else if(HomeFragment.tabclickevent==5)
                {
                    clickAddressname="Send USDT ";
                }

            }else
            {
                if(HomeFragment.tabclickevent==1)
                {
                    clickAddressname="Send SAM Koin ";

                }else if(HomeFragment.tabclickevent==5)
                {
                    clickAddressname="Send DDK ";

                }else if(HomeFragment.tabclickevent==2)
                {
                    clickAddressname="Send BTC ";

                }/*else if(HomeFragment.tabclickevent==3)
                {
                    clickAddressname="Send TRON ";

                }*/else if(HomeFragment.tabclickevent==3)
                {
                    clickAddressname="Send ETH ";

                }else if(HomeFragment.tabclickevent==4)
                {
                    clickAddressname="Send USDT ";
                }

            }
            sendheaderview.setText(clickAddressname+getResources().getString(R.string.sendcoinother));
        }
        return view;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.lytShareViaQrCode)
        {
            if (hasCameraPermission)
            {
                // Setup QREader
                MainActivity.scanFragement=0;
                MainActivity.addFragment(new QrScanFragment(), true);
            } else {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        100);
            }

        }
        if (v.getId() == R.id.lytShareViaEmailAddress) {
            MainActivity.addFragment(new EmailFragment(), true);
        }
        if (v.getId() == R.id.lytShareViaWalletAddress)
        {
            Fragment fragment = new SendDDkFragment();
            Bundle arg = new Bundle();
            arg.putString("address", "");
            fragment.setArguments(arg);
            MainActivity.addFragment(fragment,true);
        }
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
                        MainActivity.scanFragement=0;
                        MainActivity.addFragment(new QrScanFragment(), true);
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
    public void onResume() {
        super.onResume();
        MainActivity.setTitle(clickAddressname);
        MainActivity.enableBackViews(true);
    }
}
