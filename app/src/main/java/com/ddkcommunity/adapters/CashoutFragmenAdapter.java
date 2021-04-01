package com.ddkcommunity.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ddkcommunity.Constant;
import com.ddkcommunity.LoadInterface;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.CashOutFragmentNew;
import com.ddkcommunity.model.BankList;
import com.ddkcommunity.model.CashOutAddBankResponse;
import com.ddkcommunity.model.UserBankList;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.ConnectionDetector;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.Constant.SLIDERIMG;
import static com.ddkcommunity.utilies.dataPutMethods.ShowServerPost;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;
import static com.ddkcommunity.utilies.dataPutMethods.showDialogBankTrnsfer;
import static com.ddkcommunity.utilies.dataPutMethods.showDialogGcash;
import static com.facebook.FacebookSdk.getApplicationContext;

public class CashoutFragmenAdapter extends RecyclerView.Adapter<CashoutFragmenAdapter.MyViewHolder> {

    private List<UserBankList> createCancellationRequestlist;
    private List<BankList.BankData> bankList = new ArrayList<>();
    private Activity activity;
    TextView ll_SelectBankName;
    private String imagePath;
    private UserResponse userData;
    TextView btn_save;
    EditText et_GCashNo,et_AccountNo,et_bankUserName;
    String selectedBank="";
    String imaagepath,amountentervalue;
    String selectedBankId;
    CashOutFragmentNew cashOutFragmentNew;
    int usercountryselect;

    public void updateData(ArrayList<UserBankList> createCancellationRequestlist) {
        this.createCancellationRequestlist = createCancellationRequestlist;
        notifyDataSetChanged();
    }

    public CashoutFragmenAdapter(int usercountryselect,ArrayList<UserBankList> createCancellationRequestlist, Activity activity, CashOutFragmentNew cashOutFragmentNewClass) {
        this.createCancellationRequestlist=createCancellationRequestlist;
        this.activity = activity;
        this.usercountryselect=usercountryselect;
        this.cashOutFragmentNew = cashOutFragmentNewClass;
     }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_banl, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        userData = AppConfig.getUserData(activity);

