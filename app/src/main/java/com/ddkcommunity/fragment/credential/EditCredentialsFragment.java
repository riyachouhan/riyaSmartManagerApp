package com.ddkcommunity.fragment.credential;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.MainActivity;
import com.ddkcommunity.activities.SplashActivity;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.utilies.AppConfig;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditCredentialsFragment extends Fragment {
    EditText nameET, ddkET, passPhaseET, passPhase2ET, referalET, notesET;
    RadioGroup radioGroup;
    private Credential credential1;

    public EditCredentialsFragment(Credential credential) {
        this.credential1 = credential;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_create_credentials, container, false);

        nameET = view.findViewById(R.id.name_ET);
        ddkET = view.findViewById(R.id.ddk_ET);
        passPhaseET = view.findViewById(R.id.pass_phase_ET);
        passPhase2ET = view.findViewById(R.id.pass_phase2_ET);
        referalET = view.findViewById(R.id.referal_Link_ET);
        notesET = view.findViewById(R.id.notes_ET);
        radioGroup = view.findViewById(R.id.status_RG);
        view.findViewById(R.id.status_LL).setVisibility(View.VISIBLE);
        nameET.setText(credential1.getName());
        ddkET.setEnabled(false);
        passPhaseET.setEnabled(true);
        if (!AppConfig.isStringNullOrBlank(AppConfig.setStringNullBlank(credential1.getSecondPassphrase()))) {
            passPhase2ET.setEnabled(true);
        }

        ddkET.setText(credential1.getDdkcode());
        if(CredentialsFragment.credential_list.size()!=0)
        {
            for(int i=0;i<CredentialsFragment.credential_list.size();i++)
            {
                if(CredentialsFragment.credential_list.get(i).getCredentialId().equalsIgnoreCase(credential1.getCredentialId()))
                {
                    passPhaseET.setText(CredentialsFragment.credential_list.get(i).getPassphrase());
                    if (CredentialsFragment.credential_list.get(i).getSecondPassphrase() != null)
                    {
                        passPhase2ET.setText(CredentialsFragment.credential_list.get(i).getSecondPassphrase());
                    }
                }
            }
        }

        //passPhaseET.setText(credential1.getPassphrase());
        //passPhase2ET.setText(AppConfig.setStringNullBlank(credential1.getSecondPassphrase()));
        referalET.setText(credential1.getReferalLink());
        notesET.setText(AppConfig.setStringNullBlank(credential1.getNotes()));
        if (credential1.getStatus().equalsIgnoreCase("active"))
            ((RadioButton) view.findViewById(R.id.active_RB)).setChecked(true);
        else
            ((RadioButton) view.findViewById(R.id.inactive_RB)).setChecked(true);


        ((TextView) view.findViewById(R.id.add_TV)).setText("Save");
        view.findViewById(R.id.add_TV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString();
                String ddk = ddkET.getText().toString();
                String pass = passPhaseET.getText().toString();
                String pass2 = passPhase2ET.getText().toString();
                String referal = referalET.getText().toString();
                String notes = notesET.getText().toString();
                if (!AppConfig.isStringNullOrBlank(name)) {
                    if (!AppConfig.isStringNullOrBlank(ddk)) {
                        if (!AppConfig.isStringNullOrBlank(pass)) {

                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            RadioButton genderradioButton = view.findViewById(selectedId);
                            String status = genderradioButton.getText().toString();
                            addCredentialCall(credential1.getCredentialId(), App.pref.getString(Constant.USER_ID, ""), name,
                                    ddk, referal, pass, pass2, notes, status);
                        } else {
                            AppConfig.showToast("First PassPhase Required.");
                        }
                    } else {
                        AppConfig.showToast("DDK Address Required.");
                    }
                } else {
                    AppConfig.showToast("Name Required.");
                }
            }
        });


        return view;
    }

    private void addCredentialCall(String credentialId, String id, String name, String ddk, String referal, String pass, String pass2, String notes, String status) {
        if (AppConfig.isInternetOn()) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            AppConfig.showLoading(dialog, "Editing Credential..");

            Call<ResponseBody> call = AppConfig.getLoadInterface().editCredential(
                    AppConfig.setRequestBody(credentialId),
                    AppConfig.getStringPreferences(getActivity(), Constant.JWTToken),
                    AppConfig.setRequestBody(name),
                    AppConfig.setRequestBody(ddk),
                    AppConfig.setRequestBody(referal),
                    AppConfig.setRequestBody(pass),
                    AppConfig.setRequestBody(pass2),
                    AppConfig.setRequestBody(notes),
                    AppConfig.setRequestBody(status)
            );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {

                                AppConfig.showToast(object.getString("msg"));
                                getActivity().onBackPressed();

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
                        ShowApiError(getActivity(),"server error in edit-credentials");
                    }
                    AppConfig.hideLoading(dialog);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppConfig.hideLoading(dialog);
                    t.printStackTrace();
                    errordurigApiCalling(getActivity(),t.getMessage());
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.setTitle("Edit Credential");
    }

}
