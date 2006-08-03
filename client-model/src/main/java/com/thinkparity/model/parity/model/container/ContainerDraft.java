/*
 * Created On: Aug 2, 2006 2:53:41 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.document.Document;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerDraft {

    /** The container id. */
    private Long containerId;

    /** The documents. */
    private final List<Document> documents;

    /** A map of artifact ids to their respective states. */
    private final Map<Long, ContainerDraftArtifactState> stateMap;

    /** Create ContainerDraft. */
    public ContainerDraft() {
        super();
        this.documents = new ArrayList<Document>();
        this.stateMap = new HashMap<Long, ContainerDraftArtifactState>(7, 0.75F);
    }

    /**
     * Add all documents.
     * 
     * @param documents
     *            A list of documents.
     * @return True if the list is modified.
     */
    public boolean addAllDocuments(final List<Document> documents) {
        final int originalSize = this.documents.size();
        for(final Document document : documents)
            addDocument(document);
        return originalSize == this.documents.size();
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
    public ContainerDraftArtifactState getArtifactState(final Long artifactId) {
        return stateMap.get(artifactId);
    }

    /**
     * Obtain the id
     *
     * @return The Long.
     */
    public Long getContainerId() { return containerId; }

    /**
     * Obtain a list of documents in the draft.
     * 
     * @return A list of documents.
     */
    public List<Document> getDocuments() {
        return Collections.unmodifiableList(documents);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() { return containerId.hashCode(); }

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
        else { return documents.remove(document); }
    }

    /**
     * Set id.
     *
     * @param id The Long.
     */
    public void setContainerId(final Long containerId) {
        this.containerId = containerId;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer()
                .append(getClass()).append("//").append(containerId)
                .toString();
    }
}
