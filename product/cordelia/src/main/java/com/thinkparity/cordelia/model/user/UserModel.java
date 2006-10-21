/**
 * 
 */
package com.thinkparity.cordelia.model.user;

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

    public UserModel read() {
        synchronized (getImplLock()) {
            return getImpl().read();
        }
    }
}
