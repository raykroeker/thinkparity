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

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.Constants.Xml.Service;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smackx.packet.user.IQReadUsers;
import com.thinkparity.model.smackx.packet.user.IQReadUsersProvider;
import com.thinkparity.model.smackx.packet.user.IQReadUsersResult;
import com.thinkparity.model.xmpp.user.User;
import com.thinkparity.model.xmpp.user.UserNameBuilder;
import com.thinkparity.model.xmpp.user.UserNameTokenizer;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class XMPPUser {

	static {
		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:readusers", new IQReadUsersProvider());
	}

	/**
     * Obtain a loggable api id.
     * 
     * @param api
     *            The api.
     * @return A loggable api id.
     */
    private static StringBuffer getApiId(final String api) {
        return new StringBuffer("[XMPP] [USER]").append(" ").append(api);
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
		this.logger = Logger.getLogger(getClass());
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
        final VCard vCard = new VCard();
        try {
            vCard.load(xmppCore.getConnection(), jabberId.getQualifiedUsername());
        }
        catch(final XMPPException xmppx) {
            throw XMPPErrorTranslator.translate(xmppx);
        }

        final UserNameBuilder nameBuilder = new UserNameBuilder(
                vCard.getFirstName(), vCard.getMiddleName(), vCard.getLastName());
        final User user = new User();
        user.setId(jabberId);
        user.setName(nameBuilder.getName());
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

    /**
     * Save the user.
     * 
     * @param user
     *            The user.
     * @throws SmackException
     */
    void update(final User user) throws SmackException {
        logger.info("[LMODEL] [XMPP USER] [UPDATE VCARD]");
        logger.debug(user);
        try {
            final JabberId jabberId = xmppCore.getJabberId();
            Assert.assertTrue(
                    getApiId("[UPDATE] [ID DOES NOT MATCH LOGGED IN USER]"),
                    jabberId.equals(user.getId()));
            
            final VCard smackVCard = new VCard();
            smackVCard.load(xmppCore.getConnection(), jabberId.getQualifiedUsername());

            final UserNameTokenizer nameTokenizer = new UserNameTokenizer(user.getName());
            smackVCard.setFirstName(nameTokenizer.getGiven());
            if(nameTokenizer.isSetMiddle())
                smackVCard.setMiddleName(nameTokenizer.getMiddle());
            smackVCard.setLastName(nameTokenizer.getFamily());
            smackVCard.setOrganization(user.getOrganization());

            smackVCard.save(xmppCore.getConnection());
        }
        catch(final XMPPException xmppx) {
            logger.error(getApiId("[UPDATE]"), xmppx);
            throw XMPPErrorTranslator.translate(xmppx);
        }
    }
}