        try {
            final String banknem=createCancellationRequestlist.get(position).getBank().getBankName();
            if(usercountryselect==1)
            {
                holder.audlayout.setVisibility(View.GONE);
                holder.phlayout.setVisibility(View.VISIBLE);
                holder.ic_addicon.setVisibility(View.GONE);
                holder.ivBankIconDDK.setVisibility(View.GONE);
                String niconm=SLIDERIMG+createCancellationRequestlist.get(position).getBank().getImage();
                Glide.with(activity).asBitmap().load(niconm).into(holder.ivBankIconDDK);
                if (createCancellationRequestlist.get(position).getBank().getBankName().equals("Addoption")){
                    holder.ic_addicon.setVisibility(View.VISIBLE);
                    holder.ivBankIconDDK.setVisibility(View.GONE);
                    holder.tvBankNameDDk.setVisibility(View.GONE);
                    holder.phpiconview.setVisibility(View.GONE);
                    holder.tvBankNameDDk.setText(createCancellationRequestlist.get(position).getBank().getBankName());
                }else {
                    holder.phpiconview.setVisibility(View.VISIBLE);
                    String headtext=createCancellationRequestlist.get(position).getName();
                    if(!headtext.equalsIgnoreCase("") && headtext!=null)
                    {
                        String rnejek=headtext.charAt(0)+"";
                        holder.headelogotext.setText(rnejek.toUpperCase()+"");
                    }
                    holder.tvBankNameDDk.setText(createCancellationRequestlist.get(position).getBank().getBankName());
                }
                //for new

            }else
            {
                holder.audic_addicon.setVisibility(View.GONE);
                holder.audlayout.setVisibility(View.VISIBLE);
                holder.phlayout.setVisibility(View.GONE);
                holder.ivBankIconDDK.setVisibility(View.GONE);
                holder.phpiconview.setVisibility(View.GONE);
                if (createCancellationRequestlist.get(position).getBank().getBankName().equals("Addoption")){
                    holder.audic_addicon.setVisibility(View.VISIBLE);
                    holder.audtvBankNameDDk.setVisibility(View.GONE);
                }else {
                    holder.audtvBankNameDDk.setText(createCancellationRequestlist.get(position).getBank().getBankName());
                }

            }

            holder.audic_addicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    String calue = ((CashOutFragmentNew) cashOutFragmentNew).checkValidation();
                    if (calue.equals("complete")) {

                        LayoutInflater layoutInflater = LayoutInflater.from(activity);
                        View dialogView = layoutInflater.inflate(R.layout.popup_trasfer, null);
                        final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.DialogStyle);
                        dialog.setContentView(dialogView);
                        dialog.show();
                        et_GCashNo = dialog.findViewById(R.id.et_GCashNo);
                        et_AccountNo = dialog.findViewById(R.id.et_AccountNo);
                        et_bankUserName = dialog.findViewById(R.id.et_bankUserName);
                        btn_save = dialog.findViewById(R.id.btn_saveBank);
                        LinearLayout ll_SelectBank = dialog.findViewById(R.id.ll_SelectBank);
                        ll_SelectBankName = dialog.findViewById(R.id.ll_SelectBankName);
                        et_bankUserName.setVisibility(View.GONE);
                        et_AccountNo.setVisibility(View.GONE);
                        et_GCashNo.setVisibility(View.GONE);
                        btn_save.setVisibility(View.GONE);
                        ll_SelectBank.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getBankList(userData.getUser().country.get(0).id, view);
                            }
                        });

                        btn_save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String name = et_bankUserName.getText().toString();
                                String GcashNo = et_GCashNo.getText().toString();
                                String AccountNo = et_AccountNo.getText().toString();
                                if (name.equals("") || name.isEmpty())
                                {
                                    String countrydata=userData.getUser().country.get(0).country;
                                    Log.d("country",countrydata);
                                    if(countrydata!=null && countrydata.equalsIgnoreCase("Philippines")){
                                        if (selectedBank.equalsIgnoreCase("bank")) {
                                            et_bankUserName.setError("Please Enter Name");
                                        }else
                                        {
                                            et_bankUserName.setError("Please Enter GCash Name");
                                        }
                                    }else {
                                        et_bankUserName.setError("Please BSB name");
                                    }
                                    et_bankUserName.requestFocus();
                                } else if (selectedBank.equalsIgnoreCase("")) {
                                    Toast.makeText(getApplicationContext(), "Please Select Bank or E-remittance", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (selectedBank.equalsIgnoreCase("bank")) {

                                        if (AccountNo.equalsIgnoreCase("")) {
                                            Toast.makeText(getApplicationContext(), "Please Enter Account Number", Toast.LENGTH_SHORT).show();
                                        } else
                                        {
                                            if (AccountNo.length() <= 20)
                                            {
                                                acctivityGsonbody(dialog);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Invalid Account Number", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } else {
                                        if (GcashNo.equalsIgnoreCase("")) {
                                            Toast.makeText(getApplicationContext(), "Please Enter GCash Number", Toast.LENGTH_SHORT).show();

                                        } else {
                                            if (GcashNo.length() <= 20)
                                            {
                                                acctivityGsonbody(dialog);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Invalid GCash Number", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            });

            holder.ic_addicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String calue = ((CashOutFragmentNew) cashOutFragmentNew).checkValidation();
                    if (calue.equals("complete")) {
                        LayoutInflater layoutInflater = LayoutInflater.from(activity);
                        View dialogView = layoutInflater.inflate(R.layout.popup_trasfer, null);
                        final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.DialogStyle);
                        dialog.setContentView(dialogView);
                        dialog.show();
                        et_GCashNo = dialog.findViewById(R.id.et_GCashNo);
                        et_AccountNo = dialog.findViewById(R.id.et_AccountNo);
                        et_bankUserName = dialog.findViewById(R.id.et_bankUserName);
                        btn_save = dialog.findViewById(R.id.btn_saveBank);
                        LinearLayout ll_SelectBank = dialog.findViewById(R.id.ll_SelectBank);
                        ll_SelectBankName = dialog.findViewById(R.id.ll_SelectBankName);
                        String countrydata=userData.getUser().country.get(0).country;
                        et_bankUserName.setVisibility(View.GONE);
                        et_AccountNo.setVisibility(View.GONE);
                        et_GCashNo.setVisibility(View.GONE);
                        btn_save=dialog.findViewById(R.id.btn_saveBank);
                        btn_save.setVisibility(View.GONE);
                        Log.d("country",countrydata);
                        ll_SelectBank.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getBankList(userData.getUser().country.get(0).id, view);
                            }
                        });

                        btn_save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String name = et_bankUserName.getText().toString();
                                String GcashNo = et_GCashNo.getText().toString();
                                String AccountNo = et_AccountNo.getText().toString();
                                if (name.equals("") || name.isEmpty())
                                {
                                    String countrydata=userData.getUser().country.get(0).country;
                                    Log.d("country",countrydata);
                                    if(countrydata!=null && countrydata.equalsIgnoreCase("Philippines")){
                                        if (selectedBank.equalsIgnoreCase("bank")) {
                                            et_bankUserName.setError("Please Enter Name");
                                        }else
                                        {
                                            et_bankUserName.setError("Please Enter GCash Name");
                                        }
                                    }else {
                                        et_bankUserName.setError("Please BSB name");
                                    }
                                    et_bankUserName.requestFocus();
                                } else if (selectedBank.equalsIgnoreCase("")) {
                                    Toast.makeText(getApplicationContext(), "Please Select Bank or E-remittance", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (selectedBank.equalsIgnoreCase("bank")) {

                                        if (AccountNo.equalsIgnoreCase("")) {
                                            Toast.makeText(getApplicationContext(), "Please Enter Account No", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (AccountNo.length() <= 20)
                                            {
                                                acctivityGsonbody(dialog);

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Invalid Account Number", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                    } else {
                                        if (GcashNo.equalsIgnoreCase("")) {
                                            Toast.makeText(getApplicationContext(), "Please Enter GCash Number", Toast.LENGTH_SHORT).show();

                                        } else {
                                            if (GcashNo.length() <= 20)
                                            {
                                                acctivityGsonbody(dialog);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Invalid GCash Number", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            });

            holder.audlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    String calue  = ((CashOutFragmentNew)cashOutFragmentNew).checkValidation();
                    if (calue.equals("complete"))
                    {
                        String ddkaddress=   ((CashOutFragmentNew)cashOutFragmentNew).getValueData("address");
                        String ddkamount =   ((CashOutFragmentNew)cashOutFragmentNew).getValueData("ddk");
                        String phpamount =   ((CashOutFragmentNew)cashOutFragmentNew).getValueData("php");
                        String secrate= ((CashOutFragmentNew)cashOutFragmentNew).getValueData("secrate");
                        String conversionrate=((CashOutFragmentNew)cashOutFragmentNew).getValueData("conversionrate");
                        UserBankList userBankList = createCancellationRequestlist.get(position);
                        amountentervalue = ((CashOutFragmentNew)cashOutFragmentNew).amountValue();
                        String bankName = createCancellationRequestlist.get(position).getBank().getBankName();
                        String bankimage = createCancellationRequestlist.get(position).getBank().getImage();
                        String banktype = createCancellationRequestlist.get(position).getBankType();
                        int bankid = createCancellationRequestlist.get(position).getBankId();
                        String id=createCancellationRequestlist.get(position).getId().toString();
                        if (createCancellationRequestlist.get(position).getBank().getType().equals("bank")){
                            showDialogBankTrnsfer(id,conversionrate,secrate,phpamount,ddkamount,ddkaddress,v,activity,imagePath,userBankList,amountentervalue,cashOutFragmentNew);
                        }else {
                            showDialogGcash(id,bankid,conversionrate,secrate,phpamount,ddkamount,ddkaddress,v,activity,bankName,bankimage,banktype,cashOutFragmentNew,userBankList);
                        }
                    }

                }
            });

            holder.phlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    String calue  = ((CashOutFragmentNew)cashOutFragmentNew).checkValidation();
                    if (calue.equals("complete")){
                        String ddkaddress=   ((CashOutFragmentNew)cashOutFragmentNew).getValueData("address");
                        String ddkamount =   ((CashOutFragmentNew)cashOutFragmentNew).getValueData("ddk");
                        String phpamount =   ((CashOutFragmentNew)cashOutFragmentNew).getValueData("php");
                        String secrate= ((CashOutFragmentNew)cashOutFragmentNew).getValueData("secrate");
                        String conversionrate=((CashOutFragmentNew)cashOutFragmentNew).getValueData("conversionrate");
                        UserBankList userBankList = createCancellationRequestlist.get(position);
                        amountentervalue = ((CashOutFragmentNew)cashOutFragmentNew).amountValue();
                        String bankName = createCancellationRequestlist.get(position).getName();
                        String bankimage = createCancellationRequestlist.get(position).getBank().getImage();
                        String banktype = createCancellationRequestlist.get(position).getBankType();
                        int bankid = createCancellationRequestlist.get(position).getBankId();
                        String id=createCancellationRequestlist.get(position).getId().toString();
                        if (createCancellationRequestlist.get(position).getBank().getType().equals("bank")){
                            showDialogBankTrnsfer(id,conversionrate,secrate,phpamount,ddkamount,ddkaddress,v,activity,imagePath,userBankList,amountentervalue, cashOutFragmentNew);
                        }else {
                            showDialogGcash(id,bankid,conversionrate,secrate,phpamount,ddkamount,ddkaddress,v,activity,bankName,bankimage,banktype,cashOutFragmentNew,userBankList);
                        }
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
        return createCancellationRequestlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  headelogotext,tvBankNameDDk,audtvBankNameDDk;
        public ImageView audic_addicon,ic_addicon,ivBankIconDDK;
        LinearLayout phlayout,audlayout;
        RelativeLayout phpiconview;

        public MyViewHolder(View view) {
            super(view);
            headelogotext=view.findViewById(R.id.headelogotext);
            phpiconview=view.findViewById(R.id.phpiconview);
            phlayout=view.findViewById(R.id.phlayout);
            audlayout=view.findViewById(R.id.audlayout);
            ic_addicon=view.findViewById(R.id.ic_addicon);
            audic_addicon=view.findViewById(R.id.audic_addicon);
            ivBankIconDDK=view.findViewById(R.id.ivBankIconDDK);
            tvBankNameDDk=view.findViewById(R.id.tvBankNameDDk);
            audtvBankNameDDk=view.findViewById(R.id.audtvBankNameDDk);
            }
    }

    private void getBankList(String country_id, final View view) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("country_id", country_id);
        Log.d("param",hm.toString());
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        AppConfig.showLoading(dialog, "Get Bank List....");
        AppConfig.getLoadInterface().getTheBankList(AppConfig.getStringPreferences(activity, Constant.JWTToken), hm).enqueue(new Callback<BankList>() {
            @Override
            public void onResponse(Call<BankList> call, Response<BankList> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("response_banklist",response.toString());
                    if (response.body().status == 1) {
                        bankList.clear();
                        imagePath = response.body().image_path;
                        bankList.addAll(response.body().data);
                        getPopop(view,bankList);
                    } else if (response.body() != null && response.body().status == 3) {
                        AppConfig.showToast(response.body().msg);
                        AppConfig.openSplashActivity(activity);
                    } else {
                        AppConfig.showToast(response.body().msg);
                    }
                } else {
                    AppConfig.showToast("Server Error");
                }
            }

            @Override
            public void onFailure(Call<BankList> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void acctivityGsonbody(BottomSheetDialog dialog)
    {
        ConnectionDetector cd = new ConnectionDetector(activity);
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }else {
            try {
                HashMap<String, String> hm = new HashMap<>();
                 if (selectedBank.equals("bank"))
                 {
                     hm.put("name", et_bankUserName.getText().toString());
                     hm.put("account_no", et_AccountNo.getText().toString());
                     hm.put("bank_id", ""+selectedBankId);
                     hm.put("bank_type",selectedBank);
                }else {
                     hm.put("gcash_no", et_GCashNo.getText().toString());
                     hm.put("bank_id",""+selectedBankId);
                     hm.put("name", et_bankUserName.getText().toString());
                     hm.put("bank_type",selectedBank);
                }
                addBank(hm,dialog);

            } catch (Exception c) {
                c.printStackTrace();
            } }
    }

    public void addBank(HashMap<String, String> hm, final BottomSheetDialog bottomdialog) {
        final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
        Log.d("hm param",hm.toString());
        LoadInterface apiservice = AppConfig.getClient().create(LoadInterface.class);
        Call<CashOutAddBankResponse> call = apiservice.addBankAccount(AppConfig.getStringPreferences(activity, Constant.JWTToken),hm);
        AppConfig.showLoading(dialog, "Get Bank List....");
        call.enqueue(new Callback<CashOutAddBankResponse>() {
            @Override
            public void onResponse(Call<CashOutAddBankResponse> call, Response<CashOutAddBankResponse> response) {
                int code = response.code();
                String retrofitMesage = "";
                dialog.dismiss();
                if (code == 200) {
                    bottomdialog.dismiss();
                    int responseCode = response.body().getStatus();
                    retrofitMesage= response.body().msg;
                    if (responseCode == 1) {
                        Log.d("response_adbank",response.body().toString());
                        Toast.makeText(activity, retrofitMesage, Toast.LENGTH_SHORT).show();
                        ((CashOutFragmentNew)cashOutFragmentNew).getUserBankList();
                        return;
                    }
                    else
                    {
                        Toast.makeText(activity, retrofitMesage, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

            }
            @Override
            public void onFailure(Call<CashOutAddBankResponse> call, Throwable t) {
                Toast.makeText(activity, "Response getting failed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void showDialogCashoutBankTrnsfer(final View mview, final List<BankList.BankData> samkoinList, final TextView tvSelectDdkAddress, String imagePath) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View dialogView = layoutInflater.inflate(R.layout.popup_cashout_banktransfer, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.DialogStyle);
        dialog.setContentView(dialogView);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final AllTypeAvailableFragmentAdapter adapterCredential = new AllTypeAvailableFragmentAdapter(samkoinList, activity,imagePath,new AllTypeAvailableFragmentAdapter.SetOnItemClickListener() {
            @Override
            public void onItemClick(final String wallet_code,final String type,String id) {
                tvSelectDdkAddress.setText(wallet_code);
                selectedBankId = id;
                if (type.equals("bank"))
                {
                    btn_save.setVisibility(View.VISIBLE);
                    et_bankUserName.setVisibility(View.VISIBLE);
                    selectedBank = "bank";
                    et_AccountNo.setHint("Enter Account Number");
                    et_AccountNo.setVisibility(View.VISIBLE);
                    et_GCashNo.setVisibility(View.GONE);
                    String countrydata=userData.getUser().country.get(0).country;
                    Log.d("country",countrydata);
                    if(countrydata!=null && countrydata.equalsIgnoreCase("Philippines")){
                        et_bankUserName.setHint("Enter Account Name");
                    }else {
                        et_bankUserName.setHint("Enter BSB Name");
                    }

                }else {
                    btn_save.setVisibility(View.VISIBLE);
                    selectedBank = "gcash";
                    et_AccountNo.setVisibility(View.GONE);
                    et_GCashNo.setVisibility(View.VISIBLE);
                    et_GCashNo.setHint("Enter GCash Number");
                    et_bankUserName.setVisibility(View.VISIBLE);
                    et_bankUserName.setHint("Enter GCash Name");
                }

                //...........
                dialog.dismiss();
            }
        });
        recyclerView.setAdapter(adapterCredential);
        dialog.show();
    }


    private void getPopop(View view,List<BankList.BankData> bankList) {
        showDialogCashoutBankTrnsfer(view,bankList,ll_SelectBankName,imagePath);

    }

}
