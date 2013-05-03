/*
 * Created On:  Nov 19, 2007 10:11:57 AM
 */
package com.thinkparity.ophelia.support.ui.window;

import javax.swing.JFrame;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractWindow extends JFrame implements Window {

    /** A window configuration. */
    private final WindowConfiguration configuration;

    /** A window context. */
    private final WindowContext context;

    /** A window id. */
    private final String id;

    /**
     * Create AbstractWindow.
     *
     */
    protected AbstractWindow(final String id) {
        super();
        this.id = id;

        this.configuration = new WindowConfiguration(id);
        this.context = new WindowContext();

        setDefaultCloseOperation(configuration.getDefaultCloseOperation());
        setIconImage(com.thinkparity.ophelia.browser.Constants.Images.WINDOW_ICON_IMAGE);
    }

    /**
     * @see com.thinkparity.ophelia.support.ui.window.Window#getId()
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Obtain a context.
     * 
     * @return A <code>WindowContext</code>.
     */
    protected WindowContext getContext() {
        return context;
    }
}
