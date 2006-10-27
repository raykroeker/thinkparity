/*
 * Created On:  Oct 17, 2005
 */
package com.thinkparity.desdemona;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class DesdemonaTestUser extends User {

    /** A test user. */
    public static final DesdemonaTestUser JUNIT;

    /** A test user. */
    public static final DesdemonaTestUser JUNIT_X;

    /** A test user. */
    public static final DesdemonaTestUser JUNIT_Y;

    /** A test user. */
    public static final DesdemonaTestUser JUNIT_Z;

    /** The test users' password. */
    private static final String PASSWORD = "parity";

	static {
        JUNIT = new DesdemonaTestUser(Environment.TESTING_LOCALHOST, "junit");
        JUNIT_X = new DesdemonaTestUser(Environment.TESTING_LOCALHOST, "junit.x");
        JUNIT_Y = new DesdemonaTestUser(Environment.TESTING_LOCALHOST, "junit.y");
        JUNIT_Z = new DesdemonaTestUser(Environment.TESTING_LOCALHOST, "junit.z");
    }

	/** The test user's credentials. */
	private final Credentials credentials;

	/** The test user's environment. */
    private final Environment environment;

	/**
     * Create DesdemonaTestUser.
     * 
     * @param environment
     *            The <code>Environment</code> to use for the user.
     * @param username
     *            The user's login name <code>String</code>.
     */
	private DesdemonaTestUser(final Environment environment, final String username) {
		super();
		this.credentials = new Credentials();
		this.credentials.setPassword(PASSWORD);
		this.credentials.setUsername(username);
        this.environment = environment;
        setId(JabberIdBuilder.parseUsername(credentials.getUsername()));
	}

    /**
     * Obtain the user's credentials.
     * 
     * @return A set of thinkParity <code>Credentials</code>.
     */
	public Credentials getCredentials() {
        return credentials;
    }

    /**
     * Obtain the environment
     *
     * @return The Environment.
     */
    public Environment getEnvironment() {
        return environment;
    }

	/**
     * Assert that the environment is online.
     * 
     * @param assertion
     *            An assertion.
     * @param environment
     *            An environment.
     */
    protected void assertIsReachable(final Environment environment) {
        Assert.assertTrue(environment.isReachable(),
                "Environment {0} is not reachable.", environment);
    }
}
