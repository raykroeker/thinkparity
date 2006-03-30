/*
 * Mar 7, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.title;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.MenuItemFactory;
import com.thinkparity.browser.application.browser.component.TextFactory;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.javax.swing.border.TopBottomBorder;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.ImageIOUtil;

import com.thinkparity.model.parity.model.filter.artifact.Active;
import com.thinkparity.model.parity.model.filter.artifact.Closed;
import com.thinkparity.model.parity.model.filter.artifact.IsKeyHolder;
import com.thinkparity.model.parity.model.filter.artifact.IsNotKeyHolder;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SearchPanel extends AbstractJPanel {

	/**
	 * The left search icon.
	 * 
	 */
	private static final Icon SEARCH_LEFT_ICON;

	/**
	 * The right search icon.
	 * 
	 */
	private static final Icon SEARCH_RIGHT_ICON;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static {
		SEARCH_LEFT_ICON = ImageIOUtil.readIcon("SearchLeft.png");
		SEARCH_RIGHT_ICON = ImageIOUtil.readIcon("SearchRight.png");
	}

	/**
	 * The container avatar.
	 * 
	 */
	private final Avatar container;

	private JPopupMenu searchJPopupMenu;

	/**
	 * The search box.
	 * 
	 */
	private JTextField searchJTextField;

	/**
	 * The label for the image on the left side of the search box.
	 * 
	 */
	private JLabel searchLeftJLabel;

	/**
	 * The label for the icon on the right of the search box.
	 * 
	 */
	private JLabel searchRightJLabel;

	/**
	 * Create a SearchPanel.
	 * 
	 */
	public SearchPanel(final Avatar container, final MouseInputAdapter mouseInputAdapter) {
		super("SearchPanel");
		this.container = container;
		addMouseListener(mouseInputAdapter);
		addMouseMotionListener(mouseInputAdapter);
		setLayout(new GridBagLayout());
		setOpaque(false);
		initComponents();
	}

	private JMenuItem createJMenuItem(final String textLocalKey,
			final ActionListener actionListener) {
		return createJMenuItem(getString(textLocalKey),
				getMnemonic(textLocalKey), actionListener);
	}

	private JMenuItem createJMenuItem(final String text, final Integer mnemonic,
			final ActionListener actionListener) {
		final JMenuItem jMenuItem = MenuItemFactory.createCheckBox(text, mnemonic);
		jMenuItem.addActionListener(actionListener);
		return jMenuItem;

	}

	/**
	 * Obtain the browser application.
	 * 
	 * @return The browser application.
	 */
	private Browser getBrowser() { return container.getController(); }

	private Integer getMnemonic(final String textLocalKey) {
		final String mnemonicString = getString(textLocalKey + "Mnemonic");
		return new Integer(mnemonicString.charAt(0));
	}

	private JPopupMenu getSearchJPopupMenu() {
		if(null == searchJPopupMenu) {
			searchJPopupMenu = MenuFactory.createPopup();
//			searchJPopupMenu.add(createJMenuItem("ShowClosed", new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					getBrowser().applyDocumentFilter(new Closed());
//				}
//			}));
//			searchJPopupMenu.add(createJMenuItem("ShowOpen", new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					getBrowser().applyDocumentFilter(new Active());
//				}
//			}));
//			searchJPopupMenu.add(createJMenuItem("ShowKey", new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					getBrowser().applyDocumentFilter(new IsKeyHolder());
//				}
//			}));
//			searchJPopupMenu.add(createJMenuItem("ShowNotKey", new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					getBrowser().applyDocumentFilter(new IsNotKeyHolder());
//				}
//			}));
		}
		return searchJPopupMenu;
	}

	/**
	 * Initialize the swing components.
	 *
	 */
	private void initComponents() {
		searchJTextField = TextFactory.create();
		// COLOR SearchText 237,241,244,255
		searchJTextField.setBackground(new Color(237, 241, 244, 255));
		// BORDER SearchText TopBottom 204,215,226,255
		searchJTextField.setBorder(new TopBottomBorder(new Color(204, 215, 226, 255)));
		searchJTextField.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				searchJTextFieldKeyTyped(e);
			}
		});
		searchLeftJLabel = LabelFactory.create(SEARCH_LEFT_ICON);
		searchLeftJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				searchLeftJLabelMouseClicked(e);
			}
		});

		searchRightJLabel = LabelFactory.create(SEARCH_RIGHT_ICON);

		final GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.EAST;
		c.insets.left = 167;
		add(searchLeftJLabel, c.clone());

		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.insets.left = 0;
		c.weightx = 1;
		add(searchJTextField, c.clone());

		c.fill = GridBagConstraints.NONE;
		c.insets.left = 0;
		c.insets.right = 7;
		c.weightx = 0;
		add(searchRightJLabel, c.clone());
	}

	private void searchJTextFieldKeyTyped(final ActionEvent e) {
		getBrowser().runSearchArtifact(searchJTextField.getText());
	}

	private void searchLeftJLabelMouseClicked(final MouseEvent e) {
		getSearchJPopupMenu().show(searchLeftJLabel, 0, searchLeftJLabel.getSize().height);
	}
}
