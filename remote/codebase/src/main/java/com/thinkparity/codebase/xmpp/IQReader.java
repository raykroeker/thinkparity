/*
 * Created On: Thu May 11 2006 11:41 PDT
 */
package com.thinkparity.codebase.xmpp;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.zip.DataFormatException;

import com.thinkparity.codebase.CompressionUtil;
import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.util.Base64;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

/**
 * <b>Title:</b>thinkParity Remote IQ Reader <br>
 * <b>Description:</b> Reads jive server iq packets.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.4
 */
public abstract class IQReader {

    /** The xmpp internet query backing this reader. */
    protected final IQ iq;

    /**
     * Create IQReader.
     *
     * @param iq
     *      The internet query.
     */
    protected IQReader(final IQ iq) {
        super();
        this.iq = iq;
    }

    public final byte[] readByteArray(final String name) {
        final String sData = readString(name);
        if(null == sData) { return null; }
        else {
            try { return decompress(decode(sData)); }
            catch(final DataFormatException dfx) { throw new RuntimeException(dfx); }
            catch(final IOException iox) { throw new RuntimeException(iox); }
        }
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
        final List<JabberId> jabberIds = new LinkedList<JabberId>();
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
     * Decode a Base64 encoded string into an array of bytes.
     *
     * @param s
     *      A Base64 encoded string.
     * @return An array of bytes.
     */
    protected final byte[] decode(final String s) { return Base64.decode(s); }

    /**
     * Decompress an array of bytes into its object array.
     *
     * @param bytes
     *      An array of bytes.
     * @return An array of bytes.
     */
    protected final byte[] decompress(final byte[] bytes) throws DataFormatException,
            IOException {
        return CompressionUtil.decompress(bytes);
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
