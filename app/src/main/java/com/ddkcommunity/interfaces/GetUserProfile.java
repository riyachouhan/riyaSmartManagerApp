package com.ddkcommunity.interfaces;

import com.ddkcommunity.model.credential.Credential;
import com.ddkcommunity.model.user.UserResponse;

import java.util.List;

public interface GetUserProfile {
    public void getUserInfo(UserResponse userResponse);
}
