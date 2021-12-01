/*
 * Jan 13, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.component;

import javax.swing.JSeparator;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class SeparatorFactory extends ComponentFactory {
    
    /**
     * Singleton instance.
     *
     */
    private static final SeparatorFactory singleton;
    
    /**
     * Singleton synchronization lock.
     *
     */
    private static final Object singletonLock;
    
    static {
	singleton = new SeparatorFactory();
	singletonLock = new Object();
    }
    
    /**
     * Create a JSeparator.
     *
     * @return The JSeparator.
     */
    public static JSeparator create() {
	synchronized(singletonLock) { return singleton.doCreate(); }
    }
    
    /**
     * Create a SeparatorFactory.
     *
     */
    private SeparatorFactory() { super(); }
    
    /**
     * Create a separator.
     *
     * @return The separator.
     */
    private JSeparator doCreate() {
	final JSeparator jSeparator = new JSeparator();
	return jSeparator;
    }
}
