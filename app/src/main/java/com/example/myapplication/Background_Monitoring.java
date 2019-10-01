package com.example.myapplication;

import android.app.*;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.estimote.proximity_sdk.api.*;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import java.util.List;
import java.util.UUID;


public class Background_Monitoring extends Application
{
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    private BeaconManager beaconManager;
    private BeaconRegion region;


    private NotificationManagerCompat notificationManager;

    Notification notification_Found;
    Notification notification_Lost;

    @Override
    public void onCreate()
    {
        super.onCreate();

        beaconManager = new BeaconManager(this);
        region = new BeaconRegion("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
        createNotificationChannels();

        notificationManager = NotificationManagerCompat.from(this);

        //showNotification("Test", "Hello");


        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener()
        {
            @Override
            public void onEnteredRegion(BeaconRegion region, List<Beacon> beacons)
            {
                //showNotification("Beacon Found", "It is Xcm away");
                notificationManager.notify(1,notification_Found);
            }
            @Override
            public void onExitedRegion(BeaconRegion region)
            {
                //showNotification("Beacon Lost", "");
                notificationManager.notify(2,notification_Lost);
            }
        });


        beaconManager.connect(new BeaconManager.ServiceReadyCallback()
        {
            @Override
            public void onServiceReady()
            {

                beaconManager.startMonitoring(new BeaconRegion("monitored region", UUID.fromString("b9407f30-f5f8-466e-aff9-25556b57fe6d"),29056, 22750));
                beaconManager.startMonitoring(new BeaconRegion("monitored region", UUID.fromString("b9407f30-f5f8-466e-aff9-25556b57fe6d"),8805, 31055));

            }
        });


        Notification notification_Test = new NotificationCompat.Builder(this,CHANNEL_1_ID).setSmallIcon(R.drawable.ic_one).setContentTitle("Test").setContentText("Hello").setPriority(NotificationCompat.PRIORITY_HIGH).build();
        notification_Found = new NotificationCompat.Builder(this,CHANNEL_1_ID).setSmallIcon(R.drawable.ic_one).setContentTitle("Beacon Found").setContentText("Hello").setPriority(NotificationCompat.PRIORITY_HIGH).build();
        notification_Lost = new NotificationCompat.Builder(this,CHANNEL_1_ID).setSmallIcon(R.drawable.ic_one).setContentTitle("Beacon Lost").setContentText("Goodbye").setPriority(NotificationCompat.PRIORITY_HIGH).build();

        //notificationManager.notify(1,notification_Test);
        // notificationManager.notify(1,notification_Found);

    }


    private void createNotificationChannels()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID,"Channel 1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Beacon Notifications 1");


            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID,"Channel 2", NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("Beacon Notifications 2");


            NotificationManager manager = getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }


    public void showNotification(String title, String message)
    {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notification.defaults |= Notification.DEFAULT_SOUND;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

}























































/*package com.example.test_beacons;

import android.app.*;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.EditText;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.estimote.proximity_sdk.api.*;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import java.util.List;
import java.util.UUID;


public class MyApplication extends Application
{
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    private BeaconManager beaconManager;
    private EditText editTextTitle;
    private EditText editTextMessage;

    private NotificationManagerCompat notificationManager;

    Notification notification_Found;
    Notification notification_Lost;

    @Override
    public void onCreate()
    {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());
        createNotificationChannels();

        notificationManager = NotificationManagerCompat.from(this);

        //showNotification("Test", "Hello");


        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener()
        {
            @Override
            public void onEnteredRegion(BeaconRegion region, List<Beacon> beacons)
            {
                //showNotification("Beacon Found", "It is Xcm away");
                notificationManager.notify(1,notification_Found);
            }
            @Override
            public void onExitedRegion(BeaconRegion region)
            {
                //showNotification("Beacon Lost", "");
                notificationManager.notify(2,notification_Lost);
            }
        });


        beaconManager.connect(new BeaconManager.ServiceReadyCallback()
        {
            @Override
            public void onServiceReady()
            {

                beaconManager.startMonitoring(new BeaconRegion("monitored region", UUID.fromString("b9407f30-f5f8-466e-aff9-25556b57fe6d"),29056, 22750));
                beaconManager.startMonitoring(new BeaconRegion("monitored region", UUID.fromString("b9407f30-f5f8-466e-aff9-25556b57fe6d"),8805, 31055));

            }
        });


        Notification notification_Test = new NotificationCompat.Builder(this,CHANNEL_1_ID).setSmallIcon(R.drawable.ic_one).setContentTitle("Test").setContentText("Hello").setPriority(NotificationCompat.PRIORITY_HIGH).build();
        notification_Found = new NotificationCompat.Builder(this,CHANNEL_1_ID).setSmallIcon(R.drawable.ic_one).setContentTitle("Beacon Found").setContentText("Hello").setPriority(NotificationCompat.PRIORITY_HIGH).build();
        notification_Lost = new NotificationCompat.Builder(this,CHANNEL_1_ID).setSmallIcon(R.drawable.ic_one).setContentTitle("Beacon Lost").setContentText("Hello").setPriority(NotificationCompat.PRIORITY_HIGH).build();

        //notificationManager.notify(1,notification_Test);
       // notificationManager.notify(1,notification_Found);




    }

    private void createNotificationChannels()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID,"Channel 1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Beacon Notifications 1");


            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID,"Channel 2", NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("Beacon Notifications 2");


            NotificationManager manager = getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }












    public void showNotification(String title, String message)
    {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notification.defaults |= Notification.DEFAULT_SOUND;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

}
*/
