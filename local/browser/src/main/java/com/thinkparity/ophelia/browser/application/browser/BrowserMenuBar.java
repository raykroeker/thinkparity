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
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.component.ButtonFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * TODO replace with some simple buttons
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

    /** Sign-Up label icon. */
    private static final Icon SIGNUP_ICON;

    /** Sign-Up label rollover icon. */
    private static final Icon SIGNUP_ROLLOVER_ICON;

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

        SIGNUP_ICON = ImageIOUtil.readIcon("BrowserTitle_SignUp.png");
        SIGNUP_ROLLOVER_ICON = ImageIOUtil.readIcon("BrowserTitle_SignUpRollover.png");

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
            if (!browser.isBrowserWindowMaximized()) {
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
        this.add(Box.createHorizontalGlue());
        if (browser.getPlatform().isSignUpAvailable()) {
            this.add(getSignUpButton());
            this.add(Box.createRigidArea(new Dimension(2,0)));
        }

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

    /**
     * Get a button sized for an icon.
     * 
     * @param icon
     *          The icon.
     * @return A JButton.
     */
    private javax.swing.JButton getButton(final Icon icon) {
        final javax.swing.JButton button = ButtonFactory.create(icon);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFocusable(false);
        final Dimension size = new java.awt.Dimension(icon.getIconWidth(), icon.getIconHeight());
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        button.setPreferredSize(size);
        
        return button;
    }

    private javax.swing.JButton getSignUpButton() {
        final javax.swing.JButton signUpJButton = getButton(SIGNUP_ICON);
        signUpJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(final java.awt.event.MouseEvent e) {
                signUpJButtonMouseEntered(e);
            }
            public void mouseExited(final java.awt.event.MouseEvent e) {
                signUpJButtonMouseExited(e);
            }
        });
        signUpJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                signUpJButtonActionPerformed(e);
            }
        });

        return signUpJButton;
    }

    private javax.swing.JButton getMinimizeButton() {
        final javax.swing.JButton minimizeJButton = getButton(MINIMIZE_ICON);
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
        final javax.swing.JButton maximizeJButton = getButton(MAXIMIZE_ICON);
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
        final javax.swing.JButton closeJButton = getButton(CLOSE_ICON);
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

    private void signUpJButtonActionPerformed(final java.awt.event.ActionEvent e) {
        browser.runProfileSignUp();
    }

    private void signUpJButtonMouseEntered(final java.awt.event.MouseEvent e) {
        SwingUtil.setCursor((javax.swing.JButton) e.getSource(), java.awt.Cursor.HAND_CURSOR);
        ((javax.swing.JButton) e.getSource()).setIcon(SIGNUP_ROLLOVER_ICON);
    }

    private void signUpJButtonMouseExited(final java.awt.event.MouseEvent e) {
        SwingUtil.setCursor((javax.swing.JButton) e.getSource(), java.awt.Cursor.DEFAULT_CURSOR);
        ((javax.swing.JButton) e.getSource()).setIcon(SIGNUP_ICON);
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
