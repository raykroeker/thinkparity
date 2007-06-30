/*
 * Created On:  27-Apr-07 9:36:25 AM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.container.monitor.PublishStep;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;

/**
 * <b>Title:</b>thinkParity OpheliaModel Publish Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PublishVersion extends ContainerDelegate {

    /** A list of contacts to publish to. */
    private List<Contact> contacts;

    /** The container id of the container to publish. */
    private Long containerId;

    /** A list of e-mail addresses to publish to. */
    private List<EMail> emails;

    /** A publish <code>ProcessMonitor</code>. */
    private ProcessMonitor monitor;

    /** A list of team members to publish to. */
    private List<TeamMember> teamMembers;

    /** The container version id of the container to publish. */
    private Long versionId;

    /**
     * Create PublishVersionDelegate.
     *
     */
    public PublishVersion() {
        super();
    }

    /**
     * Publish version.
     *
     */
    public void publishVersion() {
        // start monitor
        notifyProcessBegin(monitor);
        final InternalSessionModel sessionModel = getSessionModel();
        final Calendar publishedOn = sessionModel.readDateTime();
        final ContainerVersion version = readVersion(containerId, versionId);
        final List<User> publishToUsers = new ArrayList<User>();
        // build the team ids and the publish to list
        final List<JabberId> teamMemberIds =
            getArtifactModel().readTeamIds(containerId);
        for (final Contact contact : contacts) {
            publishToUsers.add(contact);
            teamMemberIds.add(contact.getId());
        }
        for (final TeamMember teamMember : teamMembers) {
            if (!contains(publishToUsers, teamMember)) {
                publishToUsers.add(teamMember);
                
            }
        }
        // only create a published to reference if one for the user does not
        // already exist
        final List<User> publishedTo = containerIO.readPublishedTo(containerId,
                versionId);
        for (final User publishToUser : publishToUsers) {
            if (!contains(publishedTo, publishToUser)) {
                containerIO.createPublishedTo(containerId, versionId,
                        publishToUser, publishedOn);
            }
        }
        // include the users who have already received the package
        final List<ArtifactReceipt> receivedBy =
            containerIO.readPublishedToReceipts(containerId, versionId);
        final Iterator<ArtifactReceipt> iReceivedBy = receivedBy.iterator();
        while (iReceivedBy.hasNext()) {
            if (!iReceivedBy.next().isSetReceivedOn().booleanValue()) {
                iReceivedBy.remove();
            }
        }
        // upload
        final List<DocumentVersion> documentVersions = readDocumentVersions(
                version.getArtifactId(), version.getVersionId(),
                new ComparatorBuilder().createVersionById(Boolean.TRUE));
        final ContainerVersion previous = readPreviousVersion(containerId, versionId);
        final Map<DocumentVersion, Delta> deltas;
        if (null == previous) {
            deltas = readDocumentVersionDeltas(containerId, versionId);
        } else {
            deltas = readDocumentVersionDeltas(containerId, versionId,
                    previous.getVersionId());
        }
        uploadDocumentVersions(monitor, documentVersions, deltas);
        notifyStepBegin(monitor, PublishStep.PUBLISH);
        sessionModel.publishVersion(version, documentVersions, receivedBy,
                publishedOn, emails, publishToUsers);
        notifyStepEnd(monitor, PublishStep.PUBLISH);
    }

    /**
     * Set the contacts.
     * 
     * @param contacts
     *            A <code>List</code> of <code>Contact<code>s.
     */
    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    /**
     * Set the container id.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    /**
     * Set the emails.
     * 
     * @param emails
     *            A <code>List</code> of <code>EMail</code> addresses.
     */
    public void setEmails(List<EMail> emails) {
        this.emails = emails;
    }

    /**
     * Set the process monitor.
     *
     * @param monitor
     *		A <code>ProcessMonitor</code>.
     */
    public void setMonitor(final ProcessMonitor monitor) {
        this.monitor = monitor;
    }

    /**
     * Set teamMembers.
     *
     * @param teamMembers
     *		A List<TeamMember>.
     */
    public void setTeamMembers(List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    /**
     * Set the container version id.
     * 
     * @param versionId
     *            A version id <code>Long</code>.
     */
    public void setVersionId(final Long versionId) {
        this.versionId = versionId;
    }
}
