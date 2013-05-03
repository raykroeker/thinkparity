/*
 * Created On:  29-Sep-07 4:44:22 PM
 */
package com.thinkparity.adriana.backup.model.command;

import java.util.List;

import com.thinkparity.adriana.backup.model.util.Session;

/**
 * <b>Title:</b>thinkParity Adriana Command Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface CommandModel {

    /**
     * Execute a list of commands post backup.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param commandList
     *            A <code>List<Command></code>.
     */
    List<Result> postExecute(Session session, final List<Command> commandList);

    /**
     * Execute a list of commands prior to backup.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param commandList
     *            A <code>List<Command></code>.
     */
    List<Result> preExecute(Session session, final List<Command> commandList);
}
