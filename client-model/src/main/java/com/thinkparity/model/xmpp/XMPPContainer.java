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

import org.apache.log4j.Logger;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.codebase.StreamUtil;

import com.thinkparity.model.Constants.Xml;
import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.io.xmpp.XMPPMethod;
import com.thinkparity.model.smackx.packet.AbstractThinkParityIQ;
import com.thinkparity.model.smackx.packet.AbstractThinkParityIQProvider;
import com.thinkparity.model.xmpp.events.XMPPContainerListener;
import com.thinkparity.model.xmpp.user.User;

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

        ProviderManager.addIQProvider("query", Xml.EventHandler.Container.ARTIFACT_PUBLISHED, new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser(parser);
                final HandleArtifactPublishedIQ query = new HandleArtifactPublishedIQ();

                Boolean isComplete = Boolean.FALSE;
                while(Boolean.FALSE == isComplete) {
                    next(1);
                    if (isStartTag(Xml.Container.PUBLISHED_BY)) {
                        next(1);
                        query.publishedBy = readJabberId();
                        next(1);
                    }
                    else if (isEndTag(Xml.Container.PUBLISHED_BY)) {
                        next(1);  
                    } else if (isStartTag(Xml.Container.PUBLISHED_ON)) {
                        next(1);
                        query.publishedOn = readCalendar();
                        next(1);
                    }
                    else if (isEndTag(Xml.Container.PUBLISHED_ON)) {
                        next(1);
                    } else if (isStartTag(Xml.Container.CONTAINER_UNIQUE_ID)) {
                        next(1);
                        query.containerUniqueId = readUniqueId();
                        next(1);
                    } else if (isEndTag(Xml.Container.CONTAINER_UNIQUE_ID)) {
                        next(1);
                    } else if(isStartTag(Xml.Container.CONTAINER_VERSION_ID)) {
                        next(1);
                        query.containerVersionId = readLong();
                        next(1);
                    } else if (isEndTag(Xml.Container.CONTAINER_VERSION_ID)) {
                        next(1);
                    } else if (isStartTag(Xml.Container.ARTIFACT_COUNT)) {
                        next(1);
                        query.count = readInteger();
                        next(1);
                    } else if (isEndTag(Xml.Container.ARTIFACT_COUNT)) {
                        next(1);
                    } else if (isStartTag(Xml.Container.ARTIFACT_INDEX)) {
                        next(1);
                        query.index = readInteger();
                        next(1);
                    } else if (isEndTag(Xml.Container.ARTIFACT_INDEX)) {
                        next(1);
                    } else if (isStartTag(Xml.Artifact.UNIQUE_ID)) {
                        next(1);
                        query.uniqueId = readUniqueId();
                        next(1);
                    } else if (isEndTag(Xml.Artifact.UNIQUE_ID)) {
                        next(1);
                    } else if (isStartTag(Xml.Artifact.VERSION_ID)) {
                        next(1);
                        query.versionId = readLong();
                        next(1);
                    } else if (isEndTag(Xml.Artifact.VERSION_ID)) {
                        next(1);
                    } else if (isStartTag(Xml.Artifact.TYPE)) {
                        next(1);
                        query.type = readArtifactType();
                        next(1);
                    } else if (isEndTag(Xml.Artifact.TYPE)) {
                        next(1);
                    } else if (isStartTag(Xml.Artifact.BYTES)) {
                        next(1);
                        query.bytes = readBytes();
                        next(1);
                    } else if (isEndTag(Xml.Artifact.BYTES)) {
                        next(1);
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
        ProviderManager.addIQProvider("query", Xml.EventHandler.Container.ARTIFACT_SENT, new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser(parser);
                final HandleArtifactSentIQ query = new HandleArtifactSentIQ();

                Boolean isComplete = Boolean.FALSE;
                while(Boolean.FALSE == isComplete) {
                    next(1);
                    if (isStartTag(Xml.Container.SENT_BY)) {
                        next(1);
                        query.sentBy = readJabberId();
                        next(1);
                    } else if (isEndTag(Xml.Container.SENT_BY)) {
                        next(1);
                    } else if (isStartTag(Xml.Container.SENT_ON)) {
                        next(1);
                        query.sentOn = readCalendar();
                        next(1);
                    } else if (isEndTag(Xml.Container.SENT_ON)) {
                        next(1);
                    } else if (isStartTag(Xml.Container.CONTAINER_UNIQUE_ID)) {
                        next(1);
                        query.containerUniqueId = readUniqueId();
                        next(1);
                    } else if (isEndTag(Xml.Container.CONTAINER_UNIQUE_ID)) {
                        next(1);
                    } else if(isStartTag(Xml.Container.CONTAINER_VERSION_ID)) {
                        next(1);
                        query.containerVersionId = readLong();
                        next(1);
                    } else if (isEndTag(Xml.Container.CONTAINER_VERSION_ID)) {
                        next(1);
                    } else if (isStartTag(Xml.Container.CONTAINER_NAME)) {
                        next(1);
                        query.containerName = readString();
                        next(1);
                    } else if (isEndTag(Xml.Container.CONTAINER_NAME)) {
                        next(1);
                    } else if (isStartTag(Xml.Container.ARTIFACT_COUNT)) {
                        next(1);
                        query.count = readInteger();
                        next(1);
                    } else if (isEndTag(Xml.Container.ARTIFACT_COUNT)) {
                        next(1);
                    } else if (isStartTag(Xml.Container.ARTIFACT_INDEX)) {
                        next(1);
                        query.index = readInteger();
                        next(1);
                    } else if (isEndTag(Xml.Container.ARTIFACT_INDEX)) {
                        next(1);
                    } else if (isStartTag(Xml.Artifact.UNIQUE_ID)) {
                        next(1);
                        query.uniqueId = readUniqueId();
                        next(1);
                    } else if (isEndTag(Xml.Artifact.UNIQUE_ID)) {
                        next(1);
                    } else if (isStartTag(Xml.Artifact.VERSION_ID)) {
                        next(1);
                        query.versionId = readLong();
                        next(1);
                    } else if (isEndTag(Xml.Artifact.VERSION_ID)) {
                        next(1);
                    } else if (isStartTag(Xml.Artifact.NAME)) {
                        next(1);
                        query.name = readString();
                        next(1);
                    } else if (isEndTag(Xml.Artifact.NAME)) {
                        next(1);
                    } else if (isStartTag(Xml.Artifact.TYPE)) {
                        next(1);
                        query.type = readArtifactType();
                        next(1);
                    } else if (isEndTag(Xml.Artifact.TYPE)) {
                        next(1);
                    } else if (isStartTag(Xml.Artifact.BYTES)) {
                        next(1);
                        query.bytes = readBytes();
                        next(1);
                    } else if (isEndTag(Xml.Artifact.BYTES)) {
                        next(1);
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
    }

    /**
     * Obtain an api id.
     * 
     * @param api
     *            An api.
     * @return An api id.
     */
    private static StringBuffer getApiId(final String api) {
        return new StringBuffer("[XMPP] [CONTAINER] ").append(api);
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
                        handleArtifactPublished((HandleArtifactPublishedIQ) packet);
                    }
                },
                new PacketTypeFilter(HandleArtifactPublishedIQ.class));
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        logger.debug("packet:" + packet.toXML());
                        handleArtifactSent((HandleArtifactSentIQ) packet);
                    }
                },
                new PacketTypeFilter(HandleArtifactSentIQ.class));
    }

    /**
     * Send a container version.
     * 
     * @param version
     *            A version.
     * @param users
     *            A list of users.
     */
    void publish(final ContainerVersion version,
            final Map<DocumentVersion, InputStream> documentVersions,
            final JabberId publishedBy, final Calendar publishedOn)
            throws IOException {
        logger.info(getApiId("[PUBLISH]"));
        logger.debug(version);
        final XMPPMethod method = new XMPPMethod(Xml.Method.Container.PUBLISH);

        int i = 0;
        final Set<Entry<DocumentVersion, InputStream>> entries = documentVersions.entrySet();
        for(final Entry<DocumentVersion, InputStream> entry : entries) {
            method.setParameter(Xml.Container.PUBLISHED_BY, publishedBy);
            method.setParameter(Xml.Container.PUBLISHED_ON, publishedOn);
            method.setParameter(Xml.Container.CONTAINER_UNIQUE_ID, version.getArtifactUniqueId());
            method.setParameter(Xml.Container.CONTAINER_VERSION_ID, version.getVersionId());
            method.setParameter(Xml.Container.ARTIFACT_COUNT, entries.size());
            method.setParameter(Xml.Container.ARTIFACT_INDEX, i++);

            method.setParameter(Xml.Artifact.UNIQUE_ID, entry.getKey().getArtifactUniqueId());
            method.setParameter(Xml.Artifact.VERSION_ID, entry.getKey().getVersionId());
            method.setParameter(Xml.Artifact.TYPE, entry.getKey().getArtifactType());
            method.setParameter(Xml.Artifact.BYTES, StreamUtil.read(entry.getValue()));
            method.execute(core.getConnection());
        }
    }

    /**
     * Send a container.
     * 
     * @param version
     *            A container version.
     * @param documentVersions
     *            A list of document versions.
     * @param user
     *            A user.
     */
    void send(final ContainerVersion version,
            final Map<DocumentVersion, InputStream> documentVersions,
            final User user, final JabberId sentBy, final Calendar sentOn)
            throws IOException {
        logger.info(getApiId("[SEND]"));
        logger.debug(version);
        logger.debug(documentVersions);
        logger.debug(user);
        final XMPPMethod method = new XMPPMethod(Xml.Method.Container.SEND);

        int i = 0;
        final Set<Entry<DocumentVersion, InputStream>> entries = documentVersions.entrySet();
        for(final Entry<DocumentVersion, InputStream> entry : entries) {
            method.setParameter(Xml.User.JABBER_ID, user.getId());
            method.setParameter(Xml.Container.SENT_BY, sentBy);
            method.setParameter(Xml.Container.SENT_ON, sentOn);
            method.setParameter(Xml.Container.CONTAINER_UNIQUE_ID, version.getArtifactUniqueId());
            method.setParameter(Xml.Container.CONTAINER_VERSION_ID, version.getVersionId());
            method.setParameter(Xml.Container.CONTAINER_NAME, version.getName());
            method.setParameter(Xml.Container.ARTIFACT_COUNT, entries.size());
            method.setParameter(Xml.Container.ARTIFACT_INDEX, i++);

            method.setParameter(Xml.Artifact.UNIQUE_ID, entry.getKey().getArtifactUniqueId());
            method.setParameter(Xml.Artifact.VERSION_ID, entry.getKey().getVersionId());
            method.setParameter(Xml.Artifact.NAME, entry.getKey().getName());
            method.setParameter(Xml.Artifact.TYPE, entry.getKey().getArtifactType());
            method.setParameter(Xml.Artifact.BYTES, StreamUtil.read(entry.getValue()));
            method.execute(core.getConnection());
        }
    }

    /**
     * Handle the artifact published event generated by the remote model.
     *
     */
    private void handleArtifactPublished(final HandleArtifactPublishedIQ query) {
        synchronized(LISTENERS) {
            for(final XMPPContainerListener l : LISTENERS) {
                l.handleArtifactPublished(query.publishedBy, query.publishedOn,
                        query.containerUniqueId, query.containerVersionId,
                        query.count, query.index, query.uniqueId,
                        query.versionId, query.name, query.type, query.bytes);
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
                        query.containerName, query.count, query.index,
                        query.uniqueId, query.versionId, query.name,
                        query.type, query.bytes);
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
        protected byte[] bytes;

        /** The container unique id. */
        protected UUID containerUniqueId;

        /** The container version id. */
        protected Long containerVersionId;

        /** The artifact count. */
        protected Integer count;

        /** The artifact index. */
        protected Integer index;

        /** The type. */
        protected ArtifactType type;

        /** The unique id. */
        protected UUID uniqueId;

        /** The version id. */
        protected Long versionId;

        /** The container name. */
        private String containerName;

        /** The artifact name. */
        protected String name;

        /** Who sent the artifact. */
        private JabberId sentBy;

        /** When the artifact was sent. */
        private Calendar sentOn;

        /** Create HandleArtifactPublishedIQ. */
        private HandleArtifactSentIQ() { super(); }
    }
}
