package com.brian.napolicafe;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application {
    private PresenterComponent presenterComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        presenterComponent = DaggerPresenterComponent.builder()
                .presenterModule(new PresenterModule(this))
                .build();
    }

    public PresenterComponent getPresenterComponent() {
        return presenterComponent;
    }
}
