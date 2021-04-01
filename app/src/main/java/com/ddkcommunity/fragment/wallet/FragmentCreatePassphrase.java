package com.ddkcommunity.fragment.wallet;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
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

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCreatePassphrase extends Fragment {

    public static final String TAG = FragmentCreatePassphrase.class.getSimpleName();
    private static FragmentManager fm1;
    private static TextView passphrase_txt;
    private static String ptext1;
    private static String user_id;
    private static String passphrase;

    Activity activity;
    AppCompatButton copy_passphrase, next_step;
    ClipboardManager clipboard;
    ClipData clip;
    Typeface type;
    private Context mContext;

    public FragmentCreatePassphrase() {
        // Required empty public constructor
    }

    public static Fragment getInstance(FragmentManager fm) {
        FragmentCreatePassphrase homeFragment = new FragmentCreatePassphrase();
        fm1 = fm;

        return homeFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_wallet1, container, false);
        activity = getActivity();
        mContext = getActivity();

        TextView generate_passphrase = view.findViewById(R.id.generate_passphrase);
        SpannableString content = new SpannableString("Generate New");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        generate_passphrase.setText(content);

        TextView pass = view.findViewById(R.id.pass);
        passphrase_txt = view.findViewById(R.id.passphrase_txt);

        type = Typeface.createFromAsset(activity.getAssets(), "fonts/Montserrat-Bold.ttf");
        pass.setTypeface(type);

        passphrase = App.pref.getString("PassPhrase", "");


        copy_passphrase = view.findViewById(R.id.copy_passphrase);
        next_step = view.findViewById(R.id.next_step);


        user_id = App.pref.getString(AppConfig.USER_ID, "");

        copy_passphrase.setEnabled(true);
        next_step.setEnabled(true);
        App.editor.putString("PassPhrase", "");
        App.editor.commit();

        createPassphraseCall();
        view.findViewById(R.id.generate_passphrase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppConfig.isInternetOn()) {
                    createPassphraseCall();
                } else {
                    AppConfig.showToast("Please connect to internet.");
                }
            }
        });

        view.findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(activity, SplashActivity.class);
                startActivity(i);
                activity.finish();
            }
        });


        view.findViewById(R.id.copy_passphrase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ptext1 = passphrase_txt.getText().toString();

                if (!AppConfig.isStringNullOrBlank(ptext1)/* && !UtilMethod.isStringNullOrBlank(ptext2) && !UtilMethod.isStringNullOrBlank(ptext3) && !UtilMethod.isStringNullOrBlank(ptext4) && !UtilMethod.isStringNullOrBlank(ptext5) && !UtilMethod.isStringNullOrBlank(ptext6) && !UtilMethod.isStringNullOrBlank(ptext7) && !UtilMethod.isStringNullOrBlank(ptext8) && !UtilMethod.isStringNullOrBlank(ptext9) && !UtilMethod.isStringNullOrBlank(ptext10) && !UtilMethod.isStringNullOrBlank(ptext11) && !UtilMethod.isStringNullOrBlank(ptext12)*/) {

                    clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
                    clip = ClipData.newPlainText("Copy", passphrase);
                    clipboard.setPrimaryClip(clip);
                    AppConfig.showToast("Passphrase copied.");

                } else {
                    AppConfig.showToast("Passphrase not copy.");
                }
            }
        });


        view.findViewById(R.id.next_step).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ptext1 = passphrase_txt.getText().toString();

                if (next_step.isEnabled()) {
                    if (!AppConfig.isStringNullOrBlank(ptext1) /*&& !UtilMethod.isStringNullOrBlank(ptext2) && !UtilMethod.isStringNullOrBlank(ptext3) && !UtilMethod.isStringNullOrBlank(ptext4) && !UtilMethod.isStringNullOrBlank(ptext5) && !UtilMethod.isStringNullOrBlank(ptext6) && !UtilMethod.isStringNullOrBlank(ptext7) && !UtilMethod.isStringNullOrBlank(ptext8) && !UtilMethod.isStringNullOrBlank(ptext9) && !UtilMethod.isStringNullOrBlank(ptext10) && !UtilMethod.isStringNullOrBlank(ptext11) && !UtilMethod.isStringNullOrBlank(ptext12)*/) {

                        if (clip != null) {
                            CreateWalletActivity.addFragment(FragmentConfrimPassphrase.getInstance(fm1, passphrase), true, "confirm");
                        } else {
                            AppConfig.showToast("Please copy Passphrase.");
                        }

                    } else {
                        AppConfig.showToast("Passphrase not proper.");
                    }
                } else {
                    AppConfig.showToast("Please copy Passphrase.");
                }
            }
        });

        return view;
    }


    private void createPassphraseCall() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog, "Create User Passphrase...");
        Call<ResponseBody> call = AppConfig.getLoadInterface().createSecret(
                AppConfig.getStringPreferences(mContext, Constant.JWTToken));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(AppConfig.STATUS) == 1) {
                           /* clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
                            clip = ClipData.newPlainText("Copy", "");
                            clipboard.setPrimaryClip(clip);
                            Log.e("COPY", "%%%%" + clip);*/
                            ptext1 = "";
                            passphrase = object.getString("data");

                            passphrase_txt.setText(passphrase);
                            passphrase_txt.setTypeface(type);

                        } else if (object.getInt(Constant.STATUS) == 3) {
                            AppConfig.showToast(object.getString("msg"));
                            AppConfig.openSplashActivity(getActivity());
                        } else {
                            AppConfig.showToast("Passphrase not generated, please click on generate new.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ShowApiError(mContext,"server error in create-secret");
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
