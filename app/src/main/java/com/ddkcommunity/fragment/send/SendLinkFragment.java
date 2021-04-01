package com.ddkcommunity.fragment.send;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.CredentialListAdapter;
import com.ddkcommunity.fragment.HomeFragment;
import com.ddkcommunity.interfaces.GetAllCredential;
import com.ddkcommunity.interfaces.GetAvailableValue;
import com.ddkcommunity.model.OtpResponse;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.credential.CredentialsResponse;
import com.ddkcommunity.model.wallet.WalletResponse;
import com.ddkcommunity.utilies.AppConfig;
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
import static com.ddkcommunity.fragment.wallet.FragmentCreatePassphrase.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendLinkFragment extends Fragment
{
    private View rootView = null;
    private Context mContext;
    public SendLinkFragment() {
        // Required empty public constructor
    }

    private String link;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            View view = inflater.inflate(R.layout.fragment_link, container, false);
            rootView = view;
            mContext = getActivity();
            try {
                if (getArguments().getString("link") != null) {
                    link= getArguments().getString("link");
                }
                String linkmain=link;
                WebView webview =view.findViewById(R.id.webView);
                WebSettings settings = webview.getSettings();
                settings.setJavaScriptEnabled(true);
                webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

                final ProgressDialog progressBar = ProgressDialog.show(getActivity(), "", "Loading...");
                webview.setWebViewClient(new WebViewClient() {
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
                webview.loadUrl(link);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("");
        MainActivity.enableBackViews(true);
    }
}
