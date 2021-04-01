package com.ddkcommunity.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.App;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.interfaces.GegtSettingStatusinterface;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.model.getSettingModel;
import com.ddkcommunity.utilies.AppConfig;
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity;
import com.github.omadahealth.lollipin.lib.managers.LockManager;

import java.math.BigDecimal;

import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.getSettingServerDataSt;

public class CustomPinActivity extends AppLockActivity {

    @Override
     public void showForgotDialog()
    {
         Resources res = getResources();
        getSettingServerDataSt(CustomPinActivity.this,"php");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
         alertDialogBuilder
                 .setMessage(res.getString(R.string.activity_dialog_content))
                 .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {

                 App.editor.clear();
                 App.editor.apply();

                 LockManager lockManager = LockManager.getInstance();
                 lockManager.getAppLock().setPasscode(null);

                 Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 intent.putExtra("EXIT", true);
                 startActivity(intent);

             }
         }).setNegativeButton("Cancel" ,new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
             }
         }).setCancelable(false).show();
         // Create the builder with required paramaters - Context, Title, Positive Text
     }
    @Override
    public void onPinFailure(int attempts) {
    }

    @Override
    public void onPinSuccess(int attempts) {

    }

    @Override
    public int getPinLength() {
        return super.getPinLength();//you can override this method to change the pin length from the default 4
    }
}
