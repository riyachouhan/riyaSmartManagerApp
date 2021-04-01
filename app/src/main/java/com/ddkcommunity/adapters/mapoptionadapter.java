package com.ddkcommunity.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.CashOutFragmentNew;
import com.ddkcommunity.fragment.CashoutBankFragement;
import com.ddkcommunity.fragment.MatrixFragment;
import com.ddkcommunity.fragment.projects.Mapsubfragmentclick;
import com.ddkcommunity.fragment.send.SendLinkFragment;
import com.ddkcommunity.fragment.send.SuccessFragmentScan;
import com.ddkcommunity.model.AllAvailableBankList;
import com.ddkcommunity.model.BankList;
import com.ddkcommunity.model.PollingHistoryTransction;
import com.ddkcommunity.model.checkRefferalModel;
import com.ddkcommunity.model.mapoptionmodel;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.CommonMethodFunction;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;

public class mapoptionadapter extends RecyclerView.Adapter<mapoptionadapter.MyViewHolder> {

    int count=0;
    ArrayList<mapoptionmodel> mapoptionList;
    private Activity activity;
    String actionname;
    UserResponse userData;
    String selectedview;

    public mapoptionadapter(String selectedview,String actionname,ArrayList<mapoptionmodel> mapoptionList, Activity activity) {
        this.selectedview=selectedview;
        this.mapoptionList=mapoptionList;
        this.activity = activity;
        this.actionname=actionname;
     }

