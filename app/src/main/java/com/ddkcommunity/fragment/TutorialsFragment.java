package com.ddkcommunity.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.activities.SplashActivity;
import com.ddkcommunity.adapters.TutorialAdapter;
import com.ddkcommunity.model.Tutorial;
import com.ddkcommunity.model.TutorialResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialsFragment extends Fragment {

    private static List<Tutorial> eventList = new ArrayList<>();
    EditText searchEt;
    TutorialAdapter mAdapter;
    private RecyclerView recyclerView;
    private Context mContext;

    public TutorialsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutorials, container, false);
        mContext = getActivity();
        recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter = new TutorialAdapter(eventList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        searchEt = view.findViewById(R.id.search_ET);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(searchEt.getText().toString());
            }
        });
        upcomingEventsCall();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //HomeActivity.setHomeItem(getActivity(), R.id.tutorials);
        MainActivity.setTitle("Tutorial");
        MainActivity.enableBackViews(true);
    }

    private void upcomingEventsCall() {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            AppConfig.showLoading(dialog, "Fetching Tutorial..");

            Call<ResponseBody> call = AppConfig.getLoadInterface().tutorials(AppConfig.getStringPreferences(mContext, Constant.JWTToken));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getString(Constant.STATUS).equals("1")) {
                                TutorialResponse registerResponse = new Gson().fromJson(responseData, TutorialResponse.class);

                                eventList = registerResponse.getTutorial();
                                mAdapter.updateData(registerResponse.getTutorial());

                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity(getActivity());
                            }else {
                                AppConfig.showToast(object.getString("msg"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ShowApiError(getActivity(),"server error in get-tutorials");
                    }
                    AppConfig.hideLoading(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppConfig.hideLoading(dialog);
                    t.printStackTrace();
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    private void filter(String newText) {
        if (eventList.size() > 0) {
            List<Tutorial> doctorNew = new ArrayList<>();

            if (newText.isEmpty()) {
                doctorNew.addAll(eventList);
            } else {
                for (Tutorial event : eventList) {
                    if (event.getTutorialTitle().toLowerCase().contains(newText.toLowerCase())) {
                        doctorNew.add(event);
                    }
                }

                if (doctorNew.size() == 0) {
                    AppConfig.showToast("No search data Found.");
                }
            }
            mAdapter.updateData(doctorNew);
        }
    }
}
