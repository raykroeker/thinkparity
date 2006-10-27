/*
 * Created On: Jul 6, 2006 11:51:59 AM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.Constants.Xml.Service;
import com.thinkparity.ophelia.model.Constants.Xml.Container.Method.Publish;
import com.thinkparity.ophelia.model.Constants.Xml.Container.Method.PublishArtifact;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.util.smackx.packet.AbstractThinkParityIQ;
import com.thinkparity.ophelia.model.util.smackx.packet.AbstractThinkParityIQProvider;
import com.thinkparity.ophelia.model.util.xmpp.events.ContainerListener;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmlpull.v1.XmlPullParser;

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
final class XMPPContainer extends AbstractXMPP<ContainerListener> {

    static {
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
                    } else if (isStartTag("artifactStreamId")) {
                        query.artifactStreamId = readString2();
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
    }

    /** Create XMPPContainer. */
    XMPPContainer(final XMPPCore xmppCore) {
        super(xmppCore);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.AbstractXMPP#addEventHandlers()
     */
    @Override
    protected void addEventHandlers() {
        addEventHandler(new XMPPEventHandler<HandleArtifactPublishedIQ>() {
            public void handleEvent(final HandleArtifactPublishedIQ query) {
                handleArtifactPublished(query);
            }
        }, HandleArtifactPublishedIQ.class);
        addEventHandler(new XMPPEventHandler<HandlePublishedIQ>() {
            public void handleEvent(final HandlePublishedIQ query) {
                handlePublished(query);
            }
        }, HandlePublishedIQ.class);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.AbstractXMPP#addListener(com.thinkparity.ophelia.model.util.xmpp.events.XMPPEventListener)
     */
    @Override
    protected boolean addListener(final ContainerListener listener) {
        return super.addListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.AbstractXMPP#removeListener(com.thinkparity.ophelia.model.util.xmpp.events.XMPPEventListener)
     */
    @Override
    protected boolean removeListener(final ContainerListener listener) {
        return super.removeListener(listener);
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
            final Map<DocumentVersion, String> documents,
            final List<JabberId> publishTo, final JabberId publishedBy,
            final Calendar publishedOn) throws IOException {
        logger.logApiId();
        logger.logVariable("container", container);
        logger.logVariable("documents", documents);
        logger.logVariable("publishTo", publishTo);
        logger.logVariable("publishedBy", publishedBy);
        logger.logVariable("publishedOn", publishedOn);
        int i = 0;
        final Set<Entry<DocumentVersion, String>> entries = documents.entrySet();
        for (final Entry<DocumentVersion, String> entry : entries) {
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
            publishArtifact.setParameter("artifactStreamId", entry.getValue());
            publishArtifact.setParameter("publishTo", "publishTo", publishTo);
            publishArtifact.setParameter("publishedBy", publishedBy);
            publishArtifact.setParameter("publishedOn", publishedOn);
            execute(publishArtifact);
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
        execute(publish);
    }

    /**
     * Handle the artifact published event generated by the remote model.
     *
     */
    private void handleArtifactPublished(final HandleArtifactPublishedIQ query) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.handleArtifactPublished(query.publishedBy, query.publishedOn,
                        query.containerUniqueId, query.containerVersionId,
                        query.containerName, query.containerArtifactCount,
                        query.containerArtifactIndex, query.artifactUniqueId,
                        query.artifactVersionId, query.artifactName,
                        query.artifactType, query.artifactChecksum,
                        query.artifactStreamId);
            }
        });
    }

    /**
     * Handle the published event generated by the remote model.
     *
     */
    private void handlePublished(final HandlePublishedIQ query) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.handlePublished(query.uniqueId, query.versionId, query.name,
                        query.artifactCount, query.publishedBy,
                        query.publishedTo, query.publishedOn);
            }
        });
    }

    /**
     * <b>Title:</b>thinkParity XMPP Container Handler Reactivate Query <br>
     * <b>Description:</b>Provides a wrapper for data coming from the remote
     * event.
     * 
     * @see XMPPContainer#handleArtifactPublished(com.thinkparity.ophelia.model.util.xmpp.XMPPContainer.HandleArtifactPublishedIQ)
     * @see XMPPContainer#addEventListeners(XMPPConnection)
     */
    private static class HandleArtifactPublishedIQ extends AbstractThinkParityIQ {

        /** The checksum. */
        protected String artifactChecksum;

        /** The artifact name. */
        protected String artifactName;

        /** The artifact steam id <code>String</code>. */
        protected String artifactStreamId;

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

        /** By whom the artifact was published. */
        private JabberId publishedBy;

        /** When the artifact was published. */
        private Calendar publishedOn;

        /** Create HandleArtifactPublishedIQ. */
        private HandleArtifactPublishedIQ() { super(); }
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
}
