/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit;

import java.util.Collection;

import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.audit.event.*;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.AuditIOHandler;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class AuditModelImpl extends AbstractModelImpl {

	private final AuditIOHandler auditIO;

	/**
	 * Create a AuditModelImpl.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	AuditModelImpl(final Workspace workspace) {
		super(workspace);
		this.auditIO = IOFactory.getDefault().createAuditHandler();
	}

	void audit(final CloseEvent closeEvent) {
		logger.info("[LMODEL] [AUDIT] [AUDIT CLOSE]");
		logger.debug(closeEvent);
		auditIO.audit(closeEvent);
	}

	void audit(final CreateEvent createEvent) {
		logger.info("[LMODEL] [AUDIT] [AUDIT CREATE]");
		logger.debug(createEvent);
		auditIO.audit(createEvent);
	}

	void audit(final ReceiveEvent receiveEvent) {
		logger.info("[LMODEL] [AUDIT] [AUDIT RECEIVE]");
		logger.debug(receiveEvent);
		auditIO.audit(receiveEvent);
	}

	void audit(final ReceiveKeyEvent receiveKeyEvent) {
		logger.info("[LMODEL] [AUDIT] [AUDIT RECEIVE KEY]");
		logger.debug(receiveKeyEvent);
		auditIO.audit(receiveKeyEvent);
	}

	void audit(final RequestKeyEvent requestKeyEvent) {
		logger.info("[LMODEL] [AUDIT] [AUDIT REQUEST KEY]");
		logger.debug(requestKeyEvent);
		auditIO.audit(requestKeyEvent);
	}

	void audit(final SendEvent sendEvent) {
		logger.info("[LMODEL] [AUDIT] [AUDIT SEND]");
		logger.debug(sendEvent);
		auditIO.audit(sendEvent);
	}

	void audit(final SendKeyEvent sendKeyEvent) {
		logger.info("[LMODEL] [AUDIT] [AUDIT SEND KEY]");
		logger.debug(sendKeyEvent);
		auditIO.audit(sendKeyEvent);
	}

	void delete(final Long artifactId) {
		logger.info("[LMODEL] [AUDIT] [DELETE]");
		logger.debug(artifactId);
		auditIO.delete(artifactId);
	}

	Collection<AuditEvent> read(final Long artifactId) {
		logger.info("[LMODEL] [AUDIT] [READ]");
		logger.debug(artifactId);
		return auditIO.list(artifactId);
	}
}
