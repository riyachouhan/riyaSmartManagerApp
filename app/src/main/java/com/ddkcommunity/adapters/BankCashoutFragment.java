package com.ddkcommunity.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.CashOutFragmentNew;
import com.ddkcommunity.model.BankList;
import com.ddkcommunity.model.UserBankList;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.dataPutMethods;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.Constant.SLIDERIMG;
import static com.ddkcommunity.adapters.AllTypeCashoutFragmentAdapter.usercountryselect;
import static com.ddkcommunity.fragment.CashOutFragmentNew.banknotabv;
import static com.ddkcommunity.fragment.CashOutFragmentNew.msgbank;
import static com.ddkcommunity.utilies.dataPutMethods.ShowSameWalletDialog;
import static com.ddkcommunity.utilies.dataPutMethods.ShowServerPost;
import static com.ddkcommunity.utilies.dataPutMethods.sendOtp;
import static com.ddkcommunity.utilies.dataPutMethods.showDialogGcash;

public class BankCashoutFragment extends RecyclerView.Adapter<BankCashoutFragment.MyViewHolder> {

    private List<BankList.BankData> data;
    private String cardType;
    private SetOnItemClickListener setOnItemClickListener;
    private Activity mContext;
    private String imagePath;
    int usercountryselect;
    EditText etAccountName,etAmountSend,etEmailReceipt,etAccountNumber;

    public BankCashoutFragment(int usercountryselect,Activity mContext, String imagePath, List<BankList.BankData> data, SetOnItemClickListener setOnItemClickListener) {
        this.data = data;
        this.usercountryselect=usercountryselect;
        this.mContext = mContext;
        this.imagePath = imagePath;
        this.setOnItemClickListener = setOnItemClickListener;
    }

    public void updateData(List<BankList.BankData> data, String image_path) {
        this.data = data;
        this.imagePath = image_path;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bankitesmlayout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (usercountryselect==1)
        {
            holder.tvBankNameDDk.setText(data.get(position).bank_name);
            Glide.with(mContext).load(SLIDERIMG+ data.get(position).image).into(holder.ivBankIconDDK);
        }else
        {
            holder.tvBankNameDDk.setText(data.get(position).bank_name);
            holder.ivBankIconDDK.setVisibility(View.GONE);
            Glide.with(mContext).load(SLIDERIMG + data.get(position).image).into(holder.ivBankIconDDK);
        }
        holder.ll_selectedBank.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String bankName = data.get(position).bank_name;
                String bankimage = data.get(position).image;
                String banktype = data.get(position).getType();
                String bankid = data.get(position).id;
                String ddkaddress = ((CashOutFragmentNew) CashOutFragmentNew.CashOutFragmentasub).getValueData("address");
                String ddkamount = ((CashOutFragmentNew) CashOutFragmentNew.CashOutFragmentasub).getValueData("ddk");
                String phpamount = ((CashOutFragmentNew) CashOutFragmentNew.CashOutFragmentasub).getValueData("php");
                String secrate = ((CashOutFragmentNew) CashOutFragmentNew.CashOutFragmentasub).getValueData("secrate");
                String conversionrate = ((CashOutFragmentNew) CashOutFragmentNew.CashOutFragmentasub).getValueData("conversionrate");
                String id="";
                if (banktype.equals("bank")) {
                    showDialogBankTrnsfer(id,conversionrate, secrate, phpamount, ddkamount, ddkaddress, view, mContext, imagePath, bankName, bankimage, bankid, banktype);
                } else {
                    showDialogGcash(id,bankid,conversionrate,secrate,phpamount,ddkamount,ddkaddress,view,mContext,bankName,bankimage,banktype,CashOutFragmentNew.CashOutFragmentasub);
                }
            }
        });
        Log.e("bank_name","::"+data.get(position).bank_name);
        Log.d("response banklist","adapter set");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivBankIconDDK,ic_addicon;
        private TextView tvBankNameDDk;
        LinearLayout ll_selectedBank;

        public MyViewHolder(View view) {
            super(view);

            ivBankIconDDK = view.findViewById(R.id.ivBankIconDDK);
            tvBankNameDDk = view.findViewById(R.id.tvBankNameDDk);
            ll_selectedBank = view.findViewById(R.id.ll_selectedBank);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnItemClickListener.onItemClick(data.get(getAdapterPosition()).bank_name, imagePath + data.get(getAdapterPosition()).image, "" + data.get(getAdapterPosition()).id);
                }
            });
        }
    }

    public interface SetOnItemClickListener {
        public void onItemClick(String name, String image, String id);
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
        final EditText et_gcahsname=dialogView.findViewById(R.id.et_gcahsname);
        final LinearLayout gcahname_layout=dialogView.findViewById(R.id.gcahname_layout);
        gcahname_layout.setVisibility(View.VISIBLE);
        final View view = dialogView.findViewById(R.id.view);
        tvUpdate.setVisibility(View.GONE);
        dialog.show();
        et_gcashValue.setText(phpamount);
        et_name.setHint("Enter GCash Number");
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
                String et_gcashno=et_name.getText().toString();
                if(et_gcashno.equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Please enter GCash Number", Toast.LENGTH_SHORT).show();
                }else {

                    if(et_gcashno.length()<=20) {
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

        tvUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
                dataPutMethods.showDialogConfirmGcashNew(et_name.getText().toString(),banktype, String.valueOf(bankid),bankName,mview,mContext,cashOutFragmentNew);
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
        final TextView tv_CountryId = dialogView.findViewById(R.id.tv_CountryId);
        final TextView tv_ConMaxTransfer = dialogView.findViewById(R.id.tv_ConMaxTransfer);
        TextView tvBankNameDDk = dialogView.findViewById(R.id.tvBankNameDDk);
        final ImageView ivBankLogo = dialogView.findViewById(R.id.ivBankLogo);
        final EditText etAccountNumber = dialogView.findViewById(R.id.etAccountNumber);
        final EditText etEmailReceipt = dialogView.findViewById(R.id.etEmailReceipt);
        EditText etAmountSend = dialogView.findViewById(R.id.etAmountSend);
        final EditText etAccountName = dialogView.findViewById(R.id.etAccountName);
        View view = dialogView.findViewById(R.id.view);
        dialog.show();
        etAmountSend.setText(phpamount);
        final UserResponse userData = AppConfig.getUserData(mContext);
        final String countrydata=userData.getUser().country.get(0).country;
        Log.d("country",countrydata);
        if(countrydata!=null && countrydata.equalsIgnoreCase("Philippines")){
            tv_CountryId.setText("PHP");
            ivBankLogo.setVisibility(View.VISIBLE);
            tv_ConMaxTransfer.setText("Maximum of PHP 50,000");
            etAccountName.setHint("Enter Account Name");
        }else {
            tv_CountryId.setText("AUD");
            ivBankLogo.setVisibility(View.INVISIBLE);
            etAccountName.setHint("Enter BSB Name");
            tv_ConMaxTransfer.setText("Maximum of AUD 50,000");
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
            public void onClick(View view)
            {
                if(banknotabv.equalsIgnoreCase("0"))
                {
                    ShowSameWalletDialog(mContext,msgbank,"1");
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
                        } else
                        {
                            Toast.makeText(mContext, "Invalid Account Number", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }

}
