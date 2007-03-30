/*
 * Created On: Mar 1, 2006
 */
package com.thinkparity.desdemona.model.user;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.session.Session;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UserModel extends AbstractModel<UserModelImpl> {

    public static InternalUserModel getInternalModel(final Context context) {
        return new InternalUserModel(context);
    }

    public static InternalUserModel getInternalModel(final Context context,
            final Session session) {
        return new InternalUserModel(context, session);
    }

    public static UserModel getModel() {
        return new UserModel();
    }

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
     * Create a UserModel.
     * 
     * @param session
     *            The user session.
     */
    protected UserModel() {
        super(new UserModelImpl());
    }

    /**
     * Create a UserModel.
     * 
     * @param session
     *            The user session.
     */
    protected UserModel(final Session session) {
        super(new UserModelImpl(session));
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

    public <T extends com.thinkparity.codebase.model.user.UserVCard> T readVCard(
            final Long userId, final T vcard) {
        synchronized (getImplLock()) {
            return getImpl().readVCard(userId, vcard);
        }
    }

    public void updateVCard(final Long userId,
            final com.thinkparity.codebase.model.user.UserVCard vcard) {
        synchronized (getImplLock()) {
            getImpl().updateVCard(userId, vcard);
        }
    }
}
