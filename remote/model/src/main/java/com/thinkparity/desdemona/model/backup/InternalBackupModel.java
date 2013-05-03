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
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Internal Backup Interface<br>
 * <b>Description:</b>An internal application interface to the backup model.
 * The *Auth methods do not validate whether or not the model user (the user on
 * behalf of whom the current thread is executing) is "allowed" to see the
 * content. The definition of allowance is they must either:
 * <ul>
 * <li>Be a member of the content's team.
 * <li>Have archived the content.
 * </ul>
 * If the content is a document the appropriate container is checked.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InternalBackupModel extends BackupModel {

    Boolean isBackedUp(Artifact artifact);

    Boolean isBackupEnabled();

    List<TeamMember> readArtifactTeam(UUID uniqueId);

    Container readContainer(UUID uniqueId);

    Container readContainerAuth(UUID uniqueId);

    List<DocumentVersion> readContainerDocumentVersions(UUID uniqueId,
            Long versionId);

    List<DocumentVersion> readContainerDocumentVersionsAuth(UUID uniqueId,
            Long versionId);

    ContainerVersion readContainerLatestVersion(UUID uniqueId);

    List<Container> readContainersForDocumentAuth(Document document);

    ContainerVersion readContainerVersion(UUID uniqueId, Long versionId);

    ContainerVersion readContainerVersionAuth(UUID uniqueId, Long versionId);

    Document readDocumentAuth(UUID uniqueId);

    List<ArtifactReceipt> readPublishedToAuth(UUID uniqueId, Long versionId);
}
