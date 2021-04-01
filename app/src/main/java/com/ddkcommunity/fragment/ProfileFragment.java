package com.ddkcommunity.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.CustomPinActivity;
import com.ddkcommunity.activities.LoginActivity;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.activities.SplashActivity;
import com.ddkcommunity.adapters.UserAdapter;
import com.ddkcommunity.fragment.send.SendDDkFragment;
import com.ddkcommunity.interfaces.GegtSettingStatusinterface;
import com.ddkcommunity.model.AllowGoogleModel;
import com.ddkcommunity.model.Country;
import com.ddkcommunity.model.CountryResponse;
import com.ddkcommunity.model.LoggedUser;
import com.ddkcommunity.model.ReferralPayoutModel;
import com.ddkcommunity.model.getSettingModel;
import com.ddkcommunity.model.samModel;
import com.ddkcommunity.model.user.User;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.dataPutMethods;
import com.facebook.login.LoginManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.suke.widget.SwitchButton;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

import static android.app.Activity.RESULT_OK;
import static com.ddkcommunity.fragment.HomeFragment.profileupdatesta;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.ShowServerPost;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;
import static com.ddkcommunity.utilies.dataPutMethods.getSettingServerDataSt;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static EditText ddkCodeET, nameET, emailform_ET,emailET, oldPassET, newPassET, confirmPassET, mobileET, designationET;
    Bitmap userbitmap;
    EditText alt_contactnumber_ET,alt_email_ET,alt_name_ET;
    LinearLayout dateorbirthview,gendermainview;
    EditText dateofbirth_ET;
    private static TextView countryET, phoneCodeET, addAnotherAccountTV, image_view_TV;
    private static ImageView imageView, ddkCodeIV,image_view;
    private static String imagePath = "";
    public static String country;
    public static String mCountryId = "";
    private static UserAdapter mAdapter;
    private static List<Country> countryList = new ArrayList<>();
    private RecyclerView rvUserList;
    private Context mContext;
    private User userInfo;
    private LinearLayout lytUserInfo, lytChangePassword;
    private AppCompatButton btnChangePass;
    SwitchButton switch_button;
    int year;
    int month;
    int day;
    String userage="";
    EditText gender_value_ET,zip_ET,province_ET,city_ET,address_ET;
    RadioButton radioother,radiofemale,radiomale;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    String gendervalue="";
    RadioButton rmale,rfemale,rtoher;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public void getProfileCall(String token) {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);
            AppConfig.showLoading(dialog, "Fetching User Details..");
            HashMap<String, String> hm = new HashMap<>();
            hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
            hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
            Call<ResponseBody> call = AppConfig.getLoadInterface().getProfileCall(token,hm);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {
                                UserResponse userResponse = new Gson().fromJson(responseData, UserResponse.class);
                                AppConfig.setUserData(mContext, userResponse);
                                userInfo = userResponse.getUser();
                                ddkCodeET.setText(userResponse.getUser().unique_code);
                                MainActivity.setUserDetail(userResponse.getUser());
                                App.editor.putString(Constant.USER_NAME, userResponse.getUser().getName());
                                App.editor.putString(Constant.USER_EMAIL, userResponse.getUser().getEmail());
                                if (userResponse.getUser().getMobile() != null) {
                                    App.editor.putString(Constant.USER_MOBILE, userResponse.getUser().getMobile());
                                }
                                App.editor.apply();
                                dateofbirth_ET.setText(userResponse.getUser().getDob());
                                nameET.setText(userResponse.getUser().getName());
                                emailET.setText(userResponse.getUser().getEmail());
                                emailform_ET.setText(userResponse.getUser().getEmail());
                                city_ET.setText(userResponse.getUser().getCity());
                                province_ET.setText(userResponse.getUser().getProvince());
                                zip_ET.setText(userResponse.getUser().getZip());
                                address_ET.setText(userResponse.getUser().getAddress());
                                String gendervaluename=userResponse.getUser().getGender_name();
                                String genderval=userResponse.getUser().getGender();
                                //..........
                                if(genderval!=null) {
                                    if (genderval.equalsIgnoreCase("female")) {
                                        rfemale.setChecked(true);
                                        rmale.setChecked(false);
                                        rtoher.setChecked(false);
                                        gendervalue = "female";
                                        gendermainview.setVisibility(View.GONE);
                                        gender_value_ET.setText("");
                                    } else if (genderval.equalsIgnoreCase("male")) {
                                        rfemale.setChecked(false);
                                        rmale.setChecked(true);
                                        rtoher.setChecked(false);
                                        gendervalue = "male";
                                        gendermainview.setVisibility(View.GONE);
                                        gender_value_ET.setText("");
                                    } else {
                                        rfemale.setChecked(false);
                                        rmale.setChecked(false);
                                        rtoher.setChecked(true);
                                        gender_value_ET.setText("");
                                        gendervalue = "other";
                                        gender_value_ET.setText(gendervaluename);
                                        gendermainview.setVisibility(View.VISIBLE);
                                    }
                                }
                                if (userResponse.getUser().getMobile() != null) {
                                    mobileET.setText(userResponse.getUser().getMobile());
                                } else {
                                    mobileET.setText("");
                                }
                                oldPassET.setText("");
                                newPassET.setText("");
                                confirmPassET.setText("");
                                for (Country country : countryList) {
                                    if (country.getId().equals(userInfo.getCountryId())) {
                                        countryET.setText(country.getPhoneCode());
                                        phoneCodeET.setText(country.getCountry());
                                    }
                                }

                                if(userResponse.getUser().getUserAlternateInfo()!=null)
                                {
                                    String altname=userResponse.getUser().getUserAlternateInfo().getAltName();
                                    String altemail=userResponse.getUser().getUserAlternateInfo().getAltEmail();
                                    String altcontact=userResponse.getUser().getUserAlternateInfo().getAltContactNumber();
                                    alt_contactnumber_ET.setText(altcontact);
                                    alt_email_ET.setText(altemail);
                                    alt_name_ET.setText(altname);
                                }
                                designationET.setText(userInfo.getDesignation());
                                String imglo=userInfo.getUserImage();
                                // Toast.makeText(getActivity(), ""+imglo, Toast.LENGTH_SHORT).show();
                                Glide.with(MainActivity.activity)
                                        .asBitmap()
                                        .load(userInfo.getUserImage())
                                        .placeholder(ContextCompat.getDrawable(mContext, R.drawable.defalut_profile))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                                imageView.setImageBitmap(resource);
                                                image_view_TV.setText("Remove Photo");
                                                image_view_TV.setEnabled(true);
                                                image_view_TV.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        removePhotoCall(App.pref.getString(Constant.USER_ID, ""));
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                                super.onLoadFailed(errorDrawable);
                                                image_view_TV.setText("Upload Photo");
                                                image_view_TV.setEnabled(false);
                                                imageView.setImageResource(R.drawable.defalut_profile);
                                            }
                                        });
                                imagePath = "";
