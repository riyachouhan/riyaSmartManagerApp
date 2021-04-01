package com.ddkcommunity.fragment.send;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.HomeFragment;
import com.ddkcommunity.model.scanQRModel;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.model.userInviteModel;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.RPResultListener;
import com.ddkcommunity.utilies.RuntimePermissionUtil;

import java.util.HashMap;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.fragment.CashOutFragmentNew.msgbank;
import static com.ddkcommunity.fragment.ScanFragment.clickoption;
import static com.ddkcommunity.fragment.send.SendFragment.ddkscan;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.ShowSameWalletDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class QrScanFragment extends Fragment {
    private static final String cameraPerm = Manifest.permission.CAMERA;
    boolean hasCameraPermission = false;
    private SurfaceView mySurfaceView;
    private QREader qrEader;
    private String clickAddressname="";
    ProgressBar progressBar;
    View view;

    public QrScanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_qr_scan, container, false);
        hasCameraPermission = RuntimePermissionUtil.checkPermissonGranted(getActivity(), cameraPerm);
        mySurfaceView = view.findViewById(R.id.camera_view);
        progressBar=view.findViewById(R.id.progressBar);
        if (hasCameraPermission) {
            // Setup QREader
            setupQREader();
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    100);
        }
        UserResponse userData = AppConfig.getUserData(getContext());
        String countrydata=userData.getUser().country.get(0).country;
        String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
        if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
        {
            if (HomeFragment.tabclickevent == 1) {
                clickAddressname = "PHP ";

            } else if (HomeFragment.tabclickevent == 2) {
                clickAddressname = "Koin ";

            } else if (HomeFragment.tabclickevent == 7) {
                clickAddressname = "Send DDK ";

            } else if (HomeFragment.tabclickevent == 3) {
                clickAddressname = "Send BTC ";

            }else if (HomeFragment.tabclickevent == 4) {
                clickAddressname = "Send TRON ";

            } else if (HomeFragment.tabclickevent == 5) {
                clickAddressname = "Send ETH ";

            } else if (HomeFragment.tabclickevent == 6) {
                clickAddressname = "Send USDT ";
            }
        }else
        {
             if (HomeFragment.tabclickevent == 1) {
                clickAddressname = "Koin ";

            } else if (HomeFragment.tabclickevent == 6) {
                clickAddressname = "Send DDK ";

            } else if (HomeFragment.tabclickevent == 2) {
                clickAddressname = "Send BTC ";

            } else if (HomeFragment.tabclickevent == 3) {
                 clickAddressname = "Send TRON ";

             } else if (HomeFragment.tabclickevent == 4) {
                clickAddressname = "Send ETH ";

            } else if (HomeFragment.tabclickevent == 5) {
                clickAddressname = "Send USDT ";
            }
        }

        return view;
    }


    void setupQREader()
    {
        qrEader = new QREader.Builder(getActivity(), mySurfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data)
            {
                    ddkscan="1";
                    MainActivity.addFragment(SendDDkFragment.getInstance(data), true);
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(mySurfaceView.getHeight())
                .width(mySurfaceView.getWidth())
                .build();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (hasCameraPermission)
        {
            // Cleanup in onPause()
            // --------------------
            qrEader.releaseAndCleanup();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasCameraPermission) {

            // Init and Start with SurfaceView
            // -------------------------------
            qrEader.initAndStart(mySurfaceView);
        }
        if(MainActivity.scanFragement==1)
        {
            String clickoptionv=clickoption;
            if(clickoptionv.equalsIgnoreCase("usingphp"))
            {
                MainActivity.setTitle("Scan via QR Code");
            }else
            {
                MainActivity.setTitle("Scan via QR Code");
            }
        }else
        {
            MainActivity.setTitle(clickAddressname);
        }
        MainActivity.enableBackViews(true);
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
                        if(mySurfaceView!=null)
                        {
                            setupQREader();
                            qrEader.initAndStart(mySurfaceView);
                        }else
                        {

                        }
                       // qrEader.initAndStart(mySurfaceView);

                    }
                }

                @Override
                public void onPermissionDenied() {
                    // do nothing
                }
            });
        }
    }

}
