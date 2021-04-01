package com.ddkcommunity.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.credential.CredentialsFragment;
import com.ddkcommunity.model.AllowGoogleModel;
import com.ddkcommunity.model.googleAuthPasswordModel;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.dataPutMethods;
import com.google.zxing.WriterException;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

public class GogolePasswordFragment extends Fragment implements View.OnClickListener
{
    public GogolePasswordFragment() {
        // Required empty public constructor
    }
    private static final int PERMISSION_REQUEST_CODE = 1;
    private View view;
    private Context mContext;
    ImageView ivQrCode;
    EditText etCodeAuthe;
    AppCompatButton btnShareQrCode;
    TextView secrate_view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.googlepasswordfragment, container, false);
            mContext = getActivity();
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        secrate_view=view.findViewById(R.id.secrate_view);
        etCodeAuthe=view.findViewById(R.id.etCodeAuthe);
        ivQrCode = view.findViewById(R.id.ivQrCode);
        btnShareQrCode=view.findViewById(R.id.btnShareQrCode);
        btnShareQrCode.setOnClickListener(this);
        String googlesecrate=App.pref.getString(Constant.GOOGLEAUTHSECRATE, "");
        secrate_view.setText(googlesecrate+"");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Verify Google Auth");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnShareQrCode:
                String etCodeAuthevalue=etCodeAuthe.getText().toString().trim();
                if(etCodeAuthevalue.equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please enter Verification Code", Toast.LENGTH_SHORT).show();
                }else
                {
                    sendVerificationCode(etCodeAuthevalue);
                }
                break;
        }
    }

    private void sendVerificationCode(final String etCodeAUpdate)
    {
        String googlesecrate=App.pref.getString(Constant.GOOGLEAUTHSECRATE, "");
        final ProgressDialog pd= new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("secret", googlesecrate);
        hm.put("code", etCodeAUpdate);
        Log.d("auth user param",hm+"");
        AppConfig.getLoadInterface().checkAuthCode(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm).enqueue(new Callback<googleAuthPasswordModel>() {
            @Override
            public void onResponse(Call<googleAuthPasswordModel> call, Response<googleAuthPasswordModel> response) {
                pd.dismiss();
                Log.d("auth user",response.body().toString());
                if(response.isSuccessful())
                {
                    Log.d("auth user response",response.body().toString());
                    if (response.body().getStatus() == 1)
                    {
                        //for data
                        if(response.body().getData().getCodeStatus().toString().equalsIgnoreCase("true"))
                        {
                           if(MainActivity.ClickViewButton.equalsIgnoreCase("profile"))
                           {
                              MainActivity.addFragment(new ProfileFragment(), false);
                            }else {
                              MainActivity.addFragment(new CredentialsFragment(), false);
                            }
                        }else
                        {
                            Toast.makeText(mContext, "Please enter Valid code", Toast.LENGTH_SHORT).show();
                        }
                    }else
                    {
                        AppConfig.showToast(response.body().getMsg());
                    }
                } else
                {
                    ShowApiError(mContext,"server error all_transactions/update-user-auth-status");
                }
            }

            @Override
            public void onFailure(Call<googleAuthPasswordModel> call, Throwable t) {
                errordurigApiCalling(getActivity(),t.getMessage());
                pd.dismiss();
            }
        });
    }

}
