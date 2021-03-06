/*
 * Jun 5, 2005
 */
package com.thinkparity.ophelia.model.events;

/**
 * A document adapter implements the document listener without implementing
 * any of the events.
 *
 * @author raykroeker@gmail.com
 */
public class DocumentAdapter implements DocumentListener {

    /**
     * A send confirmation has been received.
     *
     * @param e
     *      A document event.
     */
     public void confirmationReceived(final DocumentEvent e) {}

    /**
     * A document has been archived.
     *
     * @param e
     *      A document event.
     */
    public void documentArchived(final DocumentEvent e) {}

    /**
     * A document has been closed.
     *
     * @param e
     *      The document event.
     */
    public void documentClosed(final DocumentEvent e) {}

    /**
     * A document has been created.
     *
     * @param e
     *      The document event.
     */
    public void documentCreated(final DocumentEvent e) {}

    /**
     * A document has been deleted.
     *
     * @param e
     *      The document event.
     */
    public void documentDeleted(final DocumentEvent e) {}

    /**
     * A document has been published.
     *
     * @param e
     *      The document event.
     */
    public void documentPublished(final DocumentEvent e) {}

    /**
     * A document has been reactivated.
     * 
     * @param e
     *            A document event.
     */
    public void documentReactivated(final DocumentEvent e) {}

    /**
     * A document has been udpated.
     *
     * @param e
     *      The document event.
     */
    public void documentUpdated(final DocumentEvent e) {}

    /**
     * A document key request has been accepted.
     * 
     * @param e
     *            The document event.
     */
    public void keyRequestAccepted(final DocumentEvent e) {}

    /**
     * A document key request has been declined.
     * 
     * @param e
     *            The document event.
     */
    public void keyRequestDeclined(final DocumentEvent e) {}

    /**
     * A document key has been requested.
     *
     * @param e
     *      The document event.
     */
    public void keyRequested(final DocumentEvent e) {}

    /**
     * A user has been added to the team.
     *
     * @param e
     *      The document event.
     */
    public void teamMemberAdded(final DocumentEvent e) {}

    /**
     * A user has been removed from the team.
     * 
     * @param e
     *            The document event.
     */
    public void teamMemberRemoved(final DocumentEvent e) {}
}
