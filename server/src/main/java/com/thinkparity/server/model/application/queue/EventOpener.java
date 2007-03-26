/*
 * Created On:  25-Mar-07 3:28:43 PM
 */
package com.thinkparity.desdemona.model.queue;

import java.io.IOException;
import java.io.InputStream;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Queue Event Opener<br>
 * <b>Description:</b>A delegate that allows the model to manipulate the xmpp
 * event input stream.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface EventOpener {

    /**
     * Open an input stream.
     * 
     * @param event
     *            An <code>InputStream</code>.
     */
    public XMPPEvent open(final InputStream event) throws IOException;
}
