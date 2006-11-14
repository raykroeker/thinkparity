/*
 * Created On:  10-Nov-06 2:16:55 PM
 */
package com.thinkparity.ophelia.model.util.xmpp.event;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface XMPPEventHandler<T extends XMPPEvent> {
    public void handleEvent(final T event);
}
