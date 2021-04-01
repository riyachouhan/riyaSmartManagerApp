package com.ddkcommunity.fragment.projects;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.send.SuccessFragmentScan;
import com.ddkcommunity.model.checkRefferalModel;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.activities.MainActivity.setTitle;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsAndConsitionSubscription extends Fragment implements OnPageChangeListener,OnLoadCompleteListener,View.OnClickListener {

    public TermsAndConsitionSubscription() {

    }

    UserResponse userData;
    public static final String SAMPLE_FILE = "terms_comdition.pdf";
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName,activityaction;
    private TextView bottom_view;
    CheckBox simpleCheckBox;
    private TextView btnGoBack,file_content;
    ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.terms_and_condition, container, false);
        try {
            if(getArguments().getString("activityaction")!=null) {
                activityaction= getArguments().getString("activityaction");
            }
            simpleCheckBox=view.findViewById(R.id.simpleCheckBox);
            bottom_view= view.findViewById(R.id.bottom_view);
            bottom_view.setOnClickListener(this);
            userData = AppConfig.getUserData(getContext());
            //........
            pdfView=view.findViewById(R.id.pdfView);
            displayFromAsset(SAMPLE_FILE);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return view;
    }

    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;
        pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(getActivity()))
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
     //   setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.bottom_view)
        {
            Boolean checkBoxState = simpleCheckBox.isChecked();
            if (checkBoxState)
            {
                if(!activityaction.equalsIgnoreCase("subscription"))
                {
                    CheckUserActiveStaus();
                }else
                {
                    MainActivity.addFragment(new SelectPaymentPoolingFragment(), true);
                }
            } else {
                Toast.makeText(getActivity(), "Please select check box", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // HomeActivity.setHomeItem(getActivity(), R.id.profile);
        setTitle("Terms And Condition");
        MainActivity.enableBackViews(true);
    }

    private void CheckUserActiveStaus()
    {
        String emailid=userData.getUser().getEmail();
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Please wait ....");
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
                                MainActivity.addFragment(new MapreferralFragment(), false);
                            }
                        }
                    } else {
                        ShowApiError(getContext(),"server error check-mail-exist");
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
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}