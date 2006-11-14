/*
 * Created On:  13-Nov-06 10:07:30 AM
 */
package com.thinkparity.desdemona.model.queue;

import java.io.Writer;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface XMPPEventWriter {
    public void write(final XMPPEvent event, final Writer writer);
}
