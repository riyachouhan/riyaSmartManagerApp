package com.ddkcommunity.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.model.userInviteModel;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.dataPutMethods;
import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;
import static com.ddkcommunity.utilies.dataPutMethods.getSettingServerDataSt;
import static com.ddkcommunity.utilies.dataPutMethods.putGoogleAuthStatus;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {

    public static String msg_type, mobile1, type;
    public static EditText otp_edt1, otp_edt2, otp_edt3, otp_edt4, etPassword;
    //private IncomingSms receiver;
    public static CountDownTimer countDownTimer;
    private static String email1;
    Context context;
    TextView timer, full_name_txt;
    TextView resend_otp;
    StringBuffer str_top;
    String id;
    UserResponse data;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_activity);
        context = this;
        resend_otp = findViewById(R.id.resend_otp);
        full_name_txt = findViewById(R.id.full_name_txt);
        timer = findViewById(R.id.timer);
        otp_edt1 = findViewById(R.id.otp_edt11);
        otp_edt2 = findViewById(R.id.otp_edt12);
        otp_edt3 = findViewById(R.id.otp_edt13);
        otp_edt4 = findViewById(R.id.otp_edt14);
        etPassword = findViewById(R.id.etPassword);
        image = findViewById(R.id.image);
        email1 = App.pref.getString("email", "");

        otp_edt1.requestFocus();
        startTimer();
        full_name_txt.setText("OTP has been sent successfully to your Email. Please verify your account by entering it below.");
        msg_type = "send";

        if (App.pref.getString("switchUser", "").equalsIgnoreCase("yes")) {
            etPassword.setVisibility(View.VISIBLE);
        } else {
            etPassword.setVisibility(View.GONE);
        }
        findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_top = new StringBuffer();
                str_top.append(otp_edt1.getText().toString()).append(otp_edt2.getText().toString()).append(otp_edt3.getText().toString()).append(otp_edt4.getText().toString());
                if (str_top.length() > 0 && str_top.length() == 4) {
                    if (App.pref.getString("switchUser", "").equalsIgnoreCase("yes")) {
                        switchUser(str_top.toString());
                    } else {
                        if (App.pref.getString("signUp", "").equals("yes")) {
                            checkCodeCall(email1, str_top.toString(), "register");
                        } else {
                            checkCodeCall(email1, str_top.toString(), "other");
                        }
                    }
                } else {
                    AppConfig.showToast("Code must be of 4 digits");
                }
            }
        });

        otp_edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otp_edt1.length() == 0) {
                    otp_edt1.requestFocus();
                }
                if (otp_edt1.length() == 1) {
                    otp_edt1.clearFocus();
                    otp_edt2.requestFocus();
                    otp_edt2.setCursorVisible(true);
                }
            }
        });

        otp_edt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otp_edt2.length() == 0) {
                    otp_edt2.requestFocus();
                }
                if (otp_edt2.length() == 1) {
                    otp_edt2.clearFocus();
                    otp_edt3.requestFocus();
                    otp_edt3.setCursorVisible(true);
                }
            }
        });


        otp_edt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otp_edt3.length() == 0) {
                    otp_edt3.requestFocus();
                }
                if (otp_edt3.length() == 1) {
                    otp_edt3.clearFocus();
                    otp_edt4.requestFocus();
                    otp_edt4.setCursorVisible(true);
                }
            }
        });


        otp_edt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otp_edt4.length() == 0) {
                    otp_edt4.requestFocus();
                }
                if (otp_edt4.length() == 1) {
                    otp_edt4.requestFocus();
                }
            }
        });
        otp_edt2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    otp_edt1.requestFocus();
                }
                return false;
            }
        });
        otp_edt3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    otp_edt2.requestFocus();
                }
                return false;
            }
        });
        otp_edt4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    otp_edt3.requestFocus();
                }
                return false;
            }
        });

        resend_otp.setOnClickListener(this);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {
                long min = millisUntilFinished / 60000;
                String minute = String.valueOf(min);
                if (min < 10) {
                    minute = "0" + minute;
                }
                long sec = millisUntilFinished % 60000 / 1000;
                String second = String.valueOf(sec);
                if (sec < 10) {
                    second = "0" + second;
                }
                timer.setText(String.format("Valid for %s:%s", minute, second));
            }

            public void onFinish() {
                //Toast.makeText(context, "Your OTP will be Expire.", Toast.LENGTH_SHORT).show();
                resend_otp.setVisibility(View.VISIBLE);
                timer.setText("Your OTP is Expired.");
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resend_otp:
                if (App.pref.getString("signUp", "").equals("yes"))
                    ResendOtpCall(App.pref.getString(Constant.USER_ID, ""), email1, "signup");
                else
                    ResendOtpCall(App.pref.getString(Constant.USER_ID, ""), email1, "login");
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /************************** OTP Verification ************************/

    private void checkCodeCall(String emailId, String otpStr, String verify1) {

        final ProgressDialog dialog = new ProgressDialog(context);
        AppConfig.showLoading(dialog, "Verify OTP...");
        Call<ResponseBody> call = AppConfig.getLoadInterface().CheckOtpCall(AppConfig.getStringPreferences(context, Constant.JWTToken), AppConfig.setRequestBody(emailId), AppConfig.setRequestBody(otpStr), AppConfig.setRequestBody(verify1));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();

                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(AppConfig.STATUS) == 1) {
                            countDownTimer.cancel();
                            if (App.pref.getBoolean("showPassphrase", false)) {
                                App.editor.putBoolean("showPassphrase", false);
                                App.editor.putBoolean("isPassphraseVerified", true);
                                App.editor.apply();
                                finish();
                            } else {
                                AppConfig.showToast("Login successfully.");
                                getInviteFriend();
                                getProfileCall(AppConfig.getStringPreferences(context, Constant.JWTToken));
                                getSettingServerDataSt(OTPActivity.this,"php");

                            }
                        } else if (object.getInt(AppConfig.STATUS) == 0) {
                            AppConfig.showToast(object.getString("msg"));
                        } else if (object.getInt(Constant.STATUS) == 3) {
                            AppConfig.showToast(object.getString("msg"));
                            AppConfig.openSplashActivity((Activity)context);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ShowApiError(OTPActivity.this,"server error in check_otp");
                }
                AppConfig.hideLoading(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppConfig.hideLoading(dialog);
                errordurigApiCalling(OTPActivity.this,t.getMessage());
            }
        });
    }

    private void latestDDKPrice()
    {
        UserModel.getInstance().getUSDCall(new GetUSDAndBTCCallback() {
            @Override
            public void getValues(BigDecimal btc, BigDecimal usd) {
                if (usd != null) {
                    BigDecimal ONE_HUNDRED = new BigDecimal(100);
                    BigDecimal buy = usd.multiply(UserModel.getInstance().ddkBuyPercentage).divide(ONE_HUNDRED);
                    BigDecimal sell = usd.multiply(UserModel.getInstance().ddkSellPercentage).divide(ONE_HUNDRED);

                    if (UserModel.getInstance().ddkBuyPrice == null) {
                        UserModel.getInstance().ddkBuyPrice = buy.add(usd);
                        UserModel.getInstance().ddkSellPrice = usd.subtract(sell);
                        //  LocalBroadcastManager.getInstance(CustomPinActivity.this).sendBroadcast(new Intent("updatePrice"));
                    }
                }
            }
        },OTPActivity.this);
    }

    private void getProfileCall(String token) {
        final ProgressDialog dialog = new ProgressDialog(context);
        AppConfig.showLoading(dialog, "Verify OTP...");
        AppConfig.setStringPreferences(context, Constant.JWTToken, token);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
        hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
        Call<ResponseBody> call = AppConfig.getLoadInterface().getProfileCall(token,hm);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseDatap = response.body().string();
                        JSONObject object = new JSONObject(responseDatap);
                        if (object.getInt(AppConfig.STATUS) == 1)
                        {
                            latestDDKPrice();
                            data = new Gson().fromJson(responseDatap, UserResponse.class);
                            AppConfig.setUserData(context, data);
                            String user_idvalue=data.getUser().getId().toString();
                            App.editor.putString(Constant.USER_ID,user_idvalue);
                            //...........
                            putGoogleAuthStatus(data.getUser().getGauth_status(),data.getUser().getGoogle_authentication(),data.getUser().getGoogle_auth_secret());
                            //..........
                            LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
                            lockManager.enableAppLock(OTPActivity.this, CustomPinActivity.class);

                            //for
                            Intent intent = new Intent(OTPActivity.this, CustomPinActivity.class);
                            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
                            startActivityForResult(intent, MainActivity.REQUEST_CODE_ENABLE);
                        } else if (object.getInt(Constant.STATUS) == 3) {
                            AppConfig.showToast(object.getString("msg"));
                            AppConfig.openSplashActivity((Activity)context);
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
                    ShowApiError(OTPActivity.this,"server error in eightface/user-profile");
                }
                AppConfig.hideLoading(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                AppConfig.hideLoading(dialog);
            }
        });
    }

    private void getInviteFriend()
    {
        String userReferalCode =App.pref.getString(Constant.USER_REFERAL_CODE, "");
        Log.d("token",AppConfig.getStringPreferences(OTPActivity.this, Constant.JWTToken));
        HashMap<String, String> hm = new HashMap<>();
        hm.put("referral_code", userReferalCode);
        AppConfig.getLoadInterface().getInviteFriends(AppConfig.getStringPreferences(OTPActivity.this, Constant.JWTToken), hm).enqueue(new Callback<userInviteModel>() {
            @Override
            public void onResponse(Call<userInviteModel> call, Response<userInviteModel> response) {
                try {
                    Log.d("sam erro par invi",response.body().toString());
                    if (response.isSuccessful() && response.body() != null)
                    {
                        if (response.body().getStatus() == 1)
                        {
                            try {
                                String INVITE_CONTENT = response.body().getData();
                                App.editor.putString(Constant.INVITE_CONTENT,INVITE_CONTENT);
                                App.editor.apply();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else {
                            AppConfig.showToast(response.body().getMsg());
                        }
                    } else {
                        ShowApiError(OTPActivity.this,"server error apigetreferralcode/get-referral-code");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<userInviteModel> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MainActivity.REQUEST_CODE_ENABLE) {
            App.editor.putBoolean(AppConfig.isPin, true);
            App.editor.commit();
            openHomeActivity();
        }
    }

    private void openHomeActivity() {
//        App.editor.putString(Constant.USER_ID, data.getUser().getId().toString());
        App.editor.putString(Constant.USER_EMAIL, data.getUser().getEmail());
        App.editor.putString(Constant.USER_NAME, data.getUser().getName());
        App.editor.putString(Constant.USER_MOBILE, data.getUser().getMobile());
        App.editor.putBoolean(Constant.IS_LOGIN, true);
        App.editor.apply();

        if (App.pref.getString("signUp", "").equals("yes")) {
            App.editor.putString("signUp", "");
            App.editor.commit();
            if (App.pref.getString("checked", "").equals("1")) {
             } else {
                Intent intent = new Intent(OTPActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        } else {
            Intent intent = new Intent(OTPActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void ResendOtpCall(String userId, String email, String verify) {

        final ProgressDialog dialog = new ProgressDialog(context);
        AppConfig.showLoading(dialog, "Resend OTP...");

        Call<ResponseBody> call = AppConfig.getLoadInterface().resendOtp(
                AppConfig.getStringPreferences(context, Constant.JWTToken),
                AppConfig.setRequestBody(email), AppConfig.setRequestBody(verify));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(AppConfig.STATUS) == 1) {

                            Toast.makeText(context, "Resend OTP to your email id.", Toast.LENGTH_SHORT).show();
                            timer.setVisibility(View.VISIBLE);
                            resend_otp.setVisibility(View.GONE);
                            startTimer();

                        } else if (object.getInt(Constant.STATUS) == 3) {
                            AppConfig.showToast(object.getString("msg"));
                            AppConfig.openSplashActivity((Activity)context);
                        } else if (object.getInt(AppConfig.STATUS) == 0) {
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
                    ShowApiError(OTPActivity.this,"server error in recent_otp");
                }
                AppConfig.hideLoading(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppConfig.hideLoading(dialog);
                t.printStackTrace();
            }
        });
    }


    private void switchUser(String otp) {
        if (etPassword.getText().toString().trim().isEmpty()) {
            AppConfig.showToast("Please enter password");
            return;
        }
        HashMap<String, String> hm = new HashMap<>();
        hm.put("email", App.pref.getString(Constant.USER_EMAIL, ""));
        hm.put("otp", otp);
        hm.put("password", etPassword.getText().toString().trim());
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Switching user....");

        AppConfig.getLoadInterface().switchUser(hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();

                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(AppConfig.STATUS) == 1) {
                            AppConfig.showToast("Switching successfully.");
                            clearUserData();
                            AppConfig.setStringPreferences(context, Constant.JWTToken, object.getString("token"));
                            getInviteFriend();
                            getProfileCall(object.getString("token"));
                        } else if (object.getInt(AppConfig.STATUS) == 3) {
                            AppConfig.showToast(object.getString("msg"));
                        } else if (object.getInt(AppConfig.STATUS) == 0) {
                            AppConfig.showToast(object.getString("msg"));
                        } else {
                            AppConfig.showToast("Server Error");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ShowApiError(OTPActivity.this,"server error in secondFace/switch-user");
                }
                AppConfig.hideLoading(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void clearUserData() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                AppConfig.PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        String uniqueID = sharedPrefs.getString(AppConfig.PREF_UNIQUE_ID, null);

        App.editor.clear();
        App.editor.commit();

        if (uniqueID != null && !uniqueID.isEmpty()) {
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(AppConfig.PREF_UNIQUE_ID, uniqueID);
            editor.commit();
        }

        LockManager lockManager = LockManager.getInstance();
        lockManager.getAppLock().setPasscode(null);
    }
}
