package com.ddkcommunity.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ddkcommunity.R;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.CryptoUtils;
import com.ddkcommunity.utilies.dataPutMethods;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;
import static com.ddkcommunity.utilies.dataPutMethods.generateEncyTab;
import static com.ddkcommunity.utilies.dataPutMethods.putENCYDataView;
import static com.ddkcommunity.utilies.dataPutMethods.putGoogleAuthStatus;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    public static final int RC_SIGN_IN = 007;
    private static final String TAG = LoginActivity.class.getSimpleName();

    public static CallbackManager callbackManager;
    public static TwitterLoginButton twitterLoginButton;
    public static GoogleApiClient mGoogleApiClient;
    public static Activity activity;
    private static EditText emailET, passET;
    TextView forgot_TV, signup2_TV;
    private LoginButton loginButton;
    private String regId;
    private Context mContext;
    private String deviceToken;

//    public static void googleSignIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
//    }

    private static void openHomeActivity(UserResponse data) {
        Intent intent = new Intent(LoginActivity.activity, OTPActivity.class);

        App.editor.putString("email", data.getUser().getEmail());
        App.editor.putString(Constant.USER_EMAIL, data.getUser().getEmail());
        App.editor.putString(Constant.USER_NAME, data.getUser().getName());
        App.editor.putString(Constant.USER_MOBILE, data.getUser().getMobile());
        App.editor.putString("switchUser", "no");
        App.editor.commit();

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        LoginActivity.activity.startActivity(intent);
        LoginActivity.activity.finish();

    }

    //******************Google Account Info fetch*****************************************
//    public void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
//        if (result.isSuccess()) {
//
//            GoogleSignInAccount acct = result.getSignInAccount();
//
//            Log.e(TAG, "display name: " + acct.getDisplayName());
//            String personName = acct.getDisplayName();
//            String personPhotoUrl;
//            try {
//                personPhotoUrl = acct.getPhotoUrl().toString();
//            } catch (Exception e) {
//                e.printStackTrace();
//                personPhotoUrl = "";
//            }
//            String email = acct.getEmail();
//            String socialId = acct.getId();
//
//            SocialLogin(socialId, email, personName, "", personPhotoUrl, "gplus");
//
//        }
//    }

