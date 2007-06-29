/*
 * Created On:  3-Jun-07 12:34:29 PM
 */
package com.thinkparity.desdemona.web.service;

import java.util.List;
import java.util.UUID;

import javax.jws.WebService;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.PublishedToEMail;
import com.thinkparity.codebase.model.backup.Statistics;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.desdemona.model.backup.BackupModel;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.BackupService;

/**
 * <b>Title:</b>thinkParity Desdemona Backup Service Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(endpointInterface = "com.thinkparity.service.BackupService")
public class BackupSEI extends ServiceSEI implements BackupService {

    /**
     * Create BackupSEI.
     *
     */
    public BackupSEI() {
        super();
    }

    /**
     * @see com.thinkparity.service.BackupService#delete(com.thinkparity.service.AuthToken, java.util.UUID)
     *
     */
    public void delete(final AuthToken authToken, final UUID containerUniqueId) {
        getModel(authToken).delete(containerUniqueId);
    }

    /**
     * @see com.thinkparity.service.BackupService#readContainer(com.thinkparity.service.AuthToken)
     *
     */
    public Container readContainer(final AuthToken authToken, final UUID uniqueId) {
        return getModel(authToken).readContainer(uniqueId);
    }

    /**
     * @see com.thinkparity.service.BackupService#readContainers(com.thinkparity.service.AuthToken)
     *
     */
    public List<Container> readContainers(final AuthToken authToken) {
        return getModel(authToken).readContainers();
    }

    /**
     * @see com.thinkparity.service.BackupService#readContainerVersions(com.thinkparity.service.AuthToken, java.util.UUID)
     *
     */
    public List<ContainerVersion> readContainerVersions(final AuthToken authToken,
            final UUID uniqueId) {
        return getModel(authToken).readContainerVersions(uniqueId);
    }

    /**
     * @see com.thinkparity.service.BackupService#readDocuments(com.thinkparity.service.AuthToken, java.util.UUID, java.lang.Long)
     *
     */
    public List<Document> readDocuments(final AuthToken authToken,
            final UUID containerUniqueId, final Long containerVersionId) {
        return getModel(authToken).readDocuments(containerUniqueId,
                containerVersionId);
    }

    /**
     * @see com.thinkparity.service.BackupService#readDocumentVersions(com.thinkparity.service.AuthToken, java.util.UUID, java.lang.Long)
     *
     */
    public List<DocumentVersion> readDocumentVersions(final AuthToken authToken,
            final UUID containerUniqueId, final Long containerVersionId) {
        return getModel(authToken).readDocumentVersions(containerUniqueId,
                containerVersionId);
    }

    /**
     * @see com.thinkparity.service.BackupService#readPublishedTo(com.thinkparity.service.AuthToken, java.util.UUID, java.lang.Long)
     *
     */
    public List<ArtifactReceipt> readPublishedTo(final AuthToken authToken,
            final UUID containerUniqueId, final Long containerVersionId) {
        return getModel(authToken).readPublishedTo(containerUniqueId,
                containerVersionId);
    }

    /**
     * @see com.thinkparity.service.BackupService#readPublishedToEMails(com.thinkparity.service.AuthToken, java.util.UUID, java.lang.Long)
     *
     */
    public List<PublishedToEMail> readPublishedToEMails(final AuthToken authToken,
            final UUID containerUniqueId, final Long containerVersionId) {
        return getModel(authToken).readPublishedToEMails(containerUniqueId,
                containerVersionId);
    }

    /**
     * @see com.thinkparity.service.BackupService#readStatistics(com.thinkparity.service.AuthToken)
     *
     */
    public Statistics readStatistics(final AuthToken authToken) {
        return getModel(authToken).readStatistics();
    }

    /**
     * @see com.thinkparity.service.BackupService#readTeamIds(com.thinkparity.service.AuthToken, java.util.UUID)
     *
     */
    public List<JabberId> readTeamIds(final AuthToken authToken,
            final UUID containerUniqueId) {
        return getModel(authToken).readTeamIds(containerUniqueId);
    }

    /**
     * Obtain an instance of a backup model for an authenticated user.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return An instance of <code>BackupModel</code>.
     */
    private BackupModel getModel(final AuthToken authToken) {
        return getModelFactory(authToken).getBackupModel();
    }
}
