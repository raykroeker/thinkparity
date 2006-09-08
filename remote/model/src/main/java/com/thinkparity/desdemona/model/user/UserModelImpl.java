/*
 * Mar 1, 2006
 */
package com.thinkparity.desdemona.model.user;

import java.util.LinkedList;
import java.util.List;

import org.jivesoftware.messenger.vcard.VCardManager;

import org.dom4j.Element;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;


import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.io.sql.UserSql;
import com.thinkparity.desdemona.model.session.Session;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class UserModelImpl extends AbstractModelImpl {

	/** User sql io. */
    private final UserSql userSql;

	/**
	 * The default vcard manager.
	 */
	private final VCardManager vCardManager;

    /**
	 * Create a UserModelImpl.
	 * 
	 * @param session
	 *            The user session.
	 */
	UserModelImpl(final Session session) {
		super(session);
        this.userSql = new UserSql();
		this.vCardManager = VCardManager.getInstance();
	}

    User readUser(final JabberId jabberId) {
        logApiId();
		logVariable("jabberId", jabberId);
        final Element vCard = vCardManager.getVCard(jabberId.getUsername());
        final String name = (String) vCard.element("FN").getData();
        final String organization = (String) vCard.element("ORG").element("ORGNAME").getData();
        
		final User user = new User();
		user.setId(jabberId);
        user.setName(name);
        user.setOrganization(organization);
		user.setVCard(vCard);
		return user;
	}

    User readUser(final EMail email) {
        logApiId();
        logVariable("email", email);
        try {
            final String username = userSql.readUsername(email);
            if (null == username) {
                return null;
            } else {
                final JabberId jabberId =
                    JabberIdBuilder.parseUsername(username);
                return readUser(jabberId);
            }
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

	List<User> readUsers(final List<JabberId> jabberIds) {
        logApiId();
		try {
			final List<User> users = new LinkedList<User>();
			User user;
			for(final JabberId jabberId : jabberIds) {
				user = new User();
				user.setId(jabberId);
				user.setVCard(vCardManager.getVCard(jabberId.getUsername()));
				users.add(user);
			}
			return users;
		} catch(final Throwable t) {
            throw translateError(t);
		}
	}
}
