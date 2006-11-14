/*
 * Created On: Jun 22, 2006 2:55:32 PM
 * $Id$
 */
package com.thinkparity.desdemona.util.xmpp;

import java.util.List;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.util.dom4j.ElementBuilder;

import org.xmpp.packet.IQ;

/**
 * <b>Title:</b>thinkParity Model IQ Writer <br>
 * <b>Description:</b>A custom xmpp internet query writer for the model.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class IQWriter extends com.thinkparity.codebase.xmpp.IQWriter {

    /** Create IQWriter. */
    public IQWriter(final IQ iq, final Log4JWrapper logger) {
        super(iq, logger);
    }

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

    /**
     * Write the artifact type to the internet query.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public final void writeProfileEMails(final String name,
            final List<ProfileEMail> value) {
        ElementBuilder.addProfileEMailElements(iq.getChildElement(), name,
                name, value);
    }
}
