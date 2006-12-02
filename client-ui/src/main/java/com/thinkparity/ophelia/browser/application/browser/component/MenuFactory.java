/*
 * Mar 10, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.component;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.thinkparity.codebase.swing.border.DropShadowBorder;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.Constants.PopupMenuInfo;
import com.thinkparity.ophelia.browser.application.browser.BrowserMenu;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MenuFactory {

	/** The count of popup menus. */
    private static Integer popupMenuCount = 0;

	/** A singleton instance. */
	private static final MenuFactory SINGLETON;
    
    static { SINGLETON = new MenuFactory(); }

    /**
     * Create a JMenu.
     * 
     * @param text
     *          The menu text.
     * @return The JMenu.
     */    
    public static JMenu create(final String text) {
        return SINGLETON.doCreate(text);
    }

    /**
     * Create a menu.
     * 
     * @param text
     *            The menu text <code>String</code>.
     * @param mnemonic
     *            The menu mnemonic <code>String</code>.
     * @return A <code>JMenu</code>.
     */
    public static JMenu create(final String text, final String mnemonic) {
        return SINGLETON.doCreate(text, mnemonic);
    }

    /**
	 * Create a JPopupMenu.
	 * 
	 * @return The JPopupMenu.
	 */
	public static JPopupMenu createPopup() {
		return SINGLETON.doCreatePopup();
	}
    
    /**
     * Determine if there is a popup menu showing. Will return
     * true if a popup menu was very recently made invisible.
     * 
     * @return true if there is a popup menu.
     */
    public static Boolean isPopupMenu() {
        return(popupMenuCount>0);
    }

	/**
	 * Create a MenuFactory [Singleton, Factory]
	 * 
	 */
	private MenuFactory() { super(); }

	private JMenu doCreate(final String text) {
        try {
            final BrowserMenu browserMenu = new BrowserMenu(text);
            return browserMenu;
        } catch (final AWTException awtx) {
            throw new BrowserException("Cannot instantiate browser menu.", awtx);
        }
    }

    /**
     * Create a menu.
     * 
     * @param text
     *            The menu text.
     * @param mnemonic
     *            The menu mnemonic.
     * @return A <code>JMenu</code>.
     */
    private JMenu doCreate(final String text, final String mnemonic) {
        final JMenu jMenu = doCreate(text);
        jMenu.setMnemonic(Integer.valueOf(mnemonic.charAt(0)));
        return jMenu;
    }

    /**
	 * Create a JPopupMenu.
	 * 
	 * @return The JPopupMenu.
	 */
	private JPopupMenu doCreatePopup() {
        try {
            JPopupMenu jPopupMenu = new ShadowPopupMenu();
            jPopupMenu.addPopupMenuListener(new PopupMenuListener() {
                public void popupMenuCanceled(PopupMenuEvent e) {
                }
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    final Timer popupTimer = new Timer(PopupMenuInfo.ACTIVATION_DELAY, new ActionListener() {
                        public void actionPerformed(final ActionEvent timerEvent) {
                            popupMenuCount--;
                        }
                    });
                    popupTimer.setRepeats(false);
                    popupTimer.start();
                }
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    popupMenuCount++;
                }
            });
            
    		return jPopupMenu;
        } catch (final AWTException awtx) {
            throw new BrowserException("Cannot instantiate shadow popop menu.", awtx);
        }
	}

    /**
     * A popup menu that has a shadow.
     */
    private class ShadowPopupMenu extends JPopupMenu {
        
        /** A drop shadow border. */
        private final DropShadowBorder dropShadowBorder;
        
        /**
         * Create ShadowPopupMenu.
         *
         * @throws AWTException
         */
        private ShadowPopupMenu() throws AWTException {
            super();
            dropShadowBorder = new DropShadowBorder();
            setBorder(dropShadowBorder);
        }

        /**
         * @see javax.swing.JPopupMenu#show(java.awt.Component, int, int)
         * 
         */
        @Override
        public void show(final Component invoker, final int x, final int y) {
            final Dimension preferredSize = getPreferredSize();
            dropShadowBorder.paintUnderneathBorder(invoker, x, y,
                    preferredSize.width, preferredSize.height);
            super.show(invoker, x, y);
        }
    }
}
