/**
 * Created On: 28-May-07 1:37:35 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import java.awt.event.MouseAdapter;

import com.thinkparity.ophelia.browser.util.localization.Localization;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PanelListManager {

    /** The count <code>JLabel</code>. */
    private final javax.swing.JLabel countJLabel;

    /** The first <code>JLabel</code>. */
    private final javax.swing.JLabel firstJLabel;

    /** The last <code>JLabel</code>. */
    private final javax.swing.JLabel lastJLabel;

    /** The <code>Localization</code>. */
    private final Localization localization;

    /** <code>PanelListModel</code>. */
    private final PanelListModel model;

    /** The next <code>JLabel</code>. */
    private final javax.swing.JLabel nextJLabel;

    /** The previous <code>JLabel</code>. */
    private final javax.swing.JLabel previousJLabel;

    /**
     * Create a <code>PanelListManager</code>.
     * 
     * @param model
     *            A <code>PanelListModel</code>.
     * @param localization
     *            A <code>Localization</code>.
     * @param firstJLabel
     *            The first <code>JLabel</code>.
     * @param previousJLabel
     *            The previous <code>JLabel</code>.
     * @param countJLabel
     *            The count <code>JLabel</code>.
     * @param nextJLabel
     *            The next <code>JLabel</code>.
     * @param lastJLabel
     *            The last <code>JLabel</code>.
     */
    public PanelListManager(
            final PanelListModel model,
            final Localization localization,
            final javax.swing.JLabel firstJLabel,
            final javax.swing.JLabel previousJLabel,
            final javax.swing.JLabel countJLabel,
            final javax.swing.JLabel nextJLabel,
            final javax.swing.JLabel lastJLabel) {
        super();
        this.model = model;
        model.setPanelListManager(this);
        this.localization = localization;
        this.firstJLabel = firstJLabel;
        this.previousJLabel = previousJLabel;
        this.countJLabel = countJLabel;
        this.nextJLabel = nextJLabel;
        this.lastJLabel = lastJLabel;
        initializeMouseListeners();
    }

    /**
     * Initialize, called by the model when it is populated or changed.
     */
    public void initialize() {
        reloadControls();
    }

    /**
     * Get the number of pages.
     * 
     * @return The <code>int</code> number of pages.
     */
    private int getNumberPages() {
        return model.getNumberPages();
    }

    /**
     * Get the selected page.
     * 
     * @return The <code>int</code> selected page.
     */
    private int getSelectedPage() {
        return model.getSelectedPage();
    }

    /**
     * Handle a mouse press on a label.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jLabelMousePressed(final java.awt.event.MouseEvent e) {
        if (e.getSource().equals(firstJLabel)) {
            setSelectedPage(0);
        } else if (e.getSource().equals(previousJLabel)) {
            setSelectedPage(getSelectedPage() - 1);
        } else if (e.getSource().equals(nextJLabel)) {
            setSelectedPage(getSelectedPage() + 1);
        } else if (e.getSource().equals(lastJLabel)) {
            setSelectedPage(getNumberPages() - 1);
        }
        reloadControls();
    }

    /**
     * Initialize mouse listeners.
     */
    private void initializeMouseListeners() {
        MouseAdapter mouseAdapter = new java.awt.event.MouseAdapter() {
            public void mousePressed(final java.awt.event.MouseEvent e) {
                jLabelMousePressed(e);
            }
        };
        this.firstJLabel.addMouseListener(mouseAdapter);
        this.previousJLabel.addMouseListener(mouseAdapter);
        this.nextJLabel.addMouseListener(mouseAdapter);
        this.lastJLabel.addMouseListener(mouseAdapter);
    }

    /**
     * Reload controls.
     */
    private void reloadControls() {
        reloadFirst();
        reloadPrevious();
        reloadCount();
        reloadNext();
        reloadLast();
    }

    /**
     * Reload the count JLabel.
     */
    private void reloadCount() {
        if (getNumberPages() > 1) {
            countJLabel.setText(localization.getString("countJLabel", new Object[] {getSelectedPage()+1, getNumberPages()}));
            countJLabel.setVisible(true);
        } else {
            countJLabel.setVisible(false);
        }
    }

    /**
     * Reload the first JLabel.
     */
    private void reloadFirst() {
        firstJLabel.setVisible(getSelectedPage() > 1);
    }

    /**
     * Reload the last JLabel.
     */
    private void reloadLast() {
        lastJLabel.setVisible(getNumberPages() > 1 && getSelectedPage()+2 < getNumberPages());
    }

    /**
     * Reload the next JLabel.
     */
    private void reloadNext() {
        nextJLabel.setVisible(getNumberPages() > 1 && getSelectedPage()+1 < getNumberPages());
    }

    /**
     * Reload the previous JLabel.
     */
    private void reloadPrevious() {
        previousJLabel.setVisible(getSelectedPage() > 0);
    }

    /**
     * Set the selected page.
     * 
     * @param selectedPage
     *            The <code>int</code> selected page.
     */
    private void setSelectedPage(final int selectedPage) {
        model.setSelectedPage(selectedPage);
    }
}
