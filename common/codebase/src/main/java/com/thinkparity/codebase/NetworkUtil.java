/*
 * Sep 16, 2003
 */
package com.thinkparity.codebase;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;

/**
 * <b>Title:</b>  NetworkUtil
 * <br><b>Description:</b>  Provides a simple set of network utility functions.
 * NetworkUtil has the capability to perform a lookup on a network name and
 * determine its ip.  This is done be using the ping :) method.  It can also start
 * a socket listener, and a socket client using secure sockets and supports a limited
 * set of commands through the encrypted tunnel.
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public abstract class NetworkUtil {

	/**
	 * Represents the localhost ip address.
	 */
	private static InetAddress localHost;

	/**
	 * Config for NetworkUtil
	 */
	private static final Config networkUtilConfig =
		ConfigFactory.newInstance(NetworkUtil.class);

	/**
	 * Obtain the host address of the localhost.
	 * @return <code>java.lang.String</code>
	 * @throws UnknownHostException
	 */
	public static synchronized String getIp() throws UnknownHostException {
		initLocalHost(false);
		return localHost.getHostAddress();
	}

	/**
	 * Obtain the host name of the localhost.
	 * @return <code>java.lang.String</code>
	 * @throws UnknownHostException
	 */
	public static synchronized String getMachine()
		throws UnknownHostException {
		initLocalHost(false);
		return localHost.getHostName();
	}

    /**
     * Determine if the host is reachable on the given port. This is done by
     * opening a simple socket to the host on the given port. If any error
     * occurs the host is deemed unreachable.
     * 
     * @param host
     *            A host name or ip.
     * @param port
     *            A port.
     * @return True if this socket can be opened and closed; false otherwise.
     */
    public static Boolean isTargetReachable(final String host,
            final Integer port) {
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            return Boolean.TRUE;
        }
        catch(final Throwable t) { return Boolean.FALSE; }
        finally {
            if(null != socket) {
                try { socket.close(); }
                catch(final Throwable t) { return Boolean.FALSE; }
            }
        }
    }

	/**
	 * Ping the specified host and return a colon separated list of all hosts and their
	 * related host addresses.  If hostName is null, an internal default is used
	 * instead.
	 * @param hostName <code>java.lang.String</code>
	 * @return <code>java.lang.String</code>
	 * @throws UnknownHostException
	 */	
	public static synchronized String ping(String hostName)
		throws UnknownHostException {
		if(null == hostName)
			hostName = networkUtilConfig.getProperty("ping.hostName");
		InetAddress[] addresses = InetAddress.getAllByName(hostName);
		StringBuffer pingResult = new StringBuffer();
		for(int i = 0; i < addresses.length; i++) {
			if(0 != i)
				pingResult.append(Separator.CommaSpace);
			pingResult.append(addresses[i].getHostName())
				.append(Separator.FullColon)
				.append(addresses[i].getHostAddress());
		}
		return pingResult.toString();
	} 

	/**
	 * Initialize the localhost's internet address.
	 * @param doRefresh <code>boolean</code>
	 * @throws UnknownHostException
	 */
	private static synchronized void initLocalHost(boolean doRefresh)
		throws UnknownHostException {
		if(null == localHost || doRefresh) {
			localHost= InetAddress.getLocalHost();
		}
	}

	/**
	 * Create a new NetworkUtil [Singleton]
	 */
	private NetworkUtil() {super();}

}