//                                DatabaseHelper db = new DatabaseHelper();
//                                db.updateUsers(DataHolder.getUser());
//                                mAdapter.updateData(db.getAllUser());

                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity(getActivity());
                            } else {
                                AppConfig.showToast(object.getString("msg"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ShowApiError(getActivity(),"server error in eightface/user-profile");
                    }
                    AppConfig.hideLoading(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppConfig.hideLoading(dialog);
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    private void removePhotoCall(final String id) {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(MainActivity.activity);

            AppConfig.showLoading(dialog, "Removing Photo...");
            Call<ResponseBody> call = AppConfig.getLoadInterface().removePhotoCall(AppConfig.getStringPreferences(mContext, Constant.JWTToken));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {

                                getProfileCall(AppConfig.getStringPreferences(mContext, Constant.JWTToken));
                                AppConfig.showToast(object.getString("msg"));
                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity(getActivity());
                            } else {
                                AppConfig.showToast(object.getString("msg"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ShowApiError(getActivity(),"server error in delete-profile-image");
                    }
                    AppConfig.hideLoading(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppConfig.hideLoading(dialog);
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getActivity();
        alt_contactnumber_ET=view.findViewById(R.id.alt_contactnumber_ET);
        alt_email_ET=view.findViewById(R.id.alt_email_ET);
        alt_name_ET=view.findViewById(R.id.alt_name_ET);
        gender_value_ET=view.findViewById(R.id.gender_value_ET);
        zip_ET=view.findViewById(R.id.zip_ET);
        radioGroup=view.findViewById(R.id.radio);
        rmale =view.findViewById(R.id.radiomale);
        rfemale =view.findViewById(R.id.radiofemale);
        rtoher =view.findViewById(R.id.radioother);
        province_ET=view.findViewById(R.id.province_ET);
        city_ET=view.findViewById(R.id.city_ET);
        address_ET=view.findViewById(R.id.address_ET);
        emailform_ET=view.findViewById(R.id.emailform_ET);
        gendermainview=view.findViewById(R.id.gendermainview);
        dateorbirthview=view.findViewById(R.id.dateorbirthview);
        dateofbirth_ET=view.findViewById(R.id.dateofbirth_ET);
        switch_button=view.findViewById(R.id.switch_button);
        ddkCodeET = view.findViewById(R.id.ddk_ET);
        ddkCodeIV = view.findViewById(R.id.share_IV);
        image_view=view.findViewById(R.id.image_view);
        nameET = view.findViewById(R.id.name_ET);
        emailET = view.findViewById(R.id.email_ET);
        oldPassET = view.findViewById(R.id.old_password_ET);
        newPassET = view.findViewById(R.id.new_password_ET);
        confirmPassET = view.findViewById(R.id.confirm_password_ET);
        mobileET = view.findViewById(R.id.mobile_ET);
        image_view_TV = view.findViewById(R.id.image_view_TV);
        countryET = view.findViewById(R.id.country_ET);
        phoneCodeET = view.findViewById(R.id.phone_code_ET);
        designationET = view.findViewById(R.id.designation_ET);
        imageView = view.findViewById(R.id.image_view);
        addAnotherAccountTV = view.findViewById(R.id.add_another_account_TV);
        rvUserList = view.findViewById(R.id.recycler_view);

        btnChangePass = view.findViewById(R.id.btnChangePass);
        lytUserInfo = view.findViewById(R.id.lytUserInfo);
        lytChangePassword = view.findViewById(R.id.lytChangePassword);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytChangePassword.setVisibility(View.VISIBLE);
                lytUserInfo.setVisibility(View.GONE);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // checkedId is the RadioButton selected
                int selectedIdradio = group.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButton = (RadioButton) view.findViewById(selectedIdradio);
                final String useroption=radioButton.getText().toString();
                if(useroption.equalsIgnoreCase("Female"))
                {
                    gendervalue="female";
                    gendermainview.setVisibility(View.GONE);
                    gender_value_ET.setText("");
                }else if(useroption.equalsIgnoreCase("Male"))
                {
                    gendervalue="male";
                    gendermainview.setVisibility(View.GONE);
                    gender_value_ET.setText("");
                }else
                {
                    gender_value_ET.setText("");
                    gendervalue="other";
                    gendermainview.setVisibility(View.VISIBLE);
                }
                //.......
            }
        });

        addAnotherAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // MainActivity.addFragment(new LoginFragment(),true);
                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));

            }
        });


