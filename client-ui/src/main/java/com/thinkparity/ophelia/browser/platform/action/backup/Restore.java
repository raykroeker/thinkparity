/*
 * Created On:  24-Aug-07 1:31:18 PM
 */
package com.thinkparity.ophelia.browser.platform.action.backup;

import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.session.InvalidLocationException;

import com.thinkparity.ophelia.model.backup.BackupModel;
import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.Step;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingWorker;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Restore extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create Restore.
     *
     */
    public Restore(final Browser browser) {
        super(ActionId.BACKUP_RESTORE);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     *
     */
    @Override
    protected void invoke(final Data data) {
        final String confirmKey;
        if (getProfileModel().isBackupEnabled()) {
            confirmKey = "Restore.ConfirmWithBackup";
        } else {
            confirmKey = "Restore.ConfirmWithoutBackup";
        }
        if (browser.confirm(confirmKey)) {
            /* restore */
            browser.applyBusyIndicator();
            try {
                new RestoreWorker(this).run();
            } finally {
                browser.removeBusyIndicator();
            }
        }
    }

    /** <b>Title:</b>Restore Swing Worker<br> */
    private static class RestoreWorker extends ThinkParitySwingWorker<Restore> {

        /** A backup model. */
        private final BackupModel backupModel;

        /** A process monitor. */
        private ProcessMonitor processMonitor;

        /**
         * Create RestoreWorker.
         *
         */
        private RestoreWorker(final Restore restore) {
            super(restore);
            this.backupModel = restore.getBackupModel();
            this.processMonitor = newProcessMonitor();
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingWorker#getErrorHandler(java.lang.Throwable)
         *
         */
        @Override
        public Runnable getErrorHandler(final Throwable t) {
            return new Runnable() {
                public void run() {
                    monitor.reset();
                    monitor.complete();
                    action.browser.displayErrorDialog("ErrorUnexpected", new Object[] {}, t);
                }
            };
        }

        /**
         * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingWorker#run()
         *
         */
        @Override
        public Object run() {
            try {
                backupModel.restore(processMonitor);
            } catch (final InvalidCredentialsException icx) {
            } catch (final InvalidLocationException ilx) {
            }
            return null;
        }

        /**
         * Create a process monitor. The process monitor translate model process
         * steps into swing ui updates.
         * 
         * @return A <code>ProcessMonitor</code>.
         */
        private ProcessMonitor newProcessMonitor() {
            return new ProcessAdapter() {
                @Override
                public void beginProcess() {
                }
                @Override
                public void beginStep(final Step step, final Object data) {
                }
                @Override
                public void determineSteps(final Integer steps) {
                }
                @Override
                public void endProcess() {
                }
                @Override
                public void endStep(final Step step) {
                }
            };
        }
    }
}
