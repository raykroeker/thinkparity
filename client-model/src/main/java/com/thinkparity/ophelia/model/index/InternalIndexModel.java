/*
 * Mar 6, 2006
 */
package com.thinkparity.ophelia.model.index;

import java.util.List;

import com.thinkparity.codebase.Pair;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.util.ProcessMonitor;

/**
 * <b>Title:</b>thinkParity Internal Index Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalIndexModel extends IndexModel {

    /**
     * Delete a contact from the index.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void deleteContact(final JabberId contactId);

    /**
     * Delete a container from the index.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void deleteContainer(final Long containerId);

    /**
     * Delete a document from the index.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void deleteDocument(final Long documentId);

    /**
     * Delete an incoming e-mail invitation index entry.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     */
    public void deleteIncomingEMailInvitation(final Long invitationId);

    /**
     * Delete an incoming user invitation index entry.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     */
    public void deleteIncomingUserInvitation(final Long invitationId);

    /**
     * Delete an outgoing e-mail invitation index entry.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     */
    public void deleteOutgoingEMailInvitation(final Long invitationId);

    /**
     * Delete an outgoing user invitation index entry.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     */
    public void deleteOutgoingUserInvitation(final Long invitationId);

    /**
     * Delete the user profile index
     *
     */
    public void deleteProfile();

    /**
     * Index a contact.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void indexContact(final JabberId contactId);

    /**
     * Index a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void indexContainer(final Long containerId);

    /**
     * Index a container version.
     * 
     * @param containerVersionId
     *            A <code>Pair</code>ed container and container version id
     *            <code>Long</code>.
     */
    public void indexContainerVersion(final Pair<Long, Long> containerVersionId);

    /**
     * Index a document.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     */
	public void indexDocument(final Long containerId, final Long documentId);

    /**
     * Index a help topic.
     * 
     * @param helpTopicId
     *            A help topic id <code>Long</code>.
     */
    public void indexHelpTopic(final Long helpTopicId);

    /**
     * Index an incoming invitation.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     */
    public void indexIncomingEMailInvitation(final Long invitationId);

    /**
     * Index an incoming invitation.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     */
    public void indexIncomingUserInvitation(final Long invitationId);
    
    /**
     * Index an outgoing e-mail invitation.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     */
    public void indexOutgoingEMailInvitation(final Long invitationId);

    /**
     * Index an outgoing user invitation.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     */
    public void indexOutgoingUserInvitation(final Long invitationId);

    /**
     * Index the user profile.
     *
     */
    public void indexProfile();

    /**
     * Rebuild the index.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    public void rebuild(final ProcessMonitor monitor);

    /**
     * Search the index for contacts.
     * 
     * @param expression
     *            A search expresssion.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
    public List<JabberId> searchContacts(final String expression);

    /**
     * Search the index for containers.
     * 
     * @param expression
     *            A search expresssion.
     * @return A list of containers.
     */
    public List<Long> searchContainers(final String expression);

    /**
     * Search the index for container versions.
     * 
     * @param expression
     *            A search expression.
     * @return A <code>List</code> of <code>Pair</code>ed container and
     *         container version id <code>Long</code>s.
     */
    public List<Pair<Long, Long>> searchContainerVersions(final String expression);

    /**
     * Search the index for documents.
     * 
     * @param expression
     *            A search expresssion.
     * @return A list of documents.
     */
    public List<Long> searchDocuments(final String expression);

    /**
     * Search the help topics.
     * 
     * @param expression
     *            A search expression <code>String</code>.
     * @return A <code>List<Long></code> of help topic ids.
     */
    public List<Long> searchHelpTopics(final String expression);

    /**
     * Search the index for incoming e-mail invitations.
     * 
     * @param expression
     *            A search expression <code>String</code>..
     * @return A <code>List</code> of invitation id <code>Long</code>s.
     */
    public List<Long> searchIncomingEMailInvitations(final String expression);

    /**
     * Search the index for incoming user invitations.
     * 
     * @param expression
     *            A search expression <code>String</code>..
     * @return A <code>List</code> of invitation id <code>Long</code>s.
     */
    public List<Long> searchIncomingUserInvitations(final String expression);

    /**
     * Search the index for outgoing e-mail invitations.
     * 
     * @param expression
     *            A search expression <code>String</code>..
     * @return A <code>List</code> of invitation id <code>Long</code>s.
     */
    public List<Long> searchOutgoingEMailInvitations(final String expression);

    /**
     * Search the index for outgoing user invitations.
     * 
     * @param expression
     *            A search expression <code>String</code>..
     * @return A <code>List</code> of invitation id <code>Long</code>s.
     */
    public List<Long> searchOutgoingUserInvitations(final String expression);

    /**
     * Index the user profile.
     * 
     * @param expression
     *            A search expresssion.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
    public List<JabberId> searchProfile(final String expression);

    /**
     * Update a contact in the index.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void updateContact(final JabberId contactId);
}
