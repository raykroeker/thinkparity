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
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
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

    /**
     * Confirm receipt of a container version.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param version
     *            A <code>ContainerVersion</code>.
     */
    @WebMethod
    void confirmReceipt(AuthToken authToken, ContainerVersion version,
            Calendar publishedOn, Calendar receivedOn);

    /**
     * Delete a container.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param container
     *            A <code>Container</code>.
     * @param deletedOn
     *            A deleted on <code>Calendar</code>.
     */
    @WebMethod
    void delete(AuthToken authToken, Container container, Calendar deletedOn);

    /**
     * Publish a new version of a container.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param documentVersions
     *            A <code>List<DocumentVersion></code>s.
     * @param publishToEMails
     *            A <code>List<EMail></code>.
     * @param publishToUsers
     *            A <code>List<User></code>.
     */
    @WebMethod
    void publish(AuthToken authToken, ContainerVersion version,
            List<DocumentVersion> documentVersions,
            List<EMail> publishToEMails, List<User> publishToUsers);

    /**
     * Publish an existing version of a container.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param documentVersions
     *            A <code>List<DocumentVersion></code>s.
     * @param receivedBy
     *            A <code>List<ArtifactReceipt></code>.
     * @param publishedOn
     *            A published on <code>Calendar</code>.
     * @param publishToEMails
     *            A <code>List<EMail></code>.
     * @param publishToUsers
     *            A <code>List<User></code>.
     */
    @WebMethod
    void publishVersion(AuthToken authToken, ContainerVersion version,
            List<DocumentVersion> documentVersions,
            List<ArtifactReceipt> receivedBy, Calendar publishedOn,
            List<EMail> publishToEMails, List<User> publishToUsers);

    /**
     * Publish a welcome container.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     */
    @WebMethod
    void publishWelcome(AuthToken authToken);
}
