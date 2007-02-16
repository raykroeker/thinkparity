/*
 * Created On: Jun 22, 2006 2:53:27 PM
 */
package com.thinkparity.desdemona.util.xmpp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.Constants.XmlRpc;
import com.thinkparity.codebase.DateUtil.DateImage;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xstream.XStreamUtil;

import com.thinkparity.desdemona.util.service.ServiceRequestReader;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thoughtworks.xstream.io.xml.Dom4JReader;

/**
 * <b>Title:</b>thinkParity Model IQ Reader <br>
 * <b>Description:</b>An xmpp internet query reader for the model.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public final class IQReader implements ServiceRequestReader {

    /** The local <code>Locale</code>. */
    private static final Locale LOCALE;

    /** The local <code>TimeZone</code>. */
    private static final TimeZone TIME_ZONE;

    /** The universal <code>DateFormat</code>. */
    private static final DateFormat UNIVERSAL_FORMAT;

    private static final XStreamUtil XSTREAM_UTIL;
    
    static {
        LOCALE = Locale.getDefault();
        TIME_ZONE = TimeZone.getDefault();
        final DateImage universalImage = DateImage.ISO;
        final String universalPattern = universalImage.toString();
        final TimeZone universalTimeZone = TimeZone.getTimeZone("Universal");
        UNIVERSAL_FORMAT = new SimpleDateFormat(universalPattern);
        UNIVERSAL_FORMAT.setTimeZone(universalTimeZone);
        XSTREAM_UTIL = XStreamUtil.getInstance();
    }

    /** The xmpp internet query <code>IQ</code>. */
    private final IQ iq;

    /**
     * Create IQReader.
     * 
     * @param iq
     *            An xmpp internet query <code>IQ</code>.
     */
    public IQReader(final IQ iq) {
        super();
        this.iq = iq;
    }

    /**
     * Read an artifact type parameter.
     * 
     * @param name
     *            The parameter name.
     * @return The artifact type.
     */
    public final ArtifactType readArtifactType(final String name) {
        final String sData = readString(name);
        if(null == sData) { return null; }
        else { return ArtifactType.valueOf(sData); }
    }

    public final Calendar readCalendar(final String name) {
        final String universalDateTime = readString(name);
        try {
            if (null == universalDateTime) {
                return null;
            } else {
                final Date localDate = ((DateFormat) UNIVERSAL_FORMAT.clone()).parse(universalDateTime);
                final Calendar localCalendar = Calendar.getInstance(TIME_ZONE, LOCALE);
                localCalendar.setTimeInMillis(localDate.getTime());
                return localCalendar;
            }
        } catch (final ParseException px) {
            throw new RuntimeException(px);
        }
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readContacts(java.lang.String)
     *
     */
    public final List<Contact> readContacts(final String name) {
        final Element element = iq.getChildElement().element(name);
        final Iterator iChildren = element.elementIterator("element");
        final List<Contact> contacts = new ArrayList<Contact>();
        Dom4JReader dom4JReader;
        while (iChildren.hasNext()) {
            dom4JReader = new Dom4JReader((Element) iChildren.next());
            try {
                dom4JReader.moveDown();
                contacts.add((Contact) XSTREAM_UTIL.unmarshal(dom4JReader, new Contact()));
            } finally {
                dom4JReader.moveUp();
                dom4JReader.close();
            }
        }
        return contacts;    
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readContainerVersion(java.lang.String)
     *
     */
    public final ContainerVersion readContainerVersion(final String name) {
        final Element vcardElement = iq.getChildElement().element(name);
        final Dom4JReader xmlReader = new Dom4JReader(vcardElement);
        xmlReader.moveDown();
        try {
            final ContainerVersion containerVersion = new ContainerVersion();
            XSTREAM_UTIL.unmarshal(xmlReader, containerVersion);
            return containerVersion;
        } finally {
            xmlReader.moveUp();
            xmlReader.close();
        }
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readDocumentVersions(java.lang.String)
     *
     */
    public final List<DocumentVersion> readDocumentVersions(final String name) {
        final Element element = iq.getChildElement().element(name);
        final Iterator iChildren = element.elementIterator("element");
        final List<DocumentVersion> documentVersions = new ArrayList<DocumentVersion>();
        Dom4JReader dom4JReader;
        while (iChildren.hasNext()) {
            dom4JReader = new Dom4JReader((Element) iChildren.next());
            try {
                dom4JReader.moveDown();
                documentVersions.add((DocumentVersion) XSTREAM_UTIL.unmarshal(dom4JReader, new DocumentVersion()));
            } finally {
                dom4JReader.moveUp();
                dom4JReader.close();
            }
        }
        return documentVersions;
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readStrings(java.lang.String)
     *
     */
    public final Map<DocumentVersion, String> readDocumentVersionsStreamIds(
            final String name) {
        final Element element = iq.getChildElement().element(name);
        final Iterator iKeys = element.elementIterator("key");
        final Iterator iValues = element.elementIterator("value");
        final Map<DocumentVersion, String> documentVersions = new HashMap<DocumentVersion, String>();
        Dom4JReader keyReader;
        while (iKeys.hasNext() && iValues.hasNext()) {
            keyReader = new Dom4JReader((Element) iKeys.next());
            try {
                keyReader.moveDown();
                documentVersions.put(
                        (DocumentVersion) XSTREAM_UTIL.unmarshal(keyReader, new DocumentVersion()),
                        (String) ((Element) iValues.next()).getData());
            } finally {
                keyReader.moveUp();
            }
        }
        return documentVersions;
    }

    public final EMail readEMail(final String name) {
        return EMailBuilder.parse(readString(name));
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readEMails(java.lang.String)
     *
     */
    public final List<EMail> readEMails(final String name) {
        final Element element = iq.getChildElement().element(name);
        final Iterator iChildren = element.elements("element").iterator();
        final List<EMail> emails = new ArrayList<EMail>();
        while (iChildren.hasNext()) {
            emails.add(EMailBuilder.parse((String) ((Element) iChildren.next()).getData()));
        }
        return emails;
    }

    /**
     * Read an integer parameter from the internet query.
     * 
     * @param name
     *            The parameter name.
     * @return An integer value.
     */
    public final Integer readInteger(final String name) {
        final String sData = readString(name);
        if(null == sData) { return null; }
        else { return Integer.valueOf(sData); }
    }

    /**
     * Read jabber id data.
     * 
     * @param name
     *            The element name.
     * @return The data; or null if the data does not exist.
     */
    public final JabberId readJabberId(final String name) {
        final String value = readString(name);
        if (null == value) {
            return null;
        }
        else {
            return JabberIdBuilder.parse(value);
        }
    }

    /**
     * Read a list of jabber ids.
     * 
     * @param parentName
     *            The parent parameter name.
     * @param name
     *            The parameter name.
     * @return A list of jabber ids.
     */
    public final List<JabberId> readJabberIds(final String parentName,
            final String name) {
        final Element element = iq.getChildElement().element(parentName);
        final Iterator iChildren = element.elementIterator(name);
        final List<JabberId> jabberIds = new ArrayList<JabberId>();
        while(iChildren.hasNext()) {
            jabberIds.add(JabberIdBuilder.parse(((String) ((Element) iChildren.next()).getData())));
        }
        return jabberIds;
    }

    /**
     * Read long data.
     *
     * @param name
     *      The element name.
     * @return The data; or null if the data does not exist.
     */
    public final Long readLong(final String name) {
        final String sData = readString(name);
        if(null == sData) { return null; }
        else { return Long.valueOf(sData); }
    }

    /**
     * Read long data.
     *
     * @param parentName
     *      The parent element name.
     * @param name
     *      The element name.
     * @return The data; or null if the data does not exist.
     */
    public final List<Long> readLongs(final String parentName, final String name) {
        final Element element = iq.getChildElement().element(parentName);
        final Iterator iChildren = element.elementIterator(name);
        final List<Long> longs = new LinkedList<Long>();
        while(iChildren.hasNext()) {
            longs.add(Long.valueOf((String) ((Element) iChildren.next()).getData()));
        }
        return longs;
    }

    /**
     * Read the variable names within the iq.
     * 
     * @return A list of the variable names.
     */
    public final List<String> readNames() {
        final List elements = iq.getChildElement().elements();
        final List<String> elementNames = new LinkedList<String>();
        for(final Object o : elements) {
            elementNames.add(((Element) o).getName());
        }
        return elementNames;
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readOs(java.lang.String)
     *
     */
    public OS readOs(final String name) {
        final String value = readString(name);
        if (null == value) {
            return null;
        } else {
            return OS.valueOf(value);
        }
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readProduct(java.lang.String)
     *
     */
    public Product readProduct(final String name) {
        return (Product) readXStreamObject(name, new Product());
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readProfileVCard(java.lang.String)
     *
     */
    public final ProfileVCard readProfileVCard(final String name) {
        final Element vcardElement = iq.getChildElement().element(name);
        final Dom4JReader xmlReader = new Dom4JReader(vcardElement);
        xmlReader.moveDown();
        try {
            final ProfileVCard vcard = new ProfileVCard();
            XSTREAM_UTIL.unmarshal(xmlReader, vcard);
            return vcard;
        } finally {
            xmlReader.moveUp();
            xmlReader.close();
        }
    }

    public final Release readRelease(final String name) {
        return (Release) readXStreamObject(name, new Release());
    }

    public final List<Resource> readResources(final String name) {
        final Element element = iq.getChildElement().element(name);
        final Iterator iChildren = element.elementIterator(XmlRpc.LIST_ITEM);
        final List<Resource> resources = new ArrayList<Resource>();
        Dom4JReader dom4JReader;
        while (iChildren.hasNext()) {
            dom4JReader = new Dom4JReader((Element) iChildren.next());
            try {
                dom4JReader.moveDown();
                resources.add((Resource) XSTREAM_UTIL.unmarshal(dom4JReader, new Resource()));
            } finally {
                dom4JReader.moveUp();
                dom4JReader.close();
            }
        }
        return resources;
    }


    /**
     * Read string data.
     *
     * @param name
     *      The element name.
     * @return The data; or null if the data does not exist.
     */
    public final String readString(final String name) {
        return (String) readObject(name);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readTeamMembers(java.lang.String)
     *
     */
    public final List<TeamMember> readTeamMembers(final String name) {
        final Element element = iq.getChildElement().element(name);
        final Iterator iChildren = element.elementIterator("element");
        final List<TeamMember> teamMembers = new ArrayList<TeamMember>();
        Dom4JReader dom4JReader;
        while (iChildren.hasNext()) {
            dom4JReader = new Dom4JReader((Element) iChildren.next());
            try {
                dom4JReader.moveDown();
                teamMembers.add((TeamMember) XSTREAM_UTIL.unmarshal(dom4JReader, new TeamMember()));
            } finally {
                dom4JReader.moveUp();
                dom4JReader.close();
            }
        }
        return teamMembers;
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readUsers(java.lang.String)
     *
     */
    public final List<User> readUsers(final String name) {
        final Element element = iq.getChildElement().element(name);
        final Iterator iChildren = element.elementIterator("element");
        final List<User> users = new ArrayList<User>();
        Dom4JReader dom4JReader;
        while (iChildren.hasNext()) {
            dom4JReader = new Dom4JReader((Element) iChildren.next());
            try {
                dom4JReader.moveDown();
//                users.add((User) XSTREAM_UTIL.unmarshal(dom4JReader, new User()));
                users.add((User) XSTREAM_UTIL.unmarshal(dom4JReader));
            } finally {
                dom4JReader.moveUp();
                dom4JReader.close();
            }
        }
        return users;
    }

    /**
     * Read a unique id.
     * 
     * @param name
     *            The element name.
     * @return The element value.
     */
    public final UUID readUUID(final String name) {
        final String sData = readString(name);
        if(null == sData) { return null; }
        else { return UUID.fromString(sData); }
    }

    /**
     * Read object data.
     *
     * @param name
     *      The element name.
     * @return The element data; or null if the element does not exist.
     */
    protected final Object readObject(final String name) {
        final Element e = iq.getChildElement().element(name);
        if (null == e) {
            return null;
        } else {
            return e.getData();
        }
    }
    private final <T extends Object> T readXStreamObject(final String name,
            final T object) {
        final Element element = iq.getChildElement().element(name);
        final Dom4JReader xmlReader = new Dom4JReader(element);
        xmlReader.moveDown();
        try {
            XSTREAM_UTIL.unmarshal(xmlReader, object);
            return object;
        } finally {
            xmlReader.moveUp();
            xmlReader.close();
        }
    }
}
