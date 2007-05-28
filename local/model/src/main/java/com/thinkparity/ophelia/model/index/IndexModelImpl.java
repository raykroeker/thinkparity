/*
 * Mar 6, 2006
 */
package com.thinkparity.ophelia.model.index;

import java.util.List;

import com.thinkparity.codebase.Pair;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.help.HelpContent;
import com.thinkparity.ophelia.model.help.HelpTopic;
import com.thinkparity.ophelia.model.help.InternalHelpModel;
import com.thinkparity.ophelia.model.index.contact.ContactIndexImpl;
import com.thinkparity.ophelia.model.index.contact.IncomingEMailInvitationIndexImpl;
import com.thinkparity.ophelia.model.index.contact.IncomingUserInvitationIndexImpl;
import com.thinkparity.ophelia.model.index.contact.OutgoingEMailInvitationIndexImpl;
import com.thinkparity.ophelia.model.index.contact.OutgoingUserInvitationIndexImpl;
import com.thinkparity.ophelia.model.index.container.ContainerIndexImpl;
import com.thinkparity.ophelia.model.index.container.ContainerVersionIndexImpl;
import com.thinkparity.ophelia.model.index.document.DocumentIndexEntry;
import com.thinkparity.ophelia.model.index.document.DocumentIndexImpl;
import com.thinkparity.ophelia.model.index.help.HelpIndexEntry;
import com.thinkparity.ophelia.model.index.help.HelpIndexImpl;
import com.thinkparity.ophelia.model.index.profile.ProfileIndexImpl;
import com.thinkparity.ophelia.model.profile.InternalProfileModel;
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

    /** A container version index implementation. */
    private IndexImpl<ContainerVersion, Pair<Long, Long>> containerVersionIndex;

    /** A document index implementation. */
    private IndexImpl<DocumentIndexEntry, Long> documentIndex;

    /** A help index implementation. */
    private IndexImpl<HelpIndexEntry, Long> helpIndex;

    /** An incoming e-mail invitation index implementation. */
    private IndexImpl<IncomingEMailInvitation, Long> incomingEMailInvitationIndex;

    /** An incoming user invitation index implementation. */
    private IndexImpl<IncomingUserInvitation, Long> incomingUserInvitationIndex;

    /** An outgoing e-mail invitation index implementation. */
    private IndexImpl<OutgoingEMailInvitation, Long> outgoingEMailInvitationIndex;

    /** An outgoing user invitation index implementation. */
    private IndexImpl<OutgoingUserInvitation, Long> outgoingUserInvitationIndex;

    /** A profile index implementation. */
    private IndexImpl<Profile, JabberId> profileIndex;

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
            throw panic(t);
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
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#deleteHelpTopic(java.lang.Long)
     *
     */
    public void deleteHelpTopic(final Long helpTopicId) {
        try {
            helpIndex.delete(helpTopicId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#deleteIncomingEMailInvitation(java.lang.Long)
     *
     */
    public void deleteIncomingEMailInvitation(final Long invitationId) {
        try {
            incomingEMailInvitationIndex.delete(invitationId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#deleteIncomingUserInvitation(java.lang.Long)
     * 
     */
    public void deleteIncomingUserInvitation(Long invitationId) {
        try {
            incomingUserInvitationIndex.delete(invitationId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#deleteOutgoingEMailInvitation(java.lang.Long)
     *
     */
    public void deleteOutgoingEMailInvitation(Long invitationId) {
        try {
            outgoingEMailInvitationIndex.delete(invitationId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#deleteOutgoingUserInvitation(java.lang.Long)
     *
     */
    public void deleteOutgoingUserInvitation(Long invitationId) {
        try {
            outgoingUserInvitationIndex.delete(invitationId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#deleteProfile()
     * 
     */
    public void deleteProfile() {
        try {
            final Profile profile = getProfileModel().read();
            profileIndex.delete(profile.getId());
        } catch (final Throwable t) {
            throw panic(t);
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
            throw panic(t);
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
     * Create an index entry for the container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void indexContainerVersion(final Pair<Long, Long> containerVersionId) {
        try {
            containerVersionIndex.delete(containerVersionId);
            final ContainerVersion version = getContainerModel().readVersion(
                    containerVersionId.getOne(), containerVersionId.getTwo());
            containerVersionIndex.index(version);
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
            throw panic(t);
        }
	}

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#indexHelpTopic(java.lang.Long)
     *
     */
    public void indexHelpTopic(final Long helpTopicId) {
        try {
            helpIndex.delete(helpTopicId);

            final InternalHelpModel helpModel = getHelpModel();
            final HelpTopic topic = helpModel.readTopic(helpTopicId);
            final HelpContent content = helpModel.readContent(helpTopicId);
            final HelpIndexEntry entry = new HelpIndexEntry();
            entry.setContent(content);
            entry.setTopic(topic);
            helpIndex.index(entry);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#indexIncomingEMailInvitation(java.lang.Long)
     * 
     */
    public void indexIncomingEMailInvitation(final Long invitationId) {
        try {
            incomingEMailInvitationIndex.delete(invitationId);
            final IncomingEMailInvitation invitation =
                getContactModel().readIncomingEMailInvitation(invitationId);
            incomingEMailInvitationIndex.index(invitation);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#indexIncomingUserInvitation(java.lang.Long)
     *
     */
	public void indexIncomingUserInvitation(final Long invitationId) {
        try {
            incomingUserInvitationIndex.delete(invitationId);
            final IncomingUserInvitation invitation =
                getContactModel().readIncomingUserInvitation(invitationId);
            incomingUserInvitationIndex.index(invitation);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

	/**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#indexOutgoingEMailInvitation(java.lang.Long)
     *
     */
    public void indexOutgoingEMailInvitation(final Long invitationId) {
        try {
            outgoingEMailInvitationIndex.delete(invitationId);
            final OutgoingEMailInvitation invitation =
                getContactModel().readOutgoingEMailInvitation(invitationId);
            outgoingEMailInvitationIndex.index(invitation);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#indexOutgoingUserInvitation(java.lang.Long)
     *
     */
    public void indexOutgoingUserInvitation(final Long invitationId) {
        try {
            outgoingUserInvitationIndex.delete(invitationId);
            final OutgoingUserInvitation invitation =
                getContactModel().readOutgoingUserInvitation(invitationId);
            outgoingUserInvitationIndex.index(invitation);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#indexProfile()
     *
     */
    public void indexProfile() {
        try {
            final InternalProfileModel profileModel = getProfileModel();
            final Profile profile = profileModel.read();
            profileIndex.delete(profile.getId());
            profileIndex.index(profile);
        } catch (final Throwable t) {
            throw panic(t);
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
            throw panic(t);
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
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#searchContainerVersions(java.lang.String)
     *
     */
    public List<Pair<Long, Long>> searchContainerVersions(final String expression) {
        try {
            return containerVersionIndex.search(expression);
        } catch (final Throwable t) {
            throw panic(t);
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
            throw panic(t);
        }
	}

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#searchHelpTopics(java.lang.String)
     *
     */
    public List<Long> searchHelpTopics(final String expression) {
        try {
            return helpIndex.search(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#searchIncomingInvitations(java.lang.String)
     *
     */
    public List<Long> searchIncomingEMailInvitations(final String expression) {
        try {
            return incomingEMailInvitationIndex.search(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#searchIncomingInvitations(java.lang.String)
     *
     */
    public List<Long> searchIncomingUserInvitations(final String expression) {
        try {
            return incomingUserInvitationIndex.search(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#searchOutgoingEMailInvitations(java.lang.String)
     *
     */
    public List<Long> searchOutgoingEMailInvitations(final String expression) {
        try {
            return outgoingEMailInvitationIndex.search(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#searchOutgoingEMailInvitations(java.lang.String)
     *
     */
    public List<Long> searchOutgoingUserInvitations(final String expression) {
        try {
            return outgoingUserInvitationIndex.search(expression);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.index.InternalIndexModel#searchProfile(java.lang.String)
     * 
     */
    public List<JabberId> searchProfile(final String expression) {
        try {
            return profileIndex.search(expression);
        } catch (final Throwable t) {
            throw panic(t);
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
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        this.contactIndex = new ContactIndexImpl(workspace, modelFactory);
        this.containerIndex = new ContainerIndexImpl(workspace, modelFactory);
        this.containerVersionIndex = new ContainerVersionIndexImpl(workspace, modelFactory);
        this.documentIndex = new DocumentIndexImpl(workspace, modelFactory);
        this.helpIndex = new HelpIndexImpl(workspace, modelFactory);
        this.incomingEMailInvitationIndex = new IncomingEMailInvitationIndexImpl(workspace, modelFactory);
        this.incomingUserInvitationIndex = new IncomingUserInvitationIndexImpl(workspace, modelFactory);
        this.outgoingEMailInvitationIndex = new OutgoingEMailInvitationIndexImpl(workspace, modelFactory);
        this.outgoingUserInvitationIndex = new OutgoingUserInvitationIndexImpl(workspace, modelFactory);
        this.profileIndex = new ProfileIndexImpl(workspace, modelFactory);
    }
}
