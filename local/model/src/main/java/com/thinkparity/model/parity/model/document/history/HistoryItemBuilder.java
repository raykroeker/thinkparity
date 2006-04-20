/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.document.history;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.l10n.L18n;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.audit.event.*;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HistoryItemBuilder {

	/**
	 * The document.
	 * 
	 */
	private final Document document;

	/**
	 * Localization.
	 * 
	 */
	private final L18n l18n;

	/**
	 * Create a HistoryItemBuilder.
	 * 
	 */
	public HistoryItemBuilder(final L18n l18n, final Document document) {
		super();
		this.l18n = l18n;
		this.document = document;
	}

	/**
	 * Create the history for the list of audit events.
	 * 
	 * @param auditEvents
	 *            A list of audit events.
	 * @return A list of history events.
	 * @throws ParityException
	 */
	public List<HistoryItem> create(final Iterable<AuditEvent> auditEvents,
            final JabberId loggedInUser) throws ParityException {
		final List<HistoryItem> history = new LinkedList<HistoryItem>();
		for(final AuditEvent auditEvent : auditEvents) {
			history.add(create(auditEvent, loggedInUser));
		}
		return history;
	}

	/**
	 * Create a history item.
	 * 
	 * @param auditEvent
	 *            The audit event.
	 * 
	 * @return This history item.
	 */
	private HistoryItem create(final AuditEvent auditEvent,
            final JabberId loggedInUser) throws ParityException {
		switch(auditEvent.getType()) {
		case ARCHIVE: return create(document, (ArchiveEvent) auditEvent);
        case CONFIRM_RECEIPT: return create(document, (ConfirmationReceipt) auditEvent);
		case CLOSE: return create(document, (CloseEvent) auditEvent);
		case CREATE: return create(document, (CreateEvent) auditEvent);
		case KEY_RESPONSE_DENIED: return create(document, (KeyResponseDeniedEvent) auditEvent);
		case KEY_REQUEST_DENIED: return create(document, (KeyRequestDeniedEvent) auditEvent);
		case RECEIVE: return create(document, (ReceiveEvent) auditEvent);
		case RECEIVE_KEY: return create(document, (ReceiveKeyEvent) auditEvent);
		case REQUEST_KEY: return create(loggedInUser, document, (RequestKeyEvent) auditEvent);
		case SEND: return create(document, (SendEvent) auditEvent);
		case SEND_KEY: return create(document, (SendKeyEvent) auditEvent);
		default:
			throw Assert.createUnreachable("Unuspported audit event:  " + auditEvent.getType());
		}
	}

	private HistoryItem create(final Document document, final ArchiveEvent event) {
		final Object[] arguments = new Object[] {
				event.getCreatedOn().getTime()
		};
		final HistoryItem item = new HistoryItem();
		item.setDate(event.getCreatedOn());
		item.setDocumentId(document.getId());
		item.setEvent(getString("eventText.ARCHIVE", arguments));
		return item;
	}

	private HistoryItem create(final Document document, final CloseEvent event) {
		final Object[] arguments = new Object[] {
			getName(event.getClosedBy())
		};
		final HistoryItem close = new HistoryItem();
		close.setDate(event.getCreatedOn());
		close.setDocumentId(event.getArtifactId());
		close.setEvent(getString("eventText.CLOSE", arguments));
		return close;
	}

	private HistoryItem create(final Document document, final CreateEvent event) {
		final HistoryItem create = new HistoryItem();
		create.setDate(event.getCreatedOn());
		create.setDocumentId(event.getArtifactId());
		create.setEvent(getString("eventText.CREATE"));
		return create;
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

		return item;
		
	}

	private HistoryItem create(final Document document, final ReceiveEvent event) {
		final Object[] arguments = new Object[] {
			getName(event.getReceivedFrom())
		};
		final HistoryItem receive = new HistoryItem();
		receive.setDate(event.getCreatedOn());
		receive.setDocumentId(event.getArtifactId());
		receive.setEvent(getString("eventText.RECEIVE", arguments));
		receive.setVersionId(event.getArtifactVersionId());
		return receive;
	}

	private HistoryItem create(final Document document,
            final ReceiveKeyEvent event) {
		final Object[] arguments = new Object[] {
			getName(event.getReceivedFrom())
		};
		final HistoryItem receiveKey = new HistoryItem();
		receiveKey.setDate(event.getCreatedOn());
		receiveKey.setDocumentId(event.getArtifactId());
		receiveKey.setEvent(getString("eventText.RECEIVE_KEY", arguments));
		return receiveKey;
	}

	private HistoryItem create(final JabberId loggedInUser,
            final Document document, final RequestKeyEvent event) {
		final HistoryItem requestKey = new HistoryItem();
		requestKey.setDate(event.getCreatedOn());
		requestKey.setDocumentId(document.getId());

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
		requestKey.setEvent(getString(localKey, arguments));
		return requestKey;
	}

	private HistoryItem create(final Document document, final SendEvent event) {
		final Object[] arguments = new Object[] {getName(event.getSentTo())};
		final HistoryItem send = new HistoryItem();
		send.setDate(event.getCreatedOn());
		send.setDocumentId(event.getArtifactId());
		send.setEvent(getString("eventText.SEND", arguments));
		send.setVersionId(event.getArtifactVersionId());
		return send;
	}

    private HistoryItem create(final Document document,
            final ConfirmationReceipt event) {
        final HistoryItem hi = new HistoryItem();
        hi.setDate(event.getCreatedOn());
        hi.setDocumentId(document.getId());
        // TODO Create a history item for the confirmation receipt
        hi.setEvent(getString("", null));
        return hi;
    }

	private HistoryItem create(final Document document, final SendKeyEvent event) {
		final Object[] arguments = new Object[] {getName(event.getSentTo())};
		final HistoryItem sendKey = new SendKeyHistoryItem();
		sendKey.setDate(event.getCreatedOn());
		sendKey.setDocumentId(event.getArtifactId());
		sendKey.setVersionId(event.getArtifactVersionId());
		sendKey.setEvent(getString("eventText.SEND_KEY", arguments));
		return sendKey;
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
