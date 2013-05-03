/*
 * Created On:  29-Sep-07 5:15:29 PM
 */
package com.thinkparity.adriana.backup.model.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.adriana.backup.model.Model;
import com.thinkparity.adriana.backup.model.util.Session;

/**
 * <b>Title:</b>thinkParity Adriana Backup Command Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CommandModelImpl extends Model implements CommandModel {

    /**
     * Create CommandModelImpl.
     *
     */
    public CommandModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.adriana.backup.model.command.CommandModel#postExecute(com.thinkparity.adriana.backup.model.util.Session, java.util.List)
     *
     */
    @Override
    public List<Result> postExecute(final Session session, final List<Command> commandList) {
        try {
            return execute(session, commandList);
        } catch (final Exception x) {
            throw panic(x);
        }
    }

    /**
     * @see com.thinkparity.adriana.backup.model.command.CommandModel#preExecute(com.thinkparity.adriana.backup.model.util.Session, java.util.List)
     *
     */
    @Override
    public List<Result> preExecute(final Session session, final List<Command> commandList) {
        try {
            return execute(session, commandList);
        } catch (final Exception x) {
            throw panic(x);
        }
    }

    /**
     * Execute a list of commands returning the result.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param commandList
     *            A <code>List<Command></code>.
     * @return A <code>List<Result></code>.
     */
    private List<Result> execute(final Session session,
            final List<Command> commandList) {
        final Runtime runtime = Runtime.getRuntime();
        /* create the environment */
        final String[] envp = null;
        /* reference the directory */
        final File dir = session.getCommandDirectory();
        final List<Result> resultList = new ArrayList<Result>(commandList.size());
        Result result;
        for (final Command command : commandList) {
            /* create the command */
            final String[] cmdarray = new String[1 + command.getArgumentsSize()];
            cmdarray[0] = command.getCommand();
            final List<String> argumentList = command.getArguments();
            for (int i = 0; i < argumentList.size(); i++) {
                cmdarray[i + 1] = argumentList.get(i);
            }
            result = new Result();
            result.setPass(Boolean.FALSE);
            try {
                runtime.exec(cmdarray, envp, dir);
                result.setPass(Boolean.TRUE);
            } catch (final IOException iox) {
                logger.logError(iox, "Could not execute command {0}.", command);
            } finally {
                resultList.add(result);
            }
        }
        return resultList;
    }
}