//        DatabaseHelper db = new DatabaseHelper();

        if (!AppConfig.isStringNullOrBlank(App.pref.getString(Constant.WALLET_ID, "")))
            ddkCodeIV.setVisibility(View.VISIBLE);

        getCountryCall();

        view.findViewById(R.id.image_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v)
            {
                final PickImageDialog dialog = PickImageDialog.build(new PickSetup());
                dialog.setOnPickCancel(new IPickCancel() {
                    @Override
                    public void onCancelClick() {
                        dialog.dismiss();
                    }
                }).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        //TODO: do what you have to...
                        imagePath = r.getPath();
                        userbitmap=r.getBitmap();
                        ((ImageView) view.findViewById(R.id.image_view)).setImageBitmap(r.getBitmap());
                    }
                }).show(getActivity().getSupportFragmentManager());

            }
        });

        ddkCodeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String tempStr = "Hi, Here is my referral code \n" +
                        "\n" +
                        "" + userInfo.unique_code + "\n" +
                        "\n" +
                        "Please download the Smart Asset Managers App and earn free money.\n" +
                        "\n" +
                        "https://apps.apple.com/us/app/smart-asset-managers/id1488426620?ls=1\n" +
                        "\n" +
                        "https://play.google.com/store/apps/details?id=com.ddkcommunity\n" +
                        "\n";
                AppConfig.copyShare("Share Your Referral Code", tempStr, "Share Referral Code", getActivity());
            }
        });
        view.findViewById(R.id.business_card_IV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AppConfig.copyShare("", userInfo.getCardLink(), "Share Digital Business Card", getActivity());
            }
        });


        view.findViewById(R.id.logout_BT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                alertDialogBuilder.setTitle("Logout").setMessage("Are you sure want to Logout?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                LoginManager.getInstance().logOut();
                                AppConfig.openSplashActivity(getActivity());
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
            }
        });

        view.findViewById(R.id.submit_BT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String countryname=phoneCodeET.getText().toString();
                for (Country country : countryList) {
                    if (country.getPhoneCode().equals(countryET.getText().toString()) && country.getCountry().equalsIgnoreCase(countryname))
                    {
                        mCountryId = country.getId();
                    }
                }
//                if (!AppConfig.isStringNullOrBlank(mobileET.getText().toString()) &&
//                        mobileET.getText().toString().length() >= 9
//                        && mobileET.getText().toString().length() <= 15) {
                String othervalue="";
                if(gendervalue.equalsIgnoreCase("female"))
                {
                    gendervalue="female";
                }else if(gendervalue.equalsIgnoreCase("male"))
                {
                    gendervalue="male";
                }else
                {
                    gendervalue="other";
                    othervalue=gender_value_ET.getText().toString();
                }

                String altemaileditq=alt_email_ET.getText().toString();
                if(!altemaileditq.equalsIgnoreCase(""))
                {
                    if(!AppConfig.isEmail(altemaileditq))
                    {
                        Toast.makeText(getActivity(), "Emergency Email is invalid ", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        checkStatus(othervalue);
                    }
                }else {
                    checkStatus(othervalue);
                }

            }
        });


        view.findViewById(R.id.btnSavePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = oldPassET.getText().toString();
                String newPass = newPassET.getText().toString();
                String confirm = confirmPassET.getText().toString();


                if (!AppConfig.isStringNullOrBlank(oldPass) && oldPass.length() >= 6) {
                    if (!AppConfig.isStringNullOrBlank(newPass) && newPass.length() >= 6) {
                        if (!oldPass.equals(newPass)) {
                            if (!AppConfig.isStringNullOrBlank(confirm)) {
                                if (confirm.equals(newPass)) {
                                    changePassword(oldPass, newPass, App.pref.getString(Constant.USER_ID, ""));
                                } else {
                                    AppConfig.showToast("Password not matched");
                                }
                            } else {
                                confirmPassET.setError("Confirm Password Required.");
                                AppConfig.showToast("Confirm Password Required.");
                            }
                        } else {
                            newPassET.setError("New Password must be different.");
                            AppConfig.showToast("New Password must be different.");
                        }
                    } else {
                        newPassET.setError("Password Required or greater than 6 character.");
                        AppConfig.showToast("Password Required or greater than 6 character.");
                    }
                } else {
                    oldPassET.setError("Password Required or greater than 6 character.");
                    AppConfig.showToast("Password Required or greater than 6 character.");
                }
            }
        });

        getLoggedUser();
        String googlevaue=App.pref.getString(Constant.GOOGLEAUTHOPTIONSTATUS, "");
        if(googlevaue.equalsIgnoreCase("1"))
        {
            switch_button.setChecked(true);

        }else
        {
            switch_button.setChecked(false);
        }

        switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if(isChecked)
                {
                    sendAuthStatusData("1");
                }else
                {
                    sendAuthStatusData("0");
                }
            }
        });

        dateorbirthview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDate();
            }
        });

        dateofbirth_ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDate();
            }
        });
        return view;
    }

    public void checkStatus(String othervalue)
    {
        profileupdatesta=1;
        if (imagePath.equals("")) {
            String altcontactedit = alt_contactnumber_ET.getText().toString();
            String altemailedit = alt_email_ET.getText().toString();
            String altnameedit = alt_name_ET.getText().toString();
            String addresvalue = address_ET.getText().toString();
            String zipvalue = zip_ET.getText().toString();
            String province = province_ET.getText().toString();
            String city = city_ET.getText().toString();
            String dob = dateofbirth_ET.getText().toString();
            updateProfileCall(altnameedit, altemailedit, altcontactedit, addresvalue, othervalue, gendervalue, zipvalue, province, city, dob, App.pref.getString(Constant.USER_ID, ""), ddkCodeET.getText().toString(),
                    nameET.getText().toString(), mobileET.getText().toString(), mCountryId, designationET.getText().toString());
        } else {
            String altcontactedit = alt_contactnumber_ET.getText().toString();
            String altemailedit = alt_email_ET.getText().toString();
            String altnameedit = alt_name_ET.getText().toString();
            String addresvalue = address_ET.getText().toString();
            String zipvalue = zip_ET.getText().toString();
            String province = province_ET.getText().toString();
            String city = city_ET.getText().toString();
            String dob = dateofbirth_ET.getText().toString();
            updateProfileCall(altnameedit, altemailedit, altcontactedit, addresvalue, othervalue, gendervalue, zipvalue, province, city, dob, App.pref.getString(Constant.USER_ID, ""), ddkCodeET.getText().toString(),
                    nameET.getText().toString(), mobileET.getText().toString(), mCountryId, designationET.getText().toString(), imagePath);
        }
    }

    private void SelectDate() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog = new DatePickerDialog(getContext(), datePickerListener, year, month, day);
        dateDialog.getDatePicker().setMaxDate(new Date().getTime());
        dateDialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            userage=getAge(year,month,day);
             // Toast.makeText(getActivity(), "your age "+userage, Toast.LENGTH_SHORT).show();
            // Show selected date
            dateofbirth_ET.setText(new StringBuilder().append(day)
                    .append("-").append(month+1).append("-").append(year)
                    .append(" "));

        }
    };

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    private void sendAuthStatusData(final String authUpdate)
    {
        final ProgressDialog pd= new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("auth_status", authUpdate);
        AppConfig.getLoadInterface().sendUpdateAuth(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm).enqueue(new Callback<AllowGoogleModel>() {
            @Override
            public void onResponse(Call<AllowGoogleModel> call, Response<AllowGoogleModel> response) {
                pd.dismiss();
                Log.d("auth user",response.body().toString());
                if(response.isSuccessful())
                {
                    Log.d("auth user response",response.body().toString());
                    if (response.body().getStatus() == 1)
                    {
                        //for data
                        App.editor.putString(Constant.GOOGLEAUTHOPTIONSTATUS,""+authUpdate);
                        App.editor.apply();
                        //.......
                        String googlevaue=App.pref.getString(Constant.GOOGLEAUTHSECRATE, "");
                        String googlestatus=App.pref.getString(Constant.GOOGLEAUThPendingRegit, "");
                        if(googlevaue==null || googlevaue.equalsIgnoreCase(null) || googlevaue.equalsIgnoreCase("")|| googlevaue.equalsIgnoreCase(" "))
                        {
                            Fragment fragment = new GogoleAuthFragment();
                            Bundle arg = new Bundle();
                            fragment.setArguments(arg);
                            MainActivity.addFragment(fragment, true);
                        }else
                        if(googlestatus.equalsIgnoreCase("pending"))
                        {
                            Fragment fragment = new GogoleAuthFragment();
                            Bundle arg = new Bundle();
                            fragment.setArguments(arg);
                            MainActivity.addFragment(fragment, true);
                        }

                    }else if (response.body().getStatus() == 4)
                    {
                        ShowServerPost((Activity)mContext,"ddk server error convert usdt ");
                    }else
                    {
                        AppConfig.showToast(response.body().getMsg());
                    }
                } else
                {
                    ShowApiError(mContext,"server error all_transactions/update-user-auth-status");
                }
            }

            @Override
            public void onFailure(Call<AllowGoogleModel> call, Throwable t) {
                pd.dismiss();
                errordurigApiCalling(getActivity(),t.getMessage());

            }
        });
    }

    private void getLoggedUser() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("device_id", getDeviceID());
        AppConfig.getLoadInterface().getLoggedUser(AppConfig.getStringPreferences(getActivity(), Constant.JWTToken), hm).enqueue(new Callback<LoggedUser>() {
            @Override
            public void onResponse(Call<LoggedUser> call, Response<LoggedUser> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().status == 1) {
                        mAdapter = new UserAdapter(response.body().user, getActivity(), response.body().profilePath, getDeviceID());
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        rvUserList.setLayoutManager(mLayoutManager);
                        rvUserList.setItemAnimator(new DefaultItemAnimator());
                        rvUserList.setNestedScrollingEnabled(false);
                        rvUserList.setAdapter(mAdapter);
                    } else if (response.body() != null && response.body().status == 3) {
                        AppConfig.showToast(response.body().msg);
                        AppConfig.openSplashActivity(getActivity());
                    } else if (response.body() != null && response.body().status == 0) {
                        AppConfig.showToast(response.body().msg);
                    } else {
                     ShowApiError(getContext(),"server error in get-user-logged-in-device");
                    }
                } else {
                    ShowApiError(getContext(),"server error in get-user-logged-in-device");
                }

            }

            @Override
            public void onFailure(Call<LoggedUser> call, Throwable t) {

            }
        });
    }

    private void updateProfileCall(final String altnameedit,final String altemailedit,final String altcontactedit,final String address,final String genrevlalue,final String gender,final String zipvalue,final String province,final String city,final String dob,final String userId, String ddkCode, String name, String mobile,
                                   String countryId, String designation, String imagePath) {

       if (AppConfig.isInternetOn())
        {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            AppConfig.showLoading(dialog, "Updating...");
            File file = null;
            try {
                if (imagePath != null) {
                    file = new File(imagePath);
                }

                try {
                    File userimg=new File(imagePath);
                    FileOutputStream out = new FileOutputStream(userimg);
                    userbitmap.compress(Bitmap.CompressFormat.PNG, 100, out); //100-best quality
                    out.close();
                    file = new File(imagePath);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("user_image", file.getName(), requestFile);

                Call<ResponseBody> call = AppConfig.getLoadInterface().updateProfileCall(
                        AppConfig.setRequestBody(altnameedit),
                        AppConfig.setRequestBody(altemailedit),
                        AppConfig.setRequestBody(altcontactedit),
                        AppConfig.setRequestBody(address),
                        AppConfig.setRequestBody(genrevlalue),
                        AppConfig.setRequestBody(gender),
                        AppConfig.setRequestBody(zipvalue),
                        AppConfig.setRequestBody(province),
                        AppConfig.setRequestBody(city),
                        AppConfig.setRequestBody(dob),
                        AppConfig.getStringPreferences(mContext, Constant.JWTToken),
                        AppConfig.setRequestBody(name),
                        AppConfig.setRequestBody(mobile),
                        AppConfig.setRequestBody(countryId),
                        AppConfig.setRequestBody(ddkCode),
                        AppConfig.setRequestBody(countryET.getText().toString()),
                        AppConfig.setRequestBody(designation),
                        body
                );

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            try {
                                String responseData = response.body().string();
                                JSONObject object = new JSONObject(responseData);
                                if (object.getInt(Constant.STATUS) == 1) {
                                    AppConfig.showToast("Updated successful.");
                                    UserResponse registerResponse = new Gson().fromJson(responseData, UserResponse.class);
                                    getSettingServerDataSt(getActivity(),"php");
                                    getProfileCall(AppConfig.getStringPreferences(mContext, Constant.JWTToken));
                                } else if (object.getInt(Constant.STATUS) == 3) {
                                    AppConfig.showToast(object.getString("msg"));
                                    AppConfig.openSplashActivity(getActivity());
                                } else {
                                    AppConfig.showToast(object.getString("msg"));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ShowApiError(getActivity(),"server error in edit-user-profile");
                        }
                        AppConfig.hideLoading(dialog);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        AppConfig.hideLoading(dialog);
                        errordurigApiCalling(getActivity(),t.getMessage());
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

    private void changePassword(String oldPass, String newPass, final String id) {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());

            AppConfig.showLoading(dialog, "Changing Password..");
            Call<ResponseBody> call = AppConfig.getLoadInterface().changePasswordCall(
                    AppConfig.getStringPreferences(mContext, Constant.JWTToken), AppConfig.setRequestBody(oldPass),
                    AppConfig.setRequestBody(newPass));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {
                                AppConfig.showToast(object.getString("msg"));
                                getProfileCall(AppConfig.getStringPreferences(mContext, Constant.JWTToken));
                                lytChangePassword.setVisibility(View.GONE);
                                lytUserInfo.setVisibility(View.VISIBLE);
                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity(getActivity());
                            } else {
                                AppConfig.showToast(object.getString("msg"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ShowApiError(getActivity(),"server error change-password");
                    }
                    AppConfig.hideLoading(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppConfig.hideLoading(dialog);
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    private void getCountryCall() {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            AppConfig.showLoading(dialog, "Getting Countries...");

            Call<ResponseBody> call = AppConfig.getLoadInterface().countryList();

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            Log.d("countrydata",object.toString());
                            if (object.getInt(Constant.STATUS) == 1) {

                                CountryResponse registerResponse = new Gson().fromJson(responseData, CountryResponse.class);
                                countryList = registerResponse.getData();

                                final ArrayList<Country> countrygender=new ArrayList<>();
                                for(int i=0;i<countryList.size();i++)
                                {
                                    countrygender.add(countryList.get(i));
                                }

                                final String[] gender = new String[countryList.size()];
                                final String[] stateId = new String[countryList.size()];
                                final String[] countryCode = new String[countryList.size()];

                                for (int i = 0; i < countryList.size(); i++) {
                                    gender[i] = countryList.get(i).getCountry();
                                    stateId[i] = countryList.get(i).getId();
                                    countryCode[i] = countryList.get(i).getPhoneCode();
                                }
                                getProfileCall(AppConfig.getStringPreferences(mContext, Constant.JWTToken));

                                phoneCodeET.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        dataPutMethods.showDialogForSearchCountry(view,getContext(),countrygender,gender,stateId,countryCode,countryET,phoneCodeET);
                                       }
                                });
                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity(getActivity());
                            } else {
                                AppConfig.showToast(object.getString("msg"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ShowApiError(getActivity(),"server error in country-list");
                    }
                    AppConfig.hideLoading(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppConfig.hideLoading(dialog);
                    t.printStackTrace();
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    private void updateProfileCall(final String altnameedit,final String altemailedit,final String altcontactedit,final String address,final String genrevlalue,final String gender,final String zipvalue,final String province,final String city,final String dob,final String userId, String ddkCode, String name, String mobile, String countryId, String designation) {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            AppConfig.showLoading(dialog, "Updating...");

            Call<ResponseBody> call = AppConfig.getLoadInterface().updateProfileCall(
                    AppConfig.setRequestBody(altnameedit),
                    AppConfig.setRequestBody(altemailedit),
                    AppConfig.setRequestBody(altcontactedit),
                    AppConfig.setRequestBody(address),
                    AppConfig.setRequestBody(genrevlalue),
                    AppConfig.setRequestBody(gender),
                    AppConfig.setRequestBody(zipvalue),
                    AppConfig.setRequestBody(province),
                    AppConfig.setRequestBody(city),
                    AppConfig.setRequestBody(dob),
                    AppConfig.getStringPreferences(mContext, Constant.JWTToken),
                    AppConfig.setRequestBody(name),
                    AppConfig.setRequestBody(mobile),
                    AppConfig.setRequestBody(countryId),
                    AppConfig.setRequestBody(ddkCode),
                    AppConfig.setRequestBody(countryET.getText().toString()),
                    AppConfig.setRequestBody(designation)
            );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getString(Constant.STATUS).equals("1")) {
                                AppConfig.showToast("Updated successful.");
                                UserResponse registerResponse = new Gson().fromJson(responseData, UserResponse.class);
                                getProfileCall(AppConfig.getStringPreferences(mContext, Constant.JWTToken));
                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity(getActivity());
                            } else {
                                AppConfig.showToast(object.getString("msg"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ShowApiError(getActivity(),"server error in edit-user-profile");
                    }
                    AppConfig.hideLoading(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppConfig.hideLoading(dialog);
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    public String getDeviceID() {
        String deviceId = "";
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            AppConfig.showToast("Allow to read phone state permission.");
        } else {
            TelephonyManager mTelephony = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            try {
                if (mTelephony != null && mTelephony.getDeviceId() != null) {
                    deviceId = mTelephony.getDeviceId();
                } else {
                    deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            } catch (Exception e) {
                deviceId = AppConfig.getUUIDDeviceID(mContext);
            }
            if (deviceId == null || deviceId.isEmpty()) {
                deviceId = AppConfig.getUUIDDeviceID(mContext);
            }
        }
        return deviceId;
    }

    @Override
    public void onResume() {
        super.onResume();
        // HomeActivity.setHomeItem(getActivity(), R.id.profile);
        MainActivity.setTitle("Profile");
        MainActivity.enableBackViews(true);
    }

}
