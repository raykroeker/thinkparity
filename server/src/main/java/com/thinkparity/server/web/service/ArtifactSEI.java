/*
 * Created On:  3-Jun-07 11:48:32 AM
 */
package com.thinkparity.desdemona.web.service;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.jws.WebService;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.DraftExistsException;

import com.thinkparity.desdemona.model.artifact.ArtifactModel;

import com.thinkparity.service.ArtifactService;
import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Artifact Service Endpoint Implementation<br>
 * <b>Description:</b>A service implementation of the artifact services.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(endpointInterface = "com.thinkparity.service.ArtifactService")
public class ArtifactSEI extends ServiceSEI implements ArtifactService {

    /**
     * Create ArtifactSEI.
     *
     */
    public ArtifactSEI() {
        super();
    }

    /**
     * @see com.thinkparity.service.ArtifactService#confirmReceipt(com.thinkparity.service.AuthToken,
     *      java.util.UUID, java.lang.Long,
     *      com.thinkparity.codebase.jabber.JabberId, java.util.Calendar,
     *      java.util.List, java.util.Calendar)
     * 
     */
    public void confirmReceipt(final AuthToken authToken, final UUID uniqueId,
            final Long versionId, final JabberId publishedBy,
            final Calendar publishedOn, final List<JabberId> publishedTo,
            final Calendar receivedOn) {
        getModel(authToken).confirmReceipt(uniqueId, versionId, publishedBy,
                publishedOn, publishedTo, receivedOn);
    }

    /**
     * @see com.thinkparity.service.ArtifactService#createDraft(com.thinkparity.service.AuthToken,
     *      java.util.List, java.util.UUID, java.util.Calendar)
     * 
     */
    public void createDraft(final AuthToken authToken, final List<JabberId> team,
            final UUID uniqueId, final Calendar createdOn) throws DraftExistsException {
        getModel(authToken).createDraft(team, uniqueId, createdOn);
    }

    /**
     * @see com.thinkparity.service.ArtifactService#deleteDraft(com.thinkparity.service.AuthToken,
     *      java.util.List, java.util.UUID, java.util.Calendar)
     * 
     */
    public void deleteDraft(final AuthToken authToken, final List<JabberId> team,
            final UUID uniqueId, final Calendar deletedOn) {
        getModel(authToken).deleteDraft(team, uniqueId, deletedOn);
    }

    /**
     * @see com.thinkparity.service.ArtifactService#readKeyHolder(com.thinkparity.service.AuthToken,
     *      java.util.UUID)
     * 
     */
    public JabberId readKeyHolder(final AuthToken authToken, final UUID uniqueId) {
        return getModel(authToken).readKeyHolder(uniqueId);
    }

    /**
     * Obtain an artifact model for an authenticated user.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return An instance of <code>ArtifactModel</code>.
     */
    private ArtifactModel getModel(final AuthToken authToken) {
        return getModelFactory(authToken).getArtifactModel();
    }
}
