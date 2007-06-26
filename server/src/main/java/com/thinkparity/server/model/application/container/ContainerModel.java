/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.desdemona.model.container;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.annotation.ThinkParityAuthenticate;
import com.thinkparity.desdemona.util.AuthenticationType;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Container Model<br>
 * <b>Description:</b>A public desdemona model container interface.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.22
 */
public interface ContainerModel {

    /**
     * Publish a container version.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param documentVersions
     *            A <code>List</code> of <code>DocumentVersion</code>s
     *            belonging to version.
     * @param teamMembers
     *            A <code>List</code> of <code>TeamMember</code>s.
     * @param publishedBy
     *            A published by user id <code>JabberId</code>.
     * @param publishedOn
     *            A published on <code>Calendar</code>.
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
            final List<TeamMember> team, final Calendar publishedOn,
            final List<EMail> publishToEMails, final List<User> publishToUsers);

    @ThinkParityAuthenticate(AuthenticationType.USER)
    public void publishVersion(final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final List<TeamMember> team,
            final List<ArtifactReceipt> receivedBy, final Calendar publishedOn,
            final List<EMail> publishToEMails, final List<User> publishToUsers);
}

