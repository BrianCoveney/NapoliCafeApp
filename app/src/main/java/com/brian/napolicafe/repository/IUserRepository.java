package com.brian.napolicafe.repository;

import com.brian.napolicafe.models.User;

public interface IUserRepository {
    void saveUser(User user);
    User getUser();
}
