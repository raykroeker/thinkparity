/*
 * Oct 17, 2005
 */
package com.thinkparity.model;

import com.thinkparity.model.parity.model.session.Credentials;
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

    public static ModelTestUser getX() {
		return new ModelTestUser(
				"parity", "parity", "rkutil.raykroeker.com", 5222, "junit.x");
	}

    public static ModelTestUser getY() {
        return new ModelTestUser(
                "parity", "parity", "rkutil.raykroeker.com", 5222, "junit.y");
    }

    public static ModelTestUser getZ() {
        return new ModelTestUser(
                "parity", "parity", "rkutil.raykroeker.com", 5222, "junit.z");
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

	public Credentials getCredentials() {
        final Credentials credentials = new Credentials();
        credentials.setPassword(password);
        credentials.setUsername(username);
        return credentials;
    }

	public JabberId getJabberId() {
		return JabberIdBuilder.parseQualifiedJabberId(
				new StringBuffer(username)
				.append('@')
				.append(serverHost)
				.append('/')
				.append(resource).toString());
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
}
