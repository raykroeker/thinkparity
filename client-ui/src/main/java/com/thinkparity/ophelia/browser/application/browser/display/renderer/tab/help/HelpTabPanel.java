/*
 * HelpTabPanel.java
 *
 * Created on May 24, 2007, 11:35 AM
 */

package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.help;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.model.help.HelpTopic;

import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.PanelListManager;
import com.thinkparity.ophelia.browser.util.localization.BrowserLocalization;
import com.thinkparity.ophelia.browser.util.localization.Localization;

/**
 *
 * @author  user
 */
public class HelpTabPanel extends DefaultTabPanel {

    /** The help tab's <code>ActionDelegate</code>. */
    private ActionDelegate actionDelegate;

    /* The <code>HelpContentModel</code>. */
    private final HelpContentModel helpContentModel;

    /** A <code>HelpTopic</code>. */
    private HelpTopic helpTopic;

    /** The panel localization. */
    private final Localization localization;

    /** The help tab's <code>PopupDelegate</code>. */
    private PopupDelegate popupDelegate;

    /**
     * Create HelpTabPanel.
     * 
     * @param session
     *            A <code>BrowserSession</code>.
     */
    public HelpTabPanel(final BrowserSession session) {
        super(session);
        this.localization = new BrowserLocalization("HelpTabPanel");
        this.helpContentModel = new HelpContentModel(helpContentJTextArea);
        new PanelListManager(helpContentModel, localization, firstJLabel,
                previousJLabel, countJLabel, nextJLabel, lastJLabel);
        initComponents();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#collapse(boolean)
     */
    public void collapse(final boolean animate) {
        setBorder(BORDER_COLLAPSED);
        doCollapse(animate);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#expand(boolean)
     */
    public void expand(final boolean animate) {
        setBorder(BORDER_EXPANDED);
        doExpand(animate);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#expandIconMousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void expandIconMousePressed(final MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            selectPanel();
            tabDelegate.toggleExpansion(this);
        }
    }

    /**
     * Obtain actionDelegate.
     *
     * @return An <code>ActionDelegate</code>.
     */
    public ActionDelegate getActionDelegate() {
        return actionDelegate;
    }

    /**
     * Obtain the help topic.
     * 
     * @return A <code>HelpTopic</code>.
     */
    public HelpTopic getHelpTopic() {
        return helpTopic;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#getId()
     */
    @Override
    public Object getId() {
        return new StringBuffer(getClass().getName()).append("//")
            .append(helpTopic.getId()).toString();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#getPopupDelegate()
     */
    public PopupDelegate getPopupDelegate() {
        return popupDelegate;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#isSetExpandedData()
     */
    public Boolean isSetExpandedData() {
        // NOCOMMIT
        return isSetHelpTopic();// && getHelpTopic().isSetContent();
    }

    /**
     * Determine if the help topic is set.
     * 
     * @return True if the help topic is set.
     */
    public Boolean isSetHelpTopic() {
        return null != helpTopic;
    }

    /**
     * Set the action delegate.
     *
     * @param actionDelegate
     *            An <code>ActionDelegate</code>.
     */
    public void setActionDelegate(final ActionDelegate actionDelegate) {
        this.actionDelegate = actionDelegate;
    }

    /**
     * Set the panel data.
     * 
     * @param helpTopic
     *            A <code>HelpTopic</code>.
     */
    public void setPanelData(final HelpTopic helpTopic) {
        this.helpTopic = helpTopic;
        reload(collapsedTextJLabel, helpTopic.getName());
        reload(expandedTextJLabel, helpTopic.getName());
        // NOCOMMIT
        helpContentModel.initialize(null);
        collapsedMovieJLabel.setVisible(helpTopic.isSetMovie());
        expandedMovieJLabel.setVisible(helpTopic.isSetMovie());
    }

    /**
     * Set the popup delegate.
     *
     * @param popupDelegate
     *            An <code>PopupDelegate</code>.
     */
    public void setPopupDelegate(final PopupDelegate popupDelegate) {
        this.popupDelegate = popupDelegate;
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final int height = getHeight() - getBorder().getBorderInsets(this).top
                - getBorder().getBorderInsets(this).bottom;
        adjustBorderColor(isExpanded());
        if (isExpanded() || isAnimating()) {
            renderer.paintExpandedBackground(g, this);
        } else {
            renderer.paintBackground(g, getWidth(), height, selected);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#repaintLists()
     */
    @Override
    protected void repaintLists() {}

    private void collapsedJPanelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedJPanelMousePressed
        jPanelMousePressed((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_collapsedJPanelMousePressed

    private void collapsedJPanelMouseReleased(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedJPanelMouseReleased
        jPanelMouseReleased((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_collapsedJPanelMouseReleased

    private void collapsedMovieJLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedMovieJLabelMousePressed
        movieJLabelMousePressed(e);
    }//GEN-LAST:event_collapsedMovieJLabelMousePressed

    private void collapseIconJLabelMouseEntered(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapseIconJLabelMouseEntered
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.HAND_CURSOR);
    }//GEN-LAST:event_collapseIconJLabelMouseEntered

    private void collapseIconJLabelMouseExited(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapseIconJLabelMouseExited
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_collapseIconJLabelMouseExited

    private void collapseIconJLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapseIconJLabelMousePressed
        expandIconMousePressed(e);
    }//GEN-LAST:event_collapseIconJLabelMousePressed

    /**
     * Collapse the panel.
     * 
     * @param animate
     *            Whether or not to animate.
     */
    private void doCollapse(final boolean animate) {
        doCollapse(animate, collapsedJPanel, expandedJPanel);
    }

    /**
     * Expand the panel.
     * 
     * @param animate
     *            Whether or not to animate.
     */
    private void doExpand(final boolean animate) {
        doExpand(animate, collapsedJPanel, expandedJPanel);
    }

    private void expandedJPanelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandedJPanelMousePressed
        jPanelMousePressed((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_expandedJPanelMousePressed

    private void expandedJPanelMouseReleased(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandedJPanelMouseReleased
        jPanelMouseReleased((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_expandedJPanelMouseReleased

    private void expandedMovieJLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandedMovieJLabelMousePressed
        movieJLabelMousePressed(e);
    }//GEN-LAST:event_expandedMovieJLabelMousePressed

    private void expandIconJLabelMouseEntered(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandIconJLabelMouseEntered
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.HAND_CURSOR);
    }//GEN-LAST:event_expandIconJLabelMouseEntered

    private void expandIconJLabelMouseExited(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandIconJLabelMouseExited
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_expandIconJLabelMouseExited

    private void expandIconJLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandIconJLabelMousePressed
        expandIconMousePressed(e);
    }//GEN-LAST:event_expandIconJLabelMousePressed

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        final javax.swing.JLabel collapsedFillerJLabel = new javax.swing.JLabel();
        final javax.swing.JPanel fixedSizeJPanel = new javax.swing.JPanel();
        final javax.swing.JLabel expandedFillerJLabel = new javax.swing.JLabel();
        final javax.swing.JPanel expandedContentJPanel = new javax.swing.JPanel();
        final javax.swing.JLabel fillerJLabel = new javax.swing.JLabel();
        fillerJPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        collapsedJPanel.setLayout(new java.awt.GridBagLayout());

        collapsedJPanel.setOpaque(false);
        collapsedJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                collapsedJPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                collapsedJPanelMouseReleased(evt);
            }
        });

        expandIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconExpand.png")));
        expandIconJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                expandIconJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                expandIconJLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                expandIconJLabelMousePressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        collapsedJPanel.add(expandIconJLabel, gridBagConstraints);

        collapsedIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconHelpTopic.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 5);
        collapsedJPanel.add(collapsedIconJLabel, gridBagConstraints);

        collapsedTextJLabel.setFont(Fonts.DialogFont);
        collapsedTextJLabel.setText("!Help Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 4, 0);
        collapsedJPanel.add(collapsedTextJLabel, gridBagConstraints);

        collapsedMovieJLabel.setFont(Fonts.DialogFont);
        collapsedMovieJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("HelpTabPanel.Movie"));
        collapsedMovieJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                collapsedMovieJLabelMousePressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 4, 0);
        collapsedJPanel.add(collapsedMovieJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        collapsedJPanel.add(collapsedFillerJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(collapsedJPanel, gridBagConstraints);

        expandedJPanel.setOpaque(false);
        expandedJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                expandedJPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                expandedJPanelMouseReleased(evt);
            }
        });

        fixedSizeJPanel.setLayout(new java.awt.GridBagLayout());

        fixedSizeJPanel.setOpaque(false);
        collapseIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconCollapse.png")));
        collapseIconJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                collapseIconJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                collapseIconJLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                collapseIconJLabelMousePressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        fixedSizeJPanel.add(collapseIconJLabel, gridBagConstraints);

        expandedIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconHelpTopic.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 5);
        fixedSizeJPanel.add(expandedIconJLabel, gridBagConstraints);

        expandedTextJLabel.setFont(Fonts.DialogFont);
        expandedTextJLabel.setText("!Help Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 4, 0);
        fixedSizeJPanel.add(expandedTextJLabel, gridBagConstraints);

        expandedMovieJLabel.setFont(Fonts.DialogFont);
        expandedMovieJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("HelpTabPanel.Movie"));
        expandedMovieJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                expandedMovieJLabelMousePressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 4, 0);
        fixedSizeJPanel.add(expandedMovieJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        fixedSizeJPanel.add(expandedFillerJLabel, gridBagConstraints);

        expandedContentJPanel.setLayout(new java.awt.GridBagLayout());

        expandedContentJPanel.setOpaque(false);
        helpContentJTextArea.setEditable(false);
        helpContentJTextArea.setFont(Fonts.DialogTextEntryFont);
        helpContentJTextArea.setLineWrap(true);
        helpContentJTextArea.setWrapStyleWord(true);
        helpContentJTextArea.setFocusable(false);
        helpContentJTextArea.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 49, 0, 0);
        expandedContentJPanel.add(helpContentJTextArea, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        expandedContentJPanel.add(fillerJLabel, gridBagConstraints);

        firstJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("HelpTabPanel.firstJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        expandedContentJPanel.add(firstJLabel, gridBagConstraints);

        previousJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("HelpTabPanel.previousJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        expandedContentJPanel.add(previousJLabel, gridBagConstraints);

        countJLabel.setFont(Fonts.DialogFont);
        countJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("HelpTabPanel.countJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        expandedContentJPanel.add(countJLabel, gridBagConstraints);

        nextJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("HelpTabPanel.nextJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        expandedContentJPanel.add(nextJLabel, gridBagConstraints);

        lastJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("HelpTabPanel.lastJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        expandedContentJPanel.add(lastJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        fixedSizeJPanel.add(expandedContentJPanel, gridBagConstraints);

        org.jdesktop.layout.GroupLayout expandedJPanelLayout = new org.jdesktop.layout.GroupLayout(expandedJPanel);
        expandedJPanel.setLayout(expandedJPanelLayout);
        expandedJPanelLayout.setHorizontalGroup(
            expandedJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(fixedSizeJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
            .add(fillerJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
        );
        expandedJPanelLayout.setVerticalGroup(
            expandedJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(expandedJPanelLayout.createSequentialGroup()
                .add(fixedSizeJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 164, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(fillerJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(expandedJPanel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Handle the mouse pressed event for any one of the jpanels in this tab.
     * Depending on whether or not the event is a popup trigger the popup
     * appropriate to the state of the panel will be displayed.
     * 
     * @param jPanel
     *            A <code>JPanel</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jPanelMousePressed(final javax.swing.JPanel jPanel,
            final java.awt.event.MouseEvent e) {
        if (e.getClickCount() == 1 && e.isPopupTrigger()) {
            selectPanel();
            popupDelegate.initialize(jPanel, e.getX(), e.getY());
            popupDelegate.showForHelpTopic(helpTopic);
        } else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
            selectPanel();
        } else if ((e.getClickCount() % 2) == 0 && e.getButton() == MouseEvent.BUTTON1) {
            tabDelegate.toggleExpansion(this);
        }
    }

    /**
     * Handle the mouse released event for any one of the jpanels in this tab.
     * Depending on whether or not the event is a popup trigger the popup
     * appropriate to the state of the panel will be displayed.
     * 
     * @param jPanel
     *            A <code>JPanel</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jPanelMouseReleased(final javax.swing.JPanel jPanel,
            final java.awt.event.MouseEvent e) {
        if (e.getClickCount() == 1 && e.isPopupTrigger()) {
            selectPanel();
            popupDelegate.initialize(jPanel, e.getX(), e.getY());
            popupDelegate.showForHelpTopic(helpTopic);
        }
    }

    /**
     * Handle the mouse pressed event for the movie link.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void movieJLabelMousePressed(final java.awt.event.MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            actionDelegate.invokeForHelpTopic(getHelpTopic());
        }
    }

    /**
     * Reload a display label.
     * 
     * @param jLabel
     *            A swing <code>JLabel</code>.
     * @param value
     *            The label value.
     */
    private void reload(final javax.swing.JLabel jLabel,
            final String value) {
        jLabel.setText(null == value ? Separator.Space.toString() : value);
    }

    /**
     * Select this panel.
     */
    private void selectPanel() {
        tabDelegate.selectPanel(this);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel collapseIconJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel collapsedIconJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel collapsedJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel collapsedMovieJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JLabel collapsedTextJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel countJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel expandIconJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel expandedIconJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel expandedJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel expandedMovieJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JLabel expandedTextJLabel = new javax.swing.JLabel();
    private javax.swing.JPanel fillerJPanel;
    private final javax.swing.JLabel firstJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JTextArea helpContentJTextArea = new javax.swing.JTextArea();
    private final javax.swing.JLabel lastJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JLabel nextJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JLabel previousJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    // End of variables declaration//GEN-END:variables
}
