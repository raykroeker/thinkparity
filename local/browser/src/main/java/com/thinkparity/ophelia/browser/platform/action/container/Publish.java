/*
 * Jan 10, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.monitor.PublishStep;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.Step;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
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
public class Publish extends AbstractAction {

    /** The <code>Browser</code> application. */
    private final Browser browser;

	private String comment;

	private List<Contact> contacts;

    private Container container;

    /**
     * A <code>ThinkParitySwingMonitor</code>.  Used by invoke and retry invoke.
     * 
     */
    private ThinkParitySwingMonitor monitor;

    private List<TeamMember> teamMembers;

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
        final List<User> contactsIn = getDataUsers(data, DataKey.CONTACTS);
        final List<User> teamMembersIn = getDataUsers(data, DataKey.TEAM_MEMBERS);
        final String comment = (String) data.get(DataKey.COMMENT);
        final ContainerModel containerModel = getContainerModel();
        final Container container = containerModel.read(containerId);
        // check there are documents with changes to publish.
        Boolean publishableDocuments = Boolean.FALSE;
        Boolean changes = Boolean.FALSE;
        List<Document> documents = Collections.emptyList();
        if (container.isLocalDraft()) {
            final ContainerDraft draft = containerModel.readDraft(containerId);
            if (null != draft) {
                documents = draft.getDocuments();
                for (final Document document : documents) {
                    switch (draft.getState(document)) {
                    case ADDED:
                    case MODIFIED:
                        changes = Boolean.TRUE;
                        publishableDocuments = Boolean.TRUE;
                        break;
                    case REMOVED:
                        changes = Boolean.TRUE;
                        break;
                    case NONE:
                        publishableDocuments = Boolean.TRUE;
                        break;
                    default:
                        throw Assert.createUnreachable("UNKNOWN DOCUMENT STATE");
                    }
                }
            }
        }
        if (!publishableDocuments) {
            browser.displayErrorDialog("ErrorNoDocumentToPublish",
                    new Object[] {container.getName()});
        } else if (!changes) {
            /* in this case, publish the most recent version. */
            long versionId = getContainerModel().readLatestVersion(containerId).getVersionId();
            browser.displayPublishContainerDialog(containerId, versionId);
        } else if ((null == contactsIn || contactsIn.isEmpty()) &&
            (null == teamMembersIn || teamMembersIn.isEmpty())) {
            // TODO raymond@thinkparity.com - Adjust such that the list input is never null 
            // launch publish dialog
            browser.displayPublishContainerDialog(containerId);
        } else {
            final Profile profile = getProfileModel().read();
            final ArrayList<TeamMember> teamMembers = new ArrayList<TeamMember>();
            final ArrayList<Contact> contacts = new ArrayList<Contact>();
            // build team members list, minus the current user
            for (final User teamMemberIn : teamMembersIn) {
                if (!teamMemberIn.getId().equals(profile.getId())) {
                    teamMembers.add((TeamMember)teamMemberIn);
                }
            }
            // build contacts list, minus any overlap with team members
            for (final User contactIn : contactsIn) {
                Boolean found = Boolean.FALSE;
                for (final TeamMember teamMember : teamMembers) {
                    if (teamMember.getId().equals(contactIn.getId())) {
                        found = Boolean.TRUE;
                        break;
                    }
                }
                if (!found) {
                    contacts.add((Contact) contactIn);
                }
            }
            final ThinkParitySwingMonitor monitor = (ThinkParitySwingMonitor) data.get(DataKey.MONITOR);
            invoke(monitor, comment, contacts, container, teamMembers);
        }
	}

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#retryInvokeAction()
     *
     */
    @Override
    public void retryInvokeAction() {
        invoke(monitor, comment, contacts, container, teamMembers);
    }

	/**
     * Invoke publish.
     * 
     * @param worker
     *            A <code>ThinkParitySwingWorker</code>.
     */
    private void invoke(final ThinkParitySwingMonitor monitor,
            final String comment, final List<Contact> contacts,
            final Container container, final List<TeamMember> teamMembers) {
        this.monitor = monitor;
        this.comment = comment;
        this.contacts = contacts;
        this.container = container;
        this.teamMembers = teamMembers;
        final ThinkParitySwingWorker worker = new PublishWorker(this,
                    comment, contacts, container, teamMembers);
        worker.setMonitor(monitor);
        worker.start();
    }

    /** Data keys. */
	public enum DataKey {
        COMMENT, CONTACTS, CONTAINER_ID, MONITOR, TEAM_MEMBERS
    }

    /** A publish action worker object. */
    private static class PublishWorker extends ThinkParitySwingWorker<Publish> {
        private final ArtifactModel artifactModel;
        private final String comment;
        private final List<Contact> contacts;
        private final Container container;
        private final ContainerModel containerModel;
        private ProcessMonitor publishMonitor;
        private final List<TeamMember> teamMembers;
        private PublishWorker(final Publish publish, final String comment,
                final List<Contact> contacts, final Container container,
                final List<TeamMember> teamMembers) {
            super(publish);
            this.artifactModel = publish.getArtifactModel();
            this.comment = comment;
            this.contacts = contacts;
            this.container = container;
            this.containerModel = publish.getContainerModel();
            this.teamMembers = teamMembers;
            this.publishMonitor = newPublishMonitor();
        }
        @Override
        public Object construct() {
            try {
                containerModel.saveDraft(container.getId());
            } catch (final CannotLockException clx) {
                monitor.reset();
                publishMonitor = newPublishMonitor();
                action.browser.retry(action, container.getName());
            }
            try {
                containerModel.publish(publishMonitor, container.getId(), comment,
                        contacts, teamMembers);
            } catch (final CannotLockException clx) {
                try {
                    containerModel.restoreDraft(container.getId());
                } catch (final CannotLockException clx2) {}

                monitor.reset();
                publishMonitor = newPublishMonitor();
                action.browser.retry(action, container.getName());
            }
            artifactModel.applyFlagSeen(container.getId());
            return containerModel.readLatestVersion(container.getId());
        }
        /**
         * Create a new publish monitor.
         * 
         * @return A <code>PublishMonitor</code>.
         */
        private ProcessMonitor newPublishMonitor() {
            return new ProcessAdapter() {
                private Integer stepIndex;
                private Integer steps;
                @Override
                public void determineSteps(final Integer steps) {
                    this.stepIndex = 0;
                    this.steps = steps;
                    monitor.setSteps(steps);
                    monitor.setStep(stepIndex);
                }
                @Override
                public void beginProcess() {
                    monitor.monitor();
                }
                @Override
                public void endProcess() {
                    monitor.complete();
                }
                @Override
                public void beginStep(final Step step,
                        final Object data) {
                    if (null != steps && steps.intValue() > 0) {
                        if (PublishStep.UPLOAD_STREAM == step)
                            monitor.setStep(stepIndex, (String) data);
                        else
                            monitor.setStep(stepIndex);
                    }
                }
                @Override
                public void endStep(final Step step) {
                    if (null != steps && steps.intValue() > 0) {
                        stepIndex++;
                        monitor.setStep(stepIndex);
                    }
                }
            };
        }
    }
}
