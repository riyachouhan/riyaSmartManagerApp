package com.ddkcommunity;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import androidx.multidex.MultiDexApplication;

import com.ddkcommunity.activities.CustomPinActivity;
import com.ddkcommunity.utilies.TopExceptionHandler;
import com.facebook.FacebookSdk;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class App extends MultiDexApplication {
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    public static SharedPreferences RegPref;
    public static SharedPreferences.Editor RegEditor;
    private static App instance;

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://157.245.52.206:7000");
        } catch (URISyntaxException e) {
        }
    }
    public Socket getSocket() {
        return mSocket;
    }
    public static App getInstance() {
        return instance;
    }

    public static void setInstance(App instance) {
        App.instance = instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        setInstance(this);
        UserModel.initInstance(this);
        // printHashKey();
        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, CustomPinActivity.class);
        lockManager.getAppLock().setLogoId(R.drawable.ic_lock);
        pref = this.getSharedPreferences(Constant.APP_NAME, MODE_PRIVATE);
        editor = pref.edit();

        RegPref = this.getSharedPreferences(Constant.REG_NAME, MODE_PRIVATE);
        RegEditor = RegPref.edit();

//        mSocket.connect();

        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));

    }

    public void printHashKey() {

        try {

            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ddkcommunity",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
