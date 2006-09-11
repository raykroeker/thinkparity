/*
 * Created On: Mar 1, 2006
 */
package com.thinkparity.desdemona.model.user;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.session.Session;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UserModel extends AbstractModel {

	/**
	 * Obtain a user model.
	 * 
	 * @param session
	 *            The user's session.
	 * @return A user model.
	 */
	public static UserModel getModel(final Session session) {
		final UserModel userModel = new UserModel(session);
		return userModel;
	}

	/**
	 * The implementation.
	 * 
	 */
	private final UserModelImpl impl;

	/**
	 * The implementation synchronization lock.
	 * 
	 */
	private final Object implLock;

	/**
	 * Create a UserModel.
	 * 
	 * @param session
	 *            The user session.
	 */
	private UserModel(final Session session) {
		super();
		this.impl = new UserModelImpl(session);
		this.implLock = new Object();
	}

    /**
     * Read a list of users.
     * 
     * @return A list of users.
     */
    public List<User> read() {
        synchronized (implLock) {
            return impl.read();
        }
    }

	public User read(final EMail email) {
        synchronized (implLock) {
            return impl.read(email);
        }
    }

	public User read(final JabberId userId) {
		synchronized(implLock) { return impl.read(userId); }
	}

    public List<User> read(final List<JabberId> userIds) throws ParityServerModelException {
		synchronized(implLock) { return impl.read(userIds); }
	}

    public void update(final JabberId userId, final String name,
            final String organization, final String title) {
        synchronized (implLock) {
            impl.update(userId, name, organization, title);
        }
    }
}
