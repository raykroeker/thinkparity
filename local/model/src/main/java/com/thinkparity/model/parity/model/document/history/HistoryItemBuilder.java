/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.document.history;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.l10n.L18n;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.parity.model.audit.event.*;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HistoryItemBuilder {

	/** The parity document. */
	private final Document document;

	/** A localization wrapper. */
	private final L18n l18n;

    /** An apache logger. */
    protected final Logger logger;

    /**
	 * Create a HistoryItemBuilder.
	 * 
	 */
	public HistoryItemBuilder(final L18n l18n, final Document document) {
		super();
		this.l18n = l18n;
		this.document = document;
        this.logger = ModelLoggerFactory.getLogger(getClass());
	}

	/**
     * Build the history of a document for its audit events.
     * 
     * @param iAuditEvents
     *            An iterable of audit events.
     * @return The history.
     * @throws ParityException
     */
	public List<HistoryItem> build(final List<AuditEvent> auditEvents,
            final JabberId loggedInUser) throws ParityException {
		final List<HistoryItem> history = new LinkedList<HistoryItem>();

		final ListIterator<AuditEvent> li = auditEvents.listIterator();
        AuditEvent auditEvent;
        while(li.hasNext()) {
            auditEvent = li.next();
            switch(auditEvent.getType()) {
            case ARCHIVE:
                history.add(create(document, (ArchiveEvent) auditEvent));
                break;
            case CLOSE:
                history.add(create(document, (CloseEvent) auditEvent));
                break;
            case CREATE:
                history.add(create(document, (CreateEvent) auditEvent));
                break;
            case KEY_RESPONSE_DENIED:
                history.add(create(document, (KeyResponseDeniedEvent) auditEvent));
                break;
            case KEY_REQUEST_DENIED:
                history.add(create(document, (KeyRequestDeniedEvent) auditEvent));
                break;
            case RECEIVE:
                history.add(create(document, (ReceiveEvent) auditEvent));
                break;
            case RECEIVE_KEY:
                history.add(create(document, (ReceiveKeyEvent) auditEvent));
                break;
            case REQUEST_KEY:
                history.add(create(loggedInUser, document, (RequestKeyEvent) auditEvent));
                break;
            case SEND:
                final SendEvent sendEvent = (SendEvent) auditEvent;
                final SendEventConfirmation confirmEvent =
                    findConfirmation(auditEvents.listIterator(li.nextIndex()), sendEvent);
                history.add(create(document, sendEvent, confirmEvent));
                break;
            case SEND_CONFIRMATION:
                // we do nothing
                break;
            case SEND_KEY:
                history.add(create(document, (SendKeyEvent) auditEvent));
                break;
            default:
                logger.warn("[LMODEL] [DOCUMENT] [BUILD HISTORY] [UNSUPPORTED AUDIT EVENT {0}]");
            }
		}
		return history;
	}

	/**
     * Find and the confirmation event belonging to the send event.
     * 
     * @param liAuditEvent
     *            A list iterator for the audit events.
     * @param sendEvent
     *            The send event to target.
     * @return The confirmation event corresponding to the send event; or null
     *         if the event does not exist.
     */
    private SendEventConfirmation findConfirmation(
            final ListIterator<AuditEvent> liAuditEvent,
            final SendEvent sendEvent) {
        AuditEvent auditEvent;
        while(liAuditEvent.hasNext()) {
            auditEvent = liAuditEvent.next();
            if(AuditEventType.SEND_CONFIRMATION == auditEvent.getType()) {
                if(((SendEventConfirmation) auditEvent).getConfirmedBy().equals(sendEvent.getSentTo())) {
                    return (SendEventConfirmation) auditEvent;
                }
            }
        }
        return null;
    }

	private HistoryItem create(final Document document, final ArchiveEvent event) {
		final Object[] arguments = new Object[] {
				event.getCreatedOn().getTime()
		};
		final HistoryItem item = new HistoryItem();
		item.setDate(event.getCreatedOn());
		item.setDocumentId(document.getId());
		item.setEvent(getString("eventText.ARCHIVE", arguments));
        item.setPending(Boolean.FALSE);
		return item;
	}

	private HistoryItem create(final Document document, final CloseEvent event) {
		final Object[] arguments = new Object[] {
			getName(event.getClosedBy())
		};
		final HistoryItem item = new HistoryItem();
		item.setDate(event.getCreatedOn());
		item.setDocumentId(event.getArtifactId());
		item.setEvent(getString("eventText.CLOSE", arguments));
        item.setPending(Boolean.FALSE);
		return item;
	}

	private HistoryItem create(final Document document, final CreateEvent event) {
		final HistoryItem item = new HistoryItem();
		item.setDate(event.getCreatedOn());
		item.setDocumentId(event.getArtifactId());
		item.setEvent(getString("eventText.CREATE"));
        item.setPending(Boolean.FALSE);
		return item;
	}

	private HistoryItem create(final Document document,
            final KeyRequestDeniedEvent event) {
		final Object[] arguments = new Object[] {
				getName(event.getDeniedBy())
		};
		final HistoryItem item = new HistoryItem();
		item.setDate(event.getCreatedOn());
		item.setDocumentId(event.getArtifactId());
		item.setEvent(getString("eventText.KEY_REQUEST_DENIED", arguments));
        item.setPending(Boolean.FALSE);
		return item;
	}

	private HistoryItem create(final Document document,
            final KeyResponseDeniedEvent event) {
		final Object[] arguments = new Object[] {
				getName(event.getRequestedBy())
		};
		final HistoryItem item = new HistoryItem();
		item.setDate(event.getCreatedOn());
		item.setDocumentId(event.getArtifactId());
		item.setEvent(getString("eventText.KEY_RESPONSE_DENIED", arguments));
        item.setPending(Boolean.FALSE);
		return item;
	}

	private HistoryItem create(final Document document, final ReceiveEvent event) {
		final Object[] arguments = new Object[] {
			getName(event.getReceivedFrom())
		};
		final HistoryItem item = new HistoryItem();
		item.setDate(event.getCreatedOn());
		item.setDocumentId(event.getArtifactId());
		item.setEvent(getString("eventText.RECEIVE", arguments));
		item.setVersionId(event.getArtifactVersionId());
        item.setPending(Boolean.FALSE);
		return item;
	}

	private HistoryItem create(final Document document,
            final ReceiveKeyEvent event) {
		final Object[] arguments = new Object[] {
			getName(event.getReceivedFrom())
		};
		final HistoryItem item = new HistoryItem();
		item.setDate(event.getCreatedOn());
		item.setDocumentId(event.getArtifactId());
		item.setEvent(getString("eventText.RECEIVE_KEY", arguments));
        item.setPending(Boolean.FALSE);
		return item;
	}

	private HistoryItem create(final JabberId loggedInUser,
            final Document document, final RequestKeyEvent event) {
		final HistoryItem item = new HistoryItem();
		item.setDate(event.getCreatedOn());
		item.setDocumentId(document.getId());

		final Object[] arguments = new Object[] {
		        getName(event.getRequestedBy()),
		        getName(event.getRequestedFrom())
		};
		final String localKey;
		if(event.getRequestedBy().getId().equals(loggedInUser)) {
			localKey = "eventText.REQUEST_KEY_BY";
		}
		else if(event.getRequestedFrom().getId().equals(loggedInUser)) {
			localKey = "eventText.REQUEST_KEY_FROM";			
		}
		else {
			throw Assert.createUnreachable(
					"Request key event contains neither by\\from user.");
		}
		item.setEvent(getString(localKey, arguments));
        item.setPending(Boolean.FALSE);
		return item;
	}

	private HistoryItem create(final Document document, final SendEvent sendEvent,
            final SendEventConfirmation confirmEvent) {
		final HistoryItem item = new HistoryItem();
		item.setDate(sendEvent.getCreatedOn());
		item.setDocumentId(sendEvent.getArtifactId());
        item.setPending(Boolean.FALSE);

        // use different language if there exists a send confirmation
		final Object[] arguments;
        if(null == confirmEvent) {
            arguments = new Object[] {getName(sendEvent.getSentTo())};
            item.setEvent(getString("eventText.SEND", arguments));
            item.setPending(Boolean.TRUE);
        }
        else {
            arguments = new Object[] {
                getName(sendEvent.getSentTo()),
                confirmEvent.getCreatedOn().getTime()
            };
            if(DateUtil.isSameDay(
                    sendEvent.getCreatedOn(), confirmEvent.getCreatedOn())) {
                item.setEvent(getString("eventText.SEND_CONFIRMED_SAME_DAY", arguments));
            }
            else {
                item.setEvent(getString("eventText.SEND_CONFIRMED", arguments));
            }
        }
        item.setVersionId(sendEvent.getArtifactVersionId());
		return item;
	}

	private HistoryItem create(final Document document, final SendKeyEvent event) {
		final Object[] arguments = new Object[] {getName(event.getSentTo())};
		final HistoryItem item = new SendKeyHistoryItem();
		item.setDate(event.getCreatedOn());
		item.setDocumentId(event.getArtifactId());
		item.setVersionId(event.getArtifactVersionId());
		item.setEvent(getString("eventText.SEND_KEY", arguments));
        item.setPending(Boolean.FALSE);
		return item;
	}

	private String getName(final User user) {
		final String organization = user.getOrganization();
		if(null ==  organization) {
			final Object[] arguments = new Object[] {
					user.getFirstName(),
					user.getLastName()
			};
			return getString("user.nameMinusOrganization", arguments);
		}
		else {
			final Object[] arguments = new Object[] {
					user.getFirstName(),
					user.getLastName(),
					user.getOrganization()
			};
			return getString("user.name", arguments);
		}
	}

	/**
	 * @see L18n#getString(java.lang.String)
	 * 
	 */
	private String getString(final String localKey) {
		return l18n.getString(localKey);
	}

	/**
	 * @see L18n#getString(java.lang.String, java.lang.Object[])
	 * 
	 */
	private String getString(final String localKey, final Object[] arguments) {
		return l18n.getString(localKey, arguments);
	}
}
