package com.ddkcommunity.fragment.projects;


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

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.CredentialListAdapter;
import com.ddkcommunity.fragment.HomeFragment;
import com.ddkcommunity.interfaces.GetPoolingResponse;
import com.ddkcommunity.model.OtpResponse;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.dataPutMethods;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ncorti.slidetoact.SlideToActView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class PayUsingSamKoinFragment extends Fragment implements View.OnClickListener
{

    public PayUsingSamKoinFragment()
    {
    }

    private String otp = "";
    String totalincrypto;
    private TextView tvConversion, tvRate, tvEsText, tvEstimatedFees, tvTotal;
    private EditText etDDK;
    String userEnterDDk="",con_amount_usd="";
    private BigDecimal total, tempDDKAmountWithFees, sellTemp, buyTemp;
    private Context mContext;
    private TextView tvTransactionId, tvOrderStatus, userAvailable, btnVerify;
    private TextView btnGoHome;
    private AppCompatImageView btnCopyTransactionId;
    private ImageView ivTransactionCreateCheck, ivTransactionCreateUncheck, ivOrderStatusIconUnCheck, ivOrderStatusIconCheck;
    private FrameLayout progress_bar;
    private ClipboardManager clipboard;
    private ClipData clip;
    TextView minimumamountview,tvAvailableDDK,tvDDKsecrate;
    SlideToActView slide_custom_icon;
    private LinearLayout lytCreditView, lytOtp;
    private EditText otp_edt1, otp_edt2, otp_edt3, otp_edt4;
    private StringBuffer str_top;
    private String ddkSecret = "";
    private String currentWalletBalance;
    RelativeLayout ddk_addressview;
    private BigDecimal totalCurrencyAmount;
    private BigDecimal totalTransFee;
    View view;
    String pendingteansactionamont="0";
    BigDecimal roundhaldv;
    TextView toatsmsg;
    String subscriptionamount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pay_using_samkoin, container, false);
        mContext = getActivity();
        //get all view ids
        getAllViewId();
        //set minimum subscription amount
        subscriptionamount=AppConfig.getStringPreferences(mContext, Constant.MINIMUM_SUBSCRIPTION);
        minimumamountview.setText(subscriptionamount+" USDT Min.");
        //set aviable sab koin balance
        String curkj=App.pref.getString(Constant.SAMKOIN_Balance, "");
        if(curkj!=null && !curkj.equalsIgnoreCase(null) && !App.pref.getString(Constant.SAMKOIN_Balance, "").toString().equalsIgnoreCase(""))
        {
            if(App.pref.getString(Constant.SAMKOIN_Balance, "").length()!=0)
            {
                BigDecimal currentbalance=new BigDecimal(App.pref.getString(Constant.SAMKOIN_Balance, ""));
                currentWalletBalance=ReplacecommaValue(currentbalance+"");
                roundhaldv=currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                userAvailable.setText("SAM Koin : "+roundhaldv.toPlainString()+"");
                currentWalletBalance=ReplacecommaValue(currentbalance+"");
            }
        }else
        {
            userAvailable.setText("SAM Koin : "+"0.00000000");
        }
        //for otp verifiaction view ..
        otpveirifcationView();
        //view click event
        performClickEvent();
        setBuySell();
        getTransactionStatus("USDT");
        return view;
    }

    public void performClickEvent()
    {
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
                            BigDecimal etDDKValue = new BigDecimal(etDDK.getText().toString());
                            if (etDDKValue.compareTo(BigDecimal.ZERO) == 0)
                            {

                            } else {
                                Log.d("tes","::");
                                if (etDDK.getText().toString().equalsIgnoreCase(".")) {

                                } else {
                                    total = etDDKValue.multiply(sellTemp);
                                    slide_custom_icon.setLocked(true);
                                    toatsmsg.setVisibility(View.VISIBLE);
                                    //userEnterDDk = etDDKValue + "";
                                    getCurrencyConvertRate("" + total, "USDT", "SAM");
                                }
                            }
                        }catch(NumberFormatException ex){ // handle your exception
                            Toast.makeText(getActivity(),ex.toString(),Toast.LENGTH_SHORT).show();
                            Log.d("error",ex.toString());
                        }

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
            public void onSlideComplete(SlideToActView slideToActView)
            {
                String currentpref=currentWalletBalance;
                if(currentpref.equalsIgnoreCase(""))
                {
                    currentpref="0.0";
                }
                String ddkenter=etDDK.getText().toString().trim();
                if(ddkenter.equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please Enter USDT amount", Toast.LENGTH_SHORT).show();
                    slideToActView.resetSlider();
                    return;
                }else
                {
                    String converionvalue=tvConversion.getText().toString();
                    BigDecimal amouncheck=new BigDecimal(converionvalue);
                    if (amouncheck.compareTo(roundhaldv) == 1) {
                        Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                        slideToActView.resetSlider();
                        return;
                    }
                    //1 means for getter then to enter amount, 0 means equals, and else means less then.
                    BigDecimal conversionvalue=new BigDecimal(ddkenter);
                    BigDecimal subscriptionCompare=new BigDecimal(subscriptionamount);
                    if (subscriptionCompare.compareTo(conversionvalue) == 1)
                    {
                        Toast.makeText(mContext, "Please enter USDT Amount equal to or greater than " + subscriptionamount + " USDT amount", Toast.LENGTH_SHORT).show();
                        slideToActView.resetSlider();
                        return;
                    }
                    sendOtp();
                    slideToActView.resetSlider();
                }
            }
        });
        btnVerify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnVerify:
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
                break;
        }
    }

    public void getAllViewId()
    {
        ddk_addressview=view.findViewById(R.id.ddk_addressview);
        userAvailable=view.findViewById(R.id.userAvailable);
        toatsmsg=view.findViewById(R.id.toatsmsg);
        slide_custom_icon=view.findViewById(R.id.slide_custom_icon);
        minimumamountview=view.findViewById(R.id.minimumamountview);
        etDDK = view.findViewById(R.id.etDDK);
        tvConversion = view.findViewById(R.id.tvConversion);
        tvRate = view.findViewById(R.id.tvRate);
        tvEsText = view.findViewById(R.id.tvEsText);
        tvDDKsecrate=view.findViewById(R.id.tvDDKsecrate);
        tvAvailableDDK=view.findViewById(R.id.tvAvailableDDK);
        tvEstimatedFees = view.findViewById(R.id.tvEstimatedFees);
        tvTotal = view.findViewById(R.id.tvTotal);
        lytCreditView = view.findViewById(R.id.lytCreditView);
        btnVerify = view.findViewById(R.id.btnVerify);
        lytOtp = view.findViewById(R.id.lytOtp);
        otp_edt1 = view.findViewById(R.id.otp_edt11);
        otp_edt2 = view.findViewById(R.id.otp_edt12);
        otp_edt3 = view.findViewById(R.id.otp_edt13);
        otp_edt4 = view.findViewById(R.id.otp_edt14);
    }

    //for otp verification view
    public void otpveirifcationView()
    {
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
    }

    //for validation
    private void getTransactionStatus(String typestatus)
    {
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Please wait.......");
        pd.show();

        String walletid=App.pref.getString(Constant.USDT_Add_Id, "");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("wallet_id", walletid);
        hm.put("device_type", "android");
        hm.put("device_token", typestatus);

        AppConfig.getLoadInterface().checkPaymentStatus(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               pd.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if(jsonObject.getString("status").equalsIgnoreCase("1"))
                        {
                            pendingteansactionamont=jsonObject.getString("balance");

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    AppConfig.showToast("Server error");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                AppConfig.showToast("Server error");
            }
        });
    }
    //.................

    private void getCurrencyConvertRate(final String amount, String from_currency, final String to_currency)
    {
        String transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver_USDT);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("amount", amount);
        hm.put("from_currency", from_currency);
        hm.put("to_currency", to_currency);
        hm.put("receiver_address",transactionreiver);
        hm.put("crypto_type", to_currency);
        AppConfig.getLoadInterface().SamConvertPrice(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful())
                {
                    try {
                        toatsmsg.setVisibility(View.INVISIBLE);
                        slide_custom_icon.setLocked(false);
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if(jsonObject.getString("status").equalsIgnoreCase("1"))
                        {
                            String convert_price=jsonObject.getString("convert_price");
                            String fees=jsonObject.getString("fees");
                            BigDecimal currencyTransactionPercent = new BigDecimal(fees);
                            BigDecimal resultData = new BigDecimal(convert_price);
                            BigDecimal roundtransactionfees=currencyTransactionPercent.setScale(8, BigDecimal.ROUND_HALF_UP);
                            tvEstimatedFees.setText("-"+String.format(Locale.ENGLISH, "%.4f", roundtransactionfees));
                            tvConversion.setText(String.format(Locale.ENGLISH, "%.4f",resultData));
                            BigDecimal totalamount=resultData.subtract(roundtransactionfees);
                            tvTotal.setText("" + String.format(Locale.ENGLISH, "%.4f", totalamount));

                            //con_amount_usd = tvConversion.getText().toString();
                            //for total amount
                            //totalincrypto=totalamount+"";
                            //totalTransFee=currencyTransactionPercent;
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    AppConfig.showToast("Server error");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppConfig.showToast("Server error");
            }
        });
    }

    private void sendDataOnServer()
    {
        AppConfig.showLoading("Send Data..", mContext);
        String totalincryptowithusd=tvTotal.getText().toString();
        String userselectddkaddres=""/*tvSelectDdkAddress.getText().toString().trim()*/;
        String estimatedfees=tvEstimatedFees.getText().toString().trim();
        UserModel.getInstance().sendDataToServer(estimatedfees,userselectddkaddres,"USDT","","DDK",totalincrypto,totalincryptowithusd,con_amount_usd,userEnterDDk,mContext, "", "manual", "" + sellTemp, total.toString(),
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

    private void setBuySell() {
        buyTemp = UserModel.getInstance().samkoinBuyPrice;
        sellTemp = UserModel.getInstance().samkoinSellPrice;
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
        tvTransactionId.setText(transactionId);
        ivTransactionCreateCheck.setVisibility(View.VISIBLE);
        ivTransactionCreateUncheck.setVisibility(View.GONE);
        btnGoHome.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.GONE);
        tvOrderStatus.setText("Pending");
        ivOrderStatusIconCheck.setVisibility(View.VISIBLE);
        ivOrderStatusIconUnCheck.setVisibility(View.GONE);
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
                Log.d("resp","::"+response.body().data);
                if (response.isSuccessful() && response.body().status == 1) {
                    Log.d("otp",otp);
                    otp = response.body().data;
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
         MainActivity.setTitle("Using SAM Koin");
        MainActivity.enableBackViews(true);
    }

}
