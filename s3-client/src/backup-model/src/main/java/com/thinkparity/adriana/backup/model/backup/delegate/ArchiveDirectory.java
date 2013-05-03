/*
 * Created On:  29-Sep-07 7:31:09 PM
 */
package com.thinkparity.adriana.backup.model.backup.delegate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.StreamUtil;

import org.apache.tools.tar.TarBuffer;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarOutputStream;

/**
 * <b>Title:</b>thinkParity Adriana Backup Model Archive Directory Delegate<br>
 * <b>Description:</b>Create a single archive file from a directory.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ArchiveDirectory {

    /** A buffer. */
    private final byte[] buffer;

    /**
     * Create ArchiveDirectory.
     *
     */
    public ArchiveDirectory(final ByteBuffer buffer) {
        super();
        this.buffer = buffer.array();
    }

    /**
     * Archive a directory.
     * 
     * @param source
     *            A <code>File</code>.
     * @param target
     *            A <code>File</code>.
     * @throws IOException
     */
    public void archive(final File source, final File target) throws IOException {
        final TarOutputStream targetStream = newTargetStream(target);
        try {
            targetStream.setLongFileMode(TarOutputStream.LONGFILE_GNU);
            final File[] sourceFileArray = new FileSystem(source).listFiles("/", Boolean.TRUE);
            TarEntry tarEntry;
            InputStream sourceStream;
            for (int i = 0; i < sourceFileArray.length; i++) {
                tarEntry = new TarEntry(sourceFileArray[i]);
                targetStream.putNextEntry(tarEntry);
                sourceStream = new FileInputStream(sourceFileArray[i]);
                try {
                    StreamUtil.copy(sourceStream, targetStream, buffer);
                } finally {
                    sourceStream.close();
                }
            }
        } finally {
            targetStream.close();
        }
    }

    /**
     * Create a tar output stream target.
     * 
     * @param target
     *            A <code>File</code>.
     * @return A <code>TarOutputStream</code>.
     * @throws IOException
     */
    private TarOutputStream newTargetStream(final File target) throws IOException {
        return new TarOutputStream(new FileOutputStream(target),
                TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
    }
}
