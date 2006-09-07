/*
 * Jan 31, 2006
 */
package com.thinkparity.model.parity.model.message.system;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.SystemMessageEvent;
import com.thinkparity.model.parity.api.events.SystemMessageListener;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.SystemMessageIOHandler;
import com.thinkparity.model.parity.model.sort.ComparatorBuilder;
import com.thinkparity.model.parity.model.sort.ModelSorter;
import com.thinkparity.model.parity.model.user.InternalUserModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class SystemMessageModelImpl extends AbstractModelImpl {

	/**
	 * System message listeners.
	 * 
	 */
	private static final Collection<SystemMessageListener> systemMessageListeners;

	/**
	 * Synchronization lock for the system message listeners.
	 * 
	 */
	private static final Object systemMessageListenersLock;

	static {
		systemMessageListeners = new Vector<SystemMessageListener>(7);
		systemMessageListenersLock = new Object();
	}

	/**
	 * Default system message comparator.
	 * 
	 */
	private final Comparator<SystemMessage> defaultComparator;

	/**
	 * System message io.
	 * 
	 */
	private final SystemMessageIOHandler systemMessageIO;

	/**
	 * Create a SystemMessageModelImpl.
	 */
	SystemMessageModelImpl(final Workspace workspace) {
		super(workspace);
		this.defaultComparator = new ComparatorBuilder().createSystemMessageDefault();
		this.systemMessageIO = IOFactory.getDefault().createSystemMessageHandler();
	}

	/**
	 * Add a system message listener.
	 * 
	 * @param listener
	 *            The system message listener.
	 */
	void addListener(final SystemMessageListener listener) {
		logger.info("addListener(SystemMessageListener)");
		logger.debug(listener);
		Assert.assertNotNull(
				"Cannot add a null system message listener.", listener);
		synchronized(SystemMessageModelImpl.systemMessageListenersLock) {
			Assert.assertNotTrue(
					"Cannot add the same system message listener twice.",
					SystemMessageModelImpl.systemMessageListeners.contains(listener));
			SystemMessageModelImpl.systemMessageListeners.add(listener);
		}
	}

	void createContactInvitation(final JabberId invitedBy) {
		logger.info("createPresenceRequest(JabberId)");
		logger.debug(invitedBy);
		// check to see if a similar invitation for the user already exists
		// if it does we don't want to add another one
		ContactInvitationMessage message = 
			systemMessageIO.readContactInvitation(invitedBy);
		if(null == message) {
			message = new ContactInvitationMessage();
			message.setInvitedBy(invitedBy);
			message.setType(SystemMessageType.CONTACT_INVITATION);
			systemMessageIO.create(message);
			notify_MessageCreated(message);
		}
	}

	void createContactInvitationResponse(final JabberId responseBy,
			final Boolean didAcceptInvitation) {
		logger.info("createContactInvitationResponse(JabberId,Boolean)");
		logger.debug(responseBy);
		logger.debug(didAcceptInvitation);
		// check to see if a similar invitation for the user already exists
		// if it does we don't want to add another one
		ContactInvitationResponseMessage message = 
			systemMessageIO.readContactInvitationResponse(responseBy);
		if(null == message) {
			message = new ContactInvitationResponseMessage();
			message.setDidAcceptInvitation(didAcceptInvitation);
			message.setResponseFrom(responseBy);
			message.setType(SystemMessageType.CONTACT_INVITATION_RESPONSE);
			systemMessageIO.create(message);
			notify_MessageCreated(message);
		}
	}

	SystemMessage createKeyRequest(final Long artifactId, final JabberId requestedBy) {
		logger.info("createKeyRequest(Long,User)");
		logger.debug(artifactId);
		logger.debug(requestedBy);
		final KeyRequestMessage keyRequestMessage = new KeyRequestMessage();
		keyRequestMessage.setArtifactId(artifactId);
		keyRequestMessage.setRequestedBy(requestedBy);
		keyRequestMessage.setType(SystemMessageType.KEY_REQUEST);
		systemMessageIO.create(keyRequestMessage);
		notify_MessageCreated(keyRequestMessage);
        return keyRequestMessage;
	}

	void createKeyResponse(final Long artifactId,
			final Boolean didAcceptRequest, final JabberId responseFrom) {
		logger.info("createKeyResponse(Long,User)");
		logger.debug(artifactId);
		logger.debug(responseFrom);
		final KeyResponseMessage keyResponseMessage = new KeyResponseMessage();
		keyResponseMessage.setArtifactId(artifactId);
		keyResponseMessage.setDidAcceptRequest(didAcceptRequest);
		keyResponseMessage.setResponseFrom(responseFrom);
		keyResponseMessage.setType(SystemMessageType.KEY_RESPONSE);
		systemMessageIO.create(keyResponseMessage);
		notify_MessageCreated(keyResponseMessage);
	}

	/**
	 * Delete a system message.
	 * 
	 * @param systemMessageId
	 *            The system message id.
	 */
	void delete(final Long systemMessageId) {
		logger.info("delete(Long)");
		logger.debug(systemMessageId);
		systemMessageIO.delete(systemMessageId);
	}

	/**
	 * Obtain a list of system messages.
	 * 
	 * @return A list of system messages.
	 * @throws ParityException
	 */
	Collection<SystemMessage> read() throws ParityException {
		return read(defaultComparator);
	}

	/**
	 * Obtain a list of system messages ordered according to the comparator.
	 * 
	 * @param comparator
	 *            The system message comparator.
	 * @return A list of system messages.
	 * @throws ParityException
	 */
	Collection<SystemMessage> read(final Comparator<SystemMessage> comparator)
			throws ParityException {
		logger.info("list(Comparator<SystemMessage>)");
		logger.debug(comparator);
		try {
			final List<SystemMessage> messages = systemMessageIO.read();
			ModelSorter.sortMessages(messages, comparator);
			return messages;
		}
		catch(final RuntimeException rx) {
			logger.error("list(Comparator<SystemMessage>)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain a system message.
	 * 
	 * @param systemMessageId
	 *            The system message id.
	 * @return The system message.
	 * @throws ParityException
	 */
	SystemMessage read(final Long systemMessageId) {
		logger.info("read(Long)");
		logger.debug(systemMessageId);
		return systemMessageIO.read(systemMessageId);
	}

	List<SystemMessage> readForArtifact(final Long artifactId,
            final SystemMessageType type) {
		logger.info("[LMODEL] [SYSTEM MESSAGE] [READ FOR ARTIFACT]");
		logger.debug(artifactId);
		logger.debug(type);
		final List<SystemMessage> messages =
                systemMessageIO.readForArtifact(artifactId, type);
		populateUserInfo(messages);
		return messages;
	}

	List<SystemMessage> readForNonArtifacts() throws ParityException {
		logger.info("[LMODEL] [SYSTEM MESSAGE] [READ FOR NON ARTIFACTS]");
		final List<SystemMessage> filtered = new LinkedList<SystemMessage>();
		final Collection<SystemMessage> messages = read();
		for(final SystemMessage message : messages) {
			if(!isForArtifacts(message)) { filtered.add(message); }
		}
		return filtered;
	}

	/**
	 * Remove a system message listener.
	 * 
	 * @param listener
	 *            The system message listener to remove.
	 */
	void removeListener(final SystemMessageListener listener) {
		logger.info("removeListener(SystemMessageListener)");
		logger.debug(listener);
		Assert.assertNotNull(
				"Cannot remove a null system message listener.", listener);
		synchronized(SystemMessageModelImpl.systemMessageListenersLock) {
			Assert.assertTrue(
					"Cannot remove a system message listener twice.",
					SystemMessageModelImpl.systemMessageListeners.contains(listener));
			SystemMessageModelImpl.systemMessageListeners.remove(listener);
		}
	}

	/**
	 * Determine whether the system message is for an artifact.
	 * 
	 * @param message
	 *            The system message.
	 * @return True if the message is for an artifact; false otherwise.
	 */
	private Boolean isForArtifacts(final SystemMessage message) {
		switch(message.getType()) {
		case INFO:
		case CONTACT_INVITATION:
		case CONTACT_INVITATION_RESPONSE:
			return Boolean.FALSE;
		case KEY_REQUEST:
		case KEY_RESPONSE:
			return Boolean.TRUE;
		default:
			throw Assert.createUnreachable("Unknown message type:  " + message.getType());
		}
	}

	/**
	 * Fire a message created notification event.
	 *
	 */
	private void notify_MessageCreated(final SystemMessage systemMessage) {
		synchronized(systemMessageListenersLock) {
			for(final SystemMessageListener l : systemMessageListeners) {
				l.systemMessageCreated(new SystemMessageEvent(systemMessage));
			}
		}
	}

	private void populateUserInfo(final List<SystemMessage> messages) {
		final InternalUserModel iUModel = getInternalUserModel();
		for(final SystemMessage message : messages) {
			switch(message.getType()) {
			case INFO:
			case CONTACT_INVITATION:
			case CONTACT_INVITATION_RESPONSE:
			case KEY_RESPONSE:
				break;
			case KEY_REQUEST:
				final User user = iUModel.read(((KeyRequestMessage) message).getRequestedBy());
				final Object[] arguments = new Object[] {
					user.getName(), user.getOrganization() };
				((KeyRequestMessage) message).setRequestedByName(getString("KeyRequestMessage.RequestedByName", arguments));
				break;
			default:
				Assert.assertUnreachable("Unknown message type:  " + message.getType());
			}
		}
	}
}
