/*
 * Mar 1, 2006
 */
package com.thinkparity.desdemona.model.user;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.messenger.vcard.VCardManager;

import org.dom4j.Element;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants.VCardFields;
import com.thinkparity.desdemona.model.io.sql.UserSql;
import com.thinkparity.desdemona.model.session.Session;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class UserModelImpl extends AbstractModelImpl {

	/** User sql io. */
    private final UserSql userSql;

	/** The jive vcard manager. */
	private final VCardManager vcardManager;

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

    List<User> read() {
        logApiId();
        try {
            final List<User> users =  userSql.read();
            for (final User user : users) {
                inject(user, readVCard(user.getId()));
            }
            return users;
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

    User read(final JabberId userId) {
        logApiId();
		logVariable("userId", userId);
        try {
            return inject(userSql.read(userId), readVCard(userId));
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

    void update(final JabberId userId, final String name,
            final String organization, final String title) {
        logApiId();
        logVariable("userId", userId);
        logVariable("name", name);
        logVariable("organization", organization);
        logVariable("title", title);
        try {
            final Element vcard = readVCard(userId);
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
        return user;
    }

    /**
     * Read a user's vcard information.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A user's vcard dom <code>Element</code>.
     */
    private Element readVCard(final JabberId userId) {
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
