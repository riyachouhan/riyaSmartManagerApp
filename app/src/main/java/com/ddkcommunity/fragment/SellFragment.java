package com.ddkcommunity.fragment;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.CredentialListAdapter;
import com.ddkcommunity.interfaces.GetAllCredential;
import com.ddkcommunity.interfaces.GetAvailableValue;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.wallet.WalletResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText nameEt, addressEt;
    private BigDecimal sellValue;

    public SellFragment() {
        // Required empty public constructor
    }

    private TextView tvCredential, tvProgressPer;
    private List<Credential> credentialList;
    private CredentialListAdapter adapter;
    private BottomSheetDialog dialog;
    private Context mContext;
    private TextView tvAvailableDdk, tvSelectCurrency, tvSelectCurrency2;
    private EditText etEnterDDK;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_sell, container, false);
        mContext = getActivity();
        tvCredential = view.findViewById(R.id.tvCredential);
        tvProgressPer = view.findViewById(R.id.tvProgressPer);

        tvAvailableDdk = view.findViewById(R.id.tvAvailableDdk);
        etEnterDDK = view.findViewById(R.id.etEnterDDK);
        tvSelectCurrency = view.findViewById(R.id.tvSelectCurrency);
        tvSelectCurrency2 = view.findViewById(R.id.tvSelectCurrency2);

        tvCredential.setOnClickListener(this);
        AppConfig.openOkDialog1(mContext);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.btnSeekBar);
        seekBar.setMax(100);

        Spinner spinner = (Spinner) view.findViewById(R.id.payment_spinner);
        // Spinner Drop down elements
        List<String> conversationType = new ArrayList<String>();
        conversationType.add("BTC");
        conversationType.add("ETH");
        conversationType.add("USDT");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, conversationType);

        // Drop down layout style - list view with radio button_red
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);

        getCredentialAll();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvProgressPer.setText("" + progress + "% DDK");
                if (!tvAvailableDdk.getText().toString().trim().isEmpty()) {
                    BigDecimal availableValue = new BigDecimal(tvAvailableDdk.getText().toString());
                    BigDecimal progressValue = new BigDecimal(progress);
                    sellValue = availableValue.multiply(progressValue).divide(new BigDecimal(100));
                    etEnterDDK.setText(String.format("%.4f", sellValue));

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        return view;
    }

    private void getCredentialAll() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.popup_wallet_pooling, null);
        dialog = new BottomSheetDialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(dialogView);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        final EditText searchEt = dialogView.findViewById(R.id.search_ET);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        credentialList = new ArrayList<>();

        adapter = new CredentialListAdapter(credentialList, getActivity(), searchEt, new CredentialListAdapter.SetOnItemClickListener() {
            @Override
            public void onItemClick(String wallet_code, String walletId, String passphrase) {
                tvCredential.setText(wallet_code);
                getWalletBalance(walletId);
                etEnterDDK.setText("0.0000");
                hideKeyBoard();
                dialog.dismiss();
            }
        });
        recyclerView.setAdapter(adapter);

        UserModel.getInstance().getCredentialsCall(mContext, new GetAllCredential() {
            @Override
            public void getCredential(List<Credential> credentials) {
                credentialList.clear();
                credentialList.addAll(credentials);
                adapter.updateData(credentials);
            }
        });
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
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        tvSelectCurrency.setText("Total " + "[" + item + "]");
        tvSelectCurrency2.setText("Enter " + "[" + item + "]" + " Address");
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Sell");
        MainActivity.enableBackViews(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvCredential) {
            if (credentialList.size() > 0) {
                if (!dialog.isShowing()) {
                    dialog.show();
//                        dialog.showAtLocation(tvCredential, Gravity.CENTER, 0, 0);
                } else {
                    dialog.dismiss();
                }
            }
        }
    }

    private void filter(String newText) {
        if (credentialList.size() > 0) {
            List<Credential> credentials = new ArrayList<>();

            if (newText.isEmpty()) {
                credentials.addAll(credentialList);
            } else {
                for (Credential event : credentialList) {
                    if (event.getName().toLowerCase().contains(newText.toLowerCase()) ||
                            event.getDdkcode().toLowerCase().contains(newText.toLowerCase())) {
                        credentials.add(event);
                    }
                }

                if (credentials.size() == 0) {
                    AppConfig.showToast("No search data Found.");
                }
            }
            adapter.updateData(credentials);
        }
    }


    private void getWalletBalance(String walletId) {
        UserModel.getInstance().getWalletDetails(0,walletId, mContext, new GetAvailableValue() {
            @Override
            public void getValues(String ddk, WalletResponse successResponse) {
                tvAvailableDdk.setText(ddk);
            }
        });
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
