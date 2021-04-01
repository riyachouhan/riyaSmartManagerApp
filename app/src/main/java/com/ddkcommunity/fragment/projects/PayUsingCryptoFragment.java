package com.ddkcommunity.fragment.projects;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
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
import androidx.fragment.app.Fragment;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.CredentialListAdapter;
import com.ddkcommunity.interfaces.GetCryptoSubscriptionResponse;
import com.ddkcommunity.interfaces.GetPoolingResponse;
import com.ddkcommunity.model.OtpResponse;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.CommonMethodFunction;
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
public class PayUsingCryptoFragment extends Fragment {

    private String otp = "";

    public PayUsingCryptoFragment() {
    }

    String totalincrypto;
    private TextView tvConversion, tvRate, tvEsText, tvEstimatedFees, tvTotal;
    private EditText etDDK;
    String userEnterDDk="",con_amount_usd="";
    private BigDecimal total,sellTemp, buyTemp;
    private Context mContext;
    private TextView userAvailable,toatsmsg;
    TextView minimumamountview;
    SlideToActView slide_custom_icon;
    //String pendingteansactionamont="0";
    private String currentWalletBalance;
    String owncryptoaddress;
    View view;
    BigDecimal roundhaldv;
    String subscriptionamount;
    TextView normalline,samkoinline,samconversiom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_crypto_paysubscription, container, false);
        mContext = getActivity();
        samconversiom=view.findViewById(R.id.samconversiom);
        getAllviewIDs();
        //for amount
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("creditcard"))
        {
            samconversiom.setVisibility(View.VISIBLE);
            tvConversion.setVisibility(View.VISIBLE);
            etDDK.setHint("Enter USDT Amount");
            userAvailable.setVisibility(View.GONE);
            tvEsText.setText("Terminal Fee");
        }else
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("samkoin"))
        {
            samconversiom.setVisibility(View.INVISIBLE);
            tvConversion.setVisibility(View.INVISIBLE);
            etDDK.setHint("Enter SAM Koin Amount");
            owncryptoaddress = App.pref.getString(Constant.SAMKOIN_ADD, "");
            //for ........
            String curkj=App.pref.getString(Constant.SAMKOIN_Balance, "");
            if(curkj!=null && !curkj.equalsIgnoreCase(null) && !App.pref.getString(Constant.SAMKOIN_Balance, "").toString().equalsIgnoreCase(""))
            {
                normalline.setVisibility(View.GONE);
                samkoinline.setVisibility(View.VISIBLE);
                if(App.pref.getString(Constant.SAMKOIN_Balance, "").length()!=0)
                {
                    BigDecimal currentbalance=new BigDecimal(App.pref.getString(Constant.SAMKOIN_Balance, ""));
                    currentWalletBalance=ReplacecommaValue(currentbalance+"");
                    roundhaldv=currentbalance.setScale(4, BigDecimal.ROUND_FLOOR);
                    String currentblan=setUnderlineTextview(roundhaldv.toPlainString()+"");
                    userAvailable.setText("SAM Koin : "+currentblan);
                    currentWalletBalance=ReplacecommaValue(currentbalance+"");
                }
            }else
            {
                normalline.setVisibility(View.GONE);
                samkoinline.setVisibility(View.VISIBLE);
                String currentblan=setUnderlineTextview("0.00000000");
                userAvailable.setText("SAM Koin : "+currentblan);
            }
        }else if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("btc"))
        {
            samconversiom.setVisibility(View.VISIBLE);
            tvConversion.setVisibility(View.VISIBLE);
            normalline.setVisibility(View.VISIBLE);
            samkoinline.setVisibility(View.GONE);
            etDDK.setHint("Enter BTC Amount");
            owncryptoaddress = App.pref.getString(Constant.BTC_ADD, "");
            //for .......
            String curkj = App.pref.getString(Constant.BTC_Balance, "");
            if (curkj != null && !curkj.equalsIgnoreCase(null) && !App.pref.getString(Constant.BTC_Balance, "").toString().equalsIgnoreCase("")) {
                if (App.pref.getString(Constant.BTC_Balance, "").length() != 0) {
                    BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.BTC_Balance, ""));
                    currentWalletBalance = ReplacecommaValue(currentbalance + "");
                    roundhaldv=currentbalance.setScale(8, BigDecimal.ROUND_FLOOR);
                    String currentblan=setUnderlineTextview(roundhaldv.toPlainString()+"");
                    userAvailable.setText("Available BTC : " + currentblan);
                }
            } else {
                String currentblan=setUnderlineTextview("0.00000000");
                userAvailable.setText("Available BTC : " + currentblan);
            }
        }else
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("eth"))
        {
            samconversiom.setVisibility(View.VISIBLE);
            tvConversion.setVisibility(View.VISIBLE);
            normalline.setVisibility(View.VISIBLE);
            samkoinline.setVisibility(View.GONE);
            etDDK.setHint("Enter ETH Amount");
            owncryptoaddress=App.pref.getString(Constant.Eth_ADD, "");
            String curkj = App.pref.getString(Constant.Eth_Balance, "");
            if (curkj != null && !curkj.equalsIgnoreCase(null) && !App.pref.getString(Constant.Eth_Balance, "").toString().equalsIgnoreCase("")) {
                if (App.pref.getString(Constant.Eth_Balance, "").length() != 0)
                {
                    BigDecimal currentbalance=new BigDecimal(App.pref.getString(Constant.Eth_Balance, ""));
                    roundhaldv=currentbalance.setScale(8, BigDecimal.ROUND_FLOOR);
                    String currentblan=setUnderlineTextview(roundhaldv.toPlainString()+"");
                    userAvailable.setText("Available ETH : "+currentblan);
                    currentWalletBalance=ReplacecommaValue(currentbalance+"");
                }
            } else {
                String currentblan=setUnderlineTextview("0.00000000");
                userAvailable.setText("Available ETH : " + currentblan);
            }
        }else
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("usdt"))
        {
            samconversiom.setVisibility(View.VISIBLE);
            tvConversion.setVisibility(View.VISIBLE);
            normalline.setVisibility(View.VISIBLE);
            samkoinline.setVisibility(View.GONE);
            etDDK.setHint("Enter USDT Amount");
            owncryptoaddress=App.pref.getString(Constant.USDT_ADD, "");
            String curkj = App.pref.getString(Constant.USDT_Balance, "");
            if (curkj != null && !curkj.equalsIgnoreCase(null) && !App.pref.getString(Constant.USDT_Balance, "").toString().equalsIgnoreCase("")) {
                if (App.pref.getString(Constant.USDT_Balance, "").length() != 0)
                {
                    BigDecimal currentbalance=new BigDecimal(App.pref.getString(Constant.USDT_Balance, ""));
                    roundhaldv=currentbalance.setScale(4, BigDecimal.ROUND_FLOOR);
                    String currentblan=setUnderlineTextview(roundhaldv.toPlainString()+"");
                    userAvailable.setText("Available USDT : "+currentblan);
                    currentWalletBalance=ReplacecommaValue(currentbalance+"");
                }
            } else {
                String currentblan=setUnderlineTextview("0.00000000");
                userAvailable.setText("Available USDT : " + currentblan);
            }
        }
        //.............
        subscriptionamount=AppConfig.getStringPreferences(mContext, Constant.MINIMUM_SUBSCRIPTION);
        minimumamountview.setText(subscriptionamount+" USDT Min.");
        //..........for Click evetn
        performClickEvent();
        //get buy sell view
        latestSellBuyPrice();
        //for btc previous payment
       /* if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("btc"))
        {
            getTransactionStatus("BTC");
        }*/
        return view;
    }

    public String  setUnderlineTextview(String amount)
    {
        String mystring="<u>"+amount+"</u>";
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return amount;
    }

    public void performClickEvent()
    {
        userAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String currentbalnacevalue= currentWalletBalance;
                etDDK.setText(currentbalnacevalue);
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
                            BigDecimal etAmountvalue = new BigDecimal(editable.toString());
                            if (etAmountvalue.compareTo(BigDecimal.ZERO) == 0)
                            {
                                tvEstimatedFees.setText("0");
                                tvConversion.setText("0");
                                tvTotal.setText("0");
                                total = new BigDecimal(0);

                            } else
                            {
                                total = etAmountvalue;
                                slide_custom_icon.setLocked(true);
                                toatsmsg.setVisibility(View.VISIBLE);
                                if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("samkoin"))
                                {
                                   total = etAmountvalue.multiply(sellTemp);
                                    getCurrencyConvertRate(etAmountvalue+"","" + total, "USDT", "SAM");
                                }else
                                if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("btc"))
                                {
                                    getCurrencyConvertRate(etAmountvalue+"",""+total, "BTC", "USDT");
                                }else
                                if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("eth"))
                                {
                                    getCurrencyConvertRate(etAmountvalue+"",""+total, "ETH", "USDT");
                                }else
                                if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("usdt"))
                                {
                                    getCurrencyConvertRate(etAmountvalue+"",""+total, "USDT", "USDT");
                                }else
                                if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("creditcard"))
                                {
                                    getCurrencyConvertRate(etAmountvalue+"",""+total, "USDT", "creditcard");
                                }

                            }
                        } catch(Exception ex)
                        { // handle your exception
                            Toast.makeText(getActivity(),ex.toString(),Toast.LENGTH_SHORT).show();
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
                String ddkenter=etDDK.getText().toString().trim();
                if(ddkenter.equalsIgnoreCase(""))
                {
                    if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("samkoin"))
                    {
                        Toast.makeText(mContext, "Please Enter SAM Koin Amount", Toast.LENGTH_SHORT).show();
                    }else
                    if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("btc"))
                    {
                        Toast.makeText(mContext, "Please Enter BTC Amount", Toast.LENGTH_SHORT).show();
                    }else
                    if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("eth"))
                    {
                        Toast.makeText(mContext, "Please Enter ETH Amount", Toast.LENGTH_SHORT).show();
                    }else
                    if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("usdt"))
                    {
                        Toast.makeText(mContext, "Please Enter USDT Amount", Toast.LENGTH_SHORT).show();
                    }else  if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("creditcard"))
                    {
                        Toast.makeText(mContext, "Please Enter USDT Amount", Toast.LENGTH_SHORT).show();
                    }
                        slideToActView.resetSlider();
                    return;
                }else
                {
                    if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("creditcard"))
                    {
                        BigDecimal threeHundred = new BigDecimal(subscriptionamount);
                        if (threeHundred.compareTo(new BigDecimal(tvTotal.getText().toString().trim())) == 1) {
                            Toast.makeText(mContext, "Minimum subscription amount is " + subscriptionamount + " USDT", Toast.LENGTH_SHORT).show();
                            slideToActView.resetSlider();
                            return;
                        }
                        String input_amount=etDDK.getText().toString();
                        String conversion_rate=tvRate.getText().toString().replace(" USDT","");
                        String sam_koin_conversion=tvConversion.getText().toString();
                        String fee=tvEstimatedFees.getText().toString().replace("-","");
                        String total_usdt_subscription=tvTotal.getText().toString();
                        MainActivity.mapcreditcard=0;
                        Fragment fragment = new CreditCardPaymentFragment();
                        Bundle arg = new Bundle();
                        arg.putString("input_amount", input_amount);
                        arg.putString("conversion_rate",conversion_rate);
                        arg.putString("samkoinconversion", sam_koin_conversion);
                        arg.putString("fee", fee);
                        arg.putString("total_usdt_subscription", total_usdt_subscription);
                        fragment.setArguments(arg);
                        MainActivity.addFragment(fragment,true);
                        //MainActivity.addFragment(new CreditCardPaymentFragment(input_amount,conversion_rate,sam_koin_conversion,fee,total_usdt_subscription), true);
                        slideToActView.resetSlider();

                    }else
                    {
                         String ttoalvalue = "";
                         if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("samkoin"))
                         {
                             ttoalvalue = tvTotal.getText().toString();
                         }else
                         {
                             ttoalvalue = etDDK.getText().toString();
                         }
                        BigDecimal amouncheck = new BigDecimal(ttoalvalue);
                        if (amouncheck.compareTo(roundhaldv) == 1)
                        {
                            if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("samkoin"))
                            {
                                Toast.makeText(mContext, "Sam koin is not enough for subscription", Toast.LENGTH_SHORT).show();
                            }else
                            if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("btc"))
                            {
                                Toast.makeText(mContext, "BTC is not enough for subscription", Toast.LENGTH_SHORT).show();
                            }else
                            if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("eth"))
                            {
                                Toast.makeText(mContext, "ETH is not enough for subscription", Toast.LENGTH_SHORT).show();
                            }else
                            if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("usdt"))
                            {
                                Toast.makeText(mContext, "USDT is not enough for subscription", Toast.LENGTH_SHORT).show();
                            }
                            slideToActView.resetSlider();
                            return;
                        }

                        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("samkoin"))
                        {
                        }
                        //1 means for getter then to enter amount, 0 means equals, and else means less then.
                        BigDecimal conversionvalue = new BigDecimal(tvTotal.getText().toString());
                        BigDecimal subscriptionCompare = new BigDecimal(subscriptionamount);
                        if (subscriptionCompare.compareTo(conversionvalue) == 1)
                        {
                            Toast.makeText(mContext, "Minimum subscription amount is " + subscriptionamount + " USDT ", Toast.LENGTH_SHORT).show();
                            slideToActView.resetSlider();
                            return;
                        }
                        sendOtp();
                        slideToActView.resetSlider();
                    }
                }
            }
        });

    }

    public void getAllviewIDs()
    {
        normalline=view.findViewById(R.id.normalline);
        samkoinline=view.findViewById(R.id.samkoinline);
        userAvailable=view.findViewById(R.id.userAvailable);
        slide_custom_icon=view.findViewById(R.id.slide_custom_icon);
        toatsmsg=view.findViewById(R.id.toatsmsg);
        minimumamountview=view.findViewById(R.id.minimumamountview);
        etDDK = view.findViewById(R.id.etDDK);
        tvConversion = view.findViewById(R.id.tvConversion);
        tvRate = view.findViewById(R.id.tvRate);
        tvEsText = view.findViewById(R.id.tvEsText);
        tvEstimatedFees = view.findViewById(R.id.tvEstimatedFees);
        tvTotal = view.findViewById(R.id.tvTotal);
    }
    //.................
    private void getCurrencyConvertRate(final String etAmountvalue,final String amount, String from_currency, String to_currency) {
        String transactionreiver="";
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("samkoin"))
        {
            transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver_USDT);
        }else
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("btc"))
        {
            transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver_BTC);
        }else
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("eth"))
        {
            transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver_ETH);
        }else
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("usdt"))
        {
            transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver_USDT);
        }else
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("creditcard"))
        {
            transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver_USDT);
        }
        HashMap<String, String> hm = new HashMap<>();
        hm.put("amount", amount);
        hm.put("from_currency", from_currency);
        hm.put("to_currency", to_currency);
        hm.put("receiver_address",transactionreiver);
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("samkoin") || SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("creditcard"))
        {
            hm.put("crypto_type", to_currency);
        }else
        {
            hm.put("crypto_type", from_currency);
        }
        Log.d("pm ",hm.toString());
        AppConfig.getLoadInterface().SamConvertPrice(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                try {
                    if (response.isSuccessful())
                     {
                        toatsmsg.setVisibility(View.INVISIBLE);
                        slide_custom_icon.setLocked(false);
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if(jsonObject.getString("status").equalsIgnoreCase("1"))
                        {
                            String convert_price = jsonObject.getString("convert_price");
                            String fees = jsonObject.getString("fees");
                            BigDecimal enteramount=new BigDecimal(etAmountvalue);
                            BigDecimal currencyTransactionPercent = new BigDecimal(fees);
                            BigDecimal resultData = new BigDecimal(convert_price);
                            if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("samkoin"))
                            {
                                BigDecimal finalfees=enteramount.multiply(currencyTransactionPercent);
                                tvEstimatedFees.setText("-"+String.format(Locale.ENGLISH, "%.8f", finalfees));
                                BigDecimal samconvert=enteramount.subtract(finalfees);
                                BigDecimal finalconversion = samconvert.divide(sellTemp, 5, RoundingMode.FLOOR);
                                tvConversion.setText(String.format(Locale.ENGLISH, "%.8f",finalconversion));
                                BigDecimal totalamount=samconvert.multiply(sellTemp);
                                tvTotal.setText("" + String.format(Locale.ENGLISH, "%.8f", totalamount));
                            }else if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("eth"))
                            {
                                //for new calcuakltion
                                BigDecimal finalfees = enteramount.multiply(currencyTransactionPercent);
                                tvEstimatedFees.setText("-" + String.format(Locale.ENGLISH, "%.8f", finalfees));
                                BigDecimal finalconversion = resultData.divide(buyTemp, 5, RoundingMode.FLOOR);
                                tvConversion.setText(String.format(Locale.ENGLISH, "%.8f", finalconversion));
                                BigDecimal totalamount = resultData;
                                tvTotal.setText("" + String.format(Locale.ENGLISH, "%.8f", totalamount));
                                //...................
                            }else if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("usdt"))
                            {
                                //for new calcuakltion
                                BigDecimal finalfees = enteramount.multiply(currencyTransactionPercent);
                                tvEstimatedFees.setText("-" + String.format(Locale.ENGLISH, "%.8f", finalfees));
                                BigDecimal samconvert=enteramount.subtract(finalfees);
                                BigDecimal finalconversion = samconvert.divide(buyTemp, 5, RoundingMode.FLOOR);
                                tvConversion.setText(String.format(Locale.ENGLISH, "%.8f",finalconversion));
                                tvTotal.setText("" + String.format(Locale.ENGLISH, "%.8f", samconvert));
                                //...................
                            }else if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("btc"))
                            {
                                //for new calcuakltzion
                                BigDecimal finalfees = enteramount.multiply(currencyTransactionPercent);
                                tvEstimatedFees.setText("-" + String.format(Locale.ENGLISH, "%.8f", finalfees));
                                BigDecimal finalconversion = resultData.divide(buyTemp, 5, RoundingMode.FLOOR);
                                tvConversion.setText(String.format(Locale.ENGLISH, "%.8f", finalconversion));
                                BigDecimal totalamount = resultData;
                                tvTotal.setText("" + String.format(Locale.ENGLISH, "%.8f", totalamount));
                                //...................
                            }else if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("creditcard"))
                            {
                                BigDecimal finalfees = enteramount.multiply(currencyTransactionPercent);
                                tvEstimatedFees.setText("-" + String.format(Locale.ENGLISH, "%.8f", finalfees));
                                BigDecimal samconvert=enteramount.subtract(finalfees);
                                BigDecimal finalconversion = samconvert.divide(buyTemp, 5, RoundingMode.FLOOR);
                                tvConversion.setText(String.format(Locale.ENGLISH, "%.8f",finalconversion));
                                tvTotal.setText("" + String.format(Locale.ENGLISH, "%.8f", samconvert));
                                //...................
                              }
                            String entermoutn=etDDK.getText().toString();
                            if(entermoutn.equalsIgnoreCase(""))
                            {
                                tvEstimatedFees.setText("0");
                                tvConversion.setText("0");
                                tvTotal.setText("0");
                                total = new BigDecimal(0);
                            }else
                            {
                            BigDecimal etAmountvalue = new BigDecimal(etDDK.getText().toString());
                            if (etAmountvalue.compareTo(BigDecimal.ZERO) == 0) {
                                tvEstimatedFees.setText("0");
                                tvConversion.setText("0");
                                tvTotal.setText("0");
                                total = new BigDecimal(0);

                            }
                            }
                        }
                     } else
                     {
                         toatsmsg.setVisibility(View.INVISIBLE);
                         AppConfig.showToast("Server error");
                     }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                AppConfig.showToast("Server error");
            }
        });
    }

    public void sendDataOnServer(Activity activity)
    {
        String input_amount=etDDK.getText().toString();
        String conversion_rate=tvRate.getText().toString().replace(" USDT","");
        String sam_koin_conversion=tvConversion.getText().toString();
        String fee= tvEstimatedFees.getText().toString().replace("-","");
        String total_usdt_subscription=tvTotal.getText().toString();
        String transaction_id = "";
        AppConfig.showLoading("Send Data..", activity);
        UserModel.getInstance().sendSubscriptionPayment(activity,SelectPaymentPoolingFragment.selectedSubscription,input_amount,conversion_rate,sam_koin_conversion,fee,total_usdt_subscription,transaction_id, new GetCryptoSubscriptionResponse()
                {
                    @Override
                    public void getResponse(JSONObject jsonObject) {
                        AppConfig.hideLoader();
                        try {
                            if (jsonObject.getInt(Constant.STATUS) == 1) {
                                MainActivity.isLocalManualTrans = true;
                                try {
                                    final JSONObject dataObject = jsonObject.getJSONObject("data");
                                    CommonMethodFunction.transactionStatus("Pending",getActivity(),dataObject.getString("txt_id"), dataObject.getString("lender_id"));
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

                        } catch (JSONException e)
                        {
                            AppConfig.hideLoader();
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void latestSellBuyPrice()
    {
        buyTemp = UserModel.getInstance().samkoinBuyPrice;
        sellTemp = UserModel.getInstance().samkoinSellPrice;
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("samkoin"))
        {
            tvRate.setText(String.format("%.6f", sellTemp) + " USDT");
        }else
        {
            tvRate.setText(String.format("%.4f", buyTemp) + " USDT");
        }
    }

    private void sendOtp() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("email", App.pref.getString(Constant.USER_EMAIL, ""));
        hm.put("name", App.pref.getString(Constant.USER_NAME, ""));
        Log.d("param",":"+hm);

        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Sending otp....");

        AppConfig.getLoadInterface().postOtp(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body().status == 1) {
                    Log.d("otpre","::"+response.body().data );
                    try {
                        otp = response.body().data;
                        Log.d("otp",otp);
                        initOtpVerifiaction(otp,getActivity());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    private void initOtpVerifiaction(final String otp,final Activity mContext)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        final View dialogView;
        dialogView = layoutInflater.inflate(R.layout.otpverificationlayout, null);
        final BottomSheetDialog dialogNew = new BottomSheetDialog(mContext, R.style.DialogStyle);
        dialogNew.setContentView(dialogView);
        CommonMethodFunction.setupFullHeight(mContext,dialogNew);
        ImageView logo1=dialogView.findViewById(R.id.logo1);
        final LinearLayout otherview=dialogView.findViewById(R.id.otherview);
        otherview.setVisibility(View.VISIBLE);
        logo1.setVisibility(View.GONE);
        final LinearLayout back_view=dialogView.findViewById(R.id.back_view);
        final TextView btnVerify =dialogView.findViewById(R.id.btnVerify);
        final EditText otp_edt1 =dialogView.findViewById(R.id.otp_edt11);
        final EditText otp_edt2 =dialogView.findViewById(R.id.otp_edt12);
        final EditText otp_edt3 =dialogView.findViewById(R.id.otp_edt13);
        final EditText otp_edt4 =dialogView.findViewById(R.id.otp_edt14);

        btnVerify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                StringBuffer str_top = new StringBuffer();
                str_top.append(otp_edt1.getText().toString()).append(otp_edt2.getText().toString()).append(otp_edt3.getText().toString()).append(otp_edt4.getText().toString());
                if (str_top.length() > 0 && str_top.length() == 4)
                {
                    String otpnew=str_top.toString();
                    if (str_top.toString().equalsIgnoreCase(otp))
                    {
                        hideKeyBoard();
                        sendDataOnServer(getActivity());
                    } else {
                        AppConfig.showToast("Otp is expired or incorrect");
                    }
                    //............
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
        dialogNew.show();
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("creditcard"))
        {
            MainActivity.setTitle("Using Credit Card");
        }else
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("samkoin"))
        {
            MainActivity.setTitle("Using SAM Koin");
        }else
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("btc"))
        {
            MainActivity.setTitle("Using BTC");
        }else
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("eth"))
        {
            MainActivity.setTitle("Using ETH");
        }else
        if(SelectPaymentPoolingFragment.selectedSubscription.equalsIgnoreCase("usdt"))
        {
            MainActivity.setTitle("Using USDT");
        }
        MainActivity.enableBackViews(true);
    }

}
