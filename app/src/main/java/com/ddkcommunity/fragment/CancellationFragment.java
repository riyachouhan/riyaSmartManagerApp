package com.ddkcommunity.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.CreateCancellaitonAdapter;
import com.ddkcommunity.adapters.HistoryAdapter;
import com.ddkcommunity.adapters.RequestCancellaitonAdapter;
import com.ddkcommunity.model.CreateRequestModel;
import com.ddkcommunity.model.PollingHistoryTransction;
import com.ddkcommunity.model.RequestModel;
import com.ddkcommunity.model.ShowRequestApiModel;
import com.ddkcommunity.utilies.AppConfig;
import com.google.android.material.tabs.TabLayout;
import com.ncorti.slidetoact.SlideToActView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowSameWalletDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class CancellationFragment extends Fragment {

    private RecyclerView rvCreateRecycle,rvRequestRecycle;
    LinearLayout sendrequestlayout,request_layout_view,create_layout_view;
    private Context mContext;
    private TabLayout tabLayout;
    public  ArrayList<ShowRequestApiModel.ShowRequestMidel> createCancellationRequestlist;
    private ArrayList<ShowRequestApiModel.ShowRequestMidel> createCancellationData;
    private ArrayList<RequestModel.Datum> requestCancellationData;
    TextView  ddkamountview,charge_fee,subtotal_fees,conversion_view;
    SlideToActView slide_custom_icon;

    public CancellationFragment() {
        // Required empty public constructor
    }
    private int[] tabIcons = {
            R.drawable.ic_history,
            R.drawable.new_projects_color
    };

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_canncellation, container, false);
        mContext = getActivity();
        createCancellationRequestlist=new ArrayList<>();
        rvRequestRecycle=view.findViewById(R.id.rvRequestRecycle);
        create_layout_view=view.findViewById(R.id.create_layout_view);
        request_layout_view=view.findViewById(R.id.request_layout_view);
        rvCreateRecycle= view.findViewById(R.id.rvCreateRecycle);
        sendrequestlayout=view.findViewById(R.id.sendrequestlayout);
        ddkamountview=view.findViewById(R.id.ddkamountview);
        charge_fee=view.findViewById(R.id.charge_fee);
        subtotal_fees=view.findViewById(R.id.subtotal_fees);
        conversion_view=view.findViewById(R.id.conversion_view);
        slide_custom_icon=view.findViewById(R.id.slide_custom_icon);
        //for tab
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Create"));
        tabLayout.addTab(tabLayout.newTab().setText("Request"));
        //setupTabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                {
                    create_layout_view.setVisibility(View.VISIBLE);
                    request_layout_view.setVisibility(View.GONE);
                    getCharegesDataApi();
                    if(createCancellationRequestlist.size()>0)
                    {
                        createCancellationRequestlist.clear();
                        sendrequestlayout.setVisibility(View.GONE);
                        createCancellationRequestlist=new ArrayList<>();
                    }
                } else if (tab.getPosition() == 1)
                {
                    request_layout_view.setVisibility(View.VISIBLE);
                    create_layout_view.setVisibility(View.GONE);
                    getRequestDataApi();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("Selected", "");
                if (tab.getPosition() == 0)
                {
                }
            }
        });

        //,,,,,,,,,,,,,
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvCreateRecycle.setLayoutManager(mLayoutManager);
        rvCreateRecycle.setItemAnimator(new DefaultItemAnimator());
        getCharegesDataApi();
        //..............
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        rvRequestRecycle.setLayoutManager(mLayoutManager1);
        rvRequestRecycle.setItemAnimator(new DefaultItemAnimator());

       //for
        slide_custom_icon.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView)
            {
                sendRequestDataServer();
                slide_custom_icon.resetSlider();
            }
        });
        return view;
    }

    private void sendRequestDataServer()
    {
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Please wait.......");
        pd.show();

        String ddk_address="",subscription_amount="",lender_ids="",chargevalue,ddk_amountvalue,sub_totalvalue,conversionvalue;
        sub_totalvalue=subtotal_fees.getText().toString();
        chargevalue=charge_fee.getText().toString();
        conversionvalue=conversion_view.getText().toString();
        ddk_amountvalue=ddkamountview.getText().toString();
        for(int i=0;i<createCancellationRequestlist.size();i++)
        {
            ddk_address=ddk_address+createCancellationRequestlist.get(i).getDDKSender().trim()+",";
            subscription_amount=subscription_amount+createCancellationRequestlist.get(i).getTotalLendUSD().toString().trim()+",";
            lender_ids=lender_ids+createCancellationRequestlist.get(i).getLender_id().toString().trim()+",";
        }

        int ddk_addrecount=ddk_address.length();
        ddk_address=ddk_address.substring(0,ddk_addrecount-1);
        int subscription_amountcount=subscription_amount.length();
        subscription_amount=subscription_amount.substring(0,subscription_amountcount-1);
        int lender_idscount=lender_ids.length();
        lender_ids=lender_ids.substring(0,lender_idscount-1);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("ddk_address", ddk_address);
        hm.put("subscription_amount", subscription_amount);
        hm.put("charge", chargevalue);
        hm.put("sub_total",sub_totalvalue);
        hm.put("conversion",conversionvalue);
        hm.put("ddk_amount", ddk_amountvalue);
        hm.put("lender_ids", lender_ids);
        hm.put("payment_method_type","SAM");
        Log.d("parmater",""+hm);
        AppConfig.getLoadInterface().insertrequestapi(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Log.d("parmater res",""+jsonObject.toString());
                        if(jsonObject.getString("status").equalsIgnoreCase("1"))
                        {
                            ShowSameWalletDialog(getActivity(),jsonObject.getString("msg"),"2");
                            getCharegesDataApi();

                        }else
                        {
                            Toast.makeText(mContext, ""+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    AppConfig.showToast("Server error");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                AppConfig.showToast("Server error");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Cancellation Request");
        MainActivity.enableBackViews(true);
    }

    private void  getRequestDataApi()
    {
        AppConfig.showLoading("Loading...", mContext);
        AppConfig.getLoadInterface().showallrequest(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken)).enqueue(new Callback<RequestModel>() {
            @Override
            public void onResponse(Call<RequestModel> call, Response<RequestModel> response) {
                AppConfig.hideLoader();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 1)
                    {
                        Log.d("chages data",response.body().toString());
                        if(response.body().getData().size()>0)
                        {
                            requestCancellationData=new ArrayList<>();
                            requestCancellationData.addAll(response.body().getData());

                            if(requestCancellationData.size()!=0)
                            {
                                RequestCancellaitonAdapter mAdapter = new RequestCancellaitonAdapter(requestCancellationData, getActivity());
                                rvRequestRecycle.setAdapter(mAdapter);
                            }

                        }else
                        {
                            Toast.makeText(mContext, "No Data Available", Toast.LENGTH_SHORT).show();
                        }

                    }else if (response.body().getStatus() == 3) {
                        AppConfig.showToast(response.body().getMsg());
                        AppConfig.openSplashActivity(getActivity());
                    }
                } else {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                AppConfig.hideLoader();
            }
        });

    }

    //for api calling
    private void  getCharegesDataApi()
    {
        AppConfig.showLoading("Loading...", mContext);
        AppConfig.getLoadInterface().showactivesubscription(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken)).enqueue(new Callback<ShowRequestApiModel>() {
            @Override
            public void onResponse(Call<ShowRequestApiModel> call, Response<ShowRequestApiModel> response) {
                AppConfig.hideLoader();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 1)
                    {
                        Log.d("chages data",response.toString());
                        if(response.body().getData().size()>0)
                        {
                            createCancellationData=new ArrayList<>();
                            createCancellationData.addAll(response.body().getData());

                            if(createCancellationData.size()!=0)
                            {
                                CreateCancellaitonAdapter mAdapter = new CreateCancellaitonAdapter(ddkamountview,charge_fee,subtotal_fees,conversion_view,sendrequestlayout,createCancellationRequestlist,"1",createCancellationData, getActivity());
                                rvCreateRecycle.setAdapter(mAdapter);
                            }

                        }else
                        {
                            Toast.makeText(mContext, "No Data Available", Toast.LENGTH_SHORT).show();
                        }

                    }else if (response.body().getStatus() == 3) {
                        AppConfig.showToast(response.body().getMsg());
                        AppConfig.openSplashActivity(getActivity());
                    }
                } else {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShowRequestApiModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                AppConfig.hideLoader();
            }
        });
    }


}
