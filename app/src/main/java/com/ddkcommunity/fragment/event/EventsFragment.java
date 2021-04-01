package com.ddkcommunity.fragment.event;


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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.activities.SplashActivity;
import com.ddkcommunity.adapters.EventAdapter;
import com.ddkcommunity.model.Event;
import com.ddkcommunity.model.EventsResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {
    private static List<Event> eventList = new ArrayList<>();
    EditText searchEt;
    private RecyclerView recyclerView;
    private EventAdapter mAdapter;
    private Context mContext;

    public EventsFragment() {
        // Required empty public constructor
    }

    public static void deleteEvent(String eventId) {

        for (int i = 0; i < eventList.size(); i++) {
            if (eventId.equals(eventList.get(i).getEventId())) {
                eventList.remove(i);

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        mContext = getActivity();
        recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter = new EventAdapter(eventList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
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
        view.findViewById(R.id.add_IV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.addFragment(new AddEventFragment(), true);
            }
        });

        return view;
    }

    private void filter(String newText) {
        if (eventList.size() > 0) {
            List<Event> doctorNew = new ArrayList<>();

            if (newText.isEmpty()) {
                doctorNew.addAll(eventList);
            } else {
                for (Event event : eventList) {
                    if (event.getEventName().toLowerCase().contains(newText.toLowerCase()) ||
                            event.getEventAddress().toLowerCase().contains(newText.toLowerCase()) ||
                            event.getEventStatus().toLowerCase().contains(newText.toLowerCase()) ||
                            AppConfig.changeDateInDayMonth(event.getEventStartDate() + " " + event.getEventStartTime()).toLowerCase().contains(newText.toLowerCase()) ||
                            AppConfig.changeTimeInHour(event.getEventStartDate() + " " + event.getEventStartTime()).toLowerCase().contains(newText.toLowerCase()) ||
                            event.getEventType().toLowerCase().contains(newText.toLowerCase())) {
                        doctorNew.add(event);
                    }
                }
                if (doctorNew.size() == 0) {
                    AppConfig.showToast("No search data found.");
                }
            }
            mAdapter.updateData(doctorNew);
        } else {
            AppConfig.showToast("No search data found.");
        }
    }

    private void upcomingEventsCall() {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            AppConfig.showLoading(dialog, "Fetching Upcoming Events..");

            Call<ResponseBody> call = AppConfig.getLoadInterface().eventList(AppConfig.getStringPreferences(mContext, Constant.JWTToken));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {
                                EventsResponse registerResponse = new Gson().fromJson(responseData, EventsResponse.class);

                           /* eventList = new ArrayList<>();
                            for (Event event : registerResponse.getEvents()){
                                if (event.getEventStatus().equalsIgnoreCase("Current")){
                                    eventList.add(event);
                                }
                            }*/
                                eventList = registerResponse.getEvents();
                                String event = "";
                                for (int position = 0; position < eventList.size(); position++) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    Date strDate = null;
                                    try {

                                        strDate = sdf.parse(eventList.get(position).getEventEndDate() + " " + eventList.get(position).getEventEndTime());
                                        if (System.currentTimeMillis() > strDate.getTime() &&
                                                (
                                                        eventList.get(position).getEventStatus().equalsIgnoreCase("current") ||
                                                                eventList.get(position).getEventStatus().equalsIgnoreCase("cancelled"))) {
                                            if (event.equalsIgnoreCase("")) {
                                                event = eventList.get(position).getEventId();
                                            } else {
                                                event = String.format("%s,%s", event, eventList.get(position).getEventId());
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                                if (!event.equalsIgnoreCase("")) {
                                    updateEventCall(App.pref.getString(Constant.USER_ID, ""), event);
                                } else
                                    mAdapter.updateData(registerResponse.getEvents());

                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity(getActivity());
                            } else {
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
                        ShowApiError(getActivity(),"server error in get-event-list");
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

    @Override
    public void onResume() {
        super.onResume();
        upcomingEventsCall();
        MainActivity.setTitle("Events");
        MainActivity.enableBackViews(true);

    }


    private void updateEventCall(String userId, String id) {
        if (AppConfig.isInternetOn()) {
            Call<ResponseBody> call = AppConfig.getLoadInterface().updateArchive(AppConfig.getStringPreferences(mContext, Constant.JWTToken), AppConfig.setRequestBody(id));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {
                                upcomingEventsCall();
                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity(getActivity());
                            } else {
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
                        ShowApiError(getActivity(),"server error in update-to-archive");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }
}
