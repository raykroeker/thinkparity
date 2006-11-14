/*
 * Created On: Jul 6, 2006 11:51:59 AM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerPublishedEvent;

import com.thinkparity.ophelia.model.Constants.Xml.Service;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.util.xmpp.event.ContainerListener;
import com.thinkparity.ophelia.model.util.xmpp.event.XMPPEventHandler;

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

    /**
     * Create XMPPContainer.
     * 
     */
    XMPPContainer(final XMPPCore xmppCore) {
        super(xmppCore);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.AbstractXMPP#addListener(com.thinkparity.codebase.event.EventListener)
     *
     */
    @Override
    protected boolean addListener(final ContainerListener listener) {
        return super.addListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.AbstractXMPP#registerEventHandlers()
     *
     */
    @Override
    protected void registerEventHandlers() {
        registerEventHandler(ContainerArtifactPublishedEvent.class,
                new XMPPEventHandler<ContainerArtifactPublishedEvent>() {
                    public void handleEvent(
                            final ContainerArtifactPublishedEvent query) {
                        handleArtifactPublished(query);
                    }});
        registerEventHandler(ContainerPublishedEvent.class,
                new XMPPEventHandler<ContainerPublishedEvent>() {
                    public void handleEvent(final ContainerPublishedEvent query) {
                        handlePublished(query);
                    }});
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.AbstractXMPP#removeListener(com.thinkparity.codebase.event.EventListener)
     *
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
     * @param team
     *            A <code>JabberId</code> <code>List</code> of the team.
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
            final List<JabberId> team, final List<JabberId> publishTo,
            final JabberId publishedBy, final Calendar publishedOn) {
        logger.logApiId();
        logger.logVariable("container", container);
        logger.logVariable("documents", documents);
        logger.logVariable("team", team);
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
        publish.setParameter("comment", container.getComment());
        publish.setParameter("team", "teamMember", team);
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
    private void handleArtifactPublished(final ContainerArtifactPublishedEvent event) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.handleArtifactPublished(event);
            }
        });
    }

    /**
     * Handle the published event generated by the remote model.
     *
     */
    private void handlePublished(final ContainerPublishedEvent event) {
        notifyListeners(new EventNotifier<ContainerListener>() {
            public void notifyListener(final ContainerListener listener) {
                listener.handlePublished(event);
            }
        });
    }
}
