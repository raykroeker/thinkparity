/*
 * Created On:  29-Sep-07 4:13:53 PM
 */
package com.thinkparity.adriana.backup.ui.command;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import com.thinkparity.codebase.nio.ChannelUtil;

import com.thinkparity.adriana.backup.model.backup.BackupModel;
import com.thinkparity.adriana.backup.model.backup.Resource;
import com.thinkparity.adriana.backup.model.command.Command;
import com.thinkparity.adriana.backup.model.command.CommandModel;
import com.thinkparity.adriana.backup.model.notify.NotifyModel;
import com.thinkparity.adriana.backup.ui.BackupCommand;
import com.thinkparity.adriana.backup.ui.BackupCommandContext;
import com.thinkparity.adriana.backup.ui.BackupException;
import com.thinkparity.adriana.backup.ui.BackupException.Code;

/**
 * <b>Title:</b>thinkParity Adriana Backup Command<br>
 * <b>Description:</b>Issue a backup.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Backup implements BackupCommand {

    /** A backup model. */
    private BackupModel backupModel;

    /** A command model. */
    private CommandModel commandModel;

    /** A command context. */
    private BackupCommandContext context;

    /** A notify model. */
    private NotifyModel notifyModel;

    /** A set of command utilities. */
    private final CommandUtils utils;

    /**
     * Create Backup.
     *
     */
    public Backup() {
        super();
        this.utils = new CommandUtils();
    }

    /**
     * @see com.thinkparity.adriana.backup.ui.BackupCommand#getName()
     *
     */
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    /**
     * @see com.thinkparity.adriana.backup.ui.BackupCommand#initialize(com.thinkparity.adriana.backup.ui.BackupCommandContext)
     *
     */
    @Override
    public void initialize(final BackupCommandContext context) {
        this.context = context;
        this.backupModel = context.getModelFactory().getBackupModel();
        this.commandModel = context.getModelFactory().getCommandModel();
        this.notifyModel = context.getModelFactory().getNotifyModel();
    }

    /**
     * @see com.thinkparity.adriana.backup.ui.BackupCommand#invoke()
     *
     */
    @Override
    public void invoke() throws BackupException {
        final Properties properties = context.getProperties();
        final List<Command> precommandList = newCommandList(utils.extractPrecommands(properties));
        final List<Resource> resourceList = newResourceList(utils.extractResources(properties));
        final List<Command> postcommandList = newCommandList(utils.extractPostcommands(properties));
        invoke(precommandList, resourceList, postcommandList);
    }

    /**
     * Invoke a backup.
     * 
     * @param precommandList
     *            A <code>List<Command></code>.
     * @param resourceList
     *            A <code>List<Resource></code>.
     * @param postcommandList
     *            A <code>List<Command></code>.
     */
    void invoke(final List<Command> precommandList,
            final List<Resource> resourceList,
            final List<Command> postcommandList) {
        commandModel.preExecute(context.getSession(), precommandList);
        try {
            backupModel.archive(context.getSession(), resourceList);
            backupModel.backup(context.getSession());
            notifyModel.notify(context.getSession());
        } finally {
            commandModel.postExecute(context.getSession(), postcommandList);
        }
    }

    /**
     * Instantiate a command list from a string list. Each string in the list is
     * tokenized for a comannd and its arguments.
     * 
     * @param stringList
     *            A <code>List<String></code>.
     * @return A <code>List<Command></code>.
     */
    private List<Command> newCommandList(final List<String> stringList) {
        final List<Command> commandList = new ArrayList<Command>(stringList.size());
        for (final String string : stringList) {
            final Command command = new Command();
            final StringTokenizer tokenizer = new StringTokenizer(string);
            command.setCommand(tokenizer.nextToken());
            while (tokenizer.hasMoreTokens()) {
                command.addArgument(tokenizer.nextToken());
            }
            commandList.add(command);
        }
        return commandList;
    }

    /**
     * Instantiate a resource list from a string list.
     * 
     * @param stringList
     *            A <code>List<String></code>.
     * @return A <code>List<Resource></code>.
     */
    private List<Resource> newResourceList(final List<String> stringList)
            throws BackupException {
        final List<Resource> resourceList =  new ArrayList<Resource>(stringList.size());
        for (final String string : stringList) {
            final URI uri;
            try {
                uri = new URI(string);
            } catch (final URISyntaxException urisx) {
                throw new BackupException(Code.INVALID_URI_SYNTAX, urisx,
                        string);
            }
            if ("file".equals(uri.getScheme())) {
                final File file = new File(uri);
                resourceList.add(new Resource() {
                    /**
                     * @see com.thinkparity.adriana.backup.model.backup.Resource#getPath()
                     *
                     */
                    @Override
                    public String getPath() {
                        return uri.getPath();
                    }
                    /**
                     * @see com.thinkparity.adriana.backup.model.backup.Resource#openChannel()
                     *
                     */
                    @Override
                    public ReadableByteChannel openChannel() throws IOException {
                        return ChannelUtil.openReadChannel(file);
                    }
                });
            } else {
                throw new BackupException(Code.UNSUPPORTED_RESOURCE_SCHEME,
                        uri.getScheme());
            }
        }
        return resourceList;
    }
}
