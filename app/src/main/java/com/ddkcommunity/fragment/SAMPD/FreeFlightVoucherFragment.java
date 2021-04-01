package com.ddkcommunity.fragment.SAMPD;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.LoadInterface;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.FreeFlightVoucherAdapter;
import com.ddkcommunity.model.FreeFlightVoucherList;
import com.ddkcommunity.model.FreeFlightVoucherListData;
import com.ddkcommunity.model.PdfViewPojo;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.ConnectionDetector;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static com.ddkcommunity.activities.MainActivity.setTitle;
import static com.ddkcommunity.fragment.wallet.FragmentCreatePassphrase.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FreeFlightVoucherFragment extends Fragment implements OnPageChangeListener, OnLoadCompleteListener {
    private TabLayout tabLayout;
    WebView webView;
    RecyclerView rvFreeFlightActivity;
    List<FreeFlightVoucherList>freeFlightVoucherLists = new ArrayList<>();
    String projectId,heading,document;
    Integer pageNumber = 0;


    public FreeFlightVoucherFragment(String s, String general_inforamtion) {
        this.projectId = s;
        this.heading = general_inforamtion;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_free_flight_voucher, container, false);
        tabLayout = view.findViewById(R.id.tabs_sampd);
        webView = view.findViewById(R.id.webView_document);
        rvFreeFlightActivity = view.findViewById(R.id.rvFreeFlightActivity);

        tabLayout.addTab(tabLayout.newTab().setText("Document"));
        tabLayout.addTab(tabLayout.newTab().setText("Activity"));

        for(int i=0; i < tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 20, 0);
            tab.requestLayout();
        }

        webView.setVisibility(View.VISIBLE);
        pdfGsonbody();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                //progressBar.setVisibility(View.VISIBLE);
                if (tab.getPosition() == 0)
                {
                    rvFreeFlightActivity.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    pdfGsonbody();
                    //final String linkvalue= "http://157.245.52.206/public/uploads/sam_pd/docs/1606810071_dummy.pdf";

                }
                else if (tab.getPosition() == 1)
                {
                    webView.setVisibility(View.GONE);
                    rvFreeFlightActivity.setVisibility(View.VISIBLE);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    rvFreeFlightActivity.setLayoutManager(mLayoutManager);
                    rvFreeFlightActivity.setItemAnimator(new DefaultItemAnimator());
                    acctivityGsonbody();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("Selected", "");

            }
        });

        return view;
    }

    private void acctivityGsonbody() {
        ConnectionDetector cd = new ConnectionDetector(getActivity());
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        } else {
            try {
                JsonObject gsonObject = new JsonObject();
                JSONObject jsonObject1 = new JSONObject();
// jsonObject1.put("ride_id", Constant.ride_id);
                jsonObject1.put("project_id",projectId);
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(jsonObject1.toString());
                getActivityApi(gsonObject);
            } catch (Exception c) {
                c.printStackTrace();
            }
        }
    }

    private void pdfGsonbody() {
        ConnectionDetector cd = new ConnectionDetector(getActivity());
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        } else {
            try {
                JsonObject gsonObjec = new JsonObject();
                JSONObject jsonObject1 = new JSONObject();
// jsonObject1.put("ride_id", Constant.ride_id);
                jsonObject1.put("project_id",projectId);
                JsonParser jsonParser = new JsonParser();
                gsonObjec = (JsonObject) jsonParser.parse(jsonObject1.toString());
                getPdfApi(gsonObjec);
            } catch (Exception c) {
                c.printStackTrace();
            }
        }
    }

    public void getActivityApi(JsonObject gsonObject) {
        LoadInterface apiservice = AppConfig.getClient().create(LoadInterface.class);
        Call<FreeFlightVoucherList> call = apiservice.getFreeVoucherActivity(gsonObject);
        //showProgressDiaog();
        call.enqueue(new Callback<FreeFlightVoucherList>() {
            @Override
            public void onResponse(Call<FreeFlightVoucherList> call, retrofit2.Response<FreeFlightVoucherList> response) {
                int code = response.code();
                String retrofitMesage = "";
                //hideProgress();
                if (code == 200) {
                    int responseCode = response.body().getStatus();
                    retrofitMesage= response.body().getMsg();
                    if (responseCode == 1) {
                        List<FreeFlightVoucherListData>freeFlightVoucherListData = new ArrayList<>();
                        freeFlightVoucherListData = response.body().getData();
                        Gson gson = new Gson();
                        String data  = gson.toJson(freeFlightVoucherListData.toString());
                        Log.d("data","::"+data);

                        FreeFlightVoucherAdapter freeFlightVoucherAdapter = new FreeFlightVoucherAdapter(getActivity(),freeFlightVoucherListData);
                        rvFreeFlightActivity.setAdapter(freeFlightVoucherAdapter);
                        return;
                    }
                    else
                    {
                        Toast.makeText(getActivity(), retrofitMesage, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (code == 404) {
                    //Toast.makeText(getActivity(), "" + com.ddkcommunity.rider.Constant.status_404, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (code == 500) {
                    //Toast.makeText(getActivity(), "" + com.ddkcommunity.rider.Constant.status_500, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "" + retrofitMesage, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<FreeFlightVoucherList> call, Throwable t) {
                Toast.makeText(getActivity(), "Response getting failed", Toast.LENGTH_SHORT).show();
                //hideProgress();
            }
        });
    }

    public void getPdfApi(JsonObject gsonObject) {
        LoadInterface apiservice = AppConfig.getClient().create(LoadInterface.class);
        Call<PdfViewPojo> call = apiservice.getFreeVoucherDocs(gsonObject);
        //showProgressDiaog();
        call.enqueue(new Callback<PdfViewPojo>() {
            @Override
            public void onResponse(Call<PdfViewPojo> call, retrofit2.Response<PdfViewPojo> response) {
                int code = response.code();
                String retrofitMesage = "";
                //hideProgress();
                if (code == 200) {
                    int responseCode = response.body().getStatus();
                    retrofitMesage= response.body().getMsg();
                    Log.d("pdf",response.body().toString());
                    if (responseCode == 1) {
                        PdfViewPojo.PdfModelList pdfModelList = response.body().getData();
                        document = pdfModelList.getDocument();

                        getPdfView(document);
                        return;
                    }
                    else
                    {
                        Toast.makeText(getActivity(), retrofitMesage, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (code == 404) {
                    //Toast.makeText(getActivity(), "" + com.ddkcommunity.rider.Constant.status_404, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (code == 500) {
                    //Toast.makeText(getActivity(), "" + com.ddkcommunity.rider.Constant.status_500, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "" + retrofitMesage, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PdfViewPojo> call, Throwable t) {
                Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_SHORT).show();
                //hideProgress();
            }
        });
    }

    private void getPdfView(String document){
        WebView settings = new WebView(getActivity());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        final ProgressDialog progressBar = ProgressDialog.show(getActivity(), "", "Loading...");
        progressBar.setCanceledOnTouchOutside(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        String pdf = "http://157.245.52.206/public/"+ document;
        progressBar.show();
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" +pdf);
        webView.setWebViewClient(new WebViewClient() {
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
                Toast.makeText(getActivity(), "No Record " + description, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // HomeActivity.setHomeItem(getActivity(), R.id.profile);
        setTitle(heading);
        MainActivity.enableBackViews(true);
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }
}