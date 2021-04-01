package com.ddkcommunity.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.activities.SplashActivity;
import com.ddkcommunity.fragment.event.EditEventFragment;
import com.ddkcommunity.fragment.event.EventsFragment;
import com.ddkcommunity.fragment.event.ViewEventFragment;
import com.ddkcommunity.model.Event;
import com.ddkcommunity.utilies.AppConfig;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {


    List<Event> data;
    Activity activity;

    public EventAdapter(List<Event> data, Activity activity) {
        this.activity = activity;

        this.data = data;
    }

    //private List<Movie> moviesList;

    public void updateData(List<Event> data) {

        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.title.setText(data.get(position).getEventName().toUpperCase().charAt(0) + data.get(position).getEventName().substring(1));
        holder.address.setText(data.get(position).getEventAddress());
        holder.date.setText(AppConfig.changeDateInDayMonth(data.get(position).getEventStartDate() + " " + data.get(position).getEventStartTime()));
        holder.time.setText(AppConfig.changeTimeInHour(data.get(position).getEventStartDate() + " " + data.get(position).getEventStartTime()));

        //holder.time.setText(String.format("%s %s to %s %s", data.get(position).getEventStartDate(), data.get(position).getEventStartTime(), data.get(position).getEventEndDate(), data.get(position).getEventEndTime()));
        if (App.pref.getString(Constant.USER_ID, "").equals(data.get(position).getCreatedBy())) {

            holder.editIV.setVisibility(View.VISIBLE);
            holder.deleteIV.setVisibility(View.VISIBLE);
            holder.joinBT.setVisibility(View.GONE);

        } else {

            holder.editIV.setVisibility(View.GONE);
            holder.deleteIV.setVisibility(View.GONE);
            holder.joinBT.setVisibility(View.GONE);

        }


        if (data.get(position).getEventStatus().equalsIgnoreCase("Current")) {
            holder.shareIV.setVisibility(View.VISIBLE);
        } else {
            holder.shareIV.setVisibility(View.GONE);
        }

        Glide.with(activity)
                .asBitmap()
                .load(data.get(position).getEventImage())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        holder.imageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);

                        holder.imageView.setImageResource(R.drawable.default_photo);
                    }

                });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void deleteEventCall(String createdBy, final String eventId, final int index) {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(activity);
            AppConfig.showLoading(dialog, "Deleting Event..");

            Call<ResponseBody> call = AppConfig.getLoadInterface().deleteEvent(AppConfig.getStringPreferences(activity, Constant.JWTToken), AppConfig.setRequestBody(eventId));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {

                                AppConfig.showToast(object.getString("msg"));
                                data.remove(index);
                                EventsFragment.deleteEvent(eventId);
                                notifyDataSetChanged();

                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity(activity);
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
                        ShowApiError(activity,"server error in delete-event");
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, time, date, address;
        public ImageView editIV, deleteIV, shareIV, imageView;
        public Button joinBT;

        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title_TV);
            time = view.findViewById(R.id.time_TV);
            date = view.findViewById(R.id.date_TV);
            address = view.findViewById(R.id.address_TV);
            editIV = view.findViewById(R.id.edit_IV);
            deleteIV = view.findViewById(R.id.delete_IV);
            shareIV = view.findViewById(R.id.share_IV);
            joinBT = view.findViewById(R.id.join_BT);
            imageView = view.findViewById(R.id.image_view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    DataHolder.setEvent(data.get(getAdapterPosition()));
                    MainActivity.addFragment(new ViewEventFragment(data.get(getAdapterPosition())), true);
                }
            });

            shareIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = data.get(getAdapterPosition()).getEventUrl() + "?share_id=" + AppConfig.stringToBase64(App.pref.getString(Constant.USER_ID, "")) + "&event_id=" + AppConfig.stringToBase64(data.get(getAdapterPosition()).getEventId());
                    AppConfig.copyAndShareLink("Follow the link to register for the event ", url, "Share Event", activity);
                }
            });


            editIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    DataHolder.setEvent(data.get(getAdapterPosition()));
                    MainActivity.addFragment(new EditEventFragment(data.get(getAdapterPosition())), true);
                }
            });

            deleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                    alertDialogBuilder.setTitle("Delete Event").setMessage("Are You Sure want to delete \" " + data.get(getAdapterPosition()).getEventName() + "\" event ? ").
                            setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    deleteEventCall(data.get(getAdapterPosition()).getCreatedBy(), data.get(getAdapterPosition()).getEventId(), getAdapterPosition());
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });

        }
    }


}
