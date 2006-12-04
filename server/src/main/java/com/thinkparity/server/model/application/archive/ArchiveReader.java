/*
 * Created On: Sep 16, 2006 12:41:44 PM
 */
package com.thinkparity.desdemona.model.archive;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.filter.Filter;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ArchiveReader<T extends Artifact, U extends ArtifactVersion> {

    /**
     * Obtain an empty archive reader.
     * 
     * @param <T>
     *            An archive reader artifact type.
     * @param <U>
     *            An archive reader artifact version type.
     * @return An <code>ArchiveReader&lt;T, U&gt;</code>.
     */
    static <T extends Artifact, U extends ArtifactVersion> ArchiveReader<T, U> emptyReader() {
        return new ArchiveReader<T, U>() {
            @Override
            public List<T> read() {
                return Collections.emptyList();
            }
            @Override
            public T read(final UUID uniqueId) {
                return null;
            }
            @Override
            public Map<User, ArtifactReceipt> readPublishedTo(
                    final UUID uniqueId, final Long versionId) {
                return Collections.emptyMap();
            }
            @Override
            public Map<U, Delta> readVersionDeltas(final UUID uniqueId) {
                return Collections.emptyMap();
            }
            @Override
            public Map<U, Delta> readVersionDeltas(final UUID uniqueId,
                    final Long compareToVersionId) {
                return Collections.emptyMap();
            }
            @Override
            public List<U> readVersions(final UUID uniqueId) {
                return Collections.emptyList();
            }
        };
    }

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
     * Create ArchiveReader. To be used by the empty reader singleton only.
     * 
     */
    private ArchiveReader() {
        super();
        this.filter = null;
        this.modelFactory = null;
        this.artifactModel = null;
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
     * Read the published to users.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @param versionId
     *            An artifact version id <code>Long</code>.
     * @return A list of <code>User</code>s and their
     *         <code>ArtifactReceipt</code>s.
     */
    public abstract Map<User, ArtifactReceipt> readPublishedTo(
            final UUID uniqueId, final Long versionId);

    /**
     * Read a list of archived artifact versions.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A list of <code>U</code> and it's <code>Delta</code>.
     */
    public abstract Map<U, Delta> readVersionDeltas(final UUID uniqueId);

    /**
     * Read a list of archived artifact versions.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A list of <code>U</code> and it's <code>Delta</code>.
     */
    public abstract Map<U, Delta> readVersionDeltas(final UUID uniqueId,
            final Long compareToVersionId);

    /**
     * Read a list of archived artifact versions.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A list of <code>U</code> and it's <code>Delta</code>.
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
        if (artifactModel.isFlagApplied(artifactId, ArtifactFlag.ARCHIVED)) {
            return artifactId;
        } else {
            return null;
        }
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
