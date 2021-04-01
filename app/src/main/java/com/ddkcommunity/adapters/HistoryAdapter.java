package com.ddkcommunity.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.R;
import com.ddkcommunity.model.PollingHistoryTransction;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import static com.ddkcommunity.utilies.dataPutMethods.ShowTransationHistory;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>
{
    private List<PollingHistoryTransction.PoolingHistoryData> data;
    private Activity activity;
    String wallet_code;

    public HistoryAdapter(String wallet_code,List<PollingHistoryTransction.PoolingHistoryData> data, Activity activity) {
        this.wallet_code=wallet_code;
        this.activity = activity;
        this.data = data;
    }

    public void updateData(List<PollingHistoryTransction.PoolingHistoryData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {

            holder.tvDateTime.setText(data.get(position).transactionDate);

            if(data.get(position).outgoing_asset_type!=null)
            {
                if(data.get(position).created_at!=null)
                {
                    String trasnactiondate[] = data.get(position).created_at.toString().split(" ");
                    if(trasnactiondate.length>1)
                    {
                        holder.tvDateTime.setText(trasnactiondate[0]);
                    }
                }
                holder.tvOrderNumber.setText(data.get(position).outgoing_asset_sender);
                holder.tvAmount.setText("Receiver : " + data.get(position).outgoing_asset_receiver);
                String outgoingamount=data.get(position).outgoing_amount.toString();
                BigDecimal outamount=new BigDecimal(outgoingamount);
                if(data.get(position).outgoing_asset_type.equalsIgnoreCase("sam_koin"))
                {
                    holder.tvddkamount.setVisibility(View.VISIBLE);
                   holder.tvddkamount.setText(outamount.toPlainString() + " SAM Koin");
                    holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                    holder.tvConversion.setVisibility(View.GONE);
                }else if(data.get(position).outgoing_asset_type.equalsIgnoreCase("eth"))
                {
                    holder.tvddkamount.setVisibility(View.VISIBLE);
                    holder.tvddkamount.setText(outamount.toPlainString() + " ETH");
                    holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                    holder.tvConversion.setVisibility(View.GONE);
                }else if(data.get(position).outgoing_asset_type.equalsIgnoreCase("usdt"))
                {
                    holder.tvddkamount.setVisibility(View.VISIBLE);
                    holder.tvddkamount.setText(outamount.toPlainString() + " USDT");
                    holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                    holder.tvConversion.setVisibility(View.GONE);
                }else if(data.get(position).outgoing_asset_type.equalsIgnoreCase("btc"))
                {
                    holder.tvddkamount.setVisibility(View.VISIBLE);
                    holder.tvddkamount.setText(outamount.toPlainString()  + " BTC");
                    holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                    holder.tvConversion.setVisibility(View.GONE);
                }
                holder.tvOrderType.setText("Received");
                holder.ivHistoryIcon.setImageResource(R.drawable.buy_icon);
                holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_cornor_bg_green_5));
                if(data.get(position).outgoing_admin_status!=null)
                {
                    String upperString = data.get(position).outgoing_admin_status.substring(0, 1).toUpperCase() + data.get(position).outgoing_admin_status.substring(1).toLowerCase();
                    if (data.get(position).outgoing_admin_status.equalsIgnoreCase("Verified")) {
                        holder.tvStatus.setText(upperString);
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_orange_back));

                    } else if (data.get(position).outgoing_admin_status.equalsIgnoreCase("Processing")) {

                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_green_back));
                        holder.tvStatus.setText(upperString);
                    } else if (data.get(position).outgoing_admin_status.equalsIgnoreCase("failed")) {
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_red_back));
                        holder.tvStatus.setText(upperString);
                    } else {
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_card_back));
                        if (data.get(position).outgoing_admin_status.equalsIgnoreCase("Lend")) {
                            holder.tvStatus.setText("ACTIVE");
                        } else {
                            holder.tvStatus.setText(upperString);
                        }
                    }
                }
                holder.tvFees.setVisibility(View.GONE);

            }else
            if (data.get(position).transaction_type != null)
            {
                if (data.get(position).transaction_type.toString().equalsIgnoreCase("Receive"))
                {
                    if (data.get(position).conversion != null)
                    {
                        holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " DDK Conv.");
                    }
                    holder.tvConversion.setVisibility(View.INVISIBLE);
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    holder.tvOrderNumber.setText(data.get(position).sender_address);
                    holder.tvddkamount.setVisibility(View.VISIBLE);
                    holder.tvAmount.setText("Receiver : " + data.get(position).receiver_address);

                    if(data.get(position).payment_through_currency.toString()!=null)
                    {
                        String paymentcurrent=data.get(position).payment_through_currency;
                        String valueamount=data.get(position).ddk.toString();
                        if(valueamount!=null)
                        {
                            if(paymentcurrent.equalsIgnoreCase("sam_koin"))
                            {
                                if(data.get(position).getPayment_type()!=null)
                                {
                                    if(data.get(position).getPayment_type().equalsIgnoreCase("eth") || data.get(position).getPayment_type().equalsIgnoreCase("eth_send"))
                                    {
                                        holder.tvddkamount.setText(valueamount + " ETH");
                                    }else if(data.get(position).getPayment_type().equalsIgnoreCase("btc") || data.get(position).getPayment_type().equalsIgnoreCase("btc_send"))
                                    {
                                        holder.tvddkamount.setText(valueamount + " BTC");
                                    }else if(data.get(position).getPayment_type().equalsIgnoreCase("usdt") || data.get(position).getPayment_type().equalsIgnoreCase("usdt_send"))
                                    {
                                        holder.tvddkamount.setText(valueamount + " USDT");
                                    }else  if(data.get(position).getPayment_type().equalsIgnoreCase("ddk"))
                                    {
                                        holder.tvddkamount.setText(valueamount + " DDK");
                                    }else
                                    {
                                        holder.tvddkamount.setText(valueamount + " SAM Koin");
                                    }

                                }else {
                                    holder.tvddkamount.setText(valueamount + " SAM Koin");
                                }
                            }else
                            {
                                if(data.get(position).getPayment_type().equalsIgnoreCase("sam_koin"))
                                {
                                    holder.tvddkamount.setText(valueamount + " SAM Koin");

                                }else {
                                    holder.tvddkamount.setText(valueamount + " DDK");
                                }
                            }
                        }
                    }

                    holder.tvOrderType.setText("Received");
                    holder.ivHistoryIcon.setImageResource(R.drawable.buy_icon);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_cornor_bg_green_5));
                    if(data.get(position).status!=null)
                    {
                        String upperString = data.get(position).status.substring(0, 1).toUpperCase() + data.get(position).status.substring(1).toLowerCase();
                        if (data.get(position).status.equalsIgnoreCase("Verified")) {
                            holder.tvStatus.setText(upperString);
                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_orange_back));

                        } else if (data.get(position).status.equalsIgnoreCase("Processing")) {

                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_green_back));
                            holder.tvStatus.setText(upperString);
                        } else if (data.get(position).status.equalsIgnoreCase("failed")) {
                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_red_back));
                            holder.tvStatus.setText(upperString);
                        } else {
                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_card_back));
                            if (data.get(position).status.equalsIgnoreCase("Lend")) {
                                holder.tvStatus.setText("ACTIVE");
                            } else {
                                holder.tvStatus.setText(upperString);
                            }
                        }
                    }
                    holder.tvFees.setVisibility(View.GONE);

                } else
                if (data.get(position).transaction_type.toString().equalsIgnoreCase("Cashout"))
                {
                    if (data.get(position).conversion != null)
                    {
                        holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " DDK Conv.");
                    }
                    holder.tvOrderType.setText("Sell");
                    holder.tvConversion.setVisibility(View.INVISIBLE);
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    holder.tvOrderNumber.setText(data.get(position).sender_address);
                    holder.tvddkamount.setVisibility(View.VISIBLE);
                    if (!data.get(position).php_amount.toString().equalsIgnoreCase("0")) {
                        holder.tvAmount.setVisibility(View.VISIBLE);
                        if(data.get(position).transaction_for!=null)
                        {
                            if(data.get(position).transaction_for.equalsIgnoreCase("aud"))
                            {
                               holder.tvAmount.setText(data.get(position).php_amount + " AUD");
                            }else {
                               holder.tvAmount.setText(data.get(position).php_amount + " PHP");
                            }
                        }else
                        {
                            holder.tvAmount.setText(data.get(position).php_amount + " PHP");
                        }
                    } else {
                        holder.tvAmount.setVisibility(View.INVISIBLE);
                    }

                    if(data.get(position).payment_through_currency.toString()!=null)
                    {
                        String paymentcurrent=data.get(position).payment_through_currency;
                        String valueamount=data.get(position).ddk.toString();
                        if(valueamount!=null)
                        {
                            if(paymentcurrent.equalsIgnoreCase("sam_koin"))
                            {
                                holder.tvddkamount.setText(valueamount + " SAM Koin");
                            }else
                            {
                                holder.tvddkamount.setText(valueamount + " DDK");
                            }
                        }
                    }

                    holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.colorApp));
                    holder.tvddkamount.setTextColor(ContextCompat.getColor(activity, R.color.colorApp));
                    holder.tvddkamount.setTextColor(ContextCompat.getColor(activity, R.color.colorApp));
                    holder.ivHistoryIcon.setImageResource(R.drawable.cashout);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.colorApp));
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_cashout_subs));
                    String upperString = data.get(position).status.substring(0, 1).toUpperCase() + data.get(position).status.substring(1).toLowerCase();
                    if (data.get(position).status.toString().equalsIgnoreCase("Verified")) {
                        holder.tvStatus.setText(upperString);
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_orange_back));

                    } else if (data.get(position).status.toString().equalsIgnoreCase("Processing") || data.get(position).status.toString().equalsIgnoreCase("Reward")) {

                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_green_back));
                        holder.tvStatus.setText(upperString);
                    } else if (data.get(position).status.toString().equalsIgnoreCase("failed")) {
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_red_back));
                        holder.tvStatus.setText(upperString);
                    } else {
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_card_back));
                        if (data.get(position).status.toString().equalsIgnoreCase("Lend")) {
                            holder.tvStatus.setText("Active");
                        } else {
                            holder.tvStatus.setText(upperString);
                        }
                    }
                    holder.tvFees.setVisibility(View.GONE);

                } else if (data.get(position).transaction_type.toString().equalsIgnoreCase("Subscription"))
                {
                        holder.tvOrderType.setText(data.get(position).transaction_type);
                        String amountvalue = String.valueOf(data.get(position).amount);
                        holder.tvOrderNumber.setText(data.get(position).sender_address);
                        if (!amountvalue.equalsIgnoreCase("")) {
                            if (data.get(position).amount.toString() != null && !data.get(position).transaction_type.toString().equalsIgnoreCase("Cashout")) {
                                if (data.get(position).transaction_type.toString().equalsIgnoreCase("Send"))
                                {

                                } else {
                                    holder.tvAmount.setVisibility(View.VISIBLE);
                                    holder.tvAmount.setText(new DecimalFormat("##.####").format(data.get(position).amount) + " USDT");
                                }
                            }
                        } else {
                            holder.tvAmount.setVisibility(View.GONE);
                        }

                        holder.tvConversion.setVisibility(View.VISIBLE);
                        holder.tvStatus.setVisibility(View.VISIBLE);
                        holder.tvddkamount.setVisibility(View.VISIBLE);
                        String valuepaymentytpe=data.get(position).getMode();
                        if(data.get(position).getMode()!=null)
                        {
                            if (valuepaymentytpe.equalsIgnoreCase("sam koin"))
                            {
                                if (data.get(position).conversion != null)
                                {
                                    holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " SAM Koin Conv.");
                                }
                                try
                                {
                                    if(data.get(position).TotalUSDWithCharge!=null)
                                    {
                                        String trsnamsm=data.get(position).TotalUSDWithCharge;
                                        holder.tvAmount.setText(trsnamsm+ " USDT");
                                    }
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                holder.tvddkamount.setText(data.get(position).ddk + " SAM Koin");
                            } else
                            {
                                if (valuepaymentytpe.equalsIgnoreCase("eth"))
                                {
                                    holder.tvddkamount.setText(data.get(position).ddk + " ETH");
                                }else
                                if (valuepaymentytpe.equalsIgnoreCase("btc"))
                                {
                                    holder.tvddkamount.setText(data.get(position).ddk + " BTC");
                                }else
                                if (valuepaymentytpe.equalsIgnoreCase("USDT"))
                                {
                                    holder.tvddkamount.setText(data.get(position).ddk + " USDT");
                                }else if (valuepaymentytpe.equalsIgnoreCase("DDK"))
                                {
                                    holder.tvddkamount.setText(data.get(position).ddk + " DDK");
                                }else if (valuepaymentytpe.equalsIgnoreCase("credit card"))
                                {
                                    holder.tvddkamount.setText(data.get(position).ddk + " USDT");
                                }

                                if (data.get(position).conversion != null)
                                {
                                    if(data.get(position).payment_through_currency.equalsIgnoreCase("ddk"))
                                    {
                                        holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " DDK Conv.");
                                    }else
                                    {
                                        holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " SAM Koin Conv.");
                                    }
                                }
                            }
                        }else
                        {
                            if (data.get(position).conversion != null)
                            {
                                holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " DDK Conv.");
                            }
                            holder.tvddkamount.setText(data.get(position).ddk + " DDK");
                        }
                        holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                        holder.tvddkamount.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                        holder.ivHistoryIcon.setImageResource(R.drawable.subscription_new);
                        holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                        holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_blue_cornor));
                        String upperString = data.get(position).status.substring(0, 1).toUpperCase() + data.get(position).status.substring(1).toLowerCase();
                        if (data.get(position).status.toString().equalsIgnoreCase("Verified")) {
                            holder.tvStatus.setText(upperString);
                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_orange_back));

                        } else if (data.get(position).status.toString().equalsIgnoreCase("Processing") || data.get(position).status.toString().equalsIgnoreCase("Reward")) {

                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_green_back));
                            holder.tvStatus.setText(upperString);
                        } else if (data.get(position).status.toString().equalsIgnoreCase("failed")) {
                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_red_back));
                            holder.tvStatus.setText(upperString);
                        } else {
                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_card_back));
                            if (data.get(position).status.toString().equalsIgnoreCase("Lend")) {
                                holder.tvStatus.setText("ACTIVE");
                            } else {
                                holder.tvStatus.setText(upperString);
                            }
                        }
                        holder.tvFees.setVisibility(View.GONE);

                }else if (data.get(position).transaction_type.toString().equalsIgnoreCase("map_subscription"))
                {
                    holder.tvOrderType.setText("MAP Package");
                    String amountvalue = String.valueOf(data.get(position).amount);
                    holder.tvOrderNumber.setText(data.get(position).sender_address);
                    if (!amountvalue.equalsIgnoreCase("")) {
                        if (data.get(position).amount.toString() != null && !data.get(position).transaction_type.toString().equalsIgnoreCase("Cashout")) {
                            if (data.get(position).transaction_type.toString().equalsIgnoreCase("Send"))
                            {

                            } else {
                                holder.tvAmount.setVisibility(View.VISIBLE);
                                holder.tvAmount.setText(new DecimalFormat("##.####").format(data.get(position).amount) + " USDT");
                            }
                        }
                    } else {
                        holder.tvAmount.setVisibility(View.GONE);
                    }

                    holder.tvConversion.setVisibility(View.VISIBLE);
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    holder.tvddkamount.setVisibility(View.VISIBLE);
                    String valuepaymentytpe=data.get(position).getMode();
                    if(data.get(position).getMode()!=null)
                    {
                        if (valuepaymentytpe.equalsIgnoreCase("sam koin"))
                        {
                            if (data.get(position).conversion != null)
                            {
                                holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " SAM Koin Conv.");
                            }
                            try
                            {
                                if(data.get(position).TotalUSDWithCharge!=null)
                                {
                                    String trsnamsm=data.get(position).TotalUSDWithCharge;
                                    holder.tvAmount.setText(trsnamsm+ " USDT");
                                }
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            holder.tvddkamount.setText(data.get(position).ddk + " SAM Koin");
                        } else
                        {
                            if (valuepaymentytpe.equalsIgnoreCase("eth"))
                            {
                                holder.tvddkamount.setText(data.get(position).ddk + " ETH");
                            }else
                            if (valuepaymentytpe.equalsIgnoreCase("btc"))
                            {
                                holder.tvddkamount.setText(data.get(position).ddk + " BTC");
                            }else
                            if (valuepaymentytpe.equalsIgnoreCase("USDT"))
                            {
                                holder.tvddkamount.setText(data.get(position).ddk + " USDT");
                            }else if (valuepaymentytpe.equalsIgnoreCase("DDK"))
                            {
                                holder.tvddkamount.setText(data.get(position).ddk + " DDK");
                            }else if (valuepaymentytpe.equalsIgnoreCase("credit card"))
                            {
                                holder.tvddkamount.setText(data.get(position).ddk + " USDT");
                            }

                            /*if (data.get(position).conversion != null)
                            {
                                if(data.get(position).payment_through_currency.equalsIgnoreCase("ddk"))
                                {
                                    holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " DDK Conv.");
                                }else
                                {
                                    holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " SAM Koin Conv.");
                                }
                            }*/
                        }
                    }else
                    {
                        if (data.get(position).conversion != null)
                        {
                            holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " DDK Conv.");
                        }
                        holder.tvddkamount.setText(data.get(position).ddk + " DDK");
                    }
                    holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                    holder.tvddkamount.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                    holder.ivHistoryIcon.setImageResource(R.drawable.subscription_new);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_blue_cornor));
                    String upperString = data.get(position).status.substring(0, 1).toUpperCase() + data.get(position).status.substring(1).toLowerCase();
                    if (data.get(position).status.toString().equalsIgnoreCase("Verified")) {
                        holder.tvStatus.setText(upperString);
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_orange_back));

                    } else if (data.get(position).status.toString().equalsIgnoreCase("Processing") || data.get(position).status.toString().equalsIgnoreCase("Reward")) {

                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_green_back));
                        holder.tvStatus.setText(upperString);
                    } else if (data.get(position).status.toString().equalsIgnoreCase("failed")) {
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_red_back));
                        holder.tvStatus.setText(upperString);
                    } else {
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_card_back));
                        if (data.get(position).status.toString().equalsIgnoreCase("Lend")) {
                            holder.tvStatus.setText("ACTIVE");
                        } else {
                            holder.tvStatus.setText(upperString);
                        }
                    }
                    holder.tvFees.setVisibility(View.GONE);

                } else if (data.get(position).transaction_type.toString().equalsIgnoreCase("Buy"))
                {

                    String amountvalue = String.valueOf(data.get(position).amount);
                    holder.tvOrderNumber.setText(data.get(position).sender_address);
                    if (!amountvalue.equalsIgnoreCase("")) {
                        if (data.get(position).amount.toString() != null && !data.get(position).transaction_type.toString().equalsIgnoreCase("Cashout")) {
                            if (data.get(position).transaction_type.toString().equalsIgnoreCase("Send")) {

                            } else {
                                holder.tvAmount.setVisibility(View.VISIBLE);
                                holder.tvAmount.setText(new DecimalFormat("##.####").format(data.get(position).amount) + " USDT");
                            }
                        }
                    } else {
                        holder.tvAmount.setVisibility(View.GONE);
                    }
                    holder.tvOrderType.setText(data.get(position).transaction_type);
                    holder.tvConversion.setVisibility(View.VISIBLE);
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    holder.tvddkamount.setVisibility(View.VISIBLE);
                    if(data.get(position).getType().equalsIgnoreCase("btc"))
                    {
                        if (data.get(position).conversion != null) {
                            holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " Conv.");
                        }
                        holder.tvddkamount.setText(data.get(position).quantity + " BTC");

                    }else if(data.get(position).getType().equalsIgnoreCase("eth"))
                    {
                        if (data.get(position).conversion != null) {
                            holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " Conv.");
                        }
                        holder.tvddkamount.setText(data.get(position).quantity + " ETH");

                    }else if(data.get(position).getType().equalsIgnoreCase("usdt"))
                    {
                        if (data.get(position).conversion != null) {
                            holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " Conv.");
                        }
                        holder.tvddkamount.setText(data.get(position).quantity + " USDT");

                    }else
                    {
                        if (data.get(position).conversion != null) {
                            holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " DDK Conv.");
                        }
                        holder.tvddkamount.setText(data.get(position).quantity + " DDK");
                    }
                    holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                    holder.tvddkamount.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                    holder.ivHistoryIcon.setImageResource(R.drawable.buy_icon);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                    holder.tvOrderType.setText(data.get(position).transaction_type);
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_cornor_subscription));
                    String upperString = data.get(position).status.substring(0, 1).toUpperCase() + data.get(position).status.substring(1).toLowerCase();
                    Log.d("uper",upperString);
                    if (data.get(position).status.toString().equalsIgnoreCase("Verified")) {
                        holder.tvStatus.setText(upperString);
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_orange_back));

                    } else if (data.get(position).status.toString().equalsIgnoreCase("Processing") || data.get(position).status.toString().equalsIgnoreCase("Reward")) {

                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_green_back));
                        holder.tvStatus.setText(upperString);
                    } else if (data.get(position).status.toString().equalsIgnoreCase("failed")) {
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_red_back));
                        holder.tvStatus.setText(upperString);
                    } else {
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_card_back));
                        if (data.get(position).status.toString().equalsIgnoreCase("Lend")) {
                            holder.tvStatus.setText("ACTIVE");
                        } else if (data.get(position).status.toString().equalsIgnoreCase("Completed"))
                        {
                            holder.tvStatus.setText(upperString);
                        }else
                        {
                            holder.tvStatus.setText(upperString);
                        }
                    }
                    holder.tvFees.setVisibility(View.GONE);

                } else if (data.get(position).transaction_type.toString().equalsIgnoreCase("reward"))
                {
                    String amountvalue = String.valueOf(data.get(position).amount);
                    if (data.get(position).conversion != null) {
                        holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " DDK Conv.");
                    }
                    holder.tvOrderNumber.setText(data.get(position).sender_address);
                    if (!amountvalue.equalsIgnoreCase("")) {
                        if (data.get(position).amount.toString() != null && !data.get(position).transaction_type.toString().equalsIgnoreCase("Cashout")) {
                            if (data.get(position).transaction_type.toString().equalsIgnoreCase("Send")) {

                            } else {
                                holder.tvAmount.setVisibility(View.VISIBLE);
                                holder.tvAmount.setText(new DecimalFormat("##.####").format(data.get(position).amount) + " USDT");
                            }
                        }
                    } else {
                        holder.tvAmount.setVisibility(View.GONE);
                    }
                    holder.tvConversion.setVisibility(View.VISIBLE);
                    holder.tvStatus.setVisibility(View.INVISIBLE);
                    holder.tvddkamount.setVisibility(View.VISIBLE);
                    holder.tvOrderType.setText(data.get(position).transaction_type);
                    holder.tvddkamount.setText(data.get(position).ddk + " DDK");
                    holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                    holder.tvddkamount.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                    holder.ivHistoryIcon.setImageResource(R.drawable.rewards);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_cornor_bg_green_5));
                    String upperString = data.get(position).status.substring(0, 1).toUpperCase() + data.get(position).status.substring(1).toLowerCase();
                    if (data.get(position).status.toString().equalsIgnoreCase("Verified")) {
                        holder.tvStatus.setText(upperString);
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_orange_back));

                    } else if (data.get(position).status.toString().equalsIgnoreCase("Processing") || data.get(position).status.toString().equalsIgnoreCase("Reward")) {

                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_green_back));
                        holder.tvStatus.setText(upperString);
                    } else if (data.get(position).status.toString().equalsIgnoreCase("failed")) {
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_red_back));
                        holder.tvStatus.setText(upperString);
                    } else {
                        holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                        holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_card_back));
                        if (data.get(position).status.toString().equalsIgnoreCase("Lend")) {
                            holder.tvStatus.setText("ACTIVE");
                        } else {
                            holder.tvStatus.setText(upperString);
                        }
                    }
                    holder.tvFees.setVisibility(View.GONE);

                } else if (data.get(position).transaction_type.toString().equalsIgnoreCase("Send"))
                {
                    if (data.get(position).conversion != null)
                    {
                        String valuepaymentytpe=data.get(position).getPayment_type();
                        if(data.get(position).getPayment_type()!=null)
                        {
                            if (valuepaymentytpe.equalsIgnoreCase("incoming_asset_sam_koin")) {
                                holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " SAMKoin Conv.");
                            } else {
                                holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " DDK Conv.");
                            }
                        }
                    }

                    holder.tvOrderNumber.setText(data.get(position).sender_address);
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    holder.tvConversion.setVisibility(View.GONE);
                    holder.tvddkamount.setVisibility(View.VISIBLE);
                    holder.tvAmount.setVisibility(View.VISIBLE);
                    if(data.get(position).sender_address.equalsIgnoreCase(wallet_code))
                    {
                        holder.tvOrderType.setText("Send");
                    }else
                    {
                        if(data.get(position).receiver_address.equalsIgnoreCase(wallet_code))
                        {
                            holder.tvOrderType.setText("Received");
                            holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_cornor_bg_green_5));
                            holder.ivHistoryIcon.setImageResource(R.drawable.buy_icon);
                        }else
                        {
                            holder.tvOrderType.setText("Other");
                        }
                    }
                    holder.tvAmount.setText("Receiver : " + data.get(position).receiver_address.toString());
                    Double ddkvalue = Double.valueOf(data.get(position).ddk.toString());
                    if(data.get(position).getPayment_type()!=null)
                    {
                        if (data.get(position).getPayment_type().equalsIgnoreCase("eth_send") || data.get(position).getPayment_type().equalsIgnoreCase("incoming_asset_eth"))
                        {
                            holder.tvddkamount.setText(new DecimalFormat("##.####").format(ddkvalue) + " ETH");
                        } else if (data.get(position).getPayment_type().equalsIgnoreCase("usdt_send")||data.get(position).getPayment_type().equalsIgnoreCase("incoming_asset_usdt"))
                        {
                            holder.tvddkamount.setText(new DecimalFormat("##.####").format(ddkvalue) + " USDT");
                        } else if (data.get(position).getPayment_type().equalsIgnoreCase("btc_send") || data.get(position).getPayment_type().equalsIgnoreCase("incoming_asset_btc")) {
                            holder.tvddkamount.setText(new DecimalFormat("##.####").format(ddkvalue) + " BTC");
                        } else if (data.get(position).getPayment_type().equalsIgnoreCase("incoming_asset_sam_koin")) {
                            holder.tvddkamount.setText(new DecimalFormat("##.####").format(ddkvalue) + " SAM Koin");
                        }else if (data.get(position).getPayment_type().equalsIgnoreCase("ddk_send")) {
                        holder.tvddkamount.setText(new DecimalFormat("##.####").format(ddkvalue) + " DDK");
                    } else {
                            if(data.get(position).payment_through_currency!=null)
                            {
                                String paymentcurrent=data.get(position).payment_through_currency;
                                if(paymentcurrent.equalsIgnoreCase("sam_koin"))
                                {
                                    holder.tvddkamount.setText(new DecimalFormat("##.####").format(ddkvalue) + " SAM Koin");
                                }else
                                {
                                    holder.tvddkamount.setText(new DecimalFormat("##.####").format(ddkvalue) + " DDK");
                                }
                            }
                        }
                    }
                    holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                    holder.tvddkamount.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                    holder.ivHistoryIcon.setImageResource(R.drawable.ic_send);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_blue_cornor));
                    if(data.get(position).status!=null && !data.get(position).status.toString().equalsIgnoreCase("")) {
                        String upperString = data.get(position).status.substring(0, 1).toUpperCase() + data.get(position).status.toString().substring(1).toLowerCase();
                        if (data.get(position).status.toString().equalsIgnoreCase("Verified")) {
                            holder.tvStatus.setText(upperString);
                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_orange_back));

                        } else if (data.get(position).status.toString().equalsIgnoreCase("Processing") || data.get(position).status.toString().equalsIgnoreCase("Reward")) {

                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_green_back));
                            holder.tvStatus.setText(upperString);
                        } else if (data.get(position).status.toString().equalsIgnoreCase("failed")) {
                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_red_back));
                            holder.tvStatus.setText(upperString);
                        } else {
                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_card_back));
                            if (data.get(position).status.toString().equalsIgnoreCase("Lend")) {
                                holder.tvStatus.setText("Active");
                            } else {
                                holder.tvStatus.setText(upperString);
                            }
                        }

                    }else
                    {
                        holder.tvStatus.setText("status not come");
                    }
                    holder.tvFees.setText("Fees "+data.get(position).transaction_fees);
                    if(data.get(position).transaction_fees!=null) {
                        Double transafee = Double.valueOf(data.get(position).transaction_fees);
                         holder.tvFees.setText(new DecimalFormat("##.####").format(transafee) + " Fees");
                    }
                    holder.tvFees.setVisibility(View.GONE);

                }else if(data.get(position).transaction_type.toString().equalsIgnoreCase("redeem"))
                {
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    holder.tvConversion.setVisibility(View.VISIBLE);
                    holder.tvddkamount.setVisibility(View.VISIBLE);
                    holder.tvAmount.setVisibility(View.VISIBLE);
                    holder.tvOrderType.setText(data.get(position).transaction_type);
                    String valuepaymentytpe=data.get(position).payment_type;
                    if(data.get(position).payment_type.equalsIgnoreCase("reward"))
                    {
                        holder.tvOrderNumber.setText(data.get(position).payment_type);
                        holder.tvddkamount.setText(new DecimalFormat("##.####").format(Double.valueOf(data.get(position).ddk.toString())) + " DDK");

                    }else
                    {
                        String payment_through_currency=data.get(position).payment_type;
                        if(payment_through_currency.equalsIgnoreCase("redeem_sam_koin")) {
                            holder.tvOrderNumber.setText("POINT/SAMKOIN");
                            holder.tvddkamount.setText(new DecimalFormat("##.####").format(Double.valueOf(data.get(position).ddk.toString())) + " SAM Koin");
                        }else
                        {
                            holder.tvOrderNumber.setText("POINT/DDK");
                            holder.tvddkamount.setText(new DecimalFormat("##.####").format(Double.valueOf(data.get(position).ddk.toString())) + " DDK");

                        }
                    }

                    holder.tvAmount.setText("" + data.get(position).receiver_address.toString());
                    Double ddkvalue = Double.valueOf(data.get(position).amount.toString());
                    holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.reedam));
                    holder.tvConversion.setText(new DecimalFormat("##.####").format(ddkvalue) + " POINT");
                    holder.tvddkamount.setTextColor(ContextCompat.getColor(activity, R.color.reedam));
                    holder.ivHistoryIcon.setImageResource(R.drawable.reedeem_icon);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.reedam));
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_reedeem));
                    if(data.get(position).status!=null && !data.get(position).status.toString().equalsIgnoreCase("")) {
                            String upperString = data.get(position).status.substring(0, 1).toUpperCase() + data.get(position).status.toString().substring(1).toLowerCase();
                            if (data.get(position).status.toString().equalsIgnoreCase("Verified"))
                            {
                                holder.tvStatus.setText(upperString);
                                holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                                holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_orange_back));

                            } else if (data.get(position).status.toString().equalsIgnoreCase("Processing") || data.get(position).status.toString().equalsIgnoreCase("Reward"))
                            {
                                holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                                holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_green_back));
                                holder.tvStatus.setText(upperString);
                            } else if (data.get(position).status.toString().equalsIgnoreCase("failed"))
                            {
                                holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
                                holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_red_back));
                                holder.tvStatus.setText(upperString);
                            } else
                            {
                                holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                                holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_card_back));
                                if (data.get(position).status.toString().equalsIgnoreCase("Lend")) {
                                    holder.tvStatus.setText("Active");
                                } else {
                                    holder.tvStatus.setText(upperString);
                                }
                            }

                        }else
                        {
                            holder.tvStatus.setText("status not come");
                        }
                    holder.tvFees.setVisibility(View.GONE);

                }else if(data.get(position).transaction_type.toString().equalsIgnoreCase("SAM Reward"))
                {
                    if (data.get(position).conversion != null) {
                        holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " DDK Conv.");
                    }
                    holder.tvOrderType.setText("Reward");
                    holder.tvStatus.setVisibility(View.GONE);
                    holder.tvConversion.setVisibility(View.GONE);
                    holder.tvddkamount.setVisibility(View.VISIBLE);
                    holder.tvAmount.setVisibility(View.VISIBLE);
                    if(data.get(position).payment_type.toString().equalsIgnoreCase("sam_reward"))
                    {
                        holder.tvOrderNumber.setText("SAM REWARD");
                    }else
                    {
                        holder.tvOrderNumber.setText("SAM/DDK");
                    }
                    holder.tvAmount.setText("" + data.get(position).receiver_address.toString());
                    Double ddkvalue = Double.valueOf(data.get(position).amount.toString());
                    holder.tvddkamount.setText(new DecimalFormat("##.####").format(ddkvalue) + " POINTS");
                    holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.reward_color));
                    holder.tvddkamount.setTextColor(ContextCompat.getColor(activity, R.color.reward_color));
                    holder.ivHistoryIcon.setImageResource(R.drawable.rewards);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.reward_color));
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_reward));
                    holder.tvFees.setVisibility(View.GONE);

                }else if(data.get(position).transaction_type.toString().equalsIgnoreCase("Referral Reward"))
                {
                    if (data.get(position).conversion != null) {
                        holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " DDK Conv.");
                    }
                    holder.tvOrderType.setText("Reward");
                    holder.tvStatus.setVisibility(View.GONE);
                    holder.tvConversion.setVisibility(View.GONE);
                    holder.tvddkamount.setVisibility(View.VISIBLE);
                    holder.tvAmount.setVisibility(View.VISIBLE);
                    //if(data.get(position).payment_type.toString().equalsIgnoreCase("referral_reward"))
                    {
                        holder.tvOrderNumber.setText("REFERRAL REWARD");
                    }
                    holder.tvAmount.setText("" + data.get(position).receiver_address.toString());
                    Double ddkvalue = Double.valueOf(data.get(position).amount.toString());
                    holder.tvddkamount.setText(new DecimalFormat("##.####").format(ddkvalue) + " POINTS");
                    holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.reward_color));
                    holder.tvddkamount.setTextColor(ContextCompat.getColor(activity, R.color.reward_color));
                    holder.ivHistoryIcon.setImageResource(R.drawable.rewards);
                    holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.reward_color));
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_reward));
                    holder.tvFees.setVisibility(View.GONE);

                } else if(data.get(position).transaction_type.toString().equalsIgnoreCase("currency_wallet"))
                {
                    if(data.get(position).status!=null && !data.get(position).status.toString().equalsIgnoreCase(""))
                    {
                        if(data.get(position).status.equalsIgnoreCase("Received"))
                        {

                            if(data.get(position).created_at!=null)
                            {
                                String trasnactiondate[] = data.get(position).created_at.toString().split(" ");
                                if(trasnactiondate.length>1)
                                {
                                    holder.tvDateTime.setText(trasnactiondate[0]);
                                }
                            }

                            if(data.get(position).getBy_admin().equalsIgnoreCase("1"))
                            {
                                holder.tvOrderNumber.setText("Admin");
                            }else
                            {
                                holder.tvOrderNumber.setText(data.get(position).sender_email);
                            }
                            holder.tvAmount.setText("Receiver : " + data.get(position).receiver_email);
                            holder.tvddkamount.setText(data.get(position).amount + " PHP");
                            holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                            holder.tvConversion.setVisibility(View.GONE);
                            holder.tvOrderType.setText("Received");
                            holder.ivHistoryIcon.setImageResource(R.drawable.buy_icon);
                            holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                            holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_cornor_bg_green_5));

                        }else if(data.get(position).status.equalsIgnoreCase("Send"))
                        {
                            //for new
                            holder.tvddkamount.setText(new DecimalFormat("##.####").format(data.get(position).amount) + " PHP");
                            holder.tvOrderNumber.setText(data.get(position).sender_email);
                            holder.tvStatus.setVisibility(View.VISIBLE);
                            holder.tvConversion.setVisibility(View.GONE);
                            holder.tvddkamount.setVisibility(View.VISIBLE);
                            holder.tvAmount.setVisibility(View.VISIBLE);
                            holder.tvOrderType.setText("Send");
                            holder.tvAmount.setText("Receiver : " + data.get(position).receiver_email.toString());
                            holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                            holder.tvddkamount.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                            holder.ivHistoryIcon.setImageResource(R.drawable.ic_send);
                            holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                            holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_blue_cornor));
                            //............
                        }
                    }

                        if(data.get(position).payment_status!=null && !data.get(position).payment_status.toString().equalsIgnoreCase(""))
                        {
                        String upperString = data.get(position).payment_status.substring(0, 1).toUpperCase() + data.get(position).payment_status.toString().substring(1).toLowerCase();
                        if (data.get(position).payment_status.toString().equalsIgnoreCase("Verified"))
                        {
                            holder.tvStatus.setText(upperString);
                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_orange_back));

                        } else if (data.get(position).payment_status.toString().equalsIgnoreCase("Processing") || data.get(position).status.toString().equalsIgnoreCase("Reward"))
                        {
                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_green_back));
                            holder.tvStatus.setText(upperString);
                        } else if (data.get(position).payment_status.toString().equalsIgnoreCase("failed"))
                        {
                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_red_back));
                            holder.tvStatus.setText(upperString);
                        } else
                        {
                            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                            holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_card_back));
                            if (data.get(position).payment_status.toString().equalsIgnoreCase("Lend")) {
                                holder.tvStatus.setText("Active");
                            } else {
                                holder.tvStatus.setText(upperString);
                            }
                        }

                    }else
                    {
                        holder.tvStatus.setText("status not come");
                    }

                }
                if (holder.tvOrderType.getText().toString().trim().toLowerCase().equalsIgnoreCase("received"))
                {
                    holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_cornor_bg_green_5));
                    holder.ivHistoryIcon.setImageResource(R.drawable.buy_icon);
                }
            }else
            {
                if(data.get(position).transaction_type==null)
                {
                    holder.tvOrderType.setText("Subscription");
                }
                String amountvalue = String.valueOf(data.get(position).amount);
                holder.tvOrderNumber.setText(data.get(position).sender_address);
                if (!amountvalue.equalsIgnoreCase("")) {
                    if (data.get(position).amount.toString() != null)
                    {
                        holder.tvAmount.setVisibility(View.VISIBLE);
                        holder.tvAmount.setText(new DecimalFormat("##.####").format(data.get(position).amount) + " USDT");
                    }
                } else {
                    holder.tvAmount.setVisibility(View.GONE);
                }

                holder.tvConversion.setVisibility(View.VISIBLE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvddkamount.setVisibility(View.VISIBLE);
                String valuepaymentytpe=data.get(position).getMode();
                if(data.get(position).getMode()!=null)
                {
                    if (valuepaymentytpe.equalsIgnoreCase("sam koin"))
                    {
                        if (data.get(position).conversion != null)
                        {
                            holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " SAM Koin Conv.");
                        }
                        try
                        {
                            if(data.get(position).TotalUSDWithCharge!=null)
                            {
                                String trsnamsm=data.get(position).TotalUSDWithCharge;
                                holder.tvAmount.setText(trsnamsm+ " USDT");
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        holder.tvddkamount.setText(data.get(position).ddk + " SAM Koin");
                    } else
                    {
                        if (valuepaymentytpe.equalsIgnoreCase("eth"))
                        {
                            holder.tvddkamount.setText(data.get(position).ddk + " ETH");
                        }else
                        if (valuepaymentytpe.equalsIgnoreCase("btc"))
                        {
                            holder.tvddkamount.setText(data.get(position).ddk + " BTC");
                        }else
                        if (valuepaymentytpe.equalsIgnoreCase("USDT"))
                        {
                            holder.tvddkamount.setText(data.get(position).ddk + " USDT");
                        }else if (valuepaymentytpe.equalsIgnoreCase("DDK"))
                        {
                            holder.tvddkamount.setText(data.get(position).ddk + " DDK");
                        }else if (valuepaymentytpe.equalsIgnoreCase("credit card"))
                        {
                            holder.tvddkamount.setText(data.get(position).ddk + " USDT");
                        }

                        if (data.get(position).conversion != null)
                        {
                            holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " DDK Conv.");
                        }
                    }
                }else
                {
                    if (data.get(position).conversion != null)
                    {
                        holder.tvConversion.setText(new DecimalFormat("##.####").format(data.get(position).conversion) + " DDK Conv.");
                    }
                    holder.tvddkamount.setText(data.get(position).ddk + " DDK");
                }
                holder.tvConversion.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                holder.tvddkamount.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                holder.ivHistoryIcon.setImageResource(R.drawable.subscription_new);
                holder.tvAmount.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                holder.ivHistoryIcon.setBackground(ContextCompat.getDrawable(activity, R.drawable.card_blue_cornor));
                String upperString = data.get(position).status.substring(0, 1).toUpperCase() + data.get(position).status.substring(1).toLowerCase();
                if (data.get(position).status.toString().equalsIgnoreCase("Verified")) {
                    holder.tvStatus.setText(upperString);
                    holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.orange));
                    holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_orange_back));

                } else if (data.get(position).status.toString().equalsIgnoreCase("Processing") || data.get(position).status.toString().equalsIgnoreCase("Reward")) {

                    holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorGreen));
                    holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_green_back));
                    holder.tvStatus.setText(upperString);
                } else if (data.get(position).status.toString().equalsIgnoreCase("failed")) {
                    holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
                    holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_red_back));
                    holder.tvStatus.setText(upperString);
                } else {
                    holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.darkblue));
                    holder.tvStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.light_card_back));
                    if (data.get(position).status.toString().equalsIgnoreCase("Lend")) {
                        holder.tvStatus.setText("ACTIVE");
                    } else {
                        holder.tvStatus.setText(upperString);
                    }
                }
                holder.tvFees.setVisibility(View.GONE);

            }

            holder.viewclickdata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if(data.get(position).outgoing_asset_type!=null)
                    {
                        String senderaddress = data.get(position).outgoing_asset_sender;
                        if (senderaddress == null || senderaddress.equalsIgnoreCase(null) || senderaddress.equalsIgnoreCase("null")) {
                            senderaddress = "";
                        }
                        String receivr = data.get(position).outgoing_asset_receiver;
                        if (receivr == null || receivr.equalsIgnoreCase(null) || receivr.equalsIgnoreCase("null")) {
                            receivr = "";
                        }

                        String type = "Received";
                        Log.d("add", "" + senderaddress + "::" + receivr + "::" + type);
                        ShowTransationHistory(activity, senderaddress, receivr, type);

                    }else if(data.get(position).transaction_type.toString().equalsIgnoreCase("currency_wallet"))
                    {
                        String senderaddress = "";
                        if(holder.tvOrderNumber.getText().toString().equalsIgnoreCase("Admin"))
                        {
                            if (senderaddress == null || senderaddress.equalsIgnoreCase(null) || senderaddress.equalsIgnoreCase("null")) {
                                senderaddress = "Admin";
                            }else
                            {
                                senderaddress = "Admin";
                            }
                        }else
                        {
                            if (senderaddress == null || senderaddress.equalsIgnoreCase(null) || senderaddress.equalsIgnoreCase("null")) {
                                senderaddress = "";
                            }else
                            {
                                senderaddress = data.get(position).sender_email;
                            }
                        }

                        String receivr = data.get(position).receiver_email;
                        if (receivr == null || receivr.equalsIgnoreCase(null) || receivr.equalsIgnoreCase("null")) {
                            receivr = "";
                        } else {
                            receivr = data.get(position).receiver_email.toString();
                        }

                        String type = data.get(position).transaction_type.toString();
                        Log.d("add", "" + senderaddress + "::" + receivr + "::" + type);
                        ShowTransationHistory(activity, senderaddress, receivr, type);

                    }else{
                        String senderaddress = data.get(position).sender_address;
                        if (senderaddress == null || senderaddress.equalsIgnoreCase(null) || senderaddress.equalsIgnoreCase("null")) {
                            senderaddress = "";
                        }
                        String receivr = data.get(position).receiver_address;
                        if (receivr == null || receivr.equalsIgnoreCase(null) || receivr.equalsIgnoreCase("null")) {
                            receivr = "";
                        } else {
                            receivr = data.get(position).receiver_address.toString();
                        }

                        String type = data.get(position).transaction_type.toString();
                        Log.d("add", "" + senderaddress + "::" + receivr + "::" + type);
                        ShowTransationHistory(activity, senderaddress, receivr, type);

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
        public TextView tvddkamount,tvConversion,tvFees,tvStatus,tvDateTime, tvOrderNumber, tvAmount, tvOrderType;
        public ImageView ivHistoryIcon;
        public LinearLayout viewclickdata;

        public MyViewHolder(View view) {
            super(view);
            tvFees=view.findViewById(R.id.tvFees);
            viewclickdata=view.findViewById(R.id.viewclickdata);
            tvddkamount=view.findViewById(R.id.tvddkamount);
            tvConversion=view.findViewById(R.id.tvConversion);
            tvStatus=view.findViewById(R.id.tvStatus);
            tvDateTime = view.findViewById(R.id.tvDateTime);
            ivHistoryIcon = view.findViewById(R.id.ivHistoryIcon);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvOrderNumber = view.findViewById(R.id.tvOrderNumber);
            tvOrderType = view.findViewById(R.id.tvOrderType);
        }
    }
}
