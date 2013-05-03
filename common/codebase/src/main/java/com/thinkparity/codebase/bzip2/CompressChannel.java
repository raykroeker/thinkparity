/*
 * Created On:  25-Jul-07 9:33:50 PM
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
 * <b>Title:</b>thinkParity Codebase Compress Channel<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CompressChannel {

    /**
     * Create CompressChannel.
     *
     */
    public CompressChannel() {
        super();
    }

    /**
     * Compress a source channel to a target file.
     * 
     * @param source
     *            A <code>ReadableByteChannel</code>.
     * @param target
     *            A <code>File</code>.
     * @param buffer
     *            A <code>ByteBuffer</code>.
     * @throws IOException
     */
    public void compress(final ReadableByteChannel source, final File target,
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
                ChannelUtil.copy(source, targetChannel, buffer);
            } finally {
                targetChannel.close();
            }
        } finally {
            source.close();
        }
    }
}
