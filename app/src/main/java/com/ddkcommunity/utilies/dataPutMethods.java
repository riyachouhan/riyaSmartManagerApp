package com.ddkcommunity.utilies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.CountryListAdapter;
import com.ddkcommunity.adapters.CredentialListAdapter;
import com.ddkcommunity.adapters.OurTeamAdapter;
import com.ddkcommunity.adapters.RedeemChoiceAdpater;
import com.ddkcommunity.adapters.cryptoListAdapter;
import com.ddkcommunity.adapters.tronListAdapter;
import com.ddkcommunity.adapters.veirifcationListAdapter;
import com.ddkcommunity.fragment.CashOutFragmentNew;
import com.ddkcommunity.fragment.OurTeamProfileFragment;
import com.ddkcommunity.fragment.ProfileFragment;
import com.ddkcommunity.fragment.SAMPDFragment;
import com.ddkcommunity.fragment.buy.PaymentFragment;
import com.ddkcommunity.fragment.send.QrScanFragment;
import com.ddkcommunity.fragment.send.QrScanFragmentScan;
import com.ddkcommunity.interfaces.GegtSettingStatusinterface;
import com.ddkcommunity.interfaces.GetAvailableValue;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.model.Country;
import com.ddkcommunity.model.OtpResponse;
import com.ddkcommunity.model.RedeemOptionModel;
import com.ddkcommunity.model.UserBankList;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.getSettingModel;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.model.verifcationFundSource;
import com.ddkcommunity.model.wallet.WalletResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.ddkcommunity.Constant.SLIDERIMG;
import static com.ddkcommunity.fragment.CashOutFragmentNew.banknotabv;
import static com.ddkcommunity.fragment.CashOutFragmentNew.msgbank;
import static com.ddkcommunity.fragment.HomeFragment.userselctionopt;

public class dataPutMethods
{

    public static void puttronWallet(String tronpublickey,String tronadd,String tronaddid,String tronsecret) {
        App.editor.putString(Constant.tron_ADD,tronadd);
        App.editor.putString(Constant.tron_ADD_ID,tronaddid);
        App.editor.putString(Constant.tron_Secret,tronsecret);
         App.editor.putString(Constant.tron_publickey, tronpublickey);
        App.editor.apply();
    }

    public static void putOtherWallet(String publickey,String ethsecratekeyenc,String ethadd,String ethaddid,String ethsecret) {
        App.editor.putString(Constant.Eth_ADD,ethadd);
        App.editor.putString(Constant.Eth_ADD_ID,ethaddid);
        App.editor.putString(Constant.Eth_Secret,ethsecret);
        App.editor.putString(Constant.Eth_Secret_key,ethsecratekeyenc);
        App.editor.putString(Constant.Eth_publickey, publickey);
        App.editor.apply();
    }

    public static void putUsdtWallet(String publickey,String usdtadd,String usdtaddid,String usdtsecret) {
        App.editor.putString(Constant.USDT_ADD,usdtadd);
        App.editor.putString(Constant.USDT_Add_Id,usdtaddid);
        App.editor.putString(Constant.USDT_Secaret,usdtsecret);
        App.editor.putString(Constant.USDT_publickey, publickey);
        App.editor.apply();
    }

    public static void putSamKoinWallet(String publickey,String samkoinadd,String samkoinaddid,String samkoinsecret)
    {
        App.editor.putString(Constant.SAMKOIN_publickey, publickey);
        App.editor.putString(Constant.SAMKOIN_ADD,samkoinadd);
        App.editor.putString(Constant.SAMKOIN_Add_Id,samkoinaddid);
        App.editor.putString(Constant.SAMKOIN_Secaret,samkoinsecret);
        App.editor.apply();
    }

