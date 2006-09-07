/*
 * Mar 1, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.Set;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.VCard;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.Constants.Xml.Service;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smackx.packet.user.IQReadUsers;
import com.thinkparity.model.smackx.packet.user.IQReadUsersProvider;
import com.thinkparity.model.smackx.packet.user.IQReadUsersResult;
import com.thinkparity.model.xmpp.user.User;
import com.thinkparity.model.xmpp.user.UserNameBuilder;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class XMPPUser extends AbstractXMPP {

	static {
		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:readusers", new IQReadUsersProvider());
	}

    /**
	 * Create a XMPPUser.
	 * 
	 * @param xmppCore
	 *            The core xmpp interface.
	 */
	public XMPPUser(final XMPPCore xmppCore) {
		super(xmppCore);
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
        logApiId();
        logVariable("jabberId", jabberId);
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
		logApiId();
		logVariable("jabberIds", jabberIds);
		final IQ iq = new IQReadUsers(jabberIds);
		final IQReadUsersResult result =
			(IQReadUsersResult) xmppCore.sendAndConfirmPacket(iq);
		return result.getUsers();
	}
}
