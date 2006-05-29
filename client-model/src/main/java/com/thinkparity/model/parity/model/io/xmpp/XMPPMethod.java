/*
 * May 14, 2006 9:10:20 AM
 * $Id$
 */
package com.thinkparity.model.parity.model.io.xmpp;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.codebase.CompressionUtil;
import com.thinkparity.codebase.CompressionUtil.Level;

import com.thinkparity.migrator.Library;

/**
 * An xmpp method for the parity bootstrap library.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
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
    XMPPMethod(final String name) {
        super();
        this.parameters = new LinkedList<Parameter>();
        this.name = name;
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

    String getName() { return "query"; }

    String getNamespace() { return "jabber:iq:parity:" + name; }

    void setParameter(final String name, final byte[] value) {
        parameters.add(new Parameter(name, byte[].class, value));
    }

    void setParameter(final String name, final Library.Type value) {
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
    void setParameter(final String name, final Long value) {
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
    void setParameter(final String name, final String value) {
        parameters.add(new Parameter(name, String.class, value));
    }

    void setLibraryParameters(final String listName, final String name,
            final List<Library> values) {
        final List<Parameter> parameters = new LinkedList<Parameter>();
        for(final Library value : values)
            parameters.add(new Parameter(name, Library.class, value));

        this.parameters.add(new Parameter(listName, List.class, parameters));
    }

    void setLongParameters(final String listName, final String name,
            final List<Long> values) {
        final List<Parameter> parameters = new LinkedList<Parameter>();
        for(final Long value : values)
            parameters.add(new Parameter(name, Long.class, value));

        this.parameters.add(new Parameter(listName, List.class, parameters));
    }

    private byte[] autobox(final Byte[] bytes) {
        final byte[] boxed = new byte[bytes.length];
        for(int i = 0; i < bytes.length; i++) boxed[i] = bytes[i];
        return boxed;
    }

    private byte[] compress(final byte[] bytes) {
        try { return CompressionUtil.compress(bytes, Level.Nine); }
        catch(final IOException iox) { throw new XMPPException(iox); }
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
