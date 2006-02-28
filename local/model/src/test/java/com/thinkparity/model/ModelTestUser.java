/*
 * Oct 17, 2005
 */
package com.thinkparity.model;

import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;
import com.thinkparity.model.xmpp.user.User;


/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class ModelTestUser {

	public static ModelTestUser getJUnit() {
		return new ModelTestUser(
				"parity", "parity", "rkutil.raykroeker.com", 5222, "junit");
	}
	public static ModelTestUser getJUnitBuddy0() {
		return new ModelTestUser(
				"parity", "parity", "rkutil.raykroeker.com", 5222, "junit.buddy.0");
	}

	private final String password;
	private final String resource;
	private final String serverHost;
	private final Integer serverPort;
	private final String username;

	/**
	 * Create a ModelTestUser.
	 */
	private ModelTestUser(final String password, final String resource,
			final String serverHost, final Integer serverPort,
			final String username) {
		super();
		this.password = password;
		this.resource = resource;
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.username = username;
	}

	/**
	 * @return The password.
	 */
	public String getPassword() { return password; }

	/**
	 * @return Returns the resource.
	 */
	public String getResource() { return resource; }

	/**
	 * @return The serverHost.
	 */
	public String getServerHost() { return serverHost; }

	/**
	 * @return The serverPort.
	 */
	public Integer getServerPort() { return serverPort; }

	public User getUser() { return new User(username); }

	/**
	 * @return The username.
	 */
	public String getUsername() { return username; }

	public JabberId getJabberId() {
		return JabberIdBuilder.parseQualifiedJabberId(
				new StringBuffer(username)
				.append('@')
				.append(serverHost)
				.append('/')
				.append(resource).toString());
	}
}
