package com.ddkcommunity.fragment.projects;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.AllTypeCashoutFragmentAdapter;
import com.ddkcommunity.adapters.CashoutFragmenAdapter;
import com.ddkcommunity.adapters.mapoptionadapter;
import com.ddkcommunity.fragment.CashOutFragmentNew;
import com.ddkcommunity.fragment.send.SuccessFragmentScan;
import com.ddkcommunity.model.UserBankList;
import com.ddkcommunity.model.UserBankListDetails;
import com.ddkcommunity.model.UserBankListResponse;
import com.ddkcommunity.model.checkRefferalModel;
import com.ddkcommunity.model.mapoptionmodel;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.CommonMethodFunction;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.ShowServerPost;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapOtionAllFragment extends Fragment implements View.OnClickListener
{
    RecyclerView recyclerviewGridView;
    Activity mContext;
    TextView Portal,mapoptiontext;
    LinearLayout submenuiteam,headerinactiviteview;
    ArrayList<mapoptionmodel> mapoptionList;
    String activeStatus;
    ImageView copyview;

    public MapOtionAllFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mapoptionallfragment, container, false);
        mContext = getActivity();
        mapoptiontext=view.findViewById(R.id.mapoptiontext);
        if (getArguments().getString("activeStatus") != null) {
            activeStatus= getArguments().getString("activeStatus");
        }

        if(activeStatus.equalsIgnoreCase("1"))
        {
            mapoptiontext.setText("Active");
        }else
        {
            mapoptiontext.setText("Inactive");
        }
        submenuiteam=view.findViewById(R.id.submenuiteam);
        submenuiteam.setVisibility(View.GONE);
        Portal=view.findViewById(R.id.Portal);
        String userReferalCode = App.pref.getString(Constant.USER_REFERAL_CODE, "");
        Portal.setText(userReferalCode);
        headerinactiviteview=view.findViewById(R.id.headerinactiviteview);
        headerinactiviteview.setVisibility(View.VISIBLE);
        mapoptionList=new ArrayList<>();
        mapoptionList.add(new mapoptionmodel("Buy MAP Package",R.drawable.mapgmap));
        mapoptionList.add(new mapoptionmodel("Matrix",R.drawable.matrixicon));
        mapoptionList.add(new mapoptionmodel("ILACM",R.drawable.ilcam));
        mapoptionList.add(new mapoptionmodel("Best Venture",R.drawable.bestventure));
        mapoptionList.add(new mapoptionmodel("Poultry",R.drawable.poultry));
        mapoptionList.add(new mapoptionmodel("BAWE",R.drawable.beauty));
        mapoptionList.add(new mapoptionmodel("REMJON Petroleum",R.drawable.remjon));
        mapoptionList.add(new mapoptionmodel("Dressing Plant",R.drawable.dressingplant));
        mapoptionList.add(new mapoptionmodel("Kaisan Product",R.drawable.kasianicon));
        mapoptionList.add(new mapoptionmodel("Airline Ticketing",R.drawable.airline));
        mapoptionList.add(new mapoptionmodel("Feed Processing Plant",R.drawable.feedprocessing));
        mapoptionList.add(new mapoptionmodel("Import/Export Commodity",R.drawable.pisam));
        mapoptionList.add(new mapoptionmodel("Digital Fintech",R.drawable.digitalfintech));
        mapoptionList.add(new mapoptionmodel("Payment Facility",R.drawable.paymentfac));
        mapoptionList.add(new mapoptionmodel("Gen Merchandise",R.drawable.genmerch));

        recyclerviewGridView=view.findViewById(R.id.recyclerviewGridView);
        mapoptionadapter allTypeCashoutFragmentAdapter = new mapoptionadapter("","main",mapoptionList, getActivity());
        recyclerviewGridView.setAdapter(allTypeCashoutFragmentAdapter);
        copyview=view.findViewById(R.id.copyview);
        copyview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.copyPass(Portal.getText().toString().trim(), "Copy Address", getActivity());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("M.A.P");
        MainActivity.enableBackViews(true);
    }

    @Override
    public void onClick(View v)
    {
       /* if (v.getId() == R.id.submit_BT)
        {
            Fragment fragment = new SuccessFragmentScan();
            Bundle arg = new Bundle();
            arg.putString("action", "map");
            fragment.setArguments(arg);
            MainActivity.addFragment(fragment,true);
        }*/
    }


}