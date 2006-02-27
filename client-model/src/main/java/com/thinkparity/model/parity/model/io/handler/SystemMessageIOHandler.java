/*
 * Feb 24, 2006
 */
package com.thinkparity.model.parity.model.io.handler;

import java.util.List;

import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.model.parity.model.message.system.KeyRequestMessage;
import com.thinkparity.model.parity.model.message.system.KeyResponseMessage;
import com.thinkparity.model.parity.model.message.system.PresenceRequestMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessage;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface SystemMessageIOHandler {
	public void create(final KeyRequestMessage keyRequestMessage)
		throws HypersonicException;
	public void create(final KeyResponseMessage keyResponseMessage)
		throws HypersonicException;
	public void create(final PresenceRequestMessage presenceRequestMessage)
			throws HypersonicException;
	public List<SystemMessage> read() throws HypersonicException;
	public PresenceRequestMessage readPresenceRequest(final JabberId jabberId)
			throws HypersonicException;
}
