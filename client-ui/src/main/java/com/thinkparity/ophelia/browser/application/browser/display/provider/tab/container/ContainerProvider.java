/*
 * Created On: 13-Jul-06 11:42:46 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.tab.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta;
import com.thinkparity.codebase.model.container.ContainerVersionDelta;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.user.UserModel;

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
     * Read the documents.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List&lt;DocumentVersion&gt;</code>.
     */
    public List<DocumentVersion> readDocumentVersions(final Long containerId, final Long versionId) {
        return containerModel.readDocumentVersions(containerId, versionId);
    }

    /**
	 * Read the documents and associated delta.
	 * 
	 * @param containerId
	 *            A container id <code>Long</code>.
	 * @param versionId
	 *            A container version id <code>Long</code>.
     * @return A <code>Map&lt;DocumentVersion, ContainerVersionArtifactVersionDelta&gt;</code>.
	 */
    public Map<DocumentVersion, ContainerVersionArtifactVersionDelta> readDocumentVersionsWithDelta(final Long containerId,
            final Long versionId) {
        
        // Find the delta comparing this versionId to the next one.
        final Long nextVersionId = getNextVersion(containerId, versionId);
        final ContainerVersionArtifactVersionDelta delta = getDelta(containerId, versionId, nextVersionId);
        
        // Get the documents and build a map.
        final List<DocumentVersion> documents = containerModel.readDocumentVersions(containerId, versionId);
        final Map<DocumentVersion, ContainerVersionArtifactVersionDelta> documentVersions =
            new HashMap<DocumentVersion, ContainerVersionArtifactVersionDelta>(documents.size(), 1.0F);
        for (final DocumentVersion document : documents) {
            documentVersions.put(document, delta);
        }
        int x = 0;
        
    	return documentVersions;
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
     * Get the next version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return The next version id.
     */
    private Long getNextVersion(final Long containerId, final Long versionId) {
        Long nextVersionId = null;
        final List<ContainerVersion> versions = containerModel.readVersions(containerId);
        for (int index = 0; index < versions.size()-1; index++) {
            if (versions.get(index).getVersionId().equals(versionId)) {
                nextVersionId = versions.get(index+1).getVersionId();
                break;
            }
        }
        
        return nextVersionId;
    }
    
    /**
     * Get a delta.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param compareVersionId
     *            A compare version id <code>Long</code>.
     * @return The <code>ContainerVersionArtifactVersionDelta</code>.         
     */
    private ContainerVersionArtifactVersionDelta getDelta(final Long containerId,
            final Long versionId, final Long compareVersionId) {
        ContainerVersionArtifactVersionDelta desiredDelta = null;
        if (null != compareVersionId) {
            final ContainerVersionDelta versionDelta = containerModel.readDelta(containerId, versionId, compareVersionId);
            if (null != versionDelta) {
                final List<ContainerVersionArtifactVersionDelta> deltas = versionDelta.getDeltas();
                for (final ContainerVersionArtifactVersionDelta delta : deltas) {
                    if (delta.getArtifactVersionId().equals(versionId)) {
                        desiredDelta = delta;
                        break;                   
                    }
                }
            }
        }
        
        return desiredDelta;
    }
}
