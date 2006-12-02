/*
 * Created On:  1-Dec-06 9:09:54 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface VersionsCell {
    public String getText();
    public Color getTextForeground();
    public Icon getIcon();
    public void showPopup(final Component invoker, final int x, final int y);
}
