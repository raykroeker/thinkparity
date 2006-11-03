/*
 * Created On: Aug 29, 2006 2:54:53 PM
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Delete extends AbstractHandler {

    /** Create Delete. */
    public Delete() { super("contact:delete"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        delete(readJabberId("userId"), readJabberId("contactId"));
    }

    /**
     * Delete a user's contact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    private void delete(final JabberId userId, final JabberId contactId) {
        logger.logVariable("userId", userId);
        logger.logVariable("contactId", contactId);
        getContactModel().delete(userId, contactId);
    }
}
