/*
 * Jan 9, 2006
 */
package com.thinkparity.browser.javax.swing.document;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.thinkparity.browser.log4j.BrowserLoggerFactory;

import com.thinkparity.codebase.assertion.Assert;

/**
 * Layout manager for the document shuffler.  The current layout is similar to
 * a box layout with different insets\ spacing.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentShufferLayout implements LayoutManager2 {

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = BrowserLoggerFactory.getLogger(getClass());

	/**
	 * List of components to layout.
	 * 
	 */
	private final List<Component> components;

	/**
	 * The target container to layout.
	 * 
	 */
	private final Container target;

	/**
	 * Create a DocumentShufferLayout.
	 */
	public DocumentShufferLayout(final Container target) {
		super();
		this.components = new LinkedList<Component>();
		this.target = target;
	}

	/**
	 * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component,
	 *      java.lang.Object)
	 * 
	 */
	public void addLayoutComponent(Component comp, Object constraints) {
		synchronized(comp.getTreeLock()) {
			components.add(comp);
		}
	}

	/**
	 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
	 *      java.awt.Component)
	 * 
	 */
	public void addLayoutComponent(String name, Component comp) {
		addLayoutComponent(comp, null);
	}

	/**
	 * @see java.awt.LayoutManager2#getLayoutAlignmentX(java.awt.Container)
	 * 
	 */
	public float getLayoutAlignmentX(Container target) { return 0.5F; }

	/**
	 * @see java.awt.LayoutManager2#getLayoutAlignmentY(java.awt.Container)
	 * 
	 */
	public float getLayoutAlignmentY(Container target) { return 0F; }

	/**
	 * @see java.awt.LayoutManager2#invalidateLayout(java.awt.Container)
	 * 
	 */
	public void invalidateLayout(Container target) {}

	/**
	 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
	 * 
	 */
	public void layoutContainer(Container parent) {
		assertTargetEquals(parent);
		synchronized(parent.getTreeLock()) {
			final Insets parentInsets = parent.getInsets();
			final int componentHeight = 25;
			final int componentWidth = parent.getWidth() - parentInsets.left - parentInsets.right;
			final int componentX = 12;
			final int componentY = 12;
			final int componentSpacer = 10;
			for(int i = 0; i < components.size(); i++) {
				logger.debug("componentWidth:" + componentWidth);
				components.get(i).setBounds(componentX, componentY + i * (componentHeight + componentSpacer), componentWidth, componentHeight);
				logger.debug("components.get(" + i + ").getBounds():" +
						components.get(i).getBounds().toString());
			}
		}
	}

	/**
	 * @see java.awt.LayoutManager2#maximumLayoutSize(java.awt.Container)
	 * 
	 */
	public Dimension maximumLayoutSize(Container target) {
		assertTargetEquals(target);
		synchronized(target.getTreeLock()) {
			final Insets insets = target.getInsets();
			return new Dimension(
					target.getWidth() - insets.left - insets.right,
					Integer.MAX_VALUE);
		}
	}

	/**
	 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
	 * 
	 */
	public Dimension minimumLayoutSize(Container parent) {
		synchronized(parent.getTreeLock()) {
            int width = 0;
            int height = 0;

            // grab the max height\width of all components
            Dimension maxSize;
            for(final Component component : components) {
            	maxSize = component.getMinimumSize();
                width = Math.max(maxSize.width, width);
                height = Math.max(maxSize.height, height);
            }

            final Insets insets = parent.getInsets();
            width += insets.left + insets.right;
            height += insets.top + insets.bottom;

            return new Dimension(width, height);
        }
	}

	/**
	 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
	 * 
	 */
	public Dimension preferredLayoutSize(Container parent) {
		return maximumLayoutSize(parent);
	}

	/**
	 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	 * 
	 */
	public void removeLayoutComponent(Component comp) {
		synchronized(comp.getTreeLock()) { components.remove(comp); }
	}

	/**
	 * Assert that the target container is the same as the one used in
	 * construction.
	 * 
	 * @param target
	 *            The target container.
	 */
	private void assertTargetEquals(final Container target) {
		Assert.assertTrue(
				"assertTargetEquals(Container)", this.target == target);
	}
}
