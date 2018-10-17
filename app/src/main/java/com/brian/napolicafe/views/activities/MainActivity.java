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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.brian.napolicafe.MainApplication;
import com.brian.napolicafe.R;
import com.brian.napolicafe.models.User;
import com.brian.napolicafe.presenters.IUserPresenter;
import com.brian.napolicafe.views.IUserView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.FileUtility;

public class MainActivity extends BaseActivity implements IUserView {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private CallbackManager callbackManager;
    private ProfilePictureView profilePictureView;

    private String name;
    private String email;

    @Inject
    IUserPresenter presenter;

    @BindView(R.id.fb_login_button)
    LoginButton facebookLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) getApplication()).getPresenterComponent().inject(this);

        // Inflate our layout with the BaseActivity Content ConstraintLayout
        getLayoutInflater().inflate(R.layout.activity_main, contentConstraintLayout);
        ButterKnife.bind(this);

        FileUtility.writeToFile(MainActivity.this, doSomething());

        initializeFacebookLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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

    private void initializeFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setReadPermissions(
                Arrays.asList("public_profile", "email"));

        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                try {
                                    String id = object.getString(getString(R.string.user_id));
                                    name = object.getString(getString(R.string.user_name));
                                    email = object.getString(getString(R.string.user_email));

                                    TextView userName = findViewById(R.id.user_name_tv);
                                    TextView userEmail = findViewById(R.id.user_email_tv);
                                    userName.setText(name);
                                    userEmail.setText(email);
                                    profilePictureView = findViewById(R.id.facebookUser);
                                    profilePictureView.setProfileId(id);
                                    profilePictureView.setPresetSize(ProfilePictureView.SMALL);

                                    User user = new User();
                                    user.setUserName(name);
                                    user.setEmail(email);
                                    presenter.saveUser(user);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                 Log.i(LOG_TAG, "Facebook login ERROR: " + error.toString());

            }
        });

    }

    // The 'name' and 'email' are retrieved from the initializeFacebookLogin() onSuccess() callback
    @Override
    public String getUserName() {
        return name;
    }

    @Override
    public String getUserEmail() {
        return email;
    }

    @Override
    public void showUserSavedMessage() {
        showLongToast("Saved user: " + name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
    }
}
