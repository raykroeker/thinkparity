/*
 * Created On: Aug 23, 2006 5:59:44 PM
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingWorker;
import com.thinkparity.ophelia.browser.platform.action.container.Publish.DataKey;
import com.thinkparity.ophelia.browser.platform.action.container.Publish.PublishWorker;
import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.monitor.PublishMonitor;
import com.thinkparity.ophelia.model.container.monitor.PublishStage;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PublishVersion extends AbstractAction {

    /** A thinkParity browser application. */
    private final Browser browser;

    /**
     * Create PublishVersion.
     * 
     * @param browser
     *            A thinkParity browser application.
     */
    public PublishVersion(final Browser browser) {
        super(ActionId.CONTAINER_PUBLISH_VERSION);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Long versionId = (Long) data.get(DataKey.VERSION_ID);
        final List<User> contactsIn = getDataUsers(data, DataKey.CONTACTS);
        final List<User> teamMembersIn = getDataUsers(data, DataKey.TEAM_MEMBERS);
        final String comment = (String) data.get(DataKey.COMMENT);
        
        if (((null==contactsIn) || contactsIn.isEmpty()) &&
            ((null==teamMembersIn) || teamMembersIn.isEmpty())) {
                // Launch publish dialog
                browser.displayPublishContainerDialog(containerId, versionId);
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
            final ThinkParitySwingWorker worker = new PublishVersionWorker(this,
                    comment, contacts, containerId, versionId, teamMembers);
            worker.setMonitor(monitor);
            worker.start();
        }
    }
    
    /** Data keys. */
    public enum DataKey {
        COMMENT, CONTACTS, CONTAINER_ID, MONITOR, TEAM_MEMBERS, VERSION_ID
    }
    
    /** A publish action worker object. */
    private static class PublishVersionWorker extends ThinkParitySwingWorker {
        private final ArtifactModel artifactModel;
        private final String comment;
        private final List<Contact> contacts;
        private final Long containerId;
        private final Long versionId;
        private final ContainerModel containerModel;
        private final PublishMonitor publishMonitor;
        private final List<TeamMember> teamMembers;
        private PublishVersionWorker(final PublishVersion publishVersion, final String comment,
                final List<Contact> contacts, final Long containerId, final Long versionId,
                final List<TeamMember> teamMembers) {
            super(publishVersion);
            this.artifactModel = publishVersion.getArtifactModel();
            this.comment = comment;
            this.contacts = contacts;
            this.containerId = containerId;
            this.versionId = versionId;
            this.containerModel = publishVersion.getContainerModel();
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
            containerModel.publishVersion(publishMonitor, containerId, versionId, comment,
                    contacts, teamMembers);
            artifactModel.applyFlagSeen(containerId);
            return containerModel.readLatestVersion(containerId);
        }
    }
}
