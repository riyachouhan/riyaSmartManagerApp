package com.ddkcommunity.fragment.event;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.activities.SplashActivity;
import com.ddkcommunity.model.Event;
import com.ddkcommunity.utilies.AppConfig;
import com.google.gson.JsonSyntaxException;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEventFragment extends Fragment implements View.OnClickListener {
    EditText nameEt, addressEt;
    TextView startDateTV, startTimeTV, endDateTV, endTimeTV, eventStatusTv;
    RadioGroup typeRG;
    RadioButton publicRB, privateRB;
    ImageView imageView;
    String imagePath = "", status;
    Date eDate, sDate;
    int position = 0;
    private Event eventData;

    public EditEventFragment(Event event) {
        this.eventData = event;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        nameEt = view.findViewById(R.id.event_name_ET);
        addressEt = view.findViewById(R.id.event_address_ET);
        startDateTV = view.findViewById(R.id.start_date_TV);
        startTimeTV = view.findViewById(R.id.start_time_TV);
        endDateTV = view.findViewById(R.id.end_date_TV);
        endTimeTV = view.findViewById(R.id.end_time_TV);
        eventStatusTv = view.findViewById(R.id.event_status_TV);
        typeRG = view.findViewById(R.id.event_type_RG);
        publicRB = view.findViewById(R.id.public_RB);
        privateRB = view.findViewById(R.id.private_RB);
        imageView = view.findViewById(R.id.image_view);

        startDateTV.setOnClickListener(this);
        startTimeTV.setOnClickListener(this);
        endDateTV.setOnClickListener(this);
        endTimeTV.setOnClickListener(this);
        eventStatusTv.setOnClickListener(this);


        nameEt.setText(eventData.getEventName());
        addressEt.setText(eventData.getEventAddress());
        startDateTV.setText(eventData.getEventStartDate());
        startTimeTV.setText(eventData.getEventStartTime());
        endDateTV.setText(eventData.getEventEndDate());
        endTimeTV.setText(eventData.getEventEndTime());
        eventStatusTv.setText(eventData.getEventStatus());

        view.findViewById(R.id.submit_BT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEt.getText().toString();
                String address = addressEt.getText().toString();
                String startDate = startDateTV.getText().toString();
                String startTime = startTimeTV.getText().toString();
                String endDate = endDateTV.getText().toString();
                String endTime = endTimeTV.getText().toString();
                String status = eventStatusTv.getText().toString();
                String type = ((RadioButton) view.findViewById(typeRG.getCheckedRadioButtonId())).getText().toString();


                if (!AppConfig.isStringNullOrBlank(name)) {
                    if (!AppConfig.isStringNullOrBlank(address)) {
                        if (!AppConfig.isStringNullOrBlank(startDate)) {
                            if (!AppConfig.isStringNullOrBlank(startTime)) {
                                if (!AppConfig.isStringNullOrBlank(endDate)) {
                                    if (!AppConfig.isStringNullOrBlank(endTime)) {
                                        if (!imagePath.equals("")) {
                                            updateEventCall(App.pref.getString(Constant.USER_ID, ""), name,
                                                    address, startDate, startTime, endDate, endTime, status, type, imagePath, eventData.getEventId());

                                        } else {
                                            updateEventCall(App.pref.getString(Constant.USER_ID, ""), name,
                                                    address, startDate, startTime, endDate, endTime, status, type, eventData.getEventId());
                                        }
                                    } else {
                                        AppConfig.showToast("Event End Time Required.");
                                    }
                                } else {
                                    AppConfig.showToast("Event End Date Required.");
                                }
                            } else {
                                AppConfig.showToast("Event Start Time Required.");
                            }
                        } else {
                            AppConfig.showToast("Event Start Date Required.");
                        }
                    } else {
                        AppConfig.showToast("Event Address Required.");
                    }
                } else {
                    AppConfig.showToast("Event Name Required.");
                }
            }
        });


        if (eventData.getEventType().equalsIgnoreCase("public")) {
            publicRB.setChecked(true);
        } else {
            privateRB.setChecked(true);
        }

        Glide.with(getActivity())
                .asBitmap()
                .load(eventData.getEventImage())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);

                        imageView.setImageResource(R.drawable.default_photo);
                    }

                });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final PickImageDialog dialog = PickImageDialog.build(new PickSetup());
                dialog.setOnPickCancel(new IPickCancel() {
                    @Override
                    public void onCancelClick() {
                        dialog.dismiss();
                    }
                }).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        //TODO: do what you have to...
                        imagePath = r.getPath();
                        imageView.setImageBitmap(r.getBitmap());
                    }
                }).show(getActivity().getSupportFragmentManager());
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void updateEventCall(String id, String name, String address, String startDate, String startTime, String endDate, String endTime, String status, String type, String imagePath, String eventId) {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            AppConfig.showLoading(dialog, "Updating Event..");

            File file = new File(imagePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("event_image", file.getName(), requestFile);

            Call<ResponseBody> call = AppConfig.getLoadInterface().updateEvent(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken),
                    AppConfig.setRequestBody(name),
                    AppConfig.setRequestBody(address),
                    AppConfig.setRequestBody(startDate),
                    AppConfig.setRequestBody(endDate),
                    AppConfig.setRequestBody(startTime),
                    AppConfig.setRequestBody(endTime),
                    AppConfig.setRequestBody(status),
                    AppConfig.setRequestBody(type),
                    AppConfig.setRequestBody(eventId),
                    body
            );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {

                                AppConfig.showToast(object.getString("msg"));
                                getActivity().onBackPressed();
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
                        ShowApiError(getActivity(),"server error in edit-event");
                    }
                    AppConfig.hideLoading(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppConfig.hideLoading(dialog);
                    errordurigApiCalling(getActivity(),t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    private void updateEventCall(String id, String name, String address, String startDate, String startTime, String endDate, String endTime, String status, String type, String eventId) {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            AppConfig.showLoading(dialog, "Updating Event..");

            Call<ResponseBody> call = AppConfig.getLoadInterface().updateEvent(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken),
                    AppConfig.setRequestBody(name),
                    AppConfig.setRequestBody(address),
                    AppConfig.setRequestBody(startDate),
                    AppConfig.setRequestBody(endDate),
                    AppConfig.setRequestBody(startTime),
                    AppConfig.setRequestBody(endTime),
                    AppConfig.setRequestBody(status),
                    AppConfig.setRequestBody(type),
                    AppConfig.setRequestBody(eventId)

            );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {

                                AppConfig.showToast(object.getString("msg"));
                                getActivity().onBackPressed();

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
                        ShowApiError(getActivity(),"server error in edit-event");
                    }
                    AppConfig.hideLoading(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppConfig.hideLoading(dialog);
                    t.printStackTrace();
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }


    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        switch (v.getId()) {
            case R.id.start_date_TV:

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String month = "";
                        monthOfYear = monthOfYear + 1;
                        String day = "";

                        if (monthOfYear < 10) {
                            month = "0" + String.valueOf(monthOfYear);
                        } else month = String.valueOf(monthOfYear);

                        if (dayOfMonth < 10) {
                            day = "0" + String.valueOf(dayOfMonth);
                        } else day = String.valueOf(dayOfMonth);

                        startDateTV.setText(year + "-" + month + "-" + day);
                        String EndD = year + "-" + month + "-" + day;
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        sDate = null;
                        try {
                            sDate = formatter.parse(EndD);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Start Date is " + sDate.getTime());
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                if (eDate != null) {
                    datePickerDialog.getDatePicker().setMaxDate(eDate.getTime());
                }
                datePickerDialog.show();
                break;
            case R.id.start_time_TV:

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int mHour,
                                                  int mMin) {
                                String min, hour;
                                if (mMin < 10) {
                                    min = "0" + String.valueOf(mMin);
                                } else min = String.valueOf(mMin);

                                if (mHour < 10) {
                                    hour = "0" + String.valueOf(mHour);
                                } else hour = String.valueOf(mHour);
                                startTimeTV.setText(hour + ":" + min);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.end_date_TV:

                DatePickerDialog datePickerDialog1 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String month = "";
                        monthOfYear = monthOfYear + 1;

                        String day = "";

                        if (monthOfYear < 10) {
                            month = "0" + String.valueOf(monthOfYear);
                        } else month = String.valueOf(monthOfYear);

                        if (dayOfMonth < 10) {
                            day = "0" + String.valueOf(dayOfMonth);
                        } else day = String.valueOf(dayOfMonth);

                        endDateTV.setText(year + "-" + month + "-" + day);
                        String EndD = year + "-" + month + "-" + day;
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        eDate = null;
                        try {
                            eDate = formatter.parse(EndD);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        System.out.println("End Date is " + eDate.getTime());

                    }
                }, mYear, mMonth, mDay);

                if (sDate != null)
                    datePickerDialog1.getDatePicker().setMinDate(sDate.getTime());
                else
                    datePickerDialog1.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog1.show();
                break;
            case R.id.end_time_TV:
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int mHour,
                                                  int mMin) {
                                String min, hour;
                                if (mMin < 10) {
                                    min = "0" + String.valueOf(mMin);
                                } else min = String.valueOf(mMin);

                                if (mHour < 10) {
                                    hour = "0" + String.valueOf(mHour);
                                } else hour = String.valueOf(mHour);
                                endTimeTV.setText(hour + ":" + min);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog1.show();
                break;
            case R.id.event_status_TV:
                final String[] gender = {"current", "cancelled", "archive"};

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Select Event Status").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.setSingleChoiceItems(gender, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        status = gender[which];
                        position = which;
                        eventStatusTv.setText(status);
                        dialog.dismiss();
                    }
                });
                alert.show();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.setTitle("Edit Event");
    }
}
