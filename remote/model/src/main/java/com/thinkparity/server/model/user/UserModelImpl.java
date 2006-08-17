/*
 * Mar 1, 2006
 */
package com.thinkparity.server.model.user;

import java.util.LinkedList;
import java.util.List;

import org.jivesoftware.messenger.vcard.VCardManager;

import org.dom4j.Element;

import com.thinkparity.codebase.Constants.Jabber;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityErrorTranslator;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.io.sql.user.UserSql;
import com.thinkparity.server.model.session.Session;

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
		debugVariable("jabberId", jabberId);
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

    User readUser(final String email) {
        logApiId();
        debugVariable("email", email);
        try {
            final String username = userSql.readUsername(email);
            if (null == username) {
                return null;
            } else {
                final JabberId jabberId =
                    JabberIdBuilder.build(username, session.getXmppDomain(), Jabber.RESOURCE);
                return readUser(jabberId);
            }
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

	List<User> readUsers(final List<JabberId> jabberIds)
			throws ParityServerModelException {
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
		}
		catch(final RuntimeException rx) {
			logger.error("Could not read users.", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}
}
