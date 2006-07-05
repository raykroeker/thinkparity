/*
 * Mar 1, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import com.thinkparity.model.Constants.Xml;
import com.thinkparity.model.parity.model.io.xmpp.XMPPMethod;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smackx.packet.artifact.*;
import com.thinkparity.model.xmpp.events.XMPPArtifactListener;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class XMPPArtifact {

	private static final List<XMPPArtifactListener> listeners;

	static {
		listeners = new LinkedList<XMPPArtifactListener>();

		ProviderManager.addIQProvider("query", "jabber:iq:parity:artifactreadcontacts", new IQReadContactsProvider());
        ProviderManager.addIQProvider("query", "jabber:iq:parity:artifactconfirmreceipt", new IQConfirmReceiptProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:notifyteammemberadded", new IQTeamMemberAddedNotificationProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:notifyteammemberremoved", new IQTeamMemberRemovedNotificationProvider());
        ProviderManager.addIQProvider("query", "jabber:iq:parity:artifact:reactivated", new IQArtifactReactivatedProvider());
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
		this.logger = Logger.getLogger(getClass());
	}

    /**
     * Call the remote artifact:reactivate method.
     * 
     * @param team
     *            The artifact team.
     * @param uniqueId
     *            The artifact unique id.
     */
    void reactivate(final List<JabberId> team, final UUID uniqueId) {
        logger.info("[LMODEL] [XMPP] [ARTIFACT] [REACTIVATE]");
        logger.debug(team);
        logger.debug(uniqueId);
        final XMPPMethod method = new XMPPMethod("artifact:reactivate");
        method.setJabberIdParameters(Xml.User.JABBER_IDS, Xml.User.JABBER_ID, team);
        method.setParameter(Xml.Artifact.UNIQUE_ID, uniqueId);
        method.execute(xmppCore.getConnection());
    }

	void addListener(final XMPPArtifactListener l) {
		logger.info("[LMODEL] [XMPP] [ARTIFACT] [ADD ARTIFACT LISTENER");
		logger.debug(l);
		synchronized(listeners) {
			if(listeners.contains(l)) { return; }
			listeners.add(l);
		}
	}


	/**
	 * Add the packet listeners to the connection.
	 * 
	 * @param xmppConnection
	 *            The xmpp connection.
	 */
	void addPacketListeners(final XMPPConnection xmppConnection) {
		xmppConnection.addPacketListener(
				new PacketListener() {
					public void processPacket(final Packet packet) {
						notifyTeamMemberAdded((IQTeamMemberAddedNotification) packet);
					}
				},
				new PacketTypeFilter(IQTeamMemberAddedNotification.class));
		xmppConnection.addPacketListener(
				new PacketListener() {
					public void processPacket(final Packet packet) {
						notifyTeamMemberRemoved((IQTeamMemberRemovedNotification) packet);
					}
				},
				new PacketTypeFilter(IQTeamMemberRemovedNotification.class));

		xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        notifyArtifactConfirmation((IQConfirmArtifactReceipt) packet);
                    }
                },
                new PacketTypeFilter(IQConfirmArtifactReceipt.class));
	}

	/**
     * Confirm artifact receipt.
     * 
     * @param receivedFrom
     *            From whom the artifact was received.
     * @param uniqueId
     *            The artifact unique id.
     * @param versionId
     *            The artifact version id.
     */
	void confirmReceipt(final JabberId receivedFrom, final UUID uniqueId,
            final Long versionId) throws SmackException {
	    logger.info("[LMODEL] [XMPP] [CONFIRM ARTIFACT RECEIPT]");
	    logger.debug(receivedFrom);
	    logger.debug(uniqueId);
        final IQConfirmArtifactReceipt iq = new IQConfirmArtifactReceipt();
        iq.setArtifactUUID(uniqueId);
        iq.setArtifactVersionId(versionId);
        iq.setRecievedFrom(receivedFrom);
        iq.setType(IQ.Type.SET);
        xmppCore.sendAndConfirmPacket(iq);
	}

    /**
     * Read the artifact team.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return A set of users.
     * @throws SmackException
     */
	List<User> readTeam(final UUID uniqueId) throws SmackException {
		logger.info("[LMODEL] [XMPP] [ARTIFACT] [READ TEAM]");
		logger.debug(uniqueId);
		final IQ iq = new IQReadContacts(uniqueId);
		iq.setType(IQ.Type.GET);
		final IQReadContactsResult result =
			(IQReadContactsResult) xmppCore.sendAndConfirmPacket(iq);
		return result.getContacts();
	}

	void removeListener(final XMPPArtifactListener l) {
		logger.info("[LMODEL] [XMPP] [REMOVE ARTIFACT LISTENER]");
		logger.debug(l);
		synchronized(listeners) {
			if(!listeners.contains(l)) { return; }
			listeners.remove(l);
		}
	}

	/**
     * Receive a notifcation re a artifact confirmation receipt.
     * 
     * @param confirmationPacket
     *            The confirmation packet.
     */
    private void notifyArtifactConfirmation(
            final IQConfirmArtifactReceipt packet) {
        synchronized(listeners) {
            for(final XMPPArtifactListener l : listeners) {
                l.confirmReceipt(packet.getArtifactUUID(),
                        packet.getArtifactVersionId(), packet.getFromJabberId());
            }
        }
    }

	/**
     * Receive a notification re team member addition
     * 
     * @param notificationPacket
     *            The notification packet.
     */
	private void notifyTeamMemberAdded(final IQTeamMemberAddedNotification notificationPacket) {
		synchronized(listeners) {
			for(final XMPPArtifactListener l : listeners) {
				l.teamMemberAdded(notificationPacket.getArtifactUniqueId(),
						notificationPacket.getNewTeamMember());
			}
		}
	}

    /**
     * Receive a notification re team member removal.
     * 
     * @param notificationPacket
     *            The notification packet.
     */
	private void notifyTeamMemberRemoved(final IQTeamMemberRemovedNotification notificationPacket) {
		synchronized(listeners) {
			for(final XMPPArtifactListener l : listeners) {
				l.teamMemberRemoved(notificationPacket.getArtifactUniqueId(),
						notificationPacket.getTeamMember());
			}
		}
	}
}
