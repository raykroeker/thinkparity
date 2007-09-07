/*
 * Jan 10, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.text.MessageFormat;
import java.util.List;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;

import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.monitor.PublishData;
import com.thinkparity.ophelia.model.container.monitor.PublishStep;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.session.OfflineException;
import com.thinkparity.ophelia.model.user.UserUtils;
import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.Step;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingWorker;

/**
 * Publish a document. This will send a given document version to every member
 * of the team.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Publish extends AbstractBrowserAction {

    /** An instance of <code>UserUtils</code>. */
    private static final UserUtils USER_UTIL;

    static {
        USER_UTIL = UserUtils.getInstance();
    }

    /** The <code>Browser</code> application. */
    private final Browser browser;

    /**
     * The <code>List</code> of <code>Contact</code>s used by invoke and
     * retry invoke action.
     */
	private List<Contact> contacts;

    /** The <code>Container</code> used by invoke and retry invoke action. */
    private Container container;

    /**
     * The <code>List</code> of <code>EMail</code> addresses used by invoke and
     * retry invoke action.
     */
    private List<EMail> emails;

    /**
     * A <code>ThinkParitySwingMonitor</code>.  Used by invoke and retry invoke.
     * 
     */
    private ThinkParitySwingMonitor monitor;

    /**
     * The <code>List</code> of <code>TeamMember</code>s used by invoke and
     * retry invoke action.
     */
    private List<TeamMember> teamMembers;

    /**
     * The version name <code>String</code> used by invoke and retry invoke
     * action.
     */
    private String versionName;

    /**
     * Create Publish.
     * 
     * @param browser
     *            The <code>Browser</code> application.
     */
	public Publish(final Browser browser) {
		super(ActionId.CONTAINER_PUBLISH);
        this.browser = browser;
	}

    /**
	 * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Boolean displayAvatar = (Boolean) data.get(DataKey.DISPLAY_AVATAR);
        final ContainerModel containerModel = getContainerModel();
        final Container container = containerModel.read(containerId);
        if (containerModel.isLocalDraftModified(containerId)) {
            final ContainerDraft draft = containerModel.readDraft(containerId);
            // make sure there is at least one document, excluding removed documents.
            boolean isDraftPublishable = false;
            for (final Artifact artifact : draft.getArtifacts()) {
                switch (draft.getState(artifact)) {
                case ADDED:
                case MODIFIED:
                case NONE:
                    isDraftPublishable = true;
                    break;
                case REMOVED:
                    break;
                default:
                    throw Assert.createUnreachable("Unknown artifact state.");
                }
                if (isDraftPublishable)
                    break;
            }
            if (isDraftPublishable) {
                if (displayAvatar) {
                    browser.displayPublishContainerDialog(containerId);
                } else {
                    final List<EMail> emails = data.getList(DataKey.EMAILS);
                    final List<Contact> contacts = data.getList(DataKey.CONTACTS);
                    final List<TeamMember> teamMembers = data.getList(DataKey.TEAM_MEMBERS);
                    final String versionName = (String) data.get(DataKey.VERSION_NAME);
                    final ThinkParitySwingMonitor monitor = (ThinkParitySwingMonitor) data.get(DataKey.MONITOR);
                    invoke(monitor, container, versionName, emails, contacts,
                            teamMembers);
                }
            } else {
                browser.displayErrorDialog("Publish.NoDocumentToPublish",
                    new Object[] {container.getName()});
            }
        } else {
	        browser.displayErrorDialog("NoPublish.NoChangesToDraft",
	                new Object[] {container.getName()});
        }
	}

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#retryInvokeAction()
     *
     */
    @Override
    public void retryInvokeAction() {
        invoke(monitor, container, versionName, emails, contacts, teamMembers);
    }

    /**
     * Create a swing worker/monitor and invoke publish.
     * 
     * @param monitor
     *            A <code>ThinkParitySwingMonitor</code>.
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param versionName
     *            A version name <code>String</code>.
     * @param emails
     *            A <code>List</code> of <code>EMail</code> addresses.
     * @param contacts
     *            A <code>List</code> of <code>Contact</code>s.
     * @param teamMembers
     *            A <code>List</code> of <code>TeamMember</code>s.
     */
    private void invoke(final ThinkParitySwingMonitor monitor,
            final Container container, final String versionName,
            final List<EMail> emails, final List<Contact> contacts,
            final List<TeamMember> teamMembers) {
        this.monitor = monitor;
        this.container = container;
        this.emails = emails;
        this.contacts = contacts;
        this.teamMembers = teamMembers;
        this.versionName = versionName;
        final ThinkParitySwingWorker<Publish> worker = new PublishWorker(this,
                container, versionName, emails, contacts, teamMembers);
        worker.setMonitor(monitor);
        worker.start();
    }

    /** Data keys. */
	public enum DataKey {
        CONTACTS, CONTAINER_ID, DISPLAY_AVATAR, EMAILS, MONITOR, TEAM_MEMBERS, VERSION_NAME
    }

    /** A publish action worker object. */
    private static class PublishWorker extends ThinkParitySwingWorker<Publish> {
        private final ContactModel contactModel;
        private final List<Contact> contacts;
        private final Container container;
        private final ContainerModel containerModel;
        private final List<EMail> emails;
        private ProcessMonitor publishMonitor;
        private final List<TeamMember> teamMembers;
        private final String versionName;
        private PublishWorker(final Publish publish, final Container container,
                final String versionName, final List<EMail> emails,
                final List<Contact> contacts, final List<TeamMember> teamMembers) {
            super(publish);
            this.container = container;
            this.emails = emails;
            this.contacts = contacts;
            this.teamMembers = teamMembers;
            this.versionName = versionName;

            this.contactModel = publish.getContactModel();
            this.containerModel = publish.getContainerModel();

            this.publishMonitor = newPublishMonitor();
        }
        @Override
		public Runnable getErrorHandler(final Throwable t) {
			return new Runnable() {
				public void run() {
                    monitor.reset();
                    monitor.complete();
					action.browser.displayErrorDialog("ErrorUnexpected",
					        new Object[] {}, t);
				}
			};
		}
        @Override
        public Object run() {
            monitor.monitor(action.getString("ProgressPublishStart"));
            final boolean restrictPublish;
            try {
                restrictPublish = containerModel.isPublishRestricted(emails,
                        contacts, teamMembers).booleanValue();
            } catch (final OfflineException ox) {
                monitor.reset();
                monitor.setError("ErrorOffline");
                return null;
            }
            if (restrictPublish) {
                monitor.reset();
                monitor.setError("ErrorNoUsersToPublish");
                return null;
            } else {
                /* if any e-mail addresses are contact e-mails, convert them to
                 * contact references */
                Contact contact;
                ContainerVersion latestVersion = null;
                final EMail[] duplicateEmails = (EMail[])emails.toArray(new EMail[0]);
                final List<TeamMember> fullTeam = containerModel.readPublishToTeam(container.getId());
                for (final EMail email : duplicateEmails) {
                    if (contactModel.doesExist(email)) {
                        contact = contactModel.read(email);
                        if (!contacts.contains(contact)
                                && !USER_UTIL.contains(teamMembers, contact)) {
                            if (USER_UTIL.contains(fullTeam, contact)) {
                                teamMembers.add(fullTeam.get(USER_UTIL.indexOf(fullTeam, contact)));
                            } else {
                                contacts.add(contact);
                            }
                        }
                        emails.remove(email);
                    }
                }
                try {
                    containerModel.saveDraft(container.getId());
                } catch (final CannotLockException clx) {
                    monitor.reset();
                    action.browser.retry(action, container.getName());
                    return null;
                }
                try {
                    containerModel.publish(publishMonitor, container.getId(),
                            versionName, emails, contacts, teamMembers);
                    latestVersion = containerModel.readLatestVersion(container.getId());
                    monitor.complete();
                } catch (final OfflineException ox) {
                    action.logger.logError(ox,
                            "Could not publish {0}.", container.getName());
                    try {
                        containerModel.restoreDraft(container.getId());
                    } catch (final CannotLockException clx2) {
                        // not a whole lot that can be done here
                        action.logger.logFatal(clx2,
                                "Could not restore draft for {0}.", container);
                    }
                    monitor.reset();
                    monitor.setError("ErrorOffline");
                    return null;
                } catch (final CannotLockException clx) {
                    action.logger.logError(clx,
                            "Could not publish {0}.", container.getName());
                    try {
                        containerModel.restoreDraft(container.getId());
                    } catch (final CannotLockException clx2) {
                    	// not a whole lot that can be done here
                    	action.logger.logFatal(clx2,
                    			"Could not restore draft for {0}.", container);
                    }
                    monitor.reset();
                    action.browser.retry(action, container.getName());
                    return null;
                } catch (final ThinkParityException tx) {
                    action.logger.logError(tx,
                            "Could not publish {0}.", container.getName());
                    try {
                        containerModel.restoreDraft(container.getId());
                    } catch (final CannotLockException clx2) {
                        // not a whole lot that can be done here
                        action.logger.logFatal(clx2,
                                "Could not restore draft for {0}.", container);
                    }
                    monitor.reset();
                    monitor.complete();
                    throw tx;
                }
                return latestVersion;
            }
        }

		/**
         * Create a publish process monitor.
         * 
         * @return A <code>ProcessMonitor</code>.
         */
        private ProcessMonitor newPublishMonitor() {
            return new ProcessAdapter() {
                /** A running count of encrypted/uploaded bytes. */
                private long encryptedBytes = 0, uploadedBytes = 0;
                /** A <code>MessageFormat</code>. */
                private final MessageFormat percentFormat = new MessageFormat("{0,number,0}");
                /** The current step. */
                private int step;
                /** The number of determined steps. */
                private int steps;
                /**
                 * @see com.thinkparity.ophelia.model.util.ProcessAdapter#beginStep(com.thinkparity.ophelia.model.util.Step,
                 *      java.lang.Object)
                 * 
                 */
                @Override
                public void beginStep(final Step step, final Object data) {
                    final PublishStep publishStep = (PublishStep) step;
                    final PublishData publishData = (PublishData) data;
                    switch (publishStep) {
                    case ENCRYPT_DOCUMENT_VERSION_BYTES:
                    case UPLOAD_DOCUMENT_VERSION_BYTES:
                        setStep(publishStep, publishData);
                        break;
                    case PUBLISH:
                        setStep(publishStep, publishData);
                        break;
                    case UPLOAD_DOCUMENT_VERSION:
                        setStep(publishStep, publishData);
                        break;
                    case UPLOAD_DOCUMENT_VERSIONS:
                        /* where the progress is "determined" */
                        setSteps(publishData);
                        setStep(publishStep, publishData);
                        break;
                    default:
                        Assert.assertUnreachable("Unknown publish step.");
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
                 * Format the number as a percent.
                 */
                private String formatPercent(final Double value) {
                    return percentFormat.format(new Object[] {value});
                }
                /**
                 * Obtain the step note.
                 * 
                 * @return A <code>String</code>.
                 */
                private String getStepNote(final PublishStep step,
                        final PublishData data) {
                    switch (step) {
                    case ENCRYPT_DOCUMENT_VERSION_BYTES:
                        encryptedBytes += data.getBytes();
                        return getString("ProgressEncryptStream",
                                data.getDocumentVersion().getArtifactName());
                    case PUBLISH:
                        return getString("ProgressPublishFinish");
                    case UPLOAD_DOCUMENT_VERSION:
                        encryptedBytes = uploadedBytes = 0L;
                        return getString("ProgressUpload",
                                data.getDocumentVersion().getArtifactName());
                    case UPLOAD_DOCUMENT_VERSION_BYTES:
                        uploadedBytes += data.getBytes();
                        // NOTE Don't bother showing the percent complete for small files.
                        if (2048 >= uploadedBytes) {
                            return getString("ProgressUpload",
                                    data.getDocumentVersion().getArtifactName());
                        } else {
                            final Double percent = Math.min(100.0, (uploadedBytes * 100.0) / data.getDocumentVersion().getSize());
                            return getString("ProgressUploadStream",
                                    data.getDocumentVersion().getArtifactName(),
                                    formatPercent(percent));
                        }
                    case UPLOAD_DOCUMENT_VERSIONS:
                        return getString("ProgressPublishStart");
                    default:
                        throw Assert.createUnreachable("Unknown publish step.");
                    }
                }
                /**
                 * Obtain a localized string from the action.
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
                 * Calculate the current step.
                 * 
                 * @return A number of steps.
                 */
                private void setStep(final PublishStep step,
                        final PublishData data) {
                    switch (step) {
                    case ENCRYPT_DOCUMENT_VERSION_BYTES:
                        // NOTE Step just once during encryption
                        if (0 == encryptedBytes) {
                            this.step++;
                        }
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case PUBLISH:
                        this.step = this.steps;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case UPLOAD_DOCUMENT_VERSION:
                        this.step++;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case UPLOAD_DOCUMENT_VERSION_BYTES:
                        // NOTE This works as long as the buffer used is a multiple
                        // of 1024, otherwise rounding errors will creep in
                        this.step += data.getBytes() / 1024;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    case UPLOAD_DOCUMENT_VERSIONS:
                        this.step = 0;
                        monitor.setStep(this.step, getStepNote(step, data));
                        break;
                    default:
                        Assert.assertUnreachable("Unknown publish step.");
                    }
                }
                /**
                 * Calculate the number of discrete steps involved in
                 * publishing. We need to:
                 * <ol>
                 * <li>Encrypt document version(s).
                 * <li>Upload document version(s).
                 * <li>Publish to e-mails/contacts.
                 * </ol>
                 */
                private void setSteps(final PublishData data) {
                    step = 0;
                    // One step for PUBLISH at the end
                    steps = 1;
                    final List<DocumentVersion> uploadVersions = data.getDocumentVersions();
                    for (final DocumentVersion uploadVersion : uploadVersions) {
                        // One step each for UPLOAD_DOCUMENT_VERSION, ENCRYPT_DOCUMENT_VERSION_BYTES
                        steps += 2;
                        // Steps for UPLOAD_DOCUMENT_VERSION_BYTES, may be 0 for small files
                        steps += uploadVersion.getSize() / 1024;
                    }
                    monitor.setSteps(this.steps);
                    monitor.setStep(this.step);
                }
            };
        }
    }
}
