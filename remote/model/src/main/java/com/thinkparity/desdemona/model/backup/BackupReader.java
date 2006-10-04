/*
 * Created On: Sep 16, 2006 12:41:44 PM
 */
package com.thinkparity.desdemona.model.backup;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;

import com.thinkparity.desdemona.model.archive.ClientModelFactory;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class BackupReader<T extends Artifact, U extends ArtifactVersion> {

    /**
     * Create an empty reader. The empty reader will return a backup reader that
     * returns only null and empty lists.
     * 
     * @param <T>
     *            The backup reader artifact type.
     * @param <U>
     *            The backup reader artifact version type.
     * @return An
     *         <code>ArtifactReader&lt;T extends Artifact, U extends ArtifactVersion&gt;</code>.
     */
    static final <T extends Artifact, U extends ArtifactVersion> BackupReader<T, U> emptyReader() {
        return new BackupReader<T, U>() {
            @Override
            public List<T> read() {
                return Collections.emptyList();
            }
            @Override
            public T read(final UUID uniqueId) {
                return null;
            }
            @Override
            public List<U> readVersions(final UUID uniqueId) {
                return Collections.emptyList();
            }
        };
    }

    /** A thinkParity client artifact interface. */
    protected final InternalArtifactModel artifactModel;

    /** An artifact backup filter. */
    protected final Filter<Artifact> filter;

    /** A client model factory. */
    private final ClientModelFactory modelFactory;

    /** Create BackupReader. */
    protected BackupReader(final ClientModelFactory modelFactory) {
        super();
        this.filter = new Filter<Artifact>() {
            public Boolean doFilter(final Artifact o) {
                return o.contains(ArtifactFlag.ARCHIVED);
            }
        };
        this.modelFactory = modelFactory;
        this.artifactModel = modelFactory.getArtifactModel(getClass());
    }

    /**
     * Create BackupReader. This constructor is intended to be used by the empty
     * reader singleton only.
     */
    private BackupReader() {
        super();
        this.filter = null;
        this.modelFactory = null;
        this.artifactModel = null;
    }

    /**
     * Read a list of backup artifacts.
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
     * Read a list of backup artifact versions.
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
     * Read an artifact id.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return An artifact id <code>Long</code>.
     */
    protected Long readArtifactId(final UUID uniqueId) {
        return artifactModel.readId(uniqueId);
    }

    /**
     * Read the artifact id for an backup artifact.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return An artifact id <code>Long</code>.
     */
    protected Long readBackupArtifactId(final UUID uniqueId) {
        final Long artifactId = readArtifactId(uniqueId);
        if (artifactModel.isFlagApplied(artifactId, ArtifactFlag.ARCHIVED)) {
            return null;
        } else {
            return artifactId;
        }
    }
}
