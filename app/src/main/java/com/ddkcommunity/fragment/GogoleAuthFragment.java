package com.ddkcommunity.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.model.getGoogleAuthSecraModel;
import com.ddkcommunity.model.googleAuthPasswordModel;
import com.ddkcommunity.model.samModel;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.TextToImageEncode;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;
import static com.ddkcommunity.utilies.dataPutMethods.generateEncyTab;
import static com.ddkcommunity.utilies.dataPutMethods.putGoogleAuthStatus;

public class GogoleAuthFragment extends Fragment /*implements View.OnClickListener*/
{
    public GogoleAuthFragment() {
        // Required empty public constructor
    }
    AppCompatImageView btnCopyTransactionId;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private View view;
    private Context mContext;
    private Bitmap bitmap;
    ImageView ivQrCode;
    AppCompatButton btnShareQrCode;
    EditText etAuthview;
    TextView secrate_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.google_qr_activity, container, false);
            mContext = getActivity();
            secrate_view=view.findViewById(R.id.secrate_view);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        btnShareQrCode=view.findViewById(R.id.btnShareQrCode);
        etAuthview=view.findViewById(R.id.etAuthview);
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission(); // Code for permission
            }
        }
        ivQrCode = view.findViewById(R.id.ivQrCode);
        getAuthQrCode();

        btnShareQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etCodeAuthevalue=etAuthview.getText().toString().trim();
                if(etCodeAuthevalue.equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please enter Verification Code", Toast.LENGTH_SHORT).show();
                }else
                {
                    sendVerificationCode(etCodeAuthevalue);
                }
            }
        });

        btnCopyTransactionId=view.findViewById(R.id.btnCopyTransactionId);
        btnCopyTransactionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String googlevaue=App.pref.getString(Constant.GOOGLEAUTHSECRATE, "");
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copy",googlevaue);
                clipboard.setPrimaryClip(clip);
                AppConfig.showToast("Copied");
            }
        });
        return view;
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
                            App.editor.putString(Constant.GOOGLEAUThPendingRegit, "verified");
                            MainActivity.addFragment(new ProfileFragment(), false);
                        }else
                        {
                            Toast.makeText(mContext, ""+response.body().getMsg().toString(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<googleAuthPasswordModel> call, Throwable t)
            {
                pd.dismiss();
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
        } else {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Google Authenticator");
        MainActivity.enableBackViews(true);
    }

    private void getAuthQrCode()
    {
        final ProgressDialog pd= new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        AppConfig.getLoadInterface().getQRSecrateAuth(AppConfig.getStringPreferences(mContext, Constant.JWTToken)).enqueue(new Callback<getGoogleAuthSecraModel>() {
            @Override
            public void onResponse(Call<getGoogleAuthSecraModel> call, Response<getGoogleAuthSecraModel> response) {
                Log.d("auth user",response.body().toString());
                if(response.isSuccessful())
                {
                    try {
                        Log.d("auth user response", response.body().toString());
                        if (response.body().getStatus() == 1) {
                            //for data
                            String qr_url=response.body().getData().getQrUrl();
                            String secrate=response.body().getData().getSecret();
                            App.editor.putString(Constant.GOOGLEAUTHSECRATE,""+secrate);
                            App.editor.apply();
                            secrate_view.setText("YOUR KEY :- "+secrate+"");
                            Glide.with(mContext)
                                    .asBitmap()
                                    .load(qr_url)
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                            ivQrCode.setImageBitmap(resource);
                                        }

                                        @Override
                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                            super.onLoadFailed(errorDrawable);
                                            ivQrCode.setImageResource(R.drawable.default_photo);
                                        }

                                    });
                            pd.dismiss();
                        } else {
                            pd.dismiss();
                            AppConfig.showToast(response.body().getMsg());
                        }
                    }catch (Exception e)
                    {
                        pd.dismiss();
                        e.printStackTrace();
                    }
                } else
                {
                    pd.dismiss();
                    ShowApiError(mContext,"server error all_transactions/update-user-auth-status");
                }
            }

            @Override
            public void onFailure(Call<getGoogleAuthSecraModel> call, Throwable t)
            {
                errordurigApiCalling(getActivity(),t.getMessage());
                pd.dismiss();
            }
        });
    }

}
