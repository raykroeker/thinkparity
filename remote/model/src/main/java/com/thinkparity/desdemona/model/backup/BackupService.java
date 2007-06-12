/*
 * Generated On: Sep 04 06 04:29:31 PM
 */
package com.thinkparity.desdemona.model.backup;

import java.io.File;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.Step;
import com.thinkparity.ophelia.model.workspace.InitializeMediator;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

import com.thinkparity.desdemona.model.Constants.JivePropertyNames;
import com.thinkparity.desdemona.util.DesdemonaProperties;

/**
 * <b>Title:</b>thinkParity Backup Serivce</br>
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

    /** The backup <code>Environment</code>. */
    private Environment environment;

    /** The <code>XMPPEventHandler</code>. */
    private final XMPPEventHandler eventHandler;

    /** The backup <code>FileSystem</code>. */
    private final FileSystem fileSystem;

    /** An instance of <code>DesdemonaProperties</code>. */
    private final DesdemonaProperties properties;

    /** A <code>Log4JWrapper</code>. */
    private final Log4JWrapper logger;

    /** The backup <code>InternalModelFactory</code>. */
    private InternalModelFactory modelFactory;

    /** The backup <code>Workspace</code>. */
    private Workspace workspace;

    /**
     * Create BackupService.
     * 
     */
    private BackupService() {
        super();
        this.properties = DesdemonaProperties.getInstance();

        this.eventHandler = new XMPPEventHandler();
        this.logger = new Log4JWrapper(getClass());
        this.fileSystem = readBackupFileSystem();
    }

    /**
     * An xmpp event handler.
     * 
     * @return An <code>XMPPEventHandler</code>.
     */
    public XMPPEventHandler getEventHandler() {
        return eventHandler;
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
     * Read the backup service user's environment.
     * 
     * @return An <code>Environment</code>.
     */
    private Environment readBackupEnvironment() {
        final String thinkParityEnvironment =
            properties.getProperty(JivePropertyNames.THINKPARITY_ENVIRONMENT);
        return Environment.valueOf(thinkParityEnvironment);
    }

    /**
     * Read the file system from the jive properties.
     * 
     * @return A <code>FileSystem</code>.
     */
    private FileSystem readBackupFileSystem() {
        final String thinkParityBackupRoot =
            properties.getProperty(JivePropertyNames.THINKPARITY_BACKUP_ROOT);
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
        final WorkspaceModel workspaceModel = WorkspaceModel.getInstance();
        return workspaceModel.getWorkspace(fileSystem.getRoot());
    }

    /**
     * Start the backup service.  This involves opening the backup workspace.
     *
     */
    private void startImpl() {
        logger.logInfo("Starting backup service.");
        try {
            environment = readBackupEnvironment();
            workspace = readBackupWorkspace();
            Assert.assertNotNull(environment, "Backup environment not yet initialized.");
            Assert.assertNotNull(workspace, "Backup workspace not yet initialized.");
            final WorkspaceModel workspaceModel = WorkspaceModel.getInstance();
            if (!workspaceModel.isInitialized(workspace)) {
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
                }, new InitializeMediator() {
                    public Boolean confirmRestorePremium() {
                        return null;
                    }
                    public Boolean confirmRestoreStandard() {
                        return null;
                    }
                }, workspace, null);
            }
            eventHandler.start(getModelFactory());
            logger.logInfo("Backup service started.");
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
            eventHandler.stop();

            final WorkspaceModel workspaceModel = WorkspaceModel.getInstance();
            workspaceModel.close(workspace);

            logger.logInfo("Backup service stopped.");
        } catch (final Throwable t) {
            throw new BackupException(t, "Failed to stop backup service.");
        }
    }
}
