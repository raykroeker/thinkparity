/*
 * Created On:  29-Sep-07 4:13:53 PM
 */
package com.thinkparity.adriana.backup.ui.command;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.adriana.backup.ui.BackupCommand;
import com.thinkparity.adriana.backup.ui.BackupCommandContext;

/**
 * <b>Title:</b>thinkParity Adriana Backup Usage Command<br>
 * <b>Description:</b>Display usage information.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Usage implements BackupCommand {

    /** A command context. */
    private BackupCommandContext context;

    /**
     * Create Usage.
     *
     */
    public Usage() {
        super();
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
    }

    /**
     * @see com.thinkparity.adriana.backup.ui.BackupCommand#invoke()
     *
     */
    @Override
    public void invoke() {
        // NOCOMMIT Backup.invoke NYI raymond@thinkparity.com 29-Sep-07
        throw Assert.createNotYetImplemented("");
    }

}
