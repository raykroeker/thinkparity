/*
 * Created On:  13-Dec-07 2:01:01 PM
 */
package com.thinkparity.ophelia.support.ui.action.network;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.text.MessageFormat;
import java.util.StringTokenizer;

import com.thinkparity.codebase.swing.SwingUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.methods.GetMethod;

import com.thinkparity.net.Network;
import com.thinkparity.net.NetworkAddress;
import com.thinkparity.net.NetworkConnection;
import com.thinkparity.net.NetworkException;
import com.thinkparity.net.NetworkProtocol;
import com.thinkparity.net.NetworkProxy;
import com.thinkparity.net.protocol.http.Http;
import com.thinkparity.ophelia.support.ui.Input;
import com.thinkparity.ophelia.support.ui.action.AbstractAction;
import com.thinkparity.ophelia.support.ui.avatar.network.NetworkAvatar;
import com.thinkparity.ophelia.support.util.network.NetworkTestResult;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Test extends AbstractAction {

    /**
     * Extract the proxy list from the input.
     * 
     * @param input
     *            An <code>Input</code>.
     * @return A <code>List<Proxy></code>.
     */
    private static Proxy extractProxy(final Input input) {
        return (Proxy) input.get(InputKey.PROXY);
    }

    /**
     * Extract the uri from the input.
     * 
     * @param input
     *            An <code>Input</code>.
     * @return A <code>URI</code>.
     */
    private static URI extractURI(final Input input) {
        return (URI) input.get(InputKey.URI);
    }

    /**
     * Invoke the method using the client.
     * 
     * @param client
     *            A <code>HttpClient</code>.
     * @param method
     *            A <code>HttpMethod</code>.
     * @return A <code>NetworkTestResult</code>.
     */
    private static NetworkTestResult invoke(final URI uri,
            final HttpClient client, final HttpMethod method) {
        try {
            final int response = client.executeMethod(method);
            if (response > 199 && response < 300) {
                return newPassResult(uri);
            } else {
                return newFailResult(uri, "Network test failed.  {0} - {1}",
                        String.valueOf(response), method.getResponseBodyAsString());
            }
        } catch (final HttpException hx) {
            return newFailResult(uri, hx);
        } catch (final IOException iox) {
            return newFailResult(uri, iox);
        }
    }

    /**
     * Connect the connection through the tunnel.
     * 
     * @param uri
     *            A <code>URI</code>.
     * @param connection
     *            A <code>NetworkConnection</code>.
     * @param tunnel
     *            A <code>NetworkTunnel</code>.
     * @return A <code>NetworkTestResult</code>.
     */
    private static NetworkTestResult invoke(final URI uri,
            final NetworkConnection connection, final NetworkProxy proxy) {
        try {
            if (null != proxy) {
                new Network().getConfiguration().setProxy(connection, proxy);
            }
            connection.connect();
            try {
                writeRequest(uri, connection.getSocket().getOutputStream());
                final String response = readResponse(connection.getSocket().getInputStream());
                final StringTokenizer tokenizer = new StringTokenizer(response, " ", false);
                tokenizer.nextToken();
                final Integer code = Integer.valueOf(tokenizer.nextToken());
                final String text = tokenizer.nextToken();
                if (199 < code.intValue() && 300 > code.intValue()) {
                    return newPassResult(uri);
                } else {
                    return newFailResult(uri, "{0} - {1}", code.toString(), text);
                }
            } finally {
                connection.disconnect();
            }
        } catch (final NetworkException nx) {
            return newFailResult(uri, nx);
        } catch (final IOException iox) {
            return newFailResult(uri, iox);
        }
    }

    /**
     * Invoke a http test.
     * 
     * @param uri
     *            A <code>URI</code>.
     * @param proxy
     *            A <code>Proxy</code>.
     * @return A <code>NetworkTestResult</code>.
     */
    private static NetworkTestResult invokeHttp(final URI uri, final Proxy proxy) {
        try {
            final HttpClient client = newClient(proxy);
            final HttpMethod method = newMethod(uri);
            return invoke(uri, client, method);
        } catch (final com.thinkparity.net.protocol.http.HttpException hx) {
            return newFailResult(uri, hx);
        }
    }

    /**
     * Invoke a socket test.
     * 
     * @param uri
     *            A <code>URI</code>.
     * @param proxy
     *            A <code>Proxy</code>.
     * @return A <code>NetworkTestResult</code>.
     */
    private static NetworkTestResult invokeSocket(final URI uri,
            final Proxy proxy) {
        return invoke(uri, newConnection(uri), newProxy(proxy));            
    }

    /**
     * Instantiate a network address for a proxy.
     * 
     * @param proxy
     *            A <code>Proxy</code>.
     * @return A <code>NetworkAddress</code>.
     */
    private static NetworkAddress newAddress(final Proxy proxy) {
        final InetSocketAddress address = (InetSocketAddress) proxy.address();
        return new NetworkAddress(address.getHostName(), address.getPort());
    }

    /**
     * Instantiate a network address for a uri.
     * 
     * @param uri
     *            A <code>URI</code>.
     * @return A <code>NetworkAddress</code>.
     */
    private static NetworkAddress newAddress(final URI uri) {
        return new NetworkAddress(uri.getHost(), Integer.valueOf(uri.getPort()));
    }

    /**
     * Instantiate a http client for the proxy.
     * 
     * @param proxy
     *            A <code>Proxy</code>.
     * @return A <code>HttpClient</code>.
     */
    private static HttpClient newClient(final Proxy proxy)
            throws com.thinkparity.net.protocol.http.HttpException {
        final Http http = new Http();
        final ProxyHost proxyHost = newProxyHost(proxy);
        if (null != proxyHost) {
            http.getConfiguration().setProxyHost(http, newProxyHost(proxy));
        }
        return http.newClient();
    }

    /**
     * Instantiate a network connection for a uri.
     * 
     * @param uri
     *            A <code>URI</code>.
     * @return A <code>NetworkConnection</code>.
     */
    private static NetworkConnection newConnection(final URI uri) {
        return new Network().newConnection(newSocketProtocol(), newAddress(uri));
    }

    /**
     * Instantiate a new fail network test result.
     * 
     * @param error
     *            A <code>Throwable</code>.
     * @return A <code>NetworkTestResult</code>.
     */
    private static NetworkTestResult newFailResult(final URI uri,
            final String text, final Object... textArguments) {
        final NetworkTestResult result = new NetworkTestResult();
        result.setURI(uri);
        result.setFail(text, textArguments);
        return result;
    }

    /**
     * Instantiate a new fail network test result.
     * 
     * @param error
     *            A <code>Throwable</code>.
     * @return A <code>NetworkTestResult</code>.
     */
    private static NetworkTestResult newFailResult(final URI uri,
            final Throwable cause) {
        final NetworkTestResult result = new NetworkTestResult();
        result.setURI(uri);
        result.setFail(cause);
        return result;
    }

    /**
     * Instantiate a http method for the uri.
     * 
     * @param uri
     *            A <code>URI</code>.
     * @return A <code>HttpMethod</code>.
     */
    private static HttpMethod newMethod(final URI uri) {
        return new GetMethod(uri.toString());
    }

    /**
     * Instantiate a new pass network test result.
     * 
     * @return A <code>NetworkTestResult</code>.
     */
    private static NetworkTestResult newPassResult(final URI uri) {
        final NetworkTestResult result = new NetworkTestResult();
        result.setURI(uri);
        result.setPass();
        return result;
    }

    /**
     * Instantiate a network proxy for the proxy.
     * 
     * @param proxy
     *            A <code>Proxy</code>.
     * @return A <code>NetworkProxy</code>.
     */
    private static NetworkProxy newProxy(final Proxy proxy) {
        if (null == proxy || Proxy.NO_PROXY == proxy || Proxy.Type.DIRECT == proxy.type()) {
            return null;
        } else {
            return new NetworkProxy(newAddress(proxy));
        }
    }

    /**
     * Instantiate a proxy host for the proxy.
     * 
     * @param proxy
     *            A <code>Proxy</code>.
     * @return A <code>ProxyHost</code>.
     */
    private static ProxyHost newProxyHost(final Proxy proxy) {
        if (Proxy.NO_PROXY == proxy) {
            return null;
        } else {
            final InetSocketAddress proxyAddress = (InetSocketAddress) proxy.address();
            return new ProxyHost(proxyAddress.getHostName(), proxyAddress.getPort());
        }
    }

    /**
     * Instantiate a socket network protocol.
     * 
     * @return A <code>NetworkProtocol</code>.
     */
    private static NetworkProtocol newSocketProtocol() {
        return NetworkProtocol.getProtocol("socket");
    }

    /**
     * Read an http response from the input stream and return the http response
     * line.
     * 
     * @param stream
     *            An <code>InputStream</code>.
     * @return A <code>String</code>.
     * @throws IOException
     */
    private static String readResponse(final InputStream stream)
            throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "ISO-8859-1"));
        String line = null;
        while (true) {
            line = reader.readLine();
            if (null == line) {
                break;
            }
            if (line.startsWith("HTTP/1.1")) {
                return line;
            }
        }
        return null;
    }

    /**
     * Write an http get request to the output stream.
     * 
     * @param uri
     *            A <code>URI</code>.
     * @param stream
     *            An <code>OutputStream</code>.
     * @throws IOException
     */
    private static void writeRequest(final URI uri, final OutputStream stream)
            throws IOException {
        final Writer writer = new OutputStreamWriter(stream, "ISO-8859-1");
        writer.write(MessageFormat.format("GET http://{0}{1} HTTP/1.1\r\n", uri.getHost(), uri.getPath()));  
        writer.write(MessageFormat.format("Host: {0}\r\n", uri.getHost()));  
        writer.write("Agent: thinkParity Support\r\n");  
        writer.write("\r\n");  
        writer.flush();
    }

    /**
     * Create Get.
     *
     * @param id
     */
    public Test() {
        super("/network/test");
    }

    /**
     * @see com.thinkparity.ophelia.support.ui.action.Action#invoke(com.thinkparity.ophelia.support.ui.Input)
     *
     */
    @Override
    public void invoke(final Input input) {
        final URI uri = extractURI(input);
        final Proxy proxy = extractProxy(input);

        final String scheme = uri.getScheme();
        final NetworkTestResult result;
        if ("http".equals(scheme) || "https".equals(scheme)) {
            result = invokeHttp(uri, proxy);
        } else if ("socket".equals(scheme)) {
            if (-1 == uri.getPort()) {
                result = newFailResult(uri, "Please specify a port.");
            } else {
                result = invokeSocket(uri, proxy);
            }
        } else {
            result = newFailResult(uri, "Cannot determine test for scheme.");
        }

        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                try {
                    ((NetworkAvatar) getContext().lookupAvatar("/network/network")).setTestResult(result);
                    getContext().lookupAvatar("/network/network").reload();
                } catch (final Throwable t) {
                    t.printStackTrace(System.err);
                }
            }
        });
    }

    /** <b>Title:</b>Test Input Key<br> */
    public enum InputKey { PROXY, URI }
}
