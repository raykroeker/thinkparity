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

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.filter.FilterChain;
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
         * Apply an artifact filter to the document list.
         *
         * @param filter
         *            The artifact filter.
         */
	public void applyFilter(final Filter<Artifact> filter) {
		if(!filterChain.containsFilter(filter)) {
			filterChain.addFilter(filter);
			reloadFilters();
		}
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

	private SystemMessage getSystemMessage(final Long systemMessageId) {
		return (SystemMessage) ((CompositeFlatSingleContentProvider) contentProvider).getElement(1, systemMessageId);
	}

	/**
     * Remove a filter and reload the documents.
     * 
     * @param filter
     *            The artifact filter.
     */
	public void removeFilter(final Filter<Artifact> filter) {
		if(filterChain.containsFilter(filter)) {
			filterChain.removeFilter(filter);
			reloadFilters();
		}
	}

	/**
	 * Use the model filter and the filter chain scope the list.
	 *
	 */
	private void reloadFilters() {
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}

	/**
	 * Obtain the display document.
	 * 
	 * @param documentId
	 *            The document id.
	 * @return The display document.
	 */
	private DisplayDocument getDisplayDocument(final Long documentId) {
		return (DisplayDocument) ((CompositeFlatSingleContentProvider) contentProvider).getElement(0, documentId);
	}

	/**
	 * Obtain the display documents from the provider.
	 * 
	 * @return The display documents.
	 */
	private DisplayDocument[] getDisplayDocuments() {
		return (DisplayDocument[]) ((CompositeFlatSingleContentProvider) contentProvider).getElements(0, null);
	}

	/**
	 * TODO Obtain the selected system message id.
	 * 
	 * @return The selected system message id; or null if no system message is
	 *         selected.
	 */
	private Long getSelectedSystemMessageId() { return null; }

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
	}

	/**
	 * Reload the list of system messages.
	 *
	 */
	private void reloadSystemMessages(final Long systemMessageId) {
		loadMainList(jListModel, getSystemMessages(), systemMessageId);
	}

	/**
	 * Open the document.
	 *
	 */
	private void runOpenDocumentAction(final Long documentId) {
		getController().runOpenDocument(documentId);
	}
}