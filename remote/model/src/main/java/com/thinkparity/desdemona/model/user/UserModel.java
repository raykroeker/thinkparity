/*
 * Created On: Mar 1, 2006
 */
package com.thinkparity.desdemona.model.user;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.user.Feature;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.AbstractModel;
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

	/**
     * Read a list of users.
     * 
     * @return A list of users.
     */
    public List<User> read(final Filter<? super User> filter) {
        synchronized (implLock) {
            return impl.read(filter);
        }
    }

	public User read(final JabberId userId) {
		synchronized(implLock) { return impl.read(userId); }
	}

    public List<User> read(final List<JabberId> userIds) {
		synchronized (implLock) {
            return impl.read(userIds);
		}
	}

    public Credentials readArchiveCredentials(final JabberId archiveId) {
        synchronized (implLock) {
            return impl.readArchiveCredentials(archiveId);
        }
    }

    public List<JabberId> readArchiveIds(final JabberId userId) {
        synchronized (implLock) {
            return impl.readArchiveIds(userId);
        }
    }

    /**
     * Read all features for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Feature&gt</code>.
     */
    public List<Feature> readFeatures(final JabberId userId) {
        synchronized (implLock) {
            return impl.readFeatures(userId);
        }
    }


    public void update(final JabberId userId, final String name,
            final String organization, final String title) {
        synchronized (implLock) {
            impl.update(userId, name, organization, title);
        }
    }
}
