package com.ddkcommunity.fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.CredentialAdapter;
import com.ddkcommunity.fragment.SAMPD.FreeFlightVoucherFragment;
import com.ddkcommunity.fragment.projects.TermsAndConsitionSubscription;
import com.ddkcommunity.fragment.send.SendDDkFragment;
import com.ddkcommunity.model.SAMPDModel;
import com.ddkcommunity.model.mazigneModel;
import com.ddkcommunity.utilies.AppConfig;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.ShowSAMPDDialog;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

/**
 * A simple {@link Fragment} subclass.
 */
public class SAMPDFragment extends Fragment implements View.OnClickListener
{
    private static ArrayList<SAMPDModel.Datum> SAMPDList;
    private RecyclerView rvProjectRecycle;
    private CredentialAdapter mAdapter;
    private Context mContext;
    ScrollView listscroll;
    LinearLayout ll_mazines,ll_FreeFlightVoucher,ll_GeneralInformation,ll_SamProductDisclosure,ll_ImportExport,ll_SamRide,ll_moneyRemitance
            ,ll_samDelivery,ll_iLach,ll_samPdGrp,ll_ventureProcessing,ll_beautyEntrepreneur,ll_transparentServer;

    public SAMPDFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view1 = null;
        if (view1 == null)
        {
            view1 = inflater.inflate(R.layout.fragment_smpd, container, false);
            mContext = getActivity();
            ll_mazines=view1.findViewById(R.id.ll_mazines);
            listscroll=view1.findViewById(R.id.listscroll);
            rvProjectRecycle = view1.findViewById(R.id.rvProjectRecycle);
            ll_FreeFlightVoucher = view1.findViewById(R.id.ll_FreeFlightVoucher);
            ll_SamProductDisclosure = view1.findViewById(R.id.ll_SamProductDisclosure);
            ll_GeneralInformation = view1.findViewById(R.id.ll_GeneralInformation);
            ll_ImportExport = view1.findViewById(R.id.ll_ImportExport);
            ll_SamRide = view1.findViewById(R.id.ll_SamRide);
            ll_moneyRemitance = view1.findViewById(R.id.ll_moneyRemitance);
            ll_samDelivery = view1.findViewById(R.id.ll_samDelivery);
            ll_iLach = view1.findViewById(R.id.ll_iLach);
            ll_samPdGrp = view1.findViewById(R.id.ll_samPdGrp);
            ll_ventureProcessing = view1.findViewById(R.id.ll_ventureProcessing);
            ll_beautyEntrepreneur = view1.findViewById(R.id.ll_beautyEntrepreneur);
            ll_transparentServer = view1.findViewById(R.id.ll_transparentServer);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rvProjectRecycle.setLayoutManager(mLayoutManager);
            rvProjectRecycle.setItemAnimator(new DefaultItemAnimator());
            getActiveSubscriptionStatus();

            ll_mazines.setOnClickListener(this);
            ll_FreeFlightVoucher.setOnClickListener(this);
            ll_GeneralInformation.setOnClickListener(this);
            ll_SamProductDisclosure.setOnClickListener(this);
            ll_ImportExport.setOnClickListener(this);
            ll_SamRide.setOnClickListener(this);
            ll_moneyRemitance.setOnClickListener(this);
            ll_samDelivery.setOnClickListener(this);
            ll_iLach.setOnClickListener(this);
            ll_samPdGrp.setOnClickListener(this);
            ll_ventureProcessing.setOnClickListener(this);
            ll_beautyEntrepreneur.setOnClickListener(this);
            ll_transparentServer.setOnClickListener(this);
        }
        return view1;
    }

    private void getActiveSubscriptionStatus()
    {
        AppConfig.showLoading("Loading...", mContext);
        AppConfig.getLoadInterface().getActivteSubscriptionStatus(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken)).enqueue(new Callback<SAMPDModel>() {
            @Override
            public void onResponse(Call<SAMPDModel> call, Response<SAMPDModel> response) {
                AppConfig.hideLoader();
                if (response.isSuccessful() && response.body() != null)
                {
                    if (response.body().getStatus() == 1)
                    {
                        Log.d("chages data",response.toString());
                        if(response.body().getData().size()>0)
                        {
                            listscroll.setVisibility(View.VISIBLE);
                            SAMPDList = new ArrayList<>();
                            SAMPDList.addAll(response.body().getData());
                            if(SAMPDList.size()!=0)
                            {
                               // SAMPDAdapter mAdapter = new SAMPDAdapter(SAMPDList,getActivity());
                               // rvProjectRecycle.setAdapter(mAdapter);
                            }
                        }else
                        {
                            Toast.makeText(mContext, "No Project Available", Toast.LENGTH_SHORT).show();
                        }

                    }else
                    {
                        listscroll.setVisibility(View.GONE);
                        ShowSAMPDDialog(getActivity(),response.body().getMsg());
                    }
                } else
                {
                    ShowApiError(getActivity(),"server error in all_transactions/check-active-subscription-user");
                }
            }

            @Override
            public void onFailure(Call<SAMPDModel> call, Throwable t) {
                AppConfig.hideLoader();
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

    public static void switchActivty()
    {
        Fragment fragment = new TermsAndConsitionSubscription();
        Bundle arg = new Bundle();
        fragment.setArguments(arg);
        MainActivity.addFragment(fragment, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Project Development");
        MainActivity.enableBackViews(true);
    }

    //for app
    private void getMagzineData()
    {
        AppConfig.showLoading("Loading...", mContext);
        AppConfig.getLoadInterface().getMagazieapi(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken)).enqueue(new Callback<mazigneModel>() {
            @Override
            public void onResponse(Call<mazigneModel> call, Response<mazigneModel> response)
            {
                AppConfig.hideLoader();
                if (response.isSuccessful() && response.body() != null)
                {
                    if (response.body().getStatus() == 1)
                    {
                        String pdf=response.body().getData().getMagazine();
                        String mainpdf=Constant.SLIDERIMG+pdf;
                        Fragment fragment = new SAMPDShowProject();
                        Bundle arg = new Bundle();
                        arg.putString("pdflink", mainpdf);
                        fragment.setArguments(arg);
                        MainActivity.addFragment(fragment,true);
                    }else
                    {
                        Toast.makeText(mContext, "No PDF Available", Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    ShowApiError(getActivity(),"server error in all_transactions/check-active-subscription-user");
                }
            }

            @Override
            public void onFailure(Call<mazigneModel> call, Throwable t)
            {
                AppConfig.hideLoader();
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }
    //............

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ll_mazines:
                getMagzineData();
                break;

            case R.id.ll_GeneralInformation:
                MainActivity.addFragment(new FreeFlightVoucherFragment("1","General Inforamtion"), true);
                break;

            case R.id.ll_SamProductDisclosure:
                MainActivity.addFragment(new FreeFlightVoucherFragment("2", "Sam Product Disclosure"), true);
                break;
            case R.id.ll_FreeFlightVoucher:
                MainActivity.addFragment(new FreeFlightVoucherFragment("3", "Free Flight Voucher"), true);
                break;
            case R.id.ll_ImportExport:
                MainActivity.addFragment(new FreeFlightVoucherFragment("4", "Import And Export"), true);
                break;
            case R.id.ll_SamRide:
                MainActivity.addFragment(new FreeFlightVoucherFragment("5", "Sam Ride"), true);
                break;
            case R.id.ll_moneyRemitance:
                MainActivity.addFragment(new FreeFlightVoucherFragment("6", "Money Remittance"), true);
                break;
            case R.id.ll_samDelivery:
                MainActivity.addFragment(new FreeFlightVoucherFragment("7", "Sam Delivery"), true);
                break;
            case R.id.ll_iLach:
                MainActivity.addFragment(new FreeFlightVoucherFragment("8", "Ilacm"), true);
                break;
            case R.id.ll_samPdGrp:
                MainActivity.addFragment(new FreeFlightVoucherFragment("9", "Sam Pd GRP"), true);
                break;
            case R.id.ll_ventureProcessing:
                MainActivity.addFragment(new FreeFlightVoucherFragment("10", "Venture Processing"), true);
                break;
            case R.id.ll_beautyEntrepreneur:
                MainActivity.addFragment(new FreeFlightVoucherFragment("11", "Beauty And Wellness"), true);
                break;
            case R.id.ll_transparentServer:
                MainActivity.addFragment(new FreeFlightVoucherFragment("12", "Transparent Server"), true);
                break;
        }

    }
}
