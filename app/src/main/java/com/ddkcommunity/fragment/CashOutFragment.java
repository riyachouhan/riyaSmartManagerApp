package com.ddkcommunity.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.LoadInterface;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.BankAccountAdapter;
import com.ddkcommunity.adapters.BankListAdapter;
import com.ddkcommunity.adapters.CredentialListAdapter;
import com.ddkcommunity.interfaces.GetAvailableValue;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.model.BankAccounts;
import com.ddkcommunity.model.BankList;
import com.ddkcommunity.model.OtpResponse;
import com.ddkcommunity.model.TransactionFeeData;
import com.ddkcommunity.model.TransactionFeesResponse;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.credential.CredentialsResponse;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.model.wallet.WalletResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.ConnectionDetector;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ncorti.slidetoact.SlideToActView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
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
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashOutFragment extends Fragment implements View.OnClickListener
{
    double transaction_feesValue=0.0;
    private TextView tvTransactionId, tvOrderStatus;
    private LinearLayout lytPhpPayment,lytBankDetails, lytSelectBank, lytDDKAmount, lytOtp;
    private TextView tvDeliveryTime, tvFeeCharges, btnVerify, btnShowAccount, tvBankNameMsg, tvBankNameDetails, tvBankNameDDk, btnNext, btnConfirm;
    private RecyclerView rvBankList;
    private ImageView ivBankIcon, ivBankIconDDK, btnFlagImage;
    private EditText etReceiverGcashMobile,etDDK,etBankHolderName, etBankAcNumber, etBankMobileNu, otp_edt1, otp_edt2, otp_edt3, otp_edt4;
    private BottomSheetDialog dialog;
    private BankListAdapter bankListAdapter;
    private StringBuffer str_top;
    private UserResponse userData;
    private Context mContext;
    private FrameLayout progress_bar;
    private ArrayList<BankList.BankData> bankList = new ArrayList<>();
    private BankAccountAdapter bankAccountAdapter;
    private String imagePath = "";
    private CredentialListAdapter adapterCredential;
    private List<Credential> credentialList = new ArrayList<>();
    private TextView tvAvailableDDK,tvSelectDdkAddress,transaction_fees,tvTotalInPhp,tvConversionRate;
    private String ddkSecret = "";
    private String currentWalletBalance;
    private SlideToActView slide_custom_icon;
    private BigDecimal buyTemp, sellTemp;
    private String totalPhpValue;
    private Boolean isPhpPayment;
    private ImageView ivTransactionCreateCheck, ivTransactionCreateUncheck, ivOrderStatusIconUnCheck, ivOrderStatusIconCheck;
    private AppCompatImageView btnCopyTransactionId;
    private int count = 0;
    private ClipboardManager clipboard;
    private ClipData clip;
    private TextView btnGoHome,ddkfees,tvMinimum;

    public CashOutFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cash_out, container, false);
        mContext = getActivity();
        tvMinimum=view.findViewById(R.id.tvMinimum);
        userData = AppConfig.getUserData(mContext);
        //*..........for trasnaction
        ConnectionDetector cd = new ConnectionDetector(getActivity());
        if (!cd.isConnectingToInternet())
        {
            Toast.makeText(mContext, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
        else {
            getTransactionFees(Constant.JWTToken);
        }
        /*----------------------------------------Payment Php-------------------------------------------*/
        lytPhpPayment = view.findViewById(R.id.lytPhpPayment);
        ddkfees=view.findViewById(R.id.ddkfees);
        tvSelectDdkAddress = view.findViewById(R.id.tvSelectDdkAddress);
        tvAvailableDDK = view.findViewById(R.id.tvAvailableDDK);
        tvConversionRate = view.findViewById(R.id.tvConversionRate);
        tvTotalInPhp = view.findViewById(R.id.tvTotalInPhp);
        transaction_fees=view.findViewById(R.id.transaction_fees);
        etDDK = view.findViewById(R.id.etDDK);
        etReceiverGcashMobile = view.findViewById(R.id.etReceiverGcashMobile);
        slide_custom_icon=view.findViewById(R.id.slide_custom_icon);
        tvSelectDdkAddress.setOnClickListener(this);

        lytBankDetails = view.findViewById(R.id.lytBankDetails);
        lytSelectBank = view.findViewById(R.id.lytSelectBank);
        lytDDKAmount = view.findViewById(R.id.lytDDKAmount);
        tvDeliveryTime = view.findViewById(R.id.tvDeliveryTime);
        tvFeeCharges = view.findViewById(R.id.tvFeeCharges);

        tvBankNameMsg = view.findViewById(R.id.tvBankNameMsg);
        tvDeliveryTime = view.findViewById(R.id.tvDeliveryTime);
        tvFeeCharges = view.findViewById(R.id.tvFeeCharges);

        rvBankList = view.findViewById(R.id.rvBankList);
        ivBankIcon = view.findViewById(R.id.ivBankIcon);
        tvBankNameDetails = view.findViewById(R.id.tvBankNameDetails);

        etBankHolderName = view.findViewById(R.id.etBankHolderName);
        etBankAcNumber = view.findViewById(R.id.etBankAcNumber);
        etBankMobileNu = view.findViewById(R.id.etBankMobileNu);
        tvBankNameDDk = view.findViewById(R.id.tvBankNameDDk);
        ivBankIconDDK = view.findViewById(R.id.ivBankIconDDK);
        btnNext = view.findViewById(R.id.btnNext);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnFlagImage = view.findViewById(R.id.btnFlagImage);
        btnShowAccount = view.findViewById(R.id.btnShowAccount);
        btnVerify = view.findViewById(R.id.btnVerify);
        lytOtp = view.findViewById(R.id.lytOtp);
        otp_edt1 = view.findViewById(R.id.otp_edt11);
        otp_edt2 = view.findViewById(R.id.otp_edt12);
        otp_edt3 = view.findViewById(R.id.otp_edt13);
        otp_edt4 = view.findViewById(R.id.otp_edt14);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        rvBankList.setLayoutManager(mLayoutManager);
        rvBankList.setItemAnimator(new DefaultItemAnimator());
        rvBankList.setAdapter(bankListAdapter);
        initOtpView();
        //for new layout
        String cashoutamount=AppConfig.getStringPreferences(mContext, Constant.MINIMUM_CASHOUT);
        tvMinimum.setText("Minimum Sell: "+cashoutamount+" PHP");
        String countrydata=userData.getUser().country.get(0).country;
        if(countrydata!=null && countrydata.equalsIgnoreCase("philippines"))
        {
            lytPhpPayment.setVisibility(View.VISIBLE);
            lytSelectBank.setVisibility(View.GONE);
            initTheCredentialView();
            latestDDKPrice();
            getCredentialsCall();
        }else
        {
            lytPhpPayment.setVisibility(View.GONE);
            lytSelectBank.setVisibility(View.VISIBLE);
            String countryid=userData.getUser().country.get(0).id;
            String countryname=userData.getUser().country.get(0).country;
            getBankList(countryid);
            showBankAccountList();
            AppConfig.openOkDialog1(mContext);
        }
        //...............
        String sortName = userData.getUser().country.get(0).sortname;
        Locale current = new Locale("", userData.getUser().country.get(0).sortname);
        String currency = Currency.getInstance(current).getCurrencyCode();
        tvBankNameMsg.setText("Convert your DDK to " + currency + " then send to bank selected");
        AssetManager assetManager = mContext.getAssets();
        try {
            InputStream ims = assetManager.open(sortName.toLowerCase() + ".png");
            Drawable d = Drawable.createFromStream(ims, null);
            btnFlagImage.setImageDrawable(d);
        } catch (IOException ex) {

        }
        btnConfirm.setOnClickListener(this);
        btnShowAccount.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
        bankListAdapter = new BankListAdapter(getActivity(), imagePath, bankList, new BankListAdapter.SetOnItemClickListener() {
            @Override
            public void onItemClick(String name, String image, String id) {
                lytSelectBank.setVisibility(View.GONE);
                lytDDKAmount.setVisibility(View.VISIBLE);
                lytBankDetails.setVisibility(View.GONE);
                Glide.with(mContext).load(image).into(ivBankIcon);
                Glide.with(mContext).load(image).into(ivBankIconDDK);
                tvBankNameDDk.setText(name);
                tvBankNameDetails.setText(name);

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
            public void afterTextChanged(Editable editable)
            {
                try {
                    if (etDDK.getText().toString().length() > 0)
                    {
                           if(etDDK.getText().toString().equalsIgnoreCase("."))
                           {

                           }else
                           {
                               BigDecimal etDDKValue = new BigDecimal(etDDK.getText().toString().trim());
                               if (etDDKValue.compareTo(BigDecimal.ZERO) == 0) {

                               } else
                               {
                                   convertUsdToPhp(etDDKValue);
                                   BigDecimal transactionfeeam = etDDKValue.multiply(BigDecimal.valueOf(0.1));
                                   BigDecimal transactionfeemain = transactionfeeam.divide(BigDecimal.valueOf(100));
                                   ddkfees.setText("Fee : " + transactionfeemain.toPlainString().toString() + " DDK");
                               }
                           }
                    } else {
                        tvTotalInPhp.setText("Total in Php:  ");
                        ddkfees.setText("Fees: 0 DDK");
                        transaction_fees.setText("Transaction Fees:  ");
                        transaction_fees.setVisibility(View.GONE);
                        totalPhpValue = "";
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        slide_custom_icon.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                if (etReceiverGcashMobile.getText().toString().trim().isEmpty()) {
                    AppConfig.showToast("Enter the G Cash mobile number");
                    slideToActView.resetSlider();
                    return;
                }
                if (etDDK.getText().toString().trim().isEmpty()) {
                    AppConfig.showToast("Enter ddk amount");
                    slideToActView.resetSlider();
                    return;
                }
                String cashoutamount=AppConfig.getStringPreferences(mContext, Constant.MINIMUM_CASHOUT);
                BigDecimal fiveHundred = new BigDecimal(cashoutamount);
                BigDecimal walletBalance = new BigDecimal(currentWalletBalance);
                BigDecimal etDDkPayment = new BigDecimal(etDDK.getText().toString().trim());
                // 1 means for getter then to enter amount, 0 means equals, and else means less then.
               // int comparevalue=fiveHundred.compareTo(new BigDecimal(totalPhpValue));
               /* if (fiveHundred.compareTo(new BigDecimal(totalPhpValue)) == 1) {
                    Toast.makeText(mContext, "Minimum amount will be php "+cashoutamount, Toast.LENGTH_SHORT).show();
                    slideToActView.resetSlider();
                    return;
                }*/
                if (etDDkPayment.compareTo(walletBalance) == 1) {
                    Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                    slideToActView.resetSlider();
                    return;
                }

                BigDecimal transactionfeeam=etDDkPayment.multiply(BigDecimal.valueOf(0.1));
                BigDecimal transactionfeemain=transactionfeeam.divide(BigDecimal.valueOf(100));
                BigDecimal totaltransaction=transactionfeemain.add(etDDkPayment);
                if(walletBalance.compareTo(totaltransaction)==1)
                {
                    isPhpPayment = true;
                    sendOtp();
                    slideToActView.resetSlider();
                }else
                {
                    Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                    slideToActView.resetSlider();
                    return;
                }

            }
        });
        return view;
    }

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
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    private void latestDDKPrice() {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        AppConfig.showLoading(dialog, "Fetching USDT Rate..");
        UserModel.getInstance().getUSDCall(new GetUSDAndBTCCallback() {
            @Override
            public void getValues(BigDecimal btc, BigDecimal usd) {
                AppConfig.hideLoading(dialog);
                if (usd != null)
                {
                    BigDecimal ONE_HUNDRED = new BigDecimal(100);
                    BigDecimal buy = usd.multiply(UserModel.getInstance().ddkBuyPercentage).divide(ONE_HUNDRED);
                    BigDecimal sell = usd.multiply(UserModel.getInstance().ddkSellPercentage).divide(ONE_HUNDRED);
                    buyTemp = buy.add(usd);
                    sellTemp = usd.subtract(sell);
                    tvConversionRate.setText("Conversion rate " + sellTemp);

                } else {
                    AppConfig.showToast("USDT rate not found");
                }
            }
        },getActivity());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvSelectDdkAddress) {
            if (credentialList.size() > 0) {
                if (!dialog.isShowing()) {
                    dialog.show();
                } else {
                    dialog.dismiss();
                }
            }
        }
        if (v.getId() == R.id.btnShowAccount) {
            if (!dialog.isShowing()) {
                dialog.show();
            } else {
                dialog.dismiss();
            }
        }
        if (v.getId() == R.id.btnNext) {
            lytSelectBank.setVisibility(View.GONE);
            lytDDKAmount.setVisibility(View.GONE);
            lytBankDetails.setVisibility(View.VISIBLE);
            lytOtp.setVisibility(View.GONE);
        }
        if (v.getId() == R.id.btnConfirm) {
            isPhpPayment = false;
            sendOtp();
        }
        if (v.getId() == R.id.btnVerify) {
            str_top = new StringBuffer();
            str_top.append(otp_edt1.getText().toString()).append(otp_edt2.getText().toString()).append(otp_edt3.getText().toString()).append(otp_edt4.getText().toString());
            if (str_top.length() > 0 && str_top.length() == 4) {
                if (str_top.toString().equalsIgnoreCase(otp)) {
                    if (isPhpPayment) {
                        sendPhpPayment();
                    } else {
                        hideKeyBoard();
                        openOkDialog();
                    }
                } else {
                    AppConfig.showToast("Otp is expired or incorrect");
                }
            }
        }

    }

    //for cashoutnew
    private void initTheCredentialView()
    {
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

    private void getWalletBalance(String walletId) {
        UserModel.getInstance().getWalletDetails(0,walletId, mContext, new GetAvailableValue() {
            @Override
            public void getValues(String ddk, WalletResponse successResponse) {
                currentWalletBalance = ddk;
                currentWalletBalance=ReplacecommaValue(currentWalletBalance+"");
                tvAvailableDDK.setText("Available DDk: " + ddk);
            }
        });
    }

    private void convertUsdToPhp(BigDecimal ddkvalue)
    {
        BigDecimal conversionRateValue = sellTemp;
        BigDecimal amounttocount = ddkvalue.multiply(conversionRateValue);
        String amountdata=amounttocount+"";
        HashMap<String, String> hm = new HashMap<>();
        hm.put("amount",amountdata);
        AppConfig.getLoadInterface().convertUsdToPhp(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                dialog.dismiss();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        Log.d("ddk response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getBoolean("success")) {
                            tvTotalInPhp.setText("Total in Php:  " + object.getString("result"));
                            totalPhpValue = object.getString("result");
                            String totalInPhpAmount = object.getString("result");
                            Log.d("total in php",totalPhpValue);
                            BigDecimal virtualphp = BigDecimal.valueOf(Double.parseDouble(totalInPhpAmount));
                            Log.d("virtual php",virtualphp+"");
                            BigDecimal virtualtrasaction=virtualphp.multiply(BigDecimal.valueOf(transaction_feesValue));
                            Log.d("virtual transaction",virtualtrasaction+"");
                            Log.d("transaction fees",transaction_feesValue+"");
                            String datvirtual=virtualtrasaction+"";
                            if(datvirtual.contains("0E-"))
                            {
                                Log.d("status","value is 0");
                                virtualtrasaction= BigDecimal.valueOf(0);
                            }
                            BigDecimal trasactionfees=virtualtrasaction.divide(BigDecimal.valueOf(100));
                            Log.d("virtual transafees",virtualtrasaction+"");
                            BigDecimal finalphp=virtualphp.subtract(trasactionfees);
                            totalPhpValue= String.valueOf(finalphp);
                            tvTotalInPhp.setText("Total in Php:  "+finalphp);
                            transaction_fees.setText("Transaction Fees:  "+trasactionfees.toPlainString().toString()+"");
                            transaction_fees.setVisibility(View.VISIBLE);
                        }else if (object.getInt(Constant.STATUS) == 4)
                        {
                            ShowServerPost((Activity)mContext,"ddk server error convert usdt ");
                        } else {
//                            AppConfig.hideLoading(dialog);
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } else {
                        AppConfig.showToast("Server error");
                    }
                } catch (IOException e) {
//                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                } catch (JSONException e) {
//                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                errordurigApiCalling(getActivity(),t.getMessage());
//                dialog.dismiss();
            }
        });
    }

    //get transaction fees
    private void getTransactionFees(String id) {
        LoadInterface apiservice = AppConfig.getClient().create(LoadInterface.class);
        //we havd to  correct
        Call<TransactionFeesResponse> call = apiservice.gettransactionFees(id);
        //showProgressDiaog();
        call.enqueue(new Callback<TransactionFeesResponse>() {
            @Override
            public void onResponse(Call<TransactionFeesResponse> call, Response<TransactionFeesResponse> response) {
                //  hideProgress();
                int code = response.code();
                String retrofitMesage="";
                try {
                    retrofitMesage=response.body().getMsg();
                }
                catch (Exception s)
                {

                }

                if (code==200)
                {
                    try {
                        retrofitMesage=response.body().getMsg();
                    }
                    catch (Exception s)
                    {

                    }

                    int status = response.body().getStatus();
                    if (status==1)
                    {
                        TransactionFeeData transactionFeeData = response.body().getData();
                        transaction_feesValue =transactionFeeData.getFeesAmount();
                    }else if (status == 4)
                    {
                        ShowServerPost((Activity)mContext,"ddk server error estimated fees");
                    }
                    else
                    if (status==400)
                    {
                        Toast.makeText(getActivity(), "Bad request", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), retrofitMesage, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (code==404)
                {
                    Toast.makeText(getActivity(), ""+ "Page not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (code==500)
                {
                    Toast.makeText(getActivity(), "Internal server error", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(), ""+retrofitMesage, Toast.LENGTH_SHORT).show();
                }
                // hideProgress();
            }
            @Override
            public void onFailure(Call<TransactionFeesResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Response getting failed", Toast.LENGTH_SHORT).show();
                // hideProgress();
            }
        });
    }

    private void sendPhpPayment() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("ddk_address", tvSelectDdkAddress.getText().toString().trim());
        hm.put("conversion_rate", "" + sellTemp);
        hm.put("php_amount", totalPhpValue);
        hm.put("amount", etDDK.getText().toString().trim());
        hm.put("mobile_no", etReceiverGcashMobile.getText().toString().trim());
        hm.put("secret", ddkSecret);
        hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
        hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Confirm Payment....");
        String tokenvalue=AppConfig.getStringPreferences(getActivity(), Constant.JWTToken);
        Log.d("newtoken",tokenvalue);
        AppConfig.getLoadInterface().sendCashOut(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppConfig.hideLoading(dialog);
                try {

                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.d("responsse",""+object);
                        if (object.getInt(Constant.STATUS) == 1) {
                            try {
                                final JSONObject dataObject = object.getJSONObject("data");
                                transactionStatus(dataObject.getString("txt_id"), ""+dataObject.getInt("cashout_id"));
                            } catch (JSONException e) {
                                AppConfig.showToast("Server error");
                                e.printStackTrace();
                            }
                        }else if (object.getInt(Constant.STATUS) == 4)
                        {
                            ShowServerPost((Activity)mContext,"ddk server error cashout");
                        } else {
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

    //................
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
                dialog.dismiss();
                Intent i=new Intent(getActivity(),MainActivity.class);
                startActivity(i);
                getActivity().finish();
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


    private void checkTransactionStatus(final String txt_id, final String lender_id) {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        if (count == 0) {
            AppConfig.showLoading(dialog, "Checking transaction status...");
        }
        HashMap<String, String> hm = new HashMap<>();
        hm.put("txt_id", txt_id);
        hm.put("cashout_id", lender_id);

        AppConfig.getLoadInterface().checCashOutTransactionStatus(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
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
                errordurigApiCalling(getActivity(),t.getMessage());

            }
        });
    }

    private void showBankAccountList() {

        final ArrayList<BankAccounts> bankAccounts = new ArrayList<>();
        bankAccounts.add(new BankAccounts("Rommel Santos", "2514-584741-8854", "+6125-4228-714"));
        bankAccounts.add(new BankAccounts("Mark Eleazar", "2525-694154-2250", "+63925-5241-996"));
        bankAccounts.add(new BankAccounts("James Johnson", "2541-557158-6687", "+63917-6653-2241"));

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        final View dialogView = layoutInflater.inflate(R.layout.popup_bank_country, null);
        dialog = new BottomSheetDialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(dialogView);

        RecyclerView rvCountryList = dialogView.findViewById(R.id.rvCountryList);
        final EditText searchEt = dialogView.findViewById(R.id.etSearch);


        bankAccountAdapter = new BankAccountAdapter(bankAccounts, new BankAccountAdapter.SetOnItemClickListener() {
            @Override
            public void onItemClick(String name, String accountNo, String mobileNumber) {
                etBankAcNumber.setText(accountNo);
                etBankHolderName.setText(name);
                etBankMobileNu.setText(mobileNumber);
                dialog.dismiss();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        rvCountryList.setLayoutManager(mLayoutManager);
        rvCountryList.setItemAnimator(new DefaultItemAnimator());
        rvCountryList.setAdapter(bankAccountAdapter);

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
                    bankAccountAdapter.updateData(bankAccounts);
                }
            }
        });
    }

    private void filter(String newText) {
    }


    private void getBankList(String country_id)
    {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("country_id", country_id);
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Get Bank List....");
        AppConfig.getLoadInterface().getTheBankList(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<BankList>() {
            @Override
            public void onResponse(Call<BankList> call, Response<BankList> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("response banklist",response.toString());
                    if (response.body().status == 1) {
                        bankList.clear();
                        imagePath = response.body().image_path;
                        bankList.addAll(response.body().data);
                        bankListAdapter.updateData(bankList, response.body().image_path);
                    } else if (response.body() != null && response.body().status == 3) {
                        AppConfig.showToast(response.body().msg);
                        AppConfig.openSplashActivity(getActivity());
                    } else {
                        AppConfig.showToast(response.body().msg);
                    }
                } else {
                    AppConfig.showToast("Server Error");
                }
            }

            @Override
            public void onFailure(Call<BankList> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private String otp = "";

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
                    lytSelectBank.setVisibility(View.GONE);
                    lytDDKAmount.setVisibility(View.GONE);
                    lytBankDetails.setVisibility(View.GONE);
                    lytPhpPayment.setVisibility(View.GONE);
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
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void initOtpView() {
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
    private void openOkDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup, null);

        TextView closePopupBtn = (TextView) customView.findViewById(R.id.btnClose);
        TextView tvMsg = (TextView) customView.findViewById(R.id.tvMsg);
        closePopupBtn.setText("Okay");
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(customView);

        tvMsg.setText("This feature is temporarily for demo purposes only");
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Sell");
        MainActivity.enableBackViews(true);
    }
}
