/*
 * Created On:  26-Jun-07 11:22:20 AM
 */
package com.thinkparity.desdemona.model;

import java.io.File;
import java.io.IOException;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface DownloadHelper {

    /**
     * Download the content to the target. The target file will be created.
     * 
     * @param target
     *            A target <code>File</code>.
     * @throws IOException
     */
    public void download(final File target) throws IOException;
}
