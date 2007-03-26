/*
 * Generated On: Sep 04 06 04:29:31 PM
 */
package com.thinkparity.desdemona.model.backup;

import java.io.File;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.session.InvalidLocationException;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.Step;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

import com.thinkparity.desdemona.model.Constants.JivePropertyNames;
import com.thinkparity.desdemona.model.user.InternalUserModel;
import com.thinkparity.desdemona.model.user.UserModel;

import org.jivesoftware.util.JiveProperties;

/**
 * <b>Title:</b>thinkParity Archive Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public final class BackupService {

    /** A singleton instance of the <code>BackupService</code>. */
    private static final BackupService SINGLETON;

    static {
        SINGLETON = new BackupService();
    }

    /**
     * Obtain an instance of the <code>BackupService</code>.
     * 
     * @return The <code>BackupService</code>.
     */
    public static BackupService getInstance() {
        return SINGLETON;
    }

    /** The backup user's <code>Credentials</code>. */
    private Credentials credentials;

    /** The backup <code>Environment</code>. */
    private Environment environment;

    /** The backup <code>FileSystem</code>. */
    private final FileSystem fileSystem;

    /** An instance of <code>JiveProperties</code>. */
    private final JiveProperties jiveProperties;

    /** A <code>Log4JWrapper</code>. */
    private final Log4JWrapper logger;

    /** The backup <code>InternalModelFactory</code>. */
    private InternalModelFactory modelFactory;

    /** A user sql interface. */
    private final InternalUserModel userModel;

    /** The backup <code>Workspace</code>. */
    private Workspace workspace;

    /**
     * Create BackupService.
     * 
     */
    BackupService() {
        super();
        this.jiveProperties = JiveProperties.getInstance();

        this.logger = new Log4JWrapper(getClass());
        this.fileSystem = readBackupFileSystem();
        this.userModel = UserModel.getInternalModel(new Context());
    }

    /**
     * Determine if the service is online.
     * 
     * @return True if the service is online.
     */
    public Boolean isOnline() {
        synchronized (this) {
            return isOnlineImpl();
        }
    }

    /**
     * Start the backup service.  This involves opening the backup workspace.
     *
     */
    public void start() {
        synchronized (this) {
            startImpl();
        }
    }

    /**
     * Stop the backup service. This involves closing the backup workspace.
     * 
     */
    public void stop() {
        synchronized (this) {
            stopImpl();
        }
    }

    /**
     * Obtain the archive's model factory.
     * 
     * @param archiveId
     *            An archive id <code>JabberId</code>.
     * @return An archive's <code>ClientModelFactory</code>.
     */
    InternalModelFactory getModelFactory() {
        if (null == modelFactory) {
            modelFactory = InternalModelFactory.getInstance(new Context(),
                    environment, workspace);
        }
        return modelFactory;
    }

    /**
     * Obtain a configuration property.
     * 
     * @param name
     *            A property name <code>String</code>.
     * @return A property value <code>String</code>.
     */
    private String getJiveProperty(final String name) {
        return (String) jiveProperties.get(name);
    }

    /**
     * Determine if the service is online.
     * 
     * @return True if the backup service is online.
     */
    private Boolean isOnlineImpl() {
        return getModelFactory().getSessionModel().isLoggedIn();
    }

    /**
     * Read the backup service user's credentials.
     * 
     * @return The backup service user's login <code>Credentials</code>.
     */
    private Credentials readBackupCredentials() {
        return userModel.readBackupCredentials();
    }

    /**
     * Read the backup service user's environment.
     * 
     * @return An <code>Environment</code>.
     */
    private Environment readBackupEnvironment() {
        final String thinkParityEnvironment =
            (String) JiveProperties.getInstance().get(JivePropertyNames.THINKPARITY_ENVIRONMENT);
        return Environment.valueOf(thinkParityEnvironment);
    }

    /**
     * Read the file system from the jive properties.
     * 
     * @return A <code>FileSystem</code>.
     */
    private FileSystem readBackupFileSystem() {
        final String thinkParityBackupRoot =
            getJiveProperty(JivePropertyNames.THINKPARITY_BACKUP_ROOT);
        final File thinkParityBackupRootDirectory = new File(thinkParityBackupRoot);
        if (!thinkParityBackupRootDirectory.exists())
            Assert.assertTrue(thinkParityBackupRootDirectory.mkdir(),
                    "Could not create directory {0}.",
                    thinkParityBackupRootDirectory);
        return new FileSystem(new File(thinkParityBackupRoot));
    }

    /**
     * Read the backup workspace.
     * 
     * @return The backup <code>Workspace</code>.
     */
    private Workspace readBackupWorkspace() {
        Assert.assertNotNull(environment, "Backup environment not yet initialized.");
        Assert.assertNotNull(fileSystem, "Backup file system not yet initialized.");
        final WorkspaceModel workspaceModel = WorkspaceModel.getInstance(environment);
        return workspaceModel.getWorkspace(fileSystem.getRoot());
    }

    /**
     * Start the backup service.  This involves opening the backup workspace.
     *
     */
    private void startImpl() {
        logger.logInfo("Starting backup service.");
        try {
            credentials = readBackupCredentials();
            environment = readBackupEnvironment();
            workspace = readBackupWorkspace();
            Assert.assertNotNull(credentials, "Backup credentials not yet initialized.");
            Assert.assertNotNull(environment, "Backup environment not yet initialized.");
            Assert.assertNotNull(workspace, "Backup workspace not yet initialized.");
            final WorkspaceModel workspaceModel = WorkspaceModel.getInstance(environment);
            if (!workspaceModel.isInitialized(workspace)) {
                try {
                    workspaceModel.initialize(new ProcessAdapter() {
                        @Override
                        public void beginProcess() {}
                        @Override
                        public void beginStep(final Step step, final Object data) {}
                        @Override
                        public void determineSteps(final Integer steps) {}
                        @Override
                        public void endProcess() {}
                        @Override
                        public void endStep(final Step step) {}
                    }, workspace, credentials);
                } catch (final InvalidCredentialsException icx) {
                    throw new BackupException(icx, "Cannot initialize backup workspace.");
                }
            } else {
                try {
                    getModelFactory().getSessionModel().login(new ProcessAdapter() {
                            @Override
                            public void beginProcess() {}
                            @Override
                            public void beginStep(final Step step, final Object data) {}
                            @Override
                            public void determineSteps(final Integer steps) {}
                            @Override
                            public void endProcess() {}
                            @Override
                            public void endStep(final Step step) {}
                    });
                } catch (final InvalidCredentialsException icx) {
                    throw new BackupException(icx, "Cannot login backup.");
                } catch (final InvalidLocationException ilx) {
                    throw new BackupException(ilx, "Cannot login backup.");
                }
            }
            logger.logInfo("Backup service is online.");
        } catch (final Throwable t) {
            throw new BackupException(t, "Failed to start backup service.");
        }
    }

    /**
     * Stop the backup service.
     * 
     */
    private void stopImpl() {
        logger.logInfo("Stopping backup service .");
        try {
            getModelFactory().getSessionModel().logout();

            final WorkspaceModel workspaceModel = WorkspaceModel.getInstance(environment);
            workspaceModel.close(workspace);

            logger.logInfo("Backup service is offline.");
        } catch (final Throwable t) {
            throw new BackupException(t, "Failed to stop backup service.");
        }
    }
}
