package com.ddkcommunity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ddkcommunity.fragment.projects.SelectPaymentPoolingFragment;
import com.ddkcommunity.interfaces.GegtSettingStatusinterface;
import com.ddkcommunity.interfaces.GetAllCredential;
import com.ddkcommunity.interfaces.GetAvailableValue;
import com.ddkcommunity.interfaces.GetBTCUSDTETHPriceCallback;
import com.ddkcommunity.interfaces.GetCryptoSubscriptionResponse;
import com.ddkcommunity.interfaces.GetPoolingResponse;
import com.ddkcommunity.interfaces.GetUSDAndBTCCallback;
import com.ddkcommunity.interfaces.GetUserProfile;
import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.credential.CredentialsResponse;
import com.ddkcommunity.model.getSettingModel;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.model.wallet.WalletResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.dataPutMethods;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ddkcommunity.utilies.dataPutMethods.ReplacecommaValue;
import static com.ddkcommunity.utilies.dataPutMethods.ShowApiError;
import static com.ddkcommunity.utilies.dataPutMethods.ShowServerPost;
import static com.ddkcommunity.utilies.dataPutMethods.errordurigApiCalling;
import static com.ddkcommunity.utilies.dataPutMethods.putGoogleAuthStatus;

public class UserModel {

    private static UserModel instance;

    public static void initInstance(Context applicationContext) {
        instance = new UserModel();
    }

    public static UserModel getInstance() {
        if (instance == null) {
            instance = new UserModel();
        }
        return instance;
    }

    public BigDecimal btcRate, usdRate;
    public BigDecimal ddkBuyPrice, ddkSellPrice, ethBuyPrice, ethSellPrice, btcBuyPrice, btcSellPrice, usdtBuyPrice, usdtSellPrice,samkoinSellPrice,samkoinBuyPrice,tronSellPrice,tronBuyPrice;
    public BigDecimal tronBuyPercentage, tronSellPercentage,ddkBuyPercentage, ddkSellPercentage, btcBuyPercentage, btcSellPercentage, samkoinBuyPercentage, samkoinSellPercentage,ethBuyPercentage, ethSellPercentage,usdtBuyPercentage, usdtSellPercentage,samkoinvalueper,phpvalueper;

