package com.ddkcommunity.utilies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.LoadInterface;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.SplashActivity;
import com.ddkcommunity.model.user.UserResponse;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by pintu22 on 17/11/17.
 */

public class AppConfig {

  private final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
          "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                  "\\@" +
                  "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                  "(" +
                  "\\." +
                  "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                  ")+"
  );
  public static String USER_ID = "user_id";
  public static String STATUS = "status";
  public static String isPin = "isPin";
  private static Retrofit retrofit = null,retrofitmap=null;
  private static LoadInterface loadInterface = null,loadInterfacemap=null;
//    private static String PREF_UNIQUE_ID="UUID_ID";

  public static Retrofit getClient() {
    if (retrofit == null)
    {
      retrofit = new Retrofit.Builder()
              .baseUrl(Constant.BASE_URL)
              .client(getSafeClient())
              .addConverterFactory(GsonConverterFactory.create())
              .build();
    }
    return retrofit;
  }

    public static Retrofit getClientpayout() {
       // if (retrofitmap == null)
        {
          String mapurl= App.pref.getString(Constant.MApURllive,"");
          retrofitmap = new Retrofit.Builder()
                    .baseUrl(mapurl)
                    .client(getSafeClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitmap;
    }

  private static OkHttpClient getSafeClient() {
    try {
      X509TrustManager trustAllCerts = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
          return new X509Certificate[0];
        }
      };

      SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, new TrustManager[]{trustAllCerts}, null);
      SSLSocketFactory socketFactory = sslContext.getSocketFactory();
      OkHttpClient.Builder builder = new OkHttpClient.Builder();
      builder.sslSocketFactory(socketFactory, trustAllCerts);
      builder.hostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      }).connectTimeout(100, TimeUnit.SECONDS)
              .writeTimeout(100, TimeUnit.SECONDS)
              .readTimeout(100, TimeUnit.SECONDS).build();
      return builder.build();
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      throw new RuntimeException(e);
    }
  }

  public static LoadInterface getLoadInterface() {
    if (loadInterface == null) {
      loadInterface = AppConfig.getClient().create(LoadInterface.class);
    }
    return loadInterface;
  }

  public static LoadInterface getLoadInterfaceMap() {
   // if (loadInterfacemap == null)
    {
      loadInterfacemap = AppConfig.getClientpayout().create(LoadInterface.class);
    }
    return loadInterfacemap;
  }

  public static void setUserData(Context mContext, UserResponse value) {
    SharedPreferences pref = mContext.getSharedPreferences(Constant.APP_NAME, MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();
    Gson gson = new Gson();
    String data = gson.toJson(value);
    editor.putString("userData", data);
    editor.apply();
  }

  public static void setStringPreferences(Context mContext, String key, String value) {
    SharedPreferences pref = mContext.getSharedPreferences(Constant.APP_NAME, MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();
    editor.putString(key, value);
    editor.apply();
  }

  public static String getStringPreferences(Context mContext, String key) {
    SharedPreferences pref = mContext.getSharedPreferences(Constant.APP_NAME, MODE_PRIVATE);
    return pref.getString(key, "");
  }

  public static UserResponse getUserData(Context mContext) {
    SharedPreferences pref = mContext.getSharedPreferences(Constant.APP_NAME, MODE_PRIVATE);
    Gson gson = new Gson();
    String json = pref.getString("userData", null);
    Type type = new TypeToken<UserResponse>() {
    }.getType();
    return gson.fromJson(json, type);
  }

  public static void showToast(String s) {
    Toast.makeText(App.getInstance(), s, Toast.LENGTH_SHORT).show();
  }

  public static void showLongToast(String s) {
    Toast.makeText(App.getInstance(), s, Toast.LENGTH_LONG).show();
  }

  public static boolean checkDate(String start, String end) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date strDate = null, endDate = null;
    try {
      strDate = sdf.parse(start);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    try {
      endDate = sdf.parse(end);
    } catch (ParseException e) {
      e.printStackTrace();
    }


    return endDate.getTime() > strDate.getTime();
  }


  public static ProgressDialog showLoading(ProgressDialog progress, String msg) {
    try {
      progress.setMessage(msg);
      progress.setCancelable(false);
      progress.setIndeterminate(true);
      progress.setCanceledOnTouchOutside(false);
      progress.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return progress;
  }

  private static ProgressDialog progressLoad;
  public static void showLoading(String msg, Context mContext) {
    try {
      progressLoad = new ProgressDialog(mContext);
      progressLoad.setMessage(msg);
      progressLoad.setCancelable(false);
      progressLoad.setIndeterminate(true);
      progressLoad.setCanceledOnTouchOutside(false);
      progressLoad.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void  hideLoader(){
    if (progressLoad!=null){
      progressLoad.dismiss();
    }
  }

  public static void ShowAlertDialog(Context context, String msg) {
    MaterialAlertDialogBuilder builder1 = new MaterialAlertDialogBuilder(context, R.style.LightDialogTheme);
    builder1.setMessage(msg);
    builder1.setCancelable(true);
    builder1.setPositiveButton("Ok",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
              }
            });

    AlertDialog alert11 = builder1.create();
    alert11.show();
  }


  public static void hideLoading(ProgressDialog progress) {
    try {
      if (progress != null) {
        if (progress.isShowing()) {
          progress.dismiss();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public static boolean isInternetOn() {
    ConnectivityManager connectivity = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivity != null) {
      NetworkInfo[] info = connectivity.getAllNetworkInfo();
      if (info != null) {
        for (int i = 0; i < info.length; i++) {
          Log.w("INTERNET:", String.valueOf(i));
          if (info[i].getState() == NetworkInfo.State.CONNECTED) {
            Log.w("INTERNET:", "connected!");
            return true;
          }
        }
      }
    }
    return false;

  }

  public static void hideKeyboard(Activity activity) {
    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    //Find the currently focused view, so we can grab the correct window token from it.
    View view = activity.getCurrentFocus();
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
      view = new View(activity);
    }
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  public static boolean isStringNullOrBlank(String str) {
    if (str == null) {
      return true;
    } else return str.equals("null") || str.equals("");
  }

  public static boolean isEmail(String email) {
    return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
  }

  public static RequestBody setRequestBody(String param) {
    Log.d("Parameter: ", param);
    return RequestBody.create(MediaType.parse("multipart/form-data"), param);
  }

  public static String changeDateInDayMonth(String date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date sourceDate = null;
    try {
      sourceDate = dateFormat.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd");
    return targetFormat.format(sourceDate);
  }

  public static String changeDateInDayAboveMonth(String date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date sourceDate = null;
    try {
      sourceDate = dateFormat.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    SimpleDateFormat targetFormat = new SimpleDateFormat("dd\nMMM");
    return targetFormat.format(sourceDate);
  }

  public static String changeDateInMonth(String date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date sourceDate = null;
    try {
      sourceDate = dateFormat.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
    return targetFormat.format(sourceDate);
  }

  public static String dateFormat(String date, String sourceFormat) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(sourceFormat);
    Date sourceDate = null;
    try {
      sourceDate = dateFormat.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
    return targetFormat.format(sourceDate);
  }

  public static String changeTimeInHour(String time) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date sourceDate = null;
    try {
      sourceDate = dateFormat.parse(time);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    SimpleDateFormat targetFormat = new SimpleDateFormat("HH:mm a");
    return targetFormat.format(sourceDate);
  }

  public static String stringToBase64(String str) {
    byte[] data = new byte[0];
    try {
      data = str.getBytes(StandardCharsets.UTF_8);

      String base64 = Base64.encodeToString(data, Base64.DEFAULT);
      String urlEncoded = URLEncoder.encode(base64, "UTF-8");
      return urlEncoded;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void copyAndShareLink(final String s, final String url, final String title, final Activity activity) {
    copyShare(s, url, title, activity);
  }

  public static void copy(final String s, final String url, final String title, final Activity activity) {
    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(activity);
    alertDialogBuilder.setTitle(title).setMessage(s + url)
            .setNegativeButton("Copy", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(title, url);
                if (clipboard != null) {
                  clipboard.setPrimaryClip(clip);
                }
                showToast("Copied");
              }
            }).show();
  }

  public static void copyPass(final String url, final String title, final Activity activity) {

    ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = ClipData.newPlainText(title, url);
    if (clipboard != null) {
      clipboard.setPrimaryClip(clip);
    }
    showToast("Copied");

  }

  public static void copyShare(final String sub, final String url, final String title, final Activity activity) {

    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);

    LayoutInflater inflater = LayoutInflater.from(activity);
    final View alertView = inflater.inflate(R.layout.dialog_share, null);
    builder.setView(alertView);
    final AlertDialog myDialog = builder.create();
    TextView titleTV = alertView.findViewById(R.id.title);
    TextView message = alertView.findViewById(R.id.message);
    TextView copy = alertView.findViewById(R.id.copy_IV);
    TextView share = alertView.findViewById(R.id.share_IV);
    titleTV.setText(title);
    String sourceString;
    sourceString =url;
    message.setText(sourceString);
    share.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, url);
        activity.startActivity(Intent.createChooser(sharingIntent, title));
        myDialog.dismiss();
      }
    });
    copy.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(title, url);
        clipboard.setPrimaryClip(clip);
        showToast("Copied");
        myDialog.dismiss();
      }
    });
    builder.setCancelable(false);

    myDialog.show();

  }

  /**********************GET YOUTUBE VIDEO ID FROM URL**************************/

  public static String getYoutubeVideoIdFromUrl(String inUrl) {
    if (inUrl.toLowerCase().contains("youtu.be")) {
      return inUrl.substring(inUrl.lastIndexOf("/") + 1);
    }
    String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
    Pattern compiledPattern = Pattern.compile(pattern);
    Matcher matcher = compiledPattern.matcher(inUrl);
    if (matcher.find()) {
      return matcher.group();
    }
    return null;
  }

  public static String convert(String str) {
    char ch[] = str.toCharArray();
    for (int i = 0; i < str.length(); i++) {
      if (i == 0 && ch[i] != ' ' ||
              ch[i] != ' ' && ch[i - 1] == ' ') {
        if (ch[i] >= 'a' && ch[i] <= 'z') {
          ch[i] = (char) (ch[i] - 'a' + 'A');
        }
      } else if (ch[i] >= 'A' && ch[i] <= 'Z')
        ch[i] = (char) (ch[i] + 'a' - 'A');
    }
    return new String(ch);
  }

  /**********************SET REGULAR FONT********************************/
  public static void setRegularFont(TextView view, Context context) {
    Typeface font = Typeface.createFromAsset(
            context.getAssets(),
            "fonts/Montserrat-Regular.ttf");
    view.setTypeface(font);
  }

  /**********************SET BOLD FONT********************************/
  public static void setBoldFont(TextView view, Context context) {
    Typeface font = Typeface.createFromAsset(
            context.getAssets(),
            "fonts/Montserrat-Bold.ttf");
    view.setTypeface(font);
  }

  /**********************SET ITALIC FONT********************************/
  public static void setItalicFont(TextView view, Context context) {
    Typeface font = Typeface.createFromAsset(
            context.getAssets(),
            "fonts/Montserrat-Italic.ttf");
    view.setTypeface(font);
  }

  /**********************OPEN BROWSER********************************/
  public static void openBrowser(Activity activity, String url) {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    activity.startActivity(browserIntent);
  }

  public static boolean isAppInstalled(Context context, String packageName) {
    try {
      context.getPackageManager().getApplicationInfo(packageName, 0);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
  }

  public static SpannableStringBuilder SpannableStringBuilder(final String text, final char afterChar, final float reduceBy) {
    RelativeSizeSpan smallSizeText = new RelativeSizeSpan(reduceBy);
    SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);
    ssBuilder.setSpan(
            smallSizeText,
            text.indexOf(afterChar),
            text.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    );

    return ssBuilder;
  }

  public static String setStringNullOrBlank(String str) {
    if (str == null) {
      return "-";
    } else if (str.equals("null") || str.equals("")) {
      return "-";
    }
    return str;
  }

  public static String setStringNullBlank(String str) {
    if (str == null) {
      return "";
    } else if (str.equals("null") || str.equals("")) {
      return "";
    }
    return str;
  }

  public static final String AUTHORITY = "com.ddkcommunity";

  public static void copy(InputStream in, File dst) throws IOException {
    FileOutputStream out = new FileOutputStream(dst);
    byte[] buf = new byte[1024];
    int len;

    while ((len = in.read(buf)) > 0) {
      out.write(buf, 0, len);
    }

    in.close();
    out.close();
  }

  public static void openOkDialog1(Context mContext) {
    LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View customView = layoutInflater.inflate(R.layout.popup, null);

    TextView closePopupBtn = (TextView) customView.findViewById(R.id.btnClose);
    TextView tvMsg = (TextView) customView.findViewById(R.id.tvMsg);
    closePopupBtn.setText("Okay");
    MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(mContext);
    alert.setView(customView);

    tvMsg.setText("This features is in current development Temporary for demo purposes only.");
    final AlertDialog dialog = alert.create();
    dialog.show();
    dialog.setCancelable(false);
    closePopupBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
  }


  public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  public static Random RANDOM = new Random();
  public static String PREF_UNIQUE_ID = "UUID_ID";

  public static String randomString(int len) {
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
    }
    return sb.toString();
  }


  public synchronized static String getUUIDDeviceID(Context context) {
    SharedPreferences sharedPrefs = context.getSharedPreferences(
            PREF_UNIQUE_ID, Context.MODE_PRIVATE);
    String uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
    if (uniqueID == null || uniqueID.isEmpty()) {
      uniqueID = randomString(32);
      SharedPreferences.Editor editor = sharedPrefs.edit();
      editor.putString(PREF_UNIQUE_ID, uniqueID);
      editor.commit();
    }
    return uniqueID;
  }

  public static void openSplashActivity(Activity mContext) {

    SharedPreferences sharedPrefs = mContext.getSharedPreferences(
            PREF_UNIQUE_ID, Context.MODE_PRIVATE);
    String uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);

    App.editor.clear();
    App.editor.commit();

    if (uniqueID != null && !uniqueID.isEmpty()) {
      SharedPreferences.Editor editor = sharedPrefs.edit();
      editor.putString(PREF_UNIQUE_ID, uniqueID);
      editor.commit();
    }

    mContext.startActivity(new Intent(mContext, SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    mContext.finish();
  }

  public static BigDecimal performBigDecimalDivisionWithoutScaleOrRounding(
          final BigDecimal dividend, final BigDecimal divisor)
  {
    BigDecimal quotient = null;
    try
    {
      quotient = dividend.divide(divisor);
    }
    catch(ArithmeticException arithEx)
    {
//            out.print(dividend.toPlainString() + "/" + divisor.toPlainString() + ": ");
//            out.println("leads to this exception: ");
//            out.println("\t" + arithEx.toString());

      quotient = dividend.divide(divisor,new MathContext(4));
    }
    return quotient;
  }

}
