/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.desdemona.model.container;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.annotation.ThinkParityAuthenticate;
import com.thinkparity.desdemona.util.AuthenticationType;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Container Model<br>
 * <b>Description:</b>A public desdemona model container interface.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.30
 */
public interface ContainerModel {

    /**
     * Archive the container.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    public void archive(final Container container);

    /**
     * Confirm receipt of a container version.
     * 
     * @param container
     *            A <code>ContainerVersion</code>.
     * @param publishedOn
     *            A published on <code>Calendar</code>.
     * @param receivedOn
     *            A received on <code>Calendar</code>.
     */
    public void confirmReceipt(final ContainerVersion version,
            final Calendar publishedOn, final Calendar receivedOn);

    /**
     * Delete a container.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param deletedOn
     *            A deleted on <code>Calendar</code>.
     */
    public void delete(final Container container, final Calendar deletedOn);

    /**
     * Publish a container version for the model user.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param documentVersions
     *            A <code>List</code> of <code>DocumentVersion</code>s.
     * @param publishToEMails
     *            A <code>List</code> of <code>EMail</code> addresses to
     *            publish to.  It is assumed that the e-mail addresses do not
     *            belong to a contact of the user performing the publish.
     * @param publishToUsers
     *            A <code>List</code> of <code>User</code>s to publish to.
     */
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public void publish(final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final List<EMail> publishToEMails, final List<User> publishToUsers);

    /**
     * Publish an existing version of a package for the model user.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param documentVersions
     *            A <code>List<DocumentVersion></code>.
     * @param receivedBy
     *            A <code>List<ArtifactReceipt></code>.
     * @param publishedOn
     *            A published on <code>Calendar</code>.
     * @param publishToEMails
     *            A <code>List<EMail></code>.
     * @param publishToUsers
     *            A <code>List<User></code>.
     */
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public void publishVersion(final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final List<ArtifactReceipt> receivedBy, final Calendar publishedOn,
            final List<EMail> publishToEMails, final List<User> publishToUsers);

    /**
     * Restore the container.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    public void restore(final Container container);
}

