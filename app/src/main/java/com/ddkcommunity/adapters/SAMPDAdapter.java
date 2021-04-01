package com.ddkcommunity.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.model.SAMPDModel;
import com.ddkcommunity.model.ShowRequestApiModel;
import com.ddkcommunity.utilies.AppConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ddkcommunity.utilies.dataPutMethods.ShowSameWalletDialog;

public class SAMPDAdapter extends RecyclerView.Adapter<SAMPDAdapter.MyViewHolder> {

    private List<SAMPDModel.Datum> createCancellationRequestlist;
    private Activity activity;

    public SAMPDAdapter(List<SAMPDModel.Datum> createCancellationRequestlist,Activity activity) {
        this.createCancellationRequestlist=createCancellationRequestlist;
        this.activity = activity;
    }

    public void updateData(List<SAMPDModel.Datum> createCancellationRequestlist) {
        this.createCancellationRequestlist= createCancellationRequestlist;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.samp_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            holder.name_TV.setText(createCancellationRequestlist.get(position).getProjectName().toString());
             Glide.with(activity)
                    .asBitmap()
                    .load(Constant.SLIDERIMG+createCancellationRequestlist.get(position).getImage())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            holder.image_view.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            holder.image_view.setImageResource(R.drawable.default_photo);
                        }

                    });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return createCancellationRequestlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_TV;
        public CircleImageView image_view;

        public MyViewHolder(View view) {
            super(view);
            name_TV=view.findViewById(R.id.name_TV);
            image_view=view.findViewById(R.id.image_view);
            }
    }

}
