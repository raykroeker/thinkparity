/*
 * Created On:  25-Jul-07 8:35:43 PM
 */
package com.thinkparity.codebase.bzip2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CompressStream {

    /**
     * Create CompressFile.
     *
     */
    public CompressStream() {
        super();
    }

    /**
     * Compress a file using the BZip2 compression algorithm. Note that the byte
     * buffer must be a direct allocation.
     * 
     * @param source
     *            An <code>InputStream</code>.
     * @param target
     *            A <code>File</code>.
     * @param buffer
     *            A <code>ByteBuffer</code>.
     */
    public void compress(final InputStream source, final File target,
            final ByteBuffer buffer) throws IOException {
        if (null == source || null == target || null == target) {
            throw new NullPointerException();
        }
        if (!target.exists()) {
            throw new IllegalArgumentException("Target " + target.getAbsolutePath() + " does not exist.");
        }
        if (!target.isFile()) {
            throw new IllegalArgumentException("Target " + target.getAbsolutePath() + " is not a file.");
        }
        if (!target.canRead()) {
            throw new IllegalArgumentException("Target " + target.getAbsolutePath() + " cannot be read.");
        }
        if (!target.canWrite()) {
            throw new IllegalArgumentException("Target " + target.getAbsolutePath() + " cannot be written to.");
        }
        if (!buffer.isDirect()) {
            throw new IllegalArgumentException("Buffer is not direct.");
        }
        final ReadableByteChannel sourceChannel = Channels.newChannel(source);
        try {
            new CompressChannel().compress(sourceChannel, target, buffer);
        } finally {
            sourceChannel.close();
        }
    }
}
