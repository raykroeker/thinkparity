/*
 * Apr 5, 2006
 */
package com.thinkparity.model.xmpp.events;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface XMPPContainerListener {
    public void handleReactivate(final UUID uniqueId,
            final Long versionId, final String name, final List<JabberId> team,
            final JabberId reactivatedBy, final Calendar reactivatedOn);

}
