package com.ddkcommunity.fragment.send;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
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
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
import com.ddkcommunity.fragment.HomeFragment;
import com.ddkcommunity.interfaces.GegtSettingStatusinterface;
import com.ddkcommunity.interfaces.GetAllCredential;
import com.ddkcommunity.interfaces.GetAvailableValue;
import com.ddkcommunity.model.OtpResponse;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.credential.CredentialsResponse;
import com.ddkcommunity.model.getSettingModel;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.model.wallet.WalletResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.CommonMethodFunction;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.ddkcommunity.fragment.ScanFragment.clickoption;
import static com.ddkcommunity.utilies.dataPutMethods.ReplacecommaValue;
import static com.ddkcommunity.utilies.dataPutMethods.ShowFunctionalityAlert;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayUsingScanFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private View rootView = null;
    private TextView slide_custom_icon;
    private UserResponse userData;
    String scanaddress;
    EditText etDDKCoins;
    String otp;
    String emailaddress,currentWalletBalance;
    TextView upperheading,tvbalance_samk,tvbalance_php,etWalletUserName,transactionfee;
    String clickoptionv;
    ImageView phpbalancevewi,sambalancevewi;
    LinearLayout mainviewtrancsa,samkoinmainlay,phpmainlay;
    BigDecimal totalamount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            View view = inflater.inflate(R.layout.fragmentscan_viewlayout, container, false);
            rootView = view;
            mContext = getActivity();
            samkoinmainlay=view.findViewById(R.id.samkoinmainlay);
            phpmainlay=view.findViewById(R.id.phpmainlay);
            mainviewtrancsa=view.findViewById(R.id.mainviewtrancsa);
            phpbalancevewi=view.findViewById(R.id.phpbalancevewi);
            sambalancevewi=view.findViewById(R.id.sambalancevewi);
            transactionfee=view.findViewById(R.id.transactionfee);
            tvbalance_samk=view.findViewById(R.id.tvbalance_samk);
            tvbalance_php=view.findViewById(R.id.tvbalance_php);
            upperheading=view.findViewById(R.id.upperheading);
            etDDKCoins=view.findViewById(R.id.etDDKCoins);
            try {
                if(getArguments().getString("address")!=null)
                {
                    scanaddress = getArguments().getString("address");
                }

                if(getArguments().getString("emailaddress")!=null)
                {
                    emailaddress=getArguments().getString("emailaddress");
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            slide_custom_icon=view.findViewById(R.id.slide_custom_icon);
            userData = AppConfig.getUserData(mContext);
            etWalletUserName=view.findViewById(R.id.etWalletUserName);
            clickoptionv=clickoption;
            //for php balance
            String phpcurrentWalletBalance=App.pref.getString(Constant.PHP_Balance, "");
            BigDecimal currentbalancephp;
            BigDecimal roundhaldvphp = null;
            if(phpcurrentWalletBalance!=null && !phpcurrentWalletBalance.equalsIgnoreCase("") && !phpcurrentWalletBalance.isEmpty())
            {
                currentbalancephp = new BigDecimal(phpcurrentWalletBalance);
                roundhaldvphp = currentbalancephp.setScale(4, BigDecimal.ROUND_FLOOR);
            }else
            {
                roundhaldvphp=new BigDecimal(0);
            }
            tvbalance_php.setText("Available Balance : "+roundhaldvphp+"");
            //for samkoin
            String samcurrentWalletBalance=App.pref.getString(Constant.SAMKOIN_Balance, "");
            BigDecimal currentbalancesam;
            BigDecimal roundhaldvsam = null;
            if(samcurrentWalletBalance!=null || !samcurrentWalletBalance.equalsIgnoreCase(""))
            {
                currentbalancesam = new BigDecimal(samcurrentWalletBalance);
                roundhaldvsam = currentbalancesam.setScale(4, BigDecimal.ROUND_FLOOR);
            }else
            {
                roundhaldvsam=new BigDecimal(0);
            }
            tvbalance_samk.setText("Available Balance : "+roundhaldvsam+"");

            if(clickoptionv.equalsIgnoreCase("usingphp"))
            {
                samkoinmainlay.setVisibility(View.GONE);
                phpmainlay.setVisibility(View.VISIBLE);
                mainviewtrancsa.setVisibility(View.VISIBLE);
                phpbalancevewi.setImageDrawable(getActivity().getDrawable(R.drawable.ciclieselecetd));
                sambalancevewi.setImageDrawable(getActivity().getDrawable(R.drawable.ciclieunselecetd));
                upperheading.setText("PHP");
                currentWalletBalance=App.pref.getString(Constant.PHP_Balance, "");
            }else
            {
                samkoinmainlay.setVisibility(View.VISIBLE);
                phpmainlay.setVisibility(View.GONE);
                mainviewtrancsa.setVisibility(View.VISIBLE);
                phpbalancevewi.setImageDrawable(getActivity().getDrawable(R.drawable.ciclieunselecetd));
                sambalancevewi.setImageDrawable(getActivity().getDrawable(R.drawable.ciclieselecetd));
                upperheading.setText("SAM Koin");
                currentWalletBalance=App.pref.getString(Constant.SAMKOIN_Balance, "");
            }
            etWalletUserName.setText(scanaddress);

            etDDKCoins.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {

                }

                @Override
                public void afterTextChanged(Editable editable)
                {
                    try {

                        if (etDDKCoins.getText().toString().length() > 0)
                        {
                            if(!etDDKCoins.getText().toString().equalsIgnoreCase("0") && !etDDKCoins.getText().toString().equalsIgnoreCase("0."))
                            {
                                String entervalue=etDDKCoins.getText().toString();
                                BigDecimal etDDKValue = new BigDecimal(entervalue);
                                //..........
                                String transcationfee="";
                                if(!clickoptionv.equalsIgnoreCase("usingphp"))
                                {
                                    transcationfee = App.pref.getString(Constant.sendsam_transaction_fees, "");
                                    BigDecimal transactionfeemain=(new BigDecimal(transcationfee)).divide(BigDecimal.valueOf(100));
                                    BigDecimal transactionfeeam=etDDKValue.multiply(transactionfeemain);
                                    totalamount=etDDKValue.subtract(transactionfeeam);
                                    transactionfee.setVisibility(View.VISIBLE);
                                    slide_custom_icon.setText("Pay "+totalamount+" SAMK");
                                    transactionfee.setText(""+transactionfeeam.toPlainString());

                                }else {
                                    transcationfee = App.pref.getString(Constant.sendphp_transaction_fees, "");
                                    BigDecimal transactionfeemain=(new BigDecimal(transcationfee)).divide(BigDecimal.valueOf(100));
                                    BigDecimal transactionfeeam=etDDKValue.multiply(transactionfeemain);
                                    totalamount=etDDKValue.subtract(transactionfeeam);
                                    transactionfee.setVisibility(View.VISIBLE);
                                    slide_custom_icon.setText("Pay "+totalamount+" PHP");
                                    transactionfee.setText(""+transactionfeeam.toPlainString());
                                }
                                //...........
                            }
                        } else {
                            {
                                transactionfee.setText("0.0");
                                slide_custom_icon.setText("Pay "+"0.0");
                            }
                        }

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            slide_custom_icon.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String enteramount="";
                    enteramount=etDDKCoins.getText().toString().trim();
                    if (enteramount.isEmpty())
                    {
                        AppConfig.showToast("Please Enter Amount");
                    }else {
                        String curentbalance = currentWalletBalance;
                        if (curentbalance == null || curentbalance.equalsIgnoreCase("")) {
                            curentbalance = "0.0";
                        }
                        BigDecimal walletBalance = new BigDecimal(curentbalance);
                        BigDecimal etDDKValue = new BigDecimal(enteramount);
                        //...........
                        if(clickoptionv.equalsIgnoreCase("usingphp"))
                        {
                            int statustrasan = walletBalance.compareTo(etDDKValue);
                            if (walletBalance.compareTo(etDDKValue) == 1 || walletBalance.compareTo(etDDKValue) == 0) {
                                sendOtp(enteramount);
                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                            }
                        }else
                        {
                            if(walletBalance.compareTo(etDDKValue) == 1 || walletBalance.compareTo(etDDKValue) == 0)
                            {
                                String data= App.pref.getString(Constant.minimum_samsend_transaction_fee, "");
                                BigDecimal btccondition = new BigDecimal(data);
                                if (etDDKValue.compareTo(btccondition) == 1 || etDDKValue.compareTo(btccondition) == 0) {
                                    sendOtp(enteramount);
                                } else {
                                    Toast.makeText(mContext, "SAMK Amount should be greater then or equal to "+data, Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });

        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
    }

    //for send otp
    private void sendOtpDialog(final String enteramount)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View customView = layoutInflater.inflate(R.layout.otp_verify_dialog, null);
        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(mContext);
        alert.setView(customView);
        final AlertDialog dialogNew = alert.create();
        dialogNew.show();
        dialogNew.setCancelable(false);
        final LinearLayout back_view=dialogNew.findViewById(R.id.back_view);
        final TextView btnVerify = dialogNew.findViewById(R.id.btnVerify);
        final EditText otp_edt1 = dialogNew.findViewById(R.id.otp_edt11);
        final EditText otp_edt2 = dialogNew.findViewById(R.id.otp_edt12);
        final EditText otp_edt3 = dialogNew.findViewById(R.id.otp_edt13);
        final EditText otp_edt4 = dialogNew.findViewById(R.id.otp_edt14);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer str_top = new StringBuffer();
                str_top.append(otp_edt1.getText().toString()).append(otp_edt2.getText().toString()).append(otp_edt3.getText().toString()).append(otp_edt4.getText().toString());
                if (str_top.length() > 0 && str_top.length() == 4) {
                    if (str_top.toString().equalsIgnoreCase(otp))
                    {
                        //for value
                        if(clickoptionv.equalsIgnoreCase("usingphp"))
                        {
                            sendPHPCurrencyTabScan(enteramount,"php",dialogNew);
                        }else
                        {
                            sendPHPCurrencyTab(enteramount,"samkoin",dialogNew);
                        }
                        //............
                    } else {
                        AppConfig.showToast("Otp is expired or incorrect");
                    }
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

        back_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNew.dismiss();
            }
        });
    }

    private void sendPHPCurrencyTabScan(final String enteramount,final String paymenttype,final AlertDialog dialogNewmain)
    {
        //............
        HashMap<String, String> hm = new HashMap<>();
        //............
        hm.put("amount", enteramount);
        hm.put("receiver_email", emailaddress);
        hm.put("conversion_fee", "" +transactionfee.getText().toString().trim());
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Confirm Payment....");
        Log.d("ddk api param",hm+" ");
        AppConfig.getLoadInterface().sendWalletAmountTransaction(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppConfig.hideLoading(dialog);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        Log.d("ddk api value",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(Constant.STATUS) == 1)
                        {
                            final JSONObject dataObject = object.getJSONObject("data");
                            String txtid=dataObject.getString("txt_id");
                            Fragment fragment = new SuccessFragmentScan();
                            Bundle arg = new Bundle();
                            arg.putString("action", "scan");
                            arg.putString("txtid", txtid);
                            arg.putString("amount", "" +enteramount);
                            arg.putString("name", etWalletUserName.getText().toString()+"");
                            fragment.setArguments(arg);
                            MainActivity.addFragment(fragment,true);
                            dialogNewmain.dismiss();


                        } else
                        {
                            AppConfig.hideLoading(dialog);
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } else {
                        AppConfig.showToast("Server error");
                    }
                } catch (IOException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                } catch (JSONException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                AppConfig.hideLoading(dialog);
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

    private void sendPHPCurrencyTab(final String enteramount,final String paymenttype,final AlertDialog dialogNewmain)
    {
        //............
        HashMap<String, String> hm = new HashMap<>();
        //............
        if(paymenttype.equalsIgnoreCase("php"))
        {
            hm.put("send_type","currency_wallet");
            hm.put("amount",enteramount);
            hm.put("receiver_email",emailaddress);
            hm.put("conversion_fee","0");
        }else
        {
         hm.put("send_type","crypto_wallet");
         hm.put("crypto_type","sam_koin");
         //............
         String transcationfee=App.pref.getString(Constant.sendsam_transaction_fees, "");
         String senderaddress=App.pref.getString(Constant.SAMKOIN_ADD, "");
         hm.put("sender_address",senderaddress);
         hm.put("receiver_address",emailaddress);
         hm.put("transaction_fees", transcationfee);
         hm.put("fee", "" +transactionfee.getText().toString().trim());
         hm.put("input_amount",enteramount);
         hm.put("sending_amount",totalamount+"");
        }
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Confirm Payment....");
        String tokenvalue=AppConfig.getStringPreferences(getActivity(), Constant.JWTToken);
        Log.d("sam koin",hm+" ");
        AppConfig.getLoadInterface().sendAmountAfterScan(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppConfig.hideLoading(dialog);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        Log.d("ddk api value",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(Constant.STATUS) == 1)
                        {
                            final JSONObject dataObject = object.getJSONObject("data");
                            String txtid=dataObject.getString("txt_id");
                            Fragment fragment = new SuccessFragmentScan();
                            Bundle arg = new Bundle();
                            arg.putString("action", "scan");
                            arg.putString("txtid", txtid);
                            arg.putString("amount", "" +etDDKCoins.getText().toString().trim());
                            arg.putString("name", etWalletUserName.getText().toString()+"");
                            fragment.setArguments(arg);
                            MainActivity.addFragment(fragment,true);
                            dialogNewmain.dismiss();
                        } else
                        {
                            AppConfig.hideLoading(dialog);
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } else {
                        AppConfig.showToast("Server error");
                    }
                }catch (Exception e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                AppConfig.hideLoading(dialog);
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

    //..
    private void sendOtp(final String enteramount) {
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
                    Log.d("otp value",otp);
                    sendOtpDialog(enteramount);
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
                errordurigApiCalling(getActivity(),t.getMessage());
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
        MainActivity.setTitle("Scan QR");
        MainActivity.enableBackViews(true);
        MainActivity.scanview.setVisibility(View.GONE);
    }
}
