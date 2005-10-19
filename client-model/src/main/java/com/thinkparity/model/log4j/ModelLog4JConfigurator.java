/*
 * 18-Oct-2005
 */
package com.thinkparity.model.log4j;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.thinkparity.model.log4j.or.xmpp.user.UserRenderer;
import com.thinkparity.model.xmpp.user.User;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class ModelLog4JConfigurator {

	/**
	 * Flag that keeps the configuration from being performed more than once.
	 */
	private static Boolean isConfigured = Boolean.FALSE;

	/**
	 * Prefix for all renderer properties.
	 */
	private static final String RENDERER_PROPERTY_PREFIX =
		"log4j.renderer.";

	/**
	 * Configure the parity model log4j configuration. This will configure the
	 * renderers.
	 * 
	 */
	static void configure() {
		if(Boolean.FALSE == isConfigured) {
			final Properties configuration = new Properties();

			configureRenderers(configuration);

			PropertyConfigurator.configure(configuration);
			isConfigured = Boolean.TRUE;
		}
	}

	/**
	 * Configure a renderer for a parity class.
	 * 
	 * @param configuration
	 *            The configuration to set.
	 * @param renderedClass
	 *            The class that will be rendered.
	 * @param renderingClass
	 *            The class that will render.
	 */
	private static void configureRenderer(final Properties configuration,
			final Class renderedClass, final Class renderingClass) {
		configuration.setProperty(
				RENDERER_PROPERTY_PREFIX + renderedClass.getName(),
				renderingClass.getName());
	}

	/**
	 * Configure all parity model renderers.
	 * 
	 * @param configuration
	 *            The configuration to set.
	 */
	private static void configureRenderers(final Properties configuration) {
		configureRenderer(configuration, User.class, UserRenderer.class);
	}

	/**
	 * Create a ModelLog4JConfigurator [Singleton]
	 */
	private ModelLog4JConfigurator() { super(); }
}
