/*
 * Created On:  25-Jul-07 8:35:43 PM
 */
package com.thinkparity.codebase.bzip2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.thinkparity.codebase.nio.ChannelUtil;

import org.apache.tools.bzip2.CBZip2OutputStream;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CompressFile {

    /**
     * Create CompressFile.
     *
     */
    public CompressFile() {
        super();
    }

    /**
     * Compress a file using the BZip2 compression algorithm. Note that the byte
     * buffer must be a direct allocation.
     * 
     * @param source
     *            A <code>File</code>.
     * @param target
     *            A <code>File</code>.
     * @param buffer
     *            A <code>ByteBuffer</code>.
     */
    public void compress(final File source, final File target,
            final ByteBuffer buffer) throws IOException {
        if (null == source || null == target || null == target) {
            throw new NullPointerException();
        }
        if (!source.exists()) {
            throw new IllegalArgumentException("Source " + source.getAbsolutePath() + " does not exist.");
        }
        if (!source.isFile()) {
            throw new IllegalArgumentException("Source " + source.getAbsolutePath() + " is not a file.");
        }
        if (!source.canRead()) {
            throw new IllegalArgumentException("Source " + source.getAbsolutePath() + " cannot be read.");
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
        final ReadableByteChannel sourceChannel = ChannelUtil.openReadChannel(source);
        try {
            final CBZip2OutputStream targetStream = new CBZip2OutputStream(
                    new FileOutputStream(target), CBZip2OutputStream.MAX_BLOCKSIZE);
            try {
                targetStream.write(Constants.HEADER);
                targetStream.flush();
            } catch (final IOException iox) {
                targetStream.close();
                throw iox;
            }
            final WritableByteChannel targetChannel = Channels.newChannel(targetStream);
            try {
                ChannelUtil.copy(sourceChannel, targetChannel, buffer);
            } finally {
                targetChannel.close();
            }
        } finally {
            sourceChannel.close();
        }
    }
}
