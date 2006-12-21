/*
 * Created On: 13-Jul-06 11:42:46 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.tab.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.Constants.Versioning;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.user.UserModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view.DocumentView;

/**
 * <b>Title:</b>thinkParity Container TabId Provider<br>
 * <b>Description:</b>A thinkParity container tab provider reads from the
 * various thinkParity model interfaces to provide the container tab with its
 * data.
 * 
 * It currently provides both display and non-display data.
 * 
 * @author rob_masako@shaw.ca; raymond@thinkparity.com
 * @version 1.1.2.5
 */
public class ContainerProvider extends CompositeFlatSingleContentProvider {

    /** A thinkParity container interface. */
    private final ContainerModel containerModel;
    
    /** A thinkParity document interface. */
    private final DocumentModel documentModel;

    /** A thinkParity user interface. */
    private final UserModel userModel;

    /**
     * Create ContainerProvider.
     * 
     * @param profile
     *            The local user profile.
     * @param containerModel
     *            A thinkParity container interface.
     * @param documentModel
     *            A thinkParity document interface.
     * @param userModel
     *            A thinkParity user interface.
     */
    public ContainerProvider(final Profile profile,
            final ContainerModel containerModel,
            final DocumentModel documentModel, final UserModel userModel) {
        super(profile);
        this.containerModel = containerModel;
        this.documentModel = documentModel;
        this.userModel = userModel;
    }

    @Override
	public Object getElement(Integer index, Object input) {
		throw Assert.createNotYetImplemented("ContainerProvider#getElement");
	}

	@Override
	public Object[] getElements(Integer index, Object input) {
		throw Assert.createNotYetImplemented("ContainerProvider#getElements");
	}
    
    /**
     * Determine if the container has been distributed.
     * 
     * @param containerId
     *            A <code>Long</code>.
     * @return True if this container has been distributed; false otherwise.
     */
    public Boolean isDistributed(final Long containerId) {
        return containerModel.isDistributed(containerId);
    }

	/**
     * Determine if the draft document has been modified.
     * 
     * @param documentId
     *            A document id.
     * @return True if the draft document has been modified; false otherwise.
     */
    public Boolean isDraftDocumentModified(final Long documentId) {
        return documentModel.isDraftModified(documentId);
    }
    
    /**
	 * Read a list of containers.
	 * 
	 * @return A list of containers.
	 */
    public List<Container> read() {
    	return containerModel.read();
    }
    
    /**
	 * Read a container.
	 * 
	 * @param containerId
	 *            A container id <code>Long</code>.
	 * @return A <code>Container</code>.
	 */
    public Container read(final Long containerId) {
    	return containerModel.read(containerId);
    }

    /**
     * Read and generate a list of document views for a document version. The
     * document view will consist of a document version; its delta as well as
     * the date it was first published.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List</code> of <code>DocumentView</code>.
     */
    public List<DocumentView> readDocumentViews(final Long containerId,
            final Long versionId) {
        final Map<DocumentVersion, Delta> versions;
        final ContainerVersion previousVersion = containerModel.readPreviousVersion(containerId, versionId);
        if (null == previousVersion) {
            versions = containerModel.readDocumentVersionDeltas(containerId,
                    versionId);
        } else {
            versions = containerModel.readDocumentVersionDeltas(containerId,
                    versionId, previousVersion.getVersionId());
        }
        final List<DocumentView> views = new ArrayList<DocumentView>(versions.size());
        DocumentVersion firstVersion;
        DocumentView view;
        for (final Entry<DocumentVersion, Delta> entry : versions.entrySet()) {
            firstVersion = documentModel.readVersion(
                    entry.getKey().getArtifactId(), Versioning.START);

            view = new DocumentView();
            view.setDelta(entry.getValue());
            view.setVersion(entry.getKey());
            view.setFirstPublishedOn(null == firstVersion
                    ? null : firstVersion.getCreatedOn());
            views.add(view);
        }
        return views;
    }

    /**
	 * Read a container draft.
	 * 
	 * @param containerId
	 *            A container id <code>Long</code>.
	 * @return A <code>ContainerDraft</code>.
	 */
    public ContainerDraft readDraft(final Long containerId) {
    	return containerModel.readDraft(containerId);
    } 
    
    /**
     * Read a the latest container version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A <code>ContainerVersion</code>.
     */
    public ContainerVersion readLatestVersion(final Long containerId) {
        return containerModel.readLatestVersion(containerId);
    }
    
    /**
     * Read the profile.
     * 
     * @return A <code>Profile</code>.  
     */
    public Profile readProfile() {
        return profile;
    }

    /**
     * Read the published to user list.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A <code>Map&lt;User, ArtifactReceipt&gt;</code>.
     */
    public Map<User, ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId) {
        return containerModel.readPublishedTo(containerId, versionId);
    }

    /**
     * Read the team.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A list of <code>TeamMember</code>s.
     */
    public List<TeamMember> readTeam(final Long containerId) {
        return containerModel.readTeam(containerId);
    }

    /**
     * Read a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    public User readUser(final JabberId userId) {
        return userModel.read(userId);
    }

    /**
     * Read a list of container versions.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<ContainerVersion> readVersions(final Long containerId) {
        return containerModel.readVersions(containerId);
    }
    
    /**
	 * Search for containers.
	 * 
	 * @param expression
	 *            A search expression <code>String</code>.
	 * @return A <code>List&lt;Long&gt;</code>.
	 */
    public List<Long> search(final String expression) {
    	return containerModel.search(expression);
    }
}