    public void getUSDCall(final GetUSDAndBTCCallback getUSDAndBTCCallback, final Activity activity) {
        if (AppConfig.isInternetOn()) {
            Call<ResponseBody> call = AppConfig.getLoadInterface().getDDKValue();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            Log.d("response_ddkprice",responseData);
                            JSONObject jsonObject = new JSONObject(responseData);
                            Log.d("response_ddkprice",jsonObject.toString());
                            JSONObject ddkPriceObject = jsonObject.getJSONObject("ddk_price");
                            JSONObject ddkPercentageObject = jsonObject.getJSONObject("percentage");
                            JSONObject send_transaction_fees=jsonObject.getJSONObject("send_transaction_fees");
                            String php_transaction_fees=send_transaction_fees.getString("php_wallet_transaction_fees");
                            String sam_transaction_fees=send_transaction_fees.getString("sam_transaction_fees");
                            String btc_transaction_fees=send_transaction_fees.getString("btc_transaction_fees");
                            String eth_transaction_fees=send_transaction_fees.getString("eth_transaction_fees");
                            String tron_transaction_fees=send_transaction_fees.getString("tron_transaction_fees");
                            String usdt_transaction_fees=send_transaction_fees.getString("usdt_transaction_fees");
                            String transaction_fees_mode_eth=send_transaction_fees.getString("transaction_fees_mode_eth");
                            String transaction_fees_mode_usdt=send_transaction_fees.getString("transaction_fees_mode_usdt");
                            //for sell
                            JSONObject sell_transaction_fees=jsonObject.getJSONObject("sell_transaction_fees");
                            String sell_sam_transaction_fees=sell_transaction_fees.getString("sam_transaction_fees");
                            AppConfig.setStringPreferences(App.getInstance(), Constant.sellsam_transaction_fees,""+sell_sam_transaction_fees);
                            //...........
                            AppConfig.setStringPreferences(App.getInstance(), Constant.transaction_fees_mode_eth,""+transaction_fees_mode_eth);
                            AppConfig.setStringPreferences(App.getInstance(), Constant.transaction_fees_mode_usdt,""+transaction_fees_mode_usdt);
                            AppConfig.setStringPreferences(App.getInstance(), Constant.sendphp_transaction_fees,""+php_transaction_fees);
                            AppConfig.setStringPreferences(App.getInstance(), Constant.sendsam_transaction_fees,""+sam_transaction_fees);
                            AppConfig.setStringPreferences(App.getInstance(), Constant.sendbtc_transaction_fees,""+btc_transaction_fees);
                            AppConfig.setStringPreferences(App.getInstance(), Constant.sendeth_transaction_fees,""+eth_transaction_fees);
                            AppConfig.setStringPreferences(App.getInstance(), Constant.sendusdt_transaction_fees,""+usdt_transaction_fees);
                            AppConfig.setStringPreferences(App.getInstance(), Constant.sendtron_transaction_fees,""+tron_transaction_fees);
                            //...........
                            String tempUSDTRate = ddkPriceObject.getString("last");
                            String finalddkprice = ddkPriceObject.getString("low");
                            AppConfig.setStringPreferences(App.getInstance(),Constant.MAINDDKAMOUNT,finalddkprice);
                            Double buyPercent = ddkPercentageObject.getDouble("buy");
                            Double sellPercent = ddkPercentageObject.getDouble("sell");
                            Double ethBuyPer = ddkPercentageObject.getDouble("ethBuyPer");
                            Double ethSellPer = ddkPercentageObject.getDouble("ethSellPer");
                            Double btcBuyPer = ddkPercentageObject.getDouble("btcBuyPer");
                            Double btcSellPer = ddkPercentageObject.getDouble("btcSellPer");
                            Double usdtBuyPer = ddkPercentageObject.getDouble("usdtBuyPer");
                            Double usdtSellPer = ddkPercentageObject.getDouble("usdtSellPer");
                            Double samBuyPer=ddkPercentageObject.getDouble("samBuyPer");
                            Double samSellPer=ddkPercentageObject.getDouble("samSellPer");
                            Double tronBuyPer=ddkPercentageObject.getDouble("tronBuyPer");
                            Double tronSellPer=ddkPercentageObject.getDouble("tronSellPer");
                            Double lastconversionratesell=ddkPriceObject.getDouble("last");
                            Double minimun_subscription=ddkPercentageObject.getDouble("minimun_subscription");
                            Double minimum_cashout=jsonObject.getDouble("minimum_cashout");
                            Double maximum_cashout=jsonObject.getDouble("maximum_redeem");
                            AppConfig.setStringPreferences(App.getInstance(),Constant.MINIMUM_CASHOUT,minimum_cashout+"");
                            AppConfig.setStringPreferences(App.getInstance(),Constant.MAXIMUM_CASHOUT,maximum_cashout+"");
                            AppConfig.setStringPreferences(App.getInstance(), Constant.MINIMUM_SUBSCRIPTION,""+minimun_subscription);
                            ethBuyPercentage = new BigDecimal(ethBuyPer+"");
                            ethSellPercentage = new BigDecimal(ethSellPer+"");
                            samkoinBuyPercentage=new BigDecimal(samBuyPer+"");
                            samkoinSellPercentage=new BigDecimal(samSellPer+"");
                            tronBuyPercentage=new BigDecimal(tronBuyPer+"");
                            tronSellPercentage=new BigDecimal(tronSellPer+"");
                            btcBuyPercentage = new BigDecimal(btcBuyPer+"");
                            btcSellPercentage = new BigDecimal(btcSellPer+"");
                            usdtBuyPercentage = new BigDecimal(usdtBuyPer+"");
                            usdtSellPercentage = new BigDecimal(usdtSellPer+"");
                            Double sam_value=jsonObject.getDouble("sam_value");
                            samkoinvalueper=new BigDecimal(sam_value+"");
                            ddkBuyPercentage = new BigDecimal(buyPercent+"");
                            ddkSellPercentage = new BigDecimal(sellPercent+"");
                            Double currency_wallet=jsonObject.getDouble("currency_wallet");
                            phpvalueper=new BigDecimal(currency_wallet+"");
                            usdRate = new BigDecimal(tempUSDTRate);
                            getUSDAndBTCCallback.getValues(null, usdRate);
                            App.editor.putString(Constant.SAM_CONVERSION,lastconversionratesell+"");
                            App.editor.apply();
                            //.......
                            JSONObject minimum_send_transaction_amount=jsonObject.getJSONObject("minimum_send_transaction_amount");
                            String minimum_samsend_transaction_fee=minimum_send_transaction_amount.getString("sam_koin");
                            String minimum_btcsend_transcation_fee=minimum_send_transaction_amount.getString("btc");
                            String minimum_ethsend_transcation_fee=minimum_send_transaction_amount.getString("eth");
                            String minimum_usdtsend_transaction_fee=minimum_send_transaction_amount.getString("usdt");
                            String minimum_tronsend_transaction_fee=minimum_send_transaction_amount.getString("tron");
                            AppConfig.setStringPreferences(App.getInstance(), Constant.minimum_samsend_transaction_fee,""+minimum_samsend_transaction_fee);
                            AppConfig.setStringPreferences(App.getInstance(), Constant.minimum_btcsend_transaction_fee,""+minimum_btcsend_transcation_fee);
                            AppConfig.setStringPreferences(App.getInstance(), Constant.minimum_ethsend_transaction_fee,""+minimum_ethsend_transcation_fee);
                            AppConfig.setStringPreferences(App.getInstance(), Constant.minimum_usdtsend_transaction_fee,""+minimum_usdtsend_transaction_fee);
                            AppConfig.setStringPreferences(App.getInstance(), Constant.minimum_tronsend_transaction_fee,""+minimum_tronsend_transaction_fee);

                        }catch (Exception e) {
                            ShowApiError(activity,"exception in response sixthface/ddkPrice");
                            e.printStackTrace();
                        }
                    } else {
                        getUSDAndBTCCallback.getValues(null, null);
                        ShowApiError(activity,"server error in sixthface/ddkPrice");
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    public void getUsdtEthBtcPriceCall(final GetBTCUSDTETHPriceCallback getUSDAndBTCCallback, final Activity activity) {
        if (AppConfig.isInternetOn()) {
            Call<ResponseBody> call = AppConfig.getLoadInterface().getUSDTETHKValue(Constant.BASE_URL+"commondetails/get-new-prices");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful())
                    {
                        try
                        {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            String status=object.getString("status");
                            if(status.equalsIgnoreCase("1"))
                            {
                                String btc = object.getString("BTC");
                                String eth = object.getString("eth");
                                String usdt = object.getString("usdt");
                                String tron = object.getString("tron");
                                getUSDAndBTCCallback.getValues(new BigDecimal(btc), new BigDecimal(eth), new BigDecimal(usdt), new BigDecimal(tron));
                            } else {
                                ShowApiError(activity,"server error in commondetails/get-new-prices");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ShowApiError(activity,"server error in thirdparty/get-new-prices");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    public void getWalletDetails(final int loadview, String wallet_Id, final Context mContext, final GetAvailableValue getAvailableValue)
    {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        if(loadview!=1)
        {
            AppConfig.showLoading(dialog, "Please wait.....");
        }
        String walletidvalue=wallet_Id;
        Call<ResponseBody> call = AppConfig.getLoadInterface().receiveCall(AppConfig.getStringPreferences(mContext, Constant.JWTToken), AppConfig.setRequestBody(wallet_Id),AppConfig.setRequestBody(App.pref.getString(Constant.IVPARAM, "")),AppConfig.setRequestBody(App.pref.getString(Constant.KEYENCYPARAM, "")));
        Log.d("paramter",wallet_Id+" "+App.pref.getString(Constant.IVPARAM, "")+" "+App.pref.getString(Constant.KEYENCYPARAM, ""));
        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(loadview!=1) {
                    AppConfig.hideLoading(dialog);
                }
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getInt(Constant.STATUS) == 1) {
                            WalletResponse successResponse = new Gson().fromJson(responseData, WalletResponse.class);
                            if(loadview==1)
                            {
                                App.editor.putString(Constant.PUBLIC_KEY, successResponse.getWallet().getPublicKey());
                                App.editor.putString(Constant.Wallet_ADD, successResponse.getWallet().getAddress());
                                App.editor.putString(Constant.senderDDKAddress, successResponse.getWallet().getAddress());
                                App.editor.putString(Constant.WALLET_ID, successResponse.getWallet().getWalletId());
                                App.editor.commit();
                            }

                            App.editor.putString(Constant.PUBLIC_KEY, successResponse.getWallet().getPublicKey());
                            App.editor.commit();

                            double bal = 0.0;
                            if (!AppConfig.isStringNullOrBlank(successResponse.getWallet().getTotalFrozenAmt())) {
                                bal = (Double.parseDouble(successResponse.getWallet().getBalance()) - Double.parseDouble(successResponse.getWallet().getTotalFrozenAmt()));
                                bal = (bal / 100000000);
//                                tvAvailableDdk.setText(String.format("%.4f", bal));
                            } else if (successResponse.getWallet().getTotalFrozenAmt().equalsIgnoreCase("0")) {
                                bal = (Double.parseDouble(successResponse.getWallet().getBalance()) - Double.parseDouble(successResponse.getWallet().getTotalFrozenAmt()));
                                bal = (bal / 100000000);
//                                tvAvailableDdk.setText(String.format("%.4f", bal));
                            }
                            getAvailableValue.getValues(String.format("%.8f", bal), successResponse);
//                            walletTV.setText(AppConfig.SpannableStringBuilder(walletTV.getText().toString(), '.', 0.5f));
                        } else if (object.getInt(Constant.STATUS) == 3) {
                            AppConfig.showToast(object.getString("msg"));
                            AppConfig.openSplashActivity((Activity) mContext);
                        } else if (object.getInt(Constant.STATUS) == 4)
                        {
                            ShowServerPost((Activity)mContext,"ddk server error apibalance/receive-koin");
                        }else {
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ShowApiError(mContext,"server error in apibalance/receive-koin");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(loadview!=1) {
                    AppConfig.hideLoading(dialog);
                }
                Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public void getCredentialsCall(final Context mContext, final GetAllCredential getAllCredential) {
        if (AppConfig.isInternetOn())
        {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
            hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
            Call<ResponseBody> call = AppConfig.getLoadInterface().credentialList(AppConfig.getStringPreferences(mContext, Constant.JWTToken),hm);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {
                                ArrayList<Credential> eventList = new ArrayList<>();
                                CredentialsResponse registerResponse = new Gson().fromJson(responseData, CredentialsResponse.class);

                                for (Credential credential : registerResponse.getCredentials()) {
                                    if (credential.getStatus().equalsIgnoreCase("active")) {
                                        eventList.add(credential);
                                    }
                                }
                                getAllCredential.getCredential(eventList);
                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity((Activity) mContext);
                            }else if (object.getInt(Constant.STATUS) == 4)
                            {
                                ShowServerPost((Activity)mContext,"ddk server error credientail");
                            } else {
                                AppConfig.showToast (object.getString("msg"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ShowApiError(mContext,"server error in TransferApi/get-credentials");
//                        AppConfig.showToast("Server error");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }


    public void getProfileCall(final Context mContext, final GetUserProfile getUserProfile)
    {
        if (AppConfig.isInternetOn())
        {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
            hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
            Log.d("param model",hm.toString());
            Call<ResponseBody> call = AppConfig.getLoadInterface().getProfileCall(AppConfig.getStringPreferences(mContext, Constant.JWTToken),hm);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            Log.d("user profile",responseData);
                            JSONObject object = new JSONObject(responseData);
                            if (object.getInt(Constant.STATUS) == 1) {
                                UserResponse userResponse = new Gson().fromJson(responseData, UserResponse.class);
                                AppConfig.setUserData(mContext, userResponse);
                                try {
                                    String user_idvalue=userResponse.getUser().getId().toString();
                                    App.editor.putString(Constant.USER_ID,user_idvalue);
                                    App.editor.apply();
                                    //........
                                    putGoogleAuthStatus(userResponse.getUser().getGauth_status(),userResponse.getUser().getGoogle_authentication(),userResponse.getUser().getGoogle_auth_secret());
                                    //.........
                                    JSONObject jsonob = object.getJSONObject(Constant.eth_details);
                                    if(jsonob.length()!=0)
                                    {
                                        dataPutMethods.putOtherWallet(userResponse.getEth_details().getPublic_key(),"",userResponse.getEth_details().getWallet_address(), userResponse.getEth_details().getWallet_id(),userResponse.getEth_details().getSecret());
                                    }
                                    JSONObject jsonobusdt = object.getJSONObject(Constant.usdt_details);
                                    if(jsonobusdt.length()!=0)
                                    {
                                        dataPutMethods.putUsdtWallet(userResponse.getUsdt_details().getPublic_key(),userResponse.getUsdt_details().getWallet_address(),userResponse.getUsdt_details().getWallet_id(),userResponse.getUsdt_details().getSecret());
                                    }
                                    JSONObject jsonobtron = object.getJSONObject(Constant.usdt_details);
                                    if(jsonobtron.length()!=0)
                                    {
                                        dataPutMethods.puttronWallet(userResponse.getUsdt_details().getPublic_key(),userResponse.getUsdt_details().getWallet_address().toString(),userResponse.getUsdt_details().getWallet_id().toString(),userResponse.getUsdt_details().getSecret().toString());
                                    }
                                    JSONObject jsonobbtc = object.getJSONObject(Constant.btc_details);
                                    if(jsonobbtc.length()!=0)
                                    {
                                        dataPutMethods.putBTCWallet(userResponse.getBtc_details().getPublic_key(),userResponse.getBtc_details().getWallet_address(),userResponse.getBtc_details().getWallet_id(),userResponse.getBtc_details().getSecret());
                                    }
                                    JSONObject jsonsamkoin = object.getJSONObject(Constant.samkoin_details);
                                    if(jsonsamkoin.length()!=0)
                                    {
                                        dataPutMethods.putSamKoinWallet(userResponse.getSam_koin_details().getPublic_key(),userResponse.getSam_koin_details().getWallet_address(),userResponse.getSam_koin_details().getWallet_id(),userResponse.getSam_koin_details().getSecret());
                                    }

                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                getUserProfile.getUserInfo(userResponse);
                            } else if (object.getInt(Constant.STATUS) == 3) {
                                AppConfig.showToast(object.getString("msg"));
                                AppConfig.openSplashActivity((Activity) mContext);
                            }else if (object.getInt(Constant.STATUS) == 4)
                            {
                                ShowServerPost((Activity)mContext,"ddk server error profile");
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
                        ShowApiError(mContext,"server error in eightface/user-profile");
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            AppConfig.showToast("No internet.");
        }
    }

    //.............get api setting status
    public void getSettignSatusView(final Activity activity,final String functionName,final GegtSettingStatusinterface settingResponse)
    {
        //getGeneralFunctionality
        final HashMap<String, String> hm = new HashMap<>();
        hm.put("functionality",functionName);
        Call<getSettingModel> call = AppConfig.getLoadInterface().getGeneralFunctionality(AppConfig.getStringPreferences(activity, Constant.JWTToken), hm);
        call.enqueue(new Callback<getSettingModel>()
        {
            @Override
            public void onResponse(Call<getSettingModel> call, Response<getSettingModel> response)
            {
                AppConfig.hideLoader();
                try
                {
                    if(response.body() != null)
                    {
                        if (response.isSuccessful() && response.body() != null)
                        {
                            settingResponse.getResponse(response);
                        }
                    }else
                    {
                        AppConfig.hideLoader();
                        ShowApiError(activity, "server error in commondetails/general-functionality");
                    }
                }catch (Exception e) {
                    AppConfig.hideLoader();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<getSettingModel> call, Throwable t)
            {
                errordurigApiCalling(activity,t.getMessage());
                AppConfig.hideLoader();
            }
        });
    }
    //............
    public void sendSubscriptionPayment(final Activity activity, final String subscriptiontype, final String input_amount, final String conversion_rate, final String sam_koin_conversion, final String fee, final String total_usdt_subscription, final String transaction_id, final GetCryptoSubscriptionResponse cryptoResponse)
    {
        String transactionreivervalue = "",transactionfeessreceiver="";
        final HashMap<String, String> hm = new HashMap<>();
        if(subscriptiontype.equalsIgnoreCase("creditcard"))
        {
            hm.put("secret",App.pref.getString(Constant.SAMKOIN_Secaret, ""));
            hm.put("public_key", App.pref.getString(Constant.BTC_publickey, ""));
            hm.put("subscription_mode", "stripe");
            hm.put("crypto_type","credit card");
            hm.put("sender_address", App.pref.getString(Constant.SAMKOIN_ADD, ""));
            transactionreivervalue=AppConfig.getStringPreferences(activity, Constant.Transaction_Receiver_SAMKOIN);
            transactionfeessreceiver=AppConfig.getStringPreferences(activity, Constant.SAMTransactionAddress);
        }else
        if(subscriptiontype.equalsIgnoreCase("samkoin"))
        {
            hm.put("secret",App.pref.getString(Constant.SAMKOIN_Secaret, ""));
            hm.put("public_key", App.pref.getString(Constant.SAMKOIN_publickey, ""));
            hm.put("subscription_mode", "manual");
            hm.put("crypto_type","sam_koin");
            hm.put("sender_address", App.pref.getString(Constant.SAMKOIN_ADD, ""));
            transactionreivervalue=AppConfig.getStringPreferences(activity, Constant.Transaction_Receiver_SAMKOIN);
            transactionfeessreceiver=AppConfig.getStringPreferences(activity, Constant.SAMTransactionAddress);
        }else
        if(subscriptiontype.equalsIgnoreCase("btc"))
        {
            hm.put("secret",App.pref.getString(Constant.BTC_Secaret, ""));
            hm.put("public_key", App.pref.getString(Constant.BTC_publickey, ""));
            hm.put("subscription_mode", "manual");
            hm.put("crypto_type","BTC");
            hm.put("sender_address", App.pref.getString(Constant.BTC_ADD, ""));
            transactionreivervalue=AppConfig.getStringPreferences(activity, Constant.Transaction_Receiver_BTC);
            transactionfeessreceiver=AppConfig.getStringPreferences(activity, Constant.BTCTransactionAddress);
        }else
        if(subscriptiontype.equalsIgnoreCase("eth"))
        {
            hm.put("secret",App.pref.getString(Constant.Eth_Secret, ""));
            hm.put("public_key", App.pref.getString(Constant.Eth_publickey, ""));
            hm.put("subscription_mode", "manual");
            hm.put("crypto_type","ETH");
            hm.put("sender_address", App.pref.getString(Constant.Eth_ADD, ""));
            transactionreivervalue=AppConfig.getStringPreferences(activity, Constant.Transaction_Receiver_ETH);
            transactionfeessreceiver=AppConfig.getStringPreferences(activity, Constant.ETHTranscationAddress);
        }else
        if(subscriptiontype.equalsIgnoreCase("usdt"))
        {
            hm.put("secret",App.pref.getString(Constant.USDT_Secaret, ""));
            hm.put("public_key", App.pref.getString(Constant.USDT_publickey, ""));
            hm.put("subscription_mode", "manual");
            hm.put("crypto_type","USDT");
            hm.put("sender_address", App.pref.getString(Constant.USDT_ADD, ""));
            transactionreivervalue=AppConfig.getStringPreferences(activity, Constant.Transaction_Receiver_USDT);
            transactionfeessreceiver=AppConfig.getStringPreferences(activity, Constant.USDTTransactionAddress);
        }
        hm.put("transaction_receiver_address",transactionfeessreceiver);
        hm.put("receiver_address",transactionreivervalue);
        hm.put("input_amount",input_amount);
        hm.put("status","pending");
         hm.put("device_type", "android");
        hm.put("device_token", App.RegPref.getString(Constant.FIREBASE_TOKEN, ""));
        hm.put("conversion_rate",conversion_rate);
        hm.put("sam_koin_conversion", sam_koin_conversion);
        hm.put("fee",fee);
        hm.put("total_usdt_subscription",total_usdt_subscription);
        hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
        hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
        hm.put("transaction_id", transaction_id);
        Log.d("paramterHM",hm+"");
        Call<ResponseBody> call = AppConfig.getLoadInterface().sendSubscriptionPayment(AppConfig.getStringPreferences(activity, Constant.JWTToken), hm);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (subscriptiontype.equalsIgnoreCase("creditcard") || subscriptiontype.equalsIgnoreCase("coinpayment")) {
                    AppConfig.hideLoader();
                }
                try {
                    if(response.body() != null)
                    {
                        JSONObject object = new JSONObject(response.body().string());
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("paramter response", object.toString());
                            cryptoResponse.getResponse(object);
                        } else if (object.getInt(Constant.STATUS) == 0) {
                            Toast.makeText(activity, "You do not have sufficient amount", Toast.LENGTH_SHORT).show();

                        } else if (object.getInt(Constant.STATUS) == 4) {
                            ShowServerPost(activity, "ddk server error sam-payment/send-koin-pooling-dev-new-manual");
                        } else {
                            ShowApiError(activity, "server error in sam-payment/send-koin-pooling-dev-new-manual");
                        }
                    }else
                    {
                        AppConfig.hideLoader();
                        ShowApiError(activity, "server error in sam-payment/send-koin-pooling-dev-new-manual");
                    }
                } catch (IOException e) {
                    AppConfig.hideLoader();
                    e.printStackTrace();
                } catch (JSONException e) {
                    AppConfig.hideLoader();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                errordurigApiCalling(activity,t.getMessage());
                AppConfig.hideLoader();
            }
        });
    }

    public void sendDataToServer(String estimatedfee, String usereslecteDDKAddress, String typevalue, String selectedddkaddress, String paymentmode, String totalincrypto, String totalusdwithcharge, String con_amount_usd, String userEnterDDk, final Context mContext, String tokenId, final String paymentType, String usd_conversion,
                                 String ddkCoin, String tvCredentialCreditCard, String ddkSecret,
                                 String currencyCode, String totalCurrencyAmount, String totalTransFee,
                                 String currencyTransactionPercent, final GetPoolingResponse poolingResponse) {

        final HashMap<String, String> hm = new HashMap<>();
        if (paymentType.equalsIgnoreCase("stripe"))
        {
            String transactionreivervalue=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver);
            hm.put("crypto_type",paymentmode);
            hm.put("paymentmode", paymentmode);
            hm.put("totalincrypto",totalincrypto);
            hm.put("totalusdwithcharge",totalusdwithcharge);
            hm.put("ddkreceiver",transactionreivervalue);
            hm.put("amount", con_amount_usd);
            hm.put("totalDDK",userEnterDDk);
            hm.put("con_amount_usd",userEnterDDk);
            hm.put("status", "pending");
            hm.put("conversion_usd", "" + usd_conversion);
            hm.put("recipientid", transactionreivervalue);
            hm.put("public_key", App.pref.getString(Constant.PUBLIC_KEY, ""));
            hm.put("address",selectedddkaddress);
            hm.put("secret", ddkSecret);
            hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
            hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
            hm.put("ddksender", selectedddkaddress);
            hm.put("estimated_fees",estimatedfee);

        } else if (paymentType.equalsIgnoreCase("manual")) {

            if(typevalue.equalsIgnoreCase("DDK"))
            {
                hm.put("crypto_type",typevalue);
                hm.put("paymentmode", paymentmode);
                hm.put("totalincrypto",totalincrypto);
                hm.put("totalusdwithcharge",totalusdwithcharge);
                hm.put("amount", userEnterDDk);
                hm.put("totalDDK",ddkCoin);
                hm.put("con_amount_usd",con_amount_usd);
                hm.put("status", "pending");
                hm.put("conversion_usd", "" + usd_conversion);
                hm.put("public_key", App.pref.getString(Constant.PUBLIC_KEY, ""));
                String transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver);
                hm.put("recipientid", transactionreiver);
                hm.put("ddkreceiver", transactionreiver);
                hm.put("address", App.pref.getString(Constant.Wallet_ADD, ""));
                hm.put("secret", App.pref.getString(Constant.Secret, ""));
                hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
                hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
                hm.put("ddksender", usereslecteDDKAddress);
                hm.put("estimated_fees",estimatedfee);

            }else if(typevalue.equalsIgnoreCase("BTC"))
            {
                hm.put("crypto_type",typevalue);
                hm.put("paymentmode", paymentmode);
                hm.put("totalincrypto",totalincrypto);
                hm.put("totalusdwithcharge",totalusdwithcharge);
                hm.put("amount", userEnterDDk);
                hm.put("totalDDK",con_amount_usd);
                hm.put("con_amount_usd",con_amount_usd);
                hm.put("status", "pending");
                hm.put("conversion_usd", "" + usd_conversion);
                hm.put("public_key", App.pref.getString(Constant.PUBLIC_KEY, ""));
                String transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver_BTC);
                String secratevalue=App.pref.getString(Constant.BTC_Secaret, "");
                hm.put("secret",secratevalue);
                hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
                hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
                hm.put("recipientid", transactionreiver);
                hm.put("ddkreceiver", transactionreiver);
                hm.put("address", App.pref.getString(Constant.BTC_ADD, ""));
                //for new
                hm.put("ddksender", usereslecteDDKAddress);
                hm.put("estimated_fees",estimatedfee);
                Log.d("parambtc","response"+hm);

            }else if(typevalue.equalsIgnoreCase("ETH"))
            {
                String transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver_ETH);
                String secratevalue=App.pref.getString(Constant.Eth_Secret, "");
                hm.put("secret",secratevalue);
                hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
                hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
                hm.put("crypto_type",typevalue);
                hm.put("paymentmode", paymentmode);
                hm.put("totalincrypto",totalincrypto);
                hm.put("totalusdwithcharge",totalusdwithcharge);
                hm.put("amount", userEnterDDk);
                hm.put("totalDDK",con_amount_usd);
                hm.put("con_amount_usd",con_amount_usd);
                hm.put("status", "pending");
                hm.put("conversion_usd", "" + usd_conversion);
                hm.put("public_key", App.pref.getString(Constant.PUBLIC_KEY, ""));
                hm.put("recipientid", transactionreiver);
                hm.put("ddkreceiver", transactionreiver);
                hm.put("address", App.pref.getString(Constant.Eth_ADD, ""));
                //for new
                hm.put("ddksender", usereslecteDDKAddress);
                hm.put("estimated_fees",estimatedfee);

            }else if(typevalue.equalsIgnoreCase("USDT"))
            {
                String transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver_USDT);
                String secratevalue=App.pref.getString(Constant.USDT_Secaret, "");
                hm.put("secret",secratevalue);
                hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
                hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
                hm.put("crypto_type",typevalue);
                hm.put("paymentmode", paymentmode);
                hm.put("totalincrypto",totalincrypto);
                hm.put("totalusdwithcharge",totalusdwithcharge);
                hm.put("amount", userEnterDDk);
                hm.put("totalDDK",con_amount_usd);
                hm.put("con_amount_usd",con_amount_usd);
                hm.put("status", "pending");
                hm.put("conversion_usd", "" + usd_conversion);
                hm.put("public_key", App.pref.getString(Constant.PUBLIC_KEY, ""));
                hm.put("recipientid", transactionreiver);
                hm.put("ddkreceiver", transactionreiver);
                hm.put("address", App.pref.getString(Constant.USDT_ADD, ""));
               //............
                hm.put("ddksender", usereslecteDDKAddress);
                hm.put("estimated_fees",estimatedfee);
            }

        } else if (paymentType.equalsIgnoreCase("coin_payments")) {

            String transactionreiver=AppConfig.getStringPreferences(mContext, Constant.Transaction_Receiver);
            hm.put("paymentmode", paymentmode);
            hm.put("totalincrypto",totalincrypto);
            hm.put("totalusdwithcharge",totalusdwithcharge);
            hm.put("ddkreceiver", transactionreiver);
            hm.put("amount", userEnterDDk);
            hm.put("totalDDK",ddkCoin);
            hm.put("con_amount_usd",con_amount_usd);
            hm.put("status", "pending");
            hm.put("conversion_usd", "" + usd_conversion);
            hm.put("public_key", App.pref.getString(Constant.PUBLIC_KEY, ""));
            hm.put("address", App.pref.getString(Constant.Wallet_ADD, ""));
            hm.put("recipientid", transactionreiver);
            hm.put("pay_currency", currencyCode);
            hm.put("tot_payment", "" + totalCurrencyAmount);
            hm.put("transaction_fees", "" + totalTransFee);
            hm.put("transaction_fees_percent", "" + currencyTransactionPercent);
            hm.put("secret", ddkSecret);
            hm.put("iv", App.pref.getString(Constant.IVPARAM, ""));
            hm.put("key", App.pref.getString(Constant.KEYENCYPARAM, ""));
            hm.put("ddksender", App.pref.getString(Constant.Wallet_ADD, ""));
            hm.put("estimated_fees",estimatedfee);
            hm.put("crypto_type",paymentmode);
        }
        hm.put("device_type", "Android");
        hm.put("device_token", App.pref.getString("refreshedToken", ""));
        hm.put("payment_type", paymentType);
        hm.put("transaction_id", tokenId);
        Log.d("paramterHM",hm+"");
        Call<ResponseBody> call = AppConfig.getLoadInterface().sendKoinPoolingManual(AppConfig.getStringPreferences(mContext, Constant.JWTToken), hm);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!paymentType.equalsIgnoreCase("manual")) {
                    AppConfig.hideLoader();
                }
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("paramter response",object.toString() );
                        poolingResponse.getResponse(object);
                    } else if (object.getInt(Constant.STATUS) == 0) {
                        Toast.makeText(mContext,"You do not have sufficient amount",Toast.LENGTH_SHORT).show();

                    } else if (object.getInt(Constant.STATUS) == 4) {
                        ShowServerPost((Activity)mContext,"ddk server error cryptopayemnt/send-koin-pooling-dev-new-manual");
                    }else {
                        ShowApiError(mContext,"server error in cryptopayemnt/send-koin-pooling-dev-new-manual");
                    }
                } catch (IOException e) {
                    AppConfig.hideLoader();
                    e.printStackTrace();
                } catch (JSONException e) {
                    AppConfig.hideLoader();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
               // AppConfig.showToast("Server issue");
                AppConfig.hideLoader();
            }
        });
    }

    private static String youtubeChannel;

    public static String getYoutubeChannel() {
        return youtubeChannel;
    }

    public void setYoutubeChannel(String youtubeVideoIdFromUrl) {
        youtubeChannel = youtubeVideoIdFromUrl;
    }
}
