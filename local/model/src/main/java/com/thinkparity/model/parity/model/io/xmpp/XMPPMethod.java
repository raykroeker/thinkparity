/*
 * May 14, 2006 9:10:20 AM
 * $Id$
 */
package com.thinkparity.model.parity.model.io.xmpp;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.codebase.CompressionUtil;
import com.thinkparity.codebase.CompressionUtil.Level;

import com.thinkparity.model.xmpp.JabberId;

import com.thinkparity.migrator.Library;

/**
 * <b>Title:</b>thinkParity XMPP Method<br>
 * <b>Description:</b>An xmpp method is used to execute remote method calls.
 * Similar in nature to a jdbc network call the xmpp method will serialize
 * parameters and return an xmpp method result.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class XMPPMethod extends IQ {

    /** The remote method name. */
    private final String name;

    /** A map of remote method parameters to their values. */
    private final List<Parameter> parameters;

    /**
     * Create RemoteMethod.
     *
     * @param name
     *      A method name.
     */
    public XMPPMethod(final String name) {
        super();
        this.parameters = new LinkedList<Parameter>();
        this.name = name;
    }

    /**
     * Execute this method on the give connection.
     * 
     * @param xmppConnection
     *            An xmpp connection.
     * @return An xmpp method response.
     */
    public XMPPMethodResponse execute(final XMPPConnection xmppConnection) {
        // create a collector for the response
        final PacketCollector idCollector = createPacketCollector(xmppConnection);
        // send the internet query
        xmppConnection.sendPacket(this);

        // this sleep has been inserted because when packets are sent within
        // x milliseconds of each other, they tend to get swallowed by the
        // smack library
        try { Thread.sleep(75); }
        catch(final InterruptedException ix) {}

        return ((XMPPMethodResponse) idCollector.nextResult());
    }

    /** @see org.jivesoftware.smack.packet.IQ#getChildElementXML() */
    public String getChildElementXML() {
        final StringBuffer childElementXML = new StringBuffer()
            .append("<query xmlns=\"jabber:iq:parity:")
            .append(name)
            .append("\">")
            .append(getParametersXML())
            .append("</query>");
        return childElementXML.toString();
    }

    /**
     * Set a list of jabber id parameters.
     * 
     * @param listName
     *            The list name.
     * @param name
     *            The list item names.
     * @param values
     *            The list values.
     */
    public final void setJabberIdParameters(final String listName, final String name,
            final List<JabberId> values) {
        final List<Parameter> parameters = new LinkedList<Parameter>();
        for(final JabberId value : values) {
            parameters.add(new Parameter(name, JabberId.class, value));
        }
        this.parameters.add(new Parameter(listName, List.class, parameters));
    }

    public final void setLibraryParameters(final String listName, final String name,
            final List<Library> values) {
        final List<Parameter> parameters = new LinkedList<Parameter>();
        for(final Library value : values)
            parameters.add(new Parameter(name, Library.class, value));

        this.parameters.add(new Parameter(listName, List.class, parameters));
    }

    public final void setLongParameters(final String listName, final String name,
            final List<Long> values) {
        final List<Parameter> parameters = new LinkedList<Parameter>();
        for(final Long value : values)
            parameters.add(new Parameter(name, Long.class, value));

        this.parameters.add(new Parameter(listName, List.class, parameters));
    }

    public final void setParameter(final String name, final byte[] value) {
        parameters.add(new Parameter(name, byte[].class, value));
    }

    public final void setParameter(final String name, final Library.Type value) {
        parameters.add(new Parameter(name, Library.Type.class, value));
    }

    /**
     * Set a named parameter.
     * 
     * @param name
     *            The parameter name.
     * @param value
     *            The parameter value.
     */
    public final void setParameter(final String name, final Long value) {
        parameters.add(new Parameter(name, Long.class, value));
    }

    /**
     * Set a named parameter.
     * 
     * @param name
     *            The parameter name.
     * @param value
     *            The parameter value.
     */
    public final void setParameter(final String name, final String value) {
        parameters.add(new Parameter(name, String.class, value));
    }

    /**
     * Set a named parameter.
     * 
     * @param name
     *            The parameter name.
     * @param value
     *            The parameter value.
     */
    public final void setParameter(final String name, final UUID value) {
        parameters.add(new Parameter(name, UUID.class, value));
    }

    private byte[] compress(final byte[] bytes) {
        try { return CompressionUtil.compress(bytes, Level.Nine); }
        catch(final IOException iox) { throw new XMPPException(iox); }
    }

    /**
     * Create a packet collector that will filter on packets with the same
     * query id.
     *
     * @param iq
     *      The internet query.
     * @return A packet collector.
     */
    private PacketCollector createPacketCollector(
            final XMPPConnection xmppConnection) {
        return xmppConnection.createPacketCollector(
                new PacketIDFilter(getPacketID()));
    }
    
    private String encode(final byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    /**
     * Flush the internal parameter map.
     *
     * @return A buffer of xml containing the parameters.
     */
    private String getParametersXML() {
        final StringBuffer xml = new StringBuffer();

        for(final Parameter parameter : parameters)
            xml.append(getParameterXML(parameter));

        return xml.toString();
    }

    private String getParameterXML(final Parameter parameter) {
        final StringBuffer xml = new StringBuffer();
        xml.append("<").append(parameter.name).append(" javaType=\"")
                .append(parameter.javaType.getName())
                .append("\"");
        if(null == parameter.javaValue) { xml.append("/>"); }
        else {
            xml.append(">")
                    .append(getParameterXMLValue(parameter))
                    .append("</").append(parameter.name).append(">");
        }

        return xml.toString();
    }

    private String getParameterXMLValue(final Parameter parameter) {
        if(parameter.javaType.equals(byte[].class)) {
            return encode(compress((byte[]) parameter.javaValue));
        }
        else if(parameter.javaType.equals(String.class)) {
            return parameter.javaValue.toString();
        }
        else if(parameter.javaType.equals(JabberId.class)) {
            return ((JabberId) parameter.javaValue).getQualifiedJabberId();
        }
        else if(parameter.javaType.equals(Long.class)) {
            return parameter.javaValue.toString();
        }
        else if(parameter.javaType.equals(Library.Type.class)) {
            return parameter.javaValue.toString();
        }
        else if(parameter.javaType.equals(List.class)) {
            final List<Parameter> listItems = (List<Parameter>) parameter.javaValue;
            final StringBuffer xmlValue = new StringBuffer("");

            if(null != listItems && 0 < listItems.size()) {
                for(final Parameter listItem : listItems) {
                    xmlValue.append(getParameterXML(listItem));
                }
            }

            return xmlValue.toString();
        }
        else if(parameter.javaType.equals(UUID.class)) {
            return parameter.javaValue.toString();
        }
        else {
            final String assertion =
                MessageFormat.format("[RMODEL] [XMPP METHOD] [GET PARAMTER XML VALUE] [UNKNOWN JAVA TYPE] [{0}]",
                        new Object[] {parameter.javaType.getName()});
            throw new XMPPException(assertion);
        }
    }

    private final class Parameter {
        private final Class javaType;
        private final Object javaValue;
        private final String name;
        private Parameter(final String name, final Class javaType,
                final Object javaValue) {
            this.name = name;
            this.javaType = javaType;
            this.javaValue = javaValue;
        }
    }
}
