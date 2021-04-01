package com.ddkcommunity.fragment.wallet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.CreateWalletActivity;
import com.ddkcommunity.activities.SplashActivity;
import com.ddkcommunity.utilies.AppConfig;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.ShowServerPost;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentConfrimPassphrase extends Fragment {

    private static FragmentManager fm1;
    private static String type;
    private static String passphrase, email2, user_id, getPassphrase;
    private Context context;
    private EditText editText1, referralET;


    public FragmentConfrimPassphrase() {
        // Required empty public constructor
    }

    public static Fragment getInstance(FragmentManager fm, String passphrase1) {
        FragmentConfrimPassphrase homeFragment = new FragmentConfrimPassphrase();
        fm1 = fm;
        passphrase = passphrase1;
        email2 = App.pref.getString(Constant.USER_EMAIL, "");
        return homeFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confrim_passphrase, container, false);
        context = getActivity();
        user_id = App.pref.getString(AppConfig.USER_ID, "");


        type = "email";

        //lin1 = (LinearLayout) view.findViewById(R.id.linear_layout1);
        editText1 = view.findViewById(R.id.edit_text);
        referralET = view.findViewById(R.id.referral);


        view.findViewById(R.id.go_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();

            }
        });
        view.findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();

            }
        });


        view.findViewById(R.id.confrim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPassphrase = editText1.getText().toString().trim();

                if (!AppConfig.isStringNullOrBlank(getPassphrase)) {
                    if (passphrase.trim().equals(getPassphrase)) {
                        if (AppConfig.isInternetOn()) {
                            String referral = "";
                            referral = referralET.getText().toString().trim();
                            createWallet(user_id, email2, getPassphrase, type, referral);
                        } else {
                            AppConfig.showToast("Please connect to internet.");
                        }
                    } else {
                        AppConfig.showToast("Passphrase does not match.");
                    }
                } else {
                    AppConfig.showToast("Please paste your Passphrase.");
                }
            }
        });
        return view;
    }

    private void createWallet(final String userId, final String Email, final String Secret, final String type, final String referral) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog, "Fetching User Wallet...");
        Call<ResponseBody> call = AppConfig.getLoadInterface().createWalletCall(
                AppConfig.getStringPreferences(context, Constant.JWTToken),
                AppConfig.setRequestBody(Email),
                AppConfig.setRequestBody(Secret),
                AppConfig.setRequestBody(type),
                AppConfig.setRequestBody("android"),
                AppConfig.setRequestBody(App.RegPref.getString(Constant.FIREBASE_TOKEN, "")),
                AppConfig.setRequestBody(App.pref.getString(Constant.USER_NAME, "")),
                AppConfig.setRequestBody(""),
                AppConfig.setRequestBody(referral));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(AppConfig.STATUS) == 0) {
                            AppConfig.showToast(object.getString("msg"));
                            CreateWalletActivity.clearFragmentStack();
                            CreateWalletActivity.addFragment(FragmentSafeKeeping.getInstance(fm1, getPassphrase), true, "safekeep");
                        } else if (object.getInt(Constant.STATUS)== 4)
                        {
                            ShowServerPost(getActivity(),"ddk server error create-wallet");
                        }else if (object.getInt(Constant.STATUS) == 3) {
                            AppConfig.showToast(object.getString("msg"));
                            AppConfig.openSplashActivity(getActivity());
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
                    ShowApiError(getActivity(),"server error in create-wallet");

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


}
