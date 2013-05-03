/*
 * Created On:  26-Jul-07 9:50:32 AM
 */
package com.thinkparity.codebase.model.util;

import java.io.IOException;
import java.io.Reader;

import com.thinkparity.codebase.model.user.UserVCard;

/**
 * <b>Title:</b>thinkParity Model VCard Writer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface VCardReader<T extends UserVCard> {

    /**
     * Read a vcard from a reader.
     * 
     * @param vcard
     *            A <code>T</code>.
     * @param reader
     *            A <code>Reader</code>.
     */
    public void read(final T vcard, final Reader reader) throws IOException;
}
