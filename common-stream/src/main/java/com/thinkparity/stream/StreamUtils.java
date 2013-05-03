/*
 * Created On:  25-Jun-07 9:56:30 AM
 */
package com.thinkparity.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import com.thinkparity.common.StringUtil;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.thinkparity.net.protocol.http.HttpException;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamUtils {

    /** A bytes format. */
    private static final BytesFormat BYTES_FORMAT;

    /** The xml charset (encoding). */
    private static final Charset CHARSET;

    /** A stream configuration. */
    private static final StreamConfiguration CONFIGURATION;

    /** A default retry after header value. */
    private static final Long DEFAULT_RETRY_AFTER;

    /** An set of error response node name. */
    private static final String[] ERROR_RESPONSE_NODE_NAMES;

    /** A log4j wrapper. */
    private static final Log4JWrapper LOGGER;

    static {
        BYTES_FORMAT = new BytesFormat();
        CHARSET = StringUtil.Charset.UTF_8.getCharset();
        CONFIGURATION = new StreamConfiguration();
        DEFAULT_RETRY_AFTER = 750L;
        ERROR_RESPONSE_NODE_NAMES = new String[] {
                "Error", "Code", "Message", "RequestId", "HostId"
        };
        LOGGER = new Log4JWrapper(StreamUtils.class);
    }

    /**
     * Create a new instance of an input stream reader.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @return A <code>Reader</code>.
     */
    private static Reader newStreamReader(final InputStream stream) {
        return new BufferedReader(new InputStreamReader(stream, CHARSET));
    }

    /**
     * Create an xstream xml pull parser reader for an input steam.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @return An <code>XppReader</code>.
     * @throws StreamException
     *             if the xml pull parser cannot be instantiated
     */
    private static XmlPullParser newXmlPullParser(final InputStream stream)
            throws IOException {
        final XmlPullParser parser = new MXParser();
        try {
            parser.setInput(newStreamReader(stream));
        } catch (final XmlPullParserException xppx) {
            throw new IOException(xppx);
        }
        return parser;
    }

    /** An <code>HttpClient</code>. */
    private final HttpClient httpClient;

    /**
     * Create StreamUtils.
     * 
     * @throws HttpException
     *             if the http client cannot be instantiated
     */
    StreamUtils() throws HttpException {
        super();
        this.httpClient = getHttpClient();
    }

    /**
     * Execute the method.
     * 
     * @param method
     *            A <code>HttpMethod</code>.
     * @return An http status code.
     */
    int execute(final HttpMethod method) throws IOException {
        return httpClient.executeMethod(method);
    }

    /**
     * Obtain the retry after header value from the method.
     * 
     * @param method
     *            A <code>HttpMethod</code>.
     * @return A <code>Long</code>.
     */
    Long getRetryAfter(final HttpMethod method) {
        final Header header = method.getResponseHeader("Retry-After");
        if (null == header) {
            return DEFAULT_RETRY_AFTER;
        } else {
            try {
                return Long.valueOf(header.getValue());
            } catch (final NumberFormatException nfx) {
                LOGGER.logWarning(nfx, "Could not extrapolate retry after:  {0}", header.getValue());
                return DEFAULT_RETRY_AFTER;
            }
        }
    }

    /**
     * Log the receipt of a chunk by the monitor.
     * 
     * @param monitor
     *            The <code>StreamMonitor</code>.
     * @param chunkSize
     *            A number of bytes.
     */
    void logMonitorChunkReceived(final StreamMonitor monitor,
            final int chunkSize) {
        LOGGER.logDebug("Stream monitor \"{0}\" sent {1}.",
                null == monitor ? "unknown" : monitor.getName(),
                BYTES_FORMAT.format(new Long(chunkSize)));
    }

    /**
     * Log a monitor error.
     * 
     * @param monitor
     *            The <code>StreamMonitor</code>.
     * @param chunkSize
     *            A number of bytes.
     */
    void logMonitorChunkSent(final StreamMonitor monitor, final int chunkSize) {
        LOGGER.logDebug("Stream monitor \"{0}\" sent {1}.",
                null == monitor ? "unknown" : monitor.getName(),
                BYTES_FORMAT.format(new Long(chunkSize)));
    }

    
    /**
     * Log a monitor error.
     * 
     * @param cause
     *            The error cause <code>Throwable</code>.
     * @param monitor
     *            The <code>StreamMonitor</code>.
     */
    void logMonitorError(final StreamMonitor monitor, final Throwable cause) {
        LOGGER.logWarning(cause,
                "An unexpected error has occured in stream monitor \"{0}\".",
                null == monitor ? "unknown" : monitor.getName());
    }

    /**
     * Read the error response from the http method's response body stream.
     * 
     * @param httpMethod
     *            An <code>HttpMethod</code>.
     * @return A <code>StreamErrorResponse</code>.
     */
    StreamErrorResponse readErrorResponse(final HttpMethod httpMethod) {
        final StreamErrorResponse errorResponse;
        try {
            /* read the error response xml */
            final XmlPullParser xpp = newXmlPullParser(httpMethod.getResponseBodyAsStream());
            xpp.nextTag();

            if (ERROR_RESPONSE_NODE_NAMES[0].equals(xpp.getName())) {
                errorResponse = new StreamErrorResponse();
                xpp.nextTag();
                if (ERROR_RESPONSE_NODE_NAMES[1].equals(xpp.getName())) {
                    errorResponse.setCode(xpp.nextText());
                    xpp.nextTag();
                }
        
                if (ERROR_RESPONSE_NODE_NAMES[2].equals(xpp.getName())) {
                    errorResponse.setMessage(xpp.nextText());
                    xpp.nextTag();
                }
        
                if (ERROR_RESPONSE_NODE_NAMES[3].equals(xpp.getName())) {
                    errorResponse.setRequestId(xpp.nextText());
                    xpp.nextTag();
                }
        
                if (ERROR_RESPONSE_NODE_NAMES[4].equals(xpp.getName())) {
                    errorResponse.setHostId(xpp.nextText());
                    xpp.nextTag();
                }
            } else {
                errorResponse = StreamErrorResponse.EMPTY_RESPONSE;
            }
        } catch (final IOException iox) {
            LOGGER.logWarning(iox, "Could not extract stream error response.");
            return StreamErrorResponse.EMPTY_RESPONSE;
        } catch (final XmlPullParserException xppx) {
            LOGGER.logWarning(xppx, "Could not extract stream error response.");
            return StreamErrorResponse.EMPTY_RESPONSE;
        }
        return errorResponse;
    }

    /**
     * Set headers within the method.
     * 
     * @param method
     *            A <code>HttpMethod</code>.
     * @param headers
     *            A <code>Map<String, String></code>.
     */
    void setHeaders(final HttpMethod method, final Map<String, String> headers) {
        for (final Entry<String, String> header : headers.entrySet()) {
            method.setRequestHeader(header.getKey(), header.getValue());
        }
    }

    /**
     * Wait for a given number of milliseconds.
     * 
     * @param millis
     *            A <code>Long</code>.
     */
    void wait(final Long millis) {
        try {
            Thread.sleep(millis.longValue());
        } catch (final InterruptedException ix) {
            LOGGER.logWarning(ix, "Could not wait.");
        }
    }

    /**
     * Write the error to the system error stream.
     * 
     * @param method
     *            A <code>HttpMethod</code>.
     */
    void writeError(final HttpMethod method) {
        try {
            final InputStream errorStream = method.getResponseBodyAsStream();
            if (null == errorStream) {
                LOGGER.logWarning("No stream response.");
            } else {
                StreamUtil.copy(errorStream, System.err);
            }
        } catch (final IOException iox) {
            LOGGER.logWarning(iox, "Could not write stream error.");
        }
    }

    /**
     * Obtain a http client.
     * 
     * @return An <code>HttpClient</code>.
     */
    private HttpClient getHttpClient() throws HttpException {
        return CONFIGURATION.getHttpClient();
    }
}
