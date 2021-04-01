package com.ddkcommunity.fragment.projects;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.adapters.CredentialListAdapter;
import com.ddkcommunity.adapters.WithdrawalTransactionAdapter;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.credential.CredentialsResponse;
import com.ddkcommunity.model.withdrawal.Withdrawal;
import com.ddkcommunity.model.withdrawal.WithdrawalData;
import com.ddkcommunity.utilies.AppConfig;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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

import static com.ddkcommunity.utilies.dataPutMethods.ShowServerPost;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoolingWithdrawalFragment extends Fragment implements View.OnClickListener {

    private BottomSheetDialog dialog;
    private Toast toast;
    private WithdrawalTransactionAdapter poolingTransactionAdapter;
    private float currentDDKRate = 0;
    private String currentDDKAddress;
    private Context mContext;

    public PoolingWithdrawalFragment() {

    }

    private List<Credential> eventList;
    private CredentialListAdapter adapter;
    private CheckBox btnOnCredential, btnManualEntry;
    private EditText etDDKAddress, etFirstPassphrase, etSecondPassphrase;
    private TextView tvDDKAddressDestination, tvCredential,
            btnInfoWithdrawal, tvDDKPayoutAmount, tvTotalSelected, btnConfirmWithdrawal,
            tvDayConversion, tvPayoutAmountUSD, tvWithdrawalCharge, btnGetLendList;
    private LinearLayout lytManual, lytBottom;
    private List<WithdrawalData> withdrawalDataList;
    private Boolean isCredential = true;
    private ArrayList<String> ids;
    private ArrayList<String> ddkNumbers;
    private ArrayList<String> amountUSDList;
    private ArrayList<String> ddKAddressPayoutReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_pooling_withdrawal, container, false);
        mContext = getActivity();
        etDDKAddress = view.findViewById(R.id.etDDKAddress);
        etFirstPassphrase = view.findViewById(R.id.etFirstPassphrase);
        etSecondPassphrase = view.findViewById(R.id.etSecondPassphrase);
        tvDDKAddressDestination = view.findViewById(R.id.tvDDKAddressDestination);
        tvCredential = view.findViewById(R.id.tvCredential);
        btnInfoWithdrawal = view.findViewById(R.id.btnInfoWithdrawal);
        btnGetLendList = view.findViewById(R.id.btnGetLendList);

        tvDDKPayoutAmount = view.findViewById(R.id.tvDDKPayoutAmount);
        tvTotalSelected = view.findViewById(R.id.tvTotalSelected);
        tvDayConversion = view.findViewById(R.id.tvDayConversion);
        tvPayoutAmountUSD = view.findViewById(R.id.tvPayoutAmountUSD);
        tvWithdrawalCharge = view.findViewById(R.id.tvWithdrawalCharge);
        btnConfirmWithdrawal = view.findViewById(R.id.btnConfirmWithdrawal);
        lytBottom = view.findViewById(R.id.lytBottom);

        btnConfirmWithdrawal.setOnClickListener(this);
        btnGetLendList.setOnClickListener(this);

        lytManual = view.findViewById(R.id.lytManual);

        withdrawalDataList = new ArrayList<>();


        poolingTransactionAdapter = new WithdrawalTransactionAdapter(withdrawalDataList, new WithdrawalTransactionAdapter.SetOnItemClick() {
            @Override
            public void onItemClick(ArrayList<WithdrawalData> selectedData) {

                double totalAmount = 0.0;
                double totalWithdrawalCharges = 0.0;
                double payoutAmountUsd = 0.0;
                double dDKPayoutAmount = 0.0;
                ids = new ArrayList<String>();
                ddkNumbers = new ArrayList<String>();
                amountUSDList = new ArrayList<String>();
                ddKAddressPayoutReceiver = new ArrayList<String>();
                for (WithdrawalData withdrawalData : selectedData) {
                    totalAmount = totalAmount + withdrawalData.amount;
                    ids.add("" + withdrawalData.id);
                    ddkNumbers.add("" + withdrawalData.no);
                    amountUSDList.add("" + withdrawalData.amount);
                    ddKAddressPayoutReceiver.add("" + withdrawalData.receiverAddress);
                }

                //calculate the withdrawal charges
                totalWithdrawalCharges = totalAmount * 5 / 100;

                //actual amount after deduct cancellation charges
                payoutAmountUsd = totalAmount - totalWithdrawalCharges;

                //get the ddk amount
                dDKPayoutAmount = payoutAmountUsd / currentDDKRate;

                tvTotalSelected.setText(String.format("%s", totalAmount));
                tvWithdrawalCharge.setText(String.format("%s", totalWithdrawalCharges));
                tvPayoutAmountUSD.setText(String.format("%s", payoutAmountUsd));
                tvDayConversion.setText(String.format("%s", currentDDKRate));
                tvDDKPayoutAmount.setText(String.format(Locale.getDefault(), "%.4f", dDKPayoutAmount));

            }
        });
        RecyclerView rvTransHistory = view.findViewById(R.id.rvWithdrawal);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvTransHistory.setLayoutManager(mLayoutManager);
        rvTransHistory.setItemAnimator(new DefaultItemAnimator());
        rvTransHistory.setAdapter(poolingTransactionAdapter);
        btnOnCredential = (CheckBox) view.findViewById(R.id.btnOnCredential);
        btnManualEntry = (CheckBox) view.findViewById(R.id.btnManualEntry);

        btnOnCredential.setOnClickListener(this);
        btnManualEntry.setOnClickListener(this);

        tvCredential.setOnClickListener(this);
        tvDDKAddressDestination.setOnClickListener(this);
        view.findViewById(R.id.btnInfoWithdrawal).setOnClickListener(this);

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        View dialogView = layoutInflater.inflate(R.layout.popup_wallet_pooling, null);
//        pw = new PopupWindow(view1, RelativeLayout.LayoutParams.MATCH_PARENT, 650, true);
        dialog = new BottomSheetDialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(dialogView);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        final EditText searchEt = dialogView.findViewById(R.id.search_ET);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        List<Credential> eventList = new ArrayList<>();
        getCredentialsCall();
        adapter = new CredentialListAdapter(eventList, getActivity(), searchEt, new CredentialListAdapter.SetOnItemClickListener() {
            @Override
            public void onItemClick(String wallet_code, String walletId, String passphrase) {
                if (isCredential) {
                    tvCredential.setText(wallet_code);
                    currentDDKAddress = wallet_code;
                    getWithdrawalList(wallet_code);
                } else {
                    tvDDKAddressDestination.setText(wallet_code);
                }
                hideKeyBoard();
                dialog.dismiss();
            }
        });

        recyclerView.setAdapter(adapter);

        getCurrentDDKPrice();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnOnCredential) {
            btnOnCredential.setChecked(true);
            btnManualEntry.setChecked(false);
            lytManual.setVisibility(View.GONE);
            tvCredential.setVisibility(View.VISIBLE);
            lytBottom.setVisibility(View.GONE);
        }
        if (v.getId() == R.id.btnManualEntry) {
            btnOnCredential.setChecked(false);
            btnManualEntry.setChecked(true);
            lytManual.setVisibility(View.VISIBLE);
            tvCredential.setVisibility(View.GONE);
            lytBottom.setVisibility(View.GONE);
        }
        if (v.getId() == R.id.tvCredential) {
            if (!dialog.isShowing()) {
                isCredential = true;
                dialog.show();
            } else {
                dialog.dismiss();
            }
        }
        if (v.getId() == R.id.tvDDKAddressDestination) {
            if (!dialog.isShowing()) {
                isCredential = false;
                dialog.show();
            } else {
                dialog.dismiss();
            }
        }
        if (v.getId() == R.id.btnGetLendList) {
            if (etDDKAddress.getText().toString().trim().isEmpty()) {
                AppConfig.showToast("Please enter ddk address.");
                return;
            }
            if (etFirstPassphrase.getText().toString().trim().isEmpty()) {
                AppConfig.showToast("Please enter first passphrase.");
                return;
            }
            hideKeyBoard();
            getWithdrawalList(etDDKAddress.getText().toString().trim());
        }
        if (v.getId() == R.id.btnInfoWithdrawal) {
            openCancellationCharges();
        }
        if (v.getId() == R.id.btnConfirmWithdrawal) {

            if (btnManualEntry.isChecked()) {
                if (etDDKAddress.getText().toString().isEmpty()) {
                    AppConfig.showToast("Please enter ddk address");
                    return;
                }
            } else {
                if (tvCredential.getText().toString().isEmpty()) {
                    AppConfig.showToast("Please select ddk address");
                    return;
                }
            }
            if (tvTotalSelected.getText().toString().isEmpty() || tvTotalSelected.getText().toString().equalsIgnoreCase("0.0")) {
                AppConfig.showToast("Please select amount or select check box");
                return;
            }
            if (tvDDKAddressDestination.getText().toString().isEmpty()) {
                AppConfig.showToast("Please select destination ddk address");
                return;
            }

            HashMap<String, Object> hm = new HashMap<>();
//            hm.put("user_id", App.pref.getString(Constant.USER_ID, ""));
            hm.put("TotalSelectAmount", tvTotalSelected.getText().toString().trim());
            if (btnManualEntry.isChecked()) {
                hm.put("DDkAddress", etDDKAddress.getText().toString().trim());
                currentDDKAddress = etDDKAddress.getText().toString().trim();
            } else {
                hm.put("DDkAddress", tvCredential.getText().toString().trim());
            }
            hm.put("WithdrawalChargeAmount", tvWithdrawalCharge.getText().toString().trim());
            hm.put("PayoutAmountUSD", tvPayoutAmountUSD.getText().toString().trim());
            hm.put("DayConversion", tvDayConversion.getText().toString().trim());
            hm.put("DDKPayoutAmount", tvDDKPayoutAmount.getText().toString().trim());
            hm.put("DDKAddressDestination", tvDDKAddressDestination.getText().toString().trim());
            hm.put("LenderId", ids);
            hm.put("No", ddkNumbers);
            hm.put("AmountUSD", amountUSDList);
            hm.put("DDKAddressPayoutReceiver", ddKAddressPayoutReceiver);

            AppConfig.getLoadInterface().postCancellationData(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt("status") == 1) {
                            AppConfig.showToast("Cancellation successfully.");
                            Log.d("", "");
                            getWithdrawalList(currentDDKAddress);
//                            lytBottom.setVisibility(View.GONE);
                            clearSelectedData();
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
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }
    }

    private void clearSelectedData() {
        tvTotalSelected.setText(String.format("%s", "0.0"));
        tvDDKPayoutAmount.setText(String.format(Locale.getDefault(), "%.4f", 0.0));
        tvWithdrawalCharge.setText(String.format("%s", "0.0"));
        tvPayoutAmountUSD.setText(String.format("%s", "0.0"));
        tvDayConversion.setText(String.format("%s", "0.0"));
    }

    private void openCancellationCharges() {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup, null);

        TextView closePopupBtn = (TextView) customView.findViewById(R.id.btnClose);
        TextView tvMsg = (TextView) customView.findViewById(R.id.tvMsg);
        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getActivity());
        alert.setView(customView);

        tvMsg.setText("You will be charge 5% cancellation fee as SAM PROJECT DEVELOPMENT administration and management fee");
        final AlertDialog dialog = alert.create();
        dialog.show();
        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void getCredentialsCall() {
        if (AppConfig.isInternetOn()) {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
            hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
            Call<ResponseBody> call = AppConfig.getLoadInterface().credentialList(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken),hm);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {
                                eventList = new ArrayList<>();
                                CredentialsResponse registerResponse = new Gson().fromJson(responseData, CredentialsResponse.class);

                                for (Credential credential : registerResponse.getCredentials()) {
                                    if (credential.getStatus().equalsIgnoreCase("active")) {
                                        eventList.add(credential);
                                    }
                                }
                                adapter.updateData(eventList);
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
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    private void getWithdrawalList(String address) {
        if (AppConfig.isInternetOn()) {
            HashMap<String, String> hm = new HashMap<>();
            Call<Withdrawal> call;
            if (btnManualEntry.isChecked()) {
                hm.put("address", address);
                hm.put("firstPassphrase", etFirstPassphrase.getText().toString().trim());
                hm.put("secondPassphrase", etSecondPassphrase.getText().toString().trim());
                call = AppConfig.getLoadInterface().getWithdrawalDataManual(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm);
            } else {
                hm.put("address", address);
                call = AppConfig.getLoadInterface().getWithdrawalData(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm);
            }
            call.enqueue(new Callback<Withdrawal>() {
                @Override
                public void onResponse(Call<Withdrawal> call, Response<Withdrawal> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().status == 1) {
                            poolingTransactionAdapter.updateData(response.body().data);
                            lytBottom.setVisibility(View.VISIBLE);
                            clearSelectedData();
                        } else if (response.body() != null && response.body().status == 0) {
                            AppConfig.showToast(response.body().msg);
                            lytBottom.setVisibility(View.GONE);
                        } else if (response.body() != null && response.body().status == 3) {
                            AppConfig.showToast(response.body().msg);
                            AppConfig.openSplashActivity(getActivity());
                        }else if (response.body().status == 4)
                        {
                            ShowServerPost((Activity)mContext,"ddk server error convert usdt ");
                        }
                    } else {
                        AppConfig.showToast("Server error");
                        lytBottom.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<Withdrawal> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    private void getCurrentDDKPrice() {

        UserModel.getInstance().getUSDCall(new GetUSDAndBTCCallback() {
            @Override
            public void getValues(BigDecimal btc, BigDecimal usd) {
                currentDDKRate = usd.floatValue();
            }
        },getActivity());
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
