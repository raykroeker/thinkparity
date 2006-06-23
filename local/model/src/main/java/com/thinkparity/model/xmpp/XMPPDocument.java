/*
 * Created On: Apr 5, 2006
 * $Id$
 */
package com.thinkparity.model.xmpp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import com.thinkparity.model.Constants.Xml;
import com.thinkparity.model.parity.model.io.xmpp.XMPPMethod;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smackx.packet.document.IQHandleReactivate;
import com.thinkparity.model.smackx.packet.document.IQHandleReactivateProvider;
import com.thinkparity.model.smackx.packet.document.IQSendDocument;
import com.thinkparity.model.smackx.packet.document.IQSendDocumentProvider;
import com.thinkparity.model.xmpp.events.XMPPDocumentListener;

/**
 * An xmpp interface for parity documents. It has the ability to send documents
 * across an xmpp connection; and fires events when a document is received from
 * same.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class XMPPDocument {

    /**
     * Registered xmpp document event listeners.
     * 
     */
    private static final Set<XMPPDocumentListener> listeners;

    static {
        listeners = new HashSet<XMPPDocumentListener>();

        ProviderManager.addIQProvider("query", "jabber:iq:parity:documentsend", new IQSendDocumentProvider());
        ProviderManager.addIQProvider("query", "jabber:iq:parity:documentreactivate", new IQHandleReactivateProvider());
    }

    /**
     * An apache logger.
     * 
     */
    protected final Logger logger;

    /**
     * The xmpp core functionality.
     * 
     */
    protected final XMPPCore xmppCore;

    /**
     * Create a XMPPDocument.
     */
    XMPPDocument(final XMPPCore xmppCore) {
        super();
        this.xmppCore = xmppCore;
        this.logger = Logger.getLogger(getClass());
    }

    /**
     * Add an xmpp document event listener.
     * 
     * @param l
     *            The xmpp document event listener.
     */
    void addListener(final XMPPDocumentListener l) {
        logger.info("[LMODEL] [XMPP] [ADD DOCUMENT LISTENER");
        logger.debug(l);
        synchronized(listeners) {
            if(listeners.contains(l)) { return; }
            listeners.add(l);
        }
    }

    /**
     * Add the requisite packet listeners to the xmpp connection.
     * 
     * @param xmppConnection
     *            The xmpp connection.
     */
    void addPacketListeners(final XMPPConnection xmppConnection) {
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        notifyDocumentReceived((IQSendDocument) packet);
                    }
                },
                new PacketTypeFilter(IQSendDocument.class));
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        notifyDocumentReactivated((IQHandleReactivate) packet);
                    }
                },
                new PacketTypeFilter(IQHandleReactivate.class));
    }

    void sendReactivate(final List<JabberId> team,
            final UUID uniqueId, final Long versionId, final String name,
            final byte[] bytes) throws SmackException {
        logger.info("[LMODEL] [XMPP] [DOCUMENT] [SEND REACTIVATE]");
        logger.debug(team);
        logger.debug(uniqueId);
        logger.debug(versionId);
        logger.debug(name);
        logger.debug(null == bytes ? -1 : bytes.length);
        final XMPPMethod method = new XMPPMethod("document:reactivate");
        method.setJabberIdParameters(Xml.User.JABBER_IDS, Xml.User.JABBER_ID, team);
        method.setParameter(Xml.Artifact.UNIQUE_ID, uniqueId);
        method.setParameter(Xml.Artifact.VERSION_ID, versionId);
        method.setParameter(Xml.Artifact.NAME, name);
        method.setParameter(Xml.Artifact.BYTES, bytes);
        method.execute(xmppCore.getConnection());
    }

    /**
     * Send a document to a list of users.
     * 
     * @param users
     *            The users to send the document to.
     * @param document
     *            The document to send.
     */
    void sendVersion(final Set<JabberId> sendTo, final UUID uniqueId,
            final Long versionId, final String name, final byte[] content)
            throws SmackException {
        logger.info("[LMODEL] [XMPP] [DOCUMENT] [SEND]");
        logger.debug(sendTo);
        logger.debug(uniqueId);
        logger.debug(name);
        logger.debug(content);
        final IQSendDocument iq = new IQSendDocument();
        iq.addAllSendTo(sendTo);
        iq.setUniqueId(uniqueId);
        iq.setVersionId(versionId);
        iq.setName(name);
        iq.setContent(content);
        xmppCore.sendAndConfirmPacket(iq);
    }

    /**
     * Fire a notification that a document has been reactivated.
     * 
     * @param iq
     *            The xmpp internet query.
     */
    private void notifyDocumentReactivated(final IQHandleReactivate iq) {
        synchronized(listeners) {
            for(final XMPPDocumentListener l : listeners) {
                l.documentReactivated(iq.getFromJabberId(), iq.getTeam(),
                        iq.getUniqueId(), iq.getVersionId(), iq.getName(),
                        iq.getContent());
            }
        }
    }

    /**
     * Fire a notification that a document has been received.
     * 
     * @param document
     *            The document.
     */
    private void notifyDocumentReceived(final IQSendDocument iq) {
        synchronized(listeners) {
            for(final XMPPDocumentListener l : listeners) {
                l.documentReceived(iq.getFromJabberId(), iq.getUniqueId(),
                        iq.getVersionId(), iq.getName(), iq.getContent());
            }
        }
    }
}
