/*
 * Mar 1, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smackx.packet.artifact.IQReadContacts;
import com.thinkparity.model.smackx.packet.artifact.IQReadContactsProvider;
import com.thinkparity.model.smackx.packet.artifact.IQReadContactsResult;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class XMPPArtifact {

	static {
		ProviderManager.addIQProvider("query", "jabber:iq:parity:artifactreadcontacts", new IQReadContactsProvider());
	}

	/**
	 * An apache logger.
	 * 
	 */
	private final Logger logger;

	/**
	 * The xmpp core functionality.
	 * 
	 */
	private final XMPPCore xmppCore;

	/**
	 * Create a XMPPArtifact.
	 * 
	 * @param xmppCore
	 *            The xmpp core functionality.
	 */
	XMPPArtifact(final XMPPCore xmppCore) {
		super();
		this.xmppCore = xmppCore;
		this.logger = ModelLoggerFactory.getLogger(getClass());
	}

	/**
	 * Add the packet listeners to the connection.
	 * 
	 * @param xmppConnection
	 *            The xmpp connection.
	 */
	void addPacketListeners(final XMPPConnection xmppConnection) {}

	/**
	 * Read the contacts for an artifact.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @return The artifact contacts.
	 */
	List<Contact> readContacts(final UUID artifactUniqueId) throws SmackException {
		logger.info("[XMPP] [ARTIFACT] [READ CONTACTS]");
		logger.debug(artifactUniqueId);
		final IQ iq = new IQReadContacts(artifactUniqueId);
		iq.setType(IQ.Type.GET);
		final IQReadContactsResult result =
			(IQReadContactsResult) xmppCore.sendAndConfirmPacket(iq);
		return result.getContacts();
	}
}
