package com.brian.napolicafe.presenters;

import com.brian.napolicafe.models.User;
import com.brian.napolicafe.views.IUserView;

public interface IUserPresenter {
    void setView(IUserView userView);
    void saveUser(User user);
    void loadUserDetails();
}
