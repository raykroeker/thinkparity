/*
 * Created On:  13-Feb-07 9:17:52 AM
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * <b>Title:</b>thinkParity Document Lock<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DocumentFileLock {

    /** The <code>File</code>. */
    private File file;

    /** The <code>FileChannel</code>. */
    private FileChannel fileChannel;

    /** A <code>FileLock</code>. */
    private FileLock fileLock;

    /** The <code>RandomAccessFile</code>. */
    private RandomAccessFile randomAccessFile;

    /**
     * Create DocumentLock.
     *
     */
    public DocumentFileLock() {
        super();
    }

    /**
     * Obtain file.
     *
     * @return A File.
     */
    public File getFile() {
        return file;
    }

    /**
     * Obtain fileChannel.
     *
     * @return A FileChannel.
     */
    public FileChannel getFileChannel() {
        return fileChannel;
    }

    /**
     * Obtain fileLock.
     *
     * @return A FileLock.
     */
    public FileLock getFileLock() {
        return fileLock;
    }

    /**
     * Obtain randomAccessFile.
     *
     * @return A RandomAccessFile.
     */
    public RandomAccessFile getRandomAccessFile() {
        return randomAccessFile;
    }

    /**
     * @see com.thinkparity.ophelia.model.document.LocalFileLock#release()
     *
     */
    public void release() throws IOException {
        if (null != fileLock) {
            fileLock.release();
            fileLock = null;
        }
        if (null != fileChannel) {
            if (fileChannel.isOpen()) {
                fileChannel.close();
            }
            fileChannel = null;
        }
        if (null != randomAccessFile) {
            randomAccessFile.close();
            randomAccessFile = null;
        }
    }

    /**
     * Set file.
     *
     * @param file
     *		A File.
     */
    public void setFile(final File file) {
        this.file = file;
    }

    /**
     * Set fileChannel.
     *
     * @param fileChannel
     *		A FileChannel.
     */
    public void setFileChannel(final FileChannel fileChannel) {
        this.fileChannel = fileChannel;
    }

    /**
     * Set fileLock.
     *
     * @param fileLock
     *		A FileLock.
     */
    public void setFileLock(final FileLock fileLock) {
        this.fileLock = fileLock;
    }

    /**
     * Set randomAccessFile.
     *
     * @param randomAccessFile
     *		A RandomAccessFile.
     */
    public void setRandomAccessFile(final RandomAccessFile randomAccessFile) {
        this.randomAccessFile = randomAccessFile;
    }
}
