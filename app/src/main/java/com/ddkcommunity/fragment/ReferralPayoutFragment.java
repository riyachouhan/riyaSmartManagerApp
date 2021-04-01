package com.ddkcommunity.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.adapters.ReferralPayoutLevelAdapter;
import com.ddkcommunity.adapters.TransactionDateAdapter;
import com.ddkcommunity.model.ReferralPayoutModel;
import com.ddkcommunity.model.SubModelReeralList;
import com.ddkcommunity.model.TransactionDate;
import com.ddkcommunity.utilies.AppConfig;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.DataNotFound;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferralPayoutFragment extends Fragment {

    public ReferralPayoutFragment() {
    }

    private BottomSheetDialog dialog;
    private ReferralPayoutLevelAdapter referralPayoutAdapter;
    private TextView sp_transactionDate;
    private String selectedId = "";
    String leveluse="0";
    private List<TransactionDate.TransactionDateData> transactionDateData = new ArrayList<>();
    private Context mContext;
    RecyclerView rvTransHistory,rvReferralPayout_1,rvReferralPayout_2,rvReferralPayout_3,rvReferralPayout_4,rvReferralPayout;
    LinearLayoutManager mLayoutManager;
    ProgressBar progressBar_1,progressBar_2,progressBar_3,progressBar_4,progressBar;
    //for page // initialise loading state
    ArrayList<SubModelReeralList.Datum> subpartlist;
    TextView typemainview,tvName,tvTotalRewards,tvDDKPayout,tvFrom,tvTo,tvConversion,tvTransactionDate;
    LinearLayout mainsublevellayout,mainpartview,submenuview,submenuview_two,submenuview_three,
            submenuview_four,submenuview_five;
    TextView submenu_level_1_name,submenu_level_2_name,submenu_level_3_name,submenu_level_4_name,submenu_level_5_name;
    RelativeLayout list_view_1,list_view_2,list_view_3,list_view_4,list_view_5;
    TextView tvSAM,totaldaysview_1,totaldaysview_2,totaldaysview_3,totaldaysview_4,totaldaysview_5;
    /*android:text="Total in (5) Days"*/
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    View view;
    String typepayout="ddk_payout";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_referral_payout, container, false);
        sp_transactionDate = view.findViewById(R.id.sp_transactionDate);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio);
        mContext = getActivity();
        //..........
        tvSAM=view.findViewById(R.id.tvSAM);
        typemainview=view.findViewById(R.id.typemainview);
        totaldaysview_1=view.findViewById(R.id.totaldaysview_1);
        totaldaysview_2=view.findViewById(R.id.totaldaysview_2);
        totaldaysview_3=view.findViewById(R.id.totaldaysview_3);
        totaldaysview_4=view.findViewById(R.id.totaldaysview_4);
        totaldaysview_5=view.findViewById(R.id.totaldaysview_5);
        progressBar_2=view.findViewById(R.id.progressBar_2);
        progressBar_3=view.findViewById(R.id.progressBar_3);
        progressBar_4=view.findViewById(R.id.progressBar_4);
        progressBar=view.findViewById(R.id.progressBar);
        list_view_1=view.findViewById(R.id.list_view_1);
        list_view_2=view.findViewById(R.id.list_view_2);
        list_view_3=view.findViewById(R.id.list_view_3);
        list_view_4=view.findViewById(R.id.list_view_4);
        list_view_5=view.findViewById(R.id.list_view_5);
        submenu_level_1_name=view.findViewById(R.id.submenu_level_1_name);
        submenu_level_2_name=view.findViewById(R.id.submenu_level_2_name);
        submenu_level_3_name=view.findViewById(R.id.submenu_level_3_name);
        submenu_level_4_name=view.findViewById(R.id.submenu_level_4_name);
        submenu_level_5_name=view.findViewById(R.id.submenu_level_5_name);
        tvName=view.findViewById(R.id.tvName);
        submenuview=view.findViewById(R.id.submenuview);
        submenuview_two=view.findViewById(R.id.submenuview_two);
        submenuview_three=view.findViewById(R.id.submenuview_three);
        submenuview_four=view.findViewById(R.id.submenuview_four);
        submenuview_five=view.findViewById(R.id. submenuview_five);
        tvTotalRewards=view.findViewById(R.id.tvTotalRewards);
        mainpartview=view.findViewById(R.id.mainpartview);
        tvDDKPayout=view.findViewById(R.id.tvDDKPayout);
        tvFrom=view.findViewById(R.id.tvFrom);
        tvTo=view.findViewById(R.id.tvTo);
        mainsublevellayout=view.findViewById(R.id.mainsublevellayout);
        tvConversion=view.findViewById(R.id.tvConversion);
        tvTransactionDate=view.findViewById(R.id.tvTransactionDate);
        rvReferralPayout_2=view.findViewById(R.id.rvReferralPayout_2);
        rvReferralPayout_3=view.findViewById(R.id.rvReferralPayout_3);
        rvReferralPayout_4=view.findViewById(R.id.rvReferralPayout_4);
        rvReferralPayout=view.findViewById(R.id.rvReferralPayout);
        //..........
        rvReferralPayout_1=view.findViewById(R.id.rvReferralPayout_1);
        rvTransHistory = view.findViewById(R.id.rvReferralPayout);
        getReferralTransactionDate();
        progressBar_1 = view.findViewById(R.id.progressBar_1);
        sp_transactionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.show();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // checkedId is the RadioButton selected
                int selectedIdradio = group.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButton = (RadioButton) view.findViewById(selectedIdradio);
                final String useroption=radioButton.getText().toString();
                if(useroption.equalsIgnoreCase("DDK Payouts"))
                {
                    typepayout="ddk_payout";
                    tvDDKPayout.setVisibility(View.VISIBLE);
                    tvSAM.setVisibility(View.GONE);
                    getReferralTransactionDate();
                }else
                {
                    typepayout="sam_points";
                    tvDDKPayout.setVisibility(View.GONE);
                    tvSAM.setVisibility(View.VISIBLE);
                    getReferralTransactionDate();

                }
                sp_transactionDate.setText("Select Transaction Date");
                //for
                mainsublevellayout.setVisibility(View.GONE);
                ClearAlldata("6");
                    mainpartview.setVisibility(View.GONE);
                    tvName.setText("");
                    tvFrom.setText("");
                    tvTo.setText("");
                    tvTotalRewards.setText("");
                    tvDDKPayout.setText("");
                    tvConversion.setText("₮");
                    tvTransactionDate.setText("");
                    //.......

                }
        });
        //.............
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvReferralPayout_1.setLayoutManager(mLayoutManager);
        rvReferralPayout_1.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout_1.setNestedScrollingEnabled(false);
        //........
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvReferralPayout_2.setLayoutManager(mLayoutManager);
        rvReferralPayout_2.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout_2.setNestedScrollingEnabled(false);
