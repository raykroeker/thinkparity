/*
 * May 9, 2006
 */
package com.thinkparity.migrator.model.release;

import java.util.List;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.model.AbstractModel;
import com.thinkparity.migrator.model.Context;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class ReleaseModel extends AbstractModel {

    /**
     * Obtain the remote internal parity release interface.
     * 
     * @param context
     *            The parity internal context.
     * @return The parity release interface.
     */
    public static InternalReleaseModel getInternalModel(final Context context) {
        return new InternalReleaseModel(context);
    }

    /**
     * Obtain the remote parity release interface.
     * 
     * @return The parity release interface.
     */
    public static ReleaseModel getModel() {
        return new ReleaseModel();
    }

    /** The release implementation. */
    private final ReleaseModelImpl impl;

    /** The release implementation synchronization lock. */
    private final Object implLock;

    /** Create ReleaseModel. */
    private ReleaseModel() {
        super();
        this.impl = new ReleaseModelImpl();
        this.implLock = new Object();
    }

    /**
     * Read a release.
     * 
     * @param releaseName
     *            The release name.
     * @return The release.
     */
    public Release read(final String name) {
        synchronized(implLock) { return impl.read(name); }
    }

    /**
     * Read the latest release.
     *
     * @return A release.
     */
    public Release readLatest() {
        synchronized(implLock) { return impl.readLatest(); }
    }

    /**
     * Create a release.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @param name
     *            A name.
     * @param version
     *            A version.
     * @param libraries
     *            The libraries associated with the release.
     * @return The release.
     */
    public Release create(final String artifactId, final String groupId,
            final String name, final String version,
            final List<Library> libraries) {
        synchronized(implLock) {
            return impl.create(artifactId, groupId, name, version, libraries);
        }
    }
}
