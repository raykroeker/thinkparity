/*
 * Jan 10, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.monitor.PublishMonitor;
import com.thinkparity.ophelia.model.container.monitor.PublishStage;
import com.thinkparity.ophelia.model.user.TeamMember;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingWorker;

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
        
        if ((null == contactsIn || contactsIn.isEmpty()) &&
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
            browser.executeContainerWorker(containerId, new PublishWorker(this,
                    comment, contacts, containerId, teamMembers));
        }
	}

	/**
	 * The key used to set\get the data.
	 * 
	 * @see Data
	 */
	public enum DataKey { COMMENT, CONTACTS, CONTAINER_ID, TEAM_MEMBERS }

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
                private final long timeDuration = 1 * 1000;
                private boolean monitoring;
                private long timeStart;
                private long timeEnd;
                public void initialize(final Integer stages) {
                    this.monitoring = false;
                    this.stageIndex = 0;
                    this.stages = stages;
                }
                public void processBegin() {
                    this.timeStart = System.currentTimeMillis();
                }
                public void processEnd() {
                    this.timeEnd = System.currentTimeMillis();
                }
                public void stageBegin(final PublishStage stage) {
                    final long timeNow = System.currentTimeMillis();
                    if (!monitoring && timeNow > timeStart + timeDuration) {
                        monitor.setSteps(stages);
                        monitor.setStep(stageIndex);
                        monitor.monitor();
                    }
                }
                public void stageEnd(final PublishStage stage) {
                    stageIndex++;
                    monitor.setStep(stageIndex);
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
