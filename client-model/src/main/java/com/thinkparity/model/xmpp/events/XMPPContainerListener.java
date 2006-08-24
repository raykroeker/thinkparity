/*
 * Apr 5, 2006
 */
package com.thinkparity.model.xmpp.events;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface XMPPContainerListener {
    public void handleArtifactPublished(final JabberId publishedBy,
            final Calendar publishedOn, final UUID containerUniqueId,
            final Long containerVersionId, final String containerName,
            final Integer containerArtifactCount,
            final Integer containerArtifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final byte[] artifactBytes);
    public void handleArtifactSent(final JabberId sentBy,
            final Calendar sentOn, final UUID containerUniqueId,
            final Long containerVersionId, final String containerName,
            final Integer containerArtifactCount,
            final Integer containerArtifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final byte[] artifactBytes);
    public void handleSent(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final JabberId sentBy, final Calendar sentOn,
            final List<JabberId> sentTo);
    public void handlePublished(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final JabberId publishedBy, final List<JabberId> publishedTo,
            final Calendar publishedOn);
}
