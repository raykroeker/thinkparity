/*
 * Created On:  22-Aug-07 9:50:38 AM
 */
package com.thinkparity.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

/**
 * <b>Title:</b>thinkParity Secure Network Socket<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class SecureNetworkSocket extends SSLSocket  {

    /** The connection id. */
    private final String connectionId;

    /** The delegate socket. */
    private final SSLSocket delegate;

    /**
     * Create NetworkSocket.
     * 
     * @param connectionId
     *            A connection id <code>String</code>.
     * @param delegate
     *            A <code>SSLSocket</code>.
     */
    public SecureNetworkSocket(final String connectionId,
            final SSLSocket delegate) {
        super();
        this.connectionId = connectionId;
        this.delegate = delegate;
    }

    /**
     * @see javax.net.ssl.SSLSocket#addHandshakeCompletedListener(javax.net.ssl.HandshakeCompletedListener)
     *
     */
    @Override
    public void addHandshakeCompletedListener(
            final HandshakeCompletedListener listener) {
        delegate.addHandshakeCompletedListener(listener);
    }

    /**
     * @see java.net.Socket#bind(java.net.SocketAddress)
     *
     */
    @Override
    public void bind(final SocketAddress bindpoint) throws IOException {
        delegate.bind(bindpoint);
    }

    /**
     * @see java.net.Socket#close()
     *
     */
    @Override
    public synchronized void close() throws IOException {
        delegate.close();
    }

    /**
     * @see java.net.Socket#connect(java.net.SocketAddress)
     *
     */
    @Override
    public void connect(final SocketAddress endpoint) throws IOException {
        delegate.connect(endpoint);
    }

    /**
     * @see java.net.Socket#connect(java.net.SocketAddress, int)
     *
     */
    @Override
    public void connect(final SocketAddress endpoint, final int timeout)
            throws IOException {
        delegate.connect(endpoint, timeout);
    }

    /**
     * @see java.net.Socket#getChannel()
     *
     */
    @Override
    public SocketChannel getChannel() {
        return delegate.getChannel();
    }

    /**
     * Obtain the connectionId.
     *
     * @return A <code>String</code>.
     */
    public String getConnectionId() {
        return connectionId;
    }

    /**
     * @see javax.net.ssl.SSLSocket#getEnabledCipherSuites()
     *
     */
    @Override
    public String[] getEnabledCipherSuites() {
        return delegate.getEnabledCipherSuites();
    }

    /**
     * @see javax.net.ssl.SSLSocket#getEnabledProtocols()
     *
     */
    @Override
    public String[] getEnabledProtocols() {
        return delegate.getEnabledProtocols();
    }

    /**
     * @see javax.net.ssl.SSLSocket#getEnableSessionCreation()
     *
     */
    @Override
    public boolean getEnableSessionCreation() {
        return delegate.getEnableSessionCreation();
    }

    /**
     * @see java.net.Socket#getInetAddress()
     *
     */
    @Override
    public InetAddress getInetAddress() {
        return delegate.getInetAddress();
    }

    /**
     * @see java.net.Socket#getInputStream()
     *
     */
    @Override
    public InputStream getInputStream() throws IOException {
        // TODO - SecureNetworkSocket#getInputStream() - Wrap with a counting stream.
        return delegate.getInputStream();
    }

    /**
     * @see java.net.Socket#getKeepAlive()
     *
     */
    @Override
    public boolean getKeepAlive() throws SocketException {
        return delegate.getKeepAlive();
    }

    /**
     * @see java.net.Socket#getLocalAddress()
     *
     */
    @Override
    public InetAddress getLocalAddress() {
        return delegate.getLocalAddress();
    }

    /**
     * @see java.net.Socket#getLocalPort()
     *
     */
    @Override
    public int getLocalPort() {
        return delegate.getLocalPort();
    }

    /**
     * @see java.net.Socket#getLocalSocketAddress()
     *
     */
    @Override
    public SocketAddress getLocalSocketAddress() {
        return delegate.getLocalSocketAddress();
    }

    /**
     * @see javax.net.ssl.SSLSocket#getNeedClientAuth()
     *
     */
    @Override
    public boolean getNeedClientAuth() {
        return delegate.getNeedClientAuth();
    }

    /**
     * @see java.net.Socket#getOOBInline()
     *
     */
    @Override
    public boolean getOOBInline() throws SocketException {
        return delegate.getOOBInline();
    }

    /**
     * @see java.net.Socket#getOutputStream()
     *
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        // TODO - SecureNetworkSocket#getOutputStream() - Wrap with a counting stream.
        return delegate.getOutputStream();
    }

    /**
     * @see java.net.Socket#getPort()
     *
     */
    @Override
    public int getPort() {
        return delegate.getPort();
    }

    /**
     * @see java.net.Socket#getReceiveBufferSize()
     *
     */
    @Override
    public synchronized int getReceiveBufferSize() throws SocketException {
        return delegate.getReceiveBufferSize();
    }

    /**
     * @see java.net.Socket#getRemoteSocketAddress()
     *
     */
    @Override
    public SocketAddress getRemoteSocketAddress() {
        return delegate.getRemoteSocketAddress();
    }

    /**
     * @see java.net.Socket#getReuseAddress()
     *
     */
    @Override
    public boolean getReuseAddress() throws SocketException {
        return delegate.getReuseAddress();
    }

    /**
     * @see java.net.Socket#getSendBufferSize()
     *
     */
    @Override
    public synchronized int getSendBufferSize() throws SocketException {
        return delegate.getSendBufferSize();
    }

    /**
     * @see javax.net.ssl.SSLSocket#getSession()
     *
     */
    @Override
    public SSLSession getSession() {
        return delegate.getSession();
    }

    /**
     * @see java.net.Socket#getSoLinger()
     *
     */
    @Override
    public int getSoLinger() throws SocketException {
        return delegate.getSoLinger();
    }

    /**
     * @see java.net.Socket#getSoTimeout()
     *
     */
    @Override
    public synchronized int getSoTimeout() throws SocketException {
        return delegate.getSoTimeout();
    }

    /**
     * @see javax.net.ssl.SSLSocket#getSSLParameters()
     *
     */
    @Override
    public SSLParameters getSSLParameters() {
        return delegate.getSSLParameters();
    }

    /**
     * @see javax.net.ssl.SSLSocket#getSupportedCipherSuites()
     *
     */
    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }

    /**
     * @see javax.net.ssl.SSLSocket#getSupportedProtocols()
     *
     */
    @Override
    public String[] getSupportedProtocols() {
        return delegate.getSupportedProtocols();
    }

    /**
     * @see java.net.Socket#getTcpNoDelay()
     *
     */
    @Override
    public boolean getTcpNoDelay() throws SocketException {
        return delegate.getTcpNoDelay();
    }

    /**
     * @see java.net.Socket#getTrafficClass()
     *
     */
    @Override
    public int getTrafficClass() throws SocketException {
        return delegate.getTrafficClass();
    }

    /**
     * @see javax.net.ssl.SSLSocket#getUseClientMode()
     *
     */
    @Override
    public boolean getUseClientMode() {
        return delegate.getUseClientMode();
    }

    /**
     * @see javax.net.ssl.SSLSocket#getWantClientAuth()
     *
     */
    @Override
    public boolean getWantClientAuth() {
        return delegate.getWantClientAuth();
    }

    /**
     * @see java.net.Socket#isBound()
     *
     */
    @Override
    public boolean isBound() {
        return delegate.isBound();
    }

    /**
     * @see java.net.Socket#isClosed()
     *
     */
    @Override
    public boolean isClosed() {
        return delegate.isClosed();
    }

    /**
     * @see java.net.Socket#isConnected()
     *
     */
    @Override
    public boolean isConnected() {
        return delegate.isConnected();
    }

    /**
     * @see java.net.Socket#isInputShutdown()
     *
     */
    @Override
    public boolean isInputShutdown() {
        return delegate.isInputShutdown();
    }

    /**
     * @see java.net.Socket#isOutputShutdown()
     *
     */
    @Override
    public boolean isOutputShutdown() {
        return delegate.isOutputShutdown();
    }

    /**
     * @see javax.net.ssl.SSLSocket#removeHandshakeCompletedListener(javax.net.ssl.HandshakeCompletedListener)
     *
     */
    @Override
    public void removeHandshakeCompletedListener(
            final HandshakeCompletedListener listener) {
        delegate.removeHandshakeCompletedListener(listener);
    }

    /**
     * @see java.net.Socket#sendUrgentData(int)
     *
     */
    @Override
    public void sendUrgentData(final int data) throws IOException {
        delegate.sendUrgentData(data);
    }

    /**
     * @see javax.net.ssl.SSLSocket#setEnabledCipherSuites(java.lang.String[])
     *
     */
    @Override
    public void setEnabledCipherSuites(final String[] suites) {
        delegate.setEnabledCipherSuites(suites);
    }

    /**
     * @see javax.net.ssl.SSLSocket#setEnabledProtocols(java.lang.String[])
     *
     */
    @Override
    public void setEnabledProtocols(final String[] protocols) {
        delegate.setEnabledProtocols(protocols);
    }

    /**
     * @see javax.net.ssl.SSLSocket#setEnableSessionCreation(boolean)
     *
     */
    @Override
    public void setEnableSessionCreation(final boolean flag) {
        delegate.setEnableSessionCreation(flag);
    }

    /**
     * @see java.net.Socket#setKeepAlive(boolean)
     *
     */
    @Override
    public void setKeepAlive(final boolean on) throws SocketException {
        delegate.setKeepAlive(on);
    }

    /**
     * @see javax.net.ssl.SSLSocket#setNeedClientAuth(boolean)
     *
     */
    @Override
    public void setNeedClientAuth(final boolean need) {
        delegate.setNeedClientAuth(need);
    }

    /**
     * @see java.net.Socket#setOOBInline(boolean)
     *
     */
    @Override
    public void setOOBInline(final boolean on) throws SocketException {
        delegate.setOOBInline(on);
    }

    /**
     * @see java.net.Socket#setPerformancePreferences(int, int, int)
     *
     */
    @Override
    public void setPerformancePreferences(final int connectionTime,
            final int latency, final int bandwidth) {
        delegate.setPerformancePreferences(connectionTime, latency, bandwidth);
    }

    /**
     * @see java.net.Socket#setReceiveBufferSize(int)
     *
     */
    @Override
    public synchronized void setReceiveBufferSize(final int size)
            throws SocketException {
        delegate.setReceiveBufferSize(size);
    }

    /**
     * @see java.net.Socket#setReuseAddress(boolean)
     *
     */
    @Override
    public void setReuseAddress(final boolean on) throws SocketException {
        delegate.setReuseAddress(on);
    }

    /**
     * @see java.net.Socket#setSendBufferSize(int)
     *
     */
    @Override
    public synchronized void setSendBufferSize(final int size)
            throws SocketException {
        delegate.setSendBufferSize(size);
    }

    /**
     * @see java.net.Socket#setSoLinger(boolean, int)
     *
     */
    @Override
    public void setSoLinger(final boolean on, final int linger)
            throws SocketException {
        delegate.setSoLinger(on, linger);
    }

    /**
     * @see java.net.Socket#setSoTimeout(int)
     *
     */
    @Override
    public synchronized void setSoTimeout(final int timeout)
            throws SocketException {
        delegate.setSoTimeout(timeout);
    }

    /**
     * @see javax.net.ssl.SSLSocket#setSSLParameters(javax.net.ssl.SSLParameters)
     *
     */
    @Override
    public void setSSLParameters(final SSLParameters params) {
        delegate.setSSLParameters(params);
    }

    /**
     * @see java.net.Socket#setTcpNoDelay(boolean)
     *
     */
    @Override
    public void setTcpNoDelay(final boolean on) throws SocketException {
        delegate.setTcpNoDelay(on);
    }

    /**
     * @see java.net.Socket#setTrafficClass(int)
     *
     */
    @Override
    public void setTrafficClass(final int tc) throws SocketException {
        delegate.setTrafficClass(tc);
    }

    /**
     * @see javax.net.ssl.SSLSocket#setUseClientMode(boolean)
     *
     */
    @Override
    public void setUseClientMode(final boolean mode) {
        delegate.setUseClientMode(mode);
    }

    /**
     * @see javax.net.ssl.SSLSocket#setWantClientAuth(boolean)
     *
     */
    @Override
    public void setWantClientAuth(final boolean want) {
        delegate.setWantClientAuth(want);
    }

    /**
     * @see java.net.Socket#shutdownInput()
     *
     */
    @Override
    public void shutdownInput() throws IOException {
        delegate.shutdownInput();
    }

    /**
     * @see java.net.Socket#shutdownOutput()
     *
     */
    @Override
    public void shutdownOutput() throws IOException {
        delegate.shutdownOutput();
    }

    /**
     * @see javax.net.ssl.SSLSocket#startHandshake()
     *
     */
    @Override
    public void startHandshake() throws IOException {
        delegate.startHandshake();
    }

    /**
     * @see java.net.Socket#toString()
     *
     */
    @Override
    public String toString() {
        return delegate.toString();
    }
}
