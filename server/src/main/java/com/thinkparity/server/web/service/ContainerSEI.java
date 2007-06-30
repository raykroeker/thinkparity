/*
 * Created On:  3-Jun-07 1:45:01 PM
 */
package com.thinkparity.desdemona.web.service;

import java.util.Calendar;
import java.util.List;

import javax.jws.WebService;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.container.ContainerModel;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.ContainerService;

/**
 * <b>Title:</b>thinkParity Desdemona Container Service Endpoint Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(endpointInterface = "com.thinkparity.service.ContainerService")
public class ContainerSEI extends ServiceSEI implements ContainerService {

    /**
     * Create ContainerSEI.
     *
     */
    public ContainerSEI() {
        super();
    }

    /**
     * @see com.thinkparity.service.ContainerService#archive(com.thinkparity.service.AuthToken, com.thinkparity.codebase.model.container.Container)
     *
     */
    public void archive(final AuthToken authToken, final Container container) {
        getModel(authToken).archive(container);
    }

    /**
     * @see com.thinkparity.service.ContainerService#confirmReceipt(AuthToken,
     *      ContainerVersion, Calendar, Calendar)
     * 
     */
    public void confirmReceipt(final AuthToken authToken,
            final ContainerVersion version, final Calendar publishedOn,
            final Calendar receivedOn) {
        getModel(authToken).confirmReceipt(version, publishedOn, receivedOn);
    }

    /**
     * @see com.thinkparity.service.ContainerService#delete(com.thinkparity.service.AuthToken, com.thinkparity.codebase.model.container.Container)
     *
     */
    public void delete(final AuthToken authToken, final Container container,
            final Calendar deletedOn) {
        getModel(authToken).delete(container, deletedOn);
    }

    /**
     * @see com.thinkparity.service.ContainerService#publish(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.List, java.util.List, java.util.Calendar, java.util.List,
     *      java.util.List)
     * 
     */
    public void publish(final AuthToken authToken,
            final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final List<EMail> publishToEMails, final List<User> publishToUsers) {
        getModel(authToken).publish(version, documentVersions, publishToEMails,
                publishToUsers);
    }

    /**
     * @see com.thinkparity.service.ContainerService#publishVersion(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.List, java.util.List, java.util.List, java.util.Calendar,
     *      java.util.List, java.util.List)
     * 
     */
    public void publishVersion(final AuthToken authToken,
            final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final List<ArtifactReceipt> receivedBy, final Calendar publishedOn,
            final List<EMail> publishToEMails, final List<User> publishToUsers) {
        getModel(authToken).publishVersion(version, documentVersions,
                receivedBy, publishedOn, publishToEMails, publishToUsers);
    }

    /**
     * @see com.thinkparity.service.ContainerService#restore(com.thinkparity.service.AuthToken, com.thinkparity.codebase.model.container.Container)
     *
     */
    public void restore(final AuthToken authToken, final Container container) {
        getModel(authToken).restore(container);
    }

    /**
     * Obtain a container model for an authenticated user.
     * 
     * @param authToken
     *            An <code>AuthToken</code>.
     * @return An instance of <code>ContainerModel</code>.
     */
    private ContainerModel getModel(final AuthToken authToken) {
        return getModelFactory(authToken).getContainerModel();
    }
}
