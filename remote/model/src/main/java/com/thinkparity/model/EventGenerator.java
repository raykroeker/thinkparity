/*
 * Created On: Aug 2, 2006 9:00:19 AM
 */
package com.thinkparity.model;

import org.xmpp.packet.IQ;

import com.thinkparity.model.xmpp.IQWriter;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class EventGenerator {

    protected IQWriter createEventWriter(final String eventName) {
        final IQ iq = new IQ();
        iq.setType(IQ.Type.set);
        iq.setChildElement("query", "jabber:iq:parity:" + eventName);
        return new IQWriter(iq);
    }
}
