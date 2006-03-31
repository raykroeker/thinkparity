/*
 * Mar 1, 2006
 */
package com.thinkparity.server.model.user;

import java.util.List;

import com.thinkparity.server.JabberId;
import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.session.Session;

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

	public User readUser(final JabberId jabberId) {
		synchronized(implLock) { return impl.readUser(jabberId); }
	}

	public List<User> readUsers(final List<JabberId> jabberIds) throws ParityServerModelException {
		synchronized(implLock) { return impl.readUsers(jabberIds); }
	}
}
