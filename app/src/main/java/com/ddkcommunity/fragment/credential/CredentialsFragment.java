package com.ddkcommunity.fragment.credential;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.activities.SplashActivity;
import com.ddkcommunity.adapters.CredentialAdapter;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.credential.CredentialsResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

/**
 * A simple {@link Fragment} subclass.
 */
public class CredentialsFragment extends Fragment {
    private static List<Credential> eventList = new ArrayList<>();
    EditText searchEt;
    private RecyclerView recyclerView;
    private CredentialAdapter mAdapter;
    private Context mContext;
    public static ArrayList<Credential> credential_list;

    public CredentialsFragment() {
        // Required empty public constructor
    }

    public static void deleteEvent(String eventId) {

        for (int i = 0; i < eventList.size(); i++) {
            if (eventId.equals(eventList.get(i).getCredentialId())) {
                eventList.remove(i);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credential, container, false);
        mContext = getActivity();
        eventList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter = new CredentialAdapter(eventList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

//        TextView title = view.findViewById(R.id.title_TV);
//        title.setText("Credentials");
        FloatingActionButton add = view.findViewById(R.id.btnAddCredential);
//        add.setText("Add Credentials");

        searchEt = view.findViewById(R.id.search_ET);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(searchEt.getText().toString());
            }
        });
        view.findViewById(R.id.btnAddCredential).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.addFragment(new AddCredentialsFragment(), true);
            }
        });
        return view;
    }

    private void filter(String newText) {
        if (eventList.size() > 0) {
            List<Credential> doctorNew = new ArrayList<>();

            if (newText.isEmpty()) {
                doctorNew.addAll(eventList);
            } else {
                for (Credential event : eventList) {
                    if (event.getName().toLowerCase().contains(newText.toLowerCase()) ||
                            event.getDdkcode().toLowerCase().contains(newText.toLowerCase()) ||
                            event.getPassphrase().toLowerCase().contains(newText.toLowerCase())) {
                        doctorNew.add(event);
                    }
                }

                if (doctorNew.size() == 0) {
                    AppConfig.showToast("No search data Found.");
                }
            }
            mAdapter.updateData(doctorNew);
        }
    }

    private void credentialListCall() {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            AppConfig.showLoading(dialog, "Fetching Credentials..");
            HashMap<String, String> hm = new HashMap<>();
            hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
            hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
            Call<ResponseBody> call = AppConfig.getLoadInterface().credentialList(AppConfig.getStringPreferences(mContext, Constant.JWTToken),hm);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {
                                CredentialsResponse registerResponse = new Gson().fromJson(responseData, CredentialsResponse.class);
                                eventList = registerResponse.getCredentials();
                                mAdapter.updateData(registerResponse.getCredentials());

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
                        ShowApiError(getActivity(),"server error in TransferApi/get-credentials");
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

    private void credentialSecondListCall()
    {
        if (AppConfig.isInternetOn())
        {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            AppConfig.showLoading(dialog, "Fetching Credentials..");
            Call<ResponseBody> call = AppConfig.getLoadInterface().credentialListForFragment(AppConfig.getStringPreferences(mContext, Constant.JWTToken));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            Log.d("credetiallist = ",responseData);
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {
                                CredentialsResponse registerResponse = new Gson().fromJson(responseData, CredentialsResponse.class);
                                credential_list=new ArrayList<>();
                                credential_list.addAll(registerResponse.getCredentials());

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ShowApiError(getActivity(),"server error in TransferApi/get-credentials");
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
        credentialSecondListCall();
        credentialListCall();
        MainActivity.setTitle("Credentials");
        MainActivity.enableBackViews(true);
    }

}
