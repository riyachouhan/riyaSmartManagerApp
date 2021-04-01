package com.ddkcommunity.fragment.projects;


import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.ddkcommunity.activities.WebViewActivity;
import com.ddkcommunity.adapters.CurrencyListAdapter;
import com.ddkcommunity.interfaces.GetAvailableValue;
import com.ddkcommunity.interfaces.GetPoolingResponse;
import com.ddkcommunity.model.exchange.CurrencyList;
import com.ddkcommunity.model.wallet.WalletResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ncorti.slidetoact.SlideToActView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayUsingCoinPaymentFragment extends Fragment implements View.OnClickListener{


    private float ddkCreditCard;

    public PayUsingCoinPaymentFragment() {

    }
    private EditText etDDKCoin;
    private BigDecimal total=new BigDecimal("0");
    private BigDecimal sellTemp, buyTemp;
    private Context mContext;
    private TextView tvTransactionId, tvOrderStatus;
    private TextView btnGoHome;
    private AppCompatImageView btnCopyTransactionId;
    private ImageView ivTransactionCreateCheck, ivTransactionCreateUncheck, ivOrderStatusIconUnCheck, ivOrderStatusIconCheck;
    private FrameLayout progress_bar;
    private ClipboardManager clipboard;
    private ClipData clip;

    private TextView tvConversionCoin, tvRateCoin, tvEsTextCoin,
            tvEstimatedFees, tvTotalCoin, tvSelectCurrencyType, tvCoinTransactionFee, tvCryptoCurrencyType, tvTotalCurrencyPayment;
    private ArrayList<CurrencyList.CurrencyData> currencyLists = new ArrayList<>();

    private BottomSheetDialog currencyDialog;
    private String currencyCode;
    private BigDecimal totalCurrencyAmount;
    private BigDecimal totalTransFee;
    private double currencyTransactionPercent;
    private CurrencyListAdapter currencyListAdapter;
    String userEnterDDk="",con_amount_usd="";
    SlideToActView  slide_custom_icon;

    @Override
    public void onResume() {
        super.onResume();
        // HomeActivity.setHomeItem(getActivity(), R.id.profile);
        MainActivity.setTitle("Using Coin Payment");
        MainActivity.enableBackViews(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_using_coin_payment, container, false);
        mContext = getActivity();
        etDDKCoin = view.findViewById(R.id.etDDKCoin);
        tvConversionCoin = view.findViewById(R.id.tvConversionCoin);
        tvRateCoin = view.findViewById(R.id.tvRateCoin);
        slide_custom_icon=view.findViewById(R.id.slide_custom_icon);
        tvEsTextCoin = view.findViewById(R.id.tvEsTextCoin);
        tvEstimatedFees = view.findViewById(R.id.tvEstimatedFeesCoin);
        tvTotalCoin = view.findViewById(R.id.tvTotalCoin);
        tvCoinTransactionFee = view.findViewById(R.id.tvCoinTransactionFee);
        tvCryptoCurrencyType = view.findViewById(R.id.tvCryptoCurrencyType);
        tvTotalCurrencyPayment = view.findViewById(R.id.tvTotalCurrencyPayment);
        tvSelectCurrencyType = view.findViewById(R.id.tvSelectCurrencyType);
        tvSelectCurrencyType.setOnClickListener(this);
        etDDKCoin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!AppConfig.isStringNullOrBlank(etDDKCoin.getText().toString())) {
                    try {
                        ddkCreditCard = Float.parseFloat(etDDKCoin.getText().toString());
                    } catch (NumberFormatException e) {
                        ddkCreditCard = (float) 0.0;
                    }
                    BigDecimal etDDKValue = new BigDecimal(etDDKCoin.getText().toString());
                    total = etDDKValue.multiply(buyTemp);
                    tvEstimatedFees.setText("0.0");
                    tvConversionCoin.setText(String.format(Locale.ENGLISH, "%.4f", total));

                    if (ddkCreditCard > 0) {
                        tvTotalCoin.setText(String.format(Locale.ENGLISH, "%.4f", total));
                    } else {
                        AppConfig.showToast("Please enter amount greater than 0.");
                    }
                    userEnterDDk=etDDKValue+"";
                    con_amount_usd=tvConversionCoin.getText().toString();

                } else {
                    tvEstimatedFees.setText("0");
                    tvTotalCoin.setText("0");
                    total = new BigDecimal(0);
                }
                tvTotalCurrencyPayment.setText("");
                tvCoinTransactionFee.setText("");
                tvCryptoCurrencyType.setText("");
                tvSelectCurrencyType.setText("");
                currencyCode = "";
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        getCurrencyList();

        view.findViewById(R.id.btnGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        slide_custom_icon.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                String subscriptionamount=AppConfig.getStringPreferences(mContext, Constant.MINIMUM_SUBSCRIPTION);
                BigDecimal threeHundred = new BigDecimal(subscriptionamount);
                if (threeHundred.compareTo(total) == 1) {
                    Toast.makeText(mContext, "Please enter DDK coin equal to "+subscriptionamount+"USDT amount", Toast.LENGTH_SHORT).show();
                    slideToActView.resetSlider();
                    return;
                }
                if (currencyCode.isEmpty()) {
                    slideToActView.resetSlider();
                    Toast.makeText(mContext, "Please select crypto currency type.", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendData("", "coin_payments", "" + buyTemp);
                slideToActView.resetSlider();
            }
        });
        String walletidval=App.pref.getString(Constant.WALLET_ID, "");
        getWalletDetails(walletidval);
        latestDDKPrice();
        return view;
    }

    public void getWalletDetails(String walletid) {

        UserModel.getInstance().getWalletDetails(2,walletid, mContext, new GetAvailableValue() {
            @Override
            public void getValues(String ddk, WalletResponse successResponse) {

                App.editor.putString(Constant.PUBLIC_KEY, successResponse.getWallet().getPublicKey());
                App.editor.putString(Constant.Wallet_ADD, successResponse.getWallet().getAddress());
                App.editor.putString(Constant.senderDDKAddress, successResponse.getWallet().getAddress());
                App.editor.putString(Constant.WALLET_ID, successResponse.getWallet().getWalletId());
                App.editor.apply();
                }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvSelectCurrencyType) {
            if (etDDKCoin.getText().toString().isEmpty()) {
                AppConfig.showToast("Please Enter the ddk.");
                return;
            }
            if (currencyLists.size() > 0) {
                if (!currencyDialog.isShowing()) {
                    currencyDialog.show();
                } else {
                    currencyDialog.dismiss();
                }
            }
        }
    }
    private void latestDDKPrice() {
        buyTemp = UserModel.getInstance().ddkBuyPrice;
        sellTemp = UserModel.getInstance().ddkSellPrice;

        tvRateCoin.setText(String.format(String.format(Locale.ENGLISH, "%.4f", buyTemp)+" USDT"));
//
    }

    private void sendData(String tokenID, String paymentType, String ddkConversion)
    {
        String totalincrypto=tvTotalCoin.getText().toString();
        AppConfig.showLoading("Send Data..", mContext);
        UserModel.getInstance().sendDataToServer("","","","","Coin payment",con_amount_usd,totalincrypto,con_amount_usd,userEnterDDk,mContext, "", paymentType, ddkConversion, etDDKCoin.getText().toString().trim(),
                "", "", currencyCode, "" + totalCurrencyAmount, "" + totalTransFee, "" + currencyTransactionPercent, new GetPoolingResponse() {
                    @Override
                    public void getResponse(JSONObject jsonObject) {

                        try {
                            if (jsonObject.getInt(Constant.STATUS) == 1) {
                                JSONObject dataJson = jsonObject.getJSONObject("data");
                                JSONObject resultObject = dataJson.getJSONObject("result");
                                resultObject.getString("checkout_url");
                                mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                                        .putExtra("fileName", "coin_payment").putExtra("url", resultObject.getString("checkout_url")));
                                getActivity().finish();
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

                        } catch (JSONException e) {
                            AppConfig.hideLoader();
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void getCurrencyList() {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View dialogView = layoutInflater.inflate(R.layout.popup_wallet_pooling, null);
//        pw = new PopupWindow(view1, RelativeLayout.LayoutParams.MATCH_PARENT, 650, true);
        currencyDialog = new BottomSheetDialog(getActivity(), R.style.DialogStyle);
        currencyDialog.setContentView(dialogView);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        final EditText searchEt = dialogView.findViewById(R.id.search_ET);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        currencyListAdapter = new CurrencyListAdapter(currencyLists, mContext, new CurrencyListAdapter.SetOnItemClickListener() {
            @Override
            public void onItemClick(String code, String currencyName) {
                tvSelectCurrencyType.setText(currencyName);
                hideKeyBoard();
                currencyDialog.dismiss();
                try {
                    currencyCode = code;
                    tvCryptoCurrencyType.setText(currencyName);
                    getCurrencyConvertRate("" + total, "USDT", code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        recyclerView.setAdapter(currencyListAdapter);
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
                    filterCurrency(searchEt.getText().toString());
                } else {
                    currencyListAdapter.updateData(currencyLists);
                }
            }
        });

        AppConfig.getLoadInterface().getCurrencyList().enqueue(new Callback<CurrencyList>() {
            @Override
            public void onResponse(Call<CurrencyList> call, Response<CurrencyList> response) {
                if (response.isSuccessful()) {
                    currencyLists.clear();
                    if (response.body().status == 1 && response.body().data != null) {
                        currencyLists.addAll(response.body().data);
                        currencyListAdapter.updateData(currencyLists);
                    }
                } else {
                    AppConfig.showToast("Server error");
                }
            }

            @Override
            public void onFailure(Call<CurrencyList> call, Throwable t) {
                AppConfig.showToast("Server error");
            }
        });



    }

    private void filterCurrency(String newText) {
        if (currencyLists.size() > 0) {
            List<CurrencyList.CurrencyData> currencyData = new ArrayList<>();

            if (newText.isEmpty()) {
                currencyData.addAll(currencyLists);
            } else {
                for (CurrencyList.CurrencyData currency : currencyLists) {
                    if (currency.code.toLowerCase().contains(newText.toLowerCase()) ||
                            currency.currency_name.toLowerCase().contains(newText.toLowerCase())) {
                        currencyData.add(currency);
                    }
                }

                if (currencyData.size() == 0) {
                    AppConfig.showToast("No search data Found.");
                }
            }
            currencyListAdapter.updateData(currencyData);
        }
    }


    private void getCurrencyConvertRate(String amount, String from_currency, String to_currency) {
        if (etDDKCoin.getText().toString().isEmpty()) {
            AppConfig.showToast("Please Enter the ddk amount");
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(mContext);
        AppConfig.showLoading(dialog, "Convert Currency..");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("amount", amount);
        hm.put("from_currency", from_currency);
        hm.put("to_currency", to_currency);
        AppConfig.getLoadInterface().postConvertPrice(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        JSONObject data = jsonObject.getJSONObject("data");
                        String result = data.getString("result");
                        currencyTransactionPercent = data.getDouble("transaction_fees");

                        BigDecimal resultData = new BigDecimal(result);
                        BigDecimal ONE_HUNDRED = new BigDecimal(100);
                        totalTransFee = resultData.multiply(new BigDecimal(currencyTransactionPercent)).divide(ONE_HUNDRED);
                        totalCurrencyAmount = totalTransFee.add(resultData);
                        tvTotalCurrencyPayment.setText("" + totalCurrencyAmount);
                        tvCoinTransactionFee.setText("" + totalTransFee);

//                        byt trans_ fee=result x 1/100
//                        tot_amt=result+(resultx1/a100)
//                        a ni aaega
//                        "result": 0.0208,
//                                "transaction_fees": 1
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
                AppConfig.showToast("Server error");
            }
        });
    }


    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }



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

    private int count = 0;

    private void checkTransactionStatus(final String txt_id, final String lender_id) {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        if (count == 0) {
            AppConfig.showLoading(dialog, "Checking transaction status...");
        }
        HashMap<String, String> hm = new HashMap<>();
        hm.put("txt_id", txt_id);
        hm.put("lender_id", lender_id);

        AppConfig.getLoadInterface().checkDDKTransactionStatus(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
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
                AppConfig.showToast("Server error");
            }
        });
    }



}
