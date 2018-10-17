package com.brian.napolicafe;

import com.brian.napolicafe.views.activities.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { PresenterModule.class })
public interface PresenterComponent {
    void inject(MainActivity mainActivity);
}
