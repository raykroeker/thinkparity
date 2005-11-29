/*
 * Nov 25, 2005
 */
package com.thinkparity.server;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface ParityServerConstants {

	static final String GD_PLUGIN_NAME =
		"com.thinkparity.server.ParityServer";

	static final String IQ_PARITY_FLAG_HANDER_NAME =
		"com.thinkparity.server.IQParityFlagHandler";

	static final String IQ_PARITY_FLAG_HANDLER_INFO_NAME =
		"flag";

	static final String IQ_PARITY_FLAG_HANDLER_INFO_NAMESPACE =
		"jabber:iq:parity";

	static final String IQ_PARITY_SUBSCRIPTION_HANDER_NAME =
		"com.thinkparity.server.IQParitySubscriptionHandler";

	static final String IQ_PARITY_SUBSCRIPTION_HANDLER_INFO_NAME =
		"subscription";

	static final String IQ_PARITY_SUBSCRIPTION_HANDLER_INFO_NAMESPACE =
		"jabber:iq:parity";

	public static final String LOG4J_DIRECTORY_NAME = "log4j";
}
