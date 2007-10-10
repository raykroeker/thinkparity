/*
 * Created On:  29-Sep-07 3:33:23 PM
 */
package com.thinkparity.adriana.backup.ui;

/**
 * <b>Title:</b>thinkParity Adriana Backup Command<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface BackupCommand {

    /**
     * Obtain the command name.
     * 
     * @return The command name <code>String</code>.
     */
    public String getName();

    /**
     * Initialize the command.
     * 
     * @param context
     *            A <code>BackupCommandContext</code>.
     */
    public void initialize(final BackupCommandContext context);

    /**
     * Invoke the command.
     * 
     * @throws BackupException
     *             if the command cannot be completed
     */
    public void invoke() throws BackupException;
}
