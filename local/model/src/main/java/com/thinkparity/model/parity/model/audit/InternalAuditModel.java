/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.audit.event.CloseEvent;
import com.thinkparity.model.parity.model.audit.event.CreateEvent;
import com.thinkparity.model.parity.model.audit.event.ReceiveEvent;
import com.thinkparity.model.parity.model.audit.event.ReceiveKeyEvent;
import com.thinkparity.model.parity.model.audit.event.SendEvent;
import com.thinkparity.model.parity.model.audit.event.SendKeyEvent;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InternalAuditModel extends AuditModel {

	/**
	 * Create a InternalAuditModel.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 * @param context
	 *            The parity model context.
	 */
	InternalAuditModel(final Workspace workspace, final Context context) {
		super(workspace);
		context.assertContextIsValid();
	}

	public void audit(final CloseEvent closeEvent) {
		synchronized(getImplLock()) { getImpl().audit(closeEvent); }
	}

	public void audit(final CreateEvent createEvent) {
		synchronized(getImplLock()) { getImpl().audit(createEvent); }
	}

	public void audit(final ReceiveEvent receiveEvent) {
		synchronized(getImplLock()) { getImpl().audit(receiveEvent); }
	}

	public void audit(final ReceiveKeyEvent receiveKeyEvent) {
		synchronized(getImplLock()) { getImpl().audit(receiveKeyEvent); }
	}

	public void audit(final SendEvent sendEvent) {
		synchronized(getImplLock()) { getImpl().audit(sendEvent); }
	}

	public void audit(final SendKeyEvent sendKeyEvent) {
		synchronized(getImplLock()) { getImpl().audit(sendKeyEvent); }
	}

	public void delete(final Long artifactId) {
		synchronized(getImplLock()) { getImpl().delete(artifactId); }
	}
}
