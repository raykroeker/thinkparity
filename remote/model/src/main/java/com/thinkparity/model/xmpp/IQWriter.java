/*
 * Created On: Jun 22, 2006 2:55:32 PM
 * $Id$
 */
package com.thinkparity.model.xmpp;

import org.xmpp.packet.IQ;

import com.thinkparity.model.artifact.ArtifactType;

/**
 * <b>Title:</b>thinkParity Model IQ Writer <br>
 * <b>Description:</b>A custom xmpp internet query writer for the model.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class IQWriter extends com.thinkparity.codebase.xmpp.IQWriter {

    /** Create IQWriter. */
    public IQWriter(final IQ iq) { super(iq); }

    /**
     * Write the artifact type to the internet query.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public final void writeArtifactType(final String name,
            final ArtifactType value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }
}
