/*
 * Feb 28, 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;
import com.thinkparity.ophelia.model.io.xmpp.XMPPException;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;

/**
 * Provides core xmpp functionality.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface XMPPCore {
	public XMPPMethod createMethod(final String name);
    public XMPPMethodResponse execute(final XMPPMethod method);
    public XMPPMethodResponse execute(final XMPPMethod method,
            final Boolean assertResult);
    public XMPPMethodResponse executeAnonymously(final XMPPMethod method);
    public XMPPMethodResponse executeAnonymously(final XMPPMethod method,
            final Boolean assertResult);
    public JabberId getUserId();
    public void handleError(final Throwable t);
    public <T extends XMPPEvent> void handleEvent(final T event);
    public XMPPException panic(final Throwable t);
}
