package com.ddkcommunity.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.activities.SplashActivity;
import com.ddkcommunity.fragment.credential.CredentialsFragment;
import com.ddkcommunity.fragment.credential.EditCredentialsFragment;
import com.ddkcommunity.fragment.credential.ViewCredentialsFragment;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.utilies.AppConfig;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

public class CredentialAdapter extends RecyclerView.Adapter<CredentialAdapter.MyViewHolder> {


    List<Credential> data;
    Activity activity;

    public CredentialAdapter(List<Credential> data, Activity activity) {
        this.activity = activity;
        this.data = data;
    }

    //private List<Movie> moviesList;

    public void updateData(List<Credential> data) {

        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_credential, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (data.get(position).wallet_type != null && data.get(position).wallet_type.equalsIgnoreCase("default")) {
            holder.ivMoreMenu.setVisibility(View.GONE);
        } else {
            holder.ivMoreMenu.setVisibility(View.VISIBLE);
        }

        holder.title.setText(data.get(position).getName().toUpperCase().charAt(0) + data.get(position).getName().substring(1));
        holder.head.setText(data.get(position).getName().toUpperCase().charAt(0) + data.get(position).getName().substring(1));
        holder.time.setText(String.format("DDK Address: %s", data.get(position).getDdkcode()));

        holder.ivMoreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new BottomSheetDialog(activity, R.style.SheetDialog);
                d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                d.setContentView(R.layout.bottom_dialog);
                View view = d.findViewById(R.id.bs);
                ((View) view.getParent()).setBackgroundColor(Color.TRANSPARENT);
                d.findViewById(R.id.tvEdit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                        MainActivity.addFragment(new EditCredentialsFragment(data.get(position)), true);
                    }
                });

                d.findViewById(R.id.tvDelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(activity);
                    alertDialogBuilder.setTitle("Delete Credential").setMessage("Are You Sure want to delete \" " + data.get(position).getName() + "\"?").
                            setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    deleteCredential(data.get(position).getCreatedBy(), data.get(position).getCredentialId(), position);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();                    }
                });

                d.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.setCancelable(true);
                d.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void deleteCredential(String createdBy, final String eventId, final int index) {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(activity);
            AppConfig.showLoading(dialog, "Deleting Credential..");

            Call<ResponseBody> call = AppConfig.getLoadInterface().deleteCredential(AppConfig.getStringPreferences(activity, Constant.JWTToken), AppConfig.setRequestBody(eventId));

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
                                CredentialsFragment.deleteEvent(eventId);
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
                        ShowApiError(activity,"server error in delete-credentials");
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
        public TextView title, time, head;
        public ImageView shareIV, imageView;
        private AppCompatImageView ivMoreMenu;

        public MyViewHolder(View view) {
            super(view);

            ivMoreMenu = view.findViewById(R.id.ivMoreMenu);
            title = view.findViewById(R.id.title_TV);
            time = view.findViewById(R.id.ddk_TV);
            head = view.findViewById(R.id.head_name_TV);
//            editIV = view.findViewById(R.id.edit_IV);
//            deleteIV = view.findViewById(R.id.delete_IV);
//            shareIV = view.findViewById(R.id.share_IV);
//            imageView = view.findViewById(R.id.image_view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    DataHolder.setCredential(data.get(getAdapterPosition()));
                    MainActivity.addFragment(new ViewCredentialsFragment(data.get(getAdapterPosition())), true);
                }
            });

//            shareIV.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    DataHolder.setCredential(data.get(getAdapterPosition()));
////                    MainActivity.addFragment(new ViewCredentialsFragment(), true);
//                }
//            });

//            editIV.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    DataHolder.setCredential(data.get(getAdapterPosition()));
//                    MainActivity.addFragment(new EditCredentialsFragment(data.get(getAdapterPosition())), true);
//                }
//            });
//
//            deleteIV.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
//                    alertDialogBuilder.setTitle("Delete Credential").setMessage("Are You Sure want to delete \" " + data.get(getAdapterPosition()).getName() + "\"?").
//                            setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    deleteCredential(data.get(getAdapterPosition()).getCreatedBy(), data.get(getAdapterPosition()).getCredentialId(), getAdapterPosition());
//                                }
//                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).show();
//                }
//            });

        }
    }
}
