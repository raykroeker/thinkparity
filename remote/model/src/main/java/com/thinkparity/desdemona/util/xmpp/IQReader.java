/*
 * Created On: Jun 22, 2006 2:53:27 PM
 */
package com.thinkparity.desdemona.util.xmpp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.UUID;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.profile.ProfileVCard;

import com.thinkparity.desdemona.util.service.ServiceRequestReader;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

/**
 * <b>Title:</b>thinkParity Model IQ Reader <br>
 * <b>Description:</b>An xmpp internet query reader for the model.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public final class IQReader implements ServiceRequestReader {

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
        final String sData = readString(name);
        try {
            return DateUtil.parse(sData, DateUtil.DateImage.ISO,
                    new SimpleTimeZone(0, "GMT"));
        }
        catch(final ParseException px) { throw new RuntimeException(px); }
    }

    public final EMail readEMail(final String name) {
        return EMailBuilder.parse(readString(name));
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
     * @see com.thinkparity.desdemona.util.service.ServiceRequestReader#readProfileVCard(java.lang.String)
     *
     */
    public final ProfileVCard readProfileVCard(final String name) {
        final ProfileVCard vcard = new ProfileVCard();
        vcard.setVCardXML(readString(name));
        return vcard;
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
}
