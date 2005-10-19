/*
 * Jan 17, 2005
 */
package com.thinkparity.model.parity.util;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.Parity;

/**
 * ParityUtil
 * Utility functions used by the client.
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public class ParityUtil {

	/**
	 * Handle to the Parity class.
	 */
	private static final Class clientClass =
		Parity.class;

	/**
	 * Handle to an internal config.
	 */
	private static final Config config =
		ConfigFactory.newInstance(ParityUtil.class);

	/**
	 * Handle to an internal logger.
	 */
	private static final Logger logger =
		ModelLoggerFactory.getLogger(ParityUtil.class);

	/**
	 * Obtain a handle to the Parity class.
	 * @return <code>Class</code>
	 */
	public static Class getClientClass() { return clientClass; }

	/**
	 * Obtain the name of the client application.
	 * @return <code>String</code>s
	 */
	public static String getClientName() { return config.getProperty("name"); }

	/**
	 * TODO: Determine whether this is an acceptable solution.
	 * 
	 * @return The username reserved for system actions.
	 */
	public static String getSystemUsername() { return "system@paritysoftware.com"; }

	/**
	 * Obtain the version number of the client.
	 * @return <code>String</code>
	 */
	public static String getVersion() { return config.getProperty("version"); }

	public static void launchFileWin32(final String fileAbsolutePath)
			throws IOException {
		logger.debug("fileAbsolutePath:  " + fileAbsolutePath);
		final Runtime runtime = Runtime.getRuntime();
		runtime.exec(new String[] { "rundll32.exe",
					"url.dll,FileProtocolHandler", fileAbsolutePath });
	}

	/**
	 * Create a ParityUtil [Singleton]
	 */
	private ParityUtil() { super(); }
}
