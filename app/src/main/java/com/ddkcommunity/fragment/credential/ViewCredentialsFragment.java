package com.ddkcommunity.fragment.credential;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.activities.OTPActivity;
import com.ddkcommunity.activities.SplashActivity;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.utilies.AppConfig;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewCredentialsFragment extends Fragment {

    private TextView nameET, ddkET, passPhaseET, passPhase2ET, referalET, notesET, btnShowPassphrase;
    private TextView ddkIV, passIV;
    private boolean isShowPassPhrase = false;
    private ImageView pass_2_copy_IV, pass_copy_IV;
    private Credential credential;

    public ViewCredentialsFragment(Credential credential1) {
        this.credential = credential1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_credentials, container, false);
        nameET = view.findViewById(R.id.name_ET);
        ddkET = view.findViewById(R.id.ddk_ET);
        ddkIV = view.findViewById(R.id.share_IV);
        passIV = view.findViewById(R.id.copy_IV);
        passPhaseET = view.findViewById(R.id.pass_phase_ET);
        passPhase2ET = view.findViewById(R.id.pass_phase2_ET);
        referalET = view.findViewById(R.id.referal_Link_ET);
        notesET = view.findViewById(R.id.notes_ET);
        pass_copy_IV = view.findViewById(R.id.pass_copy_IV);
        pass_2_copy_IV = view.findViewById(R.id.pass_2_copy_IV);
        btnShowPassphrase = view.findViewById(R.id.btnShowPassphrase);

        btnShowPassphrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendOtpCall(App.pref.getString(Constant.USER_ID, ""), App.pref.getString(Constant.USER_EMAIL, ""), "login");
            }
        });

        nameET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, credential.getReferalLink());
                startActivity(Intent.createChooser(sharingIntent, "Share Credentials"));
            }
        });

        if (!AppConfig.isStringNullOrBlank(credential.getDdkcode()))
            ddkIV.setVisibility(View.VISIBLE);
        if (!AppConfig.isStringNullOrBlank(credential.getPassphrase()))
            passIV.setVisibility(View.VISIBLE);

        ddkIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConfig.copyShare("", "https://preproduction1.ddkoin.com/referal/" + credential.getDdkcode(), "Share DDK Address", getActivity());

            }
        });
        passIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConfig.copy("", "https://preproduction1.ddkoin.com/referal/" + credential.getReferalLink(), "Copy ReferralLink", getActivity());

            }
        });
        view.findViewById(R.id.ddk_copy_IV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConfig.copy("", credential.getDdkcode(), "Copy DDK Address", getActivity());

            }
        });
        view.findViewById(R.id.pass_copy_IV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String credialvalue=passPhaseET.getText().toString();
                AppConfig.copyPass(credialvalue, "Copy Passpharse", getActivity());

            }
        });
        if (!AppConfig.isStringNullOrBlank(credential.getSecondPassphrase()))
            view.findViewById(R.id.pass_2_copy_IV).setVisibility(View.GONE);
        view.findViewById(R.id.pass_2_copy_IV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConfig.copyPass(passPhase2ET.getText().toString(), "Copy Second Passpharse", getActivity());

            }
        });

        nameET.setText(credential.getName());
        if (credential.getDdkcode() != null) {
            ddkET.setText(credential.getDdkcode());
        }

       //passPhaseET.setText(credential.getPassphrase());
        /*if (credential.getSecondPassphrase() != null) {
            passPhase2ET.setText(credential.getSecondPassphrase());
        }*/
        referalET.setText(credential.getReferalLink());
        if (credential.getNotes() != null) {
            notesET.setText(credential.getNotes());
        }
        if (!AppConfig.isStringNullOrBlank(credential.getReferalLink())) {
            referalET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConfig.copyAndShareLink("", credential.getReferalLink(), "Share Referral Link", getActivity());
                }
            });
        }
        String secrate=App.pref.getString(Constant.KEYENCYPARAM, "");
        if(CredentialsFragment.credential_list.size()!=0)
        {
            for(int i=0;i<CredentialsFragment.credential_list.size();i++)
            {
                if(CredentialsFragment.credential_list.get(i).getCredentialId().equalsIgnoreCase(credential.getCredentialId()))
                {
                    passPhaseET.setText(CredentialsFragment.credential_list.get(i).getPassphrase());
                    if (CredentialsFragment.credential_list.get(i).getSecondPassphrase() != null)
                    {
                        passPhase2ET.setText(CredentialsFragment.credential_list.get(i).getSecondPassphrase());
                    }
                }
            }
        }else
        {
            passPhaseET.setText(credential.getPassphrase());
        }
        return view;
    }

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public static String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            }
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("View Credential");

        if (isShowPassPhrase && App.pref.getBoolean("isPassphraseVerified", false)) {

            App.editor.putBoolean("showPassphrase", false);
            App.editor.putBoolean("isPassphraseVerified", false);
            App.editor.apply();
            isShowPassPhrase = false;
            passPhaseET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passPhase2ET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            pass_copy_IV.setVisibility(View.VISIBLE);
            if (!AppConfig.isStringNullOrBlank(credential.getSecondPassphrase())) {
                pass_2_copy_IV.setVisibility(View.VISIBLE);
            }
        }
    }

    private void SendOtpCall(final String userId, final String email, String verify) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog, "Send OTP...");

        Call<ResponseBody> call = AppConfig.getLoadInterface().resendOtp(
                AppConfig.getStringPreferences(getActivity(), Constant.JWTToken),
                AppConfig.setRequestBody(email), AppConfig.setRequestBody(verify));

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.d("otp api",responseData);
                        if (object.getInt(AppConfig.STATUS) == 1) {
                            opeOtpActivity(email, userId);
                        } else if (object.getInt(AppConfig.STATUS) == 0) {
                            AppConfig.showToast(object.getString("msg"));
                        } else if (object.getInt(Constant.STATUS) == 3) {
                            AppConfig.showToast(object.getString("msg"));
                            AppConfig.openSplashActivity(getActivity());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ShowApiError(getActivity(),"server error in recent_otp");
                }
                AppConfig.hideLoading(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppConfig.hideLoading(dialog);
                t.printStackTrace();
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

    private void opeOtpActivity(String email, String userId) {
        isShowPassPhrase = true;
        Intent intent = new Intent(getActivity(), OTPActivity.class);
        App.editor.putString("email", email);
        App.editor.putString("switchUser", "no");
//        App.editor.putString(Constant.USER_ID, userId);
        App.editor.putString(Constant.USER_EMAIL, email);
        App.editor.putBoolean("showPassphrase", true);
        App.editor.apply();
        getActivity().startActivity(intent);
    }
}
