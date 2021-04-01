package com.ddkcommunity.fragment.projects;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.utilies.AppConfig;
import com.ncorti.slidetoact.SlideToActView;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayUsingCreditCardDDkFragment extends Fragment /*implements View.OnClickListener*/ {

    private BigDecimal buyTemp;
    private BigDecimal sellTemp;
    private float ddkCreditCard;
    private BigDecimal ddkConversionValue;
    private BigDecimal tempDDKAmountWithFees;
    private String currentWalletBalance;
    String userEnterDDk="",con_amount_usd="";

    public PayUsingCreditCardDDkFragment()
    {

    }
    BigDecimal finaltottal;
    private Context mContext;
    private BigDecimal total;
    private TextView minimum_usdt, tvAvialbaleDDk,
            tvConversionCreditCard, tvRateCreditCard, tvTotalCreditCard, tvEstimatedFees;
    private EditText etDDKCreditCard;
    SlideToActView slide_custom_icon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_using_credit_card_ddk, container, false);
        mContext = getActivity();
        slide_custom_icon=view.findViewById(R.id.slide_custom_icon);
        etDDKCreditCard = view.findViewById(R.id.etDDKCreditCard);
        tvConversionCreditCard = view.findViewById(R.id.tvConversionCreditCard);
        tvRateCreditCard = view.findViewById(R.id.tvRateCreditCard);
        tvEstimatedFees = view.findViewById(R.id.tvEstimatedFees);
        tvTotalCreditCard = view.findViewById(R.id.tvTotalCreditCard);
        minimum_usdt=view.findViewById(R.id.minimum_usdt);
        String subscriptionamount=AppConfig.getStringPreferences(mContext, Constant.MINIMUM_SUBSCRIPTION);
        minimum_usdt.setText(subscriptionamount+" USDT Min.");
        tvAvialbaleDDk = view.findViewById(R.id.tvAvialbaleDDk);
        latestDDKPrice();
        tvAvialbaleDDk.setText("Available DDK: " + App.pref.getString(Constant.CurrentBalance, ""));
        currentWalletBalance=App.pref.getString(Constant.CurrentBalance, "");
        etDDKCreditCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!AppConfig.isStringNullOrBlank(etDDKCreditCard.getText().toString())) {
                    try {
                        ddkCreditCard = Float.parseFloat(etDDKCreditCard.getText().toString());
                    } catch (NumberFormatException e) {
                        ddkCreditCard = (float) 0.0;
                    }
                    BigDecimal etDDKValue = new BigDecimal(etDDKCreditCard.getText().toString());
                    BigDecimal conversionValue = AppConfig.performBigDecimalDivisionWithoutScaleOrRounding(etDDKValue, buyTemp);
                    ddkConversionValue = AppConfig.performBigDecimalDivisionWithoutScaleOrRounding(etDDKValue, buyTemp);
                    BigDecimal ONE_HUNDRED = new BigDecimal(100);
                    BigDecimal transactionFee = etDDKValue.multiply(new BigDecimal(2.9)).divide(ONE_HUNDRED);
                    transactionFee = transactionFee.add(new BigDecimal(0.30));
                    BigDecimal tempTotal = AppConfig.performBigDecimalDivisionWithoutScaleOrRounding(transactionFee, buyTemp);
                    //for data
                    total = etDDKValue.multiply(buyTemp);
                    BigDecimal tempDDKFees = etDDKValue.multiply(new BigDecimal(0.01)).divide(new BigDecimal(100));
                    tempDDKAmountWithFees = tempDDKFees.add(etDDKValue);
                    //..........
                    total = conversionValue.add(tempTotal);
                    tvEstimatedFees.setText(String.format(Locale.ENGLISH, "%.4f", tempTotal));
                    tvConversionCreditCard.setText(String.format(Locale.ENGLISH, "%.4f", conversionValue));
                    if (ddkCreditCard > 0) {
                         finaltottal=etDDKValue.add(tempTotal);
                        tvTotalCreditCard.setText(String.format(Locale.ENGLISH, "%.4f", finaltottal));
                    } else {
                       // AppConfig.showToast("Please enter amount greater than 0.");
                    }
                    userEnterDDk=etDDKValue+"";
                    con_amount_usd=tvConversionCreditCard.getText().toString();

                } else {
                    tvEstimatedFees.setText("0");
                    tvConversionCreditCard.setText("0");
                    tvTotalCreditCard.setText("0");
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
                BigDecimal walletBalance = new BigDecimal(currentpref);
                String subscriptionamount=AppConfig.getStringPreferences(mContext, Constant.MINIMUM_SUBSCRIPTION);
                BigDecimal threeHundred = new BigDecimal(subscriptionamount);
                String ddkenter=etDDKCreditCard.getText().toString().trim();
                if(ddkenter.equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please enter USDT Amount", Toast.LENGTH_SHORT).show();
                    slideToActView.resetSlider();
                    return;
                }else
                {
                        // 1 means for getter then to enter amount, 0 means equals, and else means less then.
                    if (threeHundred.compareTo(new BigDecimal(etDDKCreditCard.getText().toString().trim())) == 1) {
                        Toast.makeText(mContext, "Please enter USDT Amount equal to " + subscriptionamount + " USDT or Above", Toast.LENGTH_SHORT).show();
                        slideToActView.resetSlider();
                        return;
                    }
                        String totalamoutn= tvTotalCreditCard.getText().toString();
                        Log.e("totalamount","::"+totalamoutn);
                    /* Fragment fragment = new CreditCardPaymentFragment();
                    Bundle arg = new Bundle();
                    arg.putString("input_amount", totalamoutn);
                    arg.putString("conversion_rate",con_amount_usd);
                    arg.putString("samkoinconversion", userEnterDDk);
                    arg.putString("fee", fee);
                    arg.putString("total_usdt_subscription", total_usdt_subscription);
                    fragment.setArguments(arg);
                    MainActivity.addFragment(fragment,true);

*/
                  //  MainActivity.addFragment(new CreditCardPaymentFragment(totalamoutn,con_amount_usd,userEnterDDk,ddkConversionValue.toString(), "" + buyTemp), true);
                        slideToActView.resetSlider();
                }
            }
        });

        return view;
    }

    //......................

    /*@Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGoBack) {
            getActivity().onBackPressed();
        }
    }*/

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    private void latestDDKPrice() {

        buyTemp = UserModel.getInstance().ddkBuyPrice;
        sellTemp = UserModel.getInstance().ddkSellPrice;

        //Price will be same for both
        tvRateCreditCard.setText(String.format(Locale.ENGLISH, "%.4f", buyTemp)+" USDT");

    }

    @Override
    public void onResume() {
        super.onResume();
        // HomeActivity.setHomeItem(getActivity(), R.id.profile);
        MainActivity.setTitle("Using Credit Card");
        MainActivity.enableBackViews(true);
    }
}