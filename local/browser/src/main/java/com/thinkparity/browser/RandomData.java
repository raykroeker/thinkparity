/*
 * Jan 5, 2006
 */
package com.thinkparity.browser;

import java.util.Random;
import java.util.UUID;

import com.thinkparity.model.parity.api.ParityObjectFlag;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class RandomData {

	private static final String[] ACTION_DATA =
		{"Created", "Sent", "Received"};

	private static final String[] ARTIFACT_KEY_HOLDER_DATA =
		{"John Wayne", "Omid Ejtemai", "Raymond Kroeker", "Alan Turing"};

	private static final ParityObjectFlag[] ARTIFACT_FLAG_DATA =
		{ParityObjectFlag.CLOSED, ParityObjectFlag.SEEN, ParityObjectFlag.KEY};

	private static final String[] DATE_DATA =
		{"Today", "Yesterday", "Last Monday", "Last Tuesday", "Last Wednesday", "Last Thursday", "Last Friday"};

	private final Random random;

	/**
	 * Create a RandomData.
	 */
	public RandomData() {
		super();
		this.random = new Random();
	}

	public String getAction() { return getStringData(ACTION_DATA); }

	public String getArtifactKeyHolder() { return getStringData(ARTIFACT_KEY_HOLDER_DATA); }

	public ParityObjectFlag getArtifactFlag() {
		return (ParityObjectFlag) getData(ARTIFACT_FLAG_DATA);
	}
	
	public String getDate() { return getStringData(DATE_DATA); }

	public UUID getProjectId() {
		return UUID.fromString(System.getProperty("parity.projectId"));
	}

	private Object getData(final Object[] data) {
		return data[random.nextInt(data.length)];
	}

	private String getStringData(final String[] data) {
		return (String) getData(data);
	}
}
