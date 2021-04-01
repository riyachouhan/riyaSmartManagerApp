package com.ddkcommunity.fragment.send;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.activities.SplashActivity;
import com.ddkcommunity.adapters.WalletAddressAdapter;
import com.ddkcommunity.model.Wallet_Response;
import com.ddkcommunity.App;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.Constant;
import com.google.gson.Gson;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmailFragment extends Fragment {
    Context context;
    TextView wallet_txt;
    RecyclerView wallet_list;
    RecyclerView.LayoutManager mLayoutManager;
    public EditText etEmailAddress;

    public EmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email, container, false);
        context = getActivity();

        wallet_list = view.findViewById(R.id.wallet_list);
        wallet_txt = view.findViewById(R.id.wallet_txt);
        etEmailAddress = view.findViewById(R.id.etEmailAddress);

        wallet_txt.setVisibility(View.GONE);
//        email_phone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if(!AppConfig.isEmail(email_phone.getText().toString())){
//                    wallet_txt.setVisibility(View.GONE);
//                    wallet_list.setVisibility(View.GONE);
//                }
//            }
//        });


        view.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String etEmailAddress1 = etEmailAddress.getText().toString();
                if (!AppConfig.isStringNullOrBlank(etEmailAddress1)) {
                    try {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    if (AppConfig.isEmail(etEmailAddress1)) {
                        String type = "email";
                        walletListCall(type, etEmailAddress1);
                    } else {
                        AppConfig.showToast("Please Enter valid Email.");
                    }

                } else {
                    AppConfig.showToast("Please Enter Email.");
                }

            }
        });
        return view;
    }

    private void walletListCall(final String Viatype, String emailphone) {
        final ProgressDialog dialog = new ProgressDialog(context);
        AppConfig.showLoading(dialog, "please wait...");

        Call<ResponseBody> call = AppConfig.getLoadInterface().walletList(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), AppConfig.setRequestBody(Viatype),
                AppConfig.setRequestBody(emailphone));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String resStr = response.body().string();
                        JSONObject object = new JSONObject(resStr);
                        if (object.getInt("status") == 1) {
                            Wallet_Response response1 = new Gson().fromJson(resStr, Wallet_Response.class);
                            wallet_txt.setVisibility(View.VISIBLE);
                            wallet_list.setVisibility(View.VISIBLE);
                            WalletAddressAdapter adapterWallet = new WalletAddressAdapter(context, response1.getData());
                            mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                            wallet_list.setLayoutManager(mLayoutManager);
                            wallet_list.setAdapter(adapterWallet);
                            adapterWallet.notifyDataSetChanged();
                        } else if (object.getInt(Constant.STATUS) == 3) {
                            AppConfig.showToast(object.getString("msg"));
                            AppConfig.openSplashActivity(getActivity());
                        } else {
                            AppConfig.showToast(object.getString("msg"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    AppConfig.showToast(response.message());
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
    @Override
    public void onResume() {
        super.onResume();

        MainActivity.setTitle("Send DDK");
        MainActivity.enableBackViews(true);
    }
}
