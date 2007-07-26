/*
 * Created On:  26-Jul-07 9:50:32 AM
 */
package com.thinkparity.codebase.model.util;

import java.io.IOException;
import java.io.Writer;

import com.thinkparity.codebase.model.user.UserVCard;

/**
 * <b>Title:</b>thinkParity Model VCard Writer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface VCardWriter<T extends UserVCard> {

    /**
     * Write a vcard to a writer.
     * 
     * @param vcard
     *            A <code>T</code>.
     * @param writer
     *            A <code>Writer</code>.
     */
    public void write(final T vcard, final Writer writer) throws IOException;
}
