package com.brian.napolicafe.presenters;

import com.brian.napolicafe.models.User;
import com.brian.napolicafe.repository.IUserRepository;
import com.brian.napolicafe.views.IUserView;

import javax.inject.Inject;

public class UserPresenterImpl implements IUserPresenter {

    private IUserView view;
    private IUserRepository repository;

    @Inject
    public UserPresenterImpl(IUserRepository userRepository) {
        this.repository = userRepository;
    }

    @Override
    public void setView(IUserView userView) {
        this.view = userView;
        loadUserDetails();
    }

    @Override
    public void saveUser(User user) {
        user = new User();
        user.setUserName(view.getUserName());
        user.setEmail(view.getUserEmail());

        repository.saveUser(user);

        view.showUserSavedMessage();
    }

    @Override
    public void loadUserDetails() {
        User user = new User();
        user.setUserName("name");
        user.setEmail("email");
    }

}
