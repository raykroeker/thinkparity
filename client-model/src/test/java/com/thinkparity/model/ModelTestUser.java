/*
 * Oct 17, 2005
 */
package com.thinkparity.model;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class ModelTestUser {

	public static ModelTestUser getJUnit() {
		return new ModelTestUser(
				"parity", "thinkparity.dyndns.org", 5223, "junit");
	}

	private final String password;
	private final String serverHost;
	private final Integer serverPort;
	private final String username;

	/**
	 * Create a ModelTestUser.
	 */
	private ModelTestUser(final String password, final String serverHost,
			final Integer serverPort, final String username) {
		super();
		this.password = password;
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.username = username;
	}

	/**
	 * @return The password.
	 */
	public String getPassword() { return password; }

	/**
	 * @return The serverHost.
	 */
	public String getServerHost() {
		return serverHost;
	}

	/**
	 * @return The serverPort.
	 */
	public Integer getServerPort() {
		return serverPort;
	}

	/**
	 * @return The username.
	 */
	public String getUsername() { return username; }

}
