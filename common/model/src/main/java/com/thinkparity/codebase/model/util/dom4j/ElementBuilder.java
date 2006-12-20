/*
 * Created On: Dec 7, 2005
 */
package com.thinkparity.codebase.model.util.dom4j;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.UUID;
import java.util.Map.Entry;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactRemoteInfo;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.Token;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.user.UserVCard;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationExtendedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUpdatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContainerPublishedEvent;
import com.thinkparity.codebase.model.util.xstream.XStreamUtil;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.thoughtworks.xstream.io.xml.Dom4JWriter;

/**
 * <b>Title:</b>thinkParity Remote Element Builder <br>
 * <b>Description:</b>A dom element builder for the jive server plugins.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.5
 */
public class ElementBuilder {

    private static final DocumentFactory DOCUMENT_FACTORY;

    static {
        DOCUMENT_FACTORY = DocumentFactory.getInstance();
    }

    public static final Element addContainerElements(
            final XStreamUtil xstreamUtil, final Element parent,
            final String parentName, final String name,
            final List<Container> values) {
        if (values.size() < 1) {
            return addNullElement(parent, parentName, List.class);
        } else {
            final Element element = addElement(parent, parentName, List.class);
            for (final Container value : values) {
                addElement(xstreamUtil, element, name, value);
            }
            return element;
        }
    }

    public static final Element addContainerVersionElements(final Element parent,
            final String parentName, final String name,
            final List<ContainerVersion> values) {
        if (values.size() < 1) {
            return addNullElement(parent, parentName, List.class);
        } else {
            final Element element = addElement(parent, parentName, List.class);
            for (final ContainerVersion value : values) {
                addElement(element, name, value);
            }
            return element;
        }
    }

    public static final Element addDocumentElements(final Element parent,
            final String parentName, final String name,
            final List<Document> values) {
        if (values.size() < 1) {
            return addNullElement(parent, parentName, List.class);
        } else {
            final Element element = addElement(parent, parentName, List.class);
            for (final Document value : values) {
                addElement(element, name, value);
            }
            return element;
        }
    }

    public static final Element addDocumentVersionDeltaElements(
            final Element parent, final String name,
            final Map<DocumentVersion, Delta> values) {
        if (values.size() < 1) {
            return addNullMapElement(parent, name);
        } else {
            final Element element = addMapElement(parent, name);
            Element entryElement;
            for (final Entry<DocumentVersion, Delta> entry : values.entrySet()) {
                entryElement = addMapEntryElement(element);
                addElement(entryElement, "key", entry.getKey());
                addElement(entryElement, "value", entry.getValue());
            }
            return element;
        }
    }

    public static final Element addDocumentVersionElements(final Element parent,
            final String parentName, final String name,
            final List<DocumentVersion> values) {
        if (values.size() < 1) {
            return addNullElement(parent, parentName, List.class);
        } else {
            final Element element = addElement(parent, parentName, List.class);
            for (final DocumentVersion value : values) {
                addElement(element, name, value);
            }
            return element;
        }
    }

