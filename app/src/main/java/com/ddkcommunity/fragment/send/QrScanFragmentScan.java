package com.ddkcommunity.fragment.send;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.HomeFragment;
import com.ddkcommunity.model.scanQRModel;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.RPResultListener;
import com.ddkcommunity.utilies.RuntimePermissionUtil;

import java.util.HashMap;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.fragment.ScanFragment.clickoption;
import static com.ddkcommunity.fragment.send.SendFragment.ddkscan;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.ShowSameWalletDialog;
import static com.ddkcommunity.utilies.dataPutMethods.showScanWalletDialog;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class QrScanFragmentScan extends Fragment {
    private static final String cameraPerm = Manifest.permission.CAMERA;
    boolean hasCameraPermission = false;
    private SurfaceView mySurfaceView;
    private QREader qrEader;
    private String clickAddressname="";
    ProgressBar progressBar;
    View view;
    public static int scandialogstauus=0,apicalling=0;
    String clickoptionv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_qr_scan, container, false);
        hasCameraPermission = RuntimePermissionUtil.checkPermissonGranted(getActivity(), cameraPerm);
        mySurfaceView = view.findViewById(R.id.camera_view);
        if (hasCameraPermission) {
            // Setup QREader
            setupQREader();
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    100);
        }
        return view;
    }

    void setupQREader()
    {
        qrEader = new QREader.Builder(getActivity(), mySurfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data)
            {
                    clickoptionv=clickoption;
                    String sendtype="";
                    if(clickoptionv.equalsIgnoreCase("usingphp"))
                    {
                       sendtype="receiver_email";
                    }else
                    {
                        sendtype="receiver_address";
                    }
                    if (view.getVisibility()==View.VISIBLE)
                    {
                        if(scandialogstauus==0 && apicalling==0)
                        {
                            apicalling=1;
                            getUserValidDate(sendtype,data);
                        }

                    }

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
        MainActivity.setTitle("Scan via QR Code");
        MainActivity.scanview.setVisibility(View.GONE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        if (requestCode == 100) {
            RuntimePermissionUtil.onRequestPermissionsResult(grantResults, new RPResultListener() {
                @Override
                public void onPermissionGranted()
                {
                    if (RuntimePermissionUtil.checkPermissonGranted(getActivity(), cameraPerm))
                    {
//                        restartActivity();
                        if(mySurfaceView!=null)
                        {
                            setupQREader();
                        qrEader.initAndStart(mySurfaceView);
                        }else
                        {
                            //MainActivity.addFragment(new QrScanFragmentScan(), false);

                        }

                    }
                }

                @Override
                public void onPermissionDenied() {
                    // do nothing
                }
            });
        }
    }

    private void getUserValidDate(String sendtype,final String data)
    {
        try
        {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("send_type", sendtype);
            hm.put("receiver", data);
            AppConfig.getLoadInterface().getScanQRCode(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<scanQRModel>()
            {
                @Override
                public void onResponse(Call<scanQRModel> call, Response<scanQRModel> response)
                {
                    try {
                        Log.d("sam erro par invi", response.body().toString());
                        if (response.isSuccessful() && response.body() != null)
                        {
                            if (response.body().getStatus() == 1)
                            {
                                try {
                                    scandialogstauus=0;
                                    String emailreceiver;
                                    if(clickoptionv.equalsIgnoreCase("usingphp"))
                                    {
                                        emailreceiver = response.body().getData().getEmail();
                                    }else
                                    {
                                        emailreceiver = response.body().getData().getReceiver_address();
                                    }
                                    String nameva = response.body().getData().getName();
                                    Fragment fragment = new PayUsingScanFragment();
                                    Bundle arg = new Bundle();
                                    arg.putString("address", nameva);
                                    arg.putString("emailaddress", emailreceiver);
                                    fragment.setArguments(arg);
                                    MainActivity.addFragment(fragment, true);
                                    qrEader.releaseAndCleanup();
                                    qrEader.stop();
                                    apicalling=0;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else
                            {

                                scandialogstauus=1;
                                apicalling=0;
                                showScanWalletDialog(getActivity(),response.body().getMsg(),"1");
                                //   AppConfig.showToast(response.body().getMsg());
                            }
                        } else {
                            ShowApiError(getActivity(), "server error user-wallet/scan-qrcode");
                        }

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<scanQRModel> call, Throwable t)
                {
                  //  Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
