/*
 * Mar 1, 2006
 */
package com.thinkparity.desdemona.model.user;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.io.sql.UserSql;
import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b>thinkParity DesdemonaModel User Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class UserModelImpl extends AbstractModelImpl {

	/** User sql io. */
    private final UserSql userSql;

    /**
     * Create UserModelImpl.
     *
     */
    UserModelImpl() {
        this(null);
    }

    /**
	 * Create a UserModelImpl.
	 * 
	 * @param session
	 *            The user session.
	 */
	UserModelImpl(final Session session) {
		super(session);
        this.userSql = new UserSql();
	}

    /**
     * Determine if the user is an archive.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if the user represents an archive.
     */
    Boolean isBackup(final JabberId userId) {
        try {
            final User user = userSql.read(userId);
            final Credentials credentials = userSql.readCredentials(user.getLocalId());
            final Credentials backupCredentials = readBackupCredentials();
            // NOTE potentially not completely correct
            return credentials.getUsername().equals(
                        backupCredentials.getUsername())
                    && credentials.getPassword().equals(
                            backupCredentials.getPassword());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<User> read() {
        logApiId();
        try {
            return read(FilterManager.createDefault());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    User read(final EMail email) {
        try {
            return userSql.read(email);
        } catch(final Throwable t) {
            throw panic(t);
        }
    }

    List<User> read(final Filter<? super User> filter) {
        logApiId();
        logVariable("filter", filter);
        try {
            final List<User> users = userSql.read();
            FilterManager.filter(users, filter);
            for (final User user : users) {
                inject(user, readVCard(user.getLocalId(), new UserVCard()));
            }
            return users;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    User read(final JabberId userId) {
        logApiId();
		logVariable("userId", userId);
        try {
            final User user = userSql.read(userId);
            return inject(user, readVCard(user.getLocalId(), new UserVCard()));
        } catch (final Throwable t) {
            throw translateError(t);
        }
	}

    List<User> read(final List<JabberId> userIds) {
        logApiId();
        logVariable("userIds", userIds);
		try {
			final List<User> users = new ArrayList<User>(userIds.size());
			for(final JabberId userId : userIds) {
				users.add(read(userId));
			}
			return users;
		} catch(final Throwable t) {
            throw translateError(t);
		}
	}

    Credentials readBackupCredentials() {
        try {
            final User backupUser = userSql.read(User.THINKPARITY_BACKUP.getId());
            return userSql.readCredentials(backupUser.getLocalId());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    JabberId readBackupUserId() {
        try {
            final User backupUser = userSql.read(User.THINKPARITY_BACKUP.getId());
            return backupUser.getId();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read all features for a user.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @param productId
     *            A product id <code>Long</code>.
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    List<Feature> readFeatures(final Long userId, final Long productId) {
        try {
            return userSql.readFeatures(userId, productId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    <T extends com.thinkparity.codebase.model.user.UserVCard> T readVCard(
            final Long userId, final T vcard) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("vcard", vcard);
        try {
            userSql.openVCard(userId, new VCardOpener() {
                public void open(final InputStream stream) throws IOException {
                    XSTREAM_UTIL.fromXML(new BufferedReader(
                            new InputStreamReader(stream),
                            getDefaultBufferSize()), vcard);
                }
            });
            return vcard;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    void updateVCard(final Long userId,
            final com.thinkparity.codebase.model.user.UserVCard vcard) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("vcard", vcard);
        try {
            final File tempVCardFile = session.createTempFile();
            try {
                final FileWriter fileWriter = new FileWriter(tempVCardFile);
                try {
                    XSTREAM_UTIL.toXML(vcard, fileWriter);
                    final InputStream vcardStream = new FileInputStream(tempVCardFile);
                    try {
                        userSql.updateVCard(userId, vcardStream,
                                tempVCardFile.length(), getDefaultBufferSize());
                    } finally {
                        vcardStream.close();
                    }
                } finally {
                    fileWriter.close();
                }
            } finally {
                // TEMPFILE - UserModelImpl#updateVCard()
                tempVCardFile.delete();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Inject the user's vcard fields into the user object.
     * 
     * @param user
     *            A <code>User</code>.
     * @param vcard
     *            A user's vcard dom4j <code>Element</code>.
     */
    private User inject(final User user, final UserVCard vcard) {
        user.setName(vcard.getName());
        user.setOrganization(vcard.getOrganization());
        user.setTitle(vcard.getTitle());
        return user;
    }
}
