/*
 * Created On: Jun 22, 2006 2:53:27 PM
 */
package com.thinkparity.desdemona.util.xmpp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.Constants.XmlRpc;
import com.thinkparity.codebase.DateUtil.DateImage;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.TemporaryCredentials;
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

    /** An instance of <code>XStreamUtil</code>. */
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
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readArtifactReceipts(java.lang.String)
     *
     */
    public List<ArtifactReceipt> readArtifactReceipts(final String name) {
        final Element element = iq.getChildElement().element(name);
        final Iterator iChildren = element.elementIterator(XmlRpc.LIST_ITEM);
        final List<ArtifactReceipt> artifactReceipts = new ArrayList<ArtifactReceipt>();
        Dom4JReader dom4JReader;
        while (iChildren.hasNext()) {
            dom4JReader = new Dom4JReader((Element) iChildren.next());
            try {
                dom4JReader.moveDown();
                artifactReceipts.add((ArtifactReceipt) XSTREAM_UTIL.unmarshal(dom4JReader, new ArtifactReceipt()));
            } finally {
                dom4JReader.moveUp();
                dom4JReader.close();
            }
        }
        return artifactReceipts;
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readCalendar(java.lang.String)
     * 
     */
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
            throw new ThinkParityException(px);
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
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readCredentials(java.lang.String)
     *
     */
    public Credentials readCredentials(final String name) {
        return (Credentials) readXStreamObject(name, new Credentials());
    }

    public TemporaryCredentials readTemporaryCredentials(final String name) {
        return (TemporaryCredentials) readXStreamObject(name, new TemporaryCredentials());
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
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readDocumentVersionsStreamIds(java.lang.String)
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

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readEMail(java.lang.String)
     * 
     */
    public final EMail readEMail(final String name) {
        return EMailBuilder.parse(readString(name));
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readReservation(java.lang.String)
     *
     */
    public EMailReservation readEMailReservation(final String name) {
        return (EMailReservation) readXStreamObject(name, new EMailReservation());
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
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readError(java.lang.String)
     *
     */
    public Error readError(String name) {
        return (Error) readXStreamObject(name, new Error());
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readIncomingInvitation(java.lang.String)
     * 
     */
    public IncomingEMailInvitation readIncomingEMailInvitation(final String name) {
        return (IncomingEMailInvitation) readXStreamObject(name, new IncomingEMailInvitation());
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readIncomingInvitation(java.lang.String)
     * 
     */
    public IncomingUserInvitation readIncomingUserInvitation(final String name) {
        return (IncomingUserInvitation) readXStreamObject(name, new IncomingUserInvitation());
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readInteger(java.lang.String)
     * 
     */
    public final Integer readInteger(final String name) {
        final String sData = readString(name);
        if(null == sData) { return null; }
        else { return Integer.valueOf(sData); }
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readJabberId(java.lang.String)
     * 
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
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readJabberIds(java.lang.String,
     *      java.lang.String)
     * 
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
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readLong(java.lang.String)
     * 
     */
    public final Long readLong(final String name) {
        final String sData = readString(name);
        if(null == sData) { return null; }
        else { return Long.valueOf(sData); }
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readNames()
     * 
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
    public final OS readOs(final String name) {
        final String value = readString(name);
        if (null == value) {
            return null;
        } else {
            return OS.valueOf(value);
        }
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readOutgoingEMailInvitation(java.lang.String)
     * 
     */
    public OutgoingEMailInvitation readOutgoingEMailInvitation(final String name) {
        return (OutgoingEMailInvitation) readXStreamObject(name, new OutgoingEMailInvitation());
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readOutgoingUserInvitation(java.lang.String)
     * 
     */
    public OutgoingUserInvitation readOutgoingUserInvitation(final String name) {
        return (OutgoingUserInvitation) readXStreamObject(name, new OutgoingUserInvitation());
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readProduct(java.lang.String)
     * 
     */
    public final Product readProduct(final String name) {
        return (Product) readXStreamObject(name, new Product());
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readProfile(java.lang.String)
     *
     */
    public Profile readProfile(final String name) {
        return (Profile) readXStreamObject(name, new Profile());
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

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readRelease(java.lang.String)
     * 
     */
    public final Release readRelease(final String name) {
        return (Release) readXStreamObject(name, new Release());
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readResources(java.lang.String)
     * 
     */
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
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readString(java.lang.String)
     * 
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
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readReservation(java.lang.String)
     *
     */
    public UsernameReservation readUsernameReservation(final String name) {
        return (UsernameReservation) readXStreamObject(name, new UsernameReservation());
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
                users.add((User) XSTREAM_UTIL.unmarshal(dom4JReader));
            } finally {
                dom4JReader.moveUp();
                dom4JReader.close();
            }
        }
        return users;
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readUUID(java.lang.String)
     * 
     */
    public final UUID readUUID(final String name) {
        final String sData = readString(name);
        if(null == sData) { return null; }
        else { return UUID.fromString(sData); }
    }

    /**
     * Read the object data from the element.
     * 
     * @param name
     *            An element name <code>String</code>.
     * @return The element data <code>Object</code>.
     */
    protected final Object readObject(final String name) {
        final Element e = iq.getChildElement().element(name);
        if (null == e) {
            return null;
        } else {
            return e.getData();
        }
    }

    /**
     * Use the xstreaming utility to unmarshal an object from the xml.
     * 
     * @param name
     *            An element name <code>String</code>.
     * @param object
     *            An <code>Object</code> root.
     * @return The unmarshalled <code>Object</code>.
     */
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
