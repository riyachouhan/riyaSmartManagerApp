package com.ddkcommunity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ddkcommunity.R;
import com.ddkcommunity.model.PaginationScrollListener;
import com.ddkcommunity.model.ReferallayoutNew;
import com.ddkcommunity.model.ReferralPayout;
import com.ddkcommunity.utilies.AppConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReferralPayoutAdapter extends RecyclerView.Adapter<ReferralPayoutAdapter.MyViewHolder> {

    ArrayList<ReferallayoutNew> referallist;
    private final Context mContext;
    private Integer diff;

    public ReferralPayoutAdapter(ArrayList<ReferallayoutNew> referallist,Integer diff, Context mContext) {
        this.referallist= referallist;
        this.diff=diff;
        this.mContext = mContext;
    }

    public void updateData(ArrayList<ReferallayoutNew> referallist, Integer diff) {
        this.referallist= referallist;
        this.diff = diff;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_referral_payout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.tvTotalIn5Days.setText("Total in (" + diff+") Days");
        holder.tvName.setText("You");
        //holder.tvName.setText(referallist.get(position).getName());
        holder.tvFrom.setText(referallist.get(position).getFrom());
        holder.tvTo.setText(referallist.get(position).getTo());
        holder.tvDDKPayout.setText(String.format(Locale.getDefault(), "%.4f", referallist.get(position).getTotalDdk()));
        holder.tvTotalRewards.setText("₮" + String.format(Locale.getDefault(), "%.4f", referallist.get(position).getTotalReward()));
        holder.tvConversion.setText("₮" + String.format(Locale.getDefault(), "%.4f", referallist.get(position).getConversion()));
        holder.tvTransactionDate.setText(AppConfig.dateFormat(referallist.get(position).getTransaction(), "yyyy-MM-dd HH:mm:ss"));

       /* ReferralPayoutLevelAdapter referralChainLevelAdapter = new ReferralPayoutLevelAdapter(referallist.get(position).getRefferallist(), mContext, new ReferralPayoutLevelAdapter.SetOnItemClick() {
            @Override
            public void onItemClick(int position) {
            }
        });
        holder.rvReferralChainLevel.setAdapter(referralChainLevelAdapter);
*/
    }

    @Override
    public int getItemCount() {
        return referallist.size();
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener paginationScrollListener) {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvFrom, tvDDKPayout, tvTotalRewards, tvTo, tvConversion, tvTransactionDate, tvTotalIn5Days;
        public CheckBox checkBox;
        public RecyclerView rvReferralChainLevel;
        public LinearLayout lytCollapse, lytMain;

        public MyViewHolder(View view) {
            super(view);

//            lblLevelCount = view.findViewById(R.id.lblLevelCount);
            rvReferralChainLevel = view.findViewById(R.id.rvReferralChainLevel);
            lytCollapse = view.findViewById(R.id.lytCollapse);
            lytMain = view.findViewById(R.id.lytMain);
            tvTotalIn5Days = view.findViewById(R.id.tvTotalIn5Days);

            tvName = view.findViewById(R.id.tvName);

            tvTotalRewards = view.findViewById(R.id.tvTotalRewards);
            tvDDKPayout = view.findViewById(R.id.tvDDKPayout);
            tvFrom = view.findViewById(R.id.tvFrom);
            tvTo = view.findViewById(R.id.tvTo);
            tvConversion = view.findViewById(R.id.tvConversion);
            tvTransactionDate = view.findViewById(R.id.tvTransactionDate);


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            rvReferralChainLevel.setLayoutManager(mLayoutManager);
            rvReferralChainLevel.setItemAnimator(new DefaultItemAnimator());

            lytMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lytCollapse.getVisibility() == View.VISIBLE) {
                        tvName.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_add_black), null, null, null);
                        lytCollapse.setVisibility(View.GONE);
                    } else {
                        tvName.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.drawable.ic_minus), null, null, null);
                        lytCollapse.setVisibility(View.VISIBLE);
                    }

                }
            });
        }
    }

}
