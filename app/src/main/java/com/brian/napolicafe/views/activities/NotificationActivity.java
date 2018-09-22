package com.brian.napolicafe.views.activities;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.widget.TextView;

import com.brian.napolicafe.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import util.FileUtility;

public class NotificationActivity extends Activity {

    private static final String LOG_TAG = Notification.class.getSimpleName();

    @BindView(R.id.tv_notification)
    TextView notificationResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);

        notificationResult.setText(FileUtility.readFromFile(this));
    }
}
