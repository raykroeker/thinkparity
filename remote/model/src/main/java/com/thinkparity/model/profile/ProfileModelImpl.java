/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.model.profile;

import java.sql.SQLException;

import org.dom4j.Element;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityErrorTranslator;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.io.sql.user.UserSql;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.model.user.User;

/**
 * <b>Title:</b>thinkParity Profile Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
class ProfileModelImpl extends AbstractModelImpl {

    /** User db io. */
    private final UserSql userSql;

    /**
     * Create ProfileModelImpl.
     *
     * @param session
     *		The user's session.
     */
    ProfileModelImpl(final Session session) {
        super(session);
        this.userSql = new UserSql();
    }

    /**
     * Read a profile.
     * 
     * @param jabberId
     *            A jabber id.
     * @return A profile.
     */
    Profile read(final JabberId jabberId) throws ParityServerModelException {
        logApiId();
        logger.debug(jabberId);
        assertEquals("[CAN ONLY READ PERSONAL PROFILE]", session.getJabberId(), jabberId);
        final User user = getUserModel().readUser(jabberId);
        final Element vCardElement = user.getVCard();

        final Profile profile = new Profile();
        try { profile.addAllEmails(userSql.readEmail(jabberId)); }
        catch(final SQLException sqlx) {
            throw ParityErrorTranslator.translate(sqlx);
        }
        profile.setId(user.getId());
        profile.setName((String) vCardElement.element("FN").getData());
        profile.setOrganization((String) vCardElement.element("ORG").element("ORGNAME").getData());
        profile.setVCard(user.getVCard());
        return profile;
    }
}
