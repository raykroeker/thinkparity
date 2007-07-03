/*
 * Created On:  21-Jun-07 3:20:14 PM
 */
package com.thinkparity.amazon.s3.service.client.rest.xml;

import java.io.IOException;
import java.io.Reader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ErrorParser implements Parser<ErrorResult> {

    /** A <code>ErrorResult</code>. */
    private ErrorResult result;

    /** A set of parser utils. */
    private final ParserUtils utils;

        /** An xml pull parser. */
    private XmlPullParser xmlPullParser;

    /**
     * Create ErrorParser.
     *
     */
    public ErrorParser() {
        super();
        this.utils = new ParserUtils();
    }

    /**
     * @see com.thinkparity.amazon.s3.service.client.rest.xml.Parser#parse(java.io.Reader, com.thinkparity.amazon.s3.service.client.rest.xml.Result)
     *
     */
    public void parse(final Reader reader, final ErrorResult result)
            throws ParseException {
        this.xmlPullParser = utils.newXmlPullParser(reader);
        this.result = result;
        try {
            parse();
        } catch (final XmlPullParserException xppx) {
            throw new ParseException(xppx);
        } catch (final IOException iox) {
            throw new ParseException(iox);
        }
    }

    /**
     * Parse the error.
     * 
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void parse() throws XmlPullParserException, IOException {
        xmlPullParser.nextTag();    // <Error>
        xmlPullParser.nextTag();    //   <Code>
        xmlPullParser.next();
        result.setCode(xmlPullParser.getText());
        xmlPullParser.nextTag();    //   </Code>
        xmlPullParser.nextTag();    //   <Message>
        xmlPullParser.next();
        result.setMessage(xmlPullParser.getText());
        xmlPullParser.nextTag();    //   </Message>
        xmlPullParser.nextTag();    //   <Resource>
        xmlPullParser.next();
        result.setResource(xmlPullParser.getText());
        xmlPullParser.nextTag();    //   </Resource>
        xmlPullParser.nextTag();    //   <RequestId>
        xmlPullParser.next();
        result.setRequestId(xmlPullParser.getText());
        xmlPullParser.nextTag();    //   </RequestId>
        xmlPullParser.nextTag();    //  </Error>
    }
}
