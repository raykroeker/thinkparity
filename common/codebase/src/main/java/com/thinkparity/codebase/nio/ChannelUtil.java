/*
 * Created On:  10-May-07 10:33:41 AM
 */
package com.thinkparity.codebase.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * <b>Title:</b>thinkParity CommonCodebase Channel Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ChannelUtil {

    /**
     * Open a readable byte channel for a file.
     * 
     * @param file
     *            A <code>File</code>.
     * @return A <code>ReadableByteChannel</code>.
     * @throws FileNotFoundException
     */
    public static ReadableByteChannel openReadChannel(final File file)
            throws FileNotFoundException {
        return new RandomAccessFile(file, "r").getChannel();
    }

    /**
     * Open a writable byte channel for a file.
     * 
     * @param file
     *            A <code>File</code>.
     * @return A <code>WritableByteChannel</code>.
     * @throws FileNotFoundException
     */
    public static WritableByteChannel openWriteChannel(final File file)
            throws FileNotFoundException {
        return new RandomAccessFile(file, "rws").getChannel();
    }

    /**
     * Copy the content from a readable channel to a writable one.
     * 
     * @param readChannel
     *            A <code>ReadableByteChannel</code>.
     * @param writeChannel
     *            A <code>WritableByteChannel</code>.
     * @param buffer
     *            A direct allocation buffer <code>ByteBuffer</code>.
     * @throws IOException
     */
    public static void copy(final ReadableByteChannel readChannel,
            final WritableByteChannel writeChannel, final ByteBuffer buffer)
            throws IOException {
        new CopyChannel(buffer).copy(readChannel, writeChannel);
    }

    /**
     * Create ChannelUtil.
     *
     */
    private ChannelUtil() {
        super();
    }
}
