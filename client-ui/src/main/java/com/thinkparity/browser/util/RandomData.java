/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.util;

import java.awt.Color;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.thinkparity.browser.model.tmp.system.message.Message;
import com.thinkparity.browser.model.tmp.system.message.Sender;
import com.thinkparity.browser.util.log4j.LoggerFactory;

import com.thinkparity.model.parity.api.ParityObjectFlag;
import com.thinkparity.model.parity.api.ParityObjectType;
import com.thinkparity.model.xmpp.user.User;
import com.thinkparity.model.xmpp.user.User.Presence;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class RandomData {

	private static final String[] ACTION_DATA;

	private static final String[] ACTION_DATE_DATA;

	private static final ParityObjectFlag[] ARTIFACT_FLAG_DATA;

	private static final Boolean[] ARTIFACT_HAS_BEEN_SEEN;

	private static final String[] ARTIFACT_KEY_HOLDER_DATA;

	private static final String[] ARTIFACT_NAME_DATA;

	private static final ParityObjectType[] ARTIFACT_TYPE_DATA;

	private static final Color[] COLOR_DATA;

	private static final String[] DATE_DATA;

	private static final String[] SYSTEM_MESSAGE_BODY_DATA;
	
	private static final Integer[] SYSTEM_MESSAGE_COUNT_DATA;
	
	private static final String[] SYSTEM_MESSAGE_HEADER_DATA;

	private static final User[] USER_DATA;

	static {
		ACTION_DATA = new String[] {"Created by ", "Sent to ", "Received from "};

		ACTION_DATE_DATA =
			new String[] {" today", " yesterday", " last friday", " on Tuesday Dec 2, 2005"};

		ARTIFACT_FLAG_DATA =
			new ParityObjectFlag[] {ParityObjectFlag.CLOSED, ParityObjectFlag.SEEN, ParityObjectFlag.KEY};

		ARTIFACT_KEY_HOLDER_DATA =
			new String[] {"John Wayne", "Omid Ejtemai", "Raymond Kroeker", "Alan Turing"};

		ARTIFACT_HAS_BEEN_SEEN = new Boolean[] {
				Boolean.FALSE,
				Boolean.FALSE,
				Boolean.FALSE,
				Boolean.TRUE,
				Boolean.TRUE,
				Boolean.FALSE,
				Boolean.TRUE,
				Boolean.FALSE,
				Boolean.TRUE,
				Boolean.TRUE,
				Boolean.FALSE,
				Boolean.FALSE,
				Boolean.FALSE,
				Boolean.FALSE};

		ARTIFACT_NAME_DATA = new String[] {
				"Business Plan.doc",
				"Corporate Structure.doc",
				"Feasibility Analysis.doc",
				"1995 Balance Sheet.xls",
				"1995 Income Statement.xls",
				"1995 Jan - Dec Cash Flow.xls",
				"1996, 1995, 1994 Financial History.xls",
				"Asset List.doc",
				"2004 Balance Sheet.xls",
				"2004 Income Statement.xls",
				"2004 Jan - Dec Cash Flow.xls",
				"2005, 2004, 2003 Financial History.xls",
				"Corporate Structure.doc",
				"Target Share Vision Plan.doc"};

		ARTIFACT_TYPE_DATA = new ParityObjectType[] {
				ParityObjectType.DOCUMENT,
				ParityObjectType.DOCUMENT,
				ParityObjectType.DOCUMENT,
				ParityObjectType.DOCUMENT,
				ParityObjectType.DOCUMENT,
				ParityObjectType.DOCUMENT,
				ParityObjectType.DOCUMENT,
				ParityObjectType.DOCUMENT,
				ParityObjectType.DOCUMENT,
				ParityObjectType.DOCUMENT,
				ParityObjectType.DOCUMENT,
				ParityObjectType.DOCUMENT,
				ParityObjectType.DOCUMENT,
				ParityObjectType.DOCUMENT};

		COLOR_DATA =
			new Color[] {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW};

		DATE_DATA =
			new String[] {"Today", "Yesterday", "Last Monday", "Last Tuesday", "Last Wednesday", "Last Thursday", "Last Friday"};

		SYSTEM_MESSAGE_COUNT_DATA =
			new Integer[] {0, 1, 2, 3, 4, 5, 2, 2, 2, 3, 4, 5, 1, 3, 0};

		SYSTEM_MESSAGE_HEADER_DATA = new String[] {
				"Invitation request",
				"Invitation request declined",
				"Invitation request accepted",
				"Ownership request:  {0}",				// {0} = artifact name
				"Ownership request declined:  {0}",		// {0} = artifact name
				"Ownership request accepted:  {0}"};	// {0} = artifact name

		SYSTEM_MESSAGE_BODY_DATA = new String[] {
				"{0} has invited you to join their team.",				// {0} = user name
				"{0} has declined your invitation to join their team.",	// {0} = user name
				"{0} has accepted your invitation to join their team.",	// {0} = user name
				"Your request for ownership for {0} {1} has been declined.",	// {0} = artifact type;	{1} = artifact name
				"Your request for ownership for {0} {1} has been accepted."};	// {0} = artifact type; {1} = artifact name

		USER_DATA =
			new User[] {
				new User("John Wayne", "jwayne", Presence.OFFLINE),
				new User("Omid Ejtemai", "oejtemai", Presence.OFFLINE),
				new User("Raymond Kroeker", "rkroeker", Presence.OFFLINE),
				new User("Alan Turing", "aturing", Presence.OFFLINE),
				new User("Martin Fowler", "mfowler", Presence.OFFLINE),
				new User("Sid Kendall", "skendall", Presence.OFFLINE)};
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

	public Boolean hasBeenSeen() { return (Boolean) getData(ARTIFACT_HAS_BEEN_SEEN); }

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
	public User getActionUser() { return (User) getData(USER_DATA); }

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

	public String getArtifactName(final Integer index) {
		return ARTIFACT_NAME_DATA[index];
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
	public Message getSystemMessage() {
		final Message m = new Message();
		final Integer index = random.nextInt(SYSTEM_MESSAGE_BODY_DATA.length);
		m.setBody(getSystemMessageBody(index));
		m.setHeader(getSystemMessageHeader(index));
		m.setSender(new Sender(USER_DATA[random.nextInt(USER_DATA.length)]));
		return m;
	}

	/**
	 * Obtain random system messages.
	 * 
	 * @return Random system messages.
	 */
	public Message[] getSystemMessages() {
		final Integer messageCount = getSystemMessageCount();
		final Collection<Message> messages = new Vector<Message>(messageCount);
		for(int i = 0; i < messageCount; i++) {
			messages.add(getSystemMessage());
		}
		return messages.toArray(new Message[] {});
	}

	/**
	 * Obtain a random user.
	 * 
	 * @return A random user.
	 */
	public User getUser() { return (User) getData(USER_DATA); }

	private String format(final String pattern, final Object[] arguments) {
		return MessageFormat.format(pattern, arguments);
	}

	private String getArtifactName() {
		return getArtifactName(getDataIndex(ARTIFACT_NAME_DATA));
	}

	/**
	 * Obtain random object data from an input set.
	 * 
	 * @param data
	 *            The random data set.
	 * @return A random object.
	 */
	private Object getData(final Object[] data) {
		logger.warn("Random data.");
		return data[getDataIndex(data)];
	}

	private Integer getDataIndex(final Object[] data) {
		return random.nextInt(data.length);
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

	private String getSystemMessageBody(final Integer index) {
		if(0 == index || 1 == index || 2 == index) {
			// insert a random user's name
			return format(SYSTEM_MESSAGE_BODY_DATA[index], new String[] { getUser().getName() });
		}
		else {
			// insert a random artifact name
			final Integer i = getDataIndex(ARTIFACT_TYPE_DATA);
			return format(SYSTEM_MESSAGE_BODY_DATA[index],
					new String[] {getArtifactType(i).toString().toLowerCase(),
					getArtifactName(i)});
		}
	}

	/**
	 * Obtain the number of system messages to retreive.
	 * 
	 * @return The number of system messages to retreive.
	 */
	private Integer getSystemMessageCount() {
		return (Integer) getData(SYSTEM_MESSAGE_COUNT_DATA);
	}

	private String getSystemMessageHeader(final Integer index) {
		if(0 == index || 1 == index || 2 == index) {
			return SYSTEM_MESSAGE_HEADER_DATA[index];
		}
		else {
			// insert a random artifact name
			return format(
					SYSTEM_MESSAGE_HEADER_DATA[index],
					new String[] {getArtifactName()});
		}
	}

	public ParityObjectType getArtifactType(final Integer index) {
		return ARTIFACT_TYPE_DATA[index];
	}
}
