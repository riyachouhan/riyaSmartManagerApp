package com.ddkcommunity;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class Constant
{
    public static  String filter_current_address = "";
    public static final String WALLET_ID = "walletId";
    //public static String BASE_URL = "https://stripedemo.extreme.org.in/ddk_global/api/";
//    public static final String BASE_URL = "https://ddkcommunity.extreme.org.in/ddk_global/api/";
//    public static final String BASE_URL = "http://157.245.52.206/api/";
   //public static final String sbuurlcheil="http://128.199.182.16/new_api/api/";
  // public static final String SLIDERIMG="http://157.245.52.206/public/";
    public static final String SLIDERIMG="http://128.199.182.16/public/";
    //public static final String BASE_URL = "http://157.245.52.206/new_api/api/v2.4/";
    //public static final String BASE_URL = "http://157.245.52.206/new_api/api/v2.4/v2.4.13/";
   // public static final String BASE_URL = "http://157.245.52.206/new_api/api/v2.4/v2.4.14/";
    //public static final String BASE_URL= "http://128.199.182.16/new_api/api/v2.4/v2.4.15/";
   // public static final String BASE_URL= "http://157.245.52.206/new_api/api/v2.4/v2.4.15/";
   // public static final String BASE_URL= "http://157.245.52.206/new_api/api/v2.4/v2.4.17/";
    //public static final String BASE_URL= "http://157.245.52.206/new_api/api/v2.4/v2.4.17/";
   public static final String BASE_URL= "http://128.199.182.16/new_api/api/v2.4/v2.4.17/";
   // public static final String BASE_URL_MAP="http://159.89.206.213/Files/api/";
    public static String BASE_URL_MAP="";
    //public static final String bankimgurl="http://157.245.5.2.206/new_api/public/uploads/bankLogo/";
    public static final String IS_LOGIN = "isLogin";
    public static final String STATUS = "status";
    public static final String ANDROID = "android";
    public static final String USER_ID = "user_id";
    public static final String USER_REFERAL_CODE = "user_referalcode";
    public static final String JWTToken = "JWTToken";
    public static final String samkoin_details="sam_koin_details";
    public static final String eth_details="eth_details";
    public static final String usdt_details="usdt_details";
    public static final String btc_details="btc_details";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_NAME = "user_name";
    public static final String USER_MOBILE = "user_mobile";

    public static final String FIREBASE_TOKEN = "refreshedToken";
    //    public static final String YOUTUBE_API_KEY = "AIzaSyAFHyhGNNEpAJTNGP6DRe3vzbfIS3rVh9A";
//    public static final String URL_SHORTNER_API_KEY = "AIzaSyAK4Z9UuLirrkMs1mbJwPuNcGNeohGAgOc";
    public static final String isWallet = "isWallet";
    public static final String PUBLIC_KEY = "public_key";
    public static final String Wallet_ADD = "wallet_address";
    public static final String Eth_ADD = "eth_address";
    public static final String Eth_ADD_ID = "eth_address_id";
    public static final String Eth_Balance = "eth_balance";
    public static final String Eth_Secret = "eth_secret";
    public static final String Eth_Secret_key = "eth_secret_key";
    public static final String Eth_publickey = "eth_publickey";

    public static final String tron_ADD = "tron_address";
    public static final String tron_ADD_ID = "tron_address_id";
    public static final String tron_Balance = "tron_balance";
    public static final String tron_Secret = "tron_secret";
    public static final String tron_publickey = "tron_publickey";

    public static final String BTC_ADD = "btc_address";
    public static final String BTC_Add_Id = "btc_address_id";
    public static final String BTC_Secaret = "btc_secaret";
    public static final String BTC_Balance = "btc_balance";
    public static final String PHP_Balance = "php_balance";
    public static final String PHP_Functionality_View = "php_functionality_view";
    public static final String BTC_publickey = "btc_publickey";
    public static final String SAM_Balance = "Sam_balance";
    public static final String LIQUID = "liquid";
    public static final String INVITE_CONTENT = "invite_content";
    public static final String SAM_CONVERSION = "SamConversion";
    public static final String Strip_Payment_Key = "STRIPEKEY";
    public static final String MApURl = "MAPURLData";
    public static final String MApURllive = "MAPURLDatalive";
    //for verificaytion
    public static final String MOBILE_VERIFIED_STATUS="mobileverified";
    public static final String EMAIL_VERIFIED_STATUS="emailverified";
    public static final String FUND_VERIFIED_STATUS="fundverified";
    public static final String IDENTITY_VERIFIED_STATUS="identityverified";
    public static final String IDENTITY_VERIFIED_STATUS_COUNT="identityverifiedcount";
    public static final String VIDEO_VERIFIED_STATUS="videoverified";
    public static final String VIDEO_VERIFIED_STATUS_COUNT="videoverifiedcount";
    public static final String ADDRESS_VERIFIED_STATUS="addressverified";
    //.................
    public static final String GOOGLEAUTHSECRATE="GOOGLEAUTHSECRATE";
    public static final String GOOGLEAUTHOPTIONSTATUS="GOOGLEAUTHOPTION";
    public static final String GOOGLEAUThPendingRegit="GOOGLEAUTHREGISTRStatus";
    public static final String unpraasph = "UnPasspheres";
    public static final String IVPARAM = "IVKEY";
    public static final String KEYENCYPARAM = "KEYENCY";
    public static final String SAMKOIN_ADD = "samkoin_address";
    public static final String SAMKOIN_Secaret = "samkoin_secaret";
    public static final String SAMKOIN_Add_Id = "samkoin_address_id";
    public static final String SAMKOIN_Balance = "samkoin_balance";
    public static final String SAMKOIN_publickey= "samkoin_publickey";
    public static final String USDT_ADD = "usdt_address";
    public static final String USDT_Secaret = "usdt_secaret";
    public static final String USDT_Add_Id = "usdt_address_id";
    public static final String USDT_Balance = "usdt_balance";
    public static final String USDT_publickey = "usdt_publickey";
    public static final String Secret = "Secret";
    public static final String devicetype="android";
    public static final String CurrentBalance = "CurrentWalletBalance";
    public static final String MINIMUM_SUBSCRIPTION="minimun_subscription";
    public static final String MINIMUM_CASHOUT="minimun_cashout";
    public static final String MAXIMUM_CASHOUT="maximum_cashout";
    public static final String MAINDDKAMOUNT="mainddkamount";
    public static final String FACEBOOKURL="facebookurl";
    public static final String Transaction_Receiver="transactionreceiver";
    public static final String Transaction_Receiver_SAMKOIN="transactionreceiversamkoin";
    public static final String Transaction_Receiver_ETH="transactionreceivereth";
    public static final String Transaction_Receiver_BTC="transactionreceiverbtc";
    public static final String Transaction_Receiver_USDT="transactionreceiverusdt";
    public static final String SAMTransactionAddress="sam_trans_address";
    public static final String ETHTranscationAddress="eth_trans_address";
    public static final String BTCTransactionAddress="btc_trans_address";
    public static final String USDTTransactionAddress="usdt_trans_address";
    //..for map receiver
    public static final String Transaction_Receiver_DDK_Map="transactionreceiverddkmap";
    public static final String Transaction_Receiver_SAMKOIN_Map="transactionreceiversamkoinmap";
    public static final String Transaction_Receiver_ETH_Map="transactionreceiverethmap";
    public static final String Transaction_Receiver_BTC_Map="transactionreceiverbtcmap";
    public static final String Transaction_Receiver_USDT_Map="transactionreceiverusdtmap";
    public static final String SAMTransactionAddress_Map="sam_trans_addressmap";
    public static final String ETHTranscationAddress_Map="eth_trans_addressmap";
    public static final String BTCTransactionAddress_Map="btc_trans_addressmap";
    public static final String USDTTransactionAddress_Map="usdt_trans_addressmap";
    //...................
    public static String APP_NAME = "DDK_COMMUNITY";
    public  static String REG_NAME = "DDK_COMMUNITY_REG";
    public static String sellsam_transaction_fees="sellsam_transaction_fees";
    public static String sellsam_transaction_feeskpay="sellsam_transaction_feeskpay";
    public static String sendsam_transaction_fees="sendsam_transaction_fees";
    public static String sendphp_transaction_fees="sendphp_transaction_fees";
    public static String sendbtc_transaction_fees="sendbtc_transaction_fees";
    public static String sendeth_transaction_fees="sendeth_transaction_fees";
    public static String sendusdt_transaction_fees="sendusdt_transaction_fees";
    public static String sendtron_transaction_fees="sendtron_transaction_fees";
    public static String transaction_fees_mode_eth="transaction_fees_mode_eth";
    public static String transaction_fees_mode_usdt="transaction_fees_mode_usdt";
    public static String minimum_samsend_transaction_fee="minimum_samsend_transaction_fee";
    public static String minimum_btcsend_transaction_fee="minimum_btcsend_transaction_fee";
    public static String minimum_ethsend_transaction_fee="minimum_ethsend_transaction_fee";
    public static String minimum_usdtsend_transaction_fee="minimum_usdtsend_transaction_fee";
    public static String minimum_tronsend_transaction_fee="minimum_tronsend_transaction_fee";
    public static String isDoneToManualTrans = "DoneManual";
    public static String senderDDKAddress = "senderDDKAddress";
    public static String totalProcessingAmount = "totalProcessingAmount";

    /*Password : .DDK@123
     *Key password Alias: .DDK@123*/
    /*alias: ddkglobalcommunityalias*/

    public static void hideKeyBoard(Activity activity) {
        if (activity != null) {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(activity.findViewById(android.R.id.content).getWindowToken(), 0);
            }
        }
    }
    public static void showKeyBoard(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
