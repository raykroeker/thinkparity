/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.util;

import java.awt.Color;
import java.util.Collection;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.thinkparity.browser.util.log4j.LoggerFactory;

import com.thinkparity.model.parity.api.ParityObjectFlag;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class RandomData {

	private static final String[] ACTION_DATA;

	private static final String[] ACTION_DATE_DATA;

	private static final String[] ACTION_USER_DATA;

	private static final ParityObjectFlag[] ARTIFACT_FLAG_DATA;

	private static final String[] ARTIFACT_KEY_HOLDER_DATA;

	private static final Color[] COLOR_DATA;

	private static final String[] DATE_DATA;

	private static final Integer[] SYSTEM_MESSAGE_COUNT_DATA;

	private static final String[] SYSTEM_MESSAGE_DATA;

	private static final String[] SYSTEM_MESSAGE_HEADER_DATA;

	private static final String[] USER_DATA;

	static {
		ACTION_DATA = new String[] {"Created by ", "Sent to ", "Received from "};

		ACTION_DATE_DATA =
			new String[] {" today", " yesterday", " last friday", " on Tuesday Dec 2, 2005"};

		ACTION_USER_DATA =
			new String[] {"John Wayne", "Omid Ejtemai", "Raymond Kroeker", "Alan Turing"};

		ARTIFACT_FLAG_DATA =
			new ParityObjectFlag[] {ParityObjectFlag.CLOSED, ParityObjectFlag.SEEN, ParityObjectFlag.KEY};

		ARTIFACT_KEY_HOLDER_DATA =
			new String[] {"John Wayne", "Omid Ejtemai", "Raymond Kroeker", "Alan Turing"};

		COLOR_DATA =
			new Color[] {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW};

		DATE_DATA =
			new String[] {"Today", "Yesterday", "Last Monday", "Last Tuesday", "Last Wednesday", "Last Thursday", "Last Friday"};

		SYSTEM_MESSAGE_COUNT_DATA =
			new Integer[] {0, 1, 2, 3, 4, 5, 2, 2, 2, 3, 4, 5, 1, 3, 0};

		SYSTEM_MESSAGE_DATA =
			new String[] {"John Wayne:  Has invited you to participate on their team.",
				"Omid Ejtemai:  Has invited you to participate on their team.",
				"Raymond Kroeker:  Has invited you to participate on their team.",
				"Alan Turing:  Has invited you to participate on their team.",
				"Martin Fowler:  Has invited you to participate on their team."};

		SYSTEM_MESSAGE_HEADER_DATA =
			new String[] {"John Wayne:  Has sent you an invitation.",
				"Omid Ejtemai:  Has sent you an invitation.",
				"Raymond Kroeker:  Has sent you an invitation.",
				"Alan Turing:  sent you an invitation.",
				"Martin Fowler:  sent you an invitation."};

		USER_DATA =
			new String[] {"John Wayne", "Omid Ejtemai", "Raymond Kroeker", "Alan Turing"};
	}

	/**
	 * Apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * Randomizer.
	 * 
	 */
	private final Random random;

	/**
	 * Create a RandomData.
	 * 
	 */
	public RandomData() {
		super();
		this.logger = LoggerFactory.getLogger(getClass());
		this.random = new Random();
	}

	/**
	 * Obtain a random action.
	 * 
	 * @return A random action.
	 */
	public String getAction() { return getStringData(ACTION_DATA); }

	/**
	 * Obtain a random action date.
	 * 
	 * @return A random action date.
	 */
	public String getActionDate() { return getStringData(ACTION_DATE_DATA); }

	/**
	 * Obtain a random action username.
	 * 
	 * @return A random action username.
	 */
	public String getActionUser() { return getStringData(ACTION_USER_DATA); }

	/**
	 * Obtain a random artifact flag.
	 * 
	 * @return A random artifact flag.
	 */
	public ParityObjectFlag getArtifactFlag() {
		return (ParityObjectFlag) getData(ARTIFACT_FLAG_DATA);
	}

	/**
	 * Obtain a random artifact key holder.
	 * 
	 * @return A random artifact key holder.
	 */
	public String getArtifactKeyHolder() {
		return getStringData(ARTIFACT_KEY_HOLDER_DATA);
	}

	/**
	 * Obtain a random color.
	 * 
	 * @return A random color.
	 */
	public Color getColor() { return (Color) getData(COLOR_DATA); }

	/**
	 * Obtain a random date.
	 * 
	 * @return A random date.
	 */
	public String getDate() { return getStringData(DATE_DATA); }
	
	/**
	 * Obtain a random system message.
	 * 
	 * @return A random system message.
	 */
	public String getSystemMessage() { return getStringData(SYSTEM_MESSAGE_DATA); }

	/**
	 * Obtain a random system message header.
	 * 
	 * @return A random system message header.
	 */
	public String getSystemMessageHeader() {
		return getStringData(SYSTEM_MESSAGE_HEADER_DATA);
	}

	/**
	 * Obtain random system message headers.
	 * 
	 * @return Random system message headers.
	 */
	public String[] getSystemMessageHeaders() {
		final Integer messageCount = getSystemMessageCount();
		final Collection<String> messages = new Vector<String>(messageCount);
		for(int i = 0; i < messageCount; i++) {
			messages.add(getSystemMessageHeader());
		}
		return messages.toArray(new String[] {});
	}

	/**
	 * Obtain a random username.
	 * 
	 * @return A random username.
	 */
	public String getUser() { return getStringData(USER_DATA); }

	/**
	 * Obtain random object data from an input set.
	 * 
	 * @param data
	 *            The random data set.
	 * @return A random object.
	 */
	private Object getData(final Object[] data) {
		logger.warn("Random data.");
		return data[random.nextInt(data.length)];
	}

	/**
	 * Obtain random string data from an input set.
	 * 
	 * @param data
	 *            The random data set.
	 * @return A random string.
	 */
	private String getStringData(final String[] data) {
		return (String) getData(data);
	}

	/**
	 * Obtain the number of system messages to retreive.
	 * 
	 * @return The number of system messages to retreive.
	 */
	private Integer getSystemMessageCount() {
		return (Integer) getData(SYSTEM_MESSAGE_COUNT_DATA);
	}
}
