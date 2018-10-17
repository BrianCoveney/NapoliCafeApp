package com.brian.napolicafe;

import com.brian.napolicafe.presenters.IUserPresenter;
import com.brian.napolicafe.presenters.UserPresenterImpl;
import com.brian.napolicafe.repository.IUserRepository;
import com.brian.napolicafe.repository.UserRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    MainApplication mainApplication;

    public PresenterModule(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

    @Provides @Singleton
    public IUserRepository providesUserRepository() {
        return new UserRepositoryImpl();
    }

    @Provides
    public IUserPresenter providesUserPresenter(IUserRepository repository) {
        return new UserPresenterImpl(repository);
    }

}
