/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.thinkparity.browser.application.browser.UIConstants;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.display.avatar.main.CellRenderer;
import com.thinkparity.browser.application.browser.display.avatar.main.DocumentListItem;
import com.thinkparity.browser.application.browser.display.avatar.main.ListItem;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatContentProvider;
import com.thinkparity.browser.model.util.ArtifactUtil;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.SwingUtil;

import com.thinkparity.codebase.assertion.Assert;

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
		initBrowserMainComponents();
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
		jList.setCellRenderer(new CellRenderer());
		final JListSelectionDriver jListSelectionDriver = new JListSelectionDriver(jList);
		jList.addMouseListener(jListSelectionDriver);
		jList.addMouseMotionListener(jListSelectionDriver);
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
		c.weightx = 1;
		c.weighty = 1;
		add(jListScrollPane, c.clone());

		final JLabel addDocumentJLabel = LabelFactory.createLink(
				getString("AddDocument"), UIConstants.DefaultFont);
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

	private void runCreateDocumentAction() { getController().runCreateDocument(); }

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
		 * The index of the list item that is currently pinned.
		 * 
		 */
		private Integer pinnedIndex;

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
		 * @see javax.swing.event.MouseInputAdapter#mouseClicked(java.awt.event.MouseEvent)
		 * 
		 */
		public void mouseClicked(final MouseEvent e) {
			// first; we grab the index of the list item of the event
			// second; we grab the bounds of the list item's icon
			// third; we check to see that the icon was clicked and if it was
			//		we display the popup menu
			final Point p = e.getPoint();
			final Integer listIndex = getListIndex(p);
			final Rectangle region = getMenuIconBounds(listIndex);
			if(SwingUtil.regionContains(region, p)) {
				pinSelection(listIndex);
				final ListItem item = getSelectedItem();
				final JPopupMenu jPopupMenu = new JPopupMenu();
				jPopupMenu.addPopupMenuListener(new PopupMenuListener() {
					public void popupMenuCanceled(final PopupMenuEvent e) {}
					public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
						unpinSelection();
					}
					public void popupMenuWillBecomeVisible(
							final PopupMenuEvent e) {}
				});
				item.populateMenu(jPopupMenu);
				jPopupMenu.show(jList,
						region.x + region.width + Math.round(region.width / 4),
						region.y + Math.round(region.height / 2));
			}
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
		 * Clear the list selection.
		 *
		 */
		private void clearSelection() {
			if(null == pinnedIndex) { jList.clearSelection(); }
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

		/**
		 * Obtain the 2D region that the icon will occupy for the list item at
		 * the given index.
		 * 
		 * @param index
		 *            The list item index.
		 * @return The list item's menu icon.
		 */
		private Rectangle getMenuIconBounds(final Integer index) {
			final Rectangle cellBounds = jList.getCellBounds(index, index);
			// X_Y_WIDTH_HEIGHT 13,4,13,13
			cellBounds.x += 13;
			cellBounds.y += 4;
			cellBounds.width = cellBounds.height = 13;
			return cellBounds;
		}

		/**
		 * Obtain the selected list item from the list.
		 * 
		 * @return The ListItem.
		 */
		private ListItem getSelectedItem() {
			return (ListItem) jList.getSelectedValue();
		}

		/**
		 * Pin the list selection at the given index.
		 * 
		 * @param index
		 *            The index.
		 */
		private void pinSelection(final Integer index) {
			Assert.assertNotNull("", index);
			Assert.assertNotTrue("", -1 == index);
			select(index);
			pinnedIndex = index;
		}

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

		/**
		 * Remove the selection pin.
		 *
		 */
		private void unpinSelection() {
			pinnedIndex = null;
		}
	}
}