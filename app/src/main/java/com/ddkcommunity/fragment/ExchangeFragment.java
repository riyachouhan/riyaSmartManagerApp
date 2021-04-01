package com.ddkcommunity.fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.CreateCancellaitonAdapter;
import com.ddkcommunity.adapters.ExchangeAdapter;
import com.ddkcommunity.adapters.RequestCancellaitonAdapter;
import com.ddkcommunity.model.CreateRequestModel;
import com.ddkcommunity.model.Exchangesdata;
import com.ddkcommunity.model.RequestModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static com.ddkcommunity.utilies.dataPutMethods.ShowExcahngeDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rvRecycleTrade,rvRecycleSAMDDK,rvRecycleLocal;
    private Context mContext;
    private ArrayList<Exchangesdata> LocalDatalist,SamDdkList,TradeHistorylist;
    TextView buyview,sellview;
    public ExchangeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);
        mContext = getActivity();
        buyview=view.findViewById(R.id.buyview);
        sellview=view.findViewById(R.id.sellview);
        rvRecycleTrade=view.findViewById(R.id.rvRecycleTrade);
        rvRecycleSAMDDK=view.findViewById(R.id.rvRecycleSAMDDK);
        rvRecycleLocal=view.findViewById(R.id.rvRecycleLocal);
        //,,,,,,,,,,,,,
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvRecycleTrade.setLayoutManager(mLayoutManager);
        rvRecycleTrade.setItemAnimator(new DefaultItemAnimator());
        getCharegesData();
        //..............
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        rvRecycleSAMDDK.setLayoutManager(mLayoutManager1);
        rvRecycleSAMDDK.setItemAnimator(new DefaultItemAnimator());
        getSAMDDKData();
        //..............
        RecyclerView.LayoutManager mLayoutManager12 = new LinearLayoutManager(getActivity());
        rvRecycleLocal.setLayoutManager(mLayoutManager12);
        rvRecycleLocal.setItemAnimator(new DefaultItemAnimator());
        getLocalData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Exchange");
        MainActivity.enableBackViews(true);
    }

    private void getLocalData()
    {
        LocalDatalist=new ArrayList<>();
        Exchangesdata m1=new Exchangesdata();
        m1.setPriceddk("0.003");
        m1.setAmountsam("50.0");
        m1.setTotalsamddk("0.1512");
        m1.setAmountstatus("true");
        LocalDatalist.add(m1);

        Exchangesdata m11=new Exchangesdata();
        m11.setPriceddk("0.0035");
        m11.setAmountsam("150.006");
        m11.setTotalsamddk("0.135665");
        m11.setAmountstatus("true");
        LocalDatalist.add(m11);

        Exchangesdata m121=new Exchangesdata();
        m121.setPriceddk("2.0035");
        m121.setAmountsam("40.0234");
        m121.setTotalsamddk("80.13000");
        m121.setAmountstatus("true");
        LocalDatalist.add(m121);

        Exchangesdata m1218=new Exchangesdata();
        m1218.setPriceddk("425.0035");
        m1218.setAmountsam("4021.0234");
        m1218.setTotalsamddk("8011.13000");
        m1218.setAmountstatus("true");
        LocalDatalist.add(m1218);

        if(LocalDatalist.size()!=0)
        {
            ExchangeAdapter mAdapter = new ExchangeAdapter(LocalDatalist, getActivity());
            rvRecycleLocal.setAdapter(mAdapter);
        }
    }

    private void getSAMDDKData()
    {
        SamDdkList=new ArrayList<>();
        Exchangesdata m1=new Exchangesdata();
        m1.setPriceddk("0.003");
        m1.setAmountsam("50.0");
        m1.setTotalsamddk("0.1512");
        m1.setAmountstatus("false");
        SamDdkList.add(m1);

        Exchangesdata m11=new Exchangesdata();
        m11.setPriceddk("0.0035");
        m11.setAmountsam("150.006");
        m11.setTotalsamddk("0.135665");
        m11.setAmountstatus("false");
        SamDdkList.add(m11);

        Exchangesdata m121=new Exchangesdata();
        m121.setPriceddk("2.0035");
        m121.setAmountsam("40.0234");
        m121.setTotalsamddk("80.13000");
        m121.setAmountstatus("false");
        SamDdkList.add(m121);

        Exchangesdata m1213=new Exchangesdata();
        m1213.setPriceddk("1892.0035");
        m1213.setAmountsam("340.0234");
        m1213.setTotalsamddk("11180.130");
        m1213.setAmountstatus("false");
        SamDdkList.add(m1213);

        if(SamDdkList.size()!=0)
        {
            ExchangeAdapter mAdapter = new ExchangeAdapter(SamDdkList, getActivity());
            rvRecycleSAMDDK.setAdapter(mAdapter);
        }

    }

    private void getCharegesData()
    {
        TradeHistorylist=new ArrayList<>();
        Exchangesdata m1=new Exchangesdata();
        m1.setPriceddk("0.003");
        m1.setAmountsam("50.0");
        m1.setTotalsamddk("0.1512");
        m1.setAmountstatus("false");
        TradeHistorylist.add(m1);

        Exchangesdata m11=new Exchangesdata();
        m11.setPriceddk("0.0035");
        m11.setAmountsam("150.006");
        m11.setTotalsamddk("0.135665");
        m11.setAmountstatus("true");
        TradeHistorylist.add(m11);

        Exchangesdata m121=new Exchangesdata();
        m121.setPriceddk("2.0035");
        m121.setAmountsam("40.0234");
        m121.setTotalsamddk("80.13000");
        m121.setAmountstatus("true");
        TradeHistorylist.add(m121);

        Exchangesdata m1213=new Exchangesdata();
        m1213.setPriceddk("1892.0035");
        m1213.setAmountsam("340.0234");
        m1213.setTotalsamddk("11180.130");
        m1213.setAmountstatus("false");
        TradeHistorylist.add(m1213);

        Exchangesdata m1216=new Exchangesdata();
        m1216.setPriceddk("1232.0035");
        m1216.setAmountsam("28940.0234");
        m1216.setTotalsamddk("3380.13");
        m1216.setAmountstatus("true");
        TradeHistorylist.add(m1216);

        if(TradeHistorylist.size()!=0)
        {
            ExchangeAdapter mAdapter = new ExchangeAdapter(TradeHistorylist, getActivity());
            rvRecycleTrade.setAdapter(mAdapter);
        }

        buyview.setOnClickListener(this);
        sellview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.buyview:
                ShowExcahngeDialog(getActivity(),"buy");
                break;

            case R.id.sellview:
                ShowExcahngeDialog(getActivity(),"sell");
                break;
        }
    }

  /*  private void  getCharegesData() {
        AppConfig.showLoading("Loading...", mContext);
        AppConfig.getLoadInterface().getPoolingTransactionHistory(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken)).enqueue(new Callback<PollingHistoryTransction>() {
            @Override
            public void onResponse(Call<PollingHistoryTransction> call, Response<PollingHistoryTransction> response) {
                AppConfig.hideLoader();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().status == 1) {
                        Log.d("transaction-history",response.toString());
                        poolingHistoryData=new ArrayList<>();
                        poolingHistoryData.addAll(response.body().lendData);
                        poolingHistoryData.addAll(response.body().rewardData);
                        poolingHistoryData.addAll(response.body().Data2);
                        poolingHistoryData.addAll(response.body().Data3);
                        poolingHistoryData.addAll(response.body().Data4);
                        poolingHistoryData.addAll(response.body().Data5);
                        Collections.sort(poolingHistoryData, new Comparator<PollingHistoryTransction.PoolingHistoryData>() {
                            @Override
                            public int compare(PollingHistoryTransction.PoolingHistoryData o1, PollingHistoryTransction.PoolingHistoryData o2) {
                                DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    return f.parse(o2.transactionDate).compareTo(f.parse(o1.transactionDate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    return 0;
                                }
                            }
                        });

                        HistoryAdapter mAdapter = new HistoryAdapter(poolingHistoryData, getActivity());
                        rvRequestRecycle.setAdapter(mAdapter);

                    }else if (response.body().status == 3) {
                        AppConfig.showToast(response.body().msg);
                        AppConfig.openSplashActivity(getActivity());
                    }
                } else {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PollingHistoryTransction> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                AppConfig.hideLoader();
            }
        });
    }
*/

}
