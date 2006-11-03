/*
 * Created On:  3-Nov-06 11:02:55 AM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.io.Reader;
import java.io.Writer;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.debugger.SmackDebugger;
import org.jivesoftware.smack.packet.Packet;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class XMPPSessionDebugger implements SmackDebugger {

    /** The packet reader <code>PacketListener</code>. */
    private static final PacketListener READER_LISTENER;

    /** The packet writer <code>PacketListener</code>. */
    private static final PacketListener WRITER_LISTENER;

    /** The apache xmpp packet logger wrapper. */ 
    private static final Log4JWrapper XMPP_PACKET_LOGGER;

    static {
        READER_LISTENER = new PacketListener() {
            public void processPacket(final Packet packet) {
                XMPP_PACKET_LOGGER.logVariable("read packet", packet);
            }
        };
        WRITER_LISTENER = new PacketListener() {
            public void processPacket(final Packet packet) {
                XMPP_PACKET_LOGGER.logVariable("write packet", packet);
            }
        };
        XMPP_PACKET_LOGGER = new Log4JWrapper("XMPP_PACKET");
    }

    /** The underlying <code>Reader</code>. */
    private final Reader reader;

    /** The underlying <code>Writer</code>. */
    private final Writer writer;

    /**
     * Create XMPPSessionDebugger.
     *
     */
    public XMPPSessionDebugger(final XMPPConnection xmppConnection,
            final Writer writer, final Reader reader) {
        super();
        this.writer = writer;
        this.reader = reader;
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
        return READER_LISTENER;
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
        return WRITER_LISTENER;
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
