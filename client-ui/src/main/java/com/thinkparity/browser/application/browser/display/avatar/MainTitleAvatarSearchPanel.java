/*
 * Created on July 30, 2006, 3:30 PM
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.border.TopBottomBorder;

import com.thinkparity.browser.Constants.Colors;
import com.thinkparity.browser.Constants.Images;
import com.thinkparity.browser.Constants.Search;

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
        super("BrowserTitleSearch");
        initComponents();
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (searchJTextField.hasFocus()) {
            final Graphics2D g2 = (Graphics2D) g.create();
            try {
                final Point leftLocation = leftJLabel.getLocation();
                g2.drawImage(Images.BrowserTitle.HALO, leftLocation.x - 4, leftLocation.y - 4, null);
            } finally {
                g2.dispose();
            }
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
        searchJTextField.setBackground(Colors.Browser.MainTitle.SEARCH_BACKGROUND);
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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 11, 7);
        add(rightJLabel, gridBagConstraints);

        searchJTextField.setMinimumSize(new java.awt.Dimension(11, 20));
        searchJTextField.setPreferredSize(new java.awt.Dimension(11, 20));
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 82;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 11, 0);
        add(searchJTextField, gridBagConstraints);

        leftJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BrowserTitle_SearchLeft.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 11, 0);
        add(leftJLabel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void searchJTextFieldFocusLost(java.awt.event.FocusEvent e) {//GEN-FIRST:event_searchJTextFieldFocusLost
        repaint();
    }//GEN-LAST:event_searchJTextFieldFocusLost

    private void searchJTextFieldFocusGained(java.awt.event.FocusEvent e) {//GEN-FIRST:event_searchJTextFieldFocusGained
        repaint();
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
