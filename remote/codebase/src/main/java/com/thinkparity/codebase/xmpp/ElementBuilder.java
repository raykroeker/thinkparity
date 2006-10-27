/*
 * Created On: Dec 7, 2005
 */
package com.thinkparity.codebase.xmpp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.UUID;

import com.thinkparity.codebase.CompressionUtil;
import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.util.Base64;

import com.thinkparity.codebase.model.artifact.ArtifactRemoteInfo;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.Token;

import org.dom4j.Element;

/**
 * <b>Title:</b>thinkParity Remote Element Builder <br>
 * <b>Description:</b>A dom element builder for the jive server plugins.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.5
 */
public class ElementBuilder {

    public static final Element addContainerElements(final Element parent,
            final String parentName, final String name,
            final List<Container> values) {
        final Element element = addElement(parent, parentName, List.class);
        for (final Container value : values) {
            addElement(element, name, value);
        }
        return element;
    }

    public static final Element addContainerVersionElements(final Element parent,
            final String parentName, final String name,
            final List<ContainerVersion> values) {
        final Element element = addElement(parent, parentName, List.class);
        for (final ContainerVersion value : values) {
            addElement(element, name, value);
        }
        return element;
    }

    public static final Element addDocumentElements(final Element parent,
            final String parentName, final String name,
            final List<Document> values) {
        final Element element = addElement(parent, parentName, List.class);
        for (final Document value : values) {
            addElement(element, name, value);
        }
        return element;
    }

    public static final Element addDocumentVersionElements(final Element parent,
            final String parentName, final String name,
            final List<DocumentVersion> values) {
        final Element element = addElement(parent, parentName, List.class);
        for (final DocumentVersion value : values) {
            addElement(element, name, value);
        }
        return element;
    }

    /**
     * Add a byte array value.
     *
     * @param parent
     *      The parent element.
     * @param name
     *      The element name.
     * @param
     *      The element byte array value.
     * @return The element.
     */
    public static final Element addElement(final Element parent, final String name,
            final byte[] value) {
        try {
            return addElement(parent, name, byte[].class, encode(compress(value)));
        }
        catch(final IOException iox) { throw new RuntimeException(iox); }
    }

    /**
     * Add a calendar value.
     *
     * @param parent
     *      The parent element.
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     * @return The element.
     */
    public static final Element addElement(final Element parent, final String name,
            final Calendar value) {
        final Calendar valueGMT = DateUtil.getInstance(
                value.getTime(), new SimpleTimeZone(0, "GMT"));
        final String valueString = DateUtil.format(
                valueGMT, DateUtil.DateImage.ISO);
        return addElement(parent, name, Calendar.class, valueString);
    }

    public static final Element addElement(final Element parent,
            final String name, final Charset value) {
        if (null == value) {
            return addNullElement(parent, name, Charset.class);
        } else {
            final Element element = addElement(parent, name, value.getClass());
            element.setText(value.name());
            return element;
        }
    }

    public static final Element addElement(final Element parent,
            final String name, final Container value) {
        final Element element = addElement(parent, "container", Container.class);
        addElement(element, "createdBy", value.getCreatedBy());
        addElement(element, "createdOn", value.getCreatedOn());
        addElement(element, "draft", value.isDraft());
        addElement(element, "localDraft", value.isLocalDraft());
        addElement(element, "name", value.getName());
        addElement(element, "remoteInfo", value.getRemoteInfo());
        addElement(element, "state", value.getState());
        addElement(element, "type", value.getType());
        addElement(element, "uniqueId", value.getUniqueId());
        addElement(element, "updatedBy", value.getUpdatedBy());
        addElement(element, "updatedOn", value.getUpdatedOn());
        return element;
    }

    public static final Element addElement(final Element parent,
            final String name, final ContainerVersion value) {
        final Element element = addElement(parent, "containerVersion", ContainerVersion.class);
        addElement(element, "artifactType", value.getArtifactType());
        addElement(element, "artifactUniqueId", value.getArtifactUniqueId());
        addElement(element, "createdBy", value.getCreatedBy());
        addElement(element, "createdOn", value.getCreatedOn());
        addElement(element, "name", value.getName());
        addElement(element, "updatedBy", value.getUpdatedBy());
        addElement(element, "updatedOn", value.getUpdatedOn());
        addElement(element, "versionId", value.getVersionId());
        return element;
    }

