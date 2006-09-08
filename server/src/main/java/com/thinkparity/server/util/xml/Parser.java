/*
 * Created On: Sep 4, 2006 8:58:53 AM
 */
package com.thinkparity.desdemona.util.xml;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.log4j.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.log4j.Log4JHelper;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class Parser {

    /** An apache logger. */
    private static final Logger logger;

    static {
        logger = Logger.getLogger(Parser.class);
    }

    /** Log an api id. */
    protected static final void logApiId() {
        if(logger.isInfoEnabled()) {
            logger.info(MessageFormat.format("[{0}] [{1}]",
                    StackUtil.getCallerClassName(),
                    StackUtil.getCallerMethodName()));
        }
    }

    /**
     * Log a variable.
     * 
     * @param name
     *            The variable name.
     * @param value
     *            The variable.
     * @return The variable.
     */
    protected static <T> T logVariable(final String name, final T value) {
        if(logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format("[{0}:{1}]",
                    name, Log4JHelper.render(logger, value)));
        }
        return value;
    }

    /**
     * Translate an error.
     * 
     * @param t
     *            An error.
     * @return An xml error.
     */
    protected static XmlException translateError(final Throwable t) {
        return XmlException.translateError(t);
    }

    /**
     * Create an xml pull parser.
     * 
     * @return An <code>XmlPullParser</code>.
     * @throws XmlPullParserException
     */
    private static XmlPullParser createXmlPullParser()
            throws XmlPullParserException {
        return createXmlPullParserFactory().newPullParser();
    }

    /**
     * Create an xml pull parser factory.
     * 
     * @return <code>XmlPullParserFactory</code>.
     * @throws XmlPullParserException
     */
    private static XmlPullParserFactory createXmlPullParserFactory()
            throws XmlPullParserException {
        return XmlPullParserFactory.newInstance();
    }

    /** An xml pull parser. */
    protected final XmlPullParser parser;

    /** Create ParserWrapper. */
    protected Parser() {
        super();
        try {
            this.parser = createXmlPullParser();
        } catch (final XmlPullParserException xppx) {
            throw translateError(xppx);
        }
    }

    public final String getText(){
        try {
            return doGetText();
        } catch (final IOException iox) {
            throw translateError(iox);
        } catch (final XmlPullParserException xppx) {
            throw translateError(xppx);
        }
    }

    public final Boolean isEndDocument() {
        try {
            return determineIsEndDocument();
        } catch (final XmlPullParserException xppx) {
            throw translateError(xppx);
        }
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
    public final Boolean isEndTag(final String name) {
        try {
            return determineIsEndTag(name);
        } catch (final XmlPullParserException xppx) {
            throw translateError(xppx);
        }
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
    public final Boolean isStartTag(final String name) {
        try {
            return determineIsStartTag(name);
        } catch (final XmlPullParserException xppx) {
            throw translateError(xppx);
        }
    }

    public final void next() {
        try {
            next(1);
        } catch (final IOException iox) {
            throw translateError(iox);
        } catch (final XmlPullParserException xppx) {
            throw translateError(xppx);
        }
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

    private final Boolean determineIsEndDocument()
            throws XmlPullParserException {
        return XmlPullParser.END_DOCUMENT == parser.getEventType();
    }

    private final Boolean determineIsEndTag(final String name)
            throws XmlPullParserException {
        return XmlPullParser.END_TAG == parser.getEventType() &&
                name.equals(parser.getName());
    }

    private final Boolean determineIsStartTag(final String name)
            throws XmlPullParserException {
        return XmlPullParser.START_TAG == parser.getEventType() &&
                name.equals(parser.getName());
    }

    private String doGetText() throws IOException, XmlPullParserException { 
        return parser.getText();
    }
}
