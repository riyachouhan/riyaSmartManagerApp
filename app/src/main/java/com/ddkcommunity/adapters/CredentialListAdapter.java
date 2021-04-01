package com.ddkcommunity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.model.credential.Credential;

import java.util.List;

public class CredentialListAdapter extends RecyclerView.Adapter<CredentialListAdapter.MyViewHolder> {

    private List<Credential> data;
    private Context activity;
    private EditText searchET;
    private SetOnItemClickListener setOnItemClickListener;

    public CredentialListAdapter(List<Credential> data, Context activity, EditText searchET, SetOnItemClickListener setOnItemClickListener) {
        this.activity = activity;
        this.searchET = searchET;
        this.data = data;
        this.setOnItemClickListener = setOnItemClickListener;
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
                public void onClick(View view) {
                    setOnItemClickListener.onItemClick(data.get(getAdapterPosition()).getDdkcode(), data.get(getAdapterPosition()).getWalletId(), data.get(getAdapterPosition()).getPassphrase());
                }
            });
        }
    }

    public interface SetOnItemClickListener {
        public void onItemClick(String wallet_code, String walletId1, String passphrase);
    }
}
