/*
 * Nov 28, 2005
 */
package com.thinkparity.server.org.apache.log4j;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Element;
import org.jivesoftware.messenger.IQHandlerInfo;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.artifact.ArtifactSubscription;
import com.thinkparity.server.model.user.User;
import com.thinkparity.server.org.apache.log4j.or.com.thinkparity.server.model.artifact.ArtifactRenderer;
import com.thinkparity.server.org.apache.log4j.or.com.thinkparity.server.model.artifact.ArtifactSubscriptionRenderer;
import com.thinkparity.server.org.apache.log4j.or.com.thinkparity.server.model.user.UserRenderer;
import com.thinkparity.server.org.apache.log4j.or.com.thinkparity.server.org.xmpp.packet.IQParityRenderer;
import com.thinkparity.server.org.apache.log4j.or.org.dom4j.ElementRenderer;
import com.thinkparity.server.org.apache.log4j.or.org.jivesoftware.messenger.IQHandlerInfoRenderer;
import com.thinkparity.server.org.apache.log4j.or.org.xmpp.packet.IQRenderer;
import com.thinkparity.server.org.apache.log4j.or.org.xmpp.packet.JIDRenderer;
import com.thinkparity.server.org.xmpp.packet.IQParity;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ServerLog4jConfigurator {

	/**
	 * Prefix for all renderer properties.
	 */
	private static final String RENDERER_PROPERTY_PREFIX = "log4j.renderer.";

	/**
	 * Singleton instance.
	 */
	private static final ServerLog4jConfigurator singleton;

	/**
	 * Singleton instance synchronization lock.
	 */
	private static final Object singletonLock;

	static {
		singleton = new ServerLog4jConfigurator();
		singletonLock = new Object();
	}

	/**
	 * Configure log4j for the server.
	 * 
	 * @param log4jDirectory
	 *            The log4j output directory.
	 */
	public static void configure(final File log4jDirectory) {
		synchronized(singletonLock) { singleton.configureImpl(log4jDirectory); }
	}

	/**
	 * Obtain the configuration status.
	 * 
	 * @return True if log4j has been configured for the server; false
	 *         otherwise.
	 */
	static Boolean isConfigured() {
		synchronized(singletonLock) { return singleton.isConfiguredImpl(); }
	}

	/**
	 * ArtifactFlag indicating whether or not the configuration has been set.
	 * 
	 * @see ServerLog4jConfigurator#configure()
	 */
	private Boolean isConfigured = Boolean.FALSE;

	/**
	 * Create a ServerLog4jConfigurator [Singleton]
	 */
	private ServerLog4jConfigurator() { super(); }

	/**
	 * Configure log4j for the parity server.
	 *
	 */
	public void configureImpl(final File log4jDirectory) {
		if(Boolean.FALSE == isConfigured) {
			final Properties log4jProperties = new Properties();

			configureGlobal(log4jProperties);
			configureRenderers(log4jProperties);
			configureServerHTMLAppender(log4jProperties, log4jDirectory);

			PropertyConfigurator.configure(log4jProperties);
			isConfigured = Boolean.TRUE;
		}
	}

	/**
	 * Configure the global log4j properties.
	 * 
	 * @param globalProperties
	 *            The global log4j properties.
	 */
	private void configureGlobal(final Properties globalProperties) {
		globalProperties.setProperty("log4j.logger.com.thinkparity.server", "ERROR, serverHTML");
		globalProperties.setProperty("log4j.logger.com.thinkparity.server.ParityServer", "INFO");
		globalProperties.setProperty("log4j.logger.com.thinkparity.server.handler", "INFO");
	}

	/**
	 * Configure a renderer for a server class.
	 * 
	 * @param log4jProperties
	 *            The configuration to set.
	 * @param renderedClass
	 *            The class that will be rendered.
	 * @param renderingClass
	 *            The class that will render.
	 */
	private void configureRenderer(final Properties log4jProperties,
			final Class renderedClass, final Class renderingClass) {
		log4jProperties.setProperty(
				RENDERER_PROPERTY_PREFIX + renderedClass.getName(),
				renderingClass.getName());
	}

	/**
	 * Configure all parity model renderers.
	 * 
	 * @param log4jProperties
	 *            The configuration to set.
	 */
	private void configureRenderers(final Properties log4jProperties) {
		configureRenderer(log4jProperties, Artifact.class, ArtifactRenderer.class);
		configureRenderer(log4jProperties, ArtifactSubscription.class, ArtifactSubscriptionRenderer.class);
		configureRenderer(log4jProperties, Element.class, ElementRenderer.class);
		configureRenderer(log4jProperties, IQ.class, IQRenderer.class);
		configureRenderer(log4jProperties, IQHandlerInfo.class, IQHandlerInfoRenderer.class);
		configureRenderer(log4jProperties, IQParity.class, IQParityRenderer.class);
		configureRenderer(log4jProperties, JID.class, JIDRenderer.class);
		configureRenderer(log4jProperties, User.class, UserRenderer.class);
	}

	/**
	 * Configure the server html log file.
	 * 
	 * @param log4jProperties
	 *            The server html configuration.
	 * @param log4jDirectory
	 *            The log4j output directory.
	 */
	private void configureServerHTMLAppender(
			final Properties log4jProperties, final File log4jDirectory) {
		log4jProperties.setProperty("log4j.appender.serverHTML", "org.apache.log4j.RollingFileAppender");
		log4jProperties.setProperty("log4j.appender.serverHTML.MaxFileSize", "512MB");
		log4jProperties.setProperty("log4j.appender.serverHTML.layout", "org.apache.log4j.HTMLLayout");
		log4jProperties.setProperty("log4j.appender.serverHTML.layout.locationInfo", "true");
		log4jProperties.setProperty("log4j.appender.serverHTML.layout.title", "Parity Server");
		final File serverHTMLFile = new File(log4jDirectory, "parity.server.log4j.html");
		log4jProperties.setProperty("log4j.appender.serverHTML.File", serverHTMLFile.getAbsolutePath());
	}

	/**
	 * Obtain the configuration status.
	 * 
	 * @return True if log4j has been configured for the server; false
	 *         otherwise.
	 */	
	private Boolean isConfiguredImpl() { return isConfigured; }

}
