/*
 * Created On:  30-May-07 9:17:18 AM
 */
package com.thinkparity.service;

import java.util.List;
import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.PublishedToEMail;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

/**
 * <b>Title:</b>thinkParity Backup Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(name = "Backup")
public interface BackupService {

    @WebMethod
    void delete(AuthToken authToken, UUID containerUniqueId);

    @WebMethod
    Container readContainer(AuthToken authToken, UUID uniqueId);

    @WebMethod
    List<Container> readContainers(AuthToken authToken);

    @WebMethod
    List<ContainerVersion> readContainerVersions(AuthToken authToken,
            UUID uniqueId);

    @WebMethod
    List<Document> readDocuments(AuthToken authToken,
            UUID containerUniqueId, Long containerVersionId);

    @WebMethod
    List<DocumentVersion> readDocumentVersions(AuthToken authToken,
            UUID containerUniqueId, Long containerVersionId);

    @WebMethod
    List<ArtifactReceipt> readPublishedTo(AuthToken authToken,
            UUID containerUniqueId, Long containerVersionId);

    @WebMethod
    List<PublishedToEMail> readPublishedToEMails(AuthToken authToken,
            UUID containerUniqueId, Long containerVersionId);

    @WebMethod
    List<JabberId> readTeamIds(AuthToken authToken,
            UUID containerUniqueId);
}
