/*
 * Created On:  28-Feb-07 8:15:26 PM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;

/**
 * <b>Title:</b>thinkParity OpheliaModel XMPP Rules<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class XMPPRules extends AbstractXMPP {

    /**
     * Create XMPPRules.
     * 
     * @param xmppCore
     *            The <code>XMPPCore</code>.
     */
    XMPPRules(final XMPPCore xmppCore) {
        super(xmppCore);
    }

    /**
     * Determine if the rule for publish allows a user to publish to another
     * user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param publishFrom
     *            A publish from user id <code>JabberId</code>.
     * @param publishTo
     *            A publish to user id <code>JabberId</code>.
     * @return True if the user can published to.
     */
    Boolean isPublishRestricted(final JabberId userId,
            final JabberId publishFrom, final JabberId publishTo) {
        final XMPPMethod isPublishRestricted = xmppCore.createMethod("rules:ispublishrestricted");
        isPublishRestricted.setParameter("userId", userId);
        isPublishRestricted.setParameter("publishFrom", publishFrom);
        isPublishRestricted.setParameter("publishTo", publishTo);
        return execute(isPublishRestricted, Boolean.TRUE).readResultBoolean("isRestricted");
    }
}
