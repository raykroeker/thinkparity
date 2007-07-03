/*
 * Created On:  22-Jun-07 3:11:17 PM
 */
package com.thinkparity.amazon.s3.service.client.rest.http;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thinkparity.codebase.Constants;

import org.apache.commons.httpclient.Header;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HeaderReaderUtils {

    /** An rfc 822 date pattern (used in http headers). */
    private static final String RFC_822_DATE_PATTERN;

    static {
        RFC_822_DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss Z";
    }
    
    /**
     * Create HeaderReaderUtils.
     *
     */
    HeaderReaderUtils() {
        super();
    }

    /**
     * Find a header.
     * 
     * @param headers
     *            A <code>Header[]</code>.
     * @param name
     *            A header name.
     * @return A <code>Header</code>.
     */
    Header find(final Header[] headers, final String name) {
        for (final Header header : headers) {
            if (header.getName().toLowerCase().equals(name.toLowerCase()))
                return header;
        }
        return null;
    }

    Date parseDate(final Header header) throws ParseException {
        return newSimpleDateFormat().parse(header.getValue());
    }

    /**
     * Create a simple date format to read a calendar.
     * 
     * @return A <code>SimpleDateFormat</code>.
     */
    private SimpleDateFormat newSimpleDateFormat() {
        // see http://www.faqs.org/rfcs/rfc822.html
        final SimpleDateFormat format = new SimpleDateFormat(RFC_822_DATE_PATTERN);
        format.setTimeZone(Constants.UNIVERSAL_TIME_ZONE);
        return format;
    }
}
