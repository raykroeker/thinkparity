/*
 * Created On: Jun 22, 2006 2:55:32 PM
 * $Id$
 */
package com.thinkparity.model.xmpp;

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
    public IQWriter(final IQ iq) { super(iq); }
}
