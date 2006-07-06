/*
 * Created On: Jul 6, 2006 11:51:59 AM
 */
package com.thinkparity.model.xmpp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.UUID;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.ProviderManager;

import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.codebase.DateUtil;

import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.document.DocumentVersionContent;
import com.thinkparity.model.parity.model.io.xmpp.XMPPMethod;
import com.thinkparity.model.smackx.packet.AbstractThinkParityIQ;
import com.thinkparity.model.xmpp.events.XMPPContainerListener;

/**
 * <b>Title:</b>thinkParity XMPP Container <br>
 * <b>Description:</b>The container remote interface implemenation. Handles all
 * remote method innvocations to the thinkParity server for the container
 * artifact. Also handles the remote events generated for the container.
 * 
 * @author raymond@thinkparity.com
 * @version
 * @see XMPPCore
 */
class XMPPContainer {

    /** Container xmpp event LISTENERS. */
    private static final List<XMPPContainerListener> LISTENERS;

    static {
        LISTENERS = new ArrayList<XMPPContainerListener>();
        // register the handle reactivate remote event listener
        ProviderManager.addIQProvider("query", "jabber:iq:parity:container:handlereactivate", new IQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                final HandleReactivateIQ query = new HandleReactivateIQ();

                Integer eventType;
                String name;
                Boolean isComplete = Boolean.FALSE;
                while(Boolean.FALSE == isComplete) {
                    eventType = parser.next();
                    name = parser.getName();

                    if(XmlPullParser.START_TAG == eventType && "uuid".equals(name)) {
                        parser.next();
                        query.uniqueId = UUID.fromString(parser.getText());
                        parser.next();
                    }
                    else if(XmlPullParser.END_TAG == eventType && "uuid".equals(name)) {
                        parser.next();
                    }
                    else if(XmlPullParser.START_TAG == eventType && "versionid".equals(name)) {
                        parser.next();
                        query.versionId = Long.parseLong(parser.getText());
                        parser.next();
                    }
                    else if(XmlPullParser.END_TAG == eventType && "versionid".equals(name)) {
                        parser.next();
                    }
                    else if(XmlPullParser.START_TAG == eventType && "name".equals(name)) {
                        parser.next();
                        query.name = parser.getText();
                        parser.next();
                    }
                    else if(XmlPullParser.END_TAG == eventType && "name".equals(name)) {
                        parser.next();
                    }
                    else if(XmlPullParser.START_TAG == eventType && "reactivatedBy".equals(name)) {
                        parser.next();
                        query.reactivatedBy = JabberIdBuilder.parseQualifiedJabberId(parser.getText());
                        parser.next();
                    }
                    else if(XmlPullParser.END_TAG == eventType && "reactivatedBy".equals(name)) {
                        parser.next();
                    }
                    else if(XmlPullParser.START_TAG == eventType && "reactivatedOn".equals(name)) {
                        parser.next();
                        query.reactivatedOn = DateUtil.parse(parser.getText(),
                                DateUtil.DateImage.ISO, new SimpleTimeZone(0, "GMT"));
                        parser.next();
                    }
                    else if(XmlPullParser.END_TAG == eventType && "reactivatedOn".equals(name)) {
                        parser.next();
                    }
                    else if(XmlPullParser.START_TAG == eventType && "team".equals(name)) {
                        parser.next();
                    }
                    else if(XmlPullParser.END_TAG == eventType && "team".equals(name)) {
                        parser.next();
                    }
                    else if(XmlPullParser.END_TAG == eventType && "teamMember".equals(name)) {
                        parser.next();
                        query.team.add(JabberIdBuilder.parseQualifiedJabberId(parser.getText()));
                        parser.next();
                    }
                    else if(XmlPullParser.END_TAG == eventType && "teamMember".equals(name)) {
                        parser.next();
                    }
                    else { isComplete = Boolean.TRUE; }
                }
                return query;
            }
        });
    }

    /** Core xmpp functionality. */
    private final XMPPCore core;

    /** An apache logger. */
    private final Logger logger;

    /** Create XMPPContainer. */
    XMPPContainer(final XMPPCore core) {
        super();
        this.core = core;
        this.logger = Logger.getLogger(getClass());
    }

    /**
     * Add an xmpp container event listener.
     * 
     * @param l
     *            The xmpp container event listener.
     */
    void addListener(final XMPPContainerListener l) {
        logger.info("[LMODEL] [XMPP] [CONTAINER] [ADD LISTENER");
        logger.debug(l);
        synchronized(LISTENERS) {
            if(LISTENERS.contains(l)) { return; }
            LISTENERS.add(l);
        }
    }

    /**
     * Add the requisite packet LISTENERS to the xmpp connection.
     * 
     * @param xmppConnection
     *            The xmpp connection.
     */
    void addPacketListeners(final XMPPConnection xmppConnection) {
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        handleReactivate((HandleReactivateIQ) packet);
                    }
                },
                new PacketTypeFilter(HandleReactivateIQ.class));
    }
    /**
     * Call the remote artifact:reactivate method.
     * 
     * @param team
     *            The artifact team.
     * @param uniqueId
     *            The artifact unique id.
     */
    void reactivate(final ContainerVersion version,
            final List<DocumentVersionContent> documentVersions,
            final List<JabberId> team, final JabberId reactivatedBy,
            final Calendar reactivatedOn) {
        logger.info("[LMODEL] [XMPP] [CONTAINER] [REACTIVATE]");
        logger.debug(version);
        logger.debug(documentVersions);
        logger.debug(team);
        logger.debug(reactivatedBy);
        logger.debug(reactivatedOn);
        final XMPPMethod method = new XMPPMethod("container:reactivate");
        method.setParameter("uniqueId", version.getArtifactUniqueId());
        method.setParameter("versionId", version.getVersionId());
        method.setParameter("name", version.getName());
        method.setJabberIdParameters("team", "teamMember", team);
        method.setParameter("reactivatedBy", reactivatedBy);
        method.setParameter("reactivatedOn", reactivatedOn);
        method.setDocumentVersionParameters("documentVersions", "documentVersion", documentVersions);
        method.execute(core.getConnection());
    }

    /**
     * Handle the reactivate event generated by the remote model.
     *
     */
    private void handleReactivate(final HandleReactivateIQ query) {
        synchronized(LISTENERS) {
            for(final XMPPContainerListener l : LISTENERS) {
                l.handleReactivate(query.uniqueId, query.versionId, query.name,
                        query.team, query.reactivatedBy, query.reactivatedOn);
            }
        }
    }

    /**
     * <b>Title:</b>thinkParity XMPP Container Handler Reactivate Query <br>
     * <b>Description:</b>Provides a wrapper for data coming from the remote
     * event.
     * 
     * @see XMPPContainer#handleReactivate(HandleReactivateIQ)
     * @see XMPPContainer#addPacketListeners(XMPPConnection)
     */
    private static class HandleReactivateIQ extends AbstractThinkParityIQ {

        /** Container name. */
        private String name;

        /** Who reactivated the container. */
        private JabberId reactivatedBy;

        /** When the container was reactivated. */
        private Calendar reactivatedOn;

        /** Container team. */
        private final List<JabberId> team = new ArrayList<JabberId>();

        /** Container unique id. */
        private UUID uniqueId;

        /** Container version id. */
        private Long versionId;

        /** Create HandleReactivateIQ. */
        private HandleReactivateIQ() { super(); }
    }
}
