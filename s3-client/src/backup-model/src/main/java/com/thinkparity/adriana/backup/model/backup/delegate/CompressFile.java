/*
 * Created On:  29-Sep-07 8:09:19 PM
 */
package com.thinkparity.adriana.backup.model.backup.delegate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.thinkparity.codebase.StreamUtil;

import org.apache.tools.bzip2.CBZip2OutputStream;

/**
 * <b>Title:</b>thinkParity Adriana Backup Model Compress File Delegate<br>
 * <b>Description:</b>A BZip2 implementation of compression.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CompressFile {

    /** A buffer. */
    private final byte[] buffer;

    /**
     * Create CompressFile.
     * 
     * @param buffer
     *            A <code>ByteBuffer</code>.
     */
    public CompressFile(final ByteBuffer buffer) {
        super();
        this.buffer = buffer.array();
    }

    /**
     * Compress a source file into a target file.
     * 
     * @param source
     *            A <code>File</code>.
     * @param target
     *            A <code>File</code>.
     */
    public void compress(final File source, final File target)
            throws IOException {
        final OutputStream targetStream = newTargetStream(target);
        try {
            final InputStream sourceStream = new FileInputStream(source);
            try {
                StreamUtil.copy(sourceStream, targetStream, buffer);
            } finally {
                sourceStream.close();
            }
        } finally {
            targetStream.close();
        }
    }

    /**
     * Create a compressing output stream.
     * 
     * @param target
     *            A <code>File</code>.
     * @return An <code>OutputStream</code>.
     * @throws IOException
     */
    private OutputStream newTargetStream(final File target) throws IOException {
        return new CBZip2OutputStream(new FileOutputStream(target),
                CBZip2OutputStream.MAX_BLOCKSIZE);
    }
}
