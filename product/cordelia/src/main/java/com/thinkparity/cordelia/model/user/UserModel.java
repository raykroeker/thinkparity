/**
 * 
 */
package com.thinkparity.cordelia.model.user;

import java.util.List;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.cordelia.model.CordeliaModel;

/**
 * @author raymond
 *
 */
public class UserModel extends CordeliaModel<UserModelImpl> {

    public static UserModel getModel() {
        return new UserModel();
    }

    /**
     * Create UserModel.
     * 
     */
    private UserModel() {
        super(new UserModelImpl());
    }

    public List<User> read() {
        synchronized (getImplLock()) {
            return getImpl().read();
        }
    }
}
