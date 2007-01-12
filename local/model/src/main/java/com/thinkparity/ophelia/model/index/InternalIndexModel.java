/*
 * Mar 6, 2006
 */
package com.thinkparity.ophelia.model.index;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;

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
     * Index a document.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     */
	public void indexDocument(final Long containerId, final Long documentId);

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
     * Search the index for documents.
     * 
     * @param expression
     *            A search expresssion.
     * @return A list of documents.
     */
    public List<Long> searchDocuments(final String expression);
}
