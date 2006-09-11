/*
 * Mar 6, 2006
 */
package com.thinkparity.ophelia.model.index;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;


import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InternalIndexModel extends IndexModel {

	/**
	 * Create a InternalIndexModel.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 * @param context
	 *            The parity context.
	 */
	InternalIndexModel(final Workspace workspace, final Context context) {
		super(workspace);
		context.assertContextIsValid();
	}

    /**
     * Delete a contact from the index.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void deleteContact(final JabberId contactId) {
        synchronized (getImplLock()) {
            getImpl().deleteContact(contactId);
        }
    }

    /**
     * Delete a container from the index.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void deleteContainer(final Long containerId) {
        synchronized (getImplLock()) {
            getImpl().deleteContainer(containerId);
        }
    }

    /**
     * Delete a document from the index.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void deleteDocument(final Long documentId) {
        synchronized (getImplLock()) {
            getImpl().deleteDocument(documentId);
        }
    }

    /**
     * Index a contact.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void indexContact(final JabberId contactId) {
        synchronized (getImplLock()) {
            getImpl().indexContact(contactId);
        }
    }
    
    /**
     * Index a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void indexContainer(final Long containerId) {
        synchronized(getImplLock()) {
            getImpl().indexContainer(containerId);
        }
    }

    /**
     * Index a document.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     */
	public void indexDocument(final Long containerId, final Long documentId) {
		synchronized(getImplLock()) {
			getImpl().indexDocument(containerId, documentId);
		}
	}

    /**
     * Search the index for contacts.
     * 
     * @param expression
     *            A search expresssion.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
    public List<JabberId> searchContacts(final String expression) {
        synchronized (getImplLock()) {
            return getImpl().searchContacts(expression);
        }
    }
    /**
     * Search the index for containers.
     * 
     * @param expression
     *            A search expresssion.
     * @return A list of containers.
     */
    public List<Long> searchContainers(final String expression) {
        synchronized (getImplLock()) {
            return getImpl().searchContainers(expression);
        }
    }

    /**
     * Search the index for documents.
     * 
     * @param expression
     *            A search expresssion.
     * @return A list of documents.
     */
    public List<Long> searchDocuments(final String expression) {
        synchronized (getImplLock()) {
            return getImpl().searchDocuments(expression);
        }
    }
}
