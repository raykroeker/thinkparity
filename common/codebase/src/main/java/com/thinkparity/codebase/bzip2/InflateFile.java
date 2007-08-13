/*
 * Created On:  25-Jul-07 8:35:56 PM
 */
package com.thinkparity.codebase.bzip2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;

import com.thinkparity.codebase.delegate.CancelException;
import com.thinkparity.codebase.delegate.Cancelable;
import com.thinkparity.codebase.nio.ChannelUtil;
import com.thinkparity.codebase.nio.CopyChannel;

import org.apache.tools.bzip2.CBZip2InputStream;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class InflateFile implements Cancelable {

    /** A run indicator. */
    private boolean running;

    /** A cancel indicator. */
    private boolean cancel;

    /** A channel copy delegate. */
    private final CopyChannel copier;

    /**
     * Create InflateFile.
     * 
     * @param buffer
     *            A direct allocation <code>ByteBuffer</code>.
     */
    public InflateFile(final ByteBuffer buffer) {
        super();
        if (!buffer.isDirect()) {
            throw new IllegalArgumentException("Buffer must be a direct allocation.");
        }
        this.cancel = false;
        this.copier = new CopyChannel(buffer);
        this.running = false;
    }

    /**
     * @see com.thinkparity.codebase.delegate.Cancelable#cancel()
     *
     */
    public void cancel() throws CancelException {
        this.cancel = true;
        copier.cancel();
        if (running) {
            synchronized (this) {
                try {
                    wait();
                } catch (final InterruptedException ix) {
                    throw new CancelException(ix);
                }
            }
        }
    }

    /**
     * Inflate a source file to a target file using the bzip2 compression
     * algorithm. Note that the buffer must be a direct allocation buffer.
     * 
     * @param source
     *            A <code>File</code>.
     * @param target
     *            A <code>File</code>.
     * @throws IOException
     */
    public void inflate(final File source, final File target) throws IOException {
        running = true;
        try {
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
            final WritableByteChannel targetChannel = ChannelUtil.openWriteChannel(target);
            try {
                boolean openChannel = false;
                final InputStream sourceStream = new CBZip2InputStream(new FileInputStream(source));
                try {
                    final byte[] header = new byte[2];
                    if (false == cancel) {
                        if (2 == sourceStream.read(header)) {
                            if (Arrays.equals(Constants.HEADER, header)) {
                                final ReadableByteChannel sourceChannel = Channels.newChannel(sourceStream);
                                openChannel = true;
                                try {
                                    copier.copy(sourceChannel, targetChannel);
                                } finally {
                                    sourceChannel.close();
                                }
                            }
                        }
                    }
                } finally {
                    if (false == openChannel) {
                        sourceStream.close();
                    }
                }
            } finally {
                targetChannel.close();
            }
        } finally {
            running = false;
            synchronized (this) { 
                notifyAll();
            }
        }
    }
}
