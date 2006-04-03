/*
 * HistoryItems.java
 *
 * Created on March 16, 2006, 9:29 AM
 */

package com.thinkparity.browser.application.browser.display.avatar.history;

import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import com.thinkparity.browser.application.browser.component.ListFactory;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.ScrollPaneFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.javax.swing.border.MultiLineBorder;
import com.thinkparity.browser.model.util.ArtifactUtil;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.document.history.HistoryItem;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * A 3rd alternative to the document history avatar.
 * 
 * @author raykroeker@gmail.com
 * @version 
 */
public class HistoryItems extends Avatar {
    
    /**
     * The cell renderer for active documents.
     *
     */
    private static final HistoryItemCellRenderer CR_ACTIVE;
    
    /**
     * The cell renderer for closed documents.
     *
     */
    private static final HistoryItemCellRenderer CR_CLOSED;

    private static final Color BC_1;

    private static final Color BC_2;
    
    static {
        CR_ACTIVE = new ActiveCellRenderer();
        CR_CLOSED = new ClosedCellRenderer();

        BC_1 = new Color(137, 139, 142, 255);
        BC_2 = new Color(238, 238, 238, 255);
    }
    
    /**
     * @see java.io.Serializable
     *
     */
    private static final long serialVersionUID = 1;
    
    /**
     * The history list's model.
     *
     */
    private DefaultListModel historyModel;
    
    /**
     * Creates new form HistoryItems
     */
    public HistoryItems() {
        super("DocumentHistory", Color.WHITE);
        setBorder(new MultiLineBorder(new Color[] {BC_1, BC_2}));
        initComponents();
    }
    
    /**
     * Obtain the avatar id.
     *
     *
     * @return The avatar id.
     */
    public AvatarId getId() { return AvatarId.DOCUMENT_HISTORY3; }
    
    /**
     * Obtain the avatar's state information.
     *
     *
     * @return The avatar's state information.
     */
    public State getState() { return null; }
    
    /**
     * Reload the avatar. This event is called when either the content provider
     * or the input has changed; or as a manual reload of the avatar.
     *
     */
    public void reload() {
        reloadHistory();
        if(null != input) {
            if(ArtifactUtil.isClosed((Long) input, ArtifactType.DOCUMENT)) {
                historyList.setCellRenderer(CR_CLOSED);
            } else { historyList.setCellRenderer(CR_ACTIVE); }
        }
    }
    
    /**
     * Set the avatar's input.
     *
     *
     * @param input
     *            The avatar input.
     */
    public void setInput(final Object input) {
        Assert.assertNotNull(
            "History avatar requires java.lang.Long input.", input);
        Assert.assertOfType(
            "History avatar requires java.lang.Long input.", Long.class, input);
        this.input = input;
        reload();
    }
    
    /**
     * Set the avatar state.
     *
     *
     * @param state
     *            The avatar's state information.
     */
    public void setState(final State state) {}
    
    /**
     * Obtain the history from the content provider.
     *
     * @param documentId
     *            The document id.
     * @return The document's history.
     */
    private HistoryItem[] getHistory(final Long documentId) {
        return (HistoryItem[]) ((CompositeFlatSingleContentProvider) contentProvider)
        .getElements(0, documentId);
    }

    /**
     * Obtain the document team from the content provider.
     * 
     * @param documentId
     *            The document id.
     * @return The document's team.
     */
    Contact[] getTeam(final Long documentId) {
        return (Contact[]) ((CompositeFlatSingleContentProvider) contentProvider)
        .getElements(1, documentId);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JScrollPane historyListScrollPane;
        javax.swing.JLabel infoJLabel;

        infoJLabel = new javax.swing.JLabel();
        historyListScrollPane = ScrollPaneFactory.create();
        historyList = ListFactory.create();

        setLayout(new java.awt.GridBagLayout());

        infoJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/HistoryInfoDisplay.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(infoJLabel, gridBagConstraints);

        historyListScrollPane.setBorder(null);
        historyListScrollPane.setViewportView(historyList);
        historyListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        historyModel = new DefaultListModel();
        historyList.setCellRenderer(new ActiveCellRenderer());
        // HEIGHT History List Cell 21
        historyList.setFixedCellHeight(21);
        historyList.setLayoutOrientation(JList.VERTICAL);
        historyList.setModel(historyModel);
        historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent e) {
                historyListMouseReleased(e);
            }
        });

        historyListScrollPane.setViewportView(historyList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        add(historyListScrollPane, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void historyListMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_historyListMouseReleased
        if(e.isPopupTrigger()) {
            final Integer index = historyList.locationToIndex(e.getPoint());
            historyList.setSelectedIndex(index);
            final DisplayHistoryItem displayHistoryItem =
                (DisplayHistoryItem) historyList.getSelectedValue();
            if(displayHistoryItem.isVersionAttached()) {
                final JPopupMenu jPopupMenu = MenuFactory.createPopup();
                displayHistoryItem.populatePopupMenu(e, jPopupMenu);
                jPopupMenu.show(historyList, e.getX(), e.getY());
            }
        }
    }//GEN-LAST:event_historyListMouseReleased
    
    private void loadHistory(final DefaultListModel listModel,
        final HistoryItem[] history) {
        DisplayHistoryItem display;
        for (final HistoryItem hi : history) {
            display = new DisplayHistoryItem();
            display.setAvatar(this);
            display.setHistoryItem(hi);
            listModel.addElement(display);
        }

        if(historyList.isSelectionEmpty())
            historyList.setSelectedIndex(0);
    }

    private void reloadHistory() {
        historyModel.clear();
        if(null != input) {
            loadHistory(historyModel, getHistory((Long) input));
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList historyList;
    // End of variables declaration//GEN-END:variables
    
}
