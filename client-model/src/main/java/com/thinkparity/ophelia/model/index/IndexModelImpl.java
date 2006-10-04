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

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.index.contact.ContactIndexImpl;
import com.thinkparity.ophelia.model.index.container.ContainerIndexImpl;
import com.thinkparity.ophelia.model.index.document.DocumentIndexEntry;
import com.thinkparity.ophelia.model.index.document.DocumentIndexImpl;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class IndexModelImpl extends AbstractModelImpl {

    /** A contact index implementation. */
    private final IndexImpl<Contact, JabberId> contactIndex;

    /** A container index implementation. */
    private final IndexImpl<Container, Long> containerIndex;

    /** A document index implementation. */
    private final IndexImpl<DocumentIndexEntry, Long> documentIndex;

    /**
	 * Create a IndexModelImpl.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	IndexModelImpl(final Environment environment, final Workspace workspace) {
		super(environment, workspace);
        this.containerIndex = new ContainerIndexImpl(workspace, internalModelFactory);
        this.documentIndex = new DocumentIndexImpl(workspace, internalModelFactory);
        this.contactIndex = new ContactIndexImpl(workspace, internalModelFactory);
	}

	/**
     * Delete a contact from the index.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void deleteContact(final JabberId contactId) {
        logger.logApiId();
        logger.logVariable("contactId", contactId);
        try {
            final Contact c = getInternalContactModel().read(contactId);
            contactIndex.delete(c);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Delete a container from the index.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void deleteContainer(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        try {
            final Container c = getContainerModel().read(containerId);
            containerIndex.delete(c);
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
    void deleteDocument(final Long documentId) {
        logger.logApiId();
        logger.logVariable("documentId", documentId);
        try {
            final Document d = getInternalDocumentModel().get(documentId);
            documentIndex.delete(new DocumentIndexEntry(d));
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
    void indexContact(final JabberId contactId) {
        logger.logApiId();
        logger.logVariable("contactId", contactId);
        try {
            final Contact contact = getInternalContactModel().read(contactId);
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
    void indexContainer(final Long containerId) {
        logger.logApiId();
        logger.logVariable("containerId", containerId);
        try {
            final Container container = getContainerModel().read(containerId);
            containerIndex.index(container);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Create an index entry for a document.
     * 
     * @parma containerId A container id ,code>Long</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     */
	void indexDocument(final Long containerId, final Long documentId) {
		logger.logApiId();
        logger.logVariable("documentId", documentId);
        logger.logVariable("containerId", containerId);
        try {
            final Document document = getInternalDocumentModel().get(documentId);
            documentIndex.index(new DocumentIndexEntry(containerId, document));
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
    List<JabberId> searchContacts(final String expression) {
        logger.logApiId();
        logger.logVariable("expression", expression);
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
    List<Long> searchContainers(final String expression) {
        logger.logApiId();
        logger.logVariable("expression", expression);
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
	List<Long> searchDocuments(final String expression) {
        logger.logApiId();
        logger.logVariable("expression", expression);
        try {
            return documentIndex.search(expression);
        } catch (final Throwable t) {
            throw translateError(t);
        }
	}
}
