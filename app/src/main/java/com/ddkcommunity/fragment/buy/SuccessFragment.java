package com.ddkcommunity.fragment.buy;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ddkcommunity.R;
import com.ddkcommunity.model.Payment;
import com.ddkcommunity.utilies.AppConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuccessFragment extends Fragment {
    private String ddkValue;
    private Payment payment;

    public SuccessFragment(String ddkValue, Payment payment1) {
        // Required empty public constructor
        this.ddkValue = ddkValue;
        this.payment = payment1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_success, container, false);

        TextView status = view.findViewById(R.id.status_TV);
        TextView transaction = view.findViewById(R.id.transaction_TV);
        TextView date = view.findViewById(R.id.date_TV);
        TextView coin = view.findViewById(R.id.coin_TV);


        status.setText(AppConfig.convert(payment.getPaymentStatus()));
        coin.setText(String.format("%s", ddkValue));
        date.setText(payment.getCreatedAt());
        transaction.setText(payment.getTransactionId());


        return view;
    }



}
