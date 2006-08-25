/*
 * May 14, 2006 9:10:20 AM
 * $Id$
 */
package com.thinkparity.model.parity.model.io.xmpp;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.zip.DataFormatException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.ProviderManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.thinkparity.codebase.CompressionUtil;
import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.CompressionUtil.Level;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;

import com.thinkparity.model.Constants.Xml;
import com.thinkparity.model.Constants.Xml.Service;
import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.document.DocumentVersionContent;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;

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

    /** An apache logger. */
    protected static final Logger logger;

    static {
        logger = Logger.getLogger(XMPPMethod.class);

        ProviderManager.addIQProvider(Xml.Service.NAME, Xml.Service.NAMESPACE_RESPONSE, new XMPPMethodResponseProvider());
    }

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
        // add an executed on parameter
        setParameter(Xml.All.EXECUTED_ON, DateUtil.getInstance());

        // create a collector for the response
        final PacketCollector idCollector = createPacketCollector(xmppConnection);
        // send the internet query
        xmppConnection.sendPacket(this);

        // this sleep has been inserted because when packets are sent within
        // x milliseconds of each other, they tend to get swallowed by the
        // smack library
        try { Thread.sleep(75); }
        catch(final InterruptedException ix) {}

        // the timeout is used because the timeout is not expected to be long;
        // and it helps debug non-implemented responses
        try { return ((XMPPMethodResponse) idCollector.nextResult()); }
        catch(final ClassCastException ccx) {
            throw new XMPPException(
                    MessageFormat.format("[XMPP] [XMPP METHOD] [REMOTE METHOD NOT AVAILABLE] [{0}]", name), ccx);
        }
        // re-set the parameters post execution
        finally { parameters.clear(); }
    }

    /** @see org.jivesoftware.smack.packet.IQ#getChildElementXML() */
    public String getChildElementXML() {
        final StringBuffer childElementXML = new StringBuffer()
            .append("<query xmlns=\"jabber:iq:parity:")
            .append(name)
            .append("\">")
            .append(getParametersXML())
            .append("</query>");
        logger.debug(MessageFormat.format("[XML LENGTH] [{0}]", childElementXML.length()));
        return childElementXML.toString();
    }

    public final void setDocumentVersionParameters(final String listname,
            final String name, final List<DocumentVersionContent> values) {
        final List<Parameter> parameters = new ArrayList<Parameter>(values.size());
        for(final DocumentVersionContent value : values) {
            parameters.add(new Parameter(name, DocumentVersionContent.class, value));
        }
        this.parameters.add(new Parameter(listname, List.class, parameters));
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

    public final void setParameter(final String name, final ArtifactType value) {
        parameters.add(new Parameter(name, ArtifactType.class, value));
    }

    public final void setParameter(final String name, final EMail value) {
        parameters.add(new Parameter(name, EMail.class, value));
    }

    public final void setParameter(final String name, final byte[] value) {
        parameters.add(new Parameter(name, byte[].class, value));
    }

    public final void setParameter(final String name, final Calendar value) {
        parameters.add(new Parameter(name, Calendar.class, value));
    }

    public final void setParameter(final String name, final Integer value) {
        parameters.add(new Parameter(name, Integer.class, value));
    }

    public final void setParameter(final String name, final JabberId value) {
        parameters.add(new Parameter(name, JabberId.class, value));
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
     * Set a list of jabber id parameters.
     * 
     * @param listName
     *            The list parameter name.
     * @param itemName
     *            The item parameter name.
     * @param value
     *            The item value.
     */
    public final void setParameter(final String listName,
            final String itemName, final List<JabberId> values) {
        final List<Parameter> parameters = new ArrayList<Parameter>(values.size());
        for (final JabberId value : values) {
            parameters.add(new Parameter(itemName, JabberId.class, value));
        }
        this.parameters.add(new Parameter(listName, List.class, parameters));
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
        if(parameter.javaType.equals(ArtifactType.class)) {
            return parameter.javaValue.toString();
        }
        else if(parameter.javaType.equals(byte[].class)) {
            return encode(compress((byte[]) parameter.javaValue));
        }
        else if(parameter.javaType.equals(Calendar.class)) {
            final Calendar valueGMT =
                DateUtil.getInstance(((Calendar) parameter.javaValue).getTime(), new SimpleTimeZone(0, "GMT"));
            return DateUtil.format(valueGMT, DateUtil.DateImage.ISO);
        }
        else if(parameter.javaType.equals(DocumentVersionContent.class)) {
            final DocumentVersionContent dvc = (DocumentVersionContent) parameter.javaValue;
            return new StringBuffer("")
                    .append(getParameterXML(new Parameter("uniqueId", UUID.class, dvc.getVersion().getArtifactUniqueId())))
                    .append(getParameterXML(new Parameter("versionId", Long.class, dvc.getVersion().getVersionId())))
                    .append(getParameterXML(new Parameter("bytes", byte[].class, dvc.getContent())))
                    .toString();
        } else if (parameter.javaType.equals(EMail.class)) {
            return parameter.javaValue.toString();
        }
        else if(parameter.javaType.equals(Integer.class)) {
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
        else if(parameter.javaType.equals(String.class)) {
            return parameter.javaValue.toString();
        }
        else if(parameter.javaType.equals(UUID.class)) {
            return parameter.javaValue.toString();
        }
        else {
            final String assertion =
                MessageFormat.format("[XMPP METHOD] [GET PARAMTER XML VALUE] [UNKNOWN JAVA TYPE] [{0}]",
                        parameter.javaType.getName());
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

    /** A remote result reader. */
    private static class XMPPMethodResponseProvider implements IQProvider {

        /**
         * @see org.jivesoftware.smack.provider.IQProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
         */
        public IQ parseIQ(final XmlPullParser parser) throws Exception {
            final XMPPMethodResponse response = new XMPPMethodResponse();

            parser.next();
            while(true) {

                // stop processing when we hit the trailing query tag
                if(XmlPullParser.END_TAG == parser.getEventType()) {
                    if(Service.NAME.equals(parser.getName())) { break; }
                    else { Assert.assertUnreachable("Query end tag."); }
                }
                else {
                    final Class javaType = parseJavaType(parser);
                    final String name = parseName(parser);
                    response.writeResult(name, javaType, parseJavaObject(parser, name, javaType));
                }
            }
            return response;
        }

        private Calendar calendarValueOf(final String s) {
            try {
                return DateUtil.parse(s, DateUtil.DateImage.ISO, new SimpleTimeZone(0, "GMT"));
            }
            catch(final ParseException px) { throw new RuntimeException(px); }
        }

        private byte[] decode(final String s) {
            return Base64.decodeBase64(s.getBytes());
        }

        private byte[] decompress(final byte[] bytes) {
            try { return CompressionUtil.decompress(bytes); }
            catch(final DataFormatException dfx) { throw new XMPPException(dfx); }
            catch(final IOException iox) { throw new XMPPException(iox); }
        }

        private Object parseJavaObject(final XmlPullParser parser,
                final String name, final Class javaType) throws IOException,
                XmlPullParserException {
            if(javaType.equals(byte[].class)) {
                parser.next();
                final byte[] bValue = decompress(decode(parser.getText()));
                parser.next();
                parser.next();
                return bValue;
            }
            else if(javaType.equals(Calendar.class)) {
                parser.next();
                final Calendar cValue = calendarValueOf(parser.getText());
                parser.next();
                parser.next();
                return cValue;
            } else if (javaType.equals(EMail.class)) {
                parser.next();
                final EMail value = EMailBuilder.parse(parser.getText());
                parser.next();
                parser.next();
                return value;
            }
            else if(javaType.equals(String.class)) {
                parser.next();
                final String sValue = parser.getText();
                parser.next();
                parser.next();
                return sValue;
            }
            else if(javaType.equals(com.thinkparity.codebase.jabber.JabberId.class)) {
                parser.next();
                // NOTE The jabber id returned is not the same as the type.  This
                // is due to not easily being able to refactor the jabber id class
                // to a different package
                final JabberId jabberId = JabberIdBuilder.parseQualifiedJabberId(parser.getText());
                parser.next();
                parser.next();
                return jabberId;
            }
            else if(javaType.equals(Long.class)) {
                parser.next();
                final Long lValue = Long.valueOf(parser.getText());
                parser.next();
                parser.next();
                return lValue;
            }
            else if(javaType.equals(Library.class)) {
                parser.next();

                final Library library = new Library();
                while(true) {
                    if(XmlPullParser.END_TAG == parser.getEventType()) {
                        // the expecation is that name equals "library"
                        if(name.equals(parseName(parser))) { break; }
                        else {
                            Assert.assertUnreachable("Library end tag.");
                        }
                    }
                    else {
                        if("artifactId".equals(parseName(parser))) {
                            parser.next();
                            library.setArtifactId(parser.getText());
                            parser.next();
                        }
                        else if("createdOn".equals(parseName(parser))) {
                            parser.next();
                            library.setCreatedOn(calendarValueOf(parser.getText()));
                            parser.next();
                        }
                        else if("groupId".equals(parseName(parser))) {
                            parser.next();
                            library.setGroupId(parser.getText());
                            parser.next();
                        }
                        else if("id".equals(parseName(parser))) {
                            parser.next();
                            library.setId(Long.valueOf(parser.getText()));
                            parser.next();
                        }
                        else if("path".equals(parseName(parser))) {
                            parser.next();
                            library.setPath(parser.getText());
                            parser.next();
                        }
                        else if("type".equals(parseName(parser))) {
                            parser.next();
                            library.setType(Library.Type.valueOf(parser.getText()));
                            parser.next();
                        }
                        else if("version".equals(parseName(parser))) {
                            parser.next();
                            library.setVersion(parser.getText());
                            parser.next();
                        }
                        else {
                            Assert.assertUnreachable("Library unknown tag.");
                        }
                        parser.next();
                    }
                }
                parser.next();
                return library;
            }
            else if(javaType.equals(Library.Type.class)) {
                parser.next();
                final Library.Type type = Library.Type.valueOf(parser.getText());
                parser.next();
                parser.next();
                return type;
            }
            else if(javaType.equals(List.class)) {
                parser.next();
                final List list;
                // the list is empty
                if(XmlPullParser.END_TAG == parser.getEventType() &&
                        name.equals(parseName(parser))) {
                    list = Collections.emptyList();
                }
                else {
                    list = new ArrayList();
                    while(true) {
                        // end of the list
                        if(XmlPullParser.END_TAG == parser.getEventType() &&
                                name.equals(parseName(parser))) { break; }
                        ((ArrayList<Object>) list).add(
                                parseJavaObject(parser, parseName(parser), parseJavaType(parser)));
                    }
                }
                parser.next();
                return list;
            }
            else {
                throw Assert.createUnreachable(MessageFormat.format(
                        "[LBROWSER BOOTSTRAP] [XMPP IO] [JAVA TYPE NOT SUPPORTED] [{0}]",
                        new Object[] {javaType.getName()}));
            }
        }

        private Class parseJavaType(final XmlPullParser parser) {
            final String javaType = parser.getAttributeValue("", "javaType");
            try { return Class.forName(javaType); }
            catch(final ClassNotFoundException cnfx) {
                throw new XMPPException(MessageFormat.format(
                        "[LBROWSER BOOTSTRAP] [XMPP IO] [JAVA TYPE NOT SUPPORTED] [{0}]",
                        new Object[] {javaType}));
            }
        }

        private String parseName(final XmlPullParser parser) {
            return parser.getName();
        }
    }
}