    public static void generateEncyTab()
    {
        try {
// User types in their password: /
            String pText = "#!thsam$$aa";
            //for other
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[8];
            random.nextBytes(salt);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec1 = new PBEKeySpec(pText.toCharArray(), salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec1);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher1 = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher1.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params1 = cipher1.getParameters();
            byte[] iv = params1.getParameterSpec(IvParameterSpec.class).getIV();

            Log.d("keydemo other", "salt (hex) " + CryptoUtils.hex(salt));
            //Log.d("keydemo other","IV (hex) "+CryptoUtils.hex(iv));
            Log.d("keydemo other", "key space " + CryptoUtils.hex(secret.getEncoded()));
            //.........
            putENCYDataView(CryptoUtils.hex(salt)+"",CryptoUtils.hex(secret.getEncoded())+"");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void putGoogleAuthStatus(String GOOGLEAUThPendingRegit,String authstatus,String authsecrategoogle)
    {
        try {
            if (authsecrategoogle == null || authsecrategoogle.equalsIgnoreCase(null) || authsecrategoogle.equalsIgnoreCase("null")) {
                App.editor.putString(Constant.GOOGLEAUTHSECRATE, " ");
            } else {
                App.editor.putString(Constant.GOOGLEAUTHSECRATE, "" + authsecrategoogle);
            }
            if (GOOGLEAUThPendingRegit == null || GOOGLEAUThPendingRegit.equalsIgnoreCase(null) || GOOGLEAUThPendingRegit.equalsIgnoreCase("null")) {
                App.editor.putString(Constant.GOOGLEAUThPendingRegit, "pending");
            } else {
                App.editor.putString(Constant.GOOGLEAUThPendingRegit, "" + GOOGLEAUThPendingRegit);
            }
            App.editor.putString(Constant.GOOGLEAUTHOPTIONSTATUS, "" + authstatus);
            App.editor.apply();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void putENCYDataView(String salt,String key)
    {
            App.editor.putString(Constant.IVPARAM,""+salt);
            App.editor.putString(Constant.KEYENCYPARAM,""+key);
            App.editor.apply();
    }

    public static void putBTCWallet(String publickey,String btcadd,String btcaddid,String btcsecret)
    {
        App.editor.putString(Constant.BTC_publickey, publickey);
        App.editor.putString(Constant.BTC_ADD,btcadd);
        App.editor.putString(Constant.BTC_Add_Id,btcaddid);
        App.editor.putString(Constant.BTC_Secaret,btcsecret);
        App.editor.apply();
    }

    public static void putKeyDetails(String mapAllRunUrl,String MApURl,String stripekeyvalue)
    {
        App.editor.putString(Constant.MApURl,MApURl);
        App.editor.putString(Constant.MApURllive,mapAllRunUrl);
        App.editor.putString(Constant.Strip_Payment_Key,stripekeyvalue);
        App.editor.apply();
    }

    public static void setVerificationdata(RelativeLayout email_layout,RelativeLayout mobile_layout,RelativeLayout identity_layout,RelativeLayout fund_layout, RelativeLayout address_layout,RelativeLayout video_layout,
                                           String  emailverifcation, String moibleverifcation, String id_proof_verification, String fund_source_verification, String address_verification, String video_verification)
    {
        if(emailverifcation.equalsIgnoreCase("yes"))
        {
            email_layout.setVisibility(View.VISIBLE);
        }else
        {
            email_layout.setVisibility(View.GONE);
        }

        if(moibleverifcation.equalsIgnoreCase("yes"))
        {
            mobile_layout.setVisibility(View.VISIBLE);
        }else
        {
            mobile_layout.setVisibility(View.GONE);
        }

        if(fund_source_verification.equalsIgnoreCase("yes"))
        {
            fund_layout.setVisibility(View.VISIBLE);
        }else
        {
            fund_layout.setVisibility(View.GONE);
        }

        if(id_proof_verification.equalsIgnoreCase("yes"))
        {
            identity_layout.setVisibility(View.VISIBLE);
        }else {
            identity_layout.setVisibility(View.GONE);
        }

        if(address_verification.equalsIgnoreCase("yes"))
        {
            address_layout.setVisibility(View.VISIBLE);
        }else
        {
            address_layout.setVisibility(View.GONE);
        }

        if(video_verification.equalsIgnoreCase("yes"))
        {
            video_layout.setVisibility(View.VISIBLE);
        }else
        {
            video_layout.setVisibility(View.GONE);
        }
    }

    public static void putUserVerification(String emailverifcation,String moibleverifcation,String id_proof_1_verification_status,String id_proof_2_verification_status,String fund_source_verification_status,String address_verification_status,String single_video_verification_status)
    {
        int verifidedstatu=0,videoverifdiedstatus=0;
        if(id_proof_1_verification_status.equalsIgnoreCase("yes"))
        {
            verifidedstatu=verifidedstatu+5;
        }else
        {
            verifidedstatu=0;
        }

        if(id_proof_2_verification_status.equalsIgnoreCase("yes"))
        {
            verifidedstatu=verifidedstatu+5;
        }else
        {
            verifidedstatu=0;
        }

        if(verifidedstatu==10)
        {
            App.editor.putString(Constant.IDENTITY_VERIFIED_STATUS,"yes");
        }else
        {
            if(id_proof_2_verification_status.equalsIgnoreCase("in_review") || id_proof_1_verification_status.equalsIgnoreCase("in_review"))
            {
                App.editor.putString(Constant.IDENTITY_VERIFIED_STATUS,"in_review");
            }else if(id_proof_2_verification_status.equalsIgnoreCase("rejected") || id_proof_1_verification_status.equalsIgnoreCase("rejected"))
            {
                App.editor.putString(Constant.IDENTITY_VERIFIED_STATUS,"rejected");
            }else if(id_proof_2_verification_status.equalsIgnoreCase("not") || id_proof_1_verification_status.equalsIgnoreCase("not"))
            {
                App.editor.putString(Constant.IDENTITY_VERIFIED_STATUS,"no");
            }else if(id_proof_2_verification_status.equalsIgnoreCase("no") || id_proof_1_verification_status.equalsIgnoreCase("no"))
            {
              App.editor.putString(Constant.IDENTITY_VERIFIED_STATUS,"no");
            }else if(id_proof_2_verification_status.equalsIgnoreCase("pending") || id_proof_1_verification_status.equalsIgnoreCase("pending"))
            {
                App.editor.putString(Constant.IDENTITY_VERIFIED_STATUS,"pending");
            }else if(id_proof_2_verification_status.equalsIgnoreCase("yes") && id_proof_1_verification_status.equalsIgnoreCase("yes"))
            {
                App.editor.putString(Constant.IDENTITY_VERIFIED_STATUS,"yes");
            }else if(id_proof_2_verification_status.equalsIgnoreCase("verified") && id_proof_1_verification_status.equalsIgnoreCase("verified"))
            {
                App.editor.putString(Constant.IDENTITY_VERIFIED_STATUS,"yes");
            }
                App.editor.putString(Constant.IDENTITY_VERIFIED_STATUS_COUNT,verifidedstatu+"");
        }
        App.editor.putString(Constant.EMAIL_VERIFIED_STATUS,emailverifcation);
        App.editor.putString(Constant.MOBILE_VERIFIED_STATUS,moibleverifcation);
        App.editor.putString(Constant.FUND_VERIFIED_STATUS,fund_source_verification_status);
        App.editor.putString(Constant.ADDRESS_VERIFIED_STATUS,address_verification_status);
        if(single_video_verification_status.equalsIgnoreCase("yes"))
        {
            videoverifdiedstatus=30;
        }

        if(videoverifdiedstatus==30)
        {
            App.editor.putString(Constant.VIDEO_VERIFIED_STATUS,"yes");
        }else
        {
            if(single_video_verification_status.equalsIgnoreCase("in_review"))
            {
                App.editor.putString(Constant.VIDEO_VERIFIED_STATUS,"in_review");
            }else if(single_video_verification_status.equalsIgnoreCase("rejected"))
            {
                App.editor.putString(Constant.VIDEO_VERIFIED_STATUS,"rejected");
            }else if(single_video_verification_status.equalsIgnoreCase("not"))
            {
                App.editor.putString(Constant.VIDEO_VERIFIED_STATUS,"no");
            }else if(single_video_verification_status.equalsIgnoreCase("no")|| single_video_verification_status.equalsIgnoreCase("pending"))
            {
                App.editor.putString(Constant.VIDEO_VERIFIED_STATUS,single_video_verification_status);
            }else if(single_video_verification_status.equalsIgnoreCase("yes")
                    || single_video_verification_status.equalsIgnoreCase("verified")){
                App.editor.putString(Constant.VIDEO_VERIFIED_STATUS,"yes");
            }
            App.editor.putString(Constant.VIDEO_VERIFIED_STATUS_COUNT,videoverifdiedstatus+"");
        }
        App.editor.apply();
    }

    public static void ShowApiError(Context activity,String apierroname)
    {
        //api_error
        Log.d("context","::"+activity+"::err::"+apierroname);
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.alert_dialog_error, null);
        TextView btnGoHome =customView.findViewById(R.id.btnGoHome);
        TextView apierrorname=customView.findViewById(R.id.apierrorname);
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(customView);
        apierrorname.setText(apierroname);
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // getActivity().finish();
                dialog.dismiss();
            }
        });
    }

