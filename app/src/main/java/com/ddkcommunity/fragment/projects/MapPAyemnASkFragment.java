package com.ddkcommunity.fragment.projects;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.send.SuccessFragmentScan;
import com.ddkcommunity.interfaces.GetCryptoSubscriptionResponse;
import com.ddkcommunity.model.OtpResponse;
import com.ddkcommunity.model.checkRefferalModel;
import com.ddkcommunity.model.mapSubscriptionModel;
import com.ddkcommunity.model.mapoptionmodel;
import com.ddkcommunity.model.user.UserResponse;
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
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ReplacecommaValue;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.ShowServerPost;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapPAyemnASkFragment extends Fragment implements View.OnClickListener
{
    RecyclerView recyclerviewGridView;
    Activity mContext;
    TextView Portal,payamount;
    AppCompatButton submit_BT;
    LinearLayout paytototalview,mainpart,submenuiteam,paymenthours;
    ArrayList<mapoptionmodel> mapoptionList;
    SlideToActView slide_custom_icon;
    String packagesamt,actiontype,payamt;
    TextView currentBalance,feesview,totalview;
    String owncryptoaddress,samkoinConversion;
    private String currentWalletBalance;
    BigDecimal roundhaldv,SellTemp,BuyTemp;
    LinearLayout avaiableview;
    UserResponse userData;
    String subscriptionamount;
    String action="",usereferrlacode;
    View view;
    public MapPAyemnASkFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.mapoptionallfragment, container, false);
        mContext = getActivity();
        if (getArguments().getString("action") != null) {
            action= getArguments().getString("action");
        }

        if(action.equalsIgnoreCase("mapwithreferral"))
        {
            if (getArguments().getString("userenterreferrla") != null) {
                usereferrlacode= getArguments().getString("userenterreferrla");
            }
        }

        if (getArguments().getString("actiontype") != null)
        {
            actiontype= getArguments().getString("actiontype");
        }
        userData = AppConfig.getUserData(getContext());
        if (getArguments().getString("packages") != null)
        {
            packagesamt= getArguments().getString("packages");
        }

        if (getArguments().getString("amount") != null)
        {
            payamt= getArguments().getString("amount");
        }
        avaiableview=view.findViewById(R.id.avaiableview);
        currentBalance=view.findViewById(R.id.currentBalance);
        feesview=view.findViewById(R.id.feesview);
        totalview=view.findViewById(R.id.totalview);
        payamount=view.findViewById(R.id.payamount);
        payamount.setText("Pay $"+payamt);
        subscriptionamount=AppConfig.getStringPreferences(mContext, Constant.MINIMUM_SUBSCRIPTION);
        slide_custom_icon=view.findViewById(R.id.slide_custom_icon);
        paytototalview=view.findViewById(R.id.paytototalview);
        mainpart=view.findViewById(R.id.mainpart);
        paymenthours=view.findViewById(R.id.paymenthours);
        submit_BT=view.findViewById(R.id.submit_BT);
        submenuiteam=view.findViewById(R.id.submenuiteam);
        //for value
        if(actiontype.equalsIgnoreCase("samkoin"))
        {
            owncryptoaddress = App.pref.getString(Constant.SAMKOIN_ADD, "");
            //for ........
            BuyTemp = UserModel.getInstance().samkoinBuyPrice;
            SellTemp = UserModel.getInstance().samkoinSellPrice;
            String curkj=App.pref.getString(Constant.SAMKOIN_Balance, "");
            if(curkj!=null && !curkj.equalsIgnoreCase(null) && !App.pref.getString(Constant.SAMKOIN_Balance, "").toString().equalsIgnoreCase(""))
            {
                if(App.pref.getString(Constant.SAMKOIN_Balance, "").length()!=0)
                {
                    BigDecimal currentbalance=new BigDecimal(App.pref.getString(Constant.SAMKOIN_Balance, ""));
                    currentWalletBalance=ReplacecommaValue(currentbalance+"");
                    roundhaldv=currentbalance.setScale(4, BigDecimal.ROUND_FLOOR);
                    currentBalance.setText(roundhaldv.toPlainString()+"");
                }
            }else
            {
                currentWalletBalance=ReplacecommaValue("0.00000000");
                currentBalance.setText(currentWalletBalance+"");
            }
        }else if(actiontype.equalsIgnoreCase("btc"))
        {
            owncryptoaddress = App.pref.getString(Constant.BTC_ADD, "");
            //for .......
            BuyTemp = UserModel.getInstance().btcBuyPrice;
            SellTemp = UserModel.getInstance().btcSellPrice;
            String curkj = App.pref.getString(Constant.BTC_Balance, "");
            if (curkj != null && !curkj.equalsIgnoreCase(null) && !App.pref.getString(Constant.BTC_Balance, "").toString().equalsIgnoreCase("")) {
                if (App.pref.getString(Constant.BTC_Balance, "").length() != 0) {
                    BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.BTC_Balance, ""));
                    currentWalletBalance = ReplacecommaValue(currentbalance + "");
                    roundhaldv=currentbalance.setScale(8, BigDecimal.ROUND_FLOOR);
                    currentBalance.setText(roundhaldv.toPlainString()+"");
                }
            } else {
                currentWalletBalance=ReplacecommaValue("0.00000000");
                currentBalance.setText(currentWalletBalance+"");
            }
        }else
        if(actiontype.equalsIgnoreCase("eth"))
        {
            owncryptoaddress=App.pref.getString(Constant.Eth_ADD, "");
            BuyTemp = UserModel.getInstance().ethBuyPrice;
            SellTemp = UserModel.getInstance().ethSellPrice;
            String curkj = App.pref.getString(Constant.Eth_Balance, "");
            if (curkj != null && !curkj.equalsIgnoreCase(null) && !App.pref.getString(Constant.Eth_Balance, "").toString().equalsIgnoreCase("")) {
                if (App.pref.getString(Constant.Eth_Balance, "").length() != 0)
                {
                    BigDecimal currentbalance=new BigDecimal(App.pref.getString(Constant.Eth_Balance, ""));
                    roundhaldv=currentbalance.setScale(8, BigDecimal.ROUND_FLOOR);
                    currentWalletBalance=ReplacecommaValue(currentbalance+"");
                    currentBalance.setText(roundhaldv.toPlainString()+"");
                }
            } else {
                currentWalletBalance=ReplacecommaValue("0.00000000");
                currentBalance.setText(currentWalletBalance+"");
            }
        }else
        if(actiontype.equalsIgnoreCase("usdt"))
        {
            BuyTemp = UserModel.getInstance().usdtBuyPrice;
            SellTemp = UserModel.getInstance().usdtSellPrice;
            owncryptoaddress=App.pref.getString(Constant.USDT_ADD, "");
            String curkj = App.pref.getString(Constant.USDT_Balance, "");
            if (curkj != null && !curkj.equalsIgnoreCase(null) && !App.pref.getString(Constant.USDT_Balance, "").toString().equalsIgnoreCase("")) {
                if (App.pref.getString(Constant.USDT_Balance, "").length() != 0)
                {
                    BigDecimal currentbalance=new BigDecimal(App.pref.getString(Constant.USDT_Balance, ""));
                    roundhaldv=currentbalance.setScale(4, BigDecimal.ROUND_FLOOR);
                    currentWalletBalance=ReplacecommaValue(currentbalance+"");
                    currentBalance.setText(roundhaldv.toPlainString()+"");
                }
            } else {
                currentWalletBalance=ReplacecommaValue("0.00000000");
                currentBalance.setText(currentWalletBalance+"");
            }
        }else
        {
            currentBalance.setVisibility(View.GONE);
            avaiableview.setVisibility(View.INVISIBLE);
        }
        //.......
        submit_BT.setVisibility(View.GONE);
        slide_custom_icon.setVisibility(View.VISIBLE);
        paymenthours.setVisibility(View.GONE);
        paytototalview.setVisibility(View.VISIBLE);
        submenuiteam.setVisibility(View.GONE);
        Portal=view.findViewById(R.id.Portal);
        SpannableString content = new SpannableString("Portal");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        Portal.setText(content);
        recyclerviewGridView=view.findViewById(R.id.recyclerviewGridView);
        recyclerviewGridView.setVisibility(View.GONE);
        mainpart.setBackground(mContext.getDrawable(R.color.background));
        //for calculation
        BigDecimal total;
        total = new BigDecimal(payamt);
        if(actiontype.equalsIgnoreCase("samkoin"))
        {
            total =total.multiply(SellTemp);
            getCurrencyConvertRate(payamt+"","" + total, "USDT", "SAM");
        }else
        if(actiontype.equalsIgnoreCase("btc"))
        {
            getCurrencyConvertRate(payamt+"",""+total,  "USDT","BTC");
        }else
        if(actiontype.equalsIgnoreCase("eth"))
        {
            getCurrencyConvertRate(payamt+"",""+total, "USDT","ETH");
        }else
        if(actiontype.equalsIgnoreCase("usdt"))
        {
            getCurrencyConvertRate(payamt+"",""+total, "USDT", "USDT");
        }else
        if(actiontype.equalsIgnoreCase("creditcard"))
        {
            getCurrencyConvertRate(payamt+"",""+total, "USDT", "creditcard");
        }
        //.................
        slide_custom_icon.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView)
            {
                String input_amount=payamt;
                String conversion_rate=SellTemp+"";
                String sam_koin_conversion=samkoinConversion;
                String fee= feesview.getText().toString();
                String total_usdt_subscription=totalview.getText().toString();
                String transaction_id = "";

                if(actiontype.equalsIgnoreCase("creditcard"))
                {
                    sam_koin_conversion="0";
                    conversion_rate=SellTemp+"";
                    BigDecimal threeHundred = new BigDecimal(input_amount);
                    if (threeHundred.compareTo(new BigDecimal(totalview.getText().toString().trim())) == 1) {
                        Toast.makeText(mContext, "Minimum package amount is " + subscriptionamount + " USDT", Toast.LENGTH_SHORT).show();
                        slideToActView.resetSlider();
                        return;
                    }
                    MainActivity.mapcreditcard=1;
                    Fragment fragment = new CreditCardPaymentFragment();
                    Bundle arg = new Bundle();
                    arg.putString("userenterreferrla",usereferrlacode);
                    arg.putString("action", action);
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
                    if(actiontype.equalsIgnoreCase("samkoin"))
                    {
                        ttoalvalue = totalview.getText().toString();
                    }else
                    {
                        ttoalvalue = totalview.getText().toString();
                    }
                    BigDecimal amouncheck = new BigDecimal(ttoalvalue);
                    if (amouncheck.compareTo(roundhaldv) == 1)
                    {
                        if(actiontype.equalsIgnoreCase("samkoin"))
                        {
                            Toast.makeText(mContext, "Sam koin is not enough for package", Toast.LENGTH_SHORT).show();
                        }else
                        if(actiontype.equalsIgnoreCase("btc"))
                        {
                            Toast.makeText(mContext, "BTC is not enough for package", Toast.LENGTH_SHORT).show();
                        }else
                        if(actiontype.equalsIgnoreCase("eth"))
                        {
                            Toast.makeText(mContext, "ETH is not enough for package", Toast.LENGTH_SHORT).show();
                        }else
                        if(actiontype.equalsIgnoreCase("usdt"))
                        {
                            Toast.makeText(mContext, "USDT is not enough for package", Toast.LENGTH_SHORT).show();
                        }
                        slideToActView.resetSlider();
                        return;
                    }

                    //1 means for getter then to enter amount, 0 means equals, and else means less then.
                    BigDecimal conversionvalue = new BigDecimal(input_amount);
                    BigDecimal subscriptionCompare = new BigDecimal(subscriptionamount);
                    if (subscriptionCompare.compareTo(conversionvalue) == 1)
                    {
                        Toast.makeText(mContext, "Minimum package amount is " + subscriptionamount + " USDT ", Toast.LENGTH_SHORT).show();
                        slideToActView.resetSlider();
                        return;
                    }

                    MainActivity.mapcreditcard=1;
                    sendOtp();
                    slideToActView.resetSlider();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        MainActivity.setTitle("M.A.P");
        MainActivity.enableBackViews(true);
    }

    private void getCurrencyConvertRate(final String etAmountvalue,final String amount, String from_currency, String to_currency) {
        String transactionreiver="";
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Please wait ....");
        if(actiontype.equalsIgnoreCase("samkoin"))
        {
            transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver_USDT);
        }else
        if(actiontype.equalsIgnoreCase("btc"))
        {
            transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver_BTC);
        }else
        if(actiontype.equalsIgnoreCase("eth"))
        {
            transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver_ETH);
        }else
        if(actiontype.equalsIgnoreCase("usdt"))
        {
            transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver_USDT);
        }else
        if(actiontype.equalsIgnoreCase("creditcard"))
        {
            transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver_USDT);
        }
        HashMap<String, String> hm = new HashMap<>();
        hm.put("amount", amount);
        hm.put("from_currency", from_currency);
        hm.put("to_currency", to_currency);
        hm.put("receiver_address",transactionreiver);
        if(actiontype.equalsIgnoreCase("samkoin") || actiontype.equalsIgnoreCase("creditcard"))
        {
            hm.put("crypto_type", to_currency);
        }else
        {
            hm.put("crypto_type", to_currency);
        }
        Log.d("pm ",hm.toString());
        AppConfig.getLoadInterface().mapTransactionFeesCaluation(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                try {
                    AppConfig.hideLoading(dialog);
                    if (response.isSuccessful())
                    {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if(jsonObject.getString("status").equalsIgnoreCase("1"))
                        {
                            String convert_price = jsonObject.getString("convert_price");
                            String fees = jsonObject.getString("fees");
                            BigDecimal enteramount=new BigDecimal(etAmountvalue);
                            BigDecimal currencyTransactionPercent = new BigDecimal(fees);
                            BigDecimal resultData = new BigDecimal(convert_price);
                            if(actiontype.equalsIgnoreCase("samkoin"))
                            {
                                //BigDecimal percentage=currencyTransactionPercent.divide(new BigDecimal(100));
                                 BigDecimal baseprice=enteramount.divide(SellTemp, 5, RoundingMode.FLOOR);
                                BigDecimal finalfees=currencyTransactionPercent.multiply(baseprice);
                                feesview.setText(String.format(Locale.ENGLISH, "%.8f", finalfees));
                                BigDecimal totalamount=baseprice.add(finalfees);
                                totalview.setText("" + String.format(Locale.ENGLISH, "%.8f", totalamount));
                                BigDecimal samconvert=enteramount.subtract(finalfees);
                                BigDecimal finalconversion = samconvert.divide(SellTemp, 5, RoundingMode.FLOOR);
                                samkoinConversion=String.format(Locale.ENGLISH, "%.8f",finalconversion);

                            }else if(actiontype.equalsIgnoreCase("eth"))
                            {
                                //for new calcuakltion
                                BigDecimal basevalue=resultData;
                                BigDecimal finalfees = basevalue.multiply(currencyTransactionPercent);
                                feesview.setText(String.format(Locale.ENGLISH, "%.8f", finalfees));
                                BigDecimal totalamount = basevalue.add(finalfees);
                                totalview.setText("" + String.format(Locale.ENGLISH, "%.8f", totalamount));
                                BigDecimal samconvert=enteramount.subtract(finalfees);
                                BigDecimal finalconversion = samconvert.divide(BuyTemp, 5, RoundingMode.FLOOR);
                                samkoinConversion=String.format(Locale.ENGLISH, "%.8f",finalconversion);
                                //...................
                            }else if(actiontype.equalsIgnoreCase("usdt"))
                            {
                                //for new calcuakltion
                                BigDecimal basevalue=resultData;
                                BigDecimal finalfees = currencyTransactionPercent.multiply(basevalue);
                                feesview.setText("" + String.format(Locale.ENGLISH, "%.8f", finalfees));
                                BigDecimal totalamount = basevalue.add(finalfees);
                                totalview.setText("" + String.format(Locale.ENGLISH, "%.8f", totalamount));
                                BigDecimal samconvert=enteramount.subtract(finalfees);
                                BigDecimal finalconversion = samconvert.divide(BuyTemp, 5, BigDecimal.ROUND_FLOOR);
                                samkoinConversion=String.format(Locale.ENGLISH, "%.8f",finalconversion);
                                //...................
                            }else if(actiontype.equalsIgnoreCase("btc"))
                            {
                                //for new calcuakltzion
                                BigDecimal basevalue=resultData;
                                BigDecimal finalfees = currencyTransactionPercent.multiply(basevalue);
                                feesview.setText("" + String.format(Locale.ENGLISH, "%.8f", finalfees));
                                BigDecimal totalamount = basevalue.add(finalfees);
                                totalview.setText("" + String.format(Locale.ENGLISH, "%.8f", totalamount));
                                BigDecimal samconvert=enteramount.subtract(finalfees);
                                BigDecimal finalconversion = samconvert.divide(BuyTemp, 5,BigDecimal.ROUND_FLOOR);
                                samkoinConversion=String.format(Locale.ENGLISH, "%.8f",finalconversion);
                                //...................
                            }else if(actiontype.equalsIgnoreCase("creditcard"))
                            {
                                BigDecimal basevalue=enteramount;
                                BigDecimal finalfees = enteramount.multiply(currencyTransactionPercent);
                                feesview.setText("" + String.format(Locale.ENGLISH, "%.8f", finalfees));
                                BigDecimal totalamount = basevalue.add(finalfees);
                                totalview.setText("" + String.format(Locale.ENGLISH, "%.8f", totalamount));
                                BigDecimal samconvert=enteramount.subtract(finalfees);
                                BigDecimal finalconversion = samconvert.divide(BuyTemp, 5, RoundingMode.FLOOR);
                                samkoinConversion=String.format(Locale.ENGLISH, "%.8f",finalconversion);
                                //...................
                            }
                            String entermoutn=payamt;
                        }
                    } else
                    {
                        AppConfig.showToast("Server error");
                    }
                } catch (Exception e)
                {
                    AppConfig.hideLoading(dialog);
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
                        String otp = response.body().data;
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
                        String input_amount=payamt;
                        String conversion_rate=SellTemp+"";
                        String sam_koin_conversion=samkoinConversion;
                        String fee= feesview.getText().toString().replace("-","");
                        String total_usdt_subscription=totalview.getText().toString();
                        String transaction_id = "";
                        if(action.equalsIgnoreCase("mapwithreferral"))
                        {
                            registerUserMap(dialogNew,usereferrlacode,input_amount,conversion_rate,sam_koin_conversion,fee,total_usdt_subscription,transaction_id);
                        }else
                        {
                            ProgressDialog pd=new ProgressDialog(getActivity());
                            pd.setCanceledOnTouchOutside(false);
                            pd.setMessage("Please wait");
                            pd.show();
                            sendMapDataServer(dialogNew,pd,getActivity(),actiontype,payamt,conversion_rate,sam_koin_conversion,fee,total_usdt_subscription,"");
                        }
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

    private void registerUserMap(final BottomSheetDialog dialogNew,String userrefferal, String input_amount, final String conversion_rate, final String sam_koin_conversion, final String fee, final String total_usdt_subscription, String transaction_id)
    {
        final ProgressDialog pd=new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Please wait");
        pd.show();
        String countrydata=userData.getUser().country.get(0).country;
        final String emailid=userData.getUser().getEmail();
        String password=userData.getUser().getPassword();
        String Username=userData.getUser().getName().trim();
        String user[]=Username.split(" ");
        String firstname ="",lastname="";
        if(user.length>0)
        {
            for(int i=0;i<user.length;i++)
            {
                if(i==0)
                {
                    firstname=user[0];
                }else
                {
                    String userlast=user[1].trim();
                    lastname=userlast;
                }
            }
        }
        String mobileno=userData.getUser().getMobile();
        String dob=userData.getUser().getDob();
        String userReferalCode = App.pref.getString(Constant.USER_REFERAL_CODE, "");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("current_user_ref_code",userReferalCode);
        hm.put("first_name", firstname);
        hm.put("last_name", lastname);
        hm.put("password", password);
        hm.put("email", emailid);
        hm.put("mobile", mobileno);
        hm.put("dob", dob);
        hm.put("country", countrydata);
        hm.put("referring_user_ref_code", userrefferal);
        Log.d("create user ", hm.toString());
        AppConfig.getLoadInterfaceMap().cretaeUserFromSam(hm).enqueue(new Callback<checkRefferalModel>() {
            @Override
            public void onResponse(Call<checkRefferalModel> call, Response<checkRefferalModel> response) {
                try {
                    Log.d("sam erro par invi",response.body().toString());
                    if (response.isSuccessful() && response.body() != null)
                    {
                        if (response.body().getStatus().equalsIgnoreCase("true"))
                        {
                            sendMapDataServer(dialogNew,pd,getActivity(),actiontype,payamt,conversion_rate,sam_koin_conversion,fee,total_usdt_subscription,"");
                        }else
                        {
                            pd.dismiss();
                            Toast.makeText(getContext(), ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        pd.dismiss();
                        ShowApiError(getContext(),"server error check-mail-exist");
                    }

                } catch (Exception e) {
                   if(pd.isShowing())
                   {
                       pd.dismiss();
                   }
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<checkRefferalModel> call, Throwable t)
            {
                pd.dismiss();
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMapDataServer(final BottomSheetDialog dialogNewbottom,final ProgressDialog dialogNew, final Activity activity, final String subscriptiontype, final String input_amount, final String conversion_rate, final String sam_koin_conversion, final String fee, final String total_usdt_subscription, final String transaction_id)
    {
        HashMap<String, String> hm = new HashMap<>();
        String transactionreivervalue = "",transactionfeessreceiver="";
        if(actiontype.equalsIgnoreCase("creditcard"))
        {
            hm.put("secret",App.pref.getString(Constant.SAMKOIN_Secaret, ""));
            hm.put("public_key", App.pref.getString(Constant.BTC_publickey, ""));
            hm.put("subscription_mode", "stripe");
            hm.put("crypto_type","credit card");
            hm.put("sender_address", App.pref.getString(Constant.SAMKOIN_ADD, ""));
            transactionreivervalue=AppConfig.getStringPreferences(activity, Constant.Transaction_Receiver_SAMKOIN_Map);
            transactionfeessreceiver=AppConfig.getStringPreferences(activity, Constant.SAMTransactionAddress_Map);
        }else
        if(actiontype.equalsIgnoreCase("samkoin"))
        {
            hm.put("public_key", App.pref.getString(Constant.SAMKOIN_publickey, ""));
            hm.put("subscription_mode", "manual");
            hm.put("crypto_type","sam_koin");
            hm.put("sender_address", App.pref.getString(Constant.SAMKOIN_ADD, ""));
            transactionreivervalue=AppConfig.getStringPreferences(activity, Constant.Transaction_Receiver_SAMKOIN_Map);
            transactionfeessreceiver=AppConfig.getStringPreferences(activity, Constant.SAMTransactionAddress_Map);

        }else
        if(actiontype.equalsIgnoreCase("btc"))
        {
            hm.put("secret",App.pref.getString(Constant.BTC_Secaret, ""));
            hm.put("public_key", App.pref.getString(Constant.BTC_publickey, ""));
            hm.put("subscription_mode", "manual");
            hm.put("crypto_type","BTC");
            hm.put("sender_address", App.pref.getString(Constant.BTC_ADD, ""));
            transactionreivervalue=AppConfig.getStringPreferences(activity, Constant.Transaction_Receiver_BTC_Map);
            transactionfeessreceiver=AppConfig.getStringPreferences(activity, Constant.BTCTransactionAddress_Map);
        }else
        if(actiontype.equalsIgnoreCase("eth"))
        {
            hm.put("secret",App.pref.getString(Constant.Eth_Secret, ""));
            hm.put("public_key", App.pref.getString(Constant.Eth_publickey, ""));
            hm.put("subscription_mode", "manual");
            hm.put("crypto_type","ETH");
            hm.put("sender_address", App.pref.getString(Constant.Eth_ADD, ""));
            transactionreivervalue=AppConfig.getStringPreferences(activity, Constant.Transaction_Receiver_ETH_Map);
            transactionfeessreceiver=AppConfig.getStringPreferences(activity, Constant.ETHTranscationAddress_Map);
        }else
        if(actiontype.equalsIgnoreCase("usdt"))
        {
            hm.put("secret",App.pref.getString(Constant.USDT_Secaret, ""));
            hm.put("public_key", App.pref.getString(Constant.USDT_publickey, ""));
            hm.put("subscription_mode", "manual");
            hm.put("crypto_type","USDT");
            hm.put("sender_address", App.pref.getString(Constant.USDT_ADD, ""));
            transactionreivervalue=AppConfig.getStringPreferences(activity, Constant.Transaction_Receiver_USDT_Map);
            transactionfeessreceiver=AppConfig.getStringPreferences(activity, Constant.USDTTransactionAddress_Map);
        }
        hm.put("transaction_receiver_address",transactionfeessreceiver);
        hm.put("receiver_address",transactionreivervalue);
        hm.put("input_amount",input_amount);
        hm.put("total_amount",total_usdt_subscription);
        hm.put("conversion_rate",conversion_rate);
        hm.put("sam_koin_conversion", sam_koin_conversion);
        hm.put("fee",fee);
        hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
        hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
        hm.put("transaction_id", transaction_id);
        Log.d("paramterHM",hm+"");
        AppConfig.getLoadInterface().mapSubscription(AppConfig.getStringPreferences(activity, Constant.JWTToken), hm).enqueue(new Callback<mapSubscriptionModel>() {
            @Override
            public void onResponse(Call<mapSubscriptionModel> call, Response<mapSubscriptionModel> response) {
                try
                {
                    Log.d("sam erro par invi",response.body().toString());
                        if (response.isSuccessful() && response.body() != null)
                        {
                            if (response.body().getStatus()==1)
                            {
                                String text_id=response.body().getData().getTxtId();
                                registerAmountOnMap(dialogNewbottom,text_id,input_amount,dialogNew);
                            }else
                            {
                               dialogNew.dismiss();
                                Toast.makeText(activity, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                           dialogNew.dismiss();
                            ShowApiError(getContext(),"server error check-mail-exist");
                        }

                } catch (Exception e)
                {
                    if(dialogNew.isShowing())
                    {
                        dialogNew.dismiss();
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<mapSubscriptionModel> call, Throwable t)
            {
               dialogNew.dismiss();
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerAmountOnMap(final BottomSheetDialog dialogNew,final String textid,final String baseprice,final ProgressDialog dialog)
    {
        String cryptotypnemae="";
        if(actiontype.equalsIgnoreCase("creditcard"))
        {
            cryptotypnemae="credit card";
        }else
        if(actiontype.equalsIgnoreCase("samkoin"))
        {
            cryptotypnemae="sam_koin";
        }else
        if(actiontype.equalsIgnoreCase("btc"))
        {
            cryptotypnemae="BTC";
         }else
        if(actiontype.equalsIgnoreCase("eth"))
        {
            cryptotypnemae="ETH";
        }else
        if(actiontype.equalsIgnoreCase("usdt"))
        {
            cryptotypnemae="USDT";
        }

        final String emailid=userData.getUser().getEmail();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("email", emailid);
        hm.put("transaction_id", textid);
        hm.put("amount", baseprice);
        hm.put("payment_mode",cryptotypnemae);
        AppConfig.getLoadInterfaceMap().getSubscribePackage(hm).enqueue(new Callback<checkRefferalModel>() {
            @Override
            public void onResponse(Call<checkRefferalModel> call, Response<checkRefferalModel> response) {
                try {
                    Log.d("sam erro par invi",response.body().toString());
                    if (response.isSuccessful() && response.body() != null)
                    {
                        dialog.dismiss();
                        if (response.body().getStatus().equalsIgnoreCase("true"))
                        {
                            if(action.equalsIgnoreCase("mapwithreferral"))
                            {
                                dialogNew.dismiss();
                                Fragment fragment = new SuccessFragmentScan();
                                Bundle arg = new Bundle();
                                arg.putString("action", action);
                                arg.putString("email", emailid);
                                fragment.setArguments(arg);
                                MainActivity.addFragment(fragment, false);
                            }else
                            {
                                dialogNew.dismiss();
                                CommonMethodFunction.transactionStatus("Completed",getActivity(),textid,"");
                            }
                        }else
                        {
                            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dialog.dismiss();
                        ShowApiError(getContext(),"server error check-mail-exist");
                    }

                } catch (Exception e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<checkRefferalModel> call, Throwable t)
            {
                dialog.dismiss();
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.submit_BT)
        {
    /*
            Fragment fragment = new SuccessFragmentScan();
            Bundle arg = new Bundle();
            arg.putString("action", "map");
            fragment.setArguments(arg);
            MainActivity.addFragment(fragment,true);
    */
        }
    }

}