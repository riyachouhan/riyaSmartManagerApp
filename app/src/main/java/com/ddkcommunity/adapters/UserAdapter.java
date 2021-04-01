package com.ddkcommunity.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.OTPActivity;
import com.ddkcommunity.model.LoggedUser;
import com.ddkcommunity.utilies.AppConfig;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {


    private List<LoggedUser.LoggedUserData> data;
    private Activity activity;
    private String imagePath;
    private String deviceId;

    public UserAdapter(List<LoggedUser.LoggedUserData> data, Activity activity, String imagePath, String deviceId) {
        this.activity = activity;
        this.data = data;
        this.imagePath = imagePath;
        this.deviceId = deviceId;
    }

    public void updateData(List<LoggedUser.LoggedUserData> data, String imagePath, String deviceId) {
        this.data = data;
        this.imagePath = imagePath;
        this.deviceId = deviceId;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if (("" + data.get(position).email).equalsIgnoreCase(App.pref.getString(Constant.USER_EMAIL, ""))) {
            holder.removeIV.setVisibility(View.GONE);
        } else {
            holder.removeIV.setVisibility(View.VISIBLE);
        }

        holder.title.setText(data.get(position).name);

        if (data.get(position).user_image != null && !data.get(position).user_image.isEmpty()) {
            Glide.with(activity)
                    .asBitmap()
                    .load(imagePath + "/" + data.get(position).user_image)
                    .placeholder(ContextCompat.getDrawable(activity, R.drawable.defalut_profile))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            holder.imageView.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);

                            holder.imageView.setImageResource(R.drawable.defalut_profile);
                        }

                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public CircleImageView imageView;
        public ImageView removeIV;


        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.head_name_TV);

            imageView = view.findViewById(R.id.image_view);
            removeIV = view.findViewById(R.id.remove_IV);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    //for user switch hide temparary..............start
                  /*  MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(activity);
                    alertDialogBuilder.setTitle("Switch Account").setMessage("Do you want to Switch Account?")
                            .setPositiveButton("Switch", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
//                                    App.editor.putString(Constant.USER_ID, data.get(getAdapterPosition()).user_id.toString());
//                                    App.editor.apply();
                                    SendOtpCall("" + data.get(getAdapterPosition()).user_id, data.get(getAdapterPosition()).email, "login");
//                                    ProfileFragment.getProfileCall("" + data.get(getAdapterPosition()).user_id);
//                                    notifyDataSetChanged();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();*/
                  //..................end
                }
            });

            removeIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(activity);
                    alertDialogBuilder.setTitle("Remove Account?").setMessage("Do you want to Remove Account?")
                            .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    HashMap<String, String> hm = new HashMap<>();
                                    hm.put("user_id", "" + data.get(getAdapterPosition()).user_id);
                                    hm.put("device_id", deviceId);
                                    AppConfig.getLoadInterface().deleteUser(AppConfig.getStringPreferences(activity, Constant.JWTToken), hm).enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()) {
                                                try {
                                                    String responseData = response.body().string();
                                                    JSONObject object = new JSONObject(responseData);
                                                    if (object.getInt(Constant.STATUS) == 1) {
                                                        AppConfig.showToast(object.getString("msg"));
                                                        data.remove(data.get(getAdapterPosition()));
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
                                                ShowApiError(activity,"server error in delete-user-logged-in-device");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                                        }
                                    });
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

    private void openHomeActivity(String email, String user_id) {
        Intent intent = new Intent(activity, OTPActivity.class);

        App.editor.putString("email", email);
//        App.editor.putString(Constant.USER_ID, user_id);
        App.editor.putString(Constant.USER_EMAIL, email);
        App.editor.putString("switchUser", "yes");
        App.editor.commit();

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();

    }


    private void SendOtpCall(final String userId, final String email, String verify) {

        final ProgressDialog dialog = new ProgressDialog(activity);
        AppConfig.showLoading(dialog, "Send OTP...");

        Call<ResponseBody> call = AppConfig.getLoadInterface().resendOtp(
                AppConfig.getStringPreferences(activity, Constant.JWTToken),
                AppConfig.setRequestBody(email), AppConfig.setRequestBody(verify));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(AppConfig.STATUS) == 1) {
                            openHomeActivity(email, userId);
                        } else if (object.getInt(AppConfig.STATUS) == 0) {
                            AppConfig.showToast(object.getString("msg"));
                        } else if (object.getInt(Constant.STATUS) == 3) {
                            AppConfig.showToast(object.getString("msg"));
                            AppConfig.openSplashActivity(activity);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ShowApiError(activity,"server error in recent_otp");
                }
                AppConfig.hideLoading(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppConfig.hideLoading(dialog);
                t.printStackTrace();
            }
        });
    }


}
