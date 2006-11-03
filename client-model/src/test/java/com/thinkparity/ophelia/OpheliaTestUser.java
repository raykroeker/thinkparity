/*
 * Created On:  Oct 17, 2005
 */
package com.thinkparity.ophelia;

import java.io.File;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.Constants;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.DefaultLoginMonitor;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSession;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSessionImpl;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class OpheliaTestUser extends User {

    /** A test user. */
    public static final OpheliaTestUser JUNIT;

    /** A test user. */
    public static final OpheliaTestUser JUNIT_X;

    /** A test user. */
    public static final OpheliaTestUser JUNIT_Y;

    /** A test user. */
    public static final OpheliaTestUser JUNIT_Z;

    /** The test users' password. */
    private static final String PASSWORD = "parity";

	static {
        JUNIT = new OpheliaTestUser(OpheliaTestCase.ENVIRONMENT, "junit");
        JUNIT_X = new OpheliaTestUser(OpheliaTestCase.ENVIRONMENT, "junit.x");
        JUNIT_Y = new OpheliaTestUser(OpheliaTestCase.ENVIRONMENT, "junit.y");
        JUNIT_Z = new OpheliaTestUser(OpheliaTestCase.ENVIRONMENT, "junit.z");
    }

	/** The test user's credentials. */
	private final Credentials credentials;

	/** The test user's environment. */
    private final Environment environment;

    /** The test user's workspace. */
    private final Workspace workspace;

	/**
     * Create OpheliaTestUser.
     * 
     * @param environment
     *            The <code>Environment</code> to use for the user.
     * @param username
     *            The user's login name <code>String</code>.
     */
	private OpheliaTestUser(final Environment environment, final String username) {
		super();
		this.credentials = new Credentials();
		this.credentials.setPassword(PASSWORD);
		this.credentials.setUsername(username);
        this.environment = environment;
        this.workspace =
            WorkspaceModel.getModel(environment).getWorkspace(
                    new File(OpheliaTestCase.SESSION.getOutputDirectory(),
                            "TEST." + username));
        this.workspace.getPreferences().setUsername(username);
        processOfflineQueue();
        initialize();
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

    public Workspace getWorkspace() {
        return workspace;
    }

    /**
     * Initialize the test user. We login (via the session model); download all
     * contacts and read our profile then logout.
     * 
     */
    private void initialize() {
        SessionModel.getModel(environment, workspace).login(new DefaultLoginMonitor() {
            @Override
            public Boolean confirmSynchronize() {
                return Boolean.TRUE;
            }
        }, credentials);
        try {
            ContactModel.getModel(environment, workspace).download();
            setId(ProfileModel.getModel(environment, workspace).read().getId());
        } finally {
            SessionModel.getModel(environment, workspace).logout();
        }
        setId(JabberIdBuilder.parseUsername(credentials.getUsername()));
    }

    /**
     * Login via the xmpp library to clear any\all pending events from previous
     * test sessions.
     * 
     */
    private void processOfflineQueue() {
        assertIsReachable(environment);
        XMPPSession session = null;
        try {
            session = new XMPPSessionImpl();
            session.login(environment, credentials);
            final JabberId userId = JabberIdBuilder.build(
                    credentials.getUsername(), Constants.Jabber.DOMAIN,
                    credentials.getResource());
            session.processQueue(userId);
        } finally {
            Assert.assertNotNull(session,
                    "User {0}'s session is null.", credentials.getUsername());
            Assert.assertTrue(session.isLoggedIn(),
                    "User {0} not logged in.", credentials.getUsername());
            session.logout();
        }
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
