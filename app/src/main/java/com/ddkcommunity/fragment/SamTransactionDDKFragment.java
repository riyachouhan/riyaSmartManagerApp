package com.ddkcommunity.fragment;

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

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.model.OtpResponse;
import com.ddkcommunity.model.samBalanceModel;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.dataPutMethods;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ncorti.slidetoact.SlideToActView;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.ddkcommunity.utilies.dataPutMethods.ReplacecommaValue;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.ShowServerPost;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

public class SamTransactionDDKFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private View rootView = null;
    private TextView secret,etDDKAmount,etconversion,samwallet,tvDDKsecrate,tvSelectDdkAddress,tvAvailableDDK;
    private EditText etSamAmount;
    private SlideToActView slide_custom_icon;
    private View view;
    String sameconver;
    TextView liduid_view;
    IndicatorSeekBar seekBarWithProgress;
    String curentbalance;
    private ImageView ivTransactionCreateCheck, ivTransactionCreateUncheck, ivOrderStatusIconUnCheck, ivOrderStatusIconCheck;
    private TextView btnGoHome,ddkfees,tvMinimum;
    private TextView tvTransactionId, tvOrderStatus;
    private AppCompatImageView btnCopyTransactionId;
    private FrameLayout progress_bar;
    private ClipboardManager clipboard;
    private ClipData clip;
    String liquidvalue;
    private String otp = "";
    private StringBuffer str_top;
    BigDecimal etSamValueNew,samanother=null,etsamValue=null,finalslider=null,currentsamtoken=null,slidevaluemain=null;

    public SamTransactionDDKFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            view = inflater.inflate(R.layout.fragment_san_trasaction_ddk, container, false);
            rootView = view;
            mContext = getActivity();
            getAllViewIds();
         }

        tvSelectDdkAddress.setOnClickListener(this);

        etSamAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etSamAmount.getText().toString().length() > 0)
                {
                    if(!etSamAmount.getText().toString().equalsIgnoreCase("0") && !etSamAmount.getText().toString().equalsIgnoreCase("0."))
                    {
                        if(etSamAmount.getText().toString().equalsIgnoreCase("."))
                        {
                            Log.d("first","");
                        }else {
                            double num = Double.parseDouble(etSamAmount.getText().toString());
                            double maxmimuReddem = Double.parseDouble(AppConfig.getStringPreferences(mContext, Constant.MAXIMUM_CASHOUT));
                            Log.d("value","::"+num+"::"+maxmimuReddem);
                            if(maxmimuReddem >= num){
                                try {
                                    etsamValue = new BigDecimal(etSamAmount.getText().toString().trim());
                                    currentsamtoken = new BigDecimal(curentbalance);
                                    BigDecimal finalsamtoken = currentsamtoken.subtract(etsamValue);
                                    samwallet.setText("" + finalsamtoken.toPlainString());
                                    BigDecimal sameconvervalue = new BigDecimal(sameconver);
                                    BigDecimal ddkvalue = etsamValue.divide(sameconvervalue, 4,RoundingMode.HALF_DOWN);
                                    etDDKAmount.setText(ddkvalue.toPlainString() + "");
                                    //   Toast.makeText(getActivity(), "curret "+currentsamtoken+" sam wallet "+finalsamtoken, Toast.LENGTH_SHORT).show();

                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }else {
                                Toast.makeText(getActivity(), "Sam point cannot be greatar than "+maxmimuReddem, Toast.LENGTH_SHORT).show();
                                samwallet.setText("0.00000");
                                etSamAmount.setText("");
                                etSamAmount.setEnabled(false);
                            }
                        }


                    }
                } else {
                    etDDKAmount.setText("");
                    samwallet.setText(curentbalance);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                etSamAmount.setEnabled(true);
            }
        });


        slide_custom_icon.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView)
            {
                try {
                    if (etSamAmount.getText().toString().trim().isEmpty()) {
                        AppConfig.showToast("Enter SAM Amount");
                        slideToActView.resetSlider();
                        return;
                    }
                    if (tvSelectDdkAddress.getText().toString().trim().isEmpty()) {
                        AppConfig.showToast("Please Select DDK Address");
                        slideToActView.resetSlider();
                        return;
                    }
                    String amouhnn=etSamAmount.getText().toString();
                    etSamValueNew = new BigDecimal(etSamAmount.getText().toString().trim());
                    BigDecimal samwalletvale=new BigDecimal(curentbalance);
                    int comapre=samwalletvale.compareTo(etSamValueNew);
                    if(!amouhnn.equalsIgnoreCase("0"))
                    {
                        if (samwalletvale.compareTo(etSamValueNew) == 1 || samwalletvale.compareTo(etSamValueNew) == 0) {

                            BigDecimal mainddk=new BigDecimal(etDDKAmount.getText().toString());
                            BigDecimal liduid_view_comp=new BigDecimal(liquidvalue);
                            if(liduid_view_comp.compareTo(mainddk)==1 || liduid_view_comp.compareTo(mainddk)==0)
                            {
                                sendOtp();
                                slideToActView.resetSlider();
                            }else
                            {
                                Toast.makeText(mContext, "insufficient DDK available in SAM App", Toast.LENGTH_SHORT).show();
                                slideToActView.resetSlider();
                                return;
                            }

                        } else {
                            Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                            slideToActView.resetSlider();
                            return;
                        }
                     }else
                    {
                        Toast.makeText(mContext, "insufficient Sam balance", Toast.LENGTH_SHORT).show();
                        slideToActView.resetSlider();
                        return;
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        //latestDDKPrice();

        return rootView;
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
                } else {
                    AppConfig.showToast("USDT rate not found");
                }
            }
        },getActivity());
    }


    public void getAllViewIds()
    {
        liduid_view=rootView.findViewById(R.id.liduid_view);
        tvDDKsecrate=rootView.findViewById(R.id.tvDDKsecrate);
        tvSelectDdkAddress=rootView.findViewById(R.id.tvSelectDdkAddress);
        tvAvailableDDK=rootView.findViewById(R.id.tvAvailableDDK);
        etSamAmount=rootView.findViewById(R.id.etSamAmount);
        slide_custom_icon=rootView.findViewById(R.id.slide_custom_icon);
        samwallet=rootView.findViewById(R.id.samwallet);
        etDDKAmount=rootView.findViewById(R.id.etDDKAmount);
        etconversion=rootView.findViewById(R.id.etconversion);
        //for static data
        curentbalance=App.pref.getString(Constant.SAM_Balance,"");
        curentbalance=ReplacecommaValue(curentbalance+"");
        sameconver=App.pref.getString(Constant.SAM_CONVERSION,"");
        sameconver=ReplacecommaValue(sameconver+"");
        etconversion.setText(sameconver +"  Conversion rate");
        getDDKWholeBalaceRedeem();
        if(curentbalance.contains("-"))
        {
            curentbalance="0";
            samwallet.setText(curentbalance);
        }else
        {
            samwallet.setText(curentbalance);
        }
        seekBarWithProgress = rootView.findViewById(R.id.custom_indicator_by_java_code);
        seekBarWithProgress.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams)
            {
                try {
                        BigDecimal valuenns = new BigDecimal(seekParams.progress);
                        BigDecimal samtoken = new BigDecimal(curentbalance);
                        BigDecimal calcute = valuenns.multiply(samtoken);
                        BigDecimal fibalvalue = calcute.divide(BigDecimal.valueOf(100));
                        etSamAmount.setText("" + fibalvalue.toPlainString());
                        BigDecimal finalsamtoken = samtoken.subtract(fibalvalue);
                        samwallet.setText("" + finalsamtoken.toPlainString());
                        BigDecimal sameconvervalue = new BigDecimal(sameconver);
                        BigDecimal ddkvalue = fibalvalue.divide(sameconvervalue, 4, RoundingMode.HALF_DOWN);
                        etDDKAmount.setText(ddkvalue.toPlainString() + "");

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar)
            {
                try{
                    BigDecimal valuenns = new BigDecimal(seekBar.getProgress());
                    BigDecimal samtoken = new BigDecimal(curentbalance);
                    BigDecimal calcute = valuenns.multiply(samtoken);
                    BigDecimal fibalvalue = calcute.divide(BigDecimal.valueOf(100.46363));
                    etSamAmount.setText("" + fibalvalue.toPlainString());
                    BigDecimal finalsamtoken = samtoken.subtract(fibalvalue);
                    samwallet.setText("" + finalsamtoken.toPlainString());
                    BigDecimal sameconvervalue = new BigDecimal(sameconver);
                    BigDecimal ddkvalue = fibalvalue.divide(sameconvervalue, 4, RoundingMode.HALF_DOWN);
                    etDDKAmount.setText(ddkvalue.toPlainString() + "");

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                try{
                BigDecimal valuenns=new BigDecimal(seekBar.getProgress());
                BigDecimal samtoken=new BigDecimal(curentbalance);
                BigDecimal calcute=valuenns.multiply(samtoken);
                BigDecimal fibalvalue=calcute.divide(BigDecimal.valueOf(100));
                etSamAmount.setText(""+fibalvalue.toPlainString());
                BigDecimal finalsamtoken=samtoken.subtract(fibalvalue);
                samwallet.setText(""+finalsamtoken.toPlainString());
                BigDecimal sameconvervalue = new BigDecimal(sameconver);
                BigDecimal ddkvalue = fibalvalue.divide(sameconvervalue, 4, RoundingMode.HALF_DOWN);
                etDDKAmount.setText(ddkvalue.toPlainString() + "");
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getDDKWholeBalaceRedeem()
    {
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Please wait..........");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        Log.d("token",AppConfig.getStringPreferences(getActivity(), Constant.JWTToken));
        AppConfig.getLoadInterface().getRedeemBalance(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken)).enqueue(new Callback<samBalanceModel>() {
            @Override
            public void onResponse(Call<samBalanceModel> call, Response<samBalanceModel> response) {
                try {
                    pd.dismiss();
                    Log.d("tokentime",response.body().toString());
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus() == 1)
                        {
                            if(response.body().getData()!=null)
                            {
                                String liquid=response.body().getData().getBalance().toString();
                                String totalfrsoze=response.body().getData().getTotalFrozenAmt().toString();
                                BigDecimal forzenaount=new BigDecimal(totalfrsoze);
                                BigDecimal balance=new BigDecimal(liquid);
                                BigDecimal finalamount=balance.subtract(forzenaount);
                                BigDecimal divideao=new BigDecimal("100000000");
                                BigDecimal totalvlaue=finalamount.divide(divideao);
                                App.editor.putString(Constant.LIQUID,totalvlaue+"");
                                App.editor.apply();
                                liquidvalue=App.pref.getString(Constant.LIQUID,"");
                                liduid_view.setText(totalvlaue+"");

                            }else
                            {
                                AppConfig.showToast(response.body().getMsg());
                                Toast.makeText(mContext, "No Data Available", Toast.LENGTH_SHORT).show();
                            }

                        }else if (response.body().getStatus() == 4)
                        {
                            ShowServerPost(getActivity(),"ddk server error apibalance/get-redeem-balance");
                        }else {
                            AppConfig.showToast(response.body().getMsg());
                        }
                    } else {
                        ShowApiError(mContext,"server error apibalance/get-redeem-balance");
                    }

                } catch (Exception e) {
                    pd.dismiss();
                    ShowApiError(mContext,"error in response ninethface/token");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<samBalanceModel> call, Throwable t)
            {
                errordurigApiCalling(getActivity(),t.getMessage());
                pd.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Redeem");
        MainActivity.enableBackViews(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvSelectDdkAddress:
                dataPutMethods.showDialogForDDKAddress(view,getContext(),HomeFragment._credentialList,tvSelectDdkAddress,tvAvailableDDK,tvDDKsecrate);
                break;
        }
    }

    private int count = 0;

    private void checkTransactionStatus(final String txt_id) {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        if (count == 0) {
            AppConfig.showLoading(dialog, "Checking transaction status...");
        }
        HashMap<String, String> hm = new HashMap<>();
        hm.put("txt_id", txt_id);

        AppConfig.getLoadInterface().checReedemStatus(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
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
                                        checkTransactionStatus(txt_id);
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

    private void transactionStatus(String transactionId) {
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
                getActivity().finish();

            }
        });
        tvTransactionId.setText(transactionId);
        btnCopyTransactionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                clip = ClipData.newPlainText("Copy", tvTransactionId.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
                AppConfig.showToast("Copied");
            }
        });
        checkTransactionStatus(transactionId);
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
                    Log.d("otp value",otp);
                    sendOtpDialog();
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

    //for send otp
    private void sendOtpDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.otp_verify_dialog, null);
        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(mContext);
        alert.setView(customView);
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        final LinearLayout back_view=customView.findViewById(R.id.back_view);
        final TextView btnVerify = customView.findViewById(R.id.btnVerify);
        final EditText otp_edt1 = customView.findViewById(R.id.otp_edt11);
        final EditText otp_edt2 = customView.findViewById(R.id.otp_edt12);
        final EditText otp_edt3 = customView.findViewById(R.id.otp_edt13);
        final EditText otp_edt4 = customView.findViewById(R.id.otp_edt14);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_top = new StringBuffer();
                str_top.append(otp_edt1.getText().toString()).append(otp_edt2.getText().toString()).append(otp_edt3.getText().toString()).append(otp_edt4.getText().toString());
                if (str_top.length() > 0 && str_top.length() == 4) {
                    if (str_top.toString().equalsIgnoreCase(otp))
                    {
                        //for value
                        sendSamRequest(etSamValueNew, tvSelectDdkAddress.getText().toString(), etDDKAmount.getText().toString());
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
                dialog.dismiss();
            }
        });
    }

    private void sendSamRequest(BigDecimal etSamValue,String ddkaddress,String etDDKAmount)
    {
        //for encrypt strat
        String newceratevalue = "";
        String samamountvalue=String.valueOf(etSamValue);
        String userId =App.pref.getString(Constant.USER_ID, "");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("token", "" +samamountvalue);
        hm.put("type","decrement");//increment/decrement
        hm.put("activity","redeem_ddk");
        hm.put("ddk_address",ddkaddress);
        hm.put("ddk_amount",etDDKAmount);
        hm.put("conversion_rate",sameconver);
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Confirm Payment....");
        Log.d("sam api param",hm+" ");
        AppConfig.getLoadInterface().updateTokenRedeem(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppConfig.hideLoading(dialog);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        Log.d("eth api response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(Constant.STATUS) == 1) {
                            try {
                                String msg=object.getString("msg");
                                String transactionstatus=object.getString("data");
                                transactionStatus(transactionstatus);
                            } catch (JSONException e) {
                                AppConfig.showToast("Server error");
                                e.printStackTrace();
                            }
                        }else if (object.getInt(Constant.STATUS) == 4)
                        {
                            ShowServerPost(getActivity(),"ddk server error apibalance/get-redeem-balance");
                        } else {
                            AppConfig.hideLoading(dialog);
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } else {
                        AppConfig.showToast("Server error");
                    }
                } catch (IOException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error ");
                    e.printStackTrace();
                } catch (JSONException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error "+e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                errordurigApiCalling(getActivity(),t.getMessage());
                dialog.dismiss();
            }
        });
    }

}
