/*
 * Mar 1, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.Set;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.VCard;

import com.thinkparity.model.LoggerFactory;
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
		this.logger = LoggerFactory.getLogger(getClass());
		this.xmppCore = xmppCore;
	}

    /**
	 * Add the packet listeners to the connection.
	 * 
	 * @param xmppConnection
	 *            The xmpp connection.
	 */
	void addPacketListeners(final XMPPConnection xmppConnection) {}

	/**
     * Build a user from a jabber id.
     * 
     * @param jabberId
     *            The jabber id.
     * @return A user.
     * @throws SmackException
     */
    User read(final JabberId jabberId) throws SmackException {
        final UserVCard vCard = readVCard(jabberId);
        final User user = new User();
        user.setEmail(vCard.getEmail());
        user.setName(vCard.getName());
        user.setId(jabberId);
        user.setOrganization(vCard.getOrganization());
        return user;
    }

    /**
     * Read a set of users.
     * 
     * @param jabberIds
     *            A set of user ids.
     * @return A set of users.
     * @throws SmackException
     */
	Set<User> read(final Set<JabberId> jabberIds) throws SmackException {
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
            userVCard.setEmail(vCard.getEmailWork());
			userVCard.setJabberId(jabberId);
            userVCard.setName(vCard.getFirstName(), vCard.getMiddleName(), vCard.getLastName());
			userVCard.setOrganization(vCard.getOrganization());
			return userVCard;
		}
		catch(final XMPPException xmppx) {
			logger.error("Could not load user vCard:  " + jabberId, xmppx);
			throw XMPPErrorTranslator.translate(xmppx);
		}

	}

    /**
     * Save the vCard info for the user.
     * 
     * @param jabberId
     *            The user id.
     * @param vCard
     *            The user's vcard info.
     * @throws SmackException
     */
    void updateVCard(final JabberId jabberId, final UserVCard vCard)
            throws SmackException {
        logger.info("[LMODEL] [XMPP USER] [UPDATE VCARD]");
        logger.debug(jabberId);
        logger.debug(vCard);
        try {
            final VCard smackVCard = new VCard();
            smackVCard.load(xmppCore.getConnection(), jabberId.getQualifiedUsername());

            smackVCard.setFirstName(vCard.getFirstName());
            if(vCard.isSetMiddleName())
                smackVCard.setMiddleName(vCard.getMiddleName());
            smackVCard.setLastName(vCard.getLastName());
            smackVCard.setEmailHome(vCard.getEmail());
            smackVCard.setOrganization(vCard.getOrganization());
            smackVCard.save(xmppCore.getConnection());
        }
        catch(final XMPPException xmppx) {
            logger.error("", xmppx);
            throw XMPPErrorTranslator.translate(xmppx);
        }
    }
}
