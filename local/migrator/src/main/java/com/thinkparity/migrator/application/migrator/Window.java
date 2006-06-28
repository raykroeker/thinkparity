/*
 * Created on June 25, 2006, 11:38 AM
 * $Id; $
 */
package com.thinkparity.migrator.application.migrator;

import java.awt.Dimension;

import com.thinkparity.migrator.application.migrator.avatar.Avatar;
import com.thinkparity.migrator.javax.swing.AbstractJFrame;
import com.thinkparity.migrator.javax.swing.AbstractJPanel;

/**
 *
 * @author raymond@thinkparity.com
 * @version $Revision$s
 */
public class Window extends AbstractJFrame {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The migrator application. */
    private final Application application;

    /** The main panel. */
    private AbstractJPanel mainPanel;

    /** Creates new form Window */
    public Window(final Application application) {
        super("Migrator");
        this.application = application;
    }

    void displayAvatar(final Avatar avatar) {
        mainPanel.removeAll();
        mainPanel.add(avatar);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Open the window.
     *
     */
    void open() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() { reopen(); }
        });
    }

    void reopen() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        mainPanel = new AbstractJPanel("MainPanel") {
            /** @see java.io.Serializable */
            private static final long serialVersionUID = 1;
        };
        add(mainPanel);
        setSize(new Dimension(500, 500));

        setVisible(true);
        applyRenderingHints();
        debugGeometry();
        debugLookAndFeel();

        application.displayReleaseSummary();
    }
}
