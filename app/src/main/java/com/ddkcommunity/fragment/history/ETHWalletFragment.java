package com.ddkcommunity.fragment.history;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ddkcommunity.R;
import com.ddkcommunity.adapters.HistoryAdapter;
import com.ddkcommunity.model.WalletHistory;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ETHWalletFragment extends Fragment {

    private RecyclerView rvWalletHistory;

    public ETHWalletFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_php_wallet, container, false);
        rvWalletHistory = view.findViewById(R.id.rvWalletHistory);
        final ArrayList<WalletHistory> arrayList = new ArrayList<>();
        arrayList.add(new WalletHistory("Paid for Order No.", "5,000.00 ETH", "04/05/2020 2:25 PM", true, ContextCompat.getDrawable(getActivity(), R.drawable.ic_send)));
        arrayList.add(new WalletHistory("Converted for Order No.", "5,000.00 ETH", "04/05/2020 3:55 PM", false, ContextCompat.getDrawable(getActivity(), R.drawable.ic_convert)));

        arrayList.add(new WalletHistory("Cash out for Order No.", "8,592.00 ETH", "6:54 PM\n04/05/2020", false, ContextCompat.getDrawable(getActivity(), R.drawable.ic_cash_out)));
        arrayList.add(new WalletHistory("Converted for Order No.", "8,000.00 ETH", "11:25 PM\n04/05/2020", false, ContextCompat.getDrawable(getActivity(), R.drawable.ic_convert)));
        arrayList.add(new WalletHistory("Received for Order No.", "8,000.00 ETH", "11:25 PM\n04/05/2020", false, ContextCompat.getDrawable(getActivity(), R.drawable.ic_receive)));

/*
        HistoryAdapter mAdapter = new HistoryAdapter(arrayList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvWalletHistory.setLayoutManager(mLayoutManager);
        rvWalletHistory.setItemAnimator(new DefaultItemAnimator());
        rvWalletHistory.setAdapter(mAdapter);*/
        return view;
    }
}
