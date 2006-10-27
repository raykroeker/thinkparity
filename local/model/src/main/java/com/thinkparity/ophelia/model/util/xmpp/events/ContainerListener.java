/*
 * Apr 5, 2006
 */
package com.thinkparity.ophelia.model.util.xmpp.events;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface ContainerListener extends EventListener {
    public void handleArtifactPublished(final JabberId publishedBy,
            final Calendar publishedOn, final UUID containerUniqueId,
            final Long containerVersionId, final String containerName,
            final Integer containerArtifactCount,
            final Integer containerArtifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final String artifactStreamId);
    public void handlePublished(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final JabberId publishedBy, final List<JabberId> publishedTo,
            final Calendar publishedOn);
}
