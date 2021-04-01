package com.ddkcommunity.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.LoadInterface;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.CreateWalletActivity;
import com.ddkcommunity.activities.CustomPinActivity;
import com.ddkcommunity.activities.LoginActivity;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.activities.MapsActivity;
import com.ddkcommunity.activities.ReceivedActivity;
import com.ddkcommunity.activities.ReferralChainPayoutActivity;
import com.ddkcommunity.adapters.AnnouncementAdapter;
import com.ddkcommunity.adapters.HomeBannerPagerAdapter;
import com.ddkcommunity.adapters.OurTeamAdapter;
import com.ddkcommunity.adapters.WalletPopupAdapter;
import com.ddkcommunity.fragment.buy.BuyFragment;
import com.ddkcommunity.fragment.credential.AddCredentialsFragment;
import com.ddkcommunity.fragment.credential.CredentialsFragment;
import com.ddkcommunity.fragment.history.HistoryFragment;
import com.ddkcommunity.fragment.projects.SelectPaymentPoolingFragment;
import com.ddkcommunity.fragment.projects.TermsAndConsitionSubscription;
import com.ddkcommunity.fragment.send.SendDDkFragment;
import com.ddkcommunity.fragment.send.SendFragment;
import com.ddkcommunity.fragment.send.SendLinkFragment;
import com.ddkcommunity.interfaces.GegtSettingStatusinterface;
import com.ddkcommunity.interfaces.GetAvailableValue;
import com.ddkcommunity.interfaces.GetBTCUSDTETHPriceCallback;
import com.ddkcommunity.interfaces.GetCryptoSubscriptionResponse;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.model.Announcement;
import com.ddkcommunity.model.EthModelBalance;
import com.ddkcommunity.model.MapTransactionReceiverModel;
import com.ddkcommunity.model.OurTeamData;
import com.ddkcommunity.model.RedeemOptionModel;
import com.ddkcommunity.model.SliderImg;
import com.ddkcommunity.model.SliderImgResponse;
import com.ddkcommunity.model.TransactionFeeData;
import com.ddkcommunity.model.TransactionFeesResponse;
import com.ddkcommunity.model.buyCryptoModel;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.credential.CredentialsResponse;
import com.ddkcommunity.model.getSettingModel;
import com.ddkcommunity.model.samModel;
import com.ddkcommunity.model.user.User;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.model.userInviteModel;
import com.ddkcommunity.model.wallet.WalletResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.CommonMethodFunction;
import com.ddkcommunity.utilies.dataPutMethods;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ReplacecommaValue;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.ShowCahsoutDialog;
import static com.ddkcommunity.utilies.dataPutMethods.ShowFunctionalityAlert;
import static com.ddkcommunity.utilies.dataPutMethods.ShowServerPost;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;
import static com.ddkcommunity.utilies.dataPutMethods.getSettingServerDataSt;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    public static List<Credential> _credentialList = new ArrayList<>();
    public static PopupWindow pw;
    //    public static List<Delegate> eventList1 = new ArrayList<>();
    private WalletPopupAdapter adapter;
    private View view = null;
    private static final int NUM_PAGES = 5;
    private ViewPager mViewPager, mViewPagerAll;
    private static Context mContext;
    private ArrayList<SliderImg> drawablesList = new ArrayList<>();
    private boolean isCallWallet = true;
    private UserResponse userData;
    private View dialogueView;
    Bitmap bitmap;
    ProgressDialog pd;
    public static LinearLayout btnReceive_1,lytMain, lytAll;
    private static TextView sam_wallet,tvAddressCode, tvWalletBalance;
    private static Activity activity;
    private boolean walletTypeBTC = false;
    private ProgressBar progressBar;
    private TabLayout tabLayout;
    private TextView tvSellPrice, tvBuyPrice;
    LinearLayout buysell_layout;
    private HomeBannerPagerAdapter homeBannerPager;
    private AnnouncementAdapter announcementAdapter;
    private String wallet_type = "";
    public static int tabclickevent=0;
    public LinearLayout btnMAp,btnSAMPD_2,btnsampd,btnexchange,btnaccoutnverification,btnremittance,btnallsam,btndelivery,sam_view_layout,ridelayout,otherwalletlayout,newsamlayout,redeem_layout,history_layout,paybills_layout;
    TextView tvSelectDdkAddress,facebook_invite,share_app;
    public static ArrayList listdata;
    ImageView userimg;
    //for loging
    ShareDialog shareDialog;
    CallbackManager callbackManager;
    SharePhotoContent content;
    ArrayList<String> samkoinlist;
    ArrayList<RedeemOptionModel.Datum> Redeemoptionlist;
    public static String userselctionopt="";
    public static int profileupdatesta=0;

    public HomeFragment() {
    }

    private int count = 0;

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

    private int[] tabIcons = {
            R.drawable.sam_new_l,
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
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        String countrydata=userData.getUser().country.get(0).country;
        String curkj=App.pref.getString(Constant.PHP_Functionality_View, "");
        if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
        {
            tabLayout.getTabAt(1).setIcon(tabIcons[6]);
            tabLayout.getTabAt(2).setIcon(tabIcons[1]);
            tabLayout.getTabAt(3).setIcon(tabIcons[2]);
          //  tabLayout.getTabAt(4).setIcon(tabIcons[7]);
            tabLayout.getTabAt(4).setIcon(tabIcons[3]);
            tabLayout.getTabAt(5).setIcon(tabIcons[4]);
            tabLayout.getTabAt(6).setIcon(tabIcons[5]);
        }else
        {
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            //tabLayout.getTabAt(3).setIcon(tabIcons[7]);
            tabLayout.getTabAt(3).setIcon(tabIcons[3]);
            tabLayout.getTabAt(4).setIcon(tabIcons[4]);
            tabLayout.getTabAt(5).setIcon(tabIcons[5]);
        }
    }

    private static String ENCRYPTION_KEY = "1234512345123456";

    public void setTablayoutview()
    {
        tabLayout.removeAllTabs();
        userData = AppConfig.getUserData(mContext);
        tabLayout.addTab(tabLayout.newTab().setText("POINTS"));
        //............
        String countrydata=userData.getUser().country.get(0).country;
        String curkj=App.pref.getString(Constant.PHP_Functionality_View, "");
        if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
        {
                tabLayout.addTab(tabLayout.newTab().setText("PHP"));
        }
        //...........
        tabLayout.addTab(tabLayout.newTab().setText("KOIN"));
        tabLayout.addTab(tabLayout.newTab().setText("BTC"));
       // tabLayout.addTab(tabLayout.newTab().setText("TRON"));
        tabLayout.addTab(tabLayout.newTab().setText("ETH"));
        tabLayout.addTab(tabLayout.newTab().setText("USDT"));
        tabLayout.addTab(tabLayout.newTab().setText("DDK \u25BC"));
        setupTabIcons();
    }

   /* private void latestDDKPrice()
    {
        UserModel.getInstance().getUSDCall(new GetUSDAndBTCCallback() {
            @Override
            public void getValues(BigDecimal btc, BigDecimal usd) {
                if (usd != null) {
                    BigDecimal ONE_HUNDRED = new BigDecimal(100);
                    BigDecimal buy = usd.multiply(UserModel.getInstance().ddkBuyPercentage).divide(ONE_HUNDRED);
                    BigDecimal sell = usd.multiply(UserModel.getInstance().ddkSellPercentage).divide(ONE_HUNDRED);

                }
            }
        },getActivity());
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            mContext = getActivity();
            activity = getActivity();
            FacebookSdk.sdkInitialize(getContext());
            userData = AppConfig.getUserData(mContext);
            getSettingServerDataSt(getActivity(),"php");
            //.........
            callbackManager = CallbackManager.Factory.create();
            tabLayout = view.findViewById(R.id.tabs);
            setTablayoutview();
            //To hide the first tab
            samkoinlist=new ArrayList<>();
            samkoinlist.add("ERC20");
            samkoinlist.add("TRC20");
            tvSelectDdkAddress=view.findViewById(R.id.tvSelectDdkAddress);
            userimg=view.findViewById(R.id.userimg);
            share_app=view.findViewById(R.id.share_app);
            btnaccoutnverification=view.findViewById(R.id.btnaccoutnverification);
            btnremittance=view.findViewById(R.id.btnremittance);
            btnallsam=view.findViewById(R.id.btnallsam);
            facebook_invite=view.findViewById(R.id.facebook_invite);
            btndelivery=view.findViewById(R.id.btndelivery);
            sam_view_layout=view.findViewById(R.id.sam_view_layout);
            ridelayout=view.findViewById(R.id.ridelayout);
            otherwalletlayout=view.findViewById(R.id.otherwalletlayout);
            newsamlayout=view.findViewById(R.id.newsamlayout);
            lytAll = view.findViewById(R.id.lytAll);
            lytMain = view.findViewById(R.id.lytMain);
            //redeem_layout=view.findViewById(R.id.redeem_layout);
            history_layout=view.findViewById(R.id.history_layout);
            paybills_layout=view.findViewById(R.id.paybills_layout);
            tvWalletBalance = view.findViewById(R.id.tvWalletBalance);
            tvAddressCode = view.findViewById(R.id.tvAddressCode);
            tvBuyPrice = view.findViewById(R.id.tvBuyPrice);
            buysell_layout=view.findViewById(R.id.buysell_layout);
            tvSellPrice = view.findViewById(R.id.tvSellPrice);
            btnReceive_1=view.findViewById(R.id.btnReceive_1);
            sam_wallet=view.findViewById(R.id.sam_wallet);
            tvAddressCode.setOnClickListener(this);
            view.findViewById(R.id.btnSAMPD_2).setOnClickListener(this);
            view.findViewById(R.id.btnsampd).setOnClickListener(this);
            view.findViewById(R.id.btnexchange).setOnClickListener(this);
            view.findViewById(R.id.history_layout).setOnClickListener(this);
            view.findViewById(R.id.paybills_layout).setOnClickListener(this);
            view.findViewById(R.id.redeem_layout).setOnClickListener(this);
            view.findViewById(R.id.btnMAp).setOnClickListener(this);
            view.findViewById(R.id.btnSend_1).setOnClickListener(this);
            view.findViewById(R.id.btnCreateWallet_1).setOnClickListener(this);
            view.findViewById(R.id.btnHistory_1).setOnClickListener(this);
            view.findViewById(R.id.btnProject_1).setOnClickListener(this);
            view.findViewById(R.id.btnImport_1).setOnClickListener(this);
            view.findViewById(R.id.btnBuy_1).setOnClickListener(this);
            view.findViewById(R.id.btnCashOut_1).setOnClickListener(this);
            view.findViewById(R.id.btnProject_2).setOnClickListener(this);
            view.findViewById(R.id.btnBuy_2).setOnClickListener(this);
            view.findViewById(R.id.btnHistory_2).setOnClickListener(this);
            view.findViewById(R.id.btnImport_2).setOnClickListener(this);
            view.findViewById(R.id.btntransaparet).setOnClickListener(this);
            view.findViewById(R.id.btnCredential).setOnClickListener(this);
            view.findViewById(R.id.btnTutorials).setOnClickListener(this);
            view.findViewById(R.id.btnOurTeam).setOnClickListener(this);
            view.findViewById(R.id.cancellation).setOnClickListener(this);
            view.findViewById(R.id.btnReferralList).setOnClickListener(this);
            view.findViewById(R.id.btnReferralList_2).setOnClickListener(this);
            view.findViewById(R.id.btnProfile).setOnClickListener(this);
            view.findViewById(R.id.btnCashOut_2).setOnClickListener(this);
            view.findViewById(R.id.btnIncome).setOnClickListener(this);
            view.findViewById(R.id.btnremittance).setOnClickListener(this);
            view.findViewById(R.id.btnallsam).setOnClickListener(this);
            view.findViewById(R.id.btndelivery).setOnClickListener(this);
            view.findViewById(R.id.sam_view_layout).setOnClickListener(this);
            view.findViewById(R.id.ridelayout).setOnClickListener(this);
            view.findViewById(R.id.btnaccoutnverification).setOnClickListener(this);
            view.findViewById(R.id.btnaccoutnverification_2).setOnClickListener(this);
            progressBar = view.findViewById(R.id.progressBar);
            mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
            mViewPagerAll = (ViewPager) view.findViewById(R.id.viewPagerAll);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab)
                {
                    //........
                    String countrydata=userData.getUser().country.get(0).country;
                    if(countrydata.equalsIgnoreCase("philippines"))
                    {
                        progressBar.setVisibility(View.GONE);
                        getSettingServerOnTab("loader",tab,getActivity(), "php");
                    }else
                    {
                        progressBar.setVisibility(View.GONE);
                        setTabView(tab);
                    }
                    //............

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab)
                {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab)
                {
                    Log.d("Selected", "");
                    String curkj=App.pref.getString(Constant.PHP_Functionality_View, "");
                    String countrydata=userData.getUser().country.get(0).country;
                    if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
                    {
                        if (tab.getPosition() == 6) {
                            tabclickevent = 6;
                            if (_credentialList.size() > 0) {
                                if (!pw.isShowing()) {
                                    pw.showAtLocation(tab.view, Gravity.TOP | Gravity.LEFT, locateView(tab.view).left, locateView(tab.view).bottom);
                                } else {
                                    pw.dismiss();
                                }
                            }
                        }

                    }else
                    {
                        if (tab.getPosition() == 5)
                        {
                            tabclickevent = 5;
                            if (_credentialList.size() > 0)
                            {
                                if (!pw.isShowing())
                                {
                                    pw.showAtLocation(tab.view, Gravity.TOP | Gravity.LEFT, locateView(tab.view).left, locateView(tab.view).bottom);
                                } else {
                                    pw.dismiss();
                                }
                            }
                        }
                    }
                }
            });

        getEthBtcUsdtSellBuyy("");
        getSamToken("not");
        setCredentialPopup(inflater);
        openAnnouncementPopup();
        getEthBtcUsdtSellBuyy("");
        progressBar.setVisibility(View.GONE);
        getVerificationStatus();
        getDATAStatusKey();
        //getCurrentBalance(1);
        getRedeemOptions();
        getBannerKImages();
        getReceiverAddress();
        getMapreceiverAddress();
        getInviteFriend();
        getCryptoCurrencyList();
        }
        getFacebookShare();
        btnReceive_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.button_click));
                clickReceiverButton();
            }
        });
        //for front
        final String imgmainurl=AppConfig.getStringPreferences(mContext, Constant.FACEBOOKURL);
        if(imgmainurl!=null && !imgmainurl.isEmpty())
        Picasso.with(mContext).load(imgmainurl).into(userimg);
        //.............
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, callback);
        //..........
        facebook_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                    getFacebookShare();
                    imageShare();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                //ShareWithFacebook();
            }
        });
        // this part is optional
        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messagecontetn =App.pref.getString(Constant.INVITE_CONTENT, "");
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, messagecontetn);
                startActivity(Intent.createChooser(share, "Invite your friends"));
            }
        });

        tvSelectDdkAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataPutMethods.showDialogForSAMKoin(getActivity(),tabLayout,view,getContext(),samkoinlist,tvSelectDdkAddress);
            }
        });
        return view;
    }

    public void setTabView(TabLayout.Tab tab)
    {
        String countrydata=userData.getUser().country.get(0).country;
        String curkj=App.pref.getString(Constant.PHP_Functionality_View, "");
        if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
        {
            if (tab.getPosition() == 0)
            {
                otherwalletlayout.setVisibility(View.GONE);
                newsamlayout.setVisibility(View.VISIBLE);
                getSamToken("tab");
                changeThePriceView("sam");
                wallet_type = "sam";
                tabclickevent=0;
            }else
            if (tab.getPosition() == 1)
            {
                tvAddressCode.setVisibility(View.GONE);
                buysell_layout.setVisibility(View.GONE);
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                tvSelectDdkAddress.setVisibility(View.GONE);
                getPHPBalance();
                getEthBtcUsdtSellBuyy("php");
                wallet_type = "php";
                tabclickevent = 1;

            } else
            if (tab.getPosition() == 2)
            {
                tvAddressCode.setVisibility(View.VISIBLE);
                buysell_layout.setVisibility(View.VISIBLE);
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                tvSelectDdkAddress.setVisibility(View.GONE);
                String samkoinAddress = App.pref.getString(Constant.SAMKOIN_Add_Id, "");
                if (samkoinAddress != null && !samkoinAddress.equalsIgnoreCase("")) {
                    getSAMKoinBalance("tab",samkoinAddress);
                }
                getEthBtcUsdtSellBuyy("samkoin");
                wallet_type = "samkoin";
                tabclickevent = 2;

            } else if (tab.getPosition() == 3)
            {
                tvAddressCode.setVisibility(View.VISIBLE);
                buysell_layout.setVisibility(View.VISIBLE);
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                tvSelectDdkAddress.setVisibility(View.GONE);
                String btcAddress = App.pref.getString(Constant.BTC_Add_Id, "");
                if (btcAddress != null && !btcAddress.equalsIgnoreCase("")) {
                    getBTCBalance("tab",btcAddress);
                }
                getEthBtcUsdtSellBuyy("btc");
                wallet_type = "btc";
                tabclickevent = 3;
            } /*else if (tab.getPosition() == 4)
            {
                tvAddressCode.setVisibility(View.VISIBLE);
                buysell_layout.setVisibility(View.VISIBLE);
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                tvSelectDdkAddress.setVisibility(View.GONE);
                String tronAddress = App.pref.getString(Constant.tron_ADD_ID, "");
                if (tronAddress != null && !tronAddress.equalsIgnoreCase("")) {
                    getTRONBalance("tab",tronAddress);
                }
                getEthBtcUsdtSellBuyy("tron");
                wallet_type = "tron";
                tabclickevent = 4;
            } */else if (tab.getPosition() == 4)
            {
                tvAddressCode.setVisibility(View.VISIBLE);
                buysell_layout.setVisibility(View.VISIBLE);
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                tvSelectDdkAddress.setVisibility(View.GONE);
                String ethAddress = App.pref.getString(Constant.Eth_ADD_ID, "");
                if (ethAddress != null && !ethAddress.equalsIgnoreCase("")) {
                    getEthBalance("tab",ethAddress);
                }
                getEthBtcUsdtSellBuyy("eth");
                wallet_type = "eth";
                tabclickevent = 4;
            } else if (tab.getPosition() == 5)
            {
                tvAddressCode.setVisibility(View.VISIBLE);
                buysell_layout.setVisibility(View.VISIBLE);
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                tvSelectDdkAddress.setVisibility(View.GONE);
                String usdtAddress = App.pref.getString(Constant.USDT_Add_Id, "");
                if (usdtAddress != null && !usdtAddress.equalsIgnoreCase("")) {
                    getUSDTBalance("tab",usdtAddress);
                }
                getEthBtcUsdtSellBuyy("usdt");
                wallet_type = "usdt";
                tabclickevent = 5;
            } else if (tab.getPosition() == 6)
            {
                tvAddressCode.setVisibility(View.VISIBLE);
                buysell_layout.setVisibility(View.VISIBLE);
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                tvSelectDdkAddress.setVisibility(View.GONE);
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                if (adapter != null && isCallWallet) {
                    isCallWallet = false;
                    getCredentialsCall();
                    tabclickevent = 6;
                }
                getCurrentBalance(6);
                changeThePriceView("ddk");
                wallet_type = "ddk";
                tabclickevent = 6;
            }
        }else
        {
            tvAddressCode.setVisibility(View.VISIBLE);
            buysell_layout.setVisibility(View.VISIBLE);

            if (tab.getPosition() == 0)
            {
                otherwalletlayout.setVisibility(View.GONE);
                newsamlayout.setVisibility(View.VISIBLE);
                getSamToken("tab");
                changeThePriceView("sam");
                wallet_type = "sam";
                tabclickevent=0;
            }else
            if (tab.getPosition() == 1)
            {
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                tvSelectDdkAddress.setVisibility(View.GONE);
                String samkoinAddress = App.pref.getString(Constant.SAMKOIN_Add_Id, "");
                if (samkoinAddress != null && !samkoinAddress.equalsIgnoreCase("")) {
                    getSAMKoinBalance("tab",samkoinAddress);
                }
                getEthBtcUsdtSellBuyy("samkoin");
                wallet_type = "samkoin";
                tabclickevent = 1;

            } else if (tab.getPosition() == 2)
            {
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                tvSelectDdkAddress.setVisibility(View.GONE);
                String btcAddress = App.pref.getString(Constant.BTC_Add_Id, "");
                if (btcAddress != null && !btcAddress.equalsIgnoreCase("")) {
                    getBTCBalance("tab",btcAddress);
                }
                getEthBtcUsdtSellBuyy("btc");
                wallet_type = "btc";
                tabclickevent = 2;
            } /*else if (tab.getPosition() == 3)
            {
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                tvSelectDdkAddress.setVisibility(View.GONE);
                String tronAddress = App.pref.getString(Constant.tron_ADD_ID, "");
                if (tronAddress != null && !tronAddress.equalsIgnoreCase("")) {
                    getTRONBalance("tab",tronAddress);
                }
                getEthBtcUsdtSellBuyy("tron");
                wallet_type = "tron";
                tabclickevent = 3;
            } */else if (tab.getPosition() == 3)
            {
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                tvSelectDdkAddress.setVisibility(View.GONE);
                String ethAddress = App.pref.getString(Constant.Eth_ADD_ID, "");
                if (ethAddress != null && !ethAddress.equalsIgnoreCase("")) {
                    getEthBalance("tab",ethAddress);
                }
                getEthBtcUsdtSellBuyy("eth");
                wallet_type = "eth";
                tabclickevent = 3;
            } else if (tab.getPosition() == 4) {
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                tvSelectDdkAddress.setVisibility(View.GONE);
                String usdtAddress = App.pref.getString(Constant.USDT_Add_Id, "");
                if (usdtAddress != null && !usdtAddress.equalsIgnoreCase("")) {
                    getUSDTBalance("tab",usdtAddress);
                }
                getEthBtcUsdtSellBuyy("usdt");
                wallet_type = "usdt";
                tabclickevent = 4;
            } else if (tab.getPosition() == 5) {
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                tvSelectDdkAddress.setVisibility(View.GONE);
                otherwalletlayout.setVisibility(View.VISIBLE);
                newsamlayout.setVisibility(View.GONE);
                if (adapter != null && isCallWallet) {
                    isCallWallet = false;
                    getCredentialsCall();
                    tabclickevent = 5;
                }
                getCurrentBalance(5);
                changeThePriceView("ddk");
                wallet_type = "ddk";
                tabclickevent = 6;
            }
        }

    }

    public void imageShare()
    {
        BitmapDrawable drawable = (BitmapDrawable) userimg.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();
        content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        shareDialog.show(content);
        pd=new ProgressDialog(getContext());
        pd.setMessage("Please wait");
        pd.show();
    }

    private FacebookCallback<Sharer.Result> callback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result)
        {
            pd.dismiss();
            Toast.makeText(getActivity(), "Successfully posted", Toast.LENGTH_SHORT).show();
            Log.v("UserProfile", "Successfully posted");
            // Write some code to do some operations when you shared content successfully.
        }

        @Override
        public void onCancel() {
            Log.v("UserProfile", "Sharing cancelled");
            pd.dismiss();
           // Toast.makeText(getActivity(), "Sharing canacelled", Toast.LENGTH_SHORT).show();
            // Write some code to do some operations when you cancel sharing content.
        }

        @Override
        public void onError(FacebookException error) {
            Log.v("UserProfile", error.getMessage());
            pd.dismiss();
            Toast.makeText(getActivity(), "user profile "+error.getMessage(), Toast.LENGTH_SHORT).show();
                // Don't use the app for sharing in case of null-error
             //   shareDialog.show(content, ShareDialog.Mode.WEB);
            // Write some code to do some operations when some error occurs while sharing content.
        }
    };

    public void getEthBtcUsdtSellBuyy(final String updateview)
    {

        UserModel.getInstance().getUsdtEthBtcPriceCall(new GetBTCUSDTETHPriceCallback() {
            @Override
            public void getValues(BigDecimal btcPrice, BigDecimal eth, BigDecimal usdt, BigDecimal tron) {

                BigDecimal ONE_HUNDRED = new BigDecimal(100);
                BigDecimal btcpercent=UserModel.getInstance().btcBuyPercentage;
                if (UserModel.getInstance().btcBuyPercentage != null)
                {
                    BigDecimal btcBuyPer = btcPrice.multiply(UserModel.getInstance().btcBuyPercentage).divide(ONE_HUNDRED);
                    BigDecimal ethBuyPer = eth.multiply(UserModel.getInstance().ethBuyPercentage).divide(ONE_HUNDRED);
                    BigDecimal usdtBuyPer = usdt.multiply(UserModel.getInstance().usdtBuyPercentage).divide(ONE_HUNDRED);
                    BigDecimal samkoinBuyPer = UserModel.getInstance().samkoinvalueper.multiply(UserModel.getInstance().samkoinBuyPercentage).divide(ONE_HUNDRED);
                    BigDecimal tronBuyPer = tron.multiply(UserModel.getInstance().tronBuyPercentage).divide(ONE_HUNDRED);

                    BigDecimal btcSellPer = btcPrice.multiply(UserModel.getInstance().btcSellPercentage).divide(ONE_HUNDRED);
                    BigDecimal ethSellPer = eth.multiply(UserModel.getInstance().ethSellPercentage).divide(ONE_HUNDRED);
                    BigDecimal usdtSellPer = usdt.multiply(UserModel.getInstance().usdtSellPercentage).divide(ONE_HUNDRED);
                    BigDecimal samkoinSellPer = UserModel.getInstance().samkoinvalueper.multiply(UserModel.getInstance().samkoinSellPercentage).divide(ONE_HUNDRED);
                    BigDecimal tronSellPer = tron.multiply(UserModel.getInstance().tronSellPercentage).divide(ONE_HUNDRED);

                        UserModel.getInstance().btcBuyPrice = btcBuyPer.add(btcPrice);
                        UserModel.getInstance().btcSellPrice = btcPrice.subtract(btcSellPer);

                        UserModel.getInstance().ethBuyPrice = ethBuyPer.add(eth);
                        UserModel.getInstance().ethSellPrice = eth.subtract(ethSellPer);

                        UserModel.getInstance().usdtBuyPrice = usdtBuyPer.add(usdt);
                        UserModel.getInstance().usdtSellPrice = usdt.subtract(usdtSellPer);

                        UserModel.getInstance().samkoinBuyPrice= samkoinBuyPer.add(UserModel.getInstance().samkoinvalueper);
                        UserModel.getInstance().samkoinSellPrice= UserModel.getInstance().samkoinvalueper.subtract(samkoinSellPer);

                        UserModel.getInstance().tronBuyPrice= tronBuyPer.add(tron);
                        UserModel.getInstance().tronSellPrice= tron.subtract(tronSellPer);

                    if(!updateview.equalsIgnoreCase("")) {
                        changeThePriceView(updateview);
                    }
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("updatePrice"));
                }
            }
        }, getActivity());
    }

    public void clickReceiverButton()
    {
        startActivity(new Intent(mContext, ReceivedActivity.class));
    }

    private void getCryptoCurrencyList()
    {
        Log.d("token",AppConfig.getStringPreferences(getActivity(), Constant.JWTToken));
        AppConfig.getLoadInterface().getbuyCryptoList(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken)).enqueue(new Callback<buyCryptoModel>() {
            @Override
            public void onResponse(Call<buyCryptoModel> call, Response<buyCryptoModel> response) {
                try {
                    Log.d("sam erro par currency",response.body().toString());
                    if (response.isSuccessful() && response.body() != null)
                    {
                        if (response.body().getStatus() == 1)
                        {
                            try {
                                listdata=new ArrayList();
                                listdata.addAll(response.body().getCurrencyTypeList());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else {
                            AppConfig.showToast(response.body().getMsg());
                        }
                    } else {
                        ShowApiError(mContext,"server error ninethface/buy-crypto-list");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<buyCryptoModel> call, Throwable t) {
//                dialog.dismiss();
            }
        });
    }

    private void getPHPBalance()
    {
        try
        {
            Call<EthModelBalance> call = AppConfig.getLoadInterface().getPHPBalance(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken));
            call.enqueue(new retrofit2.Callback<EthModelBalance>() {
                @Override
                public void onResponse(Call<EthModelBalance> call, Response<EthModelBalance> response) {
                    if (response.isSuccessful())
                    {
                        try
                        {
                            if (response.isSuccessful() && response.body() != null)
                            {
                                if (response.body().getStatus() == 1)
                                {
                                        String walletbalance=response.body().getBalance();
                                        App.editor.putString(Constant.PHP_Balance, walletbalance);
                                        App.editor.apply();

                                }else {
                                    App.editor.putString(Constant.PHP_Balance,"0.000000");
                                    App.editor.apply();
                                    AppConfig.showToast(response.body().getMsg());
                                }
                            } else {
                                Log.d("context","::");
                                ShowApiError(mContext,"server error eightface/get-btc-balace");
                            }

                        } catch (Exception e)
                        {
                            AppConfig.hideLoader();
                            ShowApiError(activity,"error in json exception php balance");
                            e.printStackTrace();
                        }
                    } else {
                        AppConfig.hideLoader();
                        ShowApiError(activity,"server error in php  balance");
                    }
                }

                @Override
                public void onFailure(Call<EthModelBalance> call, Throwable t) {
                    AppConfig.hideLoader();
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getSAMKoinBalance(String tabclick,String btcAddressId)
    {
        try {
            final ProgressDialog pd = new ProgressDialog(getContext());
            if(tabclick!="tab") {
                pd.setMessage("Please wait..........");
                pd.setCanceledOnTouchOutside(false);
                pd.show();
            }
            HashMap<String, String> hm = new HashMap<>();
            hm.put("wallet_id", btcAddressId);
            hm.put("device_type", "android");
            hm.put("device_token", App.RegPref.getString(Constant.FIREBASE_TOKEN, ""));
            Log.d("btc para",hm.toString());

            AppConfig.getLoadInterface().getSAMKOINBalance(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<EthModelBalance>() {
                @Override
                public void onResponse(Call<EthModelBalance> call, Response<EthModelBalance> response) {
                    try {
                        if(pd.isShowing()) {
                            pd.dismiss();
                        }
                        if (response.isSuccessful() && response.body() != null)
                        {
                            if (response.body().getStatus() == 1)
                            {
                                try {
                                    String walletbalance=response.body().getBalance();
                                    App.editor.putString(Constant.SAMKOIN_Balance, walletbalance);
                                    App.editor.apply();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }else {
                                App.editor.putString(Constant.SAMKOIN_Balance,"0.000000");
                                App.editor.apply();
                                AppConfig.showToast(response.body().getMsg());
                            }
                        } else {
                            Log.d("context","::");
                            ShowApiError(mContext,"server error eightface/get-btc-balace");
                        }

                 //       AppConfig.hideLoader();

                    } catch (Exception e) {
                        AppConfig.hideLoader();
                        if(pd.isShowing()) {
                            pd.dismiss();
                        }
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<EthModelBalance> call, Throwable t) {
                    AppConfig.hideLoader();
                    if(pd.isShowing()) {
                        pd.dismiss();
                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getUSDTBalance(String tabclick,String usdtAddressId)
    {
        final ProgressDialog pd = new ProgressDialog(getContext());
        if(tabclick!="tab") {
            pd.setMessage("Please wait..........");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
        HashMap<String, String> hm = new HashMap<>();
        hm.put("wallet_id",usdtAddressId);
        hm.put("device_type", "android");
        hm.put("device_token", App.RegPref.getString(Constant.FIREBASE_TOKEN, ""));
        Log.d("balance.........",hm+"");
        AppConfig.getLoadInterface().getUSDTBalance(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<EthModelBalance>() {
            @Override
            public void onResponse(Call<EthModelBalance> call, Response<EthModelBalance> response) {
                try {
                    if(pd.isShowing()) {
                        pd.dismiss();
                    }
                    Log.d("sam erro par usdt",response.body().toString());
                    if (response.isSuccessful() && response.body() != null)
                    {
                        if (response.body().getStatus() == 1)
                        {
                            try {
                                String walletbalance=response.body().getBalance();
                                App.editor.putString(Constant.USDT_Balance,walletbalance);
                                App.editor.apply();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else {
                            App.editor.putString(Constant.USDT_Balance,"0.000000");
                            App.editor.apply();
                            AppConfig.showToast(response.body().getMsg());
                        }
                    } else {
                        Log.d("context",":con");
                        ShowApiError(mContext,"server error eightface/get-btc-balace");
                    }

                   // AppConfig.hideLoader();

                } catch (Exception e)
                {
                    AppConfig.hideLoader();
                    if(pd.isShowing()) {
                        pd.dismiss();
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EthModelBalance> call, Throwable t) {
                AppConfig.hideLoader();
                if(pd.isShowing()) {
                    pd.dismiss();
                }
            }
        });
    }


    private void getTRONBalance(String tabclick,String usdtAddressId)
    {
        final ProgressDialog pd = new ProgressDialog(getContext());
        if(tabclick!="tab") {
            pd.setMessage("Please wait..........");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
        HashMap<String, String> hm = new HashMap<>();
        hm.put("wallet_id",usdtAddressId);
        hm.put("device_type", "android");
        hm.put("device_token", App.RegPref.getString(Constant.FIREBASE_TOKEN, ""));
        Log.d("balance.........",hm+"");
        AppConfig.getLoadInterface().getTRONBalance(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<EthModelBalance>() {
            @Override
            public void onResponse(Call<EthModelBalance> call, Response<EthModelBalance> response) {
                try {
                    if(pd.isShowing()) {
                        pd.dismiss();
                    }
                    Log.d("sam erro par usdt",response.body().toString());
                    if (response.isSuccessful() && response.body() != null)
                    {
                        if (response.body().getStatus() == 1)
                        {
                            try {
                                String walletbalance=response.body().getBalance();
                                App.editor.putString(Constant.USDT_Balance,walletbalance);
                                App.editor.apply();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else {
                            App.editor.putString(Constant.USDT_Balance,"0.000000");
                            App.editor.apply();
                            AppConfig.showToast(response.body().getMsg());
                        }
                    } else {
                        Log.d("context",":con");
                        ShowApiError(mContext,"server error eightface/get-btc-balace");
                    }

                    // AppConfig.hideLoader();

                } catch (Exception e)
                {
                    AppConfig.hideLoader();
                    if(pd.isShowing()) {
                        pd.dismiss();
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EthModelBalance> call, Throwable t) {
                AppConfig.hideLoader();
                if(pd.isShowing()) {
                    pd.dismiss();
                }
            }
        });
    }

    private void getBTCBalance(String tabclick,String btcAddressId) {
        try {
            final ProgressDialog pd = new ProgressDialog(getContext());
            if(tabclick!="tab") {
                pd.setMessage("Please wait..........");
                pd.setCanceledOnTouchOutside(false);
                pd.show();
            }
            HashMap<String, String> hm = new HashMap<>();
            hm.put("wallet_id", btcAddressId);
            hm.put("device_type", "android");
            hm.put("device_token", App.RegPref.getString(Constant.FIREBASE_TOKEN, ""));
            Log.d("btc para",hm.toString());
            AppConfig.getLoadInterface().getBTCBalance(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<EthModelBalance>() {
                @Override
                public void onResponse(Call<EthModelBalance> call, Response<EthModelBalance> response) {
                    try {
                        if (response.isSuccessful() && response.body() != null)
                        {
                            if (response.body().getStatus() == 1)
                            {
                                try {
                                    String walletbalance=response.body().getBalance();
                                    App.editor.putString(Constant.BTC_Balance, walletbalance);
                                    App.editor.apply();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }else {
                                App.editor.putString(Constant.BTC_Balance,"0.000000");
                                App.editor.apply();
                                AppConfig.showToast(response.body().getMsg());
                            }
                        } else {
                            Log.d("context","::");
                            ShowApiError(mContext,"server error eightface/get-btc-balace");
                        }
                     //   AppConfig.hideLoader();
                        if(pd.isShowing()) {
                            pd.dismiss();
                        }
                    } catch (Exception e)
                    {
                        AppConfig.hideLoader();
                        if(pd.isShowing()) {
                            pd.dismiss();
                        }
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<EthModelBalance> call, Throwable t) {
                    AppConfig.hideLoader();
                    if(pd.isShowing()) {
                        pd.dismiss();
                    }
              }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getEthBalance(String tabclick,String ethAddressId)
    {
        try {
            final ProgressDialog pd = new ProgressDialog(getContext());
            if(tabclick!="tab") {
                pd.setMessage("Please wait..........");
                pd.setCanceledOnTouchOutside(false);
                pd.show();
            }
            HashMap<String, String> hm = new HashMap<>();
            hm.put("wallet_id",ethAddressId);
            hm.put("device_type", "android");
            hm.put("device_token", App.RegPref.getString(Constant.FIREBASE_TOKEN, ""));
            Log.d("btc para",hm.toString());
            AppConfig.getLoadInterface().getEthBalance(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<EthModelBalance>() {
                @Override
                public void onResponse(Call<EthModelBalance> call, Response<EthModelBalance> response) {
                    try
                    {
                        Log.d("sam erro par eth",response.body().toString());
                        if (response.isSuccessful() && response.body() != null)
                        {
                            if (response.body().getStatus() == 1)
                            {
                                try {
                                    String walletbalance=response.body().getBalance();
                                    App.editor.putString(Constant.Eth_Balance,walletbalance);
                                    App.editor.apply();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }else {
                                App.editor.putString(Constant.Eth_Balance,"0.000000");
                                App.editor.apply();
                                AppConfig.showToast(response.body().getMsg());
                            }
                        } else {
                            ShowApiError(mContext,"server error eightface/eth-balance");
                        }
                       // AppConfig.hideLoader();
                        if(pd.isShowing()) {
                            pd.dismiss();
                        }

                    }catch (Exception e) {
                        AppConfig.hideLoader();
                        if(pd.isShowing()) {
                            pd.dismiss();
                        }
                        ShowApiError(mContext," error in eightface/eth-balance");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<EthModelBalance> call, Throwable t) {
                    AppConfig.hideLoader();
                    if(pd.isShowing()) {
                        pd.dismiss();
                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getBannerKImages() {
        try {
            LoadInterface apiservice = AppConfig.getClient().create(LoadInterface.class);
            //we havd to  correct
            Call<SliderImgResponse> call = apiservice.getBannerLayout();
            //showProgressDiaog();
            call.enqueue(new Callback<SliderImgResponse>() {
                @Override
                public void onResponse(Call<SliderImgResponse> call, Response<SliderImgResponse> response) {
                    //  hideProgress();
                    try {
                        int code = response.code();
                        String retrofitMesage = "";
                        retrofitMesage = response.body().getMsg();
                        if (code == 200) {
                            retrofitMesage = response.body().getMsg();
                            int status = response.body().getStatus();
                            if (status == 1) {
                                drawablesList = new ArrayList<>();
                                drawablesList.addAll(response.body().lendData);
                                if (drawablesList.size() > 0) {
                                    setBannerImages();
                                }
                            } else if (status == 400) {
                               ShowApiError(getActivity(),"error in api ninethface/sliders");
                            } else {
                                Toast.makeText(getActivity(), retrofitMesage, Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        if (code == 404) {
                            Toast.makeText(getActivity(), "" + "Page not found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (code == 500) {
                            ShowApiError(getActivity(),"error in api ninethface/sliders");
                        } else {
                            Toast.makeText(getActivity(), "" + retrofitMesage, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception s) {
                        s.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SliderImgResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "Response getting failed", Toast.LENGTH_SHORT).show();
                    // hideProgress();
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getInviteFriend()
    {
        String userReferalCode =App.pref.getString(Constant.USER_REFERAL_CODE, "");
        Log.d("token",AppConfig.getStringPreferences(getActivity(), Constant.JWTToken));
        HashMap<String, String> hm = new HashMap<>();
        hm.put("referral_code", userReferalCode);
        AppConfig.getLoadInterface().getInviteFriends(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<userInviteModel>() {
            @Override
            public void onResponse(Call<userInviteModel> call, Response<userInviteModel> response) {
                try {
                    Log.d("sam erro par invi",response.body().toString());
                    if (response.isSuccessful() && response.body() != null)
                    {
                        if (response.body().getStatus() == 1)
                        {
                            try {
                                String INVITE_CONTENT = response.body().getData();
                                App.editor.putString(Constant.INVITE_CONTENT,INVITE_CONTENT);
                                App.editor.apply();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else {
                         //   AppConfig.showToast(response.body().getMsg());
                        }
                    } else {
                        ShowApiError(mContext,"server error apigetreferralcode/get-referral-code");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<userInviteModel> call, Throwable t) {
            }
        });
    }

    private void getRedeemOptions()
    {
        Log.d("token",AppConfig.getStringPreferences(getActivity(), Constant.JWTToken));
        AppConfig.getLoadInterface().getRedeemList(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken)).enqueue(new Callback<RedeemOptionModel>() {
            @Override
            public void onResponse(Call<RedeemOptionModel> call, Response<RedeemOptionModel> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus() == 1)
                        {
                            Log.d("chages data",response.toString());
                            if(response.body().getData()!=null)
                            {
                                Redeemoptionlist=new ArrayList<>();
                                Redeemoptionlist.addAll(response.body().getData());
                            }else
                            {
                                App.editor.putString(Constant.SAM_Balance,"0.00000");
                                App.editor.apply();
                                AppConfig.showToast(response.body().getMsg());
                                Toast.makeText(mContext, "No Data Available", Toast.LENGTH_SHORT).show();
                            }

                        }else if (response.body().getStatus() == 4)
                        {
                            ShowServerPost(getActivity(),"server error redeem/redeem-options");
                        }else
                        {
                            AppConfig.showToast(response.body().getMsg());
                        }
                    } else {
                        ShowApiError(mContext,"server error redeem/redeem-options");
                    }

                } catch (Exception e) {
                    ShowApiError(mContext,"error in response redeem/redeem-options");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RedeemOptionModel> call, Throwable t)
            {
                pd.dismiss();
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSamToken(String tabclick)
    {
        final ProgressDialog pd = new ProgressDialog(getContext());
        if(tabclick!="tab") {
            pd.setMessage("Please wait..........");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
        Log.d("token",AppConfig.getStringPreferences(getActivity(), Constant.JWTToken));
        AppConfig.getLoadInterface().getSameToken(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken)).enqueue(new Callback<samModel>() {
            @Override
            public void onResponse(Call<samModel> call,  Response<samModel> response) {
                try {
                    if(pd.isShowing()) {
                        pd.dismiss();
                    }

                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus() == 1)
                        {
                            Log.d("chages data",response.toString());
                            if(response.body().getData()!=null)
                            {
                                String sametoken=response.body().getData().getToken().toString();
                                App.editor.putString(Constant.SAM_Balance,sametoken);
                                App.editor.apply();
                                BigDecimal currentbalancesam=new BigDecimal(sametoken);
                                sam_wallet.setText(String.format("%.4f",currentbalancesam));

                            }else
                            {
                                App.editor.putString(Constant.SAM_Balance,"0.0000");
                                App.editor.apply();
                                AppConfig.showToast(response.body().getMsg());
                                Toast.makeText(mContext, "No Data Available", Toast.LENGTH_SHORT).show();
                            }
                        }else if (response.body().getStatus() == 4)
                        {
                            ShowServerPost(getActivity(),"server error apibalance/token");
                        }else
                        {
                            AppConfig.showToast(response.body().getMsg());
                        }
                    } else {
                        ShowApiError(mContext,"server error apibalance/token");
                    }
               //     AppConfig.hideLoader();

                } catch (Exception e) {
                    AppConfig.hideLoader();
                    if(pd.isShowing()) {
                        pd.dismiss();
                    }
                    ShowApiError(mContext,"error in response apibalance/token");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<samModel> call, Throwable t)
            {
                AppConfig.hideLoader();
                if(pd.isShowing()) {
                    pd.dismiss();
                }
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //for get liost
    private void getDATAStatusKey()
    {
        try {
            AppConfig.getLoadInterface().getKeyListData(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken)).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful() && response.body() != null)
                        {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1)
                            {
                                    JSONObject dataobj=object.getJSONObject("data");
                                    String stripekeyvalue = dataobj.getString("key");
                                    Log.d("stipe","::"+stripekeyvalue);
                                    String mapmapUrl=object.getString("mapUrl");
                                    String mapAllRunUrl=object.getString("map_api_url");
                                    dataPutMethods.putKeyDetails(mapAllRunUrl,mapmapUrl,stripekeyvalue);
                            } else {
                                AppConfig.showToast(object.getString("msg"));
                            }
                        } else {
                            ShowApiError(mContext,"server error in apiconstantkey/api-constant-key");
                        }
                    }catch (Exception e)
                    {
                        ShowApiError(mContext,"exception in apiconstantkey/api-constant-key");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t)
                        {
//                dialog.dismiss();
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //for verifcation
    private void getVerificationStatus()
    {
        String userId =App.pref.getString(Constant.USER_ID, "");
        try {
            AppConfig.getLoadInterface().getVerificationStatus(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken)).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            String responseData = response.body().string();
                            Log.d("get verification status", responseData);
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1)
                            {

                                    JSONObject dataobj=object.getJSONObject("data");
                                    String emailverifcation = dataobj.getString("email_verification_status");
                                    String moibleverifcation = dataobj.getString("mobile_verification_status");
                                    String id_proof_1_verification_status = dataobj.getString("id_proof_1_verification_status");
                                    String id_proof_2_verification_status = dataobj.getString("id_proof_2_verification_status");
                                    String fund_source_verification_status = dataobj.getString("fund_source_verification_status");
                                    String address_verification_status= dataobj.getString("address_verification_status");
                                    String single_video_verification_status=dataobj.getString("single_video_verification_status");
                                    dataPutMethods.putUserVerification(emailverifcation,moibleverifcation,id_proof_1_verification_status,id_proof_2_verification_status,fund_source_verification_status,address_verification_status,single_video_verification_status);

                            } else {
                                AppConfig.showToast(object.getString("msg"));
                            }
                        } else {
                            ShowApiError(mContext,"server error in kyc-verification-status");
                        }
                    }catch (Exception e) {
                        ShowApiError(mContext,"exception in kyc-verification-status");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                dialog.dismiss();
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getMapreceiverAddress()
    {
        LoadInterface apiservice = AppConfig.getClient().create(LoadInterface.class);
        //we havd to  correct
        Call<MapTransactionReceiverModel> call = apiservice.getMapreceiverAddress();
        //showProgressDiaog();
        call.enqueue(new Callback<MapTransactionReceiverModel>() {
            @Override
            public void onResponse(Call<MapTransactionReceiverModel> call, Response<MapTransactionReceiverModel> response) {
                //  hideProgress();
                try {
                    int code = response.code();
                    String retrofitMesage="";
                    retrofitMesage=response.body().getMsg();
                    if (code==200)
                    {
                        retrofitMesage=response.body().getMsg();
                        int status = response.body().getStatus();
                        if (status==1)
                        {
                            AppConfig.setStringPreferences(App.getInstance(), Constant.Transaction_Receiver_SAMKOIN_Map,""+response.body().getData().getSamReceiverAddress());
                            AppConfig.setStringPreferences(App.getInstance(), Constant.Transaction_Receiver_BTC_Map,""+response.body().getData().getBtcReceiverAddress());
                            AppConfig.setStringPreferences(App.getInstance(), Constant.Transaction_Receiver_ETH_Map,""+response.body().getData().getEthReceiverAddress());
                            AppConfig.setStringPreferences(App.getInstance(), Constant.Transaction_Receiver_USDT_Map,""+response.body().getData().getUsdtReceiverAddress());
                            //for transaction
                            AppConfig.setStringPreferences(App.getInstance(), Constant.SAMTransactionAddress_Map,""+response.body().getData().getSamReceiverTransAddress());
                            AppConfig.setStringPreferences(App.getInstance(), Constant.ETHTranscationAddress_Map,""+response.body().getData().getEthReceiverTransAddress());
                            AppConfig.setStringPreferences(App.getInstance(), Constant.BTCTransactionAddress_Map,""+response.body().getData().getBtcReceiverTransAddress());
                            AppConfig.setStringPreferences(App.getInstance(), Constant.USDTTransactionAddress_Map,""+response.body().getData().getUsdtReceiverTransAddress());

                        }
                        else
                        if (status==400)
                        {
                            Toast.makeText(getActivity(), "Bad request", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), retrofitMesage, Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    if (code==404)
                    {
                        ShowApiError(getContext(),"server error in eightface/received-address");
                        return;
                    }
                    if (code==500)
                    {
                        ShowApiError(getContext(),"server error in eightface/received-address");
                    }
                    else
                    {
                        Toast.makeText(getActivity(), ""+retrofitMesage, Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception s)
                {
                    s.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<MapTransactionReceiverModel> call, Throwable t)
            {
                errordurigApiCalling(getActivity(),t.getMessage());
                // hideProgress();
            }
        });
    }

    private void getReceiverAddress()
    {
        LoadInterface apiservice = AppConfig.getClient().create(LoadInterface.class);
        //we havd to  correct
        Call<TransactionFeesResponse> call = apiservice.getreceiverAddress();
        //showProgressDiaog();
        call.enqueue(new Callback<TransactionFeesResponse>() {
            @Override
            public void onResponse(Call<TransactionFeesResponse> call, Response<TransactionFeesResponse> response) {
                //  hideProgress();
                try {
                    int code = response.code();
                    String retrofitMesage="";
                    retrofitMesage=response.body().getMsg();
                    if (code==200)
                    {
                        retrofitMesage=response.body().getMsg();
                        int status = response.body().getStatus();
                        if (status==1)
                        {
                            TransactionFeeData transactionFeeData = response.body().getData();
                            String ddkaddress=transactionFeeData.getDdk_address();
                            AppConfig.setStringPreferences(App.getInstance(), Constant.Transaction_Receiver_SAMKOIN,""+transactionFeeData.getSam_address());
                            AppConfig.setStringPreferences(App.getInstance(), Constant.Transaction_Receiver,""+ddkaddress);
                            AppConfig.setStringPreferences(App.getInstance(), Constant.Transaction_Receiver_BTC,""+transactionFeeData.getBtc_address());
                            AppConfig.setStringPreferences(App.getInstance(), Constant.Transaction_Receiver_ETH,""+transactionFeeData.getEth_address());
                            AppConfig.setStringPreferences(App.getInstance(), Constant.Transaction_Receiver_USDT,""+transactionFeeData.getUsdt_address());
                            //for transaction
                            AppConfig.setStringPreferences(App.getInstance(), Constant.SAMTransactionAddress,""+transactionFeeData.getSam_trans_address());
                            AppConfig.setStringPreferences(App.getInstance(), Constant.ETHTranscationAddress,""+transactionFeeData.getEth_trans_address());
                            AppConfig.setStringPreferences(App.getInstance(), Constant.BTCTransactionAddress,""+transactionFeeData.getBtc_trans_address());
                            AppConfig.setStringPreferences(App.getInstance(), Constant.USDTTransactionAddress,""+transactionFeeData.getUsdt_trans_address());

                        }
                        else
                        if (status==400)
                        {
                            Toast.makeText(getActivity(), "Bad request", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), retrofitMesage, Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    if (code==404)
                    {
                        ShowApiError(getContext(),"server error in eightface/received-address");
                        return;
                    }
                    if (code==500)
                    {
                    ShowApiError(getContext(),"server error in eightface/received-address");
                    }
                    else
                    {
                        Toast.makeText(getActivity(), ""+retrofitMesage, Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception s)
                {
                  s.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<TransactionFeesResponse> call, Throwable t)
            {
                errordurigApiCalling(getActivity(),t.getMessage());
                // hideProgress();
            }
        });
    }

    private void getFacebookShare()
    {
        Call<ResponseBody> call = AppConfig.getLoadInterface().getFacebookShare();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        String status=jsonObject.getString("status");
                        if(status.equalsIgnoreCase("1"))
                        {
                            JSONObject jObject = jsonObject.getJSONObject("data");
                            String imgview = jObject.getString("image");
                            String combineurl=Constant.SLIDERIMG+imgview;
                            AppConfig.setStringPreferences(App.getInstance(), Constant.FACEBOOKURL,""+combineurl);
                            Picasso.with(mContext).load(combineurl).into(userimg);

                        }else
                        {
                            String msg=jsonObject.getString("msg");
                            Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e)
                    {
                        ShowApiError(activity,"exception in response ninethface/social-media-share");
                        e.printStackTrace();
                    }
                } else {
                    ShowApiError(activity,"server error in ninethface/social-media-share");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void setCredentialPopup(LayoutInflater inflater) {
        View view1 = inflater.inflate(R.layout.popup_wallet, null, false);
        pw = new PopupWindow(view1, 580, 650, true);
        RecyclerView recyclerView = view1.findViewById(R.id.recycler_view);
        final EditText searchEt = view1.findViewById(R.id.search_ET);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new WalletPopupAdapter(new ArrayList<Credential>(), getActivity(), searchEt);
        recyclerView.setAdapter(adapter);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(searchEt.getText().toString());
            }
        });
    }

    private void setBannerImages() {
        // The code below assumes that the root container has an id called 'main'
        homeBannerPager = new HomeBannerPagerAdapter(mContext, drawablesList);
        mViewPager.setAdapter(homeBannerPager);
//        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mViewPagerAll.setAdapter(homeBannerPager);
//        mViewPagerAll.setPageTransformer(true, new ZoomOutPageTransformer());

        changeViewPagerPage();
    }


    private void changeViewPagerPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count == drawablesList.size() - 1) {
                    count = 0;
                } else {
                    count++;
                }
                mViewPager.setCurrentItem(count);
                mViewPagerAll.setCurrentItem(count);
                changeViewPagerPage();
            }
        }, 3000);
    }

    private void filter(String newText)
    {
        if (_credentialList.size() > 0)
        {
            List<Credential> doctorNew = new ArrayList<>();

            if (newText.isEmpty()) {
                doctorNew.addAll(_credentialList);
            } else {
                for (Credential event : _credentialList) {
                    if (event.getName().toLowerCase().contains(newText.toLowerCase()) ||
                            event.getDdkcode().toLowerCase().contains(newText.toLowerCase())) {
                        doctorNew.add(event);
                    }
                }

                if (doctorNew.size() == 0) {
                    AppConfig.showToast("No search data Found.");
                }
            }
            adapter.updateData(doctorNew);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        MainActivity.scanview.setVisibility(View.VISIBLE);
        if (adapter != null && isCallWallet) {
            isCallWallet = false;
            getCredentialsCallVlidate();
            tabclickevent=2;
        }
        if (lytAll.getVisibility() == View.VISIBLE) {
            MainActivity.setTitle("All Features");
            MainActivity.enableBackViews(true);
        } else {
            MainActivity.setTitle("Dashboard");
            MainActivity.enableBackViews(false);
        }

        if(MainActivity.updateTabview==1)
        {
            setTablayoutview();
            MainActivity.updateTabview=0;
        }else {
            getSettingServerData(getActivity(), "php");
        }

        if(profileupdatesta==1)
        {
            getSettingServerData(getActivity(),"php");
            setTablayoutview();
            MainActivity.updateTabview=0;
            profileupdatesta=0;
        }
        getVerificationStatus();
        //for balance
        String ethAddress=App.pref.getString(Constant.Eth_ADD_ID, "");
        if(ethAddress!=null && !ethAddress.equalsIgnoreCase(""))
        {
            getEthBalance("not",ethAddress);
        }

        String btcAddress=App.pref.getString(Constant.BTC_Add_Id, "");
        if(btcAddress!=null && !btcAddress.equalsIgnoreCase(""))
        {
            getBTCBalance("not",btcAddress);
        }

        String usdtAddress=App.pref.getString(Constant.USDT_Add_Id, "");
        if(usdtAddress!=null && !usdtAddress.equalsIgnoreCase(""))
        {
            getUSDTBalance("not",usdtAddress);
        }

        String samkoinAddres=App.pref.getString(Constant.SAMKOIN_Add_Id, "");
        if(samkoinAddres!=null && !samkoinAddres.equalsIgnoreCase(""))
        {
            getSAMKoinBalance("not",samkoinAddres);
        }

    }

    private void getCurrentBalance(final int loadview)
    {
        String walletidval=App.pref.getString(Constant.WALLET_ID, "");
        if(walletidval!=null && !walletidval.equalsIgnoreCase(""))
        {
            UserModel.getInstance().getWalletDetails(loadview,walletidval, mContext, new GetAvailableValue() {
                @Override
                public void getValues(String ddk, WalletResponse successResponse)
                {
                    ddk=ReplacecommaValue(ddk+"");
                    BigDecimal currentbalance=new BigDecimal(ddk);
                    BigDecimal roundhaldv=currentbalance.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString()+"");
                    progressBar.setVisibility(View.GONE);
                }
            });

        }
    }

    public void getSettingServerOnTab(String loaderstaus,final TabLayout.Tab tab, Activity activity, final String functionname)
    {
          AppConfig.showLoading("please wait ..", activity);
        String func=functionname;
        {
            func=functionname;
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
                            String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
                            if(!curkj.equalsIgnoreCase("true"))
                            {
                                App.editor.putString(Constant.PHP_Functionality_View,"true");
                                App.editor.apply();
                                setTablayoutview();
                                // MainActivity.updateTabview=1;
                            }
                                progressBar.setVisibility(View.GONE);
                                setTabView(tab);
                    } else
                    {
                            String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
                            if(!curkj.equalsIgnoreCase("false"))
                            {
                                App.editor.putString(Constant.PHP_Functionality_View,"false");
                                App.editor.apply();
                                setTablayoutview();

                                //MainActivity.updateTabview=1;
                            }
                            progressBar.setVisibility(View.GONE);
                            setTabView(tab);
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getSettingServerData(Activity activity, final String functionname)
    {
        if(!functionname.equalsIgnoreCase("php")) {
            AppConfig.showLoading("please wait ..", activity);
        }
        String func=functionname;
        if(func.equalsIgnoreCase("send"))
        {
            String curkj=App.pref.getString(Constant.PHP_Functionality_View, "");
            String countrydata=userData.getUser().country.get(0).country;
            if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
            {
                if (HomeFragment.tabclickevent == 1) {
                    func = "send_wallet_currency";

                } else if (HomeFragment.tabclickevent == 2) {
                    func = "send_sam_koin";

                } else if (HomeFragment.tabclickevent == 6) {
                    func = "send_ddk ";

                } else if (HomeFragment.tabclickevent == 3) {
                    func = "send_btc";

                } /*else if (HomeFragment.tabclickevent == 4) {
                    func = "send_tron";

                } */else if (HomeFragment.tabclickevent == 4) {
                    func = "send_eth";

                } else if (HomeFragment.tabclickevent == 5) {
                    func = "send_usdt";
                }
            }else {
                if (HomeFragment.tabclickevent == 1) {
                    func = "send_sam_koin";

                } else if (HomeFragment.tabclickevent == 5) {
                    func = "send_ddk ";

                } else if (HomeFragment.tabclickevent == 2) {
                    func = "send_btc";

                }/* else if (HomeFragment.tabclickevent == 3) {
                    func = "send_tron";

                }*/ else if (HomeFragment.tabclickevent == 3) {
                    func = "send_eth";

                } else if (HomeFragment.tabclickevent == 4) {
                    func = "send_usdt";
                }
            }
        }else
        {
            func=functionname;
        }
        UserModel.getInstance().getSettignSatusView(activity,func,new GegtSettingStatusinterface()
        {
            @Override
            public void getResponse(Response<getSettingModel> response)
            {
                if(!functionname.equalsIgnoreCase("php")) {
                    AppConfig.hideLoader();
                }
                try
                {
                    if (response.body().getStatus() == 1)
                    {

                        if(functionname.equalsIgnoreCase("subscription"))
                        {
                            isCallWallet = false;
                            //for terms condition
                            Fragment fragment = new TermsAndConsitionSubscription();
                            Bundle arg = new Bundle();
                            arg.putString("activityaction", "subscription");
                            fragment.setArguments(arg);
                            MainActivity.addFragment(fragment,true);
                            showHideAll();
                        }else if(functionname.equalsIgnoreCase("redeem"))
                        {
                            MainActivity.addFragment(new SamKoinsRedeemFragment(), true);

                        }else if(functionname.equalsIgnoreCase("cancellation"))
                        {
                            MainActivity.addFragment(new CancellationFragment(), true);
                        }else if(functionname.equalsIgnoreCase("buy"))
                        {
                            MainActivity.addFragment(new BuyFragment(), true);
                        }else if(functionname.equalsIgnoreCase("sell"))
                        {
                            String countrydata=userData.getUser().country.get(0).country;
                            if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") || countrydata.equalsIgnoreCase("australia")))
                            {
                                //for now hide below code for show dilaog
                                //dataPutMethods.showRedeemDialog(mContext,Redeemoptionlist,"sell");
                                MainActivity.addFragment(new CashOutFragmentNew(), true);
                            }else
                            {
                                ShowCahsoutDialog(getActivity());
                            }
                        }else if(functionname.equalsIgnoreCase("map"))
                        {
                            try {
                                String mapurl= App.pref.getString(Constant.MApURl,"");
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapurl));
                                startActivity(browserIntent);

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }else if(functionname.equalsIgnoreCase("send"))
                        {
                            MainActivity.addFragment(new SendFragment(), true);

                        }else if(functionname.equalsIgnoreCase("php"))
                        {
                            String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
                            if(!curkj.equalsIgnoreCase("true"))
                            {
                                MainActivity.updateTabview=1;
                                App.editor.putString(Constant.PHP_Functionality_View,"true");
                                App.editor.apply();
                                //setTablayoutview();
                                 // MainActivity.updateTabview=1;
                            }else
                            {

                            }
                        }

                    } else
                    {
                        if(functionname.equalsIgnoreCase("php"))
                        {
                            String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
                            if(!curkj.equalsIgnoreCase("false"))
                            {
                                MainActivity.updateTabview=1;
                                App.editor.putString(Constant.PHP_Functionality_View,"false");
                                App.editor.apply();
                                //setTablayoutview();
                                //MainActivity.updateTabview=1;
                            }else
                            {

                            }

                        }else
                        {
                            ShowFunctionalityAlert(getActivity(), response.body().getMsg());
                        }
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnMAp:
                getSettingServerData(getActivity(),"map");
                break;

            case R.id.redeem_layout:
                //getSettingServerData
                //for current version 2.4.13
                getSettingServerData(getActivity(),"redeem");
                // MainActivity.addFragment(new SamTransactionDDKFragment(), true);
                //  dataPutMethods.showRedeemDialog(mContext,Redeemoptionlist,"redeem");
               // MainActivity.addFragment(new SamTransactionDDKFragment(), true);
                //for next level version 2.4.14 functoanlity is done one only and check api
//                MainActivity.addFragment(new RedeemSelectionFrgament(), true);
                break;

            case R.id.btnallsam:
                view.findViewById(R.id.lytMain).setVisibility(View.GONE);
                view.findViewById(R.id.lytAll).setVisibility(View.VISIBLE);
                MainActivity.enableBackViews(true);
                break;

            case R.id.btntransaparet:
                MainActivity.addFragment(new TransparentServerFragment(), true);
                break;

            case R.id.cancellation:
                getSettingServerData(getActivity(),"cancellation");
                break;

            case R.id.btnexchange:
              //  MainActivity.addFragment(new ExchangeFragment(), true);
                break;

            case R.id.btnSAMPD_2:
            case R.id.btnsampd:
                MainActivity.addFragment(new SAMPDFragment(), true);
                break;

            case R.id.btnremittance:
               // Toast.makeText(mContext, "click on btnremittance layout", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnaccoutnverification_2:
            case R.id.btnaccoutnverification:
                MainActivity.addFragment(new AccoutnverficationFregament(), true);
                break;

            case R.id.ridelayout:
                //goToGoogleMap();
                //Toast.makeText(mContext, "click on ride layout", Toast.LENGTH_SHORT).show();
                break;

            case R.id.paybills_layout:
            case R.id.btnProject_1:
            case R.id.btnProject_2:
                getSettingServerData(getActivity(),"subscription");
                break;

            case R.id.btnSend_1:
                getSettingServerData(getActivity(),"send");
                 break;

            case R.id.history_layout:
            case R.id.btnHistory_1:
            case R.id.btnHistory_2:
                MainActivity.addFragment(new HistoryFragment(), true);
                break;

            case R.id.btnBuy_1:
            case R.id.btnBuy_2:
                getSettingServerData(getActivity(),"buy");
                break;

            case R.id.btnCredential:
                isCallWallet = true;
                if (App.pref.getBoolean(AppConfig.isPin, false)) {
                    Intent intent = new Intent(MainActivity.activity, CustomPinActivity.class);
                    intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
                    startActivityForResult(intent, MainActivity.REQUEST_CODE_DISABLE);
                } else {
                    LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
                    lockManager.enableAppLock(MainActivity.activity, CustomPinActivity.class);
                    Intent intent = new Intent(MainActivity.activity, CustomPinActivity.class);
                    intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
                    startActivityForResult(intent, MainActivity.REQUEST_CODE_ENABLE);
                }
                break;

            case R.id.btnImport_1:
            case R.id.btnImport_2:
                isCallWallet = true;
                MainActivity.addFragment(new AddCredentialsFragment(), true);
                break;

            case R.id.btnCashOut_1:
            case R.id.btnCashOut_2:
                //for version 2.4.14 next module new launch
                getSettingServerData(getActivity(),"sell");
                //MainActivity.addFragment(new CashOutFragmentNew(), true);
                //for version 2.4.13 running
                 //MainActivity.addFragment(new CashOutFragment(), true);
                break;

            case R.id.btnCreateWallet_1:
                isCallWallet = true;
                startActivity(new Intent(getActivity(), CreateWalletActivity.class));
                break;

            case R.id.btnTutorials:
                MainActivity.addFragment(new TutorialsFragment(), true);
                break;

            case R.id.btnOurTeam:
                MainActivity.addFragment(new OurTeamFragment(), true);
                break;

            case R.id.btnReferralList_2:
            case R.id.btnReferralList:
//                startActivity(new Intent(getActivity(), ReferralListActivity.class));
                startActivity(new Intent(getActivity(), ReferralChainPayoutActivity.class));
                break;

            case R.id.btnIncome:
                MainActivity.addFragment(new IncomeOpportunitiesFragment(), true);
                break;

            case R.id.btnProfile:
                String googlevaue=App.pref.getString(Constant.GOOGLEAUTHOPTIONSTATUS, "");
                if(googlevaue.equalsIgnoreCase("1"))
                {
                    MainActivity.ClickViewButton="profile";
                    String googlestatus=App.pref.getString(Constant.GOOGLEAUThPendingRegit, "");
                    if(googlestatus.equalsIgnoreCase("pending"))
                    {
                        MainActivity.addFragment(new GogoleAuthFragment(), true);

                    }else
                    {
                        MainActivity.addFragment(new GogolePasswordFragment(), true);
                    }

                }else
                {
                    MainActivity.addFragment(new ProfileFragment(), true);
                }
                break;

                /*case R.id.btnEvents:
                MainActivity.addFragment(new EventsFragment(), true);
                break;
            case R.id.btnSell_2:
                MainActivity.addFragment(new SellFragment(), true);
                break;
             case R.id.btnConvert_2:
                MainActivity.addFragment(new ConvertFragment(), true);
                break;
            */
        }
    }

    private void goToGoogleMap()
    {
        Intent in = new Intent(getActivity(), MapsActivity.class);
        startActivity(in);
    }
    //
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case MainActivity.REQUEST_CODE_ENABLE:
                App.editor.putBoolean(AppConfig.isPin, true);
                App.editor.commit();
                String googlevaue=App.pref.getString(Constant.GOOGLEAUTHOPTIONSTATUS, "");
                if(googlevaue.equalsIgnoreCase("1"))
                {
                    MainActivity.ClickViewButton="crediential";
                    String googlestatus=App.pref.getString(Constant.GOOGLEAUThPendingRegit, "");
                    if(googlestatus.equalsIgnoreCase("pending"))
                    {
                        MainActivity.addFragment(new GogoleAuthFragment(), true);
                    }else
                    {
                        MainActivity.addFragment(new GogolePasswordFragment(), true);
                    }

                }else
                {
                    MainActivity.addFragment(new CredentialsFragment(), true);
                }

                break;
            case MainActivity.REQUEST_CODE_DISABLE:
                String googlevaue1=App.pref.getString(Constant.GOOGLEAUTHOPTIONSTATUS, "");
                if(googlevaue1.equalsIgnoreCase("1"))
                {
                    MainActivity.ClickViewButton="crediential";
                    String googlestatus=App.pref.getString(Constant.GOOGLEAUThPendingRegit, "");
                    if(googlestatus.equalsIgnoreCase("pending"))
                    {
                        MainActivity.addFragment(new GogoleAuthFragment(), true);
                    }else
                    {
                        MainActivity.addFragment(new GogolePasswordFragment(), true);
                    }

                }else
                {
                    MainActivity.addFragment(new CredentialsFragment(), true);
                }
                break;
            default:
                callbackManager.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void getCredentialsCall() {
        if (AppConfig.isInternetOn()) {
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
                                _credentialList = new ArrayList<>();
                                CredentialsResponse registerResponse = new Gson().fromJson(responseData, CredentialsResponse.class);
                                for (Credential credential : registerResponse.getCredentials()) {
                                    if (credential.getStatus().equalsIgnoreCase("active")) {
                                        _credentialList.add(credential);
                                    }
                                }

                                if (!App.pref.getBoolean(Constant.isWallet, false)) {
                                    App.editor.putString(Constant.WALLET_ID, _credentialList.get(0).getWalletId());
                                    App.editor.putBoolean(Constant.isWallet, true);
                                    App.editor.commit();
                                    String walletidvalue=App.pref.getString(Constant.WALLET_ID, "");
                                    if(walletidvalue!=null && !walletidvalue.equalsIgnoreCase(""))
                                    {
                                        getWalletDetails(walletidvalue, _credentialList.get(0).getPassphrase());
                                    }
                                } else {
                                    String activeWallet = "";
                                    String activePass = "";
                                    for (Credential credential : _credentialList) {
                                        if (App.pref.getString(Constant.WALLET_ID, "").equals(credential.getWalletId())) {
                                            activeWallet = credential.getWalletId();
                                            activePass = credential.getPassphrase();
                                        }
                                    }
                                    if (activeWallet.equals("")) {
                                        activeWallet = _credentialList.get(0).getWalletId();
                                        activePass = _credentialList.get(0).getPassphrase();

                                        App.editor.putString(Constant.WALLET_ID, _credentialList.get(0).getWalletId());
                                        App.editor.putBoolean(Constant.isWallet, true);
                                        App.editor.commit();
                                    }
                                    if(activeWallet!=null && !activeWallet.equalsIgnoreCase("")) {
                                        getWalletDetails(activeWallet, activePass);
                                    }
                                }
                                adapter.updateData(_credentialList);
                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity(getActivity());
                            } else {
                                AppConfig.showToast(object.getString("msg"));
                                setWalletDataNull();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            setWalletDataNull();
                        } catch (JsonSyntaxException e) {
                            setWalletDataNull();
                            e.printStackTrace();
                        } catch (JSONException e) {
                            setWalletDataNull();
                            e.printStackTrace();
                        }
                    } else {
                        ShowApiError(getActivity(),"server error in TransferApi/get-credentials");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    private void getCredentialsCallVlidate() {
        if (AppConfig.isInternetOn()) {
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
                                _credentialList = new ArrayList<>();
                                CredentialsResponse registerResponse = new Gson().fromJson(responseData, CredentialsResponse.class);
                                for (Credential credential : registerResponse.getCredentials()) {
                                    if (credential.getStatus().equalsIgnoreCase("active")) {
                                        _credentialList.add(credential);
                                    }
                                }

                                if (!App.pref.getBoolean(Constant.isWallet, false)) {
                                    App.editor.putString(Constant.WALLET_ID, _credentialList.get(0).getWalletId());
                                    App.editor.putBoolean(Constant.isWallet, true);
                                    App.editor.commit();
                                    String walletidvalue=App.pref.getString(Constant.WALLET_ID, "");
                                    if(walletidvalue!=null && !walletidvalue.equalsIgnoreCase(""))
                                    {
                                        App.editor.putString(Constant.Secret, _credentialList.get(0).getPassphrase());
                                        App.editor.putString(Constant.Wallet_ADD, _credentialList.get(0).getDdkcode());
                                        App.editor.commit();
                                        //                       getWalletDetails(walletidvalue, _credentialList.get(0).getPassphrase());
                                    }
                                } else {
                                    String activeWallet = "";
                                    String activePass = "";
                                    for (Credential credential : _credentialList) {
                                        if (App.pref.getString(Constant.WALLET_ID, "").equals(credential.getWalletId())) {
                                            activeWallet = credential.getWalletId();
                                            activePass = credential.getPassphrase();
                                        }
                                    }
                                    if (activeWallet.equals("")) {
                                        activeWallet = _credentialList.get(0).getWalletId();
                                        activePass = _credentialList.get(0).getPassphrase();
                                        App.editor.putString(Constant.WALLET_ID, _credentialList.get(0).getWalletId());
                                        App.editor.putBoolean(Constant.isWallet, true);
                                        App.editor.commit();
                                    }
                                    if(activeWallet!=null && !activeWallet.equalsIgnoreCase(""))
                                    {
                                       // getWalletDetails(activeWallet, activePass);
                                        App.editor.putString(Constant.Secret, _credentialList.get(0).getPassphrase());
                                        App.editor.putString(Constant.Wallet_ADD, _credentialList.get(0).getDdkcode());
                                        App.editor.commit();
                                    }
                                }
                                adapter.updateData(_credentialList);
                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity(getActivity());
                            } else {
                                AppConfig.showToast(object.getString("msg"));
                                setWalletDataNull();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            setWalletDataNull();
                        } catch (JsonSyntaxException e) {
                            setWalletDataNull();
                            e.printStackTrace();
                        } catch (JSONException e) {
                            setWalletDataNull();
                            e.printStackTrace();
                        }
                    } else {
                        ShowApiError(getActivity(),"server error in TransferApi/get-credentials");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    private void setWalletDataNull() {
        _credentialList = new ArrayList<>();
        App.editor.putBoolean(Constant.isWallet, false);
        App.editor.putString(Constant.WALLET_ID, "");
        App.editor.commit();
    }

    //
    private void showHideAll() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.findViewById(R.id.lytMain).setVisibility(View.VISIBLE);
                view.findViewById(R.id.lytAll).setVisibility(View.GONE);
                MainActivity.enableBackViews(false);
            }
        }, 1000);
    }

    //
    public static void getWalletDetails(String walletid, final String activePass) {

        if (pw != null & pw.isShowing()) {
            pw.dismiss();
        }
        UserModel.getInstance().getWalletDetails(1,walletid, mContext, new GetAvailableValue() {
            @Override
            public void getValues(String ddk, WalletResponse successResponse) {

                App.editor.putString(Constant.PUBLIC_KEY, successResponse.getWallet().getPublicKey());
                App.editor.putString(Constant.Wallet_ADD, successResponse.getWallet().getAddress());
                App.editor.putString(Constant.senderDDKAddress, successResponse.getWallet().getAddress());
                App.editor.putString(Constant.WALLET_ID, successResponse.getWallet().getWalletId());
                App.editor.putString(Constant.Secret, activePass);
                App.editor.apply();

                tvAddressCode.setText(successResponse.getWallet().getAddress());
                if (!AppConfig.isStringNullOrBlank(successResponse.getWallet().getTotalFrozenAmt())) {

                    double bal = (Double.parseDouble(successResponse.getWallet().getBalance()) - Double.parseDouble(successResponse.getWallet().getTotalFrozenAmt()));
                    bal = (bal / 100000000);
                    BigDecimal bale=new BigDecimal(bal);
                    BigDecimal roundhaldv=bale.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString()+"");
                    double stakeBal = Double.parseDouble(successResponse.getWallet().getTotalFrozenAmt());
                    stakeBal = stakeBal / 100000000;
//                                stakeTV.setText("Stake: " + String.format("%d", (int) stakeBal) + " DDK");
                } else if (successResponse.getWallet().getTotalFrozenAmt().equalsIgnoreCase("0")) {
                    double bal = (Double.parseDouble(successResponse.getWallet().getBalance()) - Double.parseDouble(successResponse.getWallet().getTotalFrozenAmt()));
                    bal = (bal / 100000000);
                    BigDecimal bale=new BigDecimal(bal);
                    BigDecimal roundhaldv=bale.setScale(8, BigDecimal.ROUND_HALF_UP);
                    tvWalletBalance.setText(roundhaldv.toPlainString()+"");
                    double stakeBal = Double.parseDouble(successResponse.getWallet().getTotalFrozenAmt());
                    stakeBal = stakeBal / 100000000;
//                                stakeTV.setText("Stake: " + String.format("%d", (int) stakeBal) + " DDK");
                } else {
                    tvWalletBalance.setText("0.0");
//                                stakeTV.setText("Stake: " + "0" + " DDK");
                }
                App.editor.putString(Constant.CurrentBalance, tvWalletBalance.getText().toString().trim());
                App.editor.apply();
            }
        });
    }

    private void openAnnouncementPopup() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());
        if (App.pref.getString("LAST_LAUNCH_DATE", "").equalsIgnoreCase(currentDate)) {

        } else {
            App.editor.putString("LAST_LAUNCH_DATE", currentDate);
            App.editor.apply();
            getTodayAnnouncement();
        }
    }

    //
//
    private void getTodayAnnouncement() {
        if (AppConfig.isInternetOn()) {
            Call<Announcement> call = AppConfig.getLoadInterface().getAppAnnouncement(AppConfig.getStringPreferences(mContext, Constant.JWTToken));
            call.enqueue(new Callback<Announcement>() {
                @Override
                public void onResponse(Call<Announcement> call, Response<Announcement> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().status == 1) {
                            if (response.body().announcementData.size() > 0) {
                                appAnnouncement(getActivity(), response.body().announcementData);
                            }
                        } else if (response.body().status == 3) {
                            AppConfig.showToast(response.body().msg);
                            AppConfig.openSplashActivity(getActivity());
                        } else {
                            AppConfig.showToast(response.body().msg);
                        }
                    } else {
                        ShowApiError(getActivity(),"server error in announcement");
                    }
                }

                @Override
                public void onFailure(Call<Announcement> call, Throwable t) {
//                    AppConfig.hideLoading(dialog);
                    t.printStackTrace();
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }


    public static void postDoNotShowAnnouncement(String announ_id) {
        if (AppConfig.isInternetOn()) {
            String userId = App.pref.getString(Constant.USER_ID, "");
            HashMap<String, String> hm = new HashMap<>();
            hm.put("announcement_id", announ_id);
            Call<ResponseBody> call = AppConfig.getLoadInterface().cancelAnnouncement(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                    } else {
                        ShowApiError(mContext,"server error in announcement-do-not-show");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    private void appAnnouncement(final Context c,
                                 final ArrayList<Announcement.AnnouncementData> announcementData) {

        final AlertDialog alertDialog;
        LayoutInflater layoutInflater = LayoutInflater.from(c);
        dialogueView = layoutInflater.inflate(R.layout.dialog_app_announcement, null);
        final MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(c);
        alertDialogBuilder.setView(dialogueView);
        alertDialog = alertDialogBuilder.create();
        RecyclerView recyclerView = dialogueView.findViewById(R.id.rvAnnouncement);
        announcementAdapter = new AnnouncementAdapter(announcementData, new AnnouncementAdapter.SetOnItemClick() {
            @Override
            public void onItemClick(int position) {
                if (announcementData.get(position).isChecked) {
                    postDoNotShowAnnouncement("" + announcementData.get(position).id);
                }
                announcementData.remove(announcementData.get(position));
                announcementAdapter.updateData(announcementData);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(announcementAdapter);

        dialogueView.findViewById((R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (announcementData.size() > 0) {
            alertDialog.show();
        }
    }

    //
    public static class ImageGetter implements Html.ImageGetter {
        public Drawable getDrawable(String source) {
            int id;
            id = mContext.getResources().getIdentifier(source, "drawable", mContext.getPackageName());
            if (id == 0) {
                // the drawable resource wasn't found in our package, maybe it is a stock android drawable?
                id = mContext.getResources().getIdentifier(source, "drawable", "android");
            }
            if (id == 0) {
                // prevent a crash if the resource still can't be found
                return null;
            } else {
                Drawable d = mContext.getResources().getDrawable(id);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                return d;
            }
        }

    }

    private void changeThePriceView(final String wallet_type)
    {
        if (wallet_type.equalsIgnoreCase("ddk"))
        {
            tvAddressCode.setText(App.pref.getString(Constant.Wallet_ADD, ""));
            String currentvalue=App.pref.getString(Constant.CurrentBalance, "");
            if(currentvalue==null || currentvalue.equalsIgnoreCase(null) || currentvalue.equalsIgnoreCase("null"))
            {
                tvWalletBalance.setText("0.000000");
            }else
            {
                if(App.pref.getString(Constant.CurrentBalance, "").length()!=0)
                {
                    BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.CurrentBalance, ""));
                    BigDecimal roundhaldv = currentbalance.setScale(8, BigDecimal.ROUND_FLOOR);
                    tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                }
            }
            tvBuyPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().ddkBuyPrice));
            tvSellPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().ddkSellPrice));
            AppConfig.hideLoader();

        } else
        {
            if (wallet_type.equalsIgnoreCase("php"))
            {
                String curkj=App.pref.getString(Constant.PHP_Balance, "");
                if(curkj!=null && !curkj.equalsIgnoreCase(null) && !App.pref.getString(Constant.PHP_Balance, "").toString().equalsIgnoreCase(""))
                {
                    if(App.pref.getString(Constant.PHP_Balance, "").length()!=0) {
                        BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.PHP_Balance, ""));
                        BigDecimal roundhaldv = currentbalance.setScale(4, BigDecimal.ROUND_FLOOR);
                        tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    }
                }else
                {
                    tvWalletBalance.setText("0.0000");
                }

            }else
            if (wallet_type.equalsIgnoreCase("btc"))
            {
                tvAddressCode.setText(App.pref.getString(Constant.BTC_ADD, ""));
                tvBuyPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().btcBuyPrice));
                tvSellPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().btcSellPrice));
                String curkj=App.pref.getString(Constant.BTC_Balance, "");
                if(curkj!=null && !curkj.equalsIgnoreCase(null) && !App.pref.getString(Constant.BTC_Balance, "").toString().equalsIgnoreCase(""))
                {
                    if(App.pref.getString(Constant.BTC_Balance, "").length()!=0) {
                        BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.BTC_Balance, ""));
                        BigDecimal roundhaldv = currentbalance.setScale(8, BigDecimal.ROUND_FLOOR);
                        tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    }
                }else
                {
                    tvWalletBalance.setText("0.00000000");
                }

            }else if (wallet_type.equalsIgnoreCase("tron"))
            {
                tvAddressCode.setText(App.pref.getString(Constant.tron_ADD, ""));
                tvBuyPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().tronBuyPrice));
                tvSellPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().tronSellPrice));
                String curkj=App.pref.getString(Constant.tron_Balance, "");
                if(curkj!=null && !curkj.equalsIgnoreCase(null) && !App.pref.getString(Constant.tron_Balance, "").toString().equalsIgnoreCase(""))
                {
                    if(App.pref.getString(Constant.tron_Balance, "").length()!=0)
                    {
                        BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.tron_Balance, ""));
                        BigDecimal roundhaldv = currentbalance.setScale(8, BigDecimal.ROUND_FLOOR);
                        tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    }
                }else
                {
                    tvWalletBalance.setText("0.00000000");
                }

            } else if (wallet_type.equalsIgnoreCase("eth"))
            {
                tvAddressCode.setText(App.pref.getString(Constant.Eth_ADD, ""));
                tvBuyPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().ethBuyPrice));
                tvSellPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().ethSellPrice));
                String currentvalue=App.pref.getString(Constant.Eth_Balance, "");
                if(currentvalue==null || currentvalue.equalsIgnoreCase(null) || currentvalue.equalsIgnoreCase("null"))
                {
                    tvWalletBalance.setText("0.000000");
                }else
                {
                    if(App.pref.getString(Constant.Eth_Balance, "").length()!=0)
                    {
                        BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.Eth_Balance, ""));
                        BigDecimal roundhaldv = currentbalance.setScale(8, BigDecimal.ROUND_FLOOR);
                        tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    }
                }
            } else if (wallet_type.equalsIgnoreCase("usdt"))
            {
                tvAddressCode.setText(App.pref.getString(Constant.USDT_ADD, ""));
                tvBuyPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().usdtBuyPrice));
                tvSellPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().usdtSellPrice));
                String currentvalue=App.pref.getString(Constant.USDT_Balance, "");
                if(currentvalue==null || currentvalue.equalsIgnoreCase(null) || currentvalue.equalsIgnoreCase("null"))
                {
                    tvWalletBalance.setText("0.000000");
                }else
                {
                    if(App.pref.getString(Constant.USDT_Balance, "").length()!=0) {

                        BigDecimal currentbalance = new BigDecimal(App.pref.getString(Constant.USDT_Balance, ""));
                        BigDecimal roundhaldv = currentbalance.setScale(8, BigDecimal.ROUND_FLOOR);
                        tvWalletBalance.setText(roundhaldv.toPlainString() + "");
                    }
                }

            }else if (wallet_type.equalsIgnoreCase("sam"))
            {
                otherwalletlayout.setVisibility(View.GONE);
                newsamlayout.setVisibility(View.VISIBLE);

            }else if (wallet_type.equalsIgnoreCase("samkoin"))
            {
                String koinadd=App.pref.getString(Constant.SAMKOIN_ADD, "");
                tvAddressCode.setText(App.pref.getString(Constant.SAMKOIN_ADD, ""));
                tvBuyPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().samkoinBuyPrice));
                tvSellPrice.setText("₮ " + String.format("%.6f", UserModel.getInstance().samkoinSellPrice));
                String samkoinvalue=App.pref.getString(Constant.SAMKOIN_Balance, "").trim();
                BigDecimal currentbalance;
                BigDecimal roundhaldv = null;
                if(samkoinvalue!=null || !samkoinvalue.equalsIgnoreCase(""))
                {
                    currentbalance = new BigDecimal(samkoinvalue);
                    roundhaldv = currentbalance.setScale(4, BigDecimal.ROUND_FLOOR);
                }
                tvWalletBalance.setText(roundhaldv+"");
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppConfig.hideLoader();
                }
            }, 15000);
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

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (wallet_type.equalsIgnoreCase("ddk"))
            {
                changeThePriceView("ddk");
            }else if (wallet_type.equalsIgnoreCase("btc"))
            {
                changeThePriceView("btc");

            } else if (wallet_type.equalsIgnoreCase("tron"))
            {
                changeThePriceView("tron");

            }else if (wallet_type.equalsIgnoreCase("eth"))
            {
                changeThePriceView("eth");
            } else if (wallet_type.equalsIgnoreCase("usdt"))
            {
                changeThePriceView("usdt");
            }else if (wallet_type.equalsIgnoreCase("samkoin"))
            {
                changeThePriceView("samkoin");
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(broadcastReceiver, new IntentFilter("updatePrice"));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(broadcastReceiver);
    }
}
