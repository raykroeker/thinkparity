/**
 * 
 */
package com.thinkparity.cordelia.ui.application.admin.provider.user;

import java.util.List;

import com.thinkparity.codebase.ui.provider.Provider;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.user.UserModel;

import com.thinkparity.cordelia.ui.application.admin.AdminApplication;

/**
 * @author raymond
 *
 */
public class UserTabProvider implements Provider {

    private final UserModel userModel;

    /**
     * Create UserTabProvider.
     * 
     */
    public UserTabProvider() {
        super();
        this.userModel = AdminApplication.getInstance().getUserModel();
    }

    public List<User> readUsers() {
        return userModel.read();
    }
}
