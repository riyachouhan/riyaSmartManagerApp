package com.ddkcommunity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.adapters.ReferralChainLevelAdapter;
import com.ddkcommunity.model.referral.ReferralChain;
import com.ddkcommunity.model.referral.ReferralLevelList;
import com.ddkcommunity.utilies.AppConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.DataNotFound;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferralChainFragment extends Fragment {
    private List<ReferralLevelList> referralChains1;
    private ReferralChainLevelAdapter referralChainLevelAdapter1;
    private LinearLayout lytCollapse;
    private TextView noRecordFound;
    //..........
    RecyclerView rvReferralPayout_1,rvReferralPayout_2,rvReferralPayout_3,rvReferralPayout_4,rvReferralPayout;
    RelativeLayout list_view_1,list_view_2,list_view_3,list_view_4,list_view_5;
    ProgressBar progressBar_1,progressBar_2,progressBar_3,progressBar_4,progressBar_5;
    LinearLayout submenuview,submenuview_two,submenuview_three,submenuview_four,submenuview_five;
    TextView submenu_level_1_name,submenu_level_2_name,submenu_level_3_name,submenu_level_4_name,submenu_level_5_name;
    String leveluse="0";
    int currentpage=1;
    boolean isscrolling=false;

    public ReferralChainFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_referral_chain, container, false);
        lytCollapse = view.findViewById(R.id.lytCollapse);
        noRecordFound = view.findViewById(R.id.tvNoRecord);
        submenu_level_1_name=view.findViewById(R.id.submenu_level_1_name);
        submenu_level_2_name=view.findViewById(R.id.submenu_level_2_name);
        submenu_level_3_name=view.findViewById(R.id.submenu_level_3_name);
        submenu_level_4_name=view.findViewById(R.id.submenu_level_4_name);
        submenu_level_5_name=view.findViewById(R.id.submenu_level_5_name);
        progressBar_1=view.findViewById(R.id.progressBar_1);
        progressBar_2=view.findViewById(R.id.progressBar_2);
        progressBar_3=view.findViewById(R.id.progressBar_3);
        progressBar_4=view.findViewById(R.id.progressBar_4);
        progressBar_5=view.findViewById(R.id.progressBar);
        list_view_1=view.findViewById(R.id.list_view_1);
        list_view_2=view.findViewById(R.id.list_view_2);
        list_view_3=view.findViewById(R.id.list_view_3);
        list_view_4=view.findViewById(R.id.list_view_4);
        list_view_5=view.findViewById(R.id.list_view_5);
        rvReferralPayout_1=view.findViewById(R.id.rvReferralPayout_1);
        rvReferralPayout_2=view.findViewById(R.id.rvReferralPayout_2);
        rvReferralPayout_3=view.findViewById(R.id.rvReferralPayout_3);
        rvReferralPayout_4=view.findViewById(R.id.rvReferralPayout_4);
        rvReferralPayout=view.findViewById(R.id.rvReferralPayout);
        submenuview=view.findViewById(R.id.submenuview);
        submenuview_two=view.findViewById(R.id.submenuview_two);
        submenuview_three=view .findViewById(R.id.submenuview_three);
        submenuview_four=view.findViewById(R.id.submenuview_four);
        submenuview_five=view.findViewById(R.id.submenuview_five);
        referralChains1 = new ArrayList<>();
        referralChainLevelAdapter1 = new ReferralChainLevelAdapter(referralChains1, getActivity(), new ReferralChainLevelAdapter.SetOnItemClick() {
            @Override
            public void onItemClick(int position) {

            }
        });
        //........
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvReferralPayout_1.setLayoutManager(mLayoutManager);
        rvReferralPayout_1.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout_1.setNestedScrollingEnabled(false);
