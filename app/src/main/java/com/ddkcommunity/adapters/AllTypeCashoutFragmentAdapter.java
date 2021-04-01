package com.ddkcommunity.adapters;

import android.app.Activity;
import android.content.Context;
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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.CashOutFragmentNew;
import com.ddkcommunity.fragment.CashoutBankFragement;
import com.ddkcommunity.model.AllAvailableBankList;
import com.ddkcommunity.model.BankList;
import com.ddkcommunity.model.UserBankList;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.dataPutMethods;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.ddkcommunity.Constant.SLIDERIMG;
import static com.ddkcommunity.fragment.CashOutFragmentNew.banknotabv;
import static com.ddkcommunity.fragment.CashOutFragmentNew.msgbank;
import static com.ddkcommunity.utilies.dataPutMethods.ShowSameWalletDialog;
import static com.ddkcommunity.utilies.dataPutMethods.sendOtp;

public class AllTypeCashoutFragmentAdapter extends RecyclerView.Adapter<AllTypeCashoutFragmentAdapter.MyViewHolder> {

    int count=0;
    private List<BankList.BankData> createCancellationRequestlist;
    private Activity activity;
    private String imagePath;
    TextView ll_SelectBankName;
    public static int usercountryselect;
    CashOutFragmentNew cashoutnewclass;

    public AllTypeCashoutFragmentAdapter(int usercountryselect,List<BankList.BankData> createCancellationRequestlist, Activity activity,CashOutFragmentNew cahsoutne) {
        this.createCancellationRequestlist=createCancellationRequestlist;
        this.activity = activity;
        this.usercountryselect=usercountryselect;
        this.cashoutnewclass=cahsoutne;
     }

