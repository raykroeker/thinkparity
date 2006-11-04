/*
 * Created On:  3-Nov-06 11:02:55 AM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.io.Reader;
import java.io.Writer;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.debugger.SmackDebugger;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class XMPPSessionDebugger implements SmackDebugger {

    /** A java <code>Reader</code>. */
    protected final Reader reader;

    /** A java <code>Writer</code>. */
    protected final Writer writer;

    /** An <code>XMPPConnection</code>. */
    protected final XMPPConnection xmppConnection;

    /**
     * Create XMPPSessionDebugger.
     * 
     * @param xmppConnection
     *            An <code>XMPPConnection</code>.
     * @param writer
     *            A java <code>Writer</code>.
     * @param reader
     *            A java <code>Reader</code>.
     */
    public XMPPSessionDebugger(final XMPPConnection xmppConnection,
            final Writer writer, final Reader reader) {
        super();
        this.xmppConnection = xmppConnection;
        this.writer = writer;
        this.reader = reader;
    }
}
