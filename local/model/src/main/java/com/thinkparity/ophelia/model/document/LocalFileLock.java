/*
 * Created On:  13-Feb-07 10:33:48 AM
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface LocalFileLock {

    /**
     * Obtain file.
     *
     * @return A File.
     */
    public File getFile();

    /**
     * Obtain fileChannel.
     *
     * @return A FileChannel.
     */
    public FileChannel getFileChannel();

    /**
     * Obtain fileLock.
     *
     * @return A FileLock.
     */
    public FileLock getFileLock();

    /**
     * Obtain randomAccessFile.
     *
     * @return A RandomAccessFile.
     */
    public RandomAccessFile getRandomAccessFile();

    /**
     * Determine if the original file was writable.
     * 
     * @return True if it could be written to.
     */
    public Boolean isWritable();

    /**
     * Set file.
     *
     * @param file
     *      A File.
     */
    public void setFile(final File file);

    /**
     * Set fileChannel.
     *
     * @param fileChannel
     *      A FileChannel.
     */
    public void setFileChannel(final FileChannel fileChannel);

    /**
     * Set fileLock.
     *
     * @param fileLock
     *      A FileLock.
     */
    public void setFileLock(final FileLock fileLock);

    /**
     * Set randomAccessFile.
     *
     * @param randomAccessFile
     *      A RandomAccessFile.
     */
    public void setRandomAccessFile(final RandomAccessFile randomAccessFile);

    /**
     * Set the writeable flag of the local file lock.
     * 
     */
    public void setWritable(final Boolean writable);
}
