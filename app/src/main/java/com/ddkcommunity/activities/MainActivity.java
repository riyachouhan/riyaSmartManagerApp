package com.ddkcommunity.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ddkcommunity.App;
import com.ddkcommunity.BuildConfig;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.fragment.ContactUsFragemnt;
import com.ddkcommunity.fragment.GogoleAuthFragment;
import com.ddkcommunity.fragment.GogolePasswordFragment;
import com.ddkcommunity.fragment.HomeFragment;
import com.ddkcommunity.fragment.ProfileFragment;
import com.ddkcommunity.fragment.ScanFragment;
import com.ddkcommunity.fragment.credential.CredentialsFragment;
import com.ddkcommunity.fragment.projects.MapOtionAllFragment;
import com.ddkcommunity.fragment.projects.MapreferralFragment;
import com.ddkcommunity.fragment.projects.TermsAndConsitionSubscription;
import com.ddkcommunity.fragment.send.SendFragment;
import com.ddkcommunity.interfaces.GegtSettingStatusinterface;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.interfaces.GetUserProfile;
import com.ddkcommunity.model.checkRefferalModel;
import com.ddkcommunity.model.getSettingModel;
import com.ddkcommunity.model.user.User;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.github.nkzawa.emitter.Emitter;
import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.ShowFunctionalityAlert;
import static com.ddkcommunity.utilies.dataPutMethods.getSettingServerDataSt;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static int scanFragement=0,updateTabview=0;
    public static final int REQUEST_CODE_ENABLE = 11;
    public static final int REQUEST_CODE_DISABLE = 12;
    public static Activity activity;
    public static TextView titleText;
    private static ActionBar actionBar;
    private static FragmentManager fm;
    boolean doubleBackToExitPressedOnce = false;
    private static TextView title, designation, mobile,footer_version;
    private static CircleImageView imageView;
    private static ActionBarDrawerToggle mDrawerToggle;
    private static boolean mToolBarNavigationListenerIsRegistered = false;
    private static DrawerLayout drawer;
    private Context mContext;
    public static boolean isLocalManualTrans = false;
    private BigDecimal usd;
    private BigDecimal ethPrice, btcPrice, usdtPrice;
    public static String ClickViewButton="profile";
    public static ImageView scanview;
    public static BottomNavigationView bottomNavigation;

    public static void addFragment(Fragment fragment, boolean addToBackStack)
    {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment, "");
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    UserResponse userData;
    public static int mapcreditcard=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        Toolbar toolbar = findViewById(R.id.toolbar);
        titleText = findViewById(R.id.title);
        scanview=findViewById(R.id.scanview);
        String versionName = BuildConfig.VERSION_NAME;
        footer_version=findViewById(R.id.footer_version);
        footer_version.setText("Version "+versionName);
        userData = AppConfig.getUserData(this);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        drawer = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        title = view.findViewById(R.id.name_TV);
        mobile = view.findViewById(R.id.phone_TV);
        designation = view.findViewById(R.id.designation_TV);
        imageView = view.findViewById(R.id.imageView);
        getSettingServerDataSt(MainActivity.this,"php");
        findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
