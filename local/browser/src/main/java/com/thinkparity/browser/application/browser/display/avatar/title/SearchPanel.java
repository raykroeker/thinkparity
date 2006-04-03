/*
 * Mar 7, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.title;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputAdapter;

import com.thinkparity.browser.application.browser.Browser;
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
	 * Obtain the browser application.
	 * 
	 * @return The browser application.
	 */
	private Browser getBrowser() { return container.getController(); }

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
				searchJTextFieldActionPerformed(e);
			}
		});
		searchJTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(final DocumentEvent e) {
				searchJTextFieldChangedUpdate(e);
			}
			public void insertUpdate(final DocumentEvent e) {
				searchJTextFieldInsertUpdate(e);
			}
			public void removeUpdate(final DocumentEvent e) {
				searchJTextFieldRemoveUpdate(e);
			}
		});
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

	private void searchJTextFieldActionPerformed(final ActionEvent e) {
		getBrowser().runSearchArtifact(searchJTextField.getText());
	}

	private void searchJTextFieldRemoveUpdate(final DocumentEvent e) {
		if(e.getDocument().getLength() == 0) {
			getBrowser().removeSearchFilter();
		}
	}

	private void searchJTextFieldInsertUpdate(final DocumentEvent e) {}

	private void searchJTextFieldChangedUpdate(final DocumentEvent e) {}
}
