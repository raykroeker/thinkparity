/*
 * Created On:  23-Jun-07 12:47:04 PM
 */
package com.thinkparity.codebase.model.util.http;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.thinkparity.codebase.Constants;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.protocol.Protocol;

/**
 * <b>Title:</b>thinkParity Model Codebase Http Utilities<br>
 * <b>Description:</b>Contains commonly used http constants and utilities.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HttpUtils {

    /** An rfc 822 simple date format pattern (used in http headers). */
    private static final String RFC_822_DATE_PATTERN;

    static {
        RFC_822_DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss Z";

        Protocol.registerProtocol("http", new Protocol("http", HttpSocketFactory.getInstance(), 80));
        Protocol.registerProtocol("https", new Protocol("https", HttpsSocketFactory.getInstance(), 443));
    }

    /**
     * Format a date value for header insertion according to RFC 822 in GMT.
     * 
     * @param date
     *            A <code>Date</code>.
     * @return A formatted <code>Date</code> in GMT.
     */
    public static String format(final Date date) {
        final SimpleDateFormat format = new SimpleDateFormat(RFC_822_DATE_PATTERN);
        format.setTimeZone(Constants.UNIVERSAL_TIME_ZONE);
        return format.format(date);
    }

    /**
     * Obtain an instance of a shared http client.
     * 
     * @param port
     *            An <code>int</code>.
     * @return A <code>HttpClient</code>.
     */
    public static HttpClient newClient() {
       return new HttpClient();
    }

    /**
     * Create HttpUtils.
     *
     */
    private HttpUtils() {
        super();
    }
   
    /** <b>Title:</b>Common Http Content Types<br> */
    public static final class ContentTypeNames {

        /** Zip */
        public static final String APPLICATION_ZIP = "application/zip";

        /** Binary stream */
        public static final String BINARY_OCTET_STREAM = "binary/octet-stream";
    }

    /** <b>Title:</b>Common Http Header Names<br> */
    public static final class HeaderNames {

        /** The authorization http header name. */
        public static final String AUTHORIZATION = "Authorization";

        /** The content length http header name. */
        public static final String CONTENT_LENGTH = "Content-Length";

        /** The content md5 http header name. */
        public static final String CONTENT_MD5 = "Content-MD5";

        /** The content type http header name. */
        public static final String CONTENT_TYPE = "Content-Type";

        /** The date http header name. */
        public static final String DATE = "Date";
    }

    /** <b>Title:</b>Http Method Names<br> */
    public static final class MethodNames {

        /** The connect method name. */
        public static final String CONNECT = "CONNECT";

        /** The delete method name. */
        public static final String DELETE = "DELETE";

        /** The get method name. */
        public static final String GET = "GET";

        /** The head method name. */
        public static final String HEAD = "HEAD";

        /** The post method name. */
        public static final String POST = "POST";

        /** The put method name. */
        public static final String PUT = "PUT";

        /** The trace method name. */
        public static final String TRACE = "TRACE";
    }
}
