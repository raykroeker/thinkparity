/*
 * Jan 5, 2006
 */
package com.thinkparity.browser;

import java.util.Random;
import java.util.UUID;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class RandomData {

	private static final String[] ACTION_DATA =
		{"Created", "Sent", "Received"};

	private static final String[] ARTIFACT_KEY_HOLDER_DATA =
		{"John Wayne", "Omid Ejtemai", "Raymond Kroeker", "Alan Turing"};

	private static final String[] ARTIFACT_STATE_DATA =
		{"Active", "Archived", "Closed"};

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

	public String getAction() { return getData(ACTION_DATA); }

	public String getArtifactKeyHolder() { return getData(ARTIFACT_KEY_HOLDER_DATA); }

	public String getArtifactState() { return getData(ARTIFACT_STATE_DATA); }
	
	public String getDate() { return getData(DATE_DATA); }

	public UUID getProjectId() {
		return UUID.fromString(System.getProperty("parity.projectId"));
	}

	private String getData(final String[] data) {
		return data[random.nextInt(data.length)];
	}
}
