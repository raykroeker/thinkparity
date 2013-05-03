/*
 * Created On:  4-Dec-06 11:58:21 AM
 */
package com.thinkparity.ophelia.browser.platform.action;

import java.awt.Component;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface PopupDelegate {

    /**
     * Initialize the delegate.
     * 
     * @param invoker
     *            A <code>Component</code> invoker.
     * @param x
     *            The popup x location <code>int</code>.
     * @param y
     *            The popup y location <code>int</code>.
     */
    public void initialize(final Component invoker, final int x, final int y);

    /**
     * Initialize the delegate.
     * 
     * @param invoker
     *            A <code>Component</code> invoker.
     * @param x
     *            The popup x location <code>int</code>.
     * @param leftJustified
     *            The <code>Boolean</code>flag indicating if the popup is left justified.
     * @param topJustified
     *            The <code>Boolean</code>flag indicating if the popup is top justified.    
     */
    public void initialize(final Component invoker, final int x, final int y,
            final Boolean leftJustified, final Boolean topJustified);

    /**
     * Show the popup.
     * 
     */
    public void show();
}