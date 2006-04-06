/*
 * Mar 1, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import com.thinkparity.model.log4j.ModelLoggerFactory;
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
		ProviderManager.addIQProvider("query", "jabber:iq:parity:notifyteammemberadded", new IQTeamMemberAddedNotificationProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:notifyteammemberremoved", new IQTeamMemberRemovedNotificationProvider());
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

	void addListener(final XMPPArtifactListener l) {
		logger.info("[LMODEL] [XMPP] [ADD ARTIFACT LISTENER");
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
	}

	/**
     * Read the artifact team.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return A set of users.
     * @throws SmackException
     */
	Set<User> readTeam(final UUID uniqueId) throws SmackException {
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
     * Receive a notification re a new team member.
     * 
     * @param newTeamMember
     *            The new team member iq.
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
     * Receive a notification re a new team member.
     * 
     * @param newTeamMember
     *            The new team member iq.
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
