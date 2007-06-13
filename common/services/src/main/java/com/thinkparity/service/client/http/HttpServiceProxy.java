/*
 * Created On:  7-Jun-07 8:51:03 AM
 */
package com.thinkparity.service.client.http;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.text.MessageFormat;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.assertion.Assert;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import com.thinkparity.service.ServiceHelper;
import com.thinkparity.service.client.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppReader;

/**
 * <b>Title:</b>thinkParity Service Client Http Service Proxy<br>
 * <b>Description:</b>An implementation of the services through an http post
 * mechanism.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class HttpServiceProxy implements InvocationHandler, RequestEntity {

    /** The xml charset (encoding). */
    private static final Charset CHARSET;

    /** The attribute name for the error note message. */
    private static final String ERROR_NODE_MESSAGE_ATTRIBUTE_NAME;

    /** The request xml components. */
    private static final char[][] REQUEST_XML;

    /** The possible node names for the response xml. */
    private static final String[] RESPONSE_NODE_NAMES;

    /** A <code>ServiceHelper</code>. */
    private static final ServiceHelper SERVICE_HELPER;

    /** An xstream instance. */
    private static final XStream XSTREAM;

    static {
        CHARSET = StringUtil.Charset.UTF_8.getCharset();

        ERROR_NODE_MESSAGE_ATTRIBUTE_NAME = "message";

        REQUEST_XML = new char[][] {
                "<?xml version=\"1.0\" encoding=\"".toCharArray(),
                "\"?><service-request service=\"".toCharArray(),
                "\" operation=\"".toCharArray(),
                "\">".toCharArray(),
                "</service-request>".toCharArray(),
        };

        RESPONSE_NODE_NAMES = new String[] {
                "service-response",
                "service-error"
        };

        SERVICE_HELPER = new ServiceHelper();

        XSTREAM = new XStream();
    }

    /**
     * Create a new operation for a method.
     * 
     * @param method
     *            A <code>Method</code>.
     * @return An <code>Operation</code>.
     */
    private static Operation newOperation(final Method method) {
        return new Operation() {
            public String getId() {
                return SERVICE_HELPER.getOperationId(method);
            }
        };
    }

    /**
     * Create a new parameter for an argument.
     * 
     * @param arg
     *            An argument <code>Object</code>.
     * @return A <code>Parameter</code>.
     */
    private static Parameter newParameter(final Object arg) {
        return new Parameter() {
            public Object getValue() {
                return arg;
            }
        };
    }

    /**
     * Create a new parameter list for an argument list.
     * 
     * @param args
     *            An argument <code>Object[]</code>.
     * @return A <code>Parameter[]</code>.
     */
    private static Parameter[] newParameters(final Object[] args) {
        final Parameter[] parameters = new Parameter[args.length];
        for (int i = 0; i < args.length; i++) {
            parameters[i] = newParameter(args[i]);
        }
        return parameters;
    }

    /**
     * Create a new service exception from the request and error response.
     * 
     * @param request
     *            A <code>ServiceRequest</code>.
     * @param response
     *            A <code>ServiceResponse</code>.
     * @return A <code>Throwable</code>.
     */
    private static Throwable newServiceException(
            final ServiceRequest request, final ServiceResponse response) {
        final ServiceException serviceError = new ServiceException(
                "A remote error has occured.  {0}:{1}:{2}",
                request.getService().getId(), request.getOperation().getId(),
                response.getErrorMessage());
        serviceError.setStackTrace(response.getErrorStackTrace());
        return serviceError;
    }

    /**
     * Create a new service request. Take the method/args and create the
     * appropriate operation/parameter model and return them within a service
     * request wrapper.
     * 
     * @param method
     *            A <code>Method</code>.
     * @param args
     *            An <code>Object[]</code>.
     * @return A <code>ServiceRequest</code>.
     */
    private static ServiceRequest newServiceRequest(final Service service,
            final Method method, final Object[] args) {
        final ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setOperation(newOperation(method));
        serviceRequest.setParameters(newParameters(args));
        serviceRequest.setService(service);
        return serviceRequest;
    }

    /**
     * Create a new service url from the url pattern and the service id.
     * 
     * @param context
     *            An <code>HttpServiceContext</code>.
     * @param service
     *            A <code>Service</code>.
     * @return A service url <code>String</code>.
     */
    private static String newServiceURL(final HttpServiceContext context,
            final Service service) {
        return MessageFormat.format(context.getURLPattern(), service.getId());
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
     * Create a new instance of a stream writer.
     * 
     * @param stream
     *            An <code>OutputStream</code>.
     * @return A <code>Writer</code>.
     */
    private static Writer newStreamWriter(final OutputStream stream) {
        return new BufferedWriter(new OutputStreamWriter(stream, CHARSET));
    }

    /**
     * Unmarshall the result of the response.
     * 
     * @param parser
     *            An <code>XmlPullParser</code>.
     * @return A <code>Object</code>.
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private static Result unmarshalResult(final XppReader xppReader) {
        final Object value = XSTREAM.unmarshal(xppReader);
        return new Result() {
            public Object getValue() {
                return value;
            }
        };
    }

    /**
     * Unmarshall the error of the response.
     * 
     * @param parser
     *            An <code>XmlPullParser</code>.
     * @return A <code>Object</code>.
     */
    private static StackTraceElement[] unmarshalStackTrace(
            final XppReader xppReader) {
        return (StackTraceElement[]) XSTREAM.unmarshal(xppReader);
    }

    /** A service context shared by all service proxy instances. */
    private final HttpServiceContext context;

    /** The service. */
    private final Service service;

    /** The service request. */
    private ServiceRequest serviceRequest;

    /**
     * Create ServiceProxy.
     * 
     * @param context
     *            A <code>ServiceContext</code>.
     * @param service
     *            A <code>Service</code>.
     */
    HttpServiceProxy(final HttpServiceContext context, final Service service) {
        super();
        this.context = context;
        this.service = service;
    }

    /**
     * @see org.apache.commons.httpclient.methods.RequestEntity#getContentLength()
     *
     */
    public long getContentLength() {
        return -1;  // indicates an unknown length
    }

    /**
     * @see org.apache.commons.httpclient.methods.RequestEntity#getContentType()
     *
     */
    public String getContentType() {
        return context.getContentType();
    }

    /**
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     *
     */
    public Object invoke(final Object proxy, final Method method,
            final Object[] args) throws Throwable {
        HttpServiceProxyMetrics.begin(method);
        try {
            serviceRequest = newServiceRequest(service, method, args);

            // execute the http post
            final HttpClient httpClient = new HttpClient();
            final PostMethod postMethod = new PostMethod(newServiceURL(context, service));
            try {
                postMethod.setRequestHeader("serviceId", serviceRequest.getService().getId());
                postMethod.setRequestHeader("operationId", serviceRequest.getService().getId());
                postMethod.setRequestEntity(this);
                httpClient.executeMethod(postMethod);
    
                /* extract the response; a 200 status code will be used even in the
                 * case of a remote error; the error will be serialized to the
                 * stream and can be determined by the top-level xml node name in
                 * the response */
                switch (postMethod.getStatusCode()) {
                case 200:
                    final ServiceResponse serviceResponse = readResponse(postMethod);
                    if (serviceResponse.isErrorResponse()) {
                        throw newServiceException(serviceRequest, serviceResponse);
                    } else {
                        if (serviceResponse.isSetResult()) {
                            return serviceResponse.getResult().getValue();
                        } else {
                            return null;
                        }
                    }
                case 404:
                    throw new ServiceException("Service {0}:{1} does not exist.",
                            serviceRequest.getService().getId(),
                            serviceRequest.getOperation().getId());
                default:
                    throw new ServiceException("Service {0}:{1} generated an unknown status code {2}.",
                            serviceRequest.getService().getId(),
                            serviceRequest.getOperation().getId(),
                            postMethod.getStatusCode());
                }
            } finally {
                postMethod.releaseConnection();
            }
        } finally {
            HttpServiceProxyMetrics.end(method);
        }
    }

    /**
     * @see org.apache.commons.httpclient.methods.RequestEntity#isRepeatable()
     *
     */
    public boolean isRepeatable() {
        return true;
    }

    /**
     * @see org.apache.commons.httpclient.methods.RequestEntity#writeRequest(java.io.OutputStream)
     *
     */
    public void writeRequest(final OutputStream stream) throws IOException {
        writeRequest(newStreamWriter(System.out));
        writeRequest(newStreamWriter(stream));
    }

    /**
     * Marshal a parameter.
     * 
     * @param parameter
     *            A <code>Parameter</code>.
     * @param writer
     *            A <code>Writer</code>.
     */
    private void marshalParameter(final Parameter parameter, final Writer writer) {
        XSTREAM.marshal(parameter.getValue(), new XmlWriter(writer));
    }

    /**
     * Create an xstream xml pull parser reader for an input steam.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @return An <code>XppReader</code>.
     */
    private XppReader newXppReader(final InputStream stream) {
        return new XppReader(newStreamReader(stream));
    }

    /**
     * Read the service response.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @return A <code>ServiceResponse</code>.
     */
    private ServiceResponse readResponse(final InputStream stream) {
        final XppReader xppReader = newXppReader(stream);

        final ServiceResponse response = new ServiceResponse();
        if (RESPONSE_NODE_NAMES[0].equals(xppReader.getNodeName())) {
            xppReader.moveDown();
            response.setResult(unmarshalResult(xppReader));
        } else if (RESPONSE_NODE_NAMES[1].equals(xppReader.getNodeName())) {
            final String errorMessage = xppReader.getAttribute(
                    ERROR_NODE_MESSAGE_ATTRIBUTE_NAME);
            xppReader.moveDown();
            response.setError(errorMessage, unmarshalStackTrace(xppReader));
        } else {
            Assert.assertUnreachable("Unknown service response node name {0}.",
                    xppReader.getNodeName());
        }
        return response;
    }

    /**
     * Read the service response.
     * 
     * @param postMethod
     *            A http client <code>PostMethod</code>.
     * @return A <code>ServiceResponse</code>.
     * @throws IOException
     *             if the post method response body stream cannot be obtained
     */
    private ServiceResponse readResponse(final PostMethod postMethod)
            throws IOException {
        return readResponse(postMethod.getResponseBodyAsStream());
    }

    /**
     * Write the request to a writer.
     * 
     * @param writer
     *            A <code>Writer</code>.
     * @throws IOException
     */
    private void writeRequest(final Writer writer) throws IOException {
        writer.write(REQUEST_XML[0]);
        writer.write(CHARSET.name());
        writer.write(REQUEST_XML[1]);
        writer.write(serviceRequest.getService().getId());
        writer.write(REQUEST_XML[2]);
        writer.write(serviceRequest.getOperation().getId());
        writer.write(REQUEST_XML[3]);
        final Parameter[] parameters = serviceRequest.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            marshalParameter(parameters[i], writer);
        }
        writer.write(REQUEST_XML[4]);
        writer.flush();
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
