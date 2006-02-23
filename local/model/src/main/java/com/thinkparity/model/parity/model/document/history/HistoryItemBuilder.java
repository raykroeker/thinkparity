/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.document.history;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.audit.event.*;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.session.InternalSessionModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HistoryItemBuilder {

	/**
	 * Create a history item.
	 * 
	 * @param sModel
	 *            The session model.
	 * @param document
	 *            The document.
	 * @param auditEvent
	 *            The audit event.
	 * @return This history item.
	 */
	public static HistoryItem create(final InternalSessionModel iSModel,
			final Document document, final AuditEvent auditEvent)
			throws ParityException {
		switch(auditEvent.getType()) {
		case CLOSE: return buildCloseHistoryItem(document, (CloseEvent) auditEvent);
		case CREATE: return buildCreateHistoryItem(document, (CreateEvent) auditEvent);
		case RECEIVE: return buildReceiveHistoryItem(iSModel, document, (ReceiveEvent) auditEvent);
		case RECEIVE_KEY: return buildReceiveKeyHistoryItem(iSModel, document, (ReceiveKeyEvent) auditEvent);
		case SEND: return buildSendHistoryItem(iSModel, document, (SendEvent) auditEvent);
		case SEND_KEY: return buildSendKeyHistoryItem(iSModel, document, (SendKeyEvent) auditEvent);
		default:
			throw Assert.createUnreachable("Unuspported audit event:  " + auditEvent.getType());
		}
	}

	private static CloseHistoryItem buildCloseHistoryItem(
			final Document document, final CloseEvent closeEvent) {
		final CloseHistoryItem closeHistoryItem = new CloseHistoryItem();
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
			final InternalSessionModel iSModel, final Document document,
			final ReceiveEvent receiveEvent) throws ParityException {
		final ReceiveHistoryItem receiveHistoryItem = new ReceiveHistoryItem();
		receiveHistoryItem.setDate(receiveEvent.getCreatedOn());
		receiveHistoryItem.setDocumentId(receiveEvent.getArtifactId());
		receiveHistoryItem.setEvent(HistoryItemEvent.RECEIVE);
		receiveHistoryItem.setName(document.getName());
		receiveHistoryItem.setReceivedFrom(
				iSModel.findRosterEntry(receiveEvent.getReceivedFrom()));
		receiveHistoryItem.setVersionId(receiveEvent.getArtifactVersionId());
		return receiveHistoryItem;
	}

	private static ReceiveKeyHistoryItem buildReceiveKeyHistoryItem(
			final InternalSessionModel iSModel, final Document document,
			final ReceiveKeyEvent receiveKeyEvent) throws ParityException {
		final ReceiveKeyHistoryItem receiveKeyHistoryItem = new ReceiveKeyHistoryItem();
		receiveKeyHistoryItem.setDate(receiveKeyEvent.getCreatedOn());
		receiveKeyHistoryItem.setDocumentId(receiveKeyEvent.getArtifactId());
		receiveKeyHistoryItem.setEvent(HistoryItemEvent.RECEIVE_KEY);
		receiveKeyHistoryItem.setName(document.getName());
		receiveKeyHistoryItem.setReceivedFrom(
				iSModel.findRosterEntry(receiveKeyEvent.getReceivedFrom()));
		receiveKeyHistoryItem.setVersionId(receiveKeyEvent.getArtifactVersionId());
		return receiveKeyHistoryItem;
	}

	private static SendHistoryItem buildSendHistoryItem(
			final InternalSessionModel iSModel, final Document document,
			final SendEvent sendEvent) throws ParityException {
		final SendHistoryItem sendHistoryItem = new SendHistoryItem();
		sendHistoryItem.setDate(sendEvent.getCreatedOn());
		sendHistoryItem.setDocumentId(sendEvent.getArtifactId());
		sendHistoryItem.setEvent(HistoryItemEvent.SEND);
		sendHistoryItem.setName(document.getName());
		for(final String sentTo : sendEvent.getSentTo()) {
			sendHistoryItem.addSentTo(iSModel.findRosterEntry(sentTo));
		}
		sendHistoryItem.setVersionId(sendEvent.getArtifactVersionId());
		return sendHistoryItem;
	}

	private static SendKeyHistoryItem buildSendKeyHistoryItem(
			final InternalSessionModel iSModel, final Document document,
			final SendKeyEvent sendKeyEvent) throws ParityException {
		final SendKeyHistoryItem sendKeyHistoryItem = new SendKeyHistoryItem();
		sendKeyHistoryItem.setDate(sendKeyEvent.getCreatedOn());
		sendKeyHistoryItem.setDocumentId(sendKeyEvent.getArtifactId());
		sendKeyHistoryItem.setEvent(HistoryItemEvent.SEND_KEY);
		sendKeyHistoryItem.setName(document.getName());
		sendKeyHistoryItem.setSentTo(
				iSModel.findRosterEntry(sendKeyEvent.getSentTo()));
		sendKeyHistoryItem.setVersionId(sendKeyEvent.getArtifactVersionId());
		return sendKeyHistoryItem;
	}

	/**
	 * Create a HistoryItemBuilder.
	 */
	public HistoryItemBuilder() {
		super();
		// TODO Auto-generated constructor stub
	}

}
