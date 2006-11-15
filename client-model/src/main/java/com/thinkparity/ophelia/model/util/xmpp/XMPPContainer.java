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

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.Constants.Xml.Service;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.util.xmpp.event.ContainerListener;

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
}
