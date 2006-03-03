/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.io.handler;

import java.util.Collection;

import com.thinkparity.model.parity.model.audit.event.*;
import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface AuditIOHandler {
	public void audit(final CloseEvent closeEvent) throws HypersonicException;
	public void audit(final CreateEvent createEvent) throws HypersonicException;
	public void audit(final ReceiveEvent receiveEvent) throws HypersonicException;
	public void audit(final ReceiveKeyEvent receiveKeyEvent) throws HypersonicException;
	public void audit(final RequestKeyEvent requestKeyEvent)
			throws HypersonicException;
	public void audit(final SendEvent sendEvent) throws HypersonicException;
	public void audit(final SendKeyEvent sendKeyEvent) throws HypersonicException;
	public void delete(final Long artifactId) throws HypersonicException;
	public Collection<AuditEvent> list(final Long artifactId)
			throws HypersonicException;
}
