/*
 * Created On:  13-Nov-06 10:07:30 AM
 */
package com.thinkparity.desdemona.model.queue;

import java.io.Reader;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface XMPPEventReader {
    public XMPPEvent read(final Reader xml);
}
