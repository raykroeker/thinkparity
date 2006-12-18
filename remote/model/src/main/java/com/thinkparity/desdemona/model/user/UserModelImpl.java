/*
 * Mar 1, 2006
 */
package com.thinkparity.desdemona.model.user;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.user.Feature;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants.VCardFields;
import com.thinkparity.desdemona.model.io.sql.UserSql;
import com.thinkparity.desdemona.model.session.Session;

import org.dom4j.Element;
import org.jivesoftware.wildfire.vcard.VCardManager;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class UserModelImpl extends AbstractModelImpl {

	/** User sql io. */
    private final UserSql userSql;

	/** The jive vcard manager. */
	private final VCardManager vcardManager;

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
		this.vcardManager = VCardManager.getInstance();
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
                inject(user, readVCardElement(user.getId()));
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
            return inject(userSql.read(userId), readVCardElement(userId));
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
    List<Feature> readFeatures(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        try {
            return userSql.readFeatures(userId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    String readVCard(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        try {
            return readVCardElement(userId).asXML();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    void update(final JabberId userId, final String name,
            final String organization, final String title) {
        logApiId();
        logVariable("userId", userId);
        logVariable("name", name);
        logVariable("organization", organization);
        logVariable("title", title);
        try {
            final Element vcard = readVCardElement(userId);
            vcard.element("FN").setText(name);
            Element organizationElement = vcard.element("ORG");
            Element organizationNameElement = null;
            if (null != organization) {
                if (null == organizationElement) {
                    organizationElement = vcard.addElement("ORG");
                }
                organizationNameElement = organizationElement.element("ORGNAME");
                if (null == organizationNameElement) {
                    organizationNameElement = organizationElement.addElement("ORGNAME");
                }
                organizationNameElement.setText(organization);
            } else {
                if (null != organizationElement) {
                    vcard.remove(organizationElement);
                }
            }
            Element titleElement = vcard.element(VCardFields.TITLE);
            if (null != title) {
                if (null == titleElement) {
                    titleElement = vcard.addElement(VCardFields.TITLE);
                }
                titleElement.setText(title);
            } else {
                if (null != titleElement) {
                    vcard.remove(titleElement);
                }
            }
            updateVCard(userId, vcard);
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
    private User inject(final User user, final Element vcard) {
        if (null == vcard) {
            logWarning("NULL VCARD");
        } else {
            user.setName((String) vcard.element("FN").getData());
            final Element organization = vcard.element("ORG");
            if (null != organization) {
                final Element organizationName = organization.element("ORGNAME");
                if (null != organizationName) {
                    user.setOrganization((String) organizationName.getData());
                }
            }
            final Element title = vcard.element(VCardFields.TITLE);
            if (null != title) {
                user.setTitle((String) title.getData());
            }
        }
        return user;
    }

    /**
     * Read a user's vcard information.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A user's vcard dom <code>Element</code>.
     */
    private Element readVCardElement(final JabberId userId) {
        return vcardManager.getVCard(userId.getUsername());
    }

    /**
     * Update a user's vcard information.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param vcard
     *            A users's vcard dom <code>Element</code>.
     * @throws Exception
     */
    private void updateVCard(final JabberId userId, final Element vcard)
            throws Exception {
        vcardManager.setVCard(userId.getUsername(), vcard);
    }
}
