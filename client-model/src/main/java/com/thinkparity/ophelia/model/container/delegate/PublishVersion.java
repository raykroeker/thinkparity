/*
 * Created On:  27-Apr-07 9:36:25 AM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;

/**
 * <b>Title:</b>thinkParity OpheliaModel Publish Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PublishVersion extends ContainerDelegate {

    /** The container id of the container to publish. */
    private Long containerId;

    /** A list of e-mail addresses to publish to. */
    private List<EMail> emails;

    /** A list of outgoing e-mail invitations generated by publish. */
    private final List<OutgoingEMailInvitation> invitations;

    /** A list of team members to publish to. */
    private List<TeamMember> teamMembers;

    /** The publish to user list. */
    private final List<User> users;

    /** The container version id of the container to publish. */
    private Long versionId;

    /**
     * Create PublishVersionDelegate.
     *
     */
    public PublishVersion() {
        super();
        this.invitations = new ArrayList<OutgoingEMailInvitation>();
        this.users = new ArrayList<User>();
    }

    /**
     * Obtain invitations.
     *
     * @return A <code>List<OutgoingEMailInvitation></code>.
     */
    public List<OutgoingEMailInvitation> getInvitations() {
        return Collections.unmodifiableList(invitations);
    }
    
    /**
     * Publish version.
     *
     */
    public void publishVersion() {
        assertIsPublishable();
        final InternalSessionModel sessionModel = getSessionModel();
        final Calendar publishedOn = sessionModel.readDateTime();
        final ContainerVersion version = readVersion(containerId, versionId);
        // only create a published to reference if one for the user does not
        // already exist
        final List<User> publishedTo = containerIO.readPublishedTo(containerId,
                versionId);
        for (final User user : users) {
            if (!contains(publishedTo, user)) {
                containerIO.createPublishedTo(containerId, versionId, user,
                        publishedOn);
            }
        }
        /* the remote publish-version invocation will potentially generate new
         * outgoing e-mail invitations that need to be pre-created */
        final InternalContactModel contactModel = getContactModel();
        OutgoingEMailInvitation invitation;
        for (final EMail email : emails) {
            if (contactModel.doesExistOutgoingEMailInvitation(email).booleanValue()) {
                invitation = contactModel.readOutgoingEMailInvitation(email);
            } else {
                invitation = contactModel.createLocalOutgoingEMailInvitation(
                                email, publishedOn);
                invitations.add(invitation);
            }
            containerIO.createPublishedTo(version.getArtifactId(),
                    version.getVersionId(), email, publishedOn);
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
        // publish
        final List<DocumentVersion> versions = readDocumentVersions(
                version.getArtifactId(), version.getVersionId(),
                new ComparatorBuilder().createVersionById(Boolean.TRUE));
        sessionModel.publishVersion(version, versions, receivedBy,
                publishedOn, emails, users);
    }

    /**
     * Set the contacts.
     * 
     * @param contacts
     *            A <code>List</code> of <code>Contact<code>s.
     */
    public void setContacts(final List<Contact> contacts) {
        this.users.addAll(contacts);
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
     * Set teamMembers.
     *
     * @param teamMembers
     *		A List<TeamMember>.
     */
    public void setTeamMembers(final List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
        this.users.addAll(teamMembers);
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

    /**
     * Ensure the e-mail address is not already tied to a contact.
     * 
     * @param emails
     *            An <code>EMail</code> address.
     */
    private void assertIsNotContact(final List<EMail> emails) {
        final InternalContactModel contactModel = getContactModel();
        for (final EMail email : emails) {
            Assert.assertNotTrue(contactModel.doesExist(email),
                    "A contact for {0} already exists.", email);
        }
    }

    /**
     * Ensure that publish can proceed. A check is made for the following
     * criteria:
     * <ol>
     * <li>The local user is not in the team member list.
     * <li>There exists a local draft.
     * <li>The local draft has been saved.
     * <li>The local draft differs from the most recent version.
     * <li>None of the e-mail addresses are contacts.
     * <li>None of the emails/contacts/team members have been restricted
     * publish.
     * </ol>
     */
    private void assertIsPublishable() {
        final List<ProfileEMail> profileEMails = getProfileModel().readEmails();
        Assert.assertNotTrue(contains(profileEMails, emails), "The local user cannot be published to.");
        Assert.assertNotTrue(contains(teamMembers, localUser()), "The local user cannot be published to.");
        Assert.assertTrue(doesExistLocalDraft(containerId), "A local draft does not exist.");
        Assert.assertTrue(isLocalDraftSaved(containerId), "The local draft has not been saved.");
        Assert.assertTrue(isLocalDraftModified(containerId), "The local draft has not been modified.");
        assertIsNotContact(emails);
        Assert.assertNotTrue(isPublishRestricted(), "The user cannot publish to the specified e-mails/users.");
    }

    /**
     * Determine if the user is restricted from publishing.
     * 
     * @return True if the user is restricted.
     */
    private Boolean isPublishRestricted() {
        return getSessionModel().isPublishRestricted(emails, users);
    }
}