//................
        final RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        rvReferralPayout_2.setLayoutManager(mLayoutManager1);
        rvReferralPayout_2.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout_2.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        rvReferralPayout_3.setLayoutManager(mLayoutManager2);
        rvReferralPayout_3.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout_3.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity());
        rvReferralPayout_4.setLayoutManager(mLayoutManager3);
        rvReferralPayout_4.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout_4.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager mLayoutManager4 = new LinearLayoutManager(getActivity());
        rvReferralPayout.setLayoutManager(mLayoutManager4);
        rvReferralPayout.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout.setNestedScrollingEnabled(false);

        //for click
        submenuview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                referralChains1=new ArrayList<>();
                if (list_view_1.getVisibility() == View.VISIBLE)
                {
                    leveluse="1";
                    ClearAlldata(leveluse);
                } else
                {
                    submenu_level_1_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_minus), null, null, null);
                    list_view_1.setVisibility(View.VISIBLE);
                    String levele="1";
                    getReferralChainData(currentpage,levele);
                }
            }
        });

        submenuview_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referralChains1=new ArrayList<>();
                if (list_view_2.getVisibility() == View.VISIBLE)
                {
                    submenu_level_2_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black), null, null, null);
                    list_view_2.setVisibility(View.GONE);
                    leveluse="2";
                    ClearAlldata(leveluse);
               } else
                {
                    submenu_level_2_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_minus), null, null, null);
                    list_view_2.setVisibility(View.VISIBLE);
                    String levele="2";
                    currentpage=1;
                    getReferralChainData(currentpage,levele);
                }
            }
        });

        submenuview_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referralChains1=new ArrayList<>();
                if (list_view_3.getVisibility() == View.VISIBLE)
                {
                    submenu_level_3_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black), null, null, null);
                    list_view_3.setVisibility(View.GONE);
                    leveluse="3";
                    ClearAlldata(leveluse);
             } else
                {
                    submenu_level_3_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_minus), null, null, null);
                    list_view_3.setVisibility(View.VISIBLE);
                    String levele="3";
                    currentpage=1;
                    getReferralChainData(currentpage,levele);
                }
            }
        });

        submenuview_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referralChains1=new ArrayList<>();
                if (list_view_4.getVisibility() == View.VISIBLE)
                {
                    submenu_level_4_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black), null, null, null);
                    list_view_4.setVisibility(View.GONE);
                    leveluse="4";
                    ClearAlldata(leveluse);
                } else
                {
                    submenu_level_4_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_minus), null, null, null);
                    list_view_4.setVisibility(View.VISIBLE);
                    String levele="4";
                    currentpage=1;
                    getReferralChainData(currentpage,levele);
                }
            }
        });

        submenuview_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referralChains1=new ArrayList<>();
                if (list_view_5.getVisibility() == View.VISIBLE)
                {
                    submenu_level_5_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black), null, null, null);
                    list_view_5.setVisibility(View.GONE);
                    leveluse="5";
                    ClearAlldata("5");
                } else
                {
                    submenu_level_5_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_minus), null, null, null);
                    list_view_5.setVisibility(View.VISIBLE);
                    String levele="5";
                    currentpage=1;
                    getReferralChainData(currentpage,levele);
                }
            }
        });

        //..................
      /*  rvReferralPayout_1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    Toast.makeText(getActivity(), "page scrolled"+currentpage, Toast.LENGTH_SHORT).show();
                    isscrolling=true;
                    currentpage=currentpage+1;
                    isscrolling=false;
                    progressBar_1.setVisibility(View.VISIBLE);
                    getReferralChainData(currentpage, "1");

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentitem=mLayoutManager1.getChildCount();
                 int totalitems=mLayoutManager1.getItemCount();
                int scrollOutItem= ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if(isscrolling && (currentitem+scrollOutItem==totalitems))
                {
                    currentpage=currentpage+1;
                    Toast.makeText(getActivity(), "api calli", Toast.LENGTH_SHORT).show();
                    isscrolling=false;
                    progressBar_1.setVisibility(View.VISIBLE);
                    getReferralChainData(currentpage, "1");
                }
            }
        });
*/
        return view;
    }

    public void ClearAlldata(String levele)
    {
        if (levele.equalsIgnoreCase("1"))
        {
            submenu_level_1_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
            list_view_1.setVisibility(View.GONE);
            rvReferralPayout_1.setAdapter(referralChainLevelAdapter1);

        } else if (levele.equalsIgnoreCase("2"))
        {
            submenu_level_2_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
            list_view_2.setVisibility(View.GONE);
            rvReferralPayout_2.setAdapter(referralChainLevelAdapter1);

        } else if (levele.equalsIgnoreCase("3"))
        {
            submenu_level_3_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
            list_view_3.setVisibility(View.GONE);
            rvReferralPayout_3.setAdapter(referralChainLevelAdapter1);

        } else if (levele.equalsIgnoreCase("4"))
        {
            submenu_level_4_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
            list_view_4.setVisibility(View.GONE);
            rvReferralPayout_4.setAdapter(referralChainLevelAdapter1);

        } else if (levele.equalsIgnoreCase("5"))
        {
            submenu_level_5_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
            list_view_5.setVisibility(View.GONE);
            rvReferralPayout.setAdapter(referralChainLevelAdapter1);
        }else {
            submenu_level_1_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
            list_view_1.setVisibility(View.GONE);
            rvReferralPayout_1.setAdapter(referralChainLevelAdapter1);
            submenu_level_2_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
            list_view_2.setVisibility(View.GONE);
            rvReferralPayout_2.setAdapter(referralChainLevelAdapter1);
            submenu_level_3_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
            list_view_3.setVisibility(View.GONE);
            rvReferralPayout_3.setAdapter(referralChainLevelAdapter1);
            submenu_level_4_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
            list_view_4.setVisibility(View.GONE);
            rvReferralPayout_4.setAdapter(referralChainLevelAdapter1);
            submenu_level_5_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
            list_view_5.setVisibility(View.GONE);
            rvReferralPayout.setAdapter(referralChainLevelAdapter1);

        }
    }

    private void getReferralChainData(final int pagecount,final String level)
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

        }else
        {
            progressBar_5.setVisibility(View.VISIBLE);

        }
        HashMap<String, String> hm = new HashMap<>();
        hm.put("level", level);
        //hm.put("page",pagecount+"");
        AppConfig.getLoadInterface().getReferralChain(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken),hm).enqueue(new Callback<ReferralChain>() {
            @Override
            public void onResponse(Call<ReferralChain> call, Response<ReferralChain> response) {
                stopProgress(level);
                Log.d("referall",response.body().toString());
                Log.d("referall",response.body().status+"::"+response.body().msg);
                if (response.isSuccessful() && response.body().status == 1 && response.body().data != null && response.body().data.size() > 0) {
                    if (response.body().data.get(0).referralLevelList != null && response.body().data.get(0).referralLevelList.size() > 0) {
                        lytCollapse.setVisibility(View.VISIBLE);
                        //noRecordFound.setVisibility(View.GONE);
                        referralChains1.addAll(response.body().data.get(0).referralLevelList);
                       /* if(currentpage==1)
                        {*/
                        referralChainLevelAdapter1= new ReferralChainLevelAdapter(referralChains1,getActivity(), new ReferralChainLevelAdapter.SetOnItemClick() {
                            @Override
                            public void onItemClick(int position)
                            {

                            }
                        });
                            if(level.equalsIgnoreCase("1"))
                            {
                                rvReferralPayout_1.setAdapter(referralChainLevelAdapter1);

                            }else if(level.equalsIgnoreCase("2"))
                            {
                                rvReferralPayout_2.setAdapter(referralChainLevelAdapter1);

                            }else if(level.equalsIgnoreCase("3"))
                            {
                                rvReferralPayout_3.setAdapter(referralChainLevelAdapter1);

                            }else if(level.equalsIgnoreCase("4"))
                            {
                                rvReferralPayout_4.setAdapter(referralChainLevelAdapter1);
                            }else
                            {
                                rvReferralPayout.setAdapter(referralChainLevelAdapter1);
                            }
                       /* }else
                        {
                            if(level.equalsIgnoreCase("1"))
                            {
                                referralChainLevelAdapter1.updateData(referralChains1);

                            }else if(level.equalsIgnoreCase("2"))
                            {
                                referralChainLevelAdapter1.updateData(referralChains1);

                            }else if(level.equalsIgnoreCase("3"))
                            {
                                referralChainLevelAdapter1.updateData(referralChains1);

                            }else if(level.equalsIgnoreCase("4"))
                            {
                                referralChainLevelAdapter1.updateData(referralChains1);
                            }else
                            {
                                referralChainLevelAdapter1.updateData(referralChains1);
                            }
                        }*/
                    } else {
                        //lytCollapse.setVisibility(View.GONE);
                        DataNotFound(getActivity(),"Record Not Found");
                        //noRecordFound.setVisibility(View.VISIBLE);
                    }
                } else if (response.body().status == 3) {
                    AppConfig.showToast(response.body().msg);
                    AppConfig.openSplashActivity(getActivity());
                } else {
                    //lytCollapse.setVisibility(View.GONE);
                    //noRecordFound.setVisibility(View.VISIBLE);
                    DataNotFound(getActivity(),"Record Not Found");
                }
            }

            @Override
            public void onFailure(Call<ReferralChain> call, Throwable t) {
                stopProgress(level);
            }
        });
    }

    public void stopProgress(String level)
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
        }else
        {
            progressBar_5.setVisibility(View.GONE);
        }
    }
}
/*extends Fragment {
    private List<ReferralLevelList> referralChains = new ArrayList<>();
    private List<ReferralChain> referralLevels = new ArrayList<>();
    private ReferralChainLevelAdapter referralChainLevelAdapter;
    private LinearLayout lytCollapse;
    private TextView noRecordFound;
    //for new view
    LinearLayout mainsublevellayout,mainpartview,submenuview,submenuview_two,submenuview_three,
            submenuview_four,submenuview_five;
    ArrayList<refrrelchainRewardAdapter.Refferallist> subpartlist;
    RelativeLayout list_view_1,list_view_2,list_view_3,list_view_4,list_view_5;
    String leveluse="0";
    TextView submenu_level_1_name,submenu_level_2_name,submenu_level_3_name,submenu_level_4_name,submenu_level_5_name;
    ProgressBar progressBar_1,progressBar_2,progressBar_3,progressBar_4,progressBar;
    private ReferralPayoutLevelAdapter referralPayoutAdapter;
    RecyclerView rvReferralPayout_1,rvReferralPayout_2,rvReferralPayout_3,rvReferralPayout_4,rvReferralPayout;
    ReferralChainAdapterNew referralChainadapter;
    RecyclerView rvReferralPayout_23;
    public ReferralChainFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_referral_chain, container, false);
        lytCollapse = view.findViewById(R.id.lytCollapse);
        noRecordFound = view.findViewById(R.id.tvNoRecord);
        submenuview=view.findViewById(R.id.submenuview);
        submenuview_two=view.findViewById(R.id.submenuview_two);
        submenuview_three=view.findViewById(R.id.submenuview_three);
        submenuview_four=view.findViewById(R.id.submenuview_four);
        submenuview_five=view.findViewById(R.id. submenuview_five);
        progressBar_2=view.findViewById(R.id.progressBar_2);
        progressBar_3=view.findViewById(R.id.progressBar_3);
        progressBar_4=view.findViewById(R.id.progressBar_4);
        progressBar_1 = view.findViewById(R.id.progressBar_1);
        progressBar=view.findViewById(R.id.progressBar);
        rvReferralPayout_2=view.findViewById(R.id.rvReferralPayout_2);
        rvReferralPayout_3=view.findViewById(R.id.rvReferralPayout_3);
        rvReferralPayout_4=view.findViewById(R.id.rvReferralPayout_4);
        rvReferralPayout=view.findViewById(R.id.rvReferralPayout);
        rvReferralPayout_1=view.findViewById(R.id.rvReferralPayout_1);
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

        referralChainLevelAdapter = new ReferralChainLevelAdapter(referralChains, getActivity(), new ReferralChainLevelAdapter.SetOnItemClick() {
            @Override
            public void onItemClick(int position) {

            }
        });

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
                    submenu_level_1_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_minus), null, null, null);
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
                    submenu_level_2_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black), null, null, null);
                    list_view_2.setVisibility(View.GONE);
                    leveluse="2";
                    ClearAlldata(leveluse);
                } else
                {
                    submenu_level_2_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_minus), null, null, null);
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
                    submenu_level_3_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black), null, null, null);
                    list_view_3.setVisibility(View.GONE);
                    leveluse="3";
                    ClearAlldata(leveluse);
                } else
                {
                    submenu_level_3_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_minus), null, null, null);
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
                    submenu_level_4_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black), null, null, null);
                    list_view_4.setVisibility(View.GONE);
                    leveluse="4";
                    ClearAlldata(leveluse);

                } else
                {
                    submenu_level_4_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_minus), null, null, null);
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
                    submenu_level_5_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black), null, null, null);
                    list_view_5.setVisibility(View.GONE);
                    leveluse="5";
                    ClearAlldata(leveluse);
                } else
                {
                    submenu_level_5_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_minus), null, null, null);
                    list_view_5.setVisibility(View.VISIBLE);
                    leveluse="5";
                    getSubListData(leveluse);
                }
            }
        });

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        rvReferralPayout_1.setLayoutManager(mLayoutManager1);
        rvReferralPayout_1.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout_1.setNestedScrollingEnabled(false);
        //........
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        rvReferralPayout_2.setLayoutManager(mLayoutManager2);
        rvReferralPayout_2.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout_2.setNestedScrollingEnabled(false);
//...............
        LinearLayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity());
        rvReferralPayout_3.setLayoutManager(mLayoutManager3);
        rvReferralPayout_3.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout_3.setNestedScrollingEnabled(false);
//...........
        LinearLayoutManager mLayoutManager4 = new LinearLayoutManager(getActivity());
        rvReferralPayout_4.setLayoutManager(mLayoutManager4);
        rvReferralPayout_4.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout_4.setNestedScrollingEnabled(false);
        // initialise loading state
        //...........
        LinearLayoutManager mLayoutManager5 = new LinearLayoutManager(getActivity());
        rvReferralPayout.setLayoutManager(mLayoutManager5);
        rvReferralPayout.setItemAnimator(new DefaultItemAnimator());
        rvReferralPayout.setNestedScrollingEnabled(false);

       *//* RecyclerView rvTransHistory = view.findViewById(R.id.rvReferralChain);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvTransHistory.setLayoutManager(mLayoutManager);
        rvTransHistory.setItemAnimator(new DefaultItemAnimator());
        rvTransHistory.setAdapter(referralChainLevelAdapter);
        getReferralChainData();
*//*
        rvReferralPayout_23=view.findViewById(R.id.rvReferralPayout_1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvReferralPayout_23.setLayoutManager(mLayoutManager);
        rvReferralPayout_23.setItemAnimator(new DefaultItemAnimator());
        getSubListData("1");
        return view;
    }

    private void getSubListData(final String level)
    {
        showProjgessDialog(level);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("level",level);
        AppConfig.getLoadInterface().getReferralChainNew(AppConfig.getStringPreferences(getContext(), Constant.JWTToken), hm).enqueue(new Callback<refrrelchainRewardAdapter>() {
            @Override
            public void onResponse(Call<refrrelchainRewardAdapter> call, Response<refrrelchainRewardAdapter> response)
            {
                if(response.isSuccessful())
                {
                    if (response.body().getStatus() == 1)
                    {
                        if (response.body().getData() != null && response.body().getData().size() > 0)
                        {
                            subpartlist=new ArrayList<>();
                            subpartlist.addAll(response.body().getData().get(0).getRefferallist());
                            progressBarStop(level);

                            if(subpartlist.size()!=0)
                            {
                                ReferralChainAdapterNew referralChainadapter= new ReferralChainAdapterNew(subpartlist,getContext());
                                rvReferralPayout_23.setAdapter(referralChainadapter);
                            }

                           *//* LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            rvReferralPayout_1.setLayoutManager(llm);
                            ReferralChainAdapterNew referralChainadapter= new ReferralChainAdapterNew(subpartlist,getContext());
                            rvReferralPayout_1.setAdapter(referralChainadapter);
*//*
                            Toast.makeText(getContext(), ""+subpartlist.size(), Toast.LENGTH_SHORT).show();
                           // referralChainadapter= new ReferralChainAdapterNew(subpartlist,getContext());
                           *//*   if(level.equalsIgnoreCase("1"))
                            {
                                rvReferralPayout_1.setAdapter(referralChainadapter);
                            }else if(level.equalsIgnoreCase("2"))
                            {
                                rvReferralPayout_2.setAdapter(referralChainadapter);
                            }else if(level.equalsIgnoreCase("3"))
                            {
                                rvReferralPayout_3.setAdapter(referralChainadapter);
                            }else if(level.equalsIgnoreCase("4"))
                            {
                                rvReferralPayout_4.setAdapter(referralChainadapter);
                            }else if(level.equalsIgnoreCase("5"))
                            {
                                rvReferralPayout.setAdapter(referralChainadapter);
                            }*//*
                        } else {
                            progressBarStop(level);
                            AppConfig.showToast(response.body().getMsg()+"");
                        }
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
            public void onFailure(Call<refrrelchainRewardAdapter> call, Throwable t) {
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

    public void ClearAlldata(String levele)
    {
        if(subpartlist!=null) {
            *//*referralPayoutAdapter = new ReferralPayoutLevelAdapter(selectedId, subpartlist, mContext, new ReferralPayoutLevelAdapter.SetOnItemClick() {
                @Override
                public void onItemClick(int position) {

                }
            });*//*
            if (levele.equalsIgnoreCase("1")) {
                submenu_level_1_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black), null, null, null);
                list_view_1.setVisibility(View.GONE);
                rvReferralPayout_1.setAdapter(referralPayoutAdapter);

            } else if (levele.equalsIgnoreCase("2")) {
                submenu_level_2_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
                list_view_2.setVisibility(View.GONE);
                rvReferralPayout_2.setAdapter(referralPayoutAdapter);

            } else if (levele.equalsIgnoreCase("3")) {
                submenu_level_3_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black), null, null, null);
                list_view_3.setVisibility(View.GONE);
                rvReferralPayout_3.setAdapter(referralPayoutAdapter);

            } else if (levele.equalsIgnoreCase("4")) {
                submenu_level_4_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
                list_view_4.setVisibility(View.GONE);
                rvReferralPayout_4.setAdapter(referralPayoutAdapter);

            } else if (levele.equalsIgnoreCase("5")) {
                submenu_level_5_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black), null, null, null);
                list_view_5.setVisibility(View.GONE);
                rvReferralPayout.setAdapter(referralPayoutAdapter);
            } else {
                submenu_level_1_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
                list_view_1.setVisibility(View.GONE);
                rvReferralPayout_1.setAdapter(referralPayoutAdapter);
                submenu_level_2_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
                list_view_2.setVisibility(View.GONE);
                rvReferralPayout_2.setAdapter(referralPayoutAdapter);
                submenu_level_3_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
                list_view_3.setVisibility(View.GONE);
                rvReferralPayout_3.setAdapter(referralPayoutAdapter);
                submenu_level_4_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
                list_view_4.setVisibility(View.GONE);
                rvReferralPayout_4.setAdapter(referralPayoutAdapter);
                submenu_level_5_name.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black), null, null, null);
                list_view_5.setVisibility(View.GONE);
                rvReferralPayout.setAdapter(referralPayoutAdapter);

            }
        }
    }

    private void getReferralChainData(String levelumber)
    {
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Please wait ...........");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("level", levelumber);

        AppConfig.getLoadInterface().getReferralChain(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken),hm).enqueue(new Callback<ReferralChain>() {
            @Override
         public void onResponse(Call<ReferralChain> call, Response<ReferralChain> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body().status == 1 && response.body().data != null && response.body().data.size() > 0) {
                    if (response.body().data.get(0).referralLevelList != null && response.body().data.get(0).referralLevelList.size() > 0) {
                        lytCollapse.setVisibility(View.VISIBLE);
                        noRecordFound.setVisibility(View.GONE);
                        referralChainLevelAdapter.updateData(response.body().data.get(0).referralLevelList);
                    } else {
                        lytCollapse.setVisibility(View.GONE);
                        noRecordFound.setVisibility(View.VISIBLE);
                    }
                } else if (response.body().status == 3) {
                    AppConfig.showToast(response.body().msg);
                    AppConfig.openSplashActivity(getActivity());
                } else {
                    lytCollapse.setVisibility(View.GONE);
                    noRecordFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ReferralChain> call, Throwable t) {
              pd.dismiss();
            }
        });
    }

}
*/