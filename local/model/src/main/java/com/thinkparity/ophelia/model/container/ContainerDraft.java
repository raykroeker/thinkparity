/*
 * Created On: Aug 2, 2006 2:53:41 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.user.TeamMember;

/**
 * <b>Title:</b>thinkParity Container Draft<br>
 * <b>Description:</b>A <code>ContainerDraft</code> contains a list of
 * artifacts. They represent "working" or "draft" iterations of an artifact.
 * They can be discarded or turned into actual versions.</br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.9
 */
public class ContainerDraft {

    /** A list of draft <code>Artifact</code>. */
    private final List<Artifact> artifacts;

    /** A list of the draft <code>Artifact</code>'s states. */
    private final Map<Long, ArtifactState> artifactsState;

    /** A comment <code>String</code>. */
    private String comment;

    /** The container the draft belongs to. */
    private Long containerId;

    /**
     * A <code>Boolean</code> indicator as to whether or not the draft is
     * local.
     */ 
    private Boolean local;

    /** The <code>TeamMember</code> the draft belongs to. */
    private TeamMember owner;

    /**
     * Create ContainerDraft.
     *
     */
    public ContainerDraft() {
        super();
        this.artifacts = new ArrayList<Artifact>();
        this.artifactsState = new HashMap<Long, ArtifactState>(7, 0.75F);
    }

