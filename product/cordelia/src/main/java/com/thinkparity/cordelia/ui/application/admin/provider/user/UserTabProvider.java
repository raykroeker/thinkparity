/**
 * 
 */
package com.thinkparity.cordelia.ui.application.admin.provider.user;

import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.ui.provider.Provider;

import com.thinkparity.codebase.model.user.User;

/**
 * @author raymond
 *
 */
public class UserTabProvider implements Provider {


    /**
     * Create UserTabProvider.
     * 
     */
    public UserTabProvider() {
        super();
    }

    public List<User> readUsers() {
        return Collections.emptyList();
    }
}
