/*
 * Nov 25, 2005
 */
package com.thinkparity.server;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface ParityServerConstants {

	public static final String CLIENT_RESOURCE = "parity";

	static final String GD_PLUGIN_NAME =
		"com.thinkparity.server.ParityServer";

	static final String IQ_PARITY_ARTIFACT_HANDER_NAME =
		"com.thinkparity.server.handler.IQParityArtifactHandler";

	static final String IQ_PARITY_ARTIFACT_HANDLER_INFO_NAMESPACE =
		"jabber:iq:parity:artifact";

	static final String IQ_PARITY_FLAG_HANDER_NAME =
		"com.thinkparity.server.handler.IQParityFlagHandler";

	static final String IQ_PARITY_FLAG_HANDLER_INFO_NAMESPACE =
		"jabber:iq:parity:flag";

	static final String IQ_PARITY_INFO_NAME =
		"query";

	static final String IQ_PARITY_SUBSCRIPTION_HANDER_NAME =
		"com.thinkparity.server.handler.IQParitySubscriptionHandler";

	static final String IQ_PARITY_SUBSCRIPTION_HANDLER_INFO_NAMESPACE =
		"jabber:iq:parity:subscription";

	static final String LOG4J_DIRECTORY_NAME = "log4j";
}