    public static final Element addElement(final Element parent,
            final String name, final Document value) {
        final Element element = addElement(parent, "document", Document.class);
        addElement(element, "createdBy", value.getCreatedBy());
        addElement(element, "createdOn", value.getCreatedOn());
        addElement(element, "name", value.getName());
        addElement(element, "remoteInfo", value.getRemoteInfo());
        addElement(element, "state", value.getState());
        addElement(element, "type", value.getType());
        addElement(element, "uniqueId", value.getUniqueId());
        addElement(element, "updatedBy", value.getUpdatedBy());
        addElement(element, "updatedOn", value.getUpdatedOn());
        return element;
    }

    public static final Element addElement(final Element parent,
            final String name, final DocumentVersion value) {
        final Element element = addElement(parent, "documentVersion", DocumentVersion.class);
        addElement(element, "artifactType", value.getArtifactType());
        addElement(element, "artifactUniqueId", value.getArtifactUniqueId());
        addElement(element, "checksum", value.getChecksum());
        addElement(element, "compression", value.getCompression());
        addElement(element, "createdBy", value.getCreatedBy());
        addElement(element, "createdOn", value.getCreatedOn());
        addElement(element, "encoding", value.getEncoding());
        addElement(element, "name", value.getName());
        addElement(element, "updatedBy", value.getUpdatedBy());
        addElement(element, "updatedOn", value.getUpdatedOn());
        addElement(element, "versionId", value.getVersionId());
        return element;
    }

    /**
     * Add an email value.
     *
     * @param parent
     *      The parent element.
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     * @return The element.
     */
    public static final Element addElement(final Element parent, final String name,
            final EMail value) {
        final String valueString = value.toString();
        return addElement(parent, name, EMail.class, valueString);
    }

    /**
     * Add an integer value.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            An integer.
     * @return The element.
     */
    public static final Element addElement(final Element parent,
            final String name, final Integer value) {
        return addElement(parent, name, Integer.class, value.toString());
    }

    /**
     * Add a jabber id value.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            A jabber id.
     * @return The element.
     */
    public static final Element addElement(final Element parent, final String name,
            final JabberId value) {
        return addElement(parent, name, JabberId.class, value.getQualifiedJabberId());
    }

    /**
     * Add a long value.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            A long.
     * @return The element.
     */
    public static final Element addElement(final Element parent,
            final String name, final Long value) {
        return addElement(parent, name, Long.class, value.toString());
    }

    /**
     * Add a stream session element.
     * 
     * @param parent
     *            The parent dom4j <code>Element</code>.
     * @param name
     *            The element name <code>String</code>.
     * @param value
     *            The element value <code>StreamSession</code>.
     * @return A dom4j <code>Element</code>.
     */
    public static final Element addElement(final Element parent,
            final String name, final StreamSession value) {
        final Element element;
        if (null == value) {
            element = addNullElement(parent, name, StreamSession.class);
        } else {
            element = addElement(parent, name, StreamSession.class);
            addElement(element, "bufferSize", value.getBufferSize());
            addElement(element, "charset", value.getCharset());
            addElement(element, "environment", value.getEnvironment());
            addElement(element, "id", value.getId());
        }
        return element;
    }

    /**
     * Add a string value.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            A string.
     * @return The element.
     */
	public static final Element addElement(final Element parent, final String name,
            final String value) {
        return addElement(parent, name, String.class, value);
    }

    public static final Element addElement(final Element parent,
            final String name, final Token value) {
        final Element element;
        if (null == value) {
            element = addNullElement(parent, name, Token.class);
        } else {
            element = addElement(parent, name, Token.class);
            addElement(element, "value", value.getValue());
        }
        return element;
    }

    /**
     * Add a uuid value.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            A uuid.
     * @return The element.
     */
    public static final Element addElement(final Element parent, final String name,
            final UUID value) {
        return addElement(parent, name, UUID.class, value.toString());
    }

