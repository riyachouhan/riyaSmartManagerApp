package com.ddkcommunity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.send.SendDDkFragment;
import com.ddkcommunity.model.Wallet_Data_Response;

import java.util.List;


public class WalletAddressAdapter extends RecyclerView.Adapter<WalletAddressAdapter.HomeCategoryViewHolder> {
    Context context;
    List<Wallet_Data_Response> android;

    public WalletAddressAdapter(Context context, List<Wallet_Data_Response> android) {
        this.context = context;
        this.android = android;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public HomeCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wallet_address, parent, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HomeCategoryViewHolder holder, final int position) {
        holder.wallet_address.setText(android.get(position).getAddress());
        holder.linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ddk_code = android.get(position).getAddress();
                MainActivity.addFragment(SendDDkFragment.getInstance(ddk_code), true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView wallet_address;
        LinearLayout linear1;

        public HomeCategoryViewHolder(View itemView) {
            super(itemView);
            linear1 = itemView.findViewById(R.id.linear1);

            image = itemView.findViewById(R.id.image);
            wallet_address = itemView.findViewById(R.id.DDKAddress);

        }
    }
}