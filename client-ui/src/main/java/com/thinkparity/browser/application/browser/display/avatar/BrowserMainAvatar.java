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

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.display.avatar.main.CellRenderer;
import com.thinkparity.browser.application.browser.display.avatar.main.DocumentListItem;
import com.thinkparity.browser.application.browser.display.avatar.main.ListItem;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatContentProvider;
import com.thinkparity.browser.model.util.ArtifactUtil;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.message.system.SystemMessage;


/**
 * The main list avatar displays a list of crucial system messages; as well as
 * the document list.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class BrowserMainAvatar extends Avatar {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

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
		setLayout(new GridBagLayout());
		initComponents();
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
		jListModel.clear();
		reloadSystemMessages();
		reloadDocuments();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}

	/**
	 * Obtain the list of system messages from the content provider.
	 * 
	 * @return The list of system messages.
	 */
	private SystemMessage[] getSystemMessages() {
		return (SystemMessage[]) ((CompositeFlatContentProvider) contentProvider).getElements(1, null);
	}

	/**
	 * Initialize the swing components.
	 *
	 */
	private void initComponents() {
		final GridBagConstraints c = new GridBagConstraints();

		jListModel = new DefaultListModel();

		// the list that resides on the browser's main avatar
		// 	* is a single selection list
		//	* spans the width of the entire avatar
		// 	* uses a custom cell renderer
		final JList jList = new JList(jListModel);
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setCellRenderer(new CellRenderer());
		// HEIGHT MainListCell 21
		jList.setFixedCellHeight(21);
		jList.addMouseListener(new MouseAdapter() {
			public void mouseReleased(final MouseEvent e) {
				if(e.isPopupTrigger()) {
					final Point p = e.getPoint();
					final Integer listIndex = jList.locationToIndex(p);
					jList.setSelectedIndex(listIndex);

					final ListItem listItem =
						(ListItem) jList.getSelectedValue();
					final JPopupMenu jPopupMenu = new JPopupMenu();
					listItem.populateMenu(jPopupMenu);
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

		c.fill = GridBagConstraints.BOTH;
		c.insets.left = c.insets.right = 2;
		c.insets.top = c.insets.bottom = 1;
		c.weightx = 1;
		c.weighty = 1;
		add(jListScrollPane, c.clone());

		final JLabel addDocumentJLabel = LabelFactory.createLink(
				getString("AddDocument"), BrowserConstants.DefaultFont);
		final Color originalForeground = addDocumentJLabel.getForeground();
		addDocumentJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				addDocumentJLabel.setForeground(originalForeground);
				runCreateDocumentAction();
			}
		});
		c.anchor = GridBagConstraints.SOUTH;
		c.fill = GridBagConstraints.NONE;
		c.insets.top = c.insets.bottom = 2;
		c.gridy = 1;
		add(addDocumentJLabel, c.clone());
	}

	/**
	 * Load the main list model with system messages.
	 * 
	 * @param listModel
	 *            The main list model.
	 * @param systemMessages
	 *            The system message list.
	 */
	private void loadMainList(final DefaultListModel listModel,
			final SystemMessage[] systemMessages) {
		for(final SystemMessage systemMessage : systemMessages) {
			listModel.addElement(ListItem.create(systemMessage));
		}
	}

	/**
	 * Reload the list of documents.
	 *
	 */
	private void reloadDocuments() {
		final Object[] elements = ((CompositeFlatContentProvider) contentProvider).getElements(0, null);
		Document d;
		for(final Object e : elements) {
			d = (Document) e;
			jListModel.addElement(
					DocumentListItem.create(
							d, ArtifactUtil.isKeyHolder(d.getId())));
		}
	}

	/**
	 * Reload the list of system messages.
	 *
	 */
	private void reloadSystemMessages() {
		loadMainList(jListModel, getSystemMessages());
	}

	/**
	 * Create a new document.
	 *
	 */
	private void runCreateDocumentAction() {
		getController().runCreateDocument();
	}
}