/*
 * Created On:  19-Jun-07 9:00:30 PM
 */
package com.thinkparity.amazon.s3.service.client.rest.xml;

import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.thinkparity.amazon.s3.service.S3Utils;
import com.thinkparity.amazon.s3.service.client.ServiceException;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ParserUtils {

    /** An xml pull parser. */
    private static final XmlPullParserFactory XML_PULL_PARSER_FACTORY;

    static {
        try {
            XML_PULL_PARSER_FACTORY = XmlPullParserFactory.newInstance();
        } catch (final XmlPullParserException xppx) {
            throw new ServiceException(xppx);
        }
    }

    /** Amazon s3 utilities. */
    private final S3Utils s3Utils;

    /**
     * Create ReaderUtils.
     *
     */
    ParserUtils() {
        super();
        this.s3Utils = S3Utils.getInstance();
    }

    /**
     * Create an xml pull parser for a rest response.
     * 
     * @param reader
     *            An xml <code>Reader</code>.
     * @return An <code>XmlPullParser</code>.
     * @throws XmlPullParserException
     * @throws IOException
     */
    XmlPullParser newXmlPullParser(final Reader reader) throws ParseException {
        try {
            final XmlPullParser xmlPullParser = XML_PULL_PARSER_FACTORY.newPullParser();
            xmlPullParser.setInput(reader);
            return xmlPullParser;
        } catch (final XmlPullParserException xppx) {
            throw new ParseException(xppx);
        }
    }

    Date parseDate(final XmlPullParser xmlPullParser) throws ParseException {
        try {
            return newSimpleDateFormat().parse(xmlPullParser.getText());
        } catch (final java.text.ParseException px) {
            throw new ParseException(px);
        }
    }

    /**
     * Create a simple date format to read a calendar.
     * 
     * @return A <code>SimpleDateFormat</code>.
     */
    private SimpleDateFormat newSimpleDateFormat() {
        return s3Utils.newSimpleDateFormat();
    }
}
