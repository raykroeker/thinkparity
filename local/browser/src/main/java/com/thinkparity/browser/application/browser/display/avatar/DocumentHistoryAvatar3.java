/*
 * DocumentHistoryAvatar3.java
 *
 * Created on March 16, 2006, 9:29 AM
 */

package com.thinkparity.browser.application.browser.display.avatar;

import com.thinkparity.browser.application.browser.component.ListFactory;
import com.thinkparity.browser.application.browser.component.ScrollPaneFactory;
import com.thinkparity.browser.application.browser.display.avatar.history.CellRenderer2;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.model.parity.model.document.history.HistoryItem;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author  raymond
 */
public class DocumentHistoryAvatar3 extends Avatar {

    /**
     * The history list's model.
     *
     */
    private DefaultListModel historyModel;
    
    
    /** Creates new form DocumentHistoryAvatar3 */
    public DocumentHistoryAvatar3() {
	super("DocumentHistory"); 
	initComponents();
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
     * Set the avatar's input.
     *
     *
     * @param input
     *            The avatar input.
     */
    public void setInput(final Object input) {
	Assert.assertNotNull("", input);
	Assert.assertOfType("", Long.class, input);
	this.input = input;
	reload();
    }
    
    /**
     * Reload the avatar. This event is called when either the content provider
     * or the input has changed; or as a manual reload of the avatar.
     */
    public void reload() { reloadHistory(); }
    
    /**
     * Obtain the avatar's state information.
     *
     *
     * @return The avatar's state information.
     */
    public State getState() { return null; }
    
    /**
     * Obtain the avatar id.
     *
     *
     * @return The avatar id.
     */
    public AvatarId getId() { return AvatarId.DOCUMENT_HISTORY3; }

    private void reloadHistory() {
	historyModel.clear();
	if(null != input) {
	    loadHistory(historyModel, getHistory((Long) input));
	}
    }

    private HistoryItem[] getHistory(final Long documentId) {
	return (HistoryItem[]) ((CompositeFlatSingleContentProvider) contentProvider).getElements(0, documentId);
    }

    private void loadHistory(final DefaultListModel listModel, final HistoryItem[] history) {
	for(final HistoryItem hi : history) {
	    logger.debug("Adding history item...");
	    listModel.addElement(hi);
	}
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JList historyList;
        javax.swing.JScrollPane historyListScrollPane;

        historyListScrollPane = ScrollPaneFactory.create();
        historyList = ListFactory.create();

        historyListScrollPane.setViewportView(historyList);
        historyListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        historyList.setBackground(new java.awt.Color(106, 114, 131));
        historyModel = new DefaultListModel();
        historyList.setCellRenderer(new CellRenderer2());
        // HEIGHT History List Cell
        historyList.setFixedCellHeight(42);
        historyList.setLayoutOrientation(JList.VERTICAL);
        historyList.setModel(historyModel);
        historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyListScrollPane.setViewportView(historyList);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, historyListScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(historyListScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
