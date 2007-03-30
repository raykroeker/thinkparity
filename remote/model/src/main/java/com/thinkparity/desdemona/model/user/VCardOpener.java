/*
 * Created On:  30-Mar-07 10:04:01 AM
 */
package com.thinkparity.desdemona.model.user;

import java.io.IOException;
import java.io.InputStream;

/**
 * <b>Title:</b>thinkParity DesdemonaModel VCard Opener<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface VCardOpener {

    /**
     * Open the vcard input stream.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @throws IOException
     */
    public void open(final InputStream stream) throws IOException;
}
