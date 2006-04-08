/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;
import java.util.TooManyListenersException;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.display.avatar.main.CellRenderer;
import com.thinkparity.browser.application.browser.display.avatar.main.DisplayDocument;
import com.thinkparity.browser.application.browser.display.avatar.main.DocumentListItem;
import com.thinkparity.browser.application.browser.display.avatar.main.ListItem;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.browser.application.browser.dnd.UpdateDocumentTxHandler;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.filter.artifact.Search;
import com.thinkparity.model.parity.model.index.IndexHit;

/**
 * The main list avatar displays a list of crucial system messages; as well as
 * the document list.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserMainAvatar extends Avatar {

	private static final String ERROR_INIT_TMLX = "[BROWSER2] [APP] [B2] [MAIN LIST] [INIT] [TOO MANY DROP TARGET LISTENERS]";

    /**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

    /**
	 * The swing list.
	 * 
	 */
	private JList jList;

    /**
     * The model for the documents in the list.
     * 
     */
    private final BrowserMainDocumentModel mainDocumentModel;

    /**
     * The search filter.
     * 
     * @see #applySearchFilter(List)
     * @see #removeSearchFilter()
     */
    private Search searchFilter;
    
    /**
	 * Create a BrowserMainAvatar.
	 * 
	 */
	BrowserMainAvatar() {
		super("BrowserMainAvatar", ScrollPolicy.NONE, Color.WHITE);
		this.mainDocumentModel = new BrowserMainDocumentModel(getController());
		setLayout(new GridBagLayout());
		initComponents();
	}

    /**
     * Apply a key holder filter to the main list.
     * 
     * @param keyHolder
     *            If true; results are filtered where the user has the key if
     *            false; results are filtered where the user does not have the
     *            key.
     * 
     * @see #removeKeyHolderFilter()
     */
    public void applyKeyHolderFilter(final Boolean keyHolder) {
        mainDocumentModel.applyKeyHolderFilter(keyHolder);
    }

    /**
     * Apply the search results to filter the main list.
     * 
     * @param searchResult
     *            The search results.
     * 
     * @see #searchFilter
     * @see #applyFilter(Filter)
     * @see #removeSearchFilter()
     */
    public void applySearchFilter(final List<IndexHit> searchResult) {
        mainDocumentModel.applySearchFilter(searchResult);
    }

    /**
     * Apply an artifact state filter.
     * 
     * @param state
     *            The artifact state to filter by.
     * 
     * @see #removeStateFilter()
     */
    public void applyStateFilter(final ArtifactState state) {
        mainDocumentModel.applyStateFilter(state);
    }

    /**
     * Remove all non-search filters from the document list.
     *
     */
    public void clearFilters() { mainDocumentModel.clearDocumentFilters(); }

    /**
     * Debug the filter applied to the main list.
     *
     */
    public void debugFilter() { mainDocumentModel.debug(); }

    /**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.BROWSER_MAIN; }

    /**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() { return null; }

	/**
     * Determine whether or not the filter is enabled.
     * 
     * @return True if it is; false otherwise.
     */
    public Boolean isFilterEnabled() {
        return mainDocumentModel.isDocumentListFiltered();
    }

    /**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
	 * 
	 */
	public void reload() {}

	/**
     * Remove the key holder filter.
     * 
     * @see #applyKeyHolderFilter(Boolean)
     */
	public void removeKeyHolderFilter() {
        mainDocumentModel.removeKeyHolderFilters();
    }

	/**
     * Remove the search filter from the list.
     *
     * @see #applySearchFilter(List)
     */
    public void removeSearchFilter() {
        mainDocumentModel.removeSearchFilter();
    }

    /**
     * Remove the state filter from the list.
     * 
     * @see #applyStateFilter(ArtifactState)
     */
    public void removeStateFilter() { mainDocumentModel.removeStateFilters(); }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setContentProvider(com.thinkparity.browser.application.browser.display.provider.ContentProvider)
     * 
     */
    public void setContentProvider(final ContentProvider contentProvider) {
        mainDocumentModel.setContentProvider((CompositeFlatSingleContentProvider) contentProvider);
        // set initial selection
        if(0 < jList.getModel().getSize()) { jList.setSelectedIndex(0); }
    }

    /**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}

    /**
     * Synchronize the document in the list.
     * 
     * @param documentId
     *            The document id.
     * @param remote
     *            Indicates whether the sync is the result of a remote event
     */
	public void syncDocument(final Long documentId, final Boolean remote) {
        final DisplayDocument selectedDocument = getSelectedDocument();
	    mainDocumentModel.syncDocument(documentId, remote);
        if(mainDocumentModel.isDocumentVisible(selectedDocument))
            selectDocument(selectedDocument);
	}

    /**
     * Synchronize the documents in the list.
     * 
     * @param documentIds
     *            The document ids.
     * @param remote
     *            Indicates whether the sync is the result of a remove event.
     */
    public void syncDocuments(final Set<Long> documentIds, final Boolean remote) {
        final DisplayDocument selectedDocument = getSelectedDocument();
        mainDocumentModel.syncDocuments(documentIds, remote);
        if(mainDocumentModel.isDocumentVisible(selectedDocument))
            selectDocument(selectedDocument);
    }

	private DisplayDocument getSelectedDocument() {
        final ListItem li = (ListItem) jList.getSelectedValue();
        if(li instanceof DocumentListItem) {
            return ((DocumentListItem) li).getDisplayDocument();
        }
        else { return null; }
    }

    /** Flag for the drop target listener to use. */
    private boolean canImportListItem;

    /**
	 * Initialize the swing components.
	 *
	 */
	private void initComponents() {
        // the list that resides on the browser's main avatar
		// 	* is a single selection list
		//	* spans the width of the entire avatar
		// 	* uses a custom cell renderer
		jList = new JList(mainDocumentModel.getListModel());
		jList.setCellRenderer(new CellRenderer(getController(), jList));
        jList.setDragEnabled(true);
		// HEIGHT MainListCell 21
		jList.setFixedCellHeight(21);
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setTransferHandler(new UpdateDocumentTxHandler(getController(), jList));
		jList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				if(2 == e.getClickCount()) {
					final Point p = e.getPoint();
					final Integer listIndex = jList.locationToIndex(p);
					jList.setSelectedIndex(listIndex);

					// TODO Fix this
					final ListItem listItem =
						(ListItem) jList.getSelectedValue();
					runOpenDocumentAction((Long) listItem.getProperty("documentId"));
				}
			}

			public void mouseReleased(final MouseEvent e) {
				if(e.isPopupTrigger()) {
					final Point p = e.getPoint();
					final Integer listIndex = jList.locationToIndex(p);
					jList.setSelectedIndex(listIndex);

					final ListItem listItem =
						(ListItem) jList.getSelectedValue();
					final JPopupMenu jPopupMenu = MenuFactory.createPopup();
					listItem.populateMenu(e, jPopupMenu);
					jPopupMenu.show(jList, e.getX(), e.getY());
				}
			}
		});
		jList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(final ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					final Integer selectedIndex = jList.getSelectedIndex();
					if(-1 != selectedIndex) {
						final ListItem item = (ListItem) jList.getSelectedValue();
						item.fireSelection();
                        canImportListItem = item.canImport();
					}
				}
			}
		});
        try {
            jList.getDropTarget().addDropTargetListener(new DropTargetAdapter() {
                public void drop(final DropTargetDropEvent dtde) {}
                public void dragOver(final DropTargetDragEvent dtde) {
                    if(!canImportListItem) { dtde.rejectDrag(); }
                }
            });
        }
        catch(final TooManyListenersException tmlx) {
            logger.error(ERROR_INIT_TMLX, tmlx);
        }

		final JScrollPane jListScrollPane = new JScrollPane(jList);
        jListScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
		c.insets.left = c.insets.right = 2;
		c.insets.top = c.insets.bottom = 1;
		c.weightx = 1;
		c.weighty = 1;
		add(jListScrollPane, c.clone());
	}

	/**
	 * Open the document.
	 *
	 */
	private void runOpenDocumentAction(final Long documentId) {
		getController().runOpenDocument(documentId);
	}

	private void selectDocument(final DisplayDocument displayDocument) {
        jList.setSelectedValue(ListItem.create(displayDocument), true);
    }
}