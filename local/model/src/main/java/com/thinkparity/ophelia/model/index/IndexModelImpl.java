/*
 * Mar 6, 2006
 */
package com.thinkparity.ophelia.model.index;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;
import com.thinkparity.ophelia.model.index.contact.ContactIndexImpl;
import com.thinkparity.ophelia.model.index.contact.IncomingInvitationIndexImpl;
import com.thinkparity.ophelia.model.index.contact.OutgoingInvitationIndexImpl;
import com.thinkparity.ophelia.model.index.container.ContainerIndexImpl;
import com.thinkparity.ophelia.model.index.document.DocumentIndexEntry;
import com.thinkparity.ophelia.model.index.document.DocumentIndexImpl;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Index Model<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * 
 * TODO Include the index within the transaction.
 */
public final class IndexModelImpl extends Model implements
        IndexModel, InternalIndexModel {

    /** A contact index implementation. */
    private IndexImpl<Contact, JabberId> contactIndex;

    /** A container index implementation. */
    private IndexImpl<Container, Long> containerIndex;

    /** A document index implementation. */
    private IndexImpl<DocumentIndexEntry, Long> documentIndex;

    /** An incoming invitation index implementation. */
    private IndexImpl<IncomingInvitation, Long> incomingInvitationIndex;

    /** An outgoing invitation index implementation. */
    private IndexImpl<OutgoingInvitation, Long> outgoingInvitationIndex;

    /**
	 * Create a IndexModelImpl.
	 * 
	 */
	public IndexModelImpl() {
		super();
	}

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#deleteContact(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
	public void deleteContact(final JabberId contactId) {
        try {
            contactIndex.delete(contactId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#updateContact(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public void updateContact(final JabberId contactId) {
        try {
            // update the contact info
            final Contact contact = getContactModel().read(contactId);
            contactIndex.delete(contactId);
            contactIndex.index(contact);
            // update the team info
            final List<Container> containers = getContainerModel().readForTeamMember(contact.getLocalId());
            for (final Container container : containers) {
                containerIndex.delete(container.getId());
                containerIndex.index(container);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Delete a container from the index.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void deleteContainer(final Long containerId) {
        try {
            containerIndex.delete(containerId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Delete a document from the index.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void deleteDocument(final Long documentId) {
        try {
            documentIndex.delete(documentId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Create a index entry for a contact.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void indexContact(final JabberId contactId) {
        try {
            contactIndex.delete(contactId);
            final Contact contact = getContactModel().read(contactId);
            contactIndex.index(contact);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * Create an index entry for the container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void indexContainer(final Long containerId) {
        try {
            containerIndex.delete(containerId);
            final Container container = getContainerModel().read(containerId);
            containerIndex.index(container);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Create an index entry for a document.
     * 
     * @param containerId A container id ,code>Long</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     */
	public void indexDocument(final Long containerId, final Long documentId) {
        try {
            documentIndex.delete(documentId);
            final Document document = getDocumentModel().get(documentId);
            final DocumentIndexEntry entry = new DocumentIndexEntry(containerId, document);
            documentIndex.index(entry);
        } catch (final Throwable t) {
            throw translateError(t);
        }
	}

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#indexIncomingInvitation(java.lang.Long)
     *
     */
    public void indexIncomingInvitation(final Long invitationId) {
        try {
            incomingInvitationIndex.delete(invitationId);
            final IncomingInvitation invitation =
                getContactModel().readIncomingInvitation(invitationId);
            incomingInvitationIndex.index(invitation);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#indexOutgoingInvitation(java.lang.Long)
     *
     */
    public void indexOutgoingInvitation(final Long invitationId) {
        try {
            outgoingInvitationIndex.delete(invitationId);
            final OutgoingInvitation invitation =
                getContactModel().readOutgoingInvitation(invitationId);
            outgoingInvitationIndex.index(invitation);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Search the contact index.
     * 
     * @param expression
     *            A search expression.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
    public List<JabberId> searchContacts(final String expression) {
        try {
            return contactIndex.search(expression);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Search the container index.
     * 
     * @param expression
     *            The search expression.
     * @return A <code>List&lt;Long&gt;</code>.
     */
    public List<Long> searchContainers(final String expression) {
        try {
            return containerIndex.search(expression);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Search the document index.
     * 
     * @param expression
     *            The search expression.
     * @return A A <code>List&lt;Document&gt;</code>.
     */
	public List<Long> searchDocuments(final String expression) {
        try {
            return documentIndex.search(expression);
        } catch (final Throwable t) {
            throw translateError(t);
        }
	}

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#searchIncomingInvitations(java.lang.String)
     *
     */
    public List<Long> searchIncomingInvitations(final String expression) {
        try {
            return incomingInvitationIndex.search(expression);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#searchOutgoingInvitations(java.lang.String)
     *
     */
    public List<Long> searchOutgoingInvitations(final String expression) {
        try {
            return outgoingInvitationIndex.search(expression);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        this.containerIndex = new ContainerIndexImpl(workspace, modelFactory);
        this.documentIndex = new DocumentIndexImpl(workspace, modelFactory);
        this.contactIndex = new ContactIndexImpl(workspace, modelFactory);
        this.incomingInvitationIndex = new IncomingInvitationIndexImpl(workspace, modelFactory);
        this.outgoingInvitationIndex = new OutgoingInvitationIndexImpl(workspace, modelFactory);
    }
}
