/*
 * Jan 31, 2006
 */
package com.thinkparity.model.parity.model.message.system;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.SystemMessageListener;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.artifact.ComparatorBuilder;
import com.thinkparity.model.parity.model.workspace.Workspace;

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
	 * Create a SystemMessageModelImpl.
	 */
	SystemMessageModelImpl(final Workspace workspace) {
		super(workspace);
		this.defaultComparator = new ComparatorBuilder().createSystemMessageDefault();
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

	/**
	 * Obtain a system message.
	 * 
	 * @param systemMessageId
	 *            The system message id.
	 * @return The system message.
	 * @throws ParityException
	 */
	SystemMessage get(final SystemMessageId systemMessageId)
			throws ParityException {
		logger.info("get(SystemMessageId)");
		logger.debug(systemMessageId);
		try {
			if(false) throw new IOException();
			return new SystemMessage();
		}
		catch(IOException iox) {
			logger.error("get(SystemMessageId)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("get(SystemMessageId)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain a list of system messages.
	 * 
	 * @return A list of system messages.
	 * @throws ParityException
	 */
	Collection<SystemMessage> list() throws ParityException {
		return list(defaultComparator);
	}

	/**
	 * Obtain a list of system messages ordered according to the comparator.
	 * 
	 * @param comparator
	 *            The system message comparator.
	 * @return A list of system messages.
	 * @throws ParityException
	 */
	Collection<SystemMessage> list(final Comparator<SystemMessage> comparator)
			throws ParityException {
		logger.info("list(Comparator<SystemMessage>)");
		logger.debug(comparator);
		try {
			if(false) throw new IOException();
			return new Vector<SystemMessage>(0);
		}
		catch(IOException iox) {
			logger.error("list(Comparator<SystemMessage>)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("list(Comparator<SystemMessage>)", rx);
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
}
