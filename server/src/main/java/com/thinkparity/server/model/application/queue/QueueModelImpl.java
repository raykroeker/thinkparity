/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.queue;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.Collection;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.io.sql.QueueSql;
import com.thinkparity.desdemona.model.session.Session;


/**
 * The queue model is used to persistantly store text for jabber ids. The text
 * is limitless; however large items will degrade the queue performance.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
class QueueModelImpl extends AbstractModelImpl {

	/**
	 * Handle to the parity queue sql interface.
	 */
	private final QueueSql queueSql;

    /** A sax xml reader. */
    private final SAXReader saxReader;

	/**
	 * Create a QueueModelImpl.
	 * 
	 * @param session
	 *            The user session.
	 */
	QueueModelImpl(final Session session) {
		super(session);
		this.queueSql = new QueueSql();
        this.saxReader = new SAXReader();
	}

	/**
	 * Enqueue a message for a jabber id to persistant storage.
	 * 
	 * @param jid
	 *            The message recipient.
	 * @param iq
	 *            The iq message.
	 * @return The enqueued item.
	 * @throws ParityServerModelException
	 */
	QueueItem enqueue(final JID jid, final IQ iq) {
        logApiId();
		logger.debug(jid);
		logger.debug(iq);
		try {
			final String username = jid.getNode();
			final String message = iq.toXML();
			final Integer queueId = queueSql.insert(
					username, message, session.getJabberId());
			return queueSql.select(queueId);
		} catch (final Throwable t) {
			throw translateError(t);
		}
	}

    
    /**
	 * Process all pending queue items for the given session.
	 * 
	 * @throws ParityServerModelException
	 */
	void processOfflineQueue() throws ParityServerModelException {
        logApiId();
		try {
			final Collection<QueueItem> queueItems = list();
			Element rootElement;
            JabberId userId;
			for(final QueueItem queueItem : queueItems) {
                rootElement = readRootElement(queueItem.getQueueMessage());
                userId = JabberIdBuilder.parseUsername(queueItem.getUsername());

                if (isOnline(userId)) {
                    getClientSession(userId).process(new IQ(rootElement));
                    dequeue(queueItem);
                }
			}
		} catch(final Throwable t) {
			throw translateError(t);
		}
	}

    /**
	 * Dequeue a previously enqueued item from the persistant store.
	 * 
	 * @param queueItem
	 *            The queue item to remove.
	 * @throws ParityServerModelException
	 */
	private void dequeue(final QueueItem queueItem) throws SQLException {
		final Integer queueId = queueItem.getQueueId();
		queueSql.delete(queueId);
	}
    
	/**
	 * Obtain a list of queued items for the jabber id.
	 * 
	 * @return A list of queued items.
	 */
	private Collection<QueueItem> list() throws SQLException {
		return queueSql.select(session.getJID().getNode());
	}

	/**
     * Read the root element of the xml.
     * 
     * @param xml
     *            An xml <code>String</code>.
     * @return The xml root <code>Element</code>.
     * @throws DocumentException
     */
    private Element readRootElement(final String xml) throws DocumentException {
        synchronized (saxReader) {
            return saxReader.read(new StringReader(xml)).getRootElement();
        }
    }
}
