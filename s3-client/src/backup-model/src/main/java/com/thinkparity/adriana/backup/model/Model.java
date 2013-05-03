/*
 * Created On:  29-Sep-07 5:12:44 PM
 */
package com.thinkparity.adriana.backup.model;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.adriana.backup.model.ModelRuntimeException.Code;
import com.thinkparity.adriana.backup.model.util.Session;

/**
 * <b>Title:</b>thinkParity Adriana Backup Model Abstraction<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class Model {

    /** A log4j wrapper. */
    protected final Log4JWrapper logger;

    /**
     * Create Model.
     *
     */
    protected Model() {
        super();
        this.logger = new Log4JWrapper(getClass());
    }

    /**
     * Create a temporary directory.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param path
     *            A <code>String</code>.
     * @return A <code>File</code>.
     */
    protected final File createTempDirectory(final Session session,
            final String path) {
        final FileSystem fileSystem = new FileSystem(session.getWorkingDirectory());
        final File file = fileSystem.find(path);
        if (null == file) {
            return fileSystem.createDirectory(path);
        } else {
            throw new ModelRuntimeException(Code.TEMP_DIRECTORY_EXISTS, file);
        }
    }

    /**
     * Create a temporary file.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param path
     *            A <code>String</code>.
     * @return A <code>File</code>.
     */
    protected final File createTempFile(final Session session, final String path)
            throws IOException {
        final FileSystem fileSystem = new FileSystem(session.getWorkingDirectory());
        final File file = fileSystem.find(path);
        if (null == file) {
            return fileSystem.createFile(path);
        } else {
            throw new ModelRuntimeException(Code.TEMP_FILE_EXISTS, file);
        }
    }

    protected final ByteBuffer getDirectBuffer() {
        return null;
    }

    /**
     * Panic. Instaniate a model runtime error if required and log the error.
     * 
     * @param error
     *            An <code>Exception</code>.
     * @return A <code>ModelRuntimeException</code>.
     */
    protected final ModelRuntimeException panic(final Exception error) {
        if (error.getClass().isAssignableFrom(ModelRuntimeException.class)) {
            return (ModelRuntimeException) error;
        } else {
            logger.logError(error, "An unexpected error has occured.");
            return new ModelRuntimeException(Code.PANIC, error);
        }
    }
}
