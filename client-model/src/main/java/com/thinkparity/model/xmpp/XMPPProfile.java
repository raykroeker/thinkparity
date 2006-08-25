/*
 * Created On: Jul 6, 2006 11:51:59 AM
 */
package com.thinkparity.model.xmpp;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import com.thinkparity.codebase.VCardBuilder;

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
class XMPPProfile {

    /** Container xmpp event LISTENERS. */
    private static final List<XMPPProfileListener> LISTENERS;

    static {
        LISTENERS = new ArrayList<XMPPProfileListener>();
    }

    /** Core xmpp functionality. */
    private final XMPPCore core;

    /** An apache logger. */
    private final Logger logger;

    /** Create XMPPContainer. */
    XMPPProfile(final XMPPCore core) {
        super();
        this.core = core;
        this.logger = Logger.getLogger(getClass());
    }

    /**
     * Add an xmpp container event listener.
     * 
     * @param l
     *            The xmpp container event listener.
     */
    void addListener(final XMPPProfileListener l) {
        logger.info("[LMODEL] [XMPP] [PROFILE] [ADD LISTENER");
        logger.debug(l);
        synchronized(LISTENERS) {
            if(LISTENERS.contains(l)) { return; }
            LISTENERS.add(l);
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
        logger.info("[LMODEL] [XMPP] [PROFILE] [READ]");
        logger.debug(jabberId);
        final XMPPMethod method = new XMPPMethod("profile:read");
        method.setParameter("jabberId", jabberId);
        final XMPPMethodResponse response = method.execute(core.getConnection());
        core.assertContainsResult("[LMODEL] [XMPP] [PROFILE] [READ] [RESPONSE IS EMPTY]", response);
        final Profile profile = new Profile();
        profile.setId(response.readResultJabberId("jabberId"));
        profile.setName(response.readResultString("name"));
        profile.setOrganization(response.readResultString("organization"));
        final VCard jiveVCard = new VCard();
        try { jiveVCard.load(core.getConnection()); }
        catch(final XMPPException xmppx) {
            throw XMPPErrorTranslator.translate(xmppx);
        }
        profile.setVCard(VCardBuilder.createVCard(jiveVCard));
        profile.addAllEmails(response.readResultEMails("emails"));
        return profile;
    }
}
