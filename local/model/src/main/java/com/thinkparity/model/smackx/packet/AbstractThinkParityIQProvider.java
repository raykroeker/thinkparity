/*
 * Created On: Jul 6, 2006 12:02:54 PM
 */
package com.thinkparity.model.smackx.packet;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.Stack;
import java.util.UUID;
import java.util.zip.DataFormatException;

import org.jivesoftware.smack.provider.IQProvider;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.thinkparity.codebase.CompressionUtil;
import com.thinkparity.codebase.DateUtil;

import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.util.Base64;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;

/**
 * <b>Title:</b>thinkParity Abstract Internet Query<br>
 * <b>Description:</b>A thinkparity internet query provides an abstraction of a
 * jive internet query wrapper used to store data members when handling remote
 * events.
 * 
 * @author raymond@thinkparity.com
 * @version
 */
public abstract class AbstractThinkParityIQProvider implements IQProvider {

    private final Stack<String> nameStack;

    /** The xml parser. */
    private XmlPullParser parser;

    /** Create AbstractThinkParityIQ. */
    protected AbstractThinkParityIQProvider() {
        super();
        this.nameStack = new Stack<String>();
    }

    /**
     * Determine if the parser is currently pointed at the end tag for the
     * given name.
     * 
     * @param parser
     *            The xml parser.
     * @param name
     *            The tag name.
     * @return True if the parser is at the start of the given name.
     * @throws XmlPullParserException
     */
    protected Boolean isEndTag(final String name)
            throws XmlPullParserException {
        return XmlPullParser.END_TAG == parser.getEventType() &&
                name.equals(parser.getName());
    }

    /**
     * Determine if the parser is currently pointed at the start tag for the
     * given name.
     * 
     * @param parser
     *            The xml parser.
     * @param name
     *            The tag name.
     * @return True if the parser is at the start of the given name.
     * @throws XmlPullParserException
     */
    protected Boolean isStartTag(final String name)
            throws XmlPullParserException {
        return XmlPullParser.START_TAG == parser.getEventType() &&
                name.equals(parser.getName());
    }

    /**
     * Move the parser next a given number of steps.
     * 
     * @param count
     *            The number of steps to move next.
     */
    protected void next(final Integer count) throws IOException,
            XmlPullParserException {
        for(int i = 0; i < count; i++) { parser.next(); }
    }

    /**
     * Read an artifact type at the current parser location.
     * 
     * @return An artifact type.
     */
    protected final ArtifactType readArtifactType() {
        return ArtifactType.valueOf(readString());
    }

    /**
     * Read an artifact type at the current parser location.
     * 
     * @return An artifact type.
     */
    protected final ArtifactType readArtifactType2() throws IOException,
            XmlPullParserException {
        return ArtifactType.valueOf(readString2());
    }

    /**
     * Read a byte array at the current parser location.
     * 
     * @return A byte array.
     */
    protected final byte[] readBytes() throws DataFormatException, IOException {
        return CompressionUtil.decompress(Base64.decodeBytes(readString()));
    }

    protected final byte[] readBytes2() throws DataFormatException,
            IOException, XmlPullParserException {
        pushName();
        next(1);
        final byte[] bytes = readBytes();
        next(1);
        if (isEndTag(popName())) {
            next(1);
        }
        return bytes;
    }

    /**
     * Read a calendar at the current parser location.
     * 
     * @return A calendar.
     */
    protected final Calendar readCalendar() throws ParseException {
        return DateUtil.parse(readString(), DateUtil.DateImage.ISO,
                new SimpleTimeZone(0, "GMT"));
    }

    /**
     * Read a calendar at the current parser location.
     * 
     * @return A calendar.
     */
    protected final Calendar readCalendar2() throws IOException,
            ParseException, XmlPullParserException {
        return DateUtil.parse(readString2(), DateUtil.DateImage.ISO,
                new SimpleTimeZone(0, "GMT"));
    }

    /**
     * Read an integer at the current parser location.
     * 
     * @return An integer.
     */
    protected final Integer readInteger() {
        return Integer.valueOf(readString());
    }

    /**
     * Read an integer at the current parser location.
     * 
     * @return An integer.
     */
    protected final Integer readInteger2() throws IOException,
            XmlPullParserException {
        return Integer.valueOf(readString2());
    }

    /**
     * Read a jabber id at the current parser location.
     * 
     * @return A jabber id.
     */
    protected final JabberId readJabberId() {
        return JabberIdBuilder.parseQualifiedJabberId(readString());
    }

    /**
     * Read a jabber id at the current parser location.
     * 
     * @return A jabber id.
     */
    protected final JabberId readJabberId2() throws IOException,
            XmlPullParserException {
        return JabberIdBuilder.parseQualifiedJabberId(readString2());
    }

    /**
     * Read a list of jabber ids.
     * 
     * @return A list of jabber ids.
     * @throws IOException
     * @throws XmlPullParserException
     */
    protected final List<JabberId> readJabberIds() throws IOException,
            XmlPullParserException {
        final List<JabberId> jabberIds = new ArrayList<JabberId>();
        final String name = parser.getName();

        next(1);
        while (!isEndTag(name)) {
            next(1);
            jabberIds.add(readJabberId());
            next(1);
        }
        next(1);
        
        return jabberIds;
    }

    /**
     * Read a list of jabber ids.
     * 
     * @return A list of jabber ids.
     * @throws IOException
     * @throws XmlPullParserException
     */
    protected final List<JabberId> readJabberIds2() throws IOException,
            XmlPullParserException {
        final List<JabberId> jabberIds = new ArrayList<JabberId>();
        pushName();

        next(1);
        while (!isEndTag(peekName())) {
            next(1);
            jabberIds.add(readJabberId());
            next(2);
        }

        if (isEndTag(popName())) {
            next(1);
        }
        
        return jabberIds;
    }

    /**
     * Read a long at the current parser location.
     * 
     * @return A long.
     */
    protected final Long readLong() {
        return Long.valueOf(readString());
    }

    /**
     * Read a long at the current parser location.
     * 
     * @return A long.
     */
    protected final Long readLong2() throws IOException, XmlPullParserException {
        return Long.valueOf(readString2());
    }

    /**
     * Read a string at the current parser location.
     * 
     * @return A string.
     */
    protected final String readString() {
        return parser.getText();
    }

    protected final String readString2() throws IOException,
            XmlPullParserException {
        pushName();
        next(1);
        final String string = readString();
        next(1);
        if (isEndTag(popName())) {
            next(1);
        }
        return string;
    }

    /**
     * Read a unique id at the current parser location.
     * 
     * @return A unique id.
     */
    protected final UUID readUniqueId() {
        return UUID.fromString(readString());
    }

    /**
     * Read a unique id at the current parser location.
     * 
     * @return A unique id.
     */
    protected final UUID readUniqueId2() throws IOException,
            XmlPullParserException {
        return UUID.fromString(readString2());
    }

    /**
     * Set the parser.
     * 
     * @param parser
     *            The xml parser.
     */
    protected void setParser(final XmlPullParser parser) {
        this.parser = parser;
    }

    /**
     * Set the parser.
     * 
     * @param parser
     *            The xml parser.
     */
    protected void setParser2(final XmlPullParser parser) throws IOException,
            XmlPullParserException {
        this.parser = parser;
        next(1);
    }

    private String popName() {
        return nameStack.pop();
    }

    private String peekName() {
        return nameStack.peek();
    }

    private void pushName() {
        nameStack.push(parser.getName());
    }
}
