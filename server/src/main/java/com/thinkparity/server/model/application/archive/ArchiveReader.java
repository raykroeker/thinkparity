/*
 * Created On: Sep 16, 2006 12:41:44 PM
 */
package com.thinkparity.desdemona.model.archive;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;

import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ArchiveReader<T extends Artifact, U extends ArtifactVersion> {

    /** A thinkParity client artifact interface. */
    protected final InternalArtifactModel artifactModel;

    /** An artifact archive filter. */
    protected final Filter<Artifact> filter;

    /** A client model factory. */
    private final ClientModelFactory modelFactory;

    /** Create ArchiveReader. */
    protected ArchiveReader(final ClientModelFactory modelFactory) {
        super();
        this.filter = new Filter<Artifact>() {
            public Boolean doFilter(final Artifact o) {
                return !o.contains(ArtifactFlag.ARCHIVED);
            }
        };
        this.modelFactory = modelFactory;
        this.artifactModel = modelFactory.getArtifactModel(getClass());
    }

    /**
     * Read a list of archived artifacts.
     * 
     * @return A <code>List&lt;T&gt;</code>.
     */
    public abstract List<T> read();

    /**
     * Read an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A <code>T</code>.
     */
    public abstract T read(final UUID uniqueId);

    /**
     * Read a list of archived artifact versions.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A <code>List&lt;U&gt;</code>.
     */
    public abstract List<U> readVersions(final UUID uniqueId);

    /**
     * Obtain a thinkParity client container interface.
     * 
     * @return A thinkParity client container interface.
     */
    protected InternalContainerModel getContainerModel() {
        return modelFactory.getContainerModel(getClass());
    }

    /**
     * Obtain a thinkParity client document interface.
     * 
     * @return A thinkParity client document interface.
     */
    protected InternalDocumentModel getDocumentModel() {
        return modelFactory.getDocumentModel(getClass());
    }

    /**
     * Read the artifact id for an archived artifact.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return An artifact id <code>Long</code>.
     */
    protected Long readArchivedArtifactId(final UUID uniqueId) {
        final Long artifactId = readArtifactId(uniqueId);
        Assert.assertTrue(artifactModel.isFlagApplied(artifactId,
                ArtifactFlag.ARCHIVED), "Artifact {0} has not been archived.",
                uniqueId);
        return artifactId;
    }

    /**
     * Read an artifact id.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return An artifact id <code>Long</code>.
     */
    protected Long readArtifactId(final UUID uniqueId) {
        return artifactModel.readId(uniqueId);
    }
}
