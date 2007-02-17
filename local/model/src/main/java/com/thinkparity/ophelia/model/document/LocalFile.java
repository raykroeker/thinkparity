/*
 * Created On: Nov 16, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.ModelHelper;
import com.thinkparity.ophelia.model.Constants.DirectoryNames;
import com.thinkparity.ophelia.model.util.MD5Util;
import com.thinkparity.ophelia.model.util.Opener;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * The local file is an abstraction of the local file is the file on disk that
 * represent's a document or a document version's content that the user
 * interacts with. It is intended to be used only by the document model
 * implementation as a means to clean up some of its code.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
class LocalFile extends ModelHelper<DocumentModelImpl> {

    /** An apache logger. */
    protected final Log4JWrapper logger;

    /** The local <code>File</code>. */
    private final File file;

    /** A document name generator. */ 
    private final DocumentNameGenerator nameGenerator;

    /**
     * Create a LocalFile.
     * 
     * @param document
     *            The document the local file represents.
     */
    LocalFile(final DocumentModelImpl model, final Workspace workspace,
            final Document document) {
        super(model);
        this.logger = new Log4JWrapper();
        this.nameGenerator = new DocumentNameGenerator();
        this.file = getFile(workspace, document);
    }

    /**
     * Create a LocalFile.
     * 
     * @param workspace
     *            The workspace.
     * @param version
     *            The version.
     */
    LocalFile(final DocumentModelImpl model, final Workspace workspace,
            final Document document, final DocumentVersion version) {
        super(model);
        this.logger = new Log4JWrapper();
        this.nameGenerator = new DocumentNameGenerator();
        this.file = getFile(workspace, document, version);
    }

    /**
     * Copy the local file.
     * 
     * @param copyTo
     *            The copy <code>File</code>.
     * @throws IOException
     */
    void copy(final File copyTo, final Integer buffer) throws IOException {
        final FileOutputStream outputStream = new FileOutputStream(copyTo);
        try {
            final InputStream inputStream = openInputStream();
            try {
                StreamUtil.copy(inputStream, outputStream, buffer);
            } finally {
                inputStream.close();
            }
        } finally {
            outputStream.close();
        }
    }

    /**
     * Delete the local file.
     *
     */
    void delete(final LocalFileLock lock) {
        assertIsValid(lock);
        Assert.assertTrue(file.delete(), "Could not delete file {0}.", file);
// NOCOMMIT
//        if(lock.getFile().exists())
//            Assert.assertTrue(file.delete(), "Could not delete file {0}.", file);
    }

    /**
     * Determine whether or not the file exists.
     * 
     * @return True if the file exists.
     */
    boolean exists() {
        return file.exists();
    }

    /**
     * Obtain the last modified date of the file.
     * 
     * @return The last modified date as a <code>Long</code> number of
     *         milliseconds.
     */
    long lastModified() {
        return file.lastModified();
    }

    /**
     * Obtain an exclusive file lock.
     * 
     * @return A <code>DocumentLock</code>.
     */
    <T extends LocalFileLock> T lock(final T instance)
            throws CannotLockException {
        return instance;
// NOCOMMIT
//        try {
//            final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
//            final FileChannel fileChannel = randomAccessFile.getChannel();
//            try {
//                instance.setFile(file);
//                instance.setRandomAccessFile(randomAccessFile);
//                instance.setFileChannel(fileChannel);
//                final FileLock fileLock = fileChannel.tryLock();
//                if (null == fileLock)
//                    throw new CannotLockException(file.getName());
//                instance.setFileLock(fileLock);
//                file.setWritable(true, true);
//                return instance;
//            } catch (final CannotLockException clx) {
//                throw clx;
//            } catch (final Throwable t) {
//                try {
//                    fileChannel.close();
//                } catch (final IOException iox) {
//                    logger.logError(iox, "Could not close file channel for {0}.", file);
//                }
//                throw panic(t);
//            }
//        } catch (final CannotLockException clx) {
//            throw clx;
//        } catch (final Throwable t) {
//            throw panic(t);
//        }
    }

    /**
     * Open the document local file.
     * 
     * @throws IOException
     */
    void open(final Opener opener) {
        opener.open(file);
    }

    /**
     * Open the local file. Note that this stream is not buffered; and not
     * closed.
     * 
     * @return An input stream.
     * @throws FileNotFoundException
     */
    InputStream openInputStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }
    
    /**
     * Read the local file's checksum.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    String readChecksum() throws FileNotFoundException, IOException {
        if (file.exists()) {
            final InputStream fileInputStream = openInputStream();
            try {
                return MD5Util.md5Hex(fileInputStream);
            } finally {
                fileInputStream.close();
            }
        } else {
            return null;
        }
    }

    /**
     * Release the exclusive file lock.
     * 
     * @param lock
     *            A <code>DocumentLock</code>.
     */
    void release(final LocalFileLock lock) {
// NOCOMMIT
//        try {
//            file.setWritable(lock.isWritable(), true);
//            if (lock.getFileLock().isValid())
//                lock.getFileLock().release();
//        } catch (final Throwable t) {
//            throw panic(t);
//        } finally {
//            try {
//                if (lock.getFileChannel().isOpen())
//                    lock.getFileChannel().close();
//            } catch (final Throwable t1) {
//                logger.logError(t1, "Could not release file lock for {0}.", file);
//            }
//        }
    }

    /**
     * Rename the file.
     * 
     * @param filename
     *            The new name.
     */
    void rename(final LocalFileLock lock, final String filename) {
        assertIsValid(lock);
        final File renameTo = new File(lock.getFile().getParentFile(), filename);
        Assert.assertTrue(file.renameTo(renameTo),
                "Cannot rename file {0} to {1}.",
                lock.getFile(), renameTo);
// NOCOMMIT
//        final File renameTo = new File(lock.getFile().getParentFile(), filename);
//        Assert.assertTrue(lock.getFile().renameTo(renameTo),
//                "Cannot rename file {0} to {1}.",
//                lock.getFile(), renameTo);
    }

    /**
     * Set the file's read-only flag.
     *
     */
    void setReadOnly(final LocalFileLock lock) {
        assertIsValid(lock);
        file.setReadOnly();
// NOCOMMIT
//        lock.getFile().setReadOnly();
    }

    /**
     * Write the input stream to the local file.
     * The file date will be the specified time.
     * 
     * @param is
     *            The input stream containing new content.
     * @param time
     *            The modified date as a <code>Long</code> number of milliseconds.        
     * @throws FileNotFoundException
     * @throws IOException
     */
    void write(final LocalFileLock lock, final InputStream is, final Long time,
            final Integer buffer) throws IOException {
        write2(is, time, buffer);
// NOCOMMIT
//        assertIsValid(lock);
//        final FileChannel fileChannel = lock.getFileChannel();
//        final byte[] bytes = new byte[buffer];
//        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
//        int bytesRead;
//        while ((bytesRead = is.read(bytes)) > 0) {
//            byteBuffer.position(0);
//            byteBuffer.limit(bytesRead);
//            fileChannel.write(byteBuffer);
//        }
//        lock.getFile().setLastModified(time);
    }

    private void write2(final InputStream is, final Long time,
            final Integer buffer) throws IOException {
        final OutputStream os = new FileOutputStream(file);
        try {
            StreamUtil.copy(is, os, buffer);
        } finally {
            os.close();
        }
        file.setLastModified(time);
    }

    /**
     * Assert the validity of the document lock.
     * 
     * @param lock
     *            A <code>DocumentLock</code>.
     */
    private void assertIsValid(final LocalFileLock lock) {
// NOCOMMIT
//        Assert.assertNotNull(lock, "Local file lock for {0} is null.", file);
//        Assert.assertNotNull(lock.getFile(), "File for {0} is null.", file);
//        Assert.assertNotNull(lock.getFileChannel(), "File channel for {0} is null.", file);
//        Assert.assertNotNull(lock.getFileLock(), "File lock for {0} is null.", file);
//        Assert.assertTrue(lock.getFileLock().isValid(), "File lock for {0} is not valid.", file);
//        Assert.assertNotNull(lock.getRandomAccessFile(), "Random access file for {0} is null.", file);
    }

    /**
     * Obtain the file for the document.
     * 
     * @param workspace
     *            The workspace.
     * @param document
     *            The document.
     * @return The file.
     */
    private File getFile(final Workspace workspace, final Document document) {
        final String child = nameGenerator.fileName(document);
        return new File(getFileParent(workspace, document), child);
    }

    /**
     * Obtain the file for the version.
     * 
     * @param workspace
     *            The workspace.
     * @param document
     *            The document.
     * @param version
     *            The version.
     * @return The file.
     */
    private File getFile(final Workspace workspace, final Document document,
            final DocumentVersion version) {
        final String child = nameGenerator.localFileName(version);
        return new File(getFileParent(workspace, document), child);
    }

    /**
     * Obtain the parent file for the document.
     * 
     * @param document
     *            The document.
     * @return The parent file.
     */
    private File getFileParent(final Workspace workspace,
            final Document document) {
        final File localDirectory = new File(workspace.getDataDirectory(),
                DirectoryNames.Workspace.Data.LOCAL);
        if(!localDirectory.exists()) {
            Assert.assertTrue(localDirectory.mkdir(),
                    "Cannot create directory {0}.",
                    localDirectory);
        }
        final File parent = new File(localDirectory,
                nameGenerator.localDirectoryName(document));
        if(!parent.exists()) {
            Assert.assertTrue(parent.mkdir(),
                    "Cannot create directory {0}.", parent);
        }
        return parent;
    }
}
