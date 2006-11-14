/*
 * Created On:  11-Nov-06 7:27:25 PM
 */
package com.thinkparity.ophelia.model.util.xstream;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.thoughtworks.xstream.converters.ErrorWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.AbstractPullReader;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;

/**
 * <b>Title:</b>thinkParity Smack XPP XStream XML Reader<br>
 * <b>Description:</b>A custom XStream xml reader for use with the smack jabber
 * library specifically.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class SmackXppReader extends AbstractPullReader {

    /** The <code>XmlPullParser</code>. */
    private final XmlPullParser parser;

    /**
     * Create XppReader.
     * 
     * @param parser
     *            An <code>XmlPullParser</code>.
     */
    public SmackXppReader(final XmlPullParser parser) {
        this(parser, new XmlFriendlyReplacer());
    }

    /**
     * Create XppReader.
     * 
     * @param parser
     *            An <code>XmlPullParser</code>.
     * @param replacer
     *            An <code>XmlFriendlyReplacer</code>.
     */
    public SmackXppReader(final XmlPullParser parser,
            final XmlFriendlyReplacer replacer) {
        super(replacer);
        this.parser = parser;
        moveDown();
    }

    /**
     * @see com.thoughtworks.xstream.io.HierarchicalStreamReader#appendErrors(com.thoughtworks.xstream.converters.ErrorWriter)
     *
     */
    public void appendErrors(final ErrorWriter errorWriter) {
        errorWriter.add("line number", String.valueOf(parser.getLineNumber()));
    }

    /**
     * @see com.thoughtworks.xstream.io.HierarchicalStreamReader#close()
     *
     */
    public void close() {
        /* NOTE Since this is a smack xpp reader; we do not close the stream. */
    }

    /**
     * @see com.thoughtworks.xstream.io.HierarchicalStreamReader#getAttribute(int)
     *
     */
    public String getAttribute(final int index) {
        return parser.getAttributeValue(index);
    }

    /**
     * @see com.thoughtworks.xstream.io.HierarchicalStreamReader#getAttribute(java.lang.String)
     *
     */
    public String getAttribute(final String name) {
        return parser.getAttributeValue(null, name);
    }

    /**
     * @see com.thoughtworks.xstream.io.HierarchicalStreamReader#getAttributeCount()
     *
     */
    public int getAttributeCount() {
        return parser.getAttributeCount();
    }

    /**
     * @see com.thoughtworks.xstream.io.HierarchicalStreamReader#getAttributeName(int)
     *
     */
    public String getAttributeName(final int index) {
        return unescapeXmlName(parser.getAttributeName(index));
    }

    /**
     * @see com.thoughtworks.xstream.io.xml.AbstractPullReader#pullElementName()
     *
     */
    @Override
    protected String pullElementName() {
        return parser.getName();
    }

    /**
     * @see com.thoughtworks.xstream.io.xml.AbstractPullReader#pullNextEvent()
     *
     */
    @Override
    protected int pullNextEvent() {
        try {
            switch (parser.next()) {
            case XmlPullParser.START_DOCUMENT:
            case XmlPullParser.START_TAG:
                return START_NODE;
            case XmlPullParser.END_DOCUMENT:
            case XmlPullParser.END_TAG:
                return END_NODE;
            case XmlPullParser.TEXT:
                return TEXT;
            case XmlPullParser.COMMENT:
                return COMMENT;
            default:
                return OTHER;
            }
        } catch (final XmlPullParserException xppx) {
            throw new StreamException(xppx);
        } catch (final IOException iox) {
            throw new StreamException(iox);
        }
    }

    /**
     * @see com.thoughtworks.xstream.io.xml.AbstractPullReader#pullText()
     *
     */
    @Override
    protected String pullText() {
        return parser.getText();
    }

}