    /**
     * Add a list of string values.
     * 
     * @param parent
     *            The parent element.
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @param values
     *            A list of strings.
     * @return The root element added.
     */
    public static final Element addEMailElements(final Element parent,
            final String parentName, final String name,
            final List<EMail> values) {
        final Element element = addElement(parent, parentName, List.class);
        for(final EMail value : values) { addElement(element, name, value); }
        return element;
    }

    /**
     * Add a list of jabber id values.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            A list of jabber ids.
     * @return The root element added.
     */
    public static final Element addJabberIdElements(final Element parent,
            final String parentName, final String name, final List<JabberId> values) {
        final Element element = addElement(parent, parentName, List.class);
        for(final JabberId value : values) { addElement(element, name, value); }
        return element;
    }

    /**
     * Add a list of long values.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param value
     *            A list of longs.
     * @return The root element added.
     */
    public static final Element addLongElements(final Element parent,
            final String parentName, final String name, final List<Long> values) {
        final Element element = addElement(parent, parentName, List.class);
        for(final Long value : values) { addElement(element, name, value); }
        return element;
    }

    /**
     * Add a list of string values.
     * 
     * @param parent
     *            The parent element.
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @param values
     *            A list of strings.
     * @return The root element added.
     */
    public static final Element addStringElements(final Element parent,
            final String parentName, final String name,
            final List<String> values) {
        final Element element = addElement(parent, parentName, List.class);
        for(final String value : values) { addElement(element, name, value); }
        return element;
    }

    /**
     * Add a typed element to the parent.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @return The element.
     */
    protected static final Element addElement(final Element parent, final String name,
            final Class type) {
        final Element element = parent.addElement(name);
        element.addAttribute("javaType", type.getName());
        return element;
    }

    /**
     * Add a typed element to the parent.
     * 
     * @param parent
     *            The parent element.
     * @param name
     *            The element name.
     * @param type
     *            The element type.
     * @param value
     *            The element value.
     * @return The element.
     */
    protected static final Element addElement(final Element parent, final String name,
            final Class type, final String value) {
        final Element element = addElement(parent, name, type);
        element.setText(value);
        return element;
    }

    protected static final Element addNullElement(final Element parent,
            final String name, final Class type) {
        final Element element = parent.addElement(name);
        element.addAttribute("javaType", type.getName());
        return element;
    }

    /**
     * Compress a byte array.
     *
     * @param bytes
     *      The bytes to compress.
     * @return The compressed bytes.
     */
    protected static final byte[] compress(final byte[] bytes) throws IOException {
        return CompressionUtil.compress(bytes, CompressionUtil.Level.Nine);
    }

    /**
     * Encode a byte array.
     *
     * @param bytes.
     *      The bytes to encode.
     * @return A Base64 encoding of the bytes.
     */
    protected static final String encode(final byte[] bytes) {
        return Base64.encode(bytes);
    }

    private static final Element addElement(final Element parent,
            final String name, final ArtifactRemoteInfo value) {
        final Element element = addElement(parent, name, ArtifactRemoteInfo.class);
        addElement(element, "updatedBy", value.getUpdatedBy());
        addElement(element, "updatedOn", value.getUpdatedOn());
        return element;
    }

    private static final Element addElement(final Element parent,
            final String name, final ArtifactState value) {
        return addElement(parent, name, value.getClass(), value.toString());
    }

    private static final Element addElement(final Element parent,
            final String name, final ArtifactType value) {
        return addElement(parent, name, value.getClass(), value.toString());
    }

    private static final Element addElement(final Element parent,
            final String name, final Boolean value) {
        return addElement(parent, name, value.getClass(), value.toString());
    }

    /**
     * Add an enumerated type element.
     * 
     * @param parent
     *            The parent dom4j <code>Element</code>.
     * @param name
     *            The element name <code>String</code>.
     * @param value
     *            The element value <code>Enum&lt;?&gt;</code>.
     * @return A dom4j <code>Element</code>.
     */
    private static final Element addElement(final Element parent,
            final String name, final Enum<?> value) {
        if (null == value) {
            return addNullElement(parent, name, value.getClass());
        } else {
            final Element element = addElement(parent, name, value.getClass());
            element.setText(value.name());
            return element;
        }
    }

	/** Create ElementBuilder */
	protected ElementBuilder() { super(); }
}
