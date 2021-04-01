package com.ddkcommunity.fragment.projects;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.CredentialListAdapter;
import com.ddkcommunity.interfaces.GetAvailableValue;
import com.ddkcommunity.interfaces.GetPoolingResponse;
import com.ddkcommunity.model.OtpResponse;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.credential.CredentialsResponse;
import com.ddkcommunity.model.wallet.WalletResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ncorti.slidetoact.SlideToActView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.ddkcommunity.utilies.dataPutMethods.ReplacecommaValue;
import static com.ddkcommunity.utilies.dataPutMethods.ShowServerPost;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayUsingDDKFragment extends Fragment {

    private String otp = "";

    public PayUsingDDKFragment() {

    }

    private TextView btnGoBack, tvEsTextCoin, tvDDKFee,
            tvConversion, tvRate, tvEsText, tvEstimatedFees, tvTotal;
    private EditText etDDK;
    String userEnterDDk="",con_amount_usd="";
    private BigDecimal total, tempDDKAmountWithFees, sellTemp, buyTemp;
    float wallet = 0, rate = 0, ddk = 0;
    private Context mContext;
    private TextView tvTransactionId, tvOrderStatus, userAvailable, btnVerify;
    private TextView btnGoHome;
    private AppCompatImageView btnCopyTransactionId;
    private ImageView ivTransactionCreateCheck, ivTransactionCreateUncheck, ivOrderStatusIconUnCheck, ivOrderStatusIconCheck;
    private FrameLayout progress_bar;
    private ClipboardManager clipboard;
    private ClipData clip;
    TextView minimumamountview,tvSelectDdkAddress,tvAvailableDDK;
    SlideToActView slide_custom_icon;
    private LinearLayout lytCreditView, lytOtp;
    private EditText otp_edt1, otp_edt2, otp_edt3, otp_edt4;
    private StringBuffer str_top;
    private BottomSheetDialog dialog;
    private CredentialListAdapter adapterCredential;
    private List<Credential> credentialList = new ArrayList<>();
    private String ddkSecret = "";
    private String currentWalletBalance;
    RelativeLayout ddk_addressview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_using_ddk, container, false);
        mContext = getActivity();
        ddk_addressview=view.findViewById(R.id.ddk_addressview);
        userAvailable=view.findViewById(R.id.userAvailable);
        tvSelectDdkAddress=view.findViewById(R.id.tvSelectDdkAddress);
        slide_custom_icon=view.findViewById(R.id.slide_custom_icon);
        minimumamountview=view.findViewById(R.id.minimumamountview);
        etDDK = view.findViewById(R.id.etDDK);
        tvConversion = view.findViewById(R.id.tvConversion);
        tvRate = view.findViewById(R.id.tvRate);
        tvEsText = view.findViewById(R.id.tvEsText);
        tvEstimatedFees = view.findViewById(R.id.tvEstimatedFees);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvDDKFee = view.findViewById(R.id.tvDDKFee);
        String subscriptionamount=AppConfig.getStringPreferences(mContext, Constant.MINIMUM_SUBSCRIPTION);
        minimumamountview.setText(subscriptionamount+" USDT Min.");
        lytCreditView = view.findViewById(R.id.lytCreditView);
        btnVerify = view.findViewById(R.id.btnVerify);
        lytOtp = view.findViewById(R.id.lytOtp);
        otp_edt1 = view.findViewById(R.id.otp_edt11);
        otp_edt2 = view.findViewById(R.id.otp_edt12);
        otp_edt3 = view.findViewById(R.id.otp_edt13);
        otp_edt4 = view.findViewById(R.id.otp_edt14);
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

        tvSelectDdkAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (credentialList.size() > 0) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    } else {
                        dialog.dismiss();
                    }
                }
            }
        });
        etDDK.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!AppConfig.isStringNullOrBlank(etDDK.getText().toString())) {

                    if(etDDK.getText().toString().equalsIgnoreCase("."))
                    {

                    }else {
                        try {
                            ddk = Float.parseFloat(etDDK.getText().toString().trim());
                        } catch (NumberFormatException e) {
                            ddk = (float) 0.0;
                        }
                        //for new
                        BigDecimal etDDKValue = new BigDecimal(etDDK.getText().toString());
                        BigDecimal ddkestimate = new BigDecimal("0.0001");
                        BigDecimal tempDDKFees = etDDKValue.multiply(ddkestimate);
                        //for conversion
                        BigDecimal conversionrate = etDDKValue.subtract(tempDDKFees);
                        BigDecimal finalconversion = conversionrate.multiply(sellTemp);
                        BigDecimal roundhaldv = finalconversion.setScale(4, BigDecimal.ROUND_HALF_UP);
                        BigDecimal estimatedfee = new BigDecimal(0);
                       // tvEstimatedFees.setText(estimatedfee.toPlainString() + "");
                        tvConversion.setText(roundhaldv + "");
                        //for total
                        BigDecimal totalfirst = conversionrate.subtract(estimatedfee);
                        BigDecimal finaltotal = totalfirst.multiply(sellTemp);
                        total = finaltotal.setScale(4, BigDecimal.ROUND_HALF_UP);
                        //for ddk fees
                        tempDDKAmountWithFees = tempDDKFees.add(etDDKValue);
                        tvDDKFee.setVisibility(View.GONE);
                        // tvDDKFee.setText("DDK Fee: " + tempDDKFees.toPlainString());
                        tvEstimatedFees.setText(""+ tempDDKFees.toPlainString());
                        if (ddk > 0) {
                            tvTotal.setText(String.format(Locale.ENGLISH, "%.4f", total));
                        } else {
                            //    AppConfig.showToast("Please enter amount greater than 0.");
                        }
                        userEnterDDk = etDDKValue + "";
                        con_amount_usd = tvConversion.getText().toString();
                        //........
                    }
                } else {
                    tvEstimatedFees.setText("0");
                    tvConversion.setText("0");
                    tvTotal.setText("0");
                    total = new BigDecimal(0);
                }
            }
        });


        slide_custom_icon.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {

                String ddkaddressn=tvSelectDdkAddress.getText().toString().trim();
                if(ddkaddressn.equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please Select DDK Address", Toast.LENGTH_SHORT).show();
                    slideToActView.resetSlider();
                }else{
                String currentpref=currentWalletBalance;
                if(currentpref.equalsIgnoreCase(""))
                {
                    currentpref="0.0";
                }
                String subscriptionamount=AppConfig.getStringPreferences(mContext, Constant.MINIMUM_SUBSCRIPTION);
                BigDecimal threeHundred = new BigDecimal(subscriptionamount);
                BigDecimal walletBalance = new BigDecimal(currentpref);
                String ddkenter=etDDK.getText().toString().trim();
                if(ddkenter.equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please enter DDK amount", Toast.LENGTH_SHORT).show();
                    slideToActView.resetSlider();
                    return;
                }else {
                    if (tempDDKAmountWithFees.compareTo(walletBalance) == 1) {
                        Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                        slideToActView.resetSlider();
                        return;
                    }
                    String totalamountph=tvTotal.getText().toString();
                    //1 means for getter then to enter amount, 0 means equals, and else means less then.
                    if (threeHundred.compareTo(new BigDecimal(totalamountph)) == 1) {
                        Toast.makeText(mContext, "Please enter DDK Amount equal to " + subscriptionamount + " USDT amount", Toast.LENGTH_SHORT).show();
                        slideToActView.resetSlider();
                        return;
                    }
                   sendOtp();
                    slideToActView.resetSlider();
                }
            }
           }
        });

        view.findViewById(R.id.btnGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_top = new StringBuffer();
                str_top.append(otp_edt1.getText().toString()).append(otp_edt2.getText().toString()).append(otp_edt3.getText().toString()).append(otp_edt4.getText().toString());
                if (str_top.length() > 0 && str_top.length() == 4) {
                    if (str_top.toString().equalsIgnoreCase(otp)) {
                        hideKeyBoard();
                        sendDataOnServer();
                    } else {
                        AppConfig.showToast("Otp is expired or incorrect");
                    }
                }
            }
        });
        initTheCredentialView();
        latestDDKPrice();
        getCredentialsCall();
        return view;
    }

    //for different ddk value
    //for cashoutnew
    private void getCredentialsCall() {
        if (AppConfig.isInternetOn())
        {
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
                                credentialList = new ArrayList<>();
                                CredentialsResponse registerResponse = new Gson().fromJson(responseData, CredentialsResponse.class);
                                for (Credential credential : registerResponse.getCredentials()) {
                                    if (credential.getStatus().equalsIgnoreCase("active")) {
                                        credentialList.add(credential);
                                    }
                                }
                                adapterCredential.updateData(credentialList);
                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity(getActivity());
                            }else if (object.getInt(Constant.STATUS) == 4)
                            {
                                ShowServerPost((Activity)mContext,"ddk server error crediental list");
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
                        AppConfig.showToast("Server error");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    private void initTheCredentialView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View dialogView = layoutInflater.inflate(R.layout.popup_wallet_pooling, null);
//        pw = new PopupWindow(view1, RelativeLayout.LayoutParams.MATCH_PARENT, 650, true);
        dialog = new BottomSheetDialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(dialogView);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        final EditText searchEt = dialogView.findViewById(R.id.search_ET);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapterCredential = new CredentialListAdapter(credentialList, mContext, searchEt, new CredentialListAdapter.SetOnItemClickListener() {
            @Override
            public void onItemClick(String wallet_code, String walletId1, String passPhrese) {
                tvSelectDdkAddress.setText(wallet_code);
                ddkSecret = passPhrese;
                getWalletBalance(walletId1);
                hideKeyBoard();
                dialog.dismiss();
            }
        });

        recyclerView.setAdapter(adapterCredential);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchEt.getText().toString().length() > 0) {
                    filter(searchEt.getText().toString());
                } else {
                    adapterCredential.updateData(credentialList);
                }
            }
        });
    }

    private void filter(String newText) {
    }

    //......................
    private void getWalletBalance(String walletId) {
        UserModel.getInstance().getWalletDetails(0,walletId, mContext, new GetAvailableValue() {
            @Override
            public void getValues(String ddk, WalletResponse successResponse) {
                try {
                    currentWalletBalance = ddk;
                    currentWalletBalance=ReplacecommaValue(currentWalletBalance);
                    userAvailable.setText("Available DDk: " + currentWalletBalance);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendDataOnServer()
    {
        AppConfig.showLoading("Send Data..", mContext);
        String totalincrypto=tvTotal.getText().toString();
        String userselectddkaddres=tvSelectDdkAddress.getText().toString().trim();
        String estimatedfees=tvEstimatedFees.getText().toString().trim();
        UserModel.getInstance().sendDataToServer(estimatedfees,userselectddkaddres,"DDK","","DDK",userEnterDDk,totalincrypto,con_amount_usd,userEnterDDk,mContext, "", "manual", "" + sellTemp, total.toString(),
                "", ddkSecret, "", "", "", "", new GetPoolingResponse() {
                    @Override
                    public void getResponse(JSONObject jsonObject) {
                        AppConfig.hideLoader();
                        try {
                            if (jsonObject.getInt(Constant.STATUS) == 1) {
                                MainActivity.isLocalManualTrans = true;
                                try {
                                    final JSONObject dataObject = jsonObject.getJSONObject("data");
                                    transactionStatus(dataObject.getString("txt_id"), dataObject.getString("lender_id"));
                                } catch (JSONException e) {
                                    AppConfig.showToast("Server error");
                                    e.printStackTrace();
                                }
                            } else if (jsonObject.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(jsonObject.getString("msg"));
                                AppConfig.openSplashActivity(getActivity());
                            } else if (jsonObject.getInt(Constant.STATUS) == 0) {
                                AppConfig.hideLoader();
                                AppConfig.showToast(jsonObject.getString("msg"));
                            } else {
                                AppConfig.hideLoader();
                                AppConfig.showToast("Data not send Please try again");
                            }

                        } catch (JSONException e) {
                            AppConfig.hideLoader();
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void latestDDKPrice() {
        buyTemp = UserModel.getInstance().ddkBuyPrice;
        sellTemp = UserModel.getInstance().ddkSellPrice;
        tvRate.setText(String.format(Locale.ENGLISH, "%.4f", sellTemp)+" USDT");
    }

    private void transactionStatus(String transactionId, String lenderId) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_transaction_status, null);
        btnGoHome = (TextView) customView.findViewById(R.id.btnGoHome);
        tvOrderStatus = customView.findViewById(R.id.tvOrderStatus);
        tvTransactionId = customView.findViewById(R.id.tvTransactionId);
        btnCopyTransactionId = customView.findViewById(R.id.btnCopyTransactionId);
        ivTransactionCreateCheck = customView.findViewById(R.id.ivTransactionCreateCheck);
        ivTransactionCreateUncheck = customView.findViewById(R.id.ivTransactionCreateUncheck);
        ivOrderStatusIconUnCheck = customView.findViewById(R.id.ivOrderStatusIconUnCheck);
        ivOrderStatusIconCheck = customView.findViewById(R.id.ivOrderStatusIconCheck);

        progress_bar = customView.findViewById(R.id.progress_bar);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(customView);

        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),MainActivity.class);
                startActivity(i);
            }
        });

        btnCopyTransactionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                clip = ClipData.newPlainText("Copy", tvTransactionId.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
                AppConfig.showToast("Copied");
            }
        });
        checkTransactionStatus(transactionId, lenderId);
    }

    private int count = 0;

    private void checkTransactionStatus(final String txt_id, final String lender_id) {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        if (count == 0) {
            AppConfig.showLoading(dialog, "Checking transaction status...");
        }
        HashMap<String, String> hm = new HashMap<>();
        hm.put("txt_id", txt_id);
        hm.put("lender_id", lender_id);

        AppConfig.getLoadInterface().checkDDKTransactionStatus(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        tvTransactionId.setText(txt_id);
                        ivTransactionCreateCheck.setVisibility(View.VISIBLE);
                        ivTransactionCreateUncheck.setVisibility(View.GONE);
                        if (jsonObject.getInt("status") == 1) {
                            btnGoHome.setVisibility(View.VISIBLE);
                            progress_bar.setVisibility(View.GONE);
                            AppConfig.showToast(jsonObject.getString("msg"));
                            tvOrderStatus.setText("Confirmed");
                            ivOrderStatusIconCheck.setVisibility(View.VISIBLE);
                            ivOrderStatusIconUnCheck.setVisibility(View.GONE);
                        } else {
                            if (count == 22) {
                                btnGoHome.setVisibility(View.VISIBLE);
                                progress_bar.setVisibility(View.GONE);
                                ivOrderStatusIconCheck.setVisibility(View.VISIBLE);
                                ivOrderStatusIconUnCheck.setVisibility(View.GONE);
                                tvOrderStatus.setText("Failed");
                                ivOrderStatusIconCheck.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_failed_transaction));
                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        count++;
                                        checkTransactionStatus(txt_id, lender_id);
                                    }
                                }, 8000);
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                    }
                } else {
                    dialog.dismiss();
                    AppConfig.showToast("Server error");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                AppConfig.showToast("Server error");
            }
        });
    }

    private void sendOtp() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("email", App.pref.getString(Constant.USER_EMAIL, ""));
        hm.put("name", App.pref.getString(Constant.USER_NAME, ""));

        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Sending otp....");

        AppConfig.getLoadInterface().postOtp(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body().status == 1) {
                    otp = response.body().data;
                    Log.d("otp",otp);
                    ddk_addressview.setVisibility(View.GONE);
                    lytCreditView.setVisibility(View.GONE);
                    lytOtp.setVisibility(View.VISIBLE);
                } else if (response.isSuccessful() && response.body() != null && response.body().status == 3) {
                    AppConfig.showToast(response.body().msg);
                    AppConfig.openSplashActivity(getActivity());
                } else if (response.isSuccessful() && response.body() != null && response.body().status == 0) {
                    AppConfig.showToast(response.body().msg);
                } else {
                    AppConfig.showToast("Server Error");
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }


    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        // HomeActivity.setHomeItem(getActivity(), R.id.profile);
        MainActivity.setTitle("Using DDK");
        MainActivity.enableBackViews(true);
    }
}
