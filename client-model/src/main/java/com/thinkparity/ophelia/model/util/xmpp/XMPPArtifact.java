/*
 * Mar 1, 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.ProviderManager;

import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;


import com.thinkparity.ophelia.model.Constants.Xml;
import com.thinkparity.ophelia.model.Constants.Xml.EventHandler;
import com.thinkparity.ophelia.model.Constants.Xml.Service;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.smack.SmackException;
import com.thinkparity.ophelia.model.util.smackx.packet.AbstractThinkParityIQ;
import com.thinkparity.ophelia.model.util.smackx.packet.AbstractThinkParityIQProvider;
import com.thinkparity.ophelia.model.util.smackx.packet.artifact.IQConfirmArtifactReceipt;
import com.thinkparity.ophelia.model.util.smackx.packet.artifact.IQConfirmReceiptProvider;
import com.thinkparity.ophelia.model.util.xmpp.events.XMPPArtifactListener;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class XMPPArtifact extends AbstractXMPP {

    private static final List<XMPPArtifactListener> LISTENERS;

    static {
		LISTENERS = new LinkedList<XMPPArtifactListener>();

        ProviderManager.addIQProvider(Service.NAME, EventHandler.Artifact.DRAFT_CREATED, new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser2(parser);
                final HandleDraftCreatedIQ query = new HandleDraftCreatedIQ();
                Boolean isComplete = Boolean.FALSE;
                while (Boolean.FALSE == isComplete) {
                    if (isStartTag("uniqueId")) {
                        query.uniqueId = readUniqueId2();
                    } else if (isStartTag("createdBy")) {
                        query.createdBy = readJabberId2();
                    } else if (isStartTag("createdOn")) {
                        query.createdOn = readCalendar2();
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
        ProviderManager.addIQProvider(Service.NAME, EventHandler.Artifact.DRAFT_DELETED, new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser2(parser);
                final HandleDraftDeletedIQ query = new HandleDraftDeletedIQ();
                Boolean isComplete = Boolean.FALSE;
                while (Boolean.FALSE == isComplete) {
                    if (isStartTag("uniqueId")) {
                        query.uniqueId = readUniqueId2();
                    } else if (isStartTag("deletedBy")) {
                        query.deletedBy = readJabberId2();
                    } else if (isStartTag("deletedOn")) {
                        query.deletedOn = readCalendar2();
                    } else {
                        isComplete = Boolean.TRUE;
                    }
                }
                return query;
            }
        });
        ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:artifactconfirmreceipt", new IQConfirmReceiptProvider());
        ProviderManager.addIQProvider(Service.NAME, Xml.EventHandler.Artifact.TEAM_MEMBER_ADDED, new IQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                final HandleTeamMemberAddedIQ query = new HandleTeamMemberAddedIQ();

                Integer eventType;
                String name;
                Boolean isComplete = Boolean.FALSE;
                while(Boolean.FALSE == isComplete) {
                    eventType = parser.next();
                    name = parser.getName();

                    if(XmlPullParser.START_TAG == eventType && Xml.Artifact.UNIQUE_ID.equals(name)) {
                        parser.next();
                        query.uniqueId = UUID.fromString(parser.getText());
                        parser.next();
                    }
                    else if(XmlPullParser.END_TAG == eventType && Xml.Artifact.UNIQUE_ID.equals(name)) {
                        parser.next();
                    }
                    else if(XmlPullParser.START_TAG == eventType && Xml.User.JABBER_ID.equals(name)) {
                        parser.next();
                        query.jabberId = JabberIdBuilder.parseQualifiedJabberId(parser.getText());
                        parser.next();
                    }
                    else if(XmlPullParser.END_TAG == eventType && Xml.User.JABBER_ID.equals(name)) {
                        parser.next();
                    }
                    else { isComplete = Boolean.TRUE; }
                }
                return query;
            }
        });
        ProviderManager.addIQProvider(Service.NAME, Xml.EventHandler.Artifact.TEAM_MEMBER_REMOVED, new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser(parser);
                final HandleTeamMemberRemovedIQ query = new HandleTeamMemberRemovedIQ();

                Boolean isComplete = Boolean.FALSE;
                while(Boolean.FALSE == isComplete) {
                    next(1);
                    if (isStartTag(Xml.Artifact.UNIQUE_ID)) {
                        next(1);
                        query.uniqueId = readUniqueId();
                        next(1);
                    } else if (isEndTag(Xml.Artifact.UNIQUE_ID)) {
                        next(1);
                    } else if (isStartTag(Xml.User.JABBER_ID)) {
                        next(1);
                        query.jabberId = readJabberId();
                        next(1);
                    } else if (isEndTag(Xml.User.JABBER_ID)) {
                        next(1);
                    }
                    else { isComplete = Boolean.TRUE; }
                }
                return query;
            }
        });
	}

	/**
	 * Create a XMPPArtifact.
	 * 
	 * @param xmppCore
	 *            The xmpp core functionality.
	 */
	XMPPArtifact(final XMPPCore xmppCore) {
		super(xmppCore);
	}

	void addListener(final XMPPArtifactListener l) {
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
	 * Add the packet listeners to the connection.
	 * 
	 * @param xmppConnection
	 *            The xmpp connection.
	 */
	void addPacketListeners(final XMPPConnection xmppConnection) {
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        handleTeamMemberAdded((HandleTeamMemberAddedIQ) packet);
                    }
                },
                new PacketTypeFilter(HandleTeamMemberAddedIQ.class));
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        handleDraftCreated((HandleDraftCreatedIQ) packet);
                    }
                },
                new PacketTypeFilter(HandleDraftCreatedIQ.class));
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        handleDraftDeleted((HandleDraftDeletedIQ) packet);
                    }
                },
                new PacketTypeFilter(HandleDraftDeletedIQ.class));
        xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        handleTeamMemberRemoved((HandleTeamMemberRemovedIQ) packet);
                    }
                },
                new PacketTypeFilter(HandleTeamMemberRemovedIQ.class));
		xmppConnection.addPacketListener(
                new PacketListener() {
                    public void processPacket(final Packet packet) {
                        notifyArtifactConfirmation((IQConfirmArtifactReceipt) packet);
                    }
                },
                new PacketTypeFilter(IQConfirmArtifactReceipt.class));
	}

	/**
     * Add a team member. This will create the team member relationship in the
     * distributed network with a pending state.
     * 
     * @param artifactId
     *            An artifact id.
     * @param jabberId
     *            A jabber id.
     * @throws SmackException
     */
    void addTeamMember(final UUID uniqueId, final JabberId jabberId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("jabberId", jabberId);
        final XMPPMethod method = new XMPPMethod(Xml.Method.Artifact.ADD_TEAM_MEMBER);
        method.setParameter(Xml.Artifact.UNIQUE_ID, uniqueId);
        method.setParameter(Xml.User.JABBER_ID, jabberId);
        method.execute(xmppCore.getConnection());
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
	    logApiId();
        logVariable("receivedFrom", receivedFrom);
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        final IQConfirmArtifactReceipt iq = new IQConfirmArtifactReceipt();
        iq.setArtifactUUID(uniqueId);
        iq.setArtifactVersionId(versionId);
        iq.setRecievedFrom(receivedFrom);
        iq.setType(IQ.Type.SET);
        xmppCore.sendAndConfirmPacket(iq);
	}

	/**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    void createDraft(final UUID uniqueId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        final XMPPMethod method = new XMPPMethod("artifact:createdraft");
        method.setParameter("uniqueId", uniqueId);
        method.execute(xmppCore.getConnection());
    }

	/**
     * Delete an artifact.
     * @param uniqueId An artifact unique id.
     */
    void delete(final UUID uniqueId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        final XMPPMethod delete = new XMPPMethod(Xml.Method.Artifact.DELETE);
        delete.setParameter(Xml.Artifact.UNIQUE_ID, uniqueId);
        delete.execute(xmppCore.getConnection());
    }

    void deleteDraft(final UUID uniqueId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        final XMPPMethod deleteDraft = new XMPPMethod("artifact:deletedraft");
        deleteDraft.setParameter("uniqueId", uniqueId);
        deleteDraft.execute(xmppCore.getConnection());
    }

	/**
     * Read the artifact key holder.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return A jabber id.
     */
    JabberId readKeyHolder(final JabberId userId, final UUID uniqueId) {
        final XMPPMethod method = new XMPPMethod("artifact:readkeyholder");
        method.setParameter("userId", userId);
        method.setParameter("uniqueId", uniqueId);
        final XMPPMethodResponse result = method.execute(xmppCore.getConnection());
        return result.readResultJabberId("keyHolder");
    }

    /**
     * Read the artifact team member ids.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
	List<JabberId> readTeamIds(final UUID uniqueId) {
		logApiId();
		logVariable("uniqueId", uniqueId);
        final XMPPMethod readTeam = new XMPPMethod("artifact:readteamids");
        readTeam.setParameter("uniqueId", uniqueId);
        final XMPPMethodResponse response = execute(readTeam, Boolean.TRUE);
        return response.readResultJabberIds("teamIds");
	}

	void removeListener(final XMPPArtifactListener l) {
		logApiId();
		logVariable("l", l);
		synchronized (LISTENERS) {
			if (!LISTENERS.contains(l)) {
                return;
			} else {
			    LISTENERS.remove(l);
            }
		}
	}

	/**
     * Remove a team member from the artifact team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
    void removeTeamMember(final UUID uniqueId, final JabberId jabberId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("jabberId", jabberId);
        final XMPPMethod method = new XMPPMethod("artifact:removeteammember");
        method.setParameter(Xml.Artifact.UNIQUE_ID, uniqueId);
        method.setParameter(Xml.User.JABBER_ID, jabberId);
        method.execute(xmppCore.getConnection());
    }

    private void handleDraftCreated(final HandleDraftCreatedIQ query) {
        synchronized (LISTENERS) {
            for (final XMPPArtifactListener l : LISTENERS) {
                l.handleDraftCreated(query.uniqueId, query.createdBy,
                        query.createdOn);
            }
        }
    }

    private void handleDraftDeleted(final HandleDraftDeletedIQ query) {
        synchronized (LISTENERS) {
            for (final XMPPArtifactListener l : LISTENERS) {
                l.handleDraftDeleted(query.uniqueId, query.deletedBy,
                        query.deletedOn);
            }
        }
    }

    /**
     * Receive a notification re team member addition
     * 
     * @param query
     *            The internet query.
     */
	private void handleTeamMemberAdded(final HandleTeamMemberAddedIQ query) {
		synchronized(LISTENERS) {
			for(final XMPPArtifactListener l : LISTENERS) {
				l.teamMemberAdded(query.uniqueId, query.jabberId);
			}
		}
	}

    /**
     * Receive a notification regarding the removal of a team member.
     * 
     * @param query
     *            The internet query.
     */
    private void handleTeamMemberRemoved(final HandleTeamMemberRemovedIQ query) {
        synchronized (LISTENERS) {
            for(final XMPPArtifactListener l : LISTENERS) {
                l.teamMemberRemoved(query.uniqueId, query.jabberId);
            }
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
        synchronized(LISTENERS) {
            for(final XMPPArtifactListener l : LISTENERS) {
                l.confirmReceipt(packet.getArtifactUUID(),
                        packet.getArtifactVersionId(), packet.getFromJabberId());
            }
        }
    }

    /**
     * <b>Title:</b>thinkparity XMPP Artifact Handle Draft Created Query<br>
     * <b>Description:</b>Provides a wrapper for the team member removed remote
     * event data.
     */
    private static class HandleDraftCreatedIQ extends AbstractThinkParityIQ {

        /** The creator. */
        private JabberId createdBy;

        /** The creation date. */
        private Calendar createdOn;

        /** The artifact unique id. */
        private UUID uniqueId;

        /** Create HandleDraftCreatedIQ. */
        private HandleDraftCreatedIQ() { super(); }
    }

    /**
     * <b>Title:</b>thinkparity XMPP Artifact Handle Draft Deleted Query<br>
     * <b>Description:</b>Provides a wrapper for the draft deleted remote
     * event data.
     */
    private static class HandleDraftDeletedIQ extends AbstractThinkParityIQ {

        /** The creator. */
        private JabberId deletedBy;

        /** The creation date. */
        private Calendar deletedOn;

        /** The artifact unique id. */
        private UUID uniqueId;

        /** Create HandleDraftDeletedIQ. */
        private HandleDraftDeletedIQ() { super(); }

    }

    /**
     * <b>Title:</b>thinkparity XMPP Artifact Handle Team Member Added Query<br>
     * <b>Description:</b>Provides a wrapper for the team member added remote
     * event data.
     */
    private static class HandleTeamMemberAddedIQ extends AbstractThinkParityIQ {
        /** The team member jabber id. */
        private JabberId jabberId;

        /** The artifact unique id. */
        private UUID uniqueId;
    }
    
    /**
     * <b>Title:</b>thinkparity XMPP Artifact Handle Team Member Removed Query<br>
     * <b>Description:</b>Provides a wrapper for the team member removed remote
     * event data.
     */
    private static class HandleTeamMemberRemovedIQ extends AbstractThinkParityIQ {
        /** The team member jabber id. */
        private JabberId jabberId;

        /** The artifact unique id. */
        private UUID uniqueId;
    }
}
