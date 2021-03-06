/*
 * Created On:  30-May-07 9:05:21 AM
 */
package com.thinkparity.service;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.DraftExistsException;
import com.thinkparity.codebase.model.artifact.IllegalVersionException;

/**
 * <b>Title:</b>thinkParity Artifact Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(name = "Artifact")
public interface ArtifactService {

    @WebMethod
    void createDraft(AuthToken authToken, List<JabberId> team, UUID uniqueId,
            Long latestVersionId, Calendar createdOn)
            throws DraftExistsException, IllegalVersionException;

    @WebMethod
    void deleteDraft(AuthToken authToken, List<JabberId> team,
            UUID uniqueId, Calendar deletedOn);

    @WebMethod
    JabberId readKeyHolder(AuthToken authToken, UUID uniqueId);
}
