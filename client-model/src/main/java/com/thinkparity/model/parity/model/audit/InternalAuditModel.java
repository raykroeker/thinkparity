/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit;

import java.util.Collection;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.audit.event.*;
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

	public void audit(final ArchiveEvent archiveEvent) {
		synchronized(getImplLock()) { getImpl().audit(archiveEvent); }
	}

	public void audit(final CloseEvent closeEvent) {
		synchronized(getImplLock()) { getImpl().audit(closeEvent); }
	}

	public void audit(final CreateEvent createEvent) {
		synchronized(getImplLock()) { getImpl().audit(createEvent); }
	}

	public void audit(final KeyRequestDeniedEvent event) {
		synchronized(getImplLock()) { getImpl().audit(event); }
	}

	public void audit(final KeyResponseDeniedEvent event) {
		synchronized(getImplLock()) { getImpl().audit(event); }
	}

	public void audit(final ReceiveEvent receiveEvent) {
		synchronized(getImplLock()) { getImpl().audit(receiveEvent); }
	}

	public void audit(final ReceiveKeyEvent receiveKeyEvent) {
		synchronized(getImplLock()) { getImpl().audit(receiveKeyEvent); }
	}

	public void audit(final RequestKeyEvent requestKeyEvent) {
		synchronized(getImplLock()) { getImpl().audit(requestKeyEvent); }
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

	public Collection<AuditEvent> read(final Long artifactId) {
		synchronized(getImplLock()) { return getImpl().read(artifactId); }
	}
}
