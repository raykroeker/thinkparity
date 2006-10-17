/*
 * May 14, 2006 9:10:20 AM
 * $Id$
 */
package com.thinkparity.ophelia.model.io.xmpp;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.zip.DataFormatException;

import org.apache.commons.codec.binary.Base64;

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
import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.CompressionUtil.Level;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.model.artifact.ArtifactRemoteInfo;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.document.DocumentVersionContent;
import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.model.Constants.Xml;
import com.thinkparity.ophelia.model.Constants.Xml.Service;

/**
 * <b>Title:</b>thinkParity XMPP Method<br>
 * <b>Description:</b>An xmpp method is used to execute remote method calls.
 * Similar in nature to a jdbc network call the xmpp method will serialize
 * parameters and return an xmpp method result.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
@SuppressWarnings("unchecked")
public class XMPPMethod extends IQ {

    /** An apache logger. */
    protected static final Log4JWrapper logger;

    static {
        logger = new Log4JWrapper();

        ProviderManager.addIQProvider(Xml.Service.NAME, Xml.Service.NAMESPACE_RESPONSE, new XMPPMethodResponseProvider());
    }

    /** A map of remote method parameters to their values. */
    protected final List<XMPPMethodParameter> parameters;

    /** The remote method name. */
    private final String name;

    /**
     * Create RemoteMethod.
     *
     * @param name
     *      A method name.
     */
    public XMPPMethod(final String name) {
        super();
        this.parameters = new LinkedList<XMPPMethodParameter>();
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
        logVariable("preSendPacket", DateUtil.getInstance());
        xmppConnection.sendPacket(this);
        logVariable("postSendPacket", DateUtil.getInstance());

        // this sleep has been inserted because when packets are sent within
        // x milliseconds of each other, they tend to get swallowed by the
        // smack library
        try { Thread.sleep(75); }
        catch(final InterruptedException ix) {}

        // the timeout is used because the timeout is not expected to be long;
        // and it helps debug non-implemented responses
        logVariable("preResponseCollected", DateUtil.getInstance());
        try {
            return (XMPPMethodResponse) idCollector.nextResult(3 * 1000);
//            return (XMPPMethodResponse) idCollector.nextResult();
        } catch (final ClassCastException ccx) {
            final String errorId = new ErrorHelper().getErrorId(ccx);
            logger.logError(errorId, ccx);
            logger.logError("name:{0}", name);
            logger.logError("xmppConnection:{0}", xmppConnection);
            throw new XMPPException(errorId);
        } finally {
            // re-set the parameters post execution
            logVariable("postResponseCollected", DateUtil.getInstance());
            parameters.clear();
        }
    }

    /**
     * Log a variable.  Note that only the variable value will be rendered.
     * 
     * @param name
     *            The variable name.
     * @param value
     *            The variable value.
     */
    protected final <V> V logVariable(final String name, final V value) {
        return logger.logVariable(name, value);
    }

    /** @see org.jivesoftware.smack.packet.IQ#getChildElementXML() */
    public String getChildElementXML() {
        final StringBuffer childElementXML = new StringBuffer()
            .append("<query xmlns=\"jabber:iq:parity:")
            .append(name)
            .append("\">")
            .append(getParametersXML())
            .append("</query>");
        logVariable("childElementXML.length():", childElementXML.length());
        return childElementXML.toString();
    }

    public final void setDocumentVersionParameters(final String listname,
            final String name, final List<DocumentVersionContent> values) {
        final List<XMPPMethodParameter> parameters = new ArrayList<XMPPMethodParameter>(values.size());
        for(final DocumentVersionContent value : values) {
            parameters.add(new XMPPMethodParameter(name, DocumentVersionContent.class, value));
        }
        this.parameters.add(new XMPPMethodParameter(listname, List.class, parameters));
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
        final List<XMPPMethodParameter> parameters = new LinkedList<XMPPMethodParameter>();
        for(final JabberId value : values) {
            parameters.add(new XMPPMethodParameter(name, JabberId.class, value));
        }
        this.parameters.add(new XMPPMethodParameter(listName, List.class, parameters));
    }

    public final void setLibraryParameters(final String listName, final String name,
            final List<Library> values) {
        final List<XMPPMethodParameter> parameters = new LinkedList<XMPPMethodParameter>();
        for(final Library value : values)
            parameters.add(new XMPPMethodParameter(name, Library.class, value));

        this.parameters.add(new XMPPMethodParameter(listName, List.class, parameters));
    }

    public final void setLongParameters(final String listName, final String name,
            final List<Long> values) {
        final List<XMPPMethodParameter> parameters = new LinkedList<XMPPMethodParameter>();
        for(final Long value : values)
            parameters.add(new XMPPMethodParameter(name, Long.class, value));

        this.parameters.add(new XMPPMethodParameter(listName, List.class, parameters));
    }

    public final void setParameter(final String name, final ArtifactType value) {
        parameters.add(new XMPPMethodParameter(name, ArtifactType.class, value));
    }

    public final void setParameter(final String name, final byte[] value) {
        parameters.add(new XMPPMethodParameter(name, byte[].class, value));
    }

    public final void setParameter(final String name, final Calendar value) {
        parameters.add(new XMPPMethodParameter(name, Calendar.class, value));
    }

    public final void setParameter(final String name, final EMail value) {
        parameters.add(new XMPPMethodParameter(name, EMail.class, value));
    }

    public final void setParameter(final String name, final Integer value) {
        parameters.add(new XMPPMethodParameter(name, Integer.class, value));
    }

    public final void setParameter(final String name, final JabberId value) {
        parameters.add(new XMPPMethodParameter(name, JabberId.class, value));
    }

    public final void setParameter(final String name, final Library.Type value) {
        parameters.add(new XMPPMethodParameter(name, Library.Type.class, value));
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
        parameters.add(new XMPPMethodParameter(name, Long.class, value));
    }

    public final void setParameter(final String name, final ProfileEMail value) {
        parameters.add(new XMPPMethodParameter(name, ProfileEMail.class, value));
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
        parameters.add(new XMPPMethodParameter(name, String.class, value));
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
        final List<XMPPMethodParameter> parameters = new ArrayList<XMPPMethodParameter>(values.size());
        for (final JabberId value : values) {
            parameters.add(new XMPPMethodParameter(itemName, JabberId.class, value));
        }
        this.parameters.add(new XMPPMethodParameter(listName, List.class, parameters));
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
        parameters.add(new XMPPMethodParameter(name, UUID.class, value));
    }

    /**
     * Flush the internal parameter map.
     *
     * @return A buffer of xml containing the parameters.
     */
    protected String getParametersXML() {
        final StringBuffer xml = new StringBuffer();

        for(final XMPPMethodParameter parameter : parameters)
            xml.append(getParameterXML(parameter));

        return xml.toString();
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
    protected PacketCollector createPacketCollector(
            final XMPPConnection xmppConnection) {
        return xmppConnection.createPacketCollector(
                new PacketIDFilter(getPacketID()));
    }

    private String encode(final byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    private String getParameterXML(final XMPPMethodParameter parameter) {
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

    private String getParameterXMLValue(final XMPPMethodParameter parameter) {
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
                    .append(getParameterXML(new XMPPMethodParameter("uniqueId", UUID.class, dvc.getVersion().getArtifactUniqueId())))
                    .append(getParameterXML(new XMPPMethodParameter("versionId", Long.class, dvc.getVersion().getVersionId())))
                    .append(getParameterXML(new XMPPMethodParameter("bytes", byte[].class, dvc.getContent())))
                    .toString();
        } else if (parameter.javaType.equals(EMail.class)) {
            return parameter.javaValue.toString();
        } else if (parameter.javaType.equals(EMail.class)) {
            return parameter.javaValue.toString();
        }
        else if(parameter.javaType.equals(Integer.class)) {
            return parameter.javaValue.toString();
        }
        else if(parameter.javaType.equals(JabberId.class)) {
            return ((JabberId) parameter.javaValue).getQualifiedUsername();
        }
        else if(parameter.javaType.equals(Long.class)) {
            return parameter.javaValue.toString();
        }
        else if(parameter.javaType.equals(Library.Type.class)) {
            return parameter.javaValue.toString();
        }
        else if(parameter.javaType.equals(List.class)) {
            final List<XMPPMethodParameter> listItems = (List<XMPPMethodParameter>) parameter.javaValue;
            final StringBuffer xmlValue = new StringBuffer("");

            if(null != listItems && 0 < listItems.size()) {
                for(final XMPPMethodParameter listItem : listItems) {
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
            if (javaType.equals(ArtifactRemoteInfo.class)) {
                parser.next();
                final ArtifactRemoteInfo remoteInfo = new ArtifactRemoteInfo();
                remoteInfo.setUpdatedBy((JabberId) parseJavaObject(parser, "updatedBy", JabberId.class));
                remoteInfo.setUpdatedOn((Calendar) parseJavaObject(parser, "updatedOn", Calendar.class));
                parser.next();
                return remoteInfo;
            } else if (javaType.equals(ArtifactState.class)) {
                parser.next();
                final ArtifactState state = ArtifactState.valueOf(parser.getText());
                parser.next();
                parser.next();
                return state;
            } else if (javaType.equals(ArtifactType.class)) {
                parser.next();
                final ArtifactType type = ArtifactType.valueOf(parser.getText());
                parser.next();
                parser.next();
                return type;
            } else if (javaType.equals(byte[].class)) {
                parser.next();
                final byte[] bValue = decompress(decode(parser.getText()));
                parser.next();
                parser.next();
                return bValue;
            } else if (javaType.equals(Boolean.class)) {
                parser.next();
                final Boolean value = Boolean.valueOf(parser.getText());
                parser.next();
                parser.next();
                return value;
            } else if(javaType.equals(Calendar.class)) {
                parser.next();
                final Calendar value = calendarValueOf(parser.getText());
                parser.next();
                parser.next();
                return value;
            } else if (javaType.equals(Container.class)) {
                parser.next();
                final Container container = new Container();
                container.setCreatedBy((JabberId) parseJavaObject(parser, "createdBy", JabberId.class));
                container.setCreatedOn((Calendar) parseJavaObject(parser, "createdOn", Calendar.class));
                container.setDraft((Boolean) parseJavaObject(parser, "draft", Boolean.class));
                container.setLocalDraft((Boolean) parseJavaObject(parser, "localDraft", Boolean.class));
                container.setName((String) parseJavaObject(parser, "name", String.class));
                container.setRemoteInfo((ArtifactRemoteInfo) parseJavaObject(parser, "remoteInfo", ArtifactRemoteInfo.class));
                container.setState((ArtifactState) parseJavaObject(parser, "state", ArtifactState.class));
                container.setType((ArtifactType) parseJavaObject(parser, "type", ArtifactType.class));
                container.setUniqueId((UUID) parseJavaObject(parser, "uniqueId", UUID.class));
                container.setUpdatedBy((JabberId) parseJavaObject(parser, "updatedBy", JabberId.class));
                container.setUpdatedOn((Calendar) parseJavaObject(parser, "updatedOn", Calendar.class));
                parser.next();
                return container;
            } else if (javaType.equals(ContainerVersion.class)) {
                parser.next();
                final ContainerVersion version = new ContainerVersion();
                version.setArtifactType((ArtifactType) parseJavaObject(parser, "artifactType", ArtifactType.class));
                version.setArtifactUniqueId((UUID) parseJavaObject(parser, "artifactUniqueId", UUID.class));
                version.setCreatedBy((JabberId) parseJavaObject(parser, "createdBy", JabberId.class));
                version.setCreatedOn((Calendar) parseJavaObject(parser, "createdOn", Calendar.class));
                version.setName((String) parseJavaObject(parser, "name", String.class));
                version.setUpdatedBy((JabberId) parseJavaObject(parser, "updatedBy", JabberId.class));
                version.setUpdatedOn((Calendar) parseJavaObject(parser, "updatedOn", Calendar.class));
                version.setVersionId((Long) parseJavaObject(parser, "versionId", Long.class));
                parser.next();
                return version;
            } else if (javaType.equals(Document.class)) {
                parser.next();
                final Document document = new Document();
                document.setCreatedBy((JabberId) parseJavaObject(parser, "createdBy", JabberId.class));
                document.setCreatedOn((Calendar) parseJavaObject(parser, "createdOn", Calendar.class));
                document.setName((String) parseJavaObject(parser, "name", String.class));
                document.setRemoteInfo((ArtifactRemoteInfo) parseJavaObject(parser, "remoteInfo", ArtifactRemoteInfo.class));
                document.setState((ArtifactState) parseJavaObject(parser, "state", ArtifactState.class));
                document.setType((ArtifactType) parseJavaObject(parser, "type", ArtifactType.class));
                document.setUniqueId((UUID) parseJavaObject(parser, "uniqueId", UUID.class));
                document.setUpdatedBy((JabberId) parseJavaObject(parser, "updatedBy", JabberId.class));
                document.setUpdatedOn((Calendar) parseJavaObject(parser, "updatedOn", Calendar.class));
                parser.next();
                return document;
            } else if (javaType.equals(DocumentVersion.class)) {
                parser.next();
                final DocumentVersion version = new DocumentVersion();
                version.setArtifactType((ArtifactType) parseJavaObject(parser, "artifactType", ArtifactType.class));
                version.setArtifactUniqueId((UUID) parseJavaObject(parser, "artifactUniqueId", UUID.class));
                version.setChecksum((String) parseJavaObject(parser, "checksum", String.class));
                version.setCompression((Integer) parseJavaObject(parser, "checksum", Integer.class));
                version.setCreatedBy((JabberId) parseJavaObject(parser, "createdBy", JabberId.class));
                version.setCreatedOn((Calendar) parseJavaObject(parser, "createdOn", Calendar.class));
                version.setEncoding((String) parseJavaObject(parser, "encoding", String.class));
                version.setName((String) parseJavaObject(parser, "name", String.class));
                version.setUpdatedBy((JabberId) parseJavaObject(parser, "updatedBy", JabberId.class));
                version.setUpdatedOn((Calendar) parseJavaObject(parser, "updatedOn", Calendar.class));
                version.setVersionId((Long) parseJavaObject(parser, "versionId", Long.class));
                parser.next();
                return version;
            } else if (javaType.equals(EMail.class)) {
                parser.next();
                final EMail value = EMailBuilder.parse(parser.getText());
                parser.next();
                parser.next();
                return value;
            } else if (javaType.equals(Integer.class)) {
                parser.next();
                final Integer value = Integer.valueOf(parser.getText());
                parser.next();
                parser.next();
                return value;
            } else if(javaType.equals(String.class)) {
                parser.next();
                final String sValue = parser.getText();
                parser.next();
                parser.next();
                return sValue;
            }
            else if(javaType.equals(com.thinkparity.codebase.jabber.JabberId.class)) {
                parser.next();
                final JabberId jabberId = JabberIdBuilder.parse(parser.getText());
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
            } else if (javaType.equals(UUID.class)) {
                parser.next();
                final UUID value = UUID.fromString(parser.getText());
                parser.next();
                parser.next();
                return value;
            } else {
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