//    private void SocialLogin(String socialIdString, String emailString,
//                             String usernameString, String mobileString,
//                             final String imageUrlString, String socialTypeString) {
//
//        DownloadFileFromURL fileFromURL = new DownloadFileFromURL();
//
//        fileFromURL.execute(socialIdString, emailString, usernameString, mobileString, imageUrlString, socialTypeString);
//
//
//    }

    public void getProfileCall(String token) {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(activity);
            AppConfig.showLoading(dialog, "Fetching User Details..");
            String snsn=App.pref.getString(Constant.IVPARAM, "");
            HashMap<String, String> hm = new HashMap<>();
            hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
            hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
            Call<ResponseBody> call = AppConfig.getLoadInterface().getProfileCall(token,hm);

            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful())
                    {
                        try {
                            String responseData = response.body().string();
                            Log.e("User profile", responseData);
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {
                                UserResponse data = new Gson().fromJson(responseData, UserResponse.class);
                                AppConfig.setUserData(mContext, data);
                                openHomeActivity(data);
                                String referalcode = data.getUser().unique_code;
                                App.editor.putString(Constant.USER_REFERAL_CODE,referalcode);
                                App.editor.apply();
                                //......
                                putGoogleAuthStatus(data.getUser().getGauth_status(),data.getUser().getGoogle_authentication(),data.getUser().getGoogle_auth_secret());
                                //for etc and bt
                                try {
                                    JSONObject jsonob = object.getJSONObject(Constant.eth_details);
                                    if(jsonob.length()!=0)
                                    {
                                        dataPutMethods.putOtherWallet(data.getEth_details().getPublic_key(),"",data.getEth_details().getWallet_address().toString(),data.getEth_details().getWallet_id().toString(),data.getEth_details().getSecret().toString());
                                    }

                                    JSONObject jsonobusdt = object.getJSONObject(Constant.usdt_details);
                                    if(jsonobusdt.length()!=0)
                                    {
                                        dataPutMethods.putUsdtWallet(data.getUsdt_details().getPublic_key(),data.getUsdt_details().getWallet_address().toString(),data.getUsdt_details().getWallet_id().toString(),data.getUsdt_details().getSecret().toString());
                                    }

                                    JSONObject jsonobtron = object.getJSONObject(Constant.usdt_details);
                                    if(jsonobtron.length()!=0)
                                    {
                                        dataPutMethods.puttronWallet(data.getUsdt_details().getPublic_key(),data.getUsdt_details().getWallet_address().toString(),data.getUsdt_details().getWallet_id().toString(),data.getUsdt_details().getSecret().toString());
                                    }

                                    JSONObject jsonobbtc = object.getJSONObject(Constant.btc_details);
                                    if(jsonobusdt.length()!=0)
                                    {
                                        dataPutMethods.putBTCWallet(data.getBtc_details().getPublic_key(),data.getBtc_details().getWallet_address(),data.getBtc_details().getWallet_id(),data.getBtc_details().getSecret());
                                    }

                                    JSONObject jsonsamkoin = object.getJSONObject(Constant.samkoin_details);
                                    if(jsonsamkoin.length()!=0)
                                    {
                                        dataPutMethods.putSamKoinWallet(data.getSam_koin_details().getPublic_key(),data.getSam_koin_details().getWallet_address(),data.getSam_koin_details().getWallet_id(),data.getSam_koin_details().getSecret());
                                    }

                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                            } else {
                                AppConfig.showToast(object.getString("msg"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ShowApiError(LoginActivity.this,"server error in eightface/user-profile");
                    }
                    AppConfig.hideLoading(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppConfig.hideLoading(dialog);
                    errordurigApiCalling(LoginActivity.this,t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;
        mContext = this;
        File Directory = new File("/sdcard/DDkCommunity/");
        Directory.mkdirs();
        emailET = findViewById(R.id.email_ET);
        passET = findViewById(R.id.password_ET);
        emailET.requestFocus();
        findViewById(R.id.signup2_TV).setOnClickListener(this);
        findViewById(R.id.submit_BT).setOnClickListener(this);
        findViewById(R.id.forgot_LL).setOnClickListener(this);
        forgot_TV = findViewById(R.id.forgot_TV);
        forgot_TV.setText(Html.fromHtml(getString(R.string.forget)));
        forgot_TV.setOnClickListener(this);
        signup2_TV = findViewById(R.id.signup2_TV);
        signup2_TV.setText(Html.fromHtml(getString(R.string.signup)));
        signup2_TV.setOnClickListener(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                deviceToken = instanceIdResult.getToken();
                AppConfig.setStringPreferences(mContext, Constant.FIREBASE_TOKEN, instanceIdResult.getToken());
            }
        });
        //for value
        String IVparam=App.pref.getString(Constant.IVPARAM, "");
        if(IVparam ==null || IVparam.equalsIgnoreCase("null") || IVparam.equalsIgnoreCase("")) {
            //for code
            generateEncyTab();
         }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup2_TV:
                openSignUpFragment();
                break;
            case R.id.submit_BT:
                String email = emailET.getText().toString();
                String pass = passET.getText().toString();
                if (!AppConfig.isStringNullOrBlank(email) && AppConfig.isEmail(email))
                {
                    if (!AppConfig.isStringNullOrBlank(pass)) {
                        loginCall(email, pass, "", Constant.ANDROID);
                    } else {
                        passET.setError("Please enter valid Password.");
                        AppConfig.showToast("Please enter valid Password.");
                    }
                } else {
                    emailET.setError("Please enter valid Email.");
                    AppConfig.showToast("Please enter valid Email.");
                }
                break;
            case R.id.forgot_TV:
            case R.id.forgot_LL:
                openForgotFragment();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.e(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void openForgotFragment() {
        startActivity(new Intent(activity, ForgotActivity.class));
        finish();
    }

    private void openSignUpFragment() {
        startActivity(new Intent(activity, SignupActivity.class));
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void loginCall(String email, String pass, String fcmToken, String deviceType) {
        String token = App.RegPref.getString(Constant.FIREBASE_TOKEN, "");
        fcmToken = token;
        Log.d("Token===>>>", token);
        if (AppConfig.isInternetOn())
        {
            final ProgressDialog dialog = new ProgressDialog(activity);
            AppConfig.showLoading(dialog, "Logging..");
            HashMap<String, String> hm = new HashMap<>();
            hm.put("email", email);
            hm.put("password", pass);
            hm.put("fcm_id", fcmToken);
            hm.put("device_type", deviceType);
            hm.put("device_id", getDeviceID());
            Call<ResponseBody> call = AppConfig.getLoadInterface().loginCall(hm);

            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful())
                    {
                        try {
                            String responseData = response.body().string();
                            Log.d("login app",responseData);
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {
                                AppConfig.showToast("Login Successfully.");
                                App.editor.putString("signUp", "no");
                                App.editor.apply();
                                UserResponse data = new Gson().fromJson(responseData, UserResponse.class);
                                AppConfig.setStringPreferences(mContext, Constant.JWTToken, object.getString("token"));
                                getProfileCall(object.getString("token"));
                            } else {
                                AppConfig.showToast(object.getString("msg"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ShowApiError(LoginActivity.this,"server error in user-login-dev");
                    }
                    AppConfig.hideLoading(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppConfig.hideLoading(dialog);
                    errordurigApiCalling(LoginActivity.this,t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    public String getDeviceID() {
        String deviceId = "";
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission====>>", "Read Phone State");
        } else {
            try {
                TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (mTelephony != null && mTelephony.getDeviceId() != null) {
                    deviceId = mTelephony.getDeviceId();
                } else {
                    deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            } catch (Exception e) {
                deviceId = AppConfig.getUUIDDeviceID(mContext);
            }
            if (deviceId == null || deviceId.isEmpty()) {
                deviceId = AppConfig.getUUIDDeviceID(mContext);
            }
        }
        return deviceId;
    }

}
