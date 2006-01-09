/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.java.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class StackLayout implements LayoutManager2 {

	public enum Orientation { BOTTOM, TOP }

	/**
	 * List of componets this manager is responsible for.
	 */
	private final List<Component> components;

	/**
	 * Create a StackLayout.
	 * 
	 */
	public StackLayout() {
		super();
		this.components = new LinkedList<Component>();
	}

	/**
	 * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component,
	 *      java.lang.Object)
	 * 
	 */
    public void addLayoutComponent(Component comp, Object constraints) {
        synchronized (comp.getTreeLock()) {
            if(Orientation.BOTTOM == constraints) { components.add(0, comp); }
            else if(Orientation.TOP == constraints) { components.add(comp); }
            else { components.add(comp); }
        }
    }

    /**
	 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
	 *      java.awt.Component)
	 * 
	 */
    public void addLayoutComponent(String name, Component comp) {
        addLayoutComponent(comp, Orientation.TOP);
    }

    /**
	 * @see java.awt.LayoutManager2#getLayoutAlignmentX(java.awt.Container)
	 * 
	 */
    public float getLayoutAlignmentX(Container target) { return 0.5f; }

    /**
	 * @see java.awt.LayoutManager2#getLayoutAlignmentY(java.awt.Container)
	 * 
	 */
    public float getLayoutAlignmentY(Container target) { return 0.5f; }

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
        synchronized(parent.getTreeLock()) {
            final Rectangle bounds =
            	new Rectangle(0, 0, parent.getWidth(), parent.getHeight());
            final int numberOfComponents = components.size();
            Component component;
            for(int i = 0; i < numberOfComponents; i++) {
            	component = components.get(i);
            	component.setBounds(bounds);
                parent.setComponentZOrder(component, numberOfComponents - i - 1);
            }
        }
    }

    /**
	 * @see java.awt.LayoutManager2#maximumLayoutSize(java.awt.Container)
	 * 
	 */
    public Dimension maximumLayoutSize(Container target) {
    	return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
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
            for(Component component : components) {
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
        synchronized(parent.getTreeLock()) {
            int width = 0;
            int height = 0;

            // grab the max height\width of all components
            Dimension maxSize;
            for(Component component : components) {
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
	 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	 * 
	 */
    public void removeLayoutComponent(Component comp) {
        synchronized(comp.getTreeLock()) { components.remove(comp); }
    }
}
