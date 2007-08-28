/**
 * Created On: Mar 5, 2007 11:29:45 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.platform;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.model.contact.monitor.DownloadData;
import com.thinkparity.ophelia.model.contact.monitor.DownloadStep;
import com.thinkparity.ophelia.model.container.monitor.RestoreBackupData;
import com.thinkparity.ophelia.model.container.monitor.RestoreBackupStep;
import com.thinkparity.ophelia.model.session.OfflineException;
import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.Step;
import com.thinkparity.ophelia.model.workspace.InitializeMediator;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.monitor.InitializeData;
import com.thinkparity.ophelia.model.workspace.monitor.InitializeStep;

import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingWorker;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Login extends AbstractAction {

    /** The browser platform. */
    private final Platform platform;

    /**
     * Create Login.
     * 
     * @param platform
     *            The browser platform.
     */
    public Login(final Platform platform) {
        super(ActionId.PLATFORM_LOGIN);
        this.platform = platform;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    public void invoke(Data data) {
        final InitializeMediator mediator = (InitializeMediator) data.get(DataKey.INITIALIZE_MEDIATOR);
        final ThinkParitySwingMonitor monitor = (ThinkParitySwingMonitor) data.get(DataKey.MONITOR);
        final String password = (String) data.get(DataKey.PASSWORD);
        final String username = (String) data.get(DataKey.USERNAME);
        invoke(monitor, username, password, mediator);
    }

    /**
     * Invoke the login swing worker.
     * 
     * @param monitor
     *            A <code>ThinkParitySwingMonitor</code>.
     * @param username
     *            A <code>String</code>.
     * @param password
     *            A <code>String</code>.
     * @param initializeMediator
     *            An <code>InitializeMediator</code>.
     */
    public void invoke(final ThinkParitySwingMonitor monitor,
            final String username, final String password,
            final InitializeMediator mediator) {
        final Credentials credentials = new Credentials();
        credentials.setPassword(password);
        credentials.setUsername(username);
        final ThinkParitySwingWorker<Login> worker = new LoginWorker(this,
                credentials, mediator);
        worker.setMonitor(monitor);
        worker.start();
    }

    /** Data keys. */
    public enum DataKey {INITIALIZE_MEDIATOR, MONITOR, PASSWORD, USERNAME}

    /** <b>Title:</b>Login Swing Worker<br> */
    private static class LoginWorker extends ThinkParitySwingWorker<Login> {
        /** A bytes format. */
        private final BytesFormat bytesFormat;
        /** The <code>Credentials</code>.. */
        private final Credentials credentials;
        /** A number of decrypted/downloaded bytes for a document version. */
        private long decryptBytes, downloadBytes;
        /** The login <code>ProcessMonitor</code>. */
        private final ProcessMonitor loginMonitor;
        /** The <code>InitializeMediator</code>. */
        private final InitializeMediator mediator;
        /** The thinkParity <code>Workspace</code>. */
        private final Workspace workspace;
        /**
         * Create LoginWorker.
         * 
         * @param login
         *            A <code>Login</code>.
         * @param credentials
         *            A set of <code>Credentials</code>.
         * @param mediator
         *            An <code>InitializeMediator</code>.
         */
        private LoginWorker(final Login login, final Credentials credentials,
                final InitializeMediator mediator) {
            super(login);
            this.bytesFormat = new BytesFormat();
            this.credentials = credentials;
            this.mediator = mediator;
            this.loginMonitor = newLoginMonitor();
            this.workspace = action.platform.getModelFactory().getWorkspace(
                    getClass());
        }
        /**
         * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingWorker#getErrorHandler(java.lang.Throwable)
         *
         */
        @Override
		public Runnable getErrorHandler(final Throwable t) {
			return new Runnable() {
			    /**
			     * @see java.lang.Runnable#run()
			     *
			     */
			    @Override
				public void run() {
				    action.logger.logError(t, "An error has occurred for login.");
				}
			};
		}
        /**
         * @see com.thinkparity.codebase.swing.SwingWorker#construct()
         * 
         */
        @Override
        public Object run() {
            monitor.monitor();
            try {
                action.platform.initializeWorkspace(loginMonitor, mediator,
                        workspace, credentials);
                monitor.complete();
            } catch (final InvalidCredentialsException icx) {
                action.logger.logError(icx, "An invalid credentials error has occurred in login.");
                monitor.reset();
                monitor.setError("ErrorInvalidCredentials");
                return null;
            } catch (final OfflineException ox) {
                action.logger.logError(ox, "An offline error has occurred in login.");
                monitor.reset();
                monitor.setError("ErrorOffline");
                return null;
            } catch (final Throwable t) {
                action.logger.logError(t, "An unexpected error has occurred in login.");
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
         * Create a new process monitor.
         * 
         * @return A <code>ProcessMonitor</code>.
         */
        private ProcessMonitor newLoginMonitor() {
            return new ProcessAdapter() {
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
                    if (step instanceof InitializeStep) {
                        final InitializeStep initializeStep = (InitializeStep) step;
                        final InitializeData initializeData = (InitializeData) data;
                        beginStep(initializeStep, initializeData);
                    } else if (step instanceof RestoreBackupStep) {
                        final RestoreBackupStep restoreBackupStep = (RestoreBackupStep) step;
                        final RestoreBackupData restoreBackupData = (RestoreBackupData) data;
                        beginStep(restoreBackupStep, restoreBackupData);
                    } else if (step instanceof DownloadStep) {
                        final DownloadStep downloadStep = (DownloadStep) step;
                        final DownloadData downloadData = (DownloadData) data;
                        beginStep(downloadStep, downloadData);
                    } else {
                        Assert.assertUnreachable("Unknown login step {0}.", step);
                    }
                }
                @Override
                public void endStep(final Step step) {
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
                 * Begin a step for initialization.
                 * 
                 * @param step
                 *            An <code>InitializeStep</code>.
                 * @param data
                 *            An <code>InitializeData</code>.
                 */
                private void beginStep(final InitializeStep step, final InitializeData data) {
                    switch (step) {
                    case PERSISTENCE_INITIALIZE:
                        setSteps(step, data);
                        break;
                    case PROFILE_CREATE:
                        setStep(step, data);
                        break;
                    case PUBLISH_WELCOME:
                        setStep(step, data);
                        break;
                    case SESSION_LOGIN:
                        setStep(step, data);
                        break;
                    case SESSION_PROCESS_QUEUE:
                        setStep(step, data);
                        break;
                    default:
                        Assert.assertUnreachable("Unknown initialize step.");
                    }
                }
                /**
                 * Begin a step for restoring backup.
                 * 
                 * @param step
                 *            A <code>RestoreBackupStep</code>.
                 * @param data
                 *            A <code>RestoreBackupData</code>.
                 */
                private void beginStep(final RestoreBackupStep step,
                        final RestoreBackupData data) {
                    switch (step) {
                    case DELETE_CONTAINER:
                    case DELETE_CONTAINERS:
                        break;
                    case RESTORE_CONTAINER:
                        this.step++;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case RESTORE_CONTAINERS:
                        setSteps(step, data);
                        break;
                    case RESTORE_DOCUMENT_VERSION:
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
                 * Obtain a progress note for an initialize step.
                 * 
                 * @param step
                 *            An <code>InitializeStep</code>.
                 * @param data
                 *            The <code>InitializeData</code>.
                 * @return A <code>String</code>.
                 */
                private String getStepNote(final InitializeStep step,
                        final InitializeData data) {
                    switch (step) {
                    case PROFILE_CREATE:
                        return getString("StepProfileCreate");
                    case PERSISTENCE_INITIALIZE:
                        return getString("StepPersistenceInitialize");
                    case PUBLISH_WELCOME:
                        return null;
                    case SESSION_LOGIN:
                        return getString("StepSessionLogin");
                    case SESSION_PROCESS_QUEUE:
                        return getString("StepProcessQueue");
                    default:
                        throw Assert.createUnreachable("UNKNOWN STEP STATE");
                    }
                }
                /**
                 * Obtain the progress note for a restore step.
                 * 
                 * @param step
                 *            A <code>RestoreBackupStep</code>.
                 * @param data
                 *            The <code>RestoreBackupData</code>.
                 * @return A <code>String</code>.
                 */
                private String getStepNote(final RestoreBackupStep step,
                        final RestoreBackupData data) {
                    switch ((RestoreBackupStep) step) {
                    case DELETE_CONTAINER:
                        return null;
                    case DELETE_CONTAINERS:
                        return null;
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
                 * Set the current initialize step.
                 * 
                 * @param step
                 *            An <code>InitializeStep</code>.
                 * @param data
                 *            An <code>InitializeData</code>.
                 */
                private void setStep(final InitializeStep step,
                        final InitializeData data) {
                    switch (step) {
                    case PERSISTENCE_INITIALIZE:
                        this.step += 4;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case PROFILE_CREATE:
                        this.step += 3;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case PUBLISH_WELCOME:
                        this.step += 2;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case SESSION_LOGIN:
                        this.step += 3;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case SESSION_PROCESS_QUEUE:
                        this.step += 5;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    default:
                        Assert.assertUnreachable("Unknown initialize step.");
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
                 * Set the initialization steps.
                 * 
                 * @param data
                 *            An <code>InitializeData</code>.
                 */
                private void setSteps(final InitializeStep step,
                        final InitializeData data) {
                    switch (step) {
                    case PERSISTENCE_INITIALIZE:
                        /* initalize persistence + login + create profile */
                        this.step = 0;
                        this.steps = 4 + 3 + 2 + 3 + 5;
                        monitor.setSteps(this.steps);
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case PROFILE_CREATE:
                    case PUBLISH_WELCOME:
                    case SESSION_LOGIN:
                    case SESSION_PROCESS_QUEUE:
                    default:
                        Assert.assertUnreachable("Unknown initialize step.");
                    }
                }
                /**
                 * Set the restore backup steps.
                 * 
                 * @param step
                 *            A <code>RestoreBackupStep</code>.
                 * @param data
                 *            An <code>RestoreBackupData</code>.
                 */
                private void setSteps(final RestoreBackupStep step,
                        final RestoreBackupData data) {
                    switch (step) {
                    case RESTORE_CONTAINERS:
                        this.step = 0;
                        this.steps = data.getRestoreContainers().size();
                        monitor.setSteps(this.steps);
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case RESTORE_DOCUMENT_VERSIONS:
                    case RESTORE_CONTAINER:
                    case DELETE_CONTAINER:
                    case DELETE_CONTAINERS:
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
