/*
 * Oct 17, 2005
 */
package com.thinkparity.ophelia;

import java.io.File;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.util.smack.SmackException;
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
        JUNIT = new OpheliaTestUser(Environment.TESTING, "junit");
        JUNIT_X = new OpheliaTestUser(Environment.TESTING, "junit.x");
        JUNIT_Y = new OpheliaTestUser(Environment.TESTING, "junit.y");
        JUNIT_Z = new OpheliaTestUser(Environment.TESTING, "junit.z");
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
            WorkspaceModel.getModel().getWorkspace(
                    new File(OpheliaTestCase.testSession.getSessionDirectory(),
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

    public Workspace getWorkspace() {
        return workspace;
    }

    /**
     * Initialize the test user. We login (via the session model); download all
     * contacts and read our profile then logout.
     * 
     */
    private void initialize() {
        SessionModel.getModel(workspace).login(credentials);
        try {
            ContactModel.getModel(workspace).download();
            setId(ProfileModel.getModel(workspace).read().getId());
        } finally {
            SessionModel.getModel(workspace).logout();
        }
        setId(JabberIdBuilder.parseUsername(credentials.getUsername()));
    }

    /**
     * Login via the xmpp library to clear any\all pending events from previous
     * test sessions.
     * 
     */
    private void processOfflineQueue() {
        XMPPSession session = null;
        try {
            session = new XMPPSessionImpl();
            session.login(environment, credentials);
            session.processOfflineQueue();
        } catch (final SmackException sx) {
            throw new RuntimeException(sx);
        } finally {
            Assert.assertNotNull(session,
                    "User {0}'s session is null.", credentials.getUsername());
            Assert.assertTrue(session.isLoggedIn(),
                    "User {0} not logged in.", credentials.getUsername());
            try {
                session.logout();
            } catch (final SmackException sx) {
                throw new RuntimeException(sx);
            }
        }
    }
}
