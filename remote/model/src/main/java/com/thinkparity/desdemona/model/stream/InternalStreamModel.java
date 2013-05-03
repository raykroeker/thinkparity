/*
 * Created On:  9-Nov-06 10:00:49 AM
 */
package com.thinkparity.desdemona.model.stream;

import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.stream.StreamInfo;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.model.migrator.Archive;

/**
 * <b>Title:</b>thinkParity Desdemona Internal Stream Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InternalStreamModel extends StreamModel {

    /**
     * Create a stream session for uploading the archive.
     * 
     * @param streamInfo
     *            A <code>StreamInfo</code>.
     * @param archive
     *            An <code>Archive</code>.
     * @return A <code>StreamSession</code>.
     */
    StreamSession newUpstreamSession(StreamInfo streamInfo,
            Product product, Release release, Archive archive);
}