    //fundtinalituy
    public static void ShowFunctionalityAlert(Context activity,String msg)
    {
//api_error
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.alert_dialog_error, null);
        TextView btnGoHome =customView.findViewById(R.id.btnGoHome);
        TextView tvOrderStatus=customView.findViewById(R.id.tvOrderStatus);
        tvOrderStatus.setText(msg);
        TextView titile=customView.findViewById(R.id.titile);
        titile.setText("Alert");
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(customView);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCancelable(false);
        btnGoHome.setText("Ok");
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
    }

    public static void ShowCahsoutDialog(Context activity)
    {
//api_error
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.alert_dialog_error, null);
        TextView btnGoHome =customView.findViewById(R.id.btnGoHome);
        TextView apierrorname=customView.findViewById(R.id.apierrorname);
        TextView tvOrderStatus=customView.findViewById(R.id.tvOrderStatus);
        apierrorname.setVisibility(View.GONE);
        TextView titile=customView.findViewById(R.id.titile);
        titile.setText("Alert");
        tvOrderStatus.setText("Country is not supported by sell");
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(customView);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCancelable(false);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
// getActivity().finish();
                dialog.dismiss();
            }
        });
    }
    //......
    public static void ShowVersionError(final Activity activity)
    {
        //api_error
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.alert_dialog_error, null);
        TextView btnGoHome =customView.findViewById(R.id.btnGoHome);
        TextView tvOrderStatus=customView.findViewById(R.id.tvOrderStatus);
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(customView);
        tvOrderStatus.setText("Latest version available please update the app.");
        btnGoHome.setText("Update");
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String appPackageName = activity.getPackageName(); // package name of the app
                try {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dialog.dismiss();
            }
        });
    }

    public static void errordurigApiCalling(final Activity activity, String apierroname)
    {
        //api_error
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.alert_network_issue, null);
        TextView btnGoHome =customView.findViewById(R.id.btnGoHome);
        TextView apierrorname=customView.findViewById(R.id.apierrorname);
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(customView);
        apierrorname.setText(apierroname);
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                activity.finishAffinity();
            }
        });
    }

    public static void DataNotFound(final Activity activity, String apierroname)
    {
        //api_error
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.alert_dialog_not_found, null);
        TextView btnGoHome =customView.findViewById(R.id.btnGoHome);
        TextView apierrorname=customView.findViewById(R.id.apidatanotfoundname);
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(customView);
        apierrorname.setText(apierroname);
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

    }

    public static void ShowServerPost(final Activity activity, String apierroname)
    {
        //api_error
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.alert_dialog_error, null);
        TextView btnGoHome =customView.findViewById(R.id.btnGoHome);
        TextView apierrorname=customView.findViewById(R.id.apierrorname);
        TextView tvOrderStatus=customView.findViewById(R.id.tvOrderStatus);
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(customView);
        tvOrderStatus.setText("DDK API CONNECTIONS IS CURRENTLY UNDER MAINTENANCE. \n" +
                "WE ARE RESOLVING THIS ISSUE AT THE MOMENT AND WE WILL BE BACK ON LINE AS SOON AS OUR TECHNICAL TEAM IMPLEMENT THE BACK UP SOLUTION.\n" +
                "\n" +
                "WE THANK YOU FOR YOUR PATIENCE");
        apierrorname.setText(apierroname);
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                Intent i=new Intent(activity,MainActivity.class);
                activity.startActivity(i);
                activity.finish();
            }
        });

    }

    public static Bitmap TextToImageEncode(Context context, String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    400, 400, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {
            Illegalargumentexception.printStackTrace();
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        context.getResources().getColor(R.color.black) : context.getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 400, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public static void ShowExcahngeDialog(Context activity,String dialogview)
    {
        //api_error
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.excahngedialogalert, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(customView);
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        ImageView close_view=customView.findViewById(R.id.close_icon);
        TextView sellview=customView.findViewById(R.id.sellview);
        TextView buyview=customView.findViewById(R.id.buyview);
        TabLayout tabLayout = customView.findViewById(R.id.tabs);
        if(dialogview.equalsIgnoreCase("buy"))
        {
            sellview.setVisibility(View.GONE);
            buyview.setVisibility(View.VISIBLE);
        }else
        {
            sellview.setVisibility(View.VISIBLE);
            buyview.setVisibility(View.GONE);
        }
        tabLayout.addTab(tabLayout.newTab().setText("GTC"));
        tabLayout.addTab(tabLayout.newTab().setText("Market"));

        close_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //for tab layout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                {
                } else if (tab.getPosition() == 1)
                {
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
        //.............
    }

    public static String ReplacecommaValue(String currenvalue)
    {
        if(currenvalue.contains(","))
        {
            String totalcurre=currenvalue.replace(",",".");
            currenvalue=totalcurre;
        }

        return currenvalue;
    }

    public static void ShowSAMPDDialog(final Activity activity, String msg)
    {
        //api_error
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.sampdletlayout, null);
        TextView no =customView.findViewById(R.id.no);
        TextView yes=customView.findViewById(R.id.yes);
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(customView);
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                Intent i=new Intent(activity,MainActivity.class);
                activity.startActivity(i);
                activity.finish();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                SAMPDFragment.switchActivty();
            }
        });

    }

    public static void getSettingServerDataSt(Activity activity, final String functionname)
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
                            App.editor.putString(Constant.PHP_Functionality_View,"true");
                            App.editor.apply();
                        }

                    } else
                    {
                        if(functionname.equalsIgnoreCase("php"))
                        {
                            MainActivity.updateTabview=1;
                            App.editor.putString(Constant.PHP_Functionality_View,"false");
                            App.editor.apply();
                        }
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void showRedeemDialog(final Context mContext, final ArrayList<RedeemOptionModel.Datum> redeemotpionlist,final String userSelection)
    {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView= layoutInflater.inflate(R.layout.redeemwalletlayout, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setView(dialogView);
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        TextView headerhint=dialog.findViewById(R.id.headerhint);
        if(userSelection.equalsIgnoreCase("redeem"))
        {
            headerhint.setText("What do you want to Redeem your Points ?");
        }else
        {
            headerhint.setText("What do you want to Sell ?");
        }
        RedeemChoiceAdpater mAdapter = new RedeemChoiceAdpater(dialog,redeemotpionlist, mContext,userSelection);
        recyclerView.setAdapter(mAdapter);
    }

    public static void showScanWalletDialog(final Context activity, String msg,String status)
    {
        //api_error
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.samwalletlayout, null);
        TextView btnGoHome =customView.findViewById(R.id.btnGoHome);
        TextView tvOrderStatus=customView.findViewById(R.id.tvOrderStatus);
        TextView header=customView.findViewById(R.id.header);
        tvOrderStatus.setText(msg);
        if(status.equalsIgnoreCase("1")) {
            header.setText("Alert");
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(customView);
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                QrScanFragmentScan.scandialogstauus=0;
                dialog.dismiss();
            }
        });

    }

    public static void ShowSameWalletDialog(final Context activity, String msg,String status)
    {
        //api_error
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.samwalletlayout, null);
        TextView btnGoHome =customView.findViewById(R.id.btnGoHome);
        TextView tvOrderStatus=customView.findViewById(R.id.tvOrderStatus);
        TextView header=customView.findViewById(R.id.header);
        tvOrderStatus.setText(msg);
        if(status.equalsIgnoreCase("1")) {
            header.setText("Alert");
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(customView);
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

    }

    //for buy option
    public static void showDialogCryptoData(final TextView ddk_ET,final Activity useravituv,final TextView tvbuy,final TextView tvcell,final View mview,final Context mContext,final ArrayList<String> cryptoListAdapter,final TextView tvcryptoview,final TextView ddkaddress)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View dialogView = layoutInflater.inflate(R.layout.select_popup, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
        dialog.setContentView(dialogView);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final cryptoListAdapter adapterCredential = new cryptoListAdapter(cryptoListAdapter, mContext,new cryptoListAdapter.SetOnItemClickListener() {
            @Override
            public void onItemClick(final String wallet_code) {
                tvcryptoview.setText(wallet_code);

                if(wallet_code.equalsIgnoreCase("DDK"))
                {
                    tvcryptoview.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_ddk),null,null,null);
                    ddkaddress.setVisibility(View.VISIBLE);
                }else
                {
                    ddkaddress.setVisibility(View.GONE);
                    if(wallet_code.equalsIgnoreCase("ETH"))
                    {
                        tvcryptoview.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_eth),null,null,null);
                    }else if(wallet_code.equalsIgnoreCase("USDT"))
                    {
                        tvcryptoview.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_usdt),null,null,null);
                    }else
                    {
                        tvcryptoview.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_bitcoin),null,null,null);
                    }
                }
                ddk_ET.setText("");
                 //for call
                UserModel.getInstance().getUSDCall(new GetUSDAndBTCCallback() {
                    @Override
                    public void getValues(BigDecimal btc, BigDecimal usd) {
                        if (usd != null)
                        {
                            BigDecimal ONE_HUNDRED = new BigDecimal(100);
                            BigDecimal buy = usd.multiply(UserModel.getInstance().ddkBuyPercentage).divide(ONE_HUNDRED);
                            BigDecimal sell = usd.multiply(UserModel.getInstance().ddkSellPercentage).divide(ONE_HUNDRED);
                            BigDecimal buyTemp = buy.add(usd);
                            BigDecimal sellTemp = usd.subtract(sell);
                            tvbuy.setText(buyTemp+"");
                            tvcell.setText(sellTemp+"");
                             if(wallet_code.equalsIgnoreCase("DDK"))
                             {
                                 tvbuy.setText("Buy Rate : " + String.format("%.6f", UserModel.getInstance().ddkBuyPrice));
                                 tvcell.setText(String.format("%.6f", UserModel.getInstance().ddkSellPrice));
                             }else
                            if(wallet_code.equalsIgnoreCase("ETH"))
                            {
                                tvbuy.setText("Buy Rate : " + String.format("%.6f", UserModel.getInstance().ethBuyPrice));
                                tvcell.setText(String.format("%.6f", UserModel.getInstance().ethSellPrice));
                            }else if(wallet_code.equalsIgnoreCase("USDT"))
                            {
                                tvbuy.setText("Buy Rate : " + String.format("%.6f", UserModel.getInstance().usdtBuyPrice));
                                tvcell.setText(String.format("%.6f", UserModel.getInstance().usdtSellPrice));
                            }else
                            {
                                tvbuy.setText("Buy Rate : " + String.format("%.6f", UserModel.getInstance().btcBuyPrice));
                                tvcell.setText(String.format("%.6f", UserModel.getInstance().btcSellPrice));
                            }

                        } else {
                            AppConfig.showToast("USDT rate not found");
                        }
                    }
                },useravituv);
                //...........
                dialog.dismiss();
            }
        });
        recyclerView.setAdapter(adapterCredential);
        dialog.show();
    }

    //.................
    public static void ShowTransationHistory(final Activity activity, String senderaddress, String receiveraddress,String type)
    {
        //api_error
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.alert_dialog_history, null);
        TextView btnGoHome =customView.findViewById(R.id.btnGoHome);
        final TextView tvReceiverAddress=customView.findViewById(R.id.tvReceiverAddress);
        final TextView tvSenderAddress=customView.findViewById(R.id.tvSenderAddress);
        final TextView receiverhint=customView.findViewById(R.id.receiverhint);
        LinearLayout sendrlayout=customView.findViewById(R.id.sendrlayout);
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(customView);
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);

        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // getActivity().finish();
                dialog.dismiss();
            }
        });

        tvReceiverAddress.setText(receiveraddress);
        tvSenderAddress.setText(senderaddress);

        tvReceiverAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_copy_small, 0);
        tvReceiverAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.copyPass(tvReceiverAddress.getText().toString().trim(), "Copy Address",activity);
            }
        });

        /*if(!type.toString().equalsIgnoreCase("Send"))
        {
            receiverhint.setVisibility(View.GONE);
            tvReceiverAddress.setVisibility(View.GONE);
        }else if (type.equals("Cashout")){
            receiverhint.setVisibility(View.GONE);
            tvReceiverAddress.setVisibility(View.GONE);
        } else*/
      /*  {
            tvReceiverAddress.setVisibility(View.VISIBLE);
        }
*/
        sendrlayout.setVisibility(View.VISIBLE);
        receiverhint.setVisibility(View.VISIBLE);
        tvReceiverAddress.setVisibility(View.VISIBLE);
        if (type.equals("Cashout")){
            receiverhint.setVisibility(View.GONE);
            tvReceiverAddress.setVisibility(View.GONE);
        }

        if (senderaddress.equalsIgnoreCase(null) || senderaddress.equalsIgnoreCase("")){
            sendrlayout.setVisibility(View.GONE);
         }

        tvSenderAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_copy_small, 0);
        tvSenderAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.copyPass(tvSenderAddress.getText().toString().trim(), "Copy Address",activity);
            }
        });
    }

    public static void showDialogVerificationView(final View mview, final Context mContext, final ArrayList<verifcationFundSource.Datum> samkoinList, final TextView tvSelectDdkAddress)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View dialogView = layoutInflater.inflate(R.layout.popup_wallet_pooling, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
        dialog.setContentView(dialogView);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        final EditText searchEt = dialogView.findViewById(R.id.search_ET);
        searchEt.setVisibility(View.GONE);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final veirifcationListAdapter adapterCredential = new veirifcationListAdapter(samkoinList, mContext,new veirifcationListAdapter.SetOnItemClickListener() {
            @Override
            public void onItemClick(final String wallet_code) {
                tvSelectDdkAddress.setText(wallet_code);
                //...........
                dialog.dismiss();
            }
        });
        recyclerView.setAdapter(adapterCredential);
        dialog.show();
    }

    public static void showDialogBankTrnsfer(final String id,final String conversionrate,final String secrate,final String phpamount,final String ddkamount,final String ddkaddress,final View mview, final Activity mContext, String imagePath, final UserBankList userBankList, final String amountentervalue, final CashOutFragmentNew cashOutFragmentNew)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View dialogView = layoutInflater.inflate(R.layout.popup_banktransfer, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
        dialog.setContentView(dialogView);
        TextView tvSendMoney = dialogView.findViewById(R.id.tvSendMoney);
        TextView tvUpdate = dialogView.findViewById(R.id.tvUpdate);
        TextView tv_CountryId = dialogView.findViewById(R.id.tv_CountryId);
        TextView tv_ConMaxTransfer = dialogView.findViewById(R.id.tv_ConMaxTransfer);
        TextView tvBankNameDDk = dialogView.findViewById(R.id.tvBankNameDDk);
        EditText etAmountSend = dialogView.findViewById(R.id.etAmountSend);
        EditText etAccountNumber = dialogView.findViewById(R.id.etAccountNumber);
        EditText etAccountName = dialogView.findViewById(R.id.etAccountName);
        final EditText etEmailReceipt=dialogView.findViewById(R.id.etEmailReceipt);
        ImageView ivBankLogo = dialogView.findViewById(R.id.ivBankLogo);
        View view = dialogView.findViewById(R.id.view);
        dialog.show();
        etAmountSend.setText(phpamount);
        etAccountNumber.setText(userBankList.getAccountNo().toString());
        etAccountName.setText(userBankList.getName().toString());
        UserResponse userData = AppConfig.getUserData(mContext);
        String countrydata=userData.getUser().country.get(0).country;
        Log.d("country",countrydata);
        if(countrydata!=null && countrydata.equalsIgnoreCase("Philippines")){
            tv_CountryId.setText("PHP");
            ivBankLogo.setVisibility(View.VISIBLE);
            tv_ConMaxTransfer.setText("Maximum of PHP 50,000");
            etAccountName.setHint("Enter Account Name");
        }else {
            tv_CountryId.setText("AUD");
            ivBankLogo.setVisibility(View.INVISIBLE);
            etAccountName.setHint("Enter BSB Name");
            tv_ConMaxTransfer.setText("Maximum of AUD 50,000");
        }
        tv_ConMaxTransfer.setVisibility(View.GONE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tvBankNameDDk.setText(userBankList.getBank().getBankName());
        Glide.with(mContext).load(SLIDERIMG + userBankList.getBank().getImage()).into(ivBankLogo);

        tvSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(banknotabv.equalsIgnoreCase("0"))
                {
                    ShowSameWalletDialog(mContext,msgbank,"1");
                }else {

                    //Bigde
                    BigDecimal countamoutn = new BigDecimal("50");
                    BigDecimal btccondition = new BigDecimal(phpamount);
                    int comare = btccondition.compareTo(countamoutn);
                    /*if (comare == 1) {
                        Toast.makeText(mContext, "Amount Should be less then 50.0", Toast.LENGTH_SHORT).show();
                    } else*/ {
                        dialog.dismiss();
                        //for send money call
                        String bank_type = userBankList.getBankType();
                        String holder_name = userBankList.getName();
                        String gcash_no = "", account_no = "";
                        if (userBankList.getBankType().toString().equalsIgnoreCase("bank")) {
                            account_no = userBankList.getAccountNo().toString();
                            gcash_no = "";
                        } else {
                            account_no = "";
                            gcash_no = userBankList.getGcashNo();
                        }
                        String email = etEmailReceipt.getText().toString();
                        String bank_id = userBankList.getBank().getId() + "";
                        sendOtp(id,bank_type, holder_name, account_no, gcash_no, email, bank_id, conversionrate, secrate, phpamount, ddkamount, ddkaddress, mContext);
                    }
                }
            }
        });

        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showDialogConfirmGcash(mview,mContext,userBankList,cashOutFragmentNew);
            }
        });

    }

    public static void sendOtp(final String id,final String bank_type, final String holder_name, final String account_no, final String gcash_no, final String email, final String bank_id, final String conversionrate, final String secrate, final String phpamount, final String ddkamount, final String ddkaddress, final Activity mContext)
    {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("email", App.pref.getString(Constant.USER_EMAIL, ""));
        hm.put("name", App.pref.getString(Constant.USER_NAME, ""));

        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Sending otp....");

        AppConfig.getLoadInterface().postOtp(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm).enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body().status == 1) {
                    String otp = response.body().data;
                    Log.d("otp",otp);
                    initOtpVerifiaction(id,bank_type,holder_name,account_no,gcash_no,email,bank_id,conversionrate,secrate,phpamount,ddkamount,ddkaddress,otp,mContext);
                } else if (response.isSuccessful() && response.body() != null && response.body().status == 3) {
                    AppConfig.showToast(response.body().msg);
                } else if (response.isSuccessful() && response.body() != null && response.body().status == 0) {
                    AppConfig.showToast(response.body().msg);
                } else {
                    AppConfig.showToast("Server Error");
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                dialog.dismiss();
                errordurigApiCalling(mContext,t.getMessage());
            }
        });
    }

    private static void initOtpVerifiaction(final String id,final String bank_type,final String holder_name,final String account_no,final String gcash_no,final String email,final String bank_id,final String conversionrate,final String secrate,final String phpamount,final String ddkamount,final String ddkaddress,final String otp,final Activity mContext)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        final View dialogView;
            dialogView = layoutInflater.inflate(R.layout.otpverificationlayout, null);
            final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
            dialog.setContentView(dialogView);
            ImageView logo1=dialogView.findViewById(R.id.logo1);
            final LinearLayout otherview=dialogView.findViewById(R.id.otherview);
            otherview.setVisibility(View.VISIBLE);
            logo1.setVisibility(View.GONE);
            final LinearLayout back_view=dialogView.findViewById(R.id.back_view);
            final TextView btnVerify =dialogView.findViewById(R.id.btnVerify);
            final EditText otp_edt1 =dialogView.findViewById(R.id.otp_edt11);
            final EditText otp_edt2 =dialogView.findViewById(R.id.otp_edt12);
            final EditText otp_edt3 =dialogView.findViewById(R.id.otp_edt13);
            final EditText otp_edt4 =dialogView.findViewById(R.id.otp_edt14);

        btnVerify.setOnClickListener(new View.OnClickListener()
        {
                @Override
                public void onClick(View v)
                {
                    StringBuffer str_top = new StringBuffer();
                    str_top.append(otp_edt1.getText().toString()).append(otp_edt2.getText().toString()).append(otp_edt3.getText().toString()).append(otp_edt4.getText().toString());
                    if (str_top.length() > 0 && str_top.length() == 4)
                    {
                        String otpnew=str_top.toString();
                            if (str_top.toString().equalsIgnoreCase(otp))
                            {
                                if(userselctionopt.equalsIgnoreCase("DDK"))
                                {
                                    sendPaymentDDk(id,bank_type,holder_name,account_no,gcash_no,email,bank_id,conversionrate,secrate,phpamount,ddkamount,ddkaddress,mContext);
                                }else
                                {
                                    sendPayment(id,bank_type,holder_name,account_no,gcash_no,email,bank_id,conversionrate,secrate,phpamount,ddkamount,ddkaddress,mContext);
                                }
                            } else {
                                AppConfig.showToast("Otp is expired or incorrect");
                            }
                        //............
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

        dialog.show();
    }

    private static void sendPayment(final String id,final String bank_type,final String holder_name,final String account_no,final String gcash_no,final String email,final String bank_id,final String conversionrate,final String secrate,final String phpamount,final String ddkamount,final String ddkaddress,final Activity mContext) {

        String conversionratevalue=conversionrate.replace("Conversion rate :","");
        String conversionratesub=conversionratevalue.replace(" USDT","");
        String substeing=conversionratesub;
        HashMap<String, String> hm = new HashMap<>();
        String koinadd=App.pref.getString(Constant.SAMKOIN_ADD, "");
        hm.put("sender_address", ""+koinadd);
        hm.put("conversion_rate", ""+substeing);
        hm.put("total_amount", phpamount);
        hm.put("input_amount", ddkamount);
        String trascationfeev=CashOutFragmentNew.transaction_fees.getText().toString().replace("Transaction Fees : ","");
        hm.put("fee", trascationfeev);
        if(CashOutFragmentNew.countrydata.equalsIgnoreCase("philippines"))
        {
            hm.put("transaction_for", "php");
        }else
        {
            hm.put("transaction_for", "aud");
        }
        hm.put("input_amount",ddkamount);
        hm.put("secret",App.pref.getString(Constant.SAMKOIN_Secaret, ""));
        hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
        hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
        //for bank
        hm.put("bank_type", bank_type);
        hm.put("holder_name",holder_name);
        hm.put("account_no", account_no);
        hm.put("gcash_no",gcash_no);
        hm.put("email", email);
        hm.put("bank_id", bank_id);
        hm.put("user_bank_id", id);
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Confirm Payment....");
        String tokenvalue=AppConfig.getStringPreferences(mContext, Constant.JWTToken);
        Log.d("param",hm.toString());
        AppConfig.getLoadInterface().sendCashOutSamKoin(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppConfig.hideLoading(dialog);
                try {

                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.d("responsse",""+object);
                        if (object.getInt(Constant.STATUS) == 1) {
                            try {
                                final JSONObject dataObject = object.getJSONObject("data");
                                transactionStatus(mContext,dataObject.getString("txt_id"));
                            } catch (JSONException e) {
                                AppConfig.showToast("Server error");
                                e.printStackTrace();
                            }
                        }else if (object.getInt(Constant.STATUS) == 4)
                        {
                            ShowServerPost((Activity)mContext,"ddk server error cashout");
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
            }
        });
    }

    //..........
    private static void sendPaymentDDk(final String id,final String bank_type,final String holder_name,final String account_no,final String gcash_no,final String email,final String bank_id,final String conversionrate,final String secrate,final String phpamount,final String ddkamount,final String ddkaddress,final Activity mContext) {

        String conversionratevalue=conversionrate.replace("Conversion rate ","");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("bank_type", bank_type);
        hm.put("holder_name", holder_name);
        hm.put("account_no", account_no);
        hm.put("gcash_no", gcash_no);
        hm.put("email", email);
        hm.put("bank_id", bank_id);
        hm.put("user_bank_id", id);
        hm.put("ddk_address", ddkaddress);
        hm.put("conversion_rate", "" +conversionratevalue);
        hm.put("php_amount", phpamount);
        hm.put("amount", ddkamount);
        hm.put("secret", secrate);
        hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
        hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Confirm Payment....");
        String tokenvalue=AppConfig.getStringPreferences(mContext, Constant.JWTToken);
        Log.d("param",hm.toString());
        AppConfig.getLoadInterface().sendCashOutNew(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppConfig.hideLoading(dialog);
                try {

                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.d("responsse",""+object);
                        if (object.getInt(Constant.STATUS) == 1) {
                            try {
                                final JSONObject dataObject = object.getJSONObject("data");
                                transactionStatus(mContext,dataObject.getString("txt_id"));
                            } catch (JSONException e) {
                                AppConfig.showToast("Server error");
                                e.printStackTrace();
                            }
                        }else if (object.getInt(Constant.STATUS) == 4)
                        {
                            ShowServerPost((Activity)mContext,"ddk server error cashout");
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
            }
        });
    }

    //................
    private static void transactionStatus(final Activity mContext, String transactionId)
    {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = layoutInflater.inflate(R.layout.popup_transaction_status, null);
            TextView btnGoHome = (TextView) customView.findViewById(R.id.btnGoHome);
            TextView tvOrderStatus = customView.findViewById(R.id.tvOrderStatus);
            final TextView tvTransactionId = customView.findViewById(R.id.tvTransactionId);
            ImageView btnCopyTransactionId = customView.findViewById(R.id.btnCopyTransactionId);
            ImageView ivTransactionCreateCheck = customView.findViewById(R.id.ivTransactionCreateCheck);
            ImageView ivTransactionCreateUncheck = customView.findViewById(R.id.ivTransactionCreateUncheck);
            ImageView ivOrderStatusIconUnCheck = customView.findViewById(R.id.ivOrderStatusIconUnCheck);
            ImageView ivOrderStatusIconCheck = customView.findViewById(R.id.ivOrderStatusIconCheck);
            FrameLayout progress_bar = customView.findViewById(R.id.progress_bar);
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setView(customView);

            final AlertDialog dialog = alert.create();
            dialog.show();
            dialog.setCancelable(false);
            btnGoHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    // getActivity().finish();
                    Intent i =new Intent(mContext,MainActivity.class);
                    mContext.startActivity(i);
                  //  mContext.finish();
                }
            });

            btnCopyTransactionId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copy", tvTransactionId.getText().toString().trim());
                    clipboard.setPrimaryClip(clip);
                    AppConfig.showToast("Copied");
                }
            });

            tvTransactionId.setText(transactionId);
            ivTransactionCreateCheck.setVisibility(View.VISIBLE);
            ivTransactionCreateUncheck.setVisibility(View.GONE);
            btnGoHome.setVisibility(View.VISIBLE);
            progress_bar.setVisibility(View.GONE);
            tvOrderStatus.setText("Pending");
            ivOrderStatusIconCheck.setVisibility(View.VISIBLE);
            ivOrderStatusIconUnCheck.setVisibility(View.GONE);
    }

    public static void showDialogGcash(final String id,final int bankid,final String conversionrate,final String secrate,final String phpamount,final String ddkamount,final String ddkaddress,final View mview, final Activity mContext,final String bankName,final String bankimg,final String banktype,final CashOutFragmentNew cashOutFragmentNew,final UserBankList userBankList)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View dialogView = layoutInflater.inflate(R.layout.popup_gcash, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
        dialog.setContentView(dialogView);
        TextView tvSendMoney = dialogView.findViewById(R.id.tvSendMoney);
        final TextView tvUpdate = dialogView.findViewById(R.id.tvUpdate);
        TextView tv_CountryId = dialogView.findViewById(R.id.tv_CountryId);
        TextView tv_ConMaxTransfer = dialogView.findViewById(R.id.tv_ConMaxTransfer);
        final EditText et_gcashValue = dialogView.findViewById(R.id.et_gcashValue);
        final EditText et_name = dialogView.findViewById(R.id.et_name);
        ImageView ivGcashLogo = dialogView.findViewById(R.id.ivGcashLogo);
        final EditText et_gcahsname=dialogView.findViewById(R.id.et_gcahsname);
        final LinearLayout gcahname_layout=dialogView.findViewById(R.id.gcahname_layout);
        gcahname_layout.setVisibility(View.GONE);
        et_gcahsname.setText(bankName);
        final View view = dialogView.findViewById(R.id.view);
        dialog.show();
        et_gcashValue.setText(phpamount);
        et_gcahsname.setEnabled(true);
        String gcahsnu=userBankList.getGcashNo();
        et_name.setText(gcahsnu);
        Glide.with(mContext).asBitmap().load(SLIDERIMG+ bankimg).into(ivGcashLogo);
        UserResponse userData = AppConfig.getUserData(mContext);
        String countrydata=userData.getUser().country.get(0).country;
        Log.d("country",countrydata);
        if(countrydata!=null && countrydata.equalsIgnoreCase("Philippines")){
            tv_CountryId.setText("PHP");
            tv_ConMaxTransfer.setText("Maximum of PHP 50,000");
        }else {
            tv_CountryId.setText("AUD");
            tv_ConMaxTransfer.setText("Maximum of AUD 50,000");
        }
        tv_ConMaxTransfer.setVisibility(View.GONE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tvSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //.....
                if(banknotabv.equalsIgnoreCase("0"))
                {
                    ShowSameWalletDialog(mContext,msgbank,"1");
                }else {

                    if(et_name.getText().toString().length()<=20) {
                        BigDecimal countamoutn = new BigDecimal("50");
                        BigDecimal btccondition = new BigDecimal(phpamount);
                        int comare = btccondition.compareTo(countamoutn);
                        {
                            dialog.dismiss();
                            //for send money call
                            String bank_type = banktype;
                            String holder_name = "";
                            String gcash_no = "", account_no = "";
                            if (bank_type.equalsIgnoreCase("bank")) {
                                account_no = "";
                                gcash_no = "";
                                holder_name = et_name.getText().toString();
                            } else {
                                account_no = "";
                                gcash_no = et_name.getText().toString();
                                holder_name = bankName;
                            }
                            String email = "";
                            String bank_id = bankid + "";
                            sendOtp(id, bank_type, holder_name, account_no, gcash_no, email, bank_id, conversionrate, secrate, phpamount, ddkamount, ddkaddress, mContext);
                        }
                    }else
                    {
                        Toast.makeText(mContext, "Invalid GCash Number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
                {
                dialog.dismiss();
                showDialogConfirmGcashNewupdate(userBankList,mview,mContext,cashOutFragmentNew);
            }
        });

    }


    //for deatta
    public static void showDialogConfirmGcashNew(final String gCash,final String banktype,final String bankid,final String bankname,final View mview, final Activity mContext,final CashOutFragmentNew cashOutFragmentNew)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View dialogView = layoutInflater.inflate(R.layout.popup_confirmtransfer, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
        dialog.setContentView(dialogView);
        TextView tvConfirmMoney = dialogView.findViewById(R.id.tvConfirmMoney);
        TextView tvDeleteBank = dialogView.findViewById(R.id.tvDeleteBank);
        TextView ll_SelectBankName= dialogView.findViewById(R.id.ll_SelectBankName);
        final EditText et_bankUserName= dialogView.findViewById(R.id.et_bankUserName);
        final EditText et_GCashNo= dialogView.findViewById(R.id.et_GCashNo);
        View view = dialogView.findViewById(R.id.view);
        dialog.show();
        UserResponse userData = AppConfig.getUserData(mContext);
        String countrydata=userData.getUser().country.get(0).country;
        Log.d("country",countrydata);
        //..........
        ll_SelectBankName.setText(bankname);
        et_GCashNo.setText(gCash);

        /*ll_SelectBankName.setText(userBankList.getBank().getBankName());
        et_bankUserName.setText(userBankList.getName());
        et_GCashNo.setText(userBankList.getAccountNo().toString());
        */view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //Glide.with(mContext).load(imagePath + bankImage).into(ivBankLogo);

        tvConfirmMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                editBankDetails(banktype,bankid,et_GCashNo.getText().toString(),et_bankUserName.getText().toString(),mContext,bankid.toString(),cashOutFragmentNew);
                dialog.dismiss();
            }
        });

        tvDeleteBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBank(mContext,bankid,cashOutFragmentNew);
                dialog.dismiss();
            }
        });
    }
    //....

    public static void showDialogConfirmGcashNewupdate(final UserBankList userBankList,final View mview, final Activity mContext,final CashOutFragmentNew cashOutFragmentNew)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View dialogView = layoutInflater.inflate(R.layout.popup_confirmtransfer, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
        dialog.setContentView(dialogView);
        TextView tvConfirmMoney = dialogView.findViewById(R.id.tvConfirmMoney);
        TextView tvDeleteBank = dialogView.findViewById(R.id.tvDeleteBank);
        TextView ll_SelectBankName= dialogView.findViewById(R.id.ll_SelectBankName);
        final EditText et_bankUserName= dialogView.findViewById(R.id.et_bankUserName);
        final EditText et_GCashNo= dialogView.findViewById(R.id.et_GCashNo);
        View view = dialogView.findViewById(R.id.view);
        dialog.show();
        UserResponse userData = AppConfig.getUserData(mContext);
        String countrydata=userData.getUser().country.get(0).country;
        Log.d("country",countrydata);
        //..........
        ll_SelectBankName.setText(userBankList.getBank().getBankName());
        et_bankUserName.setText(userBankList.getName());
        String gchanovalue="";
        if(userBankList.getGcashNo()!=null)
        {
            if (gchanovalue != null || !gchanovalue.equalsIgnoreCase(""))
            {
                gchanovalue=userBankList.getGcashNo().toString();
                et_GCashNo.setText(userBankList.getGcashNo().toString());
            }
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //Glide.with(mContext).load(imagePath + bankImage).into(ivBankLogo);

        tvConfirmMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                editBankDetails(userBankList.getBankType(),userBankList.getBank().getId().toString(),et_GCashNo.getText().toString(),et_bankUserName.getText().toString(),mContext,userBankList.getId().toString(),cashOutFragmentNew);
                dialog.dismiss();
            }
        });

        tvDeleteBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBank(mContext,userBankList.getId().toString(),cashOutFragmentNew);
                dialog.dismiss();
            }
        });
    }

    //........
    public static void showDialogConfirmGcash(final View mview, final Activity mContext, final UserBankList userBankList, final CashOutFragmentNew cashOutFragmentNew)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View dialogView = layoutInflater.inflate(R.layout.popup_confirmtransfer, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
        dialog.setContentView(dialogView);
        TextView tvConfirmMoney = dialogView.findViewById(R.id.tvConfirmMoney);
        TextView tvDeleteBank = dialogView.findViewById(R.id.tvDeleteBank);
        TextView ll_SelectBankName= dialogView.findViewById(R.id.ll_SelectBankName);
        final EditText et_bankUserName= dialogView.findViewById(R.id.et_bankUserName);
        final EditText et_GCashNo= dialogView.findViewById(R.id.et_GCashNo);
        View view = dialogView.findViewById(R.id.view);
        dialog.show();
        UserResponse userData = AppConfig.getUserData(mContext);
        String countrydata=userData.getUser().country.get(0).country;
        Log.d("country",countrydata);
        ll_SelectBankName.setText(userBankList.getBank().getBankName());
        et_bankUserName.setText(userBankList.getName());
        et_GCashNo.setText(userBankList.getAccountNo().toString());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //Glide.with(mContext).load(imagePath + bankImage).into(ivBankLogo);

        tvConfirmMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                editBankDetails(userBankList.getBankType(),userBankList.getBank().getId().toString(),et_GCashNo.getText().toString(),et_bankUserName.getText().toString(),mContext,userBankList.getId().toString(),cashOutFragmentNew);
                dialog.dismiss();
            }
        });

        tvDeleteBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBank(mContext,userBankList.getId().toString(),cashOutFragmentNew);
                dialog.dismiss();
            }
        });
    }

    private static void editBankDetails(final String banktype,final String banklisid,final String gcashvale,final String bankname,final Activity mContext,final String id, final CashOutFragmentNew cashOutFragmentNew) {
        HashMap<String, String> hm = new HashMap<>();
        if(banktype.equalsIgnoreCase("bank"))
        {
            hm.put("name",bankname);
            hm.put("account_no",gcashvale);
            hm.put("bank_id",""+banklisid);
            hm.put("id",""+id);
            hm.put("bank_type",banktype);
        }else
        {
            hm.put("name",bankname);
            hm.put("gcash_no",gcashvale);
            hm.put("bank_id",""+banklisid);
            hm.put("id",""+id);
            hm.put("bank_type",banktype);
        }
        //bank_type
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Delete Bank....");
        String tokenvalue=AppConfig.getStringPreferences(mContext, Constant.JWTToken);
        Log.d("newtoken",tokenvalue);
        AppConfig.getLoadInterface().userBankEdit(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppConfig.hideLoading(dialog);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(Constant.STATUS) == 1)
                        {
                            Toast.makeText(mContext, "Detail update successfully", Toast.LENGTH_SHORT).show();
                            cashOutFragmentNew.getUserBankList();
                        }else if (object.getInt(Constant.STATUS) == 4)
                        {
                            ShowServerPost((Activity)mContext,"ddk server error cashout");
                        } else {
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } else {
                        AppConfig.showToast("Server error");
                    }
                } catch (Exception e) {
                    AppConfig.showToast("Server error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                AppConfig.hideLoading(dialog);
                errordurigApiCalling(mContext,t.getMessage());
            }
        });
    }

    private static void deleteBank(final Activity mContext,String id, final CashOutFragmentNew cashOutFragmentNew) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("id",""+id);
        //hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Delete Bank....");
        String tokenvalue=AppConfig.getStringPreferences(mContext, Constant.JWTToken);
        Log.d("newtoken",tokenvalue);
        AppConfig.getLoadInterface().userBankDelete(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                AppConfig.hideLoading(dialog);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(Constant.STATUS) == 1) {
                                cashOutFragmentNew.getUserBankList();
                        }else if (object.getInt(Constant.STATUS) == 4)
                        {
                            ShowServerPost((Activity)mContext,"ddk server error cashout");
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
                errordurigApiCalling(mContext,t.getMessage());
            }
        });
    }

    public static void showDialogForSAMKoin(Activity activity,final TabLayout tab,final View mview,final Context mContext,final ArrayList<String> samkoinList,final TextView tvSelectDdkAddress)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        final PopupWindow popup = new PopupWindow(activity);
        View view1 = layoutInflater.inflate(R.layout.popup_wallet_pooling_for_tron, null);
        popup.setContentView(view1);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        RecyclerView recyclerView =view1.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final tronListAdapter adapterCredential = new tronListAdapter(tvSelectDdkAddress,samkoinList, mContext,new tronListAdapter.SetOnItemClickListener() {
            @Override
            public void onItemClick(final String wallet_code) {
                tvSelectDdkAddress.setText(wallet_code);
                //...........
                popup.dismiss();
            }
        });
        recyclerView.setAdapter(adapterCredential);
        // Show anchored to button
        popup.showAsDropDown(tvSelectDdkAddress);
    }

    public static void showDialogForDDKAddress(final View mview,final Context mContext,final List<Credential> _credentialList,final TextView tvSelectDdkAddress,final TextView tvAvailableDDK,final TextView tvDDKsecrate)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View dialogView = layoutInflater.inflate(R.layout.popup_wallet_pooling, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
        dialog.setContentView(dialogView);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        final EditText searchEt = dialogView.findViewById(R.id.search_ET);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final CredentialListAdapter adapterCredential = new CredentialListAdapter(_credentialList, mContext, searchEt, new CredentialListAdapter.SetOnItemClickListener() {
            @Override
            public void onItemClick(String wallet_code, String walletId1, String passPhrese) {
                tvSelectDdkAddress.setText(wallet_code);
                String ddkSecret = passPhrese;
                tvDDKsecrate.setText(ddkSecret);
                dataPutMethods.getWalletBalance(walletId1,mContext,tvAvailableDDK);
                dataPutMethods.hideKeyBoard(mContext,mview);
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
                    dataPutMethods.filter(searchEt.getText().toString());
                } else {
                    adapterCredential.updateData(_credentialList);
                }
            }
        });

        dialog.show();
    }

    public static void showDialogForSearchCountry(final View mview, final Context mContext, final ArrayList<Country> countrygender, final String[] genderlist, final String [] stateId, final String [] countryCode, final TextView countryET, final TextView phoneCodeET)
    {
       // country, mCountryId
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View dialogView = layoutInflater.inflate(R.layout.popup_wallet_pooling, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
        dialog.setContentView(dialogView);
        TextView countrynamehint=dialogView.findViewById(R.id.countrynamehint);
        countrynamehint.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        final EditText searchEt = dialogView.findViewById(R.id.search_ET);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final CountryListAdapter adapterCredential = new CountryListAdapter(countrygender,genderlist, mContext, searchEt, new CountryListAdapter.SetOnItemClickListener() {

            @Override
            public void onItemClick(String country, String stateid,String phoneid)
            {
                ProfileFragment.country = country;
                ProfileFragment.mCountryId = stateid;
                countryET.setText(phoneid);
                phoneCodeET.setText(country);
                dataPutMethods.hideKeyBoard(mContext,mview);
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
            public void afterTextChanged(Editable editable)
            {
                if (searchEt.getText().toString().length() > 0)
                {
                    dataPutMethods.filterCountry(searchEt.getText().toString(),countrygender,adapterCredential);
                } else {
                    adapterCredential.updateData(countrygender);
                }
            }
        });

        dialog.show();
    }

    public static void filter(String newText)
    {
    }

    public static void filterCountry(String newText, ArrayList<Country> countrygender,CountryListAdapter adapterCredential)
    {
        //new array list that will hold the filtered data
        ArrayList<Country> temp = new ArrayList();
        for(Country d: countrygender){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getCountry().toLowerCase().contains(newText.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        adapterCredential.updateData(temp);
    }

    public static void hideKeyBoard(Context mContext,View mview) {
        final InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mview.getWindowToken(), 0);
    }

    public static void getWalletBalance(String walletId,final Context mContext,final TextView tvWalletBalance) {
        UserModel.getInstance().getWalletDetails(0,walletId, mContext, new GetAvailableValue() {
            @Override
            public void getValues(String ddk, WalletResponse successResponse) {
                String tvWalletBalancevalue= ddk+"";
                tvWalletBalance.setText(""+ddk);
            }
        });
    }
}
