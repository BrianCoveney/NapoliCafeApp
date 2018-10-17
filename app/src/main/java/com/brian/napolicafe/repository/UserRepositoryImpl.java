package com.brian.napolicafe.repository;

import com.brian.napolicafe.models.User;

import javax.inject.Inject;

import io.realm.Realm;

public class UserRepositoryImpl implements IUserRepository {

    @Inject
    public UserRepositoryImpl() {
    }

    @Override
    public void saveUser(final User user) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(user);
                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }
    }

    @Override
    public User getUser() {
        return null;
    }
}
