/*
 * Created On: Aug 2, 2006 2:53:41 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.user.TeamMember;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerDraft {

    /** A map of artifact ids to their respective states. */
    private final Map<Long, ArtifactState> artifactsState;

    /** The container id. */
    private Long containerId;

    /** The documents. */
    private final List<Document> documents;

    /** The draft owner. */
    private TeamMember owner;

    /** Create ContainerDraft. */
    public ContainerDraft() {
        super();
        this.documents = new ArrayList<Document>();
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
        if(documents.contains(document)) { return false; }
        else { return documents.add(document); }
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
     * Obtain a list of all of the artifacts in the draft.
     * 
     * @return A list of artifacts.
     */
    public List<Artifact> getArtifacts() {
        final List<Artifact> artifacts = new ArrayList<Artifact>(documents.size());
        artifacts.addAll(documents);
        return Collections.unmodifiableList(artifacts);
    }

    /**
     * Obtain the artifact's state.
     * 
     * @param artifactId
     *            The artifact id.
     * @return The artifact state.
     */
    public ArtifactState getState(final Artifact artifact) {
        return artifactsState.get(artifact.getId());
    }

    /**
     * Obtain the id
     *
     * @return The Long.
     */
    public Long getContainerId() { return containerId; }

    public Document getDocument(final Long documentId) {
        throw Assert.createNotYetImplemented("ContainerDraft#getDocument");
    }

    /**
     * Obtain a list of documents in the draft.
     * 
     * @return A list of documents.
     */
    public List<Document> getDocuments() {
        return Collections.unmodifiableList(documents);
    }

    public TeamMember getOwner() { return owner; }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() { return containerId.hashCode(); }

    public ArtifactState putState(final Artifact artifact, final ArtifactState state) {
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
        final int originalSize = this.documents.size();
        for(final Document document : documents)
            removeDocument(document);
        return originalSize == this.documents.size();
    }

    /**
     * Remove a document from the draft.
     * 
     * @param document
     *            A document.
     * @return True if the document list is modified.
     */
    public boolean removeDocument(final Document document) {
        if(!documents.contains(document)) { return false; }
        else {
            artifactsState.remove(document.getId());
            return documents.remove(document);
        }
    }

    /**
     * Set id.
     *
     * @param id The Long.
     */
    public void setContainerId(final Long containerId) {
        this.containerId = containerId;
    }

    public void setOwner(final TeamMember owner) { this.owner = owner; }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append("/").append(containerId)
                .toString();
    }

    public enum ArtifactState { ADDED, NONE, REMOVED }
}
