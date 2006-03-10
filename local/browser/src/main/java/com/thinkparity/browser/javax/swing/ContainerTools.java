/*
 * Mar 9, 2006
 */
package com.thinkparity.browser.javax.swing;

import java.awt.Component;
import java.awt.Container;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.log4j.Logger;

import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.codebase.StringUtil.Separator;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class ContainerTools {

	/**
	 * An apache logger.
	 * 
	 */
	final Logger logger;

	/**
	 * The container.
	 * 
	 */
	private final Container container;

	/**
	 * Create a ContainerTools.
	 * 
	 */
	ContainerTools(final Container container) {
		super();
		this.container = container;
		this.logger = LoggerFactory.getLogger(getClass());
	}

	/**
	 * Debug all components of the container.
	 *
	 */
	void debugComponents() {
		if(logger.isDebugEnabled()) { debugComponents(container); }
	}

	/**
	 * Debug the geometry of the container up to the root.
	 * 
	 */
	void debugGeometry() {
		if(logger.isDebugEnabled()) { debugGeometry(container); }
	}

	/**
	 * Debug the look and feel of the container.
	 *
	 */
	void debugLookAndFeel() {
		if(logger.isDebugEnabled()) {
			final StringBuffer message =
				new StringBuffer("[BROWSER2] [JAVAX] [DEBUG LnF] [")
				.append(container.getClass().getSimpleName())
				.append("] [lnf:")
				.append(UIManager.getLookAndFeel().getClass().getName())
				.append("] [i lnf:");
			boolean isFirst = true;
			for(final LookAndFeelInfo lnfi : UIManager.getInstalledLookAndFeels()) {
				if(isFirst) { isFirst = false; }
				else { message.append(","); }
				message.append(lnfi.getClassName());
			}
			logger.debug(message.append("]"));
		}
	}

	/**
	 * Debug all components of a container recursively.
	 * 
	 * @param container
	 *            The container to debug.
	 */
	private void debugComponents(final Container container) {
		final Component[] components = container.getComponents();
		final StringBuffer message =
			new StringBuffer("[BROWSER2] [JAVAX] [DEBUG COMPONENTS] [")
			.append(container.getClass().getSimpleName())
			.append("] [c:");
		boolean isFirst = true;
		for(final Component c : components) {
			if(isFirst) { isFirst = false; }
			else { message.append(Separator.Comma); }
			if(c instanceof Container) { debugComponents((Container) c); }
			message.append(c.getClass().getSimpleName());
		}
		logger.debug(message.append("]"));
	}

	/**
	 * Debug the geometry of the container up to the root.
	 * 
	 * @param container
	 *            The container to debug the geometry for.
	 */
	private void debugGeometry(final Container container) {
		final String message =
			new StringBuffer("[BROWSER2] [JAVAX] [DEBUG GEOMETRY] [")
			.append(container.getClass().getSimpleName())
			.append("] [l:")
			.append(container.getLocation())
			.append("] [b:")
			.append(container.getBounds())
			.append("] [i:")
			.append(container.getInsets())
			.append("]")
			.toString();
		logger.debug(message);
		Container parent = container.getParent();
		if(null != parent) { debugGeometry(parent); }
	}
}
