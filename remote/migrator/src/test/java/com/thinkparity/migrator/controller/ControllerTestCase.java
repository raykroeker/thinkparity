/*
 * Created On: Thu May 11 2006 08:28 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller;

import java.util.List;

import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.MigratorTestCase;
import com.thinkparity.migrator.util.IQWriter;

/**
 * A controller test case abstraction.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class ControllerTestCase extends MigratorTestCase {

    protected static String createFailMessage(final IQ iq) {
        final PacketError packetError = iq.getError();
        return new StringBuffer("[RMIGRATOR] [CONTROLLER TEST] [FAIL] [")
            .append(packetError.getCondition().toString())
            .append("]")
            .append(Separator.SystemNewLine)
            .append(Separator.Tab)
            .append("[")
            .append(packetError.getType())
            .append("]")
            .append(Separator.SystemNewLine)
            .append(Separator.Tab)
            .append("[")
            .append(packetError.getText())
            .append("]")
            .toString();
    }

    protected static Boolean didFail(final IQ iq) { return iq.getType() == IQ.Type.error; }

    /**
     * Create ControllerTestCase.
     *
     * @param name
     *      The test name.
     */
    protected ControllerTestCase(final String name) { super(name); }

    /**
     * Create an internet query of type get.
     *
     * @return An internet query.
     */
    protected IQ createGetIQ() {
        final IQ iq = new IQ(IQ.Type.get);
        iq.setChildElement("query", "jabber:iq:parity");
        return iq;
    }

    protected void writeBytes(final IQ iq, final String name, final Byte[] value) {
        new IQWriter(iq).writeBytes(name, value);
    }

    protected void writeString(final IQ iq, final String name, final String value) {
        new IQWriter(iq).writeString(name, value);
    }

    protected void writeLibraryType(final IQ iq, final String name,
            final Library.Type libraryType) {
        new IQWriter(iq).writeLibraryType(name, libraryType);
    }

    protected void writeLong(final IQ iq, final String name, final Long value) {
        new IQWriter(iq).writeLong(name, value);
    }

    protected void writeLongs(final IQ iq, final String parentName, final String name,
            final List<Long> values) {
        new IQWriter(iq).writeLongs(parentName, name, values);
    }
}
