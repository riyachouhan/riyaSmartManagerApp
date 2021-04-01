package com.ddkcommunity.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.adapters.OurTeamAdapter;
import com.ddkcommunity.fragment.send.SendDDkFragment;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.model.OurTeamData;
import com.ddkcommunity.utilies.AppConfig;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

/**
 * A simple {@link Fragment} subclass.
 */
public class OurTeamFragment extends Fragment {

    private ArrayList<OurTeamData> arrayList;

    public OurTeamFragment() {
        // Required empty public constructor
    }

    private RecyclerView rvOurTeam;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_our_team, container, false);
        mContext = getActivity();
        rvOurTeam = view.findViewById(R.id.rvOurTeam);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvOurTeam.setLayoutManager(mLayoutManager);
        rvOurTeam.setItemAnimator(new DefaultItemAnimator());
        getOutTeam(getActivity());
        //AppConfig.openOkDialog1(mContext);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.setTitle("Our Team");
        MainActivity.enableBackViews(true);
    }

    public void getOutTeam(final Activity activity) {
        if (AppConfig.isInternetOn())
        {
            final ProgressDialog pd=new ProgressDialog(getContext());
            pd.setMessage("Please wait ............");
            pd.show();
            Call<ResponseBody> call = AppConfig.getLoadInterface().getOurTeam();
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            pd.dismiss();
                            String responseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            String status=jsonObject.getString("status");
                            if(status.equalsIgnoreCase("1"))
                            {
                                arrayList = new ArrayList<>();
                                JSONArray jObject = jsonObject.getJSONArray("data");
                                for(int i=0;i<jObject.length();i++)
                                {
                                    JSONObject jsondataObject =jObject.getJSONObject(i);
                                    arrayList.add(new OurTeamData( jsondataObject.getString("content"),
                                            jsondataObject.getString("profile_img"),
                                            jsondataObject.getString("name"),
                                            jsondataObject.getString("mobile_no"),
                                            jsondataObject.getString("email"),
                                            jsondataObject.getString("designation"),
                                            jsondataObject.getString("whatsapp_no"),
                                            jsondataObject.getString("fb_url"),
                                            jsondataObject.getString("instagram_url"),
                                            jsondataObject.getString("twitter_url"),
                                            jsondataObject.getString("youtube_url")));
                                }
                                if(arrayList.size()>0)
                                {
                                    OurTeamAdapter mAdapter = new OurTeamAdapter(arrayList, mContext, new OurTeamAdapter.SetOnClickListener() {
                                        @Override
                                        public void setClick(int position)
                                        {
                                            Fragment fragment = new OurTeamProfileFragment();
                                            Bundle arg = new Bundle();
                                            arg.putString("name",arrayList.get(position).name);
                                            arg.putString("email",arrayList.get(position).email);
                                            arg.putString("mobile_no",arrayList.get(position).phone);
                                            arg.putString("designation",arrayList.get(position).designation);
                                            arg.putString("content", String.valueOf(arrayList.get(position).description));
                                            arg.putString("profile_img",arrayList.get(position).drawable);
                                            arg.putString("whatsapp_no",arrayList.get(position).whatsapp_no);
                                            arg.putString("fb_url",arrayList.get(position).fb_url);
                                            arg.putString("instagram_url", String.valueOf(arrayList.get(position).instagram_url));
                                            arg.putString("twitter_url",arrayList.get(position).twitter_url);
                                            arg.putString("youtube_url",arrayList.get(position).youtube_url);
                                            fragment.setArguments(arg);
                                            MainActivity.addFragment(fragment,true);
                                        }
                                    });
                                    rvOurTeam.setAdapter(mAdapter);
                                }
                            }else
                            {
                                pd.dismiss();
                                String msg=jsonObject.getString("msg");
                                Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e)
                        {
                            pd.dismiss();
                            ShowApiError(activity,"error in json exception ninethface/our-team");
                            e.printStackTrace();
                        }
                    } else {
                        pd.dismiss();
                        ShowApiError(activity,"server error in ninethface/our-team");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pd.dismiss();
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

}
