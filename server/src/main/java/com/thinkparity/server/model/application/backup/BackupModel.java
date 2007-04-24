/*
 * Generated On: Oct 04 06 10:14:14 AM
 */
package com.thinkparity.desdemona.model.backup;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.backup.Statistics;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.desdemona.model.annotation.ThinkParityAuthenticate;
import com.thinkparity.desdemona.util.AuthenticationType;

/**
 * <b>Title:</b>thinkParity Backup Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
@ThinkParityAuthenticate(AuthenticationType.USER)
public interface BackupModel {

    /**
     * Archive an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    public void archive(final JabberId userId, final UUID uniqueId);

    /**
     * Upload a document version to the stream server using the provided stream
     * id. The stream can then be downloaded by a client.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     * @param uniqueId
     *            A document unique id <code>UUID</code>.
     * @param versionId
     *            A document version id <code>Long</code>.
     */
    public void createStream(final JabberId userId, final String streamId,
            final UUID uniqueId, final Long versionId);

    /**
     * Delete an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    public void delete(final JabberId userId, final UUID uniqueId);

    /**
     * Read a container.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>Container</code>.
     */
    public Container readContainer(final JabberId userId, final UUID uniqueId);

    /**
     * Read the container version's documents.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List</code> of <code>Document</code>s.
     */
    public List<Document> readContainerDocuments(final JabberId userId,
            final UUID uniqueId, final Long versionId);

    /**
     * Read the container version's document versions.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List</code> of <code>ContainerVersion</code>.
     */
    public List<DocumentVersion> readContainerDocumentVersions(
            final JabberId userId, final UUID uniqueId, final Long versionId);

    /**
     * Read the container's published to list.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List</code> of <code>ArtifactReceipt</code>s.
     */
    public List<ArtifactReceipt> readContainerPublishedTo(
            final JabberId userId, final UUID uniqueId, final Long versionId);

    /**
     * Read the containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List</code> of <code>Container</code>s.
     */
    public List<Container> readContainers(final JabberId userId);

    /**
     * Read the container's versions.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List</code> of <code>ContainerVersion</code>s.
     */
    public List<ContainerVersion> readContainerVersions(final JabberId userId, final UUID uniqueId);

    /**
     * Read the backup statistics.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return An instance of <code>Statistics</code>.
     */
    public Statistics readStatistics(final JabberId userId);

    /**
     * Read the artifact team from the backup.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    public List<JabberId> readTeamIds(final JabberId userId, final UUID uniqueId);

    /**
     * Restore an artifact. This will simply remove the archived flag within the
     * backup.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>Long</code>.
     */
    public void restore(final JabberId userId, final UUID uniqueId);
}
