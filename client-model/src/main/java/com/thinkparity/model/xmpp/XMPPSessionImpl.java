/*
 * Feb 6, 2005
 */
package com.thinkparity.model.xmpp;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.smack.SmackConnectionListener;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smack.SmackRosterListener;
import com.thinkparity.model.smack.packet.SmackPresenceFilter;
import com.thinkparity.model.smack.packet.SmackPresenceListener;
import com.thinkparity.model.smackx.XFactory;
import com.thinkparity.model.smackx.document.XMPPDocumentXFilter;
import com.thinkparity.model.smackx.document.XMPPDocumentXListener;
import com.thinkparity.model.xmpp.document.XMPPDocument;
import com.thinkparity.model.xmpp.events.XMPPExtensionListener;
import com.thinkparity.model.xmpp.events.XMPPPresenceListener;
import com.thinkparity.model.xmpp.events.XMPPSessionListener;
import com.thinkparity.model.xmpp.user.User;

/**
 * XMPPSessionImpl
 * Implementation of the XMPPSession interface into the Smack library.
 * @author raykroeker@gmail.com
 * @version 1.7
 */
public class XMPPSessionImpl implements XMPPSession {

	/**
	 * SmackConnectionListenerImpl Is used to translate smack connection events
	 * to local xmpp session events. Note that the same class is used to listen
	 * for both connection established events and connection termination events.
	 * 
	 * @author raykroeker@gmail.com
	 */
	private class SmackConnectionListenerImpl extends SmackConnectionListener {
		/**
		 * @see org.jivesoftware.smack.ConnectionListener#connectionClosed()
		 */
		public void connectionClosed() { doNotifyConnectionClosed(); }
		/**
		 * @see org.jivesoftware.smack.ConnectionListener#connectionClosedOnError(java.lang.Exception)
		 */
		public void connectionClosedOnError(Exception x) {
			doNotifyConnectionClosedOnError(x);
		}
		/**
		 * @see org.jivesoftware.smack.ConnectionEstablishedListener#connectionEstablished(org.jivesoftware.smack.XMPPConnection)
		 */
		public void connectionEstablished(final XMPPConnection xmppConnection) {
			doNotifyConnectionEstablished(xmppConnection);
		}
	}

