/*
 * Created On:  20-Aug-07 12:52:08 PM
 */
package com.thinkparity.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLServerSocketFactory;

/**
 * <b>Title:</b>thinkParity Network Secure Test Server<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class SecureSocketTestServer implements Runnable {

    /** A singleton instance. */
    private static SecureSocketTestServer SINGLETON;

    /**
     * Determine whether or not the test server is running.
     * 
     * @return True if the test server is running.
     */
    static Boolean isRunning() {
        if (null == SINGLETON) {
            return Boolean.FALSE;
        } else {
            return SINGLETON.running;
        }
    }

    /**
     * Start the secure socket test server.
     * 
     * @param bindAddress
     *            A <code>NetworkAddress</code>.
     * @throws IOException
     */
    static void start(final NetworkAddress bindAddress) throws IOException {
        if (isRunning()) {
            throw new IllegalStateException();
        }
        System.setProperty("javax.net.ssl.keyStore", "keystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        SINGLETON = new SecureSocketTestServer(bindAddress);
        SINGLETON.start();
    }

    /** A bind network address. */
    private final NetworkAddress bindAddress;

    /** A running indicator. */
    private boolean running;

    /** The server socket. */
    private ServerSocket server;

    /**
     * Create SecureSocketTestServer.
     * 
     * @param bindAddress
     *            A <code>NetworkAddress</code>.
     * @throws IOException
     */
    private SecureSocketTestServer(final NetworkAddress bindAddress) throws IOException {
        super();
        this.bindAddress = bindAddress;
    }

    /**
     * @see java.lang.Runnable#run()
     *
     */
    @Override
    public void run() {
        while (running) {
            try {
                final Socket client = server.accept();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            processRequest(client);
                        } catch (final IOException iox) {
                            iox.printStackTrace(System.err);
                        } finally {
                            try {
                                client.close();
                            } catch (final IOException iox) {
                                iox.printStackTrace(System.err);
                            }
                        }
                    }
                }, "TPS-Network-SecureSocketTestServerClient").start();
            } catch (final IOException iox) {
                iox.printStackTrace(System.err);
            }
        }
    }

    /**
     * Process a request (client socket).
     * 
     * @param client
     *            A <code>Socket</code>.
     * @throws IOException
     */
    private void processRequest(final Socket client) throws IOException {
        final PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            try {
                writer.println(reader.readLine());
            } finally {
                reader.close();
            }
        } finally {
            writer.close();
        }
    }

    /**
     * Start the test server as a daemon thread.
     * 
     * @throws IOException
     */
    private void start() throws IOException {
        running = true;
        final InetAddress bindInetAddress = InetAddress.getByName(bindAddress.getHost());
        final SSLServerSocketFactory sf =
            (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        this.server = sf.createServerSocket(bindAddress.getPort(), 75, bindInetAddress);
        final Thread thread = Executors.defaultThreadFactory().newThread(this);
        thread.setDaemon(true);
        thread.start();
    }
}
