/*
 * Feb 2, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.platform.util.ImageIOUtil;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.message.system.ContactInvitationMessage;
import com.thinkparity.model.parity.model.message.system.ContactInvitationResponseMessage;
import com.thinkparity.model.parity.model.message.system.KeyRequestMessage;
import com.thinkparity.model.parity.model.message.system.KeyResponseMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessageType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemMessageListItem extends ListItem {

	/**
	 * The background image.
	 * 
	 */
	private static final BufferedImage BG;

	/**
	 * The background image when selected.
	 * 
	 */
	private static final BufferedImage BG_SEL;

	static {
		BG = ImageIOUtil.read("SystemMessageCell.png");
		BG_SEL = ImageIOUtil.read("SystemMessageCellSelected.png");
	}

	/**
	 * Get the accept menu item.
	 * 
	 */
	private JMenuItem acceptMenuItem;

	/**
	 * The accept menu item action listener.
	 * 
	 */
	private ActionListener acceptMenuItemActionListener;

	/**
	 * Get the decline menu item.
	 * 
	 */
	private JMenuItem declineMenuItem;

	/**
	 * The decline menu item action listener.
	 * 
	 */
	private ActionListener declineMenuItemActionListener;

	private JMenuItem deleteMenuItem;

	private ActionListener deleteMenuItemActionListener;

	/**
	 * Create a SystemMessageListItem.
	 * 
	 * @param systemMessage
	 *            The system message.
	 */
	SystemMessageListItem(final SystemMessage systemMessage) {
		super("SystemMessageListItem");
		setBackgroundImage(BG);
		setBackgroundImageSelected(BG_SEL);
		setName(getName(systemMessage));
		setMessage(systemMessage);
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.avatar.main.ListItem#fireSelection()
	 * 
	 */
	public void fireSelection() {}

	/**
	 * @see com.thinkparity.browser.application.browser.display.avatar.main.ListItem#populateMenu(java.awt.event.MouseEvent,
	 *      javax.swing.JPopupMenu)
	 * 
	 */
	public void populateMenu(final MouseEvent e, final JPopupMenu jPopupMenu) {
		switch(getMessageType()) {
		case CONTACT_INVITATION:
			jPopupMenu.add(getAcceptMenuItem());
			jPopupMenu.add(getDeclineMenuItem());
			break;
		case CONTACT_INVITATION_RESPONSE:
			jPopupMenu.add(getDeleteMenuItem());
			break;
		case KEY_REQUEST:
			jPopupMenu.add(getAcceptMenuItem());
			jPopupMenu.add(getDeclineMenuItem());
			break;
		case KEY_RESPONSE:
			jPopupMenu.add(getDeleteMenuItem());
			break;
		default: Assert.assertUnreachable("Unknown message type:  " + getMessageType());
		}
		// DEBUG System Message Menu Options
		if(e.isShiftDown()) {
			jPopupMenu.addSeparator();
			jPopupMenu.add(new JMenuItem("Id:" + getMessage().getId()));
		}
	}

	/**
	 * Obtain the accept menu item.
	 * 
	 * @return The accept menu item.
	 */
	private JMenuItem getAcceptMenuItem() {
		if(null == acceptMenuItem) {
			acceptMenuItem = createJMenuItem(getString("Accept"),
					getMnemonic("AcceptMnemonic"), getAcceptMenuItemActionListener());
		}
		return acceptMenuItem;
	}

	/**
	 * Get the accept menu item action listener.
	 * 
	 * @return The accept menu item action listener.
	 */
	private ActionListener getAcceptMenuItemActionListener() {
		if(null == acceptMenuItemActionListener) {
			acceptMenuItemActionListener = new ActionListener() {
				public void actionPerformed(final ActionEvent e) { runAccept(); }
			};
		}
		return acceptMenuItemActionListener;
	}

	/**
	 * Obtain the decline menu item.
	 * 
	 * @return The decline menu item.
	 */
	private JMenuItem getDeclineMenuItem() {
		if(null == declineMenuItem) {
			declineMenuItem = createJMenuItem(getString("Decline"),
					getMnemonic("DeclineMnemonic"), getDeclineMenuItemActionListener());
		}
		return declineMenuItem;
	}

	/**
	 * Get the decline menu item action listener.
	 * 
	 * @return The decline menu item action listener.
	 */
	private ActionListener getDeclineMenuItemActionListener() {
		if(null == declineMenuItemActionListener) {
			declineMenuItemActionListener = new ActionListener() {
				public void actionPerformed(final ActionEvent e) { runDecline(); }
			};
		}
		return declineMenuItemActionListener;
	}

	private JMenuItem getDeleteMenuItem() {
		if(null == deleteMenuItem) {
			deleteMenuItem = createJMenuItem(getString("Delete"),
					getMnemonic("DeleteMnemonic"), getDeleteMenuItemActionListener());
		}
		return deleteMenuItem;
	}

	private ActionListener getDeleteMenuItemActionListener() {
		if(null == deleteMenuItemActionListener) {
			deleteMenuItemActionListener = new ActionListener() {
				public void actionPerformed(final ActionEvent e) { runDelete(); }
			};
		}
		return deleteMenuItemActionListener;
	}

	/**
	 * Get the message id.
	 * 
	 * @return The message id.
	 */
	private SystemMessage getMessage() {
		return (SystemMessage) getProperty("message");
	}

	private SystemMessageType getMessageType() {
		return (SystemMessageType) getProperty("messageType");
	}

	/**
	 * Obtain the list item name for the presence request message.
	 * 
	 * @param message
	 *            The presence request message.
	 * @return The list item name.
	 */
	private String getName(final ContactInvitationMessage message) {
		return getString(
				message.getType(),
				new Object[] {message.getInvitedBy().toString()});
	}

	/**
	 * Obtain the list item name for the contact response message.
	 * 
	 * @param message
	 *            The contact invitation response message.
	 * @return The list item name.
	 */
	private String getName(final ContactInvitationResponseMessage message) {
		if(message.didAcceptInvitation()) {
			return getString(
					message.getType() + ".ACCEPTED",
					new Object[] {message.getResponseFrom().toString()});
		}
		else {
			return getString(
					message.getType() + ".DECLINED",
					new Object[] {message.getResponseFrom().toString()});
		}
	}

	private String getName(final KeyRequestMessage message) {
		Document document;
		try { document = getDocumentModel().get(message.getArtifactId()); }
		catch(final ParityException px) { throw new RuntimeException(px); }
		return getString(
				message.getType(),
				new Object[] {message.getRequestedBy().getUsername(), document.getName()});
	}

	private String getName(final KeyResponseMessage message) {
		Document document;
		try { document = getDocumentModel().get(message.getArtifactId()); }
		catch(final ParityException px) { throw new RuntimeException(px); }
		if(message.didAcceptRequest()) {
			return getString(
					message.getType() + ".ACCEPTED",
					new Object[] {message.getResponseFrom().getUsername(), document.getName()});
		}
		else {
			return getString(
					message.getType() + ".DECLINED",
					new Object[] {message.getResponseFrom().getUsername(), document.getName()});
		}
	}

	/**
	 * Obtain the list item name for the message.
	 * 
	 * @param message
	 *            The message.
	 * @return The list item name.
	 */
	private String getName(final SystemMessage message) {
		final SystemMessageType messageType = message.getType();
		switch(messageType) {
		case CONTACT_INVITATION: return getName((ContactInvitationMessage) message);
		case CONTACT_INVITATION_RESPONSE: return getName((ContactInvitationResponseMessage) message);
		case KEY_RESPONSE: return getName((KeyResponseMessage) message);
		case KEY_REQUEST: return getName((KeyRequestMessage) message);
		default:
			throw Assert.createUnreachable("Unknown message type:  " + messageType);
		}
	}

	/**
	 * Obtain the localized string for a message type.
	 * 
	 * @param type
	 *            The message type.
	 * @return The localized string.
	 */
	private String getString(final SystemMessageType messageType,
			final Object[] arguments) {
		return getString(messageType.toString(), arguments);
	}

	private void runAccept() {
		final SystemMessage message = getMessage();
		switch(message.getType()) {
		case CONTACT_INVITATION:
			getController().runAcceptContactInvitation(message.getId());
			break;
		case KEY_REQUEST:
			getController().runAcceptKeyRequest(message.getId());
			break;
		default:
			Assert.assertUnreachable("Unknown system message:  " + message.getType());
		}
	}

	private void runDecline() {
		final SystemMessage message = getMessage();
		switch(message.getType()) {
		case CONTACT_INVITATION:
			getController().runDeclineContactInvitation(message.getId());
			break;
		case KEY_REQUEST:
			getController().runDeclineKeyRequest(message.getId());
			break;
		default:
			Assert.assertUnreachable("Unknown system message:  " + message.getType());
		}
	}

	private void runDelete() {
		final SystemMessage message = getMessage();
		switch(message.getType()) {
		case CONTACT_INVITATION_RESPONSE:
		case KEY_RESPONSE:
			getController().runDeleteSystemMessage(message.getId());
			break;
		default: Assert.assertUnreachable("Unknown system message:  " + message.getType());
		}
	}

	/**
	 * Set the message id.
	 * 
	 * @param messageId
	 *            The message id.
	 */
	private void setMessage(final SystemMessage message) {
		setProperty("message", message);
		setProperty("messageType", message.getType());
	}
}
