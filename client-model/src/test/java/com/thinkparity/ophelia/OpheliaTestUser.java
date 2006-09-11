/*
 * Oct 17, 2005
 */
package com.thinkparity.ophelia;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.user.User;


import com.thinkparity.ophelia.model.user.UserModel;


/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class OpheliaTestUser {

	public static OpheliaTestUser getJUnit() {
		return new OpheliaTestUser(
				"junit@thinkparity.com", "parity", "parity", OpheliaTestCase.TEST_SERVERHOST, OpheliaTestCase.TEST_SERVERPORT, "junit");
	}

    public static OpheliaTestUser getX() {
		return new OpheliaTestUser(
                "junit.x@thinkparity.com", "parity", "parity", OpheliaTestCase.TEST_SERVERHOST, OpheliaTestCase.TEST_SERVERPORT, "junit.x");
	}

    public static OpheliaTestUser getY() {
        return new OpheliaTestUser(
                "junit.y@thinkparity.com", "parity", "parity", OpheliaTestCase.TEST_SERVERHOST, OpheliaTestCase.TEST_SERVERPORT, "junit.y");
    }

    public static OpheliaTestUser getZ() {
        return new OpheliaTestUser(
                "junit.z@thinkparity.com", "parity", "parity", OpheliaTestCase.TEST_SERVERHOST, OpheliaTestCase.TEST_SERVERPORT, "junit.z");
    }

    private final String emailAddress;
	private final String password;
    private final String resource;
	private final String serverHost;
	private final Integer serverPort;
	private final String username;

	/**
	 * Create a ModelTestUser.
	 */
	private OpheliaTestUser(final String emailAddress, final String password,
            final String resource, final String serverHost,
            final Integer serverPort, final String username) {
		super();
        this.emailAddress = emailAddress;
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

	/**
     * Obtain the emailAddress
     *
     * @return The String.
     */
    public String getEmailAddress() {
        return emailAddress;
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

	public User getUser() {
        final User user = new User();
        user.setId(getJabberId());
        return user;
    }

    /**
	 * @return The username.
	 */
	public String getUsername() { return username; }

    /**
     * Read the user from the user model.
     * 
     * @return A user.
     */
    public User readUser() {
        return UserModel.getModel().read(getJabberId());
    }
}
