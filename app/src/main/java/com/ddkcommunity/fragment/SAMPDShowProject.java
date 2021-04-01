package com.ddkcommunity.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.projects.SelectPaymentPoolingFragment;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static com.ddkcommunity.activities.MainActivity.setTitle;

/**
 * A simple {@link Fragment} subclass.
 */
public class SAMPDShowProject extends Fragment {

    public SAMPDShowProject() {

    }

    public String SAMPLE_FILE="";
    WebView webview;
    Integer pageNumber = 0;
    String pdfFileName;
    private TextView btnGoBack,file_content;
    ProgressBar progressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.smpd_pdfview, container, false);
        try {
                if(getArguments().getString("pdflink")!=null) {
                    SAMPLE_FILE = getArguments().getString("pdflink");
                }
            Log.d("pdflink",SAMPLE_FILE);
            //........
            progressbar=view.findViewById(R.id.progressbar);
            webview=view.findViewById(R.id.webview);
            progressbar.setVisibility(View.VISIBLE);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.loadUrl("http://docs.google.com/gview?embedded=true&url="+SAMPLE_FILE);
            webview.setWebViewClient(new WebViewClient()
            {
                public void onPageFinished(WebView view ,String url)
                {
                    progressbar.setVisibility(View.GONE);
                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // HomeActivity.setHomeItem(getActivity(), R.id.profile);
        setTitle("The MAGAZINE");
        MainActivity.enableBackViews(true);
    }
}