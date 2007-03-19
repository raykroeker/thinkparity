/*
 * Created On:  19-Mar-07 11:34:10 AM
 */
package com.thinkparity.ophelia.model.document;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.thinkparity.codebase.StreamUtil;

import com.thinkparity.codebase.model.stream.StreamOpener;

import com.thinkparity.ophelia.model.ModelHelper;

/**
 * <b>Title:</b>thinkParity OpheliaModel Document Stream Writer Helper<br>
 * <b>Description:</b>A helper class that will write an input stream to a file
 * channel.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class StreamWriterHelper extends ModelHelper<DocumentModelImpl> implements
        StreamOpener {

    /** A <code>ByteBuffer</code>. */
    private final ByteBuffer buffer;

    /** A <code>FileChannel</code>. */
    private final FileChannel fileChannel;

    /**
     * Create StreamWriterHelper.
     * 
     * @param model
     *            An instance of <code>DocumentModelImpl</code>.
     * @param fileChannel
     *            A <code>FileChannel</code>.
     * @param buffer
     *            A <code>ByteBuffer</code>.
     */
    public StreamWriterHelper(final DocumentModelImpl model,
            final FileChannel fileChannel, final ByteBuffer buffer) {
        super(model);
        this.fileChannel = fileChannel;
        this.buffer = buffer;
    }

    /**
     * @see com.thinkparity.codebase.model.stream.StreamOpener#open(java.io.InputStream)
     *
     */
    public void open(final InputStream stream) throws IOException {
        try {
            writeFile(stream);
        } finally {
            stream.close();
        }
    }

    /**
     * Write a stream to the file channel using the buffer.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @throws IOException
     */
    private void writeFile(final InputStream stream) throws IOException {
        fileChannel.position(0);
        StreamUtil.copy(stream, fileChannel, buffer);
        fileChannel.force(true);
    }
}