/*
 * Created On:  29-Sep-07 7:33:50 PM
 */
package com.thinkparity.adriana.backup.model.backup.delegate;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.nio.ChannelUtil;
import com.thinkparity.codebase.nio.CopyChannel;

import com.thinkparity.adriana.backup.model.backup.Resource;

/**
 * <b>Title:</b>thinkParity Adriana Backup Model Copy Resources Delegate<br>
 * <b>Description:</b>Copy a resource list to a target directory.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CopyResources {

    /** A buffer. */
    private final ByteBuffer buffer;

    /**
     * Create CopyResources.
     *
     */
    public CopyResources(final ByteBuffer buffer) {
        super();
        this.buffer = buffer;
    }

    /**
     * Copy the source resources to the target.
     * 
     * @param source
     *            A <code>List<Resource></code>.
     * @param target
     *            A <code>File</code>.
     * @throws IOException
     */
    public void copy(final List<Resource> source, final File target)
            throws IOException {
        final FileSystem targetFileSystem = new FileSystem(target);
        ReadableByteChannel sourceChannel;
        File targetFile;
        WritableByteChannel targetChannel;
        for (int i = 0; i < source.size(); i++) {
            final Resource resource = source.get(i);
            sourceChannel = resource.openChannel();
            try {
                targetFile = targetFileSystem.createFile(resource.getPath());
                targetChannel = ChannelUtil.openWriteChannel(targetFile);
                try {
                    new CopyChannel(buffer).copy(sourceChannel, targetChannel);
                } finally {
                    targetChannel.close();
                }
            } finally {
                sourceChannel.close();
            }
        }
    }
}
