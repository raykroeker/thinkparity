/*
 * Created On: December 9, 2006, 8:14 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact;

import java.awt.Dimension;
import java.awt.Graphics;
import java.text.MessageFormat;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JList;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.EastCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.EastCellRenderer;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.EmptyCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.WestCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.WestCellRenderer;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * <b>Title:</b>thinkParity Contact Tab Panel<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContactTabPanel extends DefaultTabPanel {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel additionalTextJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel collapsedJPanel = new javax.swing.JPanel();
    private final javax.swing.JList eastJList = new javax.swing.JList();
    private final javax.swing.JPanel eastJPanel = new javax.swing.JPanel();
    private final javax.swing.JPanel expandedJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel iconJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel textJLabel = new javax.swing.JLabel();
    private final javax.swing.JList westJList = new javax.swing.JList();
    private final javax.swing.JPanel westJPanel = new javax.swing.JPanel();
    // End of variables declaration//GEN-END:variables

    /** A contact tab <code>ActionDelegate</code>. */
    private ActionDelegate actionDelegate;

    /** The <code>Contact</code> panel data. */
    private Contact contact;
    
    /** The east <code>DefaultListModel</code>. */
    private final DefaultListModel eastListModel;

    /** The panel's expanded state. */
    private boolean expanded;

    /** The <code>IncomingInvitation</code>. */
    private IncomingInvitation incoming;

    /** The invitation invited by <code>User</code>. */
    private User invitedBy;

    /** The panel localization. */
    private final MainCellL18n localization;

    /** The <code>OutgoingInvitation</code>. */
    private OutgoingInvitation outgoing;

    /** A contact tab <code>PopupDelegate</code>. */
    private PopupDelegate popupDelegate;

    /** The west <code>DefaultListModel</code>. */
    private final DefaultListModel westListModel;

    /**
     * Create ContactTabPanel.
     *
     */
    public ContactTabPanel(final BrowserSession session) {
        super(session);
        this.eastListModel = new DefaultListModel();
        this.expanded = Boolean.FALSE;
        this.localization = new MainCellL18n("ContactTabPanel");
        this.westListModel = new DefaultListModel();
        initComponents();
    }

    /**
     * Collapse the panel.
     *
     */
    public void collapse() {
        doCollapse(true);
    }

    /**
     * Expand the panel.
     *
     */
    public void expand() {
        doExpand(true);
    }

    /**
     * Obtain the contact panel data.
     * 
     * @return A <code>Contact</code>.
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Obtain the incoming invitation.
     * 
     * @return An <code>IncomingInvitation</code>.
     */
    public IncomingInvitation getIncoming() {
        return incoming;
    }

    /**
     * Obtain the outgoing invitation.
     * 
     * @return An <code>OutgoingInvitation</code>.
     */
    public OutgoingInvitation getOutgoing() {
        return outgoing;
    }

    /**
     * Determine if the contact is set.
     * 
     * @return True if the contact is set.
     */
    public Boolean isSetContact() {
        return null != contact;
    }

    /**
     * Determine if the incoming invitation is set.
     * 
     * @return True if the invitation is set.
     */
    public Boolean isSetIncoming() {
        return null != incoming;
    }

    /**
     * Determine if the outgoing invitation is set.
     * 
     * @return True if the invitation is set.
     */
    public Boolean isSetOutgoing() {
        return null != outgoing;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#getPopupDelegate()
     *
     */
    public PopupDelegate getPopupDelegate() {
        return popupDelegate;
    }

    /**
     * Determine if the panel is expanded.
     * 
     * @return True if the panel is expanded.
     */
    public Boolean isExpanded() {
        return Boolean.valueOf(expanded);
    }
    
    /**
     * Set actionDelegate.
     *
     * @param actionDelegate
     *      A ContainerTabActionDelegate.
     */
    public void setActionDelegate(final ActionDelegate actionDelegate) {
        this.actionDelegate = actionDelegate;
    }

    /**
     * Makes the panel expanded or not.
     * 
     * @param expanded
     *            Whether to expand the panel.
     */
    public void setExpanded(final Boolean expanded) {
        if (expanded.booleanValue())
            doExpand(false);
        else
            doCollapse(false);
    }

    /**
     * Set the panel data.
     * 
     * @param contact
     *            A <code>Contact</code>.
     */
    public void setPanelData(final Contact contact) {
        this.contact = contact;
        westListModel.addElement(new ContactCell());
        iconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.USER));
        textJLabel.setText(contact.getName());
        restoreSelection("eastJList", eastListModel, eastJList);
        restoreSelection("westJList", westListModel, westJList);
    }

    /**
     * Set the panel data.
     * 
     * @param incomingInvitation
     *            An <code>IncomingInvitation</code>.
     * @param invitedBy
     *            The invited by <code>User</code>.
     */
    public void setPanelData(final IncomingInvitation incoming,
            final User invitedBy) {
        this.incoming = incoming;
        this.invitedBy = invitedBy;
        westListModel.addElement(new IncomingCell());
        iconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.USER_NOT_RECEIVED));
        textJLabel.setText(invitedBy.getName());
        restoreSelection("eastJList", eastListModel, eastJList);
        restoreSelection("westJList", westListModel, westJList);
    }


    /**
     * Set the panel data.
     * 
     * @param contact
     *            A <code>Contact</code>.
     */
    public void setPanelData(final OutgoingInvitation outgoing) {
        this.outgoing = outgoing;
        westListModel.addElement(new OutgoingCell());
        iconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.USER_NOT_RECEIVED));
        textJLabel.setText(outgoing.getEmail().toString());
        restoreSelection("eastJList", eastListModel, eastJList);
        restoreSelection("westJList", westListModel, westJList);
    }

    /**
     * Set popupDelegate.
     *
     * @param popupDelegate
     *      A ContainerTabPopupDelegate.
     */
    public void setPopupDelegate(final PopupDelegate popupDelegate) {
        this.popupDelegate = popupDelegate;
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     * 
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (expanded) {
            renderer.paintExpandedBackground(g, this);
            if (!westJList.isSelectionEmpty()) {
                final int selectionIndex = westJList.getSelectedIndex();
                renderer.paintExpandedBackgroundWest(g, westJPanel.getWidth(), getHeight(), selectionIndex, this);
                renderer.paintExpandedBackgroundCenter(g, westJPanel.getWidth(), getHeight(), selectionIndex, this);
                renderer.paintExpandedBackgroundEast(g, westJPanel.getWidth(), getHeight(), selectionIndex, this);
            }
        } else {
            renderer.paintBackground(g, getWidth(), getHeight());
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#repaintLists()
     *
     */
    @Override
    protected void repaintLists() {
        eastJList.repaint();
        westJList.repaint();
    }

    private void collapsedJPanelMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedJPanelMousePressed
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) {
            tabDelegate.toggleExpansion(this);
            popupDelegate.initialize(expandedJPanel, e.getX(), e.getY());
            if (isSetContact().booleanValue())
                popupDelegate.showForContact(contact);
            if (isSetIncoming().booleanValue())
                popupDelegate.showForInvitation(incoming);
            if (isSetOutgoing().booleanValue())
                popupDelegate.showForInvitation(outgoing);
        }
    }//GEN-LAST:event_collapsedJPanelMousePressed

    private void collapsedJPanelMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedJPanelMouseReleased
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) {
            tabDelegate.toggleExpansion(this);
            popupDelegate.initialize(expandedJPanel, e.getX(), e.getY());
            if (isSetContact().booleanValue())
                popupDelegate.showForContact(contact);
            if (isSetIncoming().booleanValue())
                popupDelegate.showForInvitation(incoming);
            if (isSetOutgoing().booleanValue())
                popupDelegate.showForInvitation(outgoing);
        } else {
            tabDelegate.toggleExpansion(this);
        }
    }//GEN-LAST:event_collapsedJPanelMouseReleased

    /**
     * Collapse the panel.
     * 
     * @param animate
     *            Whether or not to animate.
     */
    private void doCollapse(final boolean animate) {
        this.expanded = false;
        remove(expandedJPanel);
        add(collapsedJPanel, constraints.clone());

        if (animate) {
            animator.collapse(20, 25);
        } else {
            final Dimension preferredSize = expandedJPanel.getPreferredSize();
            preferredSize.height = 25;
            expandedJPanel.setPreferredSize(preferredSize);
        }

        revalidate();
        reload();
        repaint();
    }

    /**
     * Expand the panel.
     * 
     * @param animate
     *            Whether or not to animate.
     */
    private void doExpand(final boolean animate) {
        this.expanded = true;
        remove(collapsedJPanel);
        add(expandedJPanel, constraints.clone());

        if (animate) {
            animator.expand(20, 165);
        } else {
            final Dimension preferredSize = expandedJPanel.getPreferredSize();
            preferredSize.height = 165;
            expandedJPanel.setPreferredSize(preferredSize);
        }

        revalidate();
        reload();
        repaint();
    }
    
    private void eastJListFocusGained(java.awt.event.FocusEvent e) {//GEN-FIRST:event_eastJListFocusGained
        jListFocusGained((JList) e.getSource(), e);
    }//GEN-LAST:event_eastJListFocusGained

    private void eastJListFocusLost(java.awt.event.FocusEvent e) {//GEN-FIRST:event_eastJListFocusLost
        jListFocusLost((JList) e.getSource(), e);
    }//GEN-LAST:event_eastJListFocusLost

    private void eastJListMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_eastJListMouseClicked
    }//GEN-LAST:event_eastJListMouseClicked

    private void eastJListMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_eastJListMousePressed
        jListMousePressed((JList) e.getSource(), e);
    }//GEN-LAST:event_eastJListMousePressed

    private void eastJListMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_eastJListMouseReleased
        jListMouseReleased((JList) e.getSource(), e);
    }//GEN-LAST:event_eastJListMouseReleased

    private void eastJListValueChanged(javax.swing.event.ListSelectionEvent e) {//GEN-FIRST:event_eastJListValueChanged
        if (e.getValueIsAdjusting() || ((javax.swing.JList) e.getSource()).isSelectionEmpty()) {
            repaint();
            return;
        }
        saveSelection("eastJList", (javax.swing.JList) e.getSource());
    }//GEN-LAST:event_eastJListValueChanged

    private void expansionJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_expansionJLabelMouseClicked
        tabDelegate.toggleExpansion(this);
    }//GEN-LAST:event_expansionJLabelMouseClicked

    private void expansionJLabelMouseEntered(java.awt.event.MouseEvent e) {//GEN-FIRST:event_expansionJLabelMouseEntered
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.HAND_CURSOR);
    }//GEN-LAST:event_expansionJLabelMouseEntered

    private void expansionJLabelMouseExited(java.awt.event.MouseEvent e) {//GEN-FIRST:event_expansionJLabelMouseExited
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_expansionJLabelMouseExited

    private void iconJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_iconJLabelMouseClicked
        actionDelegate.invokeForContact(contact);
    }//GEN-LAST:event_iconJLabelMouseClicked

    private void iconJLabelMouseEntered(java.awt.event.MouseEvent e) {//GEN-FIRST:event_iconJLabelMouseEntered
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.HAND_CURSOR);
    }//GEN-LAST:event_iconJLabelMouseEntered

    private void iconJLabelMouseExited(java.awt.event.MouseEvent e) {//GEN-FIRST:event_iconJLabelMouseExited
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_iconJLabelMouseExited

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        final javax.swing.JLabel expansionJLabel = new javax.swing.JLabel();
        final javax.swing.JScrollPane eastJScrollPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.GridBagLayout());

        setBorder(BORDER);
        collapsedJPanel.setLayout(new java.awt.GridBagLayout());

        collapsedJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                collapsedJPanelMousePressed(e);
            }
            public void mouseReleased(java.awt.event.MouseEvent e) {
                collapsedJPanelMouseReleased(e);
            }
        });

        iconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconContainer.png")));
        iconJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                iconJLabelMouseClicked(e);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                iconJLabelMouseEntered(e);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                iconJLabelMouseExited(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 32, 0, 4);
        collapsedJPanel.add(iconJLabel, gridBagConstraints);

        textJLabel.setText("!Contact Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 4, 0);
        collapsedJPanel.add(textJLabel, gridBagConstraints);

        additionalTextJLabel.setForeground(Colors.Browser.List.INNER_LIST_SELECTION_BORDER);
        additionalTextJLabel.setText("!Contact Additional Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 4, 0);
        collapsedJPanel.add(additionalTextJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(collapsedJPanel, gridBagConstraints);

        expandedJPanel.setLayout(new java.awt.GridBagLayout());

        expandedJPanel.setOpaque(false);
        westJPanel.setLayout(new java.awt.GridBagLayout());

        westJPanel.setOpaque(false);
        expansionJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconCollapse.png")));
        expansionJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                expansionJLabelMouseClicked(e);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                expansionJLabelMouseEntered(e);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                expansionJLabelMouseExited(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 8);
        westJPanel.add(expansionJLabel, gridBagConstraints);

        westJList.setModel(westListModel);
        westJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        westJList.setCellRenderer(new WestCellRenderer());
        westJList.setOpaque(false);
        westJList.setVisibleRowCount(NUMBER_VISIBLE_ROWS);
        westJList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent e) {
                westJListMouseMoved(e);
            }
        });
        westJList.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                westJListFocusGained(e);
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                westJListFocusLost(e);
            }
        });
        westJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                westJListValueChanged(e);
            }
        });
        westJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                westJListMouseClicked(e);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                westJListMouseEntered(e);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                westJListMouseExited(e);
            }
            public void mousePressed(java.awt.event.MouseEvent e) {
                westJListMousePressed(e);
            }
            public void mouseReleased(java.awt.event.MouseEvent e) {
                westJListMouseReleased(e);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        westJPanel.add(westJList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        expandedJPanel.add(westJPanel, gridBagConstraints);

        eastJPanel.setLayout(new java.awt.GridBagLayout());

        eastJPanel.setOpaque(false);
        eastJScrollPane.setBorder(null);
        eastJScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        eastJScrollPane.setOpaque(false);
        eastJScrollPane.getViewport().setOpaque(false);
        eastJList.setModel(eastListModel);
        eastJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        eastJList.setCellRenderer(new EastCellRenderer());
        eastJList.setOpaque(false);
        eastJList.setVisibleRowCount(NUMBER_VISIBLE_ROWS);
        eastJList.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                eastJListFocusGained(e);
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                eastJListFocusLost(e);
            }
        });
        eastJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                eastJListValueChanged(e);
            }
        });
        eastJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                eastJListMouseClicked(e);
            }
            public void mousePressed(java.awt.event.MouseEvent e) {
                eastJListMousePressed(e);
            }
            public void mouseReleased(java.awt.event.MouseEvent e) {
                eastJListMouseReleased(e);
            }
        });

        eastJScrollPane.setViewportView(eastJList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        eastJPanel.add(eastJScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        expandedJPanel.add(eastJPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(expandedJPanel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Reload the panel data based upon internal criteria.
     *
     */
    private void reload() {
        reloadText();
    }

    /**
     * Reload the text on the panel.
     *
     */
    private void reloadText() {
        if (!expanded) {
            if (isSetContact()) {
                final String pattern;
                final Object[] values;
                if (contact.isSetTitle()) {
                    if (contact.isSetOrganization()) {
                        pattern = "({0}, {1})";
                        values = new Object[] { contact.getTitle(),
                                contact.getOrganization() };
                    } else {
                        pattern = "({0})";
                        values = new Object[] { contact.getTitle() };
                    }
                } else {
                    if (contact.isSetOrganization()) {
                        pattern = "({0})";
                        values = new Object[] { contact.getOrganization() };
                    } else {
                        pattern = "";
                        values = new Object[] {};
                    }
                }
                additionalTextJLabel.setText(MessageFormat.format(pattern, values));
            }
        }
    }

    private void westJListMouseMoved(java.awt.event.MouseEvent e) {//GEN-FIRST:event_westJListMouseMoved
        westJListSetCursor((javax.swing.JList) e.getSource(), e);
    }//GEN-LAST:event_westJListMouseMoved

    private void westJListMouseExited(java.awt.event.MouseEvent e) {//GEN-FIRST:event_westJListMouseExited
        westJListSetCursor((javax.swing.JList) e.getSource(), e);
    }//GEN-LAST:event_westJListMouseExited

    private void westJListMouseEntered(java.awt.event.MouseEvent e) {//GEN-FIRST:event_westJListMouseEntered
        westJListSetCursor((javax.swing.JList) e.getSource(), e);
    }//GEN-LAST:event_westJListMouseEntered
    
    private void westJListFocusGained(java.awt.event.FocusEvent e) {//GEN-FIRST:event_westJListFocusGained
        jListFocusGained((JList) e.getSource(), e);
    }//GEN-LAST:event_westJListFocusGained

    private void westJListFocusLost(java.awt.event.FocusEvent e) {//GEN-FIRST:event_westJListFocusLost
        jListFocusLost((JList) e.getSource(), e);
    }//GEN-LAST:event_westJListFocusLost

    private void westJListMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_westJListMouseClicked
        westJListMouseClicked("westJList", (javax.swing.JList) e.getSource(), e);
    }//GEN-LAST:event_westJListMouseClicked

    private void westJListMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_westJListMousePressed
        jListMousePressed((JList) e.getSource(), e);
    }//GEN-LAST:event_westJListMousePressed

    private void westJListMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_westJListMouseReleased
        jListMouseReleased((JList) e.getSource(), e);
    }//GEN-LAST:event_westJListMouseReleased

    private void westJListValueChanged(javax.swing.event.ListSelectionEvent e) {//GEN-FIRST:event_westJListValueChanged
        if (e.getValueIsAdjusting() || ((javax.swing.JList) e.getSource()).isSelectionEmpty()) {
            repaint();
            return;
        }
        saveSelection("westJList", (javax.swing.JList) e.getSource());
        eastListModel.clear();
        eastListModel.addElement(EmptyCell.getEmptyCell());
        for (final Object selectedValue : westJList.getSelectedValues()) {
            for (final Cell cell : ((WestCell) selectedValue).getEastCells()) {
                eastListModel.addElement(cell);
            }
        }
    }//GEN-LAST:event_westJListValueChanged

    /** A contact cell. */
    private final class ContactCell extends WestCell {
        /**
         * Create ContactCell.
         *
         */
        private ContactCell() {
            super();
            add(new ContactFieldCell(this, localization.getString("Title"), contact.getTitle()));
            add(new ContactFieldCell(this, localization.getString("Organization"), contact.getOrganization()));
            add(new ContactFieldCell(this, localization.getString("OrganizationAddress"), contact.getOrganizationAddress()));
            add(new ContactFieldCell(this, localization.getString("Phone"), contact.getPhone()));
            add(new ContactFieldCell(this, localization.getString("MobilePhone"), contact.getMobilePhone()));
            if (0 < contact.getEmailsSize())
                add(new ContactTextCell(this, contact.getEmails().get(0).toString()));
        }
        @Override
        public Icon getIcon() {
            return IMAGE_CACHE.read(TabPanelIcon.USER);
        }
        @Override
        public String getText() {
            return contact.getName();
        }
        @Override
        public void showPopup() {
            popupDelegate.showForContact(contact);
        }
        @Override
        public Boolean isActionAvailable() {
            return Boolean.FALSE;
        }
    }

    /** A contact value cell. */
    private final class ContactFieldCell extends DefaultCell implements EastCell {
        /** The field name <code>String</code>. */
        private final String name;
        /** The parent <code>WestCell</code>. */
        private final WestCell parent;
        /** The field value <code>String</code>. */
        private final String value;
        /**
         * Create ContactFieldCell.
         * 
         * @param name
         *            The field name <code>String</code>.
         * @param value
         *            The field value <code>String</code>.
         */
        private ContactFieldCell(final WestCell parent, final String name,
                final String value) {
            super();
            this.name = name;
            this.parent = parent;
            this.value = value;
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell#getText()
         *
         */
        public String getText() {
            if (null == value) {
                return MessageFormat.format("{0}:  {1}", name, localization.getString("NoValue"));
            } else {
                return MessageFormat.format("{0}:  {1}", name, value);
            }
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell#invokeAction()
         *
         */
        public void invokeAction() {
            parent.invokeAction();
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell#showPopup()
         *
         */
        public void showPopup() {
            parent.showPopup();
        }
    }

    /** A contact text cell. */
    private final class ContactTextCell extends DefaultCell implements EastCell {
        /** The parent <code>WestCell</code>. */
        private final WestCell parent;
        /**
         * Create ContactFieldCell.
         * 
         * @param text
         *            The field text <code>String</code>.
         */
        private ContactTextCell(final WestCell parent, final String text) {
            super();
            setText(text);
            this.parent = parent;
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell#invokeAction()
         *
         */
        public void invokeAction() {
            parent.invokeAction();
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell#showPopup()
         *
         */
        public void showPopup() {
            parent.showPopup();
        }
    }

    /** An incoming invitation cell. */
    private final class IncomingCell extends WestCell {
        /**
         * Create IncomingCell.
         *
         */
        private IncomingCell() {
            super();
            add(new ContactFieldCell(this, localization.getString("CreatedOn"),
                    formatFuzzy(incoming.getCreatedOn())));
            add(new ContactFieldCell(this, localization.getString("InvitedAs"),
                    incoming.getInvitedAs().toString()));
        }
        @Override
        public Icon getIcon() {
            return IMAGE_CACHE.read(TabPanelIcon.USER);
        }
        @Override
        public String getText() {
            return invitedBy.getName();
        }
        @Override
        public void invokeAction() {
            actionDelegate.invokeForInvitation(incoming);
        }
        @Override
        public void showPopup() {
            popupDelegate.showForInvitation(incoming);
        }
    }

    /** An outgoing invitation cell. */
    private final class OutgoingCell extends WestCell {
        /**
         * Create OutgoingCell.
         *
         */
        private OutgoingCell() {
            super();
            setText(outgoing.getEmail().toString());
            add(new ContactFieldCell(this, localization.getString("CreatedOn"),
                    formatFuzzy(outgoing.getCreatedOn())));
        }
        @Override
        public Icon getIcon() {
            return IMAGE_CACHE.read(TabPanelIcon.USER);
        }
        @Override
        public void invokeAction() {
            actionDelegate.invokeForInvitation(outgoing);
        }
        @Override
        public void showPopup() {
            popupDelegate.showForInvitation(outgoing);
        }
    }
}