    public void updateData(ArrayList<mapoptionmodel> mapoptionList,String actionname,String selectedview)
    {
        this.selectedview=selectedview;
        this.actionname=actionname;
        this.mapoptionList=  mapoptionList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mapiconview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        try {
               //for php
            userData = AppConfig.getUserData(activity);
            if(actionname.equalsIgnoreCase("main"))
            {
                if (position == 0) {
                    holder.mapoptiontext.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                    holder.backviewshades.setBackground(activity.getDrawable(R.drawable.maproundselectmap));
                } else {
                    holder.mapoptiontext.setTextColor(ContextCompat.getColor(activity, R.color.black_color));
                }
                holder.iconmapop.setImageResource(mapoptionList.get(position).getImageUrl());
                holder.mapoptiontext.setText(mapoptionList.get(position).getName());

                if(mapoptionList.get(position).getName().equalsIgnoreCase("BAWE") || mapoptionList.get(position).getName().equalsIgnoreCase("Import/Export Commodity"))
                {
                  holder.iconbave.setImageResource(mapoptionList.get(position).getImageUrl());
                  holder.iconmapop.setVisibility(View.GONE);
                  holder.iconbave.setVisibility(View.VISIBLE);
                }else
                {
                    holder.iconbave.setVisibility(View.GONE);
                    holder.iconmapop.setVisibility(View.VISIBLE);
                }
            }else
            {
                if(Mapsubfragmentclick.selecetview.equalsIgnoreCase(mapoptionList.get(position).getName()))
                {
                    holder.backviewshades.setBackground(activity.getDrawable(R.drawable.maproundselectmap));
                    holder.mapoptiontext.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                }else
                {
                    holder.backviewshades.setBackground(activity.getDrawable(R.drawable.maproundboxde));
                    holder.mapoptiontext.setTextColor(ContextCompat.getColor(activity, R.color.black_color));
                }
                holder.mapoptiontext.setText(mapoptionList.get(position).getName());
                holder.iconmapop.setImageResource(mapoptionList.get(position).getImageUrl());
            }

            holder.backviewshades.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(actionname.equalsIgnoreCase("main"))
                        {
                            if(position==0)
                           {
                            Fragment fragment = new Mapsubfragmentclick();
                            Bundle arg = new Bundle();
                            arg.putString("action", "map");
                            fragment.setArguments(arg);
                            MainActivity.addFragment(fragment,true);
                            }else if (position==1)
                           {
                               getMatrixToken();
                           }
                            Mapsubfragmentclick.selecetview="";

                        }else
                        {
                            String heardnamer=mapoptionList.get(position).getName();
                            Mapsubfragmentclick.selecetview=heardnamer;
                            if(Mapsubfragmentclick.selecetview.equalsIgnoreCase(heardnamer))
                            {
                                holder.backviewshades.setBackground(activity.getDrawable(R.drawable.maproundselectmap));
                            }else
                            {
                                holder.backviewshades.setBackground(activity.getDrawable(R.drawable.maproundboxde));
                            }
                            if(heardnamer!=null && !heardnamer.equalsIgnoreCase(""))
                            {
                                String selecetpackamount=getPackageamount(heardnamer);
                                Mapsubfragmentclick.packageamount.setText(selecetpackamount.trim()+"");
                            }
                           updateData(mapoptionList,actionname,Mapsubfragmentclick.selecetview);

                        }
                    }
                });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount()
    {
        count = mapoptionList.size();
        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView iconbave,iconmapop;
        TextView mapoptiontext;
        LinearLayout backviewshades;

        public MyViewHolder(View view) {
            super(view);
            iconbave=view.findViewById(R.id.iconbave);
            backviewshades=view.findViewById(R.id.backviewshades);
            mapoptiontext=view.findViewById(R.id.mapoptiontext);
            iconmapop=view.findViewById(R.id.iconmapop);
        }
    }

    private void getMatrixToken()
    {
        final ProgressDialog pd=new ProgressDialog(activity);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Please wait.............");
        pd.show();
        String emailid=userData.getUser().getEmail();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("email", emailid);
        AppConfig.getLoadInterfaceMap().getUserToken(hm).enqueue(new Callback<checkRefferalModel>() {
            @Override
            public void onResponse(Call<checkRefferalModel> call, Response<checkRefferalModel> response) {
                try {
                    Log.d("sam erro par invi",response.body().toString());
                    if (response.isSuccessful() && response.body() != null)
                    {
                        if (response.body().getStatus().equalsIgnoreCase("true"))
                        {
                            pd.dismiss();
                          String token=response.body().getToken();
                            String mapurl= App.pref.getString(Constant.MApURllive,"")+"get-downline?token="+token;
                            String linkvalue=mapurl;
                            Log.d("urllink",linkvalue);
                            if(linkvalue!=null)
                            {
                                //send view
                                Fragment fragment = new SendLinkFragment();
                                Bundle arg = new Bundle();
                                arg.putString("link", linkvalue);
                                fragment.setArguments(arg);
                                MainActivity.addFragment(fragment, true);
                            }else
                            {
                                Toast.makeText(activity, "Link not available ", Toast.LENGTH_SHORT).show();
                            }
                        }else
                        {
                            pd.dismiss();
                            Toast.makeText(activity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else
                    {
                        pd.dismiss();
                        ShowApiError(activity,"server error check-mail-exist");
                    }

                } catch (Exception e) {
                    if (pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<checkRefferalModel> call, Throwable t)
            {
                pd.dismiss();
                Toast.makeText(activity, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getPackageamount(String selecetamount)
    {
        String amountPcakge="";
        if(selecetamount.equalsIgnoreCase("Bronze \n 20-80"))
        {
            // price is larger than 500 and less than 1000
           amountPcakge="20";
        }else if(selecetamount.equalsIgnoreCase("Sliver \n 100-480"))
        {
            // price is larger than 500 and less than 1000
            amountPcakge="100";
        }else if(selecetamount.equalsIgnoreCase("Gold \n 500-980"))
        {
            // price is larger than 500 and less than 1000
            amountPcakge="500";
        }else if(selecetamount.equalsIgnoreCase("Diamond \n 1000-4980"))
        {
            // price is larger than 500 and less than 1000
            amountPcakge="1000";
        }else if(selecetamount.equalsIgnoreCase("Platinum \n 5000-9980"))
        {
            // price is larger than 500 and less than 1000
            amountPcakge="5000";
        }else
        {
            amountPcakge="10000";
        }
        return amountPcakge;
    }
}

