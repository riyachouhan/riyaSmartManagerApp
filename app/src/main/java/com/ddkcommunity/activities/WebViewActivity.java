package com.ddkcommunity.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ddkcommunity.R;
import com.ddkcommunity.utilies.AppConfig;

public class WebViewActivity extends AppCompatActivity {
    String fileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        fileName = getIntent().getStringExtra("fileName");
        TextView tvHeader = findViewById(R.id.tvHeader);

        ImageView ivBanner = findViewById(R.id.ivBanner);

        WebView webView = findViewById(R.id.webView);
        WebSettings websettings = webView.getSettings();
        websettings.setJavaScriptEnabled(true);

        if (fileName.equalsIgnoreCase("consolidated_approaches")) {
            ivBanner.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.banner6));
        }
        if (fileName.equalsIgnoreCase("the_free_flight_voucher_update")) {
            ivBanner.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.banner2));
        }
        if (fileName.equalsIgnoreCase("smart_asset_manager")) {
            ivBanner.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.banner4));
        }
        if (fileName.equalsIgnoreCase("the_marketing")) {
            ivBanner.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.banner3));
        }
        if (fileName.equalsIgnoreCase("free_flight_voucher")) {
            ivBanner.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.banner1));
        }
        if (fileName.equalsIgnoreCase("coin_payment")) {
            final ProgressDialog dialog = new ProgressDialog(this);
            AppConfig.showLoading(dialog, "Loading...");
            ivBanner.setVisibility(View.GONE);
            String url = getIntent().getStringExtra("url");
            tvHeader.setText("Coin Payment");
            webView.loadUrl(url);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, 3000);
        } else {
            ivBanner.setVisibility(View.VISIBLE);
            tvHeader.setText(fileName.replace("_", " ").toUpperCase());
            webView.loadUrl("file:///android_asset/" + fileName + ".html");
        }


//        webView.setWebViewClient(new WebViewClient());
        ImageView imageView = findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
