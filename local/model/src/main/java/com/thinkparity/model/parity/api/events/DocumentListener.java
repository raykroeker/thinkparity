/*
 * Jun 5, 2005
 */
package com.thinkparity.model.parity.api.events;

/**
 * A document listener is an interface used to notify all clients about
 * local and remote events generated by the parity document interface.
 *
 * @author raykroeker@gmail.com
 */
public interface DocumentListener {

    /**
     * A document has been archived.
     *
     * @param e
     *      A document event.
     */
    public void documentArchived(final DocumentEvent e);

    /**
     * A document has been closed.
     *
     * @param e
     *      The document event.
     */
    public void documentClosed(final DocumentEvent e);

    /**
     * A document has been created.
     *
     * @param e
     *      The document event.
     */
    public void documentCreated(final DocumentEvent e);

    /**
     * A document has been deleted.
     *
     * @param e
     *      The document event.
     */
    public void documentDeleted(final DocumentEvent e);

    /**
     * A document has been udpated.
     *
     * @param e
     *      The document event.
     */
    public void documentUpdated(final DocumentEvent e);

    /**
     * A document key request has been generated.
     *
     * @param e
     *      The document event.
     */
    public void keyRequested(final DocumentEvent e);
}
