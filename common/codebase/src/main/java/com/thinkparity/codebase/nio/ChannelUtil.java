/*
 * Created On:  10-May-07 10:33:41 AM
 */
package com.thinkparity.codebase.nio;

import java.io.IOException;
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
        if (!buffer.isDirect())
            throw new IllegalArgumentException("Buffer must be a direct allocation.");
        int read;
        while (true) {
            buffer.clear();
            read = readChannel.read(buffer);
            if (-1 == read) {
                break;
            }
            buffer.flip();
            buffer.limit(read);
            while (buffer.hasRemaining()) {
                writeChannel.write(buffer);
            }
        }
    }

    /**
     * Create ChannelUtil.
     *
     */
    private ChannelUtil() {
        super();
    }
}
