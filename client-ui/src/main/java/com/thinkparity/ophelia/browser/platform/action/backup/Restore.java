/*
 * Created On:  24-Aug-07 1:31:18 PM
 */
package com.thinkparity.ophelia.browser.platform.action.backup;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.model.backup.BackupModel;
import com.thinkparity.ophelia.model.contact.monitor.DownloadData;
import com.thinkparity.ophelia.model.contact.monitor.DownloadStep;
import com.thinkparity.ophelia.model.container.monitor.DeleteLocalData;
import com.thinkparity.ophelia.model.container.monitor.DeleteLocalStep;
import com.thinkparity.ophelia.model.container.monitor.RestoreLocalData;
import com.thinkparity.ophelia.model.container.monitor.RestoreLocalStep;
import com.thinkparity.ophelia.model.session.OfflineException;
import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.Step;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatar.TabId;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
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
        final Boolean displayAvatar = (Boolean) data.get(DataKey.DISPLAY_AVATAR);
        if (displayAvatar) {
            browser.displayRestoreBackupDialog();
        } else {
            final Credentials credentials = (Credentials) data.get(DataKey.CREDENTIALS);
            final ThinkParitySwingMonitor monitor = (ThinkParitySwingMonitor) data.get(DataKey.MONITOR);
            final ThinkParitySwingWorker<Restore> worker = new RestoreWorker(this,
                    credentials);
            worker.setMonitor(monitor);
            worker.start();
        }
    }

    /** Data keys. */
    public enum DataKey { CREDENTIALS, DISPLAY_AVATAR, MONITOR }

    /** <b>Title:</b>Restore Swing Worker<br> */
    private static class RestoreWorker extends ThinkParitySwingWorker<Restore> {

        /** A backup model. */
        private final BackupModel backupModel;

        /** A bytes format. */
        private final BytesFormat bytesFormat;

        /** The <code>Credentials</code>.. */
        private final Credentials credentials;

        /** A number of decrypted/downloaded bytes for a document version. */
        private long decryptBytes, downloadBytes;

        /** A process monitor. */
        private ProcessMonitor processMonitor;

        /**
         * Create RestoreWorker.
         *
         */
        private RestoreWorker(final Restore restore, final Credentials credentials) {
            super(restore);
            this.bytesFormat = new BytesFormat();
            this.backupModel = restore.getBackupModel();
            this.credentials = credentials;
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
            monitor.monitor(action.getString("StepInitializing"));
            try {
                backupModel.restore(processMonitor, credentials);
                monitor.complete();
                action.browser.reloadTab(TabId.CONTAINER);
                action.browser.reloadTab(TabId.CONTACT);
                action.browser.reloadStatus();
                action.browser.reloadMenuBar();
            } catch (final InvalidCredentialsException icx) {
                action.logger.logError(icx, "An invalid credentials error has occurred in restore.");
                monitor.reset();
                monitor.setError("ErrorInvalidCredentials");
                return null;
            } catch (final OfflineException ox) {
                action.logger.logError(ox, "An offline error has occurred in restore.");
                monitor.reset();
                monitor.setError("ErrorOffline");
                return null;
            } catch (final Throwable t) {
                action.logger.logError(t, "An unexpected error has occurred in restore.");
                monitor.reset();
                monitor.setError("ErrorUnexpected");
                return null;
            }
            return null;
        }

        /**
         * Obtain a localized string.
         * 
         * @param key
         *            A <code>String</code>.
         * @param data
         *            An <code>Object[]</code>.
         * @return A <code>String</code>.
         */
        private String getString(final String key, final Object... data) {
            return action.getString(key, data);
        }

        /**
         * Create a process monitor. The process monitor translate model process
         * steps into swing ui updates.
         * 
         * @return A <code>ProcessMonitor</code>.
         */
        private ProcessMonitor newProcessMonitor() {
            return new ProcessAdapter() {
                /** The restore document version step. */
                private int restoreDocumentVersionStep;
                /** The current step. */
                private int step;
                /** The number of steps. */
                private int steps;
                /**
                 * @see com.thinkparity.ophelia.model.util.ProcessAdapter#beginStep(com.thinkparity.ophelia.model.util.Step, java.lang.Object)
                 *
                 */
                @Override
                public void beginStep(final Step step, final Object data) {
                    if (step instanceof RestoreLocalStep) {
                        beginStep((RestoreLocalStep) step, (RestoreLocalData) data);
                    } else if (step instanceof DeleteLocalStep) {
                        beginStep((DeleteLocalStep) step, (DeleteLocalData) data);
                    } else if (step instanceof DownloadStep) {
                        beginStep((DownloadStep) step, (DownloadData) data);
                    } else {
                        Assert.assertUnreachable("Unknown restore step {0}.", step);
                    }
                }
                @Override
                public void endStep(final Step step) {
                }
                private void beginStep(final DeleteLocalStep step, final DeleteLocalData data) {
                    switch (step) {
                    case DELETE_CONTAINER:
                        this.step++;
                        monitor.setStep(this.step);
                        break;
                    case DELETE_CONTAINERS:
                        setSteps(step, data);
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    }
                }
                /**
                 * Begin a step for download.
                 * 
                 * @param step
                 *            A <code>DownloadStep</code>.
                 * @param data
                 *            A <code>DownloadData</code>.
                 */
                private void beginStep(final DownloadStep step,
                        final DownloadData data) {
                    switch (step) {
                    case DOWNLOAD_SIZE:
                        setSteps(step, data);
                        setStep(step, data);
                        break;
                    case DOWNLOAD_CONTACT:
                        setStep(step, data);
                        break;
                    default:
                        Assert.assertUnreachable("Unknown download step.");
                    }
                }
                /**
                 * Begin a step for restoring backup.
                 * 
                 * @param step
                 *            A <code>RestoreLocalStep</code>.
                 * @param data
                 *            A <code>RestoreLocalData</code>.
                 */
                private void beginStep(final RestoreLocalStep step,
                        final RestoreLocalData data) {
                    switch (step) {
                    case FINALIZE_RESTORE:
                        this.step += 5;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case RESTORE_CONTAINER:
                        this.step++;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case RESTORE_CONTAINERS:
                        setSteps(step, data);
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case RESET_RESTORE_DOCUMENT_VERSION:
                        this.step = this.restoreDocumentVersionStep;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case RESTORE_DOCUMENT_VERSION:
                        this.restoreDocumentVersionStep = this.step;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case RESTORE_DOCUMENT_VERSION_DECRYPT_BYTES:
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case RESTORE_DOCUMENT_VERSION_DOWNLOAD_BYTES:
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case RESTORE_DOCUMENT_VERSIONS:
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case RESTORE_DOCUMENTS:
                        break;
                    default:
                        Assert.assertUnreachable("Unkown restore backup step.");
                    }
                }
                /**
                 * Obtain the step note for deleting local data.
                 * 
                 * @param step
                 *            A <code>DeleteLocalStep</code>.
                 * @param data
                 *            A <code>DeleteLocalData</code>.
                 * @return A <code>String</code>.
                 */
                private String getStepNote(final DeleteLocalStep step,
                        final DeleteLocalData data) {
                    switch (step) {
                        case DELETE_CONTAINERS:
                            return getString("StepDeleteContainers");
                        case DELETE_CONTAINER:      // deliberate fall-through
                        default:
                            throw Assert.createUnreachable("Unsupported delete local step.");
                    }
                }
                /**
                 * Obtain a progress note for an initialize step.
                 * 
                 * @param step
                 *            An <code>InitializeStep</code>.
                 * @param data
                 *            The <code>InitializeData</code>.
                 * @return A <code>String</code>.
                 */
                private String getStepNote(final DownloadStep step,
                        final DownloadData data) {
                    switch (step) {
                    case DOWNLOAD_CONTACT:
                        return getString("StepDownloadContact",
                                data.getContact().getName());
                    case DOWNLOAD_SIZE:
                        return getString("StepDownloadContacts", data.getSize());
                    default:
                        throw Assert.createUnreachable("UNKNOWN STEP STATE");
                    }
                }
                /**
                 * Obtain the progress note for a restore step.
                 * 
                 * @param step
                 *            A <code>RestoreLocalStep</code>.
                 * @param data
                 *            The <code>RestoreLocalData</code>.
                 * @return A <code>String</code>.
                 */
                private String getStepNote(final RestoreLocalStep step,
                        final RestoreLocalData data) {
                    switch (step) {
                    case FINALIZE_RESTORE:
                        return getString("StepFinalize");
                    case RESET_RESTORE_DOCUMENT_VERSION:
                        decryptBytes = downloadBytes = 0L;
                        return getString("StepRestoreDocumentVersion",
                                data.getRestoreDocumentVersion().getArtifactName());
                    case RESTORE_CONTAINER:
                        return getString("StepRestoreContainer",
                                data.getRestoreContainer().getName());
                    case RESTORE_CONTAINERS:
                        return getString("StepRestoreContainers",
                                data.getRestoreContainers().size());
                    case RESTORE_DOCUMENTS:
                        return getString("StepRestoreDocuments",
                                data.getRestoreDocuments().size());
                    case RESTORE_DOCUMENT_VERSION:
                        decryptBytes = downloadBytes = 0L;
                        return getString("StepRestoreDocumentVersion",
                                data.getRestoreDocumentVersion().getArtifactName());
                    case RESTORE_DOCUMENT_VERSIONS:
                        return getString("StepRestoreDocumentVersions",
                                data.getRestoreDocumentVersions().size());
                    case RESTORE_DOCUMENT_VERSION_DECRYPT_BYTES:
                        decryptBytes += data.getBytes();
                        return getString("StepRestoreDocumentVersionDecrypt",
                                data.getRestoreDocumentVersion().getArtifactName(),
                                bytesFormat.format(decryptBytes),
                                bytesFormat.format(data.getRestoreDocumentVersion().getSize()));
                    case RESTORE_DOCUMENT_VERSION_DOWNLOAD_BYTES:
                        downloadBytes += data.getBytes();
                        return getString("StepRestoreDocumentVersionDownload",
                                data.getRestoreDocumentVersion().getArtifactName(),
                                bytesFormat.format(downloadBytes),
                                bytesFormat.format(data.getRestoreDocumentVersion().getSize()));
                    default:
                        throw Assert.createUnreachable("Unknown restore backup step.");
                    }            
                }
                /**
                 * Set the current download step.
                 * 
                 * @param step
                 *            An <code>DownloadStep</code>.
                 * @param data
                 *            An <code>DownloadData</code>.
                 */
                private void setStep(final DownloadStep step,
                        final DownloadData data) {
                    switch (step) {
                    case DOWNLOAD_CONTACT:
                        this.step++;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case DOWNLOAD_SIZE:
                        this.step = 0;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    default:
                        Assert.assertUnreachable("Unknown initialize step.");
                    }
                }
                /**
                 * Adjust the number of steps based upon the delete local
                 * step/data.
                 * 
                 * @param step
                 *            A <code>DeleteLocalStep</code>.
                 * @param data
                 *            A <code>DeleteLocalData</code>.
                 */
                private void setSteps(final DeleteLocalStep step,
                        final DeleteLocalData data) {
                    switch (step) {
                    case DELETE_CONTAINERS:
                        this.step = 0;
                        this.steps = data.getDeleteContainers().size();
                        monitor.setSteps(this.steps);
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case DELETE_CONTAINER:  // deliberate fall through
                    default:
                        Assert.assertUnreachable("Unsupported delete local step.");
                    }
                }
                /**
                 * Set the download steps.
                 * 
                 * @param step
                 *            A <code>DownloadStep</code>.
                 * @param data
                 *            An <code>DownloadData</code>.
                 */
                private void setSteps(final DownloadStep step,
                        final DownloadData data) {
                    switch (step) {
                    case DOWNLOAD_SIZE:
                        this.step = 0;
                        this.steps = data.getSize();
                        monitor.setSteps(this.steps);
                        monitor.setStep(this.step);
                        break;
                    case DOWNLOAD_CONTACT:
                    default:
                        Assert.assertUnreachable("Unknown download step.");
                    }
                }
                /**
                 * Set the restore backup steps.
                 * 
                 * @param step
                 *            A <code>RestoreLocalStep</code>.
                 * @param data
                 *            An <code>RestoreLocalData</code>.
                 */
                private void setSteps(final RestoreLocalStep step,
                        final RestoreLocalData data) {
                    switch (step) {
                    case RESTORE_CONTAINERS:
                        this.step = 0;
                        this.steps = data.getRestoreContainers().size() + 5;
                        monitor.setSteps(this.steps);
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case FINALIZE_RESTORE:
                    case RESTORE_DOCUMENT_VERSIONS:
                    case RESTORE_CONTAINER:
                    case RESTORE_DOCUMENT_VERSION:
                    case RESTORE_DOCUMENT_VERSION_DECRYPT_BYTES:
                    case RESTORE_DOCUMENT_VERSION_DOWNLOAD_BYTES:
                    case RESTORE_DOCUMENTS:
                    default:
                        Assert.assertUnreachable("Unknown initialize step.");
                    }
                }
            };
        }
    }
}
