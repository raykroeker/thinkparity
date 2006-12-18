/*
 * Created On: Aug 29, 2006 2:54:53 PM
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Delete extends AbstractHandler {

    /**
     * Create Delete.
     *
     */
    public Delete() { super("contact:delete"); }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider,
     *      com.thinkparity.desdemona.util.service.ServiceRequestReader,
     *      com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     * 
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        logger.logApiId();
        delete(provider, reader.readJabberId("userId"),
                reader.readJabberId("contactId"));
    }

    /**
     * Delete a user's contact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    private void delete(final ServiceModelProvider context,
            final JabberId userId, final JabberId contactId) {
        logger.logVariable("userId", userId);
        logger.logVariable("contactId", contactId);
        context.getContactModel().delete(userId, contactId);
    }
}
