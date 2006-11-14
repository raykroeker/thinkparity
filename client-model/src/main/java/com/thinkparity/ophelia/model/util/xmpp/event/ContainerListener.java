/*
 * Apr 5, 2006
 */
package com.thinkparity.ophelia.model.util.xmpp.event;

import com.thinkparity.codebase.model.util.xmpp.event.ContainerArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerPublishedEvent;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface ContainerListener extends XMPPEventListener {
    public void handleArtifactPublished(final ContainerArtifactPublishedEvent event);
    public void handlePublished(final ContainerPublishedEvent event);
}
