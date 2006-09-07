/*
 * Mar 6, 2006
 */
package com.thinkparity.model.parity.model.index;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.container.Container;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.index.contact.ContactIndexImpl;
import com.thinkparity.model.parity.model.index.container.ContainerIndexImpl;
import com.thinkparity.model.parity.model.index.document.DocumentIndexImpl;
import com.thinkparity.model.parity.model.index.document.DocumentIndexEntry;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.contact.Contact;

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
	IndexModelImpl(final Workspace workspace) {
		super(workspace);
        this.containerIndex = new ContainerIndexImpl(context, workspace);
        this.documentIndex = new DocumentIndexImpl(context, workspace);
        this.contactIndex = new ContactIndexImpl(context, workspace);
	}

	/**
     * Delete a contact from the index.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void deleteContact(final JabberId contactId) {
        logApiId();
        logVariable("contactId", contactId);
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
        logApiId();
        logVariable("containerId", containerId);
        try {
            final Container c = getInternalContainerModel().read(containerId);
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
        logApiId();
        logVariable("documentId", documentId);
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
        logApiId();
        logVariable("contactId", contactId);
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
        logApiId();
        logVariable("containerId", containerId);
        try {
            final Container container = getInternalContainerModel().read(containerId);
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
		logApiId();
        logVariable("documentId", documentId);
        logVariable("containerId", containerId);
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
        logApiId();
        logVariable("expression", expression);
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
        logApiId();
        logVariable("expression", expression);
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
        logApiId();
        logVariable("expression", expression);
        try {
            return documentIndex.search(expression);
        } catch (final Throwable t) {
            throw translateError(t);
        }
	}
}
