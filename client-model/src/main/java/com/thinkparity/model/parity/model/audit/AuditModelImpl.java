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
		logger.info("audit(CloseEvent)");
		logger.debug(closeEvent);
		auditIO.audit(closeEvent);
	}

	void audit(final CreateEvent createEvent) {
		logger.info("audit(CreateEvent)");
		logger.debug(createEvent);
		auditIO.audit(createEvent);
	}

	void audit(final ReceiveEvent receiveEvent) {
		logger.info("audit(Receive)");
		logger.debug(receiveEvent);
		auditIO.audit(receiveEvent);
	}

	void audit(final ReceiveKeyEvent receiveKeyEvent) {
		logger.info("audit(ReceiveKey)");
		logger.debug(receiveKeyEvent);
		auditIO.audit(receiveKeyEvent);
	}

	void audit(final SendEvent sendEvent) {
		logger.info("audit(SendEvent)");
		logger.debug(sendEvent);
		auditIO.audit(sendEvent);
	}

	void audit(final SendKeyEvent sendKeyEvent) {
		logger.info("audit(SendKeyEvent)");
		logger.debug(sendKeyEvent);
		auditIO.audit(sendKeyEvent);
	}

	void delete(final Long artifactId) {
		logger.info("delete(Long)");
		logger.debug(artifactId);
		auditIO.delete(artifactId);
	}

	Collection<AuditEvent> read(final Long artifactId) {
		logger.info("list(Long)");
		logger.debug(artifactId);
		return auditIO.list(artifactId);
	}
}
