/*
 * May 14, 2006 9:10:20 AM
 * $Id$
 */
package com.thinkparity.ophelia.model.io.xmpp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.UUID;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.ErrorHelper;
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
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.Token;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;
import com.thinkparity.codebase.model.util.xstream.XStreamUtil;

import com.thinkparity.ophelia.model.Constants.Xml;
import com.thinkparity.ophelia.model.Constants.Xml.Service;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse.Result;
import com.thinkparity.ophelia.model.util.xstream.SmackXppReader;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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

        // TIME This is a local timestamp.
        logVariable("preSendPacket", DateUtil.getInstance());
        xmppConnection.sendPacket(this);
        // TIME This is a local timestamp.
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
            final XMPPMethodResponse response =
//                (XMPPMethodResponse) idCollector.nextResult(7 * 1000);
                (XMPPMethodResponse) idCollector.nextResult();
            return response;
        } catch (final Throwable t) {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, errorId);
            logger.logError("name:{0}", name);
            logger.logError("xmppConnection:{0}", xmppConnection);
            throw new XMPPException(errorId);
        } finally {
            // re-set the parameters post execution
            logVariable("postResponseCollected", DateUtil.getInstance());
            parameters.clear();
        }
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

    private String getParameterXML(final XMPPMethodParameter parameter) {
        final StringBuffer xml = new StringBuffer();
        xml.append("<").append(parameter.name).append(" javaType=\"")
                .append(parameter.javaType.getName())
                .append("\"");
        if (null == parameter.javaValue) { xml.append("/>"); }
        else {
            xml.append(">")
                    .append(getParameterXMLValue(parameter))
                    .append("</").append(parameter.name).append(">");
        }

        return xml.toString();
    }

    private String getParameterXMLValue(final XMPPMethodParameter parameter) {
        if (parameter.javaType.equals(ArtifactType.class)) {
            return parameter.javaValue.toString();
        }
        else if (parameter.javaType.equals(Calendar.class)) {
            final Calendar valueGMT =
                DateUtil.getInstance(((Calendar) parameter.javaValue).getTime(), new SimpleTimeZone(0, "GMT"));
            return DateUtil.format(valueGMT, DateUtil.DateImage.ISO);
        }
        else if (parameter.javaType.equals(DocumentVersionContent.class)) {
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
        else if (parameter.javaType.equals(Integer.class)) {
            return parameter.javaValue.toString();
        }
        else if (parameter.javaType.equals(JabberId.class)) {
            return ((JabberId) parameter.javaValue).getQualifiedUsername();
        }
        else if (parameter.javaType.equals(Long.class)) {
            return parameter.javaValue.toString();
        }
        else if (parameter.javaType.equals(Library.Type.class)) {
            return parameter.javaValue.toString();
        }
        else if (parameter.javaType.equals(List.class)) {
            final List<XMPPMethodParameter> listItems = (List<XMPPMethodParameter>) parameter.javaValue;
            final StringBuffer xmlValue = new StringBuffer("");

            if (null != listItems && 0 < listItems.size()) {
                for(final XMPPMethodParameter listItem : listItems) {
                    xmlValue.append(getParameterXML(listItem));
                }
            }

            return xmlValue.toString();
        }
        else if (parameter.javaType.equals(String.class)) {
            return parameter.javaValue.toString();
        }
        else if (parameter.javaType.equals(UUID.class)) {
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

        private final XStreamUtil xstreamUtil;

        private XMPPMethodResponseProvider() {
            super();
            this.xstreamUtil = XStreamUtil.getInstance();
        }

        /**
         * @see org.jivesoftware.smack.provider.IQProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
         */
        public IQ parseIQ(final XmlPullParser parser) throws Exception {
            try {
                return doParseIQ(parser);
            } catch (final Throwable t) {
                logger.logFatal(t, "Could not parse remote method response.");
                throw new XMPPException(t);
            }
        }

        /**
         * Parse an xmpp remote method call response.
         * 
         * @param parser
         *            An <code>XmlPullParser</code>.
         * @return An <code>XMPPMethodResponse</code>.
         * @throws XmlPullParserException
         * @throws IOException
         */
        private XMPPMethodResponse doParseIQ(final XmlPullParser parser)
                throws XmlPullParserException, IOException {
            final XMPPMethodResponse response = new XMPPMethodResponse();
            parser.next();
            while(true) {
                logger.logVariable("parser.getName()", parser.getName());
                logger.logVariable("parser.getDepth()", parser.getDepth());
                logger.logVariable("parser.getNamespace()", parser.getNamespace());
                // stop processing when we hit the trailing query tag
                if (XmlPullParser.END_TAG == parser.getEventType()) {
                    if (Service.NAME.equals(parser.getName())) {
                        break;
                    } else {
                        Assert.assertUnreachable(MessageFormat.format(
                                "Parsing incomplete for xmlns {0}; tag {1} at depth {2}.",
                                parser.getNamespace(), parser.getName(), parser.getDepth()));
                    }
                } else {
                    response.writeResult(parseResult(parser));
                }
            }
            return response;
        }

        private Calendar calendarValueOf(final String s) {
            try {
                return DateUtil.parse(s, DateUtil.DateImage.ISO, new SimpleTimeZone(0, "GMT"));
            } catch (final ParseException px) {
                throw new RuntimeException(px);
            }
        }

        private Class parseJavaType(final XmlPullParser parser) {
            final String javaType = parser.getAttributeValue("", "javaType");
            try {
                return Class.forName(javaType);
            } catch (final ClassNotFoundException cnfx) {
                throw new XMPPException(MessageFormat.format(
                        "Java type {0} not supported.", javaType));
            }
        }

        private Object parseJavaValue(final XmlPullParser parser,
                final Class javaType) throws XmlPullParserException, IOException {
            logger.logVariable("javaType", javaType);
            if (javaType.equals(List.class)) {
                if (parser.isEmptyElementTag()) {
                    parser.next();
                    parser.next();
                    return Collections.emptyList();
                } else {
                    parser.next();  // move to first list item
                    final List javaValue = new ArrayList();
                    Class listJavaType;
                    while (XmlPullParser.END_TAG != parser.getEventType()) {
                        listJavaType = parseJavaType(parser);
                        ((ArrayList) javaValue).add(
                                parseJavaValue(parser, listJavaType));
                    }
                    parser.next();  // move past end of list
                    return javaValue;
                }
            } else {
                if (parser.isEmptyElementTag()) {
                    parser.next();
                    parser.next();
                    return null;
                } else if (XMPPEvent.class.isAssignableFrom(javaType)) {
                    XMPPEvent event = null;
                    event = xstreamUtil.unmarshalEvent(new SmackXppReader(parser), event);
                    parser.next();
                    parser.next();
                    return event;
                } else {
                    parser.next();  // move to element text
                    final Object javaValue;
                    if (javaType.equals(String.class)) {
                        javaValue = parser.getText();
                        parser.next();
                    } else if (javaType.equals(ArtifactRemoteInfo.class)) {
                        javaValue = new ArtifactRemoteInfo();
                        ((ArtifactRemoteInfo) javaValue).setUpdatedBy((JabberId) parseJavaValue(parser, JabberId.class));
                        ((ArtifactRemoteInfo) javaValue).setUpdatedOn((Calendar) parseJavaValue(parser, Calendar.class));
                    } else if (javaType.equals(ArtifactState.class)) {
                        javaValue = ArtifactState.valueOf(parser.getText());
                        parser.next();
                    } else if (javaType.equals(Environment.class)) {
                        javaValue = Environment.valueOf(parser.getText());
                        parser.next();
                    } else if (javaType.equals(Charset.class)) {
                        javaValue = Charset.forName(parser.getText());
                        parser.next();
                    } else if (javaType.equals(ArtifactType.class)) {
                        javaValue = ArtifactType.valueOf(parser.getText());
                        parser.next();
                    } else if (javaType.equals(Boolean.class)) {
                        javaValue = Boolean.valueOf(parser.getText());
                        parser.next();
                    } else if (javaType.equals(Calendar.class)) {
                        javaValue = calendarValueOf(parser.getText());
                        parser.next();
                    } else if (javaType.equals(Container.class)) {
                        javaValue = new Container();
                        ((Container) javaValue).setCreatedBy((JabberId) parseJavaValue(parser, JabberId.class));
                        ((Container) javaValue).setCreatedOn((Calendar) parseJavaValue(parser, Calendar.class));
                        ((Container) javaValue).setDraft((Boolean) parseJavaValue(parser, Boolean.class));
                        ((Container) javaValue).setLocalDraft((Boolean) parseJavaValue(parser, Boolean.class));
                        ((Container) javaValue).setName((String) parseJavaValue(parser, String.class));
                        ((Container) javaValue).setRemoteInfo((ArtifactRemoteInfo) parseJavaValue(parser, ArtifactRemoteInfo.class));
                        ((Container) javaValue).setState((ArtifactState) parseJavaValue(parser, ArtifactState.class));
                        ((Container) javaValue).setType((ArtifactType) parseJavaValue(parser, ArtifactType.class));
                        ((Container) javaValue).setUniqueId((UUID) parseJavaValue(parser, UUID.class));
                        ((Container) javaValue).setUpdatedBy((JabberId) parseJavaValue(parser, JabberId.class));
                        ((Container) javaValue).setUpdatedOn((Calendar) parseJavaValue(parser, Calendar.class));
                    } else if (javaType.equals(ContainerVersion.class)) {
                        javaValue = new ContainerVersion();
                        ((ContainerVersion) javaValue).setArtifactType((ArtifactType) parseJavaValue(parser, ArtifactType.class));
                        ((ContainerVersion) javaValue).setArtifactUniqueId((UUID) parseJavaValue(parser, UUID.class));
                        ((ContainerVersion) javaValue).setComment((String) parseJavaValue(parser, String.class));
                        ((ContainerVersion) javaValue).setCreatedBy((JabberId) parseJavaValue(parser, JabberId.class));
                        ((ContainerVersion) javaValue).setCreatedOn((Calendar) parseJavaValue(parser, Calendar.class));
                        ((ContainerVersion) javaValue).setName((String) parseJavaValue(parser, String.class));
                        ((ContainerVersion) javaValue).setUpdatedBy((JabberId) parseJavaValue(parser, JabberId.class));
                        ((ContainerVersion) javaValue).setUpdatedOn((Calendar) parseJavaValue(parser, Calendar.class));
                        ((ContainerVersion) javaValue).setVersionId((Long) parseJavaValue(parser, Long.class));
                    } else if (javaType.equals(Document.class)) {
                        javaValue = new Document();
                        ((Document) javaValue).setCreatedBy((JabberId) parseJavaValue(parser, JabberId.class));
                        ((Document) javaValue).setCreatedOn((Calendar) parseJavaValue(parser, Calendar.class));
                        ((Document) javaValue).setName((String) parseJavaValue(parser, String.class));
                        ((Document) javaValue).setRemoteInfo((ArtifactRemoteInfo) parseJavaValue(parser, ArtifactRemoteInfo.class));
                        ((Document) javaValue).setState((ArtifactState) parseJavaValue(parser, ArtifactState.class));
                        ((Document) javaValue).setType((ArtifactType) parseJavaValue(parser, ArtifactType.class));
                        ((Document) javaValue).setUniqueId((UUID) parseJavaValue(parser, UUID.class));
                        ((Document) javaValue).setUpdatedBy((JabberId) parseJavaValue(parser, JabberId.class));
                        ((Document) javaValue).setUpdatedOn((Calendar) parseJavaValue(parser, Calendar.class));
                    } else if (javaType.equals(DocumentVersion.class)) {
                        javaValue = new DocumentVersion();
                        ((DocumentVersion) javaValue).setArtifactType((ArtifactType) parseJavaValue(parser, ArtifactType.class));
                        ((DocumentVersion) javaValue).setArtifactUniqueId((UUID) parseJavaValue(parser, UUID.class));
                        ((DocumentVersion) javaValue).setChecksum((String) parseJavaValue(parser, String.class));
                        ((DocumentVersion) javaValue).setCompression((Integer) parseJavaValue(parser, Integer.class));
                        ((DocumentVersion) javaValue).setCreatedBy((JabberId) parseJavaValue(parser, JabberId.class));
                        ((DocumentVersion) javaValue).setCreatedOn((Calendar) parseJavaValue(parser, Calendar.class));
                        ((DocumentVersion) javaValue).setEncoding((String) parseJavaValue(parser, String.class));
                        ((DocumentVersion) javaValue).setName((String) parseJavaValue(parser, String.class));
                        ((DocumentVersion) javaValue).setSize((Long) parseJavaValue(parser, Long.class));
                        ((DocumentVersion) javaValue).setUpdatedBy((JabberId) parseJavaValue(parser, JabberId.class));
                        ((DocumentVersion) javaValue).setUpdatedOn((Calendar) parseJavaValue(parser, Calendar.class));
                        ((DocumentVersion) javaValue).setVersionId((Long) parseJavaValue(parser, Long.class));
                    } else if (javaType.equals(EMail.class)) {
                        javaValue = EMailBuilder.parse(parser.getText());
                        parser.next();
                    } else if (javaType.equals(Integer.class)) {
                        javaValue = Integer.valueOf(parser.getText());
                        parser.next();
                    } else if (javaType.equals(StreamSession.class)) {
                        javaValue = new StreamSession();
                        ((StreamSession) javaValue).setBufferSize((Integer) parseJavaValue(parser, Integer.class));
                        ((StreamSession) javaValue).setCharset((Charset) parseJavaValue(parser, Charset.class));
                        ((StreamSession) javaValue).setEnvironment((Environment) parseJavaValue(parser, Environment.class));
                        ((StreamSession) javaValue).setId((String) parseJavaValue(parser, String.class));
                    } else if (javaType.equals(Token.class)) {
                        javaValue = new Token();
                        ((Token) javaValue).setValue((String) parseJavaValue(parser, String.class));
                    } else if (javaType.equals(JabberId.class)) {
                        javaValue = JabberIdBuilder.parse(parser.getText());
                        parser.next();
                    } else if (javaType.equals(Long.class)) {
                        javaValue = Long.valueOf(parser.getText());
                        parser.next();
                    } else if (javaType.equals(UUID.class)) {
                        javaValue = UUID.fromString(parser.getText());
                        parser.next();
                    } else {
                        throw Assert.createUnreachable("Unsupported xml type {0}.", javaType.getName());
                    }
                    parser.next();  // move to next tag
                    return javaValue;
                }
            }
        }

        private String parseName(final XmlPullParser parser) {
            return parser.getName();
        }

        private Result parseResult(final XmlPullParser parser)
                throws XmlPullParserException, IOException {
            final Result result = new Result();
            result.name = parseName(parser);
            result.javaType = parseJavaType(parser);
            result.javaValue = parseJavaValue(parser, result.javaType);
            return result;
        }
    }
}
