/*
 * Apr 5, 2006
 */
package com.thinkparity.model.xmpp.events;

import java.util.Calendar;
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
            final Long containerVersionId, final Integer count,
            final Integer index, final UUID uniqueId, final Long versionId,
            final String name, final ArtifactType type, final byte[] bytes);
    public void handleArtifactSent(final JabberId sentBy,
            final Calendar sentOn, final UUID containerUniqueId,
            final Long containerVersionId, final String containerName,
            final Integer count, final Integer index, final UUID uniqueId,
            final Long versionId, final String name, final ArtifactType type,
            final byte[] bytes);
}
