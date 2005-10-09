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
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionEstablishedListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SSLXMPPConnection;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Loggable;
import com.thinkparity.model.parity.api.ParityObjectVersion;
import com.thinkparity.model.parity.api.document.DocumentVersion;
import com.thinkparity.model.parity.util.log4j.ModelLoggerFactory;
import com.thinkparity.model.smack.SmackConnectionListener;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smack.SmackRosterListener;
import com.thinkparity.model.smack.packet.SmackPacketFilter;
import com.thinkparity.model.smack.packet.SmackPacketListener;
import com.thinkparity.model.smack.packet.SmackPresenceFilter;
import com.thinkparity.model.smack.packet.SmackPresenceListener;
import com.thinkparity.model.smackx.UnsupportedXTypeException;
import com.thinkparity.model.smackx.XFactory;
import com.thinkparity.model.smackx.packet.DocumentVersionX;
import com.thinkparity.model.smackx.packet.DocumentVersionXFilter;
import com.thinkparity.model.smackx.packet.DocumentVersionXListener;
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
	 * Interal logger implemenation.
	 */
	private static final Logger logger =
		ModelLoggerFactory.getLogger(XMPPSessionImpl.class);

	/**
	 * Logger helper class that will format XMPP specific objects into loggable
	 * statements.
	 */
	private static final XMPPLoggerFormatter loggerFormatter =
		new XMPPLoggerFormatter();

	static {
		// set the subscription mode such that all requests are manual
		Roster.setDefaultSubscriptionMode(Roster.SUBSCRIPTION_MANUAL);
	}

	/**
	 * DocumentVersionXListenerImpl
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class DocumentVersionXListenerImpl extends DocumentVersionXListener {
		/**
		 * @see com.thinkparity.model.smackx.packet.DocumentVersionXListener#processDocumentVersion(com.thinkparity.model.smackx.packet.DocumentVersionX)
		 */
		public void processDocumentVersion(
				final DocumentVersionX documentVersionX) {
			doNotifyProcessDocumentVersionX(documentVersionX);			
		}
	}

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
	 * SmackPacketListenerImpl
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class SmackPacketListenerImpl extends SmackPacketListener {
		/**
		 * @see org.jivesoftware.smack.packet.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet)
		 */
		public void processPacket(Packet packet) {
			doNotifyProcessPacket(packet);			
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

	private DocumentVersionXFilter documentVersionXFilter;
	private DocumentVersionXListenerImpl documentVersionXListenerImpl;
	private Map<String, User> pendingXMPPUsers;
	private SmackConnectionListenerImpl smackConnectionListenerImpl;
	private SmackPacketFilter smackPacketFilter;
	private SmackPacketListenerImpl smackPacketListenerImpl;
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
		debug("XMPPSessionImpl<init>:SmackConfiguration.getVersion()",
				SmackConfiguration.getVersion());
		xmppExtensionListeners = new Vector<XMPPExtensionListener>(10);
		xmppPresenceListeners = new Vector<XMPPPresenceListener>(10);
		xmppSessionListeners = new Vector<XMPPSessionListener>(10);

		smackConnectionListenerImpl = new SmackConnectionListenerImpl();
		SSLXMPPConnection.addConnectionListener((ConnectionEstablishedListener) smackConnectionListenerImpl);

		pendingXMPPUsers = new Hashtable<String, User>(10);

		smackRosterListenerImpl = new SmackRosterListenerImpl();

		smackPacketFilter = new SmackPacketFilter();
		smackPacketListenerImpl = new SmackPacketListenerImpl();

		smackPresenceFilter = new SmackPresenceFilter();
		smackPresenceListenerImpl = new SmackPresenceListenerImpl();

		documentVersionXListenerImpl = new DocumentVersionXListenerImpl();
		documentVersionXFilter = new DocumentVersionXFilter();
	}

	private void assertLoggedIn() {
		Assert.assertTrue("Cannot perform action.  User is not logged in.",
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

	private void debug(final String context, final Integer integer) {
		logger.debug(loggerFormatter.format(context, integer));
	}

	private void debug(final String context, final Loggable loggable) {
		logger.debug(loggerFormatter.format(context, loggable));
	}

	private void debug(final String context, final Packet packet) {
		logger.debug(loggerFormatter.format(context, packet));
	}

	private void debug(final String context, final RosterEntry rosterEntry) {
		logger.debug(loggerFormatter.format(context, rosterEntry));
	}

	private void debug(final String context, final String string) {
		logger.debug(loggerFormatter.format(context, string));
	}

	/**
	 * Iterate through the list of xmppExtensionListeners and fire the
	 * extensionReceived event.
	 * 
	 * @param documentVersion
	 *            <code>com.smack.extensions.DocumentVersionExtension</code>
	 */
	private void doFireDocumentVersionReceived(
			final DocumentVersion documentVersion) {
		for (Iterator<XMPPExtensionListener> i = xmppExtensionListeners
				.iterator(); i.hasNext();) {
			i.next().documentReceived(documentVersion);
		}
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
		for(Iterator<XMPPPresenceListener> i = xmppPresenceListeners.iterator();
			i.hasNext();) {
			i.next().presenceRequested(fromXMPPUser);
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
		debug("xmppsessionimpl.donotifypresencechanged:xmppAddress", xmppAddress);
		final User pendingXMPPUser = pendingXMPPUsers.remove(xmppAddress);
		if(null != pendingXMPPUser) {
			final RosterEntry rosterEntry = getRosterEntry(pendingXMPPUser);
			rosterEntry.setName(pendingXMPPUser.getName());
		}
	}

	/**
	 * Event handler for the processPacket event generated by the
	 * documentVersionXListenerImpl. This event is fired when the user
	 * receives a document version from another parity user. The parity
	 * application is notified of the new document version.
	 * 
	 * @param documentVersionExtension
	 *            <code>com.thinkparity.model.smack.extensions.DocumentVersionExtension</code>
	 */
	private void doNotifyProcessDocumentVersionX(
			final DocumentVersionX documentVersionX) {
		doFireDocumentVersionReceived(documentVersionX.getDocumentVersion());
	}

	/**
	 * Event handler for the processPacket event generated by the
	 * smackPacketListenerImpl.  This will occur for *all* packets sent to this
	 * connection.  Nothing is done except to log the packet's contents.
	 * 
	 * @param packet <code>org.jivesoftware.packet.Packet</code>
	 */
	private void doNotifyProcessPacket(final Packet packet) {
		debug("xmppsessionimpl.donotifyprocespacket:packet", packet);
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
		debug("xmppsessionimpl.donotifyprocesspresence:presence", presence);
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
		debug("xmppsessionimpl.donotifyrostermodified:entrycount", entryCount);
//		reloadRoster();
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
	 * Extract an User using the packet information.
	 * @param packet <code>org.jivesoftware.smack.packet.Packet</code>
	 * @return <code>org.kcs.projectmanager.xmpp.user.XMPPUser</code>
	 */
	private User getXMPPUser_From(final Packet packet) {
		// TODO:  Figure out a way to extract the name.
		return new User(null, packet.getFrom(), User.Presence.OFFLINE);
	}

	private void send(final User user, final PacketExtension packetExtension) {
		final Chat chat = smackXMPPConnection.createChat(user.getUsername());
		final Message message = chat.createMessage();
		message.addExtension(packetExtension);
		debug("message.toXML():", message.toXML());
		smackXMPPConnection.sendPacket(message);
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
		smackXMPPConnection.sendPacket(presence);
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
	 * Accept the presence request of user.  This will send a presence packet
	 * indicating that the user is subscribed and available.
	 * 
	 */
	public void acceptPresence(final User user) throws SmackException {
		// save the user for use by the roster event later on
		pendingXMPPUsers.put(user.getUsername(), user);
		sendPresencePacket(user.getUsername(), Type.SUBSCRIBED, Mode.AVAILABLE);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#addListener(com.thinkparity.model.xmpp.events.XMPPExtensionListener)
	 */
	public void addListener(XMPPExtensionListener xmppExtensionListener) {
		Assert.assertNotNull("Cannot register a null xmpp extension listener.",
				xmppExtensionListener);
		Assert.assertTrue(
				"Cannot register an extension listener more than once.",
				!xmppExtensionListeners.contains(xmppExtensionListener));
		xmppExtensionListeners.add(xmppExtensionListener);
	}

	public void addListener(final XMPPPresenceListener xmppPresenceListener) {
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
		assertLoggedIn();
		Assert.assertNotNull("Cannot add null user to your roster.", xmppUser);
		debug("XMPPSessionImpl$addRosterEntry():xmppUser", xmppUser);
		debug("XMPPSessionImpl$addRosterEntry():xmppUser.getUsername()",
				xmppUser.getUsername());
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

	public void debugRoster() {
		final Roster roster = getRoster();
		roster.reload();
		for(Iterator<?> i = roster.getEntries(); i.hasNext();) {
			debug("xmppsessionimpl.debugroster:rosterEntry", (RosterEntry) i
					.next());
		}
	}

	public void denyPresence(User user) throws SmackException {
		// save the user for use by the roster event later on
		pendingXMPPUsers.put(user.getUsername(), user);
		sendPresencePacket(user.getUsername(), Type.UNSUBSCRIBED);
	}

	public Collection<User> getRosterEntries() throws SmackException {
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
	 * Determine whether the parity user is logged in.
	 * @return <code>java.lang.Boolean</code>
	 */
	public Boolean isLoggedIn() {
		return (null != smackXMPPConnection
				&& smackXMPPConnection.isConnected()
				&& smackXMPPConnection.isAuthenticated());
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#login(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	public void login(final String host, final Integer port,
			final String username, final String password) throws SmackException {
		debug("xmppsessionimpl.login:host", host);
		debug("xmppsessionimpl.login:port", port);
		debug("xmppsessionimpl.login:username", username);
		debug("xmppsessionimpl.login:password", password);
		try {
			if(Boolean.TRUE == isLoggedIn())
				logout();
			smackXMPPConnection = new SSLXMPPConnection(host, port);
			smackXMPPConnection.addConnectionListener((ConnectionListener) smackConnectionListenerImpl);

			smackXMPPConnection.addPacketListener(documentVersionXListenerImpl, documentVersionXFilter);
			smackXMPPConnection.addPacketListener(smackPacketListenerImpl, smackPacketFilter);
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
		smackXMPPConnection.close();
		smackXMPPConnection = null;
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#removeListener(com.thinkparity.model.xmpp.events.XMPPExtensionListener)
	 */
	public void removeListener(XMPPExtensionListener xmppExtensionListener) {
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
		Assert.assertNotNull("Cannot un-register a null session listener.",
				xmppSessionListener);
		Assert.assertTrue(
				"Cannot unregister a non-registered session listener.",
				xmppSessionListeners.contains(xmppSessionListener));
		xmppSessionListeners.remove(xmppSessionListener);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#send(com.thinkparity.model.xmpp.user.User, com.thinkparity.model.parity.api.ParityObjectVersion)
	 */
	public void send(User user, ParityObjectVersion parityObjectVersion)
			throws SmackException {
		try { send(user, XFactory.createPacketX(parityObjectVersion)); }
		catch(UnsupportedXTypeException uxtx) {
			throw error("Could not send packet extension.", uxtx);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#updateRosterEntry(com.thinkparity.model.xmpp.user.User)
	 */
	public void updateRosterEntry(User user) {
		final Roster roster = getRoster();
		final RosterEntry rosterEntry = roster.getEntry(user.getUsername());
		rosterEntry.setName(user.getName());
	}
}