//                addFragment(ComingSoonFragment.getInstance("Notifications"), true);
            }
        });
        activity = this;
        fm = getSupportFragmentManager();
        addFragment(new HomeFragment(), false);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new com.google.android.gms.tasks.OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                try {
                    App.RegEditor.putString(Constant.FIREBASE_TOKEN, newToken);
                    App.RegEditor.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        App.getInstance().getSocket().on("ddk_usdt", onDdkUsdtValue);
        App.getInstance().getSocket().on("eth_btc_usdt", onETHBTCValue);
        App.getInstance().getSocket().on("percentage", onPercentage);
        App.getInstance().getSocket().connect();
        latestDDKPrice();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ddkcommunity",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        scanview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainActivity.addFragment(new ScanFragment(), true);
            }
        });
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener()
            {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId())
                    {
                        case R.id.navigation_home:
                            MainActivity.addFragment(new HomeFragment(), true);
                            return true;
                        case R.id.navigation_map:
                            getSettingServerOnTab(MainActivity.this,"send_map");
                             return true;

                        case R.id.navigation_shop:
                            return true;

                        case R.id.navigation_distribution:
                            return true;

                        case R.id.navigation_wallet:
                           // MainActivity.addFragment(new ProfileFragment(), true);
                            return true;

                    }
                    return false;
                }
            };

    @Override
    protected void onResume() {
        super.onResume();
        UserModel.getInstance().getProfileCall(mContext, new GetUserProfile() {
            @Override
            public void getUserInfo(UserResponse userResponse) {
                setUserDetail(userResponse.getUser());
            }
        });
    }

    private void CheckUserActiveStaus(final ProgressDialog dialog)
    {
        String emailid=userData.getUser().getEmail();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("email", emailid);
        AppConfig.getLoadInterfaceMap().CheckUserActive(hm).enqueue(new Callback<checkRefferalModel>() {
            @Override
            public void onResponse(Call<checkRefferalModel> call, Response<checkRefferalModel> response) {
                try
                {
                    AppConfig.hideLoading(dialog);
                    Log.d("sam erro par invi",response.body().toString());
                    if (response.isSuccessful() && response.body() != null)
                    {
                        if (response.body().getSubscription_status().equalsIgnoreCase("true"))
                        {
                            Fragment fragment = new MapOtionAllFragment();
                            Bundle arg = new Bundle();
                            arg.putString("action", "map");
                            arg.putString("activeStatus", "1");
                            fragment.setArguments(arg);
                            MainActivity.addFragment(fragment,true);

                        }else
                        {
                            if (response.body().getRegister_status().equalsIgnoreCase("true"))
                            {
                                Fragment fragment = new MapOtionAllFragment();
                                Bundle arg = new Bundle();
                                arg.putString("action", "map");
                                arg.putString("activeStatus", "0");
                                fragment.setArguments(arg);
                                MainActivity.addFragment(fragment,true);
                            }else
                            {
                                Fragment fragment = new TermsAndConsitionSubscription();
                                Bundle arg = new Bundle();
                                arg.putString("activityaction", "map");
                                fragment.setArguments(arg);
                                MainActivity.addFragment(fragment,true);
                            }
                        }
                    } else {
                        ShowApiError(MainActivity.this,"server error check-mail-exist");
                    }

                } catch (Exception e)
                {
                    AppConfig.hideLoading(dialog);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<checkRefferalModel> call, Throwable t)
            {
                AppConfig.hideLoading(dialog);
                Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void setTitle(String title)
    {
        titleText.setText(title);
    }

    public void getSettingServerOnTab(Activity activity, final String functionname)
    {
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Please wait ....");
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
                        CheckUserActiveStaus(dialog);
                    } else
                    {
                        AppConfig.hideLoading(dialog);
                        ShowFunctionalityAlert(MainActivity.this, response.body().getMsg());
                    }

                } catch (Exception e)
                {
                    AppConfig.hideLoading(dialog);
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        App.getInstance().getSocket().disconnect();
        App.getInstance().getSocket().off("ddk_usdt", onDdkUsdtValue);
        App.getInstance().getSocket().off("eth_btc_usdt", onETHBTCValue);
        App.getInstance().getSocket().off("percentage", onPercentage);
//        App.getInstance().getSocket().off(Socket.EVENT_CONNECT, onConnect);
//        App.getInstance().getSocket().off(Socket.EVENT_DISCONNECT, onDisconnect);
//        App.getInstance().getSocket().off(Socket.EVENT_CONNECT_ERROR, onConnectError);
//        App.getInstance().getSocket().off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
    }

    public static void setUserDetail(User user) {
        title.setText(user.getName());
        if (user.getDesignation() != null) {
            designation.setText(user.getDesignation());
        }
        if (user.getMobile() != null) {
            mobile.setText("+" + user.getPhoneCode() + user.getMobile());
        }

        if(user.getUserImage()!=null && !user.getUserImage().equalsIgnoreCase(null) && !user.getUserImage().equalsIgnoreCase(""))
        {
            Glide.with(activity)
                    .asBitmap()
                    .load(user.getUserImage())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            imageView.setImageResource(R.drawable.default_photo);
                        }

                    });
        }
    }

    @Override
    public void onBackPressed()
    {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack();
            bottomNavigation.getMenu().getItem(0).setChecked(true);
           // MainActivity.bottomNavigation.setSelectedItemId(R.id.navigation_home);

        } else if (HomeFragment.lytAll.getVisibility() == View.VISIBLE)
        {
            HomeFragment.lytAll.setVisibility(View.GONE);
            HomeFragment.lytMain.setVisibility(View.VISIBLE);
            enableBackViews(false);
        } else if (!doubleBackToExitPressedOnce)
        {
            this.doubleBackToExitPressedOnce = true;
            AppConfig.showToast("Please click BACK again to exit.");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        } else
        {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    public static void enableBackViews(boolean enable) {
        if (enable) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);
            actionBar.setDisplayHomeAsUpEnabled(false);

            mDrawerToggle.setDrawerIndicatorEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);

            if (!mToolBarNavigationListenerIsRegistered) {
                mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Doesn't have to be onBackPressed
                        activity.onBackPressed();
                    }
                });
                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu);

            mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            });
            mToolBarNavigationListenerIsRegistered = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.contact_set:
                MainActivity.addFragment(new ContactUsFragemnt(), true);
                break;

            case R.id.account_settings:
                String googlevaue=App.pref.getString(Constant.GOOGLEAUTHOPTIONSTATUS, "");
                if(googlevaue.equalsIgnoreCase("1"))
                {
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
            case R.id.credentials:
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
            case R.id.stake:
//                    startActivity(new Intent(getApplicationContext(), StakeActivity.class));
                break;
            case R.id.send:
                MainActivity.addFragment(new SendFragment(), true);
                break;
            case R.id.receive:
                startActivity(new Intent(getApplicationContext(), ReceivedActivity.class));
                break;
            case R.id.send_message:
//                MainActivity.addFragment(ComingSoonFragment.getInstance("Send us a message"), true);
                break;
            case R.id.logout:
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this);
                alertDialogBuilder.setTitle("Logout").setMessage("Are you sure want to Logout?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

//                                App.editor.clear();
//                                App.editor.apply();

                                LockManager lockManager = LockManager.getInstance();
                                lockManager.getAppLock().setPasscode(null);
                                AppConfig.openSplashActivity(activity);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case MainActivity.REQUEST_CODE_ENABLE:
                App.editor.putBoolean(AppConfig.isPin, true);
                App.editor.commit();
                String googlevaue11=App.pref.getString(Constant.GOOGLEAUTHOPTIONSTATUS, "");
                if(googlevaue11.equalsIgnoreCase("1"))
                {
                    ClickViewButton="crediential";
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
                    String googlestatus=App.pref.getString(Constant.GOOGLEAUThPendingRegit, "");
                    ClickViewButton="crediential";
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
        }
    }

    private Emitter.Listener onDdkUsdtValue = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String ddkPrice = data.getString("message");
                        usd = new BigDecimal(ddkPrice);

                        updateDDkPrice();
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private void updateDDkPrice() {
        BigDecimal ONE_HUNDRED = new BigDecimal(100);
        if (UserModel.getInstance().ddkBuyPercentage != null) {
            BigDecimal buyPer = usd.multiply(UserModel.getInstance().ddkBuyPercentage).divide(ONE_HUNDRED);
            BigDecimal sellPer = usd.multiply(UserModel.getInstance().ddkSellPercentage).divide(ONE_HUNDRED);

            UserModel.getInstance().ddkBuyPrice = buyPer.add(usd);
            UserModel.getInstance().ddkSellPrice = usd.subtract(sellPer);

            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("updatePrice"));
        }
    }

    private Emitter.Listener onETHBTCValue = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String eth_val = data.getString("eth_val");
                        String btc_val = data.getString("btc_val");
                        String usdt_val = data.getString("usdt_val");

                        ethPrice = new BigDecimal(eth_val);
                        btcPrice = new BigDecimal(btc_val);
                        usdtPrice = new BigDecimal(usdt_val);
                        updateEthUsdtBtcPrice();

                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private void updateEthUsdtBtcPrice() {
        BigDecimal ONE_HUNDRED = new BigDecimal(100);

        if (UserModel.getInstance().ethBuyPercentage != null)
        {
            BigDecimal ethBuyPer = ethPrice.multiply(UserModel.getInstance().ethBuyPercentage).divide(ONE_HUNDRED);
            BigDecimal ethSellPer = ethPrice.multiply(UserModel.getInstance().ethSellPercentage).divide(ONE_HUNDRED);
            UserModel.getInstance().ethBuyPrice = ethBuyPer.add(ethPrice);
            UserModel.getInstance().ethSellPrice = ethPrice.subtract(ethSellPer);

            BigDecimal btcBuyPer = btcPrice.multiply(UserModel.getInstance().btcBuyPercentage).divide(ONE_HUNDRED);
            BigDecimal btcSellPer = btcPrice.multiply(UserModel.getInstance().btcSellPercentage).divide(ONE_HUNDRED);
            UserModel.getInstance().btcBuyPrice = btcBuyPer.add(btcPrice);
            UserModel.getInstance().btcSellPrice = btcPrice.subtract(btcSellPer);

            BigDecimal usdtBuyPer = usdtPrice.multiply(UserModel.getInstance().usdtBuyPercentage).divide(ONE_HUNDRED);
            BigDecimal usdtSellPer = usdtPrice.multiply(UserModel.getInstance().usdtSellPercentage).divide(ONE_HUNDRED);
            UserModel.getInstance().usdtBuyPrice = usdtBuyPer.add(usdtPrice);
            UserModel.getInstance().usdtSellPrice = usdtPrice.subtract(usdtSellPer);

        }
    }


    private Emitter.Listener onPercentage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {

                        String buyPer1 = data.getString("ddkBuyPer");
                        String sellPer1 = data.getString("ddkSellPer");

                        UserModel.getInstance().ddkBuyPercentage = new BigDecimal(buyPer1);
                        UserModel.getInstance().ddkSellPercentage = new BigDecimal(sellPer1);

                        String ethBuyPer1 = data.getString("ethBuyPer");
                        String ethSellPer1 = data.getString("ethSellPer");
                        String btcBuyPer1 = data.getString("btcBuyPer");
                        String btcSellPer1 = data.getString("btcSellPer");
                        String usdtBuyPer1 = data.getString("usdtBuyPer");
                        String usdtSellPer1 = data.getString("usdtSellPer");

                        UserModel.getInstance().ethBuyPercentage = new BigDecimal(ethBuyPer1);
                        UserModel.getInstance().ethSellPercentage = new BigDecimal(ethSellPer1);
                        UserModel.getInstance().btcBuyPercentage = new BigDecimal(btcBuyPer1);
                        UserModel.getInstance().btcSellPercentage = new BigDecimal(btcSellPer1);
                        UserModel.getInstance().usdtBuyPercentage = new BigDecimal(usdtBuyPer1);
                        UserModel.getInstance().usdtSellPercentage = new BigDecimal(usdtSellPer1);

                        if (usdtPrice != null) {
                            updateEthUsdtBtcPrice();
                        }
                        if (usd!=null){
                            updateDDkPrice();
                        }
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("Socket===>", "onConnected");
//                    if(!isConnected) {
//                        if(null!=mUsername)
//                            mSocket.emit("add user", mUsername);
//                        Toast.makeText(getActivity().getApplicationContext(),
//                                R.string.connect, Toast.LENGTH_LONG).show();
//                        isConnected = true;
//                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("Socket===>", "diconnected");
//                    isConnected = false;
//                    Toast.makeText(getActivity().getApplicationContext(),
//                            R.string.disconnect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("Socket===>", "Error connecting");
//                    Toast.makeText(this,
//                            R.string.error_connect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private void latestDDKPrice() {
//        final ProgressDialog dialog = new ProgressDialog(mContext);
//        AppConfig.showLoading(dialog, "Fetching USDT Rate..");
        UserModel.getInstance().getUSDCall(new GetUSDAndBTCCallback() {
            @Override
            public void getValues(BigDecimal btc, BigDecimal usd) {
//                AppConfig.hideLoading(dialog);
                if (usd != null) {
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
        },MainActivity.this);
//
    }

}
