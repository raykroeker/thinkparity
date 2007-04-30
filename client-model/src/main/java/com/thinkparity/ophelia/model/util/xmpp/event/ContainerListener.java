/*
 * Apr 5, 2006
 */
package com.thinkparity.ophelia.model.util.xmpp.event;

import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedNotificationEvent;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface ContainerListener extends XMPPEventListener {
    public void handleEvent(final PublishedEvent event);
    public void handleEvent(final PublishedNotificationEvent event);
}
