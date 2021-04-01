package com.ddkcommunity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.CashOutFragmentNew;
import com.ddkcommunity.fragment.HomeFragment;
import com.ddkcommunity.fragment.SamKoinsRedeemFragment;
import com.ddkcommunity.fragment.SamTransactionDDKFragment;
import com.ddkcommunity.model.RedeemOptionModel;

import java.util.ArrayList;

public class RedeemChoiceAdpater extends RecyclerView.Adapter<RedeemChoiceAdpater.MyViewHolder> {

    private ArrayList<RedeemOptionModel.Datum> redeemotpionlist;
    private Context activity;
    AlertDialog dialog;
    String userSelection;

    public RedeemChoiceAdpater(AlertDialog dialog, ArrayList<RedeemOptionModel.Datum> redeemotpionlist, Context activity,String userSelection) {
        this.activity = activity;
        this.dialog=dialog;
        this.redeemotpionlist = redeemotpionlist;
        this.userSelection=userSelection;
    }

    public void updateData(ArrayList<RedeemOptionModel.Datum> redeemotpionlist) {
        this.redeemotpionlist = redeemotpionlist;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.redeemitems, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        holder.title_TV.setText(redeemotpionlist.get(position).getRedeemOption());
        holder.main_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String optionanme=redeemotpionlist.get(position).getRedeemOption();
                if(userSelection.equalsIgnoreCase("redeem"))
                {
                    if (optionanme.equalsIgnoreCase("DDk")) {
                        MainActivity.addFragment(new SamTransactionDDKFragment(), true);
                        dialog.dismiss();
                    } else {
                        MainActivity.addFragment(new SamKoinsRedeemFragment(), true);
                        dialog.dismiss();
                    }
                }else
                {
                    HomeFragment.userselctionopt=optionanme;
                    if (optionanme.equalsIgnoreCase("DDk")) {
                        MainActivity.addFragment(new CashOutFragmentNew(), true);
                        dialog.dismiss();
                    } else {
                        MainActivity.addFragment(new CashOutFragmentNew(), true);
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return redeemotpionlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title_TV;
        public LinearLayout main_lay;

        public MyViewHolder(View view) {
            super(view);
            main_lay=view.findViewById(R.id.main_lay);
            title_TV= view.findViewById(R.id.title_TV);
        }
    }

}
