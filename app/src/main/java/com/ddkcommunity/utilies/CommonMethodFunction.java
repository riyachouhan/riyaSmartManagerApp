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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.CredentialListAdapter;
import com.ddkcommunity.adapters.RedeemChoiceAdpater;
import com.ddkcommunity.adapters.cryptoListAdapter;
import com.ddkcommunity.adapters.veirifcationListAdapter;
import com.ddkcommunity.fragment.CashOutFragmentNew;
import com.ddkcommunity.fragment.SAMPDFragment;
import com.ddkcommunity.interfaces.GetAvailableValue;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.model.OtpResponse;
import com.ddkcommunity.model.RedeemOptionModel;
import com.ddkcommunity.model.UserBankList;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.model.verifcationFundSource;
import com.ddkcommunity.model.wallet.WalletResponse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

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
import static java.security.AccessController.getContext;

public class CommonMethodFunction
{

    public static void transactionStatus(final String status,final Activity activty, String transactionId, String lenderId) {
        LayoutInflater layoutInflater = (LayoutInflater) activty.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_transaction_status, null);
        TextView btnGoHome = customView.findViewById(R.id.btnGoHome);
        TextView tvOrderStatus = customView.findViewById(R.id.tvOrderStatus);
        final TextView tvTransactionId = customView.findViewById(R.id.tvTransactionId);
        AppCompatImageView btnCopyTransactionId = customView.findViewById(R.id.btnCopyTransactionId);
        ImageView ivTransactionCreateCheck = customView.findViewById(R.id.ivTransactionCreateCheck);
        ImageView ivTransactionCreateUncheck = customView.findViewById(R.id.ivTransactionCreateUncheck);
        ImageView ivOrderStatusIconUnCheck = customView.findViewById(R.id.ivOrderStatusIconUnCheck);
        ImageView ivOrderStatusIconCheck = customView.findViewById(R.id.ivOrderStatusIconCheck);
        FrameLayout progress_bar = customView.findViewById(R.id.progress_bar);
        AlertDialog.Builder alert = new AlertDialog.Builder(activty);
        alert.setView(customView);
        //for show dialog
        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCancelable(false);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activty.finish();
                Intent i=new Intent(activty,MainActivity.class);
                activty.startActivity(i);
            }
        });

        btnCopyTransactionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) activty.getSystemService(CLIPBOARD_SERVICE);
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
        tvOrderStatus.setText(status);
        ivOrderStatusIconCheck.setVisibility(View.VISIBLE);
        ivOrderStatusIconUnCheck.setVisibility(View.GONE);
    }

    public static void setupFullHeight(Activity activity,BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight(activity);
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public static int getWindowHeight(Activity activity) {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}
