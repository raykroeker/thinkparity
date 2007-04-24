/*
 * Created On:  Oct 17, 2005
 */
package com.thinkparity.ophelia;

import java.io.File;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.profile.InternalProfileModel;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.Step;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSession;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSessionImpl;
import com.thinkparity.ophelia.model.workspace.InitializeMediator;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class OpheliaTestUser extends User {

    /** A test user. */
    public static final OpheliaTestUser JUNIT;

    /** A test user. This user should not be any other user's contact. */
    public static final OpheliaTestUser JUNIT_W;

    /** A test user. */
    public static final OpheliaTestUser JUNIT_X;

    /** A test user. */
    public static final OpheliaTestUser JUNIT_Y;

    /** A test user. */
    public static final OpheliaTestUser JUNIT_Z;

    /** A system user. */
    public static final OpheliaTestUser THINKPARITY;

    /** An initialize <code>ProcessMonitor</code>. */
    private static final ProcessMonitor INITIALIZE_MONITOR;

    /** An <code>InitializeMediator</code>. */
    private static final InitializeMediator INITIALIZE_MEDIATOR;

    /** An apache <code>Log4JWrapper</code>. */
    private static final Log4JWrapper LOGGER;

    /** The test users' password. */
    private static final String PASSWORD;

	static {
        INITIALIZE_MEDIATOR = new InitializeMediator() {
            public Boolean confirmRestore(final List<Feature> features) {
                return Boolean.TRUE;
            }
        };
        INITIALIZE_MONITOR = new ProcessMonitor() {
            public void beginProcess() {
                LOGGER.logInfo("Begin workspace initialize process.");
            }
            public void beginStep(final Step step, final Object data) {
                if (null == data)
                    LOGGER.logInfo("Begin workspace initialize process step {0}.",
                            step);
                else
                    LOGGER.logInfo("Begin workspace initialize process step {0}:{1}.",
                            step, data);
            }
            public void determineSteps(final Integer steps) {
                LOGGER.logInfo("Determine workspace initialize process steps {0}.", steps);
            }
            public void endProcess() {
                LOGGER.logInfo("End workspace initialize process.");
            }
            public void endStep(final Step step) {
                LOGGER.logInfo("End workspace initialize process step {0}.", step);
            }
        };
        LOGGER = new Log4JWrapper("");
        PASSWORD = "parity";

        JUNIT = new OpheliaTestUser(OpheliaTestCase.ENVIRONMENT, "junit");
        JUNIT_W = new OpheliaTestUser(OpheliaTestCase.ENVIRONMENT, "junit.w");
        JUNIT_X = new OpheliaTestUser(OpheliaTestCase.ENVIRONMENT, "junit.x");
        JUNIT_Y = new OpheliaTestUser(OpheliaTestCase.ENVIRONMENT, "junit.y");
        JUNIT_Z = new OpheliaTestUser(OpheliaTestCase.ENVIRONMENT, "junit.z");
        THINKPARITY = new OpheliaTestUser(OpheliaTestCase.ENVIRONMENT, "thinkparity");
	}

    /** The test user's <code>Context</code>. */
    private final Context context;

	/** The test user's credentials. */
	private final Credentials credentials;

	/** The test user's e-mail address. */
    private EMail email;

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
        this.context = new Context();
		this.credentials = new Credentials();
		this.credentials.setPassword(PASSWORD);
		this.credentials.setUsername(username);
        this.environment = environment;
        final WorkspaceModel workspaceModel = WorkspaceModel.getInstance(environment);
        this.workspace =
            workspaceModel.getWorkspace(
                    new File(OpheliaTestCase.SESSION.getOutputDirectory(),
                            "TEST." + username));
        try {
            processOfflineQueue();
        } catch (final InvalidCredentialsException icx) {
            LOGGER.logFatal("Could not login as {0}.", username);
        }
        initialize();
	}

    /**
     * Obtain context.
     * 
     * @return A <code>Context</code>.
     */
    public final Context getContext() {
        return context;
    }

    /**
     * Obtain the user's credentials.
     * 
     * @return A set of thinkParity <code>Credentials</code>.
     */
	public final Credentials getCredentials() {
        return credentials;
    }

    /**
     * Obtain the test user's <code>EMail</code>.
     * 
     * @return An <code>EMail</code> address.
     */
    public EMail getEmail() {
        return email;
    }

    /**
     * Obtain the environment
     *
     * @return The Environment.
     */
    public final Environment getEnvironment() {
        return environment;
    }

    /**
     * Obtain a model factory for the user.
     * 
     * @return An instance of <code>InternalModelFactory</code>.
     */
    public final InternalModelFactory getModelFactory() {
        return InternalModelFactory.getInstance(getContext(), environment,
                workspace);
    }

	/**
     * Obtain the user's <code>Workspace</code>.
     * 
     * @return A <code>Workspace</code>.
     */
    public final Workspace getWorkspace() {
        return workspace;
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
                "Environment {0} is not reachable.", environment.name());
    }

    /**
     * Initialize the test user. We login (via the session model); download all
     * contacts and read our profile then logout.
     * 
     */
    private void initialize() {
        final WorkspaceModel workspaceModel = WorkspaceModel.getInstance(environment);
        if (!workspaceModel.isInitialized(workspace)) {
            try {
                workspaceModel.initialize(INITIALIZE_MONITOR,
                        INITIALIZE_MEDIATOR, workspace, credentials);
            } catch (final InvalidCredentialsException icx) {
                LOGGER.logFatal("Could not login with credentials {0}.", credentials);
            }
        }
        setId(JabberIdBuilder.build(
                credentials.getUsername(), environment.getXMPPService()));
        final InternalProfileModel profileModel = getModelFactory().getProfileModel();
        email = profileModel.readEmails().get(0).getEmail();
        final Profile profile = profileModel.read();
        setName(profile.getName());
        setOrganization(profile.getOrganization());
        setTitle(profile.getTitle());
        getModelFactory().getSessionModel().logout();
    }

    /**
     * Login via the xmpp library to clear any\all pending events from previous
     * test sessions.
     * 
     */
    private void processOfflineQueue() throws InvalidCredentialsException {
        assertIsReachable(environment);
        final XMPPSession session = new XMPPSessionImpl();
        session.login(environment, credentials);
        session.registerQueueListener();
        Assert.assertNotNull(session,
                "User {0}'s session is null.", credentials.getUsername());
        Assert.assertTrue(session.isOnline(),
                "User {0} not online.", credentials.getUsername());
        session.logout();
    }
}
