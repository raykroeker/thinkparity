/*
 * Mar 7, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.title;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;

import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.TextFactory;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.javax.swing.border.TopBottomBorder;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.ImageIOUtil;

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
		searchLeftJLabel = LabelFactory.create(SEARCH_LEFT_ICON);
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
}
