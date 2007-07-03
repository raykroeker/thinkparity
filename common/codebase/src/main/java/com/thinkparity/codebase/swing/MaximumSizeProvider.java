/*
 * Created On:  3-Jul-07 3:06:58 PM
 */
package com.thinkparity.codebase.swing;

import java.awt.Dimension;

import javax.swing.JComponent;

/**
 * <b>Title:</b>thinkParity Common Codebase Swing Component Maximum Size
 * Provider<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @param <T>
 *            A <code>JComponent</code> type.
 */
public interface MaximumSizeProvider<T extends JComponent> {

    /**
     * Obtain the maximum size for a component.
     * 
     * @param jComponent
     *            A <code>T</code>.
     * @return The maximum size <code>Dimension</code>.
     */
    public Dimension getMaximumSize(final T jComponent);
}
