/*
 * Created On:  4-Nov-06 9:15:48 AM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.io.Reader;
import java.io.Writer;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class XMPPSessionDebugger extends
        com.thinkparity.ophelia.model.util.xmpp.XMPPSessionDebugger {

    /** An apache logger wrapper. */
    private static final Log4JWrapper LOGGER;

    /** A <code>PacketListener</code>. */
    private static final PacketListener PACKET_LISTENER;

    static {
        LOGGER = new Log4JWrapper("XMPP_DEBUGGER");
        PACKET_LISTENER = new PacketListener() {
            public void processPacket(final Packet packet) {
                LOGGER.logDebug("{0}", packet);
            }
        };
    }

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
        super(xmppConnection, writer, reader);
    }

    /**
     * @see org.jivesoftware.smack.debugger.SmackDebugger#getReader()
     *
     */
    public Reader getReader() {
        return reader;
    }

    /**
     * @see org.jivesoftware.smack.debugger.SmackDebugger#getReaderListener()
     *
     */
    public PacketListener getReaderListener() {
        return PACKET_LISTENER;
    }

    /**
     * @see org.jivesoftware.smack.debugger.SmackDebugger#getWriter()
     *
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * @see org.jivesoftware.smack.debugger.SmackDebugger#getWriterListener()
     *
     */
    public PacketListener getWriterListener() {
        return PACKET_LISTENER;
    }

    /**
     * @see org.jivesoftware.smack.debugger.SmackDebugger#newConnectionReader(java.io.Reader)
     *
     */
    public Reader newConnectionReader(final Reader reader) {
        return reader;
    }

    /**
     * @see org.jivesoftware.smack.debugger.SmackDebugger#newConnectionWriter(java.io.Writer)
     *
     */
    public Writer newConnectionWriter(final Writer writer) {
        return writer;
    }

    /**
     * @see org.jivesoftware.smack.debugger.SmackDebugger#userHasLogged(java.lang.String)
     *
     */
    public void userHasLogged(final String user) {}
}
