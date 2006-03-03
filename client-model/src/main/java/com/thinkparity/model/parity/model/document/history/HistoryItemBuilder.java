/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.document.history;

import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;

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
	 * Create a history item.
	 * @param document
	 *            The document.
	 * @param auditEvent
	 *            The audit event.
	 * @param sModel
	 *            The session model.
	 * 
	 * @return This history item.
	 */
	public static HistoryItem create(final Document document,
			final AuditEvent auditEvent,
			final Map<JabberId, User> auditEventUsers) throws ParityException {
		switch(auditEvent.getType()) {
		case CLOSE: return buildCloseHistoryItem(document, (CloseEvent) auditEvent, auditEventUsers);
		case CREATE: return buildCreateHistoryItem(document, (CreateEvent) auditEvent);
		case RECEIVE: return buildReceiveHistoryItem(document, (ReceiveEvent) auditEvent, auditEventUsers);
		case RECEIVE_KEY: return buildReceiveKeyHistoryItem(document, (ReceiveKeyEvent) auditEvent, auditEventUsers);
		case REQUEST_KEY: return buildRequestKeyHistoryItem(document, (RequestKeyEvent) auditEvent, auditEventUsers);
		case SEND: return buildSendHistoryItem(document, (SendEvent) auditEvent, auditEventUsers);
		case SEND_KEY: return buildSendKeyHistoryItem(document, (SendKeyEvent) auditEvent, auditEventUsers);
		default:
			throw Assert.createUnreachable("Unuspported audit event:  " + auditEvent.getType());
		}
	}

	private static CloseHistoryItem buildCloseHistoryItem(
			final Document document, final CloseEvent closeEvent,
			final Map<JabberId, User> closeEventUsers) {
		final CloseHistoryItem closeHistoryItem = new CloseHistoryItem();
		closeHistoryItem.setClosedBy(closeEventUsers.get(closeEvent.getClosedBy()));
		closeHistoryItem.setDate(closeEvent.getCreatedOn());
		closeHistoryItem.setDocumentId(closeEvent.getArtifactId());
		closeHistoryItem.setEvent(HistoryItemEvent.CLOSE);
		closeHistoryItem.setName(document.getName());
		return closeHistoryItem;
	}


	private static CreateHistoryItem buildCreateHistoryItem(
			final Document document, final CreateEvent createEvent) {
		final CreateHistoryItem createHistoryItem = new CreateHistoryItem();
		createHistoryItem.setDate(createEvent.getCreatedOn());
		createHistoryItem.setDocumentId(createEvent.getArtifactId());
		createHistoryItem.setEvent(HistoryItemEvent.CREATE);
		createHistoryItem.setName(document.getName());
		return createHistoryItem;
	}

	private static ReceiveHistoryItem buildReceiveHistoryItem(
			final Document document, final ReceiveEvent receiveEvent,
			final Map<JabberId, User> receiveEventUsers) {
		final ReceiveHistoryItem receiveHistoryItem = new ReceiveHistoryItem();
		receiveHistoryItem.setDate(receiveEvent.getCreatedOn());
		receiveHistoryItem.setDocumentId(receiveEvent.getArtifactId());
		receiveHistoryItem.setEvent(HistoryItemEvent.RECEIVE);
		receiveHistoryItem.setName(document.getName());
		receiveHistoryItem.setReceivedFrom(
				receiveEventUsers.get(receiveEvent.getReceivedFrom()));
		receiveHistoryItem.setVersionId(receiveEvent.getArtifactVersionId());
		return receiveHistoryItem;
	}

	private static ReceiveKeyHistoryItem buildReceiveKeyHistoryItem(
			final Document document, final ReceiveKeyEvent receiveKeyEvent,
			final Map<JabberId, User> receiveKeyEventUsers) {
		final ReceiveKeyHistoryItem receiveKeyHistoryItem = new ReceiveKeyHistoryItem();
		receiveKeyHistoryItem.setDate(receiveKeyEvent.getCreatedOn());
		receiveKeyHistoryItem.setDocumentId(receiveKeyEvent.getArtifactId());
		receiveKeyHistoryItem.setEvent(HistoryItemEvent.RECEIVE_KEY);
		receiveKeyHistoryItem.setName(document.getName());
		receiveKeyHistoryItem.setReceivedFrom(
				receiveKeyEventUsers.get(receiveKeyEvent.getReceivedFrom()));
		return receiveKeyHistoryItem;
	}

	private static RequestKeyItem buildRequestKeyHistoryItem(
			final Document document, final RequestKeyEvent event,
			final Map<JabberId, User> eventUsers) {
		final RequestKeyItem item = new RequestKeyItem();
		item.setDate(event.getCreatedOn());
		item.setDocumentId(document.getId());
		item.setEvent(HistoryItemEvent.REQUEST_KEY);
		item.setName(document.getName());
		item.setRequestedBy(eventUsers.get(event.getRequestedBy()));
		item.setRequestedFrom(eventUsers.get(event.getRequestedFrom()));
		return item;
	}

	private static SendHistoryItem buildSendHistoryItem(
			final Document document, final SendEvent sendEvent,
			final Map<JabberId, User> sendEventUsers) {
		final SendHistoryItem sendHistoryItem = new SendHistoryItem();
		sendHistoryItem.setDate(sendEvent.getCreatedOn());
		sendHistoryItem.setDocumentId(sendEvent.getArtifactId());
		sendHistoryItem.setEvent(HistoryItemEvent.SEND);
		sendHistoryItem.setName(document.getName());
		for(final JabberId sentTo : sendEvent.getSentTo()) {
			sendHistoryItem.addSentTo(sendEventUsers.get(sentTo));
		}
		sendHistoryItem.setVersionId(sendEvent.getArtifactVersionId());
		return sendHistoryItem;
	}

	private static SendKeyHistoryItem buildSendKeyHistoryItem(
			final Document document, final SendKeyEvent sendKeyEvent,
			final Map<JabberId, User> sendKeyEventUsers) {
		final SendKeyHistoryItem sendKeyHistoryItem = new SendKeyHistoryItem();
		sendKeyHistoryItem.setDate(sendKeyEvent.getCreatedOn());
		sendKeyHistoryItem.setDocumentId(sendKeyEvent.getArtifactId());
		sendKeyHistoryItem.setEvent(HistoryItemEvent.SEND_KEY);
		sendKeyHistoryItem.setName(document.getName());
		sendKeyHistoryItem.setSentTo(
				sendKeyEventUsers.get(sendKeyEvent.getSentTo()));
		sendKeyHistoryItem.setVersionId(sendKeyEvent.getArtifactVersionId());
		return sendKeyHistoryItem;
	}

	/**
	 * Create a HistoryItemBuilder.
	 * 
	 */
	private HistoryItemBuilder() { super(); }
}
