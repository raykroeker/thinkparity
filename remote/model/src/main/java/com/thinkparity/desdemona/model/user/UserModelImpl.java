/*
 * Mar 1, 2006
 */
package com.thinkparity.desdemona.model.user;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xstream.XStreamUtil;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.io.sql.UserSql;
import com.thinkparity.desdemona.model.session.Session;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class UserModelImpl extends AbstractModelImpl {

    private static final XStreamUtil XSTREAM_UTIL;

    static {
        XSTREAM_UTIL = XStreamUtil.getInstance();
    }

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
    Boolean isArchive(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        try {
            final Credentials archiveCredentials = readArchiveCredentials(userId);
            if (null == archiveCredentials) {
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
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
        logApiId();
        logVariable("email", email);
        try {
            final String username = userSql.readUsername(email);
            if (null == username) {
                return null;
            } else {
                final JabberId jabberId =
                    JabberIdBuilder.parseUsername(username);
                return read(jabberId);
            }
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

    List<User> read(final Filter<? super User> filter) {
        logApiId();
        logVariable("filter", filter);
        try {
            final List<User> users = userSql.read();
            FilterManager.filter(users, filter);
            for (final User user : users) {
                inject(user, readVCard(user.getId(), new UserVCard()));
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
            return inject(userSql.read(userId), readVCard(userId, new UserVCard()));
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

    Credentials readArchiveCredentials(final JabberId archiveId) {
        logApiId();
        logVariable("archiveId", archiveId);
        try {
            return userSql.readArchiveCredentials(archiveId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	JabberId readArchiveId(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        try {
            // ASSUME For version 1 we assume a single archive
            final List<JabberId> archiveIds = userSql.readArchiveIds(userId);
            if (0 == archiveIds.size()) {
                return null;
            } else {
                return archiveIds.get(0);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read all features for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Feature&gt</code>.
     */
    List<Feature> readFeatures(final JabberId userId, final Long productId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("productId", productId);
        try {
            return userSql.readFeatures(userId, productId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    <T extends com.thinkparity.codebase.model.user.UserVCard> T readVCard(
            final JabberId userId, final T vcard) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        try {
            final StringReader vcardXMLReader =
                new StringReader(userSql.readProfileVCard(userId));
            try {
                XSTREAM_UTIL.fromXML(vcardXMLReader, vcard);
                return vcard;
            } finally {
                vcardXMLReader.close();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    void updateVCard(final JabberId userId,
            final com.thinkparity.codebase.model.user.UserVCard vcard) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("vcard", vcard);
        try {
            final StringWriter vcardXMLWriter = new StringWriter();
            XSTREAM_UTIL.toXML(vcard, vcardXMLWriter);
            userSql.updateProfileVCard(userId, vcardXMLWriter.toString());
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
