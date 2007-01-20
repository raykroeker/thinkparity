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

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingWorker;
import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.monitor.PublishMonitor;
import com.thinkparity.ophelia.model.container.monitor.PublishStage;

/**
 * Publish a document.  This will send a given document version to
 * every member of the team.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Publish extends AbstractAction {

	/** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

    /** The browser application. */
    private final Browser browser;

	/** Create Publish. */
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
        
        // Check there are documents with changes to publish.
        Boolean publishableDocuments = Boolean.FALSE;
        Boolean changes = Boolean.FALSE;
        List<Document> documents = Collections.emptyList();
        final Container container = containerModel.read(containerId);
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
            // In this case, publish the most recent version.
            // After publishing we want to delete the draft.
            long versionId = getContainerModel().readLatestVersion(containerId).getVersionId();
            browser.displayPublishContainerDialog(containerId, versionId, Boolean.TRUE);
        } else if ((null == contactsIn || contactsIn.isEmpty()) &&
            (null == teamMembersIn || teamMembersIn.isEmpty())) {
            // TODO raymond@thinkparity.com - Adjust such that the list input is never null 
            // Launch publish dialog
            browser.displayPublishContainerDialog(containerId);
        } else {
            final Profile profile = getProfileModel().read();
            final ArrayList<TeamMember> teamMembers = new ArrayList<TeamMember>();
            final ArrayList<Contact> contacts = new ArrayList<Contact>();
            
            // Build team members list, minus the current user
            for (final User teamMemberIn : teamMembersIn) {
                if (!teamMemberIn.getId().equals(profile.getId())) {
                    teamMembers.add((TeamMember)teamMemberIn);
                }
            }
            
            // Build contacts list, minus any overlap with team members
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
            final ThinkParitySwingMonitor monitor =
                (ThinkParitySwingMonitor) data.get(DataKey.MONITOR);
            final ThinkParitySwingWorker worker = new PublishWorker(this,
                    comment, contacts, containerId, teamMembers);
            worker.setMonitor(monitor);
            worker.start();
        }
	}

	/** Data keys. */
	public enum DataKey {
        COMMENT, CONTACTS, CONTAINER_ID, MONITOR, TEAM_MEMBERS
    }

    /** A publish action worker object. */
    private static class PublishWorker extends ThinkParitySwingWorker {
        private final ArtifactModel artifactModel;
        private final String comment;
        private final List<Contact> contacts;
        private final Long containerId;
        private final ContainerModel containerModel;
        private final PublishMonitor publishMonitor;
        private final List<TeamMember> teamMembers;
        private PublishWorker(final Publish publish, final String comment,
                final List<Contact> contacts, final Long containerId,
                final List<TeamMember> teamMembers) {
            super(publish);
            this.artifactModel = publish.getArtifactModel();
            this.comment = comment;
            this.contacts = contacts;
            this.containerId = containerId;
            this.containerModel = publish.getContainerModel();
            this.teamMembers = teamMembers;
            this.publishMonitor = new PublishMonitor() {
                private Integer stageIndex;
                private Integer stages;
                public void determine(final Integer stages) {
                    this.stageIndex = 0;
                    this.stages = stages;
                    monitor.setSteps(stages);
                    monitor.setStep(stageIndex);
                }
                public void processBegin() {
                    monitor.monitor();
                }
                public void processEnd() {}
                public void stageBegin(final PublishStage stage,
                        final Object data) {
                    if (null != stages && stages.intValue() > 0) {
                        if (PublishStage.UploadStream == stage)
                            monitor.setStep(stageIndex, (String) data);
                        else
                            monitor.setStep(stageIndex);
                    }
                }
                public void stageEnd(final PublishStage stage) {
                    if (null != stages && stages.intValue() > 0) {
                        stageIndex++;
                        monitor.setStep(stageIndex);
                    }
                }
            };
        }
        @Override
        public Object construct() {
            containerModel.publish(publishMonitor, containerId, comment,
                    contacts, teamMembers);
            artifactModel.applyFlagSeen(containerId);
            return containerModel.readLatestVersion(containerId);
        }
    }
}
