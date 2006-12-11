/*
 * Created on July 30, 2006, 3:30 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.border.TopBottomBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.Constants.Search;

/**
 *
 * @author raymond@thinkparity.com
 */
public class MainTitleAvatarSearchPanel extends MainTitleAvatarAbstractPanel {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The search activation timer. */
    private Timer searchActivationTimer;
    
    /** Creates new form BrowserTitleSearch */
    public MainTitleAvatarSearchPanel() {
        super();
        initComponents();
        addMoveListener(this);
        new Resizer(getBrowser(), this, Boolean.FALSE, Resizer.ResizeEdges.RIGHT);       
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g.create();
        try {
            if (searchJTextField.hasFocus()) {
                final Point leftLocation = leftJLabel.getLocation();
                g2.drawImage(Images.BrowserTitle.HALO, leftLocation.x - 1, leftLocation.y - 1, null);
            } else {
                final Point leftLocation = searchJTextField.getLocation();
                g2.drawImage(Images.BrowserTitle.SEARCH_BACKGROUND, leftLocation.x, leftLocation.y + 1, null);   
            }
        } finally {
            g2.dispose();
        }
    }

    /**
     * Apply a search.
     * 
     */
    private void applySearch() {
        mainTitleAvatar.getController().applySearch(
                SwingUtil.extract(searchJTextField));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JLabel rightJLabel;

        rightJLabel = new javax.swing.JLabel();
        searchJTextField = new javax.swing.JTextField();
        searchJTextField.setBorder(new TopBottomBorder(Colors.Browser.MainTitle.SEARCH_OUTLINE));
        searchJTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(final DocumentEvent e) {
                searchJTextFieldChangedUpdate(e);
            }
            public void insertUpdate(final DocumentEvent e) {
                searchJTextFieldInsertUpdate(e);
            }
            public void removeUpdate(final DocumentEvent e) {
                searchJTextFieldRemoveUpdate(e);
            }
        });
        leftJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setOpaque(false);
        rightJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BrowserTitle_SearchRight.png")));
        rightJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 4, 4);
        add(rightJLabel, gridBagConstraints);

        searchJTextField.setMargin(new java.awt.Insets(3, 5, 0, 4));
        searchJTextField.setMinimumSize(new java.awt.Dimension(1, 19));
        searchJTextField.setOpaque(false);
        searchJTextField.setPreferredSize(new java.awt.Dimension(1, 19));
        searchJTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchJTextFieldActionPerformed(evt);
            }
        });
        searchJTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchJTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchJTextFieldFocusLost(evt);
            }
        });
        searchJTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchJTextFieldMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchJTextFieldMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchJTextFieldMouseExited(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 109;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 4, 0);
        add(searchJTextField, gridBagConstraints);

        leftJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BrowserTitle_SearchLeft.png")));
        leftJLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(11, 4, 4, 0);
        add(leftJLabel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void searchJTextFieldMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_searchJTextFieldMouseClicked
        searchJTextField.getCaret().setVisible(true);
        repaint();
    }// GEN-LAST:event_searchJTextFieldMouseClicked

    private void searchJTextFieldMouseExited(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_searchJTextFieldMouseExited
        Window window = SwingUtilities.getWindowAncestor(this);
        if (null!=window) {
            window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }// GEN-LAST:event_searchJTextFieldMouseExited

    private void searchJTextFieldMouseEntered(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_searchJTextFieldMouseEntered
        Window window = SwingUtilities.getWindowAncestor(this);
        if (null!=window) {
            window.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        }
    }// GEN-LAST:event_searchJTextFieldMouseEntered

    private void searchJTextFieldFocusLost(final java.awt.event.FocusEvent e) {//GEN-FIRST:event_searchJTextFieldFocusLost
        if (null != getParent()) {
            // Note that repaint(), or getParent().repaint() on bounds, causes visible flicker drawing rectangle.
            getParent().repaint();
        }
    }//GEN-LAST:event_searchJTextFieldFocusLost

    private void searchJTextFieldFocusGained(final java.awt.event.FocusEvent e) {//GEN-FIRST:event_searchJTextFieldFocusGained
        // NOTE Perhaps reconsider this approach if we need to tab to this control.
        // Don't show the caret or repaint unless the mouse actually clicked in the JTextField
        if (searchJTextField.getCaret().isVisible()) {
            searchJTextField.getCaret().setVisible(false);
        }
    }//GEN-LAST:event_searchJTextFieldFocusGained
    
    /**
     * When the search text field's document is updated; initialize a timer to
     * run the search after a delay. Each update to the text field's document
     * re-sets the timer.
     * 
     * @param e
     *            The document event.
     */
    private void searchFieldUpdated(final DocumentEvent e) {
        if (searchActivationTimer == null) {
            searchActivationTimer= new Timer(Search.ACTIVATION_DELAY, new ActionListener() {
                public void actionPerformed(final ActionEvent timerEvent) {
                    searchActivationTimer.stop();
                    applySearch();
                }
            });
            searchActivationTimer.start();
        }
        else {
            searchActivationTimer.restart();
        }
    }

    /**
     * When the user performs an action on the search text field (i.e. hits
     * enter) the activation timer is re-set and the search run immetiately.
     * 
     * @param evt
     *            The action event.
     */
    private void searchJTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchJTextFieldActionPerformed
        if (searchActivationTimer != null) {
            searchActivationTimer.stop();
        }
        applySearch();
    }//GEN-LAST:event_searchJTextFieldActionPerformed

    /**
     * @see MainTitleAvatarSearchPanel#searchFieldUpdated(DocumentEvent)
     * 
     */
    private void searchJTextFieldChangedUpdate(final DocumentEvent e) {}

    /**
     * @see MainTitleAvatarSearchPanel#searchFieldUpdated(DocumentEvent)
     * 
     */
    private void searchJTextFieldInsertUpdate(final DocumentEvent e) {
        searchFieldUpdated(e);
    }

    /**
     * @see MainTitleAvatarSearchPanel#searchFieldUpdated(DocumentEvent)
     * 
     */
    private void searchJTextFieldRemoveUpdate(final DocumentEvent e) {
        searchFieldUpdated(e);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel leftJLabel;
    private javax.swing.JTextField searchJTextField;
    // End of variables declaration//GEN-END:variables
}
