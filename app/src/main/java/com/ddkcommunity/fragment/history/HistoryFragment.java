package com.ddkcommunity.fragment.history;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
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
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.CredentialListAdapter;
import com.ddkcommunity.adapters.HistoryAdapter;
import com.ddkcommunity.interfaces.GegtSettingStatusinterface;
import com.ddkcommunity.interfaces.GetAllCredential;
import com.ddkcommunity.model.PollingHistoryTransction;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.credential.CredentialsResponse;
import com.ddkcommunity.model.getSettingModel;
import com.ddkcommunity.model.summaryHistoryModel;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.fragment.wallet.FragmentCreatePassphrase.TAG;
import static com.ddkcommunity.utilies.dataPutMethods.ReplacecommaValue;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.ShowServerPost;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;
import static com.ddkcommunity.utilies.dataPutMethods.filter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private RecyclerView rvWalletHistory;
    TextView tvAmount_subscrip,tvAmount_ddkreward,tvAmount_samrewards,tvAmount_cashout,tvAmount_redeem;
    private Context mContext;
    private TabLayout tabLayout,tabstransactionistor;
    private ArrayList<PollingHistoryTransction.PoolingHistoryData> poolingHistoryData;
    LinearLayout transactionhistorylayout,bottom_view,summarylayout;
    TextView tvSamkoin,tvSamkoinsend,tvusdtsend,tvsamsend,tvethsend,tvddksend,tvbtcsend,tvusdt,tvsam,tveth,tvddk,tvbtc;
    private List<Credential> credentialList = new ArrayList<>();
    private BottomSheetDialog dialog;
    private CredentialListAdapter adapterCredential;
    WebView webView;
    private UserResponse userData;

    public HistoryFragment() {
        // Required empty public constructor
    }
    private int[] tabIcons = {
            R.drawable.ic_history,
            R.drawable.ic_asset__trans
          /*  , R.drawable.ic_ddk*/
    };

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        /*tabLayout.getTabAt(2).setIcon(tabIcons[2]);*/
      }
      //for other
      private int[] transactiontabIcons =
       {
               R.drawable.sam_new_l,
              R.drawable.ic_sam_icon_new,
              R.drawable.ic_bitcoin_small,
              R.drawable.ic_eth_small,
              R.drawable.ic_usdt_small,
               R.drawable.ic_ddk_small,
               R.drawable.ic_php_newda
       };

    private void settransactionupTabIcons()
    {
        tabstransactionistor.removeAllTabs();
        String countrydata=userData.getUser().country.get(0).country;
        String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");

        if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
        {
            tabstransactionistor.addTab(tabstransactionistor.newTab().setText("KOIN"));
            tabstransactionistor.addTab(tabstransactionistor.newTab().setText("PHP"));
            tabstransactionistor.addTab(tabstransactionistor.newTab().setText("POINTS"));
            tabstransactionistor.addTab(tabstransactionistor.newTab().setText("BTC"));
            tabstransactionistor.addTab(tabstransactionistor.newTab().setText("ETH"));
            tabstransactionistor.addTab(tabstransactionistor.newTab().setText("USDT"));
            tabstransactionistor.addTab(tabstransactionistor.newTab().setText("DDK \u25BC"));

            //foricon
            tabstransactionistor.getTabAt(0).setIcon(transactiontabIcons[0]);
            tabstransactionistor.getTabAt(1).setIcon(transactiontabIcons[6]);
            tabstransactionistor.getTabAt(2).setIcon(transactiontabIcons[1]);
            tabstransactionistor.getTabAt(3).setIcon(transactiontabIcons[2]);
            tabstransactionistor.getTabAt(4).setIcon(transactiontabIcons[3]);
            tabstransactionistor.getTabAt(5).setIcon(transactiontabIcons[4]);
            tabstransactionistor.getTabAt(6).setIcon(transactiontabIcons[5]);

        }else
        {
            tabstransactionistor.addTab(tabstransactionistor.newTab().setText("KOIN"));
            tabstransactionistor.addTab(tabstransactionistor.newTab().setText("POINTS"));
            tabstransactionistor.addTab(tabstransactionistor.newTab().setText("BTC"));
            tabstransactionistor.addTab(tabstransactionistor.newTab().setText("ETH"));
            tabstransactionistor.addTab(tabstransactionistor.newTab().setText("USDT"));
            tabstransactionistor.addTab(tabstransactionistor.newTab().setText("DDK \u25BC"));
            //for icon
            tabstransactionistor.getTabAt(0).setIcon(transactiontabIcons[0]);
            tabstransactionistor.getTabAt(1).setIcon(transactiontabIcons[1]);
            tabstransactionistor.getTabAt(2).setIcon(transactiontabIcons[2]);
            tabstransactionistor.getTabAt(3).setIcon(transactiontabIcons[3]);
            tabstransactionistor.getTabAt(4).setIcon(transactiontabIcons[4]);
            tabstransactionistor.getTabAt(5).setIcon(transactiontabIcons[5]);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mContext = getActivity();
        getSettingServerData(getActivity(),"php");
        userData = AppConfig.getUserData(mContext);
        webView=view.findViewById(R.id.webView);
        tabstransactionistor=view.findViewById(R.id.tabstransactionistor);
        transactionhistorylayout=view.findViewById(R.id.transactionhistorylayout);
        tvAmount_subscrip=view.findViewById(R.id.tvAmount_subscrip);
        tvAmount_ddkreward=view.findViewById(R.id.tvAmount_ddkreward);
        tvAmount_samrewards=view.findViewById(R.id.tvAmount_samrewards);
        tvAmount_cashout=view.findViewById(R.id.tvAmount_cashout);
        tvAmount_redeem=view.findViewById(R.id.tvAmount_redeem);
        summarylayout=view.findViewById(R.id.summarylayout);
        rvWalletHistory = view.findViewById(R.id.rvWalletHistory);
        tvSamkoinsend=view.findViewById(R.id.tvSamkoinsend);
        tvSamkoin=view.findViewById(R.id.tvSamkoin);
        tvusdtsend=view.findViewById(R.id.tvusdtsend);
        tvsamsend=view.findViewById(R.id.tvsamsend);
        tvethsend=view.findViewById(R.id.tvethsend);
        tvddksend=view.findViewById(R.id.tvddksend);
        tvbtcsend=view.findViewById(R.id.tvbtcsend);
        bottom_view=view.findViewById(R.id.bottom_view);
        tvusdt=view.findViewById(R.id.tvusdt);
        tvsam=view.findViewById(R.id.tvsam);
        tveth=view.findViewById(R.id.tveth);
        tvddk=view.findViewById(R.id.tvddk);
        tvbtc=view.findViewById(R.id.tvbtc);
        //for tab
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("History"));
        tabLayout.addTab(tabLayout.newTab().setText("Summary"));
       /* tabLayout.addTab(tabLayout.newTab().setText("DDK Explorer"));*/
        setupTabIcons();
        //.......
        for(int i=0; i < tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 20, 0);
            tab.requestLayout();
        }
        //..........
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                if (tab.getPosition() == 0)
                {
                    webView.setVisibility(View.GONE);
                    transactionhistorylayout.setVisibility(View.VISIBLE);
                    summarylayout.setVisibility(View.GONE);
                    tabstransactionistor.selectTab(tab);
                } else if (tab.getPosition() == 1)
                {
                    webView.setVisibility(View.GONE);
                    summarylayout.setVisibility(View.VISIBLE);
                    transactionhistorylayout.setVisibility(View.GONE);
                    getPolingTransactionHistorySumm();
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
        //for subtransaction
        tabstransactionistor= view.findViewById(R.id.tabstransactionistor);
        settransactionupTabIcons();
        for(int i=0; i < tabstransactionistor.getTabCount(); i++) {
            View tab = ((ViewGroup) tabstransactionistor.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 20, 0);
            tab.requestLayout();
        }

        tabstransactionistor.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                webView.setVisibility(View.GONE);
                getSettingServerData(getActivity(),"php");
                String curkjnew= App.pref.getString(Constant.PHP_Functionality_View, "");
                String countrydata=userData.getUser().country.get(0).country;
                if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkjnew.equalsIgnoreCase("true")))
                {
                        if (tab.getPosition() == 0)
                        {
                            setblankData();
                            String walletaddress = App.pref.getString(Constant.SAMKOIN_ADD, "");
                            String walletaddressid = App.pref.getString(Constant.SAMKOIN_Add_Id, "");
                            getPolingTransactionHistory(walletaddressid, walletaddress, "sam_koin");

                        }else
                        if (tab.getPosition() == 1)
                        {
                            setblankData();
                            String walletaddress = "";
                            String walletaddressid = "";
                            getPolingTransactionHistory(walletaddressid, walletaddress, "currency_wallet");
                        }else if (tab.getPosition() == 2)
                        {
                            setblankData();
                            String walletaddress = "";
                            String walletaddressid = "";
                            getPolingTransactionHistory(walletaddressid, walletaddress, "sam");
                        } else if (tab.getPosition() == 3)
                        {
                            setblankData();
                            String walletaddressid = App.pref.getString(Constant.BTC_Add_Id, "");
                            String walletaddress = App.pref.getString(Constant.BTC_ADD, "");
                            getPolingTransactionHistory(walletaddressid, walletaddress, "btc");
                            //for btc
                   /* final String linkvalue="https://live.blockcypher.com/btc/address/"+walletaddress;
                    if(linkvalue!=null)
                    {
                        webView.setVisibility(View.VISIBLE);
                        //for webivew
                        WebSettings settings = webView.getSettings();
                        settings.setJavaScriptEnabled(true);
                        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                        final ProgressDialog progressBar = ProgressDialog.show(getActivity(), "", "Loading...");
                        progressBar.setCanceledOnTouchOutside(false);
                        progressBar.show();
                        webView.loadUrl(linkvalue);
                        webView.setWebViewClient(new WebViewClient() {
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                Log.i(TAG, "Processing webview url click...");
                                view.loadUrl(url);
                                return true;
                            }

                            public void onPageFinished(WebView view, String url) {
                                Log.i(TAG, "Finished loading URL: " +url);
                                if (progressBar.isShowing()) {
                                    progressBar.dismiss();
                                }
                            }

                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                Log.e(TAG, "Error: " + description);
                                Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                            }
                        });
                        //end webview
                    }else
                    {
                        Toast.makeText(mContext, "Link not available ", Toast.LENGTH_SHORT).show();
                    }*/
                        } else if (tab.getPosition() == 4) {
                            setblankData();
                            String walletaddress = App.pref.getString(Constant.Eth_ADD, "");
                            String walletaddressid = App.pref.getString(Constant.Eth_ADD_ID, "");
                            getPolingTransactionHistory(walletaddressid, walletaddress, "eth");
                        } else if (tab.getPosition() == 5) {
                            setblankData();
                            String walletaddress = App.pref.getString(Constant.USDT_ADD, "");
                            String walletaddressid = App.pref.getString(Constant.USDT_Add_Id, "");
                            getPolingTransactionHistory(walletaddressid, walletaddress, "usdt");
                        } else if (tab.getPosition() == 6) {
                            setblankData();
                        }
                    }else
                    {
                        if (tab.getPosition() == 0)
                        {
                            setblankData();
                            String walletaddress = App.pref.getString(Constant.SAMKOIN_ADD, "");
                            String walletaddressid = App.pref.getString(Constant.SAMKOIN_Add_Id, "");
                            getPolingTransactionHistory(walletaddressid, walletaddress, "sam_koin");

                        }else
                        if (tab.getPosition() == 1) {
                            setblankData();
                            String walletaddress = "";
                            String walletaddressid = "";
                            getPolingTransactionHistory(walletaddressid, walletaddress, "sam");

                        } else if (tab.getPosition() == 2) {
                            setblankData();
                            String walletaddressid = App.pref.getString(Constant.BTC_Add_Id, "");
                            String walletaddress = App.pref.getString(Constant.BTC_ADD, "");
                            getPolingTransactionHistory(walletaddressid, walletaddress, "btc");
                            //for btc
                   /* final String linkvalue="https://live.blockcypher.com/btc/address/"+walletaddress;
                    if(linkvalue!=null)
                    {
                        webView.setVisibility(View.VISIBLE);
                        //for webivew
                        WebSettings settings = webView.getSettings();
                        settings.setJavaScriptEnabled(true);
                        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                        final ProgressDialog progressBar = ProgressDialog.show(getActivity(), "", "Loading...");
                        progressBar.setCanceledOnTouchOutside(false);
                        progressBar.show();
                        webView.loadUrl(linkvalue);
                        webView.setWebViewClient(new WebViewClient() {
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                Log.i(TAG, "Processing webview url click...");
                                view.loadUrl(url);
                                return true;
                            }

                            public void onPageFinished(WebView view, String url) {
                                Log.i(TAG, "Finished loading URL: " +url);
                                if (progressBar.isShowing()) {
                                    progressBar.dismiss();
                                }
                            }

                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                Log.e(TAG, "Error: " + description);
                                Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                            }
                        });
                        //end webview
                    }else
                    {
                        Toast.makeText(mContext, "Link not available ", Toast.LENGTH_SHORT).show();
                    }*/
                        } else if (tab.getPosition() == 3) {
                            setblankData();
                            String walletaddress = App.pref.getString(Constant.Eth_ADD, "");
                            String walletaddressid = App.pref.getString(Constant.Eth_ADD_ID, "");
                            getPolingTransactionHistory(walletaddressid, walletaddress, "eth");
                        } else if (tab.getPosition() == 4) {
                            setblankData();
                            String walletaddress = App.pref.getString(Constant.USDT_ADD, "");
                            String walletaddressid = App.pref.getString(Constant.USDT_Add_Id, "");
                            getPolingTransactionHistory(walletaddressid, walletaddress, "usdt");
                        } else if (tab.getPosition() == 5) {
                            setblankData();
                        }
                    }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("Selected", "");
                String curkjnew= App.pref.getString(Constant.PHP_Functionality_View, "");
                String countrydata=userData.getUser().country.get(0).country;
                if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkjnew.equalsIgnoreCase("true")))
                {
                    if (tab.getPosition() == 6) {
                        if (credentialList.size() > 0) {
                            if (!dialog.isShowing()) {
                                dialog.show();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    }
                }else {
                    if (tab.getPosition() == 5) {
                        if (credentialList.size() > 0) {
                            if (!dialog.isShowing()) {
                                dialog.show();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    }
                }
            }
        });
        initTheCredentialView();
        UserModel.getInstance().getCredentialsCall(mContext, new GetAllCredential() {
            @Override
            public void getCredential(List<Credential> credentials) {
                credentialList = new ArrayList<>();
                credentialList.addAll(credentials);
            }
        });

        getCredentialsCall();
        //,,,,,,,,,,,,,
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvWalletHistory.setLayoutManager(mLayoutManager);
        rvWalletHistory.setItemAnimator(new DefaultItemAnimator());
        String walletaddress= App.pref.getString(Constant.SAMKOIN_ADD, "");
        String walletaddressid= App.pref.getString(Constant.SAMKOIN_Add_Id, "");
        getPolingTransactionHistory(walletaddressid,walletaddress,"sam_koin");
        return view;
    }


    public void getSettingServerData(Activity activity, final String functionname)
    {
        String func=functionname;
        func=functionname;
        UserModel.getInstance().getSettignSatusView(activity,func,new GegtSettingStatusinterface()
        {
            @Override
            public void getResponse(Response<getSettingModel> response)
            {
                try
                {
                    if (response.body().getStatus() == 1)
                    {
                        if(functionname.equalsIgnoreCase("php"))
                        {
                            String curkj = App.pref.getString(Constant.PHP_Functionality_View, "");
                            if (!curkj.equalsIgnoreCase("true"))
                            {
                                MainActivity.updateTabview=1;
                                App.editor.putString(Constant.PHP_Functionality_View, "true");
                                App.editor.apply();
                                MainActivity.updateTabview=1;
                                settransactionupTabIcons();
                            }
                        }

                    } else
                    {
                        if(functionname.equalsIgnoreCase("php"))
                        {
                            String curkj = App.pref.getString(Constant.PHP_Functionality_View, "");
                            if (!curkj.equalsIgnoreCase("false")) {
                                App.editor.putString(Constant.PHP_Functionality_View, "false");
                                App.editor.apply();
                                MainActivity.updateTabview=1;
                                settransactionupTabIcons();
                            }

                        }
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    //for cashoutnew
    private void initTheCredentialView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View dialogView = layoutInflater.inflate(R.layout.popup_wallet_pooling, null);
        dialog= new BottomSheetDialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(dialogView);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        final EditText searchEt = dialogView.findViewById(R.id.search_ET);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapterCredential = new CredentialListAdapter(credentialList, mContext, searchEt, new CredentialListAdapter.SetOnItemClickListener() {
            @Override
            public void onItemClick(String wallet_code, String walletId1, String passPhrese) {
                hideKeyBoard();
                dialog.dismiss();
                getPolingTransactionHistory(walletId1,wallet_code,"ddk");

            }
        });

        recyclerView.setAdapter(adapterCredential);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchEt.getText().toString().length() > 0) {
                    filter(searchEt.getText().toString());
                } else {
                    adapterCredential.updateData(credentialList);
                }
            }
        });
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void getCredentialsCall()
    {
        if (AppConfig.isInternetOn())
        {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
            hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
            Call<ResponseBody> call = AppConfig.getLoadInterface().credentialList(AppConfig.getStringPreferences(mContext, Constant.JWTToken),hm);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {
                                credentialList = new ArrayList<>();
                                CredentialsResponse registerResponse = new Gson().fromJson(responseData, CredentialsResponse.class);
                                for (Credential credential : registerResponse.getCredentials()) {
                                    if (credential.getStatus().equalsIgnoreCase("active")) {
                                        credentialList.add(credential);
                                    }
                                }
                                adapterCredential.updateData(credentialList);
                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity(getActivity());
                            } else {
                                AppConfig.showToast(object.getString("msg"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        AppConfig.showToast("Server error");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // HomeActivity.setHomeItem(getActivity(), R.id.profile);
        MainActivity.setTitle("Transaction History");
        MainActivity.enableBackViews(true);
    }

    private void getPolingTransactionHistory(final String wallet_codeid,final String wallet_code,final String wallet_type) {
        AppConfig.showLoading("Loading...", mContext);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("wallet_address",wallet_code);
        hm.put("wallet_type", wallet_type);
        Log.d("pooling",hm.toString());

        AppConfig.getLoadInterface().getAllTransactionHistory(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken),hm).enqueue(new Callback<PollingHistoryTransction>() {
            @Override
            public void onResponse(Call<PollingHistoryTransction> call, Response<PollingHistoryTransction> response)
            {
                Log.d("transaction",response.toString());
                if (response.isSuccessful() && response.body() != null)
                {
                    if (response.body().status == 1)
                    {
                        Log.d("transaction-history",response.body().msg);
                        poolingHistoryData=new ArrayList<>();
                        if(response.body().lendData!=null && response.body().lendData.size()!=0)
                        {
                            poolingHistoryData.addAll(response.body().lendData);
                        }
                        if(response.body().rewardData!=null && response.body().rewardData.size()!=0)
                        {
                            poolingHistoryData.addAll(response.body().rewardData);
                        }
                        if(response.body().Data2!=null && response.body().Data2.size()!=0)
                        {
                            poolingHistoryData.addAll(response.body().Data2);
                        }
                        if(response.body().Data3!=null && response.body().Data3.size()!=0)
                        {
                            poolingHistoryData.addAll(response.body().Data3);
                        }
                        if(response.body().Data4!=null && response.body().Data4.size()!=0)
                        {
                            poolingHistoryData.addAll(response.body().Data4);
                        }
                        if(response.body().Data5!=null && response.body().Data5.size()!=0)
                        {
                            poolingHistoryData.addAll(response.body().Data5);
                        }
                        if(response.body().Data6!=null && response.body().Data6.size()!=0)
                        {
                            poolingHistoryData.addAll(response.body().Data6);
                        }
                        if(response.body().Data7!=null && response.body().Data7.size()!=0)
                        {
                            poolingHistoryData.addAll(response.body().Data7);
                        }
                        if(response.body().Data8!=null && response.body().Data8.size()!=0)
                        {
                            poolingHistoryData.addAll(response.body().Data8);
                        }
                        if(response.body().redeem_data!=null && response.body().redeem_data.size()!=0)
                        {
                            poolingHistoryData.addAll(response.body().redeem_data);
                        }
                        if(response.body().currency_wallet!=null && response.body().currency_wallet.size()!=0)
                        {
                            poolingHistoryData.addAll(response.body().currency_wallet);
                        }

                        Collections.sort(poolingHistoryData, new Comparator<PollingHistoryTransction.PoolingHistoryData>()
                        {
                            @Override
                            public int compare(PollingHistoryTransction.PoolingHistoryData o1, PollingHistoryTransction.PoolingHistoryData o2) {
                                DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                try {
                                    return f.parse(o2.created_at).compareTo(f.parse(o1.created_at));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    return 0;
                                }
                            }
                        });

                        HistoryAdapter mAdapter = new HistoryAdapter(wallet_code,poolingHistoryData, getActivity());
                        rvWalletHistory.setAdapter(mAdapter);
                        AppConfig.hideLoader();

                    }else if (response.body().status == 3)
                    {
                        setblankData();
                        AppConfig.hideLoader();
                        AppConfig.showToast(response.body().msg);
                        AppConfig.openSplashActivity(getActivity());
                    }else if (response.body().status == 4)
                    {
                        AppConfig.hideLoader();
                        ShowServerPost((Activity)mContext,"ddk server error transaction history ");
                    }
                } else
                {
                    setblankData();
                    AppConfig.hideLoader();
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PollingHistoryTransction> call, Throwable t) {
                setblankData();
                AppConfig.hideLoader();
                //Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

    public void setblankData()
    {
        poolingHistoryData=new ArrayList<>();
        HistoryAdapter mAdapter = new HistoryAdapter("",poolingHistoryData, getActivity());
        rvWalletHistory.setAdapter(mAdapter);
    }

    private void getPolingTransactionHistorySumm()
    {
        AppConfig.showLoading("Loading....",getContext());
        AppConfig.getLoadInterface().getPoolingTransactionHistory(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken)).enqueue(new Callback<summaryHistoryModel>() {
            @Override
            public void onResponse(Call<summaryHistoryModel> call, Response<summaryHistoryModel> response)
            {
                AppConfig.hideLoader();
                if (response.isSuccessful() && response.body() != null)
                {
                    Log.d("history",""+response.body().getData());
                    if (response.body().getStatus()== 1)
                    {
                        try
                        {
                            String subscriptionvalue= String.valueOf(response.body().getData().getSubscription());
                            subscriptionvalue=ReplacecommaValue(subscriptionvalue+"");
                            String samRewardvalue= String.valueOf(response.body().getData().getSamReward());
                            samRewardvalue=ReplacecommaValue(samRewardvalue+"");
                            String redeemvalue= String.valueOf(response.body().getData().getRedeem());
                            redeemvalue=ReplacecommaValue( redeemvalue+"");
                            String cashoutvalue= String.valueOf(response.body().getData().getCashout());
                            cashoutvalue=ReplacecommaValue(cashoutvalue+"");
                            String ddkrewardvalue= String.valueOf(response.body().getData().getDdkReward());
                            ddkrewardvalue=ReplacecommaValue(ddkrewardvalue+"");
                            String btcSendvalue= String.valueOf(response.body().getData().getBtcSend());
                            btcSendvalue=ReplacecommaValue(btcSendvalue+"");
                            String ethSendvalue= String.valueOf(response.body().getData().getEthSend());
                            ethSendvalue=ReplacecommaValue(ethSendvalue+"");
                            String usdtSendvalue= String.valueOf(response.body().getData().getUsdtSend());
                            usdtSendvalue=ReplacecommaValue(usdtSendvalue+"");
                            String ddkSendvalue= String.valueOf(response.body().getData().getBtcSend());
                            ddkSendvalue=ReplacecommaValue(ddkSendvalue+"");
                            String ddkBuyvalue= String.valueOf(response.body().getData().getDdk_buy());
                            ddkBuyvalue=ReplacecommaValue(ddkBuyvalue+"");
                            String btcbuyvalue= String.valueOf(response.body().getData().getBtc_buy());
                            btcbuyvalue=ReplacecommaValue(btcbuyvalue+"");
                            String ethbuyvalue= String.valueOf(response.body().getData().getEth_buy());
                            ethbuyvalue=ReplacecommaValue(ethbuyvalue+"");
                            String usdtbuyvalue= String.valueOf(response.body().getData().getUsdt_buy());
                            usdtbuyvalue=ReplacecommaValue(usdtbuyvalue+"");

                            String samsendkoinvalue= String.valueOf(response.body().getData().getSam_send());
                            samsendkoinvalue=ReplacecommaValue(samsendkoinvalue+"");

                            String samkoinbuyvalue= String.valueOf(response.body().getData().getSam_buy());
                            samkoinbuyvalue=ReplacecommaValue(samkoinbuyvalue+"");

                            //...........
                            tvAmount_subscrip.setText(new BigDecimal(subscriptionvalue).toPlainString()+" USDT");
                            tvAmount_ddkreward.setText(new BigDecimal(ddkrewardvalue).toPlainString()+" DDK");
                            tvAmount_samrewards.setText(new BigDecimal(samRewardvalue).toPlainString()+" POINT");
                            tvAmount_cashout.setText(new BigDecimal(cashoutvalue).toPlainString()+" SAM Koin");
                            tvAmount_redeem.setText(new BigDecimal(redeemvalue).toPlainString()+" SAM");
                            //......
                            tvSamkoinsend.setText(new BigDecimal(samsendkoinvalue).toPlainString()+" SAM Koin");
                            tvusdtsend.setText(new BigDecimal(usdtSendvalue).toPlainString()+" USDT");
                            tvsamsend.setVisibility(View.GONE);
                            tvethsend.setText(new BigDecimal(ethSendvalue).toPlainString()+" ETH");
                            tvddksend.setText(new BigDecimal(ddkSendvalue).toPlainString()+" DDK");
                            tvbtcsend.setText(new BigDecimal(btcSendvalue)+" BTC");
                            //..................
                            tvSamkoin.setText(new BigDecimal(samkoinbuyvalue).toPlainString()+" SAM Koin");
                            tvddk.setText(new BigDecimal(ddkBuyvalue).toPlainString()+" DDK");
                            tvbtc.setText(new BigDecimal(btcbuyvalue).toPlainString()+" BTC");
                            tveth.setText(new BigDecimal(ethbuyvalue).toPlainString()+" ETH");
                            tvusdt.setText(new BigDecimal(usdtbuyvalue).toPlainString()+" USDT");
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }else {
                        AppConfig.showToast(response.body().getMsg());
                    }
                } else {
                    ShowApiError(mContext,"server error ninethface/buy-crypto-list");
                }
            }

            @Override
            public void onFailure(Call<summaryHistoryModel> call, Throwable t) {
                errordurigApiCalling(getActivity(),t.getMessage());
                AppConfig.hideLoader();
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

}
