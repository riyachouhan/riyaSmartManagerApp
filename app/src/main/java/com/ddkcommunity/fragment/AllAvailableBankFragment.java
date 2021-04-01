package com.ddkcommunity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.model.AllAvailableBankList;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllAvailableBankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllAvailableBankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView rv_BankIds;
    List<AllAvailableBankList.BankData>allAvailableBankLists = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllAvailableBankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllAvailableBankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllAvailableBankFragment newInstance(String param1, String param2) {
        AllAvailableBankFragment fragment = new AllAvailableBankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_available_bank, container, false);
        // Inflate the layout for this fragment
        rv_BankIds = view.findViewById(R.id.rv_BankIds);

        AllAvailableBankList.BankData model1=new AllAvailableBankList.BankData();
        model1.setBank_name("GCash");
        model1.setImage("");
        allAvailableBankLists.add(model1);
        AllAvailableBankList.BankData model10=new AllAvailableBankList.BankData();
        model10.setBank_name("PayMaya");
        model10.setImage("paymaya");
        allAvailableBankLists.add(model10);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_BankIds.setLayoutManager(layoutManager);
        rv_BankIds.setItemAnimator(new DefaultItemAnimator());


     /*   AllTypeAvailableFragmentAdapter allTypeAvailableFragmentAdapter = new AllTypeAvailableFragmentAdapter(allAvailableBankLists,getActivity());
        rv_BankIds.setAdapter(allTypeAvailableFragmentAdapter);*/

        return view;
    }
}