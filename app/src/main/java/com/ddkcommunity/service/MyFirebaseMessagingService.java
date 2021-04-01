package com.ddkcommunity.service;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.activities.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONObject;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    Context ctx;
    int i = 0;
    int notification_count=0;
    int CHANNEL_ID=0;
    JSONObject j;
    String title="";
    String currentDateandTime;
    private boolean foregroundValue = false;
    private boolean isNotificationShowed = false;
    RemoteMessage.Builder builder1;
    String ride_status="";
    String notification_type = "";
    String driver_id = "";
    String username = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        final Map<String, String> data = remoteMessage.getData();
        System.out.println("====rider=="+remoteMessage.getData());
        System.out.println("====rider=="+remoteMessage.getData());
        /*Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                try
                {
                    String strMain = data.toString();
                    String[] arrSplit = strMain.split("=");
                    String dataStr = arrSplit[1];
                    JSONObject jsonObject = new JSONObject(dataStr);
                  try
                  {
                      notification_type = jsonObject.optString("notification_type");

                  }
                  catch (Exception c)
                  {

                  }

                    if (notification_type.equalsIgnoreCase("chat"))
                    {
                        driver_id = jsonObject.optString("driver_id");
                        username = jsonObject.optString("driver_name");
                        title = jsonObject.optString("message");
                     //   Constant.user_id = jsonObject.optString("sender_id");
                      //  com.ddkcommunity.rider.Constant.driver_id = jsonObject.optString("sender_id");
                        share = SharedPreference.getInstance(getApplicationContext());
                        share.setValue("chat_user_id",driver_id);
                        share.setValue("chat_username",username);
                        share.setValue("chat_activity","false");
                        share.setValue(com.ddkcommunity.rider.Constant.driver_id_key, driver_id);
                        try
                        {
                            Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception c)
                        {
                        }
                    }
                    else
                    {
                        String ride_id = jsonObject.optString("ride_id");
                        String driver_id = jsonObject.optString("driver_id");
                        String profile_img = jsonObject.optString("profile_img");
                        String name = jsonObject.optString("name");
                        String mobile = jsonObject.optString("mobile");
                        String rating = jsonObject.optString("rating");
                        ride_status = jsonObject.optString("ride_status");
                        title = jsonObject.optString("title");
                        String messageStr = jsonObject.optString("message");

                        try
                        {
                            Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();

                        }
                        catch (Exception c)
                        {

                        }





                        System.out.println("ride status======"+ride_status);

                        share = SharedPreference.getInstance(getApplicationContext());
                     //   share.setValue(com.ddkcommunity.rider.Constant.ride_id_key,ride_id);
                        share.setValue(com.ddkcommunity.rider.Constant.driver_id_key,driver_id);
                        share.setValue(com.ddkcommunity.rider.Constant.d_profile,profile_img);
                        share.setValue(com.ddkcommunity.rider.Constant.driver_name_key,name);
                        share.setValue(com.ddkcommunity.rider.Constant.d_mobile,mobile);
                        share.setValue(com.ddkcommunity.rider.Constant.driver_rating_key,rating);
                        share.setValue(com.ddkcommunity.rider.Constant.ride_status_key,ride_status);
                        share.setValue(com.ddkcommunity.rider.Constant.ride_title_key,title);
                    }

                  //  Toast.makeText(ctx, ""+mess, Toast.LENGTH_SHORT).show();
                }
                catch (Exception x)
                {
                    x.printStackTrace();
                }
                Random rnd = new Random();
                i=100 + rnd.nextInt(90000);
                ctx = getApplicationContext();
                SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(ctx);
                Constant.user_id = settings1.getString(Constant.USER_ID, "");
                try
                {
                    notification_count  = Integer.parseInt(settings1.getString("notification", "0"));
                }
                catch (Exception c)
                {
                    c.printStackTrace();
                }
                share.setValue(com.ddkcommunity.rider.Constant.ride_status_key,ride_status);
                SharedPreference sharedPreference = SharedPreference.getInstance(getApplicationContext());
                ride_status = sharedPreference.getValue(com.ddkcommunity.rider.Constant.ride_status_key);
                Intent in  = null;
                if(notification_type.equalsIgnoreCase("chat")) {
                   in = new Intent(getApplicationContext(), ChatActivity.class);
                                  }
                else

                if (ride_status.equalsIgnoreCase(""))
                {
                     in = new Intent(getApplicationContext(), MapsActivity.class);
                }
               else
                if (ride_status.equalsIgnoreCase(com.ddkcommunity.rider.Constant.Completed))
                {
                     in = new Intent(getApplicationContext(), BillActivity.class);
                }
                 else
                {
                     in = new Intent(getApplicationContext(), GoogleMapWithBottomSheet.class);

                }

                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, in, 0);
                String CHANNEL_ID = "my_channel_01";// The id of the channel.
                CharSequence name = "My Channel";// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                }
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Notification notification;
                notification = mBuilder
                        .setAutoCancel(true)
                        .setContentTitle("SAM Rider")
                        .setContentIntent(pIntent)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSmallIcon(R.drawable.ic_launcher_app_logo)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_app_logo))
                        .setContentText("")
                        .setChannelId(CHANNEL_ID)
                        .build();

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(mChannel);
                }
                notificationManager.notify(0, notification);

            }
        });
*/
        try {
            Intent in = new Intent("NotificationPushReceived");
            sendBroadcast(in);
        } catch (Exception cv) {
            cv.printStackTrace();
        }

    }

}
