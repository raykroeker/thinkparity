package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactType;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerArtifactPublishedEvent extends XMPPEvent {

    /** The checksum. */
    private String artifactChecksum;

    /** The artifact count. */
    private Integer artifactCount;

    /** The artifact index. */
    private Integer artifactIndex;

    /** The artifact name. */
    private String artifactName;

    /** The artifact steam id <code>String</code>. */
    private String artifactStreamId;

    /** The type. */
    private ArtifactType artifactType;

    /** The unique id. */
    private UUID artifactUniqueId;

    /** The version id. */
    private Long artifactVersionId;

    /** The container name. */
    private String name;

    /** By whom the artifact was published. */
    private JabberId publishedBy;

    /** When the artifact was published. */
    private Calendar publishedOn;

    /** The container unique id. */
    private UUID uniqueId;

    /** The container version id. */
    private Long versionId;

    /**
     * Create ContainerArtifactPublishedEvent.
     * 
     */
    public ContainerArtifactPublishedEvent() {
        super();
    }

    /**
     * Obtain artifactChecksum.
     *
     * @return A String.
     */
    public String getArtifactChecksum() {
        return artifactChecksum;
    }

    /**
     * Obtain artifactCount.
     *
     * @return A Integer.
     */
    public Integer getArtifactCount() {
        return artifactCount;
    }

    /**
     * Obtain artifactIndex.
     *
     * @return A Integer.
     */
    public Integer getArtifactIndex() {
        return artifactIndex;
    }

    /**
     * Obtain artifactName.
     *
     * @return A String.
     */
    public String getArtifactName() {
        return artifactName;
    }

    /**
     * Obtain artifactStreamId.
     *
     * @return A String.
     */
    public String getArtifactStreamId() {
        return artifactStreamId;
    }

    /**
     * Obtain artifactType.
     *
     * @return A ArtifactType.
     */
    public ArtifactType getArtifactType() {
        return artifactType;
    }

    /**
     * Obtain artifactUniqueId.
     *
     * @return A UUID.
     */
    public UUID getArtifactUniqueId() {
        return artifactUniqueId;
    }

    /**
     * Obtain artifactVersionId.
     *
     * @return A Long.
     */
    public Long getArtifactVersionId() {
        return artifactVersionId;
    }

    /**
     * Obtain name.
     *
     * @return A String.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtain publishedBy.
     *
     * @return A JabberId.
     */
    public JabberId getPublishedBy() {
        return publishedBy;
    }

    /**
     * Obtain publishedOn.
     *
     * @return A Calendar.
     */
    public Calendar getPublishedOn() {
        return publishedOn;
    }

    /**
     * Obtain the container unique id
     * 
     * @return A unique id <code>UUID</code>.
     */
    public UUID getUniqueId() {
        return uniqueId;
    }

    /**
     * Obtain versionId.
     *
     * @return A Long.
     */
    public Long getVersionId() {
        return versionId;
    }

    /**
     * Set artifactChecksum.
     *
     * @param artifactChecksum
     *		A String.
     */
    public void setArtifactChecksum(String artifactChecksum) {
        this.artifactChecksum = artifactChecksum;
    }

    /**
     * Set artifactCount.
     *
     * @param artifactCount
     *		A Integer.
     */
    public void setArtifactCount(Integer containerArtifactCount) {
        this.artifactCount = containerArtifactCount;
    }

    /**
     * Set artifactIndex.
     *
     * @param artifactIndex
     *		A Integer.
     */
    public void setArtifactIndex(Integer containerArtifactIndex) {
        this.artifactIndex = containerArtifactIndex;
    }

    /**
     * Set artifactName.
     *
     * @param artifactName
     *		A String.
     */
    public void setArtifactName(String artifactName) {
        this.artifactName = artifactName;
    }

    /**
     * Set artifactStreamId.
     *
     * @param artifactStreamId
     *		A String.
     */
    public void setArtifactStreamId(String artifactStreamId) {
        this.artifactStreamId = artifactStreamId;
    }

    /**
     * Set artifactType.
     *
     * @param artifactType
     *		A ArtifactType.
     */
    public void setArtifactType(ArtifactType artifactType) {
        this.artifactType = artifactType;
    }

    /**
     * Set artifactUniqueId.
     *
     * @param artifactUniqueId
     *		A UUID.
     */
    public void setArtifactUniqueId(UUID artifactUniqueId) {
        this.artifactUniqueId = artifactUniqueId;
    }

    /**
     * Set artifactVersionId.
     *
     * @param artifactVersionId
     *		A Long.
     */
    public void setArtifactVersionId(Long artifactVersionId) {
        this.artifactVersionId = artifactVersionId;
    }

    /**
     * Set name.
     *
     * @param name
     *		A String.
     */
    public void setName(String containerName) {
        this.name = containerName;
    }

    /**
     * Set publishedBy.
     *
     * @param publishedBy
     *		A JabberId.
     */
    public void setPublishedBy(JabberId publishedBy) {
        this.publishedBy = publishedBy;
    }

    /**
     * Set publishedOn.
     *
     * @param publishedOn
     *		A Calendar.
     */
    public void setPublishedOn(Calendar publishedOn) {
        this.publishedOn = publishedOn;
    }

    /**
     * Set uniqueId.
     *
     * @param uniqueId
     *		A UUID.
     */
    public void setUniqueId(UUID containerUniqueId) {
        this.uniqueId = containerUniqueId;
    }

    /**
     * Set versionId.
     *
     * @param versionId
     *		A Long.
     */
    public void setVersionId(Long containerVersionId) {
        this.versionId = containerVersionId;
    }

    
}