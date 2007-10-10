/*
 * Created On:  29-Sep-07 3:30:27 PM
 */
package com.thinkparity.adriana.backup.ui;

import java.io.IOException;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import org.apache.commons.cli.ParseException;

import com.thinkparity.adriana.backup.model.backup.BackupModel;
import com.thinkparity.adriana.backup.ui.BackupException.Code;

/**
 * <b>Title:</b>thinkParity Adriana Backup UI<br>
 * <b>Description:</b>Main entry point into the backup application.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class BackupClient {

    /** A default command. */
    private static final String defaultCommand;

    /** A log4j wrapper. */
    private static final Log4JWrapper logger;

    static {
        defaultCommand = "usage";
        logger = new Log4JWrapper(BackupClient.class);
    }

    /**
     * Start backup.
     * 
     * @param args
     *            A <code>String[]</code>.
     */
    public static void main(final String[] args) {
        try {
            final BackupCLI cli = new BackupCLI();
            cli.define();
            try {
                cli.parse(args);
            } catch (final ParseException px) {
                cli.printUsage();
                System.exit(1);
            }
            new BackupClient(cli).invoke();
        } catch (final Exception x) {
            logger.logFatal(x, "Could not run backup client.");
            System.exit(1);
        }
    }

    /** A command line interpreter. */
    private final BackupCLI cli;

    /** A command registry mapping commands to fully-qualified class names. */
    private final BackupCommandRegistry commandRegistry;

    /**
     * Create BackupClient.
     * 
     * @param cli
     *            A <code>BackupCLI</code>.
     */
    private BackupClient(final BackupCLI cli) {
        super();
        this.cli = cli;
        this.commandRegistry = new BackupCommandRegistry();
    }

    /**
     * Run the specified command.
     * 
     */
    private void invoke() throws NoSuchCommandException, BackupException {
        final BackupCommand command = newCommand();
        final BackupCommandContext context = newCommandContext();
        invoke(context, command);
    }

    /**
     * Invoice the command within the context.
     * 
     * @param context
     *            A <code>BackupCommandContext</code>.
     * @param command
     *            A <code>BackupCommand</code>.
     */
    private void invoke(final BackupCommandContext context, final BackupCommand command) {
        command.initialize(context);
        try {
            command.invoke();
        } catch (final BackupException bx) {
            logger.logFatal(bx, "Could not invoke backup command.");
            System.exit(1);
        }
    }

    /**
     * Instantiate the backup command.
     * 
     * @return A <code>BackupCommand</code>.
     * @throws NoSuchCommandException
     *             if the command does not exist
     */
    private BackupCommand newCommand() throws NoSuchCommandException { 
        try {
            final String className = newCommandClassName();
            final Class<?> commandClass = Class.forName(className);
            return (BackupCommand) commandClass.newInstance();
        } catch (final ClassNotFoundException cnfx) {
            throw new NoSuchCommandException(cli.getCommand());
        } catch (final IllegalAccessException iax) {
            throw new NoSuchCommandException(cli.getCommand());
        } catch (final InstantiationException ix) {
            throw new NoSuchCommandException(cli.getCommand());
        }
    }

    /**
     * Instantiate a command class name.
     * 
     * @return A <code>String</code>.
     */
    private String newCommandClassName() {
        return commandRegistry.newClassName(cli.getCommand(), defaultCommand);
    }

    /**
     * Instantiate a command context.
     * 
     * @return A <code>BackupCommandContext</code>.
     */
    private BackupCommandContext newCommandContext() throws BackupException {
        final BackupCommandContext commandContext = new BackupCommandContext();
        try {
            commandContext.loadProperties();
        } catch (final IOException iox) {
            throw new BackupException(Code.LOAD_PROPERTIES_FAILED, iox);
        }
        /* create a new backup session. */
        final BackupModel backupModel = commandContext.getModelFactory().getBackupModel();
        commandContext.setSession(backupModel.newSession());
        return commandContext;
    }
}
