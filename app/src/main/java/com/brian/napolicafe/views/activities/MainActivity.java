package com.brian.napolicafe.views.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.brian.napolicafe.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import util.FileUtility;

public class MainActivity extends BaseActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Log.i(LOG_TAG, "Log msg as needed");

        // Inflate our layout with the BaseActivity Content ConstraintLayout
        getLayoutInflater().inflate(R.layout.activity_main, contentConstraintLayout);
        ButterKnife.bind(this);

        FileUtility.writeToFile(MainActivity.this, doSomething());

    }

    @OnClick({R.id.btn_directions, R.id.btn_menu, R.id.btn_call_us})
    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_directions:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_call_us:
                dialPhoneNumber(getString(R.string.phn_num));
                break;
            case R.id.btn_menu:
                createNotifications();
                break;
        }
    }

    private void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void createNotifications() {

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, "My Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent resultIntent = new Intent(this, NotificationActivity.class);
        PendingIntent resultPendingIntent = PendingIntent
                .getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        String message = FileUtility.readFromFile(getApplicationContext());

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setWhen(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.speak_bubble2)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("You have a new message")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentText(message)
                .setContentIntent(resultPendingIntent);

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());

    }

    private String doSomething() {
        return "YES simply dummy text of the printing and typesetting industry. Lorem Ipsum has been" +
                " the industry's standard dummy text ever since the 1500s, when an unknown printer " +
                "took a galley of type and scrambled it to make a type specimen book. It has " +
                "survived not only five centuries, but also the leap into electronic typesetting, " +
                "remaining essentially unchanged. It was popularised in the 1960s with the release " +
                "of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop " +
                "publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
    }
}
