
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
import android.text.Html;
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
import androidx.appcompat.widget.AppCompatButton;
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
import com.ddkcommunity.adapters.AllTypeCashoutFragmentAdapter;
import com.ddkcommunity.adapters.BankAccountAdapter;
import com.ddkcommunity.adapters.BankListAdapter;
import com.ddkcommunity.adapters.CashoutFragmenAdapter;
import com.ddkcommunity.adapters.CredentialListAdapter;
import com.ddkcommunity.adapters.RedeemChoiceAdpater;
import com.ddkcommunity.fragment.projects.SelectPaymentPoolingFragment;
import com.ddkcommunity.interfaces.GegtSettingStatusinterface;
import com.ddkcommunity.interfaces.GetAvailableValue;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.model.AllAvailableBankList;
import com.ddkcommunity.model.BankAccounts;
import com.ddkcommunity.model.BankList;
import com.ddkcommunity.model.OtpResponse;
import com.ddkcommunity.model.TransactionFeeData;
import com.ddkcommunity.model.TransactionFeesResponse;
import com.ddkcommunity.model.UserBankList;
import com.ddkcommunity.model.UserBankListDetails;
import com.ddkcommunity.model.UserBankListResponse;
import com.ddkcommunity.model.checkRefferalModel;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.credential.CredentialsResponse;
import com.ddkcommunity.model.getSettingModel;
import com.ddkcommunity.model.mapSubscriptionModel;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.model.wallet.WalletResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.CommonMethodFunction;
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
import java.math.RoundingMode;
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
import static com.ddkcommunity.fragment.HomeFragment.userselctionopt;
import static com.ddkcommunity.utilies.dataPutMethods.DataNotFound;
import static com.ddkcommunity.utilies.dataPutMethods.ReplacecommaValue;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.ShowFunctionalityAlert;
import static com.ddkcommunity.utilies.dataPutMethods.ShowSameWalletDialog;
import static com.ddkcommunity.utilies.dataPutMethods.ShowServerPost;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;
import static com.ddkcommunity.utilies.dataPutMethods.showDialogCryptoData;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashOutFragmentNew extends Fragment implements View.OnClickListener
{
    double transaction_feesValue=0.0;
    public static CashOutFragmentNew CashOutFragmentasub;
    private TextView tvTransactionId, tvOrderStatus;
    private LinearLayout lytPhpPayment,lytBankDetails, lytSelectBank, lytDDKAmount, lytOtp;
    private TextView tvDeliveryTime, tvFeeCharges, btnVerify, btnShowAccount, tvBankNameMsg, tvBankNameDetails, tvBankNameDDk;
    private RecyclerView rvBankList;
    private ImageView ivBankIcon, ivBankIconDDK, btnFlagImage;
    private EditText etReceiverGcashMobile,etDDK,etBankHolderName, etBankAcNumber, etBankMobileNu;
    private BottomSheetDialog dialog;
    private BankListAdapter bankListAdapter;
    private AllTypeCashoutFragmentAdapter allTypeCashoutFragmentAdapter;
    private StringBuffer str_top;
    private UserResponse userData;
    public Context mContext;
    public static String banknotabv="",msgbank="";
    private FrameLayout progress_bar;
    private ArrayList<BankList.BankData> bankList = new ArrayList<>();
    private ArrayList<BankList.BankData> bankListnewli = new ArrayList<>();
    private ArrayList<UserBankListResponse> userBankListResponses = new ArrayList<>();
    public ArrayList<UserBankList> userBankLists;
    private ArrayList<AllAvailableBankList.BankData> bankLists = new ArrayList<>();
    private BankAccountAdapter bankAccountAdapter;
    public CashoutFragmenAdapter mAdapter;
    private String imagePath = "";
    private CredentialListAdapter adapterCredential;
    private List<Credential> credentialList = new ArrayList<>();
    private TextView tvAvailableDDK,tvSelectDdkAddress,tvTotalInPhp,tvConversionRate;
    public static TextView transaction_fees;
    private String ddkSecret = "";
    private String currentWalletBalance;
    private BigDecimal buyTemp, sellTemp;
    private String totalPhpValue;
    private Boolean isPhpPayment;
     private int count = 0;
    private ClipboardManager clipboard;
    private ClipData clip;
    public int usercountryselect=0;
    private TextView btnGoHome,ddkfees,tvMinimum;
    //for below cah
    public RecyclerView recyclerview1,recyclerviewGridView;
    private Object CashOutFragmentNew;
    View view;
    public static String countrydata;
    AppCompatButton transfersamwallet;

    public CashOutFragmentNew() {

    }

    public void getSettingServerOnTab(Activity activity, final String functionname)
    {
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Please wait ....");
        String func=functionname;
        {
            func=functionname;
        }
        UserModel.getInstance().getSettignSatusView(activity,func,new GegtSettingStatusinterface()
        {
            @Override
            public void getResponse(Response<getSettingModel> response)
            {
                //   AppConfig.hideLoader();
                try
                {
                    if (response.body().getStatus() == 1)
                    {
                        AppConfig.hideLoading(dialog);
                        transfersamwallet.setVisibility(View.VISIBLE);
                    } else
                    {
                        transfersamwallet.setVisibility(View.GONE);
                        AppConfig.hideLoading(dialog);
                    }

                } catch (Exception e)
                {
                    AppConfig.hideLoading(dialog);
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(userselctionopt.equalsIgnoreCase("DDK"))
        {
            view = inflater.inflate(R.layout.fragment_cash_outnew, container, false);
            tvSelectDdkAddress = view.findViewById(R.id.tvSelectDdkAddress);
            tvSelectDdkAddress.setOnClickListener(this);
        }else
        {
            view = inflater.inflate(R.layout.fragment_cash_samkoinlayout, container, false);
        }
        mContext = getActivity();
        transfersamwallet=view.findViewById(R.id.transfersamwallet);
        CashOutFragmentasub=CashOutFragmentNew.this;
        tvMinimum=view.findViewById(R.id.tvMinimum);
        userData = AppConfig.getUserData(mContext);
        //*..........for trasnaction
        ConnectionDetector cd = new ConnectionDetector(getActivity());
        if (!cd.isConnectingToInternet())
        {
            Toast.makeText(mContext, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
        else {
            if(userselctionopt.equalsIgnoreCase("DDK"))
            {
                getTransactionFees(Constant.JWTToken);
            }else
            {
            }
        }
        /*----------------------------------------Payment Php-------------------------------------------*/
        lytPhpPayment = view.findViewById(R.id.lytPhpPayment);
        ddkfees=view.findViewById(R.id.ddkfees);
        tvAvailableDDK = view.findViewById(R.id.tvAvailableDDK);
        tvConversionRate = view.findViewById(R.id.tvConversionRate);
        tvTotalInPhp = view.findViewById(R.id.tvTotalInPhp);
        transaction_fees=view.findViewById(R.id.transaction_fees);
        etDDK = view.findViewById(R.id.etDDK);
        etReceiverGcashMobile = view.findViewById(R.id.etReceiverGcashMobile);
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
        btnFlagImage = view.findViewById(R.id.btnFlagImage);
        btnShowAccount = view.findViewById(R.id.btnShowAccount);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        rvBankList.setLayoutManager(mLayoutManager);
        rvBankList.setItemAnimator(new DefaultItemAnimator());
        rvBankList.setAdapter(bankListAdapter);
        //hint
        TextView totleinhint=view.findViewById(R.id.totleinhint);
        countrydata=userData.getUser().country.get(0).country;
        String aviabelcashoutamount="0";
        String styledText1="";
        String amountminim=AppConfig.getStringPreferences(mContext, Constant.MINIMUM_CASHOUT);
        if(countrydata.equalsIgnoreCase("philippines"))
        {
            usercountryselect=1;
            totleinhint.setText("Total in PHP");
            String styledTextddk="";
            if(userselctionopt.equalsIgnoreCase("DDK"))
            {
                styledTextddk= "<font color='black'>Available DDK : </font>"+aviabelcashoutamount+"";
            }else
            {
               // getSettingServerOnTab(getActivity(),"php_buy_from_sam_koin");
                BigDecimal currentbalance;
                BigDecimal roundhaldv = null;
                String samkoinvalue=App.pref.getString(Constant.SAMKOIN_Balance, "");
                if(samkoinvalue!=null)
                {
                    currentbalance = new BigDecimal(samkoinvalue);
                    roundhaldv = currentbalance.setScale(4, BigDecimal.ROUND_FLOOR);
                }
               styledTextddk= "<font color='black'>SAM Koin : </font>"+roundhaldv+"";
            }
            tvAvailableDDK.setText(Html.fromHtml(styledTextddk), TextView.BufferType.SPANNABLE);
            styledText1 = "<font color='black'>Minimum Sell : </font>" + amountminim + " PHP";

        }else if(countrydata.equalsIgnoreCase("indonesia"))
        {
            usercountryselect=3;
            totleinhint.setText("Total in IDR");
            String styledTextddk="";
            if(userselctionopt.equalsIgnoreCase("DDK"))
            {
                styledTextddk= "<font color='black'>Available DDK : </font>"+aviabelcashoutamount+"";
            }else
            {
                // getSettingServerOnTab(getActivity(),"php_buy_from_sam_koin");
                BigDecimal currentbalance;
                BigDecimal roundhaldv = null;
                String samkoinvalue=App.pref.getString(Constant.SAMKOIN_Balance, "");
                if(samkoinvalue!=null)
                {
                    currentbalance = new BigDecimal(samkoinvalue);
                    roundhaldv = currentbalance.setScale(4, BigDecimal.ROUND_FLOOR);
                }
                styledTextddk= "<font color='black'>SAM Koin : </font>"+roundhaldv+"";
            }
            tvAvailableDDK.setText(Html.fromHtml(styledTextddk), TextView.BufferType.SPANNABLE);
            styledText1 = "<font color='black'>Minimum Sell : </font>" + amountminim + " IDR";
        }else
        {
            usercountryselect=2;
            totleinhint.setText("Total in AUD");
            styledText1 = "<font color='black'>Minimum Sell : </font>" + amountminim + " AUD";
            String styledTextddk="";
            if(userselctionopt.equalsIgnoreCase("DDK"))
            {
                styledTextddk= "<font color='black'>Available DDK : </font>"+aviabelcashoutamount+" ";
            }else
            {
                BigDecimal currentbalance;
                BigDecimal roundhaldv = null;
                String samkoinvalue=App.pref.getString(Constant.SAMKOIN_Balance, "");
                if(samkoinvalue!=null)
                {
                    currentbalance = new BigDecimal(samkoinvalue);
                    roundhaldv = currentbalance.setScale(4, BigDecimal.ROUND_FLOOR);
                }
                styledTextddk= "<font color='black'>SAM Koin : </font>"+roundhaldv+" ";
            }
            tvAvailableDDK.setText(Html.fromHtml(styledTextddk), TextView.BufferType.SPANNABLE);
        }

        //for new layout
        tvMinimum.setText(Html.fromHtml(styledText1), TextView.BufferType.SPANNABLE);
        //for stratinh hint
       //...................

        Log.d("country",countrydata);
        String curkjnew= App.pref.getString(Constant.PHP_Functionality_View, "");
        if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") || countrydata.equalsIgnoreCase("australia") || countrydata.equalsIgnoreCase("indonesia")))
        {
            lytPhpPayment.setVisibility(View.VISIBLE);
            lytSelectBank.setVisibility(View.GONE);
            initTheCredentialView();
            String countryid=userData.getUser().country.get(0).id;
            getBankList(countryid);
            if(userselctionopt.equalsIgnoreCase("DDK"))
            {
                latestDDKPrice();
                getCredentialsCall();
            }else
            {
                latestSellBuyPrice();
            }

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
        btnShowAccount.setOnClickListener(this);
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
                               tvTotalInPhp.setText("");
                               String styledText = "<font color='black'>Fee :</font> 0 DDK";
                               ddkfees.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
                               //ddkfees.setText("Fees: 0 DDK");
                               String styledText1 = "<font color='black'>Transaction Fees :</font> 0 ";
                               transaction_fees.setText(Html.fromHtml(styledText1), TextView.BufferType.SPANNABLE);
                               transaction_fees.setVisibility(View.VISIBLE);
                               totalPhpValue = "";
                           }else
                           {
                               BigDecimal etDDKValue = new BigDecimal(etDDK.getText().toString().trim());
                               if (etDDKValue.compareTo(BigDecimal.ZERO) == 0) {

                               } else
                               {
                                   String transcationfee;
                                   if(countrydata!=null && (countrydata.equalsIgnoreCase("indonesia")))
                                   {
                                       transcationfee=App.pref.getString(Constant.sellsam_transaction_feeskpay, "");
                                   }else
                                   {
                                       transcationfee=App.pref.getString(Constant.sellsam_transaction_fees, "");
                                   }
                                   BigDecimal transcationfeeserver=new BigDecimal(transcationfee);
                                   BigDecimal transactionfeeam = etDDKValue.multiply(transcationfeeserver);
                                   BigDecimal transactionfeemain = transactionfeeam.divide(BigDecimal.valueOf(100));
                                   //set datay
                                   String styledText1 = "<font color='black'>Transaction Fees : </font>"+transactionfeemain.toPlainString();
                                   transaction_fees.setText(Html.fromHtml(styledText1), TextView.BufferType.SPANNABLE);
                                   transaction_fees.setVisibility(View.VISIBLE);
                                   //............
                                   //transactionfeemain
                                   BigDecimal samconvert=etDDKValue.subtract(transactionfeemain);
                                   BigDecimal finalconversion = samconvert.multiply(sellTemp);
                                   convertUsdToPhp(finalconversion);
                                   String styledText = "<font color='black'>Fee :</font> "+transactionfeemain.toPlainString().toString()+" DDK";
                                   ddkfees.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
                               }
                           }
                    } else {
                        tvTotalInPhp.setText("");
                        String styledText = "<font color='black'>Fee :</font> 0 DDK";
                        ddkfees.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
                        String styledText1 = "<font color='black'>Transaction Fees :</font> 0 ";
                        transaction_fees.setText(Html.fromHtml(styledText1), TextView.BufferType.SPANNABLE);
                        transaction_fees.setVisibility(View.VISIBLE);
                        totalPhpValue = "";
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        //for bank list
        recyclerview1=view.findViewById(R.id.recyclerview1);
        recyclerviewGridView=view.findViewById(R.id.recyclerviewGridView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview1.setLayoutManager(layoutManager);
        recyclerview1.setItemAnimator(new DefaultItemAnimator());
        getUserBankList();
        allTypeCashoutFragmentAdapter = new AllTypeCashoutFragmentAdapter(usercountryselect,bankList, getActivity(),CashOutFragmentNew.this);
        recyclerviewGridView.setAdapter(allTypeCashoutFragmentAdapter);

        transfersamwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sendOtp();
            }
        });
        return view;
    }

    private void sendPhpAmountinWallet(String totalphpamountvalue,String userinputamount,String sam_koin_conversion,String fee)
    {
        final ProgressDialog pd=new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Please wait");
        pd.show();
        String samkoinadd=App.pref.getString(Constant.SAMKOIN_ADD, "");
        String transcationfee=App.pref.getString(Constant.sellsam_transaction_fees, "");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("sam_amount", userinputamount);
        hm.put("fee", fee);
        hm.put("conversion_rate", sam_koin_conversion);
        hm.put("sender_address",samkoinadd);
        hm.put("crypto_type","sam_koin");
        hm.put("transaction_fees", transcationfee);
        hm.put("php_amount",totalphpamountvalue);

        Log.d("php user ", hm.toString());
        AppConfig.getLoadInterface().sendSamKoinInPHPAmount(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken),hm).enqueue(new Callback<mapSubscriptionModel>() {
            @Override
            public void onResponse(Call<mapSubscriptionModel> call, Response<mapSubscriptionModel> response) {
                try {
                    Log.d("sam erro par invi",response.body().toString());
                    if (response.isSuccessful() && response.body() != null)
                    {
                        if (response.body().getStatus()==1)
                        {
                            pd.dismiss();
                            String text_id=response.body().getData().getTxtId();
                            CommonMethodFunction.transactionStatus("Completed",getActivity(),text_id,"");
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
            public void onFailure(Call<mapSubscriptionModel> call, Throwable t)
            {
                pd.dismiss();
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
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
                        String totalphpamountvalue=tvTotalInPhp.getText().toString();
                        String userinputamount=etDDK.getText().toString();
                        String sam_koin_conversion=sellTemp+"";
                        String fee= transaction_fees.getText().toString().replace("Transaction Fees : ","");
                        sendPhpAmountinWallet(totalphpamountvalue,userinputamount,sam_koin_conversion,fee);
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

    public String getValueData(String ddstatus)
    {
        String ddkamount;
        if(userselctionopt.equalsIgnoreCase("DDK"))
        {
            if(ddstatus.equalsIgnoreCase("address"))
            {
                ddkamount = tvSelectDdkAddress.getText().toString();
            }else if(ddstatus.equalsIgnoreCase("php"))
            {
                ddkamount = tvTotalInPhp.getText().toString();
            }else if(ddstatus.equalsIgnoreCase("secrate"))
            {
                ddkamount = ddkSecret;
            }else if(ddstatus.equalsIgnoreCase("conversionrate"))
            {
                ddkamount = tvConversionRate.getText().toString();
            }else
            {
                ddkamount = etDDK.getText().toString();
            }
        }else
        {
            if(ddstatus.equalsIgnoreCase("php"))
            {
                ddkamount = tvTotalInPhp.getText().toString();
            }else if(ddstatus.equalsIgnoreCase("secrate"))
            {
                ddkamount = ddkSecret;
            }else if(ddstatus.equalsIgnoreCase("conversionrate"))
            {
                ddkamount = tvConversionRate.getText().toString();
            }else
            {
                ddkamount = etDDK.getText().toString();
            }
        }
         return ddkamount;
    }

    public String checkValidation()
    {
        String status = "complete";
        String addre="";
        if(userselctionopt.equalsIgnoreCase("DDK"))
        {
            addre = tvSelectDdkAddress.getText().toString();
            String ddkamount = etDDK.getText().toString();
            String totoalphpvale=tvTotalInPhp.getText().toString();
            String cashoutamount=AppConfig.getStringPreferences(mContext, Constant.MINIMUM_CASHOUT);
            BigDecimal fiveHundred = new BigDecimal(cashoutamount);
            if (addre.equals("") || addre.equals(null)){
                status = "SelectDDkAddress";
                DataNotFound(getActivity(),"Please Select DDK Address");
            }else if (ddkamount.equals("")|| ddkamount.equals(null)){
                status = "Enter Amount";
                DataNotFound(getActivity(),"Please Enter DDK Amount");
            }else if (fiveHundred.compareTo(new BigDecimal(totoalphpvale)) == 1)
            {
                status = "Minimum amount";
                if(usercountryselect==1)
                {
                    DataNotFound(getActivity(),"Minimum amount will be PHP "+cashoutamount);

                }else if(usercountryselect==2)
                {
                    DataNotFound(getActivity(),"Minimum amount will be AUD "+cashoutamount);

                }else if(usercountryselect==3)
                {
                    DataNotFound(getActivity(),"Minimum amount will be IDR "+cashoutamount);
                }
            }else {
                status = "complete";
            }
        }else
        {
            String ddkamount = etDDK.getText().toString();
            String totoalphpvale=tvTotalInPhp.getText().toString();
            String cashoutamount=AppConfig.getStringPreferences(mContext, Constant.MINIMUM_CASHOUT);
            BigDecimal fiveHundred = new BigDecimal(cashoutamount);
            if (ddkamount.equals("")|| ddkamount.equals(null)){
                status = "Enter Amount";
                DataNotFound(getActivity(),"Please Enter SAM Koin Amount");
            }else if (fiveHundred.compareTo(new BigDecimal(totoalphpvale)) == 1)
            {
                status = "Minimum amount";
                if(usercountryselect==1)
                {
                    DataNotFound(getActivity(),"Minimum amount will be PHP "+cashoutamount);

                }else if(usercountryselect==2)
                {
                    DataNotFound(getActivity(),"Minimum amount will be AUD "+cashoutamount);

                }else if(usercountryselect==3)
                {
                    DataNotFound(getActivity(),"Minimum amount will be IDR "+cashoutamount);
                }
            }else {
                status = "complete";
            }
        }
        return status;
    }

    public String amountValue(){
        String ddkvalue = etDDK.getText().toString();
        return ddkvalue;
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

   private void latestSellBuyPrice()
        {
            buyTemp = UserModel.getInstance().samkoinBuyPrice;
            sellTemp = UserModel.getInstance().samkoinSellPrice;
            String styledText = "<font color='black'>Conversion rate : </font> "+String.format("%.6f" , sellTemp) + " USDT";
            tvConversionRate.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
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
                    BigDecimal buy,sell;
                    if(userselctionopt.equalsIgnoreCase("DDK"))
                    {
                        buy = usd.multiply(UserModel.getInstance().ddkBuyPercentage).divide(ONE_HUNDRED);
                        sell = usd.multiply(UserModel.getInstance().ddkSellPercentage).divide(ONE_HUNDRED);
                    }else
                    {
                        buy = usd.multiply(UserModel.getInstance().samkoinBuyPercentage).divide(ONE_HUNDRED);
                        sell = usd.multiply(UserModel.getInstance().samkoinSellPercentage).divide(ONE_HUNDRED);
                    }
                    buyTemp = buy.add(usd);
                    sellTemp = usd.subtract(sell);
                    String styledText = "<font color='black'>Conversion rate : </font> "+sellTemp;
                    tvConversionRate.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

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

                //.......
                String styledText1="";
                String countrydata=userData.getUser().country.get(0).country;
                styledText1 = "<font color='black'>Available DDk : </font>"+ddk+" ";
                //........
                tvAvailableDDK.setText(Html.fromHtml(styledText1), TextView.BufferType.SPANNABLE);

            }
        });
    }

    private void convertUsdToPhp(BigDecimal ddkvalue)
    {
        String amountdata=ddkvalue+"";
        HashMap<String, String> hm = new HashMap<>();
        hm.put("amount",amountdata);
        String countrydata=userData.getUser().country.get(0).country;
        if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines"))) {
            hm.put("to","php");

        }else if(countrydata!=null && (countrydata.equalsIgnoreCase("indonesia"))) {
            hm.put("to","idr");

        }else {
            hm.put("to","aud");
        }
        AppConfig.getLoadInterface().convertUsdToAny(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                dialog.dismiss();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        Log.d("ddk response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getBoolean("success"))
                        {
                            totalPhpValue = object.getString("result");
                            BigDecimal totalamouny=new BigDecimal(totalPhpValue);
                            tvTotalInPhp.setText("" + totalamouny.toPlainString());
                            if(etDDK.getText().toString().equalsIgnoreCase(""))
                            {
                                tvTotalInPhp.setText("");
                                String styledText = "<font color='black'>Fee :</font> 0 DDK";
                                ddkfees.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
                                String styledText1 = "<font color='black'>Transaction Fees :</font> 0 ";
                                transaction_fees.setText(Html.fromHtml(styledText1), TextView.BufferType.SPANNABLE);
                                transaction_fees.setVisibility(View.VISIBLE);
                                totalPhpValue = "";
                            }
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

    public void getUserBankList()
    {
        String countryid=userData.getUser().country.get(0).id;
        HashMap<String, String> hm = new HashMap<>();
        hm.put("country_id", countryid);
        AppConfig.getLoadInterface().userBankList(AppConfig.getStringPreferences(mContext, Constant.JWTToken),hm).enqueue(new Callback<UserBankListResponse>() {
            @Override
            public void onResponse(Call<UserBankListResponse> call, Response<UserBankListResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    String responseData = response.body().toString();
                    if (response.body().getStatus() == 1)
                    {
                        Log.d("checking","::");
                        userBankLists = new ArrayList<UserBankList>();
                        if(userBankLists.size()>0)
                        {
                            userBankLists.clear();
                        }
                        userBankLists.addAll(response.body().getData());
                        Log.d("checking",userBankLists.get(0).getBank().getBankName());
                        UserBankList userBankList = new UserBankList();
                        UserBankListDetails userBankListDetails = new UserBankListDetails();
                        userBankListDetails.setBankName("Addoption");
                        userBankList.setBank(userBankListDetails);
                        userBankLists.add(userBankList);
                        //.....
                        mAdapter = new CashoutFragmenAdapter(usercountryselect,userBankLists, getActivity(), CashOutFragmentNew.this);
                        recyclerview1.setAdapter(mAdapter);

                    }else if (response.body().getStatus() == 4) {

                        ShowServerPost((Activity)mContext,"ddk server error convert usdt ");

                    } else {
                        userBankLists = new ArrayList<UserBankList>();
                      //  AppConfig.showToast(response.body().getMsg());
                        UserBankList userBankList = new UserBankList();
                        UserBankListDetails userBankListDetails = new UserBankListDetails();
                        userBankListDetails.setBankName("Addoption");
                        userBankList.setBank(userBankListDetails);
                        userBankLists.add(userBankList);
                        //.....
                        mAdapter = new CashoutFragmenAdapter(usercountryselect,userBankLists, getActivity(), CashOutFragmentNew.this);
                        recyclerview1.setAdapter(mAdapter);
                    }
                } else {
                   // AppConfig.showToast("Server error");
                }
            }

            @Override
            public void onFailure(Call<UserBankListResponse> call, Throwable t)
            {
                errordurigApiCalling((Activity) mContext,t.getMessage());
                dialog.dismiss();
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
                 //       Toast.makeText(getActivity(), retrofitMesage, Toast.LENGTH_SHORT).show();
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
                   // Toast.makeText(getActivity(), ""+retrofitMesage, Toast.LENGTH_SHORT).show();
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
    //................
    private void showBankAccountList() {

        final ArrayList<BankAccounts> bankAccounts = new ArrayList<>();

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


    private void getBankList(String country_id) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("country_id", country_id);
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Get Bank List....");
        AppConfig.getLoadInterface().getTheBankList(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<BankList>() {
            @Override
            public void onResponse(Call<BankList> call, Response<BankList> response) {
                dialog.dismiss();
                if (response.isSuccessful())
                {
                    Log.d("response banklist",response.toString());
                    if (response.body().status == 1)
                    {
                        bankList.clear();
                        imagePath = response.body().image_path;
                        bankList.addAll(response.body().data);
                        int sizedata=bankList.size();
                        bankListAdapter.updateData(bankList, response.body().image_path);
                        //for list
                        if(bankList.size()>8)
                        {
                            bankListnewli.addAll(bankList);
                            bankList.clear();
                            for(int i=0;i<bankListnewli.size();i++)
                            {
                                if(bankList.size()==8)
                                {
                                    break;
                                }else
                                {
                                    bankList.add(bankListnewli.get(i));
                                }
                            }
                        }
                        BankList.BankData bankData4 = new BankList.BankData();
                        bankData4.setBank_name("All");
                        bankData4.setImage("All");
                        bankList.add(bankData4);
                        banknotabv=response.body().bank_status;
                        msgbank=response.body().msg;
                        allTypeCashoutFragmentAdapter.updateData(bankList, response.body().image_path);
                        //......

                    } else if (response.body() != null && response.body().status == 3) {
                        //AppConfig.showToast(response.body().msg);
                    }else {
                       // AppConfig.showToast(response.body().msg);
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

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
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

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyView> {

        private List<String> list;

        public class MyView extends RecyclerView.ViewHolder {

            public TextView textView;

            public MyView(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.titleview);
            }
        }

        public RecyclerViewAdapter(List<String> horizontalList) {
            this.list = horizontalList;
        }

        @Override
        public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontalitem, parent, false);

            return new MyView(itemView);
        }
        @Override
        public void onBindViewHolder(final MyView holder, final int position) {
            holder.textView.setText(list.get(position));
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
