/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.io.handler;

import com.thinkparity.model.parity.model.audit.event.CloseEvent;
import com.thinkparity.model.parity.model.audit.event.CreateEvent;
import com.thinkparity.model.parity.model.audit.event.ReceiveEvent;
import com.thinkparity.model.parity.model.audit.event.ReceiveKeyEvent;
import com.thinkparity.model.parity.model.audit.event.SendEvent;
import com.thinkparity.model.parity.model.audit.event.SendKeyEvent;
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
	public void audit(final SendEvent sendEvent) throws HypersonicException;
	public void audit(final SendKeyEvent sendKeyEvent) throws HypersonicException;
	public void delete(final Long artifactId) throws HypersonicException;
}
