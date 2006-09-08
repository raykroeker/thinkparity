/*
 * Created On: Aug 2, 2006 9:14:42 AM
 */
package com.thinkparity.desdemona.wildfire.handler;

import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

import com.thinkparity.codebase.Constants.Xml;
import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.desdemona.model.ModelTestCase;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class HandlerTestCase extends ModelTestCase {

    /**
     * Create a failure message for an error iq.
     * 
     * @param iq
     *            An error internet query.
     * @return A failure message.
     */
    protected static String createFailMessage(final Object message, final IQ iq) {
        final PacketError packetError = iq.getError();
        return new StringBuffer()
            .append(message).append(Separator.SystemNewLine)
            .append(packetError.getCondition().toString())
            .append("]")
            .append(Separator.SystemNewLine).append(Separator.Tab)
            .append("[")
            .append(packetError.getType())
            .append("]")
            .append(Separator.SystemNewLine).append(Separator.Tab)
            .append("[")
            .append(packetError.getText())
            .append("]")
            .toString();
    }

    /**
     * Determine whether or not the internet query container failure
     * information.
     * 
     * @param iq
     *            The internet query.
     * @return True if the iq is a failure.
     */
    protected static Boolean didFail(final IQ iq) {
        return iq.getType() == IQ.Type.error;
    }

    /** Handler datum. */
    protected HandlerFixture handlerDatum;

    /** Create HandlerTestCase. */
    public HandlerTestCase(String name) { super(name); }

    /**
     * @see com.thinkparity.desdemona.model.ModelTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final IQ iq = new IQ(IQ.Type.get);
        iq.setChildElement(Xml.NAME, Xml.NAMESPACE);
        this.handlerDatum = new HandlerFixture(iq);
    }

    /**
     * @see com.thinkparity.desdemona.model.ModelTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        this.handlerDatum = null;
        super.tearDown();
    }

    /** Handler datum definition. */
    protected class HandlerFixture {
        public final IQ iq;
        private HandlerFixture(final IQ iq) {
            this.iq = iq;
        }
    }
}