//...............
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvReferralPayout_3.setLayoutManager(mLayoutManager);
        rvReferralPayout_3.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout_3.setNestedScrollingEnabled(false);
//...........
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvReferralPayout_4.setLayoutManager(mLayoutManager);
        rvReferralPayout_4.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout_4.setNestedScrollingEnabled(false);
        // initialise loading state
        //...........
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvReferralPayout.setLayoutManager(mLayoutManager);
        rvReferralPayout.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout.setNestedScrollingEnabled(false);
        // initialise loading state
        // set up scroll listener
        submenuview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                subpartlist=new ArrayList<>();
                if (list_view_1.getVisibility() == View.VISIBLE)
                {
                    leveluse="1";
                    ClearAlldata(leveluse);
                } else
                {
                    submenu_level_1_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_minus), null, null, null);
                    list_view_1.setVisibility(View.VISIBLE);
                    leveluse="1";
                    getSubListData(leveluse);
                }
            }
        });
        submenuview_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subpartlist=new ArrayList<>();
                if (list_view_2.getVisibility() == View.VISIBLE)
                {
                    submenu_level_2_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                    list_view_2.setVisibility(View.GONE);
                    leveluse="2";
                    ClearAlldata(leveluse);
                } else
                {
                    submenu_level_2_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_minus), null, null, null);
                    list_view_2.setVisibility(View.VISIBLE);
                    leveluse="2";
                    getSubListData(leveluse);
                }
            }
        });
        submenuview_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subpartlist=new ArrayList<>();
                if (list_view_3.getVisibility() == View.VISIBLE)
                {
                    submenu_level_3_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                    list_view_3.setVisibility(View.GONE);
                    leveluse="3";
                    ClearAlldata(leveluse);
                } else
                {
                    submenu_level_3_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_minus), null, null, null);
                    list_view_3.setVisibility(View.VISIBLE);
                    leveluse="3";
                    getSubListData(leveluse);
                }
            }
        });
        submenuview_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subpartlist=new ArrayList<>();
                if (list_view_4.getVisibility() == View.VISIBLE)
                {
                    submenu_level_4_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                    list_view_4.setVisibility(View.GONE);
                    leveluse="4";
                    ClearAlldata(leveluse);

                } else
                {
                    submenu_level_4_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_minus), null, null, null);
                    list_view_4.setVisibility(View.VISIBLE);
                    leveluse="4";
                    getSubListData(leveluse);
                }
            }
        });
        submenuview_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subpartlist=new ArrayList<>();
                if (list_view_5.getVisibility() == View.VISIBLE)
                {
                    submenu_level_5_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                    list_view_5.setVisibility(View.GONE);
                    leveluse="5";
                    ClearAlldata(leveluse);
                } else
                {
                    submenu_level_5_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_minus), null, null, null);
                    list_view_5.setVisibility(View.VISIBLE);
                    leveluse="5";
                    getSubListData(leveluse);
                }
            }
        });
        mainpartview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (mainsublevellayout.getVisibility() == View.VISIBLE)
                {
                    tvName.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                    mainsublevellayout.setVisibility(View.GONE);
                } else
                {
                    tvName.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_minus), null, null, null);
                    mainsublevellayout.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    public void ClearAlldata(String levele)
    {
        if(subpartlist!=null) {
            referralPayoutAdapter = new ReferralPayoutLevelAdapter(selectedId, subpartlist, mContext, new ReferralPayoutLevelAdapter.SetOnItemClick() {
                @Override
                public void onItemClick(int position) {

                }
            });
            if (levele.equalsIgnoreCase("1")) {
                submenu_level_1_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                list_view_1.setVisibility(View.GONE);
                rvReferralPayout_1.setAdapter(referralPayoutAdapter);

            } else if (levele.equalsIgnoreCase("2")) {
                submenu_level_2_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                list_view_2.setVisibility(View.GONE);
                rvReferralPayout_2.setAdapter(referralPayoutAdapter);

            } else if (levele.equalsIgnoreCase("3")) {
                submenu_level_3_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                list_view_3.setVisibility(View.GONE);
                rvReferralPayout_3.setAdapter(referralPayoutAdapter);

            } else if (levele.equalsIgnoreCase("4")) {
                submenu_level_4_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                list_view_4.setVisibility(View.GONE);
                rvReferralPayout_4.setAdapter(referralPayoutAdapter);

            } else if (levele.equalsIgnoreCase("5")) {
                submenu_level_5_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                list_view_5.setVisibility(View.GONE);
                rvReferralPayout.setAdapter(referralPayoutAdapter);
            } else {
                submenu_level_1_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                list_view_1.setVisibility(View.GONE);
                rvReferralPayout_1.setAdapter(referralPayoutAdapter);
                submenu_level_2_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                list_view_2.setVisibility(View.GONE);
                rvReferralPayout_2.setAdapter(referralPayoutAdapter);
                submenu_level_3_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                list_view_3.setVisibility(View.GONE);
                rvReferralPayout_3.setAdapter(referralPayoutAdapter);
                submenu_level_4_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                list_view_4.setVisibility(View.GONE);
                rvReferralPayout_4.setAdapter(referralPayoutAdapter);
                submenu_level_5_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                list_view_5.setVisibility(View.GONE);
                rvReferralPayout.setAdapter(referralPayoutAdapter);

            }
        }
    }

    private void getSubListData(final String level)
    {
        showProjgessDialog(level);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("transaction_id", selectedId);
        hm.put("level",level);
        hm.put("report_type", typepayout);
        Log.d("referal para",hm.toString());
        AppConfig.getLoadInterface().getSubPayoutListList(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm).enqueue(new Callback<SubModelReeralList>() {
            @Override
            public void onResponse(Call<SubModelReeralList> call, Response<SubModelReeralList> response) {

                if(response.isSuccessful())
                {
                    Log.d("referall",response.body().toString());
                    Log.d("referall",response.body().getStatus().toString());
                    if (response.body().getStatus() == 1)
                    {
                        if (response.body().getData() != null && response.body().getData().size() > 0)
                        {
                                subpartlist.addAll(response.body().getData());
                                progressBarStop(level);
                                referralPayoutAdapter= new ReferralPayoutLevelAdapter(selectedId,subpartlist, mContext, new ReferralPayoutLevelAdapter.SetOnItemClick() {
                                    @Override
                                    public void onItemClick(int position)
                                    {

                                    }
                                });
                                if(level.equalsIgnoreCase("1"))
                                {
                                    rvReferralPayout_1.setAdapter(referralPayoutAdapter);
                                }else if(level.equalsIgnoreCase("2"))
                                {
                                    rvReferralPayout_2.setAdapter(referralPayoutAdapter);
                                }else if(level.equalsIgnoreCase("3"))
                                {
                                    rvReferralPayout_3.setAdapter(referralPayoutAdapter);
                                }else if(level.equalsIgnoreCase("4"))
                                {
                                    rvReferralPayout_4.setAdapter(referralPayoutAdapter);
                                }else if(level.equalsIgnoreCase("5"))
                                {
                                    rvReferralPayout.setAdapter(referralPayoutAdapter);
                                }
                        } else {
                            progressBarStop(level);
                            AppConfig.showToast(response.body().getMsg()+"");
                        }
                    }else if (response.body().getStatus() == 0){
                        progressBarStop(level);
                        DataNotFound(getActivity(),response.body().getMsg());
                    }else
                    {
                        AppConfig.showToast(response.body().getMsg());
                        progressBarStop(level);
                    }
                } else
                {
                    progressBarStop(level);
                    AppConfig.showToast("Server error");
                }
            }

            @Override
            public void onFailure(Call<SubModelReeralList> call, Throwable t) {
                progressBarStop(level);
            }
        });

    }

    public void progressBarStop(String level)
    {
        if(level.equalsIgnoreCase("1"))
        {
            progressBar_1.setVisibility(View.GONE);
        }else if(level.equalsIgnoreCase("2"))
        {
            progressBar_2.setVisibility(View.GONE);
        }else if(level.equalsIgnoreCase("3"))
        {
            progressBar_3.setVisibility(View.GONE);
        }else if(level.equalsIgnoreCase("4"))
        {
            progressBar_4.setVisibility(View.GONE);
        }else if(level.equalsIgnoreCase("5"))
        {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void showProjgessDialog(String level)
    {
            if(level.equalsIgnoreCase("1"))
            {
                progressBar_1.setVisibility(View.VISIBLE);
            }else if(level.equalsIgnoreCase("2"))
            {
                progressBar_2.setVisibility(View.VISIBLE);
            }else if(level.equalsIgnoreCase("3"))
            {
                progressBar_3.setVisibility(View.VISIBLE);
            }else if(level.equalsIgnoreCase("4"))
            {
                progressBar_4.setVisibility(View.VISIBLE);
            }else if(level.equalsIgnoreCase("5"))
            {
                progressBar.setVisibility(View.VISIBLE);
            }
    }
    //for data list
    private void getPayoutList()
    {
        int selectedIdradio = radioGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioButton = (RadioButton) view.findViewById(selectedIdradio);
        final String useroption=radioButton.getText().toString();
        if(useroption.equalsIgnoreCase("DDK Payouts"))
        {
            typepayout="ddk_payout";
        }else
        {
            typepayout="sam_points";
        }
        typemainview.setText(useroption);
        final ProgressDialog pd= new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("transaction_id", selectedId);
        hm.put("report_type", typepayout);
            AppConfig.getLoadInterface().getPayoutList(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm).enqueue(new Callback<ReferralPayoutModel>() {
                @Override
                public void onResponse(Call<ReferralPayoutModel> call, Response<ReferralPayoutModel> response) {
                  pd.dismiss();
                  if(response.isSuccessful())
                    {
                      if (response.body().getStatus() == 1)
                      {
                          if (response.body().getData() != null && response.body().getData().size() > 0)
                          {
                              Integer diffdaya=response.body().getDiff();
                              String poolingColorvalue=response.body().getData().get(0).getPoolingColor();
                              String poolingbgColorvalue=response.body().getData().get(0).getPoolingbgColor();
                              Double total_ddkvalue= response.body().getData().get(0).getTotalDdk();
                              Double total_rewardvalue=response.body().getData().get(0).getTotalReward();
                              Double SAMPointsalue=response.body().getData().get(0).getSamPoints();
                              String tovalue=response.body().getData().get(0).getTo();
                              String transactionvalue=response.body().getData().get(0).getTransaction();
                              String namevalue=response.body().getData().get(0).getName();
                              String unique_codevalue=response.body().getData().get(0).getUniqueCode();
                              Double conversionvalue=response.body().getData().get(0).getConversion();
                              String emailvalue=response.body().getData().get(0).getEmail();
                              Double idvalue=response.body().getData().get(0).getId();
                              String fromvalue=response.body().getData().get(0).getFrom();
                              tvName.setText("You");
                            //  tvSAM.setText(String.format(Locale.getDefault(), "%.4f",SAMPointsalue));
                            //  tvDDKPayout.setText(String.format(Locale.getDefault(), "%.4f",total_ddkvalue));
                              tvDDKPayout.setText(total_ddkvalue+"");
                              tvSAM.setText(SAMPointsalue+"");
                              tvFrom.setText(fromvalue);
                              tvTo.setText(tovalue);
                              tvTotalRewards.setText(String.format(Locale.getDefault(), "%.2f",total_rewardvalue));
                              if(useroption.equalsIgnoreCase("DDK Payouts"))
                              {
                                  tvDDKPayout.setVisibility(View.VISIBLE);
                                  tvSAM.setVisibility(View.GONE);
                              }else
                              {
                                  tvDDKPayout.setVisibility(View.GONE);
                                  tvSAM.setVisibility(View.VISIBLE);
                              }
                              tvConversion.setText("₮" + String.format(Locale.getDefault(), "%.2f", conversionvalue));
                              tvTransactionDate.setText(AppConfig.dateFormat(transactionvalue, "yyyy-MM-dd HH:mm:ss"));
                              mainpartview.setVisibility(View.VISIBLE);
                              totaldaysview_1.setText("Total in ("+diffdaya+") Days");
                              totaldaysview_2.setText("Total in ("+diffdaya+") Days");
                              totaldaysview_3.setText("Total in ("+diffdaya+") Days");
                              totaldaysview_4.setText("Total in ("+diffdaya+") Days");
                              totaldaysview_5.setText("Total in ("+diffdaya+") Days");
                          } else {
                              AppConfig.showToast("Data Not Available");
                          }
                      }else
                      {
                          AppConfig.showToast(response.body().getMsg());
                      }
                    } else
                    {
                        AppConfig.showToast("Server error");
                    }
                }

                @Override
                public void onFailure(Call<ReferralPayoutModel> call, Throwable t) {
                        pd.dismiss();
                }
            });
    }

    private void getReferralTransactionDate()
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.popup_transactiondate, null);
        dialog = new BottomSheetDialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(dialogView);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final TransactionDateAdapter transactionDateAdapter = new TransactionDateAdapter(transactionDateData, new TransactionDateAdapter.SetOnItemClick() {
            @Override
            public void onItemClick(int position) {
                selectedId = "" + transactionDateData.get(position).id;
                sp_transactionDate.setText(transactionDateData.get(position).from_date + " to " + transactionDateData.get(position).to_date);
                if (dialog != null) {
                    dialog.dismiss();
                    //for
                    mainsublevellayout.setVisibility(View.GONE);
                    ClearAlldata("6");
                    mainpartview.setVisibility(View.GONE);
                    tvName.setText("");
                    tvFrom.setText("");
                    tvTo.setText("");
                    tvTotalRewards.setText("");
                    tvDDKPayout.setText("");
                    tvConversion.setText("₮");
                    tvTransactionDate.setText("");
                    //.......
                    getPayoutList();
                }
            }
        });

        recyclerView.setAdapter(transactionDateAdapter);
        final ProgressDialog pd=new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        //for new
        HashMap<String, String> hm = new HashMap<>();
        //report_type:sam_points/ddk_payouts
        hm.put("date_type", typepayout);
        AppConfig.getLoadInterface().getTransactionDate(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm).enqueue(new Callback<TransactionDate>() {
            @Override
            public void onResponse(Call<TransactionDate> call, Response<TransactionDate> response) {

                if(response.isSuccessful())
                {
                    if (response.body().status == 1)
                    {
                        pd.dismiss();
                        transactionDateData.clear();
                        transactionDateData.addAll(response.body().data);
                        transactionDateAdapter.updateData(response.body().data);
                    }else
                    {
                        pd.dismiss();
                        AppConfig.showToast(response.body().msg);
                    }
                } else
                {
                    pd.dismiss();
                    AppConfig.showToast("Server error");
                }
            }

            @Override
            public void onFailure(Call<TransactionDate> call, Throwable t) {
                pd.dismiss();
            }
        });
        //.............
    }

}
