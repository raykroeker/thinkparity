/*
 * DocumentHistoryAvatar3.java
 *
 * Created on March 16, 2006, 9:29 AM
 */

package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import com.thinkparity.browser.application.browser.component.ListFactory;
import com.thinkparity.browser.application.browser.component.ScrollPaneFactory;
import com.thinkparity.browser.application.browser.display.avatar.history.ActiveCellRenderer;
import com.thinkparity.browser.application.browser.display.avatar.history.ClosedCellRenderer;
import com.thinkparity.browser.application.browser.display.avatar.history.HistoryItemCellRenderer;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.model.util.ArtifactUtil;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.document.history.HistoryItem;

/**
 * A 3rd alternative to the document history avatar.
 *
 * @author  raymond
 */
public class DocumentHistoryAvatar3 extends Avatar {

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

	static {
		CR_ACTIVE = new ActiveCellRenderer();
		CR_CLOSED = new ClosedCellRenderer();
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
    
    /** Creates new form DocumentHistoryAvatar3 */
	public DocumentHistoryAvatar3() {
		super("DocumentHistory", Color.WHITE);
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
    		}
    		else { historyList.setCellRenderer(CR_ACTIVE); }
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JScrollPane historyListScrollPane;

        historyListScrollPane = ScrollPaneFactory.create();
        historyList = ListFactory.create();

        setLayout(new java.awt.GridBagLayout());

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
        historyListScrollPane.setViewportView(historyList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        add(historyListScrollPane, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void loadHistory(final DefaultListModel listModel,
			final HistoryItem[] history) {
		for (final HistoryItem hi : history) {
			listModel.addElement(hi);
		}
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
