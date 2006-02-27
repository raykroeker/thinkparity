/*
 * Jan 31, 2006
 */
package com.thinkparity.model.parity.model.message.system;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.SystemMessageEvent;
import com.thinkparity.model.parity.api.events.SystemMessageListener;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.SystemMessageIOHandler;
import com.thinkparity.model.parity.model.sort.ComparatorBuilder;
import com.thinkparity.model.parity.model.sort.ModelSorter;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;

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

	void createKeyRequest(final Long artifactId, final JabberId requestedBy) {
		logger.info("createKeyRequest(Long,User)");
		logger.debug(artifactId);
		logger.debug(requestedBy);
		final KeyRequestMessage keyRequestMessage = new KeyRequestMessage();
		keyRequestMessage.setArtifactId(artifactId);
		keyRequestMessage.setRequestedBy(requestedBy);
		keyRequestMessage.setType(SystemMessageType.KEY_REQUEST);
		systemMessageIO.create(keyRequestMessage);
		notify_MessageCreated(keyRequestMessage);
	}

	void createKeyResponse(final Long artifactId,
			final Boolean didAcceptRequest, final JabberId responseFrom) {
		logger.info("createKeyResponse(Long,User)");
		logger.debug(artifactId);
		logger.debug(responseFrom);
		final KeyResponseMessage keyResponseMessage = new KeyResponseMessage();
		keyResponseMessage.setDidAcceptRequest(didAcceptRequest);
		keyResponseMessage.setResponseFrom(responseFrom);
		keyResponseMessage.setType(SystemMessageType.KEY_RESPONSE);
		systemMessageIO.create(keyResponseMessage);
		notify_MessageCreated(keyResponseMessage);
	}

	void createPresenceRequest(final JabberId requestedBy) {
		logger.info("createPresenceRequest(User)");
		logger.debug(requestedBy);
		// check to see if a similar presence request for the user already exists
		// if it does we don't want to add another one
		PresenceRequestMessage message = 
			systemMessageIO.readPresenceRequest(requestedBy);
		if(null == message) {
			message = new PresenceRequestMessage();
			message.setRequestedBy(requestedBy);
			message.setType(SystemMessageType.PRESENCE_REQUEST);
			systemMessageIO.create(message);
			notify_MessageCreated(message);
		}
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
			ModelSorter.sortSystemMessages(messages, comparator);
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
	SystemMessage read(final Long systemMessageId) throws ParityException {
		logger.info("read(Long)");
		logger.debug(systemMessageId);
		try { return null; }
		catch(final RuntimeException rx) {
			logger.error("Could not obtain system message:  " + systemMessageId, rx);
			throw ParityErrorTranslator.translate(rx);
		}
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
}
