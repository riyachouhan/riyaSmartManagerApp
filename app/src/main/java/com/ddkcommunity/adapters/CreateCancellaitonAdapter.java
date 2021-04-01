package com.ddkcommunity.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.model.CreateRequestModel;
import com.ddkcommunity.model.PollingHistoryTransction;
import com.ddkcommunity.model.ShowRequestApiModel;
import com.ddkcommunity.utilies.AppConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import static com.ddkcommunity.utilies.dataPutMethods.ShowSameWalletDialog;
import static com.ddkcommunity.utilies.dataPutMethods.ShowTransationHistory;

public class CreateCancellaitonAdapter extends RecyclerView.Adapter<CreateCancellaitonAdapter.MyViewHolder> {

    private List<ShowRequestApiModel.ShowRequestMidel> data,createCancellationRequestlist;
    String linestatus;
    private Activity activity;
    LinearLayout sendrequestlayout;
    BigDecimal totalselection;
    TextView ddkamountview,charge_fee,subtotal_fees,conversion_view;

    public CreateCancellaitonAdapter(TextView ddkamountview,TextView charge_fee,TextView subtotal_fees,TextView conversion_view,LinearLayout sendrequestlayout,List<ShowRequestApiModel.ShowRequestMidel> createCancellationRequestlist,String linestatus, List<ShowRequestApiModel.ShowRequestMidel> data, Activity activity) {
        this.sendrequestlayout=sendrequestlayout;
        this.createCancellationRequestlist=createCancellationRequestlist;
        this.activity = activity;
        this.linestatus=linestatus;
        this.data = data;
        this.ddkamountview=ddkamountview;
        this.charge_fee=charge_fee;
        this.subtotal_fees=subtotal_fees;
        this.conversion_view=conversion_view;
    }

    public void updateData(List<ShowRequestApiModel.ShowRequestMidel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cancellation, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            if(data.get(position).getRequest_status().equalsIgnoreCase("requested"))
            {
                holder.separteitem.setBackground(ContextCompat.getDrawable(activity, R.drawable.sub_text_bg_border));
                holder.chbContent.setEnabled(false);
            }else
            {
                holder.chbContent.setEnabled(true);
            }
            holder.tvAddress.setText(data.get(position).getDDKSender());
            holder.tvDate.setText(data.get(position).getDateTimeLend());
            holder.tvAmount.setText(data.get(position).getTotalLendUSD()+" USDT");

            holder.chbContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0)
                {
                   try {
                       final boolean isChecked = holder.chbContent.isChecked();
                       String divalue = data.get(position).getLender_id();
                       if (isChecked) {
                           createCancellationRequestlist.add(data.get(position));
                       } else {
                           for (int i = 0; i < createCancellationRequestlist.size(); i++) {
                               if (divalue.equalsIgnoreCase(createCancellationRequestlist.get(i).getLender_id())) {
                                   createCancellationRequestlist.remove(i);
                               }
                           }
                       }
                       if (createCancellationRequestlist.size() > 0) {

                           totalselection = new BigDecimal("0");
                           for (int i = 0; i < createCancellationRequestlist.size(); i++) {
                               String ddkamount = String.valueOf(createCancellationRequestlist.get(i).getTotalLendUSD());
                               BigDecimal amounvalue = new BigDecimal(ddkamount);
                               totalselection = amounvalue.add(totalselection);
                           }

                           BigDecimal percentcount = new BigDecimal("0.05");
                           BigDecimal chargevalue = totalselection.multiply(percentcount);
                           charge_fee.setText(chargevalue.toPlainString() + "");

                           BigDecimal subtotalvalue = totalselection.subtract(chargevalue);
                           subtotal_fees.setText(subtotalvalue.toPlainString() + "");

                           String finalddkamount = UserModel.getInstance().samkoinvalueper+"";
                           BigDecimal conversionamount = new BigDecimal(finalddkamount);
                           conversion_view.setText(conversionamount.toPlainString() + "");
                           BigDecimal ddkamount = subtotalvalue.divide(conversionamount, 5, RoundingMode.FLOOR);
                           ddkamountview.setText(ddkamount.toPlainString() + "");

                           sendrequestlayout.setVisibility(View.VISIBLE);
                       } else {
                           sendrequestlayout.setVisibility(View.GONE);
                       }
                   }catch (Exception e)
                   {
                       e.printStackTrace();
                   }
                }
            });

            holder.viewclickdata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(data.get(position).getRequest_status().equalsIgnoreCase("requested"))
                    {
                        ShowSameWalletDialog(activity,"You already send request for these Data.","1");
                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  tvAddress,tvAmount,tvDate;
        public CheckBox chbContent;
        public LinearLayout separteitem,viewclickdata;

        public MyViewHolder(View view) {
            super(view);
            separteitem=view.findViewById(R.id.separteitem);
            tvAddress=view.findViewById(R.id.tvAddress);
            tvAmount=view.findViewById(R.id.tvAmount);
            tvDate=view.findViewById(R.id.tvDate);
            viewclickdata=view.findViewById(R.id.viewclickdata);
            chbContent= view.findViewById(R.id.chbContent);
            }
    }

}
