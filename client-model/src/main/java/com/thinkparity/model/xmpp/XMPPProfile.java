/*
 * Created On: Jul 6, 2006 11:51:59 AM
 */
package com.thinkparity.model.xmpp;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import com.thinkparity.codebase.VCardBuilder;
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.model.parity.model.io.xmpp.XMPPMethod;
import com.thinkparity.model.parity.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.events.XMPPProfileListener;

/**
 * <b>Title:</b>thinkParity XMPP Profile<br>
 * <b>Description:</b>The profile remote interface implemenation. Handles all
 * remote method innvocations to the thinkParity server for the profile
 * component. Also handles the remote events generated for the profile.
 * 
 * @author raymond@thinkparity.com
 * @version
 * @see XMPPCore
 */
class XMPPProfile extends AbstractXMPP {

    /** Container xmpp event LISTENERS. */
    private static final List<XMPPProfileListener> LISTENERS;

    static {
        LISTENERS = new ArrayList<XMPPProfileListener>();
    }

    /** Create XMPPContainer. */
    XMPPProfile(final XMPPCore xmppCore) {
        super(xmppCore);
    }

    /**
     * Add an email to a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     */
    void addEmail(final JabberId userId, final EMail email) {
        logApiId();
        logVariable("userId", userId);
        logVariable("email", email);
        final XMPPMethod addEmail = new XMPPMethod("profile:addemail");
        addEmail.setParameter("userId", userId);
        addEmail.setParameter("email", email);
        execute(addEmail);
    }

    /**
     * Add an xmpp container event listener.
     * 
     * @param l
     *            The xmpp container event listener.
     */
    void addListener(final XMPPProfileListener l) {
        logApiId();
        logVariable("l", l);
        synchronized (LISTENERS) {
            if (LISTENERS.contains(l)) {
                return;
            } else {
                LISTENERS.add(l);
            }
        }
    }

    /**
     * Add the requisite packet listeners to the xmpp connection.
     * 
     * @param xmppConnection
     *            The xmpp connection.
     */
    void addPacketListeners(final XMPPConnection xmppConnection) {}

    /**
     * Read a profile.
     * 
     * @return A profile.
     */
    Profile read(final JabberId jabberId) throws SmackException {
        logApiId();
        logVariable("jabberId", jabberId);
        final XMPPMethod method = new XMPPMethod("profile:read");
        method.setParameter("jabberId", jabberId);
        final XMPPMethodResponse response = execute(method, Boolean.TRUE);
        final Profile profile = new Profile();
        profile.setId(response.readResultJabberId("jabberId"));
        profile.setName(response.readResultString("name"));
        profile.setOrganization(response.readResultString("organization"));
        profile.setTitle(response.readResultString("title"));
        final VCard jiveVCard = new VCard();
        try { jiveVCard.load(xmppCore.getConnection()); }
        catch(final XMPPException xmppx) {
            throw XMPPErrorTranslator.translate(xmppx);
        }
        profile.setVCard(VCardBuilder.createVCard(jiveVCard));
        return profile;
    }

    List<EMail> readEMails(final JabberId jabberId) {
        logApiId();
        logVariable("jabberId", jabberId);
        final XMPPMethod readEMails = new XMPPMethod("profile:reademails");
        readEMails.setParameter("userId", jabberId);
        final XMPPMethodResponse response = execute(readEMails, Boolean.TRUE);
        return response.readResultEMails("emails");
    }

    /**
     * Remove an email from a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     */
    void removeEmail(final JabberId userId, final EMail email) {
        logApiId();
        logVariable("userId", userId);
        logVariable("email", email);
        final XMPPMethod addEmail = new XMPPMethod("profile:removeemail");
        addEmail.setParameter("userId", userId);
        addEmail.setParameter("email", email);
        execute(addEmail);
    }

    /**
     * Verify an email in a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     * @param key
     *            An email verification key <code>String</code>.
     */
    void verifyEmail(final JabberId userId, final EMail email, final String key) {
        logApiId();
        logVariable("userId", userId);
        logVariable("email", email);
        logVariable("key", key);
        final XMPPMethod verifyEmail = new XMPPMethod("profile:verifyemail");
        verifyEmail.setParameter("userId", userId);
        verifyEmail.setParameter("email", email);
        verifyEmail.setParameter("key", key);
        execute(verifyEmail);
    }
}
