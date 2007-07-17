/*
 * Created On:  28-Jun-07 9:26:46 AM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.document.DocumentFileLock;
import com.thinkparity.ophelia.model.session.InternalSessionModel;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Delete extends ContainerDelegate {

    /** A container id. */
    private Long containerId;

    /** The deleted invitations. */
    private final List<OutgoingEMailInvitation> invitations;

    /**
     * Create Delete.
     *
     */
    public Delete() {
        super();
        this.invitations = new ArrayList<OutgoingEMailInvitation>();
    }

    /**
     * Delete a container.
     *
     */
    public void delete() throws CannotLockException {
        final Container container = read(containerId);
        final List<Document> allDocuments = readAllDocuments(containerId);
        final Map<Document, DocumentFileLock> allDocumentsLocks = new HashMap<Document, DocumentFileLock>();
        final Map<DocumentVersion, DocumentFileLock> allDocumentVersionsLocks = new HashMap<DocumentVersion, DocumentFileLock>();
        try {
            allDocumentsLocks.putAll(lockDocuments(allDocuments));
            allDocumentVersionsLocks.putAll(lockDocumentVersions(allDocuments));

            if (isDistributed(container.getId())) {
                final InternalSessionModel sessionModel = getSessionModel();
                final Calendar deletedOn = sessionModel.readDateTime();
                final List<ContainerVersion> versions = readVersions(container);
                // delete
                deleteLocal(container.getId(), allDocuments, allDocumentsLocks, allDocumentVersionsLocks);
                deleteLocalInvitations(versions);
                containerService.delete(getAuthToken(), container, deletedOn);
            } else {
                deleteLocal(container.getId(), allDocuments, allDocumentsLocks, allDocumentVersionsLocks);
            }
        } finally {
            try {
                releaseLocks(allDocumentsLocks.values());
            } finally {
                releaseLocks(allDocumentVersionsLocks.values());
            }
        }
    }

    /**
     * Obtain invitations.
     *
     * @return A List<OutgoingEMailInvitation>.
     */
    public List<OutgoingEMailInvitation> getInvitations() {
        return Collections.unmodifiableList(invitations);
    }

    /**
     * Set the container id.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void setContainerId(final Long containerId) {
        this.containerId = containerId;
    }

    /**
     * Delete the version invitations locally.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    private void deleteLocalInvitations(final List<ContainerVersion> versions) {
        final InternalContactModel contactModel = getContactModel();
        for (final ContainerVersion version : versions) {
            // delete invitations if the invitation is the only one
            final List<OutgoingEMailInvitation> invitations =
                contactModel.readOutgoingEMailInvitations(version);
            if (1 == invitations.size()) {
                this.invitations.add(
                        contactModel.deleteLocalOutgoingEMailInvitation(
                                invitations.get(0).getInvitationEMail()));
            }
        }
    }
}
