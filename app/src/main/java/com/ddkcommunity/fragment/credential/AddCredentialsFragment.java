package com.ddkcommunity.fragment.credential;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
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
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCredentialsFragment extends Fragment {
    EditText nameET, ddkET, passPhaseET, passPhase2ET, referalET, notesET;

    public AddCredentialsFragment() {
    }

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_credentials, container, false);
        mContext = getActivity();
        nameET = view.findViewById(R.id.name_ET);
        ddkET = view.findViewById(R.id.ddk_ET);
        passPhaseET = view.findViewById(R.id.pass_phase_ET);
        passPhase2ET = view.findViewById(R.id.pass_phase2_ET);
        referalET = view.findViewById(R.id.referal_Link_ET);
        notesET = view.findViewById(R.id.notes_ET);


        view.findViewById(R.id.add_TV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString();
                String ddk = ddkET.getText().toString();
                String pass = passPhaseET.getText().toString();
                String pass2 = passPhase2ET.getText().toString();
                String referal = referalET.getText().toString();
                String notes = notesET.getText().toString();


                if (!AppConfig.isStringNullOrBlank(name)) {
                    if (!AppConfig.isStringNullOrBlank(ddk)) {
                        if (!AppConfig.isStringNullOrBlank(pass)) {
//                            importwalletCall(App.pref.getString(Constant.USER_ID, ""), pass, ddk, referal, name, pass2, notes);

                            addCredentialCall(name, ddk, referal, pass, pass2, notes);

                        } else {
                            AppConfig.showToast("First PassPhase Required.");
                        }
                    } else {
                        AppConfig.showToast("DDK Address Required.");
                    }
                } else {
                    AppConfig.showToast("Name Required.");
                }
            }
        });
        return view;
    }

//    private void importwalletCall(final String userId, final String secret, final String ddk, final String referal, final String name, final String pass2, final String notes) {
//        final ProgressDialog dialog = new ProgressDialog(getActivity());
//        AppConfig.showLoading(dialog, "Importing");
//        Call<ResponseBody> call = AppConfig.getLoadInterface().importWalletCall(
//                AppConfig.getStringPreferences(mContext, Constant.JWTToken),
//                AppConfig.setRequestBody(secret),
//                AppConfig.setRequestBody(App.pref.getString(Constant.USER_NAME, "")),
//                AppConfig.setRequestBody("android"),
//                AppConfig.setRequestBody(App.RegPref.getString(Constant.FIREBASE_TOKEN, "")));
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    try {
//                        String responseData = response.body().string();
//                        JSONObject object = new JSONObject(responseData);
//                        if (object.getInt(Constant.STATUS) == 1) {
//                            JSONObject object1 = new JSONObject(object.getString("data"));
//                            JSONObject object2 = new JSONObject(object1.getString("account"));
//
//                            if (ddk.equals(object2.getString("address"))) {
//                                addCredentialCall(App.pref.getString(Constant.USER_ID, ""), name, ddk, referal, secret, pass2, notes);
//                            } else {
//                                AppConfig.showToast("Invalid DDK Address.");
//                            }
//
//                            AppConfig.showToast(object.getString("msg"));
//                        } else if (object.getInt(Constant.STATUS) == 3) {
//                            AppConfig.showToast(object.getString("msg"));
//                            AppConfig.openSplashActivity(getActivity());
//                        } else {
//                            AppConfig.showToast(object.getString("msg"));
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JsonSyntaxException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    AppConfig.showToast("Something went wrong");
//                }
//                AppConfig.hideLoading(dialog);
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                AppConfig.hideLoading(dialog);
//                t.printStackTrace();
//            }
//        });
//    }

    private void addCredentialCall(String name, String ddk, String referal, String pass, String pass2, String notes) {
        if (AppConfig.isInternetOn()) {
            hideKeyBoard();
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            AppConfig.showLoading(dialog, "Adding Credential..");

            Call<ResponseBody> call = AppConfig.getLoadInterface().addCredential(AppConfig.getStringPreferences(mContext, Constant.JWTToken),
                    AppConfig.setRequestBody(name),
                    AppConfig.setRequestBody(ddk),
                    AppConfig.setRequestBody(referal),
                    AppConfig.setRequestBody(pass),
                    AppConfig.setRequestBody(pass2),
                    AppConfig.setRequestBody(notes)
            );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {
                                AppConfig.showToast(object.getString("msg"));
                                getActivity().onBackPressed();
                            }else if (object.getInt(Constant.STATUS) == 4)
                            {
                                ShowServerPost((Activity)mContext,"ddk server error add crediential");
                            } else if (object.getInt(Constant.STATUS) == 3) {
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
                        ShowApiError(getActivity(),"server error in thirdFace/add-credentials");
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
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Add Credential");
        MainActivity.enableBackViews(true);
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
