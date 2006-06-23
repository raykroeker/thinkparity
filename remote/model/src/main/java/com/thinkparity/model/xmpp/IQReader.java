/*
 * Created On: Jun 22, 2006 2:53:27 PM
 * $Id$
 */
package com.thinkparity.model.xmpp;

import java.util.UUID;

import org.xmpp.packet.IQ;

/**
 * <b>Title:</b>thinkParity Model IQ Reader <br>
 * <b>Description:</b>An xmpp internet query reader for the model.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class IQReader extends com.thinkparity.codebase.xmpp.IQReader {

    /** Create IQReader. */
    public IQReader(final IQ iq) { super(iq); }

    /**
     * Read a unique id.
     * 
     * @param name
     *            The element name.
     * @return The element value.
     */
    public final UUID readUUID(final String name) {
        final String sData = readString(name);
        if(null == sData) { return null; }
        else { return UUID.fromString(sData); }
    }
}
