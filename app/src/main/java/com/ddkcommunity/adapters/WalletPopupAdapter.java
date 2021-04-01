package com.ddkcommunity.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.fragment.HomeFragment;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class WalletPopupAdapter extends RecyclerView.Adapter<WalletPopupAdapter.MyViewHolder> {


    private List<Credential> data;
    private Activity activity;
    private EditText searchET;

    public WalletPopupAdapter(List<Credential> data, Activity activity, EditText searchET) {
        this.activity = activity;
        this.searchET = searchET;
        this.data = data;
    }


    public void updateData(List<Credential> data) {

        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wallet, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.title.setText(data.get(position).getName());
        holder.ddk.setText(data.get(position).getDdkcode());

        if (App.pref.getString(Constant.WALLET_ID, "").equals(data.get(position).getWalletId())) {
            holder.online.setVisibility(View.VISIBLE);
        } else {
            holder.online.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, ddk;
        ImageView online;

        public MyViewHolder(View view) {
            super(view);

            online = view.findViewById(R.id.online);
            title = view.findViewById(R.id.title_TV);
            ddk = view.findViewById(R.id.ddk_code_TV);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if (online.getVisibility() == View.GONE) {
                       MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(activity);
                        alertDialogBuilder
                                .setTitle("Switch Wallet?")
                                .setMessage("Are you sure want to switch Wallet")
                                .setPositiveButton("Switch", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        App.editor.putString(Constant.WALLET_ID, data.get(getAdapterPosition()).getWalletId());
                                        App.editor.putString(Constant.Secret, data.get(getAdapterPosition()).getPassphrase());
                                        App.editor.putBoolean(Constant.isWallet, true);
                                        App.editor.commit();
                                        String walletidvalue=data.get(getAdapterPosition()).getWalletId();
                                        if(walletidvalue!=null && !walletidvalue.equalsIgnoreCase("")) {
                                            HomeFragment.getWalletDetails(walletidvalue,data.get(getAdapterPosition()).getPassphrase());
                                        }
                                        searchET.setText("");
                                        updateData(data);
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }else
                    {
                        HomeFragment.pw.dismiss();
                    }
                }
            });
        }
    }
}
