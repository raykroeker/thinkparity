/*
 * Created On: Sep 4, 2006 9:12:58 AM
 */
package com.thinkparity.desdemona.util.xml;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParserException;

import com.thinkparity.desdemona.model.Constants.Xml;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class StreamParser extends Parser {

    /**
     * Create StreamParser.
     * 
     * @param stream
     *            The <code>InputStream</code> to read from.
     */
    public StreamParser(final InputStream stream) {
        this(stream, Xml.DEFAULT_ENCODING);
    }

    /**
     * Create StreamParser.
     * 
     * @param stream
     *            The <code>InputStream</code> to read from.
     * @param encoding
     *            The encoding to use.
     */
    public StreamParser(final InputStream stream, final String encoding) {
        super();
        try {
            parser.setInput(stream, encoding);
        } catch (final XmlPullParserException xppx) {
            throw translateError(xppx);
        }
    }
}
