package com.ddkcommunity.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.fragment.buy.PaymentFragment;
import com.ddkcommunity.model.conatcModel;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.dataPutMethods;
import com.ncorti.slidetoact.SlideToActView;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowSameWalletDialog;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;
import static com.ddkcommunity.utilies.dataPutMethods.showDialogCryptoData;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragemnt extends Fragment
{
    private Context mContext;
    ImageView billingview,billingviewimg;
    View view;
    AppCompatButton submit;
    private String imagePathSecondFront = "";
    Bitmap frontimgsecondbitmap;
    EditText subject_ET,description_ET;

    public ContactUsFragemnt() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.contactfragment, container, false);
        mContext = getActivity();
        billingviewimg=view.findViewById(R.id.billingviewimg);
        submit=view.findViewById(R.id.submit);
        subject_ET=view.findViewById(R.id.subject_ET);
        description_ET=view.findViewById(R.id.description_ET);
        billingview=view.findViewById(R.id.billingview);
        billingview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PickImageDialog dialognew = PickImageDialog.build(new PickSetup());
                dialognew.setOnPickCancel(new IPickCancel()
                {
                    @Override
                    public void onCancelClick() {
                        dialognew.dismiss();
                    }
                }).setOnPickResult(new IPickResult()
                {
                    @Override
                    public void onPickResult(PickResult r)
                    {
                        //TODO: do what you have to...
                        imagePathSecondFront = r.getPath();
                        frontimgsecondbitmap=r.getBitmap();
                        billingviewimg.setImageBitmap(r.getBitmap());
                        Toast.makeText(mContext, "Image attached successfully", Toast.LENGTH_SHORT).show();
                        //changeImageCall(DataHolder.getUser().getId(),imagePath);
                    }
                }).show(getActivity().getSupportFragmentManager());

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String messagevalue=subject_ET.getText().toString();
                String description=description_ET.getText().toString();
               if(messagevalue.equalsIgnoreCase(""))
               {
                   Toast.makeText(mContext, "Please enter subject", Toast.LENGTH_SHORT).show();
               }else if(description.equalsIgnoreCase(""))
               {
                   Toast.makeText(mContext, "Please enter description", Toast.LENGTH_SHORT).show();
               }else {
                   submitBillingView(messagevalue, description);
               }
            }
        });
        //for new
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitle("Contact Us");
        MainActivity.enableBackViews(true);
    }

    private void submitBillingView(String messagevalue,String description) {

        if (AppConfig.isInternetOn())
        {
            final ProgressDialog dialognew = new ProgressDialog(getActivity());
            AppConfig.showLoading(dialognew, "Please wait verifying....");
            File filefirstfinal = null;
            try {
                if (imagePathSecondFront != null) {
                    filefirstfinal = new File(imagePathSecondFront);
                }
                try {
                    //for first
                    File userimg=new File(imagePathSecondFront);
                    FileOutputStream out = new FileOutputStream(userimg);
                    frontimgsecondbitmap.compress(Bitmap.CompressFormat.PNG, 100, out); //100-best quality
                    out.close();
                    filefirstfinal = new File(imagePathSecondFront);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Call<conatcModel> call;
                if(imagePathSecondFront==null || imagePathSecondFront.equals(""))
                {
                    RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), filefirstfinal);
                    MultipartBody.Part body1 = MultipartBody.Part.createFormData("attachment", filefirstfinal.getName(), requestFile1);
                    call = AppConfig.getLoadInterface().sendContactUswithout(
                            AppConfig.getStringPreferences(mContext, Constant.JWTToken),
                            AppConfig.setRequestBody(messagevalue),
                            AppConfig.setRequestBody(description)
                    );
                }else
                {
                    RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), filefirstfinal);
                    MultipartBody.Part body1 = MultipartBody.Part.createFormData("attachment", filefirstfinal.getName(), requestFile1);
                    call = AppConfig.getLoadInterface().sendContactUs(
                            AppConfig.getStringPreferences(mContext, Constant.JWTToken),
                            AppConfig.setRequestBody(messagevalue),
                            AppConfig.setRequestBody(description),
                            body1
                    );
                }
                 call.enqueue(new Callback<conatcModel>() {
                    @Override
                    public void onResponse(Call<conatcModel> call, Response<conatcModel> response) {
                        dialognew.dismiss();
                        try {
                            if (response.isSuccessful() && response.body() != null)
                            {
                                String responseData = response.body().toString();
                                Log.d("status", responseData);
                                dialognew.dismiss();
                                if(response.body().getStatus()==1)
                                {
                                    Toast.makeText(getContext(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    getActivity().onBackPressed();
                                }else
                                {
                                    Toast.makeText(getContext(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                AppConfig.showToast("Server error");
                                Toast.makeText(getActivity(), ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            AppConfig.showToast("Server json error");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<conatcModel> call, Throwable t) {
                        AppConfig.hideLoading(dialognew);
                        Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            }catch (Exception e)
            {
                e.printStackTrace();
                //  Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
            }
        } else {
            AppConfig.showToast("No internet.");
        }
    }

}
