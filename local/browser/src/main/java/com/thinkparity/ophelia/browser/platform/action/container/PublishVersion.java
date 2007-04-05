/*
 * Created On: Aug 23, 2006 5:59:44 PM
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.monitor.PublishStep;
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
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PublishVersion extends AbstractBrowserAction {

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
        final ContainerVersion version = getContainerModel().readVersion(
                containerId, versionId);
        final List<User> contactsIn = getDataUsers(data, DataKey.CONTACTS);
        final List<User> teamMembersIn = getDataUsers(data, DataKey.TEAM_MEMBERS);

        if ((null == contactsIn || contactsIn.isEmpty()) &&
            (null == teamMembersIn || teamMembersIn.isEmpty())) {
                // Launch publish dialog
                browser.displayPublishContainerDialog(containerId, versionId);
        } else {
            final Profile profile = getProfileModel().read();
            final List<EMail> emails = Collections.emptyList();
            final List<Contact> contacts = new ArrayList<Contact>();
            final List<TeamMember> teamMembers = new ArrayList<TeamMember>();
            
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
            final ThinkParitySwingWorker worker = new PublishVersionWorker(
                    this, version, emails, contacts, teamMembers);
            worker.setMonitor(monitor);
            worker.start();
        }
    }
    
    /** Data keys. */
    public enum DataKey {
        CONTACTS, CONTAINER_ID, EMAILS, MONITOR, TEAM_MEMBERS, VERSION_ID
    }
    
    /** A publish action worker object. */
    private static class PublishVersionWorker extends ThinkParitySwingWorker<PublishVersion> {
        private final ArtifactModel artifactModel;
        private final List<Contact> contacts;
        private final ContainerModel containerModel;
        private final List<EMail> emails;
        private final ProcessMonitor publishMonitor;
        private final List<TeamMember> teamMembers;
        private final ContainerVersion version;
        private PublishVersionWorker(final PublishVersion publishVersion,
                final ContainerVersion version, final List<EMail> emails,
                final List<Contact> contacts, final List<TeamMember> teamMembers) {
            super(publishVersion);
            this.version = version;
            this.emails = emails;
            this.contacts = contacts;
            this.teamMembers = teamMembers;

            this.artifactModel = publishVersion.getArtifactModel();
            this.containerModel = publishVersion.getContainerModel();

            this.publishMonitor = newPublishVersionMonitor();
        }
        @Override
        public Object construct() {
            containerModel.publishVersion(publishMonitor,
                    version.getArtifactId(), version.getVersionId(),
                    emails, contacts, teamMembers);
            artifactModel.applyFlagSeen(version.getArtifactId());
            return version;
        }
        /**
         * Create a new publish version monitor.
         * 
         * @return A <code>ProcessMonitor</code>.
         */
        private ProcessMonitor newPublishVersionMonitor() {
            return new ProcessAdapter() {
                private Integer stepIndex;
                private Integer steps;
                @Override
                public void beginProcess() {
                    monitor.monitor();
                }
                @Override
                public void beginStep(final Step step,
                        final Object data) {
                    if (null != steps && steps.intValue() > 0) {
                        if (PublishStep.UPLOAD_STREAM == step) {
                            monitor.setStep(stepIndex, action.getString("ProgressUploadStream", new Object[] {data}));
                        } else if (PublishStep.PUBLISH == step) {
                            monitor.setStep(stepIndex, action.getString("ProgressPublish"));
                        }
                    }
                }
                @Override
                public void determineSteps(final Integer steps) {
                    this.stepIndex = 0;
                    this.steps = steps;
                    monitor.setSteps(steps);
                    monitor.setStep(stepIndex);
                }
                @Override
                public void endProcess() {
                    monitor.complete();
                }
                @Override
                public void endStep(final Step step) {
                    if (PublishStep.PUBLISH == step) {
                        // we're done
                        stepIndex = steps - 1;
                        monitor.setStep(stepIndex);
                    } else if (null != steps && steps.intValue() > 0) {
                        stepIndex++;
                        monitor.setStep(stepIndex);
                    }
                }
            };
        }
    }
}
