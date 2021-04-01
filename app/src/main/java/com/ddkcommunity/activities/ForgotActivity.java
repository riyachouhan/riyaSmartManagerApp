package com.ddkcommunity.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ddkcommunity.R;
import com.ddkcommunity.Constant;
import com.ddkcommunity.utilies.AppConfig;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

public class ForgotActivity extends AppCompatActivity {

    EditText emailET;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        context = this;
        emailET = findViewById(R.id.email_ET);
        emailET.requestFocus();


        findViewById(R.id.submit_BT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();

                if (!AppConfig.isStringNullOrBlank(email)) {
                    if (AppConfig.isEmail(email)) {
                        getProfileCall(email);
                    }else {
                        emailET.setError("Please enter valid Email.");
                        AppConfig.showToast("Please enter valid Email.");
                    }
                } else {
                    emailET.setError("Please enter Email.");
                    AppConfig.showToast("Please enter Email.");
                }
            }
        });

    }

    private void getProfileCall(String id) {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(context);

            AppConfig.showLoading(dialog, "Fetching User Details..");
            Call<ResponseBody> call = AppConfig.getLoadInterface().forgotCall(AppConfig.setRequestBody(id));

            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getString(Constant.STATUS).equals("1")) {
                                emailET.setText("");
                                AppConfig.showToast(object.getString("msg"));
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
                        ShowApiError(ForgotActivity.this,"Server error in forgot-password");
                    }
                    AppConfig.hideLoading(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppConfig.hideLoading(dialog);
                    errordurigApiCalling(ForgotActivity.this,t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}