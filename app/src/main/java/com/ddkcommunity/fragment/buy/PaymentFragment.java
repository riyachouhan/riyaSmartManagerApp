package com.ddkcommunity.fragment.buy;


import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.ddkcommunity.adapters.CardTypeAdapter;
import com.ddkcommunity.model.CardName;
import com.ddkcommunity.model.PaymentResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.CreditCardEditTextNew;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.stripe.android.CardUtils;
import com.stripe.android.Stripe;
import com.stripe.android.StripeTextUtils;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardNumberEditText;
import com.stripe.android.view.ExpiryDateEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.ddkcommunity.fragment.buy.BuyFragment.blockchainfees;
import static com.ddkcommunity.fragment.buy.BuyFragment.creditcardfeevari;
import static com.ddkcommunity.fragment.buy.BuyFragment.totalfeesvalue;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {
    private boolean isLive = false;

    private CreditCardEditTextNew cardCVVEt, cardNameEt;
    private RecyclerView rvCardList;
    private String cardNumber, cardCVV, cardValidity, cardName;
    private ExpiryDateEditText cardValidityEt;
    private CardNumberEditText cardNumberEt;
    private Context mContext;
    private BigDecimal currentRate;
    private TextView btnGoHome;
    private FrameLayout progress_bar;
    private TextView tvTransactionId, tvOrderStatus;
    private AppCompatImageView btnCopyTransactionId;
    private ImageView ivTransactionCreateCheck, ivTransactionCreateUncheck, ivOrderStatusIconUnCheck, ivOrderStatusIconCheck;
    private String creditcardfee,cryptotype,buyamount,ddksecrate,senderaddress,blockchainfee,totalAmount, ddkValue;

    public PaymentFragment(String creditcardfee,String blockchainfee,String totalAmount, String ddkValue,String senderaddress,String cryptotype,String buyamount,String ddksecrate)
    {
        // Required empty public constructor
        this.creditcardfee=creditcardfee;
        this.cryptotype=cryptotype;
        this.buyamount=buyamount;
        this.ddksecrate=ddksecrate;
        this.blockchainfee=blockchainfee;
        this.totalAmount = totalAmount;
        this.ddkValue = ddkValue;
        this.senderaddress=senderaddress;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        mContext = getActivity();
        initCardView(view);
        currentRate = UserModel.getInstance().ddkBuyPrice;
        view.findViewById(R.id.submit_BT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                AppConfig.showLoading(dialog, "Checking Card Details..");

                Card card = getCard();
                totalAmount=totalfeesvalue;
                creditcardfee=creditcardfeevari;
                blockchainfee=blockchainfees;
                if (card != null) {
                    boolean validation = card.validateCard();
                    if (validation) {
                        Stripe stripe = null;
                        String paymentkeydeta= App.pref.getString(Constant.Strip_Payment_Key,"");
                         Log.d("stripe","s::"+paymentkeydeta);
                       // String paymentkeydeta= "pk_test_51HRfU9LYTuzcZcchoEBuMkWuOQffBISLpTqmkuyWwW1TC68gllffwGhIkxEJGnnENM1aNVu6cEWkIadE88TT8RhK00AGsbkdzN";
                        stripe=new Stripe(getActivity(),paymentkeydeta);
                        Log.d("stripe","::"+stripe);

                        //stripe = new Stripe(getContext(), buykeyStrip);
                        /*pk_live_MPDfhcqUcSIJ2pe5yI95FRDw*/

                        stripe.createToken(
                                card,
                                new TokenCallback() {
                                    public void onSuccess(Token token) {
                                        // Send token to your server
                                        AppConfig.hideLoading(dialog);
                                        String tokenId = token.getId();
                                        String rate = "";
                                        if (currentRate != null) {
                                            rate = String.format(Locale.ENGLISH, "%.6f", currentRate);
                                        }
                                        paymentCall(tokenId
                                        );
                                    }

                                    public void onError(Exception error) {
                                        // Show localized error message
                                        AppConfig.hideLoading(dialog);
                                        Toast.makeText(getContext(),
                                                error.toString(),
                                                Toast.LENGTH_LONG
                                        ).show();

                                    }
                                }
                        );
                    } else if (!card.validateNumber()) {

                        AppConfig.hideLoading(dialog);
                        AppConfig.showToast("Error : " + "The card number that you entered is invalid.");
                    } else if (!card.validateExpiryDate()) {
                        AppConfig.hideLoading(dialog);
                        AppConfig.showToast("Error : " + "The expiration date that you entered is invalid.");
                    } else if (!card.validateCVC()) {
                        AppConfig.showToast("Error : " + "The CVC code that you entered is invalid.");
                        AppConfig.hideLoading(dialog);
                    } else {
                        AppConfig.showToast("Error : " + "The card details that you entered are invalid.");
                        AppConfig.hideLoading(dialog);
                    }
                } else {
                    AppConfig.showToast("Error : " + "Enter Card Details");
                    AppConfig.hideLoading(dialog);
                }
            }
        });

        return view;
    }

    private void paymentCall(String token_id) {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            AppConfig.showLoading(dialog, "Making Payment..");
            if(blockchainfee.equalsIgnoreCase("0"))
            {
                blockchainfee="0.0";
            }
            Log.d("poaramter 1",""+blockchainfee);
            Call<ResponseBody> call = AppConfig.getLoadInterface().paymentBuy(
                    AppConfig.getStringPreferences(mContext, Constant.JWTToken),
                    AppConfig.setRequestBody(token_id),
                    AppConfig.setRequestBody(ddkValue),
                    AppConfig.setRequestBody(senderaddress),
                    AppConfig.setRequestBody(cryptotype),
                    AppConfig.setRequestBody(creditcardfee),
                    AppConfig.setRequestBody(blockchainfee),
                    AppConfig.setRequestBody(totalAmount),
                    AppConfig.setRequestBody(buyamount)
            );
            Log.d("poaramter",call+"");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try
                        {
                            String responseData = response.body().string();
                            Log.d("poaramter data",responseData);
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {

                                PaymentResponse successResponse = new Gson().fromJson(responseData, PaymentResponse.class);
                               // MainActivity.addFragment(new SuccessFragment(ddkValue, successResponse.getPayment()), false);
                                transactionStatus(successResponse.getPayment().getTransactionId(),successResponse.getMsg());
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
                        ShowApiError(getActivity(),"server error in ninethface/buy-crypto-stripe-pay");
                    }
                    AppConfig.hideLoading(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppConfig.hideLoading(dialog);
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

        private void transactionStatus(String transactionId,String msg) {
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
                public void onClick(View v)
                {
                    // getActivity().finish();
                    Intent i =new Intent(getActivity(),MainActivity.class);
                    getActivity().startActivity(i);
                }
            });

            btnCopyTransactionId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copy", tvTransactionId.getText().toString().trim());
                    clipboard.setPrimaryClip(clip);
                    AppConfig.showToast("Copied");
                }
            });

            tvTransactionId.setText(transactionId);
            ivTransactionCreateCheck.setVisibility(View.VISIBLE);
            ivTransactionCreateUncheck.setVisibility(View.GONE);
            btnGoHome.setVisibility(View.VISIBLE);
            progress_bar.setVisibility(View.GONE);
            //AppConfig.showToast(msg);
            tvOrderStatus.setText("Pending");
            ivOrderStatusIconCheck.setVisibility(View.VISIBLE);
            ivOrderStatusIconUnCheck.setVisibility(View.GONE);

        }

    private void initCardView(View view) {
        cardNameEt = view.findViewById(R.id.etCardHolderName);
        cardCVVEt = view.findViewById(R.id.etCVCNumber);
        cardValidityEt = view.findViewById(R.id.etExpiryDate);
        cardNumberEt = view.findViewById(R.id.etCardNumber);
        rvCardList = view.findViewById(R.id.rvCardList);

        final ArrayList<CardName> cardTypeList = new ArrayList<>();
        cardTypeList.add(new CardName(ContextCompat.getDrawable(mContext, R.drawable.ic_visa1), "Visa"));
        cardTypeList.add(new CardName(ContextCompat.getDrawable(mContext, R.drawable.ic_mastercard), "MasterCard"));
        cardTypeList.add(new CardName(ContextCompat.getDrawable(mContext, R.drawable.ic_amex), "American Express"));
        cardTypeList.add(new CardName(ContextCompat.getDrawable(mContext, R.drawable.ic_discover), "Discover"));
        cardTypeList.add(new CardName(ContextCompat.getDrawable(mContext, R.drawable.bt_ic_jcb), "JCB"));
        cardTypeList.add(new CardName(ContextCompat.getDrawable(mContext, R.drawable.bt_ic_unionpay), "UnionPay"));
        cardTypeList.add(new CardName(ContextCompat.getDrawable(mContext, R.drawable.bt_ic_diners_club), "Diners Club"));
        cardTypeList.add(new CardName(ContextCompat.getDrawable(mContext, R.drawable.bt_ic_unknown), "Unknown"));

        final CardTypeAdapter cardTypeAdapter = new CardTypeAdapter(cardTypeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        rvCardList.setLayoutManager(mLayoutManager);
        rvCardList.setItemAnimator(new DefaultItemAnimator());
        rvCardList.setAdapter(cardTypeAdapter);
        cardValidityEt.setErrorColor(ContextCompat.getColor(mContext, R.color.colorRed));
        cardNumberEt.setErrorColor(ContextCompat.getColor(mContext, R.color.colorRed));

        cardNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String source = s.toString();
                int length = source.length();

                if (length >= 4) {
                    String cardType = CardUtils.getPossibleCardType(cardNumberEt.getText().toString().trim());
                    cardTypeAdapter.updateData(CardUtils.getPossibleCardType(cardNumberEt.getText().toString().trim()));
                    int po = 0;
                    for (CardName cardName : cardTypeList
                    ) {
                        if (cardName.cardName.equalsIgnoreCase(cardType)) {
                            po = cardTypeList.indexOf(cardName);
                            break;
                        }
                    }
                    rvCardList.smoothScrollToPosition(po);
                    if (CardUtils.getPossibleCardType(cardNumberEt.getText().toString().trim()).equalsIgnoreCase("American Express")) {
                        mISAmEx = true;
                        setEditTextMaxLength(4);
                    } else {
                        setEditTextMaxLength(3);
                        mISAmEx = false;
                    }
                } else {
                    cardTypeAdapter.updateData("unknown");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setEditTextMaxLength(int length) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        cardCVVEt.setFilters(filterArray);
    }

    private boolean mISAmEx = false;

    public Card getCard() {
        String cardNumber = cardNumberEt.getCardNumber();
        int[] cardDate = cardValidityEt.getValidDateFields();
        if (cardNumber == null || cardDate == null || cardDate.length != 2) {
            return null;
        }

        // CVC/CVV is the only field not validated by the entry control itself, so we check here.
        int requiredLength = mISAmEx ? Card.CVC_LENGTH_AMERICAN_EXPRESS : Card.CVC_LENGTH_COMMON;
        String cvcValue = cardCVVEt.getText().toString();
        if (StripeTextUtils.isBlank(cvcValue) || cvcValue.length() != requiredLength) {
            return null;
        }

        return new Card(cardNumber, cardDate[0], cardDate[1], cvcValue);
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
