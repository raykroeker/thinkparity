/*
 * Created On: Sep 17, 2006 2:55:25 PM
 */
package com.thinkparity.desdemona.model.backup;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

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

    public void enqueueBackupEvent(final JabberId userId,
            final JabberId eventUserId);

    public List<TeamMember> readArtifactTeam(final UUID uniqueId);

    public Container readContainer(final UUID uniqueId);

    public List<DocumentVersion> readContainerDocumentVersions(
            final UUID uniqueId, final Long versionId);

    public ContainerVersion readContainerLatestVersion(final UUID uniqueId);

    public ContainerVersion readContainerVersion(final UUID uniqueId,
            final Long versionId);

    public void uploadDocumentVersion(final String streamId,
            final UUID uniqueId, final Long versionId);
}
