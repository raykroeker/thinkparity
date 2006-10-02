/*
 * Created On: Apr 8, 2006
 * $Id$
 */
package com.thinkparity.desdemona.wildfire.handler.artifact;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ConfirmReceipt extends AbstractHandler {

    /**
     * Create a ConfirmReceipt.
     * @param action
     */
    public ConfirmReceipt() { super("artifact:confirmreceipt"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        confirmReceipt(readJabberId("userId"), readUUID("uniqueId"),
                readLong("versionId"), readJabberId("receivedBy"), readCalendar("receivedOn"));
    }

    private void confirmReceipt(final JabberId userId, final UUID uniqueId,
            final Long versionId, final JabberId receivedBy, final Calendar receivedOn) {
        getArtifactModel().confirmReceipt(userId, uniqueId, versionId, receivedBy, receivedOn);
    }
}
