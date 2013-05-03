/*
 * Created On:  29-Sep-07 8:58:50 PM
 */
package com.thinkparity.adriana.backup.model.backup.delegate;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ChecksumFile {

    /**
     * Create ChecksumFile.
     *
     */
    public ChecksumFile(final ByteBuffer buffer) {
        super();
    }

    /**
     * Calculate a file's checksum.
     * 
     * @param source
     *            A <code>File</code>.
     */
    public String checksum(final File source) {
        return null;
    }
}
