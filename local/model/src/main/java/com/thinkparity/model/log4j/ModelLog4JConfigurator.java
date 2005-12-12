/*
 * 18-Oct-2005
 */
package com.thinkparity.model.log4j;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.model.log4j.or.org.xmlpull.v1.XmlPullParserRenderer;
import com.thinkparity.model.log4j.or.parity.document.DocumentContentRenderer;
import com.thinkparity.model.log4j.or.parity.document.DocumentRenderer;
import com.thinkparity.model.log4j.or.parity.document.DocumentVersionRenderer;
import com.thinkparity.model.log4j.or.parity.project.ProjectRenderer;
import com.thinkparity.model.log4j.or.smack.packet.MessageRenderer;
import com.thinkparity.model.log4j.or.smack.packet.PacketExtensionRenderer;
import com.thinkparity.model.log4j.or.smack.packet.PacketRenderer;
import com.thinkparity.model.log4j.or.xmpp.document.XMPPDocumentRenderer;
import com.thinkparity.model.log4j.or.xmpp.user.UserRenderer;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentContent;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.xmpp.document.XMPPDocument;
import com.thinkparity.model.xmpp.user.User;

/**
 * Configure the log4j framework for the parity model library. Since the model
 * is a lower level library instead of an application; this configuration should
 * consist mainly of object renderer registration.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.7
 */
class ModelLog4JConfigurator {

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
		configureRenderer(configuration, Document.class, DocumentRenderer.class);
		configureRenderer(configuration, DocumentContent.class, DocumentContentRenderer.class);
		configureRenderer(configuration, DocumentVersion.class, DocumentVersionRenderer.class);
		configureRenderer(configuration, Message.class, MessageRenderer.class);
		configureRenderer(configuration, Packet.class, PacketRenderer.class);
		configureRenderer(configuration, PacketExtension.class, PacketExtensionRenderer.class);
		configureRenderer(configuration, Project.class, ProjectRenderer.class);
		configureRenderer(configuration, XMPPDocument.class, XMPPDocumentRenderer.class);
		configureRenderer(configuration, XmlPullParser.class, XmlPullParserRenderer.class);
	}

	/**
	 * Create a ModelLog4JConfigurator [Singleton]
	 */
	private ModelLog4JConfigurator() { super(); }
}