	/**
	 * SmackPresenceListenerImpl
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class SmackPresenceListenerImpl extends SmackPresenceListener {
		/**
		 * @see org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet)
		 */
		public void processPacket(final Packet packet) {
			doNotifyProcessPresence((Presence) packet);
		}
	}

	/**
	 * SmackRosterListenerImpl
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class SmackRosterListenerImpl extends SmackRosterListener {
		/**
		 * @see com.thinkparity.model.smack.SmackRosterListener#presenceChanged(String)
		 */
		public void presenceChanged(final String xmppAddress) {
			doNotifyPresenceChanged(xmppAddress);
		}
		/**
		 * @see com.thinkparity.model.smack.SmackRosterListener#rosterModified()
		 */
		public void rosterModified() { doNotifyRosterModified(); }
	}

	/**
	 * Interal logger implemenation.
	 */
	private static final Logger logger =
		ModelLoggerFactory.getLogger(XMPPSessionImpl.class);

	/**
	 * The number of milliseconds to sleep subsequent to each maual packet sent
	 * via the smack connection. If this number is reduced or removed, the smack
	 * library will have a tendancy to swallow messages.
	 * @see XMPPSessionImpl#send(Collection, PacketExtension)
	 */
	private static final int SEND_PACKET_SLEEP_DURATION = 35;

	static {
		// set the subscription mode such that all requests are manual
		Roster.setDefaultSubscriptionMode(Roster.SUBSCRIPTION_MANUAL);
	}

	private Map<String, User> pendingXMPPUsers;
	private SmackConnectionListenerImpl smackConnectionListenerImpl;
	private SmackPresenceFilter smackPresenceFilter;
	private SmackPresenceListenerImpl smackPresenceListenerImpl;
	private SmackRosterListenerImpl smackRosterListenerImpl;
	private SSLXMPPConnection smackXMPPConnection;
	private Vector<XMPPExtensionListener> xmppExtensionListeners;
	private Vector<XMPPPresenceListener> xmppPresenceListeners;
	private Vector<XMPPSessionListener> xmppSessionListeners;

	/**
	 * Create a XMPPSessionImpl
	 */
	XMPPSessionImpl() {
		logger.info("Jive Software:  Smack:  " + SmackConfiguration.getVersion());
		xmppExtensionListeners = new Vector<XMPPExtensionListener>(10);
		xmppPresenceListeners = new Vector<XMPPPresenceListener>(10);
		xmppSessionListeners = new Vector<XMPPSessionListener>(10);

		smackConnectionListenerImpl = new SmackConnectionListenerImpl();
		SSLXMPPConnection.addConnectionListener((ConnectionEstablishedListener) smackConnectionListenerImpl);

		pendingXMPPUsers = new Hashtable<String, User>(10);

		smackRosterListenerImpl = new SmackRosterListenerImpl();

		smackPresenceFilter = new SmackPresenceFilter();
		smackPresenceListenerImpl = new SmackPresenceListenerImpl();
	}

	/**
	 * Accept the presence request of user.  This will send a presence packet
	 * indicating that the user is subscribed and available.
	 * 
	 */
	public void acceptPresence(final User user) throws SmackException {
		logger.info("acceptPresence(UserRenderer)");
		logger.debug(user);
		// save the user for use by the roster event later on
		pendingXMPPUsers.put(user.getUsername(), user);
		sendPresencePacket(user.getUsername(), Type.SUBSCRIBED, Mode.AVAILABLE);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#addListener(com.thinkparity.model.xmpp.events.XMPPExtensionListener)
	 */
	public void addListener(XMPPExtensionListener xmppExtensionListener) {
		logger.info("addListener(XMPPExtensionListener)");
		logger.debug(xmppExtensionListener);
		Assert.assertNotNull("Cannot register a null xmpp extension listener.",
				xmppExtensionListener);
		Assert.assertTrue(
				"Cannot register an extension listener more than once.",
				!xmppExtensionListeners.contains(xmppExtensionListener));
		xmppExtensionListeners.add(xmppExtensionListener);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#addListener(com.thinkparity.model.xmpp.events.XMPPPresenceListener)
	 */
	public void addListener(final XMPPPresenceListener xmppPresenceListener) {
		logger.info("addListener(XMPPPresenceListener)");
		logger.debug(xmppPresenceListener);
		Assert.assertNotNull("Cannot register a null presence listener.",
				xmppPresenceListener);
		Assert.assertTrue("Cannot re-register the same presence listener.",
				!xmppPresenceListeners.contains(xmppPresenceListener));
		xmppPresenceListeners.add(xmppPresenceListener);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#addListener(com.thinkparity.model.xmpp.events.XMPPSessionListener)
	 */
	public void addListener(final XMPPSessionListener xmppSessionListener) {
		logger.info("addListener(XMPPSessionListener)");
		logger.debug(xmppSessionListener);
		Assert.assertNotNull("Cannot register a null session listener.",
				xmppSessionListener);
		Assert.assertTrue("Cannot re-register the same session listener.",
				!xmppSessionListeners.contains(xmppSessionListener));
		xmppSessionListeners.add(xmppSessionListener);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#addRosterEntry(com.thinkparity.model.xmpp.user.User)
	 */
	public void addRosterEntry(final User xmppUser)
			throws SmackException {
		logger.info("addRosterEntry(UserRenderer)");
		logger.debug(xmppUser);
		assertLoggedIn();
		Assert.assertNotNull("Cannot add null user to your roster.", xmppUser);
		// in phase 2, we'll implement the groups
		try {
			final Roster roster = getRoster();
			if(!roster.contains(xmppUser.getUsername()))
				roster.createEntry(xmppUser.getUsername(), xmppUser.getName(), null);
		}
		catch(XMPPException xmppx) {
			throw error("Could not create roster entry.", xmppx);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#denyPresence(com.thinkparity.model.xmpp.user.User)
	 */
	public void denyPresence(User user) throws SmackException {
		logger.info("denyPresence(UserRenderer)");
		logger.debug(user);
		// save the user for use by the roster event later on
		pendingXMPPUsers.put(user.getUsername(), user);
		sendPresencePacket(user.getUsername(), Type.UNSUBSCRIBED);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#getRosterEntries()
	 */
	public Collection<User> getRosterEntries() throws SmackException {
		logger.info("getRosterEntries()");
		assertLoggedIn();
		final Collection<User> rosterEntries =
			new Vector<User>(getRosterEntryCount());
		final Roster roster = getRoster();
		RosterEntry rosterEntry;
		for(Iterator<?> i = roster.getEntries(); i.hasNext();) {
			rosterEntry = (RosterEntry) i.next();
			rosterEntries.add(createUser(roster, rosterEntry));
		}
		return rosterEntries;
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#isLoggedIn()
	 */
	public Boolean isLoggedIn() {
		logger.info("isLoggedIn()");
		return (null != smackXMPPConnection
				&& smackXMPPConnection.isConnected()
				&& smackXMPPConnection.isAuthenticated());
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#login(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	public void login(final String host, final Integer port,
			final String username, final String password) throws SmackException {
		logger.info("login(String, Integer, String, String)");
		logger.debug(host);
		logger.debug(port);
		logger.debug(username);
		try {
			if(Boolean.TRUE == isLoggedIn())
				logout();
			smackXMPPConnection = new SSLXMPPConnection(host, port);
			smackXMPPConnection.addConnectionListener((ConnectionListener) smackConnectionListenerImpl);

			// document extension handler
			smackXMPPConnection.addPacketListener(
					new XMPPDocumentXListener() {
						public void documentRecieved(
								final XMPPDocument xmppDocument) {
							handleDocumentRecieved(xmppDocument);
						}
					},
					new XMPPDocumentXFilter());

			// packet debugger
			smackXMPPConnection.addPacketListener(
					new PacketListener() {
						public void processPacket(Packet packet) {
							logger.debug(packet);
						}
					},
					new PacketFilter() {
						public boolean accept(Packet packet) { return true; }
					});

			smackXMPPConnection.addPacketListener(smackPresenceListenerImpl, smackPresenceFilter);

			smackXMPPConnection.login(username, password, "parity");
			smackXMPPConnection.getRoster().addRosterListener(smackRosterListenerImpl);
		}
		catch(XMPPException xmppx) {
			throw error("Could not establish an xmpp connection.", xmppx);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#logout()
	 */
	public void logout() throws SmackException {
		logger.info("logout()");
		smackXMPPConnection.close();
		smackXMPPConnection = null;
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#removeListener(com.thinkparity.model.xmpp.events.XMPPExtensionListener)
	 */
	public void removeListener(XMPPExtensionListener xmppExtensionListener) {
		logger.info("removeListener(XMPPExtensionListener)");
		logger.debug(xmppExtensionListener);
		Assert.assertNotNull("Cannot remove a null xmpp extension listener.",
				xmppExtensionListener);
		Assert.assertTrue(
				"Cannot remove an extension listener that has note been registered.",
				xmppExtensionListeners.contains(xmppExtensionListener));
		xmppExtensionListeners.remove(xmppExtensionListener);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#removeListener(com.thinkparity.model.xmpp.events.XMPPPresenceListener)
	 */
	public void removeListener(final XMPPPresenceListener xmppPresenceListener) {
		logger.info("removeListener(XMPPPresenceListener)");
		logger.debug(xmppPresenceListener);
		Assert.assertNotNull("Cannot un-register a null presence listener.",
				xmppPresenceListener);
		Assert.assertTrue(
				"Cannot un-register a non-registered presence listener.",
				xmppPresenceListeners.contains(xmppPresenceListener));
		xmppPresenceListeners.remove(xmppPresenceListener);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#removeListener(com.thinkparity.model.xmpp.events.XMPPSessionListener)
	 */
	public void removeListener(final XMPPSessionListener xmppSessionListener) {
		logger.info("removeListener(XMPPSessionListener)");
		logger.debug(xmppSessionListener);
		Assert.assertNotNull("Cannot un-register a null session listener.",
				xmppSessionListener);
		Assert.assertTrue(
				"Cannot unregister a non-registered session listener.",
				xmppSessionListeners.contains(xmppSessionListener));
		xmppSessionListeners.remove(xmppSessionListener);
	}

	/**
	 * Send the document to the user. This is done by creating a smack packet
	 * extension for the xmpp document, using the xstream library to serialize
	 * the xmpp document; then sending the packet extension as an attachment to
	 * a message to the user.
	 * 
	 * @param users
	 *            The users to send the document to.
	 * @param xmppDocument
	 *            The document to send.
	 * @see XMPPSessionImpl#send(Collection, PacketExtension)
	 */
	public void send(final Collection<User> users, final XMPPDocument xmppDocument)
			throws SmackException {
		logger.info("send(Collection<UserRenderer>,XMPPDocument)");
		logger.debug(users);
		logger.debug(xmppDocument);
		send(users, XFactory.createPacketX(xmppDocument));
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#updateRosterEntry(com.thinkparity.model.xmpp.user.User)
	 */
	public void updateRosterEntry(User user) {
		logger.info("updateRosterEntry(UserRenderer)");
		logger.debug(user);
		final Roster roster = getRoster();
		final RosterEntry rosterEntry = roster.getEntry(user.getUsername());
		rosterEntry.setName(user.getName());
	}

	private void assertLoggedIn() {
		Assert.assertTrue("Cannot perform action.  UserRenderer is not logged in.",
				smackXMPPConnection.isAuthenticated());
	}

	private User createUser(final Roster roster, final RosterEntry rosterEntry) {
		final Presence presence = roster.getPresence(rosterEntry.getUser());

		final User.Presence userPresence;
		if(presence == null) { userPresence = User.Presence.OFFLINE; }
		else {
			final Type type = presence.getType();
			final Mode mode = presence.getMode();
			if(Type.AVAILABLE == type && Mode.AVAILABLE == mode) {
				userPresence = User.Presence.AVAILABLE;
			}
			else { userPresence = User.Presence.UNAVAILABLE; }
		}
		return new User(rosterEntry.getName(), rosterEntry.getUser(), userPresence);
	}

	/**
	 * Iterate through the list of xmppPresenceListeners and fire the
	 * presenceRequested event.
	 * 
	 * @param packet
	 *            <code>org.jivesoftware.packet.Packet</code>
	 */
	private void doFirePresenceRequested(final Packet packet) {
		final User fromXMPPUser = getXMPPUser_From(packet);
		for(XMPPPresenceListener listener : xmppPresenceListeners) {
			listener.presenceRequested(fromXMPPUser);
		}
	}

	/**
	 * Event handler for the connectionClosed event generated by the smack
	 * connection listener impl. This will iterate through the
	 * xmppSessionListeners list and fire the sessionTerminated event.
	 */
	private void doNotifyConnectionClosed() {
		for (Iterator<XMPPSessionListener> i = xmppSessionListeners.iterator(); i
				.hasNext();) {
			i.next().sessionTerminated();
		}	
	}

	/**
	 * Event handler for the connectionClosedOnerror event generated by the
	 * smack connection listener impl. This will iterate through the
	 * xmppSessionListeners list and fire the sessionTerminated event.
	 * 
	 * @param x
	 *            <code>java.lang.Exception</code>
	 */
	private void doNotifyConnectionClosedOnError(final Exception x) {
		for (Iterator<XMPPSessionListener> i = xmppSessionListeners.iterator(); i
				.hasNext();) {
			i.next().sessionTerminated(x);
		}
	}

	/**
	 * Event handler for the connectionEstablished event generated by the smack
	 * connection listener impl. This will iterate through the
	 * xmppSessionListeners list and fire the sessionEstablished event.
	 * 
	 * @param xmppConnection
	 *            <code>org.jivesoftware.smack.XMPPConnection</code>
	 */
	private void doNotifyConnectionEstablished(final XMPPConnection xmppConnection) {
		for (Iterator<XMPPSessionListener> i = xmppSessionListeners.iterator(); i
				.hasNext();) {
			i.next().sessionEstablished();
		}
	}

	/**
	 * Event handler for the presenceChanged event generated by the
	 * smackRosterListenerImpl. This will check to see if there is a pending
	 * user in the map for the given address, and if there is, it will update
	 * the roster name accordingly.
	 * 
	 * @param xmppAddress
	 *            <code>java.lang.String</code>
	 */
	private void doNotifyPresenceChanged(final String xmppAddress) {
		logger.debug(xmppAddress);
		final User pendingXMPPUser = pendingXMPPUsers.remove(xmppAddress);
		if(null != pendingXMPPUser) {
			final RosterEntry rosterEntry = getRosterEntry(pendingXMPPUser);
			rosterEntry.setName(pendingXMPPUser.getName());
		}
	}

	/**
	 * Event handler for the procesPacket event generated by the
	 * smackPresenceListenerImpl. If the presence packet type is a subscription
	 * request, a presence requested event is passed on to the
	 * xmppPresenceListeners list. If the presence packet type is a subscription
	 * accepted notification, and the current user's roster does not have an
	 * entry for the requesting user, a subscription accepted event is sent
	 * back.
	 * 
	 * @param presence
	 *            <code>org.jivesoftware.packet.Presence</code>
	 */
	private void doNotifyProcessPresence(final Presence presence) {
		logger.debug(presence);
		final Type presencePacketType = presence.getType();
		if(presencePacketType == Type.SUBSCRIBE) {
			doFirePresenceRequested(presence);
		}
		else if(presencePacketType == Type.SUBSCRIBED) {
			final Roster roster = getRoster();
			final Presence rosterPresence = roster.getPresence(presence.getFrom());
			if(null == rosterPresence) {
				sendPresenceAcceptance(presence.getFrom());
			}
		}
	}

	/**
	 * Handle the roster modified event generated by the smack roster impl. This
	 * will obtain an instance of the roster and reload it.
	 */
	private void doNotifyRosterModified() {
		final Integer entryCount = getRosterEntryCount();
		logger.debug(entryCount);
	}

	/**
	 * Log the message\cause using the internal logger, then construct an
	 * appropriate SmackException for throwing.
	 * 
	 * @param message
	 *            <code>java.lang.String</code>
	 * @param cause
	 *            <code>java.lang.Throwable</code>
	 * @return <code>org.kcs.projectmanager.smack.SmackException</code>
	 */
	private SmackException error(final String message, final Throwable cause) {
		logger.error(message, cause);
		return new SmackException(message);
	}

	/**
	 * Obtain the roster from the underlying connection.  The roster is not
	 * automatically reloaded.
	 * 
	 * @return <code>org.jivesoftware.smack.Roster</code>
	 */
	private Roster getRoster() { return smackXMPPConnection.getRoster(); }

	/**
	 * Obtain the roster entry for a given user in 3 stages.  All versions of
	 * the username are attempted.
	 * username
	 * username@host.com
	 * username@host.com/resource
	 * @param username <code>java.lang.String</code>
	 * @return <code>java.lang.String</code>
	 * @throws SmackException
	 */
	private RosterEntry getRosterEntry(final User xmppUser) {
		final Roster roster = getRoster();
		RosterEntry rosterEntry = roster.getEntry(xmppUser.getUsername());
		if(null != rosterEntry) { return rosterEntry; }
		else {
			// check if there's a trailing resource on the username, and if so
			// strip it and try again
			final String username = xmppUser.getUsername();
			if(0 < username.lastIndexOf("/")) {
				rosterEntry = roster.getEntry(StringUtil.removeAfter(username, "/"));
				if(null != rosterEntry) { return rosterEntry; }
				else {
					if(0 < username.lastIndexOf("@")) {
						rosterEntry = roster.getEntry(StringUtil.removeAfter(username, "@"));
						if(null != rosterEntry) { return rosterEntry; }
					}
				}
			}
		}
		return null;
	}

	/**
	 * Obtain the number of entries in the current roster.
	 * 
	 * @return <code>java.lang.Integer</code>
	 */
	private Integer getRosterEntryCount() { return getRoster().getEntryCount(); }

	/**
	 * Extract an UserRenderer using the packet information.
	 * @param packet <code>org.jivesoftware.smack.packet.Packet</code>
	 * @return <code>org.kcs.projectmanager.xmpp.user.XMPPUser</code>
	 */
	private User getXMPPUser_From(final Packet packet) {
		// TODO:  Figure out a way to extract the name.
		return new User(null, packet.getFrom(), User.Presence.OFFLINE);
	}

	/**
	 * Event handler that gets called when an xmpp document is recieved via the
	 * smack connection.
	 * 
	 * @param xmppDocument
	 *            The xmpp document.
	 */
	private void handleDocumentRecieved(final XMPPDocument xmppDocument) {
		for(XMPPExtensionListener listener : xmppExtensionListeners) {
			listener.documentReceived(xmppDocument);
		}
	}

	/**
	 * Send a packet extension to a single user.
	 * 
	 * @param user
	 *            The user to send the packet extension to.
	 * @param packetExtension
	 *            The packet extension to send.
	 */
	private void send(final Collection<User> users,
			final PacketExtension packetExtension) {
		logger.debug(users);
		logger.debug(packetExtension);
		for(User user : users) {
			final Message message = smackXMPPConnection.createChat(
					user.getUsername()).createMessage();
			message.addExtension(packetExtension);
			sendPacket(message);
		}
	}

	/**
	 * Send a packet through the smack xmpp connection.
	 * 
	 * @param packet
	 *            The packet to send.
	 */
	private void sendPacket(final Packet packet) {
		smackXMPPConnection.sendPacket(packet);
		// this sleep has been inserted because when packets are sent within
		// 30 milliseconds of each other, they tend to get swallowed by the
		// smack library
		try { Thread.sleep(SEND_PACKET_SLEEP_DURATION); }
		catch(InterruptedException ix) { logger.error(ix); }
	}

	/**
	 * Send a subscription acceptance presence packet to username.
	 * @param username <code>java.lang.String</code.
	 */
	private void sendPresenceAcceptance(final String username) {
		sendPresencePacket(username, Type.SUBSCRIBED, Mode.AVAILABLE);
	}

	/**
	 * Send a presence packet to a user.
	 * 
	 * @param username
	 *            The user to send the packet to.
	 * @param type
	 *            The type of packet to send.
	 */
	private void sendPresencePacket(final String username,
			final Presence.Type type) {
		sendPresencePacket(username, type, null);
	}

	/**
	 * Send a presence packet to a user.
	 * 
	 * @param username
	 *            The user to send the packet to.
	 * @param type
	 *            The type of packet to send.
	 * @param mode
	 *            The mode of the packet. If the mode is null, no mode is set.
	 * @see XMPPSessionImpl#sendPresencePacket(String, Presence.Type)
	 */
	private void sendPresencePacket(final String username, final Presence.Type type,
			final Presence.Mode mode) {
		final Presence presence = new Presence(type);
		if(null != mode) { presence.setMode(mode); }
		presence.setTo(username);
		presence.setFrom(smackXMPPConnection.getUser());
		sendPacket(presence);
	}
}
