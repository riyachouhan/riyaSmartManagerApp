package com.ddkcommunity.fragment.projects;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.mapoptionadapter;
import com.ddkcommunity.model.checkRefferalModel;
import com.ddkcommunity.model.mapoptionmodel;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.model.userPackagesModel;
import com.ddkcommunity.utilies.AppConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;

/**
 * A simple {@link Fragment} subclass.
 */
public class Mapsubfragmentclick extends Fragment implements View.OnClickListener
{
    RecyclerView recyclerviewGridView;
    Activity mContext;
    public static EditText packageamount;
    TextView Portal;
    AppCompatButton submit_BT;
    LinearLayout submenuiteam;
    ArrayList<mapoptionmodel> mapoptionList;
    public static String selecetview;
    View view;
    mapoptionadapter allTypeCashoutFragmentAdapter;
    UserResponse userData;
    String action="",usereferrlacode;

    public Mapsubfragmentclick()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        if(view==null) {
            view = inflater.inflate(R.layout.mapoptionallfragment, container, false);
            mContext = getActivity();
            selecetview = "";
            if (getArguments().getString("action") != null) {
                action= getArguments().getString("action");
            }

            if(action.equalsIgnoreCase("mapwithreferral"))
            {
                if (getArguments().getString("userenterreferrla") != null) {
                    usereferrlacode= getArguments().getString("userenterreferrla");
                }
            }else
            {
                action="map";
            }
            userData = AppConfig.getUserData(getContext());
            packageamount=view.findViewById(R.id.packageamount);
            submit_BT = view.findViewById(R.id.submit_BT);
            submenuiteam = view.findViewById(R.id.submenuiteam);
            submit_BT.setVisibility(View.VISIBLE);
            submenuiteam.setVisibility(View.VISIBLE);
            Portal = view.findViewById(R.id.Portal);
            SpannableString content = new SpannableString("Portal");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            Portal.setText(content);
            ImageView copyview=view.findViewById(R.id.copyview);
            copyview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConfig.copyPass(Portal.getText().toString().trim(), "Copy Address", getActivity());
                }
            });
            mapoptionList = new ArrayList<>();
            mapoptionList.add(new mapoptionmodel("Bronze \n 20-80", R.drawable.bronzecoin));
            mapoptionList.add(new mapoptionmodel("Sliver \n 100-480", R.drawable.slivercoin));
            mapoptionList.add(new mapoptionmodel("Gold \n 500-980", R.drawable.samgoldcoin));
            mapoptionList.add(new mapoptionmodel("Diamond \n 1000-4980", R.drawable.diamondcoin));
            mapoptionList.add(new mapoptionmodel("Platinum \n 5000-9980", R.drawable.platinumcoin));
            mapoptionList.add(new mapoptionmodel("Titanium \n 10000-Up", R.drawable.titaniumcoin));
            recyclerviewGridView = view.findViewById(R.id.recyclerviewGridView);
            allTypeCashoutFragmentAdapter = new mapoptionadapter(selecetview, "sub", mapoptionList, getActivity());
            recyclerviewGridView.setAdapter(allTypeCashoutFragmentAdapter);
        }
        packageamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (!AppConfig.isStringNullOrBlank(packageamount.getText().toString())) {

                    if(packageamount.getText().toString().equalsIgnoreCase("."))
                    {

                    }else {
                        try {
                            BigDecimal etAmountvalue = new BigDecimal(editable.toString());
                            if (etAmountvalue.compareTo(BigDecimal.ZERO) == 0)
                            {
                                Mapsubfragmentclick.selecetview="";
                                allTypeCashoutFragmentAdapter.updateData(mapoptionList,"sub",Mapsubfragmentclick.selecetview);
                            } else
                            {
                                BigDecimal[] counvaue=etAmountvalue.divideAndRemainder(new BigDecimal(20));
                                BigDecimal amounremine=counvaue[1];
                                if(amounremine.equals(BigDecimal.ZERO))
                                {

                                    if ((etAmountvalue.compareTo(new BigDecimal(20)) >= 0) && etAmountvalue.compareTo(new BigDecimal( 80)) <= 0 )
                                    {
                                    // price is larger than 500 and less than 1000
                                        Mapsubfragmentclick.selecetview="Bronze \n 20-80";
                                    }else if ((etAmountvalue.compareTo(new BigDecimal(100)) >= 0) && etAmountvalue.compareTo(new BigDecimal(480)) <= 0 )
                                    {
                                        // price is larger than 500 and less than 1000
                                        Mapsubfragmentclick.selecetview="Sliver \n 100-480";
                                    }else if ((etAmountvalue.compareTo(new BigDecimal(500)) >= 0) && etAmountvalue.compareTo(new BigDecimal( 980 )) <= 0 )
                                    {
                                    // price is larger than 500 and less than 1000
                                    Mapsubfragmentclick.selecetview="Gold \n 500-980";
                                    }else if ((etAmountvalue.compareTo(new BigDecimal(1000)) >= 0) && etAmountvalue.compareTo(new BigDecimal( 4980 )) <= 0 )
                                    {
                                        // price is larger than 500 and less than 1000
                                        Mapsubfragmentclick.selecetview="Diamond \n 1000-4980";
                                    }else if ((etAmountvalue.compareTo(new BigDecimal(5000)) >= 0) && etAmountvalue.compareTo(new BigDecimal( 9980 )) <= 0 )
                                    {
                                        // price is larger than 500 and less than 1000
                                        Mapsubfragmentclick.selecetview="Platinum \n 5000-9980";
                                    }else
                                    {
                                        Mapsubfragmentclick.selecetview="Titanium \n 10000-Up";
                                    }
                                    allTypeCashoutFragmentAdapter.updateData(mapoptionList,"sub",Mapsubfragmentclick.selecetview);

                                }else
                                {
                                    Mapsubfragmentclick.selecetview="";
                                    allTypeCashoutFragmentAdapter.updateData(mapoptionList,"sub",Mapsubfragmentclick.selecetview);
                                }

                            }
                        } catch(Exception ex)
                        { // handle your exception
                            Toast.makeText(getActivity(),ex.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {

                }
            }
        });

        submit_BT.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        MainActivity.setTitle("M.A.P");
        MainActivity.enableBackViews(true);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.submit_BT)
        {
            if(Mapsubfragmentclick.selecetview.equalsIgnoreCase(""))
            {
                Toast.makeText(mContext, "Please enter valid amount for package", Toast.LENGTH_SHORT).show();
            }else
            {
                CheckAllPackages();
            }

        }
    }

    private void CheckAllPackages()
    {
        String emailid=userData.getUser().getEmail();
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Please wait ....");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("email", emailid);
        AppConfig.getLoadInterfaceMap().getAllPackage(hm).enqueue(new Callback<userPackagesModel>() {
            @Override
            public void onResponse(Call<userPackagesModel > call, Response<userPackagesModel > response) {
                try
                {
                    AppConfig.hideLoading(dialog);
                    Log.d("sam erro par invi",response.body().toString());
                    if (response.isSuccessful() && response.body() != null)
                    {
                        if (response.body().getStatus().equalsIgnoreCase("true"))
                        {
                            ArrayList<userPackagesModel.ExtraDatum> packageslist=new ArrayList<>();
                            packageslist.addAll(response.body().getExtraData());
                            BigDecimal userenteramount=new BigDecimal(packageamount.getText().toString());
                            if(packageslist.size()>0)
                            {
                                int listsize=packageslist.size()-1;
                                String amount=packageslist.get(listsize).getPackAmt().toString();
                                BigDecimal packageselect=new BigDecimal(amount);
                                if ((userenteramount.compareTo(packageselect) >= 0))
                                {
                                    // price is larger than 500 and less than 1000
                                    sendUsertoNext();
                                }else
                                {
                                    Toast.makeText(mContext, "You cannot downgrade your package", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                sendUsertoNext();
                            }

                        }else
                        {
                            sendUsertoNext();
                        }
                    } else {
                        ShowApiError(getContext(),"server error check-mail-exist");
                    }

                } catch (Exception e)
                {
                    AppConfig.hideLoading(dialog);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<userPackagesModel> call, Throwable t)
            {
                AppConfig.hideLoading(dialog);
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendUsertoNext()
    {
        String amoutnhsow=packageamount.getText().toString();
        if(action.equalsIgnoreCase("mapwithreferral"))
        {
            Fragment fragment = new Mappaymentviewfragment();
            Bundle arg = new Bundle();
            arg.putString("userenterreferrla",usereferrlacode);
            arg.putString("action", action);
            arg.putString("amount",amoutnhsow);
            arg.putString("selecetdpackage",Mapsubfragmentclick.selecetview);
            fragment.setArguments(arg);
            MainActivity.addFragment(fragment,false);
        }else {
            Fragment fragment = new Mappaymentviewfragment();
            Bundle arg = new Bundle();
            arg.putString("action", action);
            arg.putString("amount", amoutnhsow);
            arg.putString("selecetdpackage", Mapsubfragmentclick.selecetview);
            fragment.setArguments(arg);
            MainActivity.addFragment(fragment, true);
        }

    }
}