/*
 * Created On: Aug 22, 2006 2:22:46 PM
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JMenuBar;

import com.thinkparity.codebase.swing.GradientPainter;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class BrowserMenuBar extends JMenuBar {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    /** The browser application. */
    private final Browser browser;
    
    /** Close label icon. */
    private static final Icon CLOSE_ICON;
    
    /** Close label rollover icon. */
    private static final Icon CLOSE_ROLLOVER_ICON;

    /** Minimize label icon. */
    private static final Icon MINIMIZE_ICON;
    
    /** Minimize label rollover icon. */
    private static final Icon MINIMIZE_ROLLOVER_ICON;
    
    /** Maximize label icon. */
    private static final Icon MAXIMIZE_ICON;
    
    /** Maximize label rollover icon. */
    private static final Icon MAXIMIZE_ROLLOVER_ICON;
    
    /** Un-Maximize label icon. */
    private static final Icon UNMAXIMIZE_ICON;
    
    /** Un-Maximize label rollover icon. */
    private static final Icon UNMAXIMIZE_ROLLOVER_ICON;
    
    static {
        CLOSE_ICON = ImageIOUtil.readIcon("BrowserTitle_Close.png");
        CLOSE_ROLLOVER_ICON = ImageIOUtil.readIcon("BrowserTitle_CloseRollover.png");

        MINIMIZE_ICON = ImageIOUtil.readIcon("BrowserTitle_Minimize.png");
        MINIMIZE_ROLLOVER_ICON = ImageIOUtil.readIcon("BrowserTitle_MinimizeRollover.png");
        
        MAXIMIZE_ICON = ImageIOUtil.readIcon("BrowserTitle_Maximize.png");
        MAXIMIZE_ROLLOVER_ICON = ImageIOUtil.readIcon("BrowserTitle_MaximizeRollover.png");
        
        UNMAXIMIZE_ICON = ImageIOUtil.readIcon("BrowserTitle_UnMaximize.png");
        UNMAXIMIZE_ROLLOVER_ICON = ImageIOUtil.readIcon("BrowserTitle_UnMaximizeRollover.png");
    }

    /**
     * @see javax.swing.JMenuBar#paintBorder(java.awt.Graphics)
     */
    @Override
    protected void paintBorder(Graphics g) {
        // Prevent the drawing of the border.
    }
    
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     * 
     */
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g.create();
        try {
            GradientPainter.paintVertical(g2, getSize(),
                    com.thinkparity.ophelia.browser.Constants.Colors.Browser.MainTitleTop.BG_GRAD_START,
                    com.thinkparity.ophelia.browser.Constants.Colors.Browser.MainTitleTop.BG_GRAD_FINISH);
            
            // These images help to make the rounded corner look good.
            g2.drawImage(Images.BrowserTitle.BROWSER_TOP_LEFT_INNER,
                    0,
                    0,
                    Images.BrowserTitle.BROWSER_TOP_LEFT_INNER.getWidth(),
                    Images.BrowserTitle.BROWSER_TOP_LEFT_INNER.getHeight(), this);
            g2.drawImage(Images.BrowserTitle.BROWSER_TOP_RIGHT_INNER,
                    getSize().width - Images.BrowserTitle.BROWSER_TOP_RIGHT_INNER.getWidth(),
                    0,
                    Images.BrowserTitle.BROWSER_TOP_RIGHT_INNER.getWidth(),
                    Images.BrowserTitle.BROWSER_TOP_RIGHT_INNER.getHeight(), this);
        }
        finally { g2.dispose(); }
    }

    /**
     * Create a Browser menu bar.
     * @param browser
     *          The Browser.
     */
    public BrowserMenuBar(final Browser browser,
            final BrowserWindow browserWindow, final Boolean maximized) {
        super();
        this.browser = browser;
        new Resizer(browser, this, Boolean.FALSE, Resizer.ResizeEdges.TOP);
        installMouseListener();

        // Create the Sign-Up button
        // TODO Add this button back when the user is a guest
        this.add(Box.createHorizontalGlue());
/*        this.add(getSignUpButton());
        this.add(Box.createRigidArea(new Dimension(2,0)));*/

        // Add minimize, maximize and close buttons
        this.add(getMinimizeButton());
        this.add(Box.createRigidArea(new Dimension(2,19)));
        final javax.swing.JButton maximizeButton = getMaximizeButton(maximized);
        this.add(maximizeButton);
        this.add(Box.createRigidArea(new Dimension(2,19)));
        this.add(getCloseButton());
        this.add(Box.createRigidArea(new Dimension(4,19)));

        installWindowStateListener(browserWindow, maximizeButton);
    }

    /**
     * Install a mouse listener. Double click will maximize or un-maximize.
     */
    private void installMouseListener() {
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(final java.awt.event.MouseEvent e) {
                if (e.getButton()==MouseEvent.BUTTON1) {
                    if (e.getClickCount() % 2 == 0) {
                        browser.maximize(!browser.isBrowserWindowMaximized());
                    }
                }
            }
        });
    }

    /**
     * Install a window state listener.
     */
    private void installWindowStateListener(final BrowserWindow browserWindow,
            final javax.swing.JButton maximizeButton) {
        browserWindow.addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(final WindowEvent e) {
                if (e.getID() == WindowEvent.WINDOW_STATE_CHANGED) {
                    setMaximizeJButtonIcon(maximizeButton, browser.isBrowserWindowMaximized());
                }
            }
        });
    }

    private javax.swing.JButton getMinimizeButton() {
        javax.swing.JButton minimizeJButton = new javax.swing.JButton(MINIMIZE_ICON);
        minimizeJButton.setBorderPainted(false);
        minimizeJButton.setContentAreaFilled(false);
        minimizeJButton.setFocusPainted(false);
        minimizeJButton.setFocusable(false);
        minimizeJButton.setMaximumSize(new java.awt.Dimension(14, 14));
        minimizeJButton.setMinimumSize(new java.awt.Dimension(14, 14));
        minimizeJButton.setPreferredSize(new java.awt.Dimension(14, 14));
        minimizeJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(final java.awt.event.MouseEvent e) {
                minimizeJButtonMouseEntered(e);
            }
            public void mouseExited(final java.awt.event.MouseEvent e) {
                minimizeJButtonMouseExited(e);
            }
        });
        minimizeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                minimizeJButtonActionPerformed(e);
            }
        });

        return minimizeJButton;        
    }

    private javax.swing.JButton getMaximizeButton(final Boolean maximized) {
        javax.swing.JButton maximizeJButton = new javax.swing.JButton();
        maximizeJButton.setBorderPainted(false);
        maximizeJButton.setContentAreaFilled(false);
        maximizeJButton.setFocusPainted(false);
        maximizeJButton.setFocusable(false);
        maximizeJButton.setMaximumSize(new java.awt.Dimension(14, 14));
        maximizeJButton.setMinimumSize(new java.awt.Dimension(14, 14));
        maximizeJButton.setPreferredSize(new java.awt.Dimension(14, 14));
        setMaximizeJButtonIcon(maximizeJButton, maximized);
        maximizeJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(final java.awt.event.MouseEvent e) {
                maximizeJButtonMouseEntered(e);
            }
            public void mouseExited(final java.awt.event.MouseEvent e) {
                maximizeJButtonMouseExited(e);
            }
        });
        maximizeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                maximizeJButtonActionPerformed(e);
            }
        });

        return maximizeJButton;        
    }

    private javax.swing.JButton getCloseButton() {
        final javax.swing.JButton closeJButton = new javax.swing.JButton(CLOSE_ICON);
        closeJButton.setBorderPainted(false);
        closeJButton.setContentAreaFilled(false);
        closeJButton.setFocusPainted(false);
        closeJButton.setFocusable(false);
        closeJButton.setMaximumSize(new java.awt.Dimension(14, 14));
        closeJButton.setMinimumSize(new java.awt.Dimension(14, 14));
        closeJButton.setPreferredSize(new java.awt.Dimension(14, 14));
        closeJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(final java.awt.event.MouseEvent e) {
                closeJButtonMouseEntered(e);
            }
            public void mouseExited(final java.awt.event.MouseEvent e) {
                closeJButtonMouseExited(e);
            }
        });
        closeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                closeJButtonActionPerformed(e);
            }
        });
        
        return closeJButton;        
    }

    private void minimizeJButtonActionPerformed(final java.awt.event.ActionEvent e) {
        browser.iconify(Boolean.TRUE);
    }

    private void minimizeJButtonMouseEntered(final java.awt.event.MouseEvent e) {
        ((javax.swing.JButton) e.getSource()).setIcon(MINIMIZE_ROLLOVER_ICON);
    }

    private void minimizeJButtonMouseExited(final java.awt.event.MouseEvent e) {
        ((javax.swing.JButton) e.getSource()).setIcon(MINIMIZE_ICON);
    }

    private void maximizeJButtonActionPerformed(final java.awt.event.ActionEvent e) {
        browser.maximize(!browser.isBrowserWindowMaximized());
        
    }

    private void maximizeJButtonMouseEntered(final java.awt.event.MouseEvent e) {
        if (browser.isBrowserWindowMaximized()) {
            ((javax.swing.JButton) e.getSource()).setIcon(UNMAXIMIZE_ROLLOVER_ICON);
        } else {
            ((javax.swing.JButton) e.getSource()).setIcon(MAXIMIZE_ROLLOVER_ICON);
        }
    }

    private void maximizeJButtonMouseExited(final java.awt.event.MouseEvent e) {
        setMaximizeJButtonIcon((javax.swing.JButton) e.getSource(), browser.isBrowserWindowMaximized());
    }

    private void closeJButtonActionPerformed(final java.awt.event.ActionEvent e) {        
        browser.closeBrowserWindow();
    }

    private void closeJButtonMouseEntered(final java.awt.event.MouseEvent e) {
        ((javax.swing.JButton) e.getSource()).setIcon(CLOSE_ROLLOVER_ICON);
    }

    private void closeJButtonMouseExited(final java.awt.event.MouseEvent e) {
        ((javax.swing.JButton) e.getSource()).setIcon(CLOSE_ICON);
    }

    private void setMaximizeJButtonIcon(final javax.swing.JButton maximizeButton, final Boolean maximized) {
        if (maximized) {
            maximizeButton.setIcon(UNMAXIMIZE_ICON);
        } else {
            maximizeButton.setIcon(MAXIMIZE_ICON);
        }
    }
}
