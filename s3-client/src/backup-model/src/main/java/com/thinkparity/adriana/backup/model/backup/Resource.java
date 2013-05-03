/*
 * Created On:  29-Sep-07 6:53:25 PM
 */
package com.thinkparity.adriana.backup.model.backup;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

/**
 * <b>Title:</b>thinkParity Adriana Backup Model Resource<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Resource {

    /**
     * Obtain the resource path.
     * 
     * @return A <code>String</code>.
     */
    String getPath();

    /**
     * Open the resource.
     * 
     * @return A <code>ReadableByteChannel</code>.
     */
    ReadableByteChannel openChannel() throws IOException;
}