    public void updateData(List<BankList.BankData> data, String image_path) {
        this.createCancellationRequestlist = data;
        this.imagePath = image_path;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_banl, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            if(usercountryselect==1)
            {
                //for php
                if (createCancellationRequestlist.get(position).getBank_name().equalsIgnoreCase("All"))
                {
                    holder.iconaddmore.setVisibility(View.VISIBLE);
                    holder.audlayout.setVisibility(View.GONE);
                    holder.ll_allBankId.setVisibility(View.VISIBLE);
                    holder.ll_allBankIconId.setVisibility(View.GONE);
                }else
                {
                    holder.ll_allBankId.setVisibility(View.GONE);
                    holder.ll_allBankIconId.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(SLIDERIMG + createCancellationRequestlist.get(position).image).into(holder.iv_BankIconDDK);
                    holder.audlayout.setVisibility(View.GONE);
                }
                holder.audtvBankNameDDk.setText(createCancellationRequestlist.get(position).getBank_name());
            }else
            {
                if (createCancellationRequestlist.get(position).getBank_name().toString().equalsIgnoreCase("All"))
                {
                    holder.audlayout.setVisibility(View.GONE);
                    holder.ll_allBankId.setVisibility(View.VISIBLE);
                    holder.iconaddmore.setVisibility(View.GONE);
                    holder.ll_allBankIconId.setVisibility(View.GONE);
                }else
                {
                    holder.audlayout.setVisibility(View.VISIBLE);
                    holder.ll_allBankId.setVisibility(View.GONE);
                    holder.ll_allBankIconId.setVisibility(View.GONE);
                    Glide.with(activity).load(SLIDERIMG + createCancellationRequestlist.get(position).image).into(holder.iv_BankIconDDK);
                }
                holder.audtvBankNameDDk.setText(createCancellationRequestlist.get(position).getBank_name());
            }

            holder.ll_allBankId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String calue  = ((CashOutFragmentNew)cashoutnewclass).checkValidation();
                    if (calue.equals("complete"))
                    {
                            String ddkaddress = ((CashOutFragmentNew) cashoutnewclass).getValueData("address");
                            String ddkamount = ((CashOutFragmentNew) cashoutnewclass).getValueData("ddk");
                            String phpamount = ((CashOutFragmentNew) cashoutnewclass).getValueData("php");
                            String secrate = ((CashOutFragmentNew) cashoutnewclass).getValueData("secrate");
                            String conversionrate = ((CashOutFragmentNew) cashoutnewclass).getValueData("conversionrate");
                            Fragment fragment = new CashoutBankFragement();
                            Bundle arg = new Bundle();
                            fragment.setArguments(arg);
                            MainActivity.addFragment(fragment, true);
                        }
                }
            });

            holder.ll_allBankIconId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String calue  = ((CashOutFragmentNew)cashoutnewclass).checkValidation();
                    if (calue.equals("complete")) {
                        String ddkaddress = ((CashOutFragmentNew) cashoutnewclass).getValueData("address");
                        String ddkamount = ((CashOutFragmentNew) cashoutnewclass).getValueData("ddk");
                        String phpamount = ((CashOutFragmentNew) cashoutnewclass).getValueData("php");
                        String secrate = ((CashOutFragmentNew) cashoutnewclass).getValueData("secrate");
                        String conversionrate = ((CashOutFragmentNew) cashoutnewclass).getValueData("conversionrate");
                        String bankName = createCancellationRequestlist.get(position).bank_name;
                        String bankimage = createCancellationRequestlist.get(position).image;
                        String banktype = createCancellationRequestlist.get(position).getType();
                        String bankid = createCancellationRequestlist.get(position).id;
                        String id = "";
                        String allview=holder.audtvBankNameDDk.getText().toString();
                        if(allview.equalsIgnoreCase("All"))
                        {
                            Fragment fragment = new CashoutBankFragement();
                            Bundle arg = new Bundle();
                            fragment.setArguments(arg);
                            MainActivity.addFragment(fragment, true);

                        }else {
                            if (createCancellationRequestlist.get(position).getType().equals("bank")) {
                                showDialogBankTrnsfer(id, conversionrate, secrate, phpamount, ddkamount, ddkaddress, view, activity, imagePath, bankName, bankimage, bankid, banktype);
                            } else {
                                showDialogGcash(id, bankid, conversionrate, secrate, phpamount, ddkamount, ddkaddress, view, activity, bankName, bankimage, banktype, cashoutnewclass);
                            }
                        }
                    }
                  }
            });

            holder.audlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    String calue  = ((CashOutFragmentNew)cashoutnewclass).checkValidation();
                    if (calue.equals("complete"))
                    {
                        String bankname=holder.audtvBankNameDDk.getText().toString();
                        if(bankname.equalsIgnoreCase("all"))
                        {
                                String ddkaddress = ((CashOutFragmentNew) cashoutnewclass).getValueData("address");
                                String ddkamount = ((CashOutFragmentNew) cashoutnewclass).getValueData("ddk");
                                String phpamount = ((CashOutFragmentNew) cashoutnewclass).getValueData("php");
                                String secrate = ((CashOutFragmentNew) cashoutnewclass).getValueData("secrate");
                                String conversionrate = ((CashOutFragmentNew) cashoutnewclass).getValueData("conversionrate");
                                Fragment fragment = new CashoutBankFragement();
                                Bundle arg = new Bundle();
                                fragment.setArguments(arg);
                                MainActivity.addFragment(fragment, true);
                        }else {
                            String ddkaddress = ((CashOutFragmentNew) cashoutnewclass).getValueData("address");
                            String ddkamount = ((CashOutFragmentNew) cashoutnewclass).getValueData("ddk");
                            String phpamount = ((CashOutFragmentNew) cashoutnewclass).getValueData("php");
                            String secrate = ((CashOutFragmentNew) cashoutnewclass).getValueData("secrate");
                            String conversionrate = ((CashOutFragmentNew) cashoutnewclass).getValueData("conversionrate");
                            String bankName = createCancellationRequestlist.get(position).bank_name;
                            String bankimage = createCancellationRequestlist.get(position).image;
                            String banktype = createCancellationRequestlist.get(position).getType();
                            String  bankid = createCancellationRequestlist.get(position).id;
                            String  id = "";
                            if (createCancellationRequestlist.get(position).getType().equals("bank")) {
                                showDialogBankTrnsfer(id,conversionrate, secrate, phpamount, ddkamount, ddkaddress, v, activity, imagePath, bankName, bankimage, bankid, banktype);
                            } else {
                                showDialogGcash(id,bankid, conversionrate, secrate, phpamount, ddkamount, ddkaddress, v, activity, bankName, bankimage, banktype, cashoutnewclass);
                            }
                        }
                    }
                }
            });


        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void showDialogGcash(final String id,final String bankid,final String conversionrate,final String secrate,final String phpamount,final String ddkamount,final String ddkaddress,final View mview, final Activity mContext,final String bankName,final String bankimg,final String banktype,final CashOutFragmentNew cashOutFragmentNew)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View dialogView = layoutInflater.inflate(R.layout.popup_gcash, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
        dialog.setContentView(dialogView);
        TextView tvSendMoney = dialogView.findViewById(R.id.tvSendMoney);
        final TextView tvUpdate = dialogView.findViewById(R.id.tvUpdate);
        TextView tv_CountryId = dialogView.findViewById(R.id.tv_CountryId);
        TextView tv_ConMaxTransfer = dialogView.findViewById(R.id.tv_ConMaxTransfer);
        final EditText et_gcashValue = dialogView.findViewById(R.id.et_gcashValue);
        final EditText et_name = dialogView.findViewById(R.id.et_name);
        ImageView ivGcashLogo = dialogView.findViewById(R.id.ivGcashLogo);
        final View view = dialogView.findViewById(R.id.view);
        tvUpdate.setVisibility(View.GONE);
        final EditText et_gcahsname=dialogView.findViewById(R.id.et_gcahsname);
        final LinearLayout gcahname_layout=dialogView.findViewById(R.id.gcahname_layout);
        gcahname_layout.setVisibility(View.VISIBLE);
        dialog.show();
        et_name.setHint("Enter GCash Number");
        et_gcashValue.setText(phpamount);
        et_gcahsname.setEnabled(true);
        et_name.setEnabled(true);
        Glide.with(mContext).asBitmap().load(SLIDERIMG+ bankimg).into(ivGcashLogo);
        UserResponse userData = AppConfig.getUserData(mContext);
        String countrydata=userData.getUser().country.get(0).country;
        Log.d("country",countrydata);
        if(countrydata!=null && countrydata.equalsIgnoreCase("Philippines")){
            tv_CountryId.setText("PHP");
            tv_ConMaxTransfer.setText("Maximum of PHP 50,000");
        }else {
            tv_CountryId.setText("AUD");
            tv_ConMaxTransfer.setText("Maximum of AUD 50,000");
        }
        tv_ConMaxTransfer.setVisibility(View.GONE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tvSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String et_gcashnamevalue=et_name.getText().toString();
                if(et_gcahsname.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please enter GCash Name", Toast.LENGTH_SHORT).show();
                }else
                if(et_gcashnamevalue.equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please enter GCash Number", Toast.LENGTH_SHORT).show();
                }else {

                    if(et_gcashnamevalue.length()<=20) {
                        dialog.dismiss();
                        //.....
                        BigDecimal countamoutn = new BigDecimal("50");
                        BigDecimal btccondition = new BigDecimal(phpamount);
                        int comare = btccondition.compareTo(countamoutn);

                        {
                            dialog.dismiss();
                            //for send money call
                            String bank_type = banktype;
                            String holder_name = "";
                            String gcash_no = "", account_no = "";
                            if (bank_type.equalsIgnoreCase("bank")) {
                                account_no = "";
                                gcash_no = "";
                                holder_name = et_name.getText().toString();
                            } else {
                                account_no = "";
                                gcash_no = et_name.getText().toString();
                                holder_name = et_gcahsname.getText().toString();
                            }
                            String email = "";
                            String bank_id = bankid + "";
                            sendOtp(id, bank_type, holder_name, account_no, gcash_no, email, bank_id, conversionrate, secrate, phpamount, ddkamount, ddkaddress, mContext);
                        }
                    }else
                    {
                        Toast.makeText(mContext, "Invalid GCash Number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void showDialogBankTrnsfer(final String id,final String conversionrate, final String secrate, final String phpamount, final String ddkamount, final String ddkaddress, final View mview, final Activity mContext, String imagePath, final String bankName, String bankImage, final String bankid, final String banktype)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View dialogView = layoutInflater.inflate(R.layout.popup_searchbanktransfer, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
        dialog.setContentView(dialogView);
        TextView tvSendMoney = dialogView.findViewById(R.id.tvSendMoney);
        TextView tv_CountryId = dialogView.findViewById(R.id.tv_CountryId);
        TextView tv_ConMaxTransfer = dialogView.findViewById(R.id.tv_ConMaxTransfer);
        TextView tvBankNameDDk = dialogView.findViewById(R.id.tvBankNameDDk);
        ImageView ivBankLogo = dialogView.findViewById(R.id.ivBankLogo);
        final EditText etAccountNumber = dialogView.findViewById(R.id.etAccountNumber);
        final EditText etEmailReceipt = dialogView.findViewById(R.id.etEmailReceipt);
        EditText etAmountSend = dialogView.findViewById(R.id.etAmountSend);
        final EditText etAccountName = dialogView.findViewById(R.id.etAccountName);
        View view = dialogView.findViewById(R.id.view);
        dialog.show();
        etAmountSend.setText(phpamount);
        UserResponse userData = AppConfig.getUserData(mContext);
        final String countrydata=userData.getUser().country.get(0).country;
        Log.d("country",countrydata);
        if(countrydata!=null && countrydata.equalsIgnoreCase("Philippines")){
            tv_CountryId.setText("PHP");
            ivBankLogo.setVisibility(View.VISIBLE);
            etAccountName.setHint("Enter Account Name");
        }else {
            ivBankLogo.setVisibility(View.INVISIBLE);
            tv_CountryId.setText("AUD");
            etAccountName.setHint("Enter BSB Name");
           }
        tv_ConMaxTransfer.setVisibility(View.GONE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tvBankNameDDk.setText(bankName);
        Glide.with(mContext).load(SLIDERIMG + bankImage).into(ivBankLogo);

        tvSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(banknotabv.equalsIgnoreCase("0"))
                {
                    ShowSameWalletDialog(activity,msgbank,"1");
                }else {

                    String holder_name = etAccountName.getText().toString();
                    String etAccountNumbervalue = etAccountNumber.getText().toString();
                    if (holder_name.equalsIgnoreCase(""))
                    {
                        if(countrydata!=null && countrydata.equalsIgnoreCase("Philippines")){
                            Toast.makeText(mContext, "Please enter Account Name", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext, "Please enter BSB Name", Toast.LENGTH_SHORT).show();
                        }
                    } else if (etAccountNumbervalue.equalsIgnoreCase("")) {
                        Toast.makeText(mContext, "Please enter Account Number", Toast.LENGTH_SHORT).show();
                    } else {
                        //Bigde
                        if (etAccountNumber.getText().toString().length() <= 20)
                        {
                            BigDecimal countamoutn = new BigDecimal("50");
                            BigDecimal btccondition = new BigDecimal(phpamount);
                            int comare = btccondition.compareTo(countamoutn);
                            {
                                dialog.dismiss();
                                //for send money call
                                String bank_type = banktype;
                                String gcash_no = "", account_no = "";
                                if (bank_type.toString().equalsIgnoreCase("bank")) {
                                    account_no = etAccountNumber.getText().toString();
                                    gcash_no = "";
                                } else {
                                    account_no = "";
                                    gcash_no = gcash_no;
                                }
                                String email = etEmailReceipt.getText().toString();
                                String bank_id = bankid + "";
                                sendOtp(id, bank_type, holder_name, account_no, gcash_no, email, bank_id, conversionrate, secrate, phpamount, ddkamount, ddkaddress, mContext);
                            }

                        } else {
                            Toast.makeText(mContext, "Invalid Account Number", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }

    public void getSelectedBank(int position, List<AllAvailableBankList.BankData> createCancellationRequestlist){
        ll_SelectBankName.setText(createCancellationRequestlist.get(position).getBank_name());
    }

    @Override
    public int getItemCount() {

        count = createCancellationRequestlist.size();
        if (count>9)
        {
          count = 9;
        }
        else
        {
            count = createCancellationRequestlist.size();
        }

        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ic_addicon,iv_BankIconDDK,iconaddmore;
        LinearLayout audlayout,ll_allBankId,ll_allBankIconId;
        TextView audtvBankNameDDk;

        public MyViewHolder(View view) {
            super(view);
            iconaddmore=view.findViewById(R.id.iconaddmore);
            audtvBankNameDDk=view.findViewById(R.id.audtvBankNameDDk);
            audlayout=view.findViewById(R.id.audlayout);
            ic_addicon=view.findViewById(R.id.ic_addicon);
            iv_BankIconDDK=view.findViewById(R.id.iv_BankIconDDK);
            ll_allBankId=view.findViewById(R.id.ll_allBankId);
            ll_allBankIconId=view.findViewById(R.id.ll_allBankIconId);
        }
    }

}