    /**
     * Add an artifact type element.
     * 
     * @param parent
     *            A parent <code>Element</code>.
     * @param name
     *            The element name <code>String</code>.
     * @param value
     *            The element <code>ArtifactType</code> value.
     * @return The new <code>Element</code>.
     */
    public static final Element addElement(final Element parent,
            final String name, final ArtifactType value) {
        if (null == value) {
            return addNullElement(parent, name, ArtifactType.class);
        } else {
            return addElement(parent, name, ArtifactType.class, value.toString());
        }
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
    public static final Element addElement(final Element parent,
            final String name, final Calendar value) {
        if (null == value) {
            return addNullElement(parent, name, Calendar.class);
        } else {
            final Calendar valueGMT = DateUtil.getInstance(
                    value.getTime(), new SimpleTimeZone(0, "GMT"));
            return addElement(parent, name, Calendar.class, DateUtil.format(
                    valueGMT, DateUtil.DateImage.ISO));
        }
    }

    public static final Element addElement(final Element parent,
            final String name, final Charset value) {
        if (null == value) {
            return addNullElement(parent, name, Charset.class);
        } else {
            return addElement(parent, name, Charset.class, value.name());
        }
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
    public static final Element addElement(final Element parent,
            final String name, final Class type) {
        final Element element = parent.addElement(name);
        applyCoreAttributes(element, type);
        return element;
    }

    public static final Element addElement(final XStreamUtil xstreamUtil,
            final Element parent, final String name, final Container value) {
        if (null == value) {
            return addNullElement(parent, name, Container.class);
        } else {
            final Element element = addElement(parent, name, value.getClass());
            final Dom4JWriter writer = new Dom4JWriter(element);
            xstreamUtil.marshal(value, writer);
            return element;
        }
    }

    public static final Element addElement(final Element parent,
            final String name, final ContainerVersion value) {
        if (null == value) {
            return addNullElement(parent, name, ContainerVersion.class);
        } else {
            final Element element = addElement(parent, name, ContainerVersion.class);
            addElement(element, "artifactType", value.getArtifactType());
            addElement(element, "artifactUniqueId", value.getArtifactUniqueId());
            addElement(element, "comment", value.getComment());
            addElement(element, "createdBy", value.getCreatedBy());
            addElement(element, "createdOn", value.getCreatedOn());
            addElement(element, "name", value.getName());
            addElement(element, "updatedBy", value.getUpdatedBy());
            addElement(element, "updatedOn", value.getUpdatedOn());
            addElement(element, "versionId", value.getVersionId());
            return element;
        }
    }

    public static final Element addElement(final Element parent,
            final String name, final Document value) {
        if (null == value) {
            return addNullElement(parent, name, Document.class);
        } else {
            final Element element = addElement(parent, name, Document.class);
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
    }

    public static final Element addElement(final Element parent,
            final String name, final DocumentVersion value) {
        if (null == value) {
            return addNullElement(parent, name, DocumentVersion.class);
        } else {
            final Element element = addElement(parent, name, DocumentVersion.class);
            addElement(element, "artifactType", value.getArtifactType());
            addElement(element, "artifactUniqueId", value.getArtifactUniqueId());
            addElement(element, "checksum", value.getChecksum());
            addElement(element, "compression", value.getCompression());
            addElement(element, "createdBy", value.getCreatedBy());
            addElement(element, "createdOn", value.getCreatedOn());
            addElement(element, "encoding", value.getEncoding());
            addElement(element, "name", value.getName());
            addElement(element, "size", value.getSize());
            addElement(element, "updatedBy", value.getUpdatedBy());
            addElement(element, "updatedOn", value.getUpdatedOn());
            addElement(element, "versionId", value.getVersionId());
            return element;
        }
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
    public static final Element addElement(final Element parent,
            final String name, final EMail value) {
        if (null == value) {
            return addNullElement(parent, name, EMail.class);
        } else {
            return addElement(parent, name, EMail.class, value.toString());
        }
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
        if (null == value) {
            return addNullElement(parent, name, Integer.class);
        } else {
            return addElement(parent, name, Integer.class, value.toString());
        }
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
    public static final Element addElement(final Element parent,
            final String name, final JabberId value) {
        if (null == value) {
            return addNullElement(parent, name, JabberId.class);
        } else {
            return addElement(parent, name, JabberId.class, value.getQualifiedUsername());
        }
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
        if (null == value) {
            return addNullElement(parent, name, Long.class);
        } else {
            return addElement(parent, name, Long.class, value.toString());
        }
    }

    public static final Element addElement(final Element parent,
            final String name, final ProfileEMail value) {
        if (null == value) {
            return addNullElement(parent, name, ProfileEMail.class);
        } else {
            return addElement(parent, name, ProfileEMail.class, value.toString());
        }
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
        if (null == value) {
            return addNullElement(parent, name, StreamSession.class);
        } else {
            final Element element = addElement(parent, name, StreamSession.class);
            addElement(element, "bufferSize", value.getBufferSize());
            addElement(element, "charset", value.getCharset());
            addElement(element, "environment", value.getEnvironment());
            addElement(element, "id", value.getId());
            return element;
        }
    }

    /**
     * Add a string element.
     * 
     * @param parent
     *            The parent <code>Element</code>.
     * @param name
     *            The element name <code>String</code>.
     * @param value
     *            A element value <code>String</code>
     * @return The new <code>Element</code>.
     */
	public static final Element addElement(final Element parent,
            final String name, final String value) {
        if (null == value) {
            return addNullElement(parent, name, String.class);
        } else {
            return addElement(parent, name, String.class, value);
        }
    }

    public static final Element addElement(final Element parent,
            final String name, final Token value) {
        if (null == value) {
            return addNullElement(parent, name, Token.class);
        } else {
            final Element element = addElement(parent, name, Token.class);
            addElement(element, "value", value.getValue());
            return element;
        }
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
    public static final Element addElement(final Element parent,
            final String name, final UUID value) {
        if (null == value) {
            return addNullElement(parent, name, UUID.class);
        } else {
            return addElement(parent, name, UUID.class, value.toString());
        }
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
        if (values.size() < 1) {
            return addNullElement(parent, parentName, List.class);
        } else {
            final Element element = addElement(parent, parentName, List.class);
            for (final EMail value : values) {
                addElement(element, name, value);
            }
            return element;
        }
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
        if (values.size() < 1) {
            return addNullElement(parent, parentName, List.class);
        } else {
            final Element element = addElement(parent, parentName, List.class);
            for (final JabberId value : values) {
                addElement(element, name, value);
            }
            return element;
        }
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
        if (values.size() < 1) {
            return addNullElement(parent, parentName, List.class);
        } else {
            final Element element = addElement(parent, parentName, List.class);
            for (final Long value : values) {
                addElement(element, name, value);
            }
            return element;
        }
    }

    public static final Element addMapElement(final Element parent,
            final String name) {
        return addElement(parent, name, Map.class);
    }

    public static final Element addNullElement(final Element parent,
            final String name, final Class type) {
        final Element element = parent.addElement(name);
        applyCoreAttributes(element, type);
        return element;
    }

    public static final Element addNullMapElement(final Element parent,
            final String name) {
        final Element element = parent.addElement(name);
        applyCoreAttributes(element, Map.class);
        return element;
    }

    public static final Element addProfileEMailElements(final Element parent,
            final String parentName, final String name,
            final List<ProfileEMail> values) {
        if (values.size() < 1) {
            return addNullElement(parent, parentName, List.class);
        } else {
            final Element element = addElement(parent, parentName, List.class);
            for (final ProfileEMail value : values) {
                addElement(element, name, value);
            }
            return element;
        }
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
        if (values.size() < 1) {
            return addNullElement(parent, parentName, List.class);
        } else {
            final Element element = addElement(parent, parentName, List.class);
            for (final String value : values) {
                addElement(element, name, value);
            }
            return element;
        }
    }

    public static final Element addVCardElement(final XStreamUtil xstreamUtil,
            final Element parent, final String name, final UserVCard value) {
        if (null == value) {
            return addNullElement(parent, name, UserVCard.class);
        } else {
            final Element element = addElement(parent, name, UserVCard.class);
            final Dom4JWriter writer = new Dom4JWriter(element);
            xstreamUtil.marshal(value, writer);
            return element;
        }
    }

    public static final Element addUserReceiptElements(
            final XStreamUtil xstreamUtil, final Element parent,
            final String name, final Map<User, ArtifactReceipt> values) {
        if (values.size() < 1) {
            return addNullMapElement(parent, name);
        } else {
            final Element element = addMapElement(parent, name);
            Element entryElement;
            for (final Entry<User, ArtifactReceipt> entry : values.entrySet()) {
                entryElement = addMapEntryElement(element);
                addElement(xstreamUtil, entryElement, "key", entry.getKey());
                addElement(xstreamUtil, entryElement, "value", entry.getValue());
            }
            return element;
        }
    }

    public static final Element createElement(final String name,
            final ArtifactDraftCreatedEvent value) {
        if (null == value) {
            return createNullElement(name, ArtifactDraftCreatedEvent.class);
        } else {
            final Element element = createElement(name, ArtifactDraftCreatedEvent.class);
            addElement(element, "createdBy", value.getCreatedBy());
            addElement(element, "createdOn", value.getCreatedOn());
            addElement(element, "uniqueId", value.getUniqueId());
            return element;
        }
    }

    public static final Element createElement(final String name,
            final ArtifactDraftDeletedEvent value) {
        if (null == value) {
            return createNullElement(name, ArtifactDraftDeletedEvent.class);
        } else {
            final Element element = createElement(name, ArtifactDraftDeletedEvent.class);
            addElement(element, "deletedBy", value.getDeletedBy());
            addElement(element, "deletedOn", value.getDeletedOn());
            addElement(element, "uniqueId", value.getUniqueId());
            return element;
        }
    }

    public static final Element createElement(final String name,
            final ArtifactPublishedEvent value) {
        if (null == value) {
            return createNullElement(name, ArtifactPublishedEvent.class);
        } else {
            final Element element = createElement(name, ArtifactPublishedEvent.class);
            addElement(element, "publishedBy", value.getPublishedBy());
            addElement(element, "publishedOn", value.getPublishedOn());
            addElement(element, "uniqueId", value.getUniqueId());
            addElement(element, "versionId", value.getVersionId());
            return element;
        }
    }

    public static final Element createElement(final String name,
            final ArtifactReceivedEvent value) {
        if (null == value) {
            return createNullElement(name, ArtifactReceivedEvent.class);
        } else {
            final Element element = createElement(name, ArtifactReceivedEvent.class);
            addElement(element, "receivedBy", value.getReceivedBy());
            addElement(element, "receivedOn", value.getReceivedOn());
            addElement(element, "uniqueId", value.getUniqueId());
            addElement(element, "versionId", value.getVersionId());
            return element;
        }
    }

    public static final Element createElement(final String name,
            final ArtifactTeamMemberAddedEvent value) {
        if (null == value) {
            return createNullElement(name, ArtifactTeamMemberAddedEvent.class);
        } else {
            final Element element = createElement(name, ArtifactTeamMemberAddedEvent.class);
            addElement(element, "jabberId", value.getJabberId());
            addElement(element, "uniqueId", value.getUniqueId());
            return element;
        }
    }

    public static final Element createElement(final String name,
            final ArtifactTeamMemberRemovedEvent value) {
        if (null == value) {
            return createNullElement(name, ArtifactTeamMemberRemovedEvent.class);
        } else {
            final Element element = createElement(name, ArtifactTeamMemberRemovedEvent.class);
            addElement(element, "jabberId", value.getJabberId());
            addElement(element, "uniqueId", value.getUniqueId());
            return element;
        }
    }

    public static final Element createElement(final String name,
            final ContactDeletedEvent value) {
        if (null == value) {
            return createNullElement(name, ContactDeletedEvent.class);
        } else {
            final Element element = createElement(name, ContactDeletedEvent.class);
            addElement(element, "deletedBy", value.getDeletedBy());
            addElement(element, "deletedOn", value.getDeletedOn());
            return element;
        }
    }

    public static final Element createElement(final String name,
            final ContactInvitationAcceptedEvent value) {
        if (null == value) {
            return createNullElement(name, ContactInvitationAcceptedEvent.class);
        } else {
            final Element element = createElement(name, ContactInvitationAcceptedEvent.class);
            addElement(element, "acceptedBy", value.getAcceptedBy());
            addElement(element, "acceptedOn", value.getAcceptedOn());
            return element;
        }
    }

    public static final Element createElement(final String name,
            final ContactInvitationDeclinedEvent value) {
        if (null == value) {
            return createNullElement(name, ContactInvitationDeclinedEvent.class);
        } else {
            final Element element = createElement(name, ContactInvitationDeclinedEvent.class);
            addElement(element, "declinedBy", value.getDeclinedBy());
            addElement(element, "declinedOn", value.getDeclinedOn());
            addElement(element, "invitedAs", value.getInvitedAs());
            return element;
        }
    }

    public static final Element createElement(final String name,
            final ContactInvitationDeletedEvent value) {
        if (null == value) {
            return createNullElement(name, ContactInvitationDeclinedEvent.class);
        } else {
            final Element element = createElement(name, ContactInvitationDeclinedEvent.class);
            addElement(element, "deletedBy", value.getDeletedBy());
            addElement(element, "deletedOn", value.getDeletedOn());
            addElement(element, "invitedAs", value.getInvitedAs());
            return element;
        }
    }

    public static final Element createElement(final String name,
            final ContactInvitationExtendedEvent value) {
        if (null == value) {
            return createNullElement(name, ContactInvitationExtendedEvent.class);
        } else {
            final Element element = createElement(name, ContactInvitationExtendedEvent.class);
            addElement(element, "invitedAs", value.getInvitedAs());
            addElement(element, "invitedBy", value.getInvitedBy());
            addElement(element, "invitedOn", value.getInvitedOn());
            return element;
        }
    }

    public static final Element createElement(final String name,
            final ContactUpdatedEvent value) {
        if (null == value) {
            return createNullElement(name, ContactUpdatedEvent.class);
        } else {
            final Element element = createElement(name, value.getClass());
            addElement(element, "contactId", value.getContactId());
            addElement(element, "updatedOn", value.getUpdatedOn());
            return element;
        }
    }

    public static final Element createElement(final String name,
            final ContainerArtifactPublishedEvent value) {
        if (null == value) {
            return createNullElement(name, ContainerArtifactPublishedEvent.class);
        } else {
            final Element element = createElement(name, value.getClass());
            addElement(element, "artifactChecksum", value.getArtifactChecksum());
            addElement(element, "artifactCount", value.getArtifactCount());
            addElement(element, "artifactIndex", value.getArtifactIndex());
            addElement(element, "artifactName", value.getArtifactName());
            addElement(element, "streamId", value.getArtifactStreamId());
            addElement(element, "artifactType", value.getArtifactType());
            addElement(element, "uniqueId", value.getArtifactUniqueId());
            addElement(element, "versionId", value.getArtifactVersionId());
            addElement(element, "name", value.getName());
            addElement(element, "publishedBy", value.getPublishedBy());
            addElement(element, "publishedOn", value.getPublishedOn());
            addElement(element, "uniqueId", value.getUniqueId());
            addElement(element, "versionId", value.getVersionId());
            return element;
        }
    }

    public static final Element createElement(final String name,
            final ContainerPublishedEvent value) {
        if (null == value) {
            return createNullElement(name, ContainerArtifactPublishedEvent.class);
        } else {
            final Element element = createElement(name, value.getClass());
            addElement(element, "artifactCount", value.getArtifactCount());
            addElement(element, "comment", value.getComment());
            addElement(element, "name", value.getName());
            addElement(element, "publishedBy", value.getPublishedBy());
            addElement(element, "publishedOn", value.getPublishedOn());
            addJabberIdElements(element, "publishedTo", value.getPublishedTo());
            addElement(element, "uniqueId", value.getUniqueId());
            addElement(element, "versionId", value.getVersionId());
            return element;
        }
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

    /**
     * Add a typed element to the dom.
     * 
     * @param parent
     *            The parent <code>Element</code>.
     * @param name
     *            The element name <code>String</code>.
     * @param type
     *            The element type <code>Class</code>.
     * @param value
     *            The element value <code>String</code>.
     * @return The new <code>Element</code>.
     */
    private static final Element addElement(final Element parent,
            final String name, final Class type, final String value) {
        Assert.assertNotNull(value, "Cannot add a null value:  {0} - {1} - {2}",
                parent, name, type);
        final Element element = addElement(parent, name, type);
        element.setText(value);
        return element;
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

    private static final Element addElement(final XStreamUtil xstreamUtil,
            final Element parent, final String name, final ArtifactReceipt value) {
        if (null == value) {
            return addNullElement(parent, name, ArtifactReceipt.class);
        } else {
            final Element element = addElement(parent, name, value.getClass());
            final Dom4JWriter writer = new Dom4JWriter(element);
            xstreamUtil.marshal(value, writer);
            return element;
        }
    }

    private static final Element addElement(final XStreamUtil xstreamUtil,
            final Element parent, final String name, final User value) {
        if (null == value) {
            return addNullElement(parent, name, User.class);
        } else {
            final Element element = addElement(parent, name, value.getClass());
            final Dom4JWriter writer = new Dom4JWriter(element);
            xstreamUtil.marshal(value, writer);
            return element;
        }
    }

    private static final Element addJabberIdElements(final Element parent,
            final String name, final List<JabberId> values) {
        if (values.size() < 1) {
            return addNullElement(parent, name, List.class);
        } else {
            final String childName = MessageFormat.format("{0}-element", name);
            final Element element = addElement(parent, name, List.class);
            for (final JabberId value : values) {
                addElement(element, childName, value);
            }
            return element;
        }
    }

    private static final Element addMapEntryElement(final Element parent) {
        return addElement(parent, "entry", Map.Entry.class);
    }

    private static void applyCoreAttributes(final Element element,
            final Class type) {
        element.addAttribute("javaType", type.getName());
    }

    private static final Element createElement(final String name, final Class type) {
        final Element element = DOCUMENT_FACTORY.createElement(name);
        applyCoreAttributes(element, type);
        return element;
    }

    private static final Element createNullElement(final String name, final Class type) {
        return createElement(name, type);
    }

	/** Create ElementBuilder */
	protected ElementBuilder() { super(); }
}
