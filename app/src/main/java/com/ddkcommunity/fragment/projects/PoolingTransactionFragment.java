package com.ddkcommunity.fragment.projects;


import android.app.Activity;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.adapters.CredentialListAdapter;
import com.ddkcommunity.adapters.PoolingTransactionAdapter;
import com.ddkcommunity.interfaces.GetAllCredential;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.projects.PoolingTransactionHistory;
import com.ddkcommunity.utilies.AppConfig;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowServerPost;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoolingTransactionFragment extends Fragment {
    private TextView tvCredential, tvTotalCancelled, tvTotalLend, tvTotalRewards, tvTotalDDKRewards, tvDateStarted;
    private List<Credential> credentialList = new ArrayList<>();
    private CredentialListAdapter adapter;
    private List<PoolingTransactionHistory.PoolingHistoryData> poolingHistoryData;
    private RecyclerView rvTransHistory;
    private PoolingTransactionAdapter poolingTransactionAdapter;
    private Context mContext;

    public PoolingTransactionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pooling_transaction, container, false);
        mContext = getActivity();
        tvCredential = view.findViewById(R.id.tvCredential);
        tvTotalLend = view.findViewById(R.id.tvTotalLend);
//        tvTotalRewards = view.findViewById(R.id.tvTotalRewards);
        tvTotalDDKRewards = view.findViewById(R.id.tvTotalDDKRewards);
        tvTotalCancelled = view.findViewById(R.id.tvTotalCancelled);
        tvDateStarted = view.findViewById(R.id.tvDateStarted);


        poolingHistoryData = new ArrayList<>();
        poolingTransactionAdapter = new PoolingTransactionAdapter(mContext, poolingHistoryData, new PoolingTransactionAdapter.SetOnItemClick() {
            @Override
            public void onItemClick(int position) {

            }
        });

        rvTransHistory = view.findViewById(R.id.rvPoolingTransaction);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvTransHistory.setLayoutManager(mLayoutManager);
        rvTransHistory.setItemAnimator(new DefaultItemAnimator());
        rvTransHistory.setAdapter(poolingTransactionAdapter);

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.popup_wallet_pooling, null);
//        pw = new PopupWindow(view1, RelativeLayout.LayoutParams.MATCH_PARENT, 650, true);
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.DialogStyle);
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
                tvCredential.setText(wallet_code);
                getTransactionHistory(wallet_code);
                hideKeyBoard();
                dialog.dismiss();
            }
        });

        recyclerView.setAdapter(adapter);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(searchEt.getText().toString());
            }
        });
        tvCredential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dialog.isShowing()) {
                    dialog.show();
                } else {
                    dialog.dismiss();
                }
            }
        });


        return view;
    }

    private void filter(String newText) {
        if (credentialList.size() > 0) {
            List<Credential> credentialListTemp = new ArrayList<>();

            if (newText.isEmpty()) {
                credentialListTemp.addAll(credentialList);
            } else {
                for (Credential event : credentialList) {
                    if (event.getName().toLowerCase().contains(newText.toLowerCase()) ||
                            event.getDdkcode().toLowerCase().contains(newText.toLowerCase())) {
                        credentialListTemp.add(event);
                    }
                }

                if (credentialListTemp.size() == 0) {
                    AppConfig.showToast("No search data Found.");
                }
            }
            adapter.updateData(credentialListTemp);
        }
    }

    private void getCredentialsCall() {
        UserModel.getInstance().getCredentialsCall(getActivity(), new GetAllCredential() {
            @Override
            public void getCredential(List<Credential> credentials) {
                if (credentials.size() > 0) {
                    getTransactionHistory(credentials.get(0).getDdkcode());
                }
                adapter.updateData(credentials);
            }
        });

    }

    private void getTransactionHistory(String address) {
        AppConfig.showLoading("Loading...", mContext);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("address", address);
        AppConfig.getLoadInterface().getPoolingTransaction(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<PoolingTransactionHistory>() {
            @Override
            public void onResponse(Call<PoolingTransactionHistory> call, Response<PoolingTransactionHistory> response) {
                AppConfig.hideLoader();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().status == 1) {
                        Log.d("transaction data",response.toString());
                        tvDateStarted.setText(response.body().totalStartDate);
                        tvTotalCancelled.setText(response.body().totalCancelled);
                        tvTotalDDKRewards.setText(response.body().totalDdkReward);
                        tvTotalLend.setText(response.body().totalLend);
//                        tvTotalRewards.setText(response.body().totalReward);

                        poolingHistoryData.clear();
                        poolingHistoryData.addAll(response.body().lendData);
                        poolingHistoryData.addAll(response.body().rewardData);

                        Collections.sort(poolingHistoryData, new Comparator<PoolingTransactionHistory.PoolingHistoryData>() {
                            @Override
                            public int compare(PoolingTransactionHistory.PoolingHistoryData o1, PoolingTransactionHistory.PoolingHistoryData o2) {
                                DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    return f.parse(o2.transactionDate).compareTo(f.parse(o1.transactionDate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    return 0;
                                }
                            }
                        });
                        poolingTransactionAdapter.updateData(poolingHistoryData);
                    }else if (response.body().status == 4)
                    {
                        ShowServerPost((Activity)mContext,"ddk server error convert usdt ");
                    } else if (response.body().status == 3) {
                        AppConfig.showToast(response.body().msg);
                        AppConfig.openSplashActivity(getActivity());
                    }
                } else {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PoolingTransactionHistory> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                AppConfig.hideLoader();
            }
        });
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}


