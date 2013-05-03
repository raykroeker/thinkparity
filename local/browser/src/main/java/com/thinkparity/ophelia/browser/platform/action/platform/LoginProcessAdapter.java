/*
 * Created On:  30-Aug-07 2:50:47 PM
 */
package com.thinkparity.ophelia.browser.platform.action.platform;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.contact.monitor.DownloadData;
import com.thinkparity.ophelia.model.contact.monitor.DownloadStep;
import com.thinkparity.ophelia.model.container.monitor.RestoreLocalData;
import com.thinkparity.ophelia.model.container.monitor.RestoreLocalStep;
import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.Step;
import com.thinkparity.ophelia.model.workspace.monitor.InitializeData;
import com.thinkparity.ophelia.model.workspace.monitor.InitializeStep;

import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;

/**
 * <b>Title:</b>thinkParity Ophelia UI Login Progress Adapter<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class LoginProcessAdapter extends ProcessAdapter {

    /** A bytes format. */
    private static final BytesFormat bytesFormat;

    static {
        bytesFormat = new BytesFormat();
    }

    /** A number of decrypted bytes. */
    private long decryptBytes;

    /** A number of downloaded bytes. */
    private long downloadBytes;

    /** The login action. */
    private final Login login;

    /** A swing monitor. */
    private ThinkParitySwingMonitor monitor;

    /** The restore document version step. */
    private int restoreDocumentVersionStep;

    /** The current step. */
    private int step;

    /** The number of steps. */
    private int steps;

    /**
     * Create LoginProcessAdapter.
     * 
     * @param login
     *            A <code>Login</code>.
     */
    public LoginProcessAdapter(final Login login) {
        super();
        this.login = login;
    }

    /**
     * @see com.thinkparity.ophelia.model.util.ProcessAdapter#beginStep(com.thinkparity.ophelia.model.util.Step, java.lang.Object)
     *
     */
    @Override
    public void beginStep(final Step step, final Object data) {
        if (step instanceof InitializeStep) {
            beginStep((InitializeStep) step, (InitializeData) data);
        } else if (step instanceof RestoreLocalStep) {
            beginStep((RestoreLocalStep) step, (RestoreLocalData) data);
        } else if (step instanceof DownloadStep) {
            beginStep((DownloadStep) step, (DownloadData) data);
        } else {
            Assert.assertUnreachable("Unknown login step {0}.", step);
        }
    }
    /**
     * @see com.thinkparity.ophelia.model.util.ProcessAdapter#endStep(com.thinkparity.ophelia.model.util.Step)
     * 
     */
    @Override
    public void endStep(final Step step) {
    }

    /**
     * Set the monitor.
     *
     * @param monitor
     *		A <code>ThinkParitySwingMonitor</code>.
     */
    void setMonitor(final ThinkParitySwingMonitor monitor) {
        this.monitor = monitor;
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
        case RESET_RESTORE_DOCUMENT_VERSION:
            this.step = this.restoreDocumentVersionStep;
            monitor.setStep(this.step, getStepNote(step, data));
            break;
        case RESTORE_CONTAINER:
            this.step++;
            monitor.setStep(this.step, getStepNote(step, data));
            break;
        case RESTORE_CONTAINERS:
            setSteps(step, data);
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
     *            A <code>RestoreLocalStep</code>.
     * @param data
     *            The <code>RestoreLocalData</code>.
     * @return A <code>String</code>.
     */
    private String getStepNote(final RestoreLocalStep step,
            final RestoreLocalData data) {
        switch ((RestoreLocalStep) step) {
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
     * Obtain a localized string.
     * 
     * @param key
     *            A <code>String</code>.
     * @param arguments
     *            An <code>Object[]</code>.
     * @return A <code>String</code>.
     */
    private String getString(final String key, final Object... arguments) {
        return login.getString(key, arguments);
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
        case RESTORE_DOCUMENT_VERSIONS:
        case RESTORE_CONTAINER:
        case FINALIZE_RESTORE:
        case RESTORE_DOCUMENT_VERSION:
        case RESTORE_DOCUMENT_VERSION_DECRYPT_BYTES:
        case RESTORE_DOCUMENT_VERSION_DOWNLOAD_BYTES:
        case RESTORE_DOCUMENTS:
        default:
            Assert.assertUnreachable("Unknown initialize step.");
        }
    }
}
