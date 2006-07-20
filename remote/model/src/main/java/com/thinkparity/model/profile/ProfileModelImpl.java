/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.model.profile;

import org.dom4j.Element;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.model.AbstractModelImpl;
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

   /**
     * Obtain an apache api log id.
     * 
     * @param api
     *            The api.
     * @return The log id.
     */
    private static StringBuffer getApiId(final String api) {
        return getModelId("[PROFILE]").append(" ").append(api);
    }

    /**
     * Create ProfileModelImpl.
     *
     * @param session
     *		The user's session.
     */
    ProfileModelImpl(final Session session) {
        super(session);
    }

    /**
     * Read a profile.
     * 
     * @param jabberId
     *            A jabber id.
     * @return A profile.
     */
    Profile read(final JabberId jabberId) {
        logger.info(getApiId("[READ]"));
        logger.debug(jabberId);
        assertEquals(getApiId("[READ] [CAN ONLY READ PERSONAL PROFILE]"), session.getJabberId(), jabberId);
        final User user = getUserModel().readUser(jabberId);
        final Element vCardElement = user.getVCard();

        final Profile profile = new Profile();
        profile.setId(user.getId());
        profile.setName((String) vCardElement.element("FN").getData());
        profile.setOrganization((String) vCardElement.element("ORG").element("ORGNAME").getData());
        profile.setVCard(user.getVCard());
        return profile;
    }
}
