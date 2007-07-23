/*
 * Created On:  6-Jun-07 9:28:35 AM
 */
package com.thinkparity.desdemona.web;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkparity.codebase.StringUtil;

import com.thinkparity.desdemona.web.service.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppReader;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class WebService extends HttpServlet {

    /** The stream character set (encoding). */
    private static final Charset CHARSET;

    /** The error response xml components. */
    private static final char[][] ERROR_XML;

    /** The begin/end xml nodes for the error message. */
    private static final char[][] ERROR_XML_MESSAGE;

    /** The begin/end xml nodes for the error type. */
    private static final char[][] ERROR_XML_TYPE;

    /** The service operation registry. */
    private static final OperationRegistry OPERATION_REGISTRY;

    /** The response xml components. */
    private static final char[][] RESPONSE_XML;

    /** The service registry. */
    private static final ServiceRegistry SERVICE_REGISTRY;

    /** The operation request/response attribute name. */
    private static final String SRV_REQ_RESP_ATTR_NAME_OP;

    /** The service request/response attribute name. */
    private static final String SRV_REQ_RESP_ATTR_NAME_SRV;

    /** An xml marshaller. */
    private static final XStream XSTREAM;

    static {
        CHARSET = StringUtil.Charset.UTF_8.getCharset();

        ERROR_XML = new char[][] {
                "<?xml version=\"1.0\" encoding=\"".toCharArray(),
                "\"?><service-error service=\"".toCharArray(),
                "\" operation=\"".toCharArray(),
                "\"".toCharArray(),
                ">".toCharArray(),
                "</service-error>".toCharArray()
        };
        ERROR_XML_MESSAGE = new char[][] {
                " message=\"".toCharArray(),
                "\"".toCharArray()
        };
        ERROR_XML_TYPE = new char[][] {
                " type=\"".toCharArray(),
                "\"".toCharArray()
        };

        OPERATION_REGISTRY = OperationRegistry.getInstance();

        RESPONSE_XML = new char[][] {
                "<?xml version=\"1.0\" encoding=\"".toCharArray(),
                "\"?><service-response service=\"".toCharArray(),
                "\" operation=\"".toCharArray(),
                "\">".toCharArray(),
                "</service-response>".toCharArray()
        };

        SERVICE_REGISTRY = ServiceRegistry.getInstance();

        SRV_REQ_RESP_ATTR_NAME_OP = "operation";
        SRV_REQ_RESP_ATTR_NAME_SRV = "service";

        XSTREAM = new XStream();
    }

    /**
     * Determine if the path info is valid. If the path info is null, or if it
     * is does not map to a service; it is not valid.
     * 
     * @param pathInfo
     *            The path info <code>String</code>.
     * @return True if the path info is valid.
     */
    private static boolean isValid(final String pathInfo) {
        if (null == pathInfo)
            return false;
        return SERVICE_REGISTRY.containsService(pathInfo.substring(1));
    }

    /**
     * Marshal the error stack trace.
     * 
     * @param error
     *            The error <code>StackTraceElement[]</code> to write.
     * @param writer
     *            A <code>Writer</code>.
     */
    private static void marshalErrorStackTrace(
            final StackTraceElement[] stackTrace, final Writer writer) {
        XSTREAM.marshal(stackTrace, new XmlWriter(writer));
    }

    /**
     * Marshal the result to a writer.
     * 
     * @param result
     *            A <code>Result</code>.
     * @param writer
     *            A <code>Writer</code>.
     */
    private static void marshalResult(final Result result, final Writer writer) {
        XSTREAM.marshal(result.getValue(), new XmlWriter(writer));
    }

    /**
     * Create an error service response for a declared error
     * 
     * @param error
     *            A declared error <code>Throwable</code>.
     * @return A <code>ServiceResponse</code>.
     */
    private static ServiceResponse newErrorResponse(final ServiceException sx) {
        final ServiceResponse errorResponse = new ServiceResponse();
        errorResponse.setUndeclaredError(sx.getMessage(), newStackTrace(sx));
        return errorResponse;
    }

    /**
     * Create an error service response for a declared error
     * 
     * @param error
     *            A declared error <code>Throwable</code>.
     * @return A <code>ServiceResponse</code>.
     */
    private static ServiceResponse newErrorResponse(final Throwable error) {
        final ServiceResponse errorResponse = new ServiceResponse();
        errorResponse.setDeclaredError(error.getClass(), error.getMessage(),
                newStackTrace(error));
        return errorResponse;
    }

    /**
     * Parse the operation attribute from the parser and return the
     * corresponding operation from the service.
     * 
     * @param parser
     *            An <code>XmlPullParser</code>.
     * @param service
     *            A <code>Service</code>.
     * @return An <code>Operation</code>.
     */
    private static Operation newOperation(final XppReader xppReader,
            final Service service) {
        final String value = xppReader.getAttribute(SRV_REQ_RESP_ATTR_NAME_OP);
        if (null == value) {
            return null;
        } else {
            return OPERATION_REGISTRY.getOperation(service, value);
        }
    }

    /**
     * Parse the service attribute from the parser and return the corresponding
     * service.
     * 
     * @param parser
     *            An <code>XmlPullParser</code>.
     * @return A <code>Service</code>.
     */
    private static Service newService(final XppReader xppReader) {
        return SERVICE_REGISTRY.getService(
                xppReader.getAttribute(SRV_REQ_RESP_ATTR_NAME_SRV));
    }

    /**
     * Build a stack trace from an error including the cause elements.
     * 
     * @param error
     *            An error <code>Throwable</code>.
     * @return A <code>StackTraceElement[]</code>.
     */
    private static StackTraceElement[] newStackTrace(final Throwable error) {
        final List<StackTraceElement> stackTrace = new ArrayList<StackTraceElement>();
        for (final StackTraceElement element : error.getStackTrace()) {
            stackTrace.add(element);
        }
        Throwable cause = error.getCause();
        while (null != cause) {
            for (final StackTraceElement element : cause.getStackTrace()) {
                stackTrace.add(element);
            }
            cause = cause.getCause();
        }
        return stackTrace.toArray(new StackTraceElement[] {});
    }

    /**
     * Create a reader for an input stream.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @return A <code>Reader</code>.
     */
    private static Reader newStreamReader(final InputStream stream) {
        return new BufferedReader(new InputStreamReader(stream, CHARSET));
    }

    /**
     * Create a new instance of an output stream writer.
     * 
     * @param stream
     *            An <code>OutputStream</code>.
     * @return A <code>Writer</code>.
     */
    private static Writer newStreamWriter(final OutputStream stream) {
        return new BufferedWriter(new OutputStreamWriter(stream, CHARSET));
    }

    /**
     * Create an xstream xml pull parser reader for an input stream.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @return A <code>XppReader</code>.
     */
    private static XppReader newXppReader(final InputStream stream) {
        return new XppReader(newStreamReader(stream));
    }

    /**
     * Read a service request from an input stream.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @return A <code>ServiceRequest</code>.
     * @throws InstantiationException
     *             if an instance of a service request parameter cannot be
     *             instantiated
     * @throws IllegalAccessException
     *             if an instance of a service request parameter cannot be
     *             instantiated
     * @throws ClassNotFoundException
     *             if the type of a service request parameter cannot be located
     */
    private static ServiceRequest readRequest(final InputStream stream)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        final XppReader xppReader = newXppReader(stream);
        final Service service = newService(xppReader);
        final Operation operation = newOperation(xppReader, service);
        if (null == service || null == operation) {
            throw new WebException("No such service exists.");
        } else {
            final Parameter[] parameters = unmarshalParameters(xppReader);

            final ServiceRequest request = new ServiceRequest();
            request.setService(service);
            request.setOperation(operation);
            request.setParameters(parameters);
            return request;
        }
    }

    /**
     * Unmarshall the remainder of the parser as a parameter array.
     * 
     * @param xppReader
     *            An xstream <code>XppReader</code>.
     * @return A <code>Parameter[]</code>.
     * @throws InstantiationException
     *             if an instance of a parameter cannot be instantiated
     * @throws IllegalAccessException
     *             if an instance of a parameter cannot be instantiated
     * @throws ClassNotFoundException
     *             if the type of a parameter cannot be located
     */
    private static Parameter[] unmarshalParameters(final XppReader xppReader)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        final List<Parameter> parameters = new ArrayList<Parameter>(20);
        while (true) {
            if (xppReader.hasMoreChildren()) {
                xppReader.moveDown();
                final Object value = XSTREAM.unmarshal(xppReader);
                parameters.add(new Parameter() {
                    public Object getValue() {
                        return value;
                    }
                });
                xppReader.moveUp();
            } else {
                break;
            }
        }
        return parameters.toArray(new Parameter[] {});
    }

    /**
     * Write the service error response to the output stream.
     * 
     * @param stream
     *            An <code>OutputStream</code>.
     * @param request
     *            The original <code>ServiceResponse</code>.
     * @param error
     *            The error <code>Throwable</code>.
     */
    private static void writeErrorResponse(final OutputStream stream,
            final ServiceRequest serviceRequest,
            final ServiceResponse errorResponse) throws IOException {
        final Writer writer = newStreamWriter(stream);
        writer.write(ERROR_XML[0]);
        writer.write(CHARSET.name());
        writer.write(ERROR_XML[1]);
        writer.write(serviceRequest.getService().getId());
        writer.write(ERROR_XML[2]);
        writer.write(serviceRequest.getOperation().getId());
        writer.write(ERROR_XML[3]);
        if (errorResponse.isSetErrorMessage()) {
            writer.write(ERROR_XML_MESSAGE[0]);
            writer.write(xmlEncode(errorResponse.getErrorMessage()));
            writer.write(ERROR_XML_MESSAGE[1]);
        }
        if (errorResponse.isSetErrorType()) {
            writer.write(ERROR_XML_TYPE[0]);
            writer.write(errorResponse.getErrorType().getName());
            writer.write(ERROR_XML_TYPE[1]);
        }
        writer.write(ERROR_XML[4]);
        marshalErrorStackTrace(errorResponse.getErrorStackTrace(), writer);
        writer.write(ERROR_XML[5]);
        writer.flush();
    }

    /**
     * Write the service response to the output stream.
     * 
     * @param stream
     *            An <code>OutputStream</code>.
     * @param response
     *            A <code>ServiceResponse</code>.
     */
    private static void writeResponse(final OutputStream stream,
            final ServiceRequest serviceRequest, final ServiceResponse response)
            throws IOException {
        final Writer writer = newStreamWriter(stream);
        writer.write(RESPONSE_XML[0]);
        writer.write(CHARSET.name());
        writer.write(RESPONSE_XML[1]);
        writer.write(serviceRequest.getService().getId());
        writer.write(RESPONSE_XML[2]);
        writer.write(serviceRequest.getOperation().getId());
        writer.write(RESPONSE_XML[3]);
        if (response.isSetResult()) {
            marshalResult(response.getResult(), writer);
        }
        writer.write(RESPONSE_XML[4]);
        writer.flush();
    }

    /**
     * Encode a string for xml transport.
     * 
     * @param string
     *            A <code>String</code>.
     * @return An xml <code>String</code>.
     */
    private static String xmlEncode(final String string) {
        return StringUtil.searchAndReplace(string, "\"", "").toString();
    }

    /**
     * Create WebService.
     *
     */
    public WebService() {
        super();
    }

    /**
     * Service a request.
     * 
     * @param input
     *            A request <code>InputStream</code>.
     * @param output
     *            A response <code>OutputStream</code>.
     * @return Whether or not an error response was written.
     * @throws Exception
     */
    public ServiceResponse service(final ServiceRequest request) {
        try {
            return request.invoke();
        } catch (final AuthException ax) {
            throw ax;
        } catch (final ServiceException sx) {
            return newErrorResponse(sx);
        } catch (final Throwable throwable) {
            return newErrorResponse(throwable);
        }
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     *
     */
    @Override
    protected void doPost(final HttpServletRequest req,
            final HttpServletResponse resp) throws ServletException,
            IOException {
        WebServiceMetrics.begin(req);
        try {
            final String pathInfo = req.getPathInfo();
            if (!isValid(pathInfo)) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            // read the request before committing a response
            final ServiceRequest request;
            try {
                request = readRequest(req.getInputStream());
            } catch (final Throwable t) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                throw new ServletException(t);
            }
            // service the response; write the result
            try {
                final ServiceResponse response = service(request);
                if (response.isErrorResponse()) {
                    writeErrorResponse(resp.getOutputStream(), request, response);
                } else {
                    writeResponse(resp.getOutputStream(), request, response);
                }
            } catch (final AuthException ax) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (final Throwable t) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } finally {
            WebServiceMetrics.end(req);
        }
    }

    /**
     * <b>Title:</b>An xml writer.<br>
     * <b>Description:</b>An xml writer used to write the service response
     * to the client.  No line breaks or indent characters are used.<br>
     * 
     * @author raymond@thinkparity.com
     * @version 1.1.2.1
     */
    private static final class XmlWriter extends PrettyPrintWriter {

        /**
         * Create XmlWriter.
         * 
         * @param writer
         *            An io <code>writer</code>.
         */
        private XmlWriter(final Writer writer) {
            super(writer, new char[0], "");
        }

        /**
         * @see com.thoughtworks.xstream.io.xml.PrettyPrintWriter#close()
         *
         */
        @Override
        public void close() { /* we do not want to close */ }
    }
}
