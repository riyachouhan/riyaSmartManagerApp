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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.activities.OTPActivity;
import com.ddkcommunity.fragment.send.SuccessFragmentScan;
import com.ddkcommunity.interfaces.GegtSettingStatusinterface;
import com.ddkcommunity.model.checkRefferalModel;
import com.ddkcommunity.model.getSettingModel;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.model.userInviteModel;
import com.ddkcommunity.utilies.AppConfig;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapreferralFragment extends Fragment implements View.OnClickListener
{

    AppCompatButton submit_BT;
    TextView skip;
    EditText email_ET;
    private UserResponse userData;
    ProgressDialog dialog;

    public MapreferralFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mapreferralfragment, container, false);
        submit_BT=view.findViewById(R.id.submit_BT);
        skip=view.findViewById(R.id.skip);
        userData = AppConfig.getUserData(getContext());
        email_ET=view.findViewById(R.id.email_ET);
        SpannableString content = new SpannableString("Skip this step");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        skip.setText(content);
        submit_BT.setOnClickListener(this);
        skip.setOnClickListener(this);
        getSettingServerOnTab("map_referral_code_validation",getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Referral Code");
        MainActivity.enableBackViews(true);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.submit_BT)
        {
            String email_ETvalue=email_ET.getText().toString();
            if (email_ETvalue.equalsIgnoreCase(""))
            {
                Toast.makeText(getContext(), "Please Enter Referral Code", Toast.LENGTH_SHORT).show();
            }else
            {
                CheckUserReferral(email_ETvalue);

            }
        }

        if(v.getId()==R.id.skip)
        {
            CheckUserReferral("JOIN SAM NOW");
        }

    }

    public void getSettingServerOnTab(String functionname, Activity activity)
    {
        dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Please wait ....");
        String func=functionname;
        UserModel.getInstance().getSettignSatusView(activity,func, new GegtSettingStatusinterface()
        {
            @Override
            public void getResponse(Response<getSettingModel> response)
            {
                try
                {
                    AppConfig.hideLoading(dialog);
                    if (response.body().getStatus() == 1)
                    {
                       skip.setVisibility(View.VISIBLE);
                    } else
                    {
                        skip.setVisibility(View.GONE);
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void CheckUserReferral(final String userrefferal)
    {
        dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Please wait ....");
        String userReferalCode = App.pref.getString(Constant.USER_REFERAL_CODE, "");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("ref_code", userrefferal);
        AppConfig.getLoadInterfaceMap().getRefferalCode(hm).enqueue(new Callback<checkRefferalModel>() {
            @Override
            public void onResponse(Call<checkRefferalModel> call, Response<checkRefferalModel> response) {
                try {
                    Log.d("sam erro par invi",response.body().toString());
                    if (response.isSuccessful() && response.body() != null)
                    {
                        if (response.body().getStatus().equalsIgnoreCase("true"))
                        {
                            CheckSusbcription(userrefferal);
                        }else
                        {
                            AppConfig.hideLoading(dialog);
                            Toast.makeText(getContext(), "Referral Code Is Invalid", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppConfig.hideLoading(dialog);
                        ShowApiError(getContext(),"server error check-mail-exist");
                    }

                } catch (Exception e) {
                    AppConfig.hideLoading(dialog);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<checkRefferalModel> call, Throwable t)
            {
                AppConfig.hideLoading(dialog);
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CheckSusbcription(final String userrefferal)
    {
        AppConfig.getLoadInterface().checkSusbcription(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken)).enqueue(new Callback<checkRefferalModel>()
        {
            @Override
            public void onResponse(Call<checkRefferalModel> call, Response<checkRefferalModel> response) {
                try {
                    Log.d("sam erro par invi",response.body().toString());
                    if (response.isSuccessful() && response.body() != null)
                    {
                        if (response.body().getStatus().equalsIgnoreCase("1"))
                        {
                            AppConfig.hideLoading(dialog);
                            Fragment fragment = new Mapsubfragmentclick();
                            Bundle arg = new Bundle();
                            arg.putString("userenterreferrla",userrefferal);
                            arg.putString("action", "mapwithreferral");
                            fragment.setArguments(arg);
                            MainActivity.addFragment(fragment,false);
                            //registerUserMap(userrefferal);
                        }else
                        {
                            AppConfig.hideLoading(dialog);
                            Toast.makeText(getContext(), response.body().getMsg().toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppConfig.hideLoading(dialog);
                        ShowApiError(getContext(),"server error check-mail-exist");
                    }

                } catch (Exception e) {
                    AppConfig.hideLoading(dialog);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<checkRefferalModel> call, Throwable t)
            {
                AppConfig.hideLoading(dialog);
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}