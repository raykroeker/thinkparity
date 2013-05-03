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

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class SocketTestServer implements Runnable {

    /** A singleton instance. */
    private static SocketTestServer SINGLETON;

    /**
     * Determine if the test server is running.
     * 
     * @return True if the server is running.
     */
    static Boolean isRunning() {
        if (null == SINGLETON) {
            return Boolean.FALSE;
        } else {
            return SINGLETON.running;
        }
    }

    /**
     * Start the socket test server.
     * 
     * @param bindAddress
     *            A <code>NetworkAddress</code>.
     * @throws IOException
     */
    static void start(final NetworkAddress bindAddress) throws IOException {
        if (isRunning()) {
            throw new IllegalStateException();
        }
        SINGLETON = new SocketTestServer(bindAddress);
        SINGLETON.start();
    }

    /** A bind network address. */
    private final NetworkAddress bindAddress;

    /** A run indicator. */
    private boolean running;

    /** A socket server. */
    private ServerSocket server;

    /**
     * Create SocketTestServer.
     * 
     * @param port
     *            A <code>Integer</code>.
     * @throws IOException
     */
    private SocketTestServer(final NetworkAddress bindAddress) {
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
                }, "TPS-Network-SocketTestServerClient").start();
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
     * Start the socket test server as a daemon thread.
     * 
     * @throws IOException
     */
    private void start() throws IOException {
        running = true;
        final InetAddress bindInetAddress = InetAddress.getByName(bindAddress.getHost());
        server = new ServerSocket(bindAddress.getPort(), 75, bindInetAddress);
        final Thread thread = Executors.defaultThreadFactory().newThread(this);
        thread.setDaemon(true);
        thread.start();
    }
}
