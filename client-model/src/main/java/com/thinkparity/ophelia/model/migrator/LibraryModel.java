/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.migrator;


import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Library Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.4
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface LibraryModel {

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
            final byte[] bytes);

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
            final Library.Type type, final String version);
}
