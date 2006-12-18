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
public class UserModel extends AbstractModel<UserModelImpl> {

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

    public static UserModel getModel() {
        return new UserModel();
    }

	/**
	 * Create a UserModel.
	 * 
	 * @param session
	 *            The user session.
	 */
	private UserModel(final Session session) {
		super(new UserModelImpl(session));
	}

    private UserModel() {
        super(new UserModelImpl());
    }

    /**
     * Determine if the user is an archive.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if the user represents an archive.
     */
    public Boolean isArchive(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().isArchive(userId);
        }
    }

    public List<User> read() {
        synchronized (getImplLock()) {
            return getImpl().read();
        }
    }

	public User read(final EMail email) {
        synchronized (getImplLock()) {
            return getImpl().read(email);
        }
    }

	/**
     * Read a list of users.
     * 
     * @return A list of users.
     */
    public List<User> read(final Filter<? super User> filter) {
        synchronized (getImplLock()) {
            return getImpl().read(filter);
        }
    }

    public User read(final JabberId userId) {
		synchronized(getImplLock()) { return getImpl().read(userId); }
	}

    public List<User> read(final List<JabberId> userIds) {
		synchronized (getImplLock()) {
            return getImpl().read(userIds);
		}
	}

    public Credentials readArchiveCredentials(final JabberId archiveId) {
        synchronized (getImplLock()) {
            return getImpl().readArchiveCredentials(archiveId);
        }
    }

    public JabberId readArchiveId(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readArchiveId(userId);
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
        synchronized (getImplLock()) {
            return getImpl().readFeatures(userId);
        }
    }

    public String readVCard(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readVCard(userId);
        }
    }

    public void update(final JabberId userId, final String name,
            final String organization, final String title) {
        synchronized (getImplLock()) {
            getImpl().update(userId, name, organization, title);
        }
    }
}
