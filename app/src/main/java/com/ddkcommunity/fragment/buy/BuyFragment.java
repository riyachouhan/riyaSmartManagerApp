package com.ddkcommunity.fragment.buy;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.HomeFragment;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.App;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.Constant;
import com.ddkcommunity.utilies.dataPutMethods;
import com.ncorti.slidetoact.SlideToActView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;
import static com.ddkcommunity.utilies.dataPutMethods.showDialogCryptoData;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyFragment extends Fragment implements View.OnClickListener {
    private TextView selltype,tvAvailableDDK,tvDDKsecrate;
    private EditText ddk_ET;
    TextView  block_fee,credit_fees,total_fee;
    TextView tvSelectDdkAddressddk,tvcryptospineer;
    private Context mContext;
    View view;
    TextView tvbuy;
    private BigDecimal buyTemp, sellTemp;
    SlideToActView slide_custom_icon;
    String sendingaddress="";
    public static String blockchainfees,creditcardfeevari,totalfeesvalue;
    int sildclick=0;
    TextView toatsmsg;

    public BuyFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_buy, container, false);
        mContext = getActivity();
        //for new
        toatsmsg=view.findViewById(R.id.toatsmsg);
        tvbuy=view.findViewById(R.id.tvbuy);
        ddk_ET=view.findViewById(R.id.ddk_ET);
        slide_custom_icon=view.findViewById(R.id.slide_custom_icon);
        block_fee=view.findViewById(R.id.block_fee);
        credit_fees=view.findViewById(R.id.credit_fees);
        total_fee=view.findViewById(R.id.total_fee);
        selltype=view.findViewById(R.id.selltype);
        tvAvailableDDK=view.findViewById(R.id.tvAvailableDDK);
        tvDDKsecrate=view.findViewById(R.id.tvDDKsecrate);
        tvSelectDdkAddressddk=view.findViewById(R.id.tvSelectDdkAddressddk);
        tvcryptospineer=view.findViewById(R.id.tvcryptospineer);
        tvcryptospineer.setOnClickListener(this);
        tvSelectDdkAddressddk.setOnClickListener(this);
        ///.............
        ddk_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!AppConfig.isStringNullOrBlank(ddk_ET.getText().toString())) {
                    sildclick=0;
                    if(ddk_ET.getText().toString().equalsIgnoreCase("."))
                    {

                    }else {
                        BigDecimal etDDKValue = new BigDecimal(ddk_ET.getText().toString());
                        String wallettypevalue = tvcryptospineer.getText().toString();
                        if (wallettypevalue.equalsIgnoreCase("DDK")) {
                            sendingaddress = tvSelectDdkAddressddk.getText().toString();

                        } else if (wallettypevalue.equalsIgnoreCase("ETH")) {
                            sendingaddress = App.pref.getString(Constant.Eth_ADD, "");

                        } else if (wallettypevalue.equalsIgnoreCase("BTC")) {
                            sendingaddress = App.pref.getString(Constant.BTC_ADD, "");

                        } else if (wallettypevalue.equalsIgnoreCase("USDT")) {
                            sendingaddress = App.pref.getString(Constant.USDT_ADD, "");
                        }
                        if (etDDKValue.compareTo(BigDecimal.ZERO) != 0)
                        {
                            slide_custom_icon.setLocked(true);
                            toatsmsg.setVisibility(View.VISIBLE);
                            getFeeCount(etDDKValue + "", wallettypevalue, sendingaddress);
                        } else {
                            block_fee.setText("0.0");
                            total_fee.setText("0.0");
                            credit_fees.setText("0.0");
                        }
                    }
                } else {
                    block_fee.setText("0.0");
                    total_fee.setText("0.0");
                    credit_fees.setText("0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (!AppConfig.isStringNullOrBlank(ddk_ET.getText().toString())) {

                } else {
                    block_fee.setText("0.0");
                    total_fee.setText("0.0");
                    credit_fees.setText("0.0");
                }
            }
        });

        slide_custom_icon.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView)
            {

                if(sildclick==1)
                {
                    if (tvcryptospineer.getText().toString().trim().isEmpty()) {
                        AppConfig.showToast(":Please select Cryptocurrency");
                        slideToActView.resetSlider();
                        return;
                    }
                    if (ddk_ET.getText().toString().trim().isEmpty()) {
                        AppConfig.showToast("Enter Amount");
                        slideToActView.resetSlider();
                        return;
                    }
                    if (tvcryptospineer.getText().toString().equalsIgnoreCase("DDK")) {
                        MainActivity.addFragment(new PaymentFragment(credit_fees.getText().toString(), block_fee.getText().toString(), total_fee.getText().toString().trim(), ddk_ET.getText().toString(), sendingaddress, tvcryptospineer.getText().toString(), tvbuy.getText().toString().replace("Buy Rate : ", ""), tvDDKsecrate.getText().toString()), false);
                    } else
                    {
                        BigDecimal etDDKValue = new BigDecimal(ddk_ET.getText().toString().trim());
                        String data = "";
                        if (tvcryptospineer.getText().toString().equalsIgnoreCase("BTC")) {
                            data = "0.05";

                        } else if (tvcryptospineer.getText().toString().equalsIgnoreCase("ETH")) {
                            data = "0.25";

                        } else if (tvcryptospineer.getText().toString().equalsIgnoreCase("USDT")) {
                            data = "25";
                        }

                        BigDecimal btccondition = new BigDecimal(data);
                        int comare = etDDKValue.compareTo(btccondition);
                        if (etDDKValue.compareTo(btccondition) == 1 || etDDKValue.compareTo(btccondition) == 0) {
                            MainActivity.addFragment(new PaymentFragment(credit_fees.getText().toString(), block_fee.getText().toString(), total_fee.getText().toString().trim(), ddk_ET.getText().toString(), sendingaddress, tvcryptospineer.getText().toString(), tvbuy.getText().toString().replace("Buy Rate : ", ""), tvDDKsecrate.getText().toString()), false);
                            slideToActView.resetSlider();
                        } else {
                            Toast.makeText(mContext, "Enter Amount should be greater then or equal to " + data, Toast.LENGTH_SHORT).show();
                            slideToActView.resetSlider();
                            return;
                        }
                        //.........
                    }
                }else
                {
                    slideToActView.resetSlider();
                    return;
                }
            }
        });
        return view;
    }

    private void getFeeCount(final String entreAmout, String wallettype, String Address)
    {
        String buynewvalue=tvbuy.getText().toString().replace("Buy Rate : ","");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("amount", entreAmout);
        hm.put("crypto_type", wallettype);
        hm.put("wallet_address", Address);
        hm.put("buyfees",buynewvalue);
        String tokenvalue=AppConfig.getStringPreferences(getActivity(), Constant.JWTToken);
        Log.d("newtoken",tokenvalue);
        AppConfig.getLoadInterface().sendBuyFees(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(Constant.STATUS) == 1) {
                            try {
                            String credit_card_fees=object.getString("credit_card_fees");
                            String fees_usdt=object.getString("fees_usdt");
                            block_fee.setText(fees_usdt+"");
                            BigDecimal creditcardfee;
                            if(credit_card_fees.equalsIgnoreCase("0"))
                            {
                                creditcardfee=new BigDecimal("0.0");
                            }else
                            {
                                creditcardfee=new BigDecimal(credit_card_fees);
                            }
                            BigDecimal blockchain;
                            if(fees_usdt.equalsIgnoreCase("0"))
                            {
                                blockchain=new BigDecimal("0.0");
                            }else
                            {
                                blockchain=new BigDecimal(fees_usdt);
                            }
                            String buyvalue=tvbuy.getText().toString().replace("Buy Rate : ","");
                            BigDecimal buyvaluecount=new BigDecimal(buyvalue);
                            BigDecimal contantuserenter=new BigDecimal(entreAmout);
                            BigDecimal calcuatevaue=buyvaluecount.multiply(contantuserenter);
                            BigDecimal finatotal=calcuatevaue.add(blockchain);
                            BigDecimal finlatotalcount=finatotal.add(creditcardfee);
                                toatsmsg.setVisibility(View.INVISIBLE);
                                blockchainfees=fees_usdt+"";
                            creditcardfeevari=credit_card_fees+"";
                            totalfeesvalue=finlatotalcount+"";
                            total_fee.setText(String.format("%.5f",finlatotalcount));
                            credit_fees.setText(credit_card_fees+"");
                                sildclick=1;
                                slide_custom_icon.setLocked(false);
                                if (AppConfig.isStringNullOrBlank(ddk_ET.getText().toString()))
                                {
                                    block_fee.setText("0.0");
                                    total_fee.setText("0.0");
                                    credit_fees.setText("0.0");
                                }
                            } catch (Exception e) {
                                AppConfig.showToast("Server error");
                                e.printStackTrace();
                            }
                        } else {
                            toatsmsg.setVisibility(View.INVISIBLE);
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } else {
                        toatsmsg.setVisibility(View.INVISIBLE);
                        AppConfig.showToast("Server error");
                    }
                } catch (IOException e) {
                    toatsmsg.setVisibility(View.INVISIBLE);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                } catch (JSONException e) {
                    toatsmsg.setVisibility(View.INVISIBLE);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.setTitle("Buy Coins");
        MainActivity.enableBackViews(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvcryptospineer:
                showDialogCryptoData(ddk_ET,getActivity(),tvbuy,selltype,view,getContext(),HomeFragment.listdata,tvcryptospineer,tvSelectDdkAddressddk);
                break;

            case R.id.tvSelectDdkAddressddk:
                dataPutMethods.showDialogForDDKAddress(view,getContext(), HomeFragment._credentialList,tvSelectDdkAddressddk,tvAvailableDDK,tvDDKsecrate);
                break;

        }
    }
}
