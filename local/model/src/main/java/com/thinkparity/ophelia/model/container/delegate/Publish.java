/*
 * Created On:  27-Apr-07 9:36:25 AM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.io.StreamOpener;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.monitor.PublishStep;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.document.DocumentFileLock;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
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
public final class Publish extends ContainerDelegate {

    /** A list of contacts to publish to. */
    private List<Contact> contacts;

    /** The container id of the container to publish. */
    private Long containerId;

    /** A list of e-mail addresses to publish to. */
    private List<EMail> emails;

    /** A list of outgoing e-mail invitations generated by publish. */
    private final List<OutgoingEMailInvitation> invitations;

    /** A publish <code>ProcessMonitor</code>. */
    private ProcessMonitor monitor;

    /** A list of team members to publish to. */
    private List<TeamMember> teamMembers;

    /** The publish to user list. */
    private final List<User> users;

    /** The version name <code>String</code>. */
    private String versionName;

    /**
     * Create PublishDelegate.
     *
     */
    public Publish() {
        super();
        this.invitations = new ArrayList<OutgoingEMailInvitation>();
        this.users = new ArrayList<User>();
    }

    /**
     * Obtain the list of generated outgoing e-mail invitations.
     * 
     * @return A <code>List</code> of <code>OutgoingEMailInvitation</code>s.
     */
    public List<OutgoingEMailInvitation> getInvitations() {
        return Collections.unmodifiableList(invitations);
    }

    /**
     * Publish.
     *
     */
    public void publish() throws CannotLockException, IOException {
        assertIsPublishable();
        // lock the documents
        final ContainerDraft draft = readDraft(containerId);
        final List<Document> documents = draft.getDocuments();
        final Map<Document, DocumentFileLock> locks = lockDocuments(documents);
        try {
            final InternalSessionModel sessionModel = getSessionModel();
            final Calendar publishedOn = sessionModel.readDateTime();
            final Container container = read(containerId);
            // ensure the user is the key holder
            if (isDistributed(containerId)) {
                final JabberId keyHolder = sessionModel.readKeyHolder(container.getUniqueId());
                Assert.assertTrue("User does not own draft.",
                        keyHolder.equals(localUserId()));
            }
            // ensure draft existence
            Assert.assertTrue("User does not own draft.",
                    doesExistDraft(containerId));
            // previous version
            final ContainerVersion previous = readLatestVersion(containerId);
            // create version
            notifyProcessBegin(monitor);
            notifyStepBegin(monitor, PublishStep.CREATE_VERSION);
            final ContainerVersion version = createVersion(container.getId(),
                    readNextVersionId(containerId), versionName,
                    draft.getComment(), localUserId(), publishedOn);
            // attach artifacts to the version
            final InternalDocumentModel documentModel = getDocumentModel();
            DocumentVersion draftDocumentLatestVersion;
            for (final Document document : documents) {
                if(ContainerDraft.ArtifactState.REMOVED !=
                        draft.getState(document)) {
                    if (documentModel.isDraftModified(locks.get(document),
                            document.getId())) {
                        draftDocumentLatestVersion =
                            createDocumentVersion(container, document,
                                    locks.get(document), publishedOn);
                    } else {
                        draftDocumentLatestVersion =
                            documentModel.readLatestVersion(document.getId());
                    }
                    containerIO.addVersion(
                            version.getArtifactId(), version.getVersionId(),
                            draftDocumentLatestVersion.getArtifactId(),
                            draftDocumentLatestVersion.getVersionId(),
                            draftDocumentLatestVersion.getArtifactType());
                }
            }
            // store differences between this and the previous version
            if (null == previous) {
                logger.logInfo("First version of {0}.", container.getName());
            } else {
                // delta previous with version
                containerIO.createDelta(calculateDelta(container, version, previous));
            }
            notifyStepEnd(monitor, PublishStep.CREATE_VERSION);
            // delete draft
            for (final Document document : documents) {
                documentModel.deleteDraft(locks.get(document), document.getId());
                containerIO.deleteDraftArtifactRel(containerId, document.getId());
            }
            containerIO.deleteDraftDocuments(containerId);
            containerIO.deleteDraft(containerId);
            // create published to list
            containerIO.createPublishedTo(version.getArtifactId(),
                    version.getVersionId(), contacts, publishedOn);
            containerIO.createPublishedTo(version.getArtifactId(),
                    version.getVersionId(), teamMembers, publishedOn);
            /* the remote publish invocation will potentially generate new
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
            // upload
            final List<DocumentVersion> documentVersions = readDocumentVersions(
                    version.getArtifactId(), version.getVersionId(),
                    new ComparatorBuilder().createVersionById(Boolean.TRUE));
            final Map<DocumentVersion, Delta> deltas;
            if (null == previous) {
                deltas = readDocumentVersionDeltas(containerId,
                        version.getVersionId());
            } else {
                deltas = readDocumentVersionDeltas(containerId,
                        version.getVersionId(), previous.getVersionId());
            }
            uploadDocumentVersions(monitor, documentVersions, deltas);
            // publish
            notifyStepBegin(monitor, PublishStep.PUBLISH);
            // build published to list
            sessionModel.publish(version, documentVersions, emails, users);
            notifyStepEnd(monitor, PublishStep.PUBLISH);
        } finally {
            releaseLocks(locks.values());
        }
    }

    /**
     * Set the contacts.
     * 
     * @param contacts
     *            A <code>List</code> of <code>Contact<code>s.
     */
    public void setContacts(final List<Contact> contacts) {
        this.contacts = contacts;
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
    public void setTeamMembers(final List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
        this.users.addAll(teamMembers);
    }

    /**
     * Set the version name.
     * 
     * @param name
     *            A name <code>String</code>.
     */
    public void setVersionName(final String versionName) {
        this.versionName = versionName;
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
        getContainerConstraints().getVersionName().validate(versionName);
        assertIsNotContact(emails);
        Assert.assertNotTrue(isPublishRestricted(), "The user cannot publish to the specified e-mails/users.");
    }

    /**
     * Create a document version. Read the document content from the container
     * draft document in the database and create a document version.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param document
     *            A <code>Document</code>.
     * @param lock
     *            A <code>DocumentFileLock</code>.
     * @param publishedOn
     *            A published on <code>Calendar</code>.
     * @return The new <code>DocumentVersion</code>.
     */
    private DocumentVersion createDocumentVersion(final Container container,
            final Document document, final DocumentFileLock lock,
            final Calendar publishedOn) throws IOException {
        final InternalDocumentModel documentModel = getDocumentModel();
        openDraftDocument(container.getId(), document.getId(),
                new StreamOpener() {
                    public void open(final InputStream stream)
                            throws IOException {
                        try {
                            documentModel.createVersion(lock, document.getId(),
                                    stream, publishedOn);
                        } finally {
                            stream.close();
                        }
                    }
                });
        /* NOTE a potential synchronization issue; however as long as we
         * synchronize on the workspace this will return the correct result */
        return documentModel.readLatestVersion(document.getId());
    }

    /**
     * Determine if the user is restricted from publishing.
     * 
     * @return True if the user is restricted.
     */
    private Boolean isPublishRestricted() {
        return getSessionModel().isPublishRestricted(emails, users);
    }

    /**
     * Open a container document draft stream.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     * @param opener
     *            A <code>StreamOpener</code>.
     * @return A container draft document <code>InputStream</code>.
     */
    private void openDraftDocument(final Long containerId,
            final Long documentId, final StreamOpener opener)
            throws IOException {
        containerIO.openDraftDocument(containerId, documentId, opener);
    }
}
