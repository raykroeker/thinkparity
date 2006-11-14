/*
 * Created On: Jul 6, 2006 12:02:54 PM
 */
package com.thinkparity.ophelia.model.util.smackx.packet;

import org.jivesoftware.smack.packet.IQ;

/**
 * <b>Title:</b>thinkParity Abstract Internet Query<br>
 * <b>Description:</b>A thinkparity internet query provides an abstraction of a
 * jive internet query wrapper used to store data members when handling remote
 * events.
 * 
 * @author raymond@thinkparity.com
 * @version
 */
public abstract class AbstractThinkParityIQ extends IQ {

    /**
     * Create AbstractThinkParityIQ.
     * 
     */
    public AbstractThinkParityIQ() {
        super();
    }

    /**
     * Since we are a simple wrapper for remote event data; there is no child
     * xml required.
     * 
     */
    @Override
    public final String getChildElementXML() {
        return null;
    }
}
