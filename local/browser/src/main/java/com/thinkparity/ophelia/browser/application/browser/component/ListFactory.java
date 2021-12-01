/*
 * Feb 2, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.component;

import javax.swing.JList;

/**
 * A swing list factory. Used to create all swing lists.
 * 
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class ListFactory extends ComponentFactory {
    
    private static final ListFactory SINGLETON;
    
    static {
        SINGLETON = new ListFactory();
    }
    
    /**
     * Create a swing list.
     *
     * @return A swing list.
     */
    public static JList create() { return SINGLETON.doCreate(); }
    
    /**
     * Create a ListFactory.
     */
    private ListFactory() { super(); }
    
    /**
     * Create a swing list.
     *
     * @return A swing list.
     */
    private JList doCreate() { return new JList(); }
}
