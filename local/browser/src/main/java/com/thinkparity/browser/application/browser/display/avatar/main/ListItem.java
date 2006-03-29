/*
 * Feb 2, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.component.MenuItemFactory;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.util.l10n.ListItemLocalization;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.codebase.JVMUniqueId;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessage;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ListItem {

	/**
	 * Create a list item for a document.
	 * 
	 * @param displayDocument
	 *            The display document.
	 * @return The list item.
	 */
	public static ListItem create(final DisplayDocument displayDocument) {
		return new DocumentListItem(displayDocument);
	}

	/**
	 * Create a list item for a system message.
	 * 
	 * @param systemMessage
	 *            The system message.
	 * @return The list item.
	 */
	public static ListItem create(final SystemMessage systemMessage) {
		return new SystemMessageListItem(systemMessage);
	}

	/**
	 * List item localization.
	 * 
	 */
	protected final ListItemLocalization localization;

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * The list item background image.
	 * 
	 */
	private BufferedImage backgroundImage;

	/**
	 * The list item backgound image when the list item is selected.
	 * 
	 */
	private BufferedImage backgroundImageSelected;

	/**
	 * List item id.
	 * 
	 */
	private final JVMUniqueId id;

	/**
	 * The list item information menuIcon.
	 * 
	 */
	private ImageIcon infoIcon;

	/**
	 * The parity model factory.
	 * 
	 */
	private final ModelFactory modelFactory;

	/**
	 * The list item name.
	 * 
	 */
	private String name;

	/**
	 * The font to use with the name label.
	 * 
	 */
	private Font nameFont;

	/**
	 * The list item name color.
	 * 
	 */
	private Color nameForeground;

	/**
	 * The list item name info.
	 * 
	 */
	private String nameInfo;

	/**
	 * Property map.
	 * 
	 */
	private final Map<Object, Object> propertyMap;

	/**
	 * Create a ListItem.
	 * 
	 */
	protected ListItem(final String l18nContext) {
		super();
		this.id = JVMUniqueId.nextId();
		this.localization = new ListItemLocalization(l18nContext);
		this.logger = LoggerFactory.getLogger(getClass());
		this.modelFactory = ModelFactory.getInstance();
		this.propertyMap = new Hashtable<Object,Object>(7, 0.75F);
	}

	/**
	 * This method is called when an item is selected.
	 *
	 */
	public void fireSelection() {}

	/**
	 * Obtain the list item information menuIcon.
	 * 
	 * @return The list item information menuIcon.
	 */
	public BufferedImage getBackgroundImage(final Boolean isSelected) {
		if(isSelected) { return backgroundImageSelected; }
		else { return backgroundImage; }
	}

	/**
	 * Obtain the list item information menuIcon.
	 * 
	 * @return The list item information menuIcon.
	 */
	public ImageIcon getInfoIcon() { return infoIcon; }

	/**
	 * Obtain the list item name.
	 * 
	 * @return The list item name.
	 */
	public String getName() { return name; }

	/**
	 * Obtain the font to use for the name label.
	 * 
	 * @return The font.
	 */
	public Font getNameFont() { return nameFont; }

	/**
	 * Obtain the list item name info.
	 * 
	 * @return The list item name info.
	 */
	public String getNameInfo() { return nameInfo; }

	/**
	 * Obtain a list item property.
	 * 
	 * @return The list item property.
	 */
	public Object getProperty(final String key) {
		Assert.assertNotNull("", key);
		return propertyMap.get(key);
	}

	/**
	 * Populate the JPopupMenu for the list item.
	 * 
	 * @param jPopupMenu
	 *            The JPopupMenu to populate.
	 */
	public void populateMenu(final MouseEvent e, final JPopupMenu jPopupMenu) {}

	/**
	 * Set the list item background image.
	 * 
	 * @param backgroundImage
	 *            The list item background image.
	 */
	public void setBackgroundImage(final BufferedImage backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	/**
	 * Set the list item background image when selected.
	 * 
	 * @param backgroundImageSelected
	 *            The selected background image.
	 */
	public void setBackgroundImageSelected(
			final BufferedImage backgroundImageSelected) {
		this.backgroundImageSelected = backgroundImageSelected;
	}

	/**
	 * The list item info menuIcon.
	 * 
	 * @param infoIcon
	 *            The list item info menuIcon.
	 */
	public void setInfoIcon(final ImageIcon infoIcon) { this.infoIcon = infoIcon; }

	/**
	 * Set the list item name.
	 * 
	 * @param name
	 *            The list item name.
	 */
	public void setName(final String name) { this.name = name; }

	public void setNameInfo(final String nameInfo) { this.nameInfo = nameInfo; }

	/**
	 * Set a list item property.
	 * 
	 * @param key
	 *            The property key.
	 * @param data
	 *            The property value.
	 */
	public void setProperty(final Object key, final Object value) {
		Assert.assertNotNull("", key);
		propertyMap.put(key, value);
	}

	protected JMenuItem createJMenuItem(final String localKey,
			final ActionListener actionListener) {
		return createJMenuItem(getString(localKey), getMnemonic(localKey),
				actionListener);
	}

	/**
	 * Create a JMenuItem.
	 * 
	 * @param text
	 *            The menu item text.
	 * @param mnemonic
	 *            The menu item mnemonic.
	 * @param actionListener
	 *            The menu item action listener.
	 * @return The JMenuItem
	 */
	protected JMenuItem createJMenuItem(final String text,
			final Integer mnemonic, final ActionListener actionListener) {
		final JMenuItem jMenuItem = MenuItemFactory.create(text, mnemonic);
		jMenuItem.addActionListener(actionListener);
		return jMenuItem;
	}

	protected JMenuItem createJMenuItem(final String localKey,
			final Object[] arguments, final ActionListener actionListener) {
		return createJMenuItem(getString(localKey, arguments), getMnemonic(localKey),
				actionListener);
	}

	/**
	 * Obtain the main controller.
	 * 
	 * @return The main controller.
	 */
	protected Browser getController() { return Browser.getInstance(); }

	/**
	 * Obtain a handle to the document model.
	 * 
	 * @return The document model.
	 */
	protected DocumentModel getDocumentModel() {
		return modelFactory.getDocumentModel(getClass());
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
	protected Integer getMnemonic(final String localKey) {
		final String mnemonicString = getString(localKey + "Mnemonic");
		logger.debug("ListItem.MnemonicString:" + mnemonicString + "]");
		return new Integer(mnemonicString.charAt(0));
	}

	/**
	 * Obtain the name label foreground color.
	 * 
	 * @return The name label foreground color.
	 */
	protected Color getNameForeground() { return nameForeground; }

	/**
	 * @see ListItemLocalization#getString(String)
	 * 
	 */
	protected String getString(final String localKey) {
		return localization.getString(localKey);
	}

	/**
	 * @see ListItemLocalization#getString(String, Object[])
	 * 
	 */
	protected String getString(final String localKey, final Object[] arguments) {
		return localization.getString(localKey, arguments);
	}

	/**
	 * Set the font to use with the name label.
	 * 
	 * @param nameFont
	 *            The font.
	 */
	protected void setNameFont(final Font nameFont) {
		this.nameFont = nameFont;
	}

	/**
	 * Set the name label foreground color.
	 * 
	 * @param nameForeground
	 *            The name label foreground color.
	 */
	protected void setNameForeground(final Color nameForeground) {
		this.nameForeground = nameForeground;
	}
}
