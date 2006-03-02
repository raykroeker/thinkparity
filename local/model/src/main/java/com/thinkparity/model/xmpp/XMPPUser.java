/*
 * Mar 1, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.List;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.VCard;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smackx.packet.user.IQReadUsers;
import com.thinkparity.model.smackx.packet.user.IQReadUsersProvider;
import com.thinkparity.model.smackx.packet.user.IQReadUsersResult;
import com.thinkparity.model.xmpp.user.User;
import com.thinkparity.model.xmpp.user.UserVCard;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class XMPPUser {

	static {
		ProviderManager.addIQProvider("query", "jabber:iq:parity:readusers", new IQReadUsersProvider());
	}

	/**
	 * An apache logger.
	 * 
	 */
	private final Logger logger;

	/**
	 * The core xmpp interface.
	 * 
	 */
	private final XMPPCore xmppCore;

	/**
	 * Create a XMPPUser.
	 * 
	 * @param xmppCore
	 *            The core xmpp interface.
	 */
	public XMPPUser(final XMPPCore xmppCore) {
		super();
		this.logger = ModelLoggerFactory.getLogger(getClass());
		this.xmppCore = xmppCore;
	}

	/**
	 * Add the packet listeners to the connection.
	 * 
	 * @param xmppConnection
	 *            The xmpp connection.
	 */
	void addPacketListeners(final XMPPConnection xmppConnection) {}

	List<User> readUsers(final List<JabberId> jabberIds) throws SmackException {
		logger.info("[XMPP] [USER] [READ USERS]");
		logger.debug(jabberIds);
		final IQ iq = new IQReadUsers(jabberIds);
		final IQReadUsersResult result =
			(IQReadUsersResult) xmppCore.sendAndConfirmPacket(iq);
		return result.getUsers();
	}

	UserVCard readVCard(final JabberId jabberId) throws SmackException {
		logger.info("[XMPP] [USER] [READ VCARD]");
		logger.debug(jabberId);
		try {
			final VCard vCard = new VCard();
			vCard.load(xmppCore.getConnection(), jabberId.getQualifiedUsername());

			final UserVCard userVCard = new UserVCard();
			userVCard.setJabberId(jabberId);
			userVCard.setFirstName(vCard.getFirstName());
			userVCard.setLastName(vCard.getLastName());
			userVCard.setOrganization(vCard.getOrganization());
			return userVCard;
		}
		catch(final XMPPException xmppx) {
			logger.error("Could not load user vCard:  " + jabberId, xmppx);
			throw XMPPErrorTranslator.translate(xmppx);
		}

	}
}
