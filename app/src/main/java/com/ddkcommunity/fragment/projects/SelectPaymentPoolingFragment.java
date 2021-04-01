package com.ddkcommunity.fragment.projects;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.interfaces.GegtSettingStatusinterface;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.model.getSettingModel;
import com.ddkcommunity.utilies.AppConfig;

import java.math.BigDecimal;

import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowFunctionalityAlert;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectPaymentPoolingFragment extends Fragment implements View.OnClickListener {

    public SelectPaymentPoolingFragment() {

    }

    public static String selectedSubscription="";
    private LinearLayout btnPayUsingSamKoin,btnPayUsingCrypto, lytPayUsingCreditCard,
            lytSelectCoinPayment,btnPayUsingUSdt,btnPayUsingETH,btnPayUsingBTC;
    private TextView btnGoBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_payment_pooling, container, false);
        btnGoBack = view.findViewById(R.id.btnGoBack);
        btnPayUsingSamKoin=view.findViewById(R.id.btnPayUsingSamKoin);
        btnPayUsingCrypto = view.findViewById(R.id.btnPayUsingCrypto);
        lytSelectCoinPayment = view.findViewById(R.id.lytSelectCoinPayment);
        lytPayUsingCreditCard = view.findViewById(R.id.lytPayUsingCreditCard);
        btnPayUsingUSdt=view.findViewById(R.id.btnPayUsingUSdt);
        btnPayUsingETH=view.findViewById(R.id.btnPayUsingETH);
        btnPayUsingBTC=view.findViewById(R.id.btnPayUsingBTC);
        btnGoBack.setOnClickListener(this);
        btnPayUsingBTC.setOnClickListener(this);
        btnPayUsingUSdt.setOnClickListener(this);
        btnPayUsingETH.setOnClickListener(this);
        btnPayUsingSamKoin.setOnClickListener(this);
        btnPayUsingCrypto.setOnClickListener(this);
        lytPayUsingCreditCard.setOnClickListener(this);
        lytSelectCoinPayment.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btnGoBack)
        {
            getActivity().onBackPressed();
        }
        if (v.getId() == R.id.btnPayUsingSamKoin)
        {
            selectedSubscription="samkoin";
            getSettingServerOnTab(getActivity(),"samkoin");
        }
        if (v.getId() == R.id.btnPayUsingBTC)
        {
            selectedSubscription="btc";
            getSettingServerOnTab(getActivity(),"btc");
        }
        if (v.getId() == R.id.btnPayUsingETH)
        {
            selectedSubscription="eth";
            getSettingServerOnTab(getActivity(),"eth");
        }
        if (v.getId() == R.id.btnPayUsingUSdt)
        {
            selectedSubscription="usdt";
            getSettingServerOnTab(getActivity(),"usdt");
        }
        if (v.getId() == R.id.lytPayUsingCreditCard)
        {
            selectedSubscription="creditcard";
            getSettingServerOnTab(getActivity(),"creditcard");
        }
        if (v.getId() == R.id.lytSelectCoinPayment)
        {
            getSettingServerOnTab(getActivity(),"coin");
        }
        if (v.getId() == R.id.btnPayUsingCrypto)
        {
            getSettingServerOnTab(getActivity(),"ddk");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // HomeActivity.setHomeItem(getActivity(), R.id.profile);
        MainActivity.setTitle("Subscription");
        MainActivity.enableBackViews(true);
    }

    public void getSettingServerOnTab(Activity activity, final String functionname)
    {
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Please wait ....");
        String func="";
        if(functionname.equalsIgnoreCase("eth"))
        {
            func="subscription_using_eth";
        }
        if(functionname.equalsIgnoreCase("btc"))
        {
            func="subscription_using_btc";
        }
        if(functionname.equalsIgnoreCase("usdt"))
        {
            func="subscription_using_usdt";
        }
        if(functionname.equalsIgnoreCase("creditcard"))
        {
            func="subscription_using_credit_card";
        }
        if(functionname.equalsIgnoreCase("samkoin"))
        {
            func="subscription_using_sam_koin";
        }
        if(functionname.equalsIgnoreCase("ddk"))
        {
            func="subscription_using_ddk";
        }
        if(functionname.equalsIgnoreCase("coin"))
        {
            func="subscription_using_coin";
        }

        UserModel.getInstance().getSettignSatusView(activity,func,new GegtSettingStatusinterface()
        {
            @Override
            public void getResponse(Response<getSettingModel> response)
            {
                //   AppConfig.hideLoader();
                try
                {
                    if (response.body().getStatus() == 1)
                    {
                        if(functionname.equalsIgnoreCase("ddk"))
                        {
                            MainActivity.addFragment(new PayUsingDDKFragment(), true);
                        }else if(functionname.equalsIgnoreCase("coin"))
                        {
                            MainActivity.addFragment(new PayUsingCoinPaymentFragment(), true);
                        }
                        else{
                            MainActivity.addFragment(new PayUsingCryptoFragment(), true);
                        }
                           AppConfig.hideLoading(dialog);

                    } else
                    {
                        AppConfig.hideLoading(dialog);
                        ShowFunctionalityAlert(getActivity(), response.body().getMsg());
                    }

                } catch (Exception e)
                {
                    AppConfig.hideLoading(dialog);
                    e.printStackTrace();
                }
            }
        });
    }

}