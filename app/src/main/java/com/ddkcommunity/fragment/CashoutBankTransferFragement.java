package com.ddkcommunity.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.model.OtpResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ncorti.slidetoact.SlideToActView;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashoutBankTransferFragement extends Fragment {
    private Context mContext;
    SlideToActView slide_custom_icon;
    EditText et_transferamount;
    LinearLayout lytOtp,lytBankTransferView;
    private String otp = "";

    public CashoutBankTransferFragement() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view1 = null;
        if (view1 == null)
        {
            view1 = inflater.inflate(R.layout.fragmentcahsoutbanktranferlayit, container, false);
            mContext = getActivity();
            slide_custom_icon = view1.findViewById(R.id.slide_custom_icon);
            et_transferamount = view1.findViewById(R.id.et_transferamount);
            lytBankTransferView = view1.findViewById(R.id.lytBankTransferView);
            lytOtp = view1.findViewById(R.id.lytOtp);

            slide_custom_icon.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(SlideToActView slideToActView) {
                    /*if (etReceiverGcashMobile.getText().toString().trim().isEmpty()) {
                        AppConfig.showToast("Enter the G Cash mobile number");
                        slideToActView.resetSlider();
                        return;
                    }*/
                    if (et_transferamount.getText().toString().trim().isEmpty()) {
                        AppConfig.showToast("Enter ddk amount");
                        slideToActView.resetSlider();
                        return;
                    }
                    slideToActView.resetSlider();
                   /* String cashoutamount=AppConfig.getStringPreferences(mContext, Constant.MINIMUM_CASHOUT);
                    BigDecimal fiveHundred = new BigDecimal(cashoutamount);
                    BigDecimal walletBalance = new BigDecimal(currentWalletBalance);
                    BigDecimal etDDkPayment = new BigDecimal(etDDK.getText().toString().trim());
                    // 1 means for getter then to enter amount, 0 means equals, and else means less then.
                    int comparevalue=fiveHundred.compareTo(new BigDecimal(totalPhpValue));
                    if (fiveHundred.compareTo(new BigDecimal(totalPhpValue)) == 1) {
                        Toast.makeText(mContext, "Minimum amount will be php "+cashoutamount, Toast.LENGTH_SHORT).show();
                        slideToActView.resetSlider();
                        return;
                    }
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

                        slideToActView.resetSlider();
                    }else
                    {
                        Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                        slideToActView.resetSlider();
                        return;
                    }
*/
                }
            });


        }
        return view1;
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
                    lytBankTransferView.setVisibility(View.GONE);
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

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Bank Transfer");
        MainActivity.enableBackViews(true);
    }

}
