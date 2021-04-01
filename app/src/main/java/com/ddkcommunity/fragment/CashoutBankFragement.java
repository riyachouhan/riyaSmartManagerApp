package com.ddkcommunity.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.BankCashoutFragment;
import com.ddkcommunity.adapters.BankListAdapter;
import com.ddkcommunity.adapters.CredentialAdapter;
import com.ddkcommunity.fragment.projects.TermsAndConsitionSubscription;
import com.ddkcommunity.model.BankList;
import com.ddkcommunity.model.SAMPDModel;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.adapters.AllTypeCashoutFragmentAdapter.usercountryselect;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.ShowSAMPDDialog;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashoutBankFragement extends Fragment
{
    private static ArrayList<SAMPDModel.Datum> SAMPDList;
    private RecyclerView rvProjectRecycle;
    private CredentialAdapter mAdapter;
    private Context mContext;
    ScrollView listscroll;
    private UserResponse userData;
    private BankCashoutFragment bankListAdapter;
    private ArrayList<BankList.BankData> bankList;
    private String imagePath = "";

    public CashoutBankFragement() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view1 = null;
        if (view1 == null)
        {
            view1 = inflater.inflate(R.layout.fragmentcahsoutbanklayit, container, false);
            mContext = getActivity();
            userData = AppConfig.getUserData(mContext);
            rvProjectRecycle = view1.findViewById(R.id.rvProjectRecycle);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rvProjectRecycle.setLayoutManager(mLayoutManager);
            rvProjectRecycle.setItemAnimator(new DefaultItemAnimator());
            String countrydata=userData.getUser().country.get(0).country;
            String countryid=userData.getUser().country.get(0).id;
            String countryname=userData.getUser().country.get(0).country;
            bankList = new ArrayList<>();
            bankListAdapter = new BankCashoutFragment(usercountryselect,getActivity(), imagePath, bankList, new BankCashoutFragment.SetOnItemClickListener() {
                @Override
                public void onItemClick(String name, String image, String id) {

                }
            });
            rvProjectRecycle.setAdapter(bankListAdapter);
            getBankList(countryid);
        }
        return view1;
    }

    private void getBankList(String country_id)
    {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("country_id", country_id);
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Get Bank List....");
        AppConfig.getLoadInterface().getTheBankList(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<BankList>() {
            @Override
            public void onResponse(Call<BankList> call, Response<BankList> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("response banklist",response.body().toString());
                    if (response.body().status == 1) {
                        bankList.clear();
                        imagePath = response.body().image_path;
                        bankList.addAll(response.body().data);
                        bankListAdapter.updateData(bankList, response.body().image_path);
                    } else if (response.body() != null && response.body().status == 3) {
                        AppConfig.showToast(response.body().msg);
                        AppConfig.openSplashActivity(getActivity());
                    } else {
                        AppConfig.showToast(response.body().msg);
                    }
                } else {
                    AppConfig.showToast("Server Error");
                }
            }

            @Override
            public void onFailure(Call<BankList> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Sell");
        MainActivity.enableBackViews(true);
    }

}
