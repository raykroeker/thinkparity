/*
 * Created On: Jul 6, 2006 11:51:59 AM
 */
package com.thinkparity.model.xmpp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.codebase.StreamUtil;

import com.thinkparity.model.Constants.Xml.Service;
import com.thinkparity.model.Constants.Xml.Container.Method.Publish;
import com.thinkparity.model.Constants.Xml.Container.Method.PublishArtifact;
import com.thinkparity.model.Constants.Xml.Container.Method.Send;
import com.thinkparity.model.Constants.Xml.Container.Method.SendArtifact;
import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.io.xmpp.XMPPMethod;
import com.thinkparity.model.smackx.packet.AbstractThinkParityIQ;
import com.thinkparity.model.smackx.packet.AbstractThinkParityIQProvider;
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
class XMPPContainer extends AbstractXMPP {

    /** Container xmpp event LISTENERS. */
    private static final List<XMPPContainerListener> LISTENERS;

    static {
        LISTENERS = new ArrayList<XMPPContainerListener>();

        ProviderManager.addIQProvider(Service.NAME, Publish.EVENT_NAME, new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser2(parser);
                final HandlePublishedIQ query = new HandlePublishedIQ();
                Boolean isComplete = Boolean.FALSE;
                while (Boolean.FALSE == isComplete) {
                    if (isStartTag("uniqueId")) {
                        query.uniqueId = readUniqueId2();
                    } else if (isStartTag("versionId")) {
                        query.versionId = readLong2();
                    } else if (isStartTag("name")) {
                        query.name = readString2();
                    } else if (isStartTag("artifactCount")) {
                        query.artifactCount = readInteger2();
                    } else if (isStartTag("publishedBy")) {
                        query.publishedBy = readJabberId2();
                    } else if (isStartTag("publishedTo")) {
                        query.publishedTo = readJabberIds2();
                    } else if (isStartTag("publishedOn")) {
                        query.publishedOn = readCalendar2();
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
        ProviderManager.addIQProvider(Service.NAME, PublishArtifact.EVENT_NAME, new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser2(parser);
                final HandleArtifactPublishedIQ query = new HandleArtifactPublishedIQ();
                Boolean isComplete = Boolean.FALSE;
                while(Boolean.FALSE == isComplete) {
                    if (isStartTag("uniqueId")) {
                        query.containerUniqueId = readUniqueId2();
                    } else if(isStartTag("versionId")) {
                        query.containerVersionId = readLong2();
                    } else if (isStartTag("name")) {
                        query.containerName = readString2();
                    } else if (isStartTag("artifactCount")) {
                        query.containerArtifactCount = readInteger2();
                    } else if (isStartTag("artifactIndex")) {
                        query.containerArtifactIndex = readInteger2();
                    } else if (isStartTag("artifactUniqueId")) {
                        query.artifactUniqueId = readUniqueId2();
                    } else if (isStartTag("artifactVersionId")) {
                        query.artifactVersionId = readLong2();
                    } else if (isStartTag("artifactName")) {
                        query.artifactName = readString2();
                    } else if (isStartTag("artifactType")) {
                        query.artifactType = readArtifactType2();
                    } else if (isStartTag("artifactChecksum")) {
                        query.artifactChecksum = readString2();
                    } else if (isStartTag("artifactBytes")) {
                        query.artifactBytes = readBytes2();
                    } else if (isStartTag("publishedBy")) {
                        query.publishedBy = readJabberId2();
                    } else if (isStartTag("publishedOn")) {
                        query.publishedOn = readCalendar2();
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
        ProviderManager.addIQProvider(Service.NAME, Send.EVENT_NAME, new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser2(parser);
                final HandleSentIQ query = new HandleSentIQ();
                Boolean isComplete = Boolean.FALSE;
                while (Boolean.FALSE == isComplete) {
                    if (isStartTag("uniqueId")) {
                        query.uniqueId = readUniqueId2();
                    } else if (isStartTag("versionId")) {
                        query.versionId = readLong2();
                    } else if (isStartTag("name")) {
                        query.name = readString2();
                    } else if (isStartTag("artifactCount")) {
                        query.artifactCount = readInteger2();
                    } else if (isStartTag("sentBy")) {
                        query.sentBy = readJabberId2();
                    } else if (isStartTag("sentOn")) {
                        query.sentOn = readCalendar2();
                    } else if (isStartTag("sentTo")) {
                        query.sentTo = readJabberIds2();
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
        ProviderManager.addIQProvider(Service.NAME, SendArtifact.EVENT_NAME, new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser2(parser);
                final HandleArtifactSentIQ query = new HandleArtifactSentIQ();
                Boolean isComplete = Boolean.FALSE;
                while(Boolean.FALSE == isComplete) {
                    if (isStartTag("uniqueId")) {
                        query.containerUniqueId = readUniqueId2();
                    } else if(isStartTag("versionId")) {
                        query.containerVersionId = readLong2();
                    } else if (isStartTag("name")) {
                        query.containerName = readString2();
                    } else if (isStartTag("artifactCount")) {
                        query.containerArtifactCount = readInteger2();
                    } else if (isStartTag("artifactIndex")) {
                        query.containerArtifactIndex = readInteger2();
                    } else if (isStartTag("artifactUniqueId")) {
                        query.artifactUniqueId = readUniqueId2();
                    } else if (isStartTag("artifactVersionId")) {
                        query.artifactVersionId = readLong2();
                    } else if (isStartTag("artifactName")) {
                        query.artifactName = readString2();
                    } else if (isStartTag("artifactType")) {
                        query.artifactType = readArtifactType2();
                    } else if (isStartTag("artifactChecksum")) {
                        query.artifactChecksum = readString2();
                    } else if (isStartTag("artifactBytes")) {
                        query.artifactBytes = readBytes2();
                    } else if (isStartTag("sentBy")) {
                        query.sentBy = readJabberId2();
                    } else if (isStartTag("sentOn")) {
                        query.sentOn = readCalendar2();
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
    }

    /** Core xmpp functionality. */
    private final XMPPCore core;

    /** Create XMPPContainer. */
    XMPPContainer(final XMPPCore core) {
        super();
        this.core = core;
    }

    /**
     * Add an xmpp container event listener.
     * 
     * @param l
     *            The xmpp container event listener.
     */
    void addListener(final XMPPContainerListener l) {
        logApiId();
        logVariable("l", l);
        synchronized(LISTENERS) {
            if (LISTENERS.contains(l)) {
                return;
            }
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
                        handleArtifactPublished((HandleArtifactPublishedIQ) packet);
                    }
                },
                new PacketTypeFilter(HandleArtifactPublishedIQ.class));
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        handlePublished((HandlePublishedIQ) packet);
                    }
                },
                new PacketTypeFilter(HandlePublishedIQ.class));
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        handleArtifactSent((HandleArtifactSentIQ) packet);
                    }
                },
                new PacketTypeFilter(HandleArtifactSentIQ.class));
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        handleSent((HandleSentIQ) packet);
                    }
                },
                new PacketTypeFilter(HandleSentIQ.class));
    }

    /**
     * Publish a container.
     * 
     * @param container
     *            A container.
     * @param documents
     *            A list of documents and their content.
     * @param publishTo
     *            Whom the container is to be published to.
     * @param publishedBy
     *            Who the container is published by.
     * @param publishedOn
     *            When the container is published.
     * @throws IOException
     */
    void publish(final ContainerVersion container,
            final Map<DocumentVersion, InputStream> documents,
            final List<JabberId> publishTo, final JabberId publishedBy,
            final Calendar publishedOn) throws IOException {
        logApiId();
        logVariable("container", container);
        logVariable("documents", documents);
        logVariable("publishTo", publishTo);
        logVariable("publishedBy", publishedBy);
        logVariable("publishedOn", publishedOn);
        int i = 0;
        final Set<Entry<DocumentVersion, InputStream>> entries = documents.entrySet();
        for (final Entry<DocumentVersion, InputStream> entry : entries) {
            // publish artifact
            final XMPPMethod publishArtifact = new XMPPMethod(Service.Container.PUBLISH_ARTIFACT);
            publishArtifact.setParameter("uniqueId", container.getArtifactUniqueId());
            publishArtifact.setParameter("versionId", container.getVersionId());
            publishArtifact.setParameter("name", container.getName());
            publishArtifact.setParameter("artifactCount", entries.size());
            publishArtifact.setParameter("artifactIndex", i++);
            publishArtifact.setParameter("artifactUniqueId", entry.getKey().getArtifactUniqueId());
            publishArtifact.setParameter("artifactVersionId", entry.getKey().getVersionId());
            publishArtifact.setParameter("artifactName", entry.getKey().getName());
            publishArtifact.setParameter("artifactType", entry.getKey().getArtifactType());
            publishArtifact.setParameter("artifactChecksum", entry.getKey().getChecksum());
            publishArtifact.setParameter("artifactBytes", StreamUtil.read(entry.getValue()));
            publishArtifact.setParameter("publishTo", "publishTo", publishTo);
            publishArtifact.setParameter("publishedBy", publishedBy);
            publishArtifact.setParameter("publishedOn", publishedOn);
            publishArtifact.execute(core.getConnection());
        }
        // publish
        final XMPPMethod publish = new XMPPMethod(Service.Container.PUBLISH);
        publish.setParameter("uniqueId", container.getArtifactUniqueId());
        publish.setParameter("versionId", container.getVersionId());
        publish.setParameter("name", container.getName());
        publish.setParameter("artifactCount", entries.size());
        publish.setParameter("publishedBy", publishedBy);
        publish.setParameter("publishedTo", "publishedTo", publishTo);
        publish.setParameter("publishedOn", publishedOn);
        publish.execute(core.getConnection());
    }

    /**
     * Send a container.
     * 
     * @param container
     *            A container.
     * @param documents
     *            A list of documents and their content.
     * @param publishTo
     *            Whom the container is to be published to.
     * @param publishedBy
     *            Who the container is published by.
     * @param publishedOn
     *            When the container is published.
     * @throws IOException
     */
    void send(final ContainerVersion container,
            final Map<DocumentVersion, InputStream> documents,
            final List<JabberId> sendTo, final JabberId sentBy,
            final Calendar sentOn) throws IOException {
        logApiId();
        logVariable("container", container);
        logVariable("documents", documents);
        logVariable("sendTo", sendTo);
        logVariable("sentBy", sentBy);
        logVariable("sentOn", sentOn);
        int i = 0;
        final Set<Entry<DocumentVersion, InputStream>> entries = documents.entrySet();
        for(final Entry<DocumentVersion, InputStream> entry : entries) {
            // send artifact
            final XMPPMethod sendArtifact = new XMPPMethod(Service.Container.SEND_ARTIFACT);
            sendArtifact.setParameter("uniqueId", container.getArtifactUniqueId());
            sendArtifact.setParameter("versionId", container.getVersionId());
            sendArtifact.setParameter("name", container.getName());
            sendArtifact.setParameter("artifactCount", entries.size());
            sendArtifact.setParameter("artifactIndex", i++);
            sendArtifact.setParameter("artifactUniqueId", entry.getKey().getArtifactUniqueId());
            sendArtifact.setParameter("artifactVersionId", entry.getKey().getVersionId());
            sendArtifact.setParameter("artifactName", entry.getKey().getName());
            sendArtifact.setParameter("artifactType", entry.getKey().getArtifactType());
            sendArtifact.setParameter("artifactChecksum", entry.getKey().getChecksum());
            sendArtifact.setParameter("artifactBytes", StreamUtil.read(entry.getValue()));
            sendArtifact.setParameter("sendTo", "sendTo", sendTo);
            sendArtifact.setParameter("sentBy", sentBy);
            sendArtifact.setParameter("sentOn", sentOn);
            sendArtifact.execute(core.getConnection());
        }
        final XMPPMethod send = new XMPPMethod(Service.Container.SEND);
        send.setParameter("uniqueId", container.getArtifactUniqueId());
        send.setParameter("versionId", container.getVersionId());
        send.setParameter("name", container.getName());
        send.setParameter("artifactCount", entries.size());
        send.setParameter("sentBy", sentBy);
        send.setParameter("sentTo", "sentTo", sendTo);
        send.setParameter("sentOn", sentOn);
        send.execute(core.getConnection());
    }

    /**
     * Handle the artifact published event generated by the remote model.
     *
     */
    private void handleArtifactPublished(final HandleArtifactPublishedIQ query) {
        synchronized (LISTENERS) {
            for(final XMPPContainerListener l : LISTENERS) {
                l.handleArtifactPublished(query.publishedBy, query.publishedOn,
                        query.containerUniqueId, query.containerVersionId,
                        query.containerName, query.containerArtifactCount,
                        query.containerArtifactIndex, query.artifactUniqueId,
                        query.artifactVersionId, query.artifactName,
                        query.artifactType, query.artifactChecksum,
                        query.artifactBytes);
            }
        }
    }

    /**
     * Handle the artifact sent event generated by the remote model.
     *
     */
    private void handleArtifactSent(final HandleArtifactSentIQ query) {
        synchronized(LISTENERS) {
            for(final XMPPContainerListener l : LISTENERS) {
                l.handleArtifactSent(query.sentBy, query.sentOn,
                        query.containerUniqueId, query.containerVersionId,
                        query.containerName, query.containerArtifactCount,
                        query.containerArtifactIndex, query.artifactUniqueId,
                        query.artifactVersionId, query.artifactName,
                        query.artifactType, query.artifactChecksum,
                        query.artifactBytes);
            }
        }
    }

    private void handleSent(final HandleSentIQ query) {
        synchronized (LISTENERS) {
            for (final XMPPContainerListener l : LISTENERS) {
                l.handleSent(query.uniqueId, query.versionId, query.name,
                        query.artifactCount, query.sentBy, query.sentOn,
                        query.sentTo);
            }
        }
    }

    /**
     * Handle the published event generated by the remote model.
     *
     */
    private void handlePublished(final HandlePublishedIQ query) {
        synchronized (LISTENERS) {
            for(final XMPPContainerListener l : LISTENERS) {
                l.handlePublished(query.uniqueId, query.versionId, query.name,
                        query.artifactCount, query.publishedBy,
                        query.publishedTo, query.publishedOn);
            }
        }
    }

    /**
     * <b>Title:</b>thinkParity XMPP Container Handler Reactivate Query <br>
     * <b>Description:</b>Provides a wrapper for data coming from the remote
     * event.
     * 
     * @see XMPPContainer#handleArtifactPublished(com.thinkparity.model.xmpp.XMPPContainer.HandleArtifactPublishedIQ)
     * @see XMPPContainer#addPacketListeners(XMPPConnection)
     */
    private static class HandleArtifactPublishedIQ extends HandleArtifactSentIQ {

        /** By whom the artifact was published. */
        private JabberId publishedBy;

        /** When the artifact was published. */
        private Calendar publishedOn;

        /** Create HandleArtifactPublishedIQ. */
        private HandleArtifactPublishedIQ() { super(); }
    }

    /**
     * <b>Title:</b>thinkParity XMPP Container Handle Artifact Sent Query <br>
     * <b>Description:</b>Provides a wrapper for data coming from the remote
     * event.
     * 
     * @see XMPPContainer#handleArtifactSent(com.thinkparity.model.xmpp.XMPPContainer.HandleArtifactPublishedIQ)
     * @see XMPPContainer#addPacketListeners(XMPPConnection)
     */
    private static class HandleArtifactSentIQ extends AbstractThinkParityIQ {

        /** The bytes. */
        protected byte[] artifactBytes;

        /** The checksum. */
        protected String artifactChecksum;

        /** The artifact name. */
        protected String artifactName;

        /** The type. */
        protected ArtifactType artifactType;

        /** The unique id. */
        protected UUID artifactUniqueId;

        /** The version id. */
        protected Long artifactVersionId;

        /** The artifact count. */
        protected Integer containerArtifactCount;

        /** The artifact index. */
        protected Integer containerArtifactIndex;

        /** The container name. */
        protected String containerName;

        /** The container unique id. */
        protected UUID containerUniqueId;

        /** The container version id. */
        protected Long containerVersionId;

        /** Who sent the artifact. */
        private JabberId sentBy;

        /** When the artifact was sent. */
        private Calendar sentOn;

        /** Create HandleArtifactSentIQ. */
        private HandleArtifactSentIQ() { super(); }
    }

    /**
     *  <b>Title:</b>thinkParity XMPP Container Handle Published Query <br>
     * <b>Description:</b>Provides a wrapper for data coming from the remote
     * event.
     */
    private static class HandlePublishedIQ extends AbstractThinkParityIQ {

        /** How many artifacts in the container. */
        private Integer artifactCount;

        /** The name of the container. */
        private String name;

        /** Who published the container. */
        private JabberId publishedBy;

        /** When the container was published. */
        private Calendar publishedOn;

        /** Who the container was published to. */
        private List<JabberId> publishedTo;

        /** The artifact unique id. */
        private UUID uniqueId;

        /** Which version was published. */
        private Long versionId;

        /** Create HandlePublishedIQ. */
        private HandlePublishedIQ() { super(); }
    }

    /**
     *  <b>Title:</b>thinkParity XMPP Container Handle Sent Query <br>
     * <b>Description:</b>Provides a wrapper for data coming from the remote
     * event.
     */
    private static class HandleSentIQ extends AbstractThinkParityIQ {

        /** The container unique id. */
        private UUID uniqueId;

        /** The container version. */
        private Long versionId;

        /** The container name. */
        private String name;

        /** The artifact count in the container. */
        private Integer artifactCount;

        /** Who sent the container. */
        private JabberId sentBy;

        /** When the container was sent. */
        private Calendar sentOn;

        /** Who the container was sent to. */
        private List<JabberId> sentTo;

        /** Create HandleSentIQ. */
        private HandleSentIQ() { super(); }
    }
}
