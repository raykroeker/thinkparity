/*
 * Feb 2, 2006
 */
package com.thinkparity.browser.application.browser.component;

import com.thinkparity.browser.application.browser.BrowserConstants;
import javax.swing.JList;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * A swing list factory. Used to create all swing lists.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ListFactory extends ComponentFactory {
    
    private static final ListFactory SINGLETON;
    
    static {
        SINGLETON = new ListFactory();
        
        final UIDefaults defaults = UIManager.getDefaults();
        defaults.put("List.selectionBackground", BrowserConstants.SelectionBackground);
        defaults.put("List.selectionForeground", BrowserConstants.SelectionForeground);
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
