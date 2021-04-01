package com.ddkcommunity.fragment;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.interfaces.GetBTCUSDTETHPriceCallback;
import com.ddkcommunity.utilies.AppConfig;

import java.math.BigDecimal;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConvertFragment extends Fragment {
    private EditText etDDK;
    private TextView tvBtcConversion, tvUsdConversion, tvBtcRate, tvUsdRate;
    private BigDecimal btcRate, usdRate;
    private BigDecimal sellTemp;

    public ConvertFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_convert, container, false);
        etDDK = view.findViewById(R.id.etDDK);
        tvBtcConversion = view.findViewById(R.id.tvBtcConversion);
        tvBtcRate = view.findViewById(R.id.tvBtcRate);
        tvUsdConversion = view.findViewById(R.id.tvUsdConversion);
        tvUsdRate = view.findViewById(R.id.tvUsdRate);


        etDDK.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                BigDecimal totalBtc, totalUsd;
                if (!AppConfig.isStringNullOrBlank(etDDK.getText().toString())) {
                    BigDecimal ddk = new BigDecimal(0.0);

                    if (sellTemp != null) {
                        try {
                            ddk = new BigDecimal(etDDK.getText().toString());
                        } catch (NumberFormatException e) {
                            ddk = new BigDecimal(0.0);
                        }
                        totalBtc = btcRate.multiply(ddk);
                        totalUsd = sellTemp.multiply(ddk);


//                    tvBuyRate.setText("$ " + String.format("%.6f", buyTemp));
//                    tvSellRate.setText("$ " + String.format("%.6f", sellTemp));

                        tvBtcConversion.setText(String.format("%s", totalBtc));
                        tvUsdConversion.setText(String.format("%s", totalUsd));
                    }
                } else {
                    tvUsdConversion.setText("0");
                    tvBtcConversion.setText("0");
                    totalBtc = new BigDecimal(0.0);
                    totalUsd = new BigDecimal(0.0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        UserModel.getInstance().getUsdtEthBtcPriceCall(new GetBTCUSDTETHPriceCallback() {
            @Override
            public void getValues(BigDecimal btcPrice, BigDecimal eth, BigDecimal usdt, BigDecimal tron) {
                btcRate = btcPrice;
                tvBtcRate.setText(btcPrice + " BTC");

            }
        },getActivity());

//        UserModel.getInstance().getUSDCall(new GetUSDAndBTCCallback() {
//            @Override
//            public void getValues(BigDecimal btc, BigDecimal usd) {
//                if (usd != null) {
//                    usdRate = usd;
//                    BigDecimal ONE_HUNDRED = new BigDecimal(100);
//                    BigDecimal buy = usdRate.multiply(new BigDecimal(10)).divide(ONE_HUNDRED);
//                    sellTemp = usdRate.subtract(buy);
//                    tvUsdRate.setText(sellTemp + " USDT");
//                } else {
//                    AppConfig.showToast("USDT rate not found try again");
//                }
//            }
//        });
        sellTemp = UserModel.getInstance().ddkSellPrice;
        tvUsdRate.setText(UserModel.getInstance().ddkSellPrice + " USDT");

//        getDDkCall();
        AppConfig.openOkDialog1(getActivity());

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Convert DDK Coins");
        MainActivity.enableBackViews(true);
    }

}
