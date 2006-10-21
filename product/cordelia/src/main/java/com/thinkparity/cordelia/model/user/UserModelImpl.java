/**
 * 
 */
package com.thinkparity.cordelia.model.user;

import java.util.List;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.cordelia.model.CordeliaModelImpl;

/**
 * @author raymond
 *
 */
final class UserModelImpl extends CordeliaModelImpl {

    /**
     * 
     */
    UserModelImpl() {
        super();
    }

    List<User> read() {
        logger.logApiId();
        try {
            
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }
}