    /**
     * Add a document to the draft.
     * 
     * @param document
     *            A document.
     * @return True if the list of documents is modified.
     */
    public boolean addDocument(final Document document) {
        if (artifacts.contains(document)) {
            return false;
        } else {
            return artifacts.add(document);
        }
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof ContainerDraft) {
            return ((ContainerDraft) obj).containerId.equals(containerId);
        }
        return false;
    }

    /**
     * Obtain an artifact from the list.
     * 
     * @param artifactId
     *            An artifact id.
     * @return An artifact; or null if there is no artifact.
     */
    public Artifact getArtifact(final Long artifactId) {
        if (containsArtifact(artifactId)) {
            return artifacts.get(indexOfArtifact(artifactId));
        } else {
            return null;
        }
    }

    /**
     * Obtain a list of all of the artifacts in the draft.
     * 
     * @return A list of artifacts.
     */
    public List<Artifact> getArtifacts() {
        final List<Artifact> artifacts = new ArrayList<Artifact>(this.artifacts.size());
        artifacts.addAll(this.artifacts);
        return Collections.unmodifiableList(artifacts);
    }

    /**
     * Obtain comment.
     *
     * @return A String.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Obtain the id
     *
     * @return The Long.
     */
    public Long getContainerId() { return containerId; }

    /**
     * Obtain a document.
     * 
     * @param documentId
     *            A document id.
     * @return A document; or null if the document does not exist in the draft.
     */
    public Document getDocument(final Long documentId) {
        return (Document) getArtifact(documentId);
    }

    /**
     * Obtain the document count for the draft.
     *
     * @return A document count <code>Integer</code>.
     */
    public Integer getDocumentCount() {
       return getDocuments().size(); 
    }

    /**
     * Obtain a list of documents in the draft.
     * 
     * @return A list of documents.
     */
    public List<Document> getDocuments() {
        final List<Document> documents = new ArrayList<Document>(artifacts.size());
        for (final Artifact artifact : artifacts) {
            if (ArtifactType.DOCUMENT == artifact.getType()) {
                documents.add((Document) artifact);
            }
        }
        return Collections.unmodifiableList(documents);
    }

    /**
     * Obtain the <code>TeamMember</code> by whom the draft is being modified.
     * 
     * @return A team member.
     */
    public TeamMember getOwner() {
        return owner;
    }

    /**
     * Obtain the artifact's state.
     * 
     * @param artifact
     *            The artifact.
     * @return The artifact state.
     * @see ContainerDraft#getState(Long)
     */
    public ArtifactState getState(final Artifact artifact) {
        return artifactsState.get(artifact.getId());
    }

    /**
     * Obtain the artifact's state.
     * 
     * @param artifactId
     *            The artifact id.
     * @return The artifact state.
     */
    public ArtifactState getState(final Long artifactId) {
        return artifactsState.get(artifactId);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() { return containerId.hashCode(); }

    /**
     * Determine whether or not the draft is local.
     * 
     * @return True if the draft is local.
     */
    public Boolean isLocal() {
        return local;
    }

    /**
     * Set the state of an artifact.
     * 
     * @param artifact
     *            An artifact.
     * @param state
     *            An artifact state.
     * @return The previous value of the artifact state.
     */
    public ArtifactState putState(final Artifact artifact, final ArtifactState state) {
        assertContainsArtifact(artifact.getId());
        return this.artifactsState.put(artifact.getId(), state);
    }

    /**
     * Remove a list of documents.
     * 
     * @param documents
     *            A list of documents.
     * @return True if the list is modified as a result.
     */
    public boolean removeAllDocuments(final List<Document> documents) {
        final int originalSize = this.artifacts.size();
        for(final Document document : documents)
            removeDocument(document);
        return originalSize == this.artifacts.size();
    }

    /**
     * Remove a document from the draft.
     * 
     * @param document
     *            A document.
     * @return True if the document list is modified.
     */
    public boolean removeDocument(final Document document) {
        if(!containsDocument(document.getId())) { return false; }
        else {
            artifactsState.remove(document.getId());
            return artifacts.remove(indexOfDocument(document.getId()));
        }
    }

    /**
     * Set comment.
     *
     * @param comment
     *		A String.
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * Set the container id.
     * 
     * @param containerId
     *            A container id.
     */
    public void setContainerId(final Long containerId) {
        this.containerId = containerId;
    }

    /**
     * Set the documents.
     * 
     * @param documents
     *            A <code>Document</code> <code>List</code>.
     */
    public void setDocuments(final List<Document> documents) {
        artifacts.clear();
        artifacts.addAll(documents);
    }

    /**
     * Set local.
     *
     * @param local
     *		A <code>Boolean</code> local indicator.
     */
    public void setLocal(final Boolean local) {
        this.local = local;
    }

    /**
     * Set the <code>TeamMember</code> owner.
     * 
     * @param owner
     *            The draft owner.
     */
    public void setOwner(final TeamMember owner) {
        this.owner = owner;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append("/").append(containerId)
                .toString();
    }

    /**
     * Obtain artifactsState.
     *
     * @return A Map<Long,ArtifactState>.
     */
    Map<Long, ArtifactState> getArtifactsState() {
        return Collections.unmodifiableMap(artifactsState);
    }

    /**
     * Assert that the underlying artifact list contains the artifact.
     * 
     * @param artifactId
     *            An artifact id.
     */
    private void assertContainsArtifact(final Long artifactId) {
        Assert.assertTrue(
                "ARTIFACT LIST DOES NOT CONTAIN ARTIFACT",
                containsArtifact(artifactId));
    }

    /**
     * Determine if the artifact list contains an artifact.
     * 
     * @param artifactId
     *            An artifact id.
     * @return True if the artifact exists in the artifact list.
     */
    private Boolean containsArtifact(final Long artifactId) {
        if (-1 == indexOfArtifact(artifactId)) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    /**
     * Determine if the artifact list contains a document.
     * 
     * @param documentId
     *            A document id.
     * @return True if the document exists in the artifact list.
     */
    private Boolean containsDocument(final Long documentId) {
        return containsArtifact(documentId);
    }

    /**
     * Obtain the index of an artifact in the artifacts list.
     * 
     * @param artifactId
     *            An artifact id.
     * @return The index of the artifact in the artifact list.
     */
    private Integer indexOfArtifact(final Long artifactId) {
        for (int i = 0; i < artifacts.size(); i++) {
            if (artifacts.get(i).getId().equals(artifactId)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Obtain the index of a document in the artifacts list.
     * 
     * @param documentId
     *            A document id.
     * @return The index of the document in the artifact list.
     */
    private Integer indexOfDocument(final Long documentId) {
        return indexOfArtifact(documentId);
    }

    /**
     * An enumeration of states for an <code>Artifact</code> in the
     * <code>ContainerDraft</code>.
     */
    public enum ArtifactState { ADDED, MODIFIED, NONE, REMOVED }
}
