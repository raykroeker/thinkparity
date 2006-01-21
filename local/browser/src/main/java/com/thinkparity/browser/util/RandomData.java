/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.util;

import java.awt.Color;
import java.util.Random;

import com.thinkparity.model.parity.api.ParityObjectFlag;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class RandomData {

	private static final Color[] COLOR_DATA;

	private static final String[] ACTION_DATA;

	private static final String[] ACTION_DATE_DATA;

	private static final String[] ACTION_USER_DATA;

	private static final ParityObjectFlag[] ARTIFACT_FLAG_DATA;

	private static final String[] ARTIFACT_KEY_HOLDER_DATA;

	private static final String[] DATE_DATA;

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

		USER_DATA =
			new String[] {"John Wayne", "Omid Ejtemai", "Raymond Kroeker", "Alan Turing"};
	}

	private final Random random;

	/**
	 * Create a RandomData.
	 */
	public RandomData() {
		super();
		this.random = new Random();
	}

	public Color getColor() { return (Color) getData(COLOR_DATA); }

	public String getAction() { return getStringData(ACTION_DATA); }

	public String getActionDate() { return getStringData(ACTION_DATE_DATA); }

	public String getActionUser() { return getStringData(ACTION_USER_DATA); }

	public ParityObjectFlag getArtifactFlag() {
		return (ParityObjectFlag) getData(ARTIFACT_FLAG_DATA);
	}

	public String getArtifactKeyHolder() { return getStringData(ARTIFACT_KEY_HOLDER_DATA); }

	public String getDate() { return getStringData(DATE_DATA); }
	
	public String getUser() { return getStringData(USER_DATA); }

	private Object getData(final Object[] data) {
		return data[random.nextInt(data.length)];
	}

	private String getStringData(final String[] data) {
		return (String) getData(data);
	}
}
