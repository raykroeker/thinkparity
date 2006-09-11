/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.io.handler;

import java.util.List;

import com.thinkparity.ophelia.model.audit.event.*;
import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface AuditIOHandler {
    public void audit(final AddTeamMemberEvent event) throws HypersonicException;
    public void audit(final AddTeamMemberConfirmEvent event) throws HypersonicException;
	public void audit(final ArchiveEvent event)
            throws HypersonicException;
	public void audit(final CloseEvent event) throws HypersonicException;
    public void audit(final CreateEvent event) throws HypersonicException;
    public void audit(final CreateRemoteEvent event) throws HypersonicException;
	public void audit(final KeyRequestDeniedEvent event) throws HypersonicException;
	public void audit(final KeyResponseDeniedEvent event) throws HypersonicException;
	public void audit(final PublishEvent event) throws HypersonicException;
    public void audit(final ReactivateEvent event) throws HypersonicException;
	public void audit(final ReceiveEvent event) throws HypersonicException;
	public void audit(final ReceiveKeyEvent event) throws HypersonicException;
	public void audit(final RenameEvent event) throws HypersonicException;
	public void audit(final RequestKeyEvent event)
			throws HypersonicException;
	public void audit(final SendEvent event) throws HypersonicException;
	public void audit(final SendConfirmEvent event)
            throws HypersonicException;
	public void audit(final SendKeyEvent event) throws HypersonicException;
	public void delete(final Long artifactId) throws HypersonicException;
	public List<AuditEvent> list(final Long artifactId)
			throws HypersonicException;
}
