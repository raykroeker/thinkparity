/*
 * Created On:  11-Aug-07 4:49:59 PM
 */
package com.thinkparity.codebase.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.thinkparity.codebase.delegate.CancelException;
import com.thinkparity.codebase.delegate.Cancelable;

/**
 * <b>Title:</b>thinkParity Codebase Copy Channel<br>
 * <b>Description:</b>A delegate used to copy a source channel to a target via
 * a buffer.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CopyChannel implements Cancelable {

    /** A buffer. */
    private final ByteBuffer buffer;

    /** A cancel indicator. */
    private boolean cancel;

    /** A run indicator. */
    private boolean running;

    /**
     * Create CopyChannel.
     *
     */
    public CopyChannel(final ByteBuffer buffer) {
        super();
        if (!buffer.isDirect()) {
            throw new IllegalArgumentException("Buffer must be a direct allocation.");
        }
        this.buffer = buffer;
        this.cancel = false;
        this.running = false;
    }

    /**
     * @see com.thinkparity.codebase.delegate.Cancelable#cancel()
     * 
     */
    public void cancel() throws CancelException {
        this.cancel = true;
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
     * Copy a source buffer to a target.
     * 
     * @param source
     *            A <code>ReadableByteChannel</code>.
     * @param target
     *            A <code>WritableByteChannel</code>.
     * @throws IOException
     *             if an io error occurs
     */
    public void copy(final ReadableByteChannel source,
            final WritableByteChannel target) throws IOException {
        running = true;
        try {
            int read, limit;
            while (true) {
                // fill the buffer to capacity
                limit = 0;
                buffer.clear();
                if (cancel) {
                    break;
                } else {
                    read = source.read(buffer);
                    if (-1 == read) {   // we're done
                        break;
                    } else {
                        limit += read;
                    }
                    while (-1 < read && buffer.position() < buffer.capacity()) {
                        if (cancel)  {
                            break;
                        } else {
                            read = source.read(buffer);
                            if (-1 < read) {
                                limit += read;
                            }
                        }
                    }
                    buffer.flip();
                    buffer.limit(limit);
                    while (buffer.hasRemaining()) {
                        if (cancel) {
                            break;
                        } else {
                            target.write(buffer);
                        }
                    }
                }
            }
        } finally {
            running = false;
            synchronized (this) {
                notifyAll();
            }
        }
    }
}
