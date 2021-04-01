package com.ddkcommunity.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ddkcommunity.App;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.fragment.HomeFragment;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ReceivedActivity extends AppCompatActivity {

    public final static int QRcodeWidth = 400;
    private Bitmap bitmap;
    private CallbackManager callbackManager;
    private ImageView ivQrCode;
    private Context mContext;
    private static final String IMAGE_DIRECTORY = "/ddkpay";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private TextView header_view,sch_txt,tvCopyPhoneNumber, tvWalletAddressCopy,tvText1;
    private ClipboardManager clipboard;
    private ClipData clip;
    //    private ShareDialog shareDialog;
    private ProgressDialog pDialog;
    ShareDialog shareDialog;
    String clickAddressname;
    private UserResponse userData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        mContext = this;
        userData = AppConfig.getUserData(mContext);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        pDialog = new ProgressDialog(mContext);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        sch_txt=findViewById(R.id.sch_txt);
        header_view=findViewById(R.id.header_view);
        tvText1=findViewById(R.id.tvText1);
        ivQrCode = findViewById(R.id.ivQrCode);
        tvCopyPhoneNumber = findViewById(R.id.tvCopyPhoneNumber);
        tvWalletAddressCopy = findViewById(R.id.tvWalletAddressCopy);
        findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.tvCopyPhoneNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                clip = ClipData.newPlainText("Copy", tvCopyPhoneNumber.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
                AppConfig.showToast("Copied");
            }
        });
        findViewById(R.id.tvWalletAddressCopy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                clip = ClipData.newPlainText("Copy", tvWalletAddressCopy.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
                AppConfig.showToast("Copied");
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission(); // Code for permission
            }
        }

        try {
            String curkj= App.pref.getString(Constant.PHP_Functionality_View, "");
            String countrydata=userData.getUser().country.get(0).country;
            if(countrydata!=null && (countrydata.equalsIgnoreCase("philippines") && curkj.equalsIgnoreCase("true")))
            {
                if(HomeFragment.tabclickevent==1)
                {
                    clickAddressname=" PHP ";
                    String userEmail =App.pref.getString(Constant.USER_EMAIL, "");
                    tvWalletAddressCopy.setText(userEmail);
                    bitmap = TextToImageEncode(userEmail);

                }else if(HomeFragment.tabclickevent==2)
                {
                    clickAddressname=" KOIN ";
                    tvWalletAddressCopy.setText(App.pref.getString(Constant.SAMKOIN_ADD, ""));
                    bitmap = TextToImageEncode(App.pref.getString(Constant.SAMKOIN_ADD, ""));

                }else if(HomeFragment.tabclickevent==6)
                {
                    clickAddressname=" DDK ";
                    tvWalletAddressCopy.setText(App.pref.getString(Constant.Wallet_ADD, ""));
                    bitmap = TextToImageEncode(App.pref.getString(Constant.Wallet_ADD, ""));

                }else if(HomeFragment.tabclickevent==3)
                {
                    clickAddressname=" BTC ";
                    tvWalletAddressCopy.setText(App.pref.getString(Constant.BTC_ADD, ""));
                    bitmap = TextToImageEncode(App.pref.getString(Constant.BTC_ADD, ""));

                }/*else if(HomeFragment.tabclickevent==4)
                {
                    clickAddressname=" TRON ";
                    tvWalletAddressCopy.setText(App.pref.getString(Constant.BTC_ADD, ""));
                    bitmap = TextToImageEncode(App.pref.getString(Constant.BTC_ADD, ""));

                }*/else if(HomeFragment.tabclickevent==4)
                {
                    clickAddressname=" ETH ";
                    tvWalletAddressCopy.setText(App.pref.getString(Constant.Eth_ADD, ""));
                    bitmap = TextToImageEncode(App.pref.getString(Constant.Eth_ADD, ""));

                }else if(HomeFragment.tabclickevent==5)
                {
                    clickAddressname=" USDT ";
                    tvWalletAddressCopy.setText(App.pref.getString(Constant.USDT_ADD, ""));
                    bitmap = TextToImageEncode(App.pref.getString(Constant.USDT_ADD, ""));
                }
            }else
            {
                if(HomeFragment.tabclickevent==1)
                {
                    clickAddressname=" KOIN ";
                    tvWalletAddressCopy.setText(App.pref.getString(Constant.SAMKOIN_ADD, ""));
                    bitmap = TextToImageEncode(App.pref.getString(Constant.SAMKOIN_ADD, ""));

                }else if(HomeFragment.tabclickevent==5)
                {
                    clickAddressname=" DDK ";
                    tvWalletAddressCopy.setText(App.pref.getString(Constant.Wallet_ADD, ""));
                    bitmap = TextToImageEncode(App.pref.getString(Constant.Wallet_ADD, ""));

                }else if(HomeFragment.tabclickevent==2)
                {
                    clickAddressname=" BTC ";
                    tvWalletAddressCopy.setText(App.pref.getString(Constant.BTC_ADD, ""));
                    bitmap = TextToImageEncode(App.pref.getString(Constant.BTC_ADD, ""));

                }/*else if(HomeFragment.tabclickevent==3)
                {
                    clickAddressname=" TRON ";
                    tvWalletAddressCopy.setText(App.pref.getString(Constant.BTC_ADD, ""));
                    bitmap = TextToImageEncode(App.pref.getString(Constant.BTC_ADD, ""));

                }*/else if(HomeFragment.tabclickevent==3)
                {
                    clickAddressname=" ETH ";
                    tvWalletAddressCopy.setText(App.pref.getString(Constant.Eth_ADD, ""));
                    bitmap = TextToImageEncode(App.pref.getString(Constant.Eth_ADD, ""));

                }else if(HomeFragment.tabclickevent==4)
                {
                    clickAddressname=" USDT ";
                    tvWalletAddressCopy.setText(App.pref.getString(Constant.USDT_ADD, ""));
                    bitmap = TextToImageEncode(App.pref.getString(Constant.USDT_ADD, ""));
                }
            }

            header_view.setText(getResources().getString(R.string.toclickfirst)+clickAddressname+getResources().getString(R.string.toclicksecond));
            tvText1.setText(getResources().getString(R.string.toclickfirst)+clickAddressname+getResources().getString(R.string.othersecondhint));
            sch_txt.setText(getResources().getString(R.string.receivehint)+clickAddressname);
            ivQrCode.setImageBitmap(bitmap);
            if(bitmap!=null) {
                String path = saveImage(bitmap);  //give read write permission
            }//Toast.makeText(context, "QRCode saved to -> "+path, Toast.LENGTH_SHORT).show();
        } catch (WriterException e)
        {
            e.printStackTrace();
        }
        findViewById(R.id.btnShareQrCode).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                // Uri uri = Uri.fromFile(new File("file:///storage/emulated/0/ddkpay/qr_code.jpg"));
                try {
                    new AsyncTask<String, String, File>()
                    {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        Bitmap bitmap;
                        String path;
                        File casted_image;
                        int a;
                        Uri staticuri;
                        Uri uri;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            AppConfig.showLoading(pDialog, "please wait...");
                        }

                        @Override
                        protected File doInBackground(String... params) {

                            String s = null;
                            //s = "file:///storage/emulated/0/ddkpay/qr_code.jpg";

                            try {

                                if (AppConfig.isStringNullOrBlank(s)) {

                                    staticuri = Uri.parse(("file:///storage/emulated/0/ddkpay/qr_code.jpg"));

                                } else {
                                    shareIntent.setType("image/*");
                                    // File rootSdDirectory = Environment.getExternalStorageDirectory();
                                    String rootSdDirectory = Environment.getExternalStorageDirectory()
                                            + File.separator + "ddkpay" + File.separator;

                                    casted_image = new File(rootSdDirectory, "qr_code.jpg");
                                    if (casted_image.exists()) {
                                        casted_image.delete();
                                    }
                                    casted_image.createNewFile();

                                    FileOutputStream fos = new FileOutputStream(casted_image);
                                    URL url;

                                    s = s.replace(" ", "%20");
                                    url = new URL(s);

                                    HttpURLConnection connection = (HttpURLConnection) url
                                            .openConnection();
                                    connection.setRequestMethod("GET");
                                    connection.setDoOutput(true);
                                    connection.connect();
                                    InputStream in = connection.getInputStream();

                                    byte[] buffer = new byte[1024];
                                    int size = 0;
                                    while ((size = in.read(buffer)) > 0) {
                                        fos.write(buffer, 0, size);
                                    }
                                    fos.close();
                                }
                            } catch (FileNotFoundException e) {
                                staticuri = Uri.parse(("file:///storage/emulated/0/ddkpay/qr_code.jpg"));
                                e.printStackTrace();
                            } catch (IOException e) {

                                e.printStackTrace();
                            }

                            return casted_image;
                        }

                        @Override
                        protected void onPostExecute(File result) {
                            super.onPostExecute(result);
                            AppConfig.hideLoading(pDialog);
  							/*path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
  	                                bitmap, "Image Description.jpeg", "images");*/
                            if (casted_image != null) {
                                uri = Uri.fromFile(casted_image);
                            } else {
                                uri = staticuri;
                            }

                            if (AppConfig.isAppInstalled(mContext, "com.facebook")) {
                                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                        .setQuote(clickAddressname+"Global Community App")
                                        .setContentDescription("I'm inviting you to join"+clickAddressname +"Global Community, to Join Online Wallet.")
                                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.ddkcommunity&hl=en"))
                                        .build();
                                if (ShareDialog.canShow(ShareLinkContent.class)) {
                                    shareDialog.show(linkContent);
                                }
                            } else {
                                String addressdata=tvWalletAddressCopy.getText().toString();
                                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "Smart Assest Managers App"+" Global Community App.\nMy "+clickAddressname+"Address\n" + addressdata);
                                shareIntent.setType("image/*");
                                startActivity(Intent.createChooser(shareIntent, "Share image using"));
                            }
                        }
                    }.execute();

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

        });

    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, "qr_code" + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(mContext,
                    new String[]{f.getPath()},
                    new String[]{"image/jpg"}, null);
            fo.close();
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {
           Illegalargumentexception.printStackTrace();
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 400, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
        } else {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                break;
        }
    }


}
