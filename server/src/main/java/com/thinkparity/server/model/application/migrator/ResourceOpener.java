/*
 * Created On:  26-Mar-07 2:15:59 PM
 */
package com.thinkparity.desdemona.model.migrator;

import java.io.IOException;
import java.io.InputStream;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Resource Opener<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ResourceOpener {

    /**
     * Open a resource.
     * 
     * @param resource
     *            A resource <code>InputStream</code>.
     * @throws IOException
     */
    public void open(final InputStream resource) throws IOException;
}
