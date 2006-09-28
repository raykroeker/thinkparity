/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.migrator;


import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * The parity bootstrap library interface.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class LibraryModel extends AbstractModel<LibraryModelImpl> {

    /**
     * Obtain the internal parity library interface.
     * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
     * @param context
     *            A parity calling context.
     * @return The internal parity library interface.
     */
    public static InternalLibraryModel getInternalModel(final Context context,
            final Environment environment, final Workspace workspace) {
        return new InternalLibraryModel(context, environment, workspace);
    }

    /**
     * Obtain the parity library interface.
     * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
     * @return The parity library interface.
     */
    public static LibraryModel getModel(final Environment environment,
            final Workspace workspace) {
        return new LibraryModel(environment, workspace);
    }

    /** Create LibraryModel. */
    protected LibraryModel(final Environment environment,
            final Workspace workspace) {
        super(new LibraryModelImpl(environment, workspace));
    }

    /**
     * Create a library.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @param path
     *            A path.
     * @param type
     *            A type.
     * @param version
     *            A version.
     * @param bytes
     *            A byte array.
     * @return A library.
     */
    public Library create(final String artifactId, final String groupId,
            final String path, final Library.Type type, final String version,
            final byte[] bytes) {
        synchronized(getImplLock()) {
            return getImpl().create(artifactId, groupId, path, type, version, bytes);
        }
    }

    /**
     * Read a library.
     * 
     * @param artifactId
     *            An artifact id.
     * @param groupId
     *            A group id.
     * @param type
     *            A type.
     * @param version
     *            A version.
     * @return A library.
     */
    public Library read(final String artifactId, final String groupId,
            final Library.Type type, final String version) {
        synchronized(getImplLock()) {
            return getImpl().read(artifactId, groupId, type, version);
        }
    }
}
