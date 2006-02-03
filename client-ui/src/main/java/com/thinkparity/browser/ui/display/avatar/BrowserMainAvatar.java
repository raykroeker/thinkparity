/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.UUID;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;

import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.model.tmp.system.message.Message;
import com.thinkparity.browser.model.util.ParityObjectUtil;
import com.thinkparity.browser.ui.UIConstants;
import com.thinkparity.browser.ui.component.MenuItemFactory;
import com.thinkparity.browser.ui.display.provider.CompositeFlatContentProvider;
import com.thinkparity.browser.util.State;

import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.api.ParityObjectType;
import com.thinkparity.model.parity.model.document.Document;


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

	BrowserMainAvatar() {
		super("BrowserMainAvatar", ScrollPolicy.NONE, Color.WHITE);
		setLayout(new GridBagLayout());
		initBrowserMainComponents();
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.BROWSER_MAIN; }

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() { return null; }

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#reload()
	 * 
	 */
	public void reload() {
		jListModel.clear();
		if(null != input && 2 == ((Object[]) input).length) {
			reloadSystemMessages();
			reloadDocuments();
		}
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#setState(com.thinkparity.browser.util.State)
	 * 
	 */
	public void setState(final State state) {}

	/**
	 * Initialize the swing components.
	 *
	 */
	private void initBrowserMainComponents() {
		final GridBagConstraints c = new GridBagConstraints();

		jListModel = new DefaultListModel();

		// the list that resides on the browser's main avatar
		// 	* is a single selection list
		//	* spans the width of the entire avatar
		// 	* uses a custom cell renderer
		// 	* uses a selection driver to select an item on mouse over
		final JList jList = new JList(jListModel);
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setCellRenderer(new BrowserMainCellRenderer());
		final JListSelectionDriver jListSelectionDriver = new JListSelectionDriver(jList);
		jList.addMouseListener(jListSelectionDriver);
		jList.addMouseMotionListener(jListSelectionDriver);
		jList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(final ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					final Integer selectedIndex = jList.getSelectedIndex();
					if(-1 != selectedIndex) {
						final BrowserMainListItem item =
							(BrowserMainListItem) jList.getSelectedValue();
						switch(item.getType()) {
						case DOCUMENT:
							getController().selectDocument((UUID) item.getData());
							getController().displayDocumentHistoryListAvatar();
							break;
						case SYSTEM_MESSAGE:
							// TODO Set the message id in the controller.
							getController().displaySystemMessageAvatar();
							break;
						default:
							Assert.assertUnreachable("Unknown list item type.");
						}
					}
				}
			}
		});

		final JScrollPane jListScrollPane = new JScrollPane(jList);
        jListScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		add(jListScrollPane, c.clone());
	}

	/**
	 * Reload the list of documents.
	 *
	 */
	private void reloadDocuments() {
		final Object[] elements = ((CompositeFlatContentProvider) contentProvider).getElements(1, ((Object[]) input)[1]);
		for(final Object element : elements) {
			jListModel.addElement(new DocumentListItem((Document) element));
		}
	}

	/**
	 * Reload the list of system messages.
	 *
	 */
	private void reloadSystemMessages() {
		final Object[] elements = ((CompositeFlatContentProvider) contentProvider).getElements(0, ((Object[]) input)[0]);
		for(Object element : elements) {
			jListModel.addElement(BrowserMainListItem.create((Message) element));
		}
	}

	/**
	 * The selection driver for the main list. In this list we are "selecting"
	 * an item when the mouse resides within the bounds of the cell.
	 * 
	 * 
	 */
	private class JListSelectionDriver extends MouseInputAdapter {

		/**
		 * The JList we are listening to.
		 * 
		 */
		private final JList jList;

		/**
		 * Create a JListSelectionDriver.
		 * 
		 * @param jList
		 *            The JList to listen to.
		 */
		private JListSelectionDriver(final JList jList) {
			super();
			this.jList = jList;
		}

		/**
		 * The index of the list item that is currently pinned.
		 * 
		 */
		private Integer pinnedIndex;

		/**
		 * Select the list item of the given index. This method will check if
		 * the selection has been pinned; and if not, it will update the
		 * selection.
		 * 
		 * @param index
		 *            The list index.
		 */
		private void select(final Integer index) {
			if(null == pinnedIndex) { jList.setSelectedIndex(index); }
		}

		private void pinSelection(final Integer index) {
			Assert.assertNotNull("", index);
			Assert.assertNotTrue("", -1 == index);
			select(index);
			pinnedIndex = index;
		}

		private void unpinSelection() {
			pinnedIndex = null;
		}

		private void clearSelection() {
			if(null == pinnedIndex) { jList.clearSelection(); }
		}

		/**
		 * @see javax.swing.event.MouseInputAdapter#mouseClicked(java.awt.event.MouseEvent)
		 * 
		 */
		public void mouseClicked(final MouseEvent e) {
			pinSelection(getListIndex(e.getPoint()));
			final BrowserMainListItem item = getSelectedItem();
			final JPopupMenu jPopupMenu = new JPopupMenu();
			item.populateMenu(jPopupMenu);
			jPopupMenu.show(jList, 26, e.getPoint().y);
		}

		/**
		 * Obtain the selected list item from the list.
		 * 
		 * @return The BrowserMainListItem.
		 */
		private BrowserMainListItem getSelectedItem() {
			return (BrowserMainListItem) jList.getSelectedValue();
		}

		/**
		 * @see javax.swing.event.MouseInputAdapter#mouseExited(java.awt.event.MouseEvent)
		 * 
		 */
		public void mouseExited(final MouseEvent e) {
			setCursor(UIConstants.DefaultCursor);
			// we might be on a menu (but still witin the list)
			if(!jList.contains(e.getPoint())) {
				unpinSelection();
				clearSelection();
			}
		}

		/**
		 * @see javax.swing.event.MouseInputAdapter#mouseMoved(java.awt.event.MouseEvent)
		 * 
		 */
		public void mouseMoved(MouseEvent e) {
			setCursor(UIConstants.HandCursor);
			clearSelection();
			select(getListIndex(e.getPoint()));
		}

		/**
		 * Obtain the list index at a given location.
		 * 
		 * @param p
		 *            The location.
		 * @return The list index.
		 */
		private Integer getListIndex(final Point p) {
			return jList.locationToIndex(p);
		}
	}

	/**
	 * The document icon.
	 * 
	 */
	private static final ImageIcon DOCUMENT_ICON;

	static {
		DOCUMENT_ICON =
			new ImageIcon(ResourceUtil.getURL("images/documentIconBlue.png"));
	}

	/**
	 * The document list item.
	 * 
	 */
	private class DocumentListItem extends BrowserMainListItem {

		/**
		 * The document.
		 * 
		 */
		private final Document document;

		/**
		 * Create a DocumentListItem.
		 * 
		 * @param document
		 *            The document.
		 */
		private DocumentListItem(final Document document) {
			super(Type.DOCUMENT, DOCUMENT_ICON, document.getName(), document.getId());
			this.document = document;
		}

		/**
		 * @see com.thinkparity.browser.ui.display.avatar.BrowserMainListItem#populateMenu(javax.swing.JPopupMenu)
		 * 
		 */
		protected void populateMenu(final JPopupMenu jPopupMenu) {
			final JMenuItem sendJMenuItem =
				createMenuItem("Send", new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						getController().displaySessionSendFormAvatar();
					}});
			jPopupMenu.add(sendJMenuItem);
			if(isKeyHolder()) {
				final JMenuItem sendKeyJMenuItem =
					createMenuItem("SendKey", new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							getController().displaySessionSendKeyFormAvatar();
						}});
				jPopupMenu.add(sendKeyJMenuItem);
			}
			else {
				final JMenuItem requestKeyJMenuItem =
					createMenuItem("RequestKey", new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							getController().runRequestArtifactKey(document.getId());
						}});
				jPopupMenu.add(requestKeyJMenuItem);
			}
			if(canClose()) {
				final JMenuItem closeJMenuItem =
					createMenuItem("Close", new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							getController().runCloseDocument(document.getId());
						}});
				jPopupMenu.addSeparator();
				jPopupMenu.add(closeJMenuItem);
			}
			if(canDelete()) {
				final JMenuItem deleteJMenuItem =
					createMenuItem("Delete", new ActionListener() {
							public void actionPerformed(final ActionEvent e) {
								getController().runDeleteDocument(document.getId());
							}});
				jPopupMenu.addSeparator();
				jPopupMenu.add(deleteJMenuItem);
			}
		}

		/**
		 * @see ParityObjectUtil#isKeyHolder(UUID, ParityObjectType)
		 * 
		 */
		private Boolean isKeyHolder() {
			return ParityObjectUtil.isKeyHolder(
					document.getId(), ParityObjectType.DOCUMENT);
		}

		/**
		 * @see ParityObjectUtil#canClose(UUID, ParityObjectType)
		 * 
		 */
		private Boolean canClose() {
			return ParityObjectUtil.canClose(
					document.getId(), ParityObjectType.DOCUMENT);
		}

		/**
		 * @see ParityObjectUtil#canDelete(UUID, ParityObjectType)
		 * 
		 */
		private Boolean canDelete() {
			return ParityObjectUtil.canDelete(
					document.getId(), ParityObjectType.DOCUMENT);
		}
	}

	/**
	 * Obtain a menu item mnemonic from the l18n resources. This will simply
	 * look for the item with Mnemonic tacked on the end of the key, and convert
	 * the first character in that string to an acsii integer.
	 * 
	 * @param localKey
	 *            The local key.
	 * @return The menu item mnemonic.
	 * 
	 * @see AbstractJPanel#getString(String)
	 */
	protected Integer getJMenuItemMnemonic(final String localKey) {
		final String mnemonicString = getString(localKey + "Mnemonic");
		return new Integer(mnemonicString.charAt(0));
	}

	protected JMenuItem createMenuItem(final String localKey,
			final ActionListener actionListener) {
		final JMenuItem jMenuItem = MenuItemFactory.create(
				getString(localKey), getJMenuItemMnemonic(localKey));
		jMenuItem.addActionListener(actionListener);
		return jMenuItem;
	}
}