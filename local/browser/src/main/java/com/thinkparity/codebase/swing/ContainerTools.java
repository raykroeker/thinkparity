/*
 * Mar 9, 2006
 */
package com.thinkparity.codebase.swing;

import java.awt.Component;
import java.awt.Container;
import java.text.MessageFormat;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.log4j.Logger;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class ContainerTools {

	/** An apache logger. */
	final Logger logger;

	/** A container. */
	private final Container container;

	/**
     * Create ContainerTools.
     * 
     * @param container
     *            The <code>Container</code> to analyze.
     */
	ContainerTools(final Container container) {
		super();
		this.container = container;
		this.logger = Logger.getLogger(getClass());
	}

	/**
	 * Debug all components of the container.
	 *
	 */
	void debug() {
		if (logger.isDebugEnabled()) {
		    for (final LookAndFeelInfo lnfi :
                    UIManager.getInstalledLookAndFeels()) {
		        logger.debug(lnfi.getClassName());
		        debug(0, container);
            }
		}
	}

    /**
     * Debug the container.
     * 
     * @param depth
     *            The depth of the container.
     * @param container
     *            A container.
     */
    private void debug(final Integer depth, final Container container) {
        final Component[] components = container.getComponents();
        final StringBuffer offset = new StringBuffer();
        for (int i = 0; i < depth; i++)
            offset.append("   ");
        logger.debug(
                MessageFormat.format("{0}[{1}]",
                        offset, container.getClass().getSimpleName()));
        logger.debug(
                MessageFormat.format("{0}\t[{1}] [{2}]",
                        offset, "l", container.getLocation()));
        logger.debug(
                MessageFormat.format("{0}\t[{1}] [{2}]",
                        offset, "b", container.getBounds()));
        logger.debug(
                MessageFormat.format("{0}\t[{1}] [{2}]",
                        offset, "i", ((Container) container).getInsets()));
        for (final Component component : components) {
            offset.setLength(0);
            for (int i = 0; i < depth; i++)
                offset.append("   ");
            logger.debug(
                    MessageFormat.format("{0}[{1}]",
                            offset, component.getClass().getSimpleName()));
            logger.debug(
                    MessageFormat.format("{0}\t[{1}] [{2}]",
                            offset, "l", component.getLocation()));
            logger.debug(
                    MessageFormat.format("{0}\t[{1}] [{2}]",
                            offset, "b", component.getBounds()));
            if (component instanceof Container) {
                logger.debug(
                        MessageFormat.format("{0}\t[{1}] [{2}]",
                                offset, "i", ((Container) component).getInsets()));
                debug(depth + 1, (Container) component);
            }
        }
    }
}
