/*
 * Created On:  20-Jun-07 8:23:13 AM
 */
package com.thinkparity.amazon.s3.service.client.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thinkparity.codebase.Constants;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import com.thinkparity.amazon.s3.service.S3Context;
import com.thinkparity.amazon.s3.service.S3Utils;
import com.thinkparity.amazon.s3.service.bucket.S3Filter;
import com.thinkparity.amazon.s3.service.client.ServiceException;
import com.thinkparity.amazon.s3.service.client.rest.http.HeaderReader;
import com.thinkparity.amazon.s3.service.client.rest.xml.ErrorParser;
import com.thinkparity.amazon.s3.service.client.rest.xml.ErrorResult;
import com.thinkparity.amazon.s3.service.client.rest.xml.ParseException;
import com.thinkparity.amazon.s3.service.client.rest.xml.Parser;
import com.thinkparity.amazon.s3.service.object.S3Object;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class RestUtils {

    /** The constant <code>String</code> portions of the authorization header. */
    private static final String[] AUTHORIZATION_HEADER;

    /** An error parser. */
    private static final Parser<ErrorResult> ERROR_PARSER;

    /** A log4j wrapper. */
    private static final Log4JWrapper LOGGER;

    /** An rfc 822 date pattern (used in http headers). */
    private static final String RFC_822_DATE_PATTERN;

    static {
        AUTHORIZATION_HEADER = new String[] { "AWS ", ":" };

        ERROR_PARSER = new ErrorParser();

        LOGGER = new Log4JWrapper(RestUtils.class);

        RFC_822_DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss Z";
    }

    /**
     * Execute a http method for a rest request.
     * 
     * @param request
     *            A <code>RestRequest</code>.
     * @param method
     *            A <code>HttpMethod</code>.
     * @throws IOException
     */
    private static void executeMethod(final RestRequest request,
            final HttpMethod method) throws IOException {
        request.getContext().getHttpClient().executeMethod(method);
        logRequestHeaders(method);
        logResponseHeaders(method);
        LOGGER.logVariable("statusCode", method.getStatusCode());
        LOGGER.logVariable("statusLine", method.getStatusLine());
        LOGGER.logVariable("statusText", method.getStatusText());
    }

    /**
     * Format the current date/time as a string for inclusion in an http header.
     * Uses the rfc 822 date pattern.
     * 
     * @return A formatted date/time.
     */
    private static String formatHttpCurrentDateTime() {
        // see http://www.faqs.org/rfcs/rfc822.html
        final SimpleDateFormat format = new SimpleDateFormat(RFC_822_DATE_PATTERN);
        format.setTimeZone(Constants.UNIVERSAL_TIME_ZONE);
        return format.format(new Date());
    }

    /**
     * Log all of the http headers.
     * 
     * @param headers
     *            A <code>Header[]</code>.
     */
    private static void logHeaders(final Header[] headers) {
        for (int i = 0; i < headers.length; i++) {
            LOGGER.logVariable("header", headers[i].toString());
        }
    }

    /**
     * Log all of the http method request headers.
     * 
     * @param httpMethod
     *            An <code>HttpMethod</code>.
     */
    private static void logRequestHeaders(final HttpMethod httpMethod) {
        logHeaders(httpMethod.getRequestHeaders());
    }

    /**
     * Log all of the http method request headers.
     * 
     * @param httpMethod
     *            An <code>HttpMethod</code>.
     */
    private static void logResponseHeaders(final HttpMethod httpMethod) {
        logHeaders(httpMethod.getResponseHeaders());
    }

    /**
     * Create a response stream reader. Buffer the stream.
     * 
     * @param context
     *            A <code>S3Context</code>.
     * @param stream
     *            An <code>InputStream</code>.
     * @return A <code>Reader</code>.
     * @throws IOException
     */
    private static Reader newResponseStreamReader(final S3Context context,
            final InputStream stream) throws IOException {
        return new BufferedReader(new InputStreamReader(stream,
                context.getCharset()));
    }

    /**
     * Create a new uri.
     * 
     * @param request
     *            A <code>RestRequest</code>.
     * @return A service uri.
     */
    private static String newURI(final RestRequest request) {
        final StringBuilder buffer = new StringBuilder(128)
            .append(request.getContext().getBaseURI())
            .append('/');
        if (request.isSetBucket()) {
            buffer.append(request.getBucket().getName());
            if (request.isSetKey()) {
                buffer.append('/').append(request.getKey().getResource());
            }
        } else {
            if (request.isSetKey()) {
                buffer.append(request.getKey().getResource());
            }
        }
        if (request.isSetFilter()) {
            final S3Filter filter = request.getFilter();
            buffer.append("?prefix=").append(filter.getPrefix());
            if (filter.isSetDelimiter()) {
                buffer.append('&').append(filter.getDelimiter());
            }
        } else {
            if (request.isACLRequest()) {
                buffer.append("?acl");
            }
        }
        return buffer.toString();
    }

    /**
     * Parse the request method's response stream for an error result.
     * 
     * @param request
     *            A <code>RestRequest</code>.
     * @param method
     *            A <code>HttpMethod</code>.
     * @return A <code>ServiceException</code>.
     */
    private static ServiceException parseErrorResult(final RestRequest request,
            final HttpMethod method) {
        final ErrorResult errorResult = new ErrorResult();
        try {
            ERROR_PARSER.parse(newResponseStreamReader(
                    request.getContext(),
                    method.getResponseBodyAsStream()), errorResult);
        } catch (final Exception x) {
            // cannot parse error result; use http status info
            throw new ServiceException("{0} - {1}\n{2}",
                    method.getStatusCode(),
                    method.getStatusLine(),
                    method.getStatusText());
        }
        throw new ServiceException(
                "Code:{0}{1}Message:{2}{1}Resource:{3}{1}Request Id:{4}",
                errorResult.getCode(), "\n\t", errorResult.getMessage(),
                errorResult.getResource(), errorResult.getRequestId());
    }

    /** Amazon s3 utility methods. */
    private final S3Utils s3Utils;

    /**
     * Create RestUtils.
     *
     */
    public RestUtils() {
        super();
        this.s3Utils = S3Utils.getInstance();
    }

    /**
     * Service a rest request.
     * 
     * @param request
     *            A <code>RestRequest</code>.
     * @throws IOException
     */
    public void service(final RestRequest request) throws IOException {
        final HttpMethod httpMethod = newHttpMethod(request);
        try {
            executeMethod(request, httpMethod);
            switch (httpMethod.getStatusCode()) {
            case 200:
                // do nothing; there is no xml
                break;
            case 204:
                // do nothing; there is no content
                break;
            default:
                throw parseErrorResult(request, httpMethod);
            }
        } finally {
            httpMethod.releaseConnection();
        }
    }

    /**
     * Service a rest request providing direct body serialization.
     * 
     * @param request
     *            A <code>RestRequest</code>.
     * @param serializer
     *            A <code>ResponseBodySerializer</code>.
     * @throws IOException
     */
    public void service(final RestRequest request,
            final ResponseBodySerializer serializer) throws IOException {
        final HttpMethod httpMethod = newHttpMethod(request);
        try {
            executeMethod(request, httpMethod);
            switch (httpMethod.getStatusCode()) {
            case 200:
                serializer.read(httpMethod.getResponseBodyAsStream());
                break;
            default:
                throw parseErrorResult(request, httpMethod);
            }
        } finally {
            httpMethod.releaseConnection();
        }
    }

    /**
     * Service a rest request and parse the content of the response.
     * 
     * @param <T>
     *            A result type.
     * @param request
     *            A <code>RestRequest</code>.
     * @param parser
     *            A typed <code>Parser</code>.
     * @param result
     *            A <code>T</code>
     * @return A typed <code>RestResponse</code>.
     * @throws IOException
     * @throws ParseException
     */
    public <T extends Object> RestResponse<T> service(final RestRequest request,
            final Parser<T> parser, final T result) throws IOException,
            ParseException {
        final HttpMethod httpMethod = newHttpMethod(request);
        try {
            request.getContext().getHttpClient().executeMethod(httpMethod);
            switch (httpMethod.getStatusCode()) {
            case 200:
                parser.parse(newResponseStreamReader(request.getContext(),
                        httpMethod.getResponseBodyAsStream()), result);
                break;
            default:
                throw parseErrorResult(request, httpMethod);
            }

            final RestResponse<T> response = new RestResponse<T>();
            response.setResult(result);
            return response;
        } finally {
            httpMethod.releaseConnection();
        }
    }

    /**
     * Service a rest request and parse the content of the response.
     * 
     * @param <T>
     *            A result type.
     * @param request
     *            A <code>RestRequest</code>.
     * @param reader
     *            A typed <code>RestHeaderReader</code>.
     * @param result
     *            A <code>T</code>
     * @return A typed <code>RestResponse</code>.
     * @throws IOException
     * @throws ParseException
     */
    public <T extends Object> RestResponse<T> service(
            final RestRequest request, final HeaderReader<T> reader,
            final T result) throws IOException, ParseException {
        final HttpMethod httpMethod = newHttpMethod(request);
        try {
            request.getContext().getHttpClient().executeMethod(httpMethod);
            switch (httpMethod.getStatusCode()) {
            case 200:
                reader.read(httpMethod.getResponseHeaders(), result);
                break;
            default:
                throw parseErrorResult(request, httpMethod);
            }

            final RestResponse<T> response = new RestResponse<T>();
            response.setResult(result);
            return response;
        } finally {
            httpMethod.releaseConnection();
        }
    }

    /**
     * Create the apache http method for the rest request.
     * 
     * @param request
     *            A <code>RestRequest</code>.
     * @return An <code>HttpMethod</code>.
     */
    private HttpMethod newHttpMethod(final RestRequest request) {
        final HttpMethod httpMethod;
        switch (request.getType()) {
        case DELETE:
            httpMethod = new DeleteMethod(newURI(request));
            break;
        case GET:
            httpMethod = new GetMethod(newURI(request));
            break;
        case HEAD:
            httpMethod = new HeadMethod(newURI(request));
            break;
        case PUT:
            httpMethod = new PutMethod(newURI(request));
            if (request.isSetSerializer().booleanValue()) {
                ((PutMethod) httpMethod).setRequestEntity(new RequestEntity() {
                    public long getContentLength() {
                        return request.getSerializer().getContentLength();
                    }
                    public String getContentType() {
                        return request.getSerializer().getContentType();
                    }
                    public boolean isRepeatable() {
                        return true;
                    }
                    public void writeRequest(final OutputStream out)
                            throws IOException {
                        request.getSerializer().write(out);
                    }
                });
            }
            break;
        default:
            throw Assert.createUnreachable("Unknown request type {0}.",
                    request.getType());
        }
        httpMethod.setRequestHeader("Date", formatHttpCurrentDateTime());
        // set headers
        if (request.isSetObject()) {
            final S3Object object = request.getObject();
            httpMethod.setRequestHeader(HttpHeaders.CONTENT_LENGTH,
                    String.valueOf(object.getSize()));
            httpMethod.setRequestHeader(HttpHeaders.CONTENT_MD5,
                    object.getChecksum());
            httpMethod.setRequestHeader(HttpHeaders.CONTENT_TYPE,
                    object.getType().getValue());
        }

        final String canonical = s3Utils.canoncialize(httpMethod,
                request.getBucket(), request.getKey(), request.isACLRequest());
        final byte[] encrypted = s3Utils.encrypt(request.getAuthentication(),
                request.getContext(), canonical);
        final byte[] encoded = s3Utils.encode(encrypted);
        final String authorization = new StringBuilder(64)
            .append(AUTHORIZATION_HEADER[0])
            .append(request.getAuthentication().getAccessKeyId())
            .append(AUTHORIZATION_HEADER[1])
            .append(new String(encoded, request.getContext().getCharset()))
            .toString();
        httpMethod.setRequestHeader(HttpHeaders.AUTHORIZATION, authorization);
        httpMethod.setFollowRedirects(false);
        return httpMethod;
    }

    /** <b>Title:</b>Rest Http Header Names<br> */
    private static class HttpHeaders {
        private static final String AUTHORIZATION = "Authorization";
        private static final String CONTENT_LENGTH = "Content-Length";
        private static final String CONTENT_MD5 = "Content-MD5";
        private static final String CONTENT_TYPE = "Content-Type";
    }
}
