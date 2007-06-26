/*
 * Created On:  30-May-07 9:40:22 AM
 */
package com.thinkparity.service;

import java.util.Calendar;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity Container Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(name = "Container")
public interface ContainerService {

    @WebMethod
    void publish(AuthToken authToken, ContainerVersion version,
            List<DocumentVersion> documentVersions, List<TeamMember> team,
            Calendar publishedOn, List<EMail> publishToEMails,
            List<User> publishToUsers);

    @WebMethod
    void publishVersion(AuthToken authToken, ContainerVersion version,
            List<DocumentVersion> documentVersions, List<TeamMember> team,
            List<ArtifactReceipt> receivedBy, Calendar publishedOn,
            List<EMail> publishToEMails, List<User> publishToUsers);
}
