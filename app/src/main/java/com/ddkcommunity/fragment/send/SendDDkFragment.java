package com.ddkcommunity.fragment.send;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.CredentialListAdapter;
import com.ddkcommunity.fragment.CancellationFragment;
import com.ddkcommunity.fragment.CashOutFragmentNew;
import com.ddkcommunity.fragment.HomeFragment;
import com.ddkcommunity.fragment.SamKoinsRedeemFragment;
import com.ddkcommunity.fragment.buy.BuyFragment;
import com.ddkcommunity.fragment.projects.TermsAndConsitionSubscription;
import com.ddkcommunity.interfaces.GegtSettingStatusinterface;
import com.ddkcommunity.interfaces.GetAllCredential;
import com.ddkcommunity.interfaces.GetAvailableValue;
import com.ddkcommunity.interfaces.GetBTCUSDTETHPriceCallback;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.model.OtpResponse;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.App;
import com.ddkcommunity.model.credential.CredentialsResponse;
import com.ddkcommunity.model.getSettingModel;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.model.wallet.WalletResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.Constant;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ncorti.slidetoact.SlideToActView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.ddkcommunity.utilies.dataPutMethods.ReplacecommaValue;
import static com.ddkcommunity.utilies.dataPutMethods.ShowCahsoutDialog;
import static com.ddkcommunity.utilies.dataPutMethods.ShowFunctionalityAlert;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendDDkFragment extends Fragment implements View.OnClickListener {

    private double fees;
    private EditText etWalletAddress,etDDKCoins;
    private TextWatcher tt = null;
    private String walletId;
    private StringBuffer str_top;
    private List<Credential> credentialList = new ArrayList<>();
    private CredentialListAdapter adapterCredential;
    private Context mContext;
    private BottomSheetDialog dialog;
    private View rootView = null;
    private PopupWindow pw;
    private SlideToActView slide_custom_icon;
    private String ddkSecret = "";
    private String currentWalletBalance;
    private String otp = "";
    private TextView btnGoHome, totalsendamount;
    private TextView tvTransactionId, tvOrderStatus;
    private ImageView ivTransactionCreateCheck, ivTransactionCreateUncheck, ivOrderStatusIconUnCheck, ivOrderStatusIconCheck;
    private AppCompatImageView btnCopyTransactionId;
    private FrameLayout progress_bar;
    private ClipboardManager clipboard;
    private ClipData clip;
    private TextView transactionfee;
    private int count = 0,tabclick=0;

    public SendDDkFragment()
    {
        // Required empty public constructor
    }

    public static Fragment getInstance(String data)
    {
        Fragment fragment = new SendDDkFragment();
        Bundle arg = new Bundle();
        arg.putString("address", data);
        fragment.setArguments(arg);
        return fragment;
    }
    String ddkselectaddress="";
    private ProgressBar progressBar;
    private TabLayout tabLayout;
    LinearLayout buysell_layout;
    private TextView hintform,tvSellPrice, tvBuyPrice, tvAddressCode, tvWalletBalance;
    private String clickAddressname="";
    private UserResponse userData;

    private int[] tabIcons = {
            R.drawable.sam_new_l,
            R.drawable.ic_bitcoin,
            R.drawable.ic_eth,
            R.drawable.ic_usdt,
            R.drawable.ic_ddk,
            R.drawable.ic_php_newda,
            R.drawable.tron_icon
    };

    private void setupTabIcons()
    {
        String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
        String countrydata=userData.getUser().country.get(0).country;
        if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
        {
            tabLayout.getTabAt(0).setIcon(tabIcons[5]);
            tabLayout.getTabAt(1).setIcon(tabIcons[0]);
            tabLayout.getTabAt(2).setIcon(tabIcons[1]);
          //  tabLayout.getTabAt(3).setIcon(tabIcons[6]);
            tabLayout.getTabAt(3).setIcon(tabIcons[2]);
            tabLayout.getTabAt(4).setIcon(tabIcons[3]);
            tabLayout.getTabAt(5).setIcon(tabIcons[4]);
        }else
        {
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
           // tabLayout.getTabAt(2).setIcon(tabIcons[6]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            tabLayout.getTabAt(3).setIcon(tabIcons[3]);
            tabLayout.getTabAt(4).setIcon(tabIcons[4]);
        }
    }

    private void latestDDKPrice() {
        UserModel.getInstance().getUSDCall(new GetUSDAndBTCCallback() {
            @Override
            public void getValues(BigDecimal btc, BigDecimal usd) {
                if (usd != null)
                {
                    BigDecimal ONE_HUNDRED = new BigDecimal(100);
                    BigDecimal buy = usd.multiply(UserModel.getInstance().ddkBuyPercentage).divide(ONE_HUNDRED);
                    BigDecimal sell = usd.multiply(UserModel.getInstance().ddkSellPercentage).divide(ONE_HUNDRED);

                    if (UserModel.getInstance().ddkBuyPrice == null) {
                        UserModel.getInstance().ddkBuyPrice = buy.add(usd);
                        UserModel.getInstance().ddkSellPrice = usd.subtract(sell);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("updatePrice"));
                    }
                }
            }
        },getActivity());
//
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            View view = inflater.inflate(R.layout.fragment_send_ddk, container, false);
            rootView = view;
            mContext = getActivity();
            etWalletAddress=view.findViewById(R.id.etWalletAddress);
            try {
                if(getArguments().getString("address")!=null) {
                    ddkselectaddress = getArguments().getString("address");
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            buysell_layout=view.findViewById(R.id.buysell_layout);
            progress_bar =view.findViewById(R.id.progress_bar);
            tabLayout = view.findViewById(R.id.tabs);
            hintform=view.findViewById(R.id.hintform);
            userData = AppConfig.getUserData(mContext);
            String countrydata=userData.getUser().country.get(0).country;
            String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
            if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
            {
                tabLayout.addTab(tabLayout.newTab().setText("PHP"));
                tabLayout.addTab(tabLayout.newTab().setText("KOIN"));
                tabLayout.addTab(tabLayout.newTab().setText("BTC"));
              //  tabLayout.addTab(tabLayout.newTab().setText("TRON"));
                tabLayout.addTab(tabLayout.newTab().setText("ETH"));
                tabLayout.addTab(tabLayout.newTab().setText("USDT"));
                tabLayout.addTab(tabLayout.newTab().setText("DDK \u25BC"));
                setupTabIcons();
            }else
            {
                tabLayout.addTab(tabLayout.newTab().setText("KOIN"));
                tabLayout.addTab(tabLayout.newTab().setText("BTC"));
                //tabLayout.addTab(tabLayout.newTab().setText("TRON"));
                tabLayout.addTab(tabLayout.newTab().setText("ETH"));
                tabLayout.addTab(tabLayout.newTab().setText("USDT"));
                tabLayout.addTab(tabLayout.newTab().setText("DDK \u25BC"));
                setupTabIcons();
            }
            totalsendamount=view.findViewById(R.id.totalsendamount);
            transactionfee=view.findViewById(R.id.transactionfee);
            etDDKCoins=view.findViewById(R.id.etDDKCoins);
            tvWalletBalance = view.findViewById(R.id.tvWalletBalance);
            tvAddressCode = view.findViewById(R.id.tvAddressCode);
            tvBuyPrice = view.findViewById(R.id.tvBuyPrice);
            tvSellPrice = view.findViewById(R.id.tvSellPrice);
            progressBar = view.findViewById(R.id.progressBar);
            slide_custom_icon=view.findViewById(R.id.slide_custom_icon);
            currentWalletBalance=App.pref.getString(Constant.CurrentBalance, "");
            currentWalletBalance=ReplacecommaValue(currentWalletBalance+"");
            ddkSecret=App.pref.getString(Constant.Secret,"");
            initTheCredentialView();
            etDDKCoins.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {

                }

                @Override
                public void afterTextChanged(Editable editable)
                {
                    try {

                        if (etDDKCoins.getText().toString().length() > 0)
                        {
                            if(!etDDKCoins.getText().toString().equalsIgnoreCase("0") && !etDDKCoins.getText().toString().equalsIgnoreCase("0."))
                            {
                                String entervalue=etDDKCoins.getText().toString();
                                BigDecimal etDDKValue = new BigDecimal(entervalue);
                                //..........
                                String transcationfee="";
                                String countrydata=userData.getUser().country.get(0).country;
                                String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
                                if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
                                {
                                    if (tabclick == 1)
                                    {
                                        transcationfee = App.pref.getString(Constant.sendphp_transaction_fees, "");
                                    } else if (tabclick == 2) {
                                        transcationfee = App.pref.getString(Constant.sendsam_transaction_fees, "");
                                    } else if (tabclick == 3) {
                                        transcationfee = App.pref.getString(Constant.sendbtc_transaction_fees, "");
                                    } /*else if (tabclick == 4) {
                                        transcationfee = App.pref.getString(Constant.sendtron_transaction_fees, "");
                                    } */else if (tabclick == 4) {
                                        transcationfee = App.pref.getString(Constant.sendeth_transaction_fees, "");
                                    } else if (tabclick == 5) {
                                        transcationfee = App.pref.getString(Constant.sendusdt_transaction_fees, "");
                                    } else if (tabclick == 6) {
                                        transcationfee = "0";
                                    }

                                }else {
                                    if (tabclick == 1) {
                                        transcationfee = App.pref.getString(Constant.sendsam_transaction_fees, "");
                                    } else if (tabclick == 2) {
                                        transcationfee = App.pref.getString(Constant.sendbtc_transaction_fees, "");
                                    } /*else if (tabclick == 3) {
                                        transcationfee = App.pref.getString(Constant.sendtron_transaction_fees, "");
                                    } */else if (tabclick == 3) {
                                        transcationfee = App.pref.getString(Constant.sendeth_transaction_fees, "");
                                    } else if (tabclick == 4) {
                                        transcationfee = App.pref.getString(Constant.sendusdt_transaction_fees, "");
                                    } else if (tabclick == 5) {
                                        transcationfee = "0";
                                    }
                                }
                                //...........
                                BigDecimal transactionfeemain;
                                BigDecimal transactionfeeam;
                                 if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
                                 {
                                     if (tabclick ==3)
                                     {
                                        String transaction_fees_mode_eth = App.pref.getString(Constant.transaction_fees_mode_eth, "");
                                        if(transaction_fees_mode_eth.equalsIgnoreCase("fix"))
                                        {
                                            transactionfeeam=(new BigDecimal(transcationfee));
                                        }else {
                                            transactionfeemain = (new BigDecimal(transcationfee));
                                            transactionfeeam = etDDKValue.multiply(transactionfeemain);
                                        }
                                     } else if(tabclick==4)
                                     {
                                         String transaction_fees_mode_eth = App.pref.getString(Constant.transaction_fees_mode_usdt, "");
                                         if(transaction_fees_mode_eth.equalsIgnoreCase("fix"))
                                         {
                                             transactionfeeam=(new BigDecimal(transcationfee));
                                         }else {
                                             transactionfeemain = (new BigDecimal(transcationfee));
                                             transactionfeeam = etDDKValue.multiply(transactionfeemain);
                                         }
                                     }else
                                     {
                                         transactionfeemain=(new BigDecimal(transcationfee))/*.divide(BigDecimal.valueOf(100))*/;
                                         transactionfeeam=etDDKValue.multiply(transactionfeemain);
                                     }
                                }else {
                                     if (tabclick ==2)
                                     {
                                         String transaction_fees_mode_eth = App.pref.getString(Constant.transaction_fees_mode_eth, "");
                                         if(transaction_fees_mode_eth.equalsIgnoreCase("fix"))
                                         {
                                             transactionfeeam=(new BigDecimal(transcationfee));
                                         }else {
                                             transactionfeemain = (new BigDecimal(transcationfee));
                                             transactionfeeam = etDDKValue.multiply(transactionfeemain);
                                         }
                                     } else if(tabclick==3)
                                     {
                                         String transaction_fees_mode_eth = App.pref.getString(Constant.transaction_fees_mode_usdt, "");
                                         if(transaction_fees_mode_eth.equalsIgnoreCase("fix"))
                                         {
                                             transactionfeeam=(new BigDecimal(transcationfee));
                                         }else {
                                             transactionfeemain = (new BigDecimal(transcationfee));
                                             transactionfeeam = etDDKValue.multiply(transactionfeemain);
                                         }
                                     }else
                                     {
                                         transactionfeemain=(new BigDecimal(transcationfee))/*.divide(BigDecimal.valueOf(100))*/;
                                         transactionfeeam=etDDKValue.multiply(transactionfeemain);
                                     }
                                 }
                                BigDecimal totalamount=etDDKValue.subtract(transactionfeeam);
                                transactionfee.setVisibility(View.VISIBLE);
                                totalsendamount.setText(""+totalamount);
                                transactionfee.setText(""+transactionfeeam.toPlainString());
                            }
                        } else {
                            if(tabclick==5) {
                                transactionfee.setText("0.0");
                            }else
                            {
                                //BigDecimal transactionfeemain=(new BigDecimal(transcationfee)).divide(BigDecimal.valueOf(100));
                                transactionfee.setText("0.0");
                                transactionfee.setVisibility(View.VISIBLE);
                                totalsendamount.setText("0.0");
                            }
                        }

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab)
                {
                    latestDDKPrice();
                    progressBar.setVisibility(View.VISIBLE);
                    String countrydata=userData.getUser().country.get(0).country;
                    String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
                    if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
                    {
                        if (tab.getPosition() == 0)
                        {
                            getSettingServerData(getActivity(),"send_wallet_currency","php");
                        } else if (tab.getPosition() == 1)
                        {
                            getSettingServerData(getActivity(),"send_sam_koin","samkoin");
                        } else if (tab.getPosition() == 5)
                        {
                            getSettingServerData(getActivity(),"send_ddk","ddk");
                        } else if (tab.getPosition() == 2)
                        {
                            getSettingServerData(getActivity(),"send_btc","btc");
                        }/* else if (tab.getPosition() == 3)
                        {
                            getSettingServerData(getActivity(),"send_tron","tron");
                        }*/ else if (tab.getPosition() == 3)
                        {
                            getSettingServerData(getActivity(),"send_eth","eth");
                        } else if (tab.getPosition() == 4)
                        {
                            getSettingServerData(getActivity(),"send_usdt","usdt");
                        }
                    }else
                    {
                        if (tab.getPosition() == 0)
                        {
                            getSettingServerData(getActivity(),"send_sam_koin","samkoin");
                        } else if (tab.getPosition() == 4)
                        {
                            getSettingServerData(getActivity(),"send_ddk","ddk");
                        } else if (tab.getPosition() == 1)
                        {
                            getSettingServerData(getActivity(),"send_btc","btc");
                        }/*else if (tab.getPosition() == 2)
                        {
                            getSettingServerData(getActivity(),"send_tron","tron");
                        }*/ else if (tab.getPosition() == 2)
                        {
                            getSettingServerData(getActivity(),"send_eth","eth");
                        } else if (tab.getPosition() == 3)
                        {
                            getSettingServerData(getActivity(),"send_usdt","usdt");
                        }
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    Log.d("Selected", "");
                    String countrydata=userData.getUser().country.get(0).country;
                    String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
                    if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
                    {
                        if (tab.getPosition() == 5) {
                            if (credentialList.size() > 0) {
                                if (!dialog.isShowing()) {
                                    dialog.show();
                                } else {
                                    dialog.dismiss();
                                }
                            }
                        }

                    }else {
                        if (tab.getPosition() == 4) {
                            if (credentialList.size() > 0)
                            {
                                if (!dialog.isShowing())
                                {
                                    dialog.show();
                                } else
                                {
                                    dialog.dismiss();
                                }
                            }
                        }
                    }
                }
            });
            tvBuyPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().ddkBuyPrice));
            tvSellPrice.setText("₮ " + String.format("%.6f",  UserModel.getInstance().ddkSellPrice));
            progressBar.setVisibility(View.GONE);
            slide_custom_icon.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(SlideToActView slideToActView) {
                    String countrydata = userData.getUser().country.get(0).country;
                    String curkj = App.pref.getString(Constant.PHP_Functionality_View, "");
                    if (countrydata != null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true"))) {

                        if (tabclick == 1)
                        {
                            if (etDDKCoins.getText().toString().trim().isEmpty()) {
                                AppConfig.showToast("Enter PHP Amount");
                                slideToActView.resetSlider();
                                return;
                            }
                            String curentbalance = tvWalletBalance.getText().toString();
                            if (curentbalance == null || curentbalance.equalsIgnoreCase("")) {
                                curentbalance = "0.0";
                            }
                            String etddkcoinsvalue = etDDKCoins.getText().toString();
                            BigDecimal walletBalance = new BigDecimal(curentbalance);
                            BigDecimal etDDKValue = new BigDecimal(etddkcoinsvalue);
                            //......
                            if (walletBalance.compareTo(etDDKValue) == 1 || walletBalance.compareTo(etDDKValue) == 0) {
                                getSettingServerData(getActivity(), "php", "final");
                                slideToActView.resetSlider();
                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                                slideToActView.resetSlider();
                                return;
                            }

                        }else
                        if (tabclick == 2) {
                            if (etDDKCoins.getText().toString().trim().isEmpty()) {
                                AppConfig.showToast("Enter SAM Koin Amount");
                                slideToActView.resetSlider();
                                return;
                            }
                            String curentbalance = tvWalletBalance.getText().toString();
                            if (curentbalance == null || curentbalance.equalsIgnoreCase("")) {
                                curentbalance = "0.0";
                            }
                            String etddkcoinsvalue = etDDKCoins.getText().toString();
                            BigDecimal walletBalance = new BigDecimal(curentbalance);
                            BigDecimal etDDKValue = new BigDecimal(etddkcoinsvalue);
                            if (walletBalance.compareTo(etDDKValue) == 1 || walletBalance.compareTo(etDDKValue) == 0) {

                                String data= App.pref.getString(Constant.minimum_samsend_transaction_fee, "");
                                BigDecimal btccondition = new BigDecimal(data);
                                if (etDDKValue.compareTo(btccondition) == 1 || etDDKValue.compareTo(btccondition) == 0) {
                                    getSettingServerData(getActivity(), "send_sam_koin", "final");
                                    slideToActView.resetSlider();
                                } else {
                                    Toast.makeText(mContext, "In USDT Amount should be greater then or equal to "+data, Toast.LENGTH_SHORT).show();
                                    slideToActView.resetSlider();
                                    return;
                                }

                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                                slideToActView.resetSlider();
                                return;
                            }

                        } else if (tabclick == 6)
                        {
                            if (etDDKCoins.getText().toString().trim().isEmpty())
                            {
                                AppConfig.showToast("Enter DDK amount");
                                slideToActView.resetSlider();
                                return;
                            }
                            String curentbalance = tvWalletBalance.getText().toString();
                            if (curentbalance == null || curentbalance.equalsIgnoreCase(""))
                            {
                                curentbalance = "0.0";
                            }
                            BigDecimal walletBalance = new BigDecimal(curentbalance);
                            String enteramou = etDDKCoins.getText().toString();
                            BigDecimal etDDKValue = new BigDecimal(enteramou);
                            if (walletBalance.compareTo(etDDKValue) == 1 || walletBalance.compareTo(etDDKValue) == 0) {
                                getSettingServerData(getActivity(), "send_ddk", "final");
                                slideToActView.resetSlider();
                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                                slideToActView.resetSlider();
                                return;
                            }
                        } else if (tabclick == 3)
                        {
                            if (etDDKCoins.getText().toString().isEmpty()) {
                                AppConfig.showToast("Enter BTC amount");
                                slideToActView.resetSlider();
                                return;
                            }
                            String curentbalance = App.pref.getString(Constant.BTC_Balance, "");
                            if (curentbalance == null || curentbalance.equalsIgnoreCase("")) {
                                curentbalance = "0.0";
                            }
                            String etddkcoinsva = etDDKCoins.getText().toString();
                            BigDecimal walletBalance = new BigDecimal(curentbalance);
                            BigDecimal etDDKValue = new BigDecimal(etddkcoinsva);
                            if (walletBalance.compareTo(etDDKValue) == 1) {
                               // String data = "0.005";
                                String data= App.pref.getString(Constant.minimum_btcsend_transaction_fee, "");
                                BigDecimal btccondition = new BigDecimal(data);
                                if (etDDKValue.compareTo(btccondition) == 1 || etDDKValue.compareTo(btccondition) == 0) {
                                    getSettingServerData(getActivity(), "send_btc", "final");
                                    slideToActView.resetSlider();
                                } else {
                                    Toast.makeText(mContext, "In BTC Amount should be greater then or equal to 0.005", Toast.LENGTH_SHORT).show();
                                    slideToActView.resetSlider();
                                    return;
                                }
                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                                slideToActView.resetSlider();
                                return;
                            }
                        }/* else if (tabclick == 4)
                        {
                            if (etDDKCoins.getText().toString().isEmpty()) {
                                AppConfig.showToast("Enter TRON amount");
                                slideToActView.resetSlider();
                                return;
                            }
                            String curentbalance = App.pref.getString(Constant.BTC_Balance, "");
                            if (curentbalance == null || curentbalance.equalsIgnoreCase("")) {
                                curentbalance = "0.0";
                            }
                            String etddkcoinsva = etDDKCoins.getText().toString();
                            BigDecimal walletBalance = new BigDecimal(curentbalance);
                            BigDecimal etDDKValue = new BigDecimal(etddkcoinsva);
                            if (walletBalance.compareTo(etDDKValue) == 1) {
                                // String data = "0.005";
                                String data= App.pref.getString(Constant.minimum_btcsend_transaction_fee, "");
                                BigDecimal btccondition = new BigDecimal(data);
                                if (etDDKValue.compareTo(btccondition) == 1 || etDDKValue.compareTo(btccondition) == 0) {
                                    getSettingServerData(getActivity(), "send_btc", "final");
                                    slideToActView.resetSlider();
                                } else {
                                    Toast.makeText(mContext, "In TRON Amount should be greater then or equal to 0.005", Toast.LENGTH_SHORT).show();
                                    slideToActView.resetSlider();
                                    return;
                                }
                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                                slideToActView.resetSlider();
                                return;
                            }
                        }*/ else if (tabclick == 4) {
                            if (etDDKCoins.getText().toString().isEmpty()) {
                                AppConfig.showToast("Enter ETH amount");
                                slideToActView.resetSlider();
                                return;
                            }
                            String curentbalance = App.pref.getString(Constant.Eth_Balance, "");
                            if (curentbalance == null || curentbalance.equalsIgnoreCase("")) {
                                curentbalance = "0.0";
                            }
                            BigDecimal walletBalance = new BigDecimal(curentbalance);
                            String etddkcoinsva = etDDKCoins.getText().toString();
                            BigDecimal etDDKValue = new BigDecimal(etddkcoinsva);
                            int statustrasan = walletBalance.compareTo(etDDKValue);
                            if (walletBalance.compareTo(etDDKValue) == 1) {
                                //0.25 .minimum_samsend_transaction_fee
                                //String data = "0.25";
                                String data= App.pref.getString(Constant.minimum_ethsend_transaction_fee, "");
                                BigDecimal btccondition = new BigDecimal(data);
                                int comare = etDDKValue.compareTo(btccondition);
                                if (etDDKValue.compareTo(btccondition) == 1 || etDDKValue.compareTo(btccondition) == 0) {
                                    getSettingServerData(getActivity(), "send_eth", "final");
                                    slideToActView.resetSlider();
                                } else {
                                    Toast.makeText(mContext, "In ETH Amount should be greater then or equal to 0.25", Toast.LENGTH_SHORT).show();
                                    slideToActView.resetSlider();
                                    return;
                                }
                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                                slideToActView.resetSlider();
                                return;
                            }
                        } else if (tabclick == 5) {
                            if (etDDKCoins.getText().toString().isEmpty()) {
                                AppConfig.showToast("Enter USTD amount");
                                slideToActView.resetSlider();
                                return;
                            }
                            String curentbalance = App.pref.getString(Constant.USDT_Balance, "");
                            if (curentbalance == null || curentbalance.equalsIgnoreCase("")) {
                                curentbalance = "0.0";
                            }
                            String entervalue = etDDKCoins.getText().toString();
                            BigDecimal walletBalance = new BigDecimal(curentbalance);
                            BigDecimal etDDKValue = new BigDecimal(entervalue);
                            if (walletBalance.compareTo(etDDKValue) == 1) {
                               // String data = "25";
                                String data= App.pref.getString(Constant.minimum_usdtsend_transaction_fee, "");
                                BigDecimal btccondition = new BigDecimal(data);
                                if (etDDKValue.compareTo(btccondition) == 1 || etDDKValue.compareTo(btccondition) == 0) {
                                    getSettingServerData(getActivity(), "send_usdt", "final");
                                    slideToActView.resetSlider();
                                } else {
                                    Toast.makeText(mContext, "In USDT Amount should be greater then or equal to 25", Toast.LENGTH_SHORT).show();
                                    slideToActView.resetSlider();
                                    return;
                                }
                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                                slideToActView.resetSlider();
                                return;
                            }
                        }

                    } else {
                        if (tabclick == 1) {
                            if (etDDKCoins.getText().toString().trim().isEmpty()) {
                                AppConfig.showToast("Enter SAM Koin Amount");
                                slideToActView.resetSlider();
                                return;
                            }
                            String curentbalance = tvWalletBalance.getText().toString();
                            if (curentbalance == null || curentbalance.equalsIgnoreCase("")) {
                                curentbalance = "0.0";
                            }
                            String etddkcoinsvalue = etDDKCoins.getText().toString();
                            BigDecimal walletBalance = new BigDecimal(curentbalance);
                            BigDecimal etDDKValue = new BigDecimal(etddkcoinsvalue);
                            if (walletBalance.compareTo(etDDKValue) == 1 || walletBalance.compareTo(etDDKValue) == 0)
                            {
                                String data= App.pref.getString(Constant.minimum_samsend_transaction_fee, "");
                                BigDecimal btccondition = new BigDecimal(data);
                                if (etDDKValue.compareTo(btccondition) == 1 || etDDKValue.compareTo(btccondition) == 0) {
                                    getSettingServerData(getActivity(), "send_sam_koin", "final");
                                    slideToActView.resetSlider();
                                } else {
                                    Toast.makeText(mContext, "In USDT Amount should be greater then or equal to "+data, Toast.LENGTH_SHORT).show();
                                    slideToActView.resetSlider();
                                    return;
                                }
                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                                slideToActView.resetSlider();
                                return;
                            }

                        } else if (tabclick == 5) {
                            if (etDDKCoins.getText().toString().trim().isEmpty()) {
                                AppConfig.showToast("Enter DDK amount");
                                slideToActView.resetSlider();
                                return;
                            }
                            String curentbalance = tvWalletBalance.getText().toString();
                            if (curentbalance == null || curentbalance.equalsIgnoreCase("")) {
                                curentbalance = "0.0";
                            }
                            BigDecimal walletBalance = new BigDecimal(curentbalance);
                            String enteramou = etDDKCoins.getText().toString();
                            BigDecimal etDDKValue = new BigDecimal(enteramou);
                            if (walletBalance.compareTo(etDDKValue) == 1 || walletBalance.compareTo(etDDKValue) == 0) {
                                getSettingServerData(getActivity(), "send_ddk", "final");
                                slideToActView.resetSlider();
                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                                slideToActView.resetSlider();
                                return;
                            }
                        } else if (tabclick == 2) {
                            if (etDDKCoins.getText().toString().isEmpty()) {
                                AppConfig.showToast("Enter BTC amount");
                                slideToActView.resetSlider();
                                return;
                            }
                            String curentbalance = App.pref.getString(Constant.BTC_Balance, "");
                            if (curentbalance == null || curentbalance.equalsIgnoreCase("")) {
                                curentbalance = "0.0";
                            }
                            String etddkcoinsva = etDDKCoins.getText().toString();
                            BigDecimal walletBalance = new BigDecimal(curentbalance);
                            BigDecimal etDDKValue = new BigDecimal(etddkcoinsva);
                            if (walletBalance.compareTo(etDDKValue) == 1) {
                                //String data = "0.005";
                                String data= App.pref.getString(Constant.minimum_btcsend_transaction_fee, "");
                                BigDecimal btccondition = new BigDecimal(data);
                                if (etDDKValue.compareTo(btccondition) == 1 || etDDKValue.compareTo(btccondition) == 0) {
                                    getSettingServerData(getActivity(), "send_btc", "final");
                                    slideToActView.resetSlider();
                                } else {
                                    Toast.makeText(mContext, "In BTC Amount should be greater then or equal to "+data, Toast.LENGTH_SHORT).show();
                                    slideToActView.resetSlider();
                                    return;
                                }
                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                                slideToActView.resetSlider();
                                return;
                            }
                        }/* else if (tabclick == 3) {
                            if (etDDKCoins.getText().toString().isEmpty()) {
                                AppConfig.showToast("Enter TRON amount");
                                slideToActView.resetSlider();
                                return;
                            }
                            String curentbalance = App.pref.getString(Constant.BTC_Balance, "");
                            if (curentbalance == null || curentbalance.equalsIgnoreCase("")) {
                                curentbalance = "0.0";
                            }
                            String etddkcoinsva = etDDKCoins.getText().toString();
                            BigDecimal walletBalance = new BigDecimal(curentbalance);
                            BigDecimal etDDKValue = new BigDecimal(etddkcoinsva);
                            if (walletBalance.compareTo(etDDKValue) == 1) {
                                //String data = "0.005";
                                String data= App.pref.getString(Constant.minimum_btcsend_transaction_fee, "");
                                BigDecimal btccondition = new BigDecimal(data);
                                if (etDDKValue.compareTo(btccondition) == 1 || etDDKValue.compareTo(btccondition) == 0) {
                                    getSettingServerData(getActivity(), "send_btc", "final");
                                    slideToActView.resetSlider();
                                } else {
                                    Toast.makeText(mContext, "In TRON Amount should be greater then or equal to "+data, Toast.LENGTH_SHORT).show();
                                    slideToActView.resetSlider();
                                    return;
                                }
                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                                slideToActView.resetSlider();
                                return;
                            }
                        }*/ else if (tabclick == 3) {
                            if (etDDKCoins.getText().toString().isEmpty()) {
                                AppConfig.showToast("Enter ETH amount");
                                slideToActView.resetSlider();
                                return;
                            }
                            String curentbalance = App.pref.getString(Constant.Eth_Balance, "");
                            if (curentbalance == null || curentbalance.equalsIgnoreCase("")) {
                                curentbalance = "0.0";
                            }
                            BigDecimal walletBalance = new BigDecimal(curentbalance);
                            String etddkcoinsva = etDDKCoins.getText().toString();
                            BigDecimal etDDKValue = new BigDecimal(etddkcoinsva);
                            int statustrasan = walletBalance.compareTo(etDDKValue);
                            if (walletBalance.compareTo(etDDKValue) == 1) {
                                //0.25
                               // String data = "0.25";
                                String data= App.pref.getString(Constant.minimum_ethsend_transaction_fee, "");
                                BigDecimal btccondition = new BigDecimal(data);
                                int comare = etDDKValue.compareTo(btccondition);
                                if (etDDKValue.compareTo(btccondition) == 1 || etDDKValue.compareTo(btccondition) == 0) {
                                    getSettingServerData(getActivity(), "send_eth", "final");
                                    slideToActView.resetSlider();
                                } else {
                                    Toast.makeText(mContext, "In ETH Amount should be greater then or equal to "+data, Toast.LENGTH_SHORT).show();
                                    slideToActView.resetSlider();
                                    return;
                                }
                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                                slideToActView.resetSlider();
                                return;
                            }
                        } else if (tabclick == 4) {
                            if (etDDKCoins.getText().toString().isEmpty()) {
                                AppConfig.showToast("Enter USTD amount");
                                slideToActView.resetSlider();
                                return;
                            }
                            String curentbalance = App.pref.getString(Constant.USDT_Balance, "");
                            if (curentbalance == null || curentbalance.equalsIgnoreCase("")) {
                                curentbalance = "0.0";
                            }
                            String entervalue = etDDKCoins.getText().toString();
                            BigDecimal walletBalance = new BigDecimal(curentbalance);
                            BigDecimal etDDkPayment = new BigDecimal(entervalue);
                            BigDecimal etDDKValue = new BigDecimal(entervalue);
                            if (walletBalance.compareTo(etDDKValue) == 1) {
                                //String data = "25";
                                String data= App.pref.getString(Constant.minimum_usdtsend_transaction_fee, "");
                                BigDecimal btccondition = new BigDecimal(data);
                                if (etDDKValue.compareTo(btccondition) == 1 || etDDKValue.compareTo(btccondition) == 0) {
                                    getSettingServerData(getActivity(), "send_usdt", "final");
                                    slideToActView.resetSlider();
                                } else {
                                    Toast.makeText(mContext, "In USDT Amount should be greater then or equal to "+data, Toast.LENGTH_SHORT).show();
                                    slideToActView.resetSlider();
                                    return;
                                }
                            } else {
                                Toast.makeText(mContext, "insufficient wallet balance", Toast.LENGTH_SHORT).show();
                                slideToActView.resetSlider();
                                return;
                            }
                        }
                    }
                }
            });

            UserModel.getInstance().getCredentialsCall(mContext, new GetAllCredential() {
                @Override
                public void getCredential(List<Credential> credentials) {
                    credentialList = new ArrayList<>();
                    credentialList.addAll(credentials);
                }
            });

            //for condition
            String curkjnw= App.pref.getString(Constant.PHP_Functionality_View, "");
            if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkjnw.equalsIgnoreCase("true")))
            {
                if (HomeFragment.tabclickevent == 1)
                {
                    buysell_layout.setVisibility(View.GONE);
                    tvAddressCode.setVisibility(View.INVISIBLE);
                    clickAddressname = "Send PHP ";
                    TabLayout.Tab tab = tabLayout.getTabAt(0);
                    tab.select();
                    changeThePriceView("php");
                    String usdtvalue = App.pref.getString(Constant.PHP_Balance, "");
                    if (usdtvalue == null || usdtvalue.equalsIgnoreCase("")) {
                        App.editor.putString(Constant.PHP_Balance, "0");
                        App.editor.apply();
                    }
                    BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.PHP_Balance, ""));
                    BigDecimal roundhaldv = currentbalance.setScale(4, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    if (!ddkselectaddress.equalsIgnoreCase("")) {
                        etWalletAddress.setText(ddkselectaddress);
                    }

                } else if (HomeFragment.tabclickevent == 2)
                {
                    buysell_layout.setVisibility(View.VISIBLE);
                    tvAddressCode.setVisibility(View.VISIBLE);
                    clickAddressname = "Send SAM Koin ";
                    TabLayout.Tab tab = tabLayout.getTabAt(1);
                    tab.select();
                    changeThePriceView("samkoin");
                    tvAddressCode.setText(App.pref.getString(Constant.SAMKOIN_ADD, ""));
                    String usdtvalue = App.pref.getString(Constant.SAMKOIN_Balance, "");
                    if (usdtvalue == null || usdtvalue.equalsIgnoreCase("")) {
                        App.editor.putString(Constant.SAMKOIN_Balance, "0");
                        App.editor.apply();
                    }
                    BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.SAMKOIN_Balance, ""));
                    BigDecimal roundhaldv = currentbalance.setScale(4, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    if (!ddkselectaddress.equalsIgnoreCase("")) {
                        etWalletAddress.setText(ddkselectaddress);
                    }

                } else if (HomeFragment.tabclickevent == 6)
                {
                    buysell_layout.setVisibility(View.VISIBLE);
                    tvAddressCode.setVisibility(View.VISIBLE);
                    clickAddressname = "Send DDK ";
                    TabLayout.Tab tab = tabLayout.getTabAt(5);
                    tab.select();
                    changeThePriceView("ddk");
                    tvAddressCode.setText(App.pref.getString(Constant.Wallet_ADD, ""));
                    getCurrentBalance();
                    if (!ddkselectaddress.equalsIgnoreCase("")) {
                        etWalletAddress.setText(ddkselectaddress);
                    }

                } else if (HomeFragment.tabclickevent == 3)
                {
                    buysell_layout.setVisibility(View.VISIBLE);
                    tvAddressCode.setVisibility(View.VISIBLE);
                    clickAddressname = "Send BTC ";
                    TabLayout.Tab tab = tabLayout.getTabAt(2);
                    tab.select();
                    changeThePriceView("btc");
                    tvAddressCode.setText(App.pref.getString(Constant.BTC_ADD, ""));
                    String usdtvalue = App.pref.getString(Constant.BTC_Balance, "");
                    if (usdtvalue == null || usdtvalue.equalsIgnoreCase("")) {
                        App.editor.putString(Constant.BTC_Balance, "0");
                        App.editor.apply();
                    }

                    BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.BTC_Balance, ""));
                    BigDecimal roundhaldv = currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    if (!ddkselectaddress.equalsIgnoreCase("")) {
                        etWalletAddress.setText(ddkselectaddress);
                    }

                }/* else if (HomeFragment.tabclickevent == 4)
                {
                    buysell_layout.setVisibility(View.VISIBLE);
                    tvAddressCode.setVisibility(View.VISIBLE);
                    clickAddressname = "Send TRON ";
                    TabLayout.Tab tab = tabLayout.getTabAt(3);
                    tab.select();
                    changeThePriceView("tron");
                    tvAddressCode.setText(App.pref.getString(Constant.BTC_ADD, ""));
                    String usdtvalue = App.pref.getString(Constant.BTC_Balance, "");
                    if (usdtvalue == null || usdtvalue.equalsIgnoreCase("")) {
                        App.editor.putString(Constant.BTC_Balance, "0");
                        App.editor.apply();
                    }

                    BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.BTC_Balance, ""));
                    BigDecimal roundhaldv = currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    if (!ddkselectaddress.equalsIgnoreCase("")) {
                        etWalletAddress.setText(ddkselectaddress);
                    }

                }*/ else if (HomeFragment.tabclickevent == 4)
                {
                    buysell_layout.setVisibility(View.VISIBLE);
                    tvAddressCode.setVisibility(View.VISIBLE);
                    clickAddressname = "Send ETH ";
                    TabLayout.Tab tab = tabLayout.getTabAt(3);
                    tab.select();
                    changeThePriceView("eth");
                    tvAddressCode.setText(App.pref.getString(Constant.Eth_ADD, ""));
                    String usdtvalue = App.pref.getString(Constant.Eth_Balance, "");
                    if (usdtvalue == null || usdtvalue.equalsIgnoreCase("")) {
                        App.editor.putString(Constant.Eth_Balance, "0");
                        App.editor.apply();
                    }
                    BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.Eth_Balance, ""));
                    BigDecimal roundhaldv = currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    if (!ddkselectaddress.equalsIgnoreCase("")) {
                        etWalletAddress.setText(ddkselectaddress);
                    }

                } else if (HomeFragment.tabclickevent == 5)
                {
                    buysell_layout.setVisibility(View.VISIBLE);
                    tvAddressCode.setVisibility(View.VISIBLE);
                    clickAddressname = "Send USDT ";
                    TabLayout.Tab tab = tabLayout.getTabAt(4);
                    tab.select();
                    changeThePriceView("usdt");
                    tvAddressCode.setText(App.pref.getString(Constant.USDT_ADD, ""));
                    String usdtvalue = App.pref.getString(Constant.USDT_Balance, "");
                    if (usdtvalue == null || usdtvalue.equalsIgnoreCase("")) {
                        App.editor.putString(Constant.USDT_Balance, "0");
                        App.editor.apply();
                    }
                    BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.USDT_Balance, ""));
                    BigDecimal roundhaldv = currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    if (!ddkselectaddress.equalsIgnoreCase("")) {
                        etWalletAddress.setText(ddkselectaddress);
                    }

                }

            }else
            {
                if (HomeFragment.tabclickevent == 1)
                {
                    clickAddressname = "Send SAM Koin ";
                    TabLayout.Tab tab = tabLayout.getTabAt(0);
                    tab.select();
                    changeThePriceView("samkoin");
                    tvAddressCode.setText(App.pref.getString(Constant.SAMKOIN_ADD, ""));
                    String usdtvalue = App.pref.getString(Constant.SAMKOIN_Balance, "");
                    if (usdtvalue == null || usdtvalue.equalsIgnoreCase("")) {
                        App.editor.putString(Constant.SAMKOIN_Balance, "0");
                        App.editor.apply();
                    }
                    BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.SAMKOIN_Balance, ""));
                    BigDecimal roundhaldv = currentbalance.setScale(4, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    if (!ddkselectaddress.equalsIgnoreCase("")) {
                        etWalletAddress.setText(ddkselectaddress);
                    }

                } else if (HomeFragment.tabclickevent == 5)
                {
                    clickAddressname = "Send DDK ";
                    TabLayout.Tab tab = tabLayout.getTabAt(4);
                    tab.select();
                    changeThePriceView("ddk");
                    tvAddressCode.setText(App.pref.getString(Constant.Wallet_ADD, ""));
                    getCurrentBalance();
                    if (!ddkselectaddress.equalsIgnoreCase("")) {
                        etWalletAddress.setText(ddkselectaddress);
                    }

                } else if (HomeFragment.tabclickevent == 2)
                {
                    clickAddressname = "Send BTC ";
                    TabLayout.Tab tab = tabLayout.getTabAt(1);
                    tab.select();
                    changeThePriceView("btc");
                    tvAddressCode.setText(App.pref.getString(Constant.BTC_ADD, ""));
                    String usdtvalue = App.pref.getString(Constant.BTC_Balance, "");
                    if (usdtvalue == null || usdtvalue.equalsIgnoreCase("")) {
                        App.editor.putString(Constant.BTC_Balance, "0");
                        App.editor.apply();
                    }

                    BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.BTC_Balance, ""));
                    BigDecimal roundhaldv = currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    if (!ddkselectaddress.equalsIgnoreCase("")) {
                        etWalletAddress.setText(ddkselectaddress);
                    }

                }/*else if (HomeFragment.tabclickevent == 3)
                {
                    clickAddressname = "Send TRON ";
                    TabLayout.Tab tab = tabLayout.getTabAt(2);
                    tab.select();
                    changeThePriceView("tron");
                    tvAddressCode.setText(App.pref.getString(Constant.BTC_ADD, ""));
                    String usdtvalue = App.pref.getString(Constant.BTC_Balance, "");
                    if (usdtvalue == null || usdtvalue.equalsIgnoreCase("")) {
                        App.editor.putString(Constant.BTC_Balance, "0");
                        App.editor.apply();
                    }

                    BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.BTC_Balance, ""));
                    BigDecimal roundhaldv = currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    if (!ddkselectaddress.equalsIgnoreCase("")) {
                        etWalletAddress.setText(ddkselectaddress);
                    }

                }*/ else if (HomeFragment.tabclickevent == 3)
                {
                    clickAddressname = "Send ETH ";
                    TabLayout.Tab tab = tabLayout.getTabAt(2);
                    tab.select();
                    changeThePriceView("eth");
                    tvAddressCode.setText(App.pref.getString(Constant.Eth_ADD, ""));
                    String usdtvalue = App.pref.getString(Constant.Eth_Balance, "");
                    if (usdtvalue == null || usdtvalue.equalsIgnoreCase("")) {
                        App.editor.putString(Constant.Eth_Balance, "0");
                        App.editor.apply();
                    }
                    BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.Eth_Balance, ""));
                    BigDecimal roundhaldv = currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    if (!ddkselectaddress.equalsIgnoreCase("")) {
                        etWalletAddress.setText(ddkselectaddress);
                    }

                } else if (HomeFragment.tabclickevent == 4)
                {
                    clickAddressname = "Send USDT ";
                    TabLayout.Tab tab = tabLayout.getTabAt(3);
                    tab.select();
                    changeThePriceView("usdt");
                    tvAddressCode.setText(App.pref.getString(Constant.USDT_ADD, ""));
                    String usdtvalue = App.pref.getString(Constant.USDT_Balance, "");
                    if (usdtvalue == null || usdtvalue.equalsIgnoreCase(""))
                    {
                        App.editor.putString(Constant.USDT_Balance, "0");
                        App.editor.apply();
                    }
                    BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.USDT_Balance, ""));
                    BigDecimal roundhaldv = currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    if (!ddkselectaddress.equalsIgnoreCase(""))
                    {
                        etWalletAddress.setText(ddkselectaddress);
                    }

                }
            }
            //..........
            getCredentialsCall();
        }

        return rootView;
    }

    public void getSettingServerData(Activity activity, final String functionname,final String tabaction)
    {
        AppConfig.showLoading("Please wait..", activity);

        UserModel.getInstance().getSettignSatusView(activity,functionname,new GegtSettingStatusinterface()
        {
            @Override
            public void getResponse(Response<getSettingModel> response)
            {
                AppConfig.hideLoader();
                try
                {
                    if ((response.body().getStatus() == 1))
                    {
                        if(tabaction =="final")
                        {
                            sendOtp();
                        }else {
                            progressBar.setVisibility(View.GONE);
                            changeThePriceView(tabaction);
                        }
                    } else {
                        if(tabaction =="final")
                        {
                        }else
                        {
                            progressBar.setVisibility(View.GONE);
                            String countrydata=userData.getUser().country.get(0).country;
                            String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
                            if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
                            {
                                if(tabclick==1)
                                {
                                    TabLayout.Tab tab = tabLayout.getTabAt(0);
                                    tab.select();
                                }else if(tabclick==2)
                                {
                                    TabLayout.Tab tab = tabLayout.getTabAt(1);
                                    tab.select();
                                }else if(tabclick==3)
                                {
                                    TabLayout.Tab tab = tabLayout.getTabAt(2);
                                    tab.select();
                                }else if(tabclick==4)
                                {
                                    TabLayout.Tab tab = tabLayout.getTabAt(3);
                                    tab.select();
                                }else if(tabclick==5)
                                {
                                    TabLayout.Tab tab = tabLayout.getTabAt(4);
                                    tab.select();
                                }else if(tabclick==6)
                                {
                                    TabLayout.Tab tab = tabLayout.getTabAt(5);
                                    tab.select();
                                }/*else if(tabclick==7)
                                {
                                    TabLayout.Tab tab = tabLayout.getTabAt(6);
                                    tab.select();
                                }*/
                            }else
                            {
                                if(tabclick==1)
                                {
                                    TabLayout.Tab tab = tabLayout.getTabAt(0);
                                    tab.select();
                                }else if(tabclick==2)
                                {
                                    TabLayout.Tab tab = tabLayout.getTabAt(1);
                                    tab.select();
                                }else if(tabclick==3)
                                {
                                    TabLayout.Tab tab = tabLayout.getTabAt(2);
                                    tab.select();
                                }else if(tabclick==4)
                                {
                                    TabLayout.Tab tab = tabLayout.getTabAt(3);
                                    tab.select();
                                }else if(tabclick==5)
                                {
                                    TabLayout.Tab tab = tabLayout.getTabAt(4);
                                    tab.select();
                                }/*else if(tabclick==6)
                                {
                                    TabLayout.Tab tab = tabLayout.getTabAt(5);
                                    tab.select();
                                }*/
                            }
                        }
                        ShowFunctionalityAlert(getActivity(), response.body().getMsg());
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getCurrentBalance()
    {
        progressBar.setVisibility(View.VISIBLE);
        UserModel.getInstance().getWalletDetails(1,App.pref.getString(Constant.WALLET_ID, ""), mContext, new GetAvailableValue() {
            @Override
            public void getValues(String ddk, WalletResponse successResponse)
            {
                BigDecimal currentbalance=new BigDecimal(ddk);
                BigDecimal roundhaldv=currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                tvWalletBalance.setText(roundhaldv.toPlainString()+"");
                progressBar.setVisibility(View.GONE);

            }
        });
    }
    private void sendOtp() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("email", App.pref.getString(Constant.USER_EMAIL, ""));
        hm.put("name", App.pref.getString(Constant.USER_NAME, ""));

        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Sending otp....");

        AppConfig.getLoadInterface().postOtp(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body().status == 1) {
                    otp = response.body().data;
                    Log.d("otp value",otp);
                    sendOtpDialog();
                } else if (response.isSuccessful() && response.body() != null && response.body().status == 3) {
                    AppConfig.showToast(response.body().msg);
                    AppConfig.openSplashActivity(getActivity());
                } else if (response.isSuccessful() && response.body() != null && response.body().status == 0) {
                    AppConfig.showToast(response.body().msg);
                } else {
                    AppConfig.showToast("Server Error");
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                dialog.dismiss();
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

    private void getCredentialsCall() {
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
                public void onFailure(Call<ResponseBody> call, Throwable t)
                {
                    t.printStackTrace();
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    @Override
    public void onClick(View v) {
    }

    //for cashoutnew
    private void initTheCredentialView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View dialogView = layoutInflater.inflate(R.layout.popup_wallet_pooling, null);
        dialog = new BottomSheetDialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(dialogView);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        final EditText searchEt = dialogView.findViewById(R.id.search_ET);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapterCredential = new CredentialListAdapter(credentialList, mContext, searchEt, new CredentialListAdapter.SetOnItemClickListener() {
            @Override
            public void onItemClick(String wallet_code, String walletId1, String passPhrese) {
                tvAddressCode.setText(wallet_code);
                ddkSecret = passPhrese;
                getWalletBalance(walletId1);
                hideKeyBoard();
                dialog.dismiss();
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

    //for send otp
    private void sendOtpDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.otp_verify_dialog, null);
        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(mContext);
        alert.setView(customView);
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        final LinearLayout back_view=customView.findViewById(R.id.back_view);
        final TextView btnVerify = customView.findViewById(R.id.btnVerify);
        final EditText otp_edt1 = customView.findViewById(R.id.otp_edt11);
        final EditText otp_edt2 = customView.findViewById(R.id.otp_edt12);
        final EditText otp_edt3 = customView.findViewById(R.id.otp_edt13);
        final EditText otp_edt4 = customView.findViewById(R.id.otp_edt14);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_top = new StringBuffer();
                str_top.append(otp_edt1.getText().toString()).append(otp_edt2.getText().toString()).append(otp_edt3.getText().toString()).append(otp_edt4.getText().toString());
                if (str_top.length() > 0 && str_top.length() == 4) {
                    if (str_top.toString().equalsIgnoreCase(otp))
                    {
                           //for value
                        String countrydata=userData.getUser().country.get(0).country;
                        String curkj=App.pref.getString(Constant.PHP_Functionality_View, "");
                        if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
                        {
                            if(tabclick==6)
                            {
                                sendPhpPayment(dialog);
                            }else if(tabclick==1)
                            {
                                sendPHPCurrencyTab(dialog);
                            }else
                            {
                                //for sam koin
                                sendSAMKoinPayment(dialog);
                            }
                        }else
                        {
                            if(tabclick==5)
                            {
                                sendPhpPayment(dialog);
                            }else
                            {
                                //for sam koin
                                sendSAMKoinPayment(dialog);
                            }
                        }
                            //............
                    } else {
                        AppConfig.showToast("Otp is expired or incorrect");
                    }
                }
            }
        });
        otp_edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otp_edt1.length() == 0) {
                    otp_edt1.requestFocus();
                }
                if (otp_edt1.length() == 1) {
                    otp_edt1.clearFocus();
                    otp_edt2.requestFocus();
                    otp_edt2.setCursorVisible(true);
                }
            }
        });

        otp_edt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otp_edt2.length() == 0) {
                    otp_edt2.requestFocus();
                }
                if (otp_edt2.length() == 1) {
                    otp_edt2.clearFocus();
                    otp_edt3.requestFocus();
                    otp_edt3.setCursorVisible(true);
                }
            }
        });


        otp_edt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otp_edt3.length() == 0) {
                    otp_edt3.requestFocus();
                }
                if (otp_edt3.length() == 1) {
                    otp_edt3.clearFocus();
                    otp_edt4.requestFocus();
                    otp_edt4.setCursorVisible(true);
                }
            }
        });


        otp_edt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (otp_edt4.length() == 0) {
                    otp_edt4.requestFocus();
                }
                if (otp_edt4.length() == 1) {
                    otp_edt4.requestFocus();
                }
            }
        });
        otp_edt2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    otp_edt1.requestFocus();
                }
                return false;
            }
        });
        otp_edt3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    otp_edt2.requestFocus();
                }
                return false;
            }
        });
        otp_edt4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    otp_edt3.requestFocus();
                }
                return false;
            }
        });

        back_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void sendPHPCurrencyTab(final AlertDialog dialogotp)
    {
        //............
        HashMap<String, String> hm = new HashMap<>();
        String receiveraddress=etWalletAddress.getText().toString().trim();
        //............
        hm.put("amount", "" +etDDKCoins.getText().toString().trim());
        hm.put("receiver_email", receiveraddress);
        hm.put("conversion_fee", "" +transactionfee.getText().toString().trim());
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Confirm Payment....");
        Log.d("ddk api param",hm+" ");
        AppConfig.getLoadInterface().sendWalletAmountTransaction(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppConfig.hideLoading(dialog);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        Log.d("ddk api value",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(Constant.STATUS) == 1)
                        {
                            final JSONObject dataObject = object.getJSONObject("data");
                            transactionStatus(dataObject.getString("txt_id"),dialogotp);
                        } else
                        {
                            AppConfig.hideLoading(dialog);
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } else {
                        AppConfig.showToast("Server error");
                    }
                } catch (IOException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                } catch (JSONException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                AppConfig.hideLoading(dialog);
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

    private void sendSAMKoinPayment(final AlertDialog dialogotp) {
        String receiveraddress=etWalletAddress.getText().toString().trim();
        String senderaddress=tvAddressCode.getText().toString().trim();
        //............
        HashMap<String, String> hm = new HashMap<>();
        String transcationfee="";
        String countrydata=userData.getUser().country.get(0).country;
        String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
        if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
        {
            if (tabclick == 2) {
                hm.put("crypto_type", "sam_koin");
                transcationfee = App.pref.getString(Constant.sendsam_transaction_fees, "");
            } else if (tabclick == 3) {
                hm.put("crypto_type", "btc");
                transcationfee = App.pref.getString(Constant.sendbtc_transaction_fees, "");
            } /*else if (tabclick == 4) {
                hm.put("crypto_type", "tron");
                transcationfee = App.pref.getString(Constant.sendtron_transaction_fees, "");
            }*/ else if (tabclick == 4) {
                hm.put("crypto_type", "eth");
                transcationfee = App.pref.getString(Constant.sendeth_transaction_fees, "");
            } else if (tabclick == 5) {
                hm.put("crypto_type", "usdt");
                transcationfee = App.pref.getString(Constant.sendusdt_transaction_fees, "");
            }
        }else {
            if (tabclick == 1) {
                hm.put("crypto_type", "sam_koin");
                transcationfee = App.pref.getString(Constant.sendsam_transaction_fees, "");
            } else if (tabclick == 2) {
                hm.put("crypto_type", "btc");
                transcationfee = App.pref.getString(Constant.sendbtc_transaction_fees, "");
            }/*  else if (tabclick == 3) {
                hm.put("crypto_type", "tron");
                transcationfee = App.pref.getString(Constant.sendtron_transaction_fees, "");
            }*/  else if (tabclick == 3) {
                hm.put("crypto_type", "eth");
                transcationfee = App.pref.getString(Constant.sendeth_transaction_fees, "");
            } else if (tabclick == 4) {
                hm.put("crypto_type", "usdt");
                transcationfee = App.pref.getString(Constant.sendusdt_transaction_fees, "");
            }
        }
        //............
        hm.put("sender_address", senderaddress);
        hm.put("input_amount", "" +etDDKCoins.getText().toString().trim());
        hm.put("fee", "" +transactionfee.getText().toString().trim());
        hm.put("receiver_address", receiveraddress);
        String sameconver=UserModel.getInstance().samkoinvalueper+"";
        sameconver=ReplacecommaValue(sameconver+"");
        hm.put("conversion_rate", sameconver);
        hm.put("transaction_fees", transcationfee);
        hm.put("sending_amount", totalsendamount.getText().toString().trim());
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Confirm Payment....");
        String tokenvalue=AppConfig.getStringPreferences(getActivity(), Constant.JWTToken);
        Log.d("newtoken",tokenvalue);
        Log.d("ddk api param",hm+" ");
        AppConfig.getLoadInterface().createSAMKOINTransaction(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppConfig.hideLoading(dialog);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        Log.d("ddk api value",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(Constant.STATUS) == 1)
                        {
                            final JSONObject dataObject = object.getJSONObject("data");
                            transactionStatus(dataObject.getString("txt_id"),dialogotp);
                        } else
                        {
                            AppConfig.hideLoading(dialog);
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } else {
                        AppConfig.showToast("Server error");
                    }
                } catch (IOException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                } catch (JSONException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                AppConfig.hideLoading(dialog);
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

    private void sendPhpPayment(final AlertDialog dialogotp) {
        String receiveraddress=etWalletAddress.getText().toString().trim();
        String senderaddress=tvAddressCode.getText().toString().trim();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("sender_address", senderaddress);
        hm.put("amount", "" +etDDKCoins.getText().toString().trim());
        hm.put("receiver_address", receiveraddress);
        hm.put("secret", ddkSecret);
        hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
        hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Confirm Payment....");
        String tokenvalue=AppConfig.getStringPreferences(getActivity(), Constant.JWTToken);
        Log.d("newtoken",tokenvalue);
        Log.d("ddk api param",hm+" ");
        AppConfig.getLoadInterface().createDDKTransaction(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppConfig.hideLoading(dialog);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        Log.d("ddk api value",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(Constant.STATUS) == 1) {
                            try {
                                final JSONObject dataObject = object.getJSONObject("data");
                                transactionStatus(dataObject.getString("txt_id"),dialogotp);
                            } catch (JSONException e) {
                                AppConfig.showToast("Server error");
                                e.printStackTrace();
                            }
                        } else {
                            AppConfig.hideLoading(dialog);
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } else {
                        AppConfig.showToast("Server error");
                    }
                } catch (IOException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                } catch (JSONException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                AppConfig.hideLoading(dialog);
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

    private void sendETHPayment(final AlertDialog dialogotp) {
        String receiveraddress=etWalletAddress.getText().toString().trim();
        String senderaddress=tvAddressCode.getText().toString().trim();
        //for encrypt strat
        String newceratevalue = "";
        String secratevalue=App.pref.getString(Constant.Eth_Secret,"");
        //................
        HashMap<String, String> hm = new HashMap<>();
        hm.put("sender_wallet", senderaddress);
        hm.put("amount", "" +etDDKCoins.getText().toString().trim());
        hm.put("receiver_wallet", receiveraddress);
        hm.put("secret",secratevalue);
        hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
        hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Confirm Payment....");
        String tokenvalue=AppConfig.getStringPreferences(getActivity(), Constant.JWTToken);
        Log.d("newtoken",tokenvalue);
        Log.d("eth api param",hm+" ");
        AppConfig.getLoadInterface().createETHTransaction(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppConfig.hideLoading(dialog);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        Log.d("eth api response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(Constant.STATUS) == 1) {
                            try {
                                String msg=object.getString("msg");
                                transactionStatusOther(object.getString("txHash"),msg,dialogotp);
                            } catch (JSONException e) {
                                AppConfig.showToast("Server error");
                                e.printStackTrace();
                            }
                        } else {
                            AppConfig.hideLoading(dialog);
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } else {
                        AppConfig.showToast("Server error");
                    }
                } catch (IOException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                } catch (JSONException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppConfig.hideLoading(dialog);
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

    private void sendBTCPayment(final AlertDialog dialogotp) {
        String receiveraddress=etWalletAddress.getText().toString().trim();
        String senderaddress=tvAddressCode.getText().toString().trim();
        //for encrypt strat
        String secratevalue=App.pref.getString(Constant.BTC_Secaret,"");
        //................
        HashMap<String, String> hm = new HashMap<>();
        hm.put("sender_address", senderaddress);
        hm.put("amount", "" +etDDKCoins.getText().toString().trim());
        hm.put("receiver_address", receiveraddress);
        hm.put("secret",secratevalue);
        hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
        hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Confirm Payment....");
        String tokenvalue=AppConfig.getStringPreferences(getActivity(), Constant.JWTToken);
        Log.d("newtoken",tokenvalue);
        Log.d("btc api param",hm+" ");
        AppConfig.getLoadInterface().createBTCTransaction(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppConfig.hideLoading(dialog);
                try {
                    if (response.isSuccessful() && response.body() != null)
                    {
                        String responseData = response.body().string();
                        Log.d("btc api response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(Constant.STATUS) == 1) {
                            try {
                                String msg=object.getString("msg");
                                transactionStatusOther(object.getString("txHash"),msg,dialogotp);
                            } catch (JSONException e) {
                                AppConfig.showToast("Server error");
                                e.printStackTrace();
                            }
                        } else {
                            AppConfig.hideLoading(dialog);
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } else {
                        AppConfig.showToast("Server error");
                    }
                } catch (IOException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                } catch (JSONException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppConfig.hideLoading(dialog);
                errordurigApiCalling(getActivity(),t.getMessage());
            }
        });
    }

    private void sendUSDTPayment(final AlertDialog dialogotp) {
        String receiveraddress=etWalletAddress.getText().toString().trim();
        String senderaddress=tvAddressCode.getText().toString().trim();
        //for encrypt strat
        String secratevalue=App.pref.getString(Constant.USDT_Secaret,"");
        //................
        HashMap<String, String> hm = new HashMap<>();
        hm.put("sender_address", senderaddress);
        hm.put("amount", "" +etDDKCoins.getText().toString().trim());
        hm.put("receiver_address", receiveraddress);
        hm.put("secret",secratevalue);
        hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
        hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Confirm Payment....");
        String tokenvalue=AppConfig.getStringPreferences(getActivity(), Constant.JWTToken);
        Log.d("newtoken",tokenvalue);
        Log.d("usdt api param",hm+" ");
        AppConfig.getLoadInterface().createUSDTTransaction(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppConfig.hideLoading(dialog);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        Log.d("usdt api response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(Constant.STATUS) == 1) {
                            try {
                                String msg=object.getString("msg");
                                transactionStatusOther(object.getString("transaction_hash"),msg,dialogotp);
                            } catch (JSONException e) {
                                AppConfig.showToast("Server error");
                                e.printStackTrace();
                            }
                        } else {
                            AppConfig.hideLoading(dialog);
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } else {
                        AppConfig.showToast("Server error");
                    }
                } catch (IOException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                } catch (JSONException e) {
                    AppConfig.hideLoading(dialog);
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppConfig.hideLoading(dialog);
                errordurigApiCalling(getActivity(),t.getMessage());
          }
        });
    }

     private void transactionStatusOther(String transactionId,String msg,final AlertDialog dialogotp) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_transaction_status, null);
        btnGoHome = (TextView) customView.findViewById(R.id.btnGoHome);
        tvOrderStatus = customView.findViewById(R.id.tvOrderStatus);
        tvTransactionId = customView.findViewById(R.id.tvTransactionId);
        btnCopyTransactionId = customView.findViewById(R.id.btnCopyTransactionId);
        ivTransactionCreateCheck = customView.findViewById(R.id.ivTransactionCreateCheck);
        ivTransactionCreateUncheck = customView.findViewById(R.id.ivTransactionCreateUncheck);
        ivOrderStatusIconUnCheck = customView.findViewById(R.id.ivOrderStatusIconUnCheck);
        ivOrderStatusIconCheck = customView.findViewById(R.id.ivOrderStatusIconCheck);
        progress_bar = customView.findViewById(R.id.progress_bar);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(customView);

        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i =new Intent(getActivity(),MainActivity.class);
                getActivity().startActivity(i);
                dialogotp.dismiss();
                getActivity().finish();
            }
        });

        btnCopyTransactionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                clip = ClipData.newPlainText("Copy", tvTransactionId.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
                AppConfig.showToast("Copied");
            }
        });

        tvTransactionId.setText(transactionId);
        ivTransactionCreateCheck.setVisibility(View.VISIBLE);
        ivTransactionCreateUncheck.setVisibility(View.GONE);
        btnGoHome.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.GONE);
        AppConfig.showToast(msg);
        tvOrderStatus.setText("Pending");
        ivOrderStatusIconCheck.setVisibility(View.VISIBLE);
        ivOrderStatusIconUnCheck.setVisibility(View.GONE);

    }

    private void transactionStatus(String transactionId, final AlertDialog dialogotp) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_transaction_status, null);
        btnGoHome = (TextView) customView.findViewById(R.id.btnGoHome);
        tvOrderStatus = customView.findViewById(R.id.tvOrderStatus);
        tvTransactionId = customView.findViewById(R.id.tvTransactionId);
        btnCopyTransactionId = customView.findViewById(R.id.btnCopyTransactionId);
        ivTransactionCreateCheck = customView.findViewById(R.id.ivTransactionCreateCheck);
        ivTransactionCreateUncheck = customView.findViewById(R.id.ivTransactionCreateUncheck);
        ivOrderStatusIconUnCheck = customView.findViewById(R.id.ivOrderStatusIconUnCheck);
        ivOrderStatusIconCheck = customView.findViewById(R.id.ivOrderStatusIconCheck);
        progress_bar = customView.findViewById(R.id.progress_bar);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(customView);

        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i =new Intent(getActivity(),MainActivity.class);
                getActivity().startActivity(i);
                dialogotp.dismiss();
                getActivity().finish();
            }
        });

        btnCopyTransactionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                clip = ClipData.newPlainText("Copy", tvTransactionId.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
                AppConfig.showToast("Copied");
            }
        });

        tvTransactionId.setText(transactionId);
        ivTransactionCreateCheck.setVisibility(View.VISIBLE);
        ivTransactionCreateUncheck.setVisibility(View.GONE);
        progress_bar.setVisibility(View.GONE);
        tvOrderStatus.setText("Completed");
        ivOrderStatusIconCheck.setVisibility(View.VISIBLE);
        ivOrderStatusIconUnCheck.setVisibility(View.GONE);
        btnGoHome.setVisibility(View.VISIBLE);
        //checkTransactionStatus(transactionId);
    }

    private void checkTransactionStatus(final String txt_id) {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        if (count == 0) {
            AppConfig.showLoading(dialog, "Checking transaction status...");
        }
        HashMap<String, String> hm = new HashMap<>();
        hm.put("txn_id", txt_id);

        if(tabclick==1)
        {
            Log.d("ddk ",hm.toString());
            AppConfig.getLoadInterface().checkSendDDKTransaction(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    Log.d("ddk trans response",response.toString());
                    if (response.isSuccessful())
                    {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            tvTransactionId.setText(txt_id);
                            ivTransactionCreateCheck.setVisibility(View.VISIBLE);
                            ivTransactionCreateUncheck.setVisibility(View.GONE);
                            if (jsonObject.getInt("status") == 1) {
                                btnGoHome.setVisibility(View.VISIBLE);
                                progress_bar.setVisibility(View.GONE);
                                AppConfig.showToast(jsonObject.getString("msg"));
                                tvOrderStatus.setText("Confirmed");
                                ivOrderStatusIconCheck.setVisibility(View.VISIBLE);
                                ivOrderStatusIconUnCheck.setVisibility(View.GONE);
                            } else {
                                if (count == 22) {
                                    btnGoHome.setVisibility(View.VISIBLE);
                                    progress_bar.setVisibility(View.GONE);
                                    ivOrderStatusIconCheck.setVisibility(View.VISIBLE);
                                    ivOrderStatusIconUnCheck.setVisibility(View.GONE);
                                    tvOrderStatus.setText("Failed");
                                    ivOrderStatusIconCheck.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_failed_transaction));
                                } else {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            count++;
                                            checkTransactionStatus(txt_id);
                                        }
                                    }, 8000);
                                }
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                        AppConfig.showToast("Server error"+response.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });

        }
     }

    //...............

    private void getWalletBalance(String walletId) {
        UserModel.getInstance().getWalletDetails(0,walletId, mContext, new GetAvailableValue() {
            @Override
            public void getValues(String ddk, WalletResponse successResponse) {
                currentWalletBalance = ddk;
                currentWalletBalance=ReplacecommaValue(currentWalletBalance+"");
                BigDecimal currentbalance=new BigDecimal(currentWalletBalance);
                BigDecimal roundhaldv=currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                tvWalletBalance.setText(roundhaldv.toPlainString()+"");
            }
        });
    }

    private void filter(String newText) {
        if (credentialList.size() > 0) {
            List<Credential> doctorNew = new ArrayList<>();

            if (newText.isEmpty()) {
                doctorNew.addAll(credentialList);
            } else {
                for (Credential event : credentialList) {
                    if (event.getName().toLowerCase().contains(newText.toLowerCase()) ||
                            event.getDdkcode().toLowerCase().contains(newText.toLowerCase())) {
                        doctorNew.add(event);
                    }
                }

                if (doctorNew.size() == 0) {
                    AppConfig.showToast("No search data Found.");
                }
            }
        }
    }

    public static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void changeThePriceView(final String wallet_type)
    {
            if (wallet_type.equalsIgnoreCase("ddk")) {
                getCurrentBalance();
                transactionfee.setText("0.0");
                totalsendamount.setText("0.0");
                tvAddressCode.setVisibility(View.VISIBLE);
                buysell_layout.setVisibility(View.VISIBLE);
                tvAddressCode.setText(App.pref.getString(Constant.Wallet_ADD, ""));
                tvBuyPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().ddkBuyPrice));
                tvSellPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().ddkSellPrice));
                String currentvalue=App.pref.getString(Constant.CurrentBalance, "");
                if(currentvalue==null || currentvalue.equalsIgnoreCase(null) || currentvalue.equalsIgnoreCase("null"))
                {
                    tvWalletBalance.setText("0.000000");
                }else
                {
                    if(App.pref.getString(Constant.CurrentBalance, "").length()!=0)
                    {
                        BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.CurrentBalance, ""));
                        BigDecimal roundhaldv = currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                        tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    }
                }
                etWalletAddress.setHint("Wallet Address");
                hintform.setText("Send DDK ");
                etDDKCoins.setHint("Enter DDK ");
                etDDKCoins.setText("");
                String countrydata = userData.getUser().country.get(0).country;
                String curkj = App.pref.getString(Constant.PHP_Functionality_View, "");
                if (countrydata != null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
                {
                    tabclick = 6;
                    if (HomeFragment.tabclickevent != 6) {
                        etWalletAddress.setText("");
                    }
                }else
                {
                    tabclick = 5;
                    if (HomeFragment.tabclickevent != 5) {
                        etWalletAddress.setText("");
                    }
                }
                tvAddressCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppConfig.copyPass(tvAddressCode.getText().toString().trim(), "Copy Address", getActivity());
                    }
                });
            } else {
                tvAddressCode.setVisibility(View.VISIBLE);
                if (wallet_type.equalsIgnoreCase("php"))
                {
                    tvAddressCode.setVisibility(View.INVISIBLE);
                    BigDecimal currentbalance=new BigDecimal(App.pref.getString(Constant.PHP_Balance, ""));
                    BigDecimal roundhaldv=currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString()+"");
                    buysell_layout.setVisibility(View.GONE);
                    hintform.setText("Send PHP");
                    tabclick = 1;
                    etWalletAddress.setHint("Enter Email Address");
                    etDDKCoins.setHint("Enter Amount ");
                    etDDKCoins.setText("");
                    if (HomeFragment.tabclickevent != 1) {
                        etWalletAddress.setText("");
                    }
                    String transcationfee=App.pref.getString(Constant.sendsam_transaction_fees, "");
                    //BigDecimal transactionfeemain=(new BigDecimal(transcationfee)).divide(BigDecimal.valueOf(100));
                    transactionfee.setText("0.0");
                    totalsendamount.setText("0.0");
                    transactionfee.setVisibility(View.VISIBLE);

                }else if (wallet_type.equalsIgnoreCase("samkoin"))
                {
                    tvAddressCode.setVisibility(View.VISIBLE);
                    buysell_layout.setVisibility(View.VISIBLE);
                    tvAddressCode.setText(App.pref.getString(Constant.SAMKOIN_ADD, ""));
                    BigDecimal currentbalance=new BigDecimal(App.pref.getString(Constant.SAMKOIN_Balance, ""));
                    BigDecimal roundhaldv=currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString()+"");
                    tvBuyPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().samkoinBuyPrice));
                    tvSellPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().samkoinSellPrice));
                    hintform.setText("Send SAM Koin ");
                    String countrydata = userData.getUser().country.get(0).country;
                    String curkj = App.pref.getString(Constant.PHP_Functionality_View, "");
                    if (countrydata != null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
                    {
                        tabclick = 2;
                        if (HomeFragment.tabclickevent != 2) {
                            etWalletAddress.setText("");
                        }
                    }else
                    {
                        tabclick = 1;
                        if (HomeFragment.tabclickevent != 1) {
                            etWalletAddress.setText("");
                        }
                    }
                    etWalletAddress.setHint("Wallet Address");
                    etDDKCoins.setHint("Enter SAM Koin ");
                    etDDKCoins.setText("");
                    String transcationfee=App.pref.getString(Constant.sendsam_transaction_fees, "");
                    //BigDecimal transactionfeemain=(new BigDecimal(transcationfee)).divide(BigDecimal.valueOf(100));
                     transactionfee.setText("0.0");
                    totalsendamount.setText("0.0");
                    transactionfee.setVisibility(View.VISIBLE);

                }else
                if (wallet_type.equalsIgnoreCase("btc")) {

                    String countrydata = userData.getUser().country.get(0).country;
                    String curkj = App.pref.getString(Constant.PHP_Functionality_View, "");
                    if (countrydata != null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
                    {
                        tabclick = 3;
                        if (HomeFragment.tabclickevent != 3) {
                            etWalletAddress.setText("");
                        }
                    }else
                    {
                        tabclick = 2;
                        if (HomeFragment.tabclickevent != 2) {
                            etWalletAddress.setText("");
                        }
                    }
                    tvAddressCode.setVisibility(View.VISIBLE);
                    buysell_layout.setVisibility(View.VISIBLE);
                    tvBuyPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().btcBuyPrice));
                    tvSellPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().btcSellPrice));
                    tvAddressCode.setText(App.pref.getString(Constant.BTC_ADD, ""));
                    BigDecimal currentbalance=new BigDecimal(App.pref.getString(Constant.BTC_Balance, ""));
                    BigDecimal roundhaldv=currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString()+"");
                    hintform.setText("Send BTC");
                    etWalletAddress.setHint("Wallet Address");
                    etDDKCoins.setHint("Enter BTC ");
                    etDDKCoins.setText("");
                    transactionfee.setText("0.0");
                    totalsendamount.setText("0.0");
                    transactionfee.setVisibility(View.VISIBLE);

                } /*else
                if (wallet_type.equalsIgnoreCase("tron")) {

                    String countrydata = userData.getUser().country.get(0).country;
                    String curkj = App.pref.getString(Constant.PHP_Functionality_View, "");
                    if (countrydata != null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
                    {
                        tabclick = 4;
                        if (HomeFragment.tabclickevent != 4) {
                            etWalletAddress.setText("");
                        }
                    }else
                    {
                        tabclick = 3;
                        if (HomeFragment.tabclickevent != 3) {
                            etWalletAddress.setText("");
                        }
                    }
                    tvAddressCode.setVisibility(View.VISIBLE);
                    buysell_layout.setVisibility(View.VISIBLE);
                    tvBuyPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().tronBuyPrice));
                    tvSellPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().tronSellPrice));
                    tvAddressCode.setText(App.pref.getString(Constant.BTC_ADD, ""));
                    BigDecimal currentbalance=new BigDecimal(App.pref.getString(Constant.tron_Balance, ""));
                    BigDecimal roundhaldv=currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString()+"");
                    hintform.setText("Send TRON");
                    etWalletAddress.setHint("Wallet Address");
                    etDDKCoins.setHint("Enter TRON ");
                    etDDKCoins.setText("");
                    transactionfee.setText("0.0");
                    totalsendamount.setText("0.0");
                    transactionfee.setVisibility(View.VISIBLE);

                } */else if (wallet_type.equalsIgnoreCase("eth")) {

                    String countrydata = userData.getUser().country.get(0).country;
                    String curkj = App.pref.getString(Constant.PHP_Functionality_View, "");
                    if (countrydata != null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
                    {
                        tabclick = 4;
                        if (HomeFragment.tabclickevent != 4) {
                            etWalletAddress.setText("");
                        }
                    }else
                    {
                        tabclick = 3;
                        if (HomeFragment.tabclickevent != 3) {
                            etWalletAddress.setText("");
                        }
                    }
                    tvAddressCode.setVisibility(View.VISIBLE);
                    buysell_layout.setVisibility(View.VISIBLE);
                    tvAddressCode.setText(App.pref.getString(Constant.Eth_ADD, ""));
                    BigDecimal currentbalance=new BigDecimal(App.pref.getString(Constant.Eth_Balance, ""));
                    BigDecimal roundhaldv=currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString()+"");
                    tvBuyPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().ethBuyPrice));
                    tvSellPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().ethSellPrice));
                    hintform.setText("Send ETH ");
                    etDDKCoins.setHint("Enter ETH ");
                    etWalletAddress.setHint("Wallet Address");
                    etDDKCoins.setText("");
                    transactionfee.setText("0.0");
                    totalsendamount.setText("0.0");
                    transactionfee.setVisibility(View.VISIBLE);

                } else if (wallet_type.equalsIgnoreCase("usdt"))
                {
                    tvAddressCode.setVisibility(View.VISIBLE);
                    buysell_layout.setVisibility(View.VISIBLE);
                    tvAddressCode.setText(App.pref.getString(Constant.USDT_ADD, ""));
                    BigDecimal currentbalance=new BigDecimal(App.pref.getString(Constant.USDT_Balance, ""));
                    BigDecimal roundhaldv=currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString()+"");
                    tvBuyPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().usdtBuyPrice));
                    tvSellPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().usdtSellPrice));
                    hintform.setText("Send USDT ");
                    String countrydata = userData.getUser().country.get(0).country;
                    String curkj = App.pref.getString(Constant.PHP_Functionality_View, "");
                    if (countrydata != null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
                    {
                        tabclick = 5;
                        if (HomeFragment.tabclickevent != 5) {
                            etWalletAddress.setText("");
                        }
                    }else
                    {
                        tabclick = 4;
                        if (HomeFragment.tabclickevent != 4) {
                            etWalletAddress.setText("");
                        }
                    }

                    etWalletAddress.setHint("Wallet Address");
                    etDDKCoins.setHint("Enter USDT ");
                    etDDKCoins.setText("");
                    transactionfee.setText("0.0");
                    totalsendamount.setText("0.0");
                    transactionfee.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }
            tvAddressCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_copy_small, 0);
            tvAddressCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppConfig.copyPass(tvAddressCode.getText().toString().trim(), "Copy Address", getActivity());
                }
            });
    }
    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle(clickAddressname);
        MainActivity.enableBackViews(true);
    }
}
