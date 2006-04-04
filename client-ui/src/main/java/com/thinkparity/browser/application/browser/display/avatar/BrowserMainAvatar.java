/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
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
import com.thinkparity.browser.application.browser.display.avatar.main.SystemMessageListItem;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.Pair;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.filter.FilterChain;
import com.thinkparity.model.parity.model.filter.artifact.Active;
import com.thinkparity.model.parity.model.filter.artifact.Closed;
import com.thinkparity.model.parity.model.filter.artifact.IsKeyHolder;
import com.thinkparity.model.parity.model.filter.artifact.IsNotKeyHolder;
import com.thinkparity.model.parity.model.filter.artifact.Search;
import com.thinkparity.model.parity.model.index.IndexHit;
import com.thinkparity.model.parity.model.message.system.SystemMessage;

/**
 * The main list avatar displays a list of crucial system messages; as well as
 * the document list.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserMainAvatar extends Avatar {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

    /**
     * The filter that is passed to the provider when filtering documents in
     * the list.
     * 
     * @see #getDisplayDocument(Long)
     * @see #getDisplayDocuments()
     * @see #applyFilter(Filter)
     * @see #clearFilters()
     * @see #removeFilter(Filter)
     */
	private final FilterChain<Artifact> filterChain;

	/**
	 * The list.
	 * 
	 */
	private JList jList;

	/**
	 * The list model used to populate the list.
	 * 
	 */
	private DefaultListModel jListModel;

	/**
     * The not key holder filter.
     * 
     * @see #applyKeyHolderFilter(Boolean)
     * @see #removeKeyHolderFilter()
     */
    private Filter<Artifact> keyFilterFalse;

    /**
     * The key holder filter.
     * 
     * @see #applyKeyHolderFilter(Boolean)
     * @see #removeKeyHolderFilter()
     */
    private Filter<Artifact> keyFilterTrue;

    /**
     * The search filter.
     * 
     * @see #applySearchFilter(List)
     * @see #removeSearchFilter()
     */
    private Search searchFilter;
    
    /**
     * The active state filter.
     * 
     * @see #applyStateFilter(ArtifactState)
     * @see #removeStateFilter()
     */
    private Filter<Artifact> stateFilterActive;

    /**
     * The closed state filter.
     * 
     * @see #applyStateFilter(ArtifactState)
     * @see #removeStateFilter()
     */
    private Filter<Artifact> stateFilterClosed;

    /**
	 * Create a BrowserMainAvatar.
	 * 
	 */
	BrowserMainAvatar() {
		super("BrowserMainAvatar", ScrollPolicy.NONE, Color.WHITE);
		this.filterChain = new FilterChain<Artifact>();
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
     * @see #keyFilterFalse
     * @see #keyFilterTrue
     * @see #applyFilter(Filter)
     * @see #removeKeyHolderFilter()
     */
    public void applyKeyHolderFilter(final Boolean keyHolder) {
        if(null == keyFilterTrue) { keyFilterTrue = new IsKeyHolder(); }
        if(null == keyFilterFalse) { keyFilterFalse = new IsNotKeyHolder(); }
        invokeLater(new Runnable() {
            public void run() {
                applyFilter(keyHolder ? keyFilterTrue : keyFilterFalse);
            }
        });
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
        if(null == searchFilter) { searchFilter = new Search(new LinkedList<IndexHit>()); }
        searchFilter.setResults(searchResult);

        invokeLater(new Runnable() {
            public void run() { applyFilter(searchFilter); }
        });
    }

    /**
     * Apply an artifact state filter.
     * 
     * @param state
     *            The artifact state to filter by.
     * 
     * @see #stateFilter
     * @see #applyFilter(Filter)
     * @see #removeStateFilter()
     */
    public void applyStateFilter(final ArtifactState state) {
        if(null == stateFilterActive) { stateFilterActive = new Active(); }
        if(null == stateFilterClosed) { stateFilterClosed = new Closed(); }
        invokeLater(new Runnable() {
            public void run() {
                if(ArtifactState.ACTIVE == state) {
                    applyFilter(stateFilterActive);
                }
                else if(ArtifactState.CLOSED == state) {
                    applyFilter(stateFilterClosed);
                }
                else {
                    Assert.assertUnreachable(
                            "[BROWSER2] [APP] [B2] [MAIN AVATAR] [CANNOT FILTER BY STATE " + state + "]");
                }
            }
        });
    }

    /**
     * Remove all non-search filters from the document list.
     *
     */
    public void clearFilters() {
        Filter<Artifact> f;
        for(final Iterator<Filter<Artifact>> i = filterChain.iterator(); i.hasNext();) {
            f = i.next();
            if(f != searchFilter) { i.remove(); }
        }
        jListModel.clear();
        reloadDocuments(getController().getSelectedDocumentId());
    }

    /**
     * Debug the filter applied to the main list.
     *
     */
    public void debugFilter() {
        Assert.assertTrue(
                "[BROWSER2] [APP] [B2] [MAIN AVATAR] [IS NOT DEBUG MODE]",
                isDebugMode());
        filterChain.debug(logger);
    }

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
        if(filterChain.isEmpty()) { return Boolean.FALSE; }
        else {
            if(filterChain.containsFilter(searchFilter)) {
                if(filterChain.containsFilter(stateFilterActive) ||
                        filterChain.containsFilter(stateFilterClosed) ||
                        filterChain.containsFilter(keyFilterFalse) ||
                        filterChain.containsFilter(keyFilterTrue)) {
                    return Boolean.TRUE;
                }
                else { return Boolean.FALSE; }
                
            }
            else { return Boolean.TRUE; }
        }
    }

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
	 * 
	 */
	public void reload() {
		final Long systemMessageId = getSelectedSystemMessageId();
		final Long documentId = getController().getSelectedDocumentId();

		jListModel.clear();
		reloadSystemMessages(systemMessageId);
		reloadDocuments(documentId);
	}

	/**
     * Reload the document in the list.
     * 
     * @param documentId
     *            The document id.
     * @param remoteReload
     *            Indicates wether the reload is the result of a remote event
     */
	public void reloadDocument(final Long documentId, final Boolean remoteReload) {
		final DisplayDocument displayDocument = getDisplayDocument(documentId);
		// if the display document is null; we can assume the document has been
		// deleted (it's not longer being created by the provider); so we find
		// the document and remove it
		if(null == displayDocument) {
			ListItem listItem;
			for(int i = 0; i < jListModel.size(); i++) {
				listItem = (ListItem) jListModel.get(i);
				if(listItem instanceof DocumentListItem) {
					if(((DocumentListItem) listItem).getDocumentId().equals(documentId)) {
						jListModel.remove(i);
						jList.setSelectedIndex(i);
						break;
					}
				}
			}
		}
		else {
			final ListItem listItem = DocumentListItem.create(displayDocument);
			// if the document list item is in the list; we need to remove it;
			// and re-add it.
			// 
			// if the reload is not the result of a remote event; put it back
			// where it was; otherwise move it to the top
			if(jListModel.contains(listItem)) {
				final int index = jListModel.indexOf(listItem);
				jListModel.remove(index);
				if(remoteReload) { jListModel.add(0, listItem); }
				else { jListModel.add(index, listItem); }
			}
			// if it's not in the list; just add it to the top
			else { jListModel.add(0, listItem); }

			// maintain selection
			final Integer modelIndex = jListModel.indexOf(listItem);
			jList.setSelectedIndex(modelIndex);
		}

        // if the list is empty; disable history
        if(jListModel.isEmpty()) { getController().disableHistory(); }
        else { getController().enableHistory(); }
	}

    /**
     * Reload the system message in the list.
     * 
     * @param systemMessageId
     *            The system message id.
     */
	public void reloadSystemMessage(final Long systemMessageId) {
		final SystemMessage systemMessage = getSystemMessage(systemMessageId);
		if(null == systemMessage) {
			ListItem listItem;
			for(int i = 0; i < jListModel.size(); i++) {
				listItem = (ListItem) jListModel.get(i);
				if(listItem instanceof SystemMessageListItem) {
					if(((SystemMessageListItem) listItem).getId().equals(systemMessageId)) {
						jListModel.remove(i);
						break;
					}
				}
			}
		}
	}

    /**
     * Remove the key holder filter.
     * 
     * @see #keyFilterFalse
     * @see #keyFilterTrue
     * @see #applyKeyHolderFilter(Boolean)
     * @see #removeFilter(Filter)
     */
	public void removeKeyHolderFilter() {
        invokeLater(new Runnable() {
            public void run() {
                removeFilter(keyFilterFalse);
                removeFilter(keyFilterTrue);
            }
        });
    }

    /**
     * Remove the search filter from the list.
     *
     * @see #removeFilter(Filter)
     * @see #applySearchFilter(List)
     */
    public void removeSearchFilter() {
        invokeLater(new Runnable() {
            public void run() { removeFilter(searchFilter); }
        });
    }

	/**
     * Remove the state filter from the list.
     * 
     * @see #stateFilterActive
     * @see #stateFilterClosed
     * @see #removeFilter(Filter)
     * @see #applyStateFilter(ArtifactState)
     */
    public void removeStateFilter() {
        invokeLater(new Runnable() {
            public void run() {
                removeFilter(stateFilterActive);
                removeFilter(stateFilterClosed); 
            }
        });
    }

    /**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}

	/**
     * Apply an artifact filter to the document list.
     * 
     * @param filter
     *            The artifact filter.
     */
	private void applyFilter(final Filter<Artifact> filter) {
		filterChain.addFilter(filter);
		jListModel.clear();
		reloadDocuments(getController().getSelectedDocumentId());
	}

	/**
	 * Obtain the display document.
	 * 
	 * @param documentId
	 *            The document id.
	 * @return The display document.
	 */
	private DisplayDocument getDisplayDocument(final Long documentId) {
		return (DisplayDocument) ((CompositeFlatSingleContentProvider) contentProvider).getElement(0, new Pair(documentId, filterChain));
	}

	/**
	 * Obtain the display documents from the provider.
	 * 
	 * @return The display documents.
	 */
	private DisplayDocument[] getDisplayDocuments() {
		return (DisplayDocument[]) ((CompositeFlatSingleContentProvider) contentProvider).getElements(0, filterChain);
	}

	/**
	 * TODO Obtain the selected system message id.
	 * 
	 * @return The selected system message id; or null if no system message is
	 *         selected.
	 */
	private Long getSelectedSystemMessageId() { return null; }

	private SystemMessage getSystemMessage(final Long systemMessageId) {
		return (SystemMessage) ((CompositeFlatSingleContentProvider) contentProvider).getElement(1, systemMessageId);
	}

	/**
	 * Obtain the list of system messages from the content provider.
	 * 
	 * @return The list of system messages.
	 */
	private SystemMessage[] getSystemMessages() {
		return (SystemMessage[]) ((CompositeFlatSingleContentProvider) contentProvider).getElements(1, null);
	}

	/**
	 * Initialize the swing components.
	 *
	 */
	private void initComponents() {
		jListModel = new DefaultListModel();

		// the list that resides on the browser's main avatar
		// 	* is a single selection list
		//	* spans the width of the entire avatar
		// 	* uses a custom cell renderer
		jList = new JList(jListModel);
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setCellRenderer(new CellRenderer());
		// HEIGHT MainListCell 21
		jList.setFixedCellHeight(21);
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
					}
				}
			}
		});

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
	 * Load the main list model with system messages.
	 * 
	 * @param listModel
	 *            The main list model.
	 * @param systemMessages
	 *            The system message list.
	 * @param systemMessageId
	 *            The selected system message prior to load.
	 */
	private void loadMainList(final DefaultListModel listModel,
			final SystemMessage[] systemMessages, final Long systemMessageId) {
		for(final SystemMessage systemMessage : systemMessages) {
			listModel.addElement(ListItem.create(systemMessage));
		}
		// TODO Maintain the system message selection
	}

	/**
	 * Reload the list of documents.
	 *
	 */
	private void reloadDocuments(final Long documentId) {
		// TODO Maintain the document selection
		final DisplayDocument[] displayDocuments = getDisplayDocuments();
		int index = 0;
		for(final DisplayDocument displayDocument : displayDocuments) {
			jListModel.addElement(DocumentListItem.create(displayDocument));
			if(displayDocument.getDocumentId().equals(documentId)) {
				jList.setSelectedIndex(index);
			}
			index++;
		}
		// if there is no selection; and the list is not empty; select
		// the first document
		if(jList.isSelectionEmpty()) {
			if(!jListModel.isEmpty()) { jList.setSelectedIndex(0); }
		}

        // if the list is empty; disable history
        if(jListModel.isEmpty()) { getController().disableHistory(); }
        else { getController().enableHistory(); }
	}

	/**
	 * Reload the list of system messages.
	 *
	 */
	private void reloadSystemMessages(final Long systemMessageId) {
		loadMainList(jListModel, getSystemMessages(), systemMessageId);
	}

	/**
     * Remove a filter and reload the documents.
     * 
     * @param filter
     *            The artifact filter.
     */
	private void removeFilter(final Filter<Artifact> filter) {
		filterChain.removeFilter(filter);
		jListModel.clear();
		reloadDocuments(getController().getSelectedDocumentId());
	}

	/**
	 * Open the document.
	 *
	 */
	private void runOpenDocumentAction(final Long documentId) {
		getController().runOpenDocument(documentId);
	}
}