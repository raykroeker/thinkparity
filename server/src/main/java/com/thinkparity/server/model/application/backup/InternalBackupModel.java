/*
 * Created On: Sep 17, 2006 2:55:25 PM
 */
package com.thinkparity.desdemona.model.backup;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Internal Backup Interface<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InternalBackupModel extends BackupModel {

    /**
     * Archive an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    public void archive(final UUID uniqueId);

    /**
     * Enqueue a backup event for the model user.
     *
     */
    public void enqueueBackupEvent();

    public Boolean isArchived(final Artifact artifact);

    public Boolean isBackedUp(final Artifact artifact);

    public Boolean isBackupEnabled();

    public List<TeamMember> readArtifactTeam(final UUID uniqueId);

    public Container readContainer(final UUID uniqueId);

    public List<DocumentVersion> readContainerDocumentVersions(
            final UUID uniqueId, final Long versionId);

    public ContainerVersion readContainerLatestVersion(final UUID uniqueId);

    public ContainerVersion readContainerVersion(final UUID uniqueId,
            final Long versionId);

    public List<ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId);

    /**
     * Restore an artifact. This will simply remove the archived flag within the
     * backup.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>Long</code>.
     */
    public void restore(final UUID containerUniqueId);
}
