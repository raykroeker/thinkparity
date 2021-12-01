/*
 * Jan 5, 2006
 */
package com.thinkparity.ophelia.browser.platform.util;

import java.awt.Color;
import java.util.Random;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactType;


/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class RandomData {

	private static final String[] ACTION_DATA;

	private static final String[] ACTION_DATE_DATA;

	private static final ArtifactFlag[] ARTIFACT_FLAG_DATA;

	private static final Boolean[] ARTIFACT_HAS_BEEN_SEEN;

	private static final String[] ARTIFACT_KEY_HOLDER_DATA;

	private static final String[] ARTIFACT_NAME_DATA;

	private static final ArtifactType[] ARTIFACT_TYPE_DATA;

	private static final Color[] COLOR_DATA;

	private static final String[] DATE_DATA;

	static {
		ACTION_DATA = new String[] {"Created by ", "Sent to ", "Received from "};

		ACTION_DATE_DATA =
			new String[] {" today", " yesterday", " last friday", " on Tuesday Dec 2, 2005"};

		ARTIFACT_FLAG_DATA =
			new ArtifactFlag[] {ArtifactFlag.SEEN};

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

		ARTIFACT_TYPE_DATA = new ArtifactType[] {
				ArtifactType.DOCUMENT,
				ArtifactType.DOCUMENT,
				ArtifactType.DOCUMENT,
				ArtifactType.DOCUMENT,
				ArtifactType.DOCUMENT,
				ArtifactType.DOCUMENT,
				ArtifactType.DOCUMENT,
				ArtifactType.DOCUMENT,
				ArtifactType.DOCUMENT,
				ArtifactType.DOCUMENT,
				ArtifactType.DOCUMENT,
				ArtifactType.DOCUMENT,
				ArtifactType.DOCUMENT,
				ArtifactType.DOCUMENT};

		COLOR_DATA =
			new Color[] {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW};

		DATE_DATA =
			new String[] {" today", " yesterday", " last monday", " last tuesday", " last wednesday", " last thursday", " last friday", " on Jan 2", " on Jan 23", " on Dec 26, 2005", " on Sep 4, 2004"};
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
		this.logger = Logger.getLogger(getClass());
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
	 * Obtain a random artifact flag.
	 * 
	 * @return A random artifact flag.
	 */
	public ArtifactFlag getArtifactFlag() {
		return (ArtifactFlag) getData(ARTIFACT_FLAG_DATA);
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

	public ArtifactType getArtifactType(final Integer index) {
		return ARTIFACT_TYPE_DATA[index];
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

	public Boolean hasBeenSeen() { return (Boolean) getData(ARTIFACT_HAS_BEEN_SEEN); }

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
}